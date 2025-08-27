package org.aspectj.weaver;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import com.mysql.jdbc.SQLError;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.WeakHashMap;
import org.apache.tomcat.jni.Time;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.IMessageHandler;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.Message;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.bridge.context.PinpointingMessageHandler;
import org.aspectj.util.IStructureModel;
import org.aspectj.weaver.AjAttribute;
import org.aspectj.weaver.Dump;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.patterns.Declare;
import org.aspectj.weaver.patterns.DeclareAnnotation;
import org.aspectj.weaver.patterns.DeclareParents;
import org.aspectj.weaver.patterns.DeclarePrecedence;
import org.aspectj.weaver.patterns.DeclareSoft;
import org.aspectj.weaver.patterns.DeclareTypeErrorOrWarning;
import org.aspectj.weaver.patterns.Pointcut;
import org.aspectj.weaver.patterns.TypePattern;
import org.aspectj.weaver.tools.PointcutDesignatorHandler;
import org.aspectj.weaver.tools.Trace;
import org.aspectj.weaver.tools.TraceFactory;
import org.springframework.beans.PropertyAccessor;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/World.class */
public abstract class World implements Dump.INode {
    private TypeVariableDeclaringElement typeVariableLookupScope;
    private Set<PointcutDesignatorHandler> pointcutDesignators;
    private final AspectPrecedenceCalculator precedenceCalculator;
    private boolean XnoInline;
    private boolean XlazyTjp;
    private static boolean systemPropertyOverWeaving;
    private boolean errorThreshold;
    private boolean warningThreshold;
    private BoundedReferenceType wildcard;
    public static final String xsetAVOID_FINAL = "avoidFinal";
    public static final String xsetWEAVE_JAVA_PACKAGES = "weaveJavaPackages";
    public static final String xsetWEAVE_JAVAX_PACKAGES = "weaveJavaxPackages";
    public static final String xsetCAPTURE_ALL_CONTEXT = "captureAllContext";
    public static final String xsetRUN_MINIMAL_MEMORY = "runMinimalMemory";
    public static final String xsetDEBUG_STRUCTURAL_CHANGES_CODE = "debugStructuralChangesCode";
    public static final String xsetDEBUG_BRIDGING = "debugBridging";
    public static final String xsetTRANSIENT_TJP_FIELDS = "makeTjpFieldsTransient";
    public static final String xsetBCEL_REPOSITORY_CACHING = "bcelRepositoryCaching";
    public static final String xsetPIPELINE_COMPILATION = "pipelineCompilation";
    public static final String xsetGENERATE_STACKMAPS = "generateStackMaps";
    public static final String xsetPIPELINE_COMPILATION_DEFAULT = "true";
    public static final String xsetCOMPLETE_BINARY_TYPES = "completeBinaryTypes";
    public static final String xsetCOMPLETE_BINARY_TYPES_DEFAULT = "false";
    public static final String xsetTYPE_DEMOTION = "typeDemotion";
    public static final String xsetTYPE_DEMOTION_DEBUG = "typeDemotionDebug";
    public static final String xsetTYPE_REFS = "useWeakTypeRefs";
    public static final String xsetBCEL_REPOSITORY_CACHING_DEFAULT = "true";
    public static final String xsetFAST_PACK_METHODS = "fastPackMethods";
    public static final String xsetOVERWEAVING = "overWeaving";
    public static final String xsetOPTIMIZED_MATCHING = "optimizedMatching";
    public static final String xsetTIMERS_PER_JOINPOINT = "timersPerJoinpoint";
    public static final String xsetTIMERS_PER_FASTMATCH_CALL = "timersPerFastMatchCall";
    public static final String xsetITD_VERSION = "itdVersion";
    public static final String xsetITD_VERSION_ORIGINAL = "1";
    public static final String xsetITD_VERSION_2NDGEN = "2";
    public static final String xsetITD_VERSION_DEFAULT = "2";
    public static final String xsetMINIMAL_MODEL = "minimalModel";
    public static final String xsetTARGETING_RUNTIME_1610 = "targetRuntime1_6_10";
    public static final String xsetGENERATE_NEW_LVTS = "generateNewLocalVariableTables";
    public static boolean createInjarHierarchy = true;
    private static Trace trace = TraceFactory.getTraceFactory().getTrace(World.class);
    private IMessageHandler messageHandler = IMessageHandler.SYSTEM_ERR;
    private ICrossReferenceHandler xrefHandler = null;
    protected TypeMap typeMap = new TypeMap(this);
    private final CrosscuttingMembersSet crosscuttingMembersSet = new CrosscuttingMembersSet(this);
    private IStructureModel model = null;
    private Lint lint = new Lint(this);
    private boolean XhasMember = false;
    private boolean Xpinpoint = false;
    private boolean behaveInJava5Way = false;
    private boolean timing = false;
    private boolean timingPeriodically = true;
    private boolean incrementalCompileCouldFollow = false;
    private String targetAspectjRuntimeLevel = "1.5";
    private boolean optionalJoinpoint_ArrayConstruction = false;
    private boolean optionalJoinpoint_Synchronization = false;
    private boolean addSerialVerUID = false;
    private Properties extraConfiguration = null;
    private boolean checkedAdvancedConfiguration = false;
    private boolean synchronizationPointcutsInUse = false;
    private boolean runMinimalMemory = false;
    private boolean transientTjpFields = false;
    private boolean runMinimalMemorySet = false;
    private boolean shouldPipelineCompilation = true;
    private boolean shouldGenerateStackMaps = false;
    protected boolean bcelRepositoryCaching = "true".equalsIgnoreCase("true");
    private boolean fastMethodPacking = false;
    private int itdVersion = 2;
    private boolean minimalModel = true;
    private boolean useFinal = true;
    private boolean targettingRuntime1_6_10 = false;
    private boolean completeBinaryTypes = false;
    private boolean overWeaving = false;
    public boolean forDEBUG_structuralChangesCode = false;
    public boolean forDEBUG_bridgingCode = false;
    public boolean optimizedMatching = true;
    public boolean generateNewLvts = true;
    protected long timersPerJoinpoint = 25000;
    protected long timersPerType = 250;
    public int infoMessagesEnabled = 0;
    private List<RuntimeException> dumpState_cantFindTypeExceptions = null;
    public final ResolvedType.Primitive BYTE = new ResolvedType.Primitive("B", 1, 0);
    public final ResolvedType.Primitive CHAR = new ResolvedType.Primitive("C", 1, 1);
    public final ResolvedType.Primitive DOUBLE = new ResolvedType.Primitive("D", 2, 2);
    public final ResolvedType.Primitive FLOAT = new ResolvedType.Primitive("F", 1, 3);
    public final ResolvedType.Primitive INT = new ResolvedType.Primitive("I", 1, 4);
    public final ResolvedType.Primitive LONG = new ResolvedType.Primitive("J", 2, 5);
    public final ResolvedType.Primitive SHORT = new ResolvedType.Primitive("S", 1, 6);
    public final ResolvedType.Primitive BOOLEAN = new ResolvedType.Primitive("Z", 1, 7);
    public final ResolvedType.Primitive VOID = new ResolvedType.Primitive("V", 0, 8);
    private Object buildingTypeLock = new Object();
    private boolean allLintIgnored = false;
    private final Map<Class<?>, TypeVariable[]> workInProgress1 = new HashMap();
    private Map<ResolvedType, Set<ResolvedType>> exclusionMap = new HashMap();
    private TimeCollector timeCollector = null;

