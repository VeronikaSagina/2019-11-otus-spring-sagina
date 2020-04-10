package ru.otus.spring.sagina.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.entity.Book;
import ru.otus.spring.sagina.enums.UserRole;
import ru.otus.spring.sagina.repository.GenreRepository;
import ru.otus.spring.sagina.security.UserDetailsAdapter;
import ru.otus.spring.sagina.services.acl.utils.BookAclPermission;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@Service
public class AclBookService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AclBookService.class);
    private static final Long KIDS_BOOK_GENRE = 5L;

    @Resource
    private MutableAclService aclService;
    private GenreRepository genreRepository;

    public AclBookService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Transactional
    public void createAcl(Book book) {
        LOGGER.debug("добавление acl для книги {} с id={}", book.getName(), book.getId());
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetailsAdapter user;
        if (principal instanceof UserDetailsAdapter) {
            user = (UserDetailsAdapter) principal;
        } else {
            throw new IllegalStateException(String.format("не поддерживаемый тип пользователя [%s]", principal));
        }
        ObjectIdentity objectIdentity = new ObjectIdentityImpl(Book.class, book.getId());
        MutableAcl acl;
        try {
            acl = (MutableAcl) aclService.readAclById(objectIdentity);
        } catch (NotFoundException e) {
            acl = aclService.createAcl(objectIdentity);
        }
        Sid adminSid = new GrantedAuthoritySid(UserRole.ROLE_ADMIN.name());
        Sid userSid = new GrantedAuthoritySid(UserRole.ROLE_USER.name());
        Sid littleUserSid = new GrantedAuthoritySid(UserRole.ROLE_LITTLE_USER.name());

        acl.insertAce(acl.getEntries().size(), BookAclPermission.READ_CREATE, userSid, true);
        acl.insertAce(acl.getEntries().size(), BookAclPermission.ALL_OPERATIONS, adminSid, true);
        acl.insertAce(
                acl.getEntries().size(), BasePermission.READ, littleUserSid,
                book.getGenres().contains(genreRepository.getOne(KIDS_BOOK_GENRE)));
        aclService.updateAcl(acl);
        LOGGER.info("пользователь {} добавил книгу {}", user.getUsername(), book.getName());
    }
}
