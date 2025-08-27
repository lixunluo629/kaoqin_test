package org.springframework.data.repository.core.support;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.projection.DefaultMethodInvokingMethodInterceptor;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.DefaultEvaluationContextProvider;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/RepositoryFactorySupport.class */
public abstract class RepositoryFactorySupport implements BeanClassLoaderAware, BeanFactoryAware {
    private static final boolean IS_JAVA_8 = ClassUtils.isPresent("java.util.Optional", RepositoryFactorySupport.class.getClassLoader());
    private Class<?> repositoryBaseClass;
    private QueryLookupStrategy.Key queryLookupStrategyKey;
    private BeanFactory beanFactory;
    private final Map<RepositoryInformationCacheKey, RepositoryInformation> repositoryInformationCache = new HashMap();
    private final List<RepositoryProxyPostProcessor> postProcessors = new ArrayList();
    private List<QueryCreationListener<?>> queryPostProcessors = new ArrayList();
    private NamedQueries namedQueries = PropertiesBasedNamedQueries.EMPTY;
    private ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
    private EvaluationContextProvider evaluationContextProvider = DefaultEvaluationContextProvider.INSTANCE;
    private QueryCollectingQueryCreationListener collectingListener = new QueryCollectingQueryCreationListener();

    public abstract <T, ID extends Serializable> EntityInformation<T, ID> getEntityInformation(Class<T> cls);

    protected abstract Object getTargetRepository(RepositoryInformation repositoryInformation);

    protected abstract Class<?> getRepositoryBaseClass(RepositoryMetadata repositoryMetadata);

    public RepositoryFactorySupport() {
        this.queryPostProcessors.add(this.collectingListener);
    }

    public void setQueryLookupStrategyKey(QueryLookupStrategy.Key key) {
        this.queryLookupStrategyKey = key;
    }

    public void setNamedQueries(NamedQueries namedQueries) {
        this.namedQueries = namedQueries == null ? PropertiesBasedNamedQueries.EMPTY : namedQueries;
    }

