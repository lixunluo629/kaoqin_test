package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.Checker;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.ShadowMunger;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.World;
import org.aspectj.weaver.ast.Literal;
import org.aspectj.weaver.ast.Test;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/KindedPointcut.class */
public class KindedPointcut extends Pointcut {
    Shadow.Kind kind;
    private SignaturePattern signature;
    private int matchKinds;
    private ShadowMunger munger;

    public KindedPointcut(Shadow.Kind kind, SignaturePattern signature) {
        this.munger = null;
        this.kind = kind;
        this.signature = signature;
        this.pointcutKind = (byte) 1;
        this.matchKinds = kind.bit;
    }

    public KindedPointcut(Shadow.Kind kind, SignaturePattern signature, ShadowMunger munger) {
        this(kind, signature);
        this.munger = munger;
    }

    public SignaturePattern getSignature() {
        return this.signature;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public int couldMatchKinds() {
        return this.matchKinds;
    }

    public boolean couldEverMatchSameJoinPointsAs(KindedPointcut other) {
        if (this.kind != other.kind) {
            return false;
        }
        String myName = this.signature.getName().maybeGetSimpleName();
        String yourName = other.signature.getName().maybeGetSimpleName();
        if (myName != null && yourName != null && !myName.equals(yourName)) {
            return false;
        }
        if (this.signature.getParameterTypes().ellipsisCount == 0 && other.signature.getParameterTypes().ellipsisCount == 0 && this.signature.getParameterTypes().getTypePatterns().length != other.signature.getParameterTypes().getTypePatterns().length) {
            return false;
        }
        return true;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public FuzzyBoolean fastMatch(FastMatchInfo info) {
        if (info.getKind() != null && info.getKind() != this.kind) {
            return FuzzyBoolean.NO;
        }
        if (info.world.optimizedMatching && ((this.kind == Shadow.MethodExecution || this.kind == Shadow.Initialization) && info.getKind() == null)) {
            boolean fastMatchingOnAspect = info.getType().isAspect();
            if (fastMatchingOnAspect) {
                return FuzzyBoolean.MAYBE;
            }
            if (getSignature().isExactDeclaringTypePattern()) {
                ExactTypePattern typePattern = (ExactTypePattern) getSignature().getDeclaringType();
                ResolvedType patternExactType = typePattern.getResolvedExactType(info.world);
                if (patternExactType.isInterface()) {
                    ResolvedType curr = info.getType();
                    Iterator<ResolvedType> hierarchyWalker = curr.getHierarchy(true, true);
                    boolean found = false;
                    while (true) {
                        if (!hierarchyWalker.hasNext()) {
                            break;
                        }
                        ResolvedType curr2 = hierarchyWalker.next();
                        if (typePattern.matchesStatically(curr2)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        return FuzzyBoolean.NO;
                    }
                } else if (patternExactType.isClass()) {
                    ResolvedType curr3 = info.getType();
                    while (!typePattern.matchesStatically(curr3)) {
                        curr3 = curr3.getSuperclass();
                        if (curr3 == null) {
                            break;
                        }
                    }
                    if (curr3 == null) {
                        return FuzzyBoolean.NO;
                    }
                }
            } else if (getSignature().getDeclaringType() instanceof AnyWithAnnotationTypePattern) {
                ResolvedType type = info.getType();
                AnnotationTypePattern annotationTypePattern = ((AnyWithAnnotationTypePattern) getSignature().getDeclaringType()).getAnnotationPattern();
                if (annotationTypePattern instanceof ExactAnnotationTypePattern) {
                    ExactAnnotationTypePattern exactAnnotationTypePattern = (ExactAnnotationTypePattern) annotationTypePattern;
                    if (exactAnnotationTypePattern.getAnnotationValues() == null || exactAnnotationTypePattern.getAnnotationValues().size() == 0) {
                        ResolvedType annotationType = exactAnnotationTypePattern.getAnnotationType().resolve(info.world);
                        if (type.hasAnnotation(annotationType)) {
                            return FuzzyBoolean.MAYBE;
                        }
                        if (annotationType.isInheritedAnnotation()) {
                            ResolvedType toMatchAgainst = type.getSuperclass();
                            boolean found2 = false;
                            while (true) {
                                if (toMatchAgainst == null) {
                                    break;
                                }
                                if (toMatchAgainst.hasAnnotation(annotationType)) {
                                    found2 = true;
                                    break;
                                }
                                toMatchAgainst = toMatchAgainst.getSuperclass();
                            }
                            if (!found2) {
                                return FuzzyBoolean.NO;
                            }
                        } else {
                            return FuzzyBoolean.NO;
                        }
                    }
                }
            }
        }
        return FuzzyBoolean.MAYBE;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected FuzzyBoolean matchInternal(Shadow shadow) {
        if (shadow.getKind() != this.kind) {
            return FuzzyBoolean.NO;
        }
        if (shadow.getKind() == Shadow.SynchronizationLock && this.kind == Shadow.SynchronizationLock) {
            return FuzzyBoolean.YES;
        }
        if (shadow.getKind() == Shadow.SynchronizationUnlock && this.kind == Shadow.SynchronizationUnlock) {
            return FuzzyBoolean.YES;
        }
        if (!this.signature.matches(shadow.getMatchingSignature(), shadow.getIWorld(), this.kind == Shadow.MethodCall)) {
            if (this.kind == Shadow.MethodCall) {
                warnOnConfusingSig(shadow);
            }
            return FuzzyBoolean.NO;
        }
        return FuzzyBoolean.YES;
    }

    private void warnOnConfusingSig(Shadow shadow) {
        ResolvedMember rm;
        if (!shadow.getIWorld().getLint().unmatchedSuperTypeInCall.isEnabled() || (this.munger instanceof Checker)) {
            return;
        }
        World world = shadow.getIWorld();
        UnresolvedType exactDeclaringType = this.signature.getDeclaringType().getExactType();
        ResolvedType shadowDeclaringType = shadow.getSignature().getDeclaringType().resolve(world);
        if (this.signature.getDeclaringType().isStar() || ResolvedType.isMissing(exactDeclaringType) || exactDeclaringType.resolve(world).isMissing() || !shadowDeclaringType.isAssignableFrom(exactDeclaringType.resolve(world)) || (rm = shadow.getSignature().resolve(world)) == null) {
            return;
        }
        int shadowModifiers = rm.getModifiers();
        if (!ResolvedType.isVisible(shadowModifiers, shadowDeclaringType, exactDeclaringType.resolve(world)) || !this.signature.getReturnType().matchesStatically(shadow.getSignature().getReturnType().resolve(world))) {
            return;
        }
        if (exactDeclaringType.resolve(world).isInterface() && shadowDeclaringType.equals(world.resolve("java.lang.Object"))) {
            return;
        }
        SignaturePattern nonConfusingPattern = new SignaturePattern(this.signature.getKind(), this.signature.getModifiers(), this.signature.getReturnType(), TypePattern.ANY, this.signature.getName(), this.signature.getParameterTypes(), this.signature.getThrowsPattern(), this.signature.getAnnotationPattern());
        if (nonConfusingPattern.matches(shadow.getSignature(), shadow.getIWorld(), true)) {
            shadow.getIWorld().getLint().unmatchedSuperTypeInCall.signal(new String[]{shadow.getSignature().getDeclaringType().toString(), this.signature.getDeclaringType().toString()}, getSourceLocation(), new ISourceLocation[]{shadow.getSourceLocation()});
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof KindedPointcut)) {
            return false;
        }
        KindedPointcut o = (KindedPointcut) other;
        return o.kind == this.kind && o.signature.equals(this.signature);
    }

    public int hashCode() {
        int result = (37 * 17) + this.kind.hashCode();
        return (37 * result) + this.signature.hashCode();
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.kind.getSimpleName());
        buf.append("(");
        buf.append(this.signature.toString());
        buf.append(")");
        return buf.toString();
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public void postRead(ResolvedType enclosingType) {
        this.signature.postRead(enclosingType);
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(1);
        this.kind.write(s);
        this.signature.write(s);
        writeLocation(s);
    }

    public static Pointcut read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        Shadow.Kind kind = Shadow.Kind.read(s);
        SignaturePattern sig = SignaturePattern.read(s, context);
        KindedPointcut ret = new KindedPointcut(kind, sig);
        ret.readLocation(context, s);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public void resolveBindings(IScope scope, Bindings bindings) {
        if (this.kind == Shadow.Initialization) {
        }
        this.signature = this.signature.resolveBindings(scope, bindings);
        if (this.kind == Shadow.ConstructorExecution && this.signature.getDeclaringType() != null) {
            World world = scope.getWorld();
            UnresolvedType exactType = this.signature.getDeclaringType().getExactType();
            if (this.signature.getKind() == Member.CONSTRUCTOR && !ResolvedType.isMissing(exactType) && exactType.resolve(world).isInterface() && !this.signature.getDeclaringType().isIncludeSubtypes()) {
                world.getLint().noInterfaceCtorJoinpoint.signal(exactType.toString(), getSourceLocation());
            }
        }
        if (this.kind == Shadow.StaticInitialization) {
            HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor visitor = new HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor();
            this.signature.getDeclaringType().traverse(visitor, null);
            if (visitor.wellHasItThen()) {
                scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.NO_STATIC_INIT_JPS_FOR_PARAMETERIZED_TYPES), getSourceLocation()));
            }
        }
        if (this.kind == Shadow.FieldGet || this.kind == Shadow.FieldSet) {
            HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor visitor2 = new HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor();
            this.signature.getDeclaringType().traverse(visitor2, null);
            if (visitor2.wellHasItThen()) {
                scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.GET_AND_SET_DONT_SUPPORT_DEC_TYPE_PARAMETERS), getSourceLocation()));
            }
            UnresolvedType returnType = this.signature.getReturnType().getExactType();
            if (returnType.equals(UnresolvedType.VOID)) {
                scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.FIELDS_CANT_HAVE_VOID_TYPE), getSourceLocation()));
            }
        }
        if (this.kind == Shadow.Initialization || this.kind == Shadow.PreInitialization) {
            HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor visitor3 = new HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor();
            this.signature.getDeclaringType().traverse(visitor3, null);
            if (visitor3.wellHasItThen()) {
                scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.NO_INIT_JPS_FOR_PARAMETERIZED_TYPES), getSourceLocation()));
            }
            HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor visitor4 = new HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor();
            this.signature.getThrowsPattern().traverse(visitor4, null);
            if (visitor4.wellHasItThen()) {
                scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.NO_GENERIC_THROWABLES), getSourceLocation()));
            }
        }
        if (this.kind == Shadow.MethodExecution || this.kind == Shadow.ConstructorExecution) {
            HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor visitor5 = new HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor();
            this.signature.getDeclaringType().traverse(visitor5, null);
            if (visitor5.wellHasItThen()) {
                scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.EXECUTION_DOESNT_SUPPORT_PARAMETERIZED_DECLARING_TYPES), getSourceLocation()));
            }
            HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor visitor6 = new HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor();
            this.signature.getThrowsPattern().traverse(visitor6, null);
            if (visitor6.wellHasItThen()) {
                scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.NO_GENERIC_THROWABLES), getSourceLocation()));
            }
        }
        if (this.kind == Shadow.MethodCall || this.kind == Shadow.ConstructorCall) {
            HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor visitor7 = new HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor();
            this.signature.getDeclaringType().traverse(visitor7, null);
            if (visitor7.wellHasItThen()) {
                scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.CALL_DOESNT_SUPPORT_PARAMETERIZED_DECLARING_TYPES), getSourceLocation()));
            }
            HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor visitor8 = new HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor();
            this.signature.getThrowsPattern().traverse(visitor8, null);
            if (visitor8.wellHasItThen()) {
                scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.NO_GENERIC_THROWABLES), getSourceLocation()));
            }
            if (!scope.getWorld().isJoinpointArrayConstructionEnabled() && this.kind == Shadow.ConstructorCall && this.signature.getDeclaringType().isArray()) {
                scope.message(MessageUtil.warn(WeaverMessages.format(WeaverMessages.NO_NEWARRAY_JOINPOINTS_BY_DEFAULT), getSourceLocation()));
            }
        }
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected Test findResidueInternal(Shadow shadow, ExposedState state) {
        return match(shadow).alwaysTrue() ? Literal.TRUE : Literal.FALSE;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Pointcut concretize1(ResolvedType inAspect, ResolvedType declaringType, IntMap bindings) {
        Pointcut ret = new KindedPointcut(this.kind, this.signature, bindings.getEnclosingAdvice());
        ret.copyLocationFrom(this);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Pointcut parameterizeWith(Map typeVariableMap, World w) {
        Pointcut ret = new KindedPointcut(this.kind, this.signature.parameterizeWith((Map<String, UnresolvedType>) typeVariableMap, w), this.munger);
        ret.copyLocationFrom(this);
        return ret;
    }

    public Shadow.Kind getKind() {
        return this.kind;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
