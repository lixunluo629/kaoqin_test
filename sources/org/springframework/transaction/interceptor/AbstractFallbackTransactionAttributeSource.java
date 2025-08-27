package org.springframework.transaction.interceptor;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.MethodClassKey;
import org.springframework.util.ClassUtils;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/interceptor/AbstractFallbackTransactionAttributeSource.class */
public abstract class AbstractFallbackTransactionAttributeSource implements TransactionAttributeSource {
    private static final TransactionAttribute NULL_TRANSACTION_ATTRIBUTE = new DefaultTransactionAttribute() { // from class: org.springframework.transaction.interceptor.AbstractFallbackTransactionAttributeSource.1
        @Override // org.springframework.transaction.support.DefaultTransactionDefinition
        public String toString() {
            return "null";
        }
    };
    protected final Log logger = LogFactory.getLog(getClass());
    private final Map<Object, TransactionAttribute> attributeCache = new ConcurrentHashMap(1024);

    protected abstract TransactionAttribute findTransactionAttribute(Class<?> cls);

    protected abstract TransactionAttribute findTransactionAttribute(Method method);

    @Override // org.springframework.transaction.interceptor.TransactionAttributeSource
    public TransactionAttribute getTransactionAttribute(Method method, Class<?> targetClass) throws SecurityException, IllegalArgumentException {
        if (method.getDeclaringClass() == Object.class) {
            return null;
        }
        Object cacheKey = getCacheKey(method, targetClass);
        TransactionAttribute cached = this.attributeCache.get(cacheKey);
        if (cached != null) {
            if (cached == NULL_TRANSACTION_ATTRIBUTE) {
                return null;
            }
            return cached;
        }
        TransactionAttribute txAttr = computeTransactionAttribute(method, targetClass);
        if (txAttr == null) {
            this.attributeCache.put(cacheKey, NULL_TRANSACTION_ATTRIBUTE);
        } else {
            String methodIdentification = ClassUtils.getQualifiedMethodName(method, targetClass);
            if (txAttr instanceof DefaultTransactionAttribute) {
                ((DefaultTransactionAttribute) txAttr).setDescriptor(methodIdentification);
            }
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Adding transactional method '" + methodIdentification + "' with attribute: " + txAttr);
            }
            this.attributeCache.put(cacheKey, txAttr);
        }
        return txAttr;
    }

    protected Object getCacheKey(Method method, Class<?> targetClass) {
        return new MethodClassKey(method, targetClass);
    }

    protected TransactionAttribute computeTransactionAttribute(Method method, Class<?> targetClass) throws SecurityException, IllegalArgumentException {
        if (allowPublicMethodsOnly() && !Modifier.isPublic(method.getModifiers())) {
            return null;
        }
        Class<?> userClass = ClassUtils.getUserClass(targetClass);
        Method specificMethod = BridgeMethodResolver.findBridgedMethod(ClassUtils.getMostSpecificMethod(method, userClass));
        TransactionAttribute txAttr = findTransactionAttribute(specificMethod);
        if (txAttr != null) {
            return txAttr;
        }
        TransactionAttribute txAttr2 = findTransactionAttribute(specificMethod.getDeclaringClass());
        if (txAttr2 != null && ClassUtils.isUserLevelMethod(method)) {
            return txAttr2;
        }
        if (specificMethod != method) {
            TransactionAttribute txAttr3 = findTransactionAttribute(method);
            if (txAttr3 != null) {
                return txAttr3;
            }
            TransactionAttribute txAttr4 = findTransactionAttribute(method.getDeclaringClass());
            if (txAttr4 != null && ClassUtils.isUserLevelMethod(method)) {
                return txAttr4;
            }
            return null;
        }
        return null;
    }

    protected boolean allowPublicMethodsOnly() {
        return false;
    }
}
