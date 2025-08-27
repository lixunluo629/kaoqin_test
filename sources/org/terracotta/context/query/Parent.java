package org.terracotta.context.query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.terracotta.context.TreeNode;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/query/Parent.class */
class Parent implements Query {
    static final Query INSTANCE = new Parent();

    Parent() {
    }

    @Override // org.terracotta.context.query.Query
    public Set<TreeNode> execute(Set<TreeNode> input) throws IllegalStateException {
        Set<TreeNode> output = new HashSet<>();
        for (TreeNode node : input) {
            List<? extends TreeNode> pathes = node.getPath();
            if (pathes.size() > 1) {
                output.add(pathes.get(pathes.size() - 2));
            }
        }
        return output;
    }

    public String toString() {
        return "parent";
    }
}