    @Override // org.springframework.beans.factory.BeanClassLoaderAware
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader == null ? ClassUtils.getDefaultClassLoader() : classLoader;
    }

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void setEvaluationContextProvider(EvaluationContextProvider evaluationContextProvider) {
        this.evaluationContextProvider = evaluationContextProvider == null ? DefaultEvaluationContextProvider.INSTANCE : evaluationContextProvider;
    }

    public void setRepositoryBaseClass(Class<?> repositoryBaseClass) {
        this.repositoryBaseClass = repositoryBaseClass;
    }

    public void addQueryCreationListener(QueryCreationListener<?> listener) {
        Assert.notNull(listener, "Listener must not be null!");
        this.queryPostProcessors.add(listener);
    }

    public void addRepositoryProxyPostProcessor(RepositoryProxyPostProcessor processor) {
        Assert.notNull(processor, "RepositoryProxyPostProcessor must not be null!");
        this.postProcessors.add(processor);
    }

    public <T> T getRepository(Class<T> cls) {
        return (T) getRepository(cls, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> T getRepository(Class<T> cls, Object obj) {
        RepositoryInformation repositoryInformation = getRepositoryInformation(getRepositoryMetadata(cls), null == obj ? null : obj.getClass());
        validate(repositoryInformation, obj);
        Object targetRepository = getTargetRepository(repositoryInformation);
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(targetRepository);
        proxyFactory.setInterfaces(cls, Repository.class);
        proxyFactory.addAdvice(SurroundingTransactionDetectorMethodInterceptor.INSTANCE);
        proxyFactory.addAdvisor(ExposeInvocationInterceptor.ADVISOR);
        Class<?> transactionProxyType = getTransactionProxyType();
        if (transactionProxyType != null) {
            proxyFactory.addInterface(transactionProxyType);
        }
        Iterator<RepositoryProxyPostProcessor> it = this.postProcessors.iterator();
        while (it.hasNext()) {
            it.next().postProcess(proxyFactory, repositoryInformation);
        }
        if (IS_JAVA_8) {
            proxyFactory.addAdvice(new DefaultMethodInvokingMethodInterceptor());
        }
        proxyFactory.addAdvice(new QueryExecutorMethodInterceptor(repositoryInformation, obj, targetRepository, getProjectionFactory(this.classLoader, this.beanFactory)));
        return (T) proxyFactory.getProxy(this.classLoader);
    }

    protected ProjectionFactory getProjectionFactory(ClassLoader classLoader, BeanFactory beanFactory) throws BeansException {
        SpelAwareProxyProjectionFactory factory = new SpelAwareProxyProjectionFactory();
        factory.setBeanClassLoader(classLoader);
        factory.setBeanFactory(beanFactory);
        return factory;
    }

    protected RepositoryMetadata getRepositoryMetadata(Class<?> repositoryInterface) {
        return AbstractRepositoryMetadata.getMetadata(repositoryInterface);
    }

    protected RepositoryInformation getRepositoryInformation(RepositoryMetadata metadata, Class<?> customImplementationClass) {
        RepositoryInformationCacheKey cacheKey = new RepositoryInformationCacheKey(metadata, customImplementationClass);
        RepositoryInformation repositoryInformation = this.repositoryInformationCache.get(cacheKey);
        if (repositoryInformation != null) {
            return repositoryInformation;
        }
        Class<?> repositoryBaseClass = this.repositoryBaseClass == null ? getRepositoryBaseClass(metadata) : this.repositoryBaseClass;
        RepositoryInformation repositoryInformation2 = new DefaultRepositoryInformation(metadata, repositoryBaseClass, customImplementationClass);
        this.repositoryInformationCache.put(cacheKey, repositoryInformation2);
        return repositoryInformation2;
    }

    protected List<QueryMethod> getQueryMethods() {
        return this.collectingListener.getQueryMethods();
    }

    protected QueryLookupStrategy getQueryLookupStrategy(QueryLookupStrategy.Key key) {
        return null;
    }

    protected QueryLookupStrategy getQueryLookupStrategy(QueryLookupStrategy.Key key, EvaluationContextProvider evaluationContextProvider) {
        return null;
    }

    private void validate(RepositoryInformation repositoryInformation, Object customImplementation) {
        if (null == customImplementation && repositoryInformation.hasCustomMethod()) {
            throw new IllegalArgumentException(String.format("You have custom methods in %s but not provided a custom implementation!", repositoryInformation.getRepositoryInterface()));
        }
        validate(repositoryInformation);
    }

    protected void validate(RepositoryMetadata repositoryMetadata) {
    }

    protected final <R> R getTargetRepositoryViaReflection(RepositoryInformation repositoryInformation, Object... objArr) throws SecurityException {
        Class<?> repositoryBaseClass = repositoryInformation.getRepositoryBaseClass();
        Constructor<?> constructorFindConstructor = ReflectionUtils.findConstructor(repositoryBaseClass, objArr);
        if (constructorFindConstructor == null) {
            ArrayList arrayList = new ArrayList(objArr.length);
            for (Object obj : objArr) {
                arrayList.add(obj.getClass());
            }
            throw new IllegalStateException(String.format("No suitable constructor found on %s to match the given arguments: %s. Make sure you implement a constructor taking these", repositoryBaseClass, arrayList));
        }
        return (R) BeanUtils.instantiateClass(constructorFindConstructor, objArr);
    }

    private Class<?> getTransactionProxyType() {
        try {
            return ClassUtils.forName("org.springframework.transaction.interceptor.TransactionalProxy", this.classLoader);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/RepositoryFactorySupport$QueryExecutorMethodInterceptor.class */
    public class QueryExecutorMethodInterceptor implements MethodInterceptor {
        private final Map<Method, RepositoryQuery> queries = new ConcurrentHashMap();
        private final Object customImplementation;
        private final RepositoryInformation repositoryInformation;
        private final QueryExecutionResultHandler resultHandler;
        private final Object target;

        public QueryExecutorMethodInterceptor(RepositoryInformation repositoryInformation, Object customImplementation, Object target, ProjectionFactory projectionFactory) {
            Assert.notNull(repositoryInformation, "RepositoryInformation must not be null!");
            Assert.notNull(target, "Target must not be null!");
            this.resultHandler = new QueryExecutionResultHandler();
            this.repositoryInformation = repositoryInformation;
            this.customImplementation = customImplementation;
            this.target = target;
            QueryLookupStrategy lookupStrategy = RepositoryFactorySupport.this.getQueryLookupStrategy(RepositoryFactorySupport.this.queryLookupStrategyKey, RepositoryFactorySupport.this.evaluationContextProvider);
            QueryLookupStrategy lookupStrategy2 = lookupStrategy == null ? RepositoryFactorySupport.this.getQueryLookupStrategy(RepositoryFactorySupport.this.queryLookupStrategyKey) : lookupStrategy;
            Iterable<Method> queryMethods = repositoryInformation.getQueryMethods();
            if (lookupStrategy2 == null) {
                if (queryMethods.iterator().hasNext()) {
                    throw new IllegalStateException("You have defined query method in the repository but you don't have any query lookup strategy defined. The infrastructure apparently does not support query methods!");
                }
                return;
            }
            for (Method method : queryMethods) {
                RepositoryQuery query = lookupStrategy2.resolveQuery(method, repositoryInformation, projectionFactory, RepositoryFactorySupport.this.namedQueries);
                invokeListeners(query);
                this.queries.put(method, query);
            }
        }

        private void invokeListeners(RepositoryQuery query) {
            for (QueryCreationListener listener : RepositoryFactorySupport.this.queryPostProcessors) {
                Class<?> typeArgument = GenericTypeResolver.resolveTypeArgument(listener.getClass(), QueryCreationListener.class);
                if (typeArgument != null && typeArgument.isAssignableFrom(query.getClass())) {
                    listener.onCreation(query);
                }
            }
        }

        @Override // org.aopalliance.intercept.MethodInterceptor
        public Object invoke(MethodInvocation invocation) throws Throwable {
            Object result = doInvoke(invocation);
            return this.resultHandler.postProcessInvocationResult(result, invocation.getMethod());
        }

        private Object doInvoke(MethodInvocation invocation) throws Throwable {
            Method method = invocation.getMethod();
            Object[] arguments = invocation.getArguments();
            if (isCustomMethodInvocation(invocation)) {
                Method actualMethod = this.repositoryInformation.getTargetClassMethod(method);
                return executeMethodOn(this.customImplementation, actualMethod, arguments);
            }
            if (hasQueryFor(method)) {
                return this.queries.get(method).execute(arguments);
            }
            Method actualMethod2 = this.repositoryInformation.getTargetClassMethod(method);
            return executeMethodOn(this.target, actualMethod2, arguments);
        }

        private Object executeMethodOn(Object target, Method method, Object[] parameters) throws Exception {
            try {
                return method.invoke(target, parameters);
            } catch (Exception e) {
                org.springframework.data.repository.util.ClassUtils.unwrapReflectionException(e);
                throw new IllegalStateException("Should not occur!");
            }
        }

        private boolean hasQueryFor(Method method) {
            return this.queries.containsKey(method);
        }

        private boolean isCustomMethodInvocation(MethodInvocation invocation) {
            if (null == this.customImplementation) {
                return false;
            }
            return this.repositoryInformation.isCustomMethod(invocation.getMethod());
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/RepositoryFactorySupport$QueryCollectingQueryCreationListener.class */
    private static class QueryCollectingQueryCreationListener implements QueryCreationListener<RepositoryQuery> {
        private List<QueryMethod> queryMethods;

        private QueryCollectingQueryCreationListener() {
            this.queryMethods = new ArrayList();
        }

        public List<QueryMethod> getQueryMethods() {
            return this.queryMethods;
        }

        @Override // org.springframework.data.repository.core.support.QueryCreationListener
        public void onCreation(RepositoryQuery query) {
            this.queryMethods.add(query.getQueryMethod());
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/RepositoryFactorySupport$RepositoryInformationCacheKey.class */
    private static class RepositoryInformationCacheKey {
        private final String repositoryInterfaceName;
        private final String customImplementationClassName;

        public RepositoryInformationCacheKey(RepositoryMetadata metadata, Class<?> customImplementationType) {
            this.repositoryInterfaceName = metadata.getRepositoryInterface().getName();
            this.customImplementationClassName = customImplementationType == null ? null : customImplementationType.getName();
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof RepositoryInformationCacheKey)) {
                return false;
            }
            RepositoryInformationCacheKey that = (RepositoryInformationCacheKey) obj;
            return this.repositoryInterfaceName.equals(that.repositoryInterfaceName) && ObjectUtils.nullSafeEquals(this.customImplementationClassName, that.customImplementationClassName);
        }

        public int hashCode() {
            int result = 31 + (17 * this.repositoryInterfaceName.hashCode());
            return result + (17 * ObjectUtils.nullSafeHashCode(this.customImplementationClassName));
        }
    }
}
