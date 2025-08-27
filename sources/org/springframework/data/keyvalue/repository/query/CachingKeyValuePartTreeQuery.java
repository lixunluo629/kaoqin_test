package org.springframework.data.keyvalue.repository.query;

import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.keyvalue.core.query.KeyValueQuery;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/repository/query/CachingKeyValuePartTreeQuery.class */
public class CachingKeyValuePartTreeQuery extends KeyValuePartTreeQuery {
    private KeyValueQuery<?> cachedQuery;

    public CachingKeyValuePartTreeQuery(QueryMethod queryMethod, EvaluationContextProvider evaluationContextProvider, KeyValueOperations keyValueOperations, Class<? extends AbstractQueryCreator<?, ?>> queryCreator) {
        super(queryMethod, evaluationContextProvider, keyValueOperations, queryCreator);
    }

    @Override // org.springframework.data.keyvalue.repository.query.KeyValuePartTreeQuery
    protected KeyValueQuery<?> prepareQuery(Object[] parameters) {
        if (this.cachedQuery == null) {
            this.cachedQuery = super.prepareQuery(parameters);
        }
        return prepareQuery(this.cachedQuery, parameters);
    }
}
