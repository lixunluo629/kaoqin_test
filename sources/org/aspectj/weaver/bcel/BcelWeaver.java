package org.aspectj.weaver.bcel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.aspectj.apache.bcel.classfile.ClassFormatException;
import org.aspectj.apache.bcel.classfile.ClassParser;
import org.aspectj.apache.bcel.classfile.JavaClass;
import org.aspectj.asm.AsmManager;
import org.aspectj.asm.IProgramElement;
import org.aspectj.asm.internal.AspectJElementHierarchy;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.Message;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.bridge.SourceLocation;
import org.aspectj.bridge.WeaveMessage;
import org.aspectj.bridge.context.CompilationAndWeavingContext;
import org.aspectj.bridge.context.ContextToken;
import org.aspectj.util.FileUtil;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.Advice;
import org.aspectj.weaver.AdviceKind;
import org.aspectj.weaver.AnnotationAJ;
import org.aspectj.weaver.AnnotationOnTypeMunger;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ConcreteTypeMunger;
import org.aspectj.weaver.CrosscuttingMembersSet;
import org.aspectj.weaver.CustomMungerFactory;
import org.aspectj.weaver.IClassFileProvider;
import org.aspectj.weaver.IUnwovenClassFile;
import org.aspectj.weaver.IWeaveRequestor;
import org.aspectj.weaver.NewParentTypeMunger;
import org.aspectj.weaver.ReferenceType;
import org.aspectj.weaver.ReferenceTypeDelegate;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.ResolvedTypeMunger;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.ShadowMunger;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.WeaverStateInfo;
import org.aspectj.weaver.World;
import org.aspectj.weaver.bcel.UnwovenClassFile;
import org.aspectj.weaver.model.AsmRelationshipProvider;
import org.aspectj.weaver.patterns.AndPointcut;
import org.aspectj.weaver.patterns.BindingPattern;
import org.aspectj.weaver.patterns.BindingTypePattern;
import org.aspectj.weaver.patterns.ConcreteCflowPointcut;
import org.aspectj.weaver.patterns.DeclareAnnotation;
import org.aspectj.weaver.patterns.DeclareParents;
import org.aspectj.weaver.patterns.DeclareTypeErrorOrWarning;
import org.aspectj.weaver.patterns.FastMatchInfo;
import org.aspectj.weaver.patterns.IfPointcut;
import org.aspectj.weaver.patterns.KindedPointcut;
import org.aspectj.weaver.patterns.NameBindingPointcut;
import org.aspectj.weaver.patterns.NotPointcut;
import org.aspectj.weaver.patterns.OrPointcut;
import org.aspectj.weaver.patterns.Pointcut;
import org.aspectj.weaver.patterns.PointcutRewriter;
import org.aspectj.weaver.patterns.WithinPointcut;
import org.aspectj.weaver.tools.Trace;
import org.aspectj.weaver.tools.TraceFactory;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.util.ClassUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/BcelWeaver.class */
public class BcelWeaver {
    public static final String CLOSURE_CLASS_PREFIX = "$Ajc";
    public static final String SYNTHETIC_CLASS_POSTFIX = "$ajc";
    private static Trace trace = TraceFactory.getTraceFactory().getTrace(BcelWeaver.class);
    private final transient BcelWorld world;
    private final CrosscuttingMembersSet xcutSet;
    private ZipOutputStream zipOutputStream;
    private CustomMungerFactory customMungerFactory;
    private boolean inReweavableMode = false;
    private transient List<UnwovenClassFile> addedClasses = new ArrayList();
    private transient List<String> deletedTypenames = new ArrayList();
    private transient List<ShadowMunger> shadowMungerList = null;
    private transient List<ConcreteTypeMunger> typeMungerList = null;
    private transient List<ConcreteTypeMunger> lateTypeMungerList = null;
    private transient List<DeclareParents> declareParentsList = null;
    private Manifest manifest = null;
    private boolean needToReweaveWorld = false;
    private boolean isBatchWeave = true;
    private Set<IProgramElement> candidatesForRemoval = null;

    public BcelWeaver(BcelWorld world) {
        if (trace.isTraceEnabled()) {
            trace.enter("<init>", this, world);
        }
        this.world = world;
        this.xcutSet = world.getCrosscuttingMembersSet();
        if (trace.isTraceEnabled()) {
            trace.exit("<init>");
        }
    }

    public ResolvedType addLibraryAspect(String aspectName) {
        if (trace.isTraceEnabled()) {
            trace.enter("addLibraryAspect", this, aspectName);
        }
        UnresolvedType unresolvedT = UnresolvedType.forName(aspectName);
        unresolvedT.setNeedsModifiableDelegate(true);
        ResolvedType type = this.world.resolve(unresolvedT, true);
        if (type.isMissing()) {
            String fixedName = aspectName;
            int hasDot = fixedName.lastIndexOf(46);
            while (hasDot > 0) {
                char[] fixedNameChars = fixedName.toCharArray();
                fixedNameChars[hasDot] = '$';
                fixedName = new String(fixedNameChars);
                hasDot = fixedName.lastIndexOf(46);
                UnresolvedType ut = UnresolvedType.forName(fixedName);
                ut.setNeedsModifiableDelegate(true);
                type = this.world.resolve(ut, true);
                if (!type.isMissing()) {
                    break;
                }
            }
        }
        if (type.isAspect()) {
            WeaverStateInfo wsi = type.getWeaverState();
            if (wsi != null && wsi.isReweavable()) {
                BcelObjectType classType = getClassType(type.getName());
                JavaClass wovenJavaClass = classType.getJavaClass();
                byte[] bytes = wsi.getUnwovenClassFileData(wovenJavaClass.getBytes());
                JavaClass unwovenJavaClass = Utility.makeJavaClass(wovenJavaClass.getFileName(), bytes);
                this.world.storeClass(unwovenJavaClass);
                classType.setJavaClass(unwovenJavaClass, true);
            }
            this.xcutSet.addOrReplaceAspect(type);
            if (trace.isTraceEnabled()) {
                trace.exit("addLibraryAspect", type);
            }
            if (type.getSuperclass().isAspect()) {
                addLibraryAspect(type.getSuperclass().getName());
            }
            return type;
        }
        if (type.isMissing()) {
            IMessage message = new Message("The specified aspect '" + aspectName + "' cannot be found", null, true);
            this.world.getMessageHandler().handleMessage(message);
            return null;
        }
        IMessage message2 = new Message("Cannot register '" + aspectName + "' because the type found with that name is not an aspect", null, true);
        this.world.getMessageHandler().handleMessage(message2);
        return null;
    }

    public void addLibraryJarFile(File inFile) throws IOException {
        List<ResolvedType> addedAspects;
        if (inFile.isDirectory()) {
            addedAspects = addAspectsFromDirectory(inFile);
        } else {
            addedAspects = addAspectsFromJarFile(inFile);
        }
        for (ResolvedType addedAspect : addedAspects) {
            this.xcutSet.addOrReplaceAspect(addedAspect);
        }
    }

    private List<ResolvedType> addAspectsFromJarFile(File inFile) throws IOException {
        ZipInputStream inStream = new ZipInputStream(new FileInputStream(inFile));
        List<ResolvedType> addedAspects = new ArrayList<>();
        while (true) {
            try {
                ZipEntry entry = inStream.getNextEntry();
                if (entry != null) {
                    if (!entry.isDirectory() && entry.getName().endsWith(ClassUtils.CLASS_FILE_SUFFIX)) {
                        ClassParser parser = new ClassParser(new ByteArrayInputStream(FileUtil.readAsByteArray(inStream)), entry.getName());
                        JavaClass jc = parser.parse();
                        inStream.closeEntry();
                        ResolvedType type = this.world.addSourceObjectType(jc, false).getResolvedTypeX();
                        type.setBinaryPath(inFile.getAbsolutePath());
                        if (type.isAspect()) {
                            addedAspects.add(type);
                        } else {
                            this.world.demote(type);
                        }
                    }
                } else {
                    return addedAspects;
                }
            } finally {
                inStream.close();
            }
        }
    }

