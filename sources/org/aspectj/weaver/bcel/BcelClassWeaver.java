package org.aspectj.weaver.bcel;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;
import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.aspectj.apache.bcel.classfile.Method;
import org.aspectj.apache.bcel.classfile.annotation.AnnotationGen;
import org.aspectj.apache.bcel.generic.FieldGen;
import org.aspectj.apache.bcel.generic.FieldInstruction;
import org.aspectj.apache.bcel.generic.Instruction;
import org.aspectj.apache.bcel.generic.InstructionBranch;
import org.aspectj.apache.bcel.generic.InstructionCP;
import org.aspectj.apache.bcel.generic.InstructionConstants;
import org.aspectj.apache.bcel.generic.InstructionFactory;
import org.aspectj.apache.bcel.generic.InstructionHandle;
import org.aspectj.apache.bcel.generic.InstructionLV;
import org.aspectj.apache.bcel.generic.InstructionList;
import org.aspectj.apache.bcel.generic.InstructionSelect;
import org.aspectj.apache.bcel.generic.InstructionTargeter;
import org.aspectj.apache.bcel.generic.InvokeInstruction;
import org.aspectj.apache.bcel.generic.LineNumberTag;
import org.aspectj.apache.bcel.generic.LocalVariableTag;
import org.aspectj.apache.bcel.generic.MULTIANEWARRAY;
import org.aspectj.apache.bcel.generic.MethodGen;
import org.aspectj.apache.bcel.generic.ObjectType;
import org.aspectj.apache.bcel.generic.RET;
import org.aspectj.apache.bcel.generic.Tag;
import org.aspectj.apache.bcel.generic.Type;
import org.aspectj.asm.AsmManager;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.IMessageHandler;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.Message;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.bridge.WeaveMessage;
import org.aspectj.bridge.context.CompilationAndWeavingContext;
import org.aspectj.bridge.context.ContextToken;
import org.aspectj.util.PartialOrder;
import org.aspectj.weaver.AjAttribute;
import org.aspectj.weaver.AjcMemberMaker;
import org.aspectj.weaver.AnnotationAJ;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.ConcreteTypeMunger;
import org.aspectj.weaver.IClassWeaver;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.MissingResolvedTypeWithKnownSignature;
import org.aspectj.weaver.NameMangler;
import org.aspectj.weaver.NewConstructorTypeMunger;
import org.aspectj.weaver.NewFieldTypeMunger;
import org.aspectj.weaver.NewMethodTypeMunger;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedMemberImpl;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.ResolvedTypeMunger;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.ShadowMunger;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.UnresolvedTypeVariableReferenceType;
import org.aspectj.weaver.WeaverStateInfo;
import org.aspectj.weaver.World;
import org.aspectj.weaver.model.AsmRelationshipProvider;
import org.aspectj.weaver.patterns.DeclareAnnotation;
import org.aspectj.weaver.patterns.ExactTypePattern;
import org.aspectj.weaver.tools.Trace;
import org.aspectj.weaver.tools.TraceFactory;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/BcelClassWeaver.class */
class BcelClassWeaver implements IClassWeaver {
    private final LazyClassGen clazz;
    private final List<ShadowMunger> shadowMungers;
    private final List<ConcreteTypeMunger> typeMungers;
    private final List<ConcreteTypeMunger> lateTypeMungers;
    private List<ShadowMunger>[] indexedShadowMungers;
    private final BcelObjectType ty;
    private final BcelWorld world;
    private final ConstantPool cpg;
    private final InstructionFactory fact;
    private static Trace trace = TraceFactory.getTraceFactory().getTrace(BcelClassWeaver.class);
    private static boolean checkedXsetForLowLevelContextCapturing = false;
    private static boolean captureLowLevelContext = false;
    private boolean canMatchBodyShadows = false;
    private final List<LazyMethodGen> addedLazyMethodGens = new ArrayList();
    private final Set<ResolvedMember> addedDispatchTargets = new HashSet();
    private boolean inReweavableMode = false;
    private List<IfaceInitList> addedSuperInitializersAsList = null;
    private final Map<ResolvedType, IfaceInitList> addedSuperInitializers = new HashMap();
    private final List<ConcreteTypeMunger> addedThisInitializers = new ArrayList();
    private final List<ConcreteTypeMunger> addedClassInitializers = new ArrayList();
    private final Map<ResolvedMember, ResolvedMember> mapToAnnotationHolder = new HashMap();
    private final List<BcelShadow> initializationShadows = new ArrayList();

    public static boolean weave(BcelWorld world, LazyClassGen clazz, List<ShadowMunger> shadowMungers, List<ConcreteTypeMunger> typeMungers, List<ConcreteTypeMunger> lateTypeMungers, boolean inReweavableMode) throws AbortException, NumberFormatException {
        BcelClassWeaver classWeaver = new BcelClassWeaver(world, clazz, shadowMungers, typeMungers, lateTypeMungers);
        classWeaver.setReweavableMode(inReweavableMode);
        boolean b = classWeaver.weave();
        return b;
    }

    private BcelClassWeaver(BcelWorld world, LazyClassGen clazz, List<ShadowMunger> shadowMungers, List<ConcreteTypeMunger> typeMungers, List<ConcreteTypeMunger> lateTypeMungers) throws AbortException {
        this.world = world;
        this.clazz = clazz;
        this.shadowMungers = shadowMungers;
        this.typeMungers = typeMungers;
        this.lateTypeMungers = lateTypeMungers;
        this.ty = clazz.getBcelObjectType();
        this.cpg = clazz.getConstantPool();
        this.fact = clazz.getFactory();
        indexShadowMungers();
        initializeSuperInitializerMap(this.ty.getResolvedTypeX());
        if (!checkedXsetForLowLevelContextCapturing) {
            Properties p = world.getExtraConfiguration();
            if (p != null) {
                String s = p.getProperty(World.xsetCAPTURE_ALL_CONTEXT, "false");
                captureLowLevelContext = s.equalsIgnoreCase("true");
                if (captureLowLevelContext) {
                    world.getMessageHandler().handleMessage(MessageUtil.info("[captureAllContext=true] Enabling collection of low level context for debug/crash messages"));
                }
            }
            checkedXsetForLowLevelContextCapturing = true;
        }
    }

    private boolean canMatch(Shadow.Kind kind) {
        return this.indexedShadowMungers[kind.getKey()] != null;
    }

    private void initializeSuperInitializerMap(ResolvedType child) {
        ResolvedType[] superInterfaces = child.getDeclaredInterfaces();
        int len = superInterfaces.length;
        for (int i = 0; i < len; i++) {
            if (this.ty.getResolvedTypeX().isTopmostImplementor(superInterfaces[i]) && addSuperInitializer(superInterfaces[i])) {
                initializeSuperInitializerMap(superInterfaces[i]);
            }
        }
    }

    private void indexShadowMungers() {
        this.indexedShadowMungers = new List[14];
        for (ShadowMunger shadowMunger : this.shadowMungers) {
            int couldMatchKinds = shadowMunger.getPointcut().couldMatchKinds();
            for (Shadow.Kind kind : Shadow.SHADOW_KINDS) {
                if (kind.isSet(couldMatchKinds)) {
                    byte k = kind.getKey();
                    if (this.indexedShadowMungers[k] == null) {
                        this.indexedShadowMungers[k] = new ArrayList();
                        if (!kind.isEnclosingKind()) {
                            this.canMatchBodyShadows = true;
                        }
                    }
                    this.indexedShadowMungers[k].add(shadowMunger);
                }
            }
        }
    }

    private boolean addSuperInitializer(ResolvedType onType) {
        if (onType.isRawType() || onType.isParameterizedType()) {
            onType = onType.getGenericType();
        }
        IfaceInitList l = this.addedSuperInitializers.get(onType);
        if (l != null) {
            return false;
        }
        IfaceInitList l2 = new IfaceInitList(onType);
        this.addedSuperInitializers.put(onType, l2);
        return true;
    }

