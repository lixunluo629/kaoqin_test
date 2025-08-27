package org.springframework.transaction.support;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/support/ResourceHolder.class */
public interface ResourceHolder {
    void reset();

    void unbound();

    boolean isVoid();
}
