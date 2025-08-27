package org.aspectj.weaver.bcel;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.aspectj.apache.bcel.classfile.ClassParser;
import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.aspectj.apache.bcel.classfile.JavaClass;
import org.aspectj.apache.bcel.generic.FieldInstruction;
import org.aspectj.apache.bcel.generic.INVOKEINTERFACE;
import org.aspectj.apache.bcel.generic.Instruction;
import org.aspectj.apache.bcel.generic.InstructionHandle;
import org.aspectj.apache.bcel.generic.InvokeInstruction;
import org.aspectj.apache.bcel.generic.MULTIANEWARRAY;
import org.aspectj.apache.bcel.generic.ObjectType;
import org.aspectj.apache.bcel.generic.Type;
import org.aspectj.apache.bcel.util.ClassLoaderReference;
import org.aspectj.apache.bcel.util.ClassLoaderRepository;
import org.aspectj.apache.bcel.util.ClassPath;
import org.aspectj.apache.bcel.util.NonCachingClassLoaderRepository;
import org.aspectj.apache.bcel.util.Repository;
import org.aspectj.asm.AsmManager;
import org.aspectj.asm.IRelationship;
import org.aspectj.asm.internal.CharOperation;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.IMessageHandler;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.Message;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.bridge.WeaveMessage;
import org.aspectj.weaver.Advice;
import org.aspectj.weaver.AdviceKind;
import org.aspectj.weaver.AnnotationAJ;
import org.aspectj.weaver.AnnotationOnTypeMunger;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.Checker;
import org.aspectj.weaver.ICrossReferenceHandler;
import org.aspectj.weaver.IWeavingSupport;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.MemberImpl;
import org.aspectj.weaver.MemberKind;
import org.aspectj.weaver.NewParentTypeMunger;
import org.aspectj.weaver.ReferenceType;
import org.aspectj.weaver.ReferenceTypeDelegate;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedMemberImpl;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.ResolvedTypeMunger;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.ShadowMunger;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.World;
import org.aspectj.weaver.bcel.ClassPathManager;
import org.aspectj.weaver.loadtime.definition.Definition;
import org.aspectj.weaver.loadtime.definition.DocumentParser;
import org.aspectj.weaver.model.AsmRelationshipProvider;
import org.aspectj.weaver.patterns.DeclareAnnotation;
import org.aspectj.weaver.patterns.DeclareParents;
import org.aspectj.weaver.patterns.ParserException;
import org.aspectj.weaver.patterns.PatternParser;
import org.aspectj.weaver.patterns.TypePattern;
import org.aspectj.weaver.tools.Trace;
import org.aspectj.weaver.tools.TraceFactory;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/BcelWorld.class */
public class BcelWorld extends World implements Repository {
    private final ClassPathManager classPath;
    protected Repository delegate;
    private BcelWeakClassLoaderReference loaderRef;
    private final BcelWeavingSupport bcelWeavingSupport;
    private boolean isXmlConfiguredWorld;
    private WeavingXmlConfig xmlConfiguration;
    private List<TypeDelegateResolver> typeDelegateResolvers;
    private static Trace trace = TraceFactory.getTraceFactory().getTrace(BcelWorld.class);
    private List<String> aspectRequiredTypesProcessed;
    private Map<String, String> aspectRequiredTypes;

    public BcelWorld() {
        this("");
    }

    public BcelWorld(String cp) {
        this(makeDefaultClasspath(cp), IMessageHandler.THROW, (ICrossReferenceHandler) null);
    }

    public IRelationship.Kind determineRelKind(ShadowMunger munger) {
        AdviceKind ak = ((Advice) munger).getKind();
        if (ak.getKey() == AdviceKind.Before.getKey()) {
            return IRelationship.Kind.ADVICE_BEFORE;
        }
        if (ak.getKey() == AdviceKind.After.getKey()) {
            return IRelationship.Kind.ADVICE_AFTER;
        }
        if (ak.getKey() == AdviceKind.AfterThrowing.getKey()) {
            return IRelationship.Kind.ADVICE_AFTERTHROWING;
        }
        if (ak.getKey() == AdviceKind.AfterReturning.getKey()) {
            return IRelationship.Kind.ADVICE_AFTERRETURNING;
        }
        if (ak.getKey() == AdviceKind.Around.getKey()) {
            return IRelationship.Kind.ADVICE_AROUND;
        }
        if (ak.getKey() == AdviceKind.CflowEntry.getKey() || ak.getKey() == AdviceKind.CflowBelowEntry.getKey() || ak.getKey() == AdviceKind.InterInitializer.getKey() || ak.getKey() == AdviceKind.PerCflowEntry.getKey() || ak.getKey() == AdviceKind.PerCflowBelowEntry.getKey() || ak.getKey() == AdviceKind.PerThisEntry.getKey() || ak.getKey() == AdviceKind.PerTargetEntry.getKey() || ak.getKey() == AdviceKind.Softener.getKey() || ak.getKey() == AdviceKind.PerTypeWithinEntry.getKey()) {
            return null;
        }
        throw new RuntimeException("Shadow.determineRelKind: What the hell is it? " + ak);
    }

    @Override // org.aspectj.weaver.World
    public void reportMatch(ShadowMunger munger, Shadow shadow) throws AbortException {
        if (getCrossReferenceHandler() != null) {
            getCrossReferenceHandler().addCrossReference(munger.getSourceLocation(), shadow.getSourceLocation(), determineRelKind(munger).getName(), ((Advice) munger).hasDynamicTests());
        }
        if (!getMessageHandler().isIgnoring(IMessage.WEAVEINFO)) {
            reportWeavingMessage(munger, shadow);
        }
        if (getModel() != null) {
            AsmRelationshipProvider.addAdvisedRelationship(getModelAsAsmManager(), shadow, munger);
        }
    }

