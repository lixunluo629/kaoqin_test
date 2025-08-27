package org.aspectj.weaver.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedMemberImpl;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.World;
import org.aspectj.weaver.ast.Var;
import org.aspectj.weaver.tools.MatchingContext;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/reflect/StandardShadow.class */
public class StandardShadow extends Shadow {
    private final World world;
    private final ResolvedType enclosingType;
    private final ResolvedMember enclosingMember;
    private final MatchingContext matchContext;
    private Var thisVar;
    private Var targetVar;
    private Var[] argsVars;
    private Var atThisVar;
    private Var atTargetVar;
    private Map atArgsVars;
    private Map withinAnnotationVar;
    private Map withinCodeAnnotationVar;
    private Map annotationVar;
    private AnnotationFinder annotationFinder;

    public static Shadow makeExecutionShadow(World inWorld, Member forMethod, MatchingContext withContext) {
        Shadow.Kind kind = forMethod instanceof Method ? Shadow.MethodExecution : Shadow.ConstructorExecution;
        org.aspectj.weaver.Member signature = ReflectionBasedReferenceTypeDelegateFactory.createResolvedMember(forMethod, inWorld);
        ResolvedType enclosingType = signature.getDeclaringType().resolve(inWorld);
        return new StandardShadow(inWorld, kind, signature, null, enclosingType, null, withContext);
    }

    public static Shadow makeExecutionShadow(World inWorld, ResolvedMember forMethod, MatchingContext withContext) {
        Shadow.Kind kind = forMethod.getName().equals("<init>") ? Shadow.ConstructorExecution : Shadow.MethodExecution;
        return new StandardShadow(inWorld, kind, forMethod, null, (ResolvedType) forMethod.getDeclaringType(), null, withContext);
    }

    public static Shadow makeAdviceExecutionShadow(World inWorld, Method forMethod, MatchingContext withContext) {
        Shadow.Kind kind = Shadow.AdviceExecution;
        org.aspectj.weaver.Member signature = ReflectionBasedReferenceTypeDelegateFactory.createResolvedAdviceMember(forMethod, inWorld);
        ResolvedType enclosingType = signature.getDeclaringType().resolve(inWorld);
        return new StandardShadow(inWorld, kind, signature, null, enclosingType, null, withContext);
    }

    public static Shadow makeCallShadow(World inWorld, ResolvedMember aMember, ResolvedMember withinCode, MatchingContext withContext) {
        Shadow enclosingShadow = makeExecutionShadow(inWorld, withinCode, withContext);
        Shadow.Kind kind = !aMember.getName().equals("<init>") ? Shadow.MethodCall : Shadow.ConstructorCall;
        return new StandardShadow(inWorld, kind, aMember, enclosingShadow, (ResolvedType) withinCode.getDeclaringType(), withinCode, withContext);
    }

    public static Shadow makeCallShadow(World inWorld, Member aMember, Class thisClass, MatchingContext withContext) {
        Shadow enclosingShadow = makeStaticInitializationShadow(inWorld, thisClass, withContext);
        org.aspectj.weaver.Member signature = ReflectionBasedReferenceTypeDelegateFactory.createResolvedMember(aMember, inWorld);
        ResolvedMember enclosingMember = ReflectionBasedReferenceTypeDelegateFactory.createStaticInitMember(thisClass, inWorld);
        ResolvedType enclosingType = enclosingMember.getDeclaringType().resolve(inWorld);
        Shadow.Kind kind = aMember instanceof Method ? Shadow.MethodCall : Shadow.ConstructorCall;
        return new StandardShadow(inWorld, kind, signature, enclosingShadow, enclosingType, enclosingMember, withContext);
    }

    public static Shadow makeStaticInitializationShadow(World inWorld, Class forType, MatchingContext withContext) {
        org.aspectj.weaver.Member signature = ReflectionBasedReferenceTypeDelegateFactory.createStaticInitMember(forType, inWorld);
        ResolvedType enclosingType = signature.getDeclaringType().resolve(inWorld);
        Shadow.Kind kind = Shadow.StaticInitialization;
        return new StandardShadow(inWorld, kind, signature, null, enclosingType, null, withContext);
    }

