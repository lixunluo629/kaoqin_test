package org.terracotta.context;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.terracotta.context.WeakIdentityHashMap;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/TreeNode.class */
public interface TreeNode extends WeakIdentityHashMap.Cleanable {
    Set<? extends TreeNode> getChildren();

    List<? extends TreeNode> getPath() throws IllegalStateException;

    Collection<List<? extends TreeNode>> getPaths();

    ContextElement getContext();

    String toTreeString();
}