    private void reportWeavingMessage(ShadowMunger munger, Shadow shadow) throws AbortException {
        Message msg;
        Advice advice = (Advice) munger;
        AdviceKind aKind = advice.getKind();
        if (aKind == null || advice.getConcreteAspect() == null) {
            return;
        }
        if (!aKind.equals(AdviceKind.Before) && !aKind.equals(AdviceKind.After) && !aKind.equals(AdviceKind.AfterReturning) && !aKind.equals(AdviceKind.AfterThrowing) && !aKind.equals(AdviceKind.Around) && !aKind.equals(AdviceKind.Softener)) {
            return;
        }
        if (shadow.getKind() == Shadow.SynchronizationUnlock) {
            if (advice.lastReportedMonitorExitJoinpointLocation != null && areTheSame(shadow.getSourceLocation(), advice.lastReportedMonitorExitJoinpointLocation)) {
                advice.lastReportedMonitorExitJoinpointLocation = null;
                return;
            }
            advice.lastReportedMonitorExitJoinpointLocation = shadow.getSourceLocation();
        }
        String description = advice.getKind().toString();
        String advisedType = shadow.getEnclosingType().getName();
        String advisingType = advice.getConcreteAspect().getName();
        if (advice.getKind().equals(AdviceKind.Softener)) {
            msg = WeaveMessage.constructWeavingMessage(WeaveMessage.WEAVEMESSAGE_SOFTENS, new String[]{advisedType, beautifyLocation(shadow.getSourceLocation()), advisingType, beautifyLocation(munger.getSourceLocation())}, advisedType, advisingType);
        } else {
            boolean runtimeTest = advice.hasDynamicTests();
            String joinPointDescription = shadow.toString();
            WeaveMessage.WeaveMessageKind weaveMessageKind = WeaveMessage.WEAVEMESSAGE_ADVISES;
            String[] strArr = new String[7];
            strArr[0] = joinPointDescription;
            strArr[1] = advisedType;
            strArr[2] = beautifyLocation(shadow.getSourceLocation());
            strArr[3] = description;
            strArr[4] = advisingType;
            strArr[5] = beautifyLocation(munger.getSourceLocation());
            strArr[6] = runtimeTest ? " [with runtime test]" : "";
            msg = WeaveMessage.constructWeavingMessage(weaveMessageKind, strArr, advisedType, advisingType);
        }
        getMessageHandler().handleMessage(msg);
    }

    private boolean areTheSame(ISourceLocation locA, ISourceLocation locB) {
        if (locA == null) {
            return locB == null;
        }
        if (locB == null || locA.getLine() != locB.getLine()) {
            return false;
        }
        File fA = locA.getSourceFile();
        File fB = locA.getSourceFile();
        if (fA == null) {
            return fB == null;
        }
        if (fB == null) {
            return false;
        }
        return fA.getName().equals(fB.getName());
    }

    private String beautifyLocation(ISourceLocation isl) {
        StringBuffer nice = new StringBuffer();
        if (isl == null || isl.getSourceFile() == null || isl.getSourceFile().getName().indexOf("no debug info available") != -1) {
            nice.append("no debug info available");
        } else {
            int takeFrom = isl.getSourceFile().getPath().lastIndexOf(47);
            if (takeFrom == -1) {
                takeFrom = isl.getSourceFile().getPath().lastIndexOf(92);
            }
            int binary = isl.getSourceFile().getPath().lastIndexOf(33);
            if (binary != -1 && binary < takeFrom) {
                String pathToBinaryLoc = isl.getSourceFile().getPath().substring(0, binary + 1);
                if (pathToBinaryLoc.indexOf(".jar") != -1) {
                    int lastSlash = pathToBinaryLoc.lastIndexOf(47);
                    if (lastSlash == -1) {
                        lastSlash = pathToBinaryLoc.lastIndexOf(92);
                    }
                    nice.append(pathToBinaryLoc.substring(lastSlash + 1));
                }
            }
            nice.append(isl.getSourceFile().getPath().substring(takeFrom + 1));
            if (isl.getLine() != 0) {
                nice.append(":").append(isl.getLine());
            }
            if (isl.getSourceFileName() != null) {
                nice.append("(from " + isl.getSourceFileName() + ")");
            }
        }
        return nice.toString();
    }

    private static List<String> makeDefaultClasspath(String cp) {
        List<String> classPath = new ArrayList<>();
        classPath.addAll(getPathEntries(cp));
        classPath.addAll(getPathEntries(ClassPath.getClassPath()));
        return classPath;
    }

    private static List<String> getPathEntries(String s) {
        List<String> ret = new ArrayList<>();
        StringTokenizer tok = new StringTokenizer(s, File.pathSeparator);
        while (tok.hasMoreTokens()) {
            ret.add(tok.nextToken());
        }
        return ret;
    }

    public BcelWorld(List classPath, IMessageHandler handler, ICrossReferenceHandler xrefHandler) {
        this.bcelWeavingSupport = new BcelWeavingSupport();
        this.isXmlConfiguredWorld = false;
        this.aspectRequiredTypesProcessed = new ArrayList();
        this.aspectRequiredTypes = null;
        this.classPath = new ClassPathManager(classPath, handler);
        setMessageHandler(handler);
        setCrossReferenceHandler(xrefHandler);
        this.delegate = this;
    }

    public BcelWorld(ClassPathManager cpm, IMessageHandler handler, ICrossReferenceHandler xrefHandler) {
        this.bcelWeavingSupport = new BcelWeavingSupport();
        this.isXmlConfiguredWorld = false;
        this.aspectRequiredTypesProcessed = new ArrayList();
        this.aspectRequiredTypes = null;
        this.classPath = cpm;
        setMessageHandler(handler);
        setCrossReferenceHandler(xrefHandler);
        this.delegate = this;
    }