    public void addInitializer(ConcreteTypeMunger cm) {
        NewFieldTypeMunger m = (NewFieldTypeMunger) cm.getMunger();
        ResolvedType onType = m.getSignature().getDeclaringType().resolve(this.world);
        if (onType.isRawType()) {
            onType = onType.getGenericType();
        }
        if (Modifier.isStatic(m.getSignature().getModifiers())) {
            this.addedClassInitializers.add(cm);
        } else if (onType == this.ty.getResolvedTypeX()) {
            this.addedThisInitializers.add(cm);
        } else {
            IfaceInitList l = this.addedSuperInitializers.get(onType);
            l.list.add(cm);
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/BcelClassWeaver$IfaceInitList.class */
    private static class IfaceInitList implements PartialOrder.PartialComparable {
        final ResolvedType onType;
        List<ConcreteTypeMunger> list = new ArrayList();

        IfaceInitList(ResolvedType onType) {
            this.onType = onType;
        }

        @Override // org.aspectj.util.PartialOrder.PartialComparable
        public int compareTo(Object other) {
            IfaceInitList o = (IfaceInitList) other;
            if (this.onType.isAssignableFrom(o.onType)) {
                return 1;
            }
            if (o.onType.isAssignableFrom(this.onType)) {
                return -1;
            }
            return 0;
        }

        @Override // org.aspectj.util.PartialOrder.PartialComparable
        public int fallbackCompareTo(Object other) {
            return 0;
        }
    }

    public boolean addDispatchTarget(ResolvedMember m) {
        return this.addedDispatchTargets.add(m);
    }

    public void addLazyMethodGen(LazyMethodGen gen) {
        this.addedLazyMethodGens.add(gen);
    }

    public void addOrReplaceLazyMethodGen(LazyMethodGen mg) {
        if (alreadyDefined(this.clazz, mg)) {
            return;
        }
        Iterator<LazyMethodGen> i = this.addedLazyMethodGens.iterator();
        while (i.hasNext()) {
            LazyMethodGen existing = i.next();
            if (signaturesMatch(mg, existing)) {
                if (existing.definingType == null || mg.definingType.isAssignableFrom(existing.definingType)) {
                    return;
                }
                if (existing.definingType.isAssignableFrom(mg.definingType)) {
                    i.remove();
                    this.addedLazyMethodGens.add(mg);
                    return;
                }
                throw new BCException("conflict between: " + mg + " and " + existing);
            }
        }
        this.addedLazyMethodGens.add(mg);
    }

    private boolean alreadyDefined(LazyClassGen clazz, LazyMethodGen mg) {
        Iterator<LazyMethodGen> i = clazz.getMethodGens().iterator();
        while (i.hasNext()) {
            LazyMethodGen existing = i.next();
            if (signaturesMatch(mg, existing)) {
                if (!mg.isAbstract() && existing.isAbstract()) {
                    i.remove();
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    private boolean signaturesMatch(LazyMethodGen mg, LazyMethodGen existing) {
        return mg.getName().equals(existing.getName()) && mg.getSignature().equals(existing.getSignature());
    }

    protected static LazyMethodGen makeBridgeMethod(LazyClassGen gen, ResolvedMember member) {
        int mods = member.getModifiers();
        if (Modifier.isAbstract(mods)) {
            mods -= 1024;
        }
        LazyMethodGen ret = new LazyMethodGen(mods, BcelWorld.makeBcelType(member.getReturnType()), member.getName(), BcelWorld.makeBcelTypes(member.getParameterTypes()), UnresolvedType.getNames(member.getExceptions()), gen);
        return ret;
    }

    private static void createBridgeMethod(BcelWorld world, LazyMethodGen whatToBridgeToMethodGen, LazyClassGen clazz, ResolvedMember theBridgeMethod) {
        int pos = 0;
        ResolvedMember whatToBridgeTo = whatToBridgeToMethodGen.getMemberView();
        if (whatToBridgeTo == null) {
            whatToBridgeTo = new ResolvedMemberImpl(Member.METHOD, whatToBridgeToMethodGen.getEnclosingClass().getType(), whatToBridgeToMethodGen.getAccessFlags(), whatToBridgeToMethodGen.getName(), whatToBridgeToMethodGen.getSignature());
        }
        LazyMethodGen bridgeMethod = makeBridgeMethod(clazz, theBridgeMethod);
        int newflags = bridgeMethod.getAccessFlags() | 64 | 4096;
        if ((newflags & 256) != 0) {
            newflags -= 256;
        }
        bridgeMethod.setAccessFlags(newflags);
        Type returnType = BcelWorld.makeBcelType(theBridgeMethod.getReturnType());
        Type[] paramTypes = BcelWorld.makeBcelTypes(theBridgeMethod.getParameterTypes());
        Type[] newParamTypes = whatToBridgeToMethodGen.getArgumentTypes();
        InstructionList body = bridgeMethod.getBody();
        InstructionFactory fact = clazz.getFactory();
        if (!whatToBridgeToMethodGen.isStatic()) {
            body.append(InstructionFactory.createThis());
            pos = 0 + 1;
        }
        int len = paramTypes.length;
        for (int i = 0; i < len; i++) {
            Type paramType = paramTypes[i];
            body.append(InstructionFactory.createLoad(paramType, pos));
            if (!newParamTypes[i].equals(paramTypes[i])) {
                if (world.forDEBUG_bridgingCode) {
                    System.err.println("Bridging: Cast " + newParamTypes[i] + " from " + paramTypes[i]);
                }
                body.append(fact.createCast(paramTypes[i], newParamTypes[i]));
            }
            pos += paramType.getSize();
        }
        body.append(Utility.createInvoke(fact, world, whatToBridgeTo));
        body.append(InstructionFactory.createReturn(returnType));
        clazz.addMethodGen(bridgeMethod);
    }

    @Override // org.aspectj.weaver.IClassWeaver
    public boolean weave() throws AbortException, NumberFormatException {
        if (this.clazz.isWoven() && !this.clazz.isReweavable()) {
            if (this.world.getLint().nonReweavableTypeEncountered.isEnabled()) {
                this.world.getLint().nonReweavableTypeEncountered.signal(this.clazz.getType().getName(), this.ty.getSourceLocation());
                return false;
            }
            return false;
        }
        Set<String> aspectsAffectingType = null;
        if (this.inReweavableMode || this.clazz.getType().isAspect()) {
            aspectsAffectingType = new HashSet<>();
        }
        boolean isChanged = false;
        if (this.clazz.getType().isAspect()) {
            isChanged = true;
        }
        WeaverStateInfo typeWeaverState = this.world.isOverWeaving() ? getLazyClassGen().getType().getWeaverState() : null;
        for (ConcreteTypeMunger o : this.typeMungers) {
            if (o instanceof BcelTypeMunger) {
                BcelTypeMunger munger = (BcelTypeMunger) o;
                if (typeWeaverState == null || !typeWeaverState.isAspectAlreadyApplied(munger.getAspectType())) {
                    boolean typeMungerAffectedType = munger.munge(this);
                    if (typeMungerAffectedType) {
                        isChanged = true;
                        if (this.inReweavableMode || this.clazz.getType().isAspect()) {
                            aspectsAffectingType.add(munger.getAspectType().getSignature());
                        }
                    }
                }
            }
        }
        boolean isChanged2 = weaveDeclareAtMethodCtor(this.clazz) || isChanged;
        boolean isChanged3 = weaveDeclareAtField(this.clazz) || isChanged2;
        this.addedSuperInitializersAsList = new ArrayList(this.addedSuperInitializers.values());
        this.addedSuperInitializersAsList = PartialOrder.sort(this.addedSuperInitializersAsList);
        if (this.addedSuperInitializersAsList == null) {
            throw new BCException("circularity in inter-types");
        }
        LazyMethodGen staticInit = this.clazz.getStaticInitializer();
        staticInit.getBody().insert(genInitInstructions(this.addedClassInitializers, true));
        List<LazyMethodGen> methodGens = new ArrayList<>(this.clazz.getMethodGens());
        for (LazyMethodGen member : methodGens) {
            if (member.hasBody()) {
                if (this.world.isJoinpointSynchronizationEnabled() && this.world.areSynchronizationPointcutsInUse() && member.getMethod().isSynchronized()) {
                    transformSynchronizedMethod(member);
                }
                boolean shadowMungerMatched = match(member);
                if (shadowMungerMatched) {
                    if (this.inReweavableMode || this.clazz.getType().isAspect()) {
                        aspectsAffectingType.addAll(findAspectsForMungers(member));
                    }
                    isChanged3 = true;
                }
            }
        }
        for (LazyMethodGen methodGen : methodGens) {
            if (methodGen.hasBody()) {
                implement(methodGen);
            }
        }
        if (!this.initializationShadows.isEmpty()) {
            List<LazyMethodGen> recursiveCtors = new ArrayList<>();
            while (inlineSelfConstructors(methodGens, recursiveCtors)) {
            }
            positionAndImplement(this.initializationShadows);
        }
        if (this.lateTypeMungers != null) {
            Iterator<ConcreteTypeMunger> i = this.lateTypeMungers.iterator();
            while (i.hasNext()) {
                BcelTypeMunger munger2 = (BcelTypeMunger) i.next();
                if (munger2.matches(this.clazz.getType())) {
                    boolean typeMungerAffectedType2 = munger2.munge(this);
                    if (typeMungerAffectedType2) {
                        isChanged3 = true;
                        if (this.inReweavableMode || this.clazz.getType().isAspect()) {
                            aspectsAffectingType.add(munger2.getAspectType().getSignature());
                        }
                    }
                }
            }
        }
        if (isChanged3) {
            this.clazz.getOrCreateWeaverStateInfo(this.inReweavableMode);
            weaveInAddedMethods();
        }
        if (this.inReweavableMode) {
            WeaverStateInfo wsi = this.clazz.getOrCreateWeaverStateInfo(true);
            wsi.addAspectsAffectingType(aspectsAffectingType);
            wsi.setUnwovenClassFileData(this.ty.getJavaClass().getBytes());
            wsi.setReweavable(true);
        } else {
            this.clazz.getOrCreateWeaverStateInfo(false).setReweavable(false);
        }
        for (LazyMethodGen mg : methodGens) {
            BcelMethod method = mg.getMemberView();
            if (method != null) {
                method.wipeJoinpointSignatures();
            }
        }
        return isChanged3;
    }

    private static ResolvedMember isOverriding(ResolvedType typeToCheck, ResolvedMember methodThatMightBeGettingOverridden, String mname, String mrettype, int mmods, boolean inSamePackage, UnresolvedType[] methodParamsArray) {
        if (Modifier.isStatic(methodThatMightBeGettingOverridden.getModifiers()) || Modifier.isPrivate(methodThatMightBeGettingOverridden.getModifiers()) || !methodThatMightBeGettingOverridden.getName().equals(mname) || methodThatMightBeGettingOverridden.getParameterTypes().length != methodParamsArray.length || !isVisibilityOverride(mmods, methodThatMightBeGettingOverridden, inSamePackage)) {
            return null;
        }
        if (typeToCheck.getWorld().forDEBUG_bridgingCode) {
            System.err.println("  Bridging:seriously considering this might be getting overridden '" + methodThatMightBeGettingOverridden + "'");
        }
        World w = typeToCheck.getWorld();
        boolean sameParams = true;
        int max = methodThatMightBeGettingOverridden.getParameterTypes().length;
        for (int p = 0; p < max; p++) {
            UnresolvedType mtmbgoParameter = methodThatMightBeGettingOverridden.getParameterTypes()[p];
            UnresolvedType ptype = methodParamsArray[p];
            if (mtmbgoParameter.isTypeVariableReference()) {
                if (!mtmbgoParameter.resolve(w).isAssignableFrom(ptype.resolve(w))) {
                    sameParams = false;
                }
            } else {
                boolean b = !methodThatMightBeGettingOverridden.getParameterTypes()[p].getErasureSignature().equals(methodParamsArray[p].getErasureSignature());
                UnresolvedType parameterType = methodThatMightBeGettingOverridden.getParameterTypes()[p];
                if (parameterType instanceof UnresolvedTypeVariableReferenceType) {
                    ((UnresolvedTypeVariableReferenceType) parameterType).getTypeVariable().getFirstBound();
                }
                if (b) {
                    sameParams = false;
                }
            }
        }
        if (sameParams) {
            if (typeToCheck.isParameterizedType()) {
                return methodThatMightBeGettingOverridden.getBackingGenericMember();
            }
            if (!methodThatMightBeGettingOverridden.getReturnType().getErasureSignature().equals(mrettype)) {
                ResolvedType superReturn = typeToCheck.getWorld().resolve(UnresolvedType.forSignature(methodThatMightBeGettingOverridden.getReturnType().getErasureSignature()));
                ResolvedType subReturn = typeToCheck.getWorld().resolve(UnresolvedType.forSignature(mrettype));
                if (superReturn.isAssignableFrom(subReturn)) {
                    return methodThatMightBeGettingOverridden;
                }
                return null;
            }
            return methodThatMightBeGettingOverridden;
        }
        return null;
    }

    static boolean isVisibilityOverride(int methodMods, ResolvedMember inheritedMethod, boolean inSamePackage) {
        int inheritedModifiers = inheritedMethod.getModifiers();
        if (Modifier.isStatic(inheritedModifiers)) {
            return false;
        }
        if (methodMods == inheritedModifiers) {
            return true;
        }
        if (Modifier.isPrivate(inheritedModifiers)) {
            return false;
        }
        boolean isPackageVisible = (Modifier.isPrivate(inheritedModifiers) || Modifier.isProtected(inheritedModifiers) || Modifier.isPublic(inheritedModifiers)) ? false : true;
        if (isPackageVisible && !inSamePackage) {
            return false;
        }
        return true;
    }

    public static void checkForOverride(ResolvedType typeToCheck, String mname, String mparams, String mrettype, int mmods, String mpkg, UnresolvedType[] methodParamsArray, List<ResolvedMember> overriddenMethodsCollector) {
        if (typeToCheck == null || (typeToCheck instanceof MissingResolvedTypeWithKnownSignature)) {
            return;
        }
        if (typeToCheck.getWorld().forDEBUG_bridgingCode) {
            System.err.println("  Bridging:checking for override of " + mname + " in " + typeToCheck);
        }
        String packageName = typeToCheck.getPackageName();
        if (packageName == null) {
            packageName = "";
        }
        boolean inSamePackage = packageName.equals(mpkg);
        ResolvedMember[] methods = typeToCheck.getDeclaredMethods();
        for (ResolvedMember methodThatMightBeGettingOverridden : methods) {
            ResolvedMember isOverriding = isOverriding(typeToCheck, methodThatMightBeGettingOverridden, mname, mrettype, mmods, inSamePackage, methodParamsArray);
            if (isOverriding != null) {
                overriddenMethodsCollector.add(isOverriding);
            }
        }
        List<ConcreteTypeMunger> l = typeToCheck.isRawType() ? typeToCheck.getGenericType().getInterTypeMungers() : typeToCheck.getInterTypeMungers();
        for (ConcreteTypeMunger o : l) {
            if (o instanceof BcelTypeMunger) {
                BcelTypeMunger element = (BcelTypeMunger) o;
                if (element.getMunger() instanceof NewMethodTypeMunger) {
                    if (typeToCheck.getWorld().forDEBUG_bridgingCode) {
                        System.err.println("Possible ITD candidate " + element);
                    }
                    ResolvedMember aMethod = element.getSignature();
                    ResolvedMember isOverriding2 = isOverriding(typeToCheck, aMethod, mname, mrettype, mmods, inSamePackage, methodParamsArray);
                    if (isOverriding2 != null) {
                        overriddenMethodsCollector.add(isOverriding2);
                    }
                }
            }
        }
        if (typeToCheck.equals(UnresolvedType.OBJECT)) {
            return;
        }
        ResolvedType superclass = typeToCheck.getSuperclass();
        checkForOverride(superclass, mname, mparams, mrettype, mmods, mpkg, methodParamsArray, overriddenMethodsCollector);
        ResolvedType[] interfaces = typeToCheck.getDeclaredInterfaces();
        for (ResolvedType anInterface : interfaces) {
            checkForOverride(anInterface, mname, mparams, mrettype, mmods, mpkg, methodParamsArray, overriddenMethodsCollector);
        }
    }

    public static boolean calculateAnyRequiredBridgeMethods(BcelWorld world, LazyClassGen clazz) {
        world.ensureAdvancedConfigurationProcessed();
        if (!world.isInJava5Mode() || clazz.isInterface()) {
            return false;
        }
        boolean didSomething = false;
        List<LazyMethodGen> methods = clazz.getMethodGens();
        Set<String> methodsSet = new HashSet<>();
        for (int i = 0; i < methods.size(); i++) {
            LazyMethodGen aMethod = methods.get(i);
            methodsSet.add(aMethod.getName() + aMethod.getSignature());
        }
        for (int i2 = 0; i2 < methods.size(); i2++) {
            LazyMethodGen bridgeToCandidate = methods.get(i2);
            if (!bridgeToCandidate.isBridgeMethod()) {
                String name = bridgeToCandidate.getName();
                String psig = bridgeToCandidate.getParameterSignature();
                String rsig = bridgeToCandidate.getReturnType().getSignature();
                if (!bridgeToCandidate.isStatic() && !name.endsWith("init>")) {
                    if (world.forDEBUG_bridgingCode) {
                        System.err.println("Bridging: Determining if we have to bridge to " + clazz.getName() + "." + name + "" + bridgeToCandidate.getSignature());
                    }
                    ResolvedType theSuperclass = clazz.getSuperClass();
                    if (world.forDEBUG_bridgingCode) {
                        System.err.println("Bridging: Checking supertype " + theSuperclass);
                    }
                    String pkgName = clazz.getPackageName();
                    UnresolvedType[] bm = BcelWorld.fromBcel(bridgeToCandidate.getArgumentTypes());
                    List<ResolvedMember> overriddenMethodsCollector = new ArrayList<>();
                    checkForOverride(theSuperclass, name, psig, rsig, bridgeToCandidate.getAccessFlags(), pkgName, bm, overriddenMethodsCollector);
                    if (overriddenMethodsCollector.size() != 0) {
                        for (ResolvedMember overriddenMethod : overriddenMethodsCollector) {
                            String key = overriddenMethod.getName() + overriddenMethod.getSignatureErased();
                            boolean alreadyHaveABridgeMethod = methodsSet.contains(key);
                            if (!alreadyHaveABridgeMethod) {
                                if (world.forDEBUG_bridgingCode) {
                                    System.err.println("Bridging:bridging to '" + overriddenMethod + "'");
                                }
                                createBridgeMethod(world, bridgeToCandidate, clazz, overriddenMethod);
                                methodsSet.add(key);
                                didSomething = true;
                            }
                        }
                    }
                    String[] interfaces = clazz.getInterfaceNames();
                    for (int j = 0; j < interfaces.length; j++) {
                        if (world.forDEBUG_bridgingCode) {
                            System.err.println("Bridging:checking superinterface " + interfaces[j]);
                        }
                        ResolvedType interfaceType = world.resolve(interfaces[j]);
                        overriddenMethodsCollector.clear();
                        checkForOverride(interfaceType, name, psig, rsig, bridgeToCandidate.getAccessFlags(), clazz.getPackageName(), bm, overriddenMethodsCollector);
                        for (ResolvedMember overriddenMethod2 : overriddenMethodsCollector) {
                            String key2 = new StringBuffer().append(overriddenMethod2.getName()).append(overriddenMethod2.getSignatureErased()).toString();
                            boolean alreadyHaveABridgeMethod2 = methodsSet.contains(key2);
                            if (!alreadyHaveABridgeMethod2) {
                                createBridgeMethod(world, bridgeToCandidate, clazz, overriddenMethod2);
                                methodsSet.add(key2);
                                didSomething = true;
                                if (world.forDEBUG_bridgingCode) {
                                    System.err.println("Bridging:bridging to " + overriddenMethod2);
                                }
                            }
                        }
                    }
                }
            }
        }
        return didSomething;
    }

    private boolean weaveDeclareAtMethodCtor(LazyClassGen clazz) throws AbortException {
        List<Integer> reportedProblems = new ArrayList<>();
        List<DeclareAnnotation> allDecams = this.world.getDeclareAnnotationOnMethods();
        if (allDecams.isEmpty()) {
            return false;
        }
        boolean isChanged = false;
        List<ConcreteTypeMunger> itdMethodsCtors = getITDSubset(clazz, ResolvedTypeMunger.Method);
        itdMethodsCtors.addAll(getITDSubset(clazz, ResolvedTypeMunger.Constructor));
        if (!itdMethodsCtors.isEmpty()) {
            isChanged = weaveAtMethodOnITDSRepeatedly(allDecams, itdMethodsCtors, reportedProblems);
        }
        List<DeclareAnnotation> decaMs = getMatchingSubset(allDecams, clazz.getType());
        if (decaMs.isEmpty()) {
            return false;
        }
        Set<DeclareAnnotation> unusedDecams = new HashSet<>();
        unusedDecams.addAll(decaMs);
        if (this.addedLazyMethodGens != null) {
            for (LazyMethodGen method : this.addedLazyMethodGens) {
                ResolvedMember resolvedmember = new ResolvedMemberImpl(ResolvedMember.METHOD, method.getEnclosingClass().getType(), method.getAccessFlags(), BcelWorld.fromBcel(method.getReturnType()), method.getName(), BcelWorld.fromBcel(method.getArgumentTypes()), UnresolvedType.forNames(method.getDeclaredExceptions()));
                resolvedmember.setAnnotationTypes(method.getAnnotationTypes());
                resolvedmember.setAnnotations(method.getAnnotations());
                List<DeclareAnnotation> worthRetrying = new ArrayList<>();
                boolean modificationOccured = false;
                for (DeclareAnnotation decam : decaMs) {
                    if (decam.matches(resolvedmember, this.world)) {
                        if (doesAlreadyHaveAnnotation(resolvedmember, decam, reportedProblems, false)) {
                            unusedDecams.remove(decam);
                        } else {
                            AnnotationGen a = ((BcelAnnotation) decam.getAnnotation()).getBcelAnnotation();
                            AnnotationAJ aj2 = new BcelAnnotation(new AnnotationGen(a, clazz.getConstantPool(), true), this.world);
                            method.addAnnotation(aj2);
                            resolvedmember.addAnnotation(decam.getAnnotation());
                            AsmRelationshipProvider.addDeclareAnnotationMethodRelationship(decam.getSourceLocation(), clazz.getName(), resolvedmember, this.world.getModelAsAsmManager());
                            reportMethodCtorWeavingMessage(clazz, resolvedmember, decam, method.getDeclarationLineNumber());
                            isChanged = true;
                            modificationOccured = true;
                            unusedDecams.remove(decam);
                        }
                    } else if (!decam.isStarredAnnotationPattern()) {
                        worthRetrying.add(decam);
                    }
                }
                while (!worthRetrying.isEmpty() && modificationOccured) {
                    modificationOccured = false;
                    List<DeclareAnnotation> forRemoval = new ArrayList<>();
                    for (DeclareAnnotation decam2 : worthRetrying) {
                        if (decam2.matches(resolvedmember, this.world)) {
                            if (doesAlreadyHaveAnnotation(resolvedmember, decam2, reportedProblems, false)) {
                                unusedDecams.remove(decam2);
                            } else {
                                AnnotationGen a2 = ((BcelAnnotation) decam2.getAnnotation()).getBcelAnnotation();
                                AnnotationAJ aj3 = new BcelAnnotation(new AnnotationGen(a2, clazz.getConstantPool(), true), this.world);
                                method.addAnnotation(aj3);
                                resolvedmember.addAnnotation(decam2.getAnnotation());
                                AsmRelationshipProvider.addDeclareAnnotationMethodRelationship(decam2.getSourceLocation(), clazz.getName(), resolvedmember, this.world.getModelAsAsmManager());
                                isChanged = true;
                                modificationOccured = true;
                                forRemoval.add(decam2);
                                unusedDecams.remove(decam2);
                            }
                        }
                    }
                    worthRetrying.removeAll(forRemoval);
                }
            }
        }
        List<LazyMethodGen> members = clazz.getMethodGens();
        if (!members.isEmpty()) {
            for (int memberCounter = 0; memberCounter < members.size(); memberCounter++) {
                LazyMethodGen mg = members.get(memberCounter);
                if (!mg.getName().startsWith(NameMangler.PREFIX)) {
                    List<DeclareAnnotation> worthRetrying2 = new ArrayList<>();
                    boolean modificationOccured2 = false;
                    List<AnnotationGen> annotationsToAdd = null;
                    for (DeclareAnnotation decaM : decaMs) {
                        if (decaM.matches(mg.getMemberView(), this.world)) {
                            if (doesAlreadyHaveAnnotation((ResolvedMember) mg.getMemberView(), decaM, reportedProblems, true)) {
                                unusedDecams.remove(decaM);
                            } else {
                                if (annotationsToAdd == null) {
                                    annotationsToAdd = new ArrayList<>();
                                }
                                AnnotationGen a3 = ((BcelAnnotation) decaM.getAnnotation()).getBcelAnnotation();
                                AnnotationGen ag = new AnnotationGen(a3, clazz.getConstantPool(), true);
                                annotationsToAdd.add(ag);
                                mg.addAnnotation(decaM.getAnnotation());
                                AsmRelationshipProvider.addDeclareAnnotationMethodRelationship(decaM.getSourceLocation(), clazz.getName(), mg.getMemberView(), this.world.getModelAsAsmManager());
                                reportMethodCtorWeavingMessage(clazz, mg.getMemberView(), decaM, mg.getDeclarationLineNumber());
                                isChanged = true;
                                modificationOccured2 = true;
                                unusedDecams.remove(decaM);
                            }
                        } else if (!decaM.isStarredAnnotationPattern()) {
                            worthRetrying2.add(decaM);
                        }
                    }
                    while (!worthRetrying2.isEmpty() && modificationOccured2) {
                        modificationOccured2 = false;
                        List<DeclareAnnotation> forRemoval2 = new ArrayList<>();
                        for (DeclareAnnotation decaM2 : worthRetrying2) {
                            if (decaM2.matches(mg.getMemberView(), this.world)) {
                                if (doesAlreadyHaveAnnotation((ResolvedMember) mg.getMemberView(), decaM2, reportedProblems, true)) {
                                    unusedDecams.remove(decaM2);
                                } else {
                                    if (annotationsToAdd == null) {
                                        annotationsToAdd = new ArrayList<>();
                                    }
                                    AnnotationGen a4 = ((BcelAnnotation) decaM2.getAnnotation()).getBcelAnnotation();
                                    AnnotationGen ag2 = new AnnotationGen(a4, clazz.getConstantPool(), true);
                                    annotationsToAdd.add(ag2);
                                    mg.addAnnotation(decaM2.getAnnotation());
                                    AsmRelationshipProvider.addDeclareAnnotationMethodRelationship(decaM2.getSourceLocation(), clazz.getName(), mg.getMemberView(), this.world.getModelAsAsmManager());
                                    isChanged = true;
                                    modificationOccured2 = true;
                                    forRemoval2.add(decaM2);
                                    unusedDecams.remove(decaM2);
                                }
                            }
                        }
                        worthRetrying2.removeAll(forRemoval2);
                    }
                    if (annotationsToAdd != null) {
                        Method oldMethod = mg.getMethod();
                        MethodGen myGen = new MethodGen(oldMethod, clazz.getClassName(), clazz.getConstantPool(), false);
                        for (AnnotationGen a5 : annotationsToAdd) {
                            myGen.addAnnotation(a5);
                        }
                        Method newMethod = myGen.getMethod();
                        members.set(memberCounter, new LazyMethodGen(newMethod, clazz));
                    }
                }
            }
            checkUnusedDeclareAts(unusedDecams, false);
        }
        return isChanged;
    }

    private void reportMethodCtorWeavingMessage(LazyClassGen clazz, ResolvedMember member, DeclareAnnotation decaM, int memberLineNumber) throws AbortException {
        if (!getWorld().getMessageHandler().isIgnoring(IMessage.WEAVEINFO)) {
            StringBuffer parmString = new StringBuffer("(");
            UnresolvedType[] paramTypes = member.getParameterTypes();
            for (int i = 0; i < paramTypes.length; i++) {
                UnresolvedType type = paramTypes[i];
                String s = org.aspectj.apache.bcel.classfile.Utility.signatureToString(type.getSignature());
                if (s.lastIndexOf(46) != -1) {
                    s = s.substring(s.lastIndexOf(46) + 1);
                }
                parmString.append(s);
                if (i + 1 < paramTypes.length) {
                    parmString.append(",");
                }
            }
            parmString.append(")");
            String methodName = member.getName();
            StringBuffer sig = new StringBuffer();
            sig.append(org.aspectj.apache.bcel.classfile.Utility.accessToString(member.getModifiers()));
            sig.append(SymbolConstants.SPACE_SYMBOL);
            sig.append(member.getReturnType().toString());
            sig.append(SymbolConstants.SPACE_SYMBOL);
            sig.append(member.getDeclaringType().toString());
            sig.append(".");
            sig.append(methodName.equals("<init>") ? "new" : methodName);
            sig.append(parmString);
            StringBuffer loc = new StringBuffer();
            if (clazz.getFileName() == null) {
                loc.append("no debug info available");
            } else {
                loc.append(clazz.getFileName());
                if (memberLineNumber != -1) {
                    loc.append(":" + memberLineNumber);
                }
            }
            IMessageHandler messageHandler = getWorld().getMessageHandler();
            WeaveMessage.WeaveMessageKind weaveMessageKind = WeaveMessage.WEAVEMESSAGE_ANNOTATES;
            String[] strArr = new String[6];
            strArr[0] = sig.toString();
            strArr[1] = loc.toString();
            strArr[2] = decaM.getAnnotationString();
            strArr[3] = methodName.startsWith("<init>") ? "constructor" : JamXmlElements.METHOD;
            strArr[4] = decaM.getAspect().toString();
            strArr[5] = Utility.beautifyLocation(decaM.getSourceLocation());
            messageHandler.handleMessage(WeaveMessage.constructWeavingMessage(weaveMessageKind, strArr));
        }
    }

    private List<DeclareAnnotation> getMatchingSubset(List<DeclareAnnotation> declareAnnotations, ResolvedType type) {
        List<DeclareAnnotation> subset = new ArrayList<>();
        for (DeclareAnnotation da : declareAnnotations) {
            if (da.couldEverMatch(type)) {
                subset.add(da);
            }
        }
        return subset;
    }

    private List<ConcreteTypeMunger> getITDSubset(LazyClassGen clazz, ResolvedTypeMunger.Kind wantedKind) {
        List<ConcreteTypeMunger> subset = new ArrayList<>();
        for (ConcreteTypeMunger typeMunger : clazz.getBcelObjectType().getTypeMungers()) {
            if (typeMunger.getMunger().getKind() == wantedKind) {
                subset.add(typeMunger);
            }
        }
        return subset;
    }

    public LazyMethodGen locateAnnotationHolderForFieldMunger(LazyClassGen clazz, ConcreteTypeMunger fieldMunger) {
        NewFieldTypeMunger newFieldMunger = (NewFieldTypeMunger) fieldMunger.getMunger();
        ResolvedMember lookingFor = AjcMemberMaker.interFieldInitializer(newFieldMunger.getSignature(), clazz.getType());
        for (LazyMethodGen method : clazz.getMethodGens()) {
            if (method.getName().equals(lookingFor.getName())) {
                return method;
            }
        }
        return null;
    }

    public LazyMethodGen locateAnnotationHolderForMethodCtorMunger(LazyClassGen clazz, ConcreteTypeMunger methodCtorMunger) {
        ResolvedMember lookingFor;
        ResolvedTypeMunger rtMunger = methodCtorMunger.getMunger();
        if (rtMunger instanceof NewMethodTypeMunger) {
            lookingFor = AjcMemberMaker.interMethodDispatcher(((NewMethodTypeMunger) rtMunger).getSignature(), methodCtorMunger.getAspectType());
        } else if (rtMunger instanceof NewConstructorTypeMunger) {
            NewConstructorTypeMunger nftm = (NewConstructorTypeMunger) rtMunger;
            lookingFor = AjcMemberMaker.postIntroducedConstructor(methodCtorMunger.getAspectType(), nftm.getSignature().getDeclaringType(), nftm.getSignature().getParameterTypes());
        } else {
            throw new BCException("Not sure what this is: " + methodCtorMunger);
        }
        String name = lookingFor.getName();
        String paramSignature = lookingFor.getParameterSignature();
        for (LazyMethodGen member : clazz.getMethodGens()) {
            if (member.getName().equals(name) && member.getParameterSignature().equals(paramSignature)) {
                return member;
            }
        }
        return null;
    }

    private boolean weaveAtFieldRepeatedly(List<DeclareAnnotation> decaFs, List<ConcreteTypeMunger> itdFields, List<Integer> reportedErrors) {
        boolean isChanged = false;
        Iterator<ConcreteTypeMunger> iter = itdFields.iterator();
        while (iter.hasNext()) {
            BcelTypeMunger fieldMunger = (BcelTypeMunger) iter.next();
            ResolvedMember itdIsActually = fieldMunger.getSignature();
            Set<DeclareAnnotation> worthRetrying = new LinkedHashSet<>();
            boolean modificationOccured = false;
            for (DeclareAnnotation decaF : decaFs) {
                if (decaF.matches(itdIsActually, this.world)) {
                    if (decaF.isRemover()) {
                        LazyMethodGen annotationHolder = locateAnnotationHolderForFieldMunger(this.clazz, fieldMunger);
                        if (annotationHolder.hasAnnotation(decaF.getAnnotationType())) {
                            isChanged = true;
                            annotationHolder.removeAnnotation(decaF.getAnnotationType());
                            AsmRelationshipProvider.addDeclareAnnotationRelationship(this.world.getModelAsAsmManager(), decaF.getSourceLocation(), itdIsActually.getSourceLocation(), true);
                        } else {
                            worthRetrying.add(decaF);
                        }
                    } else {
                        LazyMethodGen annotationHolder2 = locateAnnotationHolderForFieldMunger(this.clazz, fieldMunger);
                        if (!doesAlreadyHaveAnnotation(annotationHolder2, itdIsActually, decaF, reportedErrors)) {
                            annotationHolder2.addAnnotation(decaF.getAnnotation());
                            AsmRelationshipProvider.addDeclareAnnotationRelationship(this.world.getModelAsAsmManager(), decaF.getSourceLocation(), itdIsActually.getSourceLocation(), false);
                            isChanged = true;
                            modificationOccured = true;
                        }
                    }
                } else if (!decaF.isStarredAnnotationPattern()) {
                    worthRetrying.add(decaF);
                }
            }
            while (!worthRetrying.isEmpty() && modificationOccured) {
                modificationOccured = false;
                List<DeclareAnnotation> forRemoval = new ArrayList<>();
                for (DeclareAnnotation decaF2 : worthRetrying) {
                    if (decaF2.matches(itdIsActually, this.world)) {
                        if (decaF2.isRemover()) {
                            LazyMethodGen annotationHolder3 = locateAnnotationHolderForFieldMunger(this.clazz, fieldMunger);
                            if (annotationHolder3.hasAnnotation(decaF2.getAnnotationType())) {
                                isChanged = true;
                                annotationHolder3.removeAnnotation(decaF2.getAnnotationType());
                                AsmRelationshipProvider.addDeclareAnnotationRelationship(this.world.getModelAsAsmManager(), decaF2.getSourceLocation(), itdIsActually.getSourceLocation(), true);
                                forRemoval.add(decaF2);
                            }
                        } else {
                            LazyMethodGen annotationHolder4 = locateAnnotationHolderForFieldMunger(this.clazz, fieldMunger);
                            if (!doesAlreadyHaveAnnotation(annotationHolder4, itdIsActually, decaF2, reportedErrors)) {
                                annotationHolder4.addAnnotation(decaF2.getAnnotation());
                                AsmRelationshipProvider.addDeclareAnnotationRelationship(this.world.getModelAsAsmManager(), decaF2.getSourceLocation(), itdIsActually.getSourceLocation(), false);
                                isChanged = true;
                                modificationOccured = true;
                                forRemoval.add(decaF2);
                            }
                        }
                    }
                }
                worthRetrying.removeAll(forRemoval);
            }
        }
        return isChanged;
    }

    private boolean weaveAtMethodOnITDSRepeatedly(List<DeclareAnnotation> decaMCs, List<ConcreteTypeMunger> itdsForMethodAndConstructor, List<Integer> reportedErrors) throws AbortException {
        boolean isChanged = false;
        AsmManager asmManager = this.world.getModelAsAsmManager();
        for (ConcreteTypeMunger methodctorMunger : itdsForMethodAndConstructor) {
            ResolvedMember unMangledInterMethod = methodctorMunger.getSignature();
            List<DeclareAnnotation> worthRetrying = new ArrayList<>();
            boolean modificationOccured = false;
            for (DeclareAnnotation decaMC : decaMCs) {
                if (decaMC.matches(unMangledInterMethod, this.world)) {
                    LazyMethodGen annotationHolder = locateAnnotationHolderForMethodCtorMunger(this.clazz, methodctorMunger);
                    if (annotationHolder != null && !doesAlreadyHaveAnnotation(annotationHolder, unMangledInterMethod, decaMC, reportedErrors)) {
                        annotationHolder.addAnnotation(decaMC.getAnnotation());
                        isChanged = true;
                        AsmRelationshipProvider.addDeclareAnnotationRelationship(asmManager, decaMC.getSourceLocation(), unMangledInterMethod.getSourceLocation(), false);
                        reportMethodCtorWeavingMessage(this.clazz, unMangledInterMethod, decaMC, -1);
                        modificationOccured = true;
                    }
                } else if (!decaMC.isStarredAnnotationPattern()) {
                    worthRetrying.add(decaMC);
                }
            }
            while (!worthRetrying.isEmpty() && modificationOccured) {
                modificationOccured = false;
                List<DeclareAnnotation> forRemoval = new ArrayList<>();
                for (DeclareAnnotation decaMC2 : worthRetrying) {
                    if (decaMC2.matches(unMangledInterMethod, this.world)) {
                        LazyMethodGen annotationHolder2 = locateAnnotationHolderForFieldMunger(this.clazz, methodctorMunger);
                        if (!doesAlreadyHaveAnnotation(annotationHolder2, unMangledInterMethod, decaMC2, reportedErrors)) {
                            annotationHolder2.addAnnotation(decaMC2.getAnnotation());
                            unMangledInterMethod.addAnnotation(decaMC2.getAnnotation());
                            AsmRelationshipProvider.addDeclareAnnotationRelationship(asmManager, decaMC2.getSourceLocation(), unMangledInterMethod.getSourceLocation(), false);
                            isChanged = true;
                            modificationOccured = true;
                            forRemoval.add(decaMC2);
                        }
                    }
                    worthRetrying.removeAll(forRemoval);
                }
            }
        }
        return isChanged;
    }

    private boolean dontAddTwice(DeclareAnnotation decaF, AnnotationAJ[] dontAddMeTwice) {
        for (AnnotationAJ ann : dontAddMeTwice) {
            if (ann != null && decaF.getAnnotation().getTypeName().equals(ann.getTypeName())) {
                return true;
            }
        }
        return false;
    }

    private AnnotationAJ[] removeFromAnnotationsArray(AnnotationAJ[] annotations, AnnotationAJ annotation) {
        for (int i = 0; i < annotations.length; i++) {
            if (annotations[i] != null && annotation.getTypeName().equals(annotations[i].getTypeName())) {
                AnnotationAJ[] newArray = new AnnotationAJ[annotations.length - 1];
                int index = 0;
                for (int j = 0; j < annotations.length; j++) {
                    if (j != i) {
                        int i2 = index;
                        index++;
                        newArray[i2] = annotations[j];
                    }
                }
                return newArray;
            }
        }
        return annotations;
    }

    private boolean weaveDeclareAtField(LazyClassGen clazz) throws AbortException {
        List<Integer> reportedProblems = new ArrayList<>();
        List<DeclareAnnotation> allDecafs = this.world.getDeclareAnnotationOnFields();
        if (allDecafs.isEmpty()) {
            return false;
        }
        boolean typeIsChanged = false;
        List<ConcreteTypeMunger> relevantItdFields = getITDSubset(clazz, ResolvedTypeMunger.Field);
        if (relevantItdFields != null) {
            typeIsChanged = weaveAtFieldRepeatedly(allDecafs, relevantItdFields, reportedProblems);
        }
        List<DeclareAnnotation> decafs = getMatchingSubset(allDecafs, clazz.getType());
        if (decafs.isEmpty()) {
            return typeIsChanged;
        }
        List<BcelField> fields = clazz.getFieldGens();
        if (fields != null) {
            Set<DeclareAnnotation> unusedDecafs = new HashSet<>();
            unusedDecafs.addAll(decafs);
            for (BcelField field : fields) {
                if (!field.getName().startsWith(NameMangler.PREFIX)) {
                    Set<DeclareAnnotation> worthRetrying = new LinkedHashSet<>();
                    boolean modificationOccured = false;
                    AnnotationAJ[] dontAddMeTwice = field.getAnnotations();
                    for (DeclareAnnotation decaf : decafs) {
                        if (decaf.getAnnotation() == null) {
                            return false;
                        }
                        if (decaf.matches(field, this.world)) {
                            if (decaf.isRemover()) {
                                AnnotationAJ annotation = decaf.getAnnotation();
                                if (field.hasAnnotation(annotation.getType())) {
                                    typeIsChanged = true;
                                    field.removeAnnotation(annotation);
                                    AsmRelationshipProvider.addDeclareAnnotationFieldRelationship(this.world.getModelAsAsmManager(), decaf.getSourceLocation(), clazz.getName(), field, true);
                                    reportFieldAnnotationWeavingMessage(clazz, field, decaf, true);
                                    dontAddMeTwice = removeFromAnnotationsArray(dontAddMeTwice, annotation);
                                } else {
                                    worthRetrying.add(decaf);
                                }
                                unusedDecafs.remove(decaf);
                            } else {
                                if (!dontAddTwice(decaf, dontAddMeTwice)) {
                                    if (doesAlreadyHaveAnnotation((ResolvedMember) field, decaf, reportedProblems, true)) {
                                        unusedDecafs.remove(decaf);
                                    } else {
                                        field.addAnnotation(decaf.getAnnotation());
                                    }
                                }
                                AsmRelationshipProvider.addDeclareAnnotationFieldRelationship(this.world.getModelAsAsmManager(), decaf.getSourceLocation(), clazz.getName(), field, false);
                                reportFieldAnnotationWeavingMessage(clazz, field, decaf, false);
                                typeIsChanged = true;
                                modificationOccured = true;
                                unusedDecafs.remove(decaf);
                            }
                        } else if (!decaf.isStarredAnnotationPattern() || decaf.isRemover()) {
                            worthRetrying.add(decaf);
                        }
                    }
                    while (!worthRetrying.isEmpty() && modificationOccured) {
                        modificationOccured = false;
                        List<DeclareAnnotation> forRemoval = new ArrayList<>();
                        for (DeclareAnnotation decaF : worthRetrying) {
                            if (decaF.matches(field, this.world)) {
                                if (decaF.isRemover()) {
                                    AnnotationAJ annotation2 = decaF.getAnnotation();
                                    if (field.hasAnnotation(annotation2.getType())) {
                                        modificationOccured = true;
                                        typeIsChanged = true;
                                        forRemoval.add(decaF);
                                        field.removeAnnotation(annotation2);
                                        AsmRelationshipProvider.addDeclareAnnotationFieldRelationship(this.world.getModelAsAsmManager(), decaF.getSourceLocation(), clazz.getName(), field, true);
                                        reportFieldAnnotationWeavingMessage(clazz, field, decaF, true);
                                    }
                                } else {
                                    unusedDecafs.remove(decaF);
                                    if (!doesAlreadyHaveAnnotation((ResolvedMember) field, decaF, reportedProblems, true)) {
                                        field.addAnnotation(decaF.getAnnotation());
                                        AsmRelationshipProvider.addDeclareAnnotationFieldRelationship(this.world.getModelAsAsmManager(), decaF.getSourceLocation(), clazz.getName(), field, false);
                                        modificationOccured = true;
                                        typeIsChanged = true;
                                        forRemoval.add(decaF);
                                    }
                                }
                            }
                        }
                        worthRetrying.removeAll(forRemoval);
                    }
                }
            }
            checkUnusedDeclareAts(unusedDecafs, true);
        }
        return typeIsChanged;
    }

    private void checkUnusedDeclareAts(Set<DeclareAnnotation> unusedDecaTs, boolean isDeclareAtField) throws AbortException {
        IMessage message;
        for (DeclareAnnotation declA : unusedDecaTs) {
            boolean shouldCheck = declA.isExactPattern() || declA.getSignaturePattern().getExactDeclaringTypes().size() != 0;
            if (shouldCheck && declA.getKind() != DeclareAnnotation.AT_CONSTRUCTOR) {
                if (declA.getSignaturePattern().isMatchOnAnyName()) {
                    shouldCheck = false;
                } else {
                    List<ExactTypePattern> declaringTypePatterns = declA.getSignaturePattern().getExactDeclaringTypes();
                    if (declaringTypePatterns.size() == 0) {
                        shouldCheck = false;
                    } else {
                        Iterator<ExactTypePattern> it = declaringTypePatterns.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            ExactTypePattern exactTypePattern = it.next();
                            if (exactTypePattern.isIncludeSubtypes()) {
                                shouldCheck = false;
                                break;
                            }
                        }
                    }
                }
            }
            if (shouldCheck) {
                boolean itdMatch = false;
                List<ConcreteTypeMunger> lst = this.clazz.getType().getInterTypeMungers();
                Iterator<ConcreteTypeMunger> iterator = lst.iterator();
                while (iterator.hasNext() && !itdMatch) {
                    ConcreteTypeMunger element = iterator.next();
                    if (element.getMunger() instanceof NewFieldTypeMunger) {
                        NewFieldTypeMunger nftm = (NewFieldTypeMunger) element.getMunger();
                        itdMatch = declA.matches(nftm.getSignature(), this.world);
                    } else if (element.getMunger() instanceof NewMethodTypeMunger) {
                        NewMethodTypeMunger nmtm = (NewMethodTypeMunger) element.getMunger();
                        itdMatch = declA.matches(nmtm.getSignature(), this.world);
                    } else if (element.getMunger() instanceof NewConstructorTypeMunger) {
                        NewConstructorTypeMunger nctm = (NewConstructorTypeMunger) element.getMunger();
                        itdMatch = declA.matches(nctm.getSignature(), this.world);
                    }
                }
                if (!itdMatch) {
                    if (isDeclareAtField) {
                        message = new Message("The field '" + declA.getSignaturePattern().toString() + "' does not exist", declA.getSourceLocation(), true);
                    } else {
                        message = new Message("The method '" + declA.getSignaturePattern().toString() + "' does not exist", declA.getSourceLocation(), true);
                    }
                    IMessage message2 = message;
                    this.world.getMessageHandler().handleMessage(message2);
                }
            }
        }
    }

    private void reportFieldAnnotationWeavingMessage(LazyClassGen clazz, BcelField theField, DeclareAnnotation decaf, boolean isRemove) throws AbortException {
        if (!getWorld().getMessageHandler().isIgnoring(IMessage.WEAVEINFO)) {
            this.world.getMessageHandler().handleMessage(WeaveMessage.constructWeavingMessage(isRemove ? WeaveMessage.WEAVEMESSAGE_REMOVES_ANNOTATION : WeaveMessage.WEAVEMESSAGE_ANNOTATES, new String[]{theField.getFieldAsIs().toString() + "' of type '" + clazz.getName(), clazz.getFileName(), decaf.getAnnotationString(), JamXmlElements.FIELD, decaf.getAspect().toString(), Utility.beautifyLocation(decaf.getSourceLocation())}));
        }
    }

    private boolean doesAlreadyHaveAnnotation(ResolvedMember rm, DeclareAnnotation deca, List<Integer> reportedProblems, boolean reportError) {
        if (rm.hasAnnotation(deca.getAnnotationType())) {
            if (reportError && this.world.getLint().elementAlreadyAnnotated.isEnabled()) {
                Integer uniqueID = new Integer(rm.hashCode() * deca.hashCode());
                if (!reportedProblems.contains(uniqueID)) {
                    reportedProblems.add(uniqueID);
                    this.world.getLint().elementAlreadyAnnotated.signal(new String[]{rm.toString(), deca.getAnnotationType().toString()}, rm.getSourceLocation(), new ISourceLocation[]{deca.getSourceLocation()});
                    return true;
                }
                return true;
            }
            return true;
        }
        return false;
    }

    private boolean doesAlreadyHaveAnnotation(LazyMethodGen rm, ResolvedMember itdfieldsig, DeclareAnnotation deca, List<Integer> reportedProblems) {
        if (rm != null && rm.hasAnnotation(deca.getAnnotationType())) {
            if (this.world.getLint().elementAlreadyAnnotated.isEnabled()) {
                Integer uniqueID = new Integer(rm.hashCode() * deca.hashCode());
                if (!reportedProblems.contains(uniqueID)) {
                    reportedProblems.add(uniqueID);
                    reportedProblems.add(new Integer(itdfieldsig.hashCode() * deca.hashCode()));
                    this.world.getLint().elementAlreadyAnnotated.signal(new String[]{itdfieldsig.toString(), deca.getAnnotationType().toString()}, rm.getSourceLocation(), new ISourceLocation[]{deca.getSourceLocation()});
                    return true;
                }
                return true;
            }
            return true;
        }
        return false;
    }

    private Set<String> findAspectsForMungers(LazyMethodGen mg) {
        Set<String> aspectsAffectingType = new HashSet<>();
        for (BcelShadow shadow : mg.matchedShadows) {
            for (ShadowMunger munger : shadow.getMungers()) {
                if (munger instanceof BcelAdvice) {
                    BcelAdvice bcelAdvice = (BcelAdvice) munger;
                    if (bcelAdvice.getConcreteAspect() != null) {
                        aspectsAffectingType.add(bcelAdvice.getConcreteAspect().getSignature());
                    }
                }
            }
        }
        return aspectsAffectingType;
    }

    private boolean inlineSelfConstructors(List<LazyMethodGen> methodGens, List<LazyMethodGen> recursiveCtors) {
        InstructionHandle ih;
        boolean inlinedSomething = false;
        List<LazyMethodGen> newRecursiveCtors = new ArrayList<>();
        for (LazyMethodGen methodGen : methodGens) {
            if (methodGen.getName().equals("<init>") && (ih = findSuperOrThisCall(methodGen)) != null && isThisCall(ih)) {
                LazyMethodGen donor = getCalledMethod(ih);
                if (donor.equals(methodGen)) {
                    newRecursiveCtors.add(donor);
                } else if (!recursiveCtors.contains(donor)) {
                    inlineMethod(donor, methodGen, ih);
                    inlinedSomething = true;
                }
            }
        }
        recursiveCtors.addAll(newRecursiveCtors);
        return inlinedSomething;
    }

    private void positionAndImplement(List<BcelShadow> initializationShadows) {
        for (BcelShadow s : initializationShadows) {
            positionInitializationShadow(s);
            s.implement();
        }
    }

    private void positionInitializationShadow(BcelShadow s) {
        LazyMethodGen mg = s.getEnclosingMethod();
        InstructionHandle call = findSuperOrThisCall(mg);
        InstructionList body = mg.getBody();
        ShadowRange r = new ShadowRange(body);
        r.associateWithShadow(s);
        if (s.getKind() == Shadow.PreInitialization) {
            r.associateWithTargets(Range.genStart(body, body.getStart().getNext()), Range.genEnd(body, call.getPrev()));
        } else {
            r.associateWithTargets(Range.genStart(body, call.getNext()), Range.genEnd(body));
        }
    }

    private boolean isThisCall(InstructionHandle ih) {
        InvokeInstruction inst = (InvokeInstruction) ih.getInstruction();
        return inst.getClassName(this.cpg).equals(this.clazz.getName());
    }

    public static void inlineMethod(LazyMethodGen donor, LazyMethodGen recipient, InstructionHandle call) {
        InstructionFactory fact = recipient.getEnclosingClass().getFactory();
        IntMap frameEnv = new IntMap();
        InstructionList argumentStores = genArgumentStores(donor, recipient, frameEnv, fact);
        InstructionList inlineInstructions = genInlineInstructions(donor, recipient, frameEnv, fact, false);
        inlineInstructions.insert(argumentStores);
        recipient.getBody().append(call, inlineInstructions);
        Utility.deleteInstruction(call, recipient);
    }

    public static void transformSynchronizedMethod(LazyMethodGen synchronizedMethod) throws NumberFormatException {
        if (trace.isTraceEnabled()) {
            trace.enter("transformSynchronizedMethod", synchronizedMethod);
        }
        InstructionFactory fact = synchronizedMethod.getEnclosingClass().getFactory();
        InstructionList body = synchronizedMethod.getBody();
        InstructionList prepend = new InstructionList();
        Type enclosingClassType = BcelWorld.makeBcelType(synchronizedMethod.getEnclosingClass().getType());
        if (synchronizedMethod.isStatic()) {
            if (synchronizedMethod.getEnclosingClass().isAtLeastJava5()) {
                int slotForLockObject = synchronizedMethod.allocateLocal(enclosingClassType);
                prepend.append(fact.createConstant(enclosingClassType));
                prepend.append(InstructionFactory.createDup(1));
                prepend.append(InstructionFactory.createStore(enclosingClassType, slotForLockObject));
                prepend.append(InstructionFactory.MONITORENTER);
                InstructionList finallyBlock = new InstructionList();
                finallyBlock.append(InstructionFactory.createLoad(Type.getType(Class.class), slotForLockObject));
                finallyBlock.append(InstructionConstants.MONITOREXIT);
                finallyBlock.append(InstructionConstants.ATHROW);
                List<InstructionHandle> rets = new ArrayList<>();
                for (InstructionHandle walker = body.getStart(); walker != null; walker = walker.getNext()) {
                    if (walker.getInstruction().isReturnInstruction()) {
                        rets.add(walker);
                    }
                }
                if (!rets.isEmpty()) {
                    for (InstructionHandle element : rets) {
                        InstructionList monitorExitBlock = new InstructionList();
                        monitorExitBlock.append(InstructionFactory.createLoad(enclosingClassType, slotForLockObject));
                        monitorExitBlock.append(InstructionConstants.MONITOREXIT);
                        InstructionHandle monitorExitBlockStart = body.insert(element, monitorExitBlock);
                        for (InstructionTargeter targeter : element.getTargetersCopy()) {
                            if (!(targeter instanceof LocalVariableTag) && !(targeter instanceof LineNumberTag)) {
                                if (targeter instanceof InstructionBranch) {
                                    targeter.updateTarget(element, monitorExitBlockStart);
                                } else {
                                    throw new BCException("Unexpected targeter encountered during transform: " + targeter);
                                }
                            }
                        }
                    }
                }
                InstructionHandle finallyStart = finallyBlock.getStart();
                InstructionHandle tryPosition = body.getStart();
                InstructionHandle catchPosition = body.getEnd();
                body.insert(body.getStart(), prepend);
                synchronizedMethod.getBody().append(finallyBlock);
                synchronizedMethod.addExceptionHandler(tryPosition, catchPosition, finallyStart, null, false);
                synchronizedMethod.addExceptionHandler(finallyStart, finallyStart.getNext(), finallyStart, null, false);
            } else {
                Type classType = BcelWorld.makeBcelType(synchronizedMethod.getEnclosingClass().getType());
                Type clazzType = Type.getType(Class.class);
                InstructionList parttwo = new InstructionList();
                parttwo.append(InstructionFactory.createDup(1));
                int slotForThis = synchronizedMethod.allocateLocal(classType);
                parttwo.append(InstructionFactory.createStore(clazzType, slotForThis));
                parttwo.append(InstructionFactory.MONITORENTER);
                String fieldname = synchronizedMethod.getEnclosingClass().allocateField("class$");
                FieldGen f = new FieldGen(10, Type.getType(Class.class), fieldname, synchronizedMethod.getEnclosingClass().getConstantPool());
                synchronizedMethod.getEnclosingClass().addField(f, null);
                String name = synchronizedMethod.getEnclosingClass().getName();
                prepend.append(fact.createGetStatic(name, fieldname, Type.getType(Class.class)));
                prepend.append(InstructionFactory.createDup(1));
                prepend.append(InstructionFactory.createBranchInstruction((short) 199, parttwo.getStart()));
                prepend.append(InstructionFactory.POP);
                prepend.append(fact.createConstant(name));
                InstructionHandle tryInstruction = prepend.getEnd();
                prepend.append(fact.createInvoke("java.lang.Class", "forName", clazzType, new Type[]{Type.getType(String.class)}, (short) 184));
                InstructionHandle catchInstruction = prepend.getEnd();
                prepend.append(InstructionFactory.createDup(1));
                prepend.append(fact.createPutStatic(synchronizedMethod.getEnclosingClass().getType().getName(), fieldname, Type.getType(Class.class)));
                prepend.append(InstructionFactory.createBranchInstruction((short) 167, parttwo.getStart()));
                InstructionList catchBlockForLiteralLoadingFail = new InstructionList();
                catchBlockForLiteralLoadingFail.append(fact.createNew((ObjectType) Type.getType(NoClassDefFoundError.class)));
                catchBlockForLiteralLoadingFail.append(InstructionFactory.createDup_1(1));
                catchBlockForLiteralLoadingFail.append(InstructionFactory.SWAP);
                catchBlockForLiteralLoadingFail.append(fact.createInvoke("java.lang.Throwable", "getMessage", Type.getType(String.class), new Type[0], (short) 182));
                catchBlockForLiteralLoadingFail.append(fact.createInvoke("java.lang.NoClassDefFoundError", "<init>", Type.VOID, new Type[]{Type.getType(String.class)}, (short) 183));
                catchBlockForLiteralLoadingFail.append(InstructionFactory.ATHROW);
                InstructionHandle catchBlockStart = catchBlockForLiteralLoadingFail.getStart();
                prepend.append(catchBlockForLiteralLoadingFail);
                prepend.append(parttwo);
                InstructionList finallyBlock2 = new InstructionList();
                finallyBlock2.append(InstructionFactory.createLoad(Type.getType(Class.class), slotForThis));
                finallyBlock2.append(InstructionConstants.MONITOREXIT);
                finallyBlock2.append(InstructionConstants.ATHROW);
                List<InstructionHandle> rets2 = new ArrayList<>();
                for (InstructionHandle walker2 = body.getStart(); walker2 != null; walker2 = walker2.getNext()) {
                    if (walker2.getInstruction().isReturnInstruction()) {
                        rets2.add(walker2);
                    }
                }
                if (rets2.size() > 0) {
                    for (InstructionHandle ret : rets2) {
                        InstructionList monitorExitBlock2 = new InstructionList();
                        monitorExitBlock2.append(InstructionFactory.createLoad(classType, slotForThis));
                        monitorExitBlock2.append(InstructionConstants.MONITOREXIT);
                        InstructionHandle monitorExitBlockStart2 = body.insert(ret, monitorExitBlock2);
                        for (InstructionTargeter targeter2 : ret.getTargetersCopy()) {
                            if (!(targeter2 instanceof LocalVariableTag) && !(targeter2 instanceof LineNumberTag)) {
                                if (targeter2 instanceof InstructionBranch) {
                                    targeter2.updateTarget(ret, monitorExitBlockStart2);
                                } else {
                                    throw new BCException("Unexpected targeter encountered during transform: " + targeter2);
                                }
                            }
                        }
                    }
                }
                InstructionHandle finallyStart2 = finallyBlock2.getStart();
                InstructionHandle tryPosition2 = body.getStart();
                InstructionHandle catchPosition2 = body.getEnd();
                body.insert(body.getStart(), prepend);
                synchronizedMethod.getBody().append(finallyBlock2);
                synchronizedMethod.addExceptionHandler(tryPosition2, catchPosition2, finallyStart2, null, false);
                synchronizedMethod.addExceptionHandler(tryInstruction, catchInstruction, catchBlockStart, (ObjectType) Type.getType(ClassNotFoundException.class), true);
                synchronizedMethod.addExceptionHandler(finallyStart2, finallyStart2.getNext(), finallyStart2, null, false);
            }
        } else {
            Type classType2 = BcelWorld.makeBcelType(synchronizedMethod.getEnclosingClass().getType());
            prepend.append(InstructionFactory.createLoad(classType2, 0));
            prepend.append(InstructionFactory.createDup(1));
            int slotForThis2 = synchronizedMethod.allocateLocal(classType2);
            prepend.append(InstructionFactory.createStore(classType2, slotForThis2));
            prepend.append(InstructionFactory.MONITORENTER);
            InstructionList finallyBlock3 = new InstructionList();
            finallyBlock3.append(InstructionFactory.createLoad(classType2, slotForThis2));
            finallyBlock3.append(InstructionConstants.MONITOREXIT);
            finallyBlock3.append(InstructionConstants.ATHROW);
            List<InstructionHandle> rets3 = new ArrayList<>();
            for (InstructionHandle walker3 = body.getStart(); walker3 != null; walker3 = walker3.getNext()) {
                if (walker3.getInstruction().isReturnInstruction()) {
                    rets3.add(walker3);
                }
            }
            if (!rets3.isEmpty()) {
                for (InstructionHandle element2 : rets3) {
                    InstructionList monitorExitBlock3 = new InstructionList();
                    monitorExitBlock3.append(InstructionFactory.createLoad(classType2, slotForThis2));
                    monitorExitBlock3.append(InstructionConstants.MONITOREXIT);
                    InstructionHandle monitorExitBlockStart3 = body.insert(element2, monitorExitBlock3);
                    for (InstructionTargeter targeter3 : element2.getTargetersCopy()) {
                        if (!(targeter3 instanceof LocalVariableTag) && !(targeter3 instanceof LineNumberTag)) {
                            if (targeter3 instanceof InstructionBranch) {
                                targeter3.updateTarget(element2, monitorExitBlockStart3);
                            } else {
                                throw new BCException("Unexpected targeter encountered during transform: " + targeter3);
                            }
                        }
                    }
                }
            }
            InstructionHandle finallyStart3 = finallyBlock3.getStart();
            InstructionHandle tryPosition3 = body.getStart();
            InstructionHandle catchPosition3 = body.getEnd();
            body.insert(body.getStart(), prepend);
            synchronizedMethod.getBody().append(finallyBlock3);
            synchronizedMethod.addExceptionHandler(tryPosition3, catchPosition3, finallyStart3, null, false);
            synchronizedMethod.addExceptionHandler(finallyStart3, finallyStart3.getNext(), finallyStart3, null, false);
        }
        if (trace.isTraceEnabled()) {
            trace.exit("transformSynchronizedMethod");
        }
    }

    static InstructionList genInlineInstructions(LazyMethodGen donor, LazyMethodGen recipient, IntMap frameEnv, InstructionFactory fact, boolean keepReturns) {
        int freshIndex;
        InstructionHandle instructionHandleAppend;
        InstructionList footer = new InstructionList();
        InstructionHandle end = footer.append(InstructionConstants.NOP);
        InstructionList ret = new InstructionList();
        InstructionList sourceList = donor.getBody();
        Map<InstructionHandle, InstructionHandle> srcToDest = new HashMap<>();
        ConstantPool donorCpg = donor.getEnclosingClass().getConstantPool();
        ConstantPool recipientCpg = recipient.getEnclosingClass().getConstantPool();
        boolean isAcrossClass = donorCpg != recipientCpg;
        InstructionHandle start = sourceList.getStart();
        while (true) {
            InstructionHandle src = start;
            if (src == null) {
                break;
            }
            Instruction fresh = Utility.copyInstruction(src.getInstruction());
            if (fresh.isConstantPoolInstruction() && isAcrossClass) {
                InstructionCP cpi = (InstructionCP) fresh;
                cpi.setIndex(recipientCpg.addConstant(donorCpg.getConstant(cpi.getIndex()), donorCpg));
            }
            if (src.getInstruction() == Range.RANGEINSTRUCTION) {
                instructionHandleAppend = ret.append(Range.RANGEINSTRUCTION);
            } else if (fresh.isReturnInstruction()) {
                if (keepReturns) {
                    instructionHandleAppend = ret.append(fresh);
                } else {
                    instructionHandleAppend = ret.append(InstructionFactory.createBranchInstruction((short) 167, end));
                }
            } else if (fresh instanceof InstructionBranch) {
                instructionHandleAppend = ret.append((InstructionBranch) fresh);
            } else if (fresh.isLocalVariableInstruction() || (fresh instanceof RET)) {
                int oldIndex = fresh.getIndex();
                if (!frameEnv.hasKey(oldIndex)) {
                    freshIndex = recipient.allocateLocal(2);
                    frameEnv.put(oldIndex, freshIndex);
                } else {
                    freshIndex = frameEnv.get(oldIndex);
                }
                if (fresh instanceof RET) {
                    fresh.setIndex(freshIndex);
                } else {
                    fresh = ((InstructionLV) fresh).setIndexAndCopyIfNecessary(freshIndex);
                }
                instructionHandleAppend = ret.append(fresh);
            } else {
                instructionHandleAppend = ret.append(fresh);
            }
            InstructionHandle dest = instructionHandleAppend;
            srcToDest.put(src, dest);
            start = src.getNext();
        }
        Map<Tag, Tag> tagMap = new HashMap<>();
        Map<BcelShadow, BcelShadow> shadowMap = new HashMap<>();
        InstructionHandle dest2 = ret.getStart();
        InstructionHandle start2 = sourceList.getStart();
        while (true) {
            InstructionHandle src2 = start2;
            if (dest2 == null) {
                break;
            }
            Instruction inst = dest2.getInstruction();
            if (inst instanceof InstructionBranch) {
                InstructionBranch branch = (InstructionBranch) inst;
                InstructionHandle oldTarget = branch.getTarget();
                InstructionHandle newTarget = srcToDest.get(oldTarget);
                if (newTarget != null) {
                    branch.setTarget(newTarget);
                    if (branch instanceof InstructionSelect) {
                        InstructionSelect select = (InstructionSelect) branch;
                        InstructionHandle[] oldTargets = select.getTargets();
                        for (int k = oldTargets.length - 1; k >= 0; k--) {
                            select.setTarget(k, srcToDest.get(oldTargets[k]));
                        }
                    }
                }
            }
            for (InstructionTargeter old : src2.getTargeters()) {
                if (old instanceof Tag) {
                    Tag oldTag = (Tag) old;
                    Tag fresh2 = tagMap.get(oldTag);
                    if (fresh2 == null) {
                        fresh2 = oldTag.copy();
                        if (old instanceof LocalVariableTag) {
                            LocalVariableTag lvTag = (LocalVariableTag) old;
                            LocalVariableTag lvTagFresh = (LocalVariableTag) fresh2;
                            if (lvTag.getSlot() == 0) {
                                fresh2 = new LocalVariableTag(lvTag.getRealType().getSignature(), "ajc$aspectInstance", frameEnv.get(lvTag.getSlot()), 0);
                            } else {
                                lvTagFresh.updateSlot(frameEnv.get(lvTag.getSlot()));
                            }
                        }
                        tagMap.put(oldTag, fresh2);
                    }
                    dest2.addTargeter(fresh2);
                } else if (old instanceof ExceptionRange) {
                    ExceptionRange er = (ExceptionRange) old;
                    if (er.getStart() == src2) {
                        ExceptionRange freshEr = new ExceptionRange(recipient.getBody(), er.getCatchType(), er.getPriority());
                        freshEr.associateWithTargets(dest2, srcToDest.get(er.getEnd()), srcToDest.get(er.getHandler()));
                    }
                } else if (old instanceof ShadowRange) {
                    ShadowRange oldRange = (ShadowRange) old;
                    if (oldRange.getStart() == src2) {
                        BcelShadow oldShadow = oldRange.getShadow();
                        BcelShadow freshEnclosing = oldShadow.getEnclosingShadow() == null ? null : shadowMap.get(oldShadow.getEnclosingShadow());
                        BcelShadow freshShadow = oldShadow.copyInto(recipient, freshEnclosing);
                        ShadowRange freshRange = new ShadowRange(recipient.getBody());
                        freshRange.associateWithShadow(freshShadow);
                        freshRange.associateWithTargets(dest2, srcToDest.get(oldRange.getEnd()));
                        shadowMap.put(oldShadow, freshShadow);
                    }
                }
            }
            dest2 = dest2.getNext();
            start2 = src2.getNext();
        }
        if (!keepReturns) {
            ret.append(footer);
        }
        return ret;
    }

    private static InstructionList genArgumentStores(LazyMethodGen donor, LazyMethodGen recipient, IntMap frameEnv, InstructionFactory fact) {
        InstructionList ret = new InstructionList();
        int donorFramePos = 0;
        if (!donor.isStatic()) {
            int targetSlot = recipient.allocateLocal(Type.OBJECT);
            ret.insert(InstructionFactory.createStore(Type.OBJECT, targetSlot));
            frameEnv.put(0, targetSlot);
            donorFramePos = 0 + 1;
        }
        Type[] argTypes = donor.getArgumentTypes();
        for (Type argType : argTypes) {
            int argSlot = recipient.allocateLocal(argType);
            ret.insert(InstructionFactory.createStore(argType, argSlot));
            frameEnv.put(donorFramePos, argSlot);
            donorFramePos += argType.getSize();
        }
        return ret;
    }

    private LazyMethodGen getCalledMethod(InstructionHandle ih) {
        InvokeInstruction inst = (InvokeInstruction) ih.getInstruction();
        String methodName = inst.getName(this.cpg);
        String signature = inst.getSignature(this.cpg);
        return this.clazz.getLazyMethodGen(methodName, signature);
    }

    private void weaveInAddedMethods() {
        Collections.sort(this.addedLazyMethodGens, new Comparator<LazyMethodGen>() { // from class: org.aspectj.weaver.bcel.BcelClassWeaver.1
            @Override // java.util.Comparator
            public int compare(LazyMethodGen aa, LazyMethodGen bb) {
                int i = aa.getName().compareTo(bb.getName());
                if (i != 0) {
                    return i;
                }
                return aa.getSignature().compareTo(bb.getSignature());
            }
        });
        for (LazyMethodGen addedMember : this.addedLazyMethodGens) {
            this.clazz.addMethodGen(addedMember);
        }
    }

    private InstructionHandle findSuperOrThisCall(LazyMethodGen mg) {
        int depth = 1;
        InstructionHandle start = mg.getBody().getStart();
        while (true) {
            InstructionHandle start2 = start;
            if (start2 == null) {
                return null;
            }
            Instruction inst = start2.getInstruction();
            if (inst.opcode == 183 && ((InvokeInstruction) inst).getName(this.cpg).equals("<init>")) {
                depth--;
                if (depth == 0) {
                    return start2;
                }
            } else if (inst.opcode == 187) {
                depth++;
            }
            start = start2.getNext();
        }
    }

    private boolean match(LazyMethodGen mg) {
        BcelShadow enclosingShadow;
        List<BcelShadow> shadowAccumulator = new ArrayList<>();
        boolean isOverweaving = this.world.isOverWeaving();
        boolean startsAngly = mg.getName().charAt(0) == '<';
        if (startsAngly && mg.getName().equals("<init>")) {
            return matchInit(mg, shadowAccumulator);
        }
        if (!shouldWeaveBody(mg)) {
            return false;
        }
        if (startsAngly && mg.getName().equals("<clinit>")) {
            enclosingShadow = BcelShadow.makeStaticInitialization(this.world, mg);
        } else if (mg.isAdviceMethod()) {
            enclosingShadow = BcelShadow.makeAdviceExecution(this.world, mg);
        } else {
            AjAttribute.EffectiveSignatureAttribute effective = mg.getEffectiveSignature();
            if (effective == null) {
                if (isOverweaving && mg.getName().startsWith(NameMangler.PREFIX)) {
                    return false;
                }
                enclosingShadow = BcelShadow.makeMethodExecution(this.world, mg, !this.canMatchBodyShadows);
            } else if (effective.isWeaveBody()) {
                ResolvedMember rm = effective.getEffectiveSignature();
                fixParameterNamesForResolvedMember(rm, mg.getMemberView());
                fixAnnotationsForResolvedMember(rm, mg.getMemberView());
                enclosingShadow = BcelShadow.makeShadowForMethod(this.world, mg, effective.getShadowKind(), rm);
            } else {
                return false;
            }
        }
        if (this.canMatchBodyShadows) {
            InstructionHandle start = mg.getBody().getStart();
            while (true) {
                InstructionHandle h = start;
                if (h == null) {
                    break;
                }
                match(mg, h, enclosingShadow, shadowAccumulator);
                start = h.getNext();
            }
        }
        if (canMatch(enclosingShadow.getKind()) && ((mg.getName().charAt(0) != 'a' || !mg.getName().startsWith("ajc$interFieldInit")) && match(enclosingShadow, shadowAccumulator))) {
            enclosingShadow.init();
        }
        mg.matchedShadows = shadowAccumulator;
        return !shadowAccumulator.isEmpty();
    }

    private boolean matchInit(LazyMethodGen mg, List<BcelShadow> shadowAccumulator) {
        InstructionHandle superOrThisCall = findSuperOrThisCall(mg);
        if (superOrThisCall == null) {
            return false;
        }
        BcelShadow enclosingShadow = BcelShadow.makeConstructorExecution(this.world, mg, superOrThisCall);
        if (mg.getEffectiveSignature() != null) {
            enclosingShadow.setMatchingSignature(mg.getEffectiveSignature().getEffectiveSignature());
        }
        boolean beforeSuperOrThisCall = true;
        if (shouldWeaveBody(mg)) {
            if (this.canMatchBodyShadows) {
                InstructionHandle start = mg.getBody().getStart();
                while (true) {
                    InstructionHandle h = start;
                    if (h == null) {
                        break;
                    }
                    if (h == superOrThisCall) {
                        beforeSuperOrThisCall = false;
                    } else {
                        match(mg, h, beforeSuperOrThisCall ? null : enclosingShadow, shadowAccumulator);
                    }
                    start = h.getNext();
                }
            }
            if (canMatch(Shadow.ConstructorExecution)) {
                match(enclosingShadow, shadowAccumulator);
            }
        }
        if (!isThisCall(superOrThisCall)) {
            InstructionHandle curr = enclosingShadow.getRange().getStart();
            for (IfaceInitList l : this.addedSuperInitializersAsList) {
                Member ifaceInitSig = AjcMemberMaker.interfaceConstructor(l.onType);
                BcelShadow initShadow = BcelShadow.makeIfaceInitialization(this.world, mg, ifaceInitSig);
                InstructionList inits = genInitInstructions(l.list, false);
                if (match(initShadow, shadowAccumulator) || !inits.isEmpty()) {
                    initShadow.initIfaceInitializer(curr);
                    initShadow.getRange().insert(inits, Range.OutsideBefore);
                }
            }
            enclosingShadow.getRange().insert(genInitInstructions(this.addedThisInitializers, false), Range.OutsideBefore);
        }
        boolean addedInitialization = match(BcelShadow.makeUnfinishedInitialization(this.world, mg), this.initializationShadows);
        boolean addedInitialization2 = addedInitialization | match(BcelShadow.makeUnfinishedPreinitialization(this.world, mg), this.initializationShadows);
        mg.matchedShadows = shadowAccumulator;
        return addedInitialization2 || !shadowAccumulator.isEmpty();
    }

    private boolean shouldWeaveBody(LazyMethodGen mg) {
        if (mg.isBridgeMethod()) {
            return false;
        }
        if (mg.isAjSynthetic()) {
            return mg.getName().equals("<clinit>");
        }
        AjAttribute.EffectiveSignatureAttribute a = mg.getEffectiveSignature();
        if (a != null) {
            return a.isWeaveBody();
        }
        return true;
    }

    private InstructionList genInitInstructions(List<ConcreteTypeMunger> list, boolean isStatic) {
        List<ConcreteTypeMunger> list2 = PartialOrder.sort(list);
        if (list2 == null) {
            throw new BCException("circularity in inter-types");
        }
        InstructionList ret = new InstructionList();
        for (ConcreteTypeMunger cmunger : list2) {
            NewFieldTypeMunger munger = (NewFieldTypeMunger) cmunger.getMunger();
            ResolvedMember initMethod = munger.getInitMethod(cmunger.getAspectType());
            if (!isStatic) {
                ret.append(InstructionConstants.ALOAD_0);
            }
            ret.append(Utility.createInvoke(this.fact, this.world, initMethod));
        }
        return ret;
    }

    private void match(LazyMethodGen mg, InstructionHandle ih, BcelShadow enclosingShadow, List<BcelShadow> shadowAccumulator) {
        Instruction i = ih.getInstruction();
        if (canMatch(Shadow.ExceptionHandler) && !Range.isRangeHandle(ih)) {
            Set<InstructionTargeter> targeters = ih.getTargetersCopy();
            for (InstructionTargeter t : targeters) {
                if (t instanceof ExceptionRange) {
                    ExceptionRange er = (ExceptionRange) t;
                    if (er.getCatchType() == null) {
                        continue;
                    } else {
                        if (isInitFailureHandler(ih)) {
                            return;
                        }
                        if (!ih.getInstruction().isStoreInstruction() && ih.getInstruction().getOpcode() != 0) {
                            mg.getBody().insert(ih, InstructionConstants.NOP);
                            InstructionHandle newNOP = ih.getPrev();
                            er.updateTarget(ih, newNOP, mg.getBody());
                            for (InstructionTargeter t2 : targeters) {
                                newNOP.addTargeter(t2);
                            }
                            ih.removeAllTargeters();
                            match(BcelShadow.makeExceptionHandler(this.world, er, mg, newNOP, enclosingShadow), shadowAccumulator);
                        } else {
                            match(BcelShadow.makeExceptionHandler(this.world, er, mg, ih, enclosingShadow), shadowAccumulator);
                        }
                    }
                }
            }
        }
        if ((i instanceof FieldInstruction) && (canMatch(Shadow.FieldGet) || canMatch(Shadow.FieldSet))) {
            FieldInstruction fi = (FieldInstruction) i;
            if (fi.opcode == 181 || fi.opcode == 179) {
                InstructionHandle prevHandle = ih.getPrev();
                Instruction prevI = prevHandle.getInstruction();
                if (Utility.isConstantPushInstruction(prevI)) {
                    Member field = BcelWorld.makeFieldJoinPointSignature(this.clazz, (FieldInstruction) i);
                    ResolvedMember resolvedField = field.resolve(this.world);
                    if (resolvedField != null && !Modifier.isFinal(resolvedField.getModifiers()) && canMatch(Shadow.FieldSet)) {
                        matchSetInstruction(mg, ih, enclosingShadow, shadowAccumulator);
                        return;
                    }
                    return;
                }
                if (canMatch(Shadow.FieldSet)) {
                    matchSetInstruction(mg, ih, enclosingShadow, shadowAccumulator);
                    return;
                }
                return;
            }
            if (canMatch(Shadow.FieldGet)) {
                matchGetInstruction(mg, ih, enclosingShadow, shadowAccumulator);
                return;
            }
            return;
        }
        if (i instanceof InvokeInstruction) {
            InvokeInstruction ii = (InvokeInstruction) i;
            if (ii.getMethodName(this.clazz.getConstantPool()).equals("<init>")) {
                if (canMatch(Shadow.ConstructorCall)) {
                    match(BcelShadow.makeConstructorCall(this.world, mg, ih, enclosingShadow), shadowAccumulator);
                    return;
                }
                return;
            } else {
                if (ii.opcode == 183) {
                    String onTypeName = ii.getClassName(this.cpg);
                    if (onTypeName.equals(mg.getEnclosingClass().getName())) {
                        matchInvokeInstruction(mg, ih, ii, enclosingShadow, shadowAccumulator);
                        return;
                    }
                    return;
                }
                if (ii.getOpcode() != 186) {
                    matchInvokeInstruction(mg, ih, ii, enclosingShadow, shadowAccumulator);
                    return;
                }
                return;
            }
        }
        if (this.world.isJoinpointArrayConstructionEnabled() && i.isArrayCreationInstruction()) {
            if (canMatch(Shadow.ConstructorCall)) {
                if (i.opcode == 189) {
                    BcelShadow ctorCallShadow = BcelShadow.makeArrayConstructorCall(this.world, mg, ih, enclosingShadow);
                    match(ctorCallShadow, shadowAccumulator);
                    return;
                } else if (i.opcode == 188) {
                    BcelShadow ctorCallShadow2 = BcelShadow.makeArrayConstructorCall(this.world, mg, ih, enclosingShadow);
                    match(ctorCallShadow2, shadowAccumulator);
                    return;
                } else {
                    if (i instanceof MULTIANEWARRAY) {
                        BcelShadow ctorCallShadow3 = BcelShadow.makeArrayConstructorCall(this.world, mg, ih, enclosingShadow);
                        match(ctorCallShadow3, shadowAccumulator);
                        return;
                    }
                    return;
                }
            }
            return;
        }
        if (this.world.isJoinpointSynchronizationEnabled()) {
            if (i.getOpcode() == 194 || i.getOpcode() == 195) {
                if (i.getOpcode() == 194) {
                    BcelShadow monitorEntryShadow = BcelShadow.makeMonitorEnter(this.world, mg, ih, enclosingShadow);
                    match(monitorEntryShadow, shadowAccumulator);
                } else {
                    BcelShadow monitorExitShadow = BcelShadow.makeMonitorExit(this.world, mg, ih, enclosingShadow);
                    match(monitorExitShadow, shadowAccumulator);
                }
            }
        }
    }

    private boolean isInitFailureHandler(InstructionHandle ih) {
        InstructionHandle twoInstructionsAway = ih.getNext().getNext();
        if (twoInstructionsAway.getInstruction().opcode == 179) {
            String name = ((FieldInstruction) twoInstructionsAway.getInstruction()).getFieldName(this.cpg);
            if (name.equals(NameMangler.INITFAILURECAUSE_FIELD_NAME)) {
                return true;
            }
            return false;
        }
        return false;
    }

    private void matchSetInstruction(LazyMethodGen mg, InstructionHandle ih, BcelShadow enclosingShadow, List<BcelShadow> shadowAccumulator) {
        ResolvedMember resolvedField;
        FieldInstruction fi = (FieldInstruction) ih.getInstruction();
        Member field = BcelWorld.makeFieldJoinPointSignature(this.clazz, fi);
        if (field.getName().startsWith(NameMangler.PREFIX) || (resolvedField = field.resolve(this.world)) == null) {
            return;
        }
        if ((Modifier.isFinal(resolvedField.getModifiers()) && Utility.isConstantPushInstruction(ih.getPrev().getInstruction())) || resolvedField.isSynthetic()) {
            return;
        }
        BcelShadow bs = BcelShadow.makeFieldSet(this.world, resolvedField, mg, ih, enclosingShadow);
        String cname = fi.getClassName(this.cpg);
        if (!resolvedField.getDeclaringType().getName().equals(cname)) {
            bs.setActualTargetType(cname);
        }
        match(bs, shadowAccumulator);
    }

    private void matchGetInstruction(LazyMethodGen mg, InstructionHandle ih, BcelShadow enclosingShadow, List<BcelShadow> shadowAccumulator) {
        ResolvedMember resolvedField;
        FieldInstruction fi = (FieldInstruction) ih.getInstruction();
        Member field = BcelWorld.makeFieldJoinPointSignature(this.clazz, fi);
        if (field.getName().startsWith(NameMangler.PREFIX) || (resolvedField = field.resolve(this.world)) == null || resolvedField.isSynthetic()) {
            return;
        }
        BcelShadow bs = BcelShadow.makeFieldGet(this.world, resolvedField, mg, ih, enclosingShadow);
        String cname = fi.getClassName(this.cpg);
        if (!resolvedField.getDeclaringType().getName().equals(cname)) {
            bs.setActualTargetType(cname);
        }
        match(bs, shadowAccumulator);
    }

    private ResolvedMember findResolvedMemberNamed(ResolvedType type, String methodName) {
        ResolvedMember[] allMethods = type.getDeclaredMethods();
        for (ResolvedMember member : allMethods) {
            if (member.getName().equals(methodName)) {
                return member;
            }
        }
        return null;
    }

    private ResolvedMember findResolvedMemberNamed(ResolvedType type, String methodName, UnresolvedType[] params) {
        ResolvedMember[] allMethods = type.getDeclaredMethods();
        List<ResolvedMember> candidates = new ArrayList<>();
        for (ResolvedMember candidate : allMethods) {
            if (candidate.getName().equals(methodName) && candidate.getArity() == params.length) {
                candidates.add(candidate);
            }
        }
        if (candidates.size() == 0) {
            return null;
        }
        if (candidates.size() == 1) {
            return candidates.get(0);
        }
        for (ResolvedMember candidate2 : candidates) {
            boolean allOK = true;
            UnresolvedType[] candidateParams = candidate2.getParameterTypes();
            int p = 0;
            while (true) {
                if (p >= candidateParams.length) {
                    break;
                }
                if (candidateParams[p].getErasureSignature().equals(params[p].getErasureSignature())) {
                    p++;
                } else {
                    allOK = false;
                    break;
                }
            }
            if (allOK) {
                return candidate2;
            }
        }
        return null;
    }

    private void fixParameterNamesForResolvedMember(ResolvedMember rm, ResolvedMember declaredSig) {
        UnresolvedType memberHostType = declaredSig.getDeclaringType();
        String methodName = declaredSig.getName();
        String[] pnames = null;
        if (rm.getKind() == Member.METHOD && !rm.isAbstract()) {
            if (methodName.startsWith("ajc$inlineAccessMethod") || methodName.startsWith("ajc$superDispatch")) {
                ResolvedMember resolvedDooberry = this.world.resolve(declaredSig);
                pnames = resolvedDooberry.getParameterNames();
            } else {
                ResolvedMember realthing = AjcMemberMaker.interMethodDispatcher(rm.resolve(this.world), memberHostType).resolve(this.world);
                ResolvedMember theRealMember = findResolvedMemberNamed(memberHostType.resolve(this.world), realthing.getName());
                if (theRealMember != null) {
                    pnames = theRealMember.getParameterNames();
                    if (pnames.length > 0 && pnames[0].equals("ajc$this_")) {
                        String[] pnames2 = new String[pnames.length - 1];
                        System.arraycopy(pnames, 1, pnames2, 0, pnames2.length);
                        pnames = pnames2;
                    }
                }
            }
        }
        rm.setParameterNames(pnames);
    }

    private void fixAnnotationsForResolvedMember(ResolvedMember rm, ResolvedMember declaredSig) {
        AnnotationAJ[] annotations;
        ResolvedType[] annotationTypes;
        try {
            UnresolvedType memberHostType = declaredSig.getDeclaringType();
            boolean containsKey = this.mapToAnnotationHolder.containsKey(rm);
            ResolvedMember realAnnotationHolder = this.mapToAnnotationHolder.get(rm);
            String methodName = declaredSig.getName();
            if (!containsKey) {
                if (rm.getKind() == Member.FIELD) {
                    if (methodName.startsWith("ajc$inlineAccessField")) {
                        realAnnotationHolder = this.world.resolve(rm);
                    } else {
                        realAnnotationHolder = this.world.resolve(AjcMemberMaker.interFieldInitializer(rm, memberHostType));
                    }
                } else if (rm.getKind() == Member.METHOD && !rm.isAbstract()) {
                    if (methodName.startsWith("ajc$inlineAccessMethod") || methodName.startsWith("ajc$superDispatch")) {
                        realAnnotationHolder = this.world.resolve(declaredSig);
                    } else {
                        ResolvedMember realthing = AjcMemberMaker.interMethodDispatcher(rm.resolve(this.world), memberHostType).resolve(this.world);
                        realAnnotationHolder = findResolvedMemberNamed(memberHostType.resolve(this.world), realthing.getName(), realthing.getParameterTypes());
                        if (realAnnotationHolder == null) {
                            throw new UnsupportedOperationException("Known limitation in M4 - can't find ITD members when type variable is used as an argument and has upper bound specified");
                        }
                    }
                } else if (rm.getKind() == Member.CONSTRUCTOR) {
                    ResolvedMember realThing = AjcMemberMaker.postIntroducedConstructor(memberHostType.resolve(this.world), rm.getDeclaringType(), rm.getParameterTypes());
                    realAnnotationHolder = this.world.resolve(realThing);
                    if (realAnnotationHolder == null) {
                        throw new UnsupportedOperationException("Known limitation in M4 - can't find ITD members when type variable is used as an argument and has upper bound specified");
                    }
                }
                this.mapToAnnotationHolder.put(rm, realAnnotationHolder);
            }
            if (realAnnotationHolder != null) {
                annotationTypes = realAnnotationHolder.getAnnotationTypes();
                annotations = realAnnotationHolder.getAnnotations();
                if (annotationTypes == null) {
                    annotationTypes = ResolvedType.EMPTY_ARRAY;
                }
                if (annotations == null) {
                    annotations = AnnotationAJ.EMPTY_ARRAY;
                }
            } else {
                annotations = AnnotationAJ.EMPTY_ARRAY;
                annotationTypes = ResolvedType.EMPTY_ARRAY;
            }
            rm.setAnnotations(annotations);
            rm.setAnnotationTypes(annotationTypes);
        } catch (UnsupportedOperationException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new BCException("Unexpectedly went bang when searching for annotations on " + rm, t);
        }
    }

    private void matchInvokeInstruction(LazyMethodGen mg, InstructionHandle ih, InvokeInstruction invoke, BcelShadow enclosingShadow, List<BcelShadow> shadowAccumulator) {
        Shadow.Kind kind;
        String methodName = invoke.getName(this.cpg);
        if (methodName.startsWith(NameMangler.PREFIX)) {
            Member jpSig = this.world.makeJoinPointSignatureForMethodInvocation(this.clazz, invoke);
            ResolvedMember declaredSig = jpSig.resolve(this.world);
            if (declaredSig == null) {
                return;
            }
            if (declaredSig.getKind() == Member.FIELD) {
                if (jpSig.getReturnType().equals(UnresolvedType.VOID)) {
                    kind = Shadow.FieldSet;
                } else {
                    kind = Shadow.FieldGet;
                }
                if (canMatch(Shadow.FieldGet) || canMatch(Shadow.FieldSet)) {
                    match(BcelShadow.makeShadowForMethodCall(this.world, mg, ih, enclosingShadow, kind, declaredSig), shadowAccumulator);
                    return;
                }
                return;
            }
            if (!declaredSig.getName().startsWith(NameMangler.PREFIX)) {
                if (canMatch(Shadow.MethodCall)) {
                    match(BcelShadow.makeShadowForMethodCall(this.world, mg, ih, enclosingShadow, Shadow.MethodCall, declaredSig), shadowAccumulator);
                    return;
                }
                return;
            }
            AjAttribute.EffectiveSignatureAttribute effectiveSig = declaredSig.getEffectiveSignature();
            if (effectiveSig == null || effectiveSig.isWeaveBody()) {
                return;
            }
            ResolvedMember rm = effectiveSig.getEffectiveSignature();
            fixParameterNamesForResolvedMember(rm, declaredSig);
            fixAnnotationsForResolvedMember(rm, declaredSig);
            if (canMatch(effectiveSig.getShadowKind())) {
                match(BcelShadow.makeShadowForMethodCall(this.world, mg, ih, enclosingShadow, effectiveSig.getShadowKind(), rm), shadowAccumulator);
                return;
            }
            return;
        }
        if (canMatch(Shadow.MethodCall)) {
            boolean proceed = true;
            if (this.world.isOverWeaving()) {
                String s = invoke.getClassName(mg.getConstantPool());
                if ((s.length() > 4 && s.charAt(4) == 'a' && (s.equals(NameMangler.CFLOW_COUNTER_TYPE) || s.equals(NameMangler.CFLOW_STACK_TYPE) || s.equals("org.aspectj.runtime.reflect.Factory"))) || methodName.equals("aspectOf")) {
                    proceed = false;
                }
            }
            if (proceed) {
                match(BcelShadow.makeMethodCall(this.world, mg, ih, enclosingShadow), shadowAccumulator);
            }
        }
    }

    private boolean match(BcelShadow shadow, List<BcelShadow> shadowAccumulator) {
        if (captureLowLevelContext) {
            ContextToken shadowMatchToken = CompilationAndWeavingContext.enteringPhase(28, shadow);
            boolean isMatched = false;
            Shadow.Kind shadowKind = shadow.getKind();
            List<ShadowMunger> candidateMungers = this.indexedShadowMungers[shadowKind.getKey()];
            if (candidateMungers != null) {
                for (ShadowMunger munger : candidateMungers) {
                    ContextToken mungerMatchToken = CompilationAndWeavingContext.enteringPhase(30, munger.getPointcut());
                    if (munger.match(shadow, this.world)) {
                        shadow.addMunger(munger);
                        isMatched = true;
                        if (shadow.getKind() == Shadow.StaticInitialization) {
                            this.clazz.warnOnAddedStaticInitializer(shadow, munger.getSourceLocation());
                        }
                    }
                    CompilationAndWeavingContext.leavingPhase(mungerMatchToken);
                }
                if (isMatched) {
                    shadowAccumulator.add(shadow);
                }
            }
            CompilationAndWeavingContext.leavingPhase(shadowMatchToken);
            return isMatched;
        }
        boolean isMatched2 = false;
        Shadow.Kind shadowKind2 = shadow.getKind();
        List<ShadowMunger> candidateMungers2 = this.indexedShadowMungers[shadowKind2.getKey()];
        if (candidateMungers2 != null) {
            for (ShadowMunger munger2 : candidateMungers2) {
                if (munger2.match(shadow, this.world)) {
                    shadow.addMunger(munger2);
                    isMatched2 = true;
                    if (shadow.getKind() == Shadow.StaticInitialization) {
                        this.clazz.warnOnAddedStaticInitializer(shadow, munger2.getSourceLocation());
                    }
                }
            }
            if (isMatched2) {
                shadowAccumulator.add(shadow);
            }
        }
        return isMatched2;
    }

    private void implement(LazyMethodGen mg) {
        List<BcelShadow> shadows = mg.matchedShadows;
        if (shadows == null) {
            return;
        }
        for (BcelShadow shadow : shadows) {
            ContextToken tok = CompilationAndWeavingContext.enteringPhase(29, shadow);
            shadow.implement();
            CompilationAndWeavingContext.leavingPhase(tok);
        }
        mg.getMaxLocals();
        mg.matchedShadows = null;
    }

    public LazyClassGen getLazyClassGen() {
        return this.clazz;
    }

    public BcelWorld getWorld() {
        return this.world;
    }

    public void setReweavableMode(boolean mode) {
        this.inReweavableMode = mode;
    }

    public boolean getReweavableMode() {
        return this.inReweavableMode;
    }

    public String toString() {
        return "BcelClassWeaver instance for : " + this.clazz;
    }
}
