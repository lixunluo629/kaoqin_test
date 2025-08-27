package org.aspectj.asm;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/asm/HierarchyWalker.class */
public abstract class HierarchyWalker {
    protected void preProcess(IProgramElement node) {
    }

    protected void postProcess(IProgramElement node) {
    }

    public IProgramElement process(IProgramElement node) {
        preProcess(node);
        node.walk(this);
        postProcess(node);
        return node;
    }
}