    public static Shadow makeStaticInitializationShadow(World inWorld, ResolvedType forType, MatchingContext withContext) {
        ResolvedMember[] members = forType.getDeclaredMethods();
        int clinit = -1;
        for (int i = 0; i < members.length && clinit == -1; i++) {
            if (members[i].getName().equals("<clinit>")) {
                clinit = i;
            }
        }
        Shadow.Kind kind = Shadow.StaticInitialization;
        if (clinit == -1) {
            org.aspectj.weaver.Member clinitMember = new ResolvedMemberImpl(org.aspectj.weaver.Member.STATIC_INITIALIZATION, forType, 8, UnresolvedType.VOID, "<clinit>", new UnresolvedType[0], new UnresolvedType[0]);
            return new StandardShadow(inWorld, kind, clinitMember, null, forType, null, withContext);
        }
        return new StandardShadow(inWorld, kind, members[clinit], null, forType, null, withContext);
    }

    public static Shadow makePreInitializationShadow(World inWorld, Constructor forConstructor, MatchingContext withContext) {
        Shadow.Kind kind = Shadow.PreInitialization;
        org.aspectj.weaver.Member signature = ReflectionBasedReferenceTypeDelegateFactory.createResolvedMember(forConstructor, inWorld);
        ResolvedType enclosingType = signature.getDeclaringType().resolve(inWorld);
        return new StandardShadow(inWorld, kind, signature, null, enclosingType, null, withContext);
    }

    public static Shadow makeInitializationShadow(World inWorld, Constructor forConstructor, MatchingContext withContext) {
        Shadow.Kind kind = Shadow.Initialization;
        org.aspectj.weaver.Member signature = ReflectionBasedReferenceTypeDelegateFactory.createResolvedMember(forConstructor, inWorld);
        ResolvedType enclosingType = signature.getDeclaringType().resolve(inWorld);
        return new StandardShadow(inWorld, kind, signature, null, enclosingType, null, withContext);
    }

    public static Shadow makeHandlerShadow(World inWorld, Class exceptionType, Class withinType, MatchingContext withContext) {
        Shadow.Kind kind = Shadow.ExceptionHandler;
        Shadow enclosingShadow = makeStaticInitializationShadow(inWorld, withinType, withContext);
        org.aspectj.weaver.Member signature = ReflectionBasedReferenceTypeDelegateFactory.createHandlerMember(exceptionType, withinType, inWorld);
        ResolvedMember enclosingMember = ReflectionBasedReferenceTypeDelegateFactory.createStaticInitMember(withinType, inWorld);
        ResolvedType enclosingType = enclosingMember.getDeclaringType().resolve(inWorld);
        return new StandardShadow(inWorld, kind, signature, enclosingShadow, enclosingType, enclosingMember, withContext);
    }

    public static Shadow makeHandlerShadow(World inWorld, Class exceptionType, Member withinCode, MatchingContext withContext) {
        Shadow.Kind kind = Shadow.ExceptionHandler;
        Shadow enclosingShadow = makeExecutionShadow(inWorld, withinCode, withContext);
        org.aspectj.weaver.Member signature = ReflectionBasedReferenceTypeDelegateFactory.createHandlerMember(exceptionType, withinCode.getDeclaringClass(), inWorld);
        ResolvedMember enclosingMember = ReflectionBasedReferenceTypeDelegateFactory.createResolvedMember(withinCode, inWorld);
        ResolvedType enclosingType = enclosingMember.getDeclaringType().resolve(inWorld);
        return new StandardShadow(inWorld, kind, signature, enclosingShadow, enclosingType, enclosingMember, withContext);
    }

    public static Shadow makeFieldGetShadow(World inWorld, Field forField, Class callerType, MatchingContext withContext) {
        Shadow enclosingShadow = makeStaticInitializationShadow(inWorld, callerType, withContext);
        org.aspectj.weaver.Member signature = ReflectionBasedReferenceTypeDelegateFactory.createResolvedField(forField, inWorld);
        ResolvedMember enclosingMember = ReflectionBasedReferenceTypeDelegateFactory.createStaticInitMember(callerType, inWorld);
        ResolvedType enclosingType = enclosingMember.getDeclaringType().resolve(inWorld);
        Shadow.Kind kind = Shadow.FieldGet;
        return new StandardShadow(inWorld, kind, signature, enclosingShadow, enclosingType, enclosingMember, withContext);
    }

