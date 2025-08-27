package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.Map;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.Message;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.Advice;
import org.aspectj.weaver.AjcMemberMaker;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.PerTypeWithinTargetTypeMunger;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.ResolvedTypeMunger;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.World;
import org.aspectj.weaver.ast.Expr;
import org.aspectj.weaver.ast.Literal;
import org.aspectj.weaver.ast.Test;
import org.aspectj.weaver.patterns.PerClause;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/PerTypeWithin.class */
public class PerTypeWithin extends PerClause {
    private TypePattern typePattern;
    private static final int kindSet = Shadow.ALL_SHADOW_KINDS_BITS;

    public TypePattern getTypePattern() {
        return this.typePattern;
    }

    public PerTypeWithin(TypePattern p) {
        this.typePattern = p;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public int couldMatchKinds() {
        return kindSet;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Pointcut parameterizeWith(Map<String, UnresolvedType> typeVariableMap, World w) {
        PerTypeWithin ret = new PerTypeWithin(this.typePattern.parameterizeWith(typeVariableMap, w));
        ret.copyLocationFrom(this);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public FuzzyBoolean fastMatch(FastMatchInfo info) {
        if (this.typePattern.annotationPattern instanceof AnyAnnotationTypePattern) {
            return isWithinType(info.getType());
        }
        return FuzzyBoolean.MAYBE;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected FuzzyBoolean matchInternal(Shadow shadow) throws AbortException {
        ResolvedType enclosingType = shadow.getIWorld().resolve(shadow.getEnclosingType(), true);
        if (enclosingType.isMissing()) {
            IMessage msg = new Message("Cant find type pertypewithin matching...", shadow.getSourceLocation(), true, new ISourceLocation[]{getSourceLocation()});
            shadow.getIWorld().getMessageHandler().handleMessage(msg);
        }
        if (enclosingType.isInterface()) {
            return FuzzyBoolean.NO;
        }
        this.typePattern.resolve(shadow.getIWorld());
        return isWithinType(enclosingType);
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public void resolveBindings(IScope scope, Bindings bindings) {
        this.typePattern = this.typePattern.resolveBindings(scope, bindings, false, false);
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected Test findResidueInternal(Shadow shadow, ExposedState state) {
        Expr myInstance = Expr.makeCallExpr(AjcMemberMaker.perTypeWithinLocalAspectOf(shadow.getEnclosingType(), this.inAspect), Expr.NONE, this.inAspect);
        state.setAspectInstance(myInstance);
        return match(shadow).alwaysTrue() ? Literal.TRUE : Literal.FALSE;
    }

    @Override // org.aspectj.weaver.patterns.PerClause
    public PerClause concretize(ResolvedType inAspect) {
        PerTypeWithin ret = new PerTypeWithin(this.typePattern);
        ret.copyLocationFrom(this);
        ret.inAspect = inAspect;
        if (inAspect.isAbstract()) {
            return ret;
        }
        World world = inAspect.getWorld();
        SignaturePattern sigpat = new SignaturePattern(Member.STATIC_INITIALIZATION, ModifiersPattern.ANY, TypePattern.ANY, TypePattern.ANY, NamePattern.ANY, TypePatternList.ANY, ThrowsPattern.ANY, AnnotationTypePattern.ANY);
        Pointcut staticInitStar = new KindedPointcut(Shadow.StaticInitialization, sigpat);
        Pointcut withinTp = new WithinPointcut(this.typePattern);
        Pointcut andPcut = new AndPointcut(staticInitStar, withinTp);
        inAspect.crosscuttingMembers.addConcreteShadowMunger(Advice.makePerTypeWithinEntry(world, andPcut, inAspect));
        ResolvedTypeMunger munger = new PerTypeWithinTargetTypeMunger(inAspect, ret);
        inAspect.crosscuttingMembers.addTypeMunger(world.getWeavingSupport().concreteTypeMunger(munger, inAspect));
        if (inAspect.isAnnotationStyleAspect() && !inAspect.isAbstract()) {
            inAspect.crosscuttingMembers.addLateTypeMunger(world.getWeavingSupport().makePerClauseAspect(inAspect, getKind()));
        }
        if (inAspect.isAnnotationStyleAspect() && !world.isXnoInline()) {
            inAspect.crosscuttingMembers.addTypeMunger(world.getWeavingSupport().createAccessForInlineMunger(inAspect));
        }
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        PERTYPEWITHIN.write(s);
        this.typePattern.write(s);
        writeLocation(s);
    }

    public static PerClause readPerClause(VersionedDataInputStream s, ISourceContext context) throws IOException {
        PerClause ret = new PerTypeWithin(TypePattern.read(s, context));
        ret.readLocation(context, s);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.PerClause
    public PerClause.Kind getKind() {
        return PERTYPEWITHIN;
    }

    public String toString() {
        return "pertypewithin(" + this.typePattern + ")";
    }

    @Override // org.aspectj.weaver.patterns.PerClause
    public String toDeclarationString() {
        return toString();
    }

    private FuzzyBoolean isWithinType(ResolvedType type) {
        while (type != null) {
            if (this.typePattern.matchesStatically(type)) {
                return FuzzyBoolean.YES;
            }
            type = type.getDeclaringType();
        }
        return FuzzyBoolean.NO;
    }

    public boolean equals(Object other) {
        if (!(other instanceof PerTypeWithin)) {
            return false;
        }
        PerTypeWithin pc = (PerTypeWithin) other;
        if (pc.inAspect != null ? pc.inAspect.equals(this.inAspect) : this.inAspect == null) {
            if (pc.typePattern != null ? pc.typePattern.equals(this.typePattern) : this.typePattern == null) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int result = (37 * 17) + (this.inAspect == null ? 0 : this.inAspect.hashCode());
        return (37 * result) + (this.typePattern == null ? 0 : this.typePattern.hashCode());
    }
}
