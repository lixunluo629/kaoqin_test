package org.aspectj.weaver;

import java.util.Collections;
import java.util.List;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.weaver.AjAttribute;
import org.aspectj.weaver.Lint;
import org.aspectj.weaver.patterns.AndPointcut;
import org.aspectj.weaver.patterns.PerClause;
import org.aspectj.weaver.patterns.Pointcut;
import org.aspectj.weaver.patterns.TypePattern;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/Advice.class */
public abstract class Advice extends ShadowMunger {
    protected AjAttribute.AdviceAttribute attribute;
    protected transient AdviceKind kind;
    protected Member signature;
    private boolean isAnnotationStyle;
    protected ResolvedType concreteAspect;
    protected List<ShadowMunger> innerCflowEntries;
    protected int nFreeVars;
    protected TypePattern exceptionType;
    protected UnresolvedType[] bindingParameterTypes;
    protected boolean hasMatchedAtLeastOnce;
    protected List<Lint.Kind> suppressedLintKinds;
    public ISourceLocation lastReportedMonitorExitJoinpointLocation;
    private volatile int hashCode;
    public static final int ExtraArgument = 1;
    public static final int ThisJoinPoint = 2;
    public static final int ThisJoinPointStaticPart = 4;
    public static final int ThisEnclosingJoinPointStaticPart = 8;
    public static final int ParameterMask = 15;
    public static final int ConstantReference = 16;
    public static final int ConstantValue = 32;
    public static final int ThisAspectInstance = 64;

    public abstract boolean hasDynamicTests();

    public static Advice makeCflowEntry(World world, Pointcut entry, boolean isBelow, Member stackField, int nFreeVars, List<ShadowMunger> innerCflowEntries, ResolvedType inAspect) {
        Advice ret = world.createAdviceMunger(isBelow ? AdviceKind.CflowBelowEntry : AdviceKind.CflowEntry, entry, stackField, 0, entry, inAspect);
        ret.innerCflowEntries = innerCflowEntries;
        ret.nFreeVars = nFreeVars;
        ret.setDeclaringType(inAspect);
        return ret;
    }

    public static Advice makePerCflowEntry(World world, Pointcut entry, boolean isBelow, Member stackField, ResolvedType inAspect, List<ShadowMunger> innerCflowEntries) {
        Advice ret = world.createAdviceMunger(isBelow ? AdviceKind.PerCflowBelowEntry : AdviceKind.PerCflowEntry, entry, stackField, 0, entry, inAspect);
        ret.innerCflowEntries = innerCflowEntries;
        ret.concreteAspect = inAspect;
        return ret;
    }

    public static Advice makePerObjectEntry(World world, Pointcut entry, boolean isThis, ResolvedType inAspect) {
        Advice ret = world.createAdviceMunger(isThis ? AdviceKind.PerThisEntry : AdviceKind.PerTargetEntry, entry, null, 0, entry, inAspect);
        ret.concreteAspect = inAspect;
        return ret;
    }

    public static Advice makePerTypeWithinEntry(World world, Pointcut p, ResolvedType inAspect) {
        Advice ret = world.createAdviceMunger(AdviceKind.PerTypeWithinEntry, p, null, 0, p, inAspect);
        ret.concreteAspect = inAspect;
        return ret;
    }

    public static Advice makeSoftener(World world, Pointcut entry, TypePattern exceptionType, ResolvedType inAspect, IHasSourceLocation loc) {
        Advice ret = world.createAdviceMunger(AdviceKind.Softener, entry, null, 0, loc, inAspect);
        ret.exceptionType = exceptionType;
        return ret;
    }

    public Advice(AjAttribute.AdviceAttribute attribute, Pointcut pointcut, Member signature) {
        super(pointcut, attribute.getStart(), attribute.getEnd(), attribute.getSourceContext(), 1);
        this.innerCflowEntries = Collections.emptyList();
        this.hasMatchedAtLeastOnce = false;
        this.suppressedLintKinds = null;
        this.lastReportedMonitorExitJoinpointLocation = null;
        this.hashCode = 0;
        this.attribute = attribute;
        this.isAnnotationStyle = (signature == null || signature.getName().startsWith(NameMangler.PREFIX)) ? false : true;
        this.kind = attribute.getKind();
        this.signature = signature;
        if (signature != null) {
            this.bindingParameterTypes = signature.getParameterTypes();
        } else {
            this.bindingParameterTypes = new UnresolvedType[0];
        }
    }