    public BcelWorld(ClassLoader loader, IMessageHandler handler, ICrossReferenceHandler xrefHandler) {
        this.bcelWeavingSupport = new BcelWeavingSupport();
        this.isXmlConfiguredWorld = false;
        this.aspectRequiredTypesProcessed = new ArrayList();
        this.aspectRequiredTypes = null;
        this.classPath = null;
        this.loaderRef = new BcelWeakClassLoaderReference(loader);
        setMessageHandler(handler);
        setCrossReferenceHandler(xrefHandler);
    }

    public void ensureRepositorySetup() {
        if (this.delegate == null) {
            this.delegate = getClassLoaderRepositoryFor(this.loaderRef);
        }
    }

    public Repository getClassLoaderRepositoryFor(ClassLoaderReference loader) {
        if (this.bcelRepositoryCaching) {
            return new ClassLoaderRepository(loader);
        }
        return new NonCachingClassLoaderRepository(loader);
    }

    public void addPath(String name) {
        this.classPath.addPath(name, getMessageHandler());
    }

    public static Type makeBcelType(UnresolvedType type) {
        return Type.getType(type.getErasureSignature());
    }

    static Type[] makeBcelTypes(UnresolvedType[] types) {
        Type[] ret = new Type[types.length];
        int len = types.length;
        for (int i = 0; i < len; i++) {
            ret[i] = makeBcelType(types[i]);
        }
        return ret;
    }

    public static Type[] makeBcelTypes(String[] types) {
        if (types == null || types.length == 0) {
            return null;
        }
        Type[] ret = new Type[types.length];
        int len = types.length;
        for (int i = 0; i < len; i++) {
            ret[i] = makeBcelType(types[i]);
        }
        return ret;
    }

    public static Type makeBcelType(String type) {
        return Type.getType(type);
    }

    static String[] makeBcelTypesAsClassNames(UnresolvedType[] types) {
        String[] ret = new String[types.length];
        int len = types.length;
        for (int i = 0; i < len; i++) {
            ret[i] = types[i].getName();
        }
        return ret;
    }

    public static UnresolvedType fromBcel(Type t) {
        return UnresolvedType.forSignature(t.getSignature());
    }

    static UnresolvedType[] fromBcel(Type[] ts) {
        UnresolvedType[] ret = new UnresolvedType[ts.length];
        int len = ts.length;
        for (int i = 0; i < len; i++) {
            ret[i] = fromBcel(ts[i]);
        }
        return ret;
    }

    public ResolvedType resolve(Type t) {
        return resolve(fromBcel(t));
    }

    @Override // org.aspectj.weaver.World
    protected ReferenceTypeDelegate resolveDelegate(ReferenceType ty) {
        String name = ty.getName();
        ensureAdvancedConfigurationProcessed();
        JavaClass jc = lookupJavaClass(this.classPath, name);
        if (jc == null) {
            if (this.typeDelegateResolvers != null) {
                for (TypeDelegateResolver tdr : this.typeDelegateResolvers) {
                    ReferenceTypeDelegate delegate = tdr.getDelegate(ty);
                    if (delegate != null) {
                        return delegate;
                    }
                }
                return null;
            }
            return null;
        }
        return buildBcelDelegate(ty, jc, false, false);
    }

    public BcelObjectType buildBcelDelegate(ReferenceType type, JavaClass jc, boolean artificial, boolean exposedToWeaver) {
        BcelObjectType ret = new BcelObjectType(type, jc, artificial, exposedToWeaver);
        return ret;
    }

    private JavaClass lookupJavaClass(ClassPathManager classPath, String name) {
        if (classPath == null) {
            try {
                ensureRepositorySetup();
                JavaClass jc = this.delegate.loadClass(name);
                if (trace.isTraceEnabled()) {
                    trace.event("lookupJavaClass", (Object) this, new Object[]{name, jc});
                }
                return jc;
            } catch (ClassNotFoundException e) {
                if (!trace.isTraceEnabled()) {
                    return null;
                }
                trace.error("Unable to find class '" + name + "' in repository", e);
                return null;
            }
        }
        ClassPathManager.ClassFile file = null;
        try {
            try {
                file = classPath.find(UnresolvedType.forName(name));
                if (file == null) {
                    if (file != null) {
                        file.close();
                    }
                    return null;
                }
                ClassParser parser = new ClassParser(file.getInputStream(), file.getPath());
                JavaClass jc2 = parser.parse();
                if (file != null) {
                    file.close();
                }
                return jc2;
            } catch (IOException ioe) {
                if (trace.isTraceEnabled()) {
                    trace.error("IOException whilst processing class", ioe);
                }
                if (file != null) {
                    file.close();
                }
                return null;
            }
        } catch (Throwable th) {
            if (file != null) {
                file.close();
            }
            throw th;
        }
    }

    public BcelObjectType addSourceObjectType(JavaClass jc, boolean artificial) {
        return addSourceObjectType(jc.getClassName(), jc, artificial);
    }

