package org.apache.tomcat.util.security;

import java.security.Permission;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/security/PermissionCheck.class */
public interface PermissionCheck {
    boolean check(Permission permission);
}