    @Override // org.aspectj.weaver.ShadowMunger
    public boolean match(Shadow shadow, World world) {
        if (super.match(shadow, world)) {
            if (shadow.getKind() == Shadow.ExceptionHandler && (this.kind.isAfter() || this.kind == AdviceKind.Around)) {
                world.showMessage(IMessage.WARNING, WeaverMessages.format(WeaverMessages.ONLY_BEFORE_ON_HANDLER), getSourceLocation(), shadow.getSourceLocation());
                return false;
            }
            if ((shadow.getKind() == Shadow.SynchronizationLock || shadow.getKind() == Shadow.SynchronizationUnlock) && this.kind == AdviceKind.Around) {
                world.showMessage(IMessage.WARNING, WeaverMessages.format(WeaverMessages.NO_AROUND_ON_SYNCHRONIZATION), getSourceLocation(), shadow.getSourceLocation());
                return false;
            }
            if (hasExtraParameter() && this.kind == AdviceKind.AfterReturning) {
                ResolvedType resolvedExtraParameterType = getExtraParameterType().resolve(world);
                ResolvedType shadowReturnType = shadow.getReturnType().resolve(world);
                boolean matches = resolvedExtraParameterType.isConvertableFrom(shadowReturnType) && shadow.getKind().hasReturnValue();
                if (matches && resolvedExtraParameterType.isParameterizedType()) {
                    maybeIssueUncheckedMatchWarning(resolvedExtraParameterType, shadowReturnType, shadow, world);
                }
                return matches;
            }
            if (hasExtraParameter() && this.kind == AdviceKind.AfterThrowing) {
                ResolvedType exceptionType = getExtraParameterType().resolve(world);
                if (!exceptionType.isCheckedException() || exceptionType.getName().equals("java.lang.Exception")) {
                    return true;
                }
                UnresolvedType[] shadowThrows = shadow.getSignature().getExceptions(world);
                boolean matches2 = false;
                for (int i = 0; i < shadowThrows.length && !matches2; i++) {
                    ResolvedType type = shadowThrows[i].resolve(world);
                    if (exceptionType.isAssignableFrom(type)) {
                        matches2 = true;
                    }
                }
                return matches2;
            }
            if (this.kind == AdviceKind.PerTargetEntry) {
                return shadow.hasTarget();
            }
            if (this.kind == AdviceKind.PerThisEntry) {
                if (shadow.getEnclosingCodeSignature().getName().equals("<init>") && world.resolve(shadow.getEnclosingType()).isGroovyObject()) {
                    return false;
                }
                return shadow.hasThis();
            }
            if (this.kind == AdviceKind.Around) {
                if (shadow.getKind() == Shadow.PreInitialization) {
                    world.showMessage(IMessage.WARNING, WeaverMessages.format(WeaverMessages.AROUND_ON_PREINIT), getSourceLocation(), shadow.getSourceLocation());
                    return false;
                }
                if (shadow.getKind() == Shadow.Initialization) {
                    world.showMessage(IMessage.WARNING, WeaverMessages.format(WeaverMessages.AROUND_ON_INIT), getSourceLocation(), shadow.getSourceLocation());
                    return false;
                }
                if (shadow.getKind() == Shadow.StaticInitialization && shadow.getEnclosingType().resolve(world).isInterface()) {
                    world.showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.AROUND_ON_INTERFACE_STATICINIT, shadow.getEnclosingType().getName()), getSourceLocation(), shadow.getSourceLocation());
                    return false;
                }
                if (getSignature().getReturnType().equals(UnresolvedType.VOID)) {
                    if (!shadow.getReturnType().equals(UnresolvedType.VOID)) {
                        String s = shadow.toString();
                        String s2 = WeaverMessages.format(WeaverMessages.NON_VOID_RETURN, s);
                        world.showMessage(IMessage.ERROR, s2, getSourceLocation(), shadow.getSourceLocation());
                        return false;
                    }
                    return true;
                }
                if (getSignature().getReturnType().equals(UnresolvedType.OBJECT)) {
                    return true;
                }
                ResolvedType shadowReturnType2 = shadow.getReturnType().resolve(world);
                ResolvedType adviceReturnType = getSignature().getGenericReturnType().resolve(world);
                if (!shadowReturnType2.isParameterizedType() || !adviceReturnType.isRawType()) {
                    if (!shadowReturnType2.isAssignableFrom(adviceReturnType)) {
                        world.showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.INCOMPATIBLE_RETURN_TYPE, shadow), getSourceLocation(), shadow.getSourceLocation());
                        return false;
                    }
                    return true;
                }
                ResolvedType shadowReturnGenericType = shadowReturnType2.getGenericType();
                ResolvedType adviceReturnGenericType = adviceReturnType.getGenericType();
                if (shadowReturnGenericType.isAssignableFrom(adviceReturnGenericType) && world.getLint().uncheckedAdviceConversion.isEnabled()) {
                    world.getLint().uncheckedAdviceConversion.signal(new String[]{shadow.toString(), shadowReturnType2.getName(), adviceReturnType.getName()}, shadow.getSourceLocation(), new ISourceLocation[]{getSourceLocation()});
                    return true;
                }
                return true;
            }
            return true;
        }
        return false;
    }

    private void maybeIssueUncheckedMatchWarning(ResolvedType afterReturningType, ResolvedType shadowReturnType, Shadow shadow, World world) {
        boolean inDoubt = !afterReturningType.isAssignableFrom(shadowReturnType);
        if (inDoubt && world.getLint().uncheckedArgument.isEnabled()) {
            String uncheckedMatchWith = afterReturningType.getSimpleBaseName();
            if (shadowReturnType.isParameterizedType() && shadowReturnType.getRawType() == afterReturningType.getRawType()) {
                uncheckedMatchWith = shadowReturnType.getSimpleName();
            }
            if (!Utils.isSuppressing(getSignature().getAnnotations(), "uncheckedArgument")) {
                world.getLint().uncheckedArgument.signal(new String[]{afterReturningType.getSimpleName(), uncheckedMatchWith, afterReturningType.getSimpleBaseName(), shadow.toResolvedString(world)}, getSourceLocation(), new ISourceLocation[]{shadow.getSourceLocation()});
            }
        }
    }

    public AdviceKind getKind() {
        return this.kind;
    }

    public Member getSignature() {
        return this.signature;
    }

    public boolean hasExtraParameter() {
        return (getExtraParameterFlags() & 1) != 0;
    }

    protected int getExtraParameterFlags() {
        return this.attribute.getExtraParameterFlags();
    }

    protected int getExtraParameterCount() {
        return countOnes(getExtraParameterFlags() & 15);
    }

    public UnresolvedType[] getBindingParameterTypes() {
        return this.bindingParameterTypes;
    }

    public void setBindingParameterTypes(UnresolvedType[] types) {
        this.bindingParameterTypes = types;
    }

    public static int countOnes(int bits) {
        int ret = 0;
        while (bits != 0) {
            if ((bits & 1) != 0) {
                ret++;
            }
            bits >>= 1;
        }
        return ret;
    }

    public int getBaseParameterCount() {
        return getSignature().getParameterTypes().length - getExtraParameterCount();
    }

    public String[] getBaseParameterNames(World world) {
        String[] allNames = getSignature().getParameterNames(world);
        int extras = getExtraParameterCount();
        if (extras == 0) {
            return allNames;
        }
        String[] result = new String[getBaseParameterCount()];
        for (int i = 0; i < result.length; i++) {
            result[i] = allNames[i];
        }
        return result;
    }

    public UnresolvedType getExtraParameterType() {
        if (!hasExtraParameter()) {
            return ResolvedType.MISSING;
        }
        if (this.signature instanceof ResolvedMember) {
            ResolvedMember method = (ResolvedMember) this.signature;
            UnresolvedType[] parameterTypes = method.getGenericParameterTypes();
            if (getConcreteAspect().isAnnotationStyleAspect()) {
                String[] pnames = method.getParameterNames();
                if (pnames != null) {
                    AnnotationAJ[] annos = getSignature().getAnnotations();
                    String parameterToLookup = null;
                    if (annos != null && (getKind() == AdviceKind.AfterThrowing || getKind() == AdviceKind.AfterReturning)) {
                        for (int i = 0; i < annos.length && parameterToLookup == null; i++) {
                            AnnotationAJ anno = annos[i];
                            String annosig = anno.getType().getSignature();
                            if (annosig.equals("Lorg/aspectj/lang/annotation/AfterThrowing;")) {
                                parameterToLookup = anno.getStringFormOfValue("throwing");
                            } else if (annosig.equals("Lorg/aspectj/lang/annotation/AfterReturning;")) {
                                parameterToLookup = anno.getStringFormOfValue("returning");
                            }
                        }
                    }
                    if (parameterToLookup != null) {
                        for (int i2 = 0; i2 < pnames.length; i2++) {
                            if (pnames[i2].equals(parameterToLookup)) {
                                return parameterTypes[i2];
                            }
                        }
                    }
                }
                int baseParmCnt = getBaseParameterCount();
                while (baseParmCnt + 1 < parameterTypes.length && (parameterTypes[baseParmCnt].equals(AjcMemberMaker.TYPEX_JOINPOINT) || parameterTypes[baseParmCnt].equals(AjcMemberMaker.TYPEX_STATICJOINPOINT) || parameterTypes[baseParmCnt].equals(AjcMemberMaker.TYPEX_ENCLOSINGSTATICJOINPOINT))) {
                    baseParmCnt++;
                }
                return parameterTypes[baseParmCnt];
            }
            return parameterTypes[getBaseParameterCount()];
        }
        return this.signature.getParameterTypes()[getBaseParameterCount()];
    }

    public UnresolvedType getDeclaringAspect() {
        return getOriginalSignature().getDeclaringType();
    }

    protected Member getOriginalSignature() {
        return this.signature;
    }

    protected String extraParametersToString() {
        if (getExtraParameterFlags() == 0) {
            return "";
        }
        return "(extraFlags: " + getExtraParameterFlags() + ")";
    }

    @Override // org.aspectj.weaver.ShadowMunger
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override // org.aspectj.weaver.ShadowMunger
    public ShadowMunger concretize(ResolvedType fromType, World world, PerClause clause) {
        Pointcut p = this.pointcut.concretize(fromType, getDeclaringType(), this.signature.getArity(), this);
        if (clause != null) {
            p = new AndPointcut(clause, p);
            p.copyLocationFrom(p);
            p.state = Pointcut.CONCRETE;
            p.m_ignoreUnboundBindingForNames = p.m_ignoreUnboundBindingForNames;
        }
        Advice munger = world.getWeavingSupport().createAdviceMunger(this.attribute, p, this.signature, fromType);
        munger.bindingParameterTypes = this.bindingParameterTypes;
        munger.setDeclaringType(getDeclaringType());
        return munger;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("(").append(getKind()).append(extraParametersToString());
        sb.append(": ").append(this.pointcut).append("->").append(this.signature).append(")");
        return sb.toString();
    }

    public boolean equals(Object other) {
        if (!(other instanceof Advice)) {
            return false;
        }
        Advice o = (Advice) other;
        return o.kind.equals(this.kind) && (o.pointcut != null ? o.pointcut.equals(this.pointcut) : this.pointcut == null) && (o.signature != null ? o.signature.equals(this.signature) : this.signature == null);
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            int result = (37 * 17) + this.kind.hashCode();
            this.hashCode = (37 * ((37 * result) + (this.pointcut == null ? 0 : this.pointcut.hashCode()))) + (this.signature == null ? 0 : this.signature.hashCode());
        }
        return this.hashCode;
    }

    public void setLexicalPosition(int lexicalPosition) {
        this.start = lexicalPosition;
    }

    public boolean isAnnotationStyle() {
        return this.isAnnotationStyle;
    }

    @Override // org.aspectj.weaver.ShadowMunger
    public ResolvedType getConcreteAspect() {
        return this.concreteAspect;
    }

    public boolean hasMatchedSomething() {
        return this.hasMatchedAtLeastOnce;
    }

    public void setHasMatchedSomething(boolean hasMatchedSomething) {
        this.hasMatchedAtLeastOnce = hasMatchedSomething;
    }
}
