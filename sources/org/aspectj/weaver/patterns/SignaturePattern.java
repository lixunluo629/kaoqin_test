package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.AjcMemberMaker;
import org.aspectj.weaver.AnnotationTargetKind;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ConcreteTypeMunger;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.JoinPointSignature;
import org.aspectj.weaver.JoinPointSignatureIterator;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.MemberKind;
import org.aspectj.weaver.NewFieldTypeMunger;
import org.aspectj.weaver.ResolvableTypeList;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/SignaturePattern.class */
public class SignaturePattern extends PatternNode implements ISignaturePattern {
    private MemberKind kind;
    private ModifiersPattern modifiers;
    private TypePattern returnType;
    private TypePattern declaringType;
    private NamePattern name;
    private TypePatternList parameterTypes;
    private static final int PARAMETER_ANNOTATION_MATCHING = 1;
    private static final int CHECKED_FOR_PARAMETER_ANNOTATION_MATCHING = 2;
    private ThrowsPattern throwsPattern;
    private AnnotationTypePattern annotationPattern;
    private transient boolean isExactDeclaringTypePattern;
    private int bits = 0;
    private transient int hashcode = -1;

    @Override // org.aspectj.weaver.patterns.ISignaturePattern
    public /* bridge */ /* synthetic */ ISignaturePattern parameterizeWith(Map map, World world) {
        return parameterizeWith((Map<String, UnresolvedType>) map, world);
    }

    public SignaturePattern(MemberKind kind, ModifiersPattern modifiers, TypePattern returnType, TypePattern declaringType, NamePattern name, TypePatternList parameterTypes, ThrowsPattern throwsPattern, AnnotationTypePattern annotationPattern) {
        this.isExactDeclaringTypePattern = false;
        this.kind = kind;
        this.modifiers = modifiers;
        this.returnType = returnType;
        this.name = name;
        this.declaringType = declaringType;
        this.parameterTypes = parameterTypes;
        this.throwsPattern = throwsPattern;
        this.annotationPattern = annotationPattern;
        this.isExactDeclaringTypePattern = declaringType instanceof ExactTypePattern;
    }

    @Override // org.aspectj.weaver.patterns.ISignaturePattern
    public SignaturePattern resolveBindings(IScope scope, Bindings bindings) {
        if (this.returnType != null) {
            this.returnType = this.returnType.resolveBindings(scope, bindings, false, false);
            checkForIncorrectTargetKind(this.returnType, scope, false);
        }
        if (this.declaringType != null) {
            this.declaringType = this.declaringType.resolveBindings(scope, bindings, false, false);
            checkForIncorrectTargetKind(this.declaringType, scope, false);
            this.isExactDeclaringTypePattern = this.declaringType instanceof ExactTypePattern;
        }
        if (this.parameterTypes != null) {
            this.parameterTypes = this.parameterTypes.resolveBindings(scope, bindings, false, false);
            checkForIncorrectTargetKind(this.parameterTypes, scope, false, true);
        }
        if (this.throwsPattern != null) {
            this.throwsPattern = this.throwsPattern.resolveBindings(scope, bindings);
            if (this.throwsPattern.getForbidden().getTypePatterns().length > 0 || this.throwsPattern.getRequired().getTypePatterns().length > 0) {
                checkForIncorrectTargetKind(this.throwsPattern, scope, false);
            }
        }
        if (this.annotationPattern != null) {
            this.annotationPattern = this.annotationPattern.resolveBindings(scope, bindings, false);
            checkForIncorrectTargetKind(this.annotationPattern, scope, true);
        }
        this.hashcode = -1;
        return this;
    }

    private void checkForIncorrectTargetKind(PatternNode patternNode, IScope scope, boolean targetsOtherThanTypeAllowed) {
        checkForIncorrectTargetKind(patternNode, scope, targetsOtherThanTypeAllowed, false);
    }

