package org.terracotta.context.query;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.terracotta.context.TreeNode;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/query/Filter.class */
class Filter implements Query {
    private final Matcher<? super TreeNode> filter;

    public Filter(Matcher<? super TreeNode> filter) {
        if (filter == null) {
            throw new NullPointerException("Cannot filter using a null matcher");
        }
        this.filter = filter;
    }

    @Override // org.terracotta.context.query.Query
    public Set<TreeNode> execute(Set<TreeNode> input) {
        Set<TreeNode> output = new HashSet<>(input);
        Iterator<TreeNode> it = output.iterator();
        while (it.hasNext()) {
            if (!this.filter.matches(it.next())) {
                it.remove();
            }
        }
        return output;
    }

    public String toString() {
        return "filter for nodes with " + this.filter;
    }
}
