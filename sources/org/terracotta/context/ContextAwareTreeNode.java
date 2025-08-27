package org.terracotta.context;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.terracotta.statistics.StatisticsManager;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/ContextAwareTreeNode.class */
class ContextAwareTreeNode implements TreeNode {
    private Object context;
    private TreeNode wrappedNode;

    public ContextAwareTreeNode(TreeNode node, Object context) {
        this.context = context;
        this.wrappedNode = node;
    }

    @Override // org.terracotta.context.TreeNode
    public Set<? extends TreeNode> getChildren() {
        return this.wrappedNode.getChildren();
    }

    @Override // org.terracotta.context.TreeNode
    public List<? extends TreeNode> getPath() throws IllegalStateException {
        return this.wrappedNode.getPath();
    }

    @Override // org.terracotta.context.TreeNode
    public Collection<List<? extends TreeNode>> getPaths() {
        return this.wrappedNode.getPaths();
    }

    @Override // org.terracotta.context.TreeNode
    public ContextElement getContext() {
        return this.wrappedNode.getContext();
    }

    @Override // org.terracotta.context.TreeNode
    public String toTreeString() {
        return this.wrappedNode.toTreeString();
    }

    @Override // org.terracotta.context.WeakIdentityHashMap.Cleanable
    public void clean() {
        this.wrappedNode.clean();
        StatisticsManager.removePassThroughStatistics(this.context);
    }
}