    private void checkForIncorrectTargetKind(PatternNode patternNode, IScope scope, boolean targetsOtherThanTypeAllowed, boolean parameterTargettingAnnotationsAllowed) {
        AnnotationTargetKind[] targetKinds;
        if (!scope.getWorld().isInJava5Mode() || scope.getWorld().getLint().unmatchedTargetKind == null || (patternNode instanceof AnyTypePattern)) {
            return;
        }
        if (patternNode instanceof ExactAnnotationTypePattern) {
            ResolvedType resolvedType = ((ExactAnnotationTypePattern) patternNode).getAnnotationType().resolve(scope.getWorld());
            if (targetsOtherThanTypeAllowed) {
                AnnotationTargetKind[] targetKinds2 = resolvedType.getAnnotationTargetKinds();
                if (targetKinds2 == null) {
                    return;
                }
                reportUnmatchedTargetKindMessage(targetKinds2, patternNode, scope, true);
                return;
            }
            if (targetsOtherThanTypeAllowed || resolvedType.canAnnotationTargetType() || (targetKinds = resolvedType.getAnnotationTargetKinds()) == null) {
                return;
            }
            reportUnmatchedTargetKindMessage(targetKinds, patternNode, scope, false);
            return;
        }
        TypePatternVisitor visitor = new TypePatternVisitor(scope, targetsOtherThanTypeAllowed, parameterTargettingAnnotationsAllowed);
        patternNode.traverse(visitor, null);
        if (visitor.containedIncorrectTargetKind()) {
            Set<ExactAnnotationTypePattern> keys = visitor.getIncorrectTargetKinds().keySet();
            for (PatternNode node : keys) {
                reportUnmatchedTargetKindMessage(visitor.getIncorrectTargetKinds().get(node), node, scope, false);
            }
        }
    }

