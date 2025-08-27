package org.aspectj.weaver;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.util.PartialOrder;
import org.aspectj.util.TypeSafeEnum;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.PropertyAccessor;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/Shadow.class */
public abstract class Shadow {
    private final Kind kind;
    private final Member signature;
    private Member matchingSignature;
    private ResolvedMember resolvedSignature;
    protected final Shadow enclosingShadow;
    protected List<ShadowMunger> mungers = Collections.emptyList();
    public int shadowId;
    public static final int MethodCallBit = 2;
    public static final int ConstructorCallBit = 4;
    public static final int MethodExecutionBit = 8;
    public static final int ConstructorExecutionBit = 16;
    public static final int FieldGetBit = 32;
    public static final int FieldSetBit = 64;
    public static final int StaticInitializationBit = 128;
    public static final int PreInitializationBit = 256;
    public static final int AdviceExecutionBit = 512;
    public static final int InitializationBit = 1024;
    public static final int ExceptionHandlerBit = 2048;
    public static final int SynchronizationLockBit = 4096;
    public static final int SynchronizationUnlockBit = 8192;
    public static final int MAX_SHADOW_KIND = 13;
    private static int nextShadowID = 100;
    public static String METHOD_EXECUTION = JoinPoint.METHOD_EXECUTION;
    public static String METHOD_CALL = JoinPoint.METHOD_CALL;
    public static String CONSTRUCTOR_EXECUTION = JoinPoint.CONSTRUCTOR_EXECUTION;
    public static String CONSTRUCTOR_CALL = JoinPoint.CONSTRUCTOR_CALL;
    public static String FIELD_GET = JoinPoint.FIELD_GET;
    public static String FIELD_SET = JoinPoint.FIELD_SET;
    public static String STATICINITIALIZATION = JoinPoint.STATICINITIALIZATION;
    public static String PREINITIALIZATION = JoinPoint.PREINITIALIZATION;
    public static String INITIALIZATION = JoinPoint.INITIALIZATION;
    public static String EXCEPTION_HANDLER = JoinPoint.EXCEPTION_HANDLER;
    public static String SYNCHRONIZATION_LOCK = JoinPoint.SYNCHRONIZATION_LOCK;
    public static String SYNCHRONIZATION_UNLOCK = JoinPoint.SYNCHRONIZATION_UNLOCK;
    public static String ADVICE_EXECUTION = JoinPoint.ADVICE_EXECUTION;
    public static final Kind MethodCall = new Kind(METHOD_CALL, 1, true);
    public static final Kind ConstructorCall = new Kind(CONSTRUCTOR_CALL, 2, true);
    public static final Kind MethodExecution = new Kind(METHOD_EXECUTION, 3, false);
    public static final Kind ConstructorExecution = new Kind(CONSTRUCTOR_EXECUTION, 4, false);
    public static final Kind FieldGet = new Kind(FIELD_GET, 5, true);
    public static final Kind FieldSet = new Kind(FIELD_SET, 6, true);
    public static final Kind StaticInitialization = new Kind(STATICINITIALIZATION, 7, false);
    public static final Kind PreInitialization = new Kind(PREINITIALIZATION, 8, false);
    public static final Kind AdviceExecution = new Kind(ADVICE_EXECUTION, 9, false);
    public static final Kind Initialization = new Kind(INITIALIZATION, 10, false);
    public static final Kind ExceptionHandler = new Kind(EXCEPTION_HANDLER, 11, true);
    public static final Kind SynchronizationLock = new Kind(SYNCHRONIZATION_LOCK, 12, true);
    public static final Kind SynchronizationUnlock = new Kind(SYNCHRONIZATION_UNLOCK, 13, true);
    public static final Kind[] SHADOW_KINDS = {MethodCall, ConstructorCall, MethodExecution, ConstructorExecution, FieldGet, FieldSet, StaticInitialization, PreInitialization, AdviceExecution, Initialization, ExceptionHandler, SynchronizationLock, SynchronizationUnlock};
    public static final int ALL_SHADOW_KINDS_BITS = 16382;
    public static final int NO_SHADOW_KINDS_BITS = 0;

    public abstract World getIWorld();

    public abstract Var getThisVar();

    public abstract Var getTargetVar();

    public abstract UnresolvedType getEnclosingType();

    public abstract Var getArgVar(int i);

    public abstract Var getThisJoinPointVar();