    protected abstract ReferenceTypeDelegate resolveDelegate(ReferenceType referenceType);

    public abstract IWeavingSupport getWeavingSupport();

    public abstract boolean isLoadtimeWeaving();

    static {
        systemPropertyOverWeaving = false;
        try {
            String value = System.getProperty("aspectj.overweaving", "false");
            if (value.equalsIgnoreCase("true")) {
                System.out.println("ASPECTJ: aspectj.overweaving=true: overweaving switched ON");
                systemPropertyOverWeaving = true;
            }
        } catch (Throwable t) {
            System.err.println("ASPECTJ: Unable to read system properties");
            t.printStackTrace();
        }
    }

    protected World() {
        this.typeMap.put("B", this.BYTE);
        this.typeMap.put("S", this.SHORT);
        this.typeMap.put("I", this.INT);
        this.typeMap.put("J", this.LONG);
        this.typeMap.put("F", this.FLOAT);
        this.typeMap.put("D", this.DOUBLE);
        this.typeMap.put("C", this.CHAR);
        this.typeMap.put("Z", this.BOOLEAN);
        this.typeMap.put("V", this.VOID);
        this.precedenceCalculator = new AspectPrecedenceCalculator(this);
    }

    @Override // org.aspectj.weaver.Dump.INode
    public void accept(Dump.IVisitor visitor) {
        visitor.visitObject("Shadow mungers:");
        visitor.visitList(this.crosscuttingMembersSet.getShadowMungers());
        visitor.visitObject("Type mungers:");
        visitor.visitList(this.crosscuttingMembersSet.getTypeMungers());
        visitor.visitObject("Late Type mungers:");
        visitor.visitList(this.crosscuttingMembersSet.getLateTypeMungers());
        if (this.dumpState_cantFindTypeExceptions != null) {
            visitor.visitObject("Cant find type problems:");
            visitor.visitList(this.dumpState_cantFindTypeExceptions);
            this.dumpState_cantFindTypeExceptions = null;
        }
    }

    public ResolvedType resolve(UnresolvedType ty) {
        return resolve(ty, false);
    }

    public ResolvedType resolve(UnresolvedType ty, ISourceLocation isl) {
        ResolvedType ret = resolve(ty, true);
        if (ResolvedType.isMissing(ty)) {
            getLint().cantFindType.signal(WeaverMessages.format(WeaverMessages.CANT_FIND_TYPE, ty.getName()), isl);
        }
        return ret;
    }

    public ResolvedType[] resolve(UnresolvedType[] types) {
        if (types == null) {
            return ResolvedType.NONE;
        }
        ResolvedType[] ret = new ResolvedType[types.length];
        for (int i = 0; i < types.length; i++) {
            ret[i] = resolve(types[i]);
        }
        return ret;
    }

    public ResolvedType resolve(UnresolvedType ty, boolean allowMissing) {
        ResolvedType ret;
        if (ty instanceof ResolvedType) {
            ResolvedType rty = resolve((ResolvedType) ty);
            if (!rty.isTypeVariableReference() || ((TypeVariableReferenceType) rty).isTypeVariableResolved()) {
                return rty;
            }
        }
        if (ty.isTypeVariableReference()) {
            return ty.resolve(this);
        }
        String signature = ty.getSignature();
        ResolvedType ret2 = this.typeMap.get(signature);
        if (ret2 != null) {
            ret2.world = this;
            return ret2;
        }
        if (signature.equals("?") || signature.equals("*")) {
            ResolvedType something = getWildcard();
            this.typeMap.put("?", something);
            return something;
        }
        synchronized (this.buildingTypeLock) {
            if (ty.isArray()) {
                ResolvedType componentType = resolve(ty.getComponentType(), allowMissing);
                ret = new ArrayReferenceType(signature, PropertyAccessor.PROPERTY_KEY_PREFIX + componentType.getErasureSignature(), this, componentType);
            } else {
                ret = resolveToReferenceType(ty, allowMissing);
                if (!allowMissing && ret.isMissing()) {
                    ret = handleRequiredMissingTypeDuringResolution(ty);
                }
                if (this.completeBinaryTypes) {
                    completeBinaryType(ret);
                }
            }
        }
        ResolvedType result = this.typeMap.get(signature);
        if (result == null && !ret.isMissing()) {
            ResolvedType ret3 = ensureRawTypeIfNecessary(ret);
            this.typeMap.put(signature, ret3);
            return ret3;
        }
        if (result == null) {
            return ret;
        }
        return result;
    }

    private BoundedReferenceType getWildcard() {
        if (this.wildcard == null) {
            this.wildcard = new BoundedReferenceType(this);
        }
        return this.wildcard;
    }

    protected void completeBinaryType(ResolvedType ret) {
    }

    public boolean isLocallyDefined(String classname) {
        return false;
    }

    private ResolvedType handleRequiredMissingTypeDuringResolution(UnresolvedType ty) {
        if (this.dumpState_cantFindTypeExceptions == null) {
            this.dumpState_cantFindTypeExceptions = new ArrayList();
        }
        if (this.dumpState_cantFindTypeExceptions.size() < 100) {
            this.dumpState_cantFindTypeExceptions.add(new RuntimeException("Can't find type " + ty.getName()));
        }
        return new MissingResolvedTypeWithKnownSignature(ty.getSignature(), this);
    }

    public ResolvedType resolve(ResolvedType ty) {
        if (ty.isTypeVariableReference()) {
            return ty;
        }
        ResolvedType resolved = this.typeMap.get(ty.getSignature());
        if (resolved == null) {
            this.typeMap.put(ty.getSignature(), ensureRawTypeIfNecessary(ty));
            resolved = ty;
        }
        resolved.world = this;
        return resolved;
    }

    private ResolvedType ensureRawTypeIfNecessary(ResolvedType type) {
        if (!isInJava5Mode() || type.isRawType()) {
            return type;
        }
        if ((type instanceof ReferenceType) && ((ReferenceType) type).getDelegate() != null && type.isGenericType()) {
            ReferenceType rawType = new ReferenceType(type.getSignature(), this);
            rawType.typeKind = UnresolvedType.TypeKind.RAW;
            ReferenceTypeDelegate delegate = ((ReferenceType) type).getDelegate();
            rawType.setDelegate(delegate);
            rawType.setGenericType((ReferenceType) type);
            return rawType;
        }
        return type;
    }

    public ResolvedType resolve(String name) {
        ResolvedType ret = resolve(UnresolvedType.forName(name));
        return ret;
    }

    public ReferenceType resolveToReferenceType(String name) {
        return (ReferenceType) resolve(name);
    }

    public ResolvedType resolve(String name, boolean allowMissing) {
        return resolve(UnresolvedType.forName(name), allowMissing);
    }

