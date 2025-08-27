package org.terracotta.context;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/RootNode.class */
class RootNode extends AbstractTreeNode {
    private final Collection<ContextListener> listeners = new CopyOnWriteArrayList();

    RootNode() {
    }

    @Override // org.terracotta.context.AbstractTreeNode
    void addedParent(AbstractTreeNode child) {
        throw new IllegalStateException();
    }

    @Override // org.terracotta.context.AbstractTreeNode
    void removedParent(AbstractTreeNode child) {
        throw new IllegalStateException();
    }

    @Override // org.terracotta.context.AbstractTreeNode
    Set<AbstractTreeNode> getAncestors() {
        return Collections.emptySet();
    }

    @Override // org.terracotta.context.AbstractTreeNode
    Collection<ContextListener> getListeners() {
        return Collections.unmodifiableCollection(this.listeners);
    }

    public void addListener(ContextListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(ContextListener listener) {
        this.listeners.remove(listener);
    }

    @Override // org.terracotta.context.TreeNode
    public ContextElement getContext() {
        throw new IllegalStateException();
    }

    @Override // org.terracotta.context.TreeNode
    public Collection<List<? extends TreeNode>> getPaths() {
        return Collections.singleton(Collections.emptyList());
    }
}
