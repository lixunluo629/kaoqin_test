package org.springframework.data.repository.core.support;

import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.util.ProxyUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Ejb3TransactionAnnotationParser;
import org.springframework.transaction.annotation.JtaTransactionAnnotationParser;
import org.springframework.transaction.annotation.SpringTransactionAnnotationParser;
import org.springframework.transaction.annotation.TransactionAnnotationParser;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/TransactionalRepositoryProxyPostProcessor.class */
class TransactionalRepositoryProxyPostProcessor implements RepositoryProxyPostProcessor {
    private final BeanFactory beanFactory;
    private final String transactionManagerName;
    private final boolean enableDefaultTransactions;

    public TransactionalRepositoryProxyPostProcessor(ListableBeanFactory beanFactory, String transactionManagerName, boolean enableDefaultTransaction) {
        Assert.notNull(beanFactory, "BeanFactory must not be null!");
        Assert.notNull(transactionManagerName, "TransactionManagerName must not be null!");
        this.beanFactory = beanFactory;
        this.transactionManagerName = transactionManagerName;
        this.enableDefaultTransactions = enableDefaultTransaction;
    }

    @Override // org.springframework.data.repository.core.support.RepositoryProxyPostProcessor
    public void postProcess(ProxyFactory factory, RepositoryInformation repositoryInformation) {
        CustomAnnotationTransactionAttributeSource transactionAttributeSource = new CustomAnnotationTransactionAttributeSource();
        transactionAttributeSource.setRepositoryInformation(repositoryInformation);
        transactionAttributeSource.setEnableDefaultTransactions(this.enableDefaultTransactions);
        TransactionInterceptor transactionInterceptor = new TransactionInterceptor((PlatformTransactionManager) null, transactionAttributeSource);
        transactionInterceptor.setTransactionManagerBeanName(this.transactionManagerName);
        transactionInterceptor.setBeanFactory(this.beanFactory);
        transactionInterceptor.afterPropertiesSet();
        factory.addAdvice(transactionInterceptor);
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/TransactionalRepositoryProxyPostProcessor$CustomAnnotationTransactionAttributeSource.class */
    static class CustomAnnotationTransactionAttributeSource extends AbstractFallbackTransactionAttributeSource implements Serializable {
        private static final boolean jta12Present = ClassUtils.isPresent("javax.transaction.Transactional", CustomAnnotationTransactionAttributeSource.class.getClassLoader());
        private static final boolean ejb3Present = ClassUtils.isPresent("javax.ejb.TransactionAttribute", CustomAnnotationTransactionAttributeSource.class.getClassLoader());
        private final boolean publicMethodsOnly;
        private final Set<TransactionAnnotationParser> annotationParsers;

        public CustomAnnotationTransactionAttributeSource() {
            this(true);
        }

        public CustomAnnotationTransactionAttributeSource(boolean publicMethodsOnly) {
            this.publicMethodsOnly = publicMethodsOnly;
            this.annotationParsers = new LinkedHashSet(2);
            this.annotationParsers.add(new SpringTransactionAnnotationParser());
            if (jta12Present) {
                this.annotationParsers.add(new JtaTransactionAnnotationParser());
            }
            if (ejb3Present) {
                this.annotationParsers.add(new Ejb3TransactionAnnotationParser());
            }
        }

        public CustomAnnotationTransactionAttributeSource(TransactionAnnotationParser annotationParser) {
            this.publicMethodsOnly = true;
            Assert.notNull(annotationParser, "TransactionAnnotationParser must not be null");
            this.annotationParsers = Collections.singleton(annotationParser);
        }

        public CustomAnnotationTransactionAttributeSource(TransactionAnnotationParser... annotationParsers) {
            this.publicMethodsOnly = true;
            Assert.notEmpty(annotationParsers, "At least one TransactionAnnotationParser needs to be specified");
            Set<TransactionAnnotationParser> parsers = new LinkedHashSet<>(annotationParsers.length);
            Collections.addAll(parsers, annotationParsers);
            this.annotationParsers = parsers;
        }

        public CustomAnnotationTransactionAttributeSource(Set<TransactionAnnotationParser> annotationParsers) {
            this.publicMethodsOnly = true;
            Assert.notEmpty(annotationParsers, "At least one TransactionAnnotationParser needs to be specified");
            this.annotationParsers = annotationParsers;
        }

        @Override // org.springframework.data.repository.core.support.TransactionalRepositoryProxyPostProcessor.AbstractFallbackTransactionAttributeSource
        protected TransactionAttribute findTransactionAttribute(Method method) {
            return determineTransactionAttribute(method);
        }

        @Override // org.springframework.data.repository.core.support.TransactionalRepositoryProxyPostProcessor.AbstractFallbackTransactionAttributeSource
        protected TransactionAttribute findTransactionAttribute(Class<?> clazz) {
            return determineTransactionAttribute(clazz);
        }

        protected TransactionAttribute determineTransactionAttribute(AnnotatedElement ae) {
            for (TransactionAnnotationParser annotationParser : this.annotationParsers) {
                TransactionAttribute attr = annotationParser.parseTransactionAnnotation(ae);
                if (attr != null) {
                    return attr;
                }
            }
            return null;
        }

        @Override // org.springframework.data.repository.core.support.TransactionalRepositoryProxyPostProcessor.AbstractFallbackTransactionAttributeSource
        protected boolean allowPublicMethodsOnly() {
            return this.publicMethodsOnly;
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof CustomAnnotationTransactionAttributeSource)) {
                return false;
            }
            CustomAnnotationTransactionAttributeSource otherTas = (CustomAnnotationTransactionAttributeSource) other;
            return this.annotationParsers.equals(otherTas.annotationParsers) && this.publicMethodsOnly == otherTas.publicMethodsOnly;
        }

