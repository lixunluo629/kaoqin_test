package org.springframework.beans.factory.support;

import java.security.AccessControlContext;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/SecurityContextProvider.class */
public interface SecurityContextProvider {
    AccessControlContext getAccessControlContext();
}
