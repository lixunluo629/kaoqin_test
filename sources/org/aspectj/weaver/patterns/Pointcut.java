package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.Map;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.util.TypeSafeEnum;
import org.aspectj.weaver.Advice;
import org.aspectj.weaver.AdviceKind;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.Checker;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.PoliceExtensionUse;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.ShadowMunger;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.World;
import org.aspectj.weaver.ast.Literal;
import org.aspectj.weaver.ast.Test;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/Pointcut.class */
public abstract class Pointcut extends PatternNode {
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final State SYMBOLIC = new State("symbolic", 0);
    public static final State RESOLVED = new State("resolved", 1);
    public static final State CONCRETE = new State("concrete", 2);
    protected byte pointcutKind;
    protected int lastMatchedShadowId;
    private FuzzyBoolean lastMatchedShadowResult;
    public static final byte KINDED = 1;
    public static final byte WITHIN = 2;
    public static final byte THIS_OR_TARGET = 3;
    public static final byte ARGS = 4;
    public static final byte AND = 5;
    public static final byte OR = 6;
    public static final byte NOT = 7;
    public static final byte REFERENCE = 8;
    public static final byte IF = 9;
    public static final byte CFLOW = 10;
    public static final byte WITHINCODE = 12;
    public static final byte HANDLER = 13;
    public static final byte IF_TRUE = 14;
    public static final byte IF_FALSE = 15;
    public static final byte ANNOTATION = 16;
    public static final byte ATWITHIN = 17;
    public static final byte ATWITHINCODE = 18;
    public static final byte ATTHIS_OR_TARGET = 19;
    public static final byte NONE = 20;
    public static final byte ATARGS = 21;
    public static final byte USER_EXTENSION = 22;
    public String[] m_ignoreUnboundBindingForNames = EMPTY_STRING_ARRAY;
    private String[] typeVariablesInScope = EMPTY_STRING_ARRAY;
    protected boolean hasBeenParameterized = false;
    public State state = SYMBOLIC;

    public abstract FuzzyBoolean fastMatch(FastMatchInfo fastMatchInfo);

    public abstract int couldMatchKinds();

    protected abstract FuzzyBoolean matchInternal(Shadow shadow);

    protected abstract void resolveBindings(IScope iScope, Bindings bindings);

    protected abstract Pointcut concretize1(ResolvedType resolvedType, ResolvedType resolvedType2, IntMap intMap);

    protected abstract Test findResidueInternal(Shadow shadow, ExposedState exposedState);

    public abstract Pointcut parameterizeWith(Map<String, UnresolvedType> map, World world);

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/Pointcut$State.class */
    public static final class State extends TypeSafeEnum {
        public State(String name, int key) {
            super(name, key);
        }
    }

    public String[] getTypeVariablesInScope() {
        return this.typeVariablesInScope;
    }

    public void setTypeVariablesInScope(String[] typeVars) {
        this.typeVariablesInScope = typeVars;
    }

    public final FuzzyBoolean match(Shadow shadow) {
        FuzzyBoolean ret;
        if (shadow.shadowId == this.lastMatchedShadowId) {
            return this.lastMatchedShadowResult;
        }
        if (shadow.getKind().isSet(couldMatchKinds())) {
            ret = matchInternal(shadow);
        } else {
            ret = FuzzyBoolean.NO;
        }
        this.lastMatchedShadowId = shadow.shadowId;
        this.lastMatchedShadowResult = ret;
        return ret;
    }

    public byte getPointcutKind() {
        return this.pointcutKind;
    }

    public final Pointcut resolve(IScope scope) {
        assertState(SYMBOLIC);
        Bindings bindingTable = new Bindings(scope.getFormalCount());
        IScope bindingResolutionScope = scope;
        if (this.typeVariablesInScope.length > 0) {
            bindingResolutionScope = new ScopeWithTypeVariables(this.typeVariablesInScope, scope);
        }
        resolveBindings(bindingResolutionScope, bindingTable);
        bindingTable.checkAllBound(bindingResolutionScope);
        this.state = RESOLVED;
        return this;
    }

    public final Pointcut concretize(ResolvedType inAspect, ResolvedType declaringType, int arity) {
        Pointcut ret = concretize(inAspect, declaringType, IntMap.idMap(arity));
        ret.m_ignoreUnboundBindingForNames = this.m_ignoreUnboundBindingForNames;
        return ret;
    }

    public final Pointcut concretize(ResolvedType inAspect, ResolvedType declaringType, int arity, ShadowMunger advice) {
        IntMap map = IntMap.idMap(arity);
        map.setEnclosingAdvice(advice);
        map.setConcreteAspect(inAspect);
        return concretize(inAspect, declaringType, map);
    }

    public boolean isDeclare(ShadowMunger munger) {
        if (munger == null) {
            return false;
        }
        if ((munger instanceof Checker) || ((Advice) munger).getKind().equals(AdviceKind.Softener)) {
            return true;
        }
        return false;
    }

