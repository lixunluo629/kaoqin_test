package org.terracotta.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/MutableTreeNode.class */
class MutableTreeNode extends AbstractTreeNode {
    private final CopyOnWriteArraySet<AbstractTreeNode> parents = new CopyOnWriteArraySet<>();
    private final ContextElement context;

    public MutableTreeNode(ContextElement context) {
        this.context = context;
    }

    @Override // org.terracotta.context.TreeNode
    public ContextElement getContext() {
        return this.context;
    }

    public String toString() {
        return "{" + this.context + "}";
    }

    @Override // org.terracotta.context.AbstractTreeNode
    Set<AbstractTreeNode> getAncestors() {
        Set<AbstractTreeNode> ancestors = Collections.newSetFromMap(new IdentityHashMap());
        ancestors.addAll(this.parents);
        Iterator i$ = this.parents.iterator();
        while (i$.hasNext()) {
            AbstractTreeNode parent = i$.next();
            ancestors.addAll(parent.getAncestors());
        }
        return Collections.unmodifiableSet(ancestors);
    }

    @Override // org.terracotta.context.AbstractTreeNode
    public Collection<ContextListener> getListeners() {
        return Collections.emptyList();
    }

    @Override // org.terracotta.context.AbstractTreeNode
    void addedParent(AbstractTreeNode parent) {
        this.parents.add(parent);
    }

    @Override // org.terracotta.context.AbstractTreeNode
    void removedParent(AbstractTreeNode parent) {
        this.parents.remove(parent);
    }

    @Override // org.terracotta.context.TreeNode
    public Collection<List<? extends TreeNode>> getPaths() {
        Collection<List<? extends TreeNode>> paths = new ArrayList<>();
        Iterator i$ = this.parents.iterator();
        while (i$.hasNext()) {
            AbstractTreeNode node = i$.next();
            for (List<? extends TreeNode> path : node.getPaths()) {
                List<TreeNode> newPath = new ArrayList<>(path);
                newPath.add(this);
                paths.add(newPath);
            }
        }
        return paths;
    }
}