    public static Shadow makeFieldGetShadow(World inWorld, Field forField, Member inMember, MatchingContext withContext) {
        Shadow enclosingShadow = makeExecutionShadow(inWorld, inMember, withContext);
        org.aspectj.weaver.Member signature = ReflectionBasedReferenceTypeDelegateFactory.createResolvedField(forField, inWorld);
        ResolvedMember enclosingMember = ReflectionBasedReferenceTypeDelegateFactory.createResolvedMember(inMember, inWorld);
        ResolvedType enclosingType = enclosingMember.getDeclaringType().resolve(inWorld);
        Shadow.Kind kind = Shadow.FieldGet;
        return new StandardShadow(inWorld, kind, signature, enclosingShadow, enclosingType, enclosingMember, withContext);
    }

    public static Shadow makeFieldSetShadow(World inWorld, Field forField, Class callerType, MatchingContext withContext) {
        Shadow enclosingShadow = makeStaticInitializationShadow(inWorld, callerType, withContext);
        org.aspectj.weaver.Member signature = ReflectionBasedReferenceTypeDelegateFactory.createResolvedField(forField, inWorld);
        ResolvedMember enclosingMember = ReflectionBasedReferenceTypeDelegateFactory.createStaticInitMember(callerType, inWorld);
        ResolvedType enclosingType = enclosingMember.getDeclaringType().resolve(inWorld);
        Shadow.Kind kind = Shadow.FieldSet;
        return new StandardShadow(inWorld, kind, signature, enclosingShadow, enclosingType, enclosingMember, withContext);
    }

