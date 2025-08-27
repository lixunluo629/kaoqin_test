package org.springframework.transaction.support;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/support/SimpleTransactionScope.class */
public class SimpleTransactionScope implements Scope {
    @Override // org.springframework.beans.factory.config.Scope
    public Object get(String name, ObjectFactory<?> objectFactory) throws IllegalStateException, BeansException {
        ScopedObjectsHolder scopedObjects = (ScopedObjectsHolder) TransactionSynchronizationManager.getResource(this);
        if (scopedObjects == null) {
            scopedObjects = new ScopedObjectsHolder();
            TransactionSynchronizationManager.registerSynchronization(new CleanupSynchronization(scopedObjects));
            TransactionSynchronizationManager.bindResource(this, scopedObjects);
        }
        Object scopedObject = scopedObjects.scopedInstances.get(name);
        if (scopedObject == null) {
            scopedObject = objectFactory.getObject();
            scopedObjects.scopedInstances.put(name, scopedObject);
        }
        return scopedObject;
    }

    @Override // org.springframework.beans.factory.config.Scope
    public Object remove(String name) {
        ScopedObjectsHolder scopedObjects = (ScopedObjectsHolder) TransactionSynchronizationManager.getResource(this);
        if (scopedObjects != null) {
            scopedObjects.destructionCallbacks.remove(name);
            return scopedObjects.scopedInstances.remove(name);
        }
        return null;
    }

    @Override // org.springframework.beans.factory.config.Scope
    public void registerDestructionCallback(String name, Runnable callback) {
        ScopedObjectsHolder scopedObjects = (ScopedObjectsHolder) TransactionSynchronizationManager.getResource(this);
        if (scopedObjects != null) {
            scopedObjects.destructionCallbacks.put(name, callback);
        }
    }

    @Override // org.springframework.beans.factory.config.Scope
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override // org.springframework.beans.factory.config.Scope
    public String getConversationId() {
        return TransactionSynchronizationManager.getCurrentTransactionName();
    }

    /* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/support/SimpleTransactionScope$ScopedObjectsHolder.class */
    static class ScopedObjectsHolder {
        final Map<String, Object> scopedInstances = new HashMap();
        final Map<String, Runnable> destructionCallbacks = new LinkedHashMap();

        ScopedObjectsHolder() {
        }
    }

    /* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/support/SimpleTransactionScope$CleanupSynchronization.class */
    private class CleanupSynchronization extends TransactionSynchronizationAdapter {
        private final ScopedObjectsHolder scopedObjects;

        public CleanupSynchronization(ScopedObjectsHolder scopedObjects) {
            this.scopedObjects = scopedObjects;
        }

        @Override // org.springframework.transaction.support.TransactionSynchronizationAdapter, org.springframework.transaction.support.TransactionSynchronization
        public void suspend() throws IllegalStateException {
            TransactionSynchronizationManager.unbindResource(SimpleTransactionScope.this);
        }

        @Override // org.springframework.transaction.support.TransactionSynchronizationAdapter, org.springframework.transaction.support.TransactionSynchronization
        public void resume() throws IllegalStateException {
            TransactionSynchronizationManager.bindResource(SimpleTransactionScope.this, this.scopedObjects);
        }

        @Override // org.springframework.transaction.support.TransactionSynchronizationAdapter, org.springframework.transaction.support.TransactionSynchronization
        public void afterCompletion(int status) {
            TransactionSynchronizationManager.unbindResourceIfPossible(SimpleTransactionScope.this);
            for (Runnable callback : this.scopedObjects.destructionCallbacks.values()) {
                callback.run();
            }
            this.scopedObjects.destructionCallbacks.clear();
            this.scopedObjects.scopedInstances.clear();
        }
    }
}
