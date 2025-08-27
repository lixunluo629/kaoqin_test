package org.springframework.cache.interceptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvoker;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.lang.UsesJava8;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/CacheAspectSupport.class */
public abstract class CacheAspectSupport extends AbstractCacheInvoker implements BeanFactoryAware, InitializingBean, SmartInitializingSingleton {
    private static Class<?> javaUtilOptionalClass;
    private CacheOperationSource cacheOperationSource;
    private CacheResolver cacheResolver;
    private BeanFactory beanFactory;
    protected final Log logger = LogFactory.getLog(getClass());
    private final Map<CacheOperationCacheKey, CacheOperationMetadata> metadataCache = new ConcurrentHashMap(1024);
    private final CacheOperationExpressionEvaluator evaluator = new CacheOperationExpressionEvaluator();
    private KeyGenerator keyGenerator = new SimpleKeyGenerator();
    private boolean initialized = false;

    static {
        javaUtilOptionalClass = null;
        try {
            javaUtilOptionalClass = ClassUtils.forName("java.util.Optional", CacheAspectSupport.class.getClassLoader());
        } catch (ClassNotFoundException e) {
        }
    }

    public void setCacheOperationSources(CacheOperationSource... cacheOperationSources) {
        Assert.notEmpty(cacheOperationSources, "At least 1 CacheOperationSource needs to be specified");
        this.cacheOperationSource = cacheOperationSources.length > 1 ? new CompositeCacheOperationSource(cacheOperationSources) : cacheOperationSources[0];
    }

    public CacheOperationSource getCacheOperationSource() {
        return this.cacheOperationSource;
    }

    public void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    public KeyGenerator getKeyGenerator() {
        return this.keyGenerator;
    }

    public void setCacheResolver(CacheResolver cacheResolver) {
        this.cacheResolver = cacheResolver;
    }