    private List<ResolvedType> addAspectsFromDirectory(File directory) throws IOException, ClassFormatException {
        List<ResolvedType> addedAspects = new ArrayList<>();
        File[] classFiles = FileUtil.listFiles(directory, new FileFilter() { // from class: org.aspectj.weaver.bcel.BcelWeaver.1
            @Override // java.io.FileFilter
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(ClassUtils.CLASS_FILE_SUFFIX);
            }
        });
        for (File classFile : classFiles) {
            FileInputStream fis = new FileInputStream(classFile);
            byte[] classBytes = FileUtil.readAsByteArray(fis);
            ResolvedType aspectType = isAspect(classBytes, classFile.getAbsolutePath(), directory);
            if (aspectType != null) {
                addedAspects.add(aspectType);
            }
            fis.close();
        }
        return addedAspects;
    }

    private ResolvedType isAspect(byte[] classbytes, String name, File dir) throws IOException, ClassFormatException {
        String binaryPath;
        ClassParser parser = new ClassParser(new ByteArrayInputStream(classbytes), name);
        JavaClass jc = parser.parse();
        ResolvedType type = this.world.addSourceObjectType(jc, false).getResolvedTypeX();
        String typeName = type.getName().replace('.', File.separatorChar);
        int end = name.lastIndexOf(typeName + ClassUtils.CLASS_FILE_SUFFIX);
        if (end == -1) {
            binaryPath = dir.getAbsolutePath();
        } else {
            binaryPath = name.substring(0, end - 1);
        }
        type.setBinaryPath(binaryPath);
        if (type.isAspect()) {
            return type;
        }
        this.world.demote(type);
        return null;
    }

    public List<UnwovenClassFile> addDirectoryContents(File inFile, File outDir) throws IOException {
        List<UnwovenClassFile> addedClassFiles = new ArrayList<>();
        File[] files = FileUtil.listFiles(inFile, new FileFilter() { // from class: org.aspectj.weaver.bcel.BcelWeaver.2
            @Override // java.io.FileFilter
            public boolean accept(File f) {
                boolean accept = !f.isDirectory();
                return accept;
            }
        });
        for (File file : files) {
            addedClassFiles.add(addClassFile(file, inFile, outDir));
        }
        return addedClassFiles;
    }

    public List<UnwovenClassFile> addJarFile(File inFile, File outDir, boolean canBeDirectory) throws AbortException {
        List<UnwovenClassFile> addedClassFiles = new ArrayList<>();
        this.needToReweaveWorld = true;
        JarFile inJar = null;
        try {
            try {
                try {
                    if (inFile.isDirectory() && canBeDirectory) {
                        addedClassFiles.addAll(addDirectoryContents(inFile, outDir));
                    } else {
                        inJar = new JarFile(inFile);
                        try {
                            addManifest(inJar.getManifest());
                            Enumeration entries = inJar.entries();
                            while (entries.hasMoreElements()) {
                                JarEntry entry = entries.nextElement();
                                String filename = entry.getName();
                                String filenameLowercase = filename.toLowerCase();
                                if (!filenameLowercase.startsWith("meta-inf") && !filenameLowercase.endsWith("module-info.class")) {
                                    InputStream inStream = inJar.getInputStream(entry);
                                    byte[] bytes = FileUtil.readAsByteArray(inStream);
                                    UnwovenClassFile classFile = new UnwovenClassFile(new File(outDir, filename).getAbsolutePath(), bytes);
                                    if (filenameLowercase.endsWith(ClassUtils.CLASS_FILE_SUFFIX)) {
                                        ReferenceType type = addClassFile(classFile, false);
                                        StringBuffer sb = new StringBuffer();
                                        sb.append(inFile.getAbsolutePath());
                                        sb.append("!");
                                        sb.append(entry.getName());
                                        type.setBinaryPath(sb.toString());
                                        addedClassFiles.add(classFile);
                                    }
                                    inStream.close();
                                }
                            }
                            inJar.close();
                        } finally {
                            inJar.close();
                        }
                    }
                    if (inJar != null) {
                        try {
                            inJar.close();
                        } catch (IOException ex) {
                            IMessage message = new Message("Could not close input jar file " + inFile.getPath() + "(" + ex.getMessage() + ")", new SourceLocation(inFile, 0), true);
                            this.world.getMessageHandler().handleMessage(message);
                        }
                    }
                } catch (IOException ex2) {
                    IMessage message2 = new Message("Could not read input jar file " + inFile.getPath() + "(" + ex2.getMessage() + ")", new SourceLocation(inFile, 0), true);
                    this.world.getMessageHandler().handleMessage(message2);
                    if (0 != 0) {
                        try {
                            inJar.close();
                        } catch (IOException ex3) {
                            IMessage message3 = new Message("Could not close input jar file " + inFile.getPath() + "(" + ex3.getMessage() + ")", new SourceLocation(inFile, 0), true);
                            this.world.getMessageHandler().handleMessage(message3);
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                IMessage message4 = new Message("Could not find input jar file " + inFile.getPath() + ", ignoring", new SourceLocation(inFile, 0), false);
                this.world.getMessageHandler().handleMessage(message4);
                if (0 != 0) {
                    try {
                        inJar.close();
                    } catch (IOException ex4) {
                        IMessage message5 = new Message("Could not close input jar file " + inFile.getPath() + "(" + ex4.getMessage() + ")", new SourceLocation(inFile, 0), true);
                        this.world.getMessageHandler().handleMessage(message5);
                    }
                }
            }
            return addedClassFiles;
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    inJar.close();
                } catch (IOException ex5) {
                    IMessage message6 = new Message("Could not close input jar file " + inFile.getPath() + "(" + ex5.getMessage() + ")", new SourceLocation(inFile, 0), true);
                    this.world.getMessageHandler().handleMessage(message6);
                }
            }
            throw th;
        }
    }

    public boolean needToReweaveWorld() {
        return this.needToReweaveWorld;
    }

    public ReferenceType addClassFile(UnwovenClassFile classFile, boolean fromInpath) {
        this.addedClasses.add(classFile);
        ReferenceType type = this.world.addSourceObjectType(classFile.getJavaClass(), false).getResolvedTypeX();
        if (fromInpath) {
            type.setBinaryPath(classFile.getFilename());
        }
        return type;
    }

    public UnwovenClassFile addClassFile(File classFile, File inPathDir, File outDir) throws IOException {
        FileInputStream fis = new FileInputStream(classFile);
        byte[] bytes = FileUtil.readAsByteArray(fis);
        String filename = classFile.getAbsolutePath().substring(inPathDir.getAbsolutePath().length() + 1);
        UnwovenClassFile ucf = new UnwovenClassFile(new File(outDir, filename).getAbsolutePath(), bytes);
        if (filename.endsWith(ClassUtils.CLASS_FILE_SUFFIX)) {
            StringBuffer sb = new StringBuffer();
            sb.append(inPathDir.getAbsolutePath());
            sb.append("!");
            sb.append(filename);
            ReferenceType type = addClassFile(ucf, false);
            type.setBinaryPath(sb.toString());
        }
        fis.close();
        return ucf;
    }

    public void deleteClassFile(String typename) {
        this.deletedTypenames.add(typename);
        this.world.deleteSourceObjectType(UnresolvedType.forName(typename));
    }

    public void setIsBatchWeave(boolean b) {
        this.isBatchWeave = b;
    }

    public void prepareForWeave() {
        if (trace.isTraceEnabled()) {
            trace.enter("prepareForWeave", this);
        }
        this.needToReweaveWorld = this.xcutSet.hasChangedSinceLastReset();
        for (UnwovenClassFile jc : this.addedClasses) {
            String name = jc.getClassName();
            ResolvedType type = this.world.resolve(name);
            if (type.isAspect()) {
                this.needToReweaveWorld |= this.xcutSet.addOrReplaceAspect(type);
            }
        }
        for (String name2 : this.deletedTypenames) {
            if (this.xcutSet.deleteAspect(UnresolvedType.forName(name2))) {
                this.needToReweaveWorld = true;
            }
        }
        this.shadowMungerList = this.xcutSet.getShadowMungers();
        rewritePointcuts(this.shadowMungerList);
        this.typeMungerList = this.xcutSet.getTypeMungers();
        this.lateTypeMungerList = this.xcutSet.getLateTypeMungers();
        this.declareParentsList = this.xcutSet.getDeclareParents();
        addCustomMungers();
        Collections.sort(this.shadowMungerList, new Comparator<ShadowMunger>() { // from class: org.aspectj.weaver.bcel.BcelWeaver.3
            @Override // java.util.Comparator
            public int compare(ShadowMunger sm1, ShadowMunger sm2) {
                if (sm1.getSourceLocation() == null) {
                    return sm2.getSourceLocation() == null ? 0 : 1;
                }
                if (sm2.getSourceLocation() == null) {
                    return -1;
                }
                return sm2.getSourceLocation().getOffset() - sm1.getSourceLocation().getOffset();
            }
        });
        if (this.inReweavableMode) {
            this.world.showMessage(IMessage.INFO, WeaverMessages.format(WeaverMessages.REWEAVABLE_MODE), null, null);
        }
        if (trace.isTraceEnabled()) {
            trace.exit("prepareForWeave");
        }
    }

    private void addCustomMungers() {
        if (this.customMungerFactory != null) {
            for (UnwovenClassFile jc : this.addedClasses) {
                String name = jc.getClassName();
                ResolvedType type = this.world.resolve(name);
                if (type.isAspect()) {
                    Collection<ShadowMunger> shadowMungers = this.customMungerFactory.createCustomShadowMungers(type);
                    if (shadowMungers != null) {
                        this.shadowMungerList.addAll(shadowMungers);
                    }
                    Collection<ConcreteTypeMunger> typeMungers = this.customMungerFactory.createCustomTypeMungers(type);
                    if (typeMungers != null) {
                        this.typeMungerList.addAll(typeMungers);
                    }
                }
            }
        }
    }

    public void setCustomMungerFactory(CustomMungerFactory factory) {
        this.customMungerFactory = factory;
    }

    private void rewritePointcuts(List<ShadowMunger> shadowMungers) {
        PointcutRewriter rewriter = new PointcutRewriter();
        for (ShadowMunger munger : shadowMungers) {
            Pointcut p = munger.getPointcut();
            Pointcut newP = rewriter.rewrite(p);
            if (munger instanceof Advice) {
                Advice advice = (Advice) munger;
                if (advice.getSignature() != null) {
                    if ((advice.getConcreteAspect().isAnnotationStyleAspect() && advice.getDeclaringAspect() != null && advice.getDeclaringAspect().resolve(this.world).isAnnotationStyleAspect()) || advice.isAnnotationStyle()) {
                        int numFormals = advice.getBaseParameterCount();
                        int numArgs = advice.getSignature().getParameterTypes().length;
                        if (numFormals > 0) {
                            String[] names = advice.getSignature().getParameterNames(this.world);
                            validateBindings(newP, p, numArgs, names);
                        }
                    } else {
                        int numFormals2 = advice.getBaseParameterCount();
                        if (numFormals2 > 0) {
                            String[] names2 = advice.getBaseParameterNames(this.world);
                            validateBindings(newP, p, numFormals2, names2);
                        }
                    }
                }
            }
            newP.m_ignoreUnboundBindingForNames = p.m_ignoreUnboundBindingForNames;
            munger.setPointcut(newP);
        }
        Map<Pointcut, Pointcut> pcMap = new HashMap<>();
        for (ShadowMunger munger2 : shadowMungers) {
            Pointcut p2 = munger2.getPointcut();
            Pointcut newP2 = shareEntriesFromMap(p2, pcMap);
            newP2.m_ignoreUnboundBindingForNames = p2.m_ignoreUnboundBindingForNames;
            munger2.setPointcut(newP2);
        }
    }

    private Pointcut shareEntriesFromMap(Pointcut p, Map<Pointcut, Pointcut> pcMap) {
        if (p instanceof NameBindingPointcut) {
            return p;
        }
        if (p instanceof IfPointcut) {
            return p;
        }
        if (p instanceof ConcreteCflowPointcut) {
            return p;
        }
        if (p instanceof AndPointcut) {
            AndPointcut apc = (AndPointcut) p;
            Pointcut left = shareEntriesFromMap(apc.getLeft(), pcMap);
            Pointcut right = shareEntriesFromMap(apc.getRight(), pcMap);
            return new AndPointcut(left, right);
        }
        if (p instanceof OrPointcut) {
            OrPointcut opc = (OrPointcut) p;
            Pointcut left2 = shareEntriesFromMap(opc.getLeft(), pcMap);
            Pointcut right2 = shareEntriesFromMap(opc.getRight(), pcMap);
            return new OrPointcut(left2, right2);
        }
        if (p instanceof NotPointcut) {
            NotPointcut npc = (NotPointcut) p;
            Pointcut not = shareEntriesFromMap(npc.getNegatedPointcut(), pcMap);
            return new NotPointcut(not);
        }
        if (pcMap.containsKey(p)) {
            return pcMap.get(p);
        }
        pcMap.put(p, p);
        return p;
    }

    private void validateBindings(Pointcut dnfPointcut, Pointcut userPointcut, int numFormals, String[] names) {
        if (numFormals == 0 || dnfPointcut.couldMatchKinds() == Shadow.NO_SHADOW_KINDS_BITS) {
            return;
        }
        if (dnfPointcut instanceof OrPointcut) {
            OrPointcut orBasedDNFPointcut = (OrPointcut) dnfPointcut;
            Pointcut[] leftBindings = new Pointcut[numFormals];
            Pointcut[] rightBindings = new Pointcut[numFormals];
            validateOrBranch(orBasedDNFPointcut, userPointcut, numFormals, names, leftBindings, rightBindings);
            return;
        }
        Pointcut[] bindings = new Pointcut[numFormals];
        validateSingleBranch(dnfPointcut, userPointcut, numFormals, names, bindings);
    }

    private void validateOrBranch(OrPointcut pc, Pointcut userPointcut, int numFormals, String[] names, Pointcut[] leftBindings, Pointcut[] rightBindings) {
        Pointcut left = pc.getLeft();
        Pointcut right = pc.getRight();
        if (left instanceof OrPointcut) {
            Pointcut[] newRightBindings = new Pointcut[numFormals];
            validateOrBranch((OrPointcut) left, userPointcut, numFormals, names, leftBindings, newRightBindings);
        } else if (left.couldMatchKinds() != Shadow.NO_SHADOW_KINDS_BITS) {
            validateSingleBranch(left, userPointcut, numFormals, names, leftBindings);
        }
        if (right instanceof OrPointcut) {
            Pointcut[] newLeftBindings = new Pointcut[numFormals];
            validateOrBranch((OrPointcut) right, userPointcut, numFormals, names, newLeftBindings, rightBindings);
        } else if (right.couldMatchKinds() != Shadow.NO_SHADOW_KINDS_BITS) {
            validateSingleBranch(right, userPointcut, numFormals, names, rightBindings);
        }
        int kindsInCommon = left.couldMatchKinds() & right.couldMatchKinds();
        if (kindsInCommon != Shadow.NO_SHADOW_KINDS_BITS && couldEverMatchSameJoinPoints(left, right)) {
            List<String> ambiguousNames = new ArrayList<>();
            for (int i = 0; i < numFormals; i++) {
                if (leftBindings[i] == null) {
                    if (rightBindings[i] != null) {
                        ambiguousNames.add(names[i]);
                    }
                } else if (!leftBindings[i].equals(rightBindings[i])) {
                    ambiguousNames.add(names[i]);
                }
            }
            if (!ambiguousNames.isEmpty()) {
                raiseAmbiguityInDisjunctionError(userPointcut, ambiguousNames);
            }
        }
    }

    private void validateSingleBranch(Pointcut pc, Pointcut userPointcut, int numFormals, String[] names, Pointcut[] bindings) {
        boolean[] foundFormals = new boolean[numFormals];
        for (int i = 0; i < foundFormals.length; i++) {
            foundFormals[i] = false;
        }
        validateSingleBranchRecursion(pc, userPointcut, foundFormals, names, bindings);
        for (int i2 = 0; i2 < foundFormals.length; i2++) {
            if (!foundFormals[i2]) {
                boolean ignore = false;
                int j = 0;
                while (true) {
                    if (j >= userPointcut.m_ignoreUnboundBindingForNames.length) {
                        break;
                    }
                    if (names[i2] == null || !names[i2].equals(userPointcut.m_ignoreUnboundBindingForNames[j])) {
                        j++;
                    } else {
                        ignore = true;
                        break;
                    }
                }
                if (!ignore) {
                    raiseUnboundFormalError(names[i2], userPointcut);
                }
            }
        }
    }

    private void validateSingleBranchRecursion(Pointcut pc, Pointcut userPointcut, boolean[] foundFormals, String[] names, Pointcut[] bindings) {
        if (pc instanceof NotPointcut) {
            NotPointcut not = (NotPointcut) pc;
            if (not.getNegatedPointcut() instanceof NameBindingPointcut) {
                NameBindingPointcut nnbp = (NameBindingPointcut) not.getNegatedPointcut();
                if (!nnbp.getBindingAnnotationTypePatterns().isEmpty() && !nnbp.getBindingTypePatterns().isEmpty()) {
                    raiseNegationBindingError(userPointcut);
                    return;
                }
                return;
            }
            return;
        }
        if (pc instanceof AndPointcut) {
            AndPointcut and = (AndPointcut) pc;
            validateSingleBranchRecursion(and.getLeft(), userPointcut, foundFormals, names, bindings);
            validateSingleBranchRecursion(and.getRight(), userPointcut, foundFormals, names, bindings);
            return;
        }
        if (!(pc instanceof NameBindingPointcut)) {
            if (pc instanceof ConcreteCflowPointcut) {
                ConcreteCflowPointcut cfp = (ConcreteCflowPointcut) pc;
                int[] slots = cfp.getUsedFormalSlots();
                for (int i = 0; i < slots.length; i++) {
                    bindings[slots[i]] = cfp;
                    if (foundFormals[slots[i]]) {
                        raiseAmbiguousBindingError(names[slots[i]], userPointcut);
                    } else {
                        foundFormals[slots[i]] = true;
                    }
                }
                return;
            }
            return;
        }
        List<BindingTypePattern> bindingTypePatterns = ((NameBindingPointcut) pc).getBindingTypePatterns();
        for (BindingTypePattern bindingTypePattern : bindingTypePatterns) {
            int index = bindingTypePattern.getFormalIndex();
            bindings[index] = pc;
            if (foundFormals[index]) {
                raiseAmbiguousBindingError(names[index], userPointcut);
            } else {
                foundFormals[index] = true;
            }
        }
        List<BindingPattern> bindingAnnotationTypePatterns = ((NameBindingPointcut) pc).getBindingAnnotationTypePatterns();
        for (BindingPattern bindingAnnotationTypePattern : bindingAnnotationTypePatterns) {
            int index2 = bindingAnnotationTypePattern.getFormalIndex();
            bindings[index2] = pc;
            if (foundFormals[index2]) {
                raiseAmbiguousBindingError(names[index2], userPointcut);
            } else {
                foundFormals[index2] = true;
            }
        }
    }

    private boolean couldEverMatchSameJoinPoints(Pointcut left, Pointcut right) {
        if (left instanceof OrPointcut) {
            OrPointcut leftOrPointcut = (OrPointcut) left;
            if (couldEverMatchSameJoinPoints(leftOrPointcut.getLeft(), right) || couldEverMatchSameJoinPoints(leftOrPointcut.getRight(), right)) {
                return true;
            }
            return false;
        }
        if (right instanceof OrPointcut) {
            OrPointcut rightOrPointcut = (OrPointcut) right;
            if (couldEverMatchSameJoinPoints(left, rightOrPointcut.getLeft()) || couldEverMatchSameJoinPoints(left, rightOrPointcut.getRight())) {
                return true;
            }
            return false;
        }
        WithinPointcut leftWithin = (WithinPointcut) findFirstPointcutIn(left, WithinPointcut.class);
        WithinPointcut rightWithin = (WithinPointcut) findFirstPointcutIn(right, WithinPointcut.class);
        if (leftWithin != null && rightWithin != null && !leftWithin.couldEverMatchSameJoinPointsAs(rightWithin)) {
            return false;
        }
        KindedPointcut leftKind = (KindedPointcut) findFirstPointcutIn(left, KindedPointcut.class);
        KindedPointcut rightKind = (KindedPointcut) findFirstPointcutIn(right, KindedPointcut.class);
        if (leftKind != null && rightKind != null && !leftKind.couldEverMatchSameJoinPointsAs(rightKind)) {
            return false;
        }
        return true;
    }

    private Pointcut findFirstPointcutIn(Pointcut toSearch, Class toLookFor) {
        if (toSearch instanceof NotPointcut) {
            return null;
        }
        if (toLookFor.isInstance(toSearch)) {
            return toSearch;
        }
        if (toSearch instanceof AndPointcut) {
            AndPointcut apc = (AndPointcut) toSearch;
            Pointcut left = findFirstPointcutIn(apc.getLeft(), toLookFor);
            if (left != null) {
                return left;
            }
            return findFirstPointcutIn(apc.getRight(), toLookFor);
        }
        return null;
    }

    private void raiseNegationBindingError(Pointcut userPointcut) {
        this.world.showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.NEGATION_DOESNT_ALLOW_BINDING), userPointcut.getSourceContext().makeSourceLocation(userPointcut), null);
    }

    private void raiseAmbiguousBindingError(String name, Pointcut pointcut) {
        this.world.showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.AMBIGUOUS_BINDING, name), pointcut.getSourceContext().makeSourceLocation(pointcut), null);
    }

    private void raiseAmbiguityInDisjunctionError(Pointcut userPointcut, List<String> names) {
        StringBuffer formalNames = new StringBuffer(names.get(0).toString());
        for (int i = 1; i < names.size(); i++) {
            formalNames.append(", ");
            formalNames.append(names.get(i));
        }
        this.world.showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.AMBIGUOUS_BINDING_IN_OR, formalNames), userPointcut.getSourceContext().makeSourceLocation(userPointcut), null);
    }

    private void raiseUnboundFormalError(String name, Pointcut userPointcut) {
        this.world.showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.UNBOUND_FORMAL, name), userPointcut.getSourceLocation(), null);
    }

    public void addManifest(Manifest newManifest) {
        if (this.manifest == null) {
            this.manifest = newManifest;
        }
    }

    public Manifest getManifest(boolean shouldCreate) {
        if (this.manifest == null && shouldCreate) {
            Attributes.Name CREATED_BY = new Attributes.Name("Created-By");
            this.manifest = new Manifest();
            Attributes attributes = this.manifest.getMainAttributes();
            attributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
            attributes.put(CREATED_BY, "AspectJ Compiler");
        }
        return this.manifest;
    }

    public Collection<String> weave(File file) throws IOException {
        OutputStream os = FileUtil.makeOutputStream(file);
        this.zipOutputStream = new ZipOutputStream(os);
        prepareForWeave();
        Collection<String> c = weave(new IClassFileProvider() { // from class: org.aspectj.weaver.bcel.BcelWeaver.4
            @Override // org.aspectj.weaver.IClassFileProvider
            public boolean isApplyAtAspectJMungersOnly() {
                return false;
            }

            @Override // org.aspectj.weaver.IClassFileProvider
            public Iterator<UnwovenClassFile> getClassFileIterator() {
                return BcelWeaver.this.addedClasses.iterator();
            }

            @Override // org.aspectj.weaver.IClassFileProvider
            public IWeaveRequestor getRequestor() {
                return new IWeaveRequestor() { // from class: org.aspectj.weaver.bcel.BcelWeaver.4.1
                    @Override // org.aspectj.weaver.IWeaveRequestor
                    public void acceptResult(IUnwovenClassFile result) {
                        try {
                            BcelWeaver.this.writeZipEntry(result.getFilename(), result.getBytes());
                        } catch (IOException e) {
                        }
                    }

                    @Override // org.aspectj.weaver.IWeaveRequestor
                    public void processingReweavableState() {
                    }

                    @Override // org.aspectj.weaver.IWeaveRequestor
                    public void addingTypeMungers() {
                    }

                    @Override // org.aspectj.weaver.IWeaveRequestor
                    public void weavingAspects() {
                    }

                    @Override // org.aspectj.weaver.IWeaveRequestor
                    public void weavingClasses() {
                    }

                    @Override // org.aspectj.weaver.IWeaveRequestor
                    public void weaveCompleted() {
                    }
                };
            }
        });
        this.zipOutputStream.close();
        return c;
    }

    public Collection<String> weave(IClassFileProvider input) throws IOException {
        if (trace.isTraceEnabled()) {
            trace.enter("weave", this, input);
        }
        ContextToken weaveToken = CompilationAndWeavingContext.enteringPhase(22, "");
        Collection<String> wovenClassNames = new ArrayList<>();
        IWeaveRequestor requestor = input.getRequestor();
        if (this.world.getModel() != null && this.world.isMinimalModel()) {
            this.candidatesForRemoval = new HashSet();
        }
        if (this.world.getModel() != null && !this.isBatchWeave) {
            AsmManager manager = this.world.getModelAsAsmManager();
            Iterator<UnwovenClassFile> i = input.getClassFileIterator();
            while (i.hasNext()) {
                manager.removeRelationshipsTargettingThisType(i.next().getClassName());
            }
        }
        Iterator<UnwovenClassFile> i2 = input.getClassFileIterator();
        while (i2.hasNext()) {
            ResolvedType theType = this.world.resolve(i2.next().getClassName());
            if (theType != null) {
                theType.ensureConsistent();
            }
        }
        if (input.isApplyAtAspectJMungersOnly()) {
            ContextToken atAspectJMungersOnly = CompilationAndWeavingContext.enteringPhase(32, "");
            requestor.weavingAspects();
            CompilationAndWeavingContext.enteringPhase(25, "");
            Iterator<UnwovenClassFile> i3 = input.getClassFileIterator();
            while (i3.hasNext()) {
                UnwovenClassFile classFile = i3.next();
                String className = classFile.getClassName();
                ResolvedType theType2 = this.world.resolve(className);
                if (theType2.isAnnotationStyleAspect()) {
                    BcelObjectType classType = BcelWorld.getBcelObjectType(theType2);
                    if (classType == null) {
                        throw new BCException("Can't find bcel delegate for " + className + " type=" + theType2.getClass());
                    }
                    LazyClassGen clazz = classType.getLazyClassGen();
                    BcelPerClauseAspectAdder selfMunger = new BcelPerClauseAspectAdder(theType2, theType2.getPerClause().getKind());
                    selfMunger.forceMunge(clazz, true);
                    classType.finishedWith();
                    UnwovenClassFile[] newClasses = getClassFilesFor(clazz);
                    for (UnwovenClassFile unwovenClassFile : newClasses) {
                        requestor.acceptResult(unwovenClassFile);
                    }
                    wovenClassNames.add(classFile.getClassName());
                }
            }
            requestor.weaveCompleted();
            CompilationAndWeavingContext.leavingPhase(atAspectJMungersOnly);
            return wovenClassNames;
        }
        requestor.processingReweavableState();
        ContextToken reweaveToken = CompilationAndWeavingContext.enteringPhase(23, "");
        prepareToProcessReweavableState();
        Iterator<UnwovenClassFile> i4 = input.getClassFileIterator();
        while (i4.hasNext()) {
            String className2 = i4.next().getClassName();
            BcelObjectType classType2 = getClassType(className2);
            if (classType2 != null) {
                ContextToken tok = CompilationAndWeavingContext.enteringPhase(23, className2);
                processReweavableStateIfPresent(className2, classType2);
                CompilationAndWeavingContext.leavingPhase(tok);
            }
        }
        CompilationAndWeavingContext.leavingPhase(reweaveToken);
        ContextToken typeMungingToken = CompilationAndWeavingContext.enteringPhase(24, "");
        requestor.addingTypeMungers();
        List<String> typesToProcess = new ArrayList<>();
        Iterator<UnwovenClassFile> iter = input.getClassFileIterator();
        while (iter.hasNext()) {
            UnwovenClassFile clf = iter.next();
            typesToProcess.add(clf.getClassName());
        }
        while (typesToProcess.size() > 0) {
            weaveParentsFor(typesToProcess, typesToProcess.get(0), null);
        }
        Iterator<UnwovenClassFile> i5 = input.getClassFileIterator();
        while (i5.hasNext()) {
            addNormalTypeMungers(i5.next().getClassName());
        }
        CompilationAndWeavingContext.leavingPhase(typeMungingToken);
        requestor.weavingAspects();
        ContextToken aspectToken = CompilationAndWeavingContext.enteringPhase(25, "");
        Iterator<UnwovenClassFile> i6 = input.getClassFileIterator();
        while (i6.hasNext()) {
            UnwovenClassFile classFile2 = i6.next();
            String className3 = classFile2.getClassName();
            ResolvedType theType3 = this.world.resolve(className3);
            if (theType3.isAspect()) {
                BcelObjectType classType3 = BcelWorld.getBcelObjectType(theType3);
                if (classType3 == null) {
                    ReferenceTypeDelegate theDelegate = ((ReferenceType) theType3).getDelegate();
                    if (!theDelegate.getClass().getName().endsWith("EclipseSourceType")) {
                        throw new BCException("Can't find bcel delegate for " + className3 + " type=" + theType3.getClass());
                    }
                } else {
                    weaveAndNotify(classFile2, classType3, requestor);
                    wovenClassNames.add(className3);
                }
            }
        }
        CompilationAndWeavingContext.leavingPhase(aspectToken);
        requestor.weavingClasses();
        ContextToken classToken = CompilationAndWeavingContext.enteringPhase(26, "");
        Iterator<UnwovenClassFile> i7 = input.getClassFileIterator();
        while (i7.hasNext()) {
            UnwovenClassFile classFile3 = i7.next();
            String className4 = classFile3.getClassName();
            ResolvedType theType4 = this.world.resolve(className4);
            if (!theType4.isAspect()) {
                BcelObjectType classType4 = BcelWorld.getBcelObjectType(theType4);
                if (classType4 == null) {
                    ReferenceTypeDelegate theDelegate2 = ((ReferenceType) theType4).getDelegate();
                    if (!theDelegate2.getClass().getName().endsWith("EclipseSourceType")) {
                        throw new BCException("Can't find bcel delegate for " + className4 + " type=" + theType4.getClass());
                    }
                } else {
                    weaveAndNotify(classFile3, classType4, requestor);
                    wovenClassNames.add(className4);
                }
            }
        }
        CompilationAndWeavingContext.leavingPhase(classToken);
        this.addedClasses.clear();
        this.deletedTypenames.clear();
        requestor.weaveCompleted();
        CompilationAndWeavingContext.leavingPhase(weaveToken);
        if (trace.isTraceEnabled()) {
            trace.exit("weave", wovenClassNames);
        }
        if (this.world.getModel() != null && this.world.isMinimalModel()) {
            this.candidatesForRemoval.clear();
        }
        return wovenClassNames;
    }

    public void allWeavingComplete() {
        warnOnUnmatchedAdvice();
    }

    private void warnOnUnmatchedAdvice() {
        if (this.world.isInJava5Mode() && this.world.getLint().adviceDidNotMatch.isEnabled()) {
            List l = this.world.getCrosscuttingMembersSet().getShadowMungers();
            HashSet hashSet = new HashSet();
            for (ShadowMunger element : l) {
                if (element instanceof BcelAdvice) {
                    BcelAdvice ba = (BcelAdvice) element;
                    if (ba.getKind() != AdviceKind.CflowEntry && ba.getKind() != AdviceKind.CflowBelowEntry && !ba.hasMatchedSomething() && ba.getSignature() != null) {
                        Object obj = new Object(ba) { // from class: org.aspectj.weaver.bcel.BcelWeaver.1AdviceLocation
                            private final int lineNo;
                            private final UnresolvedType inAspect;

                            {
                                this.lineNo = ba.getSourceLocation().getLine();
                                this.inAspect = ba.getDeclaringAspect();
                            }

                            public boolean equals(Object obj2) {
                                if (!(obj2 instanceof C1AdviceLocation)) {
                                    return false;
                                }
                                C1AdviceLocation other = (C1AdviceLocation) obj2;
                                if (this.lineNo != other.lineNo || !this.inAspect.equals(other.inAspect)) {
                                    return false;
                                }
                                return true;
                            }

                            public int hashCode() {
                                return 37 + (17 * this.lineNo) + (17 * this.inAspect.hashCode());
                            }
                        };
                        if (!hashSet.contains(obj)) {
                            hashSet.add(obj);
                            if (!(ba.getSignature() instanceof BcelMethod) || !Utility.isSuppressing(ba.getSignature(), "adviceDidNotMatch")) {
                                this.world.getLint().adviceDidNotMatch.signal(ba.getDeclaringAspect().toString(), new SourceLocation(element.getSourceLocation().getSourceFile(), element.getSourceLocation().getLine()));
                            }
                        }
                    }
                }
            }
        }
    }

    private void weaveParentsFor(List<String> typesForWeaving, String typeToWeave, ResolvedType resolvedTypeToWeave) throws AbortException {
        if (resolvedTypeToWeave == null) {
            resolvedTypeToWeave = this.world.resolve(typeToWeave);
        }
        ResolvedType superclassType = resolvedTypeToWeave.getSuperclass();
        String superclassTypename = superclassType == null ? null : superclassType.getName();
        if (superclassType != null && !superclassType.isTypeHierarchyComplete() && superclassType.isExposedToWeaver() && typesForWeaving.contains(superclassTypename)) {
            weaveParentsFor(typesForWeaving, superclassTypename, superclassType);
        }
        ResolvedType[] interfaceTypes = resolvedTypeToWeave.getDeclaredInterfaces();
        for (ResolvedType resolvedSuperInterface : interfaceTypes) {
            if (!resolvedSuperInterface.isTypeHierarchyComplete()) {
                String interfaceTypename = resolvedSuperInterface.getName();
                if (resolvedSuperInterface.isExposedToWeaver()) {
                    weaveParentsFor(typesForWeaving, interfaceTypename, resolvedSuperInterface);
                }
            }
        }
        ContextToken tok = CompilationAndWeavingContext.enteringPhase(7, resolvedTypeToWeave.getName());
        if (!resolvedTypeToWeave.isTypeHierarchyComplete()) {
            weaveParentTypeMungers(resolvedTypeToWeave);
        }
        CompilationAndWeavingContext.leavingPhase(tok);
        typesForWeaving.remove(typeToWeave);
        resolvedTypeToWeave.tagAsTypeHierarchyComplete();
    }

    public void prepareToProcessReweavableState() {
    }

    public void processReweavableStateIfPresent(String className, BcelObjectType classType) throws AbortException {
        WeaverStateInfo wsi = classType.getWeaverState();
        if (wsi != null && wsi.isReweavable()) {
            this.world.showMessage(IMessage.INFO, WeaverMessages.format(WeaverMessages.PROCESSING_REWEAVABLE, className, classType.getSourceLocation().getSourceFile()), null, null);
            Set<String> aspectsPreviouslyInWorld = wsi.getAspectsAffectingType();
            Set<String> alreadyConfirmedReweavableState = new HashSet<>();
            for (String requiredTypeSignature : aspectsPreviouslyInWorld) {
                if (!alreadyConfirmedReweavableState.contains(requiredTypeSignature)) {
                    ResolvedType rtx = this.world.resolve(UnresolvedType.forSignature(requiredTypeSignature), true);
                    boolean exists = !rtx.isMissing();
                    if (!exists) {
                        this.world.getLint().missingAspectForReweaving.signal(new String[]{rtx.getName(), className}, classType.getSourceLocation(), null);
                    } else {
                        if (!this.world.isOverWeaving()) {
                            if (!this.xcutSet.containsAspect(rtx)) {
                                this.world.showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.REWEAVABLE_ASPECT_NOT_REGISTERED, rtx.getName(), className), null, null);
                            } else if (!this.world.getMessageHandler().isIgnoring(IMessage.INFO)) {
                                this.world.showMessage(IMessage.INFO, WeaverMessages.format(WeaverMessages.VERIFIED_REWEAVABLE_TYPE, rtx.getName(), rtx.getSourceLocation().getSourceFile()), null, null);
                            }
                        }
                        alreadyConfirmedReweavableState.add(requiredTypeSignature);
                    }
                }
            }
            if (!this.world.isOverWeaving()) {
                byte[] bytes = wsi.getUnwovenClassFileData(classType.getJavaClass().getBytes());
                classType.getWeaverVersionAttribute();
                JavaClass newJavaClass = Utility.makeJavaClass(classType.getJavaClass().getFileName(), bytes);
                classType.setJavaClass(newJavaClass, true);
                classType.getResolvedTypeX().ensureConsistent();
            }
        }
    }

    private void weaveAndNotify(UnwovenClassFile classFile, BcelObjectType classType, IWeaveRequestor requestor) throws IOException {
        trace.enter("weaveAndNotify", (Object) this, new Object[]{classFile, classType, requestor});
        ContextToken tok = CompilationAndWeavingContext.enteringPhase(27, classType.getResolvedTypeX().getName());
        LazyClassGen clazz = weaveWithoutDump(classFile, classType);
        classType.finishedWith();
        if (clazz != null) {
            UnwovenClassFile[] newClasses = getClassFilesFor(clazz);
            if (newClasses[0].getClassName().equals(classFile.getClassName())) {
                newClasses[0].setClassNameAsChars(classFile.getClassNameAsChars());
            }
            for (UnwovenClassFile unwovenClassFile : newClasses) {
                requestor.acceptResult(unwovenClassFile);
            }
        } else {
            requestor.acceptResult(classFile);
        }
        classType.weavingCompleted();
        CompilationAndWeavingContext.leavingPhase(tok);
        trace.exit("weaveAndNotify");
    }

    public BcelObjectType getClassType(String forClass) {
        return BcelWorld.getBcelObjectType(this.world.resolve(forClass));
    }

    public void addParentTypeMungers(String typeName) throws AbortException {
        weaveParentTypeMungers(this.world.resolve(typeName));
    }

    public void addNormalTypeMungers(String typeName) throws AbortException {
        weaveNormalTypeMungers(this.world.resolve(typeName));
    }

    public UnwovenClassFile[] getClassFilesFor(LazyClassGen clazz) {
        List<UnwovenClassFile.ChildClass> childClasses = clazz.getChildClasses(this.world);
        UnwovenClassFile[] ret = new UnwovenClassFile[1 + childClasses.size()];
        ret[0] = new UnwovenClassFile(clazz.getFileName(), clazz.getClassName(), clazz.getJavaClassBytesIncludingReweavable(this.world));
        int index = 1;
        for (UnwovenClassFile.ChildClass element : childClasses) {
            UnwovenClassFile childClass = new UnwovenClassFile(clazz.getFileName() + PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX + element.name, element.bytes);
            int i = index;
            index++;
            ret[i] = childClass;
        }
        return ret;
    }

    public void weaveParentTypeMungers(ResolvedType onType) throws AbortException {
        if (onType.isRawType() || onType.isParameterizedType()) {
            onType = onType.getGenericType();
        }
        onType.clearInterTypeMungers();
        List<DeclareParents> decpToRepeat = new ArrayList<>();
        boolean aParentChangeOccurred = false;
        boolean anAnnotationChangeOccurred = false;
        for (DeclareParents decp : this.declareParentsList) {
            boolean typeChanged = applyDeclareParents(decp, onType);
            if (typeChanged) {
                aParentChangeOccurred = true;
            } else {
                decpToRepeat.add(decp);
            }
        }
        for (DeclareAnnotation decA : this.xcutSet.getDeclareAnnotationOnTypes()) {
            boolean typeChanged2 = applyDeclareAtType(decA, onType, true);
            if (typeChanged2) {
                anAnnotationChangeOccurred = true;
            }
        }
        while (true) {
            if ((aParentChangeOccurred || anAnnotationChangeOccurred) && !decpToRepeat.isEmpty()) {
                aParentChangeOccurred = false;
                anAnnotationChangeOccurred = false;
                List<DeclareParents> decpToRepeatNextTime = new ArrayList<>();
                for (DeclareParents decp2 : decpToRepeat) {
                    boolean typeChanged3 = applyDeclareParents(decp2, onType);
                    if (typeChanged3) {
                        aParentChangeOccurred = true;
                    } else {
                        decpToRepeatNextTime.add(decp2);
                    }
                }
                for (DeclareAnnotation decA2 : this.xcutSet.getDeclareAnnotationOnTypes()) {
                    boolean typeChanged4 = applyDeclareAtType(decA2, onType, false);
                    if (typeChanged4) {
                        anAnnotationChangeOccurred = true;
                    }
                }
                decpToRepeat = decpToRepeatNextTime;
            } else {
                return;
            }
        }
    }

    private boolean applyDeclareAtType(DeclareAnnotation decA, ResolvedType onType, boolean reportProblems) throws AbortException {
        boolean didSomething = false;
        if (decA.matches(onType)) {
            AnnotationAJ theAnnotation = decA.getAnnotation();
            if (theAnnotation == null || onType.hasAnnotation(theAnnotation.getType())) {
                return false;
            }
            AnnotationAJ annoX = decA.getAnnotation();
            boolean problemReported = verifyTargetIsOK(decA, onType, annoX, reportProblems);
            if (!problemReported) {
                AsmRelationshipProvider.addDeclareAnnotationRelationship(this.world.getModelAsAsmManager(), decA.getSourceLocation(), onType.getSourceLocation(), false);
                if (!getWorld().getMessageHandler().isIgnoring(IMessage.WEAVEINFO)) {
                    getWorld().getMessageHandler().handleMessage(WeaveMessage.constructWeavingMessage(WeaveMessage.WEAVEMESSAGE_ANNOTATES, new String[]{onType.toString(), Utility.beautifyLocation(onType.getSourceLocation()), decA.getAnnotationString(), "type", decA.getAspect().toString(), Utility.beautifyLocation(decA.getSourceLocation())}));
                }
                didSomething = true;
                ResolvedTypeMunger newAnnotationTM = new AnnotationOnTypeMunger(annoX);
                newAnnotationTM.setSourceLocation(decA.getSourceLocation());
                onType.addInterTypeMunger(new BcelTypeMunger(newAnnotationTM, decA.getAspect().resolve(this.world)), false);
                decA.copyAnnotationTo(onType);
            }
        }
        return didSomething;
    }

    private boolean verifyTargetIsOK(DeclareAnnotation decA, ResolvedType onType, AnnotationAJ annoX, boolean outputProblems) throws AbortException {
        boolean problemReported = false;
        if (annoX.specifiesTarget() && ((onType.isAnnotation() && !annoX.allowedOnAnnotationType()) || !annoX.allowedOnRegularType())) {
            if (outputProblems) {
                if (decA.isExactPattern()) {
                    this.world.getMessageHandler().handleMessage(MessageUtil.error(WeaverMessages.format(WeaverMessages.INCORRECT_TARGET_FOR_DECLARE_ANNOTATION, onType.getName(), annoX.getTypeName(), annoX.getValidTargets()), decA.getSourceLocation()));
                } else if (this.world.getLint().invalidTargetForAnnotation.isEnabled()) {
                    this.world.getLint().invalidTargetForAnnotation.signal(new String[]{onType.getName(), annoX.getTypeName(), annoX.getValidTargets()}, decA.getSourceLocation(), new ISourceLocation[]{onType.getSourceLocation()});
                }
            }
            problemReported = true;
        }
        return problemReported;
    }

    private boolean applyDeclareParents(DeclareParents p, ResolvedType onType) throws AbortException {
        boolean didSomething = false;
        List<ResolvedType> newParents = p.findMatchingNewParents(onType, true);
        if (!newParents.isEmpty()) {
            didSomething = true;
            BcelWorld.getBcelObjectType(onType);
            for (ResolvedType newParent : newParents) {
                onType.addParent(newParent);
                NewParentTypeMunger newParentMunger = new NewParentTypeMunger(newParent, p.getDeclaringType());
                if (p.isMixin()) {
                    newParentMunger.setIsMixin(true);
                }
                newParentMunger.setSourceLocation(p.getSourceLocation());
                onType.addInterTypeMunger(new BcelTypeMunger(newParentMunger, this.xcutSet.findAspectDeclaringParents(p)), false);
            }
        }
        return didSomething;
    }

    public void weaveNormalTypeMungers(ResolvedType onType) throws AbortException {
        ContextToken tok = CompilationAndWeavingContext.enteringPhase(24, onType.getName());
        if (onType.isRawType() || onType.isParameterizedType()) {
            onType = onType.getGenericType();
        }
        for (ConcreteTypeMunger m : this.typeMungerList) {
            if (!m.isLateMunger() && m.matches(onType)) {
                onType.addInterTypeMunger(m, false);
            }
        }
        CompilationAndWeavingContext.leavingPhase(tok);
    }

    public LazyClassGen weaveWithoutDump(UnwovenClassFile classFile, BcelObjectType classType) throws IOException {
        return weave(classFile, classType, false);
    }

    LazyClassGen weave(UnwovenClassFile classFile, BcelObjectType classType) throws IOException {
        LazyClassGen ret = weave(classFile, classType, true);
        return ret;
    }

    private LazyClassGen weave(UnwovenClassFile classFile, BcelObjectType classType, boolean dump) throws IOException {
        String classDebugInfo;
        String classDebugInfo2;
        IProgramElement parent;
        try {
            if (classType.isSynthetic()) {
                if (dump) {
                    dumpUnchanged(classFile);
                }
                return null;
            }
            ReferenceType resolvedClassType = classType.getResolvedTypeX();
            if (this.world.isXmlConfigured() && this.world.getXmlConfiguration().excludesType(resolvedClassType)) {
                if (!this.world.getMessageHandler().isIgnoring(IMessage.INFO)) {
                    this.world.getMessageHandler().handleMessage(MessageUtil.info("Type '" + resolvedClassType.getName() + "' not woven due to exclusion via XML weaver exclude section"));
                }
                if (dump) {
                    dumpUnchanged(classFile);
                }
                this.world.demote();
                return null;
            }
            List<ShadowMunger> shadowMungers = fastMatch(this.shadowMungerList, resolvedClassType);
            List<ConcreteTypeMunger> typeMungers = classType.getResolvedTypeX().getInterTypeMungers();
            resolvedClassType.checkInterTypeMungers();
            boolean mightNeedToWeave = shadowMungers.size() > 0 || typeMungers.size() > 0 || classType.isAspect() || this.world.getDeclareAnnotationOnMethods().size() > 0 || this.world.getDeclareAnnotationOnFields().size() > 0;
            boolean mightNeedBridgeMethods = this.world.isInJava5Mode() && !classType.isInterface() && resolvedClassType.getInterTypeMungersIncludingSupers().size() > 0;
            LazyClassGen clazz = null;
            if (mightNeedToWeave || mightNeedBridgeMethods) {
                clazz = classType.getLazyClassGen();
                boolean isChanged = false;
                if (mightNeedToWeave) {
                    try {
                        try {
                            isChanged = BcelClassWeaver.weave(this.world, clazz, shadowMungers, typeMungers, this.lateTypeMungerList, this.inReweavableMode);
                        } catch (RuntimeException re) {
                            try {
                                classDebugInfo = clazz.toLongString();
                            } catch (Throwable e) {
                                new RuntimeException("Crashed whilst crashing with this exception: " + e, e).printStackTrace();
                                classDebugInfo = clazz.getClassName();
                            }
                            String messageText = "trouble in: \n" + classDebugInfo;
                            getWorld().getMessageHandler().handleMessage(new Message(messageText, IMessage.ABORT, re, (ISourceLocation) null));
                        }
                    } catch (Error re2) {
                        try {
                            classDebugInfo2 = clazz.toLongString();
                        } catch (OutOfMemoryError e2) {
                            System.err.println("Ran out of memory creating debug info for an error");
                            re2.printStackTrace(System.err);
                            classDebugInfo2 = clazz.getClassName();
                        } catch (Throwable th) {
                            classDebugInfo2 = clazz.getClassName();
                        }
                        String messageText2 = "trouble in: \n" + classDebugInfo2;
                        getWorld().getMessageHandler().handleMessage(new Message(messageText2, IMessage.ABORT, re2, (ISourceLocation) null));
                    }
                }
                checkDeclareTypeErrorOrWarning(this.world, classType);
                if (mightNeedBridgeMethods) {
                    isChanged = BcelClassWeaver.calculateAnyRequiredBridgeMethods(this.world, clazz) || isChanged;
                }
                if (isChanged) {
                    if (dump) {
                        dump(classFile, clazz);
                    }
                    this.world.demote();
                    return clazz;
                }
            } else {
                checkDeclareTypeErrorOrWarning(this.world, classType);
            }
            AsmManager model = this.world.getModelAsAsmManager();
            if (this.world.isMinimalModel() && model != null && !classType.isAspect()) {
                AspectJElementHierarchy hierarchy = (AspectJElementHierarchy) model.getHierarchy();
                String pkgname = classType.getResolvedTypeX().getPackageName();
                String tname = classType.getResolvedTypeX().getSimpleBaseName();
                IProgramElement typeElement = hierarchy.findElementForType(pkgname, tname);
                if (typeElement != null && hasInnerType(typeElement)) {
                    this.candidatesForRemoval.add(typeElement);
                }
                if (typeElement != null && !hasInnerType(typeElement) && (parent = typeElement.getParent()) != null) {
                    parent.removeChild(typeElement);
                    if (parent.getKind().isSourceFile()) {
                        removeSourceFileIfNoMoreTypeDeclarationsInside(hierarchy, typeElement, parent);
                    } else {
                        hierarchy.forget(null, typeElement);
                        walkUpRemovingEmptyTypesAndPossiblyEmptySourceFile(hierarchy, tname, parent);
                    }
                }
            }
            if (dump) {
                dumpUnchanged(classFile);
                LazyClassGen lazyClassGen = clazz;
                this.world.demote();
                return lazyClassGen;
            }
            if (clazz == null || clazz.getChildClasses(this.world).isEmpty()) {
                this.world.demote();
                return null;
            }
            LazyClassGen lazyClassGen2 = clazz;
            this.world.demote();
            return lazyClassGen2;
        } finally {
            this.world.demote();
        }
    }

    private void walkUpRemovingEmptyTypesAndPossiblyEmptySourceFile(AspectJElementHierarchy hierarchy, String tname, IProgramElement typeThatHasChildRemoved) {
        IProgramElement parent;
        while (typeThatHasChildRemoved != null && !typeThatHasChildRemoved.getKind().isType() && !typeThatHasChildRemoved.getKind().isSourceFile()) {
            typeThatHasChildRemoved = typeThatHasChildRemoved.getParent();
        }
        if (this.candidatesForRemoval.contains(typeThatHasChildRemoved) && !hasInnerType(typeThatHasChildRemoved) && (parent = typeThatHasChildRemoved.getParent()) != null) {
            parent.removeChild(typeThatHasChildRemoved);
            this.candidatesForRemoval.remove(typeThatHasChildRemoved);
            if (parent.getKind().isSourceFile()) {
                removeSourceFileIfNoMoreTypeDeclarationsInside(hierarchy, typeThatHasChildRemoved, parent);
            } else {
                walkUpRemovingEmptyTypesAndPossiblyEmptySourceFile(hierarchy, tname, parent);
            }
        }
    }

    private void removeSourceFileIfNoMoreTypeDeclarationsInside(AspectJElementHierarchy hierarchy, IProgramElement typeElement, IProgramElement sourceFileNode) {
        boolean anyOtherTypeDeclarations = false;
        Iterator<IProgramElement> it = sourceFileNode.getChildren().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            IProgramElement child = it.next();
            IProgramElement.Kind k = child.getKind();
            if (k.isType()) {
                anyOtherTypeDeclarations = true;
                break;
            }
        }
        if (!anyOtherTypeDeclarations) {
            IProgramElement cuParent = sourceFileNode.getParent();
            if (cuParent != null) {
                sourceFileNode.setParent(null);
                cuParent.removeChild(sourceFileNode);
            }
            hierarchy.forget(sourceFileNode, typeElement);
            return;
        }
        hierarchy.forget(null, typeElement);
    }

    private boolean hasInnerType(IProgramElement typeNode) {
        for (IProgramElement child : typeNode.getChildren()) {
            IProgramElement.Kind kind = child.getKind();
            if (kind.isType()) {
                return true;
            }
            if (kind.isType() || kind == IProgramElement.Kind.METHOD || kind == IProgramElement.Kind.CONSTRUCTOR) {
                boolean b = hasInnerType(child);
                if (b) {
                    return b;
                }
            }
        }
        return false;
    }

    private void checkDeclareTypeErrorOrWarning(BcelWorld world2, BcelObjectType classType) throws AbortException {
        List<DeclareTypeErrorOrWarning> dteows = this.world.getDeclareTypeEows();
        for (DeclareTypeErrorOrWarning dteow : dteows) {
            if (dteow.getTypePattern().matchesStatically(classType.getResolvedTypeX())) {
                if (dteow.isError()) {
                    this.world.getMessageHandler().handleMessage(MessageUtil.error(dteow.getMessage(), classType.getResolvedTypeX().getSourceLocation()));
                } else {
                    this.world.getMessageHandler().handleMessage(MessageUtil.warn(dteow.getMessage(), classType.getResolvedTypeX().getSourceLocation()));
                }
            }
        }
    }

    private void dumpUnchanged(UnwovenClassFile classFile) throws IOException {
        if (this.zipOutputStream != null) {
            writeZipEntry(getEntryName(classFile.getJavaClass().getClassName()), classFile.getBytes());
        } else {
            classFile.writeUnchangedBytes();
        }
    }

    private String getEntryName(String className) {
        return className.replace('.', '/') + ClassUtils.CLASS_FILE_SUFFIX;
    }

    private void dump(UnwovenClassFile classFile, LazyClassGen clazz) throws IOException {
        if (this.zipOutputStream != null) {
            String mainClassName = classFile.getJavaClass().getClassName();
            writeZipEntry(getEntryName(mainClassName), clazz.getJavaClass(this.world).getBytes());
            List<UnwovenClassFile.ChildClass> childClasses = clazz.getChildClasses(this.world);
            if (!childClasses.isEmpty()) {
                for (UnwovenClassFile.ChildClass c : childClasses) {
                    writeZipEntry(getEntryName(mainClassName + PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX + c.name), c.bytes);
                }
                return;
            }
            return;
        }
        classFile.writeWovenBytes(clazz.getJavaClass(this.world).getBytes(), clazz.getChildClasses(this.world));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeZipEntry(String name, byte[] bytes) throws IOException {
        ZipEntry newEntry = new ZipEntry(name);
        this.zipOutputStream.putNextEntry(newEntry);
        this.zipOutputStream.write(bytes);
        this.zipOutputStream.closeEntry();
    }

    private List<ShadowMunger> fastMatch(List<ShadowMunger> list, ResolvedType type) {
        if (list == null) {
            return Collections.emptyList();
        }
        boolean isOverweaving = this.world.isOverWeaving();
        WeaverStateInfo typeWeaverState = isOverweaving ? type.getWeaverState() : null;
        FastMatchInfo info = new FastMatchInfo(type, null, this.world);
        List<ShadowMunger> result = new ArrayList<>();
        if (this.world.areInfoMessagesEnabled() && this.world.isTimingEnabled()) {
            for (ShadowMunger munger : list) {
                if (typeWeaverState != null) {
                    ResolvedType declaringAspect = munger.getDeclaringType();
                    if (typeWeaverState.isAspectAlreadyApplied(declaringAspect)) {
                    }
                }
                Pointcut pointcut = munger.getPointcut();
                long starttime = System.nanoTime();
                FuzzyBoolean fb = pointcut.fastMatch(info);
                long endtime = System.nanoTime();
                this.world.recordFastMatch(pointcut, endtime - starttime);
                if (fb.maybeTrue()) {
                    result.add(munger);
                }
            }
        } else {
            for (ShadowMunger munger2 : list) {
                if (typeWeaverState != null) {
                    ResolvedType declaringAspect2 = munger2.getConcreteAspect();
                    if (typeWeaverState.isAspectAlreadyApplied(declaringAspect2)) {
                    }
                }
                FuzzyBoolean fb2 = munger2.getPointcut().fastMatch(info);
                if (fb2.maybeTrue()) {
                    result.add(munger2);
                }
            }
        }
        return result;
    }

    public void setReweavableMode(boolean xNotReweavable) {
        this.inReweavableMode = !xNotReweavable;
        WeaverStateInfo.setReweavableModeDefaults(!xNotReweavable, false, true);
    }

    public boolean isReweavable() {
        return this.inReweavableMode;
    }

    public World getWorld() {
        return this.world;
    }

    public void tidyUp() {
        if (trace.isTraceEnabled()) {
            trace.enter("tidyUp", this);
        }
        this.shadowMungerList = null;
        this.typeMungerList = null;
        this.lateTypeMungerList = null;
        this.declareParentsList = null;
        if (trace.isTraceEnabled()) {
            trace.exit("tidyUp");
        }
    }

    public void write(CompressingDataOutputStream dos) throws IOException {
        this.xcutSet.write(dos);
    }

    public void setShadowMungers(List<ShadowMunger> shadowMungers) {
        this.shadowMungerList = shadowMungers;
    }
}
