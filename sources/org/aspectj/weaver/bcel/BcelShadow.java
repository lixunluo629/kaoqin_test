package org.aspectj.weaver.bcel;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.ognl.OgnlContext;
import org.aspectj.apache.bcel.classfile.ClassFormatException;
import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.aspectj.apache.bcel.classfile.Field;
import org.aspectj.apache.bcel.generic.ArrayType;
import org.aspectj.apache.bcel.generic.FieldInstruction;
import org.aspectj.apache.bcel.generic.INVOKEINTERFACE;
import org.aspectj.apache.bcel.generic.Instruction;
import org.aspectj.apache.bcel.generic.InstructionBranch;
import org.aspectj.apache.bcel.generic.InstructionConstants;
import org.aspectj.apache.bcel.generic.InstructionFactory;
import org.aspectj.apache.bcel.generic.InstructionHandle;
import org.aspectj.apache.bcel.generic.InstructionLV;
import org.aspectj.apache.bcel.generic.InstructionList;
import org.aspectj.apache.bcel.generic.InstructionTargeter;
import org.aspectj.apache.bcel.generic.InvokeInstruction;
import org.aspectj.apache.bcel.generic.LineNumberTag;
import org.aspectj.apache.bcel.generic.LocalVariableTag;
import org.aspectj.apache.bcel.generic.MULTIANEWARRAY;
import org.aspectj.apache.bcel.generic.ObjectType;
import org.aspectj.apache.bcel.generic.TargetLostException;
import org.aspectj.apache.bcel.generic.Type;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.weaver.Advice;
import org.aspectj.weaver.AdviceKind;
import org.aspectj.weaver.AjcMemberMaker;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.ConcreteTypeMunger;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.MemberImpl;
import org.aspectj.weaver.NameMangler;
import org.aspectj.weaver.NewConstructorTypeMunger;
import org.aspectj.weaver.NewFieldTypeMunger;
import org.aspectj.weaver.NewMethodTypeMunger;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedMemberImpl;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.ShadowMunger;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.World;
import org.aspectj.weaver.ast.Var;
import org.aspectj.weaver.patterns.AbstractPatternNodeVisitor;
import org.aspectj.weaver.patterns.AndPointcut;
import org.aspectj.weaver.patterns.NotPointcut;
import org.aspectj.weaver.patterns.OrPointcut;
import org.aspectj.weaver.patterns.ThisOrTargetPointcut;
import org.springframework.validation.DataBinder;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/BcelShadow.class */
public class BcelShadow extends Shadow {
    private static final String[] NoDeclaredExceptions = new String[0];
    private ShadowRange range;
    private final BcelWorld world;
    private final LazyMethodGen enclosingMethod;
    public static boolean appliedLazyTjpOptimization;
    private String actualInstructionTargetType;
    private List<BcelAdvice> badAdvice;
    private int sourceline;
    private BcelVar thisVar;
    private BcelVar targetVar;
    private BcelVar[] argVars;
    private Map<ResolvedType, AnnotationAccessVar> kindedAnnotationVars;
    private Map<ResolvedType, TypeAnnotationAccessVar> thisAnnotationVars;
    private Map<ResolvedType, TypeAnnotationAccessVar> targetAnnotationVars;
    private Map<ResolvedType, AnnotationAccessVar> withinAnnotationVars;
    private Map<ResolvedType, AnnotationAccessVar> withincodeAnnotationVars;
    private boolean allArgVarsInitialized;
    private BcelVar thisJoinPointVar;
    private boolean isThisJoinPointLazy;
    private int lazyTjpConsumers;
    private BcelVar thisJoinPointStaticPartVar;

    public BcelShadow(BcelWorld world, Shadow.Kind kind, Member signature, LazyMethodGen enclosingMethod, BcelShadow enclosingShadow) {
        super(kind, signature, enclosingShadow);
        this.badAdvice = null;
        this.sourceline = -1;
        this.thisVar = null;
        this.targetVar = null;
        this.argVars = null;
        this.kindedAnnotationVars = null;
        this.thisAnnotationVars = null;
        this.targetAnnotationVars = null;
        this.withinAnnotationVars = null;
        this.withincodeAnnotationVars = null;
        this.allArgVarsInitialized = false;
        this.thisJoinPointVar = null;
        this.lazyTjpConsumers = 0;
        this.thisJoinPointStaticPartVar = null;
        this.world = world;
        this.enclosingMethod = enclosingMethod;
    }

    public BcelShadow copyInto(LazyMethodGen recipient, BcelShadow enclosing) {
        BcelShadow s = new BcelShadow(this.world, getKind(), getSignature(), recipient, enclosing);
        if (this.mungers.size() > 0) {
            List<ShadowMunger> src = this.mungers;
            if (s.mungers == Collections.EMPTY_LIST) {
                s.mungers = new ArrayList();
            }
            List<ShadowMunger> dest = s.mungers;
            Iterator<ShadowMunger> i = src.iterator();
            while (i.hasNext()) {
                dest.add(i.next());
            }
        }
        return s;
    }

    @Override // org.aspectj.weaver.Shadow
    public World getIWorld() {
        return this.world;
    }

    private boolean deleteNewAndDup() {
        InstructionHandle ih;
        ConstantPool cpool = getEnclosingClass().getConstantPool();
        int depth = 1;
        InstructionHandle start = this.range.getStart();
        while (true) {
            ih = start;
            if (ih == null) {
                break;
            }
            Instruction inst = ih.getInstruction();
            if (inst.opcode == 183 && ((InvokeInstruction) inst).getName(cpool).equals("<init>")) {
                depth++;
            } else if (inst.opcode == 187) {
                depth--;
                if (depth == 0) {
                    break;
                }
            } else {
                continue;
            }
            start = ih.getPrev();
        }
        if (ih == null) {
            return false;
        }
        InstructionHandle endHandle = ih.getNext();
        if (endHandle.getInstruction().opcode == 89) {
            InstructionHandle nextHandle = endHandle.getNext();
            retargetFrom(ih, nextHandle);
            retargetFrom(endHandle, nextHandle);
        } else if (endHandle.getInstruction().opcode == 90) {
            endHandle = endHandle.getNext();
            InstructionHandle nextHandle2 = endHandle.getNext();
            boolean skipEndRepositioning = false;
            if (endHandle.getInstruction().opcode != 95) {
                if (endHandle.getInstruction().opcode == 254) {
                    skipEndRepositioning = true;
                } else {
                    throw new RuntimeException("Unhandled kind of new " + endHandle);
                }
            }
            retargetFrom(ih, nextHandle2);
            retargetFrom(endHandle, nextHandle2);
            if (!skipEndRepositioning) {
                retargetFrom(endHandle, nextHandle2);
            }
        } else {
            endHandle = ih;
            retargetFrom(ih, endHandle.getNext());
            getRange().insert(InstructionConstants.POP, Range.OutsideAfter);
        }
        try {
            this.range.getBody().delete(ih, endHandle);
            return true;
        } catch (TargetLostException e) {
            throw new BCException("shouldn't happen");
        }
    }

    private void retargetFrom(InstructionHandle old, InstructionHandle fresh) {
        for (InstructionTargeter targeter : old.getTargetersCopy()) {
            if (targeter instanceof ExceptionRange) {
                ExceptionRange it = (ExceptionRange) targeter;
                it.updateTarget(old, fresh, it.getBody());
            } else {
                targeter.updateTarget(old, fresh);
            }
        }
    }

    public void addAdvicePreventingLazyTjp(BcelAdvice advice) {
        if (this.badAdvice == null) {
            this.badAdvice = new ArrayList();
        }
        this.badAdvice.add(advice);
    }

    @Override // org.aspectj.weaver.Shadow
    protected void prepareForMungers() {
        boolean deletedNewAndDup = true;
        if (getKind() == ConstructorCall) {
            if (!this.world.isJoinpointArrayConstructionEnabled() || !getSignature().getDeclaringType().isArray()) {
                deletedNewAndDup = deleteNewAndDup();
            }
            initializeArgVars();
        } else if (getKind() == PreInitialization) {
            getRange().insert(InstructionConstants.NOP, Range.InsideAfter);
        } else if (getKind() == ExceptionHandler) {
            ShadowRange range = getRange();
            InstructionList body = range.getBody();
            InstructionHandle start = range.getStart();
            this.argVars = new BcelVar[1];
            UnresolvedType tx = getArgType(0);
            this.argVars[0] = genTempVar(tx, "ajc$arg0");
            InstructionHandle insertedInstruction = range.insert(this.argVars[0].createStore(getFactory()), Range.OutsideBefore);
            for (InstructionTargeter t : start.getTargetersCopy()) {
                if (t instanceof ExceptionRange) {
                    ExceptionRange er = (ExceptionRange) t;
                    er.updateTarget(start, insertedInstruction, body);
                }
            }
        }
        this.isThisJoinPointLazy = true;
        this.badAdvice = null;
        for (ShadowMunger munger : this.mungers) {
            munger.specializeOn(this);
        }
        initializeThisJoinPoint();
        if (this.thisJoinPointVar != null && !this.isThisJoinPointLazy && this.badAdvice != null && this.badAdvice.size() > 1) {
            int valid = 0;
            for (BcelAdvice element : this.badAdvice) {
                ISourceLocation sLoc = element.getSourceLocation();
                if (sLoc != null && sLoc.getLine() > 0) {
                    valid++;
                }
            }
            if (valid != 0) {
                ISourceLocation[] badLocs = new ISourceLocation[valid];
                int i = 0;
                for (BcelAdvice element2 : this.badAdvice) {
                    ISourceLocation sLoc2 = element2.getSourceLocation();
                    if (sLoc2 != null) {
                        int i2 = i;
                        i++;
                        badLocs[i2] = sLoc2;
                    }
                }
                this.world.getLint().multipleAdviceStoppingLazyTjp.signal(new String[]{toString()}, getSourceLocation(), badLocs);
            }
        }
        this.badAdvice = null;
        InstructionFactory fact = getFactory();
        if (getKind().argsOnStack() && this.argVars != null) {
            if (getKind() == ExceptionHandler && this.range.getEnd().getNext().getInstruction().equals(InstructionConstants.POP)) {
                this.range.getEnd().getNext().setInstruction(InstructionConstants.NOP);
                return;
            }
            this.range.insert(BcelRenderer.renderExprs(fact, this.world, this.argVars), Range.InsideBefore);
            if (this.targetVar != null) {
                this.range.insert(BcelRenderer.renderExpr(fact, this.world, this.targetVar), Range.InsideBefore);
            }
            if (getKind() == ConstructorCall) {
                if ((!this.world.isJoinpointArrayConstructionEnabled() || !getSignature().getDeclaringType().isArray()) && deletedNewAndDup) {
                    this.range.insert(InstructionFactory.createDup(1), Range.InsideBefore);
                    this.range.insert(fact.createNew((ObjectType) BcelWorld.makeBcelType(getSignature().getDeclaringType())), Range.InsideBefore);
                }
            }
        }
    }

    public ShadowRange getRange() {
        return this.range;
    }

    public void setRange(ShadowRange range) {
        this.range = range;
    }

    public int getSourceLine() {
        if (this.sourceline != -1) {
            return this.sourceline;
        }
        Shadow.Kind kind = getKind();
        if ((kind == MethodExecution || kind == ConstructorExecution || kind == AdviceExecution || kind == StaticInitialization || kind == PreInitialization || kind == Initialization) && getEnclosingMethod().hasDeclaredLineNumberInfo()) {
            this.sourceline = getEnclosingMethod().getDeclarationLineNumber();
            return this.sourceline;
        }
        if (this.range == null) {
            if (getEnclosingMethod().hasBody()) {
                this.sourceline = Utility.getSourceLine(getEnclosingMethod().getBody().getStart());
                return this.sourceline;
            }
            this.sourceline = 0;
            return this.sourceline;
        }
        this.sourceline = Utility.getSourceLine(this.range.getStart());
        if (this.sourceline < 0) {
            this.sourceline = 0;
        }
        return this.sourceline;
    }

    @Override // org.aspectj.weaver.Shadow
    public ResolvedType getEnclosingType() {
        return getEnclosingClass().getType();
    }

    public LazyClassGen getEnclosingClass() {
        return this.enclosingMethod.getEnclosingClass();
    }

    public BcelWorld getWorld() {
        return this.world;
    }

    public static BcelShadow makeConstructorExecution(BcelWorld world, LazyMethodGen enclosingMethod, InstructionHandle justBeforeStart) {
        InstructionList body = enclosingMethod.getBody();
        BcelShadow s = new BcelShadow(world, ConstructorExecution, world.makeJoinPointSignatureFromMethod(enclosingMethod, Member.CONSTRUCTOR), enclosingMethod, null);
        ShadowRange r = new ShadowRange(body);
        r.associateWithShadow(s);
        r.associateWithTargets(Range.genStart(body, justBeforeStart.getNext()), Range.genEnd(body));
        return s;
    }

    public static BcelShadow makeStaticInitialization(BcelWorld world, LazyMethodGen enclosingMethod) {
        InstructionList body = enclosingMethod.getBody();
        InstructionHandle clinitStart = body.getStart();
        if (clinitStart.getInstruction() instanceof InvokeInstruction) {
            InvokeInstruction ii = (InvokeInstruction) clinitStart.getInstruction();
            if (ii.getName(enclosingMethod.getEnclosingClass().getConstantPool()).equals(NameMangler.AJC_PRE_CLINIT_NAME)) {
                clinitStart = clinitStart.getNext();
            }
        }
        InstructionHandle clinitEnd = body.getEnd();
        BcelShadow s = new BcelShadow(world, StaticInitialization, world.makeJoinPointSignatureFromMethod(enclosingMethod, Member.STATIC_INITIALIZATION), enclosingMethod, null);
        ShadowRange r = new ShadowRange(body);
        r.associateWithShadow(s);
        r.associateWithTargets(Range.genStart(body, clinitStart), Range.genEnd(body, clinitEnd));
        return s;
    }

