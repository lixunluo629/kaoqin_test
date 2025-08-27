package org.terracotta.context.query;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/query/Queries.class */
public abstract class Queries {
    private Queries() {
    }

    public static Query self() {
        return QueryBuilder.queryBuilder().build();
    }

    public static Query children() {
        return QueryBuilder.queryBuilder().children().build();
    }

    public static Query descendants() {
        return QueryBuilder.queryBuilder().descendants().build();
    }
}
