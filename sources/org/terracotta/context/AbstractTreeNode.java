package org.terracotta.context;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/AbstractTreeNode.class */
abstract class AbstractTreeNode implements TreeNode {
    private final CopyOnWriteArraySet<AbstractTreeNode> children = new CopyOnWriteArraySet<>();

    abstract void addedParent(AbstractTreeNode abstractTreeNode);

    abstract void removedParent(AbstractTreeNode abstractTreeNode);

    abstract Set<AbstractTreeNode> getAncestors();

    abstract Collection<ContextListener> getListeners();

    AbstractTreeNode() {
    }

    public boolean addChild(AbstractTreeNode child) {
        synchronized (this) {
            Collection<AbstractTreeNode> ancestors = new HashSet<>(getAncestors());
            ancestors.removeAll(child.getAncestors());
            if (this.children.add(child)) {
                child.addedParent(this);
                for (AbstractTreeNode ancestor : ancestors) {
                    for (ContextListener listener : ancestor.getListeners()) {
                        listener.graphAdded(this, child);
                    }
                }
                return true;
            }
            return false;
        }
    }

    public boolean removeChild(AbstractTreeNode child) {
        synchronized (this) {
            if (this.children.remove(child)) {
                child.removedParent(this);
                Collection<AbstractTreeNode> ancestors = new HashSet<>(getAncestors());
                ancestors.removeAll(child.getAncestors());
                for (AbstractTreeNode ancestor : ancestors) {
                    for (ContextListener listener : ancestor.getListeners()) {
                        listener.graphRemoved(this, child);
                    }
                }
                return true;
            }
            return false;
        }
    }

    @Override // org.terracotta.context.TreeNode
    public Set<? extends AbstractTreeNode> getChildren() {
        return Collections.unmodifiableSet(this.children);
    }

    @Override // org.terracotta.context.TreeNode
    public List<? extends TreeNode> getPath() {
        Collection<List<? extends TreeNode>> paths = getPaths();
        if (paths.size() == 1) {
            return paths.iterator().next();
        }
        throw new IllegalStateException("No unique path to root");
    }

    @Override // org.terracotta.context.TreeNode
    public String toTreeString() {
        return dumpSubtree(0, this);
    }

    public static String dumpSubtree(int indent, TreeNode node) {
        char[] indentChars = new char[indent];
        Arrays.fill(indentChars, ' ');
        StringBuilder sb = new StringBuilder();
        String nodeString = node.toString();
        sb.append(indentChars).append(nodeString).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        for (TreeNode child : node.getChildren()) {
            sb.append(dumpSubtree(indent + 2, child));
        }
        return sb.toString();
    }

    @Override // org.terracotta.context.WeakIdentityHashMap.Cleanable
    public void clean() {
        for (AbstractTreeNode child : getChildren()) {
            removeChild(child);
        }
        for (AbstractTreeNode parent : getAncestors()) {
            parent.removeChild(this);
        }
    }
}