    public static BcelShadow makeExceptionHandler(BcelWorld world, ExceptionRange exceptionRange, LazyMethodGen enclosingMethod, InstructionHandle startOfHandler, BcelShadow enclosingShadow) {
        InstructionList body = enclosingMethod.getBody();
        UnresolvedType catchType = exceptionRange.getCatchType();
        UnresolvedType inType = enclosingMethod.getEnclosingClass().getType();
        ResolvedMemberImpl sig = MemberImpl.makeExceptionHandlerSignature(inType, catchType);
        sig.setParameterNames(new String[]{findHandlerParamName(startOfHandler)});
        BcelShadow s = new BcelShadow(world, ExceptionHandler, sig, enclosingMethod, enclosingShadow);
        ShadowRange r = new ShadowRange(body);
        r.associateWithShadow(s);
        InstructionHandle start = Range.genStart(body, startOfHandler);
        InstructionHandle end = Range.genEnd(body, start);
        r.associateWithTargets(start, end);
        exceptionRange.updateTarget(startOfHandler, start, body);
        return s;
    }

    private static String findHandlerParamName(InstructionHandle startOfHandler) {
        if (startOfHandler.getInstruction().isStoreInstruction() && startOfHandler.getNext() != null) {
            int slot = startOfHandler.getInstruction().getIndex();
            for (InstructionTargeter targeter : startOfHandler.getNext().getTargeters()) {
                if (targeter instanceof LocalVariableTag) {
                    LocalVariableTag t = (LocalVariableTag) targeter;
                    if (t.getSlot() == slot) {
                        return t.getName();
                    }
                }
            }
            return "<missing>";
        }
        return "<missing>";
    }

    public static BcelShadow makeIfaceInitialization(BcelWorld world, LazyMethodGen constructor, Member interfaceConstructorSignature) {
        constructor.getBody();
        BcelShadow s = new BcelShadow(world, Initialization, interfaceConstructorSignature, constructor, null);
        return s;
    }

    public void initIfaceInitializer(InstructionHandle end) {
        InstructionList body = this.enclosingMethod.getBody();
        ShadowRange r = new ShadowRange(body);
        r.associateWithShadow(this);
        InstructionHandle nop = body.insert(end, InstructionConstants.NOP);
        r.associateWithTargets(Range.genStart(body, nop), Range.genEnd(body, nop));
    }

    public static BcelShadow makeUnfinishedInitialization(BcelWorld world, LazyMethodGen constructor) {
        BcelShadow ret = new BcelShadow(world, Initialization, world.makeJoinPointSignatureFromMethod(constructor, Member.CONSTRUCTOR), constructor, null);
        if (constructor.getEffectiveSignature() != null) {
            ret.setMatchingSignature(constructor.getEffectiveSignature().getEffectiveSignature());
        }
        return ret;
    }

    public static BcelShadow makeUnfinishedPreinitialization(BcelWorld world, LazyMethodGen constructor) {
        BcelShadow ret = new BcelShadow(world, PreInitialization, world.makeJoinPointSignatureFromMethod(constructor, Member.CONSTRUCTOR), constructor, null);
        if (constructor.getEffectiveSignature() != null) {
            ret.setMatchingSignature(constructor.getEffectiveSignature().getEffectiveSignature());
        }
        return ret;
    }

    public static BcelShadow makeMethodExecution(BcelWorld world, LazyMethodGen enclosingMethod, boolean lazyInit) {
        if (!lazyInit) {
            return makeMethodExecution(world, enclosingMethod);
        }
        BcelShadow s = new BcelShadow(world, MethodExecution, enclosingMethod.getMemberView(), enclosingMethod, null);
        return s;
    }

    public void init() {
        if (this.range != null) {
            return;
        }
        InstructionList body = this.enclosingMethod.getBody();
        ShadowRange r = new ShadowRange(body);
        r.associateWithShadow(this);
        r.associateWithTargets(Range.genStart(body), Range.genEnd(body));
    }

    public static BcelShadow makeMethodExecution(BcelWorld world, LazyMethodGen enclosingMethod) {
        return makeShadowForMethod(world, enclosingMethod, MethodExecution, enclosingMethod.getMemberView());
    }

    public static BcelShadow makeShadowForMethod(BcelWorld world, LazyMethodGen enclosingMethod, Shadow.Kind kind, Member sig) {
        InstructionList body = enclosingMethod.getBody();
        BcelShadow s = new BcelShadow(world, kind, sig, enclosingMethod, null);
        ShadowRange r = new ShadowRange(body);
        r.associateWithShadow(s);
        r.associateWithTargets(Range.genStart(body), Range.genEnd(body));
        return s;
    }

    public static BcelShadow makeAdviceExecution(BcelWorld world, LazyMethodGen enclosingMethod) {
        InstructionList body = enclosingMethod.getBody();
        BcelShadow s = new BcelShadow(world, AdviceExecution, world.makeJoinPointSignatureFromMethod(enclosingMethod, Member.ADVICE), enclosingMethod, null);
        ShadowRange r = new ShadowRange(body);
        r.associateWithShadow(s);
        r.associateWithTargets(Range.genStart(body), Range.genEnd(body));
        return s;
    }

    public static BcelShadow makeConstructorCall(BcelWorld world, LazyMethodGen enclosingMethod, InstructionHandle callHandle, BcelShadow enclosingShadow) {
        InstructionList body = enclosingMethod.getBody();
        Member sig = world.makeJoinPointSignatureForMethodInvocation(enclosingMethod.getEnclosingClass(), (InvokeInstruction) callHandle.getInstruction());
        BcelShadow s = new BcelShadow(world, ConstructorCall, sig, enclosingMethod, enclosingShadow);
        ShadowRange r = new ShadowRange(body);
        r.associateWithShadow(s);
        r.associateWithTargets(Range.genStart(body, callHandle), Range.genEnd(body, callHandle));
        retargetAllBranches(callHandle, r.getStart());
        return s;
    }

    public static BcelShadow makeArrayConstructorCall(BcelWorld world, LazyMethodGen enclosingMethod, InstructionHandle arrayInstruction, BcelShadow enclosingShadow) {
        InstructionList body = enclosingMethod.getBody();
        Member sig = world.makeJoinPointSignatureForArrayConstruction(enclosingMethod.getEnclosingClass(), arrayInstruction);
        BcelShadow s = new BcelShadow(world, ConstructorCall, sig, enclosingMethod, enclosingShadow);
        ShadowRange r = new ShadowRange(body);
        r.associateWithShadow(s);
        r.associateWithTargets(Range.genStart(body, arrayInstruction), Range.genEnd(body, arrayInstruction));
        retargetAllBranches(arrayInstruction, r.getStart());
        return s;
    }

    public static BcelShadow makeMonitorEnter(BcelWorld world, LazyMethodGen enclosingMethod, InstructionHandle monitorInstruction, BcelShadow enclosingShadow) {
        InstructionList body = enclosingMethod.getBody();
        Member sig = world.makeJoinPointSignatureForMonitorEnter(enclosingMethod.getEnclosingClass(), monitorInstruction);
        BcelShadow s = new BcelShadow(world, SynchronizationLock, sig, enclosingMethod, enclosingShadow);
        ShadowRange r = new ShadowRange(body);
        r.associateWithShadow(s);
        r.associateWithTargets(Range.genStart(body, monitorInstruction), Range.genEnd(body, monitorInstruction));
        retargetAllBranches(monitorInstruction, r.getStart());
        return s;
    }

    public static BcelShadow makeMonitorExit(BcelWorld world, LazyMethodGen enclosingMethod, InstructionHandle monitorInstruction, BcelShadow enclosingShadow) {
        InstructionList body = enclosingMethod.getBody();
        Member sig = world.makeJoinPointSignatureForMonitorExit(enclosingMethod.getEnclosingClass(), monitorInstruction);
        BcelShadow s = new BcelShadow(world, SynchronizationUnlock, sig, enclosingMethod, enclosingShadow);
        ShadowRange r = new ShadowRange(body);
        r.associateWithShadow(s);
        r.associateWithTargets(Range.genStart(body, monitorInstruction), Range.genEnd(body, monitorInstruction));
        retargetAllBranches(monitorInstruction, r.getStart());
        return s;
    }

    public static BcelShadow makeMethodCall(BcelWorld world, LazyMethodGen enclosingMethod, InstructionHandle callHandle, BcelShadow enclosingShadow) {
        InstructionList body = enclosingMethod.getBody();
        BcelShadow s = new BcelShadow(world, MethodCall, world.makeJoinPointSignatureForMethodInvocation(enclosingMethod.getEnclosingClass(), (InvokeInstruction) callHandle.getInstruction()), enclosingMethod, enclosingShadow);
        ShadowRange r = new ShadowRange(body);
        r.associateWithShadow(s);
        r.associateWithTargets(Range.genStart(body, callHandle), Range.genEnd(body, callHandle));
        retargetAllBranches(callHandle, r.getStart());
        return s;
    }

    public static BcelShadow makeShadowForMethodCall(BcelWorld world, LazyMethodGen enclosingMethod, InstructionHandle callHandle, BcelShadow enclosingShadow, Shadow.Kind kind, ResolvedMember sig) {
        InstructionList body = enclosingMethod.getBody();
        BcelShadow s = new BcelShadow(world, kind, sig, enclosingMethod, enclosingShadow);
        ShadowRange r = new ShadowRange(body);
        r.associateWithShadow(s);
        r.associateWithTargets(Range.genStart(body, callHandle), Range.genEnd(body, callHandle));
        retargetAllBranches(callHandle, r.getStart());
        return s;
    }

    public static BcelShadow makeFieldGet(BcelWorld world, ResolvedMember field, LazyMethodGen enclosingMethod, InstructionHandle getHandle, BcelShadow enclosingShadow) {
        InstructionList body = enclosingMethod.getBody();
        BcelShadow s = new BcelShadow(world, FieldGet, field, enclosingMethod, enclosingShadow);
        ShadowRange r = new ShadowRange(body);
        r.associateWithShadow(s);
        r.associateWithTargets(Range.genStart(body, getHandle), Range.genEnd(body, getHandle));
        retargetAllBranches(getHandle, r.getStart());
        return s;
    }

    public static BcelShadow makeFieldSet(BcelWorld world, ResolvedMember field, LazyMethodGen enclosingMethod, InstructionHandle setHandle, BcelShadow enclosingShadow) {
        InstructionList body = enclosingMethod.getBody();
        BcelShadow s = new BcelShadow(world, FieldSet, field, enclosingMethod, enclosingShadow);
        ShadowRange r = new ShadowRange(body);
        r.associateWithShadow(s);
        r.associateWithTargets(Range.genStart(body, setHandle), Range.genEnd(body, setHandle));
        retargetAllBranches(setHandle, r.getStart());
        return s;
    }

    public static void retargetAllBranches(InstructionHandle from, InstructionHandle to) {
        for (InstructionTargeter source : from.getTargetersCopy()) {
            if (source instanceof InstructionBranch) {
                source.updateTarget(from, to);
            }
        }
    }

    public boolean terminatesWithReturn() {
        return getRange().getRealNext() == null;
    }

    public boolean arg0HoldsThis() {
        if (getKind().isEnclosingKind()) {
            return !Modifier.isStatic(getSignature().getModifiers());
        }
        if (this.enclosingShadow == null) {
            return !this.enclosingMethod.isStatic();
        }
        return ((BcelShadow) this.enclosingShadow).arg0HoldsThis();
    }

    @Override // org.aspectj.weaver.Shadow
    public Var getThisVar() {
        if (!hasThis()) {
            throw new IllegalStateException("no this");
        }
        initializeThisVar();
        return this.thisVar;
    }

    @Override // org.aspectj.weaver.Shadow
    public Var getThisAnnotationVar(UnresolvedType forAnnotationType) {
        if (!hasThis()) {
            throw new IllegalStateException("no this");
        }
        initializeThisAnnotationVars();
        Var v = this.thisAnnotationVars.get(forAnnotationType);
        if (v == null) {
            v = new TypeAnnotationAccessVar(forAnnotationType.resolve(this.world), (BcelVar) getThisVar());
        }
        return v;
    }

    @Override // org.aspectj.weaver.Shadow
    public Var getTargetVar() {
        if (!hasTarget()) {
            throw new IllegalStateException("no target");
        }
        initializeTargetVar();
        return this.targetVar;
    }

    @Override // org.aspectj.weaver.Shadow
    public Var getTargetAnnotationVar(UnresolvedType forAnnotationType) {
        if (!hasTarget()) {
            throw new IllegalStateException("no target");
        }
        initializeTargetAnnotationVars();
        Var v = this.targetAnnotationVars.get(forAnnotationType);
        if (v == null) {
            v = new TypeAnnotationAccessVar(forAnnotationType.resolve(this.world), (BcelVar) getTargetVar());
        }
        return v;
    }

    @Override // org.aspectj.weaver.Shadow
    public Var getArgVar(int i) {
        ensureInitializedArgVar(i);
        return this.argVars[i];
    }

    @Override // org.aspectj.weaver.Shadow
    public Var getArgAnnotationVar(int i, UnresolvedType forAnnotationType) {
        return new TypeAnnotationAccessVar(forAnnotationType.resolve(this.world), (BcelVar) getArgVar(i));
    }

