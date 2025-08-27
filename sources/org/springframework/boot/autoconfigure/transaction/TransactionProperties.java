package org.springframework.boot.autoconfigure.transaction;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

@ConfigurationProperties(prefix = "spring.transaction")
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/transaction/TransactionProperties.class */
public class TransactionProperties implements PlatformTransactionManagerCustomizer<AbstractPlatformTransactionManager> {
    private Integer defaultTimeout;
    private Boolean rollbackOnCommitFailure;

    public Integer getDefaultTimeout() {
        return this.defaultTimeout;
    }

    public void setDefaultTimeout(Integer defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    public Boolean getRollbackOnCommitFailure() {
        return this.rollbackOnCommitFailure;
    }

    public void setRollbackOnCommitFailure(Boolean rollbackOnCommitFailure) {
        this.rollbackOnCommitFailure = rollbackOnCommitFailure;
    }

    @Override // org.springframework.boot.autoconfigure.transaction.PlatformTransactionManagerCustomizer
    public void customize(AbstractPlatformTransactionManager transactionManager) {
        if (this.defaultTimeout != null) {
            transactionManager.setDefaultTimeout(this.defaultTimeout.intValue());
        }
        if (this.rollbackOnCommitFailure != null) {
            transactionManager.setRollbackOnCommitFailure(this.rollbackOnCommitFailure.booleanValue());
        }
    }
}