    public abstract Var getThisJoinPointStaticPartVar();

    public abstract Var getThisEnclosingJoinPointStaticPartVar();

    public abstract Var getThisAspectInstanceVar(ResolvedType resolvedType);

    public abstract Var getKindedAnnotationVar(UnresolvedType unresolvedType);

    public abstract Var getWithinAnnotationVar(UnresolvedType unresolvedType);

    public abstract Var getWithinCodeAnnotationVar(UnresolvedType unresolvedType);

    public abstract Var getThisAnnotationVar(UnresolvedType unresolvedType);

    public abstract Var getTargetAnnotationVar(UnresolvedType unresolvedType);

    public abstract Var getArgAnnotationVar(int i, UnresolvedType unresolvedType);

    public abstract Member getEnclosingCodeSignature();

    public abstract ISourceLocation getSourceLocation();

    protected Shadow(Kind kind, Member signature, Shadow enclosingShadow) {
        int i = nextShadowID;
        nextShadowID = i + 1;
        this.shadowId = i;
        this.kind = kind;
        this.signature = signature;
        this.enclosingShadow = enclosingShadow;
    }

    public List<ShadowMunger> getMungers() {
        return this.mungers;
    }

    public final boolean hasThis() {
        if (getKind().neverHasThis()) {
            return false;
        }
        if (getKind().isEnclosingKind()) {
            return !Modifier.isStatic(getSignature().getModifiers());
        }
        if (this.enclosingShadow == null) {
            return false;
        }
        return this.enclosingShadow.hasThis();
    }

    public final UnresolvedType getThisType() {
        if (!hasThis()) {
            throw new IllegalStateException("no this");
        }
        if (getKind().isEnclosingKind()) {
            return getSignature().getDeclaringType();
        }
        return this.enclosingShadow.getThisType();
    }

    public final boolean hasTarget() {
        if (getKind().neverHasTarget()) {
            return false;
        }
        if (getKind().isTargetSameAsThis()) {
            return hasThis();
        }
        return !Modifier.isStatic(getSignature().getModifiers());
    }

    public final UnresolvedType getTargetType() {
        if (!hasTarget()) {
            throw new IllegalStateException("no target");
        }
        return getSignature().getDeclaringType();
    }

    public UnresolvedType[] getArgTypes() {
        if (getKind() == FieldSet) {
            return new UnresolvedType[]{getSignature().getReturnType()};
        }
        return getSignature().getParameterTypes();
    }

    public boolean isShadowForArrayConstructionJoinpoint() {
        return getKind() == ConstructorCall && this.signature.getDeclaringType().isArray();
    }

    public boolean isShadowForMonitor() {
        return getKind() == SynchronizationLock || getKind() == SynchronizationUnlock;
    }

    public ResolvedType[] getArgumentTypesForArrayConstructionShadow() {
        String s = this.signature.getDeclaringType().getSignature();
        int pos = s.indexOf(PropertyAccessor.PROPERTY_KEY_PREFIX);
        int dims = 1;
        while (pos < s.length()) {
            pos++;
            if (pos < s.length()) {
                dims += s.charAt(pos) == '[' ? 1 : 0;
            }
        }
        ResolvedType intType = UnresolvedType.INT.resolve(getIWorld());
        if (dims == 1) {
            return new ResolvedType[]{intType};
        }
        ResolvedType[] someInts = new ResolvedType[dims];
        for (int i = 0; i < dims; i++) {
            someInts[i] = intType;
        }
        return someInts;
    }

    public UnresolvedType[] getGenericArgTypes() {
        if (isShadowForArrayConstructionJoinpoint()) {
            return getArgumentTypesForArrayConstructionShadow();
        }
        if (isShadowForMonitor()) {
            return UnresolvedType.ARRAY_WITH_JUST_OBJECT;
        }
        if (getKind() == FieldSet) {
            return new UnresolvedType[]{getResolvedSignature().getGenericReturnType()};
        }
        return getResolvedSignature().getGenericParameterTypes();
    }

    public UnresolvedType getArgType(int arg) {
        if (getKind() == FieldSet) {
            return getSignature().getReturnType();
        }
        return getSignature().getParameterTypes()[arg];
    }

    public int getArgCount() {
        if (getKind() == FieldSet) {
            return 1;
        }
        return getSignature().getParameterTypes().length;
    }

    public Kind getKind() {
        return this.kind;
    }