    public CacheResolver getCacheResolver() {
        return this.cacheResolver;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheResolver = new SimpleCacheResolver(cacheManager);
    }

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Deprecated
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.beanFactory = applicationContext;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        Assert.state(getCacheOperationSource() != null, "The 'cacheOperationSources' property is required: If there are no cacheable methods, then don't use a cache aspect.");
        Assert.state(getErrorHandler() != null, "The 'errorHandler' property is required");
    }

    @Override // org.springframework.beans.factory.SmartInitializingSingleton
    public void afterSingletonsInstantiated() {
        if (getCacheResolver() == null) {
            try {
                setCacheManager((CacheManager) this.beanFactory.getBean(CacheManager.class));
            } catch (NoUniqueBeanDefinitionException e) {
                throw new IllegalStateException("No CacheResolver specified, and no unique bean of type CacheManager found. Mark one as primary or declare a specific CacheManager to use.");
            } catch (NoSuchBeanDefinitionException e2) {
                throw new IllegalStateException("No CacheResolver specified, and no bean of type CacheManager found. Register a CacheManager bean or remove the @EnableCaching annotation from your configuration.");
            }
        }
        this.initialized = true;
    }

    protected String methodIdentification(Method method, Class<?> targetClass) {
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
        return ClassUtils.getQualifiedMethodName(specificMethod);
    }

    protected Collection<? extends Cache> getCaches(CacheOperationInvocationContext<CacheOperation> context, CacheResolver cacheResolver) {
        Collection<? extends Cache> caches = cacheResolver.resolveCaches(context);
        if (caches.isEmpty()) {
            throw new IllegalStateException("No cache could be resolved for '" + context.getOperation() + "' using resolver '" + cacheResolver + "'. At least one cache should be provided per cache operation.");
        }
        return caches;
    }

    protected CacheOperationContext getOperationContext(CacheOperation operation, Method method, Object[] args, Object target, Class<?> targetClass) {
        CacheOperationMetadata metadata = getCacheOperationMetadata(operation, method, targetClass);
        return new CacheOperationContext(metadata, args, target);
    }

    protected CacheOperationMetadata getCacheOperationMetadata(CacheOperation operation, Method method, Class<?> targetClass) {
        KeyGenerator operationKeyGenerator;
        CacheResolver operationCacheResolver;
        CacheOperationCacheKey cacheKey = new CacheOperationCacheKey(operation, method, targetClass);
        CacheOperationMetadata metadata = this.metadataCache.get(cacheKey);
        if (metadata == null) {
            if (StringUtils.hasText(operation.getKeyGenerator())) {
                operationKeyGenerator = (KeyGenerator) getBean(operation.getKeyGenerator(), KeyGenerator.class);
            } else {
                operationKeyGenerator = getKeyGenerator();
            }
            if (StringUtils.hasText(operation.getCacheResolver())) {
                operationCacheResolver = (CacheResolver) getBean(operation.getCacheResolver(), CacheResolver.class);
            } else if (StringUtils.hasText(operation.getCacheManager())) {
                CacheManager cacheManager = (CacheManager) getBean(operation.getCacheManager(), CacheManager.class);
                operationCacheResolver = new SimpleCacheResolver(cacheManager);
            } else {
                operationCacheResolver = getCacheResolver();
            }
            metadata = new CacheOperationMetadata(operation, method, targetClass, operationKeyGenerator, operationCacheResolver);
            this.metadataCache.put(cacheKey, metadata);
        }
        return metadata;
    }

    protected <T> T getBean(String str, Class<T> cls) {
        return (T) BeanFactoryAnnotationUtils.qualifiedBeanOfType(this.beanFactory, cls, str);
    }

    protected void clearMetadataCache() {
        this.metadataCache.clear();
        this.evaluator.clear();
    }

    protected Object execute(CacheOperationInvoker invoker, Object target, Method method, Object[] args) {
        if (this.initialized) {
            Class<?> targetClass = getTargetClass(target);
            Collection<CacheOperation> operations = getCacheOperationSource().getCacheOperations(method, targetClass);
            if (!CollectionUtils.isEmpty(operations)) {
                return execute(invoker, method, new CacheOperationContexts(operations, method, args, target, targetClass));
            }
        }
        return invoker.invoke();
    }

    protected Object invokeOperation(CacheOperationInvoker invoker) {
        return invoker.invoke();
    }

    private Class<?> getTargetClass(Object target) {
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
        if (targetClass == null && target != null) {
            targetClass = target.getClass();
        }
        return targetClass;
    }

    private Object execute(final CacheOperationInvoker invoker, Method method, CacheOperationContexts contexts) {
        Object returnValue;
        Object cacheValue;
        if (contexts.isSynchronized()) {
            CacheOperationContext context = contexts.get(CacheableOperation.class).iterator().next();
            if (isConditionPassing(context, CacheOperationExpressionEvaluator.NO_RESULT)) {
                Object key = generateKey(context, CacheOperationExpressionEvaluator.NO_RESULT);
                Cache cache = context.getCaches().iterator().next();
                try {
                    return wrapCacheValue(method, cache.get(key, new Callable<Object>() { // from class: org.springframework.cache.interceptor.CacheAspectSupport.1
                        @Override // java.util.concurrent.Callable
                        public Object call() throws Exception {
                            return CacheAspectSupport.this.unwrapReturnValue(CacheAspectSupport.this.invokeOperation(invoker));
                        }
                    }));
                } catch (Cache.ValueRetrievalException ex) {
                    throw ((CacheOperationInvoker.ThrowableWrapper) ex.getCause());
                }
            }
            return invokeOperation(invoker);
        }
        processCacheEvicts(contexts.get(CacheEvictOperation.class), true, CacheOperationExpressionEvaluator.NO_RESULT);
        Cache.ValueWrapper cacheHit = findCachedItem(contexts.get(CacheableOperation.class));
        List<CachePutRequest> cachePutRequests = new LinkedList<>();
        if (cacheHit == null) {
            collectPutRequests(contexts.get(CacheableOperation.class), CacheOperationExpressionEvaluator.NO_RESULT, cachePutRequests);
        }
        if (cacheHit != null && cachePutRequests.isEmpty() && !hasCachePut(contexts)) {
            cacheValue = cacheHit.get();
            returnValue = wrapCacheValue(method, cacheValue);
        } else {
            returnValue = invokeOperation(invoker);
            cacheValue = unwrapReturnValue(returnValue);
        }
        collectPutRequests(contexts.get(CachePutOperation.class), cacheValue, cachePutRequests);
        for (CachePutRequest cachePutRequest : cachePutRequests) {
            cachePutRequest.apply(cacheValue);
        }
        processCacheEvicts(contexts.get(CacheEvictOperation.class), false, cacheValue);
        return returnValue;
    }

    private Object wrapCacheValue(Method method, Object cacheValue) {
        if (method.getReturnType() == javaUtilOptionalClass && (cacheValue == null || cacheValue.getClass() != javaUtilOptionalClass)) {
            return OptionalUnwrapper.wrap(cacheValue);
        }
        return cacheValue;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object unwrapReturnValue(Object returnValue) {
        if (returnValue != null && returnValue.getClass() == javaUtilOptionalClass) {
            return OptionalUnwrapper.unwrap(returnValue);
        }
        return returnValue;
    }

    private boolean hasCachePut(CacheOperationContexts contexts) {
        Collection<CacheOperationContext> cachePutContexts = contexts.get(CachePutOperation.class);
        Collection<CacheOperationContext> excluded = new ArrayList<>();
        for (CacheOperationContext context : cachePutContexts) {
            try {
                if (!context.isConditionPassing(CacheOperationExpressionEvaluator.RESULT_UNAVAILABLE)) {
                    excluded.add(context);
                }
            } catch (VariableNotAvailableException e) {
            }
        }
        return cachePutContexts.size() != excluded.size();
    }

    private void processCacheEvicts(Collection<CacheOperationContext> contexts, boolean beforeInvocation, Object result) {
        for (CacheOperationContext context : contexts) {
            CacheEvictOperation operation = (CacheEvictOperation) context.metadata.operation;
            if (beforeInvocation == operation.isBeforeInvocation() && isConditionPassing(context, result)) {
                performCacheEvict(context, operation, result);
            }
        }
    }

    private void performCacheEvict(CacheOperationContext context, CacheEvictOperation operation, Object result) {
        Object key = null;
        for (Cache cache : context.getCaches()) {
            if (operation.isCacheWide()) {
                logInvalidating(context, operation, null);
                doClear(cache);
            } else {
                if (key == null) {
                    key = context.generateKey(result);
                }
                logInvalidating(context, operation, key);
                doEvict(cache, key);
            }
        }
    }

    private void logInvalidating(CacheOperationContext context, CacheEvictOperation operation, Object key) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Invalidating " + (key != null ? "cache key [" + key + "]" : "entire cache") + " for operation " + operation + " on method " + context.metadata.method);
        }
    }

    private Cache.ValueWrapper findCachedItem(Collection<CacheOperationContext> contexts) {
        Object result = CacheOperationExpressionEvaluator.NO_RESULT;
        for (CacheOperationContext context : contexts) {
            if (isConditionPassing(context, result)) {
                Object key = generateKey(context, result);
                Cache.ValueWrapper cached = findInCaches(context, key);
                if (cached != null) {
                    return cached;
                }
                if (this.logger.isTraceEnabled()) {
                    this.logger.trace("No cache entry for key '" + key + "' in cache(s) " + context.getCacheNames());
                }
            }
        }
        return null;
    }

    private void collectPutRequests(Collection<CacheOperationContext> contexts, Object result, Collection<CachePutRequest> putRequests) {
        for (CacheOperationContext context : contexts) {
            if (isConditionPassing(context, result)) {
                Object key = generateKey(context, result);
                putRequests.add(new CachePutRequest(context, key));
            }
        }
    }

    private Cache.ValueWrapper findInCaches(CacheOperationContext context, Object key) {
        for (Cache cache : context.getCaches()) {
            Cache.ValueWrapper wrapper = doGet(cache, key);
            if (wrapper != null) {
                if (this.logger.isTraceEnabled()) {
                    this.logger.trace("Cache entry for key '" + key + "' found in cache '" + cache.getName() + "'");
                }
                return wrapper;
            }
        }
        return null;
    }

    private boolean isConditionPassing(CacheOperationContext context, Object result) {
        boolean passing = context.isConditionPassing(result);
        if (!passing && this.logger.isTraceEnabled()) {
            this.logger.trace("Cache condition failed on method " + context.metadata.method + " for operation " + context.metadata.operation);
        }
        return passing;
    }

    private Object generateKey(CacheOperationContext context, Object result) {
        Object key = context.generateKey(result);
        if (key == null) {
            throw new IllegalArgumentException("Null key returned for cache operation (maybe you are using named params on classes without debug info?) " + context.metadata.operation);
        }
        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Computed cache key '" + key + "' for operation " + context.metadata.operation);
        }
        return key;
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/CacheAspectSupport$CacheOperationContexts.class */
    private class CacheOperationContexts {
        private final MultiValueMap<Class<? extends CacheOperation>, CacheOperationContext> contexts = new LinkedMultiValueMap();
        private final boolean sync;

        /* JADX WARN: Multi-variable type inference failed */
        public CacheOperationContexts(Collection<? extends CacheOperation> operations, Method method, Object[] args, Object target, Class<?> targetClass) {
            for (CacheOperation operation : operations) {
                this.contexts.add(operation.getClass(), CacheAspectSupport.this.getOperationContext(operation, method, args, target, targetClass));
            }
            this.sync = determineSyncFlag(method);
        }

        public Collection<CacheOperationContext> get(Class<? extends CacheOperation> operationClass) {
            Collection<CacheOperationContext> result = (Collection) this.contexts.get(operationClass);
            return result != null ? result : Collections.emptyList();
        }

        public boolean isSynchronized() {
            return this.sync;
        }

        private boolean determineSyncFlag(Method method) {
            List<CacheOperationContext> cacheOperationContexts = (List) this.contexts.get(CacheableOperation.class);
            if (cacheOperationContexts == null) {
                return false;
            }
            boolean syncEnabled = false;
            Iterator<CacheOperationContext> it = cacheOperationContexts.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                if (((CacheableOperation) it.next().getOperation()).isSync()) {
                    syncEnabled = true;
                    break;
                }
            }
            if (syncEnabled) {
                if (this.contexts.size() > 1) {
                    throw new IllegalStateException("@Cacheable(sync=true) cannot be combined with other cache operations on '" + method + "'");
                }
                if (cacheOperationContexts.size() > 1) {
                    throw new IllegalStateException("Only one @Cacheable(sync=true) entry is allowed on '" + method + "'");
                }
                CacheOperationContext cacheOperationContext = cacheOperationContexts.iterator().next();
                CacheableOperation operation = (CacheableOperation) cacheOperationContext.getOperation();
                if (cacheOperationContext.getCaches().size() > 1) {
                    throw new IllegalStateException("@Cacheable(sync=true) only allows a single cache on '" + operation + "'");
                }
                if (StringUtils.hasText(operation.getUnless())) {
                    throw new IllegalStateException("@Cacheable(sync=true) does not support unless attribute on '" + operation + "'");
                }
                return true;
            }
            return false;
        }
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/CacheAspectSupport$CacheOperationMetadata.class */
    protected static class CacheOperationMetadata {
        private final CacheOperation operation;
        private final Method method;
        private final Class<?> targetClass;
        private final KeyGenerator keyGenerator;
        private final CacheResolver cacheResolver;

        public CacheOperationMetadata(CacheOperation operation, Method method, Class<?> targetClass, KeyGenerator keyGenerator, CacheResolver cacheResolver) {
            this.operation = operation;
            this.method = method;
            this.targetClass = targetClass;
            this.keyGenerator = keyGenerator;
            this.cacheResolver = cacheResolver;
        }
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/CacheAspectSupport$CacheOperationContext.class */
    protected class CacheOperationContext implements CacheOperationInvocationContext<CacheOperation> {
        private final CacheOperationMetadata metadata;
        private final Object[] args;
        private final Object target;
        private final Collection<? extends Cache> caches;
        private final Collection<String> cacheNames;
        private final AnnotatedElementKey methodCacheKey;

        public CacheOperationContext(CacheOperationMetadata metadata, Object[] args, Object target) {
            this.metadata = metadata;
            this.args = extractArgs(metadata.method, args);
            this.target = target;
            this.caches = CacheAspectSupport.this.getCaches(this, metadata.cacheResolver);
            this.cacheNames = createCacheNames(this.caches);
            this.methodCacheKey = new AnnotatedElementKey(metadata.method, metadata.targetClass);
        }

        @Override // org.springframework.cache.interceptor.CacheOperationInvocationContext
        public CacheOperation getOperation() {
            return this.metadata.operation;
        }

        @Override // org.springframework.cache.interceptor.CacheOperationInvocationContext
        public Object getTarget() {
            return this.target;
        }

        @Override // org.springframework.cache.interceptor.CacheOperationInvocationContext
        public Method getMethod() {
            return this.metadata.method;
        }

        @Override // org.springframework.cache.interceptor.CacheOperationInvocationContext
        public Object[] getArgs() {
            return this.args;
        }

        private Object[] extractArgs(Method method, Object[] args) {
            if (!method.isVarArgs()) {
                return args;
            }
            Object[] varArgs = ObjectUtils.toObjectArray(args[args.length - 1]);
            Object[] combinedArgs = new Object[(args.length - 1) + varArgs.length];
            System.arraycopy(args, 0, combinedArgs, 0, args.length - 1);
            System.arraycopy(varArgs, 0, combinedArgs, args.length - 1, varArgs.length);
            return combinedArgs;
        }

        protected boolean isConditionPassing(Object result) {
            if (StringUtils.hasText(this.metadata.operation.getCondition())) {
                EvaluationContext evaluationContext = createEvaluationContext(result);
                return CacheAspectSupport.this.evaluator.condition(this.metadata.operation.getCondition(), this.methodCacheKey, evaluationContext);
            }
            return true;
        }

        protected boolean canPutToCache(Object value) {
            String unless = "";
            if (this.metadata.operation instanceof CacheableOperation) {
                unless = ((CacheableOperation) this.metadata.operation).getUnless();
            } else if (this.metadata.operation instanceof CachePutOperation) {
                unless = ((CachePutOperation) this.metadata.operation).getUnless();
            }
            if (StringUtils.hasText(unless)) {
                EvaluationContext evaluationContext = createEvaluationContext(value);
                return !CacheAspectSupport.this.evaluator.unless(unless, this.methodCacheKey, evaluationContext);
            }
            return true;
        }

        protected Object generateKey(Object result) {
            if (!StringUtils.hasText(this.metadata.operation.getKey())) {
                return this.metadata.keyGenerator.generate(this.target, this.metadata.method, this.args);
            }
            EvaluationContext evaluationContext = createEvaluationContext(result);
            return CacheAspectSupport.this.evaluator.key(this.metadata.operation.getKey(), this.methodCacheKey, evaluationContext);
        }

        private EvaluationContext createEvaluationContext(Object result) {
            return CacheAspectSupport.this.evaluator.createEvaluationContext(this.caches, this.metadata.method, this.args, this.target, this.metadata.targetClass, result, CacheAspectSupport.this.beanFactory);
        }

        protected Collection<? extends Cache> getCaches() {
            return this.caches;
        }

        protected Collection<String> getCacheNames() {
            return this.cacheNames;
        }

        private Collection<String> createCacheNames(Collection<? extends Cache> caches) {
            Collection<String> names = new ArrayList<>();
            for (Cache cache : caches) {
                names.add(cache.getName());
            }
            return names;
        }
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/CacheAspectSupport$CachePutRequest.class */
    private class CachePutRequest {
        private final CacheOperationContext context;
        private final Object key;

        public CachePutRequest(CacheOperationContext context, Object key) {
            this.context = context;
            this.key = key;
        }

        public void apply(Object result) {
            if (this.context.canPutToCache(result)) {
                for (Cache cache : this.context.getCaches()) {
                    CacheAspectSupport.this.doPut(cache, this.key, result);
                }
            }
        }
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/CacheAspectSupport$CacheOperationCacheKey.class */
    private static final class CacheOperationCacheKey implements Comparable<CacheOperationCacheKey> {
        private final CacheOperation cacheOperation;
        private final AnnotatedElementKey methodCacheKey;

        private CacheOperationCacheKey(CacheOperation cacheOperation, Method method, Class<?> targetClass) {
            this.cacheOperation = cacheOperation;
            this.methodCacheKey = new AnnotatedElementKey(method, targetClass);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof CacheOperationCacheKey)) {
                return false;
            }
            CacheOperationCacheKey otherKey = (CacheOperationCacheKey) other;
            return this.cacheOperation.equals(otherKey.cacheOperation) && this.methodCacheKey.equals(otherKey.methodCacheKey);
        }

        public int hashCode() {
            return (this.cacheOperation.hashCode() * 31) + this.methodCacheKey.hashCode();
        }

        public String toString() {
            return this.cacheOperation + " on " + this.methodCacheKey;
        }

        @Override // java.lang.Comparable
        public int compareTo(CacheOperationCacheKey other) {
            int result = this.cacheOperation.getName().compareTo(other.cacheOperation.getName());
            if (result == 0) {
                result = this.methodCacheKey.compareTo(other.methodCacheKey);
            }
            return result;
        }
    }

    @UsesJava8
    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/CacheAspectSupport$OptionalUnwrapper.class */
    private static class OptionalUnwrapper {
        private OptionalUnwrapper() {
        }

        public static Object unwrap(Object optionalObject) {
            Optional<?> optional = (Optional) optionalObject;
            if (!optional.isPresent()) {
                return null;
            }
            Object result = optional.get();
            Assert.isTrue(!(result instanceof Optional), "Multi-level Optional usage not supported");
            return result;
        }

        public static Object wrap(Object value) {
            return Optional.ofNullable(value);
        }
    }
}
