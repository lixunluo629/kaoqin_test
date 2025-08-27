package org.terracotta.context.query;

import java.util.HashSet;
import java.util.Set;
import org.terracotta.context.TreeNode;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/query/Descendants.class */
class Descendants implements Query {
    static final Query INSTANCE = new Descendants();

    Descendants() {
    }

    @Override // org.terracotta.context.query.Query
    public Set<TreeNode> execute(Set<TreeNode> input) {
        Set<TreeNode> descendants = new HashSet<>();
        Set<TreeNode> setExecute = Children.INSTANCE.execute(input);
        while (true) {
            Set<TreeNode> children = setExecute;
            if (children.isEmpty() || !descendants.addAll(children)) {
                break;
            }
            setExecute = Children.INSTANCE.execute(children);
        }
        return descendants;
    }

    public String toString() {
        return "descendants";
    }
}
