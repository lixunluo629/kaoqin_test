package org.aspectj.weaver.bcel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.aspectj.apache.bcel.classfile.ClassFormatException;
import org.aspectj.apache.bcel.classfile.LocalVariable;
import org.aspectj.apache.bcel.classfile.LocalVariableTable;
import org.aspectj.apache.bcel.generic.InstructionConstants;
import org.aspectj.apache.bcel.generic.InstructionFactory;
import org.aspectj.apache.bcel.generic.InstructionHandle;
import org.aspectj.apache.bcel.generic.InstructionList;
import org.aspectj.apache.bcel.generic.LineNumberTag;
import org.aspectj.apache.bcel.generic.LocalVariableTag;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.Message;
import org.aspectj.weaver.Advice;
import org.aspectj.weaver.AdviceKind;
import org.aspectj.weaver.AjAttribute;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.IEclipseSourceContext;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.Lint;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.ReferenceType;
import org.aspectj.weaver.ReferenceTypeDelegate;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedMemberImpl;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.ShadowMunger;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.World;
import org.aspectj.weaver.ast.Literal;
import org.aspectj.weaver.ast.Test;
import org.aspectj.weaver.patterns.ExactTypePattern;
import org.aspectj.weaver.patterns.ExposedState;
import org.aspectj.weaver.patterns.PerClause;
import org.aspectj.weaver.patterns.Pointcut;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/BcelAdvice.class */
class BcelAdvice extends Advice {
    private Test runtimeTest;
    private ExposedState exposedState;
    private int containsInvokedynamic;
    private Collection<ResolvedType> thrownExceptions;

    public BcelAdvice(AjAttribute.AdviceAttribute attribute, Pointcut pointcut, Member adviceSignature, ResolvedType concreteAspect) {
        super(attribute, pointcut, simplify(attribute.getKind(), adviceSignature));
        this.containsInvokedynamic = 0;
        this.thrownExceptions = null;
        this.concreteAspect = concreteAspect;
    }

    private static Member simplify(AdviceKind kind, Member adviceSignature) {
        if (adviceSignature != null) {
            UnresolvedType adviceDeclaringType = adviceSignature.getDeclaringType();
            if ((kind != AdviceKind.Around || ((adviceDeclaringType instanceof ResolvedType) && ((ResolvedType) adviceDeclaringType).getWorld().isXnoInline())) && (adviceSignature instanceof BcelMethod)) {
                BcelMethod bm = (BcelMethod) adviceSignature;
                if (bm.getMethod() != null && bm.getMethod().getAnnotations() != null) {
                    return adviceSignature;
                }
                ResolvedMemberImpl simplermember = new ResolvedMemberImpl(bm.getKind(), bm.getDeclaringType(), bm.getModifiers(), bm.getReturnType(), bm.getName(), bm.getParameterTypes());
                simplermember.setParameterNames(bm.getParameterNames());
                return simplermember;
            }
        }
        return adviceSignature;
    }

    @Override // org.aspectj.weaver.Advice, org.aspectj.weaver.ShadowMunger
    public ShadowMunger concretize(ResolvedType fromType, World world, PerClause clause) {
        if (!world.areAllLintIgnored()) {
            suppressLintWarnings(world);
        }
        ShadowMunger ret = super.concretize(fromType, world, clause);
        if (!world.areAllLintIgnored()) {
            clearLintSuppressions(world, this.suppressedLintKinds);
        }
        IfFinder ifinder = new IfFinder();
        ret.getPointcut().accept(ifinder, null);
        boolean hasGuardTest = ifinder.hasIf && getKind() != AdviceKind.Around;
        boolean isAround = getKind() == AdviceKind.Around;
        if ((getExtraParameterFlags() & 2) != 0 && !isAround && !hasGuardTest && world.getLint().noGuardForLazyTjp.isEnabled()) {
            world.getLint().noGuardForLazyTjp.signal("", getSourceLocation());
        }
        return ret;
    }

    @Override // org.aspectj.weaver.ShadowMunger
    public ShadowMunger parameterizeWith(ResolvedType declaringType, Map<String, UnresolvedType> typeVariableMap) {
        Pointcut pc = getPointcut().parameterizeWith(typeVariableMap, declaringType.getWorld());
        Member adviceSignature = this.signature;
        if ((this.signature instanceof ResolvedMember) && this.signature.getDeclaringType().isGenericType()) {
            adviceSignature = ((ResolvedMember) this.signature).parameterizedWith(declaringType.getTypeParameters(), declaringType, declaringType.isParameterizedType());
        }
        BcelAdvice ret = new BcelAdvice(this.attribute, pc, adviceSignature, this.concreteAspect);
        return ret;
    }