    public BcelObjectType addSourceObjectType(String classname, JavaClass jc, boolean artificial) {
        BcelObjectType ret;
        if (!jc.getClassName().equals(classname)) {
            throw new RuntimeException(jc.getClassName() + "!=" + classname);
        }
        String signature = UnresolvedType.forName(jc.getClassName()).getSignature();
        ResolvedType resolvedTypeFromTypeMap = this.typeMap.get(signature);
        if (resolvedTypeFromTypeMap != null && !(resolvedTypeFromTypeMap instanceof ReferenceType)) {
            StringBuffer exceptionText = new StringBuffer();
            exceptionText.append("Found invalid (not a ReferenceType) entry in the type map. ");
            exceptionText.append("Signature=[" + signature + "] Found=[" + resolvedTypeFromTypeMap + "] Class=[" + resolvedTypeFromTypeMap.getClass() + "]");
            throw new BCException(exceptionText.toString());
        }
        ReferenceType referenceTypeFromTypeMap = (ReferenceType) resolvedTypeFromTypeMap;
        if (referenceTypeFromTypeMap == null) {
            if (jc.isGeneric() && isInJava5Mode()) {
                ReferenceType rawType = ReferenceType.fromTypeX(UnresolvedType.forRawTypeName(jc.getClassName()), this);
                ret = buildBcelDelegate(rawType, jc, artificial, true);
                ReferenceType genericRefType = new ReferenceType(UnresolvedType.forGenericTypeSignature(signature, ret.getDeclaredGenericSignature()), this);
                rawType.setDelegate(ret);
                genericRefType.setDelegate(ret);
                rawType.setGenericType(genericRefType);
                this.typeMap.put(signature, rawType);
            } else {
                ReferenceType referenceTypeFromTypeMap2 = new ReferenceType(signature, this);
                ret = buildBcelDelegate(referenceTypeFromTypeMap2, jc, artificial, true);
                this.typeMap.put(signature, referenceTypeFromTypeMap2);
            }
        } else {
            ret = buildBcelDelegate(referenceTypeFromTypeMap, jc, artificial, true);
        }
        return ret;
    }

    public BcelObjectType addSourceObjectType(String classname, byte[] bytes, boolean artificial) {
        BcelObjectType retval;
        String signature = UnresolvedType.forName(classname).getSignature();
        ResolvedType resolvedTypeFromTypeMap = this.typeMap.get(signature);
        if (resolvedTypeFromTypeMap != null && !(resolvedTypeFromTypeMap instanceof ReferenceType)) {
            StringBuffer exceptionText = new StringBuffer();
            exceptionText.append("Found invalid (not a ReferenceType) entry in the type map. ");
            exceptionText.append("Signature=[" + signature + "] Found=[" + resolvedTypeFromTypeMap + "] Class=[" + resolvedTypeFromTypeMap.getClass() + "]");
            throw new BCException(exceptionText.toString());
        }
        ReferenceType referenceTypeFromTypeMap = (ReferenceType) resolvedTypeFromTypeMap;
        if (referenceTypeFromTypeMap == null) {
            JavaClass jc = Utility.makeJavaClass(classname, bytes);
            if (jc.isGeneric() && isInJava5Mode()) {
                ReferenceType referenceTypeFromTypeMap2 = ReferenceType.fromTypeX(UnresolvedType.forRawTypeName(jc.getClassName()), this);
                retval = buildBcelDelegate(referenceTypeFromTypeMap2, jc, artificial, true);
                ReferenceType genericRefType = new ReferenceType(UnresolvedType.forGenericTypeSignature(signature, retval.getDeclaredGenericSignature()), this);
                referenceTypeFromTypeMap2.setDelegate(retval);
                genericRefType.setDelegate(retval);
                referenceTypeFromTypeMap2.setGenericType(genericRefType);
                this.typeMap.put(signature, referenceTypeFromTypeMap2);
            } else {
                ReferenceType referenceTypeFromTypeMap3 = new ReferenceType(signature, this);
                retval = buildBcelDelegate(referenceTypeFromTypeMap3, jc, artificial, true);
                this.typeMap.put(signature, referenceTypeFromTypeMap3);
            }
        } else {
            ReferenceTypeDelegate existingDelegate = referenceTypeFromTypeMap.getDelegate();
            if (!(existingDelegate instanceof BcelObjectType)) {
                throw new IllegalStateException("For " + classname + " should be BcelObjectType, but is " + existingDelegate.getClass());
            }
            retval = buildBcelDelegate(referenceTypeFromTypeMap, Utility.makeJavaClass(classname, bytes), artificial, true);
        }
        return retval;
    }

    void deleteSourceObjectType(UnresolvedType ty) {
        this.typeMap.remove(ty.getSignature());
    }

    public static Member makeFieldJoinPointSignature(LazyClassGen cg, FieldInstruction fi) {
        ConstantPool cpg = cg.getConstantPool();
        return MemberImpl.field(fi.getClassName(cpg), (fi.opcode == 178 || fi.opcode == 179) ? 8 : 0, fi.getName(cpg), fi.getSignature(cpg));
    }

    public Member makeJoinPointSignatureFromMethod(LazyMethodGen mg, MemberKind kind) {
        Member ret = mg.getMemberView();
        if (ret == null) {
            int mods = mg.getAccessFlags();
            if (mg.getEnclosingClass().isInterface()) {
                mods |= 512;
            }
            return new ResolvedMemberImpl(kind, UnresolvedType.forName(mg.getClassName()), mods, fromBcel(mg.getReturnType()), mg.getName(), fromBcel(mg.getArgumentTypes()));
        }
        return ret;
    }

    public Member makeJoinPointSignatureForMonitorEnter(LazyClassGen cg, InstructionHandle h) {
        return MemberImpl.monitorEnter();
    }

    public Member makeJoinPointSignatureForMonitorExit(LazyClassGen cg, InstructionHandle h) {
        return MemberImpl.monitorExit();
    }

