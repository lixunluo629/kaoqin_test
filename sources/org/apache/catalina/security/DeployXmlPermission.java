package org.apache.catalina.security;

import java.security.BasicPermission;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/security/DeployXmlPermission.class */
public class DeployXmlPermission extends BasicPermission {
    private static final long serialVersionUID = 1;

    public DeployXmlPermission(String name) {
        super(name);
    }

    public DeployXmlPermission(String name, String actions) {
        super(name, actions);
    }
}