    @Override // org.aspectj.weaver.Advice, org.aspectj.weaver.ShadowMunger
    public boolean match(Shadow shadow, World world) {
        if (world.areAllLintIgnored()) {
            return super.match(shadow, world);
        }
        suppressLintWarnings(world);
        boolean ret = super.match(shadow, world);
        clearLintSuppressions(world, this.suppressedLintKinds);
        return ret;
    }

    @Override // org.aspectj.weaver.ShadowMunger
    public void specializeOn(Shadow shadow) {
        if (getKind() == AdviceKind.Around) {
            ((BcelShadow) shadow).initializeForAroundClosure();
        }
        if (getKind() == null) {
            this.exposedState = new ExposedState(0);
            return;
        }
        if (getKind().isPerEntry()) {
            this.exposedState = new ExposedState(0);
        } else if (getKind().isCflow()) {
            this.exposedState = new ExposedState(this.nFreeVars);
        } else if (getSignature() != null) {
            this.exposedState = new ExposedState(getSignature());
        } else {
            this.exposedState = new ExposedState(0);
            return;
        }
        World world = shadow.getIWorld();
        if (!world.areAllLintIgnored()) {
            suppressLintWarnings(world);
        }
        this.exposedState.setConcreteAspect(this.concreteAspect);
        this.runtimeTest = getPointcut().findResidue(shadow, this.exposedState);
        if (!world.areAllLintIgnored()) {
            clearLintSuppressions(world, this.suppressedLintKinds);
        }
        if (getKind() == AdviceKind.PerThisEntry) {
            shadow.getThisVar();
        } else if (getKind() == AdviceKind.PerTargetEntry) {
            shadow.getTargetVar();
        }
        if ((getExtraParameterFlags() & 4) != 0) {
            ((BcelShadow) shadow).getThisJoinPointStaticPartVar();
            ((BcelShadow) shadow).getEnclosingClass().warnOnAddedStaticInitializer(shadow, getSourceLocation());
        }
        if ((getExtraParameterFlags() & 2) != 0) {
            boolean hasGuardTest = (this.runtimeTest == Literal.TRUE || getKind() == AdviceKind.Around) ? false : true;
            boolean isAround = getKind() == AdviceKind.Around;
            ((BcelShadow) shadow).requireThisJoinPoint(hasGuardTest, isAround);
            ((BcelShadow) shadow).getEnclosingClass().warnOnAddedStaticInitializer(shadow, getSourceLocation());
            if (!hasGuardTest && world.getLint().multipleAdviceStoppingLazyTjp.isEnabled()) {
                ((BcelShadow) shadow).addAdvicePreventingLazyTjp(this);
            }
        }
        if ((getExtraParameterFlags() & 8) != 0) {
            ((BcelShadow) shadow).getThisEnclosingJoinPointStaticPartVar();
            ((BcelShadow) shadow).getEnclosingClass().warnOnAddedStaticInitializer(shadow, getSourceLocation());
        }
    }

    private boolean canInline(Shadow s) {
        BcelObjectType boType;
        if (this.attribute.isProceedInInners() || this.concreteAspect == null || this.concreteAspect.isMissing() || this.concreteAspect.getWorld().isXnoInline() || (boType = BcelWorld.getBcelObjectType(this.concreteAspect)) == null) {
            return false;
        }
        if (boType.javaClass.getMajor() == 52 && this.containsInvokedynamic == 0) {
            this.containsInvokedynamic = 1;
            LazyMethodGen lmg = boType.getLazyClassGen().getLazyMethodGen(this.signature.getName(), this.signature.getSignature(), true);
            ResolvedType searchType = this.concreteAspect;
            while (lmg == null) {
                searchType = searchType.getSuperclass();
                if (searchType == null) {
                    break;
                }
                ReferenceTypeDelegate rtd = ((ReferenceType) searchType).getDelegate();
                if (rtd instanceof BcelObjectType) {
                    BcelObjectType bot = (BcelObjectType) rtd;
                    if (bot.javaClass.getMajor() < 52) {
                        break;
                    }
                    lmg = bot.getLazyClassGen().getLazyMethodGen(this.signature.getName(), this.signature.getSignature(), true);
                }
            }
            if (lmg != null) {
                InstructionList ilist = lmg.getBody();
                InstructionHandle start = ilist.getStart();
                while (true) {
                    InstructionHandle src = start;
                    if (src == null) {
                        break;
                    }
                    if (src.getInstruction().opcode != 186) {
                        start = src.getNext();
                    } else {
                        this.containsInvokedynamic = 2;
                        break;
                    }
                }
            }
        }
        if (this.containsInvokedynamic == 2) {
            return false;
        }
        return boType.getLazyClassGen().isWoven();
    }

