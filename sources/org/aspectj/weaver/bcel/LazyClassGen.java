package org.aspectj.weaver.bcel;

import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;
import org.apache.ibatis.javassist.bytecode.SignatureAttribute;
import org.apache.ibatis.javassist.bytecode.SyntheticAttribute;
import org.aspectj.apache.bcel.Constants;
import org.aspectj.apache.bcel.classfile.Attribute;
import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.aspectj.apache.bcel.classfile.Field;
import org.aspectj.apache.bcel.classfile.JavaClass;
import org.aspectj.apache.bcel.classfile.Method;
import org.aspectj.apache.bcel.classfile.Signature;
import org.aspectj.apache.bcel.classfile.Synthetic;
import org.aspectj.apache.bcel.classfile.annotation.AnnotationGen;
import org.aspectj.apache.bcel.generic.BasicType;
import org.aspectj.apache.bcel.generic.ClassGen;
import org.aspectj.apache.bcel.generic.FieldGen;
import org.aspectj.apache.bcel.generic.InstructionConstants;
import org.aspectj.apache.bcel.generic.InstructionFactory;
import org.aspectj.apache.bcel.generic.InstructionHandle;
import org.aspectj.apache.bcel.generic.InstructionList;
import org.aspectj.apache.bcel.generic.ObjectType;
import org.aspectj.apache.bcel.generic.Type;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.SourceLocation;
import org.aspectj.weaver.AjAttribute;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.NameMangler;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.SignatureUtils;
import org.aspectj.weaver.TypeVariable;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.WeaverStateInfo;
import org.aspectj.weaver.World;
import org.aspectj.weaver.bcel.UnwovenClassFile;
import org.aspectj.weaver.bcel.asm.AsmDetector;
import org.aspectj.weaver.bcel.asm.StackMapAdder;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/LazyClassGen.class */
public final class LazyClassGen {
    private static final int ACC_SYNTHETIC = 4096;
    int highestLineNumber;
    private final SortedMap<String, InlinedSourceFileInfo> inlinedFiles;
    private boolean regenerateGenericSignatureAttribute;
    private BcelObjectType myType;
    private ClassGen myGen;
    private final ConstantPool cp;
    private final World world;
    private final String packageName;
    private final List<BcelField> fields;
    private final List<LazyMethodGen> methodGens;
    private final List<LazyClassGen> classGens;
    private final List<AnnotationGen> annotations;
    private int childCounter;
    private final InstructionFactory fact;
    private boolean isSerializable;
    private boolean hasSerialVersionUIDField;
    private boolean serialVersionUIDRequiresInitialization;
    private long calculatedSerialVersionUID;
    private boolean hasClinit;
    private ResolvedType[] extraSuperInterfaces;
    private ResolvedType superclass;
    private Map<BcelShadow, Field> tjpFields;
    Map<CacheKey, Field> annotationCachingFieldCache;
    private int tjpFieldsCounter;
    private int annoFieldsCounter;
    private static final String[] NO_STRINGS = new String[0];
    public static final ObjectType proceedingTjpType = new ObjectType("org.aspectj.lang.ProceedingJoinPoint");
    public static final ObjectType tjpType = new ObjectType("org.aspectj.lang.JoinPoint");
    public static final ObjectType staticTjpType = new ObjectType("org.aspectj.lang.JoinPoint$StaticPart");
    public static final ObjectType typeForAnnotation = new ObjectType("java.lang.annotation.Annotation");
    public static final ObjectType enclosingStaticTjpType = new ObjectType("org.aspectj.lang.JoinPoint$EnclosingStaticPart");
    private static final ObjectType sigType = new ObjectType("org.aspectj.lang.Signature");
    private static final ObjectType factoryType = new ObjectType("org.aspectj.runtime.reflect.Factory");
    private static final ObjectType classType = new ObjectType("java.lang.Class");
    private static final Type[] ARRAY_7STRING_INT = {Type.STRING, Type.STRING, Type.STRING, Type.STRING, Type.STRING, Type.STRING, Type.STRING, Type.INT};
    private static final Type[] ARRAY_8STRING_INT = {Type.STRING, Type.STRING, Type.STRING, Type.STRING, Type.STRING, Type.STRING, Type.STRING, Type.STRING, Type.INT};

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/LazyClassGen$InlinedSourceFileInfo.class */
    static class InlinedSourceFileInfo {
        int highestLineNumber;
        int offset;

        InlinedSourceFileInfo(int highestLineNumber) {
            this.highestLineNumber = highestLineNumber;
        }
    }

    void addInlinedSourceFileInfo(String fullpath, int highestLineNumber) {
        Object o = this.inlinedFiles.get(fullpath);
        if (o != null) {
            InlinedSourceFileInfo info = (InlinedSourceFileInfo) o;
            if (info.highestLineNumber < highestLineNumber) {
                info.highestLineNumber = highestLineNumber;
                return;
            }
            return;
        }
        this.inlinedFiles.put(fullpath, new InlinedSourceFileInfo(highestLineNumber));
    }

    void calculateSourceDebugExtensionOffsets() {
        int i = roundUpToHundreds(this.highestLineNumber);
        for (InlinedSourceFileInfo element : this.inlinedFiles.values()) {
            element.offset = i;
            i = roundUpToHundreds(i + element.highestLineNumber);
        }
    }

    private static int roundUpToHundreds(int i) {
        return ((i / 100) + 1) * 100;
    }

    int getSourceDebugExtensionOffset(String fullpath) {
        return this.inlinedFiles.get(fullpath).offset;
    }

    public static void disassemble(String path, String name, PrintStream out) throws IOException {
        if (null == out) {
            return;
        }
        BcelWorld world = new BcelWorld(path);
        UnresolvedType ut = UnresolvedType.forName(name);
        ut.setNeedsModifiableDelegate(true);
        LazyClassGen clazz = new LazyClassGen(BcelWorld.getBcelObjectType(world.resolve(ut)));
        clazz.print(out);
        out.println();
    }

    public String getNewGeneratedNameTag() {
        int i = this.childCounter;
        this.childCounter = i + 1;
        return new Integer(i).toString();
    }