    @Override // org.aspectj.weaver.Shadow
    public Var getKindedAnnotationVar(UnresolvedType forAnnotationType) {
        initializeKindedAnnotationVars();
        return this.kindedAnnotationVars.get(forAnnotationType);
    }

    @Override // org.aspectj.weaver.Shadow
    public Var getWithinAnnotationVar(UnresolvedType forAnnotationType) {
        initializeWithinAnnotationVars();
        return this.withinAnnotationVars.get(forAnnotationType);
    }

    @Override // org.aspectj.weaver.Shadow
    public Var getWithinCodeAnnotationVar(UnresolvedType forAnnotationType) {
        initializeWithinCodeAnnotationVars();
        return this.withincodeAnnotationVars.get(forAnnotationType);
    }

    @Override // org.aspectj.weaver.Shadow
    public final Var getThisJoinPointStaticPartVar() {
        return getThisJoinPointStaticPartBcelVar();
    }

    @Override // org.aspectj.weaver.Shadow
    public final Var getThisEnclosingJoinPointStaticPartVar() {
        return getThisEnclosingJoinPointStaticPartBcelVar();
    }

    public void requireThisJoinPoint(boolean hasGuardTest, boolean isAround) {
        if (!isAround) {
            if (!hasGuardTest) {
                this.isThisJoinPointLazy = false;
            } else {
                this.lazyTjpConsumers++;
            }
        }
        if (this.thisJoinPointVar == null) {
            this.thisJoinPointVar = genTempVar(UnresolvedType.forName("org.aspectj.lang.JoinPoint"));
        }
    }

    @Override // org.aspectj.weaver.Shadow
    public Var getThisJoinPointVar() {
        requireThisJoinPoint(false, false);
        return this.thisJoinPointVar;
    }

    void initializeThisJoinPoint() {
        if (this.thisJoinPointVar == null) {
            return;
        }
        if (this.isThisJoinPointLazy) {
            this.isThisJoinPointLazy = checkLazyTjp();
        }
        if (this.isThisJoinPointLazy) {
            appliedLazyTjpOptimization = true;
            createThisJoinPoint();
            if (this.lazyTjpConsumers == 1) {
                return;
            }
            InstructionFactory fact = getFactory();
            InstructionList il = new InstructionList();
            il.append(InstructionConstants.ACONST_NULL);
            il.append(this.thisJoinPointVar.createStore(fact));
            this.range.insert(il, Range.OutsideBefore);
            return;
        }
        appliedLazyTjpOptimization = false;
        InstructionFactory fact2 = getFactory();
        InstructionList il2 = createThisJoinPoint();
        il2.append(this.thisJoinPointVar.createStore(fact2));
        this.range.insert(il2, Range.OutsideBefore);
    }

    private boolean checkLazyTjp() {
        for (ShadowMunger munger : this.mungers) {
            if ((munger instanceof Advice) && ((Advice) munger).getKind() == AdviceKind.Around) {
                if (munger.getSourceLocation() != null && this.world.getLint().canNotImplementLazyTjp.isEnabled()) {
                    this.world.getLint().canNotImplementLazyTjp.signal(new String[]{toString()}, getSourceLocation(), new ISourceLocation[]{munger.getSourceLocation()});
                    return false;
                }
                return false;
            }
        }
        return true;
    }

    InstructionList loadThisJoinPoint() {
        InstructionFactory fact = getFactory();
        InstructionList il = new InstructionList();
        if (this.isThisJoinPointLazy) {
            il.append(createThisJoinPoint());
            if (this.lazyTjpConsumers > 1) {
                il.append(this.thisJoinPointVar.createStore(fact));
                InstructionHandle end = il.append(this.thisJoinPointVar.createLoad(fact));
                il.insert(InstructionFactory.createBranchInstruction((short) 199, end));
                il.insert(this.thisJoinPointVar.createLoad(fact));
            }
        } else {
            this.thisJoinPointVar.appendLoad(il, fact);
        }
        return il;
    }

    InstructionList createThisJoinPoint() {
        InstructionFactory fact = getFactory();
        InstructionList il = new InstructionList();
        BcelVar staticPart = getThisJoinPointStaticPartBcelVar();
        staticPart.appendLoad(il, fact);
        if (hasThis()) {
            ((BcelVar) getThisVar()).appendLoad(il, fact);
        } else {
            il.append(InstructionConstants.ACONST_NULL);
        }
        if (hasTarget()) {
            ((BcelVar) getTargetVar()).appendLoad(il, fact);
        } else {
            il.append(InstructionConstants.ACONST_NULL);
        }
        switch (getArgCount()) {
            case 0:
                il.append(fact.createInvoke("org.aspectj.runtime.reflect.Factory", "makeJP", LazyClassGen.tjpType, new Type[]{LazyClassGen.staticTjpType, Type.OBJECT, Type.OBJECT}, (short) 184));
                break;
            case 1:
                ((BcelVar) getArgVar(0)).appendLoadAndConvert(il, fact, this.world.getCoreType(ResolvedType.OBJECT));
                il.append(fact.createInvoke("org.aspectj.runtime.reflect.Factory", "makeJP", LazyClassGen.tjpType, new Type[]{LazyClassGen.staticTjpType, Type.OBJECT, Type.OBJECT, Type.OBJECT}, (short) 184));
                break;
            case 2:
                ((BcelVar) getArgVar(0)).appendLoadAndConvert(il, fact, this.world.getCoreType(ResolvedType.OBJECT));
                ((BcelVar) getArgVar(1)).appendLoadAndConvert(il, fact, this.world.getCoreType(ResolvedType.OBJECT));
                il.append(fact.createInvoke("org.aspectj.runtime.reflect.Factory", "makeJP", LazyClassGen.tjpType, new Type[]{LazyClassGen.staticTjpType, Type.OBJECT, Type.OBJECT, Type.OBJECT, Type.OBJECT}, (short) 184));
                break;
            default:
                il.append(makeArgsObjectArray());
                il.append(fact.createInvoke("org.aspectj.runtime.reflect.Factory", "makeJP", LazyClassGen.tjpType, new Type[]{LazyClassGen.staticTjpType, Type.OBJECT, Type.OBJECT, new ArrayType(Type.OBJECT, 1)}, (short) 184));
                break;
        }
        return il;
    }

    public BcelVar getThisJoinPointStaticPartBcelVar() {
        return getThisJoinPointStaticPartBcelVar(false);
    }

    @Override // org.aspectj.weaver.Shadow
    public BcelVar getThisAspectInstanceVar(ResolvedType aspectType) {
        return new AspectInstanceVar(aspectType);
    }

    public BcelVar getThisJoinPointStaticPartBcelVar(boolean isEnclosingJp) {
        ResolvedType sjpType;
        if (this.thisJoinPointStaticPartVar == null) {
            Field field = getEnclosingClass().getTjpField(this, isEnclosingJp);
            if (this.world.isTargettingAspectJRuntime12()) {
                sjpType = this.world.getCoreType(UnresolvedType.JOINPOINT_STATICPART);
            } else {
                sjpType = isEnclosingJp ? this.world.getCoreType(UnresolvedType.JOINPOINT_ENCLOSINGSTATICPART) : this.world.getCoreType(UnresolvedType.JOINPOINT_STATICPART);
            }
            this.thisJoinPointStaticPartVar = new BcelFieldRef(sjpType, getEnclosingClass().getClassName(), field.getName());
        }
        return this.thisJoinPointStaticPartVar;
    }

    public BcelVar getThisEnclosingJoinPointStaticPartBcelVar() {
        if (this.enclosingShadow == null) {
            return getThisJoinPointStaticPartBcelVar(true);
        }
        return ((BcelShadow) this.enclosingShadow).getThisJoinPointStaticPartBcelVar(true);
    }

    @Override // org.aspectj.weaver.Shadow
    public Member getEnclosingCodeSignature() {
        if (getKind().isEnclosingKind()) {
            return getSignature();
        }
        if (getKind() == Shadow.PreInitialization) {
            return getSignature();
        }
        if (this.enclosingShadow == null) {
            return getEnclosingMethod().getMemberView();
        }
        return this.enclosingShadow.getSignature();
    }

    public Member getRealEnclosingCodeSignature() {
        return this.enclosingMethod.getMemberView();
    }

    private InstructionList makeArgsObjectArray() {
        InstructionFactory fact = getFactory();
        BcelVar arrayVar = genTempVar(UnresolvedType.OBJECTARRAY);
        InstructionList il = new InstructionList();
        int alen = getArgCount();
        il.append(Utility.createConstant(fact, alen));
        il.append(fact.createNewArray(Type.OBJECT, (short) 1));
        arrayVar.appendStore(il, fact);
        int stateIndex = 0;
        int len = getArgCount();
        for (int i = 0; i < len; i++) {
            arrayVar.appendConvertableArrayStore(il, fact, stateIndex, (BcelVar) getArgVar(i));
            stateIndex++;
        }
        arrayVar.appendLoad(il, fact);
        return il;
    }

    private void initializeThisVar() {
        if (this.thisVar != null) {
            return;
        }
        this.thisVar = new BcelVar(getThisType().resolve(this.world), 0);
        this.thisVar.setPositionInAroundState(0);
    }

    public void initializeTargetVar() {
        InstructionFactory fact = getFactory();
        if (this.targetVar != null) {
            return;
        }
        if (getKind().isTargetSameAsThis()) {
            if (hasThis()) {
                initializeThisVar();
            }
            this.targetVar = this.thisVar;
        } else {
            initializeArgVars();
            UnresolvedType type = getTargetType();
            this.targetVar = genTempVar(ensureTargetTypeIsCorrect(type), "ajc$target");
            this.range.insert(this.targetVar.createStore(fact), Range.OutsideBefore);
            this.targetVar.setPositionInAroundState(hasThis() ? 1 : 0);
        }
    }

    public UnresolvedType ensureTargetTypeIsCorrect(UnresolvedType tx) {
        InstructionHandle searchPtr;
        LocalVariableTag lvt;
        Member msig = getSignature();
        if (msig.getArity() == 0 && getKind() == MethodCall && msig.getName().charAt(0) == 'c' && tx.equals(ResolvedType.OBJECT) && msig.getReturnType().equals(ResolvedType.OBJECT) && msig.getName().equals("clone")) {
            InstructionHandle prev = this.range.getStart().getPrev();
            while (true) {
                searchPtr = prev;
                if (!Range.isRangeHandle(searchPtr) && !searchPtr.getInstruction().isStoreInstruction()) {
                    break;
                }
                prev = searchPtr.getPrev();
            }
            if (searchPtr.getInstruction().isLoadInstruction() && (lvt = LazyMethodGen.getLocalVariableTag(searchPtr, searchPtr.getInstruction().getIndex())) != null) {
                return UnresolvedType.forSignature(lvt.getType());
            }
            if (searchPtr.getInstruction() instanceof FieldInstruction) {
                FieldInstruction si = (FieldInstruction) searchPtr.getInstruction();
                Type t = si.getFieldType(getEnclosingClass().getConstantPool());
                return BcelWorld.fromBcel(t);
            }
            if (searchPtr.getInstruction().opcode == 189) {
                return BcelWorld.fromBcel(new ArrayType(Type.OBJECT, 1));
            }
            if (searchPtr.getInstruction() instanceof MULTIANEWARRAY) {
                MULTIANEWARRAY ana = (MULTIANEWARRAY) searchPtr.getInstruction();
                return BcelWorld.fromBcel(new ArrayType(Type.OBJECT, ana.getDimensions()));
            }
            throw new BCException("Can't determine real target of clone() when processing instruction " + searchPtr.getInstruction() + ".  Perhaps avoid selecting clone with your pointcut?");
        }
        return tx;
    }

    public void ensureInitializedArgVar(int argNumber) {
        if (this.allArgVarsInitialized) {
            return;
        }
        if (this.argVars != null && this.argVars[argNumber] != null) {
            return;
        }
        InstructionFactory fact = getFactory();
        int len = getArgCount();
        if (this.argVars == null) {
            this.argVars = new BcelVar[len];
        }
        int positionOffset = (hasTarget() ? 1 : 0) + ((!hasThis() || getKind().isTargetSameAsThis()) ? 0 : 1);
        if (getKind().argsOnStack()) {
            for (int i = len - 1; i >= 0; i--) {
                BcelVar tmp = genTempVar(getArgType(i), "ajc$arg" + i);
                this.range.insert(tmp.createStore(getFactory()), Range.OutsideBefore);
                int position = i;
                tmp.setPositionInAroundState(position + positionOffset);
                this.argVars[i] = tmp;
            }
            this.allArgVarsInitialized = true;
            return;
        }
        int index = 0;
        if (arg0HoldsThis()) {
            index = 0 + 1;
        }
        boolean allInited = true;
        for (int i2 = 0; i2 < len; i2++) {
            UnresolvedType type = getArgType(i2);
            if (i2 == argNumber) {
                this.argVars[argNumber] = genTempVar(type, "ajc$arg" + argNumber);
                this.range.insert(this.argVars[argNumber].createCopyFrom(fact, index), Range.OutsideBefore);
                this.argVars[argNumber].setPositionInAroundState(argNumber + positionOffset);
            }
            allInited = allInited && this.argVars[i2] != null;
            index += type.getSize();
        }
        if (allInited && argNumber + 1 == len) {
            this.allArgVarsInitialized = true;
        }
    }

