package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.Message;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.MemberImpl;
import org.aspectj.weaver.NameMangler;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.World;
import org.aspectj.weaver.ast.Expr;
import org.aspectj.weaver.ast.Test;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/ConcreteCflowPointcut.class */
public class ConcreteCflowPointcut extends Pointcut {
    private final Member cflowField;
    List<Slot> slots;
    boolean usesCounter;
    ResolvedType aspect;
    private static final Member cflowStackIsValidMethod = MemberImpl.method(NameMangler.CFLOW_STACK_UNRESOLVEDTYPE, 0, UnresolvedType.BOOLEAN, "isValid", UnresolvedType.NONE);
    private static final Member cflowCounterIsValidMethod = MemberImpl.method(NameMangler.CFLOW_COUNTER_UNRESOLVEDTYPE, 0, UnresolvedType.BOOLEAN, "isValid", UnresolvedType.NONE);

    public ConcreteCflowPointcut(ResolvedType aspect, Member cflowField, List<Slot> slots, boolean usesCounter) {
        this.aspect = aspect;
        this.cflowField = cflowField;
        this.slots = slots;
        this.usesCounter = usesCounter;
        this.pointcutKind = (byte) 10;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public int couldMatchKinds() {
        return Shadow.ALL_SHADOW_KINDS_BITS;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public FuzzyBoolean fastMatch(FastMatchInfo type) {
        return FuzzyBoolean.MAYBE;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected FuzzyBoolean matchInternal(Shadow shadow) throws AbortException {
        if (this.slots != null) {
            for (Slot slot : this.slots) {
                ResolvedType rt = slot.formalType;
                if (rt.isMissing()) {
                    ISourceLocation[] locs = {getSourceLocation()};
                    Message m = new Message(WeaverMessages.format(WeaverMessages.MISSING_TYPE_PREVENTS_MATCH, rt.getName()), "", Message.WARNING, shadow.getSourceLocation(), null, locs);
                    rt.getWorld().getMessageHandler().handleMessage(m);
                    return FuzzyBoolean.NO;
                }
            }
        }
        return FuzzyBoolean.MAYBE;
    }

    public int[] getUsedFormalSlots() {
        if (this.slots == null) {
            return new int[0];
        }
        int[] indices = new int[this.slots.size()];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = this.slots.get(i).formalIndex;
        }
        return indices;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        throw new RuntimeException("unimplemented");
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public void resolveBindings(IScope scope, Bindings bindings) {
        throw new RuntimeException("unimplemented");
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Pointcut parameterizeWith(Map<String, UnresolvedType> typeVariableMap, World w) {
        throw new RuntimeException("unimplemented");
    }

    public boolean equals(Object other) {
        if (!(other instanceof ConcreteCflowPointcut)) {
            return false;
        }
        ConcreteCflowPointcut o = (ConcreteCflowPointcut) other;
        return o.cflowField.equals(this.cflowField);
    }

    public int hashCode() {
        int result = (37 * 17) + this.cflowField.hashCode();
        return result;
    }

    public String toString() {
        return "concretecflow(" + this.cflowField + ")";
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected Test findResidueInternal(Shadow shadow, ExposedState state) {
        if (this.usesCounter) {
            return Test.makeFieldGetCall(this.cflowField, cflowCounterIsValidMethod, Expr.NONE);
        }
        if (this.slots != null) {
            for (Slot slot : this.slots) {
                state.set(slot.formalIndex, this.aspect.getWorld().getWeavingSupport().makeCflowAccessVar(slot.formalType, this.cflowField, slot.arrayIndex));
            }
        }
        return Test.makeFieldGetCall(this.cflowField, cflowStackIsValidMethod, Expr.NONE);
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Pointcut concretize1(ResolvedType inAspect, ResolvedType declaringType, IntMap bindings) {
        throw new RuntimeException("unimplemented");
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/ConcreteCflowPointcut$Slot.class */
    public static class Slot {
        int formalIndex;
        ResolvedType formalType;
        int arrayIndex;

        public Slot(int formalIndex, ResolvedType formalType, int arrayIndex) {
            this.formalIndex = formalIndex;
            this.formalType = formalType;
            this.arrayIndex = arrayIndex;
        }

        public boolean equals(Object other) {
            if (!(other instanceof Slot)) {
                return false;
            }
            Slot o = (Slot) other;
            return o.formalIndex == this.formalIndex && o.arrayIndex == this.arrayIndex && o.formalType.equals(this.formalType);
        }

        public int hashCode() {
            int result = (37 * 19) + this.formalIndex;
            return (37 * ((37 * result) + this.arrayIndex)) + this.formalType.hashCode();
        }

        public String toString() {
            return "Slot(" + this.formalIndex + ", " + this.formalType + ", " + this.arrayIndex + ")";
        }
    }
}