    public LazyClassGen(String class_name, String super_class_name, String file_name, int access_flags, String[] interfaces, World world) {
        this.highestLineNumber = 0;
        this.inlinedFiles = new TreeMap();
        this.regenerateGenericSignatureAttribute = false;
        this.packageName = null;
        this.fields = new ArrayList();
        this.methodGens = new ArrayList();
        this.classGens = new ArrayList();
        this.annotations = new ArrayList();
        this.childCounter = 0;
        this.isSerializable = false;
        this.hasSerialVersionUIDField = false;
        this.serialVersionUIDRequiresInitialization = false;
        this.hasClinit = false;
        this.extraSuperInterfaces = null;
        this.superclass = null;
        this.tjpFields = new HashMap();
        this.annotationCachingFieldCache = new HashMap();
        this.tjpFieldsCounter = -1;
        this.annoFieldsCounter = 0;
        this.myGen = new ClassGen(class_name, super_class_name, file_name, access_flags, interfaces);
        this.cp = this.myGen.getConstantPool();
        this.fact = new InstructionFactory(this.myGen, this.cp);
        this.regenerateGenericSignatureAttribute = true;
        this.world = world;
    }

    public LazyClassGen(BcelObjectType myType) {
        this.highestLineNumber = 0;
        this.inlinedFiles = new TreeMap();
        this.regenerateGenericSignatureAttribute = false;
        this.packageName = null;
        this.fields = new ArrayList();
        this.methodGens = new ArrayList();
        this.classGens = new ArrayList();
        this.annotations = new ArrayList();
        this.childCounter = 0;
        this.isSerializable = false;
        this.hasSerialVersionUIDField = false;
        this.serialVersionUIDRequiresInitialization = false;
        this.hasClinit = false;
        this.extraSuperInterfaces = null;
        this.superclass = null;
        this.tjpFields = new HashMap();
        this.annotationCachingFieldCache = new HashMap();
        this.tjpFieldsCounter = -1;
        this.annoFieldsCounter = 0;
        this.myGen = new ClassGen(myType.getJavaClass());
        this.cp = this.myGen.getConstantPool();
        this.fact = new InstructionFactory(this.myGen, this.cp);
        this.myType = myType;
        this.world = myType.getResolvedTypeX().getWorld();
        if (implementsSerializable(getType())) {
            this.isSerializable = true;
            this.hasSerialVersionUIDField = hasSerialVersionUIDField(getType());
            ResolvedMember[] methods = getType().getDeclaredMethods();
            for (ResolvedMember method : methods) {
                if (method.getName().equals("<clinit>")) {
                    if (method.getKind() != Member.STATIC_INITIALIZATION) {
                        throw new RuntimeException("qui?");
                    }
                    this.hasClinit = true;
                }
            }
            if (!getType().isInterface() && !this.hasSerialVersionUIDField && this.world.isAddSerialVerUID()) {
                this.calculatedSerialVersionUID = this.myGen.getSUID();
                FieldGen fg = new FieldGen(26, BasicType.LONG, "serialVersionUID", getConstantPool());
                addField(fg);
                this.hasSerialVersionUIDField = true;
                this.serialVersionUIDRequiresInitialization = true;
                if (this.world.getLint().calculatingSerialVersionUID.isEnabled()) {
                    this.world.getLint().calculatingSerialVersionUID.signal(new String[]{getClassName(), Long.toString(this.calculatedSerialVersionUID) + StandardRoles.L}, null, null);
                }
            }
        }
        for (ResolvedMember resolvedMember : myType.getDeclaredMethods()) {
            addMethodGen(new LazyMethodGen((BcelMethod) resolvedMember, this));
        }
        ResolvedMember[] fields = myType.getDeclaredFields();
        for (ResolvedMember resolvedMember2 : fields) {
            this.fields.add((BcelField) resolvedMember2);
        }
    }

    public static boolean hasSerialVersionUIDField(ResolvedType type) {
        ResolvedMember[] fields = type.getDeclaredFields();
        for (ResolvedMember field : fields) {
            if (field.getName().equals("serialVersionUID") && Modifier.isStatic(field.getModifiers()) && field.getType().equals(UnresolvedType.LONG)) {
                return true;
            }
        }
        return false;
    }

    public String getInternalClassName() {
        return getConstantPool().getConstantString_CONSTANTClass(this.myGen.getClassNameIndex());
    }

    public String getInternalFileName() {
        String str = getInternalClassName();
        int index = str.lastIndexOf(47);
        if (index == -1) {
            return getFileName();
        }
        return str.substring(0, index + 1) + getFileName();
    }

    public String getPackageName() {
        if (this.packageName != null) {
            return this.packageName;
        }
        String str = getInternalClassName();
        int index = str.indexOf("<");
        if (index != -1) {
            str = str.substring(0, index);
        }
        int index2 = str.lastIndexOf("/");
        if (index2 == -1) {
            return "";
        }
        return str.substring(0, index2).replace('/', '.');
    }

    public void addMethodGen(LazyMethodGen gen) {
        this.methodGens.add(gen);
        if (this.highestLineNumber < gen.highestLineNumber) {
            this.highestLineNumber = gen.highestLineNumber;
        }
    }

    public boolean removeMethodGen(LazyMethodGen gen) {
        return this.methodGens.remove(gen);
    }

    public void addMethodGen(LazyMethodGen gen, ISourceLocation sourceLocation) {
        addMethodGen(gen);
        if (!gen.getMethod().isPrivate()) {
            warnOnAddedMethod(gen.getMethod(), sourceLocation);
        }
    }

    public void errorOnAddedField(FieldGen field, ISourceLocation sourceLocation) {
        if (this.isSerializable && !this.hasSerialVersionUIDField) {
            getWorld().getLint().serialVersionUIDBroken.signal(new String[]{this.myType.getResolvedTypeX().getName(), field.getName()}, sourceLocation, null);
        }
    }

    public void warnOnAddedInterface(String name, ISourceLocation sourceLocation) {
        warnOnModifiedSerialVersionUID(sourceLocation, "added interface " + name);
    }

    public void warnOnAddedMethod(Method method, ISourceLocation sourceLocation) {
        warnOnModifiedSerialVersionUID(sourceLocation, "added non-private method " + method.getName());
    }

