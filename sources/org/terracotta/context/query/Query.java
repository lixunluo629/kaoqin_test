package org.terracotta.context.query;

import java.util.Set;
import org.terracotta.context.TreeNode;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/query/Query.class */
public interface Query {
    Set<TreeNode> execute(Set<TreeNode> set);
}