    public final Pointcut concretize(ResolvedType inAspect, ResolvedType declaringType, IntMap bindings) {
        Pointcut ret = concretize1(inAspect, declaringType, bindings);
        if (shouldCopyLocationForConcretize()) {
            ret.copyLocationFrom(this);
        }
        ret.state = CONCRETE;
        ret.m_ignoreUnboundBindingForNames = this.m_ignoreUnboundBindingForNames;
        return ret;
    }

    protected boolean shouldCopyLocationForConcretize() {
        return true;
    }

    public final Test findResidue(Shadow shadow, ExposedState state) {
        Test ret = findResidueInternal(shadow, state);
        this.lastMatchedShadowId = shadow.shadowId;
        return ret;
    }

    public void postRead(ResolvedType enclosingType) {
    }

    public static Pointcut read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        Pointcut ret;
        byte kind = s.readByte();
        switch (kind) {
            case 1:
                ret = KindedPointcut.read(s, context);
                break;
            case 2:
                ret = WithinPointcut.read(s, context);
                break;
            case 3:
                ret = ThisOrTargetPointcut.read(s, context);
                break;
            case 4:
                ret = ArgsPointcut.read(s, context);
                break;
            case 5:
                ret = AndPointcut.read(s, context);
                break;
            case 6:
                ret = OrPointcut.read(s, context);
                break;
            case 7:
                ret = NotPointcut.read(s, context);
                break;
            case 8:
                ret = ReferencePointcut.read(s, context);
                break;
            case 9:
                ret = IfPointcut.read(s, context);
                break;
            case 10:
                ret = CflowPointcut.read(s, context);
                break;
            case 11:
            default:
                throw new BCException("unknown kind: " + ((int) kind));
            case 12:
                ret = WithincodePointcut.read(s, context);
                break;
            case 13:
                ret = HandlerPointcut.read(s, context);
                break;
            case 14:
                ret = IfPointcut.makeIfTruePointcut(RESOLVED);
                break;
            case 15:
                ret = IfPointcut.makeIfFalsePointcut(RESOLVED);
                break;
            case 16:
                ret = AnnotationPointcut.read(s, context);
                break;
            case 17:
                ret = WithinAnnotationPointcut.read(s, context);
                break;
            case 18:
                ret = WithinCodeAnnotationPointcut.read(s, context);
                break;
            case 19:
                ret = ThisOrTargetAnnotationPointcut.read(s, context);
                break;
            case 20:
                ret = makeMatchesNothing(RESOLVED);
                break;
            case 21:
                ret = ArgsAnnotationPointcut.read(s, context);
                break;
        }
        ret.state = RESOLVED;
        ret.pointcutKind = kind;
        return ret;
    }

    public void check(ISourceContext ctx, World world) {
        PoliceExtensionUse pointcutPolice = new PoliceExtensionUse(world, this);
        accept(pointcutPolice, null);
        if (pointcutPolice.synchronizationDesignatorEncountered()) {
            world.setSynchronizationPointcutsInUse();
        }
    }

    public static Pointcut fromString(String str) {
        PatternParser parser = new PatternParser(str);
        return parser.parsePointcut();
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/Pointcut$MatchesNothingPointcut.class */
    static class MatchesNothingPointcut extends Pointcut {
        MatchesNothingPointcut() {
        }

        @Override // org.aspectj.weaver.patterns.Pointcut
        protected Test findResidueInternal(Shadow shadow, ExposedState state) {
            return Literal.FALSE;
        }

        @Override // org.aspectj.weaver.patterns.Pointcut
        public int couldMatchKinds() {
            return Shadow.NO_SHADOW_KINDS_BITS;
        }

        @Override // org.aspectj.weaver.patterns.Pointcut
        public FuzzyBoolean fastMatch(FastMatchInfo type) {
            return FuzzyBoolean.NO;
        }

        @Override // org.aspectj.weaver.patterns.Pointcut
        protected FuzzyBoolean matchInternal(Shadow shadow) {
            return FuzzyBoolean.NO;
        }

        @Override // org.aspectj.weaver.patterns.Pointcut
        public void resolveBindings(IScope scope, Bindings bindings) {
        }

        @Override // org.aspectj.weaver.patterns.Pointcut
        public void postRead(ResolvedType enclosingType) {
        }

        @Override // org.aspectj.weaver.patterns.Pointcut
        public Pointcut concretize1(ResolvedType inAspect, ResolvedType declaringType, IntMap bindings) {
            return makeMatchesNothing(this.state);
        }

        @Override // org.aspectj.weaver.patterns.PatternNode
        public void write(CompressingDataOutputStream s) throws IOException {
            s.writeByte(20);
        }

        public String toString() {
            return "";
        }

        @Override // org.aspectj.weaver.patterns.PatternNode
        public Object accept(PatternNodeVisitor visitor, Object data) {
            return visitor.visit(this, data);
        }

        @Override // org.aspectj.weaver.patterns.Pointcut
        public Pointcut parameterizeWith(Map<String, UnresolvedType> typeVariableMap, World w) {
            return this;
        }
    }

    public static Pointcut makeMatchesNothing(State state) {
        Pointcut ret = new MatchesNothingPointcut();
        ret.state = state;
        return ret;
    }

    public void assertState(State state) {
        if (this.state != state) {
            throw new BCException("expected state: " + state + " got: " + this.state);
        }
    }
}
