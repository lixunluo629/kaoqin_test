package org.terracotta.context.query;

import java.util.Collections;
import java.util.Set;
import org.terracotta.context.TreeNode;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/query/EmptyQuery.class */
class EmptyQuery implements Query {
    static final Query INSTANCE = new EmptyQuery();

    private EmptyQuery() {
    }

    @Override // org.terracotta.context.query.Query
    public Set<TreeNode> execute(Set<TreeNode> input) {
        return Collections.emptySet();
    }

    public String toString() {
        return "<empty>";
    }
}