    private boolean aspectIsBroken() {
        if (this.concreteAspect instanceof ReferenceType) {
            ReferenceTypeDelegate rtDelegate = ((ReferenceType) this.concreteAspect).getDelegate();
            if (!(rtDelegate instanceof BcelObjectType)) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override // org.aspectj.weaver.ShadowMunger
    public boolean implementOn(Shadow s) throws ClassFormatException {
        this.hasMatchedAtLeastOnce = true;
        if (aspectIsBroken()) {
            return false;
        }
        BcelShadow shadow = (BcelShadow) s;
        if (!shadow.getWorld().isIgnoringUnusedDeclaredThrownException() && !getThrownExceptions().isEmpty()) {
            Member member = shadow.getSignature();
            if (member instanceof BcelMethod) {
                removeUnnecessaryProblems((BcelMethod) member, ((BcelMethod) member).getDeclarationLineNumber());
            } else {
                ResolvedMember resolvedMember = shadow.getSignature().resolve(shadow.getWorld());
                if ((resolvedMember instanceof BcelMethod) && (shadow.getEnclosingShadow() instanceof BcelShadow)) {
                    Member enclosingMember = shadow.getEnclosingShadow().getSignature();
                    if (enclosingMember instanceof BcelMethod) {
                        removeUnnecessaryProblems((BcelMethod) enclosingMember, ((BcelMethod) resolvedMember).getDeclarationLineNumber());
                    }
                }
            }
        }
        if (shadow.getIWorld().isJoinpointSynchronizationEnabled() && shadow.getKind() == Shadow.MethodExecution && (s.getSignature().getModifiers() & 32) != 0) {
            shadow.getIWorld().getLint().advisingSynchronizedMethods.signal(new String[]{shadow.toString()}, shadow.getSourceLocation(), new ISourceLocation[]{getSourceLocation()});
        }
        if (this.runtimeTest == Literal.FALSE) {
            Member sig = shadow.getSignature();
            if (sig.getArity() == 0 && shadow.getKind() == Shadow.MethodCall && sig.getName().charAt(0) == 'c' && sig.getReturnType().equals(ResolvedType.OBJECT) && sig.getName().equals("clone")) {
                return false;
            }
        }
        if (getKind() == AdviceKind.Before) {
            shadow.weaveBefore(this);
            return true;
        }
        if (getKind() == AdviceKind.AfterReturning) {
            shadow.weaveAfterReturning(this);
            return true;
        }
        if (getKind() == AdviceKind.AfterThrowing) {
            UnresolvedType catchType = hasExtraParameter() ? getExtraParameterType() : UnresolvedType.THROWABLE;
            shadow.weaveAfterThrowing(this, catchType);
            return true;
        }
        if (getKind() == AdviceKind.After) {
            shadow.weaveAfter(this);
            return true;
        }
        if (getKind() == AdviceKind.Around) {
            LazyClassGen enclosingClass = shadow.getEnclosingClass();
            if (enclosingClass != null && enclosingClass.isInterface() && shadow.getEnclosingMethod().getName().charAt(0) == '<') {
                shadow.getWorld().getLint().cannotAdviseJoinpointInInterfaceWithAroundAdvice.signal(shadow.toString(), shadow.getSourceLocation());
                return false;
            }
            if (!canInline(s)) {
                shadow.weaveAroundClosure(this, hasDynamicTests());
                return true;
            }
            shadow.weaveAroundInline(this, hasDynamicTests());
            return true;
        }
        if (getKind() == AdviceKind.InterInitializer) {
            shadow.weaveAfterReturning(this);
            return true;
        }
        if (getKind().isCflow()) {
            shadow.weaveCflowEntry(this, getSignature());
            return true;
        }
        if (getKind() == AdviceKind.PerThisEntry) {
            shadow.weavePerObjectEntry(this, (BcelVar) shadow.getThisVar());
            return true;
        }
        if (getKind() == AdviceKind.PerTargetEntry) {
            shadow.weavePerObjectEntry(this, (BcelVar) shadow.getTargetVar());
            return true;
        }
        if (getKind() == AdviceKind.Softener) {
            shadow.weaveSoftener(this, ((ExactTypePattern) this.exceptionType).getType());
            return true;
        }
        if (getKind() == AdviceKind.PerTypeWithinEntry) {
            shadow.weavePerTypeWithinAspectInitialization(this, shadow.getEnclosingType());
            return true;
        }
        throw new BCException("unimplemented kind: " + getKind());
    }

    private void removeUnnecessaryProblems(BcelMethod method, int problemLineNumber) {
        ISourceContext sourceContext = method.getSourceContext();
        if (sourceContext instanceof IEclipseSourceContext) {
            ((IEclipseSourceContext) sourceContext).removeUnnecessaryProblems(method, problemLineNumber);
        }
    }

    private Collection<ResolvedType> collectCheckedExceptions(UnresolvedType[] excs) {
        if (excs == null || excs.length == 0) {
            return Collections.emptyList();
        }
        Collection<ResolvedType> ret = new ArrayList<>();
        World world = this.concreteAspect.getWorld();
        ResolvedType runtimeException = world.getCoreType(UnresolvedType.RUNTIME_EXCEPTION);
        ResolvedType error = world.getCoreType(UnresolvedType.ERROR);
        int len = excs.length;
        for (int i = 0; i < len; i++) {
            ResolvedType t = world.resolve(excs[i], true);
            if (t.isMissing()) {
                world.getLint().cantFindType.signal(WeaverMessages.format(WeaverMessages.CANT_FIND_TYPE_EXCEPTION_TYPE, excs[i].getName()), getSourceLocation());
            }
            if (!runtimeException.isAssignableFrom(t) && !error.isAssignableFrom(t)) {
                ret.add(t);
            }
        }
        return ret;
    }

    @Override // org.aspectj.weaver.ShadowMunger
    public Collection<ResolvedType> getThrownExceptions() {
        if (this.thrownExceptions == null) {
            if (this.concreteAspect != null && this.concreteAspect.getWorld() != null && (getKind().isAfter() || getKind() == AdviceKind.Before || getKind() == AdviceKind.Around)) {
                World world = this.concreteAspect.getWorld();
                ResolvedMember m = world.resolve(this.signature);
                if (m == null) {
                    this.thrownExceptions = Collections.emptyList();
                } else {
                    this.thrownExceptions = collectCheckedExceptions(m.getExceptions());
                }
            } else {
                this.thrownExceptions = Collections.emptyList();
            }
        }
        return this.thrownExceptions;
    }

    @Override // org.aspectj.weaver.ShadowMunger
    public boolean mustCheckExceptions() {
        return getConcreteAspect() == null || !getConcreteAspect().isAnnotationStyleAspect();
    }

    @Override // org.aspectj.weaver.Advice
    public boolean hasDynamicTests() {
        return (this.runtimeTest == null || this.runtimeTest == Literal.TRUE) ? false : true;
    }

    InstructionList getAdviceInstructions(BcelShadow s, BcelVar extraArgVar, InstructionHandle ifNoAdvice) {
        InstructionFactory fact = s.getFactory();
        BcelWorld world = s.getWorld();
        InstructionList il = new InstructionList();
        if (hasExtraParameter() && getKind() == AdviceKind.AfterReturning) {
            UnresolvedType extraParameterType = getExtraParameterType();
            if (!extraParameterType.equals(UnresolvedType.OBJECT) && !extraParameterType.isPrimitiveType()) {
                il.append(BcelRenderer.renderTest(fact, world, Test.makeInstanceof(extraArgVar, getExtraParameterType().resolve(world)), null, ifNoAdvice, null));
            }
        }
        il.append(getAdviceArgSetup(s, extraArgVar, null));
        il.append(getNonTestAdviceInstructions(s));
        InstructionHandle ifYesAdvice = il.getStart();
        il.insert(getTestInstructions(s, ifYesAdvice, ifNoAdvice, ifYesAdvice));
        if (s.getKind() == Shadow.MethodExecution && getKind() == AdviceKind.Before) {
            int lineNumber = s.getEnclosingMethod().getMemberView().getLineNumberOfFirstInstruction();
            InstructionHandle start = il.getStart();
            if (lineNumber > 0) {
                start.addTargeter(new LineNumberTag(lineNumber));
            }
            LocalVariableTable lvt = s.getEnclosingMethod().getMemberView().getMethod().getLocalVariableTable();
            if (lvt != null) {
                LocalVariable[] lvTable = lvt.getLocalVariableTable();
                for (LocalVariable lv : lvTable) {
                    if (lv.getStartPC() == 0) {
                        start.addTargeter(new LocalVariableTag(lv.getSignature(), lv.getName(), lv.getIndex(), 0));
                    }
                }
            }
        }
        return il;
    }

    public InstructionList getAdviceArgSetup(BcelShadow shadow, BcelVar extraVar, InstructionList closureInstantiation) throws AbortException {
        InstructionFactory fact = shadow.getFactory();
        BcelWorld world = shadow.getWorld();
        InstructionList il = new InstructionList();
        if (this.exposedState.getAspectInstance() != null) {
            il.append(BcelRenderer.renderExpr(fact, world, this.exposedState.getAspectInstance()));
        }
        boolean x = getDeclaringAspect().resolve(world).isAnnotationStyleAspect();
        boolean isAnnotationStyleAspect = getConcreteAspect() != null && getConcreteAspect().isAnnotationStyleAspect() && x;
        boolean previousIsClosure = false;
        int len = this.exposedState.size();
        for (int i = 0; i < len; i++) {
            if (!this.exposedState.isErroneousVar(i)) {
                BcelVar v = (BcelVar) this.exposedState.get(i);
                if (v == null) {
                    if (isAnnotationStyleAspect) {
                        if ("Lorg/aspectj/lang/ProceedingJoinPoint;".equals(getSignature().getParameterTypes()[i].getSignature())) {
                            if (getKind() != AdviceKind.Around) {
                                previousIsClosure = false;
                                getConcreteAspect().getWorld().getMessageHandler().handleMessage(new Message("use of ProceedingJoinPoint is allowed only on around advice (arg " + i + " in " + toString() + ")", getSourceLocation(), true));
                                il.append(InstructionConstants.ACONST_NULL);
                            } else if (previousIsClosure) {
                                il.append(InstructionConstants.DUP);
                            } else {
                                previousIsClosure = true;
                                il.append(closureInstantiation.copy());
                            }
                        } else if ("Lorg/aspectj/lang/JoinPoint$StaticPart;".equals(getSignature().getParameterTypes()[i].getSignature())) {
                            previousIsClosure = false;
                            if ((getExtraParameterFlags() & 4) != 0) {
                                shadow.getThisJoinPointStaticPartBcelVar().appendLoad(il, fact);
                            }
                        } else if ("Lorg/aspectj/lang/JoinPoint;".equals(getSignature().getParameterTypes()[i].getSignature())) {
                            previousIsClosure = false;
                            if ((getExtraParameterFlags() & 2) != 0) {
                                il.append(shadow.loadThisJoinPoint());
                            }
                        } else if ("Lorg/aspectj/lang/JoinPoint$EnclosingStaticPart;".equals(getSignature().getParameterTypes()[i].getSignature())) {
                            previousIsClosure = false;
                            if ((getExtraParameterFlags() & 8) != 0) {
                                shadow.getThisEnclosingJoinPointStaticPartBcelVar().appendLoad(il, fact);
                            }
                        } else if (hasExtraParameter()) {
                            previousIsClosure = false;
                            extraVar.appendLoadAndConvert(il, fact, getExtraParameterType().resolve(world));
                        } else {
                            previousIsClosure = false;
                            getConcreteAspect().getWorld().getMessageHandler().handleMessage(new Message("use of ProceedingJoinPoint is allowed only on around advice (arg " + i + " in " + toString() + ")", getSourceLocation(), true));
                            il.append(InstructionConstants.ACONST_NULL);
                        }
                    }
                } else {
                    UnresolvedType desiredTy = getBindingParameterTypes()[i];
                    v.appendLoadAndConvert(il, fact, desiredTy.resolve(world));
                }
            }
        }
        if (!isAnnotationStyleAspect) {
            if (getKind() == AdviceKind.Around) {
                il.append(closureInstantiation);
            } else if (hasExtraParameter()) {
                extraVar.appendLoadAndConvert(il, fact, getExtraParameterType().resolve(world));
            }
            if ((getExtraParameterFlags() & 4) != 0) {
                shadow.getThisJoinPointStaticPartBcelVar().appendLoad(il, fact);
            }
            if ((getExtraParameterFlags() & 2) != 0) {
                il.append(shadow.loadThisJoinPoint());
            }
            if ((getExtraParameterFlags() & 8) != 0) {
                shadow.getThisEnclosingJoinPointStaticPartBcelVar().appendLoad(il, fact);
            }
        }
        return il;
    }

    public InstructionList getNonTestAdviceInstructions(BcelShadow shadow) {
        return new InstructionList(Utility.createInvoke(shadow.getFactory(), shadow.getWorld(), getOriginalSignature()));
    }

    @Override // org.aspectj.weaver.Advice
    public Member getOriginalSignature() {
        Member sig = getSignature();
        if (sig instanceof ResolvedMember) {
            ResolvedMember rsig = (ResolvedMember) sig;
            if (rsig.hasBackingGenericMember()) {
                return rsig.getBackingGenericMember();
            }
        }
        return sig;
    }

    public InstructionList getTestInstructions(BcelShadow shadow, InstructionHandle sk, InstructionHandle fk, InstructionHandle next) {
        return BcelRenderer.renderTest(shadow.getFactory(), shadow.getWorld(), this.runtimeTest, sk, fk, next);
    }

    @Override // org.aspectj.util.PartialOrder.PartialComparable
    public int compareTo(Object other) {
        if (!(other instanceof BcelAdvice)) {
            return 0;
        }
        BcelAdvice o = (BcelAdvice) other;
        if (this.kind.getPrecedence() != o.kind.getPrecedence()) {
            if (this.kind.getPrecedence() > o.kind.getPrecedence()) {
                return 1;
            }
            return -1;
        }
        if (this.kind.isCflow()) {
            boolean isBelow = this.kind == AdviceKind.CflowBelowEntry;
            if (this.innerCflowEntries.contains(o)) {
                return isBelow ? 1 : -1;
            }
            if (o.innerCflowEntries.contains(this)) {
                return isBelow ? -1 : 1;
            }
            return 0;
        }
        if (this.kind.isPerEntry() || this.kind == AdviceKind.Softener) {
            return 0;
        }
        World world = this.concreteAspect.getWorld();
        int ret = this.concreteAspect.getWorld().compareByPrecedence(this.concreteAspect, o.concreteAspect);
        if (ret != 0) {
            return ret;
        }
        ResolvedType declaringAspect = getDeclaringAspect().resolve(world);
        ResolvedType o_declaringAspect = o.getDeclaringAspect().resolve(world);
        if (declaringAspect == o_declaringAspect) {
            return (this.kind.isAfter() || o.kind.isAfter()) ? getStart() < o.getStart() ? -1 : 1 : getStart() < o.getStart() ? 1 : -1;
        }
        if (declaringAspect.isAssignableFrom(o_declaringAspect)) {
            return -1;
        }
        if (o_declaringAspect.isAssignableFrom(declaringAspect)) {
            return 1;
        }
        return 0;
    }

    public BcelVar[] getExposedStateAsBcelVars(boolean isAround) {
        if (isAround && getConcreteAspect() != null && getConcreteAspect().isAnnotationStyleAspect()) {
            return BcelVar.NONE;
        }
        if (this.exposedState == null) {
            return BcelVar.NONE;
        }
        int len = this.exposedState.vars.length;
        BcelVar[] ret = new BcelVar[len];
        for (int i = 0; i < len; i++) {
            ret[i] = (BcelVar) this.exposedState.vars[i];
        }
        return ret;
    }

    protected void suppressLintWarnings(World inWorld) {
        if (this.suppressedLintKinds == null) {
            if (this.signature instanceof BcelMethod) {
                this.suppressedLintKinds = Utility.getSuppressedWarnings(this.signature.getAnnotations(), inWorld.getLint());
            } else {
                this.suppressedLintKinds = Collections.emptyList();
                return;
            }
        }
        inWorld.getLint().suppressKinds(this.suppressedLintKinds);
    }

    protected void clearLintSuppressions(World inWorld, Collection<Lint.Kind> toClear) {
        inWorld.getLint().clearSuppressions(toClear);
    }

    public BcelAdvice(AdviceKind kind, Pointcut pointcut, Member signature, int extraArgumentFlags, int start, int end, ISourceContext sourceContext, ResolvedType concreteAspect) {
        this(new AjAttribute.AdviceAttribute(kind, pointcut, extraArgumentFlags, start, end, sourceContext), pointcut, signature, concreteAspect);
        this.thrownExceptions = Collections.emptyList();
    }
}
