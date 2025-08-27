package org.apache.catalina;

import java.security.Principal;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/Role.class */
public interface Role extends Principal {
    String getDescription();

    void setDescription(String str);

    String getRolename();

    void setRolename(String str);

    UserDatabase getUserDatabase();
}