    public Member makeJoinPointSignatureForArrayConstruction(LazyClassGen cg, InstructionHandle handle) {
        Member retval;
        UnresolvedType ut;
        Instruction i = handle.getInstruction();
        ConstantPool cpg = cg.getConstantPool();
        if (i.opcode == 189) {
            UnresolvedType ut2 = fromBcel(i.getType(cpg));
            retval = MemberImpl.method(UnresolvedType.makeArray(ut2, 1), 1, UnresolvedType.VOID, "<init>", new ResolvedType[]{this.INT});
        } else if (i instanceof MULTIANEWARRAY) {
            MULTIANEWARRAY arrayInstruction = (MULTIANEWARRAY) i;
            int dimensions = arrayInstruction.getDimensions();
            ObjectType ot = arrayInstruction.getLoadClassType(cpg);
            if (ot != null) {
                UnresolvedType ut3 = fromBcel(ot);
                ut = UnresolvedType.makeArray(ut3, dimensions);
            } else {
                Type t = arrayInstruction.getType(cpg);
                ut = fromBcel(t);
            }
            ResolvedType[] parms = new ResolvedType[dimensions];
            for (int ii = 0; ii < dimensions; ii++) {
                parms[ii] = this.INT;
            }
            retval = MemberImpl.method(ut, 1, UnresolvedType.VOID, "<init>", parms);
        } else if (i.opcode == 188) {
            UnresolvedType ut4 = fromBcel(i.getType());
            retval = MemberImpl.method(ut4, 1, UnresolvedType.VOID, "<init>", new ResolvedType[]{this.INT});
        } else {
            throw new BCException("Cannot create array construction signature for this non-array instruction:" + i);
        }
        return retval;
    }

