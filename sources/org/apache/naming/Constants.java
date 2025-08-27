package org.apache.naming;

@Deprecated
/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/naming/Constants.class */
public final class Constants {
    public static final String Package = "org.apache.naming";
    public static final boolean IS_SECURITY_ENABLED;

    static {
        IS_SECURITY_ENABLED = System.getSecurityManager() != null;
    }
}