    private final ResolvedType resolveToReferenceType(UnresolvedType ty, boolean allowMissing) {
        if (ty.isParameterizedType()) {
            ResolvedType rt = resolveGenericTypeFor(ty, allowMissing);
            if (rt.isMissing()) {
                return rt;
            }
            ReferenceType parameterizedType = TypeFactory.createParameterizedType((ReferenceType) rt, ty.typeParameters, this);
            return parameterizedType;
        }
        if (ty.isGenericType()) {
            ResolvedType rt2 = resolveGenericTypeFor(ty, false);
            ReferenceType genericType = (ReferenceType) rt2;
            if (rt2.isMissing()) {
                return rt2;
            }
            return genericType;
        }
        if (ty.isGenericWildcard()) {
            return resolveGenericWildcardFor((WildcardedUnresolvedType) ty);
        }
        String erasedSignature = ty.getErasureSignature();
        ReferenceType simpleOrRawType = new ReferenceType(erasedSignature, this);
        if (ty.needsModifiableDelegate()) {
            simpleOrRawType.setNeedsModifiableDelegate(true);
        }
        ReferenceTypeDelegate delegate = resolveDelegate(simpleOrRawType);
        if (delegate == null) {
            return new MissingResolvedTypeWithKnownSignature(ty.getSignature(), erasedSignature, this);
        }
        if (delegate.isGeneric() && this.behaveInJava5Way) {
            simpleOrRawType.typeKind = UnresolvedType.TypeKind.RAW;
            if (simpleOrRawType.hasNewInterfaces()) {
                throw new IllegalStateException("Simple type promoted forced to raw, but it had new interfaces/superclass.  Type is " + simpleOrRawType.getName());
            }
            ReferenceType genericType2 = makeGenericTypeFrom(delegate, simpleOrRawType);
            simpleOrRawType.setDelegate(delegate);
            genericType2.setDelegate(delegate);
            simpleOrRawType.setGenericType(genericType2);
            return simpleOrRawType;
        }
        simpleOrRawType.setDelegate(delegate);
        return simpleOrRawType;
    }

    public ResolvedType resolveGenericTypeFor(UnresolvedType anUnresolvedType, boolean allowMissing) {
        String rawSignature = anUnresolvedType.getRawType().getSignature();
        ResolvedType rawType = this.typeMap.get(rawSignature);
        if (rawType == null) {
            rawType = resolve(UnresolvedType.forSignature(rawSignature), allowMissing);
            this.typeMap.put(rawSignature, rawType);
        }
        if (rawType.isMissing()) {
            return rawType;
        }
        ResolvedType genericType = rawType.getGenericType();
        if (rawType.isSimpleType() && (anUnresolvedType.typeParameters == null || anUnresolvedType.typeParameters.length == 0)) {
            rawType.world = this;
            return rawType;
        }
        if (genericType != null) {
            genericType.world = this;
            return genericType;
        }
        ReferenceTypeDelegate delegate = resolveDelegate((ReferenceType) rawType);
        ReferenceType genericRefType = makeGenericTypeFrom(delegate, (ReferenceType) rawType);
        ((ReferenceType) rawType).setGenericType(genericRefType);
        genericRefType.setDelegate(delegate);
        ((ReferenceType) rawType).setDelegate(delegate);
        return genericRefType;
    }

    private ReferenceType makeGenericTypeFrom(ReferenceTypeDelegate delegate, ReferenceType rawType) {
        String genericSig = delegate.getDeclaredGenericSignature();
        if (genericSig != null) {
            return new ReferenceType(UnresolvedType.forGenericTypeSignature(rawType.getSignature(), delegate.getDeclaredGenericSignature()), this);
        }
        return new ReferenceType(UnresolvedType.forGenericTypeVariables(rawType.getSignature(), delegate.getTypeVariables()), this);
    }

    private ReferenceType resolveGenericWildcardFor(WildcardedUnresolvedType aType) {
        BoundedReferenceType ret;
        if (aType.isExtends()) {
            ResolvedType resolvedUpperBound = resolve(aType.getUpperBound());
            if (resolvedUpperBound.isMissing()) {
                return getWildcard();
            }
            ret = new BoundedReferenceType((ReferenceType) resolvedUpperBound, true, this);
        } else if (aType.isSuper()) {
            ResolvedType resolvedLowerBound = resolve(aType.getLowerBound());
            if (resolvedLowerBound.isMissing()) {
                return getWildcard();
            }
            ret = new BoundedReferenceType((ReferenceType) resolvedLowerBound, false, this);
        } else {
            ret = getWildcard();
        }
        return ret;
    }

    public ResolvedType getCoreType(UnresolvedType tx) {
        ResolvedType coreTy = resolve(tx, true);
        if (coreTy.isMissing()) {
            MessageUtil.error(this.messageHandler, WeaverMessages.format(WeaverMessages.CANT_FIND_CORE_TYPE, tx.getName()));
        }
        return coreTy;
    }

    public ReferenceType lookupOrCreateName(UnresolvedType ty) {
        String signature = ty.getSignature();
        ReferenceType ret = lookupBySignature(signature);
        if (ret == null) {
            ret = ReferenceType.fromTypeX(ty, this);
            this.typeMap.put(signature, ret);
        }
        return ret;
    }

    public ReferenceType lookupBySignature(String signature) {
        return (ReferenceType) this.typeMap.get(signature);
    }

    public ResolvedMember resolve(Member member) {
        ResolvedMember ret;
        ResolvedType declaring = member.getDeclaringType().resolve(this);
        if (declaring.isRawType()) {
            declaring = declaring.getGenericType();
        }
        if (member.getKind() == Member.FIELD) {
            ret = declaring.lookupField(member);
        } else {
            ret = declaring.lookupMethod(member);
        }
        if (ret != null) {
            return ret;
        }
        return declaring.lookupSyntheticMember(member);
    }

    public void setAllLintIgnored() {
        this.allLintIgnored = true;
    }

    public boolean areAllLintIgnored() {
        return this.allLintIgnored;
    }

    public final Advice createAdviceMunger(AdviceKind kind, Pointcut p, Member signature, int extraParameterFlags, IHasSourceLocation loc, ResolvedType declaringAspect) {
        AjAttribute.AdviceAttribute attribute = new AjAttribute.AdviceAttribute(kind, p, extraParameterFlags, loc.getStart(), loc.getEnd(), loc.getSourceContext());
        return getWeavingSupport().createAdviceMunger(attribute, p, signature, declaringAspect);
    }

    public int compareByPrecedence(ResolvedType aspect1, ResolvedType aspect2) {
        return this.precedenceCalculator.compareByPrecedence(aspect1, aspect2);
    }

    public Integer getPrecedenceIfAny(ResolvedType aspect1, ResolvedType aspect2) {
        return this.precedenceCalculator.getPrecedenceIfAny(aspect1, aspect2);
    }

    public int compareByPrecedenceAndHierarchy(ResolvedType aspect1, ResolvedType aspect2) {
        return this.precedenceCalculator.compareByPrecedenceAndHierarchy(aspect1, aspect2);
    }

    public IMessageHandler getMessageHandler() {
        return this.messageHandler;
    }

    public void setMessageHandler(IMessageHandler messageHandler) {
        if (isInPinpointMode()) {
            this.messageHandler = new PinpointingMessageHandler(messageHandler);
        } else {
            this.messageHandler = messageHandler;
        }
    }

    public void showMessage(IMessage.Kind kind, String message, ISourceLocation loc1, ISourceLocation loc2) {
        if (loc1 != null) {
            this.messageHandler.handleMessage(new Message(message, kind, (Throwable) null, loc1));
            if (loc2 != null) {
                this.messageHandler.handleMessage(new Message(message, kind, (Throwable) null, loc2));
                return;
            }
            return;
        }
        this.messageHandler.handleMessage(new Message(message, kind, (Throwable) null, loc2));
    }