    public Member makeJoinPointSignatureForMethodInvocation(LazyClassGen cg, InvokeInstruction ii) {
        int i;
        ConstantPool cpg = cg.getConstantPool();
        String name = ii.getName(cpg);
        String declaring = ii.getClassName(cpg);
        UnresolvedType declaringType = null;
        String signature = ii.getSignature(cpg);
        if (name.startsWith("ajc$privMethod$")) {
            try {
                declaringType = UnresolvedType.forName(declaring);
                String typeNameAsFoundInAccessorName = declaringType.getName().replace('.', '_');
                int indexInAccessorName = name.lastIndexOf(typeNameAsFoundInAccessorName);
                if (indexInAccessorName != -1) {
                    String methodName = name.substring(indexInAccessorName + typeNameAsFoundInAccessorName.length() + 1);
                    ResolvedType resolvedDeclaringType = declaringType.resolve(this);
                    ResolvedMember[] methods = resolvedDeclaringType.getDeclaredMethods();
                    for (ResolvedMember method : methods) {
                        if (method.getName().equals(methodName) && method.getSignature().equals(signature) && Modifier.isPrivate(method.getModifiers())) {
                            return method;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (ii instanceof INVOKEINTERFACE) {
            i = 512;
        } else if (ii.opcode == 184) {
            i = 8;
        } else {
            i = (ii.opcode != 183 || name.equals("<init>")) ? 0 : 2;
        }
        int modifier = i;
        if (ii.opcode == 184) {
            ResolvedType appearsDeclaredBy = resolve(declaring);
            Iterator<ResolvedMember> iterator = appearsDeclaredBy.getMethods(true, true);
            while (true) {
                if (!iterator.hasNext()) {
                    break;
                }
                ResolvedMember method2 = iterator.next();
                if (Modifier.isStatic(method2.getModifiers()) && name.equals(method2.getName()) && signature.equals(method2.getSignature())) {
                    declaringType = method2.getDeclaringType();
                    break;
                }
            }
        }
        if (declaringType == null) {
            if (declaring.charAt(0) == '[') {
                declaringType = UnresolvedType.forSignature(declaring);
            } else {
                declaringType = UnresolvedType.forName(declaring);
            }
        }
        return MemberImpl.method(declaringType, modifier, name, signature);
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("BcelWorld(");
        buf.append(")");
        return buf.toString();
    }

    public static BcelObjectType getBcelObjectType(ResolvedType concreteAspect) {
        if (concreteAspect == null || !(concreteAspect instanceof ReferenceType)) {
            return null;
        }
        ReferenceTypeDelegate rtDelegate = ((ReferenceType) concreteAspect).getDelegate();
        if (rtDelegate instanceof BcelObjectType) {
            return (BcelObjectType) rtDelegate;
        }
        return null;
    }

    public void tidyUp() throws AbortException {
        this.classPath.closeArchives();
        this.typeMap.report();
        this.typeMap.demote(true);
    }

    @Override // org.aspectj.apache.bcel.util.Repository
    public JavaClass findClass(String className) {
        return lookupJavaClass(this.classPath, className);
    }

    @Override // org.aspectj.apache.bcel.util.Repository
    public JavaClass loadClass(String className) throws ClassNotFoundException {
        return lookupJavaClass(this.classPath, className);
    }

    @Override // org.aspectj.apache.bcel.util.Repository
    public void storeClass(JavaClass clazz) {
    }

    @Override // org.aspectj.apache.bcel.util.Repository
    public void removeClass(JavaClass clazz) {
        throw new RuntimeException("Not implemented");
    }

    @Override // org.aspectj.apache.bcel.util.Repository
    public JavaClass loadClass(Class clazz) throws ClassNotFoundException {
        throw new RuntimeException("Not implemented");
    }

    @Override // org.aspectj.apache.bcel.util.Repository
    public void clear() {
        this.delegate.clear();
    }

    @Override // org.aspectj.weaver.World
    public void validateType(UnresolvedType type) {
        ResolvedType result = this.typeMap.get(type.getSignature());
        if (result == null || !result.isExposedToWeaver()) {
            return;
        }
        result.ensureConsistent();
    }

    private boolean applyDeclareParents(DeclareParents p, ResolvedType onType) throws AbortException {
        boolean didSomething = false;
        List<ResolvedType> newParents = p.findMatchingNewParents(onType, true);
        if (!newParents.isEmpty()) {
            didSomething = true;
            getBcelObjectType(onType);
            for (ResolvedType newParent : newParents) {
                onType.addParent(newParent);
                ResolvedTypeMunger newParentMunger = new NewParentTypeMunger(newParent, p.getDeclaringType());
                newParentMunger.setSourceLocation(p.getSourceLocation());
                onType.addInterTypeMunger(new BcelTypeMunger(newParentMunger, getCrosscuttingMembersSet().findAspectDeclaringParents(p)), false);
            }
        }
        return didSomething;
    }

    private boolean applyDeclareAtType(DeclareAnnotation decA, ResolvedType onType, boolean reportProblems) throws AbortException {
        boolean didSomething = false;
        if (decA.matches(onType)) {
            if (onType.hasAnnotation(decA.getAnnotation().getType())) {
                return false;
            }
            AnnotationAJ annoX = decA.getAnnotation();
            boolean isOK = checkTargetOK(decA, onType, annoX);
            if (isOK) {
                didSomething = true;
                ResolvedTypeMunger newAnnotationTM = new AnnotationOnTypeMunger(annoX);
                newAnnotationTM.setSourceLocation(decA.getSourceLocation());
                onType.addInterTypeMunger(new BcelTypeMunger(newAnnotationTM, decA.getAspect().resolve(this)), false);
                decA.copyAnnotationTo(onType);
            }
        }
        return didSomething;
    }

    private boolean applyDeclareAtField(DeclareAnnotation deca, ResolvedType type) {
        boolean changedType = false;
        ResolvedMember[] fields = type.getDeclaredFields();
        for (ResolvedMember field : fields) {
            if (deca.matches(field, this)) {
                AnnotationAJ anno = deca.getAnnotation();
                if (!field.hasAnnotation(anno.getType())) {
                    field.addAnnotation(anno);
                    changedType = true;
                }
            }
        }
        return changedType;
    }

    private boolean checkTargetOK(DeclareAnnotation decA, ResolvedType onType, AnnotationAJ annoX) {
        if (annoX.specifiesTarget()) {
            if ((onType.isAnnotation() && !annoX.allowedOnAnnotationType()) || !annoX.allowedOnRegularType()) {
                return false;
            }
            return true;
        }
        return true;
    }

    protected void weaveInterTypeDeclarations(ResolvedType onType) throws AbortException {
        List<DeclareParents> declareParentsList = getCrosscuttingMembersSet().getDeclareParents();
        if (onType.isRawType()) {
            onType = onType.getGenericType();
        }
        onType.clearInterTypeMungers();
        List<DeclareParents> decpToRepeat = new ArrayList<>();
        boolean aParentChangeOccurred = false;
        boolean anAnnotationChangeOccurred = false;
        for (DeclareParents decp : declareParentsList) {
            boolean typeChanged = applyDeclareParents(decp, onType);
            if (typeChanged) {
                aParentChangeOccurred = true;
            } else if (!decp.getChild().isStarAnnotation()) {
                decpToRepeat.add(decp);
            }
        }
        for (DeclareAnnotation decA : getCrosscuttingMembersSet().getDeclareAnnotationOnTypes()) {
            boolean typeChanged2 = applyDeclareAtType(decA, onType, true);
            if (typeChanged2) {
                anAnnotationChangeOccurred = true;
            }
        }
        for (DeclareAnnotation deca : getCrosscuttingMembersSet().getDeclareAnnotationOnFields()) {
            if (applyDeclareAtField(deca, onType)) {
                anAnnotationChangeOccurred = true;
            }
        }
        while (true) {
            if ((aParentChangeOccurred || anAnnotationChangeOccurred) && !decpToRepeat.isEmpty()) {
                aParentChangeOccurred = false;
                anAnnotationChangeOccurred = false;
                List<DeclareParents> decpToRepeatNextTime = new ArrayList<>();
                for (DeclareParents decp2 : decpToRepeat) {
                    if (applyDeclareParents(decp2, onType)) {
                        aParentChangeOccurred = true;
                    } else {
                        decpToRepeatNextTime.add(decp2);
                    }
                }
                for (DeclareAnnotation deca2 : getCrosscuttingMembersSet().getDeclareAnnotationOnTypes()) {
                    if (applyDeclareAtType(deca2, onType, false)) {
                        anAnnotationChangeOccurred = true;
                    }
                }
                for (DeclareAnnotation deca3 : getCrosscuttingMembersSet().getDeclareAnnotationOnFields()) {
                    if (applyDeclareAtField(deca3, onType)) {
                        anAnnotationChangeOccurred = true;
                    }
                }
                decpToRepeat = decpToRepeatNextTime;
            } else {
                return;
            }
        }
    }

    @Override // org.aspectj.weaver.World
    public IWeavingSupport getWeavingSupport() {
        return this.bcelWeavingSupport;
    }

    @Override // org.aspectj.weaver.World
    public void reportCheckerMatch(Checker checker, Shadow shadow) throws AbortException {
        IMessage iMessage = new Message(checker.getMessage(shadow), shadow.toString(), checker.isError() ? IMessage.ERROR : IMessage.WARNING, shadow.getSourceLocation(), null, new ISourceLocation[]{checker.getSourceLocation()}, true, 0, -1, -1);
        getMessageHandler().handleMessage(iMessage);
        if (getCrossReferenceHandler() != null) {
            getCrossReferenceHandler().addCrossReference(checker.getSourceLocation(), shadow.getSourceLocation(), checker.isError() ? IRelationship.Kind.DECLARE_ERROR.getName() : IRelationship.Kind.DECLARE_WARNING.getName(), false);
        }
        if (getModel() != null) {
            AsmRelationshipProvider.addDeclareErrorOrWarningRelationship(getModelAsAsmManager(), shadow, checker);
        }
    }

    public AsmManager getModelAsAsmManager() {
        return (AsmManager) getModel();
    }

    void raiseError(String message) throws AbortException {
        getMessageHandler().handleMessage(MessageUtil.error(message));
    }

    public void setXmlFiles(List<File> xmlFiles) throws AbortException {
        if (!this.isXmlConfiguredWorld && !xmlFiles.isEmpty()) {
            raiseError("xml configuration files only supported by the compiler when -xmlConfigured option specified");
            return;
        }
        if (!xmlFiles.isEmpty()) {
            this.xmlConfiguration = new WeavingXmlConfig(this, 1);
        }
        for (File xmlfile : xmlFiles) {
            try {
                Definition d = DocumentParser.parse(xmlfile.toURI().toURL());
                this.xmlConfiguration.add(d);
            } catch (MalformedURLException e) {
                raiseError("Unexpected problem processing XML config file '" + xmlfile.getName() + "' :" + e.getMessage());
            } catch (Exception e2) {
                raiseError("Unexpected problem processing XML config file '" + xmlfile.getName() + "' :" + e2.getMessage());
            }
        }
    }

    public void addScopedAspect(String name, String scope) {
        this.isXmlConfiguredWorld = true;
        if (this.xmlConfiguration == null) {
            this.xmlConfiguration = new WeavingXmlConfig(this, 2);
        }
        this.xmlConfiguration.addScopedAspect(name, scope);
    }

    public void setXmlConfigured(boolean b) {
        this.isXmlConfiguredWorld = b;
    }

    @Override // org.aspectj.weaver.World
    public boolean isXmlConfigured() {
        return this.isXmlConfiguredWorld && this.xmlConfiguration != null;
    }

    public WeavingXmlConfig getXmlConfiguration() {
        return this.xmlConfiguration;
    }

    @Override // org.aspectj.weaver.World
    public boolean isAspectIncluded(ResolvedType aspectType) {
        if (!isXmlConfigured()) {
            return true;
        }
        return this.xmlConfiguration.specifiesInclusionOfAspect(aspectType.getName());
    }

    @Override // org.aspectj.weaver.World
    public TypePattern getAspectScope(ResolvedType declaringType) {
        return this.xmlConfiguration.getScopeFor(declaringType.getName());
    }

    @Override // org.aspectj.weaver.World
    public boolean hasUnsatisfiedDependency(ResolvedType aspectType) throws AbortException {
        String aspectName = aspectType.getName();
        if (aspectType.hasAnnotations()) {
            AnnotationAJ[] annos = aspectType.getAnnotations();
            for (AnnotationAJ anno : annos) {
                if (anno.getTypeName().equals("org.aspectj.lang.annotation.RequiredTypes")) {
                    String values = anno.getStringFormOfValue("value");
                    if (values != null && values.length() > 2) {
                        StringTokenizer tokenizer = new StringTokenizer(values.substring(1, values.length() - 1), ",");
                        boolean anythingMissing = false;
                        while (tokenizer.hasMoreElements()) {
                            String requiredTypeName = tokenizer.nextToken();
                            ResolvedType rt = resolve(UnresolvedType.forName(requiredTypeName));
                            if (rt.isMissing()) {
                                if (!getMessageHandler().isIgnoring(IMessage.INFO)) {
                                    getMessageHandler().handleMessage(MessageUtil.info("deactivating aspect '" + aspectName + "' as it requires type '" + requiredTypeName + "' which cannot be found on the classpath"));
                                }
                                anythingMissing = true;
                                if (this.aspectRequiredTypes == null) {
                                    this.aspectRequiredTypes = new HashMap();
                                }
                                this.aspectRequiredTypes.put(aspectName, requiredTypeName);
                            }
                        }
                        if (anythingMissing) {
                            return true;
                        }
                        return false;
                    }
                    return false;
                }
            }
        }
        if (this.aspectRequiredTypes == null) {
            return false;
        }
        if (!this.aspectRequiredTypesProcessed.contains(aspectName)) {
            String requiredTypeName2 = this.aspectRequiredTypes.get(aspectName);
            if (requiredTypeName2 == null) {
                this.aspectRequiredTypesProcessed.add(aspectName);
                return false;
            }
            ResolvedType rt2 = resolve(UnresolvedType.forName(requiredTypeName2));
            if (!rt2.isMissing()) {
                this.aspectRequiredTypesProcessed.add(aspectName);
                this.aspectRequiredTypes.remove(aspectName);
                return false;
            }
            if (!getMessageHandler().isIgnoring(IMessage.INFO)) {
                getMessageHandler().handleMessage(MessageUtil.info("deactivating aspect '" + aspectName + "' as it requires type '" + requiredTypeName2 + "' which cannot be found on the classpath"));
            }
            this.aspectRequiredTypesProcessed.add(aspectName);
            return true;
        }
        return this.aspectRequiredTypes.containsKey(aspectName);
    }

    public void addAspectRequires(String aspectClassName, String requiredType) {
        if (this.aspectRequiredTypes == null) {
            this.aspectRequiredTypes = new HashMap();
        }
        this.aspectRequiredTypes.put(aspectClassName, requiredType);
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/BcelWorld$WeavingXmlConfig.class */
    static class WeavingXmlConfig {
        static final int MODE_COMPILE = 1;
        static final int MODE_LTW = 2;
        private int mode;
        private boolean initialized = false;
        private List<Definition> definitions = new ArrayList();
        private List<String> resolvedIncludedAspects = new ArrayList();
        private Map<String, TypePattern> scopes = new HashMap();
        private List<String> includedFastMatchPatterns = Collections.emptyList();
        private List<TypePattern> includedPatterns = Collections.emptyList();
        private List<String> excludedFastMatchPatterns = Collections.emptyList();
        private List<TypePattern> excludedPatterns = Collections.emptyList();
        private BcelWorld world;

        public WeavingXmlConfig(BcelWorld bcelWorld, int mode) {
            this.world = bcelWorld;
            this.mode = mode;
        }

        public void add(Definition d) {
            this.definitions.add(d);
        }

        public void addScopedAspect(String aspectName, String scope) throws AbortException {
            ensureInitialized();
            this.resolvedIncludedAspects.add(aspectName);
            try {
                TypePattern scopePattern = new PatternParser(scope).parseTypePattern();
                scopePattern.resolve(this.world);
                this.scopes.put(aspectName, scopePattern);
                if (!this.world.getMessageHandler().isIgnoring(IMessage.INFO)) {
                    this.world.getMessageHandler().handleMessage(MessageUtil.info("Aspect '" + aspectName + "' is scoped to apply against types matching pattern '" + scopePattern.toString() + "'"));
                }
            } catch (Exception e) {
                this.world.getMessageHandler().handleMessage(MessageUtil.error("Unable to parse scope as type pattern.  Scope was '" + scope + "': " + e.getMessage()));
            }
        }

        public void ensureInitialized() {
            if (!this.initialized) {
                try {
                    this.resolvedIncludedAspects = new ArrayList();
                    for (Definition definition : this.definitions) {
                        List<String> aspectNames = definition.getAspectClassNames();
                        for (String name : aspectNames) {
                            this.resolvedIncludedAspects.add(name);
                            String scope = definition.getScopeForAspect(name);
                            if (scope != null) {
                                try {
                                    TypePattern scopePattern = new PatternParser(scope).parseTypePattern();
                                    scopePattern.resolve(this.world);
                                    this.scopes.put(name, scopePattern);
                                    if (!this.world.getMessageHandler().isIgnoring(IMessage.INFO)) {
                                        this.world.getMessageHandler().handleMessage(MessageUtil.info("Aspect '" + name + "' is scoped to apply against types matching pattern '" + scopePattern.toString() + "'"));
                                    }
                                } catch (Exception e) {
                                    this.world.getMessageHandler().handleMessage(MessageUtil.error("Unable to parse scope as type pattern.  Scope was '" + scope + "': " + e.getMessage()));
                                }
                            }
                        }
                        try {
                            List<String> includePatterns = definition.getIncludePatterns();
                            if (includePatterns.size() > 0) {
                                this.includedPatterns = new ArrayList();
                                this.includedFastMatchPatterns = new ArrayList();
                            }
                            for (String includePattern : includePatterns) {
                                if (includePattern.endsWith("..*")) {
                                    this.includedFastMatchPatterns.add(includePattern.substring(0, includePattern.length() - 2));
                                } else {
                                    TypePattern includedPattern = new PatternParser(includePattern).parseTypePattern();
                                    this.includedPatterns.add(includedPattern);
                                }
                            }
                            List<String> excludePatterns = definition.getExcludePatterns();
                            if (excludePatterns.size() > 0) {
                                this.excludedPatterns = new ArrayList();
                                this.excludedFastMatchPatterns = new ArrayList();
                            }
                            for (String excludePattern : excludePatterns) {
                                if (excludePattern.endsWith("..*")) {
                                    this.excludedFastMatchPatterns.add(excludePattern.substring(0, excludePattern.length() - 2));
                                } else {
                                    TypePattern excludedPattern = new PatternParser(excludePattern).parseTypePattern();
                                    this.excludedPatterns.add(excludedPattern);
                                }
                            }
                        } catch (ParserException pe) {
                            this.world.getMessageHandler().handleMessage(MessageUtil.error("Unable to parse type pattern: " + pe.getMessage()));
                        }
                    }
                } finally {
                    this.initialized = true;
                }
            }
        }

        public boolean specifiesInclusionOfAspect(String name) {
            ensureInitialized();
            return this.resolvedIncludedAspects.contains(name);
        }

        public TypePattern getScopeFor(String name) {
            return this.scopes.get(name);
        }

        public boolean excludesType(ResolvedType type) {
            if (this.mode == 2) {
                return false;
            }
            String typename = type.getName();
            boolean excluded = false;
            Iterator<String> it = this.excludedFastMatchPatterns.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                String excludedPattern = it.next();
                if (typename.startsWith(excludedPattern)) {
                    excluded = true;
                    break;
                }
            }
            if (!excluded) {
                Iterator<TypePattern> it2 = this.excludedPatterns.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    TypePattern excludedPattern2 = it2.next();
                    if (excludedPattern2.matchesStatically(type)) {
                        excluded = true;
                        break;
                    }
                }
            }
            return excluded;
        }
    }

    @Override // org.aspectj.weaver.World
    public World.TypeMap getTypeMap() {
        return this.typeMap;
    }

    @Override // org.aspectj.weaver.World
    public boolean isLoadtimeWeaving() {
        return false;
    }

    public void addTypeDelegateResolver(TypeDelegateResolver typeDelegateResolver) {
        if (this.typeDelegateResolvers == null) {
            this.typeDelegateResolvers = new ArrayList();
        }
        this.typeDelegateResolvers.add(typeDelegateResolver);
    }

    @Override // org.aspectj.weaver.World
    public void classWriteEvent(char[][] compoundName) {
        this.typeMap.classWriteEvent(new String(CharOperation.concatWith(compoundName, '.')));
    }

    public void demote(ResolvedType type) {
        this.typeMap.demote(type);
    }
}
