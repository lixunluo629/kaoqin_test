package org.springframework.data.support;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/support/IsNewStrategyFactory.class */
public interface IsNewStrategyFactory {
    IsNewStrategy getIsNewStrategy(Class<?> cls);
}