    public void setCrossReferenceHandler(ICrossReferenceHandler xrefHandler) {
        this.xrefHandler = xrefHandler;
    }

    public ICrossReferenceHandler getCrossReferenceHandler() {
        return this.xrefHandler;
    }

    public void setTypeVariableLookupScope(TypeVariableDeclaringElement scope) {
        this.typeVariableLookupScope = scope;
    }

    public TypeVariableDeclaringElement getTypeVariableLookupScope() {
        return this.typeVariableLookupScope;
    }

    public List<DeclareParents> getDeclareParents() {
        return this.crosscuttingMembersSet.getDeclareParents();
    }

    public List<DeclareAnnotation> getDeclareAnnotationOnTypes() {
        return this.crosscuttingMembersSet.getDeclareAnnotationOnTypes();
    }

    public List<DeclareAnnotation> getDeclareAnnotationOnFields() {
        return this.crosscuttingMembersSet.getDeclareAnnotationOnFields();
    }

    public List<DeclareAnnotation> getDeclareAnnotationOnMethods() {
        return this.crosscuttingMembersSet.getDeclareAnnotationOnMethods();
    }

    public List<DeclareTypeErrorOrWarning> getDeclareTypeEows() {
        return this.crosscuttingMembersSet.getDeclareTypeEows();
    }

    public List<DeclareSoft> getDeclareSoft() {
        return this.crosscuttingMembersSet.getDeclareSofts();
    }

    public CrosscuttingMembersSet getCrosscuttingMembersSet() {
        return this.crosscuttingMembersSet;
    }

    public IStructureModel getModel() {
        return this.model;
    }

    public void setModel(IStructureModel model) {
        this.model = model;
    }

    public Lint getLint() {
        return this.lint;
    }

    public void setLint(Lint lint) {
        this.lint = lint;
    }

    public boolean isXnoInline() {
        return this.XnoInline;
    }

    public void setXnoInline(boolean xnoInline) {
        this.XnoInline = xnoInline;
    }

    public boolean isXlazyTjp() {
        return this.XlazyTjp;
    }

    public void setXlazyTjp(boolean b) {
        this.XlazyTjp = b;
    }

    public boolean isHasMemberSupportEnabled() {
        return this.XhasMember;
    }

    public void setXHasMemberSupportEnabled(boolean b) {
        this.XhasMember = b;
    }

    public boolean isInPinpointMode() {
        return this.Xpinpoint;
    }

    public void setPinpointMode(boolean b) {
        this.Xpinpoint = b;
    }

    public boolean useFinal() {
        return this.useFinal;
    }

    public boolean isMinimalModel() throws AbortException {
        ensureAdvancedConfigurationProcessed();
        return this.minimalModel;
    }

    public boolean isTargettingRuntime1_6_10() throws AbortException {
        ensureAdvancedConfigurationProcessed();
        return this.targettingRuntime1_6_10;
    }

    public void setBehaveInJava5Way(boolean b) {
        this.behaveInJava5Way = b;
    }

    public void setTiming(boolean timersOn, boolean reportPeriodically) {
        this.timing = timersOn;
        this.timingPeriodically = reportPeriodically;
    }

    public void setErrorAndWarningThreshold(boolean errorThreshold, boolean warningThreshold) {
        this.errorThreshold = errorThreshold;
        this.warningThreshold = warningThreshold;
    }

    public boolean isIgnoringUnusedDeclaredThrownException() {
        return this.errorThreshold || this.warningThreshold;
    }

    public void performExtraConfiguration(String config) throws AbortException {
        int pos2;
        if (config == null) {
            return;
        }
        this.extraConfiguration = new Properties();
        while (true) {
            int pos = config.indexOf(",");
            if (pos == -1) {
                break;
            }
            String nvpair = config.substring(0, pos);
            int pos22 = nvpair.indexOf(SymbolConstants.EQUAL_SYMBOL);
            if (pos22 != -1) {
                String n = nvpair.substring(0, pos22);
                String v = nvpair.substring(pos22 + 1);
                this.extraConfiguration.setProperty(n, v);
            }
            config = config.substring(pos + 1);
        }
        if (config.length() > 0 && (pos2 = config.indexOf(SymbolConstants.EQUAL_SYMBOL)) != -1) {
            String n2 = config.substring(0, pos2);
            String v2 = config.substring(pos2 + 1);
            this.extraConfiguration.setProperty(n2, v2);
        }
        ensureAdvancedConfigurationProcessed();
    }

    public boolean areInfoMessagesEnabled() {
        if (this.infoMessagesEnabled == 0) {
            this.infoMessagesEnabled = this.messageHandler.isIgnoring(IMessage.INFO) ? 1 : 2;
        }
        return this.infoMessagesEnabled == 2;
    }

    public Properties getExtraConfiguration() {
        return this.extraConfiguration;
    }

    public boolean isInJava5Mode() {
        return this.behaveInJava5Way;
    }

    public boolean isTimingEnabled() {
        return this.timing;
    }

    public void setTargetAspectjRuntimeLevel(String s) {
        this.targetAspectjRuntimeLevel = s;
    }

    public void setOptionalJoinpoints(String jps) {
        if (jps == null) {
            return;
        }
        if (jps.indexOf("arrayconstruction") != -1) {
            this.optionalJoinpoint_ArrayConstruction = true;
        }
        if (jps.indexOf("synchronization") != -1) {
            this.optionalJoinpoint_Synchronization = true;
        }
    }

    public boolean isJoinpointArrayConstructionEnabled() {
        return this.optionalJoinpoint_ArrayConstruction;
    }

    public boolean isJoinpointSynchronizationEnabled() {
        return this.optionalJoinpoint_Synchronization;
    }

    public String getTargetAspectjRuntimeLevel() {
        return this.targetAspectjRuntimeLevel;
    }

