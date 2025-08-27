package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.aspectj.bridge.IMessage;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.World;
import org.aspectj.weaver.ast.Literal;
import org.aspectj.weaver.ast.Test;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/ArgsPointcut.class */
public class ArgsPointcut extends NameBindingPointcut {
    private static final String ASPECTJ_JP_SIGNATURE_PREFIX = "Lorg/aspectj/lang/JoinPoint";
    private static final String ASPECTJ_SYNTHETIC_SIGNATURE_PREFIX = "Lorg/aspectj/runtime/internal/";
    private TypePatternList arguments;
    private String stringRepresentation;

    public ArgsPointcut(TypePatternList arguments) {
        this.arguments = arguments;
        this.pointcutKind = (byte) 4;
        this.stringRepresentation = "args" + arguments.toString() + "";
    }

    public TypePatternList getArguments() {
        return this.arguments;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Pointcut parameterizeWith(Map<String, UnresolvedType> typeVariableMap, World w) {
        ArgsPointcut ret = new ArgsPointcut(this.arguments.parameterizeWith(typeVariableMap, w));
        ret.copyLocationFrom(this);
        return ret;
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
        ResolvedType[] argumentsToMatchAgainst = getArgumentsToMatchAgainst(shadow);
        FuzzyBoolean ret = this.arguments.matches(argumentsToMatchAgainst, TypePattern.DYNAMIC);
        return ret;
    }

    private ResolvedType[] getArgumentsToMatchAgainst(Shadow shadow) {
        if (shadow.isShadowForArrayConstructionJoinpoint()) {
            return shadow.getArgumentTypesForArrayConstructionShadow();
        }
        ResolvedType[] argumentsToMatchAgainst = shadow.getIWorld().resolve(shadow.getGenericArgTypes());
        if (shadow.getKind() == Shadow.AdviceExecution) {
            int numExtraArgs = 0;
            for (ResolvedType resolvedType : argumentsToMatchAgainst) {
                String argumentSignature = resolvedType.getSignature();
                if (argumentSignature.startsWith(ASPECTJ_JP_SIGNATURE_PREFIX) || argumentSignature.startsWith(ASPECTJ_SYNTHETIC_SIGNATURE_PREFIX)) {
                    numExtraArgs++;
                } else {
                    numExtraArgs = 0;
                }
            }
            if (numExtraArgs > 0) {
                int newArgLength = argumentsToMatchAgainst.length - numExtraArgs;
                ResolvedType[] argsSubset = new ResolvedType[newArgLength];
                System.arraycopy(argumentsToMatchAgainst, 0, argsSubset, 0, newArgLength);
                argumentsToMatchAgainst = argsSubset;
            }
        } else if (shadow.getKind() == Shadow.ConstructorExecution && shadow.getMatchingSignature().getParameterTypes().length < argumentsToMatchAgainst.length) {
            int newArgLength2 = shadow.getMatchingSignature().getParameterTypes().length;
            ResolvedType[] argsSubset2 = new ResolvedType[newArgLength2];
            System.arraycopy(argumentsToMatchAgainst, 0, argsSubset2, 0, newArgLength2);
            argumentsToMatchAgainst = argsSubset2;
        }
        return argumentsToMatchAgainst;
    }

    @Override // org.aspectj.weaver.patterns.NameBindingPointcut
    public List<BindingPattern> getBindingAnnotationTypePatterns() {
        return Collections.emptyList();
    }

    @Override // org.aspectj.weaver.patterns.NameBindingPointcut
    public List<BindingTypePattern> getBindingTypePatterns() {
        List<BindingTypePattern> l = new ArrayList<>();
        TypePattern[] pats = this.arguments.getTypePatterns();
        for (int i = 0; i < pats.length; i++) {
            if (pats[i] instanceof BindingTypePattern) {
                l.add((BindingTypePattern) pats[i]);
            }
        }
        return l;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(4);
        this.arguments.write(s);
        writeLocation(s);
    }

    public static Pointcut read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        ArgsPointcut ret = new ArgsPointcut(TypePatternList.read(s, context));
        ret.readLocation(context, s);
        return ret;
    }