    public void initializeArgVars() {
        if (this.allArgVarsInitialized) {
            return;
        }
        InstructionFactory fact = getFactory();
        int len = getArgCount();
        if (this.argVars == null) {
            this.argVars = new BcelVar[len];
        }
        int positionOffset = (hasTarget() ? 1 : 0) + ((!hasThis() || getKind().isTargetSameAsThis()) ? 0 : 1);
        if (getKind().argsOnStack()) {
            for (int i = len - 1; i >= 0; i--) {
                BcelVar tmp = genTempVar(getArgType(i), "ajc$arg" + i);
                this.range.insert(tmp.createStore(getFactory()), Range.OutsideBefore);
                int position = i;
                tmp.setPositionInAroundState(position + positionOffset);
                this.argVars[i] = tmp;
            }
        } else {
            int index = 0;
            if (arg0HoldsThis()) {
                index = 0 + 1;
            }
            for (int i2 = 0; i2 < len; i2++) {
                UnresolvedType type = getArgType(i2);
                if (this.argVars[i2] == null) {
                    BcelVar tmp2 = genTempVar(type, "ajc$arg" + i2);
                    this.range.insert(tmp2.createCopyFrom(fact, index), Range.OutsideBefore);
                    this.argVars[i2] = tmp2;
                    tmp2.setPositionInAroundState(i2 + positionOffset);
                }
                index += type.resolve(this.world).getSize();
            }
        }
        this.allArgVarsInitialized = true;
    }

    public void initializeForAroundClosure() {
        initializeArgVars();
        if (hasTarget()) {
            initializeTargetVar();
        }
        if (hasThis()) {
            initializeThisVar();
        }
    }

    public void initializeThisAnnotationVars() {
        if (this.thisAnnotationVars != null) {
            return;
        }
        this.thisAnnotationVars = new HashMap();
    }

    public void initializeTargetAnnotationVars() {
        if (this.targetAnnotationVars != null) {
            return;
        }
        if (getKind().isTargetSameAsThis()) {
            if (hasThis()) {
                initializeThisAnnotationVars();
            }
            this.targetAnnotationVars = this.thisAnnotationVars;
            return;
        }
        this.targetAnnotationVars = new HashMap();
        ResolvedType[] rtx = getTargetType().resolve(this.world).getAnnotationTypes();
        for (ResolvedType typeX : rtx) {
            this.targetAnnotationVars.put(typeX, new TypeAnnotationAccessVar(typeX, (BcelVar) getTargetVar()));
        }
    }

    protected ResolvedMember getRelevantMember(ResolvedMember foundMember, Member relevantMember, ResolvedType relevantType) {
        ResolvedMember foundMember2;
        if (foundMember != null) {
            return foundMember;
        }
        ResolvedMember foundMember3 = getSignature().resolve(this.world);
        if (foundMember3 == null && relevantMember != null) {
            foundMember3 = relevantType.lookupMemberWithSupersAndITDs(relevantMember);
        }
        List<ConcreteTypeMunger> mungers = relevantType.resolve(this.world).getInterTypeMungers();
        for (ConcreteTypeMunger typeMunger : mungers) {
            if ((typeMunger.getMunger() instanceof NewMethodTypeMunger) || (typeMunger.getMunger() instanceof NewConstructorTypeMunger)) {
                ResolvedMember fakerm = typeMunger.getSignature();
                if (fakerm.getName().equals(getSignature().getName()) && fakerm.getParameterSignature().equals(getSignature().getParameterSignature())) {
                    if (foundMember3.getKind() == ResolvedMember.CONSTRUCTOR) {
                        foundMember2 = AjcMemberMaker.interConstructor(relevantType, foundMember3, typeMunger.getAspectType());
                    } else {
                        foundMember2 = AjcMemberMaker.interMethod(foundMember3, typeMunger.getAspectType(), false);
                    }
                    return foundMember2;
                }
            }
        }
        return foundMember3;
    }

    protected ResolvedType[] getAnnotations(ResolvedMember foundMember, Member relevantMember, ResolvedType relevantType) {
        ResolvedMember resolvedMemberInterMethodDispatcher;
        if (foundMember == null) {
            List<ConcreteTypeMunger> mungers = relevantType.resolve(this.world).getInterTypeMungers();
            for (Object munger : mungers) {
                ConcreteTypeMunger typeMunger = (ConcreteTypeMunger) munger;
                if ((typeMunger.getMunger() instanceof NewMethodTypeMunger) || (typeMunger.getMunger() instanceof NewConstructorTypeMunger)) {
                    ResolvedMember fakerm = typeMunger.getSignature();
                    if (getSignature().getKind() == ResolvedMember.CONSTRUCTOR) {
                        resolvedMemberInterMethodDispatcher = AjcMemberMaker.postIntroducedConstructor(typeMunger.getAspectType(), fakerm.getDeclaringType(), fakerm.getParameterTypes());
                    } else {
                        resolvedMemberInterMethodDispatcher = AjcMemberMaker.interMethodDispatcher(fakerm, typeMunger.getAspectType());
                    }
                    ResolvedMember ajcMethod = resolvedMemberInterMethodDispatcher;
                    ResolvedMember rmm = findMethod(typeMunger.getAspectType(), ajcMethod);
                    if (fakerm.getName().equals(getSignature().getName()) && fakerm.getParameterSignature().equals(getSignature().getParameterSignature())) {
                        typeMunger.getAspectType();
                        return rmm.getAnnotationTypes();
                    }
                }
            }
            foundMember = relevantType.lookupMemberWithSupersAndITDs(relevantMember);
            if (foundMember == null) {
                throw new IllegalStateException("Couldn't find member " + relevantMember + " for type " + relevantType);
            }
        }
        return foundMember.getAnnotationTypes();
    }

    public void initializeKindedAnnotationVars() {
        if (this.kindedAnnotationVars != null) {
            return;
        }
        this.kindedAnnotationVars = new HashMap();
        ResolvedType[] annotations = null;
        Member shadowSignature = getSignature();
        Member annotationHolder = getSignature();
        ResolvedType relevantType = shadowSignature.getDeclaringType().resolve(this.world);
        if (relevantType.isRawType() || relevantType.isParameterizedType()) {
            relevantType = relevantType.getGenericType();
        }
        if (getKind() == Shadow.StaticInitialization) {
            annotations = relevantType.resolve(this.world).getAnnotationTypes();
        } else if (getKind() == Shadow.MethodCall || getKind() == Shadow.ConstructorCall) {
            ResolvedMember foundMember = findMethod2(relevantType.resolve(this.world).getDeclaredMethods(), getSignature());
            annotations = getAnnotations(foundMember, shadowSignature, relevantType);
            annotationHolder = getRelevantMember(foundMember, shadowSignature, relevantType);
            relevantType = annotationHolder.getDeclaringType().resolve(this.world);
        } else if (getKind() == Shadow.FieldSet || getKind() == Shadow.FieldGet) {
            annotationHolder = findField(relevantType.getDeclaredFields(), getSignature());
            if (annotationHolder == null) {
                List<ConcreteTypeMunger> mungers = relevantType.resolve(this.world).getInterTypeMungers();
                for (ConcreteTypeMunger typeMunger : mungers) {
                    if (typeMunger.getMunger() instanceof NewFieldTypeMunger) {
                        ResolvedMember fakerm = typeMunger.getSignature();
                        ResolvedMember ajcMethod = AjcMemberMaker.interFieldInitializer(fakerm, typeMunger.getAspectType());
                        ResolvedMember rmm = findMethod(typeMunger.getAspectType(), ajcMethod);
                        if (fakerm.equals(getSignature())) {
                            relevantType = typeMunger.getAspectType();
                            annotationHolder = rmm;
                        }
                    }
                }
            }
            annotations = ((ResolvedMember) annotationHolder).getAnnotationTypes();
        } else if (getKind() == Shadow.MethodExecution || getKind() == Shadow.ConstructorExecution || getKind() == Shadow.AdviceExecution) {
            ResolvedMember foundMember2 = findMethod2(relevantType.getDeclaredMethods(), getSignature());
            annotations = getAnnotations(foundMember2, shadowSignature, relevantType);
            annotationHolder = getRelevantMember(foundMember2, annotationHolder, relevantType);
            UnresolvedType ut = annotationHolder.getDeclaringType();
            relevantType = ut.resolve(this.world);
        } else if (getKind() == Shadow.ExceptionHandler) {
            relevantType = getSignature().getParameterTypes()[0].resolve(this.world);
            annotations = relevantType.getAnnotationTypes();
        } else if (getKind() == Shadow.PreInitialization || getKind() == Shadow.Initialization) {
            ResolvedMember found = findMethod2(relevantType.getDeclaredMethods(), getSignature());
            annotations = found.getAnnotationTypes();
        }
        if (annotations == null) {
            throw new BCException("Could not discover annotations for shadow: " + getKind());
        }
        for (ResolvedType annotationType : annotations) {
            AnnotationAccessVar accessVar = new AnnotationAccessVar(this, getKind(), annotationType.resolve(this.world), relevantType, annotationHolder, false);
            this.kindedAnnotationVars.put(annotationType, accessVar);
        }
    }

