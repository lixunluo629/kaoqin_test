package org.terracotta.context.query;

import java.util.Set;
import org.terracotta.context.TreeNode;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/query/NullQuery.class */
class NullQuery implements Query {
    static final Query INSTANCE = new NullQuery();

    private NullQuery() {
    }

    @Override // org.terracotta.context.query.Query
    public Set<TreeNode> execute(Set<TreeNode> input) {
        return input;
    }

    public String toString() {
        return "";
    }
}
