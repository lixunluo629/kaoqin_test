package org.terracotta.context.query;

import java.util.Set;
import org.terracotta.context.TreeNode;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/query/EnsureUnique.class */
class EnsureUnique implements Query {
    static Query INSTANCE = new EnsureUnique();

    private EnsureUnique() {
    }

    @Override // org.terracotta.context.query.Query
    public Set<TreeNode> execute(Set<TreeNode> input) {
        if (input.size() == 1) {
            return input;
        }
        throw new IllegalStateException("Expected a uniquely identified node: found " + input.size());
    }
}
