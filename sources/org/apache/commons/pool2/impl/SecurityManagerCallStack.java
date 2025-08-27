package org.apache.commons.pool2.impl;

import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/SecurityManagerCallStack.class */
public class SecurityManagerCallStack implements CallStack {
    private final String messageFormat;
    private final DateFormat dateFormat;
    private final PrivateSecurityManager securityManager;
    private volatile Snapshot snapshot;

    public SecurityManagerCallStack(String messageFormat, boolean useTimestamp) {
        this.messageFormat = messageFormat;
        this.dateFormat = useTimestamp ? new SimpleDateFormat(messageFormat) : null;
        this.securityManager = (PrivateSecurityManager) AccessController.doPrivileged(new PrivilegedAction<PrivateSecurityManager>() { // from class: org.apache.commons.pool2.impl.SecurityManagerCallStack.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public PrivateSecurityManager run() {
                return new PrivateSecurityManager();
            }
        });
    }

    @Override // org.apache.commons.pool2.impl.CallStack
    public boolean printStackTrace(PrintWriter writer) {
        String message;
        Snapshot snapshot = this.snapshot;
        if (snapshot == null) {
            return false;
        }
        if (this.dateFormat == null) {
            message = this.messageFormat;
        } else {
            synchronized (this.dateFormat) {
                message = this.dateFormat.format(Long.valueOf(snapshot.timestamp));
            }
        }
        writer.println(message);
        for (WeakReference<Class<?>> reference : snapshot.stack) {
            writer.println(reference.get());
        }
        return true;
    }

    @Override // org.apache.commons.pool2.impl.CallStack
    public void fillInStackTrace() {
        this.snapshot = new Snapshot(this.securityManager.getCallStack());
    }

    @Override // org.apache.commons.pool2.impl.CallStack
    public void clear() {
        this.snapshot = null;
    }

    /* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/SecurityManagerCallStack$PrivateSecurityManager.class */
    private static class PrivateSecurityManager extends SecurityManager {
        private PrivateSecurityManager() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public List<WeakReference<Class<?>>> getCallStack() {
            Class<?>[] classes = getClassContext();
            List<WeakReference<Class<?>>> stack = new ArrayList<>(classes.length);
            for (Class<?> klass : classes) {
                stack.add(new WeakReference<>(klass));
            }
            return stack;
        }
    }

    /* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/SecurityManagerCallStack$Snapshot.class */
    private static class Snapshot {
        private final long timestamp;
        private final List<WeakReference<Class<?>>> stack;

        private Snapshot(List<WeakReference<Class<?>>> stack) {
            this.timestamp = System.currentTimeMillis();
            this.stack = stack;
        }
    }
}
