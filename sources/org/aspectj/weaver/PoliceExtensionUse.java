package org.aspectj.weaver;

import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.weaver.patterns.AbstractPatternNodeVisitor;
import org.aspectj.weaver.patterns.AndPointcut;
import org.aspectj.weaver.patterns.KindedPointcut;
import org.aspectj.weaver.patterns.NotPointcut;
import org.aspectj.weaver.patterns.OrPointcut;
import org.aspectj.weaver.patterns.Pointcut;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/PoliceExtensionUse.class */
public class PoliceExtensionUse extends AbstractPatternNodeVisitor {
    private boolean synchronizationDesignatorEncountered = false;
    private World world;
    private Pointcut p;

    public PoliceExtensionUse(World w, Pointcut p) {
        this.world = w;
        this.p = p;
    }

    public boolean synchronizationDesignatorEncountered() {
        return this.synchronizationDesignatorEncountered;
    }

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(KindedPointcut node, Object data) throws AbortException {
        if (this.world == null) {
            return super.visit(node, data);
        }
        if (node.getKind() == Shadow.SynchronizationLock || node.getKind() == Shadow.SynchronizationUnlock) {
            this.synchronizationDesignatorEncountered = true;
        }
        if (!this.world.isJoinpointSynchronizationEnabled()) {
            if (node.getKind() == Shadow.SynchronizationLock) {
                IMessage m = MessageUtil.warn("lock() pointcut designator cannot be used without the option -Xjoinpoints:synchronization", this.p.getSourceLocation());
                this.world.getMessageHandler().handleMessage(m);
            } else if (node.getKind() == Shadow.SynchronizationUnlock) {
                IMessage m2 = MessageUtil.warn("unlock() pointcut designator cannot be used without the option -Xjoinpoints:synchronization", this.p.getSourceLocation());
                this.world.getMessageHandler().handleMessage(m2);
            }
        }
        return super.visit(node, data);
    }

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(AndPointcut node, Object data) {
        node.getLeft().accept(this, data);
        node.getRight().accept(this, data);
        return node;
    }

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(NotPointcut node, Object data) {
        node.getNegatedPointcut().accept(this, data);
        return node;
    }

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(OrPointcut node, Object data) {
        node.getLeft().accept(this, data);
        node.getRight().accept(this, data);
        return node;
    }
}
