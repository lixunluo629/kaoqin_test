package org.terracotta.context.query;

import org.terracotta.context.TreeNode;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/query/QueryBuilder.class */
public class QueryBuilder {
    private Query current = NullQuery.INSTANCE;

    private QueryBuilder() {
    }

    public static QueryBuilder queryBuilder() {
        return new QueryBuilder();
    }

    public QueryBuilder filter(Matcher<? super TreeNode> filter) {
        return chain(new Filter(filter));
    }

    public QueryBuilder children() {
        return chain(Children.INSTANCE);
    }

    public QueryBuilder parent() {
        return chain(Parent.INSTANCE);
    }

    public QueryBuilder descendants() {
        return chain(Descendants.INSTANCE);
    }

    public QueryBuilder chain(Query query) {
        this.current = new ChainedQuery(this.current, query);
        return this;
    }

    public QueryBuilder ensureUnique() {
        return chain(EnsureUnique.INSTANCE);
    }

    public QueryBuilder empty() {
        this.current = EmptyQuery.INSTANCE;
        return this;
    }

    public Query build() {
        return this.current;
    }
}
