package org.apache.commons.pool2.impl;

import java.security.AccessControlException;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/CallStackUtils.class */
public final class CallStackUtils {
    private static final boolean CAN_CREATE_SECURITY_MANAGER = canCreateSecurityManager();

    private static boolean canCreateSecurityManager() {
        SecurityManager manager = System.getSecurityManager();
        if (manager == null) {
            return true;
        }
        try {
            manager.checkPermission(new RuntimePermission("createSecurityManager"));
            return true;
        } catch (AccessControlException e) {
            return false;
        }
    }

    public static CallStack newCallStack(String messageFormat, boolean useTimestamp) {
        return CAN_CREATE_SECURITY_MANAGER ? new SecurityManagerCallStack(messageFormat, useTimestamp) : new ThrowableCallStack(messageFormat, useTimestamp);
    }

    private CallStackUtils() {
    }
}