    public boolean equals(Object other) {
        if (!(other instanceof ArgsPointcut)) {
            return false;
        }
        ArgsPointcut o = (ArgsPointcut) other;
        return o.arguments.equals(this.arguments);
    }

    public int hashCode() {
        return this.arguments.hashCode();
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public void resolveBindings(IScope scope, Bindings bindings) {
        this.arguments.resolveBindings(scope, bindings, true, true);
        if (this.arguments.ellipsisCount > 1) {
            scope.message(IMessage.ERROR, this, "uses more than one .. in args (compiler limitation)");
        }
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public void postRead(ResolvedType enclosingType) {
        this.arguments.postRead(enclosingType);
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Pointcut concretize1(ResolvedType inAspect, ResolvedType declaringType, IntMap bindings) {
        if (isDeclare(bindings.getEnclosingAdvice())) {
            inAspect.getWorld().showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.ARGS_IN_DECLARE), bindings.getEnclosingAdvice().getSourceLocation(), null);
            return Pointcut.makeMatchesNothing(Pointcut.CONCRETE);
        }
        TypePatternList args = this.arguments.resolveReferences(bindings);
        if (inAspect.crosscuttingMembers != null) {
            inAspect.crosscuttingMembers.exposeTypes(args.getExactTypes());
        }
        Pointcut ret = new ArgsPointcut(args);
        ret.copyLocationFrom(this);
        return ret;
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x008e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private org.aspectj.weaver.ast.Test findResidueNoEllipsis(org.aspectj.weaver.Shadow r9, org.aspectj.weaver.patterns.ExposedState r10, org.aspectj.weaver.patterns.TypePattern[] r11) {
        /*
            Method dump skipped, instructions count: 342
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.aspectj.weaver.patterns.ArgsPointcut.findResidueNoEllipsis(org.aspectj.weaver.Shadow, org.aspectj.weaver.patterns.ExposedState, org.aspectj.weaver.patterns.TypePattern[]):org.aspectj.weaver.ast.Test");
    }

    private boolean isUncheckedArgumentWarningSuppressed() {
        return false;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected Test findResidueInternal(Shadow shadow, ExposedState state) {
        ResolvedType[] argsToMatch = getArgumentsToMatchAgainst(shadow);
        if (this.arguments.matches(argsToMatch, TypePattern.DYNAMIC).alwaysFalse()) {
            return Literal.FALSE;
        }
        int ellipsisCount = this.arguments.ellipsisCount;
        if (ellipsisCount == 0) {
            return findResidueNoEllipsis(shadow, state, this.arguments.getTypePatterns());
        }
        if (ellipsisCount == 1) {
            TypePattern[] patternsWithEllipsis = this.arguments.getTypePatterns();
            TypePattern[] patternsWithoutEllipsis = new TypePattern[argsToMatch.length];
            int lenWithEllipsis = patternsWithEllipsis.length;
            int lenWithoutEllipsis = patternsWithoutEllipsis.length;
            int indexWithEllipsis = 0;
            int indexWithoutEllipsis = 0;
            while (indexWithoutEllipsis < lenWithoutEllipsis) {
                int i = indexWithEllipsis;
                indexWithEllipsis++;
                TypePattern p = patternsWithEllipsis[i];
                if (p == TypePattern.ELLIPSIS) {
                    int newLenWithoutEllipsis = lenWithoutEllipsis - (lenWithEllipsis - indexWithEllipsis);
                    while (indexWithoutEllipsis < newLenWithoutEllipsis) {
                        int i2 = indexWithoutEllipsis;
                        indexWithoutEllipsis++;
                        patternsWithoutEllipsis[i2] = TypePattern.ANY;
                    }
                } else {
                    int i3 = indexWithoutEllipsis;
                    indexWithoutEllipsis++;
                    patternsWithoutEllipsis[i3] = p;
                }
            }
            return findResidueNoEllipsis(shadow, state, patternsWithoutEllipsis);
        }
        throw new BCException("unimplemented");
    }

    public String toString() {
        return this.stringRepresentation;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