    private void reportUnmatchedTargetKindMessage(AnnotationTargetKind[] annotationTargetKinds, PatternNode node, IScope scope, boolean checkMatchesMemberKindName) {
        StringBuffer targetNames = new StringBuffer("{");
        for (int i = 0; i < annotationTargetKinds.length; i++) {
            AnnotationTargetKind targetKind = annotationTargetKinds[i];
            if (checkMatchesMemberKindName && this.kind.getName().equals(targetKind.getName())) {
                return;
            }
            if (i < annotationTargetKinds.length - 1) {
                targetNames.append("ElementType." + targetKind.getName() + ",");
            } else {
                targetNames.append("ElementType." + targetKind.getName() + "}");
            }
        }
        scope.getWorld().getLint().unmatchedTargetKind.signal(new String[]{node.toString(), targetNames.toString()}, getSourceLocation(), new ISourceLocation[0]);
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/SignaturePattern$TypePatternVisitor.class */
    private class TypePatternVisitor extends AbstractPatternNodeVisitor {
        private IScope scope;
        private Map<ExactAnnotationTypePattern, AnnotationTargetKind[]> incorrectTargetKinds = new HashMap();
        private boolean targetsOtherThanTypeAllowed;
        private boolean parameterTargettingAnnotationsAllowed;

        public TypePatternVisitor(IScope scope, boolean targetsOtherThanTypeAllowed, boolean parameterTargettingAnnotationsAllowed) {
            this.scope = scope;
            this.targetsOtherThanTypeAllowed = targetsOtherThanTypeAllowed;
            this.parameterTargettingAnnotationsAllowed = parameterTargettingAnnotationsAllowed;
        }

        @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
        public Object visit(WildAnnotationTypePattern node, Object data) {
            node.getTypePattern().accept(this, data);
            return node;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
        public Object visit(ExactAnnotationTypePattern node, Object data) {
            ResolvedType resolvedType = node.getAnnotationType().resolve(this.scope.getWorld());
            if (this.targetsOtherThanTypeAllowed) {
                AnnotationTargetKind[] targetKinds = resolvedType.getAnnotationTargetKinds();
                if (targetKinds == null) {
                    return data;
                }
                List<AnnotationTargetKind> incorrectTargets = new ArrayList<>();
                for (int i = 0; i < targetKinds.length; i++) {
                    if (targetKinds[i].getName().equals(SignaturePattern.this.kind.getName()) || (targetKinds[i].getName().equals("PARAMETER") && node.isForParameterAnnotationMatch())) {
                        return data;
                    }
                    incorrectTargets.add(targetKinds[i]);
                }
                if (incorrectTargets.isEmpty()) {
                    return data;
                }
                AnnotationTargetKind[] kinds = new AnnotationTargetKind[incorrectTargets.size()];
                this.incorrectTargetKinds.put(node, incorrectTargets.toArray(kinds));
            } else if (!this.targetsOtherThanTypeAllowed && !resolvedType.canAnnotationTargetType()) {
                AnnotationTargetKind[] targetKinds2 = resolvedType.getAnnotationTargetKinds();
                if (targetKinds2 == null) {
                    return data;
                }
                if (this.parameterTargettingAnnotationsAllowed) {
                    for (AnnotationTargetKind annotationTargetKind : targetKinds2) {
                        if (annotationTargetKind.getName().equals("PARAMETER") && node.isForParameterAnnotationMatch()) {
                            return data;
                        }
                    }
                }
                this.incorrectTargetKinds.put(node, targetKinds2);
            }
            return data;
        }

        @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
        public Object visit(ExactTypePattern node, Object data) {
            ExactAnnotationTypePattern eatp = new ExactAnnotationTypePattern(node.getExactType().resolve(this.scope.getWorld()), null);
            eatp.accept(this, data);
            return data;
        }

        @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
        public Object visit(AndTypePattern node, Object data) {
            node.getLeft().accept(this, data);
            node.getRight().accept(this, data);
            return node;
        }

        @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
        public Object visit(OrTypePattern node, Object data) {
            node.getLeft().accept(this, data);
            node.getRight().accept(this, data);
            return node;
        }

        @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
        public Object visit(AnyWithAnnotationTypePattern node, Object data) {
            node.getAnnotationPattern().accept(this, data);
            return node;
        }

        public boolean containedIncorrectTargetKind() {
            return this.incorrectTargetKinds.size() != 0;
        }

        public Map<ExactAnnotationTypePattern, AnnotationTargetKind[]> getIncorrectTargetKinds() {
            return this.incorrectTargetKinds;
        }
    }

    public void postRead(ResolvedType enclosingType) {
        if (this.returnType != null) {
            this.returnType.postRead(enclosingType);
        }
        if (this.declaringType != null) {
            this.declaringType.postRead(enclosingType);
        }
        if (this.parameterTypes != null) {
            this.parameterTypes.postRead(enclosingType);
        }
    }

    @Override // org.aspectj.weaver.patterns.ISignaturePattern
    public SignaturePattern parameterizeWith(Map<String, UnresolvedType> typeVariableMap, World w) {
        SignaturePattern ret = new SignaturePattern(this.kind, this.modifiers, this.returnType.parameterizeWith(typeVariableMap, w), this.declaringType.parameterizeWith(typeVariableMap, w), this.name, this.parameterTypes.parameterizeWith(typeVariableMap, w), this.throwsPattern.parameterizeWith(typeVariableMap, w), this.annotationPattern.parameterizeWith(typeVariableMap, w));
        ret.copyLocationFrom(this);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.ISignaturePattern
    public boolean matches(Member joinPointSignature, World world, boolean allowBridgeMethods) {
        if (joinPointSignature == null || this.kind != joinPointSignature.getKind()) {
            return false;
        }
        if (this.kind == Member.ADVICE) {
            return true;
        }
        boolean subjectMatch = true;
        boolean wantsAnnotationMatch = wantToMatchAnnotationPattern();
        JoinPointSignatureIterator candidateMatches = joinPointSignature.getJoinPointSignatures(world);
        while (candidateMatches.hasNext()) {
            JoinPointSignature aSig = candidateMatches.next();
            FuzzyBoolean matchResult = matchesExactly(aSig, world, allowBridgeMethods, subjectMatch);
            if (matchResult.alwaysTrue()) {
                return true;
            }
            if (matchResult.alwaysFalse()) {
                return false;
            }
            subjectMatch = false;
            if (wantsAnnotationMatch) {
                return false;
            }
        }
        return false;
    }

    private FuzzyBoolean matchesExactly(JoinPointSignature aMember, World inAWorld, boolean allowBridgeMethods, boolean subjectMatch) {
        if (aMember.isBridgeMethod() && !allowBridgeMethods) {
            return FuzzyBoolean.MAYBE;
        }
        if (subjectMatch && !this.modifiers.matches(aMember.getModifiers())) {
            return FuzzyBoolean.NO;
        }
        FuzzyBoolean matchesIgnoringAnnotations = FuzzyBoolean.YES;
        if (this.kind == Member.STATIC_INITIALIZATION) {
            matchesIgnoringAnnotations = matchesExactlyStaticInitialization(aMember, inAWorld);
        } else if (this.kind == Member.FIELD) {
            matchesIgnoringAnnotations = matchesExactlyField(aMember, inAWorld);
        } else if (this.kind == Member.METHOD) {
            matchesIgnoringAnnotations = matchesExactlyMethod(aMember, inAWorld, subjectMatch);
        } else if (this.kind == Member.CONSTRUCTOR) {
            matchesIgnoringAnnotations = matchesExactlyConstructor(aMember, inAWorld);
        }
        if (matchesIgnoringAnnotations.alwaysFalse()) {
            return FuzzyBoolean.NO;
        }
        if (subjectMatch) {
            if (!matchesAnnotations(aMember, inAWorld).alwaysTrue()) {
                return FuzzyBoolean.NO;
            }
            return matchesIgnoringAnnotations;
        }
        if (this.annotationPattern instanceof AnyAnnotationTypePattern) {
            return matchesIgnoringAnnotations;
        }
        return FuzzyBoolean.NO;
    }

    private boolean wantToMatchAnnotationPattern() {
        return !(this.annotationPattern instanceof AnyAnnotationTypePattern);
    }

    private FuzzyBoolean matchesExactlyStaticInitialization(JoinPointSignature aMember, World world) {
        return FuzzyBoolean.fromBoolean(this.declaringType.matchesStatically(aMember.getDeclaringType().resolve(world)));
    }

    private FuzzyBoolean matchesExactlyField(JoinPointSignature aField, World world) {
        if (!this.name.matches(aField.getName())) {
            return FuzzyBoolean.NO;
        }
        ResolvedType fieldDeclaringType = aField.getDeclaringType().resolve(world);
        if (!this.declaringType.matchesStatically(fieldDeclaringType)) {
            return FuzzyBoolean.MAYBE;
        }
        if (!this.returnType.matchesStatically(aField.getReturnType().resolve(world)) && !this.returnType.matchesStatically(aField.getGenericReturnType().resolve(world))) {
            return FuzzyBoolean.MAYBE;
        }
        return FuzzyBoolean.YES;
    }

    private boolean parametersCannotMatch(JoinPointSignature methodJoinpoint) {
        if (methodJoinpoint.isVarargsMethod()) {
            return false;
        }
        int patternParameterCount = this.parameterTypes.size();
        if (patternParameterCount == 0 || this.parameterTypes.ellipsisCount == 0) {
            boolean equalCount = patternParameterCount == methodJoinpoint.getParameterTypes().length;
            if (patternParameterCount == 0 && !equalCount) {
                return true;
            }
            if (this.parameterTypes.ellipsisCount == 0 && !equalCount) {
                if (patternParameterCount > 0 && this.parameterTypes.get(patternParameterCount - 1).isVarArgs()) {
                    return false;
                }
                return true;
            }
            return false;
        }
        return false;
    }

    private FuzzyBoolean matchesExactlyMethod(JoinPointSignature aMethod, World world, boolean subjectMatch) {
        if (parametersCannotMatch(aMethod)) {
            return FuzzyBoolean.NO;
        }
        if (!this.name.matches(aMethod.getName())) {
            return FuzzyBoolean.NO;
        }
        if (subjectMatch && !this.throwsPattern.matches(aMethod.getExceptions(), world)) {
            return FuzzyBoolean.NO;
        }
        if (!this.declaringType.isStar() && !this.declaringType.matchesStatically(aMethod.getDeclaringType().resolve(world))) {
            return FuzzyBoolean.MAYBE;
        }
        if (!this.returnType.isStar()) {
            boolean b = this.returnType.isBangVoid();
            if (b) {
                String s = aMethod.getReturnType().getSignature();
                if (s.length() == 1 && s.charAt(0) == 'V') {
                    return FuzzyBoolean.NO;
                }
            } else if (this.returnType.isVoid()) {
                String s2 = aMethod.getReturnType().getSignature();
                if (s2.length() != 1 || s2.charAt(0) != 'V') {
                    return FuzzyBoolean.NO;
                }
            } else if (!this.returnType.matchesStatically(aMethod.getReturnType().resolve(world)) && !this.returnType.matchesStatically(aMethod.getGenericReturnType().resolve(world))) {
                return FuzzyBoolean.MAYBE;
            }
        }
        if (this.parameterTypes.size() == 1 && this.parameterTypes.get(0).isEllipsis()) {
            return FuzzyBoolean.YES;
        }
        if (!this.parameterTypes.canMatchSignatureWithNParameters(aMethod.getParameterTypes().length)) {
            return FuzzyBoolean.NO;
        }
        ResolvableTypeList rtl = new ResolvableTypeList(world, aMethod.getParameterTypes());
        ResolvedType[][] parameterAnnotationTypes = (ResolvedType[][]) null;
        if (isMatchingParameterAnnotations()) {
            parameterAnnotationTypes = aMethod.getParameterAnnotationTypes();
            if (parameterAnnotationTypes != null && parameterAnnotationTypes.length == 0) {
                parameterAnnotationTypes = (ResolvedType[][]) null;
            }
        }
        if (!this.parameterTypes.matches(rtl, TypePattern.STATIC, parameterAnnotationTypes).alwaysTrue() && !this.parameterTypes.matches(new ResolvableTypeList(world, aMethod.getGenericParameterTypes()), TypePattern.STATIC, parameterAnnotationTypes).alwaysTrue()) {
            return FuzzyBoolean.MAYBE;
        }
        if (!matchesVarArgs(aMethod, world)) {
            return FuzzyBoolean.MAYBE;
        }
        return FuzzyBoolean.YES;
    }

    private boolean isMatchingParameterAnnotations() {
        if ((this.bits & 2) == 0) {
            this.bits |= 2;
            int max = this.parameterTypes.size();
            for (int tp = 0; tp < max; tp++) {
                TypePattern typePattern = this.parameterTypes.get(tp);
                if (isParameterAnnotationMatching(typePattern)) {
                    this.bits |= 1;
                }
            }
        }
        return (this.bits & 1) != 0;
    }

    private boolean isParameterAnnotationMatching(TypePattern tp) {
        if (tp instanceof OrTypePattern) {
            OrTypePattern orAtp = (OrTypePattern) tp;
            return isParameterAnnotationMatching(orAtp.getLeft()) || isParameterAnnotationMatching(orAtp.getRight());
        }
        if (tp instanceof AndTypePattern) {
            AndTypePattern andAtp = (AndTypePattern) tp;
            return isParameterAnnotationMatching(andAtp.getLeft()) || isParameterAnnotationMatching(andAtp.getRight());
        }
        if (tp instanceof NotTypePattern) {
            NotTypePattern notAtp = (NotTypePattern) tp;
            return isParameterAnnotationMatching(notAtp.getNegatedPattern());
        }
        AnnotationTypePattern atp = tp.getAnnotationPattern();
        return isParameterAnnotationMatching(atp);
    }

    private boolean isParameterAnnotationMatching(AnnotationTypePattern tp) {
        if (tp instanceof OrAnnotationTypePattern) {
            OrAnnotationTypePattern orAtp = (OrAnnotationTypePattern) tp;
            return isParameterAnnotationMatching(orAtp.getLeft()) || isParameterAnnotationMatching(orAtp.getRight());
        }
        if (tp instanceof AndAnnotationTypePattern) {
            AndAnnotationTypePattern andAtp = (AndAnnotationTypePattern) tp;
            return isParameterAnnotationMatching(andAtp.getLeft()) || isParameterAnnotationMatching(andAtp.getRight());
        }
        if (tp instanceof NotAnnotationTypePattern) {
            NotAnnotationTypePattern notAtp = (NotAnnotationTypePattern) tp;
            return isParameterAnnotationMatching(notAtp.negatedPattern);
        }
        return tp.isForParameterAnnotationMatch();
    }

    private FuzzyBoolean matchesExactlyConstructor(JoinPointSignature aConstructor, World world) {
        if (!this.declaringType.matchesStatically(aConstructor.getDeclaringType().resolve(world))) {
            return FuzzyBoolean.NO;
        }
        if (!this.parameterTypes.canMatchSignatureWithNParameters(aConstructor.getParameterTypes().length)) {
            return FuzzyBoolean.NO;
        }
        ResolvedType[] resolvedParameters = world.resolve(aConstructor.getParameterTypes());
        ResolvedType[][] parameterAnnotationTypes = aConstructor.getParameterAnnotationTypes();
        if (parameterAnnotationTypes == null || parameterAnnotationTypes.length == 0) {
            parameterAnnotationTypes = (ResolvedType[][]) null;
        }
        if (!this.parameterTypes.matches(resolvedParameters, TypePattern.STATIC, parameterAnnotationTypes).alwaysTrue() && !this.parameterTypes.matches(world.resolve(aConstructor.getGenericParameterTypes()), TypePattern.STATIC).alwaysTrue()) {
            return FuzzyBoolean.MAYBE;
        }
        if (!matchesVarArgs(aConstructor, world)) {
            return FuzzyBoolean.NO;
        }
        if (!this.throwsPattern.matches(aConstructor.getExceptions(), world)) {
            return FuzzyBoolean.NO;
        }
        return FuzzyBoolean.YES;
    }

    private boolean matchesVarArgs(JoinPointSignature aMethodOrConstructor, World inAWorld) {
        if (this.parameterTypes.size() == 0) {
            return true;
        }
        TypePattern lastPattern = this.parameterTypes.get(this.parameterTypes.size() - 1);
        boolean canMatchVarArgsSignature = lastPattern.isStar() || lastPattern.isVarArgs() || lastPattern == TypePattern.ELLIPSIS;
        if (aMethodOrConstructor.isVarargsMethod()) {
            if (!canMatchVarArgsSignature) {
                inAWorld.getLint().cantMatchArrayTypeOnVarargs.signal(aMethodOrConstructor.toString(), getSourceLocation());
                return false;
            }
            return true;
        }
        if (lastPattern.isVarArgs()) {
            return false;
        }
        return true;
    }

    private FuzzyBoolean matchesAnnotations(ResolvedMember member, World world) {
        if (member == null) {
            return FuzzyBoolean.NO;
        }
        this.annotationPattern.resolve(world);
        if (this.annotationPattern instanceof AnyAnnotationTypePattern) {
            return FuzzyBoolean.YES;
        }
        if (member.isAnnotatedElsewhere() && member.getKind() == Member.FIELD) {
            List<ConcreteTypeMunger> mungers = member.getDeclaringType().resolve(world).getInterTypeMungers();
            for (ConcreteTypeMunger typeMunger : mungers) {
                if (typeMunger.getMunger() instanceof NewFieldTypeMunger) {
                    ResolvedMember fakerm = typeMunger.getSignature();
                    ResolvedMember ajcMethod = AjcMemberMaker.interFieldInitializer(fakerm, typeMunger.getAspectType());
                    ResolvedMember rmm = findMethod(typeMunger.getAspectType(), ajcMethod);
                    if (fakerm.equals(member)) {
                        member = rmm;
                    }
                }
            }
        }
        if (this.annotationPattern.matches(member).alwaysTrue()) {
            return FuzzyBoolean.YES;
        }
        return FuzzyBoolean.NO;
    }

    private ResolvedMember findMethod(ResolvedType aspectType, ResolvedMember ajcMethod) {
        ResolvedMember[] decMethods = aspectType.getDeclaredMethods();
        for (ResolvedMember member : decMethods) {
            if (member.equals(ajcMethod)) {
                return member;
            }
        }
        return null;
    }

    public boolean declaringTypeMatchAllowingForCovariance(Member member, UnresolvedType shadowDeclaringType, World world, TypePattern returnTypePattern, ResolvedType sigReturn) {
        ResolvedType onType = shadowDeclaringType.resolve(world);
        if (this.declaringType.matchesStatically(onType) && returnTypePattern.matchesStatically(sigReturn)) {
            return true;
        }
        Collection<ResolvedType> declaringTypes = member.getDeclaringTypes(world);
        for (ResolvedType type : declaringTypes) {
            if (this.declaringType.matchesStatically(type)) {
                if (1 == 0) {
                    return true;
                }
                ResolvedMember rm = type.lookupMethod(member);
                if (rm == null) {
                    rm = type.lookupMethodInITDs(member);
                }
                if (rm == null) {
                    continue;
                } else {
                    UnresolvedType returnTypeX = rm.getReturnType();
                    ResolvedType returnType = returnTypeX.resolve(world);
                    if (returnTypePattern.matchesStatically(returnType)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public NamePattern getName() {
        return this.name;
    }

    public TypePattern getDeclaringType() {
        return this.declaringType;
    }

    public MemberKind getKind() {
        return this.kind;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        if (this.annotationPattern != AnnotationTypePattern.ANY) {
            buf.append(this.annotationPattern.toString());
            buf.append(' ');
        }
        if (this.modifiers != ModifiersPattern.ANY) {
            buf.append(this.modifiers.toString());
            buf.append(' ');
        }
        if (this.kind == Member.STATIC_INITIALIZATION) {
            buf.append(this.declaringType.toString());
            buf.append(".<clinit>()");
        } else if (this.kind == Member.HANDLER) {
            buf.append("handler(");
            buf.append(this.parameterTypes.get(0));
            buf.append(")");
        } else {
            if (this.kind != Member.CONSTRUCTOR) {
                buf.append(this.returnType.toString());
                buf.append(' ');
            }
            if (this.declaringType != TypePattern.ANY) {
                buf.append(this.declaringType.toString());
                buf.append('.');
            }
            if (this.kind == Member.CONSTRUCTOR) {
                buf.append("new");
            } else {
                buf.append(this.name.toString());
            }
            if (this.kind == Member.METHOD || this.kind == Member.CONSTRUCTOR) {
                buf.append(this.parameterTypes.toString());
            }
        }
        return buf.toString();
    }

    public boolean equals(Object other) {
        if (!(other instanceof SignaturePattern)) {
            return false;
        }
        SignaturePattern o = (SignaturePattern) other;
        return o.kind.equals(this.kind) && o.modifiers.equals(this.modifiers) && o.returnType.equals(this.returnType) && o.declaringType.equals(this.declaringType) && o.name.equals(this.name) && o.parameterTypes.equals(this.parameterTypes) && o.throwsPattern.equals(this.throwsPattern) && o.annotationPattern.equals(this.annotationPattern);
    }

    public int hashCode() {
        if (this.hashcode == -1) {
            this.hashcode = 17;
            this.hashcode = (37 * this.hashcode) + this.kind.hashCode();
            this.hashcode = (37 * this.hashcode) + this.modifiers.hashCode();
            this.hashcode = (37 * this.hashcode) + this.returnType.hashCode();
            this.hashcode = (37 * this.hashcode) + this.declaringType.hashCode();
            this.hashcode = (37 * this.hashcode) + this.name.hashCode();
            this.hashcode = (37 * this.hashcode) + this.parameterTypes.hashCode();
            this.hashcode = (37 * this.hashcode) + this.throwsPattern.hashCode();
            this.hashcode = (37 * this.hashcode) + this.annotationPattern.hashCode();
        }
        return this.hashcode;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        this.kind.write(s);
        this.modifiers.write(s);
        this.returnType.write(s);
        this.declaringType.write(s);
        this.name.write(s);
        this.parameterTypes.write(s);
        this.throwsPattern.write(s);
        this.annotationPattern.write(s);
        writeLocation(s);
    }

    public static SignaturePattern read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        MemberKind kind = MemberKind.read(s);
        ModifiersPattern modifiers = ModifiersPattern.read(s);
        TypePattern returnType = TypePattern.read(s, context);
        TypePattern declaringType = TypePattern.read(s, context);
        NamePattern name = NamePattern.read(s);
        TypePatternList parameterTypes = TypePatternList.read(s, context);
        ThrowsPattern throwsPattern = ThrowsPattern.read(s, context);
        AnnotationTypePattern annotationPattern = AnnotationTypePattern.ANY;
        if (s.getMajorVersion() >= 2) {
            annotationPattern = AnnotationTypePattern.read(s, context);
        }
        SignaturePattern ret = new SignaturePattern(kind, modifiers, returnType, declaringType, name, parameterTypes, throwsPattern, annotationPattern);
        ret.readLocation(context, s);
        return ret;
    }

    public ModifiersPattern getModifiers() {
        return this.modifiers;
    }

    public TypePatternList getParameterTypes() {
        return this.parameterTypes;
    }

    public TypePattern getReturnType() {
        return this.returnType;
    }

    public ThrowsPattern getThrowsPattern() {
        return this.throwsPattern;
    }

    public AnnotationTypePattern getAnnotationPattern() {
        return this.annotationPattern;
    }

    @Override // org.aspectj.weaver.patterns.ISignaturePattern
    public boolean isStarAnnotation() {
        return this.annotationPattern == AnnotationTypePattern.ANY;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    public boolean isExactDeclaringTypePattern() {
        return this.isExactDeclaringTypePattern;
    }

    @Override // org.aspectj.weaver.patterns.ISignaturePattern
    public boolean isMatchOnAnyName() {
        return getName().isAny();
    }

    @Override // org.aspectj.weaver.patterns.ISignaturePattern
    public List<ExactTypePattern> getExactDeclaringTypes() {
        if (this.declaringType instanceof ExactTypePattern) {
            List<ExactTypePattern> l = new ArrayList<>();
            l.add((ExactTypePattern) this.declaringType);
            return l;
        }
        return Collections.emptyList();
    }

    @Override // org.aspectj.weaver.patterns.ISignaturePattern
    public boolean couldEverMatch(ResolvedType type) {
        return this.declaringType.matches(type, TypePattern.STATIC).maybeTrue();
    }
}
