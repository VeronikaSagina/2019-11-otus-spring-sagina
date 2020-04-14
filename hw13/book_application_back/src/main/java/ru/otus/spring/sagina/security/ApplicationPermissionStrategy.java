package ru.otus.spring.sagina.security;

import org.springframework.security.acls.domain.AuditLogger;
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Permission;

public class ApplicationPermissionStrategy extends DefaultPermissionGrantingStrategy {

    public ApplicationPermissionStrategy(AuditLogger auditLogger) {
        super(auditLogger);
    }

    @Override
    public boolean isGranted(AccessControlEntry ace, Permission p) {
        return (ace.getPermission().getMask() & p.getMask()) > 0;
    }
}
