package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.Advice;
import org.aspectj.weaver.AjcMemberMaker;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.CrosscuttingMembers;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.NameMangler;
import org.aspectj.weaver.ResolvedMemberImpl;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.ShadowMunger;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.World;
import org.aspectj.weaver.ast.Expr;
import org.aspectj.weaver.ast.Test;
import org.aspectj.weaver.patterns.PerClause;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/PerCflow.class */
public class PerCflow extends PerClause {
    private final boolean isBelow;
    private final Pointcut entry;

    public PerCflow(Pointcut entry, boolean isBelow) {
        this.entry = entry;
        this.isBelow = isBelow;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
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
    protected FuzzyBoolean matchInternal(Shadow shadow) {
        return FuzzyBoolean.YES;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public void resolveBindings(IScope scope, Bindings bindings) {
        this.entry.resolve(scope);
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Pointcut parameterizeWith(Map<String, UnresolvedType> typeVariableMap, World w) {
        PerCflow ret = new PerCflow(this.entry.parameterizeWith(typeVariableMap, w), this.isBelow);
        ret.copyLocationFrom(this);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected Test findResidueInternal(Shadow shadow, ExposedState state) {
        Expr myInstance = Expr.makeCallExpr(AjcMemberMaker.perCflowAspectOfMethod(this.inAspect), Expr.NONE, this.inAspect);
        state.setAspectInstance(myInstance);
        return Test.makeCall(AjcMemberMaker.perCflowHasAspectMethod(this.inAspect), Expr.NONE);
    }

    @Override // org.aspectj.weaver.patterns.PerClause
    public PerClause concretize(ResolvedType inAspect) {
        PerCflow ret = new PerCflow(this.entry, this.isBelow);
        ret.inAspect = inAspect;
        if (inAspect.isAbstract()) {
            return ret;
        }
        Member cflowStackField = new ResolvedMemberImpl(Member.FIELD, inAspect, 25, UnresolvedType.forName(NameMangler.CFLOW_STACK_TYPE), NameMangler.PERCFLOW_FIELD_NAME, UnresolvedType.NONE);
        World world = inAspect.getWorld();
        CrosscuttingMembers xcut = inAspect.crosscuttingMembers;
        Collection<?> previousCflowEntries = xcut.getCflowEntries();
        Pointcut concreteEntry = this.entry.concretize(inAspect, inAspect, 0, null);
        List<ShadowMunger> innerCflowEntries = new ArrayList<>(xcut.getCflowEntries());
        innerCflowEntries.removeAll(previousCflowEntries);
        xcut.addConcreteShadowMunger(Advice.makePerCflowEntry(world, concreteEntry, this.isBelow, cflowStackField, inAspect, innerCflowEntries));
        if (inAspect.isAnnotationStyleAspect() && !inAspect.isAbstract()) {
            inAspect.crosscuttingMembers.addLateTypeMunger(inAspect.getWorld().getWeavingSupport().makePerClauseAspect(inAspect, getKind()));
        }
        if (inAspect.isAnnotationStyleAspect() && !inAspect.getWorld().isXnoInline()) {
            inAspect.crosscuttingMembers.addTypeMunger(inAspect.getWorld().getWeavingSupport().createAccessForInlineMunger(inAspect));
        }
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        PERCFLOW.write(s);
        this.entry.write(s);
        s.writeBoolean(this.isBelow);
        writeLocation(s);
    }

    public static PerClause readPerClause(VersionedDataInputStream s, ISourceContext context) throws IOException {
        PerCflow ret = new PerCflow(Pointcut.read(s, context), s.readBoolean());
        ret.readLocation(context, s);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.PerClause
    public PerClause.Kind getKind() {
        return PERCFLOW;
    }

    public Pointcut getEntry() {
        return this.entry;
    }

    public String toString() {
        return "percflow(" + this.inAspect + " on " + this.entry + ")";
    }

    @Override // org.aspectj.weaver.patterns.PerClause
    public String toDeclarationString() {
        if (this.isBelow) {
            return "percflowbelow(" + this.entry + ")";
        }
        return "percflow(" + this.entry + ")";
    }

    public boolean equals(Object other) {
        if (!(other instanceof PerCflow)) {
            return false;
        }
        PerCflow pc = (PerCflow) other;
        return pc.isBelow && this.isBelow && (pc.inAspect != null ? pc.inAspect.equals(this.inAspect) : this.inAspect == null) && (pc.entry != null ? pc.entry.equals(this.entry) : this.entry == null);
    }

    public int hashCode() {
        int result = (37 * 17) + (this.isBelow ? 0 : 1);
        return (37 * ((37 * result) + (this.inAspect == null ? 0 : this.inAspect.hashCode()))) + (this.entry == null ? 0 : this.entry.hashCode());
    }
}
