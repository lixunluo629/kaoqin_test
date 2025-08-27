package org.springframework.beans.factory.support;

import java.security.AccessControlContext;
import java.security.AccessController;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/SimpleSecurityContextProvider.class */
public class SimpleSecurityContextProvider implements SecurityContextProvider {
    private final AccessControlContext acc;

    public SimpleSecurityContextProvider() {
        this(null);
    }

    public SimpleSecurityContextProvider(AccessControlContext acc) {
        this.acc = acc;
    }

    @Override // org.springframework.beans.factory.support.SecurityContextProvider
    public AccessControlContext getAccessControlContext() {
        return this.acc != null ? this.acc : AccessController.getContext();
    }
}
