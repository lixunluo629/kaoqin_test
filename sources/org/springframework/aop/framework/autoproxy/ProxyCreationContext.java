package org.springframework.aop.framework.autoproxy;

import org.springframework.core.NamedThreadLocal;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/autoproxy/ProxyCreationContext.class */
public class ProxyCreationContext {
    private static final ThreadLocal<String> currentProxiedBeanName = new NamedThreadLocal("Name of currently proxied bean");

    public static String getCurrentProxiedBeanName() {
        return currentProxiedBeanName.get();
    }

    static void setCurrentProxiedBeanName(String beanName) {
        if (beanName != null) {
            currentProxiedBeanName.set(beanName);
        } else {
            currentProxiedBeanName.remove();
        }
    }
}
