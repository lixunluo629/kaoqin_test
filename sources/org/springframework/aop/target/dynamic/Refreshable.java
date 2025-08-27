package org.springframework.aop.target.dynamic;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/target/dynamic/Refreshable.class */
public interface Refreshable {
    void refresh();

    long getRefreshCount();

    long getLastRefreshTime();
}
