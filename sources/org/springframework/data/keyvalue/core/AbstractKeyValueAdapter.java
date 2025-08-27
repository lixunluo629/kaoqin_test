package org.springframework.data.keyvalue.core;

import java.io.Serializable;
import java.util.Collection;
import org.springframework.data.keyvalue.core.query.KeyValueQuery;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/AbstractKeyValueAdapter.class */
public abstract class AbstractKeyValueAdapter implements KeyValueAdapter {
    private final QueryEngine<? extends KeyValueAdapter, ?, ?> engine;

    @Override // org.springframework.data.keyvalue.core.KeyValueAdapter
    public /* bridge */ /* synthetic */ Iterable find(KeyValueQuery keyValueQuery, Serializable serializable) {
        return find((KeyValueQuery<?>) keyValueQuery, serializable);
    }

    protected AbstractKeyValueAdapter() {
        this(null);
    }

    protected AbstractKeyValueAdapter(QueryEngine<? extends KeyValueAdapter, ?, ?> engine) {
        this.engine = engine != null ? engine : new SpelQueryEngine<>();
        this.engine.registerAdapter(this);
    }

    protected QueryEngine<? extends KeyValueAdapter, ?, ?> getQueryEngine() {
        return this.engine;
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueAdapter
    public <T> T get(Serializable serializable, Serializable serializable2, Class<T> cls) {
        return (T) get(serializable, serializable2);
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueAdapter
    public <T> T delete(Serializable serializable, Serializable serializable2, Class<T> cls) {
        return (T) delete(serializable, serializable2);
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueAdapter
    public <T> Iterable<T> find(KeyValueQuery<?> query, Serializable keyspace, Class<T> type) {
        return this.engine.execute(query, keyspace, type);
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueAdapter
    public Collection<?> find(KeyValueQuery<?> query, Serializable keyspace) {
        return this.engine.execute(query, keyspace);
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueAdapter
    public long count(KeyValueQuery<?> query, Serializable keyspace) {
        return this.engine.count(query, keyspace);
    }
}