    public static Shadow makeFieldSetShadow(World inWorld, Field forField, Member inMember, MatchingContext withContext) {
        Shadow enclosingShadow = makeExecutionShadow(inWorld, inMember, withContext);
        org.aspectj.weaver.Member signature = ReflectionBasedReferenceTypeDelegateFactory.createResolvedField(forField, inWorld);
        ResolvedMember enclosingMember = ReflectionBasedReferenceTypeDelegateFactory.createResolvedMember(inMember, inWorld);
        ResolvedType enclosingType = enclosingMember.getDeclaringType().resolve(inWorld);
        Shadow.Kind kind = Shadow.FieldSet;
        return new StandardShadow(inWorld, kind, signature, enclosingShadow, enclosingType, enclosingMember, withContext);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public StandardShadow(World world, Shadow.Kind kind, org.aspectj.weaver.Member signature, Shadow enclosingShadow, ResolvedType enclosingType, ResolvedMember enclosingMember, MatchingContext withContext) {
        super(kind, signature, enclosingShadow);
        this.thisVar = null;
        this.targetVar = null;
        this.argsVars = null;
        this.atThisVar = null;
        this.atTargetVar = null;
        this.atArgsVars = new HashMap();
        this.withinAnnotationVar = new HashMap();
        this.withinCodeAnnotationVar = new HashMap();
        this.annotationVar = new HashMap();
        this.world = world;
        this.enclosingType = enclosingType;
        this.enclosingMember = enclosingMember;
        this.matchContext = withContext;
        if (world instanceof IReflectionWorld) {
            this.annotationFinder = ((IReflectionWorld) world).getAnnotationFinder();
        }
    }

    @Override // org.aspectj.weaver.Shadow
    public World getIWorld() {
        return this.world;
    }

    @Override // org.aspectj.weaver.Shadow
    public Var getThisVar() {
        if (this.thisVar == null && hasThis()) {
            this.thisVar = ReflectionVar.createThisVar(getThisType().resolve(this.world), this.annotationFinder);
        }
        return this.thisVar;
    }

    @Override // org.aspectj.weaver.Shadow
    public Var getTargetVar() {
        if (this.targetVar == null && hasTarget()) {
            this.targetVar = ReflectionVar.createTargetVar(getThisType().resolve(this.world), this.annotationFinder);
        }
        return this.targetVar;
    }

    @Override // org.aspectj.weaver.Shadow
    public UnresolvedType getEnclosingType() {
        return this.enclosingType;
    }

    @Override // org.aspectj.weaver.Shadow
    public Var getArgVar(int i) {
        if (this.argsVars == null) {
            this.argsVars = new Var[getArgCount()];
            for (int j = 0; j < this.argsVars.length; j++) {
                this.argsVars[j] = ReflectionVar.createArgsVar(getArgType(j).resolve(this.world), j, this.annotationFinder);
            }
        }
        if (i < this.argsVars.length) {
            return this.argsVars[i];
        }
        return null;
    }

    @Override // org.aspectj.weaver.Shadow
    public Var getThisJoinPointVar() {
        return null;
    }

    @Override // org.aspectj.weaver.Shadow
    public Var getThisJoinPointStaticPartVar() {
        return null;
    }

    @Override // org.aspectj.weaver.Shadow
    public Var getThisEnclosingJoinPointStaticPartVar() {
        return null;
    }

    @Override // org.aspectj.weaver.Shadow
    public Var getThisAspectInstanceVar(ResolvedType aspectType) {
        return null;
    }

    @Override // org.aspectj.weaver.Shadow
    public Var getKindedAnnotationVar(UnresolvedType forAnnotationType) {
        ResolvedType annType = forAnnotationType.resolve(this.world);
        if (this.annotationVar.get(annType) == null) {
            this.annotationVar.put(annType, ReflectionVar.createAtAnnotationVar(annType, this.annotationFinder));
        }
        return (Var) this.annotationVar.get(annType);
    }

    @Override // org.aspectj.weaver.Shadow
    public Var getWithinAnnotationVar(UnresolvedType forAnnotationType) {
        ResolvedType annType = forAnnotationType.resolve(this.world);
        if (this.withinAnnotationVar.get(annType) == null) {
            this.withinAnnotationVar.put(annType, ReflectionVar.createWithinAnnotationVar(annType, this.annotationFinder));
        }
        return (Var) this.withinAnnotationVar.get(annType);
    }

    @Override // org.aspectj.weaver.Shadow
    public Var getWithinCodeAnnotationVar(UnresolvedType forAnnotationType) {
        ResolvedType annType = forAnnotationType.resolve(this.world);
        if (this.withinCodeAnnotationVar.get(annType) == null) {
            this.withinCodeAnnotationVar.put(annType, ReflectionVar.createWithinCodeAnnotationVar(annType, this.annotationFinder));
        }
        return (Var) this.withinCodeAnnotationVar.get(annType);
    }

    @Override // org.aspectj.weaver.Shadow
    public Var getThisAnnotationVar(UnresolvedType forAnnotationType) {
        if (this.atThisVar == null) {
            this.atThisVar = ReflectionVar.createThisAnnotationVar(forAnnotationType.resolve(this.world), this.annotationFinder);
        }
        return this.atThisVar;
    }

    @Override // org.aspectj.weaver.Shadow
    public Var getTargetAnnotationVar(UnresolvedType forAnnotationType) {
        if (this.atTargetVar == null) {
            this.atTargetVar = ReflectionVar.createTargetAnnotationVar(forAnnotationType.resolve(this.world), this.annotationFinder);
        }
        return this.atTargetVar;
    }

    @Override // org.aspectj.weaver.Shadow
    public Var getArgAnnotationVar(int i, UnresolvedType forAnnotationType) {
        ResolvedType annType = forAnnotationType.resolve(this.world);
        if (this.atArgsVars.get(annType) == null) {
            this.atArgsVars.put(annType, new Var[getArgCount()]);
        }
        Var[] vars = (Var[]) this.atArgsVars.get(annType);
        if (i > vars.length - 1) {
            return null;
        }
        if (vars[i] == null) {
            vars[i] = ReflectionVar.createArgsAnnotationVar(annType, i, this.annotationFinder);
        }
        return vars[i];
    }

    @Override // org.aspectj.weaver.Shadow
    public org.aspectj.weaver.Member getEnclosingCodeSignature() {
        if (getKind().isEnclosingKind()) {
            return getSignature();
        }
        if (getKind() == Shadow.PreInitialization) {
            return getSignature();
        }
        if (this.enclosingShadow == null) {
            return this.enclosingMember;
        }
        return this.enclosingShadow.getSignature();
    }

    @Override // org.aspectj.weaver.Shadow
    public ISourceLocation getSourceLocation() {
        return null;
    }

    public MatchingContext getMatchingContext() {
        return this.matchContext;
    }
}