    private ResolvedMember findMethod2(ResolvedMember[] members, Member sig) {
        String signatureName = sig.getName();
        String parameterSignature = sig.getParameterSignature();
        for (ResolvedMember member : members) {
            if (member.getName().equals(signatureName) && member.getParameterSignature().equals(parameterSignature)) {
                return member;
            }
        }
        return null;
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

    private ResolvedMember findField(ResolvedMember[] members, Member lookingFor) {
        for (ResolvedMember member : members) {
            if (member.getName().equals(getSignature().getName()) && member.getType().equals(getSignature().getType())) {
                return member;
            }
        }
        return null;
    }

    public void initializeWithinAnnotationVars() {
        if (this.withinAnnotationVars != null) {
            return;
        }
        this.withinAnnotationVars = new HashMap();
        ResolvedType[] annotations = getEnclosingType().resolve(this.world).getAnnotationTypes();
        for (ResolvedType ann : annotations) {
            Shadow.Kind k = Shadow.StaticInitialization;
            this.withinAnnotationVars.put(ann, new AnnotationAccessVar(this, k, ann, getEnclosingType(), null, true));
        }
    }

    public void initializeWithinCodeAnnotationVars() {
        if (this.withincodeAnnotationVars != null) {
            return;
        }
        this.withincodeAnnotationVars = new HashMap();
        ResolvedType[] annotations = getEnclosingMethod().getMemberView().getAnnotationTypes();
        for (ResolvedType ann : annotations) {
            Shadow.Kind k = getEnclosingMethod().getMemberView().getKind() == Member.CONSTRUCTOR ? Shadow.ConstructorExecution : Shadow.MethodExecution;
            this.withincodeAnnotationVars.put(ann, new AnnotationAccessVar(this, k, ann, getEnclosingType(), getEnclosingCodeSignature(), true));
        }
    }

    void weaveBefore(BcelAdvice munger) {
        this.range.insert(munger.getAdviceInstructions(this, null, this.range.getRealStart()), Range.InsideBefore);
    }

    public void weaveAfter(BcelAdvice munger) {
        weaveAfterThrowing(munger, UnresolvedType.THROWABLE);
        weaveAfterReturning(munger);
    }

    public void weaveAfterReturning(BcelAdvice munger) {
        List<InstructionHandle> returns = findReturnInstructions();
        boolean hasReturnInstructions = !returns.isEmpty();
        InstructionList retList = new InstructionList();
        BcelVar returnValueVar = null;
        if (hasReturnInstructions) {
            returnValueVar = generateReturnInstructions(returns, retList);
        } else {
            retList.append(InstructionConstants.NOP);
        }
        InstructionList advice = getAfterReturningAdviceDispatchInstructions(munger, retList.getStart());
        if (hasReturnInstructions) {
            InstructionHandle gotoTarget = advice.getStart();
            for (InstructionHandle ih : returns) {
                retargetReturnInstruction(munger.hasExtraParameter(), returnValueVar, gotoTarget, ih);
            }
        }
        this.range.append(advice);
        this.range.append(retList);
    }

    private List<InstructionHandle> findReturnInstructions() {
        List<InstructionHandle> returns = new ArrayList<>();
        InstructionHandle start = this.range.getStart();
        while (true) {
            InstructionHandle ih = start;
            if (ih != this.range.getEnd()) {
                if (ih.getInstruction().isReturnInstruction()) {
                    returns.add(ih);
                }
                start = ih.getNext();
            } else {
                return returns;
            }
        }
    }

    private BcelVar generateReturnInstructions(List<InstructionHandle> returns, InstructionList returnInstructions) {
        BcelVar returnValueVar = null;
        if (hasANonVoidReturnType()) {
            Instruction newReturnInstruction = null;
            for (int i = returns.size() - 1; newReturnInstruction == null && i >= 0; i--) {
                InstructionHandle ih = returns.get(i);
                if (ih.getInstruction().opcode != 177) {
                    newReturnInstruction = Utility.copyInstruction(ih.getInstruction());
                }
            }
            returnValueVar = genTempVar(getReturnType());
            returnValueVar.appendLoad(returnInstructions, getFactory());
            returnInstructions.append(newReturnInstruction);
        } else {
            InstructionHandle lastReturnHandle = returns.get(returns.size() - 1);
            Instruction newReturnInstruction2 = Utility.copyInstruction(lastReturnHandle.getInstruction());
            returnInstructions.append(newReturnInstruction2);
        }
        return returnValueVar;
    }

    private boolean hasANonVoidReturnType() {
        return !getReturnType().equals(UnresolvedType.VOID);
    }

    private InstructionList getAfterReturningAdviceDispatchInstructions(BcelAdvice munger, InstructionHandle firstInstructionInReturnSequence) {
        InstructionList advice = new InstructionList();
        BcelVar tempVar = null;
        if (munger.hasExtraParameter()) {
            tempVar = insertAdviceInstructionsForBindingReturningParameter(advice);
        }
        advice.append(munger.getAdviceInstructions(this, tempVar, firstInstructionInReturnSequence));
        return advice;
    }

    private BcelVar insertAdviceInstructionsForBindingReturningParameter(InstructionList advice) {
        BcelVar tempVar;
        UnresolvedType tempVarType = getReturnType();
        if (tempVarType.equals(UnresolvedType.VOID)) {
            tempVar = genTempVar(UnresolvedType.OBJECT);
            advice.append(InstructionConstants.ACONST_NULL);
            tempVar.appendStore(advice, getFactory());
        } else {
            tempVar = genTempVar(tempVarType);
            advice.append(InstructionFactory.createDup(tempVarType.getSize()));
            tempVar.appendStore(advice, getFactory());
        }
        return tempVar;
    }

    private void retargetReturnInstruction(boolean hasReturningParameter, BcelVar returnValueVar, InstructionHandle gotoTarget, InstructionHandle returnHandle) {
        InstructionList newInstructions = new InstructionList();
        if (returnValueVar != null) {
            if (hasReturningParameter) {
                newInstructions.append(InstructionFactory.createDup(getReturnType().getSize()));
            }
            returnValueVar.appendStore(newInstructions, getFactory());
        }
        if (!isLastInstructionInRange(returnHandle, this.range)) {
            newInstructions.append(InstructionFactory.createBranchInstruction((short) 167, gotoTarget));
        }
        if (newInstructions.isEmpty()) {
            newInstructions.append(InstructionConstants.NOP);
        }
        Utility.replaceInstruction(returnHandle, newInstructions, this.enclosingMethod);
    }

    private boolean isLastInstructionInRange(InstructionHandle ih, ShadowRange aRange) {
        return ih.getNext() == aRange.getEnd();
    }

    public void weaveAfterThrowing(BcelAdvice munger, UnresolvedType catchType) {
        if (getRange().getStart().getNext() == getRange().getEnd()) {
            return;
        }
        InstructionFactory fact = getFactory();
        InstructionList handler = new InstructionList();
        BcelVar exceptionVar = genTempVar(catchType);
        exceptionVar.appendStore(handler, fact);
        if (getEnclosingMethod().getName().equals("<clinit>")) {
            ResolvedType eiieType = this.world.resolve("java.lang.ExceptionInInitializerError");
            ObjectType eiieBcelType = (ObjectType) BcelWorld.makeBcelType(eiieType);
            InstructionList ih = new InstructionList(InstructionConstants.NOP);
            handler.append(exceptionVar.createLoad(fact));
            handler.append(fact.createInstanceOf(eiieBcelType));
            InstructionBranch bi = InstructionFactory.createBranchInstruction((short) 153, ih.getStart());
            handler.append(bi);
            handler.append(exceptionVar.createLoad(fact));
            handler.append(fact.createCheckCast(eiieBcelType));
            handler.append(InstructionConstants.ATHROW);
            handler.append(ih);
        }
        InstructionList endHandler = new InstructionList(exceptionVar.createLoad(fact));
        handler.append(munger.getAdviceInstructions(this, exceptionVar, endHandler.getStart()));
        handler.append(endHandler);
        handler.append(InstructionConstants.ATHROW);
        InstructionHandle handlerStart = handler.getStart();
        if (isFallsThrough()) {
            InstructionHandle jumpTarget = handler.append(InstructionConstants.NOP);
            handler.insert(InstructionFactory.createBranchInstruction((short) 167, jumpTarget));
        }
        InstructionHandle protectedEnd = handler.getStart();
        this.range.insert(handler, Range.InsideAfter);
        this.enclosingMethod.addExceptionHandler(this.range.getStart().getNext(), protectedEnd.getPrev(), handlerStart, (ObjectType) BcelWorld.makeBcelType(catchType), getKind().hasHighPriorityExceptions());
    }

    public void weaveSoftener(BcelAdvice munger, UnresolvedType catchType) {
        if (getRange().getStart().getNext() == getRange().getEnd()) {
            return;
        }
        InstructionFactory fact = getFactory();
        InstructionList handler = new InstructionList();
        InstructionList rtExHandler = new InstructionList();
        BcelVar exceptionVar = genTempVar(catchType);
        handler.append(fact.createNew(NameMangler.SOFT_EXCEPTION_TYPE));
        handler.append(InstructionFactory.createDup(1));
        handler.append(exceptionVar.createLoad(fact));
        handler.append(fact.createInvoke(NameMangler.SOFT_EXCEPTION_TYPE, "<init>", Type.VOID, new Type[]{Type.THROWABLE}, (short) 183));
        handler.append(InstructionConstants.ATHROW);
        exceptionVar.appendStore(rtExHandler, fact);
        rtExHandler.append(exceptionVar.createLoad(fact));
        rtExHandler.append(fact.createInstanceOf(new ObjectType("java.lang.RuntimeException")));
        rtExHandler.append(InstructionFactory.createBranchInstruction((short) 153, handler.getStart()));
        rtExHandler.append(exceptionVar.createLoad(fact));
        rtExHandler.append(InstructionFactory.ATHROW);
        InstructionHandle handlerStart = rtExHandler.getStart();
        if (isFallsThrough()) {
            InstructionHandle jumpTarget = this.range.getEnd();
            rtExHandler.insert(InstructionFactory.createBranchInstruction((short) 167, jumpTarget));
        }
        rtExHandler.append(handler);
        InstructionHandle protectedEnd = rtExHandler.getStart();
        this.range.insert(rtExHandler, Range.InsideAfter);
        this.enclosingMethod.addExceptionHandler(this.range.getStart().getNext(), protectedEnd.getPrev(), handlerStart, (ObjectType) BcelWorld.makeBcelType(catchType), getKind().hasHighPriorityExceptions());
    }

    public void weavePerObjectEntry(BcelAdvice munger, BcelVar onVar) {
        InstructionFactory fact = getFactory();
        InstructionList entryInstructions = new InstructionList();
        InstructionList entrySuccessInstructions = new InstructionList();
        onVar.appendLoad(entrySuccessInstructions, fact);
        entrySuccessInstructions.append(Utility.createInvoke(fact, this.world, AjcMemberMaker.perObjectBind(munger.getConcreteAspect())));
        InstructionList testInstructions = munger.getTestInstructions(this, entrySuccessInstructions.getStart(), this.range.getRealStart(), entrySuccessInstructions.getStart());
        entryInstructions.append(testInstructions);
        entryInstructions.append(entrySuccessInstructions);
        this.range.insert(entryInstructions, Range.InsideBefore);
    }

    public void weavePerTypeWithinAspectInitialization(BcelAdvice munger, UnresolvedType t) {
        if (t.resolve(this.world).isInterface()) {
            return;
        }
        InstructionFactory fact = getFactory();
        InstructionList entryInstructions = new InstructionList();
        InstructionList entrySuccessInstructions = new InstructionList();
        BcelWorld.getBcelObjectType(munger.getConcreteAspect());
        String aspectname = munger.getConcreteAspect().getName();
        String ptwField = NameMangler.perTypeWithinFieldForTarget(munger.getConcreteAspect());
        entrySuccessInstructions.append(InstructionFactory.PUSH(fact.getConstantPool(), t.getName()));
        entrySuccessInstructions.append(fact.createInvoke(aspectname, NameMangler.PERTYPEWITHIN_CREATEASPECTINSTANCE_METHOD, new ObjectType(aspectname), new Type[]{new ObjectType("java.lang.String")}, (short) 184));
        entrySuccessInstructions.append(fact.createPutStatic(t.getName(), ptwField, new ObjectType(aspectname)));
        entryInstructions.append(entrySuccessInstructions);
        this.range.insert(entryInstructions, Range.InsideBefore);
    }

    public void weaveCflowEntry(final BcelAdvice munger, final Member cflowField) {
        boolean isPer = munger.getKind() == AdviceKind.PerCflowBelowEntry || munger.getKind() == AdviceKind.PerCflowEntry;
        if (!isPer && getKind() == PreInitialization) {
            return;
        }
        Type objectArrayType = new ArrayType(Type.OBJECT, 1);
        final InstructionFactory fact = getFactory();
        final BcelVar testResult = genTempVar(UnresolvedType.BOOLEAN);
        InstructionList entryInstructions = new InstructionList();
        InstructionList entrySuccessInstructions = new InstructionList();
        if (munger.hasDynamicTests()) {
            entryInstructions.append(Utility.createConstant(fact, 0));
            testResult.appendStore(entryInstructions, fact);
            entrySuccessInstructions.append(Utility.createConstant(fact, 1));
            testResult.appendStore(entrySuccessInstructions, fact);
        }
        if (isPer) {
            entrySuccessInstructions.append(fact.createInvoke(munger.getConcreteAspect().getName(), NameMangler.PERCFLOW_PUSH_METHOD, Type.VOID, new Type[0], (short) 184));
        } else {
            BcelVar[] cflowStateVars = munger.getExposedStateAsBcelVars(false);
            if (cflowStateVars.length == 0) {
                if (!cflowField.getType().getName().endsWith("CFlowCounter")) {
                    throw new RuntimeException("Incorrectly attempting counter operation on stacked cflow");
                }
                entrySuccessInstructions.append(Utility.createGet(fact, cflowField));
                entrySuccessInstructions.append(fact.createInvoke(NameMangler.CFLOW_COUNTER_TYPE, "inc", Type.VOID, new Type[0], (short) 182));
            } else {
                BcelVar arrayVar = genTempVar(UnresolvedType.OBJECTARRAY);
                int alen = cflowStateVars.length;
                entrySuccessInstructions.append(Utility.createConstant(fact, alen));
                entrySuccessInstructions.append(fact.createNewArray(Type.OBJECT, (short) 1));
                arrayVar.appendStore(entrySuccessInstructions, fact);
                for (int i = 0; i < alen; i++) {
                    arrayVar.appendConvertableArrayStore(entrySuccessInstructions, fact, i, cflowStateVars[i]);
                }
                entrySuccessInstructions.append(Utility.createGet(fact, cflowField));
                arrayVar.appendLoad(entrySuccessInstructions, fact);
                entrySuccessInstructions.append(fact.createInvoke(NameMangler.CFLOW_STACK_TYPE, "push", Type.VOID, new Type[]{objectArrayType}, (short) 182));
            }
        }
        InstructionList testInstructions = munger.getTestInstructions(this, entrySuccessInstructions.getStart(), this.range.getRealStart(), entrySuccessInstructions.getStart());
        entryInstructions.append(testInstructions);
        entryInstructions.append(entrySuccessInstructions);
        BcelAdvice exitAdvice = new BcelAdvice(null, null, null, 0, 0, 0, null, munger.getConcreteAspect()) { // from class: org.aspectj.weaver.bcel.BcelShadow.1
            @Override // org.aspectj.weaver.bcel.BcelAdvice
            public InstructionList getAdviceInstructions(BcelShadow s, BcelVar extraArgVar, InstructionHandle ifNoAdvice) {
                InstructionList exitInstructions = new InstructionList();
                if (munger.hasDynamicTests()) {
                    testResult.appendLoad(exitInstructions, fact);
                    exitInstructions.append(InstructionFactory.createBranchInstruction((short) 153, ifNoAdvice));
                }
                exitInstructions.append(Utility.createGet(fact, cflowField));
                if (munger.getKind() != AdviceKind.PerCflowEntry && munger.getKind() != AdviceKind.PerCflowBelowEntry && munger.getExposedStateAsBcelVars(false).length == 0) {
                    exitInstructions.append(fact.createInvoke(NameMangler.CFLOW_COUNTER_TYPE, "dec", Type.VOID, new Type[0], (short) 182));
                } else {
                    exitInstructions.append(fact.createInvoke(NameMangler.CFLOW_STACK_TYPE, "pop", Type.VOID, new Type[0], (short) 182));
                }
                return exitInstructions;
            }
        };
        weaveAfter(exitAdvice);
        this.range.insert(entryInstructions, Range.InsideBefore);
    }

    public void weaveAroundInline(BcelAdvice munger, boolean hasDynamicTest) throws ClassFormatException {
        InstructionList iList;
        boolean isProceedWithArgs;
        String string;
        Member mungerSig = munger.getSignature();
        if (mungerSig instanceof ResolvedMember) {
            ResolvedMember rm = (ResolvedMember) mungerSig;
            if (rm.hasBackingGenericMember()) {
                mungerSig = rm.getBackingGenericMember();
            }
        }
        ResolvedType declaringAspectType = this.world.resolve(mungerSig.getDeclaringType(), true);
        if (declaringAspectType.isMissing()) {
            this.world.getLint().cantFindType.signal(new String[]{WeaverMessages.format(WeaverMessages.CANT_FIND_TYPE_DURING_AROUND_WEAVE, declaringAspectType.getClassName())}, getSourceLocation(), new ISourceLocation[]{munger.getSourceLocation()});
        }
        ResolvedType rt = declaringAspectType.isParameterizedType() ? declaringAspectType.getGenericType() : declaringAspectType;
        BcelObjectType ot = BcelWorld.getBcelObjectType(rt);
        LazyMethodGen adviceMethod = ot.getLazyClassGen().getLazyMethodGen(mungerSig);
        if (!adviceMethod.getCanInline()) {
            weaveAroundClosure(munger, hasDynamicTest);
            return;
        }
        if (isAnnotationStylePassingProceedingJoinPointOutOfAdvice(munger, hasDynamicTest, adviceMethod)) {
            return;
        }
        this.enclosingMethod.setCanInline(false);
        LazyClassGen shadowClass = getEnclosingClass();
        String extractedShadowMethodName = NameMangler.aroundShadowMethodName(getSignature(), shadowClass.getNewGeneratedNameTag());
        List<String> parameterNames = new ArrayList<>();
        boolean shadowClassIsInterface = shadowClass.isInterface();
        LazyMethodGen extractedShadowMethod = extractShadowInstructionsIntoNewMethod(extractedShadowMethodName, shadowClassIsInterface ? 1 : 2, munger.getSourceLocation(), parameterNames, shadowClassIsInterface);
        List<BcelVar> argsToCallLocalAdviceMethodWith = new ArrayList<>();
        List<BcelVar> proceedVarList = new ArrayList<>();
        int extraParamOffset = 0;
        if (this.thisVar != null) {
            argsToCallLocalAdviceMethodWith.add(this.thisVar);
            proceedVarList.add(new BcelVar(this.thisVar.getType(), 0));
            extraParamOffset = 0 + this.thisVar.getType().getSize();
        }
        if (this.targetVar != null && this.targetVar != this.thisVar) {
            argsToCallLocalAdviceMethodWith.add(this.targetVar);
            proceedVarList.add(new BcelVar(this.targetVar.getType(), extraParamOffset));
            extraParamOffset += this.targetVar.getType().getSize();
        }
        int len = getArgCount();
        for (int i = 0; i < len; i++) {
            argsToCallLocalAdviceMethodWith.add(this.argVars[i]);
            proceedVarList.add(new BcelVar(this.argVars[i].getType(), extraParamOffset));
            extraParamOffset += this.argVars[i].getType().getSize();
        }
        if (this.thisJoinPointVar != null) {
            argsToCallLocalAdviceMethodWith.add(this.thisJoinPointVar);
            proceedVarList.add(new BcelVar(this.thisJoinPointVar.getType(), extraParamOffset));
            extraParamOffset += this.thisJoinPointVar.getType().getSize();
        }
        Type[] adviceParameterTypes = BcelWorld.makeBcelTypes(munger.getSignature().getParameterTypes());
        adviceMethod.getArgumentTypes();
        Type[] extractedMethodParameterTypes = extractedShadowMethod.getArgumentTypes();
        Type[] parameterTypes = new Type[extractedMethodParameterTypes.length + adviceParameterTypes.length + 1];
        System.arraycopy(extractedMethodParameterTypes, 0, parameterTypes, 0, extractedMethodParameterTypes.length);
        int parameterIndex = 0 + extractedMethodParameterTypes.length;
        parameterTypes[parameterIndex] = BcelWorld.makeBcelType(adviceMethod.getEnclosingClass().getType());
        System.arraycopy(adviceParameterTypes, 0, parameterTypes, parameterIndex + 1, adviceParameterTypes.length);
        String localAdviceMethodName = NameMangler.aroundAdviceMethodName(getSignature(), shadowClass.getNewGeneratedNameTag());
        int localAdviceMethodModifiers = 2 | (this.world.useFinal() & (!shadowClassIsInterface) ? 16 : 0) | 8;
        LazyMethodGen localAdviceMethod = new LazyMethodGen(localAdviceMethodModifiers, BcelWorld.makeBcelType(mungerSig.getReturnType()), localAdviceMethodName, parameterTypes, NoDeclaredExceptions, shadowClass);
        shadowClass.addMethodGen(localAdviceMethod);
        int nVars = adviceMethod.getMaxLocals() + extraParamOffset;
        IntMap varMap = IntMap.idMap(nVars);
        for (int i2 = extraParamOffset; i2 < nVars; i2++) {
            varMap.put(i2 - extraParamOffset, i2);
        }
        InstructionFactory fact = getFactory();
        localAdviceMethod.getBody().insert(BcelClassWeaver.genInlineInstructions(adviceMethod, localAdviceMethod, varMap, fact, true));
        localAdviceMethod.setMaxLocals(nVars);
        InstructionList advice = new InstructionList();
        for (BcelVar var : argsToCallLocalAdviceMethodWith) {
            var.appendLoad(advice, fact);
        }
        boolean isAnnoStyleConcreteAspect = munger.getConcreteAspect().isAnnotationStyleAspect();
        boolean isAnnoStyleDeclaringAspect = munger.getDeclaringAspect() != null ? munger.getDeclaringAspect().resolve(this.world).isAnnotationStyleAspect() : false;
        if (isAnnoStyleConcreteAspect && isAnnoStyleDeclaringAspect) {
            iList = loadThisJoinPoint();
            iList.append(Utility.createConversion(getFactory(), LazyClassGen.tjpType, LazyClassGen.proceedingTjpType));
        } else {
            iList = new InstructionList(InstructionConstants.ACONST_NULL);
        }
        advice.append(munger.getAdviceArgSetup(this, null, iList));
        advice.append(Utility.createInvoke(fact, localAdviceMethod));
        advice.append(Utility.createConversion(getFactory(), BcelWorld.makeBcelType(mungerSig.getReturnType()), extractedShadowMethod.getReturnType(), this.world.isInJava5Mode()));
        if (!isFallsThrough()) {
            advice.append(InstructionFactory.createReturn(extractedShadowMethod.getReturnType()));
        }
        if (!hasDynamicTest) {
            this.range.append(advice);
        } else {
            InstructionList afterThingie = new InstructionList(InstructionConstants.NOP);
            InstructionList callback = makeCallToCallback(extractedShadowMethod);
            if (terminatesWithReturn()) {
                callback.append(InstructionFactory.createReturn(extractedShadowMethod.getReturnType()));
            } else {
                advice.append(InstructionFactory.createBranchInstruction((short) 167, afterThingie.getStart()));
            }
            this.range.append(munger.getTestInstructions(this, advice.getStart(), callback.getStart(), advice.getStart()));
            this.range.append(advice);
            this.range.append(callback);
            this.range.append(afterThingie);
        }
        if (!munger.getDeclaringType().isAnnotationStyleAspect()) {
            String proceedName = NameMangler.proceedMethodName(munger.getSignature().getName());
            InstructionHandle curr = localAdviceMethod.getBody().getStart();
            InstructionHandle end = localAdviceMethod.getBody().getEnd();
            ConstantPool cpg = localAdviceMethod.getEnclosingClass().getConstantPool();
            while (curr != end) {
                InstructionHandle next = curr.getNext();
                Instruction inst = curr.getInstruction();
                if (inst.opcode == 184 && proceedName.equals(((InvokeInstruction) inst).getMethodName(cpg))) {
                    localAdviceMethod.getBody().append(curr, getRedoneProceedCall(fact, extractedShadowMethod, munger, localAdviceMethod, proceedVarList));
                    Utility.deleteInstruction(curr, localAdviceMethod);
                }
                curr = next;
            }
        } else {
            InstructionHandle curr2 = localAdviceMethod.getBody().getStart();
            InstructionHandle end2 = localAdviceMethod.getBody().getEnd();
            ConstantPool cpg2 = localAdviceMethod.getEnclosingClass().getConstantPool();
            while (curr2 != end2) {
                InstructionHandle next2 = curr2.getNext();
                Instruction inst2 = curr2.getInstruction();
                if ((inst2 instanceof INVOKEINTERFACE) && "proceed".equals(((INVOKEINTERFACE) inst2).getMethodName(cpg2))) {
                    if (((INVOKEINTERFACE) inst2).getArgumentTypes(cpg2).length == 1) {
                        isProceedWithArgs = true;
                    } else {
                        isProceedWithArgs = false;
                    }
                    InstructionList insteadProceedIl = getRedoneProceedCallForAnnotationStyle(fact, extractedShadowMethod, munger, localAdviceMethod, proceedVarList, isProceedWithArgs);
                    localAdviceMethod.getBody().append(curr2, insteadProceedIl);
                    Utility.deleteInstruction(curr2, localAdviceMethod);
                }
                curr2 = next2;
            }
        }
        InstructionHandle start = localAdviceMethod.getBody().getStart();
        InstructionHandle end3 = localAdviceMethod.getBody().getEnd();
        while (start.getInstruction().opcode == 254) {
            start = start.getNext();
        }
        while (end3.getInstruction().opcode == 254) {
            end3 = end3.getPrev();
        }
        Type[] args = localAdviceMethod.getArgumentTypes();
        int argNumber = 0;
        int slot = 0;
        while (slot < extraParamOffset) {
            if (argNumber >= args.length || parameterNames.size() == 0 || argNumber >= parameterNames.size()) {
                string = new StringBuffer("unknown").append(argNumber).toString();
            } else {
                string = parameterNames.get(argNumber);
            }
            String argumentName = string;
            String argumentSignature = args[argNumber].getSignature();
            LocalVariableTag lvt = new LocalVariableTag(argumentSignature, argumentName, slot, 0);
            start.addTargeter(lvt);
            end3.addTargeter(lvt);
            slot += args[argNumber].getSize();
            argNumber++;
        }
    }

    private boolean isAnnotationStylePassingProceedingJoinPointOutOfAdvice(BcelAdvice munger, boolean hasDynamicTest, LazyMethodGen adviceMethod) {
        if (munger.getConcreteAspect().isAnnotationStyleAspect()) {
            boolean canSeeProceedPassedToOther = false;
            InstructionHandle curr = adviceMethod.getBody().getStart();
            InstructionHandle end = adviceMethod.getBody().getEnd();
            ConstantPool cpg = adviceMethod.getEnclosingClass().getConstantPool();
            while (true) {
                if (curr == end) {
                    break;
                }
                InstructionHandle next = curr.getNext();
                Instruction inst = curr.getInstruction();
                if ((inst instanceof InvokeInstruction) && ((InvokeInstruction) inst).getSignature(cpg).indexOf("Lorg/aspectj/lang/ProceedingJoinPoint;") > 0) {
                    canSeeProceedPassedToOther = true;
                    break;
                }
                curr = next;
            }
            if (canSeeProceedPassedToOther) {
                adviceMethod.setCanInline(false);
                weaveAroundClosure(munger, hasDynamicTest);
                return true;
            }
            return false;
        }
        return false;
    }

    private InstructionList getRedoneProceedCall(InstructionFactory fact, LazyMethodGen callbackMethod, BcelAdvice munger, LazyMethodGen localAdviceMethod, List<BcelVar> argVarList) {
        InstructionList ret = new InstructionList();
        BcelVar[] adviceVars = munger.getExposedStateAsBcelVars(true);
        IntMap proceedMap = makeProceedArgumentMap(adviceVars);
        ResolvedType[] proceedParamTypes = this.world.resolve(munger.getSignature().getParameterTypes());
        if (munger.getBaseParameterCount() + 1 < proceedParamTypes.length) {
            int len = munger.getBaseParameterCount() + 1;
            ResolvedType[] newTypes = new ResolvedType[len];
            System.arraycopy(proceedParamTypes, 0, newTypes, 0, len);
            proceedParamTypes = newTypes;
        }
        BcelVar[] proceedVars = Utility.pushAndReturnArrayOfVars(proceedParamTypes, ret, fact, localAdviceMethod);
        Type[] stateTypes = callbackMethod.getArgumentTypes();
        int len2 = stateTypes.length;
        for (int i = 0; i < len2; i++) {
            Type stateType = stateTypes[i];
            ResolvedType stateTypeX = BcelWorld.fromBcel(stateType).resolve(this.world);
            if (proceedMap.hasKey(i)) {
                proceedVars[proceedMap.get(i)].appendLoadAndConvert(ret, fact, stateTypeX);
            } else {
                argVarList.get(i).appendLoad(ret, fact);
            }
        }
        ret.append(Utility.createInvoke(fact, callbackMethod));
        ret.append(Utility.createConversion(fact, callbackMethod.getReturnType(), BcelWorld.makeBcelType(munger.getSignature().getReturnType()), this.world.isInJava5Mode()));
        return ret;
    }

    private InstructionList getRedoneProceedCallForAnnotationStyle(InstructionFactory fact, LazyMethodGen callbackMethod, BcelAdvice munger, LazyMethodGen localAdviceMethod, List<BcelVar> argVarList, boolean isProceedWithArgs) throws ClassFormatException {
        InstructionList ret = new InstructionList();
        if (isProceedWithArgs) {
            Type objectArrayType = Type.OBJECT_ARRAY;
            int theObjectArrayLocalNumber = localAdviceMethod.allocateLocal(objectArrayType);
            ret.append(InstructionFactory.createStore(objectArrayType, theObjectArrayLocalNumber));
            Type proceedingJpType = Type.getType("Lorg/aspectj/lang/ProceedingJoinPoint;");
            int pjpLocalNumber = localAdviceMethod.allocateLocal(proceedingJpType);
            ret.append(InstructionFactory.createStore(proceedingJpType, pjpLocalNumber));
            boolean pointcutBindsThis = bindsThis(munger);
            boolean pointcutBindsTarget = bindsTarget(munger);
            boolean targetIsSameAsThis = getKind().isTargetSameAsThis();
            int nextArgumentToProvideForCallback = 0;
            if (hasThis() && (!pointcutBindsTarget || !targetIsSameAsThis)) {
                if (pointcutBindsThis) {
                    ret.append(InstructionFactory.createLoad(objectArrayType, theObjectArrayLocalNumber));
                    ret.append(Utility.createConstant(fact, 0));
                    ret.append(InstructionFactory.createArrayLoad(Type.OBJECT));
                    ret.append(Utility.createConversion(fact, Type.OBJECT, callbackMethod.getArgumentTypes()[0]));
                } else {
                    ret.append(InstructionFactory.createALOAD(0));
                }
                nextArgumentToProvideForCallback = 0 + 1;
            }
            if (hasTarget()) {
                if (pointcutBindsTarget) {
                    if (getKind().isTargetSameAsThis()) {
                        ret.append(InstructionFactory.createLoad(objectArrayType, theObjectArrayLocalNumber));
                        ret.append(Utility.createConstant(fact, pointcutBindsThis ? 1 : 0));
                        ret.append(InstructionFactory.createArrayLoad(Type.OBJECT));
                        ret.append(Utility.createConversion(fact, Type.OBJECT, callbackMethod.getArgumentTypes()[0]));
                    } else {
                        int position = (hasThis() && pointcutBindsThis) ? 1 : 0;
                        ret.append(InstructionFactory.createLoad(objectArrayType, theObjectArrayLocalNumber));
                        ret.append(Utility.createConstant(fact, position));
                        ret.append(InstructionFactory.createArrayLoad(Type.OBJECT));
                        ret.append(Utility.createConversion(fact, Type.OBJECT, callbackMethod.getArgumentTypes()[nextArgumentToProvideForCallback]));
                    }
                    nextArgumentToProvideForCallback++;
                } else if (!getKind().isTargetSameAsThis()) {
                    ret.append(InstructionFactory.createLoad(localAdviceMethod.getArgumentTypes()[0], hasThis() ? 1 : 0));
                    nextArgumentToProvideForCallback++;
                }
            }
            int indexIntoObjectArrayForArguments = (pointcutBindsThis ? 1 : 0) + (pointcutBindsTarget ? 1 : 0);
            int len = callbackMethod.getArgumentTypes().length;
            for (int i = nextArgumentToProvideForCallback; i < len; i++) {
                Type stateType = callbackMethod.getArgumentTypes()[i];
                BcelWorld.fromBcel(stateType).resolve(this.world);
                if ("Lorg/aspectj/lang/JoinPoint;".equals(stateType.getSignature())) {
                    ret.append(new InstructionLV((short) 25, pjpLocalNumber));
                } else {
                    ret.append(InstructionFactory.createLoad(objectArrayType, theObjectArrayLocalNumber));
                    ret.append(Utility.createConstant(fact, (i - nextArgumentToProvideForCallback) + indexIntoObjectArrayForArguments));
                    ret.append(InstructionFactory.createArrayLoad(Type.OBJECT));
                    ret.append(Utility.createConversion(fact, Type.OBJECT, stateType));
                }
            }
        } else {
            Type proceedingJpType2 = Type.getType("Lorg/aspectj/lang/ProceedingJoinPoint;");
            int localJp = localAdviceMethod.allocateLocal(proceedingJpType2);
            ret.append(InstructionFactory.createStore(proceedingJpType2, localJp));
            int idx = 0;
            int len2 = callbackMethod.getArgumentTypes().length;
            for (int i2 = 0; i2 < len2; i2++) {
                Type stateType2 = callbackMethod.getArgumentTypes()[i2];
                BcelWorld.fromBcel(stateType2).resolve(this.world);
                if ("Lorg/aspectj/lang/JoinPoint;".equals(stateType2.getSignature())) {
                    ret.append(InstructionFactory.createALOAD(localJp));
                    idx++;
                } else {
                    ret.append(InstructionFactory.createLoad(stateType2, idx));
                    idx += stateType2.getSize();
                }
            }
        }
        ret.append(Utility.createInvoke(fact, callbackMethod));
        if (!UnresolvedType.OBJECT.equals(munger.getSignature().getReturnType())) {
            ret.append(Utility.createConversion(fact, callbackMethod.getReturnType(), Type.OBJECT));
        }
        ret.append(Utility.createConversion(fact, callbackMethod.getReturnType(), BcelWorld.makeBcelType(munger.getSignature().getReturnType()), this.world.isInJava5Mode()));
        return ret;
    }

    private boolean bindsThis(BcelAdvice munger) {
        UsesThisVisitor utv = new UsesThisVisitor();
        munger.getPointcut().accept(utv, null);
        return utv.usesThis;
    }

    private boolean bindsTarget(BcelAdvice munger) {
        UsesTargetVisitor utv = new UsesTargetVisitor();
        munger.getPointcut().accept(utv, null);
        return utv.usesTarget;
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/BcelShadow$UsesThisVisitor.class */
    private static class UsesThisVisitor extends AbstractPatternNodeVisitor {
        boolean usesThis;

        private UsesThisVisitor() {
            this.usesThis = false;
        }

        @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
        public Object visit(ThisOrTargetPointcut node, Object data) {
            if (node.isThis() && node.isBinding()) {
                this.usesThis = true;
            }
            return node;
        }

        @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
        public Object visit(AndPointcut node, Object data) {
            if (!this.usesThis) {
                node.getLeft().accept(this, data);
            }
            if (!this.usesThis) {
                node.getRight().accept(this, data);
            }
            return node;
        }

        @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
        public Object visit(NotPointcut node, Object data) {
            if (!this.usesThis) {
                node.getNegatedPointcut().accept(this, data);
            }
            return node;
        }

        @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
        public Object visit(OrPointcut node, Object data) {
            if (!this.usesThis) {
                node.getLeft().accept(this, data);
            }
            if (!this.usesThis) {
                node.getRight().accept(this, data);
            }
            return node;
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/BcelShadow$UsesTargetVisitor.class */
    private static class UsesTargetVisitor extends AbstractPatternNodeVisitor {
        boolean usesTarget;

        private UsesTargetVisitor() {
            this.usesTarget = false;
        }

        @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
        public Object visit(ThisOrTargetPointcut node, Object data) {
            if (!node.isThis() && node.isBinding()) {
                this.usesTarget = true;
            }
            return node;
        }

        @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
        public Object visit(AndPointcut node, Object data) {
            if (!this.usesTarget) {
                node.getLeft().accept(this, data);
            }
            if (!this.usesTarget) {
                node.getRight().accept(this, data);
            }
            return node;
        }

        @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
        public Object visit(NotPointcut node, Object data) {
            if (!this.usesTarget) {
                node.getNegatedPointcut().accept(this, data);
            }
            return node;
        }

        @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
        public Object visit(OrPointcut node, Object data) {
            if (!this.usesTarget) {
                node.getLeft().accept(this, data);
            }
            if (!this.usesTarget) {
                node.getRight().accept(this, data);
            }
            return node;
        }
    }

    public void weaveAroundClosure(BcelAdvice munger, boolean hasDynamicTest) {
        InstructionList returnConversionCode;
        InstructionFactory fact = getFactory();
        this.enclosingMethod.setCanInline(false);
        int linenumber = getSourceLine();
        boolean shadowClassIsInterface = getEnclosingClass().isInterface();
        LazyMethodGen callbackMethod = extractShadowInstructionsIntoNewMethod(NameMangler.aroundShadowMethodName(getSignature(), getEnclosingClass().getNewGeneratedNameTag()), shadowClassIsInterface ? 1 : 0, munger.getSourceLocation(), new ArrayList(), shadowClassIsInterface);
        BcelVar[] adviceVars = munger.getExposedStateAsBcelVars(true);
        String closureClassName = NameMangler.makeClosureClassName(getEnclosingClass().getType(), getEnclosingClass().getNewGeneratedNameTag());
        Member constructorSig = new MemberImpl(Member.CONSTRUCTOR, UnresolvedType.forName(closureClassName), 0, "<init>", "([Ljava/lang/Object;)V");
        BcelVar closureHolder = null;
        if (getKind() == PreInitialization) {
            closureHolder = genTempVar(AjcMemberMaker.AROUND_CLOSURE_TYPE);
        }
        InstructionList closureInstantiation = makeClosureInstantiation(constructorSig, closureHolder);
        makeClosureClassAndReturnConstructor(closureClassName, callbackMethod, makeProceedArgumentMap(adviceVars));
        if (getKind() == PreInitialization) {
            returnConversionCode = new InstructionList();
            BcelVar stateTempVar = genTempVar(UnresolvedType.OBJECTARRAY);
            closureHolder.appendLoad(returnConversionCode, fact);
            returnConversionCode.append(Utility.createInvoke(fact, this.world, AjcMemberMaker.aroundClosurePreInitializationGetter()));
            stateTempVar.appendStore(returnConversionCode, fact);
            Type[] stateTypes = getSuperConstructorParameterTypes();
            returnConversionCode.append(InstructionConstants.ALOAD_0);
            int len = stateTypes.length;
            for (int i = 0; i < len; i++) {
                UnresolvedType bcelTX = BcelWorld.fromBcel(stateTypes[i]);
                ResolvedType stateRTX = this.world.resolve(bcelTX, true);
                if (stateRTX.isMissing()) {
                    this.world.getLint().cantFindType.signal(new String[]{WeaverMessages.format(WeaverMessages.CANT_FIND_TYPE_DURING_AROUND_WEAVE_PREINIT, bcelTX.getClassName())}, getSourceLocation(), new ISourceLocation[]{munger.getSourceLocation()});
                }
                stateTempVar.appendConvertableArrayLoad(returnConversionCode, fact, i, stateRTX);
            }
        } else {
            Member mungerSignature = munger.getSignature();
            if ((munger.getSignature() instanceof ResolvedMember) && ((ResolvedMember) mungerSignature).hasBackingGenericMember()) {
                mungerSignature = ((ResolvedMember) mungerSignature).getBackingGenericMember();
            }
            UnresolvedType returnType = mungerSignature.getReturnType();
            returnConversionCode = Utility.createConversion(getFactory(), BcelWorld.makeBcelType(returnType), callbackMethod.getReturnType(), this.world.isInJava5Mode());
            if (!isFallsThrough()) {
                returnConversionCode.append(InstructionFactory.createReturn(callbackMethod.getReturnType()));
            }
        }
        int bitflags = 0;
        if (getKind().isTargetSameAsThis()) {
            bitflags = 0 | 65536;
        }
        if (hasThis()) {
            bitflags |= 4096;
        }
        if (bindsThis(munger)) {
            bitflags |= 256;
        }
        if (hasTarget()) {
            bitflags |= 16;
        }
        if (bindsTarget(munger)) {
            bitflags |= 1;
        }
        if (munger.getConcreteAspect() != null && munger.getConcreteAspect().isAnnotationStyleAspect() && munger.getDeclaringAspect() != null && munger.getDeclaringAspect().resolve(this.world).isAnnotationStyleAspect()) {
            closureInstantiation.append(fact.createConstant(Integer.valueOf(bitflags)));
            closureInstantiation.append(Utility.createInvoke(getFactory(), getWorld(), new MemberImpl(Member.METHOD, UnresolvedType.forName("org.aspectj.runtime.internal.AroundClosure"), 1, "linkClosureAndJoinPoint", "(I)Lorg/aspectj/lang/ProceedingJoinPoint;")));
        }
        InstructionList advice = new InstructionList();
        advice.append(munger.getAdviceArgSetup(this, null, closureInstantiation));
        advice.append(munger.getNonTestAdviceInstructions(this));
        advice.append(returnConversionCode);
        if (getKind() == Shadow.MethodExecution && linenumber > 0) {
            advice.getStart().addTargeter(new LineNumberTag(linenumber));
        }
        if (!hasDynamicTest) {
            this.range.append(advice);
            return;
        }
        InstructionList callback = makeCallToCallback(callbackMethod);
        InstructionList postCallback = new InstructionList();
        if (terminatesWithReturn()) {
            callback.append(InstructionFactory.createReturn(callbackMethod.getReturnType()));
        } else {
            advice.append(InstructionFactory.createBranchInstruction((short) 167, postCallback.append(InstructionConstants.NOP)));
        }
        this.range.append(munger.getTestInstructions(this, advice.getStart(), callback.getStart(), advice.getStart()));
        this.range.append(advice);
        this.range.append(callback);
        this.range.append(postCallback);
    }

    InstructionList makeCallToCallback(LazyMethodGen callbackMethod) {
        InstructionFactory fact = getFactory();
        InstructionList callback = new InstructionList();
        if (this.thisVar != null) {
            callback.append(InstructionConstants.ALOAD_0);
        }
        if (this.targetVar != null && this.targetVar != this.thisVar) {
            callback.append(BcelRenderer.renderExpr(fact, this.world, this.targetVar));
        }
        callback.append(BcelRenderer.renderExprs(fact, this.world, this.argVars));
        if (this.thisJoinPointVar != null) {
            callback.append(BcelRenderer.renderExpr(fact, this.world, this.thisJoinPointVar));
        }
        callback.append(Utility.createInvoke(fact, callbackMethod));
        return callback;
    }

    private InstructionList makeClosureInstantiation(Member constructor, BcelVar holder) {
        InstructionFactory fact = getFactory();
        BcelVar arrayVar = genTempVar(UnresolvedType.OBJECTARRAY);
        InstructionList il = new InstructionList();
        int alen = getArgCount() + (this.thisVar == null ? 0 : 1) + ((this.targetVar == null || this.targetVar == this.thisVar) ? 0 : 1) + (this.thisJoinPointVar == null ? 0 : 1);
        il.append(Utility.createConstant(fact, alen));
        il.append(fact.createNewArray(Type.OBJECT, (short) 1));
        arrayVar.appendStore(il, fact);
        int stateIndex = 0;
        if (this.thisVar != null) {
            arrayVar.appendConvertableArrayStore(il, fact, 0, this.thisVar);
            this.thisVar.setPositionInAroundState(0);
            stateIndex = 0 + 1;
        }
        if (this.targetVar != null && this.targetVar != this.thisVar) {
            arrayVar.appendConvertableArrayStore(il, fact, stateIndex, this.targetVar);
            this.targetVar.setPositionInAroundState(stateIndex);
            stateIndex++;
        }
        int len = getArgCount();
        for (int i = 0; i < len; i++) {
            arrayVar.appendConvertableArrayStore(il, fact, stateIndex, this.argVars[i]);
            this.argVars[i].setPositionInAroundState(stateIndex);
            stateIndex++;
        }
        if (this.thisJoinPointVar != null) {
            arrayVar.appendConvertableArrayStore(il, fact, stateIndex, this.thisJoinPointVar);
            this.thisJoinPointVar.setPositionInAroundState(stateIndex);
            int i2 = stateIndex + 1;
        }
        il.append(fact.createNew(new ObjectType(constructor.getDeclaringType().getName())));
        il.append(InstructionConstants.DUP);
        arrayVar.appendLoad(il, fact);
        il.append(Utility.createInvoke(fact, this.world, constructor));
        if (getKind() == PreInitialization) {
            il.append(InstructionConstants.DUP);
            holder.appendStore(il, fact);
        }
        return il;
    }

    private IntMap makeProceedArgumentMap(BcelVar[] adviceArgs) {
        int pos;
        IntMap ret = new IntMap();
        int len = adviceArgs.length;
        for (int i = 0; i < len; i++) {
            BcelVar v = adviceArgs[i];
            if (v != null && (pos = v.getPositionInAroundState()) >= 0) {
                ret.put(pos, i);
            }
        }
        return ret;
    }

    private LazyMethodGen makeClosureClassAndReturnConstructor(String closureClassName, LazyMethodGen callbackMethod, IntMap proceedMap) {
        Type objectArrayType = new ArrayType(Type.OBJECT, 1);
        LazyClassGen closureClass = new LazyClassGen(closureClassName, "org.aspectj.runtime.internal.AroundClosure", getEnclosingClass().getFileName(), 1, new String[0], getWorld());
        InstructionFactory fact = new InstructionFactory(closureClass.getConstantPool());
        LazyMethodGen constructor = new LazyMethodGen(1, Type.VOID, "<init>", new Type[]{objectArrayType}, new String[0], closureClass);
        InstructionList cbody = constructor.getBody();
        cbody.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        cbody.append(InstructionFactory.createLoad(objectArrayType, 1));
        cbody.append(fact.createInvoke("org.aspectj.runtime.internal.AroundClosure", "<init>", Type.VOID, new Type[]{objectArrayType}, (short) 183));
        cbody.append(InstructionFactory.createReturn(Type.VOID));
        closureClass.addMethodGen(constructor);
        LazyMethodGen runMethod = new LazyMethodGen(1, Type.OBJECT, "run", new Type[]{objectArrayType}, new String[0], closureClass);
        InstructionList mbody = runMethod.getBody();
        BcelVar proceedVar = new BcelVar(UnresolvedType.OBJECTARRAY.resolve(this.world), 1);
        BcelVar stateVar = new BcelVar(UnresolvedType.OBJECTARRAY.resolve(this.world), runMethod.allocateLocal(1));
        mbody.append(InstructionFactory.createThis());
        mbody.append(fact.createGetField("org.aspectj.runtime.internal.AroundClosure", "state", objectArrayType));
        mbody.append(stateVar.createStore(fact));
        Type[] stateTypes = callbackMethod.getArgumentTypes();
        int len = stateTypes.length;
        for (int i = 0; i < len; i++) {
            ResolvedType resolvedStateType = BcelWorld.fromBcel(stateTypes[i]).resolve(this.world);
            if (proceedMap.hasKey(i)) {
                mbody.append(proceedVar.createConvertableArrayLoad(fact, proceedMap.get(i), resolvedStateType));
            } else {
                mbody.append(stateVar.createConvertableArrayLoad(fact, i, resolvedStateType));
            }
        }
        mbody.append(Utility.createInvoke(fact, callbackMethod));
        if (getKind() == PreInitialization) {
            mbody.append(Utility.createSet(fact, AjcMemberMaker.aroundClosurePreInitializationField()));
            mbody.append(InstructionConstants.ACONST_NULL);
        } else {
            mbody.append(Utility.createConversion(fact, callbackMethod.getReturnType(), Type.OBJECT));
        }
        mbody.append(InstructionFactory.createReturn(Type.OBJECT));
        closureClass.addMethodGen(runMethod);
        getEnclosingClass().addGeneratedInner(closureClass);
        return constructor;
    }

    LazyMethodGen extractShadowInstructionsIntoNewMethod(String extractedMethodName, int extractedMethodVisibilityModifier, ISourceLocation adviceSourceLocation, List<String> parameterNames, boolean beingPlacedInInterface) {
        if (!getKind().allowsExtraction()) {
            throw new BCException("Attempt to extract method from a shadow kind (" + getKind() + ") that does not support this operation");
        }
        LazyMethodGen newMethod = createShadowMethodGen(extractedMethodName, extractedMethodVisibilityModifier, parameterNames, beingPlacedInInterface);
        IntMap remapper = makeRemap();
        this.range.extractInstructionsInto(newMethod, remapper, getKind() != PreInitialization && isFallsThrough());
        if (getKind() == PreInitialization) {
            addPreInitializationReturnCode(newMethod, getSuperConstructorParameterTypes());
        }
        getEnclosingClass().addMethodGen(newMethod, adviceSourceLocation);
        return newMethod;
    }

    private void addPreInitializationReturnCode(LazyMethodGen extractedMethod, Type[] superConstructorTypes) {
        InstructionList body = extractedMethod.getBody();
        InstructionFactory fact = getFactory();
        BcelVar arrayVar = new BcelVar(this.world.getCoreType(UnresolvedType.OBJECTARRAY), extractedMethod.allocateLocal(1));
        int len = superConstructorTypes.length;
        body.append(Utility.createConstant(fact, len));
        body.append(fact.createNewArray(Type.OBJECT, (short) 1));
        arrayVar.appendStore(body, fact);
        for (int i = len - 1; i >= 0; i++) {
            body.append(Utility.createConversion(fact, superConstructorTypes[i], Type.OBJECT));
            arrayVar.appendLoad(body, fact);
            body.append(InstructionConstants.SWAP);
            body.append(Utility.createConstant(fact, i));
            body.append(InstructionConstants.SWAP);
            body.append(InstructionFactory.createArrayStore(Type.OBJECT));
        }
        arrayVar.appendLoad(body, fact);
        body.append(InstructionConstants.ARETURN);
    }

    private Type[] getSuperConstructorParameterTypes() {
        InstructionHandle superCallHandle = getRange().getEnd().getNext();
        InvokeInstruction superCallInstruction = (InvokeInstruction) superCallHandle.getInstruction();
        return superCallInstruction.getArgumentTypes(getEnclosingClass().getConstantPool());
    }

    private IntMap makeRemap() {
        IntMap ret = new IntMap(5);
        int reti = 0;
        if (this.thisVar != null) {
            reti = 0 + 1;
            ret.put(0, 0);
        }
        if (this.targetVar != null && this.targetVar != this.thisVar) {
            int i = reti;
            reti++;
            ret.put(this.targetVar.getSlot(), i);
        }
        int len = this.argVars.length;
        for (int i2 = 0; i2 < len; i2++) {
            ret.put(this.argVars[i2].getSlot(), reti);
            reti += this.argVars[i2].getType().getSize();
        }
        if (this.thisJoinPointVar != null) {
            int i3 = reti;
            int i4 = reti + 1;
            ret.put(this.thisJoinPointVar.getSlot(), i3);
        }
        if (!getKind().argsOnStack()) {
            int oldi = 0;
            int newi = 0;
            if (arg0HoldsThis()) {
                ret.put(0, 0);
                oldi = 0 + 1;
                newi = 0 + 1;
            }
            for (int i5 = 0; i5 < getArgCount(); i5++) {
                UnresolvedType type = getArgType(i5);
                ret.put(oldi, newi);
                oldi += type.getSize();
                newi += type.getSize();
            }
        }
        return ret;
    }

    private LazyMethodGen createShadowMethodGen(String newMethodName, int visibilityModifier, List<String> parameterNames, boolean beingPlacedInInterface) {
        UnresolvedType returnType;
        Type[] shadowParameterTypes = BcelWorld.makeBcelTypes(getArgTypes());
        int modifiers = ((!this.world.useFinal() || beingPlacedInInterface) ? 0 : 16) | 8 | visibilityModifier;
        if (this.targetVar != null && this.targetVar != this.thisVar) {
            UnresolvedType targetType = ensureTargetTypeIsCorrect(getTargetType());
            if ((getKind() == FieldGet || getKind() == FieldSet) && getActualTargetType() != null && !getActualTargetType().equals(targetType.getName())) {
                targetType = UnresolvedType.forName(getActualTargetType()).resolve(this.world);
            }
            ResolvedMember resolvedMember = getSignature().resolve(this.world);
            if (resolvedMember != null && Modifier.isProtected(resolvedMember.getModifiers()) && !samePackage(resolvedMember.getDeclaringType().getPackageName(), getEnclosingType().getPackageName()) && !resolvedMember.getName().equals("clone")) {
                if (!hasThis()) {
                    if (Modifier.isStatic(this.enclosingMethod.getAccessFlags()) && this.enclosingMethod.getName().startsWith("access$")) {
                        targetType = BcelWorld.fromBcel(this.enclosingMethod.getArgumentTypes()[0]);
                    }
                } else {
                    if (!targetType.resolve(this.world).isAssignableFrom(getThisType().resolve(this.world))) {
                        throw new BCException("bad bytecode");
                    }
                    targetType = getThisType();
                }
            }
            parameterNames.add(DataBinder.DEFAULT_OBJECT_NAME);
            shadowParameterTypes = addTypeToFront(BcelWorld.makeBcelType(targetType), shadowParameterTypes);
        }
        if (this.thisVar != null) {
            UnresolvedType thisType = getThisType();
            parameterNames.add(0, "ajc$this");
            shadowParameterTypes = addTypeToFront(BcelWorld.makeBcelType(thisType), shadowParameterTypes);
        }
        if (getKind() == Shadow.FieldSet || getKind() == Shadow.FieldGet) {
            parameterNames.add(getSignature().getName());
        } else {
            String[] pnames = getSignature().getParameterNames(this.world);
            if (pnames != null) {
                for (int i = 0; i < pnames.length; i++) {
                    if (i == 0 && pnames[i].equals(OgnlContext.THIS_CONTEXT_KEY)) {
                        parameterNames.add("ajc$this");
                    } else {
                        parameterNames.add(pnames[i]);
                    }
                }
            }
        }
        if (this.thisJoinPointVar != null) {
            parameterNames.add("thisJoinPoint");
            shadowParameterTypes = addTypeToEnd(LazyClassGen.tjpType, shadowParameterTypes);
        }
        if (getKind() == PreInitialization) {
            returnType = UnresolvedType.OBJECTARRAY;
        } else if (getKind() == ConstructorCall) {
            returnType = getSignature().getDeclaringType();
        } else if (getKind() == FieldSet) {
            returnType = UnresolvedType.VOID;
        } else {
            returnType = getSignature().getReturnType().resolve(this.world);
        }
        return new LazyMethodGen(modifiers, BcelWorld.makeBcelType(returnType), newMethodName, shadowParameterTypes, NoDeclaredExceptions, getEnclosingClass());
    }

    private boolean samePackage(String p1, String p2) {
        if (p1 == null) {
            return p2 == null;
        }
        if (p2 == null) {
            return false;
        }
        return p1.equals(p2);
    }

    private Type[] addTypeToFront(Type type, Type[] types) {
        int len = types.length;
        Type[] ret = new Type[len + 1];
        ret[0] = type;
        System.arraycopy(types, 0, ret, 1, len);
        return ret;
    }

    private Type[] addTypeToEnd(Type type, Type[] types) {
        int len = types.length;
        Type[] ret = new Type[len + 1];
        ret[len] = type;
        System.arraycopy(types, 0, ret, 0, len);
        return ret;
    }

    public BcelVar genTempVar(UnresolvedType utype) {
        ResolvedType rtype = utype.resolve(this.world);
        return new BcelVar(rtype, genTempVarIndex(rtype.getSize()));
    }

    public BcelVar genTempVar(UnresolvedType typeX, String localName) {
        BcelVar tv = genTempVar(typeX);
        return tv;
    }

    private int genTempVarIndex(int size) {
        return this.enclosingMethod.allocateLocal(size);
    }

    public InstructionFactory getFactory() {
        return getEnclosingClass().getFactory();
    }

    @Override // org.aspectj.weaver.Shadow
    public ISourceLocation getSourceLocation() {
        int sourceLine = getSourceLine();
        if (sourceLine == 0 || sourceLine == -1) {
            return getEnclosingClass().getType().getSourceLocation();
        }
        if (getKind() == Shadow.StaticInitialization && getEnclosingClass().getType().getSourceLocation().getOffset() != 0) {
            return getEnclosingClass().getType().getSourceLocation();
        }
        int offset = 0;
        Shadow.Kind kind = getKind();
        if ((kind == MethodExecution || kind == ConstructorExecution || kind == AdviceExecution || kind == StaticInitialization || kind == PreInitialization || kind == Initialization) && getEnclosingMethod().hasDeclaredLineNumberInfo()) {
            offset = getEnclosingMethod().getDeclarationOffset();
        }
        return getEnclosingClass().getType().getSourceContext().makeSourceLocation(sourceLine, offset);
    }

    public Shadow getEnclosingShadow() {
        return this.enclosingShadow;
    }

    public LazyMethodGen getEnclosingMethod() {
        return this.enclosingMethod;
    }

    public boolean isFallsThrough() {
        return !terminatesWithReturn();
    }

    public void setActualTargetType(String className) {
        this.actualInstructionTargetType = className;
    }

    public String getActualTargetType() {
        return this.actualInstructionTargetType;
    }
}
