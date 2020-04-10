package ru.otus.spring.sagina.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.spring.sagina.BookApplication;
import ru.otus.spring.sagina.entity.Book;
import ru.otus.spring.sagina.enums.UserRole;
import ru.otus.spring.sagina.security.UserDetailsAdapter;
import ru.otus.spring.sagina.services.acl.utils.BookAclPermission;
import ru.otus.spring.sagina.testdata.AuthorData;
import ru.otus.spring.sagina.testdata.GenreData;
import ru.otus.spring.sagina.testdata.UserData;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ContextConfiguration(classes = BookApplication.class)
class AclBookServiceTest {
    @Resource
    private MutableAclService aclService;
    @Autowired
    private AclBookService aclBookService;

    @Test
    @Transactional
    void createAclTest() {
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(
                        new UserDetailsAdapter(UserData.ADMIN_VERONIKA),
                        UserData.ADMIN_VERONIKA.getPassword(),
                        List.of(new SimpleGrantedAuthority(UserRole.ROLE_ADMIN.name()))));
        Book created = new Book(10L,
                "name",
                "description",
                AuthorData.TOLSTOY,
                List.of(GenreData.NOVEL),
                List.of());
        aclBookService.createAcl(created);

        Acl acl = aclService.readAclById(new ObjectIdentityImpl(Book.class, created.getId()));
        Sid adminSid = new GrantedAuthoritySid(UserRole.ROLE_ADMIN.name());
        Sid userSid = new GrantedAuthoritySid(UserRole.ROLE_USER.name());
        Sid littleUserSid = new GrantedAuthoritySid(UserRole.ROLE_LITTLE_USER.name());

        assertTrue(acl.isGranted(List.of(BookAclPermission.ALL_OPERATIONS), List.of(adminSid), false));
        assertTrue(acl.isGranted(List.of(BookAclPermission.READ_CREATE), List.of(userSid), false));
        assertFalse(acl.isGranted(List.of(BasePermission.READ), List.of(littleUserSid), false));
    }
}