        public int hashCode() {
            return this.annotationParsers.hashCode();
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/TransactionalRepositoryProxyPostProcessor$AbstractFallbackTransactionAttributeSource.class */
    static abstract class AbstractFallbackTransactionAttributeSource implements TransactionAttributeSource {
        private static final TransactionAttribute NULL_TRANSACTION_ATTRIBUTE = new DefaultTransactionAttribute();
        private RepositoryInformation repositoryInformation;
        protected final Logger logger = LoggerFactory.getLogger(getClass());
        final Map<Object, TransactionAttribute> attributeCache = new ConcurrentHashMap();
        private boolean enableDefaultTransactions = true;

        protected abstract TransactionAttribute findTransactionAttribute(Method method);

        protected abstract TransactionAttribute findTransactionAttribute(Class<?> cls);

        AbstractFallbackTransactionAttributeSource() {
        }

        public void setRepositoryInformation(RepositoryInformation repositoryInformation) {
            this.repositoryInformation = repositoryInformation;
        }

        public void setEnableDefaultTransactions(boolean enableDefaultTransactions) {
            this.enableDefaultTransactions = enableDefaultTransactions;
        }

        @Override // org.springframework.transaction.interceptor.TransactionAttributeSource
        public TransactionAttribute getTransactionAttribute(Method method, Class<?> targetClass) throws SecurityException, IllegalArgumentException {
            Object cacheKey = getCacheKey(method, targetClass);
            Object cached = this.attributeCache.get(cacheKey);
            if (cached != null) {
                if (cached == NULL_TRANSACTION_ATTRIBUTE) {
                    return null;
                }
                return (TransactionAttribute) cached;
            }
            TransactionAttribute txAtt = computeTransactionAttribute(method, targetClass);
            if (txAtt == null) {
                this.attributeCache.put(cacheKey, NULL_TRANSACTION_ATTRIBUTE);
            } else {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Adding transactional method '" + method.getName() + "' with attribute: " + txAtt);
                }
                this.attributeCache.put(cacheKey, txAtt);
            }
            return txAtt;
        }

        protected Object getCacheKey(Method method, Class<?> targetClass) {
            return new DefaultCacheKey(method, targetClass);
        }

        private TransactionAttribute computeTransactionAttribute(Method method, Class<?> targetClass) throws SecurityException, IllegalArgumentException {
            if (allowPublicMethodsOnly() && !Modifier.isPublic(method.getModifiers())) {
                return null;
            }
            Class<?> userClass = ProxyUtils.getUserClass(targetClass);
            Method specificMethod = BridgeMethodResolver.findBridgedMethod(ClassUtils.getMostSpecificMethod(method, userClass));
            if (specificMethod != method) {
                TransactionAttribute txAtt = findTransactionAttribute(method);
                if (txAtt != null) {
                    return txAtt;
                }
                TransactionAttribute txAtt2 = findTransactionAttribute(method.getDeclaringClass());
                if (txAtt2 != null || !this.enableDefaultTransactions) {
                    return txAtt2;
                }
            }
            TransactionAttribute txAtt3 = findTransactionAttribute(specificMethod);
            if (txAtt3 != null) {
                return txAtt3;
            }
            TransactionAttribute txAtt4 = findTransactionAttribute(specificMethod.getDeclaringClass());
            if (txAtt4 != null) {
                return txAtt4;
            }
            if (!this.enableDefaultTransactions) {
                return null;
            }
            Method targetClassMethod = this.repositoryInformation.getTargetClassMethod(method);
            if (targetClassMethod.equals(method)) {
                return null;
            }
            TransactionAttribute txAtt5 = findTransactionAttribute(targetClassMethod);
            if (txAtt5 != null) {
                return txAtt5;
            }
            TransactionAttribute txAtt6 = findTransactionAttribute(targetClassMethod.getDeclaringClass());
            if (txAtt6 != null) {
                return txAtt6;
            }
            return null;
        }

        protected boolean allowPublicMethodsOnly() {
            return false;
        }

        /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/TransactionalRepositoryProxyPostProcessor$AbstractFallbackTransactionAttributeSource$DefaultCacheKey.class */
        private static class DefaultCacheKey {
            private final Method method;
            private final Class<?> targetClass;

            public DefaultCacheKey(Method method, Class<?> targetClass) {
                this.method = method;
                this.targetClass = targetClass;
            }

            public boolean equals(Object other) {
                if (this == other) {
                    return true;
                }
                if (!(other instanceof DefaultCacheKey)) {
                    return false;
                }
                DefaultCacheKey otherKey = (DefaultCacheKey) other;
                return this.method.equals(otherKey.method) && ObjectUtils.nullSafeEquals(this.targetClass, otherKey.targetClass);
            }

            public int hashCode() {
                return (this.method.hashCode() * 29) + (this.targetClass != null ? this.targetClass.hashCode() : 0);
            }
        }
    }
}
