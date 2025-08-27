package org.springframework.data.keyvalue.core;

import java.io.Serializable;
import java.util.Collection;
import org.springframework.data.keyvalue.core.KeyValueAdapter;
import org.springframework.data.keyvalue.core.query.KeyValueQuery;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/QueryEngine.class */
public abstract class QueryEngine<ADAPTER extends KeyValueAdapter, CRITERIA, SORT> {
    private final CriteriaAccessor<CRITERIA> criteriaAccessor;
    private final SortAccessor<SORT> sortAccessor;
    private ADAPTER adapter;

    public abstract Collection<?> execute(CRITERIA criteria, SORT sort, int i, int i2, Serializable serializable);

    public abstract long count(CRITERIA criteria, Serializable serializable);

    public QueryEngine(CriteriaAccessor<CRITERIA> criteriaAccessor, SortAccessor<SORT> sortAccessor) {
        this.criteriaAccessor = criteriaAccessor;
        this.sortAccessor = sortAccessor;
    }

    public Collection<?> execute(KeyValueQuery<?> query, Serializable keyspace) {
        CRITERIA criteria = this.criteriaAccessor != null ? this.criteriaAccessor.resolve(query) : null;
        SORT sort = this.sortAccessor != null ? this.sortAccessor.resolve(query) : null;
        return execute(criteria, sort, query.getOffset(), query.getRows(), keyspace);
    }

    public <T> Collection<T> execute(KeyValueQuery<?> query, Serializable keyspace, Class<T> type) {
        CRITERIA criteria = this.criteriaAccessor != null ? this.criteriaAccessor.resolve(query) : null;
        SORT sort = this.sortAccessor != null ? this.sortAccessor.resolve(query) : null;
        return execute(criteria, sort, query.getOffset(), query.getRows(), keyspace, type);
    }

    public long count(KeyValueQuery<?> query, Serializable keyspace) {
        CRITERIA criteria = this.criteriaAccessor != null ? this.criteriaAccessor.resolve(query) : null;
        return count((QueryEngine<ADAPTER, CRITERIA, SORT>) criteria, keyspace);
    }

    public <T> Collection<T> execute(CRITERIA criteria, SORT sort, int i, int i2, Serializable serializable, Class<T> cls) {
        return (Collection<T>) execute(criteria, sort, i, i2, serializable);
    }

    protected ADAPTER getAdapter() {
        return this.adapter;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void registerAdapter(KeyValueAdapter keyValueAdapter) {
        if (this.adapter == null) {
            this.adapter = keyValueAdapter;
            return;
        }
        throw new IllegalArgumentException("Cannot register more than one adapter for this QueryEngine.");
    }
}
