package org.springframework.transaction.event;

import java.lang.reflect.Method;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ApplicationListenerMethodAdapter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/event/ApplicationListenerMethodTransactionalAdapter.class */
class ApplicationListenerMethodTransactionalAdapter extends ApplicationListenerMethodAdapter {
    private final TransactionalEventListener annotation;

    public ApplicationListenerMethodTransactionalAdapter(String beanName, Class<?> targetClass, Method method) {
        super(beanName, targetClass, method);
        this.annotation = (TransactionalEventListener) AnnotatedElementUtils.findMergedAnnotation(method, TransactionalEventListener.class);
        if (this.annotation == null) {
            throw new IllegalStateException("No TransactionalEventListener annotation found on method: " + method);
        }
    }

    @Override // org.springframework.context.event.ApplicationListenerMethodAdapter, org.springframework.context.ApplicationListener
    public void onApplicationEvent(ApplicationEvent event) throws IllegalStateException {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronization transactionSynchronization = createTransactionSynchronization(event);
            TransactionSynchronizationManager.registerSynchronization(transactionSynchronization);
        } else {
            if (this.annotation.fallbackExecution()) {
                if (this.annotation.phase() == TransactionPhase.AFTER_ROLLBACK && this.logger.isWarnEnabled()) {
                    this.logger.warn("Processing " + event + " as a fallback execution on AFTER_ROLLBACK phase");
                }
                processEvent(event);
                return;
            }
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("No transaction is active - skipping " + event);
            }
        }
    }

    private TransactionSynchronization createTransactionSynchronization(ApplicationEvent event) {
        return new TransactionSynchronizationEventAdapter(this, event, this.annotation.phase());
    }

    /* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/event/ApplicationListenerMethodTransactionalAdapter$TransactionSynchronizationEventAdapter.class */
    private static class TransactionSynchronizationEventAdapter extends TransactionSynchronizationAdapter {
        private final ApplicationListenerMethodAdapter listener;
        private final ApplicationEvent event;
        private final TransactionPhase phase;

        public TransactionSynchronizationEventAdapter(ApplicationListenerMethodAdapter listener, ApplicationEvent event, TransactionPhase phase) {
            this.listener = listener;
            this.event = event;
            this.phase = phase;
        }

        @Override // org.springframework.transaction.support.TransactionSynchronizationAdapter, org.springframework.core.Ordered
        public int getOrder() {
            return this.listener.getOrder();
        }

        @Override // org.springframework.transaction.support.TransactionSynchronizationAdapter, org.springframework.transaction.support.TransactionSynchronization
        public void beforeCommit(boolean readOnly) {
            if (this.phase == TransactionPhase.BEFORE_COMMIT) {
                processEvent();
            }
        }

        @Override // org.springframework.transaction.support.TransactionSynchronizationAdapter, org.springframework.transaction.support.TransactionSynchronization
        public void afterCompletion(int status) {
            if (this.phase == TransactionPhase.AFTER_COMMIT && status == 0) {
                processEvent();
                return;
            }
            if (this.phase == TransactionPhase.AFTER_ROLLBACK && status == 1) {
                processEvent();
            } else if (this.phase == TransactionPhase.AFTER_COMPLETION) {
                processEvent();
            }
        }

        protected void processEvent() {
            this.listener.processEvent(this.event);
        }
    }
}
