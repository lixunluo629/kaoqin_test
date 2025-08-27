package org.springframework.data.keyvalue.repository.support;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.springframework.beans.BeanUtils;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.keyvalue.repository.query.KeyValuePartTreeQuery;
import org.springframework.data.keyvalue.repository.query.SpelQueryCreator;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.QueryDslUtils;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.PersistentEntityInformation;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/repository/support/KeyValueRepositoryFactory.class */
public class KeyValueRepositoryFactory extends RepositoryFactorySupport {
    private static final Class<SpelQueryCreator> DEFAULT_QUERY_CREATOR = SpelQueryCreator.class;
    private final KeyValueOperations keyValueOperations;
    private final MappingContext<?, ?> context;
    private final Class<? extends AbstractQueryCreator<?, ?>> queryCreator;
    private final Class<? extends RepositoryQuery> repositoryQueryType;

    public KeyValueRepositoryFactory(KeyValueOperations keyValueOperations) {
        this(keyValueOperations, DEFAULT_QUERY_CREATOR);
    }

    public KeyValueRepositoryFactory(KeyValueOperations keyValueOperations, Class<? extends AbstractQueryCreator<?, ?>> queryCreator) {
        this(keyValueOperations, queryCreator, KeyValuePartTreeQuery.class);
    }

    public KeyValueRepositoryFactory(KeyValueOperations keyValueOperations, Class<? extends AbstractQueryCreator<?, ?>> queryCreator, Class<? extends RepositoryQuery> repositoryQueryType) {
        Assert.notNull(keyValueOperations, "KeyValueOperations must not be null!");
        Assert.notNull(queryCreator, "Query creator type must not be null!");
        Assert.notNull(repositoryQueryType, "RepositoryQueryType type must not be null!");
        this.queryCreator = queryCreator;
        this.keyValueOperations = keyValueOperations;
        this.context = keyValueOperations.getMappingContext();
        this.repositoryQueryType = repositoryQueryType;
    }

    @Override // org.springframework.data.repository.core.support.RepositoryFactorySupport
    public <T, ID extends Serializable> EntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
        PersistentEntity<T, ?> entity = this.context.getPersistentEntity((Class<?>) domainClass);
        PersistentEntityInformation<T, ID> entityInformation = new PersistentEntityInformation<>(entity);
        return entityInformation;
    }

    @Override // org.springframework.data.repository.core.support.RepositoryFactorySupport
    protected Object getTargetRepository(RepositoryInformation repositoryInformation) {
        EntityInformation<?, Serializable> entityInformation = getEntityInformation(repositoryInformation.getDomainType());
        return super.getTargetRepositoryViaReflection(repositoryInformation, entityInformation, this.keyValueOperations);
    }

    @Override // org.springframework.data.repository.core.support.RepositoryFactorySupport
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return isQueryDslRepository(metadata.getRepositoryInterface()) ? QuerydslKeyValueRepository.class : SimpleKeyValueRepository.class;
    }

    private static boolean isQueryDslRepository(Class<?> repositoryInterface) {
        return QueryDslUtils.QUERY_DSL_PRESENT && QueryDslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
    }

    @Override // org.springframework.data.repository.core.support.RepositoryFactorySupport
    protected QueryLookupStrategy getQueryLookupStrategy(QueryLookupStrategy.Key key, EvaluationContextProvider evaluationContextProvider) {
        return new KeyValueQueryLookupStrategy(key, evaluationContextProvider, this.keyValueOperations, this.queryCreator, this.repositoryQueryType);
    }

    /* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/repository/support/KeyValueRepositoryFactory$KeyValueQueryLookupStrategy.class */
    private static class KeyValueQueryLookupStrategy implements QueryLookupStrategy {
        private EvaluationContextProvider evaluationContextProvider;
        private KeyValueOperations keyValueOperations;
        private Class<? extends AbstractQueryCreator<?, ?>> queryCreator;
        private Class<? extends RepositoryQuery> repositoryQueryType;

        public KeyValueQueryLookupStrategy(QueryLookupStrategy.Key key, EvaluationContextProvider evaluationContextProvider, KeyValueOperations keyValueOperations, Class<? extends AbstractQueryCreator<?, ?>> queryCreator) {
            this(key, evaluationContextProvider, keyValueOperations, queryCreator, KeyValuePartTreeQuery.class);
        }

        public KeyValueQueryLookupStrategy(QueryLookupStrategy.Key key, EvaluationContextProvider evaluationContextProvider, KeyValueOperations keyValueOperations, Class<? extends AbstractQueryCreator<?, ?>> queryCreator, Class<? extends RepositoryQuery> repositoryQueryType) {
            Assert.notNull(evaluationContextProvider, "EvaluationContextProvider must not be null!");
            Assert.notNull(keyValueOperations, "KeyValueOperations must not be null!");
            Assert.notNull(queryCreator, "Query creator type must not be null!");
            Assert.notNull(repositoryQueryType, "RepositoryQueryType type must not be null!");
            this.evaluationContextProvider = evaluationContextProvider;
            this.keyValueOperations = keyValueOperations;
            this.queryCreator = queryCreator;
            this.repositoryQueryType = repositoryQueryType;
        }

        @Override // org.springframework.data.repository.query.QueryLookupStrategy
        public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory, NamedQueries namedQueries) {
            QueryMethod queryMethod = new QueryMethod(method, metadata, factory);
            Constructor<? extends KeyValuePartTreeQuery> constructor = ClassUtils.getConstructorIfAvailable(this.repositoryQueryType, QueryMethod.class, EvaluationContextProvider.class, KeyValueOperations.class, Class.class);
            return (RepositoryQuery) BeanUtils.instantiateClass(constructor, queryMethod, this.evaluationContextProvider, this.keyValueOperations, this.queryCreator);
        }
    }
}
