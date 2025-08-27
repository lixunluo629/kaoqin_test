package org.apache.tomcat.util.threads;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/threads/Constants.class */
public final class Constants {
    public static final long DEFAULT_THREAD_RENEWAL_DELAY = 1000;
    public static final boolean IS_SECURITY_ENABLED;

    static {
        IS_SECURITY_ENABLED = System.getSecurityManager() != null;
    }
}