    public boolean isTargettingAspectJRuntime12() {
        boolean b;
        if (!isInJava5Mode()) {
            b = true;
        } else {
            b = getTargetAspectjRuntimeLevel().equals(Constants.RUNTIME_LEVEL_12);
        }
        return b;
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/World$TypeMap.class */
    public static class TypeMap {
        public static final int DONT_USE_REFS = 0;
        public static final int USE_WEAK_REFS = 1;
        public static final int USE_SOFT_REFS = 2;
        public List<String> addedSinceLastDemote;
        public List<String> writtenClasses;
        private static boolean debug = false;
        public static boolean useExpendableMap = true;
        private boolean demotionSystemActive;
        private final World w;
        private boolean memoryProfiling;
        private boolean debugDemotion = false;
        public int policy = 1;
        final Map<String, ResolvedType> tMap = new HashMap();
        final Map<String, Reference<ResolvedType>> expendableMap = Collections.synchronizedMap(new WeakHashMap());
        private int maxExpendableMapSize = -1;
        private int collectedTypes = 0;
        private final ReferenceQueue<ResolvedType> rq = new ReferenceQueue<>();

        TypeMap(World w) {
            this.memoryProfiling = false;
            this.demotionSystemActive = w.isDemotionActive() && (w.isLoadtimeWeaving() || w.couldIncrementalCompileFollow());
            this.addedSinceLastDemote = new ArrayList();
            this.writtenClasses = new ArrayList();
            this.w = w;
            this.memoryProfiling = false;
        }

        public Map<String, Reference<ResolvedType>> getExpendableMap() {
            return this.expendableMap;
        }

        public Map<String, ResolvedType> getMainMap() {
            return this.tMap;
        }

        public int demote() {
            return demote(false);
        }

        public int demote(boolean atEndOfCompile) {
            List<ConcreteTypeMunger> typeMungers;
            if (!this.demotionSystemActive) {
                return 0;
            }
            if (this.debugDemotion) {
                System.out.println("Demotion running " + this.addedSinceLastDemote);
            }
            boolean isLtw = this.w.isLoadtimeWeaving();
            int demotionCounter = 0;
            if (isLtw) {
                for (String key : this.addedSinceLastDemote) {
                    ResolvedType type = this.tMap.get(key);
                    if (type != null && !type.isAspect() && !type.equals(UnresolvedType.OBJECT) && !type.isPrimitiveType() && ((typeMungers = type.getInterTypeMungers()) == null || typeMungers.size() == 0)) {
                        this.tMap.remove(key);
                        insertInExpendableMap(key, type);
                        demotionCounter++;
                    }
                }
                this.addedSinceLastDemote.clear();
            } else {
                List<String> forRemoval = new ArrayList<>();
                for (String key2 : this.addedSinceLastDemote) {
                    ResolvedType type2 = this.tMap.get(key2);
                    if (type2 == null) {
                        forRemoval.add(key2);
                    } else if (this.writtenClasses.contains(type2.getName())) {
                        if (type2 != null && !type2.isAspect() && !type2.equals(UnresolvedType.OBJECT) && !type2.isPrimitiveType()) {
                            List<ConcreteTypeMunger> typeMungers2 = type2.getInterTypeMungers();
                            if (typeMungers2 == null || typeMungers2.size() == 0) {
                                ReferenceTypeDelegate delegate = ((ReferenceType) type2).getDelegate();
                                boolean isWeavable = delegate == null ? false : delegate.isExposedToWeaver();
                                boolean hasBeenWoven = delegate == null ? false : delegate.hasBeenWoven();
                                if (!isWeavable || hasBeenWoven) {
                                    if (this.debugDemotion) {
                                        System.out.println("Demoting " + key2);
                                    }
                                    forRemoval.add(key2);
                                    this.tMap.remove(key2);
                                    insertInExpendableMap(key2, type2);
                                    demotionCounter++;
                                }
                            } else {
                                this.writtenClasses.remove(type2.getName());
                                forRemoval.add(key2);
                            }
                        } else {
                            this.writtenClasses.remove(type2.getName());
                            forRemoval.add(key2);
                        }
                    }
                }
                this.addedSinceLastDemote.removeAll(forRemoval);
            }
            if (this.debugDemotion) {
                System.out.println("Demoted " + demotionCounter + " types.  Types remaining in fixed set #" + this.tMap.keySet().size() + ".  addedSinceLastDemote size is " + this.addedSinceLastDemote.size());
                System.out.println("writtenClasses.size() = " + this.writtenClasses.size() + ": " + this.writtenClasses);
            }
            if (atEndOfCompile) {
                if (this.debugDemotion) {
                    System.out.println("Clearing writtenClasses");
                }
                this.writtenClasses.clear();
            }
            return demotionCounter;
        }

        private void insertInExpendableMap(String key, ResolvedType type) {
            if (useExpendableMap && !this.expendableMap.containsKey(key)) {
                if (this.policy == 2) {
                    this.expendableMap.put(key, new SoftReference(type));
                } else {
                    this.expendableMap.put(key, new WeakReference(type));
                }
            }
        }

        public ResolvedType put(String key, ResolvedType type) {
            if (!type.isCacheable()) {
                return type;
            }
            if (type.isParameterizedType() && type.isParameterizedWithTypeVariable()) {
                if (debug) {
                    System.err.println("Not putting a parameterized type that utilises member declared type variables into the typemap: key=" + key + " type=" + type);
                }
                return type;
            }
            if (type.isTypeVariableReference()) {
                if (debug) {
                    System.err.println("Not putting a type variable reference type into the typemap: key=" + key + " type=" + type);
                }
                return type;
            }
            if (type instanceof BoundedReferenceType) {
                if (debug) {
                    System.err.println("Not putting a bounded reference type into the typemap: key=" + key + " type=" + type);
                }
                return type;
            }
            if (type instanceof MissingResolvedTypeWithKnownSignature) {
                if (debug) {
                    System.err.println("Not putting a missing type into the typemap: key=" + key + " type=" + type);
                }
                return type;
            }
            if ((type instanceof ReferenceType) && ((ReferenceType) type).getDelegate() == null && this.w.isExpendable(type)) {
                if (debug) {
                    System.err.println("Not putting expendable ref type with null delegate into typemap: key=" + key + " type=" + type);
                }
                return type;
            }
            if ((type instanceof ReferenceType) && type.getWorld().isInJava5Mode() && ((ReferenceType) type).getDelegate() != null && type.isGenericType()) {
                throw new BCException("Attempt to add generic type to typemap " + type.toString() + " (should be raw)");
            }
            if (this.w.isExpendable(type)) {
                if (useExpendableMap) {
                    if (this.policy == 1) {
                        if (this.memoryProfiling) {
                            this.expendableMap.put(key, new WeakReference(type, this.rq));
                        } else {
                            this.expendableMap.put(key, new WeakReference(type));
                        }
                    } else if (this.policy == 2) {
                        if (this.memoryProfiling) {
                            this.expendableMap.put(key, new SoftReference(type, this.rq));
                        } else {
                            this.expendableMap.put(key, new SoftReference(type));
                        }
                    }
                }
                if (this.memoryProfiling && this.expendableMap.size() > this.maxExpendableMapSize) {
                    this.maxExpendableMapSize = this.expendableMap.size();
                }
                return type;
            }
            if (this.demotionSystemActive) {
                this.addedSinceLastDemote.add(key);
            }
            return this.tMap.put(key, type);
        }

        public void report() throws AbortException {
            if (!this.memoryProfiling) {
                return;
            }
            checkq();
            this.w.getMessageHandler().handleMessage(MessageUtil.info("MEMORY: world expendable type map reached maximum size of #" + this.maxExpendableMapSize + " entries"));
            this.w.getMessageHandler().handleMessage(MessageUtil.info("MEMORY: types collected through garbage collection #" + this.collectedTypes + " entries"));
        }

        public void checkq() {
            if (!this.memoryProfiling) {
                return;
            }
            while (this.rq.poll() != null) {
                this.collectedTypes++;
            }
        }

        public ResolvedType get(String key) {
            SoftReference<ResolvedType> ref;
            checkq();
            ResolvedType ret = this.tMap.get(key);
            if (ret == null) {
                if (this.policy == 1) {
                    WeakReference<ResolvedType> ref2 = (WeakReference) this.expendableMap.get(key);
                    if (ref2 != null) {
                        ret = ref2.get();
                    }
                } else if (this.policy == 2 && (ref = (SoftReference) this.expendableMap.get(key)) != null) {
                    ret = ref.get();
                }
            }
            return ret;
        }

        public ResolvedType remove(String key) {
            SoftReference<ResolvedType> wref;
            ResolvedType ret = this.tMap.remove(key);
            if (ret == null) {
                if (this.policy == 1) {
                    WeakReference<ResolvedType> wref2 = (WeakReference) this.expendableMap.remove(key);
                    if (wref2 != null) {
                        ret = wref2.get();
                    }
                } else if (this.policy == 2 && (wref = (SoftReference) this.expendableMap.remove(key)) != null) {
                    ret = wref.get();
                }
            }
            return ret;
        }

        public void classWriteEvent(String classname) {
            if (this.demotionSystemActive) {
                this.writtenClasses.add(classname);
            }
            if (this.debugDemotion) {
                System.out.println("Class write event for " + classname);
            }
        }

        public void demote(ResolvedType type) {
            String key = type.getSignature();
            if (this.debugDemotion) {
                this.addedSinceLastDemote.remove(key);
            }
            this.tMap.remove(key);
            insertInExpendableMap(key, type);
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/World$AspectPrecedenceCalculator.class */
    private static class AspectPrecedenceCalculator {
        private final World world;
        private final Map<PrecedenceCacheKey, Integer> cachedResults = new HashMap();

        public AspectPrecedenceCalculator(World forSomeWorld) {
            this.world = forSomeWorld;
        }

        public int compareByPrecedence(ResolvedType firstAspect, ResolvedType secondAspect) throws AbortException {
            PrecedenceCacheKey key = new PrecedenceCacheKey(firstAspect, secondAspect);
            if (this.cachedResults.containsKey(key)) {
                return this.cachedResults.get(key).intValue();
            }
            int order = 0;
            DeclarePrecedence orderer = null;
            Iterator<Declare> i = this.world.getCrosscuttingMembersSet().getDeclareDominates().iterator();
            while (i.hasNext()) {
                DeclarePrecedence d = (DeclarePrecedence) i.next();
                int thisOrder = d.compare(firstAspect, secondAspect);
                if (thisOrder != 0) {
                    if (orderer == null) {
                        orderer = d;
                    }
                    if (order != 0 && order != thisOrder) {
                        ISourceLocation[] isls = {orderer.getSourceLocation(), d.getSourceLocation()};
                        Message m = new Message("conflicting declare precedence orderings for aspects: " + firstAspect.getName() + " and " + secondAspect.getName(), (ISourceLocation) null, true, isls);
                        this.world.getMessageHandler().handleMessage(m);
                    } else {
                        order = thisOrder;
                    }
                }
            }
            this.cachedResults.put(key, new Integer(order));
            return order;
        }

        public Integer getPrecedenceIfAny(ResolvedType aspect1, ResolvedType aspect2) {
            return this.cachedResults.get(new PrecedenceCacheKey(aspect1, aspect2));
        }

        public int compareByPrecedenceAndHierarchy(ResolvedType firstAspect, ResolvedType secondAspect) throws AbortException {
            if (firstAspect.equals(secondAspect)) {
                return 0;
            }
            int ret = compareByPrecedence(firstAspect, secondAspect);
            if (ret != 0) {
                return ret;
            }
            if (firstAspect.isAssignableFrom(secondAspect)) {
                return -1;
            }
            if (secondAspect.isAssignableFrom(firstAspect)) {
                return 1;
            }
            return 0;
        }

        /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/World$AspectPrecedenceCalculator$PrecedenceCacheKey.class */
        private static class PrecedenceCacheKey {
            public ResolvedType aspect1;
            public ResolvedType aspect2;

            public PrecedenceCacheKey(ResolvedType a1, ResolvedType a2) {
                this.aspect1 = a1;
                this.aspect2 = a2;
            }

            public boolean equals(Object obj) {
                if (!(obj instanceof PrecedenceCacheKey)) {
                    return false;
                }
                PrecedenceCacheKey other = (PrecedenceCacheKey) obj;
                return this.aspect1 == other.aspect1 && this.aspect2 == other.aspect2;
            }

            public int hashCode() {
                return this.aspect1.hashCode() + this.aspect2.hashCode();
            }
        }
    }

    public void validateType(UnresolvedType type) {
    }

    public boolean isDemotionActive() {
        return true;
    }

    public TypeVariable[] getTypeVariablesCurrentlyBeingProcessed(Class<?> baseClass) {
        return this.workInProgress1.get(baseClass);
    }

    public void recordTypeVariablesCurrentlyBeingProcessed(Class<?> baseClass, TypeVariable[] typeVariables) {
        this.workInProgress1.put(baseClass, typeVariables);
    }

    public void forgetTypeVariablesCurrentlyBeingProcessed(Class<?> baseClass) {
        this.workInProgress1.remove(baseClass);
    }

    public void setAddSerialVerUID(boolean b) {
        this.addSerialVerUID = b;
    }

    public boolean isAddSerialVerUID() {
        return this.addSerialVerUID;
    }

    public void flush() {
        this.typeMap.expendableMap.clear();
    }

    public void ensureAdvancedConfigurationProcessed() throws AbortException {
        if (!this.checkedAdvancedConfiguration) {
            Properties p = getExtraConfiguration();
            if (p != null) {
                String s = p.getProperty(xsetBCEL_REPOSITORY_CACHING, "true");
                this.bcelRepositoryCaching = s.equalsIgnoreCase("true");
                if (!this.bcelRepositoryCaching) {
                    getMessageHandler().handleMessage(MessageUtil.info("[bcelRepositoryCaching=false] AspectJ will not use a bcel cache for class information"));
                }
                String s2 = p.getProperty(xsetITD_VERSION, "2");
                if (s2.equals("1")) {
                    this.itdVersion = 1;
                }
                String s3 = p.getProperty(xsetAVOID_FINAL, "false");
                if (s3.equalsIgnoreCase("true")) {
                    this.useFinal = false;
                }
                String s4 = p.getProperty(xsetMINIMAL_MODEL, "true");
                if (s4.equalsIgnoreCase("false")) {
                    this.minimalModel = false;
                }
                String s5 = p.getProperty(xsetTARGETING_RUNTIME_1610, "false");
                if (s5.equalsIgnoreCase("true")) {
                    this.targettingRuntime1_6_10 = true;
                }
                String s6 = p.getProperty(xsetFAST_PACK_METHODS, "true");
                this.fastMethodPacking = s6.equalsIgnoreCase("true");
                String s7 = p.getProperty(xsetPIPELINE_COMPILATION, "true");
                this.shouldPipelineCompilation = s7.equalsIgnoreCase("true");
                String s8 = p.getProperty(xsetGENERATE_STACKMAPS, "false");
                this.shouldGenerateStackMaps = s8.equalsIgnoreCase("true");
                String s9 = p.getProperty(xsetCOMPLETE_BINARY_TYPES, "false");
                this.completeBinaryTypes = s9.equalsIgnoreCase("true");
                if (this.completeBinaryTypes) {
                    getMessageHandler().handleMessage(MessageUtil.info("[completeBinaryTypes=true] Completion of binary types activated"));
                }
                String s10 = p.getProperty(xsetTYPE_DEMOTION);
                if (s10 != null) {
                    boolean b = this.typeMap.demotionSystemActive;
                    if (b && s10.equalsIgnoreCase("false")) {
                        System.out.println("typeDemotion=false: type demotion switched OFF");
                        this.typeMap.demotionSystemActive = false;
                    } else if (!b && s10.equalsIgnoreCase("true")) {
                        System.out.println("typeDemotion=true: type demotion switched ON");
                        this.typeMap.demotionSystemActive = true;
                    }
                }
                String s11 = p.getProperty(xsetOVERWEAVING, "false");
                if (s11.equalsIgnoreCase("true")) {
                    this.overWeaving = true;
                }
                String s12 = p.getProperty(xsetTYPE_DEMOTION_DEBUG, "false");
                if (s12.equalsIgnoreCase("true")) {
                    this.typeMap.debugDemotion = true;
                }
                String s13 = p.getProperty(xsetTYPE_REFS, "true");
                if (s13.equalsIgnoreCase("false")) {
                    this.typeMap.policy = 2;
                }
                this.runMinimalMemorySet = p.getProperty(xsetRUN_MINIMAL_MEMORY) != null;
                String s14 = p.getProperty(xsetRUN_MINIMAL_MEMORY, "false");
                this.runMinimalMemory = s14.equalsIgnoreCase("true");
                String s15 = p.getProperty(xsetDEBUG_STRUCTURAL_CHANGES_CODE, "false");
                this.forDEBUG_structuralChangesCode = s15.equalsIgnoreCase("true");
                String s16 = p.getProperty(xsetTRANSIENT_TJP_FIELDS, "false");
                this.transientTjpFields = s16.equalsIgnoreCase("true");
                String s17 = p.getProperty(xsetDEBUG_BRIDGING, "false");
                this.forDEBUG_bridgingCode = s17.equalsIgnoreCase("true");
                String s18 = p.getProperty(xsetGENERATE_NEW_LVTS, "true");
                this.generateNewLvts = s18.equalsIgnoreCase("true");
                if (!this.generateNewLvts) {
                    getMessageHandler().handleMessage(MessageUtil.info("[generateNewLvts=false] for methods without an incoming local variable table, do not generate one"));
                }
                String s19 = p.getProperty(xsetOPTIMIZED_MATCHING, "true");
                this.optimizedMatching = s19.equalsIgnoreCase("true");
                if (!this.optimizedMatching) {
                    getMessageHandler().handleMessage(MessageUtil.info("[optimizedMatching=false] optimized matching turned off"));
                }
                String s20 = p.getProperty(xsetTIMERS_PER_JOINPOINT, SQLError.SQL_STATE_INVALID_TRANSACTION_STATE);
                try {
                    this.timersPerJoinpoint = Integer.parseInt(s20);
                } catch (Exception e) {
                    getMessageHandler().handleMessage(MessageUtil.error("unable to process timersPerJoinpoint value of " + s20));
                    this.timersPerJoinpoint = 25000L;
                }
                String s21 = p.getProperty(xsetTIMERS_PER_FASTMATCH_CALL, "250");
                try {
                    this.timersPerType = Integer.parseInt(s21);
                } catch (Exception e2) {
                    getMessageHandler().handleMessage(MessageUtil.error("unable to process timersPerType value of " + s21));
                    this.timersPerType = 250L;
                }
            }
            try {
                if (systemPropertyOverWeaving) {
                    this.overWeaving = true;
                }
                String value = System.getProperty("aspectj.typeDemotion", "false");
                if (value.equalsIgnoreCase("true")) {
                    System.out.println("ASPECTJ: aspectj.typeDemotion=true: type demotion switched ON");
                    this.typeMap.demotionSystemActive = true;
                }
                String value2 = System.getProperty("aspectj.minimalModel", "false");
                if (value2.equalsIgnoreCase("true")) {
                    System.out.println("ASPECTJ: aspectj.minimalModel=true: minimal model switched ON");
                    this.minimalModel = true;
                }
            } catch (Throwable t) {
                System.err.println("ASPECTJ: Unable to read system properties");
                t.printStackTrace();
            }
            this.checkedAdvancedConfiguration = true;
        }
    }

    public boolean isRunMinimalMemory() throws AbortException {
        ensureAdvancedConfigurationProcessed();
        return this.runMinimalMemory;
    }

    public boolean isTransientTjpFields() throws AbortException {
        ensureAdvancedConfigurationProcessed();
        return this.transientTjpFields;
    }

    public boolean isRunMinimalMemorySet() throws AbortException {
        ensureAdvancedConfigurationProcessed();
        return this.runMinimalMemorySet;
    }

    public boolean shouldFastPackMethods() throws AbortException {
        ensureAdvancedConfigurationProcessed();
        return this.fastMethodPacking;
    }

    public boolean shouldPipelineCompilation() throws AbortException {
        ensureAdvancedConfigurationProcessed();
        return this.shouldPipelineCompilation;
    }

    public boolean shouldGenerateStackMaps() throws AbortException {
        ensureAdvancedConfigurationProcessed();
        return this.shouldGenerateStackMaps;
    }

    public void setIncrementalCompileCouldFollow(boolean b) {
        this.incrementalCompileCouldFollow = b;
    }

    public boolean couldIncrementalCompileFollow() {
        return this.incrementalCompileCouldFollow;
    }

    public void setSynchronizationPointcutsInUse() {
        if (trace.isTraceEnabled()) {
            trace.enter("setSynchronizationPointcutsInUse", this);
        }
        this.synchronizationPointcutsInUse = true;
        if (trace.isTraceEnabled()) {
            trace.exit("setSynchronizationPointcutsInUse");
        }
    }

    public boolean areSynchronizationPointcutsInUse() {
        return this.synchronizationPointcutsInUse;
    }

    public void registerPointcutHandler(PointcutDesignatorHandler designatorHandler) {
        if (this.pointcutDesignators == null) {
            this.pointcutDesignators = new HashSet();
        }
        this.pointcutDesignators.add(designatorHandler);
    }

    public Set<PointcutDesignatorHandler> getRegisteredPointcutHandlers() {
        if (this.pointcutDesignators == null) {
            return Collections.emptySet();
        }
        return this.pointcutDesignators;
    }

    public void reportMatch(ShadowMunger munger, Shadow shadow) {
    }

    public boolean isOverWeaving() {
        return this.overWeaving;
    }

    public void reportCheckerMatch(Checker checker, Shadow shadow) {
    }

    public boolean isXmlConfigured() {
        return false;
    }

    public boolean isAspectIncluded(ResolvedType aspectType) {
        return true;
    }

    public boolean hasUnsatisfiedDependency(ResolvedType aspectType) {
        return false;
    }

    public TypePattern getAspectScope(ResolvedType declaringType) {
        return null;
    }

    public Map<String, ResolvedType> getFixed() {
        return this.typeMap.tMap;
    }

    public Map<String, Reference<ResolvedType>> getExpendable() {
        return this.typeMap.expendableMap;
    }

    public void demote() {
        this.typeMap.demote();
    }

    protected boolean isExpendable(ResolvedType type) {
        return (type.equals(UnresolvedType.OBJECT) || type.isExposedToWeaver() || type.isPrimitiveType() || type.isPrimitiveArray()) ? false : true;
    }

    public Map<ResolvedType, Set<ResolvedType>> getExclusionMap() {
        return this.exclusionMap;
    }

    public void record(Pointcut pointcut, long timetaken) throws AbortException {
        if (this.timeCollector == null) {
            ensureAdvancedConfigurationProcessed();
            this.timeCollector = new TimeCollector(this);
        }
        this.timeCollector.record(pointcut, timetaken);
    }

    public void recordFastMatch(Pointcut pointcut, long timetaken) throws AbortException {
        if (this.timeCollector == null) {
            ensureAdvancedConfigurationProcessed();
            this.timeCollector = new TimeCollector(this);
        }
        this.timeCollector.recordFastMatch(pointcut, timetaken);
    }

    public void reportTimers() throws AbortException {
        if (this.timeCollector != null && !this.timingPeriodically) {
            this.timeCollector.report();
            this.timeCollector = new TimeCollector(this);
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/World$TimeCollector.class */
    private static class TimeCollector {
        private World world;
        long perJoinpointCount;
        long perTypes;
        Map<String, Long> joinpointsPerPointcut;
        Map<String, Long> timePerPointcut;
        Map<String, Long> fastMatchTimesPerPointcut = new HashMap();
        Map<String, Long> fastMatchTypesPerPointcut = new HashMap();
        long joinpointCount = 0;
        long typeCount = 0;

        TimeCollector(World world) {
            this.joinpointsPerPointcut = new HashMap();
            this.timePerPointcut = new HashMap();
            this.perJoinpointCount = world.timersPerJoinpoint;
            this.perTypes = world.timersPerType;
            this.world = world;
            this.joinpointsPerPointcut = new HashMap();
            this.timePerPointcut = new HashMap();
        }

        public void report() throws AbortException {
            long totalTime = 0;
            Iterator<String> it = this.joinpointsPerPointcut.keySet().iterator();
            while (it.hasNext()) {
                totalTime += this.timePerPointcut.get(it.next()).longValue();
            }
            this.world.getMessageHandler().handleMessage(MessageUtil.info("Pointcut matching cost (total=" + (totalTime / Time.APR_USEC_PER_SEC) + "ms for " + this.joinpointCount + " joinpoint match calls):"));
            for (String p : this.joinpointsPerPointcut.keySet()) {
                StringBuffer sb = new StringBuffer();
                sb.append("Time:" + (this.timePerPointcut.get(p).longValue() / Time.APR_USEC_PER_SEC) + "ms (jps:#" + this.joinpointsPerPointcut.get(p) + ") matching against " + p);
                this.world.getMessageHandler().handleMessage(MessageUtil.info(sb.toString()));
            }
            this.world.getMessageHandler().handleMessage(MessageUtil.info("---"));
            long totalTime2 = 0;
            Iterator<String> it2 = this.fastMatchTimesPerPointcut.keySet().iterator();
            while (it2.hasNext()) {
                totalTime2 += this.fastMatchTimesPerPointcut.get(it2.next()).longValue();
            }
            this.world.getMessageHandler().handleMessage(MessageUtil.info("Pointcut fast matching cost (total=" + (totalTime2 / Time.APR_USEC_PER_SEC) + "ms for " + this.typeCount + " fast match calls):"));
            for (String p2 : this.fastMatchTimesPerPointcut.keySet()) {
                StringBuffer sb2 = new StringBuffer();
                sb2.append("Time:" + (this.fastMatchTimesPerPointcut.get(p2).longValue() / Time.APR_USEC_PER_SEC) + "ms (types:#" + this.fastMatchTypesPerPointcut.get(p2) + ") fast matching against " + p2);
                this.world.getMessageHandler().handleMessage(MessageUtil.info(sb2.toString()));
            }
            this.world.getMessageHandler().handleMessage(MessageUtil.info("---"));
        }

        void record(Pointcut pointcut, long timetakenInNs) throws AbortException {
            Long time;
            this.joinpointCount++;
            String pointcutText = pointcut.toString();
            Long jpcounter = this.joinpointsPerPointcut.get(pointcutText);
            this.joinpointsPerPointcut.put(pointcutText, jpcounter == null ? 1L : Long.valueOf(jpcounter.longValue() + 1));
            Long time2 = this.timePerPointcut.get(pointcutText);
            if (time2 == null) {
                time = Long.valueOf(timetakenInNs);
            } else {
                time = Long.valueOf(time2.longValue() + timetakenInNs);
            }
            this.timePerPointcut.put(pointcutText, time);
            if (this.world.timingPeriodically && this.joinpointCount % this.perJoinpointCount == 0) {
                long totalTime = 0;
                Iterator<String> it = this.joinpointsPerPointcut.keySet().iterator();
                while (it.hasNext()) {
                    totalTime += this.timePerPointcut.get(it.next()).longValue();
                }
                this.world.getMessageHandler().handleMessage(MessageUtil.info("Pointcut matching cost (total=" + (totalTime / Time.APR_USEC_PER_SEC) + "ms for " + this.joinpointCount + " joinpoint match calls):"));
                for (String p : this.joinpointsPerPointcut.keySet()) {
                    StringBuffer sb = new StringBuffer();
                    sb.append("Time:" + (this.timePerPointcut.get(p).longValue() / Time.APR_USEC_PER_SEC) + "ms (jps:#" + this.joinpointsPerPointcut.get(p) + ") matching against " + p);
                    this.world.getMessageHandler().handleMessage(MessageUtil.info(sb.toString()));
                }
                this.world.getMessageHandler().handleMessage(MessageUtil.info("---"));
            }
        }

        void recordFastMatch(Pointcut pointcut, long timetakenInNs) throws AbortException {
            Long time;
            this.typeCount++;
            String pointcutText = pointcut.toString();
            Long typecounter = this.fastMatchTypesPerPointcut.get(pointcutText);
            this.fastMatchTypesPerPointcut.put(pointcutText, typecounter == null ? 1L : Long.valueOf(typecounter.longValue() + 1));
            Long time2 = this.fastMatchTimesPerPointcut.get(pointcutText);
            if (time2 == null) {
                time = Long.valueOf(timetakenInNs);
            } else {
                time = Long.valueOf(time2.longValue() + timetakenInNs);
            }
            this.fastMatchTimesPerPointcut.put(pointcutText, time);
            if (this.world.timingPeriodically && this.typeCount % this.perTypes == 0) {
                long totalTime = 0;
                Iterator<String> it = this.fastMatchTimesPerPointcut.keySet().iterator();
                while (it.hasNext()) {
                    totalTime += this.fastMatchTimesPerPointcut.get(it.next()).longValue();
                }
                this.world.getMessageHandler().handleMessage(MessageUtil.info("Pointcut fast matching cost (total=" + (totalTime / Time.APR_USEC_PER_SEC) + "ms for " + this.typeCount + " fast match calls):"));
                for (String p : this.fastMatchTimesPerPointcut.keySet()) {
                    StringBuffer sb = new StringBuffer();
                    sb.append("Time:" + (this.fastMatchTimesPerPointcut.get(p).longValue() / Time.APR_USEC_PER_SEC) + "ms (types:#" + this.fastMatchTypesPerPointcut.get(p) + ") fast matching against " + p);
                    this.world.getMessageHandler().handleMessage(MessageUtil.info(sb.toString()));
                }
                this.world.getMessageHandler().handleMessage(MessageUtil.info("---"));
            }
        }
    }

    public TypeMap getTypeMap() {
        return this.typeMap;
    }

    public static void reset() {
    }

    public int getItdVersion() {
        return this.itdVersion;
    }

    public void classWriteEvent(char[][] compoundName) {
    }
}
