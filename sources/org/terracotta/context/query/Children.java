package org.terracotta.context.query;

import java.util.HashSet;
import java.util.Set;
import org.terracotta.context.TreeNode;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/query/Children.class */
class Children implements Query {
    static final Query INSTANCE = new Children();

    Children() {
    }

    @Override // org.terracotta.context.query.Query
    public Set<TreeNode> execute(Set<TreeNode> input) {
        Set<TreeNode> output = new HashSet<>();
        for (TreeNode node : input) {
            output.addAll(node.getChildren());
        }
        return output;
    }

    public String toString() {
        return "children";
    }
}
