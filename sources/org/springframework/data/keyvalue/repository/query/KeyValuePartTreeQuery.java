package org.springframework.data.keyvalue.repository.query;

import java.lang.reflect.Constructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.keyvalue.core.IterableConverter;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.keyvalue.core.SpelCriteria;
import org.springframework.data.keyvalue.core.query.KeyValueQuery;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.ResultProcessor;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;
import org.springframework.data.repository.query.parser.PartTree;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/repository/query/KeyValuePartTreeQuery.class */
public class KeyValuePartTreeQuery implements RepositoryQuery {
    private final EvaluationContextProvider evaluationContextProvider;
    private final QueryMethod queryMethod;
    private final KeyValueOperations keyValueOperations;
    private final Class<? extends AbstractQueryCreator<?, ?>> queryCreator;

    public KeyValuePartTreeQuery(QueryMethod queryMethod, EvaluationContextProvider evaluationContextProvider, KeyValueOperations keyValueOperations, Class<? extends AbstractQueryCreator<?, ?>> queryCreator) {
        Assert.notNull(queryMethod, "Query method must not be null!");
        Assert.notNull(evaluationContextProvider, "EvaluationContextprovider must not be null!");
        Assert.notNull(keyValueOperations, "KeyValueOperations must not be null!");
        Assert.notNull(queryCreator, "QueryCreator type must not be null!");
        this.queryMethod = queryMethod;
        this.keyValueOperations = keyValueOperations;
        this.evaluationContextProvider = evaluationContextProvider;
        this.queryCreator = queryCreator;
    }

    @Override // org.springframework.data.repository.query.RepositoryQuery
    public Object execute(Object[] parameters) {
        ParameterAccessor accessor = new ParametersParameterAccessor(getQueryMethod().getParameters(), parameters);
        KeyValueQuery<?> query = prepareQuery(parameters);
        ResultProcessor processor = this.queryMethod.getResultProcessor().withDynamicProjection(accessor);
        return processor.processResult(doExecute(parameters, query));
    }

    protected Object doExecute(Object[] parameters, KeyValueQuery<?> query) {
        if (this.queryMethod.isPageQuery() || this.queryMethod.isSliceQuery()) {
            Pageable page = (Pageable) parameters[this.queryMethod.getParameters().getPageableIndex()];
            query.setOffset(page.getOffset());
            query.setRows(page.getPageSize());
            Iterable<?> result = this.keyValueOperations.find(query, this.queryMethod.getEntityInformation().getJavaType());
            long count = this.queryMethod.isSliceQuery() ? 0L : this.keyValueOperations.count(query, this.queryMethod.getEntityInformation().getJavaType());
            return new PageImpl(IterableConverter.toList(result), page, count);
        }
        if (this.queryMethod.isCollectionQuery()) {
            return this.keyValueOperations.find(query, this.queryMethod.getEntityInformation().getJavaType());
        }
        if (this.queryMethod.isQueryForEntity()) {
            Iterable<?> result2 = this.keyValueOperations.find(query, this.queryMethod.getEntityInformation().getJavaType());
            if (result2.iterator().hasNext()) {
                return result2.iterator().next();
            }
            return null;
        }
        throw new UnsupportedOperationException("Query method not supported.");
    }

    protected KeyValueQuery<?> prepareQuery(Object[] parameters) {
        return prepareQuery(createQuery(new ParametersParameterAccessor(getQueryMethod().getParameters(), parameters)), parameters);
    }

    protected KeyValueQuery<?> prepareQuery(KeyValueQuery<?> instance, Object[] parameters) {
        ParametersParameterAccessor accessor = new ParametersParameterAccessor(getQueryMethod().getParameters(), parameters);
        Object criteria = instance.getCriteria();
        if ((criteria instanceof SpelCriteria) || (criteria instanceof SpelExpression)) {
            SpelExpression spelExpression = getSpelExpression(criteria);
            EvaluationContext context = this.evaluationContextProvider.getEvaluationContext(getQueryMethod().getParameters(), parameters);
            criteria = new SpelCriteria(spelExpression, context);
        }
        KeyValueQuery<?> query = new KeyValueQuery<>(criteria);
        Pageable pageable = accessor.getPageable();
        Sort sort = accessor.getSort();
        query.setOffset(pageable == null ? -1 : pageable.getOffset());
        if (pageable != null) {
            query.setRows(pageable.getPageSize());
        } else if (instance.getRows() >= 0) {
            query.setRows(instance.getRows());
        }
        query.setSort(sort == null ? instance.getSort() : sort);
        return query;
    }

    private SpelExpression getSpelExpression(Object criteria) {
        if (criteria instanceof SpelExpression) {
            return (SpelExpression) criteria;
        }
        if (criteria instanceof SpelCriteria) {
            return getSpelExpression(((SpelCriteria) criteria).getExpression());
        }
        throw new IllegalStateException(String.format("Cannot retrieve SpelExpression from %s", criteria));
    }

    public KeyValueQuery<?> createQuery(ParameterAccessor accessor) {
        PartTree tree = new PartTree(getQueryMethod().getName(), getQueryMethod().getEntityInformation().getJavaType());
        Constructor<? extends AbstractQueryCreator<?, ?>> constructor = ClassUtils.getConstructorIfAvailable(this.queryCreator, PartTree.class, ParameterAccessor.class);
        KeyValueQuery<?> query = (KeyValueQuery) ((AbstractQueryCreator) BeanUtils.instantiateClass(constructor, tree, accessor)).createQuery();
        if (tree.isLimiting()) {
            query.setRows(tree.getMaxResults().intValue());
        }
        return query;
    }

    @Override // org.springframework.data.repository.query.RepositoryQuery
    public QueryMethod getQueryMethod() {
        return this.queryMethod;
    }
}
