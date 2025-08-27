package org.springframework.data.keyvalue.core.query;

import org.springframework.data.domain.Sort;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/query/KeyValueQuery.class */
public class KeyValueQuery<T> {
    private Sort sort;
    private int offset = -1;
    private int rows = -1;
    private T criteria;

    public KeyValueQuery() {
    }

    public KeyValueQuery(T criteria) {
        this.criteria = criteria;
    }

    public KeyValueQuery(Sort sort) {
        this.sort = sort;
    }

    @Deprecated
    public T getCritieria() {
        return this.criteria;
    }

    public T getCriteria() {
        return this.criteria;
    }

    public Sort getSort() {
        return this.sort;
    }

    public int getOffset() {
        return this.offset;
    }

    public int getRows() {
        return this.rows;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public KeyValueQuery<T> orderBy(Sort sort) {
        if (sort == null) {
            return this;
        }
        if (this.sort != null) {
            this.sort.and(sort);
        } else {
            this.sort = sort;
        }
        return this;
    }

    public KeyValueQuery<T> skip(int offset) {
        setOffset(offset);
        return this;
    }

    public KeyValueQuery<T> limit(int rows) {
        setRows(rows);
        return this;
    }
}
