package ru.otus.spring.sagina.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.exceptions.InvalidJwtTokenException;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JwtTokenService {
    private static final String CLAIM_AUTHORITIES = "authorities";
    private static final String CLAIM_LOGIN = "login";
    private final Key secretKey;
    private final Long expirationTime;

    public JwtTokenService(@Value("${jwt.secret:BookApplicationTokenSecretKeyToGenJWTs}") String secret,
                           @Value("${jwt.expiration_time:86400000}") Long expirationTime) {
        secretKey = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());
        this.expirationTime = expirationTime;
    }

    public Authentication authentication(String token) {
        validateToken(token);
        String id = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return new UsernamePasswordAuthenticationToken(
                new AuthUser(UUID.fromString(id), getByClaim(token, CLAIM_LOGIN)),
                null, getUserAuthorities(token));
    }

    public String createToken(UUID id, String name, List<GrantedAuthority> authorities) {
        Claims claims = Jwts.claims().setSubject(id.toString());
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        claims.put(CLAIM_LOGIN, name);

        String auth = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        claims.put(CLAIM_AUTHORITIES, auth);

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        return Jwts.builder().setId(id.toString())
                .setIssuedAt(now)
                .setClaims(claims)
                .signWith(secretKey, signatureAlgorithm)
                .setExpiration(new Date(nowMillis + expirationTime))
                .compact();
    }

    public void validateToken(String token) {
        getByClaim(token, CLAIM_AUTHORITIES);
        getByClaim(token, CLAIM_LOGIN);
    }

    private List<GrantedAuthority> getUserAuthorities(String token) {
        String authorities = getByClaim(token, CLAIM_AUTHORITIES);
        return Arrays.stream(authorities.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private String getByClaim(String token, String claim) {
        return Optional.ofNullable(Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get(claim, String.class))
                .orElseThrow(() -> new InvalidJwtTokenException("не найдено поле " + claim));
    }
}
