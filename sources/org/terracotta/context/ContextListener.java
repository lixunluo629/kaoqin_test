package org.terracotta.context;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/ContextListener.class */
public interface ContextListener {
    void graphAdded(TreeNode treeNode, TreeNode treeNode2);

    void graphRemoved(TreeNode treeNode, TreeNode treeNode2);
}
