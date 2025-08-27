package org.springframework.aop.target;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/target/PoolingConfig.class */
public interface PoolingConfig {
    int getMaxSize();

    int getActiveCount() throws UnsupportedOperationException;

    int getIdleCount() throws UnsupportedOperationException;
}
