package org.springframework.aop.framework.adapter;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/adapter/GlobalAdvisorAdapterRegistry.class */
public abstract class GlobalAdvisorAdapterRegistry {
    private static AdvisorAdapterRegistry instance = new DefaultAdvisorAdapterRegistry();

    public static AdvisorAdapterRegistry getInstance() {
        return instance;
    }

    static void reset() {
        instance = new DefaultAdvisorAdapterRegistry();
    }
}
