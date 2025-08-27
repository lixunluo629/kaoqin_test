package org.springframework.boot.autoconfigure.transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.ResolvableType;
import org.springframework.transaction.PlatformTransactionManager;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/transaction/TransactionManagerCustomizers.class */
public class TransactionManagerCustomizers {
    private static final Log logger = LogFactory.getLog(TransactionManagerCustomizers.class);
    private final List<PlatformTransactionManagerCustomizer<?>> customizers;

    public TransactionManagerCustomizers(Collection<? extends PlatformTransactionManagerCustomizer<?>> customizers) {
        this.customizers = customizers != null ? new ArrayList(customizers) : null;
    }

    public void customize(PlatformTransactionManager transactionManager) {
        if (this.customizers != null) {
            for (PlatformTransactionManagerCustomizer<?> customizer : this.customizers) {
                Class<?> generic = ResolvableType.forClass(PlatformTransactionManagerCustomizer.class, customizer.getClass()).resolveGeneric(new int[0]);
                if (generic.isInstance(transactionManager)) {
                    customize(transactionManager, customizer);
                }
            }
        }
    }

    private void customize(PlatformTransactionManager transactionManager, PlatformTransactionManagerCustomizer customizer) {
        try {
            customizer.customize(transactionManager);
        } catch (ClassCastException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("Non-matching transaction manager type for customizer: " + customizer, ex);
            }
        }
    }
}
