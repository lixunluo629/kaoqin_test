package org.springframework.boot.autoconfigure.transaction;

import org.springframework.transaction.PlatformTransactionManager;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/transaction/PlatformTransactionManagerCustomizer.class */
public interface PlatformTransactionManagerCustomizer<T extends PlatformTransactionManager> {
    void customize(T t);
}
