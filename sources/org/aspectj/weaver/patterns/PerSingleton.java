package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.Map;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.AjcMemberMaker;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.World;
import org.aspectj.weaver.ast.Expr;
import org.aspectj.weaver.ast.Literal;
import org.aspectj.weaver.ast.Test;
import org.aspectj.weaver.patterns.PerClause;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/PerSingleton.class */
public class PerSingleton extends PerClause {
    private ResolvedMember perSingletonAspectOfMethod;

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
        return FuzzyBoolean.YES;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected FuzzyBoolean matchInternal(Shadow shadow) {
        return FuzzyBoolean.YES;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public void resolveBindings(IScope scope, Bindings bindings) {
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Pointcut parameterizeWith(Map<String, UnresolvedType> typeVariableMap, World w) {
        return this;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Test findResidueInternal(Shadow shadow, ExposedState state) {
        if (this.perSingletonAspectOfMethod == null) {
            this.perSingletonAspectOfMethod = AjcMemberMaker.perSingletonAspectOfMethod(this.inAspect);
        }
        Expr myInstance = Expr.makeCallExpr(this.perSingletonAspectOfMethod, Expr.NONE, this.inAspect);
        state.setAspectInstance(myInstance);
        return Literal.TRUE;
    }

    @Override // org.aspectj.weaver.patterns.PerClause
    public PerClause concretize(ResolvedType inAspect) {
        PerSingleton ret = new PerSingleton();
        ret.copyLocationFrom(this);
        World world = inAspect.getWorld();
        ret.inAspect = inAspect;
        if (inAspect.isAnnotationStyleAspect() && !inAspect.isAbstract()) {
            if (getKind() == SINGLETON) {
                inAspect.crosscuttingMembers.addTypeMunger(world.getWeavingSupport().makePerClauseAspect(inAspect, getKind()));
            } else {
                inAspect.crosscuttingMembers.addLateTypeMunger(world.getWeavingSupport().makePerClauseAspect(inAspect, getKind()));
            }
        }
        if (inAspect.isAnnotationStyleAspect() && !inAspect.getWorld().isXnoInline()) {
            inAspect.crosscuttingMembers.addTypeMunger(world.getWeavingSupport().createAccessForInlineMunger(inAspect));
        }
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        SINGLETON.write(s);
        writeLocation(s);
    }

    public static PerClause readPerClause(VersionedDataInputStream s, ISourceContext context) throws IOException {
        PerSingleton ret = new PerSingleton();
        ret.readLocation(context, s);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.PerClause
    public PerClause.Kind getKind() {
        return SINGLETON;
    }

    public String toString() {
        return "persingleton(" + this.inAspect + ")";
    }

    @Override // org.aspectj.weaver.patterns.PerClause
    public String toDeclarationString() {
        return "";
    }

    public boolean equals(Object other) {
        if (!(other instanceof PerSingleton)) {
            return false;
        }
        PerSingleton pc = (PerSingleton) other;
        return pc.inAspect == null ? this.inAspect == null : pc.inAspect.equals(this.inAspect);
    }

    public int hashCode() {
        int result = (37 * 17) + (this.inAspect == null ? 0 : this.inAspect.hashCode());
        return result;
    }
}