    public void warnOnAddedStaticInitializer(Shadow shadow, ISourceLocation sourceLocation) {
        if (!this.hasClinit) {
            warnOnModifiedSerialVersionUID(sourceLocation, "added static initializer");
        }
    }

    public void warnOnModifiedSerialVersionUID(ISourceLocation sourceLocation, String reason) {
        if (this.isSerializable && !this.hasSerialVersionUIDField) {
            getWorld().getLint().needsSerialVersionUIDField.signal(new String[]{this.myType.getResolvedTypeX().getName().toString(), reason}, sourceLocation, null);
        }
    }

    public World getWorld() {
        return this.world;
    }

    public List<LazyMethodGen> getMethodGens() {
        return this.methodGens;
    }

    public List<BcelField> getFieldGens() {
        return this.fields;
    }

    public boolean fieldExists(String name) {
        for (BcelField f : this.fields) {
            if (f.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private void writeBack(BcelWorld world) {
        if (getConstantPool().getSize() > 32767) {
            reportClassTooBigProblem();
            return;
        }
        if (this.annotations.size() > 0) {
            for (AnnotationGen element : this.annotations) {
                this.myGen.addAnnotation(element);
            }
        }
        if (!this.myGen.hasAttribute(AjAttribute.WeaverVersionInfo.AttributeName)) {
            this.myGen.addAttribute(Utility.bcelAttribute(new AjAttribute.WeaverVersionInfo(), getConstantPool()));
        }
        if ((!world.isOverWeaving() || !this.myGen.hasAttribute(AjAttribute.WeaverState.AttributeName)) && this.myType != null && this.myType.getWeaverState() != null) {
            this.myGen.addAttribute(Utility.bcelAttribute(new AjAttribute.WeaverState(this.myType.getWeaverState()), getConstantPool()));
        }
        addAjcInitializers();
        if (0 != 0) {
            calculateSourceDebugExtensionOffsets();
        }
        this.methodGens.size();
        this.myGen.setMethods(Method.NoMethods);
        for (LazyMethodGen gen : this.methodGens) {
            if (!isEmptyClinit(gen)) {
                this.myGen.addMethod(gen.getMethod());
            }
        }
        int len = this.fields.size();
        this.myGen.setFields(Field.NoFields);
        for (int i = 0; i < len; i++) {
            this.myGen.addField(this.fields.get(i).getField(this.cp));
        }
        if (0 != 0 && this.inlinedFiles.size() != 0 && hasSourceDebugExtensionAttribute(this.myGen)) {
            world.showMessage(IMessage.WARNING, WeaverMessages.format(WeaverMessages.OVERWRITE_JSR45, getFileName()), null, null);
        }
        fixupGenericSignatureAttribute();
    }

    private void fixupGenericSignatureAttribute() {
        if ((getWorld() != null && !getWorld().isInJava5Mode()) || !this.regenerateGenericSignatureAttribute) {
            return;
        }
        Signature sigAttr = null;
        if (this.myType != null) {
            sigAttr = (Signature) this.myGen.getAttribute(SignatureAttribute.tag);
        }
        boolean needAttribute = false;
        if (sigAttr != null) {
            needAttribute = true;
        }
        if (!needAttribute) {
            if (this.myType != null) {
                ResolvedType[] interfaceRTXs = this.myType.getDeclaredInterfaces();
                for (ResolvedType typeX : interfaceRTXs) {
                    if (typeX.isGenericType() || typeX.isParameterizedType()) {
                        needAttribute = true;
                    }
                }
                if (this.extraSuperInterfaces != null) {
                    for (int i = 0; i < this.extraSuperInterfaces.length; i++) {
                        ResolvedType interfaceType = this.extraSuperInterfaces[i];
                        if (interfaceType.isGenericType() || interfaceType.isParameterizedType()) {
                            needAttribute = true;
                        }
                    }
                }
            }
            if (this.myType == null) {
                ResolvedType superclassRTX = this.superclass;
                if (superclassRTX != null && (superclassRTX.isGenericType() || superclassRTX.isParameterizedType())) {
                    needAttribute = true;
                }
            } else {
                ResolvedType superclassRTX2 = getSuperClass();
                if (superclassRTX2.isGenericType() || superclassRTX2.isParameterizedType()) {
                    needAttribute = true;
                }
            }
        }
        if (needAttribute) {
            StringBuffer signature = new StringBuffer();
            if (this.myType != null) {
                TypeVariable[] tVars = this.myType.getTypeVariables();
                if (tVars.length > 0) {
                    signature.append("<");
                    for (TypeVariable variable : tVars) {
                        signature.append(variable.getSignatureForAttribute());
                    }
                    signature.append(">");
                }
            }
            String supersig = getSuperClass().getSignatureForAttribute();
            signature.append(supersig);
            if (this.myType != null) {
                for (ResolvedType resolvedType : this.myType.getDeclaredInterfaces()) {
                    String s = resolvedType.getSignatureForAttribute();
                    signature.append(s);
                }
                if (this.extraSuperInterfaces != null) {
                    for (int i2 = 0; i2 < this.extraSuperInterfaces.length; i2++) {
                        String s2 = this.extraSuperInterfaces[i2].getSignatureForAttribute();
                        signature.append(s2);
                    }
                }
            }
            if (sigAttr != null) {
                this.myGen.removeAttribute(sigAttr);
            }
            this.myGen.addAttribute(createSignatureAttribute(signature.toString()));
        }
    }

    private Signature createSignatureAttribute(String signature) {
        int nameIndex = this.cp.addUtf8(SignatureAttribute.tag);
        int sigIndex = this.cp.addUtf8(signature);
        return new Signature(nameIndex, 2, sigIndex, this.cp);
    }

    private void reportClassTooBigProblem() {
        this.myGen = new ClassGen(this.myGen.getClassName(), this.myGen.getSuperclassName(), this.myGen.getFileName(), this.myGen.getModifiers(), this.myGen.getInterfaceNames());
        getWorld().showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.CLASS_TOO_BIG, getClassName()), new SourceLocation(new File(this.myGen.getFileName()), 0), null);
    }

    private static boolean hasSourceDebugExtensionAttribute(ClassGen gen) {
        return gen.hasAttribute("SourceDebugExtension");
    }

    public JavaClass getJavaClass(BcelWorld world) {
        writeBack(world);
        return this.myGen.getJavaClass();
    }

    public byte[] getJavaClassBytesIncludingReweavable(BcelWorld world) {
        writeBack(world);
        byte[] wovenClassFileData = this.myGen.getJavaClass().getBytes();
        if ((this.myGen.getMajor() == 50 && world.shouldGenerateStackMaps()) || this.myGen.getMajor() > 50) {
            if (!AsmDetector.isAsmAround) {
                throw new BCException("Unable to find Asm for stackmap generation (Looking for 'aj.org.objectweb.asm.ClassReader'). Stackmap generation for woven code is required to avoid verify errors on a Java 1.7 or higher runtime");
            }
            wovenClassFileData = StackMapAdder.addStackMaps(world, wovenClassFileData);
        }
        WeaverStateInfo wsi = this.myType.getWeaverState();
        if (wsi != null && wsi.isReweavable()) {
            return wsi.replaceKeyWithDiff(wovenClassFileData);
        }
        return wovenClassFileData;
    }

    public void addGeneratedInner(LazyClassGen newClass) {
        this.classGens.add(newClass);
    }

    public void addInterface(ResolvedType newInterface, ISourceLocation sourceLocation) {
        this.regenerateGenericSignatureAttribute = true;
        if (this.extraSuperInterfaces == null) {
            this.extraSuperInterfaces = new ResolvedType[1];
            this.extraSuperInterfaces[0] = newInterface;
        } else {
            ResolvedType[] x = new ResolvedType[this.extraSuperInterfaces.length + 1];
            System.arraycopy(this.extraSuperInterfaces, 0, x, 1, this.extraSuperInterfaces.length);
            x[0] = newInterface;
            this.extraSuperInterfaces = x;
        }
        this.myGen.addInterface(newInterface.getRawName());
        if (!newInterface.equals(UnresolvedType.SERIALIZABLE)) {
            warnOnAddedInterface(newInterface.getName(), sourceLocation);
        }
    }

    public void setSuperClass(ResolvedType newSuperclass) {
        this.regenerateGenericSignatureAttribute = true;
        this.superclass = newSuperclass;
        if (newSuperclass.getGenericType() != null) {
            newSuperclass = newSuperclass.getGenericType();
        }
        this.myGen.setSuperclassName(newSuperclass.getName());
    }

    public ResolvedType getSuperClass() {
        if (this.superclass != null) {
            return this.superclass;
        }
        return this.myType.getSuperclass();
    }

    public String[] getInterfaceNames() {
        return this.myGen.getInterfaceNames();
    }

    private List<LazyClassGen> getClassGens() {
        List<LazyClassGen> ret = new ArrayList<>();
        ret.add(this);
        ret.addAll(this.classGens);
        return ret;
    }

    public List<UnwovenClassFile.ChildClass> getChildClasses(BcelWorld world) {
        if (this.classGens.isEmpty()) {
            return Collections.emptyList();
        }
        List<UnwovenClassFile.ChildClass> ret = new ArrayList<>();
        for (LazyClassGen clazz : this.classGens) {
            byte[] bytes = clazz.getJavaClass(world).getBytes();
            String name = clazz.getName();
            int index = name.lastIndexOf(36);
            ret.add(new UnwovenClassFile.ChildClass(name.substring(index + 1), bytes));
        }
        return ret;
    }

    public String toString() {
        return toShortString();
    }

    public String toShortString() {
        String s = org.aspectj.apache.bcel.classfile.Utility.accessToString(this.myGen.getModifiers(), true);
        if (!s.equals("")) {
            s = s + SymbolConstants.SPACE_SYMBOL;
        }
        return ((s + org.aspectj.apache.bcel.classfile.Utility.classOrInterface(this.myGen.getModifiers())) + SymbolConstants.SPACE_SYMBOL) + this.myGen.getClassName();
    }

    public String toLongString() {
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        print(new PrintStream(s));
        return new String(s.toByteArray());
    }

    public void print() {
        print(System.out);
    }

    public void print(PrintStream out) {
        List<LazyClassGen> classGens = getClassGens();
        Iterator<LazyClassGen> iter = classGens.iterator();
        while (iter.hasNext()) {
            LazyClassGen element = iter.next();
            element.printOne(out);
            if (iter.hasNext()) {
                out.println();
            }
        }
    }

    private void printOne(PrintStream out) {
        out.print(toShortString());
        out.print(" extends ");
        out.print(org.aspectj.apache.bcel.classfile.Utility.compactClassName(this.myGen.getSuperclassName(), false));
        int size = this.myGen.getInterfaces().length;
        if (size > 0) {
            out.print(" implements ");
            for (int i = 0; i < size; i++) {
                out.print(this.myGen.getInterfaceNames()[i]);
                if (i < size - 1) {
                    out.print(", ");
                }
            }
        }
        out.print(":");
        out.println();
        if (this.myType != null) {
            this.myType.printWackyStuff(out);
        }
        Field[] fields = this.myGen.getFields();
        for (Field field : fields) {
            out.print("  ");
            out.println(field);
        }
        List<LazyMethodGen> methodGens = getMethodGens();
        Iterator<LazyMethodGen> iter = methodGens.iterator();
        while (iter.hasNext()) {
            LazyMethodGen gen = iter.next();
            if (!isEmptyClinit(gen)) {
                gen.print(out, this.myType != null ? this.myType.getWeaverVersionAttribute() : AjAttribute.WeaverVersionInfo.UNKNOWN);
                if (iter.hasNext()) {
                    out.println();
                }
            }
        }
        out.println("end " + toShortString());
    }

    private boolean isEmptyClinit(LazyMethodGen gen) {
        if (!gen.getName().equals("<clinit>")) {
            return false;
        }
        InstructionHandle start = gen.getBody().getStart();
        while (true) {
            InstructionHandle start2 = start;
            if (start2 != null) {
                if (Range.isRangeHandle(start2) || start2.getInstruction().opcode == 177) {
                    start = start2.getNext();
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    public ConstantPool getConstantPool() {
        return this.cp;
    }

    public String getName() {
        return this.myGen.getClassName();
    }

    public boolean isWoven() {
        return this.myType.getWeaverState() != null;
    }

    public boolean isReweavable() {
        if (this.myType.getWeaverState() == null) {
            return true;
        }
        return this.myType.getWeaverState().isReweavable();
    }

    public Set<String> getAspectsAffectingType() {
        if (this.myType.getWeaverState() == null) {
            return null;
        }
        return this.myType.getWeaverState().getAspectsAffectingType();
    }

    public WeaverStateInfo getOrCreateWeaverStateInfo(boolean inReweavableMode) {
        WeaverStateInfo ret = this.myType.getWeaverState();
        if (ret != null) {
            return ret;
        }
        WeaverStateInfo ret2 = new WeaverStateInfo(inReweavableMode);
        this.myType.setWeaverState(ret2);
        return ret2;
    }

    public InstructionFactory getFactory() {
        return this.fact;
    }

    public LazyMethodGen getStaticInitializer() {
        for (LazyMethodGen gen : this.methodGens) {
            if (gen.getName().equals("<clinit>")) {
                return gen;
            }
        }
        LazyMethodGen clinit = new LazyMethodGen(8, Type.VOID, "<clinit>", new Type[0], NO_STRINGS, this);
        clinit.getBody().insert(InstructionConstants.RETURN);
        this.methodGens.add(clinit);
        return clinit;
    }

    public LazyMethodGen getAjcPreClinit() {
        if (isInterface()) {
            throw new IllegalStateException();
        }
        for (LazyMethodGen methodGen : this.methodGens) {
            if (methodGen.getName().equals(NameMangler.AJC_PRE_CLINIT_NAME)) {
                return methodGen;
            }
        }
        LazyMethodGen ajcPreClinit = new LazyMethodGen(10, Type.VOID, NameMangler.AJC_PRE_CLINIT_NAME, Type.NO_ARGS, NO_STRINGS, this);
        ajcPreClinit.getBody().insert(InstructionConstants.RETURN);
        this.methodGens.add(ajcPreClinit);
        getStaticInitializer().getBody().insert(Utility.createInvoke(this.fact, ajcPreClinit));
        return ajcPreClinit;
    }

    public LazyMethodGen createExtendedAjcPreClinit(LazyMethodGen previousPreClinit, int i) {
        LazyMethodGen ajcPreClinit = new LazyMethodGen(10, Type.VOID, NameMangler.AJC_PRE_CLINIT_NAME + i, Type.NO_ARGS, NO_STRINGS, this);
        ajcPreClinit.getBody().insert(InstructionConstants.RETURN);
        this.methodGens.add(ajcPreClinit);
        previousPreClinit.getBody().insert(Utility.createInvoke(this.fact, ajcPreClinit));
        return ajcPreClinit;
    }

    public Field getTjpField(BcelShadow shadow, boolean isEnclosingJp) {
        int modifiers;
        ObjectType jpType;
        List<BcelField> existingFields;
        Field tjpField = this.tjpFields.get(shadow);
        if (tjpField != null) {
            return tjpField;
        }
        LazyMethodGen encMethod = shadow.getEnclosingMethod();
        boolean shadowIsInAroundAdvice = false;
        if (encMethod != null && encMethod.getName().startsWith("ajc$around")) {
            shadowIsInAroundAdvice = true;
        }
        if (getType().isInterface() || shadowIsInAroundAdvice) {
            modifiers = 24 | 1;
        } else {
            modifiers = 24 | 2;
        }
        if (this.world.isTargettingAspectJRuntime12()) {
            jpType = staticTjpType;
        } else {
            jpType = isEnclosingJp ? enclosingStaticTjpType : staticTjpType;
        }
        if (this.tjpFieldsCounter == -1) {
            if (!this.world.isOverWeaving() || (existingFields = getFieldGens()) == null) {
                this.tjpFieldsCounter = 0;
            } else {
                BcelField lastField = null;
                for (BcelField field : existingFields) {
                    if (field.getName().startsWith("ajc$tjp_")) {
                        lastField = field;
                    }
                }
                if (lastField == null) {
                    this.tjpFieldsCounter = 0;
                } else {
                    this.tjpFieldsCounter = Integer.parseInt(lastField.getName().substring(8)) + 1;
                }
            }
        }
        if (!isInterface() && this.world.isTransientTjpFields()) {
            modifiers |= 128;
        }
        StringBuilder sbAppend = new StringBuilder().append("ajc$tjp_");
        int i = this.tjpFieldsCounter;
        this.tjpFieldsCounter = i + 1;
        FieldGen fGen = new FieldGen(modifiers, jpType, sbAppend.append(i).toString(), getConstantPool());
        addField(fGen);
        Field tjpField2 = fGen.getField();
        this.tjpFields.put(shadow, tjpField2);
        return tjpField2;
    }

    public Field getAnnotationCachingField(BcelShadow shadow, ResolvedType toType, boolean isWithin) {
        CacheKey cacheKey = new CacheKey(shadow, toType, isWithin);
        Field field = this.annotationCachingFieldCache.get(cacheKey);
        if (field == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(NameMangler.ANNOTATION_CACHE_FIELD_NAME);
            int i = this.annoFieldsCounter;
            this.annoFieldsCounter = i + 1;
            sb.append(i);
            FieldGen annotationCacheField = new FieldGen(10, typeForAnnotation, sb.toString(), this.cp);
            addField(annotationCacheField);
            field = annotationCacheField.getField();
            this.annotationCachingFieldCache.put(cacheKey, field);
        }
        return field;
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/LazyClassGen$CacheKey.class */
    static class CacheKey {
        private Object key;
        private ResolvedType annotationType;

        CacheKey(BcelShadow shadow, ResolvedType annotationType, boolean isWithin) {
            this.key = isWithin ? shadow : shadow.toString();
            this.annotationType = annotationType;
        }

        public int hashCode() {
            return (this.key.hashCode() * 37) + this.annotationType.hashCode();
        }

        public boolean equals(Object other) {
            if (!(other instanceof CacheKey)) {
                return false;
            }
            CacheKey oCacheKey = (CacheKey) other;
            return this.key.equals(oCacheKey.key) && this.annotationType.equals(oCacheKey.annotationType);
        }
    }

    private void addAjcInitializers() {
        LazyMethodGen prevMethod;
        if (this.tjpFields.size() == 0 && !this.serialVersionUIDRequiresInitialization) {
            return;
        }
        InstructionList[] il = null;
        if (this.tjpFields.size() > 0) {
            il = initializeAllTjps();
        }
        if (this.serialVersionUIDRequiresInitialization) {
            InstructionList[] ilSVUID = {new InstructionList()};
            ilSVUID[0].append(InstructionFactory.PUSH(getConstantPool(), this.calculatedSerialVersionUID));
            ilSVUID[0].append(getFactory().createFieldAccess(getClassName(), "serialVersionUID", BasicType.LONG, (short) 179));
            if (il == null) {
                il = ilSVUID;
            } else {
                InstructionList[] newIl = new InstructionList[il.length + ilSVUID.length];
                System.arraycopy(il, 0, newIl, 0, il.length);
                System.arraycopy(ilSVUID, 0, newIl, il.length, ilSVUID.length);
                il = newIl;
            }
        }
        LazyMethodGen nextMethod = null;
        if (isInterface()) {
            prevMethod = getStaticInitializer();
        } else {
            prevMethod = getAjcPreClinit();
        }
        for (int counter = 1; counter <= il.length; counter++) {
            if (il.length > counter) {
                nextMethod = createExtendedAjcPreClinit(prevMethod, counter);
            }
            prevMethod.getBody().insert(il[counter - 1]);
            prevMethod = nextMethod;
        }
    }

    private InstructionList initInstructionList() {
        InstructionList list = new InstructionList();
        InstructionFactory fact = getFactory();
        list.append(fact.createNew(factoryType));
        list.append(InstructionFactory.createDup(1));
        list.append(InstructionFactory.PUSH(getConstantPool(), getFileName()));
        list.append(fact.PUSHCLASS(this.cp, this.myGen.getClassName()));
        list.append(fact.createInvoke(factoryType.getClassName(), "<init>", Type.VOID, new Type[]{Type.STRING, classType}, (short) 183));
        list.append(InstructionFactory.createStore(factoryType, 0));
        return list;
    }

    private InstructionList[] initializeAllTjps() {
        Vector<InstructionList> lists = new Vector<>();
        InstructionList list = initInstructionList();
        lists.add(list);
        List<Map.Entry<BcelShadow, Field>> entries = new ArrayList<>(this.tjpFields.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<BcelShadow, Field>>() { // from class: org.aspectj.weaver.bcel.LazyClassGen.1
            @Override // java.util.Comparator
            public int compare(Map.Entry<BcelShadow, Field> a, Map.Entry<BcelShadow, Field> b) {
                return a.getValue().getName().compareTo(b.getValue().getName());
            }
        });
        long estimatedSize = 0;
        for (Map.Entry<BcelShadow, Field> entry : entries) {
            if (estimatedSize > Constants.EXCEPTION_THROWER) {
                estimatedSize = 0;
                list = initInstructionList();
                lists.add(list);
            }
            estimatedSize += entry.getValue().getSignature().getBytes().length;
            initializeTjp(this.fact, list, entry.getValue(), entry.getKey());
        }
        InstructionList[] listArrayModel = new InstructionList[1];
        return (InstructionList[]) lists.toArray(listArrayModel);
    }

    private void initializeTjp(InstructionFactory fact, InstructionList list, Field field, BcelShadow shadow) {
        String factoryMethod;
        boolean fastSJP = false;
        boolean isFastSJPAvailable = shadow.getWorld().isTargettingRuntime1_6_10() && !enclosingStaticTjpType.equals(field.getType());
        Member sig = shadow.getSignature();
        list.append(InstructionFactory.createLoad(factoryType, 0));
        list.append(InstructionFactory.PUSH(getConstantPool(), shadow.getKind().getName()));
        if (this.world.isTargettingAspectJRuntime12() || !isFastSJPAvailable || !sig.getKind().equals(Member.METHOD)) {
            list.append(InstructionFactory.createLoad(factoryType, 0));
        }
        String signatureMakerName = SignatureUtils.getSignatureMakerName(sig);
        ObjectType signatureType = new ObjectType(SignatureUtils.getSignatureType(sig));
        UnresolvedType[] exceptionTypes = null;
        if (this.world.isTargettingAspectJRuntime12()) {
            list.append(InstructionFactory.PUSH(this.cp, SignatureUtils.getSignatureString(sig, shadow.getWorld())));
            list.append(fact.createInvoke(factoryType.getClassName(), signatureMakerName, signatureType, Type.STRINGARRAY1, (short) 182));
        } else if (sig.getKind().equals(Member.METHOD)) {
            BcelWorld w = shadow.getWorld();
            list.append(InstructionFactory.PUSH(this.cp, makeString(sig.getModifiers(w))));
            list.append(InstructionFactory.PUSH(this.cp, sig.getName()));
            list.append(InstructionFactory.PUSH(this.cp, makeString(sig.getDeclaringType())));
            list.append(InstructionFactory.PUSH(this.cp, makeString(sig.getParameterTypes())));
            list.append(InstructionFactory.PUSH(this.cp, makeString(sig.getParameterNames(w))));
            exceptionTypes = sig.getExceptions(w);
            if (isFastSJPAvailable && exceptionTypes.length == 0) {
                fastSJP = true;
            } else {
                list.append(InstructionFactory.PUSH(this.cp, makeString(exceptionTypes)));
            }
            list.append(InstructionFactory.PUSH(this.cp, makeString(sig.getReturnType())));
            if (isFastSJPAvailable) {
                fastSJP = true;
            } else {
                list.append(fact.createInvoke(factoryType.getClassName(), signatureMakerName, signatureType, Type.STRINGARRAY7, (short) 182));
            }
        } else if (sig.getKind().equals(Member.MONITORENTER) || sig.getKind().equals(Member.MONITOREXIT)) {
            list.append(InstructionFactory.PUSH(this.cp, makeString(sig.getDeclaringType())));
            list.append(fact.createInvoke(factoryType.getClassName(), signatureMakerName, signatureType, Type.STRINGARRAY1, (short) 182));
        } else if (sig.getKind().equals(Member.HANDLER)) {
            BcelWorld w2 = shadow.getWorld();
            list.append(InstructionFactory.PUSH(this.cp, makeString(sig.getDeclaringType())));
            list.append(InstructionFactory.PUSH(this.cp, makeString(sig.getParameterTypes())));
            list.append(InstructionFactory.PUSH(this.cp, makeString(sig.getParameterNames(w2))));
            list.append(fact.createInvoke(factoryType.getClassName(), signatureMakerName, signatureType, Type.STRINGARRAY3, (short) 182));
        } else if (sig.getKind().equals(Member.CONSTRUCTOR)) {
            BcelWorld w3 = shadow.getWorld();
            if (w3.isJoinpointArrayConstructionEnabled() && sig.getDeclaringType().isArray()) {
                list.append(InstructionFactory.PUSH(this.cp, makeString(1)));
                list.append(InstructionFactory.PUSH(this.cp, makeString(sig.getDeclaringType())));
                list.append(InstructionFactory.PUSH(this.cp, makeString(sig.getParameterTypes())));
                list.append(InstructionFactory.PUSH(this.cp, ""));
                list.append(InstructionFactory.PUSH(this.cp, ""));
                list.append(fact.createInvoke(factoryType.getClassName(), signatureMakerName, signatureType, Type.STRINGARRAY5, (short) 182));
            } else {
                list.append(InstructionFactory.PUSH(this.cp, makeString(sig.getModifiers(w3))));
                list.append(InstructionFactory.PUSH(this.cp, makeString(sig.getDeclaringType())));
                list.append(InstructionFactory.PUSH(this.cp, makeString(sig.getParameterTypes())));
                list.append(InstructionFactory.PUSH(this.cp, makeString(sig.getParameterNames(w3))));
                list.append(InstructionFactory.PUSH(this.cp, makeString(sig.getExceptions(w3))));
                list.append(fact.createInvoke(factoryType.getClassName(), signatureMakerName, signatureType, Type.STRINGARRAY5, (short) 182));
            }
        } else if (sig.getKind().equals(Member.FIELD)) {
            list.append(InstructionFactory.PUSH(this.cp, makeString(sig.getModifiers(shadow.getWorld()))));
            list.append(InstructionFactory.PUSH(this.cp, sig.getName()));
            UnresolvedType dType = sig.getDeclaringType();
            if (dType.getTypekind() == UnresolvedType.TypeKind.PARAMETERIZED || dType.getTypekind() == UnresolvedType.TypeKind.GENERIC) {
                dType = sig.getDeclaringType().resolve(this.world).getGenericType();
            }
            list.append(InstructionFactory.PUSH(this.cp, makeString(dType)));
            list.append(InstructionFactory.PUSH(this.cp, makeString(sig.getReturnType())));
            list.append(fact.createInvoke(factoryType.getClassName(), signatureMakerName, signatureType, Type.STRINGARRAY4, (short) 182));
        } else if (sig.getKind().equals(Member.ADVICE)) {
            BcelWorld w4 = shadow.getWorld();
            list.append(InstructionFactory.PUSH(this.cp, makeString(sig.getModifiers(w4))));
            list.append(InstructionFactory.PUSH(this.cp, sig.getName()));
            list.append(InstructionFactory.PUSH(this.cp, makeString(sig.getDeclaringType())));
            list.append(InstructionFactory.PUSH(this.cp, makeString(sig.getParameterTypes())));
            list.append(InstructionFactory.PUSH(this.cp, makeString(sig.getParameterNames(w4))));
            list.append(InstructionFactory.PUSH(this.cp, makeString(sig.getExceptions(w4))));
            list.append(InstructionFactory.PUSH(this.cp, makeString(sig.getReturnType())));
            list.append(fact.createInvoke(factoryType.getClassName(), signatureMakerName, signatureType, new Type[]{Type.STRING, Type.STRING, Type.STRING, Type.STRING, Type.STRING, Type.STRING, Type.STRING}, (short) 182));
        } else if (sig.getKind().equals(Member.STATIC_INITIALIZATION)) {
            list.append(InstructionFactory.PUSH(this.cp, makeString(sig.getModifiers(shadow.getWorld()))));
            list.append(InstructionFactory.PUSH(this.cp, makeString(sig.getDeclaringType())));
            list.append(fact.createInvoke(factoryType.getClassName(), signatureMakerName, signatureType, Type.STRINGARRAY2, (short) 182));
        } else {
            list.append(InstructionFactory.PUSH(this.cp, SignatureUtils.getSignatureString(sig, shadow.getWorld())));
            list.append(fact.createInvoke(factoryType.getClassName(), signatureMakerName, signatureType, Type.STRINGARRAY1, (short) 182));
        }
        list.append(Utility.createConstant(fact, shadow.getSourceLine()));
        if (this.world.isTargettingAspectJRuntime12()) {
            list.append(fact.createInvoke(factoryType.getClassName(), "makeSJP", staticTjpType, new Type[]{Type.STRING, sigType, Type.INT}, (short) 182));
            list.append(fact.createFieldAccess(getClassName(), field.getName(), staticTjpType, (short) 179));
            return;
        }
        if (staticTjpType.equals(field.getType())) {
            factoryMethod = "makeSJP";
        } else if (enclosingStaticTjpType.equals(field.getType())) {
            factoryMethod = "makeESJP";
        } else {
            throw new Error("should not happen");
        }
        if (fastSJP) {
            if (exceptionTypes != null && exceptionTypes.length != 0) {
                list.append(fact.createInvoke(factoryType.getClassName(), factoryMethod, field.getType(), ARRAY_8STRING_INT, (short) 182));
            } else {
                list.append(fact.createInvoke(factoryType.getClassName(), factoryMethod, field.getType(), ARRAY_7STRING_INT, (short) 182));
            }
        } else {
            list.append(fact.createInvoke(factoryType.getClassName(), factoryMethod, field.getType(), new Type[]{Type.STRING, sigType, Type.INT}, (short) 182));
        }
        list.append(fact.createFieldAccess(getClassName(), field.getName(), field.getType(), (short) 179));
    }

    protected String makeString(int i) {
        return Integer.toString(i, 16);
    }

    protected String makeString(UnresolvedType t) {
        if (t.isArray()) {
            return t.getSignature().replace('/', '.');
        }
        if (t.isParameterizedType()) {
            return t.getRawType().getName();
        }
        return t.getName();
    }

    protected String makeString(UnresolvedType[] types) {
        if (types == null) {
            return "";
        }
        StringBuilder buf = new StringBuilder();
        int len = types.length;
        for (int i = 0; i < len; i++) {
            if (i > 0) {
                buf.append(':');
            }
            buf.append(makeString(types[i]));
        }
        return buf.toString();
    }

    protected String makeString(String[] names) {
        if (names == null) {
            return "";
        }
        StringBuilder buf = new StringBuilder();
        int len = names.length;
        for (int i = 0; i < len; i++) {
            if (i > 0) {
                buf.append(':');
            }
            buf.append(names[i]);
        }
        return buf.toString();
    }

    public ResolvedType getType() {
        if (this.myType == null) {
            return null;
        }
        return this.myType.getResolvedTypeX();
    }

    public BcelObjectType getBcelObjectType() {
        return this.myType;
    }

    public String getFileName() {
        return this.myGen.getFileName();
    }

    private void addField(FieldGen field) {
        BcelField bcelField;
        makeSyntheticAndTransientIfNeeded(field);
        if (getBcelObjectType() != null) {
            bcelField = new BcelField(getBcelObjectType(), field.getField());
        } else {
            bcelField = new BcelField(getName(), field.getField(), this.world);
        }
        this.fields.add(bcelField);
    }

    private void makeSyntheticAndTransientIfNeeded(FieldGen field) {
        if (field.getName().startsWith(NameMangler.PREFIX) && !field.getName().startsWith("ajc$interField$") && !field.getName().startsWith("ajc$instance$")) {
            if (!field.isStatic()) {
                field.setModifiers(field.getModifiers() | 128);
            }
            if (getWorld().isInJava5Mode()) {
                field.setModifiers(field.getModifiers() | 4096);
            }
            if (!hasSyntheticAttribute(field.getAttributes())) {
                ConstantPool cpg = this.myGen.getConstantPool();
                int index = cpg.addUtf8(SyntheticAttribute.tag);
                Attribute synthetic = new Synthetic(index, 0, new byte[0], cpg);
                field.addAttribute(synthetic);
            }
        }
    }

    private boolean hasSyntheticAttribute(List<Attribute> attributes) {
        for (int i = 0; i < attributes.size(); i++) {
            if (attributes.get(i).getName().equals(SyntheticAttribute.tag)) {
                return true;
            }
        }
        return false;
    }

    public void addField(FieldGen field, ISourceLocation sourceLocation) {
        addField(field);
        if (!field.isPrivate() || (!field.isStatic() && !field.isTransient())) {
            errorOnAddedField(field, sourceLocation);
        }
    }

    public String getClassName() {
        return this.myGen.getClassName();
    }

    public boolean isInterface() {
        return this.myGen.isInterface();
    }

    public boolean isAbstract() {
        return this.myGen.isAbstract();
    }

    public LazyMethodGen getLazyMethodGen(Member m) {
        return getLazyMethodGen(m.getName(), m.getSignature(), false);
    }

    public LazyMethodGen getLazyMethodGen(String name, String signature) {
        return getLazyMethodGen(name, signature, false);
    }

    public LazyMethodGen getLazyMethodGen(String name, String signature, boolean allowMissing) {
        for (LazyMethodGen gen : this.methodGens) {
            if (gen.getName().equals(name) && gen.getSignature().equals(signature)) {
                return gen;
            }
        }
        if (!allowMissing) {
            throw new BCException("Class " + getName() + " does not have a method " + name + " with signature " + signature);
        }
        return null;
    }

    public void forcePublic() {
        this.myGen.setModifiers(Utility.makePublic(this.myGen.getModifiers()));
    }

    public boolean hasAnnotation(UnresolvedType t) {
        AnnotationGen[] agens = this.myGen.getAnnotations();
        if (agens == null) {
            return false;
        }
        for (AnnotationGen gen : agens) {
            if (t.equals(UnresolvedType.forSignature(gen.getTypeSignature()))) {
                return true;
            }
        }
        return false;
    }

    public void addAnnotation(AnnotationGen a) {
        if (!hasAnnotation(UnresolvedType.forSignature(a.getTypeSignature()))) {
            this.annotations.add(new AnnotationGen(a, getConstantPool(), true));
        }
    }

    public void addAttribute(AjAttribute attribute) {
        this.myGen.addAttribute(Utility.bcelAttribute(attribute, getConstantPool()));
    }

    public void addAttribute(Attribute attribute) {
        this.myGen.addAttribute(attribute);
    }

    public Collection<Attribute> getAttributes() {
        return this.myGen.getAttributes();
    }

    private boolean implementsSerializable(ResolvedType aType) {
        if (aType.getSignature().equals(UnresolvedType.SERIALIZABLE.getSignature())) {
            return true;
        }
        ResolvedType[] interfaces = aType.getDeclaredInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            if (!interfaces[i].isMissing() && implementsSerializable(interfaces[i])) {
                return true;
            }
        }
        ResolvedType superType = aType.getSuperclass();
        if (superType != null && !superType.isMissing()) {
            return implementsSerializable(superType);
        }
        return false;
    }

    public boolean isAtLeastJava5() {
        return this.myGen.getMajor() >= 49;
    }

    public String allocateField(String prefix) throws NumberFormatException {
        int highestAllocated = -1;
        List<BcelField> fs = getFieldGens();
        for (BcelField field : fs) {
            if (field.getName().startsWith(prefix)) {
                try {
                    int num = Integer.parseInt(field.getName().substring(prefix.length()));
                    if (num > highestAllocated) {
                        highestAllocated = num;
                    }
                } catch (NumberFormatException e) {
                }
            }
        }
        return prefix + Integer.toString(highestAllocated + 1);
    }
}