    public Member getSignature() {
        return this.signature;
    }

    public Member getMatchingSignature() {
        return this.matchingSignature != null ? this.matchingSignature : this.signature;
    }

    public void setMatchingSignature(Member member) {
        this.matchingSignature = member;
    }

    public ResolvedMember getResolvedSignature() {
        if (this.resolvedSignature == null) {
            this.resolvedSignature = this.signature.resolve(getIWorld());
        }
        return this.resolvedSignature;
    }

    public UnresolvedType getReturnType() {
        if (this.kind == ConstructorCall) {
            return getSignature().getDeclaringType();
        }
        if (this.kind == FieldSet) {
            return UnresolvedType.VOID;
        }
        if (this.kind == SynchronizationLock || this.kind == SynchronizationUnlock) {
            return UnresolvedType.VOID;
        }
        return getResolvedSignature().getGenericReturnType();
    }

    public static int howMany(int i) {
        int count = 0;
        for (int j = 0; j < SHADOW_KINDS.length; j++) {
            if ((i & SHADOW_KINDS[j].bit) != 0) {
                count++;
            }
        }
        return count;
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/Shadow$Kind.class */
    public static final class Kind extends TypeSafeEnum {
        public int bit;
        private static final int hasReturnValueFlag = 558;
        private static final int isEnclosingKindFlag = 1688;
        private static final int isTargetSameAsThisFlag = 1944;
        private static final int neverHasTargetFlag = 14724;
        private static final int neverHasThisFlag = 384;

        public Kind(String name, int key, boolean argsOnStack) {
            super(name, key);
            this.bit = 1 << key;
        }

        public String toLegalJavaIdentifier() {
            return getName().replace('-', '_');
        }

        public boolean argsOnStack() {
            return !isTargetSameAsThis();
        }

        public boolean allowsExtraction() {
            return true;
        }

        public boolean isSet(int i) {
            return (i & this.bit) != 0;
        }

        public boolean hasHighPriorityExceptions() {
            return !isTargetSameAsThis();
        }

        public boolean hasReturnValue() {
            return (this.bit & hasReturnValueFlag) != 0;
        }

        public boolean isEnclosingKind() {
            return (this.bit & 1688) != 0;
        }

        public boolean isTargetSameAsThis() {
            return (this.bit & isTargetSameAsThisFlag) != 0;
        }

        public boolean neverHasTarget() {
            return (this.bit & neverHasTargetFlag) != 0;
        }

        public boolean neverHasThis() {
            return (this.bit & 384) != 0;
        }

        public String getSimpleName() {
            int dash = getName().lastIndexOf(45);
            if (dash == -1) {
                return getName();
            }
            return getName().substring(dash + 1);
        }

        public static Kind read(DataInputStream s) throws IOException {
            int key = s.readByte();
            switch (key) {
                case 1:
                    return Shadow.MethodCall;
                case 2:
                    return Shadow.ConstructorCall;
                case 3:
                    return Shadow.MethodExecution;
                case 4:
                    return Shadow.ConstructorExecution;
                case 5:
                    return Shadow.FieldGet;
                case 6:
                    return Shadow.FieldSet;
                case 7:
                    return Shadow.StaticInitialization;
                case 8:
                    return Shadow.PreInitialization;
                case 9:
                    return Shadow.AdviceExecution;
                case 10:
                    return Shadow.Initialization;
                case 11:
                    return Shadow.ExceptionHandler;
                case 12:
                    return Shadow.SynchronizationLock;
                case 13:
                    return Shadow.SynchronizationUnlock;
                default:
                    throw new BCException("unknown kind: " + key);
            }
        }
    }

    protected boolean checkMunger(ShadowMunger munger) {
        if (munger.mustCheckExceptions()) {
            Iterator<ResolvedType> i = munger.getThrownExceptions().iterator();
            while (i.hasNext()) {
                if (!checkCanThrow(munger, i.next())) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    protected boolean checkCanThrow(ShadowMunger munger, ResolvedType resolvedTypeX) {
        if (getKind() != ExceptionHandler && !isDeclaredException(resolvedTypeX, getSignature())) {
            getIWorld().showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.CANT_THROW_CHECKED, resolvedTypeX, this), getSourceLocation(), munger.getSourceLocation());
            return true;
        }
        return true;
    }

    private boolean isDeclaredException(ResolvedType resolvedTypeX, Member member) {
        ResolvedType[] excs = getIWorld().resolve(member.getExceptions(getIWorld()));
        for (ResolvedType resolvedType : excs) {
            if (resolvedType.isAssignableFrom(resolvedTypeX)) {
                return true;
            }
        }
        return false;
    }

    public void addMunger(ShadowMunger munger) {
        if (checkMunger(munger)) {
            if (this.mungers == Collections.EMPTY_LIST) {
                this.mungers = new ArrayList();
            }
            this.mungers.add(munger);
        }
    }

    public final void implement() throws AbortException {
        sortMungers();
        if (this.mungers == null) {
            return;
        }
        prepareForMungers();
        implementMungers();
    }

    private void sortMungers() throws AbortException {
        List sorted = PartialOrder.sort(this.mungers);
        possiblyReportUnorderedAdvice(sorted);
        if (sorted == null) {
            for (ShadowMunger m : this.mungers) {
                getIWorld().getMessageHandler().handleMessage(MessageUtil.error(WeaverMessages.format(WeaverMessages.CIRCULAR_DEPENDENCY, this), m.getSourceLocation()));
            }
        }
        this.mungers = sorted;
    }

    private void possiblyReportUnorderedAdvice(List sorted) {
        Integer order;
        if (sorted != null && getIWorld().getLint().unorderedAdviceAtShadow.isEnabled() && this.mungers.size() > 1) {
            Set<String> clashingAspects = new HashSet<>();
            int max = this.mungers.size();
            for (int i = max - 1; i >= 0; i--) {
                for (int j = 0; j < i; j++) {
                    Object a = this.mungers.get(i);
                    Object b = this.mungers.get(j);
                    if ((a instanceof Advice) && (b instanceof Advice)) {
                        Advice adviceA = (Advice) a;
                        Advice adviceB = (Advice) b;
                        if (!adviceA.concreteAspect.equals(adviceB.concreteAspect)) {
                            AdviceKind adviceKindA = adviceA.getKind();
                            AdviceKind adviceKindB = adviceB.getKind();
                            if (adviceKindA.getKey() < 6 && adviceKindB.getKey() < 6 && adviceKindA.getPrecedence() == adviceKindB.getPrecedence() && (order = getIWorld().getPrecedenceIfAny(adviceA.concreteAspect, adviceB.concreteAspect)) != null && order.equals(new Integer(0))) {
                                String key = adviceA.getDeclaringAspect() + ":" + adviceB.getDeclaringAspect();
                                String possibleExistingKey = adviceB.getDeclaringAspect() + ":" + adviceA.getDeclaringAspect();
                                if (!clashingAspects.contains(possibleExistingKey)) {
                                    clashingAspects.add(key);
                                }
                            }
                        }
                    }
                }
            }
            for (String element : clashingAspects) {
                String aspect1 = element.substring(0, element.indexOf(":"));
                String aspect2 = element.substring(element.indexOf(":") + 1);
                getIWorld().getLint().unorderedAdviceAtShadow.signal(new String[]{toString(), aspect1, aspect2}, getSourceLocation(), null);
            }
        }
    }

    protected void prepareForMungers() {
        throw new RuntimeException("Generic shadows cannot be prepared");
    }

    private void implementMungers() {
        World world = getIWorld();
        for (ShadowMunger munger : this.mungers) {
            if (munger.implementOn(this)) {
                world.reportMatch(munger, this);
            }
        }
    }

    public String toString() {
        return getKind() + "(" + getSignature() + ")";
    }

    public String toResolvedString(World world) {
        StringBuffer sb = new StringBuffer();
        sb.append(getKind());
        sb.append("(");
        Member m = getSignature();
        if (m == null) {
            sb.append("<<missing signature>>");
        } else {
            ResolvedMember rm = world.resolve(m);
            if (rm == null) {
                sb.append("<<unresolvableMember:").append(m).append(">>");
            } else {
                String genString = rm.toGenericString();
                if (genString == null) {
                    sb.append("<<unableToGetGenericStringFor:").append(rm).append(">>");
                } else {
                    sb.append(genString);
                }
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public static Set<Kind> toSet(int i) {
        Set<Kind> results = new HashSet<>();
        for (int j = 0; j < SHADOW_KINDS.length; j++) {
            Kind k = SHADOW_KINDS[j];
            if (k.isSet(i)) {
                results.add(k);
            }
        }
        return results;
    }
}
