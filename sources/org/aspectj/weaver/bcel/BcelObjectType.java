package org.aspectj.weaver.bcel;

import java.io.PrintStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.aspectj.apache.bcel.classfile.Attribute;
import org.aspectj.apache.bcel.classfile.AttributeUtils;
import org.aspectj.apache.bcel.classfile.ConstantClass;
import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.aspectj.apache.bcel.classfile.EnclosingMethod;
import org.aspectj.apache.bcel.classfile.Field;
import org.aspectj.apache.bcel.classfile.InnerClass;
import org.aspectj.apache.bcel.classfile.InnerClasses;
import org.aspectj.apache.bcel.classfile.JavaClass;
import org.aspectj.apache.bcel.classfile.Method;
import org.aspectj.apache.bcel.classfile.Signature;
import org.aspectj.apache.bcel.classfile.annotation.AnnotationGen;
import org.aspectj.apache.bcel.classfile.annotation.EnumElementValue;
import org.aspectj.apache.bcel.classfile.annotation.NameValuePair;
import org.aspectj.asm.AsmManager;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessageHandler;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.util.GenericSignature;
import org.aspectj.weaver.AbstractReferenceTypeDelegate;
import org.aspectj.weaver.AjAttribute;
import org.aspectj.weaver.AjcMemberMaker;
import org.aspectj.weaver.AnnotationAJ;
import org.aspectj.weaver.AnnotationTargetKind;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.BindingScope;
import org.aspectj.weaver.ConcreteTypeMunger;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ReferenceType;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedPointcutDefinition;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.SourceContextImpl;
import org.aspectj.weaver.TypeVariable;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.WeaverStateInfo;
import org.aspectj.weaver.World;
import org.aspectj.weaver.bcel.BcelGenericSignatureToTypeXConverter;
import org.aspectj.weaver.patterns.Declare;
import org.aspectj.weaver.patterns.DeclareErrorOrWarning;
import org.aspectj.weaver.patterns.DeclarePrecedence;
import org.aspectj.weaver.patterns.FormalBinding;
import org.aspectj.weaver.patterns.IScope;
import org.aspectj.weaver.patterns.PerClause;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/BcelObjectType.class */
public class BcelObjectType extends AbstractReferenceTypeDelegate {
    public JavaClass javaClass;
    private boolean artificial;
    private LazyClassGen lazyClassGen;
    private int modifiers;
    private String className;
    private String superclassSignature;
    private String superclassName;
    private String[] interfaceSignatures;
    private ResolvedMember[] fields;
    private ResolvedMember[] methods;
    private ResolvedType[] annotationTypes;
    private AnnotationAJ[] annotations;
    private TypeVariable[] typeVars;
    private String retentionPolicy;
    private AnnotationTargetKind[] annotationTargetKinds;
    private AjAttribute.WeaverVersionInfo wvInfo;
    private ResolvedPointcutDefinition[] pointcuts;
    private ResolvedMember[] privilegedAccess;
    private WeaverStateInfo weaverState;
    private PerClause perClause;
    private List<ConcreteTypeMunger> typeMungers;
    private List<Declare> declares;
    private GenericSignature.FormalTypeParameter[] formalsForResolution;
    private String declaredSignature;
    private boolean hasBeenWoven;
    private boolean isGenericType;
    private boolean isInterface;
    private boolean isEnum;
    private boolean isAnnotation;
    private boolean isAnonymous;
    private boolean isNested;
    private boolean isObject;
    private boolean isAnnotationStyleAspect;
    private boolean isCodeStyleAspect;
    private WeakReference<ResolvedType> superTypeReference;
    private WeakReference<ResolvedType[]> superInterfaceReferences;
    private int bitflag;
    private static final int DISCOVERED_ANNOTATION_RETENTION_POLICY = 1;
    private static final int UNPACKED_GENERIC_SIGNATURE = 2;
    private static final int UNPACKED_AJATTRIBUTES = 4;
    private static final int DISCOVERED_ANNOTATION_TARGET_KINDS = 8;
    private static final int DISCOVERED_DECLARED_SIGNATURE = 16;
    private static final int DISCOVERED_WHETHER_ANNOTATION_STYLE = 32;
    private static final int ANNOTATION_UNPACK_IN_PROGRESS = 256;
    private static final String[] NO_INTERFACE_SIGS = new String[0];

    BcelObjectType(ReferenceType resolvedTypeX, JavaClass javaClass, boolean artificial, boolean exposedToWeaver) throws AbortException {
        super(resolvedTypeX, exposedToWeaver);
        this.lazyClassGen = null;
        this.fields = null;
        this.methods = null;
        this.annotationTypes = null;
        this.annotations = null;
        this.typeVars = null;
        this.wvInfo = AjAttribute.WeaverVersionInfo.UNKNOWN;
        this.pointcuts = null;
        this.privilegedAccess = null;
        this.weaverState = null;
        this.perClause = null;
        this.typeMungers = Collections.emptyList();
        this.declares = Collections.emptyList();
        this.formalsForResolution = null;
        this.declaredSignature = null;
        this.hasBeenWoven = false;
        this.isGenericType = false;
        this.isObject = false;
        this.isAnnotationStyleAspect = false;
        this.isCodeStyleAspect = false;
        this.superTypeReference = new WeakReference<>(null);
        this.superInterfaceReferences = new WeakReference<>(null);
        this.bitflag = 0;
        this.javaClass = javaClass;
        this.artificial = artificial;
        initializeFromJavaclass();
        resolvedTypeX.setDelegate(this);
        ISourceContext sourceContext = resolvedTypeX.getSourceContext();
        if (sourceContext == SourceContextImpl.UNKNOWN_SOURCE_CONTEXT) {
            ISourceContext sourceContext2 = new SourceContextImpl(this);
            setSourceContext(sourceContext2);
        }
        this.isObject = javaClass.getSuperclassNameIndex() == 0;
        ensureAspectJAttributesUnpacked();
        setSourcefilename(javaClass.getSourceFileName());
    }

    public void setJavaClass(JavaClass newclass, boolean artificial) throws AbortException {
        this.javaClass = newclass;
        this.artificial = artificial;
        resetState();
        initializeFromJavaclass();
    }

    @Override // org.aspectj.weaver.AbstractReferenceTypeDelegate, org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isCacheable() {
        return true;
    }

    private void initializeFromJavaclass() {
        this.isInterface = this.javaClass.isInterface();
        this.isEnum = this.javaClass.isEnum();
        this.isAnnotation = this.javaClass.isAnnotation();
        this.isAnonymous = this.javaClass.isAnonymous();
        this.isNested = this.javaClass.isNested();
        this.modifiers = this.javaClass.getModifiers();
        this.superclassName = this.javaClass.getSuperclassName();
        this.className = this.javaClass.getClassName();
        this.cachedGenericClassTypeSignature = null;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isInterface() {
        return this.isInterface;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isEnum() {
        return this.isEnum;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isAnnotation() {
        return this.isAnnotation;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isAnonymous() {
        return this.isAnonymous;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isNested() {
        return this.isNested;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public int getModifiers() {
        return this.modifiers;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedType getSuperclass() {
        if (this.isObject) {
            return null;
        }
        ResolvedType supertype = this.superTypeReference.get();
        if (supertype == null) {
            ensureGenericSignatureUnpacked();
            if (this.superclassSignature == null) {
                if (this.superclassName == null) {
                    this.superclassName = this.javaClass.getSuperclassName();
                }
                this.superclassSignature = getResolvedTypeX().getWorld().resolve(UnresolvedType.forName(this.superclassName)).getSignature();
            }
            World world = getResolvedTypeX().getWorld();
            supertype = world.resolve(UnresolvedType.forSignature(this.superclassSignature));
            this.superTypeReference = new WeakReference<>(supertype);
        }
        return supertype;
    }

    public World getWorld() {
        return getResolvedTypeX().getWorld();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedType[] getDeclaredInterfaces() {
        ResolvedType[] interfaceTypes;
        ResolvedType[] cachedInterfaceTypes = this.superInterfaceReferences.get();
        if (cachedInterfaceTypes == null) {
            ensureGenericSignatureUnpacked();
            if (this.interfaceSignatures == null) {
                String[] names = this.javaClass.getInterfaceNames();
                if (names.length == 0) {
                    this.interfaceSignatures = NO_INTERFACE_SIGS;
                    interfaceTypes = ResolvedType.NONE;
                } else {
                    this.interfaceSignatures = new String[names.length];
                    interfaceTypes = new ResolvedType[names.length];
                    int len = names.length;
                    for (int i = 0; i < len; i++) {
                        interfaceTypes[i] = getResolvedTypeX().getWorld().resolve(UnresolvedType.forName(names[i]));
                        this.interfaceSignatures[i] = interfaceTypes[i].getSignature();
                    }
                }
            } else {
                interfaceTypes = new ResolvedType[this.interfaceSignatures.length];
                int len2 = this.interfaceSignatures.length;
                for (int i2 = 0; i2 < len2; i2++) {
                    interfaceTypes[i2] = getResolvedTypeX().getWorld().resolve(UnresolvedType.forSignature(this.interfaceSignatures[i2]));
                }
            }
            this.superInterfaceReferences = new WeakReference<>(interfaceTypes);
            return interfaceTypes;
        }
        return cachedInterfaceTypes;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedMember[] getDeclaredMethods() {
        ensureGenericSignatureUnpacked();
        if (this.methods == null) {
            Method[] ms = this.javaClass.getMethods();
            ResolvedMember[] newMethods = new ResolvedMember[ms.length];
            for (int i = ms.length - 1; i >= 0; i--) {
                newMethods[i] = new BcelMethod(this, ms[i]);
            }
            this.methods = newMethods;
        }
        return this.methods;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedMember[] getDeclaredFields() {
        ensureGenericSignatureUnpacked();
        if (this.fields == null) {
            Field[] fs = this.javaClass.getFields();
            ResolvedMember[] newfields = new ResolvedMember[fs.length];
            int len = fs.length;
            for (int i = 0; i < len; i++) {
                newfields[i] = new BcelField(this, fs[i]);
            }
            this.fields = newfields;
        }
        return this.fields;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public TypeVariable[] getTypeVariables() {
        if (!isGeneric()) {
            return TypeVariable.NONE;
        }
        if (this.typeVars == null) {
            GenericSignature.ClassSignature classSig = getGenericClassTypeSignature();
            this.typeVars = new TypeVariable[classSig.formalTypeParameters.length];
            for (int i = 0; i < this.typeVars.length; i++) {
                GenericSignature.FormalTypeParameter ftp = classSig.formalTypeParameters[i];
                try {
                    this.typeVars[i] = BcelGenericSignatureToTypeXConverter.formalTypeParameter2TypeVariable(ftp, classSig.formalTypeParameters, getResolvedTypeX().getWorld());
                } catch (BcelGenericSignatureToTypeXConverter.GenericSignatureFormatException e) {
                    throw new IllegalStateException("While getting the type variables for type " + toString() + " with generic signature " + classSig + " the following error condition was detected: " + e.getMessage());
                }
            }
        }
        return this.typeVars;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public Collection<ConcreteTypeMunger> getTypeMungers() {
        return this.typeMungers;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public Collection<Declare> getDeclares() {
        return this.declares;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public Collection<ResolvedMember> getPrivilegedAccesses() {
        if (this.privilegedAccess == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(this.privilegedAccess);
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedMember[] getDeclaredPointcuts() {
        return this.pointcuts;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isAspect() {
        return this.perClause != null;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isAnnotationStyleAspect() {
        if ((this.bitflag & 32) == 0) {
            this.bitflag |= 32;
            this.isAnnotationStyleAspect = !this.isCodeStyleAspect && hasAnnotation(AjcMemberMaker.ASPECT_ANNOTATION);
        }
        return this.isAnnotationStyleAspect;
    }

    private void ensureAspectJAttributesUnpacked() throws AbortException {
        if ((this.bitflag & 4) != 0) {
            return;
        }
        this.bitflag |= 4;
        IMessageHandler msgHandler = getResolvedTypeX().getWorld().getMessageHandler();
        try {
            List<AjAttribute> l = Utility.readAjAttributes(this.className, this.javaClass.getAttributes(), getResolvedTypeX().getSourceContext(), getResolvedTypeX().getWorld(), AjAttribute.WeaverVersionInfo.UNKNOWN, new BcelConstantPoolReader(this.javaClass.getConstantPool()));
            List<ResolvedPointcutDefinition> pointcuts = new ArrayList<>();
            this.typeMungers = new ArrayList();
            this.declares = new ArrayList();
            processAttributes(l, pointcuts, false);
            ReferenceType type = getResolvedTypeX();
            AsmManager asmManager = ((BcelWorld) type.getWorld()).getModelAsAsmManager();
            List<AjAttribute> l2 = AtAjAttributes.readAj5ClassAttributes(asmManager, this.javaClass, type, type.getSourceContext(), msgHandler, this.isCodeStyleAspect);
            AjAttribute.Aspect deferredAspectAttribute = processAttributes(l2, pointcuts, true);
            if (pointcuts.size() == 0) {
                this.pointcuts = ResolvedPointcutDefinition.NO_POINTCUTS;
            } else {
                this.pointcuts = (ResolvedPointcutDefinition[]) pointcuts.toArray(new ResolvedPointcutDefinition[pointcuts.size()]);
            }
            resolveAnnotationDeclares(l2);
            if (deferredAspectAttribute != null) {
                this.perClause = deferredAspectAttribute.reifyFromAtAspectJ(getResolvedTypeX());
            }
            if (isAspect() && !Modifier.isAbstract(getModifiers()) && isGeneric()) {
                msgHandler.handleMessage(MessageUtil.error("The generic aspect '" + getResolvedTypeX().getName() + "' must be declared abstract", getResolvedTypeX().getSourceLocation()));
            }
        } catch (RuntimeException re) {
            throw new RuntimeException("Problem processing attributes in " + this.javaClass.getFileName(), re);
        }
    }

    private AjAttribute.Aspect processAttributes(List<AjAttribute> attributeList, List<ResolvedPointcutDefinition> pointcuts, boolean fromAnnotations) {
        AjAttribute.Aspect deferredAspectAttribute = null;
        for (AjAttribute a : attributeList) {
            if (a instanceof AjAttribute.Aspect) {
                if (fromAnnotations) {
                    deferredAspectAttribute = (AjAttribute.Aspect) a;
                } else {
                    this.perClause = ((AjAttribute.Aspect) a).reify(getResolvedTypeX());
                    this.isCodeStyleAspect = true;
                }
            } else if (a instanceof AjAttribute.PointcutDeclarationAttribute) {
                pointcuts.add(((AjAttribute.PointcutDeclarationAttribute) a).reify());
            } else if (a instanceof AjAttribute.WeaverState) {
                this.weaverState = ((AjAttribute.WeaverState) a).reify();
            } else if (a instanceof AjAttribute.TypeMunger) {
                this.typeMungers.add(((AjAttribute.TypeMunger) a).reify(getResolvedTypeX().getWorld(), getResolvedTypeX()));
            } else if (a instanceof AjAttribute.DeclareAttribute) {
                this.declares.add(((AjAttribute.DeclareAttribute) a).getDeclare());
            } else if (a instanceof AjAttribute.PrivilegedAttribute) {
                AjAttribute.PrivilegedAttribute privAttribute = (AjAttribute.PrivilegedAttribute) a;
                this.privilegedAccess = privAttribute.getAccessedMembers();
            } else if (a instanceof AjAttribute.SourceContextAttribute) {
                if (getResolvedTypeX().getSourceContext() instanceof SourceContextImpl) {
                    AjAttribute.SourceContextAttribute sca = (AjAttribute.SourceContextAttribute) a;
                    ((SourceContextImpl) getResolvedTypeX().getSourceContext()).configureFromAttribute(sca.getSourceFileName(), sca.getLineBreaks());
                    setSourcefilename(sca.getSourceFileName());
                }
            } else if (a instanceof AjAttribute.WeaverVersionInfo) {
                this.wvInfo = (AjAttribute.WeaverVersionInfo) a;
            } else {
                throw new BCException("bad attribute " + a);
            }
        }
        return deferredAspectAttribute;
    }

    private void resolveAnnotationDeclares(List<AjAttribute> attributeList) {
        FormalBinding[] bindings = new FormalBinding[0];
        IScope bindingScope = new BindingScope(getResolvedTypeX(), getResolvedTypeX().getSourceContext(), bindings);
        for (AjAttribute a : attributeList) {
            if (a instanceof AjAttribute.DeclareAttribute) {
                Declare decl = ((AjAttribute.DeclareAttribute) a).getDeclare();
                if (decl instanceof DeclareErrorOrWarning) {
                    decl.resolve(bindingScope);
                } else if (decl instanceof DeclarePrecedence) {
                    ((DeclarePrecedence) decl).setScopeForResolution(bindingScope);
                }
            }
        }
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public PerClause getPerClause() throws AbortException {
        ensureAspectJAttributesUnpacked();
        return this.perClause;
    }

    public JavaClass getJavaClass() {
        return this.javaClass;
    }

    public boolean isArtificial() {
        return this.artificial;
    }

    public void resetState() throws AbortException {
        if (this.javaClass == null) {
            throw new BCException("can't weave evicted type");
        }
        this.bitflag = 0;
        this.annotationTypes = null;
        this.annotations = null;
        this.interfaceSignatures = null;
        this.superclassSignature = null;
        this.superclassName = null;
        this.fields = null;
        this.methods = null;
        this.pointcuts = null;
        this.perClause = null;
        this.weaverState = null;
        this.lazyClassGen = null;
        this.hasBeenWoven = false;
        this.isObject = this.javaClass.getSuperclassNameIndex() == 0;
        this.isAnnotationStyleAspect = false;
        ensureAspectJAttributesUnpacked();
    }

    public void finishedWith() {
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public WeaverStateInfo getWeaverState() {
        return this.weaverState;
    }

    void setWeaverState(WeaverStateInfo weaverState) {
        this.weaverState = weaverState;
    }

    public void printWackyStuff(PrintStream out) {
        if (this.typeMungers.size() > 0) {
            out.println("  TypeMungers: " + this.typeMungers);
        }
        if (this.declares.size() > 0) {
            out.println("     declares: " + this.declares);
        }
    }

    public LazyClassGen getLazyClassGen() {
        LazyClassGen ret = this.lazyClassGen;
        if (ret == null) {
            ret = new LazyClassGen(this);
            if (isAspect()) {
                this.lazyClassGen = ret;
            }
        }
        return ret;
    }

    public boolean isSynthetic() {
        return getResolvedTypeX().isSynthetic();
    }

    public AjAttribute.WeaverVersionInfo getWeaverVersionAttribute() {
        return this.wvInfo;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedType[] getAnnotationTypes() {
        ensureAnnotationsUnpacked();
        return this.annotationTypes;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public AnnotationAJ[] getAnnotations() {
        ensureAnnotationsUnpacked();
        return this.annotations;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean hasAnnotations() {
        ensureAnnotationsUnpacked();
        return this.annotations.length != 0;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean hasAnnotation(UnresolvedType ofType) {
        if (isUnpackingAnnotations()) {
            AnnotationGen[] annos = this.javaClass.getAnnotations();
            if (annos == null || annos.length == 0) {
                return false;
            }
            String lookingForSignature = ofType.getSignature();
            for (AnnotationGen annotation : annos) {
                if (lookingForSignature.equals(annotation.getTypeSignature())) {
                    return true;
                }
            }
            return false;
        }
        ensureAnnotationsUnpacked();
        int max = this.annotationTypes.length;
        for (int i = 0; i < max; i++) {
            UnresolvedType ax = this.annotationTypes[i];
            if (ax == null) {
                throw new RuntimeException("Annotation entry " + i + " on type " + getResolvedTypeX().getName() + " is null!");
            }
            if (ax.equals(ofType)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isAnnotationWithRuntimeRetention() {
        if (getRetentionPolicy() == null) {
            return false;
        }
        return getRetentionPolicy().equals("RUNTIME");
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public String getRetentionPolicy() {
        if ((this.bitflag & 1) == 0) {
            this.bitflag |= 1;
            this.retentionPolicy = null;
            if (isAnnotation()) {
                ensureAnnotationsUnpacked();
                for (int i = this.annotations.length - 1; i >= 0; i--) {
                    AnnotationAJ ax = this.annotations[i];
                    if (ax.getTypeName().equals(UnresolvedType.AT_RETENTION.getName())) {
                        List<NameValuePair> values = ((BcelAnnotation) ax).getBcelAnnotation().getValues();
                        Iterator<NameValuePair> it = values.iterator();
                        if (it.hasNext()) {
                            NameValuePair element = it.next();
                            EnumElementValue v = (EnumElementValue) element.getValue();
                            this.retentionPolicy = v.getEnumValueString();
                            return this.retentionPolicy;
                        }
                    }
                }
            }
        }
        return this.retentionPolicy;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean canAnnotationTargetType() {
        AnnotationTargetKind[] targetKinds = getAnnotationTargetKinds();
        if (targetKinds == null) {
            return true;
        }
        for (AnnotationTargetKind annotationTargetKind : targetKinds) {
            if (annotationTargetKind.equals(AnnotationTargetKind.TYPE)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public AnnotationTargetKind[] getAnnotationTargetKinds() {
        Set<String> targets;
        if ((this.bitflag & 8) != 0) {
            return this.annotationTargetKinds;
        }
        this.bitflag |= 8;
        this.annotationTargetKinds = null;
        List<AnnotationTargetKind> targetKinds = new ArrayList<>();
        if (isAnnotation()) {
            AnnotationAJ[] annotationsOnThisType = getAnnotations();
            for (AnnotationAJ a : annotationsOnThisType) {
                if (a.getTypeName().equals(UnresolvedType.AT_TARGET.getName()) && (targets = a.getTargets()) != null) {
                    for (String targetKind : targets) {
                        if (targetKind.equals("ANNOTATION_TYPE")) {
                            targetKinds.add(AnnotationTargetKind.ANNOTATION_TYPE);
                        } else if (targetKind.equals("CONSTRUCTOR")) {
                            targetKinds.add(AnnotationTargetKind.CONSTRUCTOR);
                        } else if (targetKind.equals("FIELD")) {
                            targetKinds.add(AnnotationTargetKind.FIELD);
                        } else if (targetKind.equals("LOCAL_VARIABLE")) {
                            targetKinds.add(AnnotationTargetKind.LOCAL_VARIABLE);
                        } else if (targetKind.equals("METHOD")) {
                            targetKinds.add(AnnotationTargetKind.METHOD);
                        } else if (targetKind.equals("PACKAGE")) {
                            targetKinds.add(AnnotationTargetKind.PACKAGE);
                        } else if (targetKind.equals("PARAMETER")) {
                            targetKinds.add(AnnotationTargetKind.PARAMETER);
                        } else if (targetKind.equals("TYPE")) {
                            targetKinds.add(AnnotationTargetKind.TYPE);
                        }
                    }
                }
            }
            if (!targetKinds.isEmpty()) {
                this.annotationTargetKinds = new AnnotationTargetKind[targetKinds.size()];
                return (AnnotationTargetKind[]) targetKinds.toArray(this.annotationTargetKinds);
            }
        }
        return this.annotationTargetKinds;
    }

    private boolean isUnpackingAnnotations() {
        return (this.bitflag & 256) != 0;
    }

    private void ensureAnnotationsUnpacked() {
        if (isUnpackingAnnotations()) {
            throw new BCException("Re-entered weaver instance whilst unpacking annotations on " + this.className);
        }
        if (this.annotationTypes == null) {
            try {
                this.bitflag |= 256;
                AnnotationGen[] annos = this.javaClass.getAnnotations();
                if (annos == null || annos.length == 0) {
                    this.annotationTypes = ResolvedType.NONE;
                    this.annotations = AnnotationAJ.EMPTY_ARRAY;
                } else {
                    World w = getResolvedTypeX().getWorld();
                    this.annotationTypes = new ResolvedType[annos.length];
                    this.annotations = new AnnotationAJ[annos.length];
                    for (int i = 0; i < annos.length; i++) {
                        AnnotationGen annotation = annos[i];
                        String typeSignature = annotation.getTypeSignature();
                        ResolvedType rType = w.resolve(UnresolvedType.forSignature(typeSignature));
                        if (rType == null) {
                            throw new RuntimeException("Whilst unpacking annotations on '" + getResolvedTypeX().getName() + "', failed to resolve type '" + typeSignature + "'");
                        }
                        this.annotationTypes[i] = rType;
                        this.annotations[i] = new BcelAnnotation(annotation, rType);
                    }
                }
            } finally {
                this.bitflag &= -257;
            }
        }
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public String getDeclaredGenericSignature() {
        ensureGenericInfoProcessed();
        return this.declaredSignature;
    }

    private void ensureGenericSignatureUnpacked() {
        ReferenceType genericType;
        if ((this.bitflag & 2) != 0) {
            return;
        }
        this.bitflag |= 2;
        if (!getResolvedTypeX().getWorld().isInJava5Mode()) {
            return;
        }
        GenericSignature.ClassSignature cSig = getGenericClassTypeSignature();
        if (cSig != null) {
            this.formalsForResolution = cSig.formalTypeParameters;
            if (isNested()) {
                GenericSignature.FormalTypeParameter[] extraFormals = getFormalTypeParametersFromOuterClass();
                if (extraFormals.length > 0) {
                    List<GenericSignature.FormalTypeParameter> allFormals = new ArrayList<>();
                    for (int i = 0; i < this.formalsForResolution.length; i++) {
                        allFormals.add(this.formalsForResolution[i]);
                    }
                    for (GenericSignature.FormalTypeParameter formalTypeParameter : extraFormals) {
                        allFormals.add(formalTypeParameter);
                    }
                    this.formalsForResolution = new GenericSignature.FormalTypeParameter[allFormals.size()];
                    allFormals.toArray(this.formalsForResolution);
                }
            }
            GenericSignature.ClassTypeSignature superSig = cSig.superclassSignature;
            try {
                ResolvedType rt = BcelGenericSignatureToTypeXConverter.classTypeSignature2TypeX(superSig, this.formalsForResolution, getResolvedTypeX().getWorld());
                this.superclassSignature = rt.getSignature();
                this.superclassName = rt.getName();
                if (cSig.superInterfaceSignatures.length == 0) {
                    this.interfaceSignatures = NO_INTERFACE_SIGS;
                } else {
                    this.interfaceSignatures = new String[cSig.superInterfaceSignatures.length];
                    for (int i2 = 0; i2 < cSig.superInterfaceSignatures.length; i2++) {
                        try {
                            this.interfaceSignatures[i2] = BcelGenericSignatureToTypeXConverter.classTypeSignature2TypeX(cSig.superInterfaceSignatures[i2], this.formalsForResolution, getResolvedTypeX().getWorld()).getSignature();
                        } catch (BcelGenericSignatureToTypeXConverter.GenericSignatureFormatException e) {
                            throw new IllegalStateException("While determing the generic superinterfaces of " + this.className + " with generic signature " + getDeclaredGenericSignature() + " the following error was detected: " + e.getMessage());
                        }
                    }
                }
            } catch (BcelGenericSignatureToTypeXConverter.GenericSignatureFormatException e2) {
                throw new IllegalStateException("While determining the generic superclass of " + this.className + " with generic signature " + getDeclaredGenericSignature() + " the following error was detected: " + e2.getMessage());
            }
        }
        if (isGeneric() && (genericType = this.resolvedTypeX.getGenericType()) != null) {
            genericType.setStartPos(this.resolvedTypeX.getStartPos());
            this.resolvedTypeX = genericType;
        }
    }

    public GenericSignature.FormalTypeParameter[] getAllFormals() {
        ensureGenericSignatureUnpacked();
        if (this.formalsForResolution == null) {
            return new GenericSignature.FormalTypeParameter[0];
        }
        return this.formalsForResolution;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedType getOuterClass() {
        if (!isNested()) {
            throw new IllegalStateException("Can't get the outer class of non-nested type: " + this.className);
        }
        for (Attribute attr : this.javaClass.getAttributes()) {
            if (attr instanceof InnerClasses) {
                InnerClass[] innerClss = ((InnerClasses) attr).getInnerClasses();
                ConstantPool cpool = this.javaClass.getConstantPool();
                for (InnerClass innerCls : innerClss) {
                    if (innerCls.getInnerClassIndex() != 0 && innerCls.getOuterClassIndex() != 0) {
                        ConstantClass innerClsInfo = (ConstantClass) cpool.getConstant(innerCls.getInnerClassIndex());
                        String innerClsName = cpool.getConstantUtf8(innerClsInfo.getNameIndex()).getValue().replace('/', '.');
                        if (innerClsName.compareTo(this.className) == 0) {
                            ConstantClass outerClsInfo = (ConstantClass) cpool.getConstant(innerCls.getOuterClassIndex());
                            String outerClsName = cpool.getConstantUtf8(outerClsInfo.getNameIndex()).getValue().replace('/', '.');
                            UnresolvedType outer = UnresolvedType.forName(outerClsName);
                            return outer.resolve(getResolvedTypeX().getWorld());
                        }
                    }
                }
            }
        }
        for (Attribute attr2 : this.javaClass.getAttributes()) {
            ConstantPool cpool2 = this.javaClass.getConstantPool();
            if (attr2 instanceof EnclosingMethod) {
                EnclosingMethod enclosingMethodAttribute = (EnclosingMethod) attr2;
                if (enclosingMethodAttribute.getEnclosingClassIndex() != 0) {
                    ConstantClass outerClassInfo = enclosingMethodAttribute.getEnclosingClass();
                    String outerClassName = cpool2.getConstantUtf8(outerClassInfo.getNameIndex()).getValue().replace('/', '.');
                    UnresolvedType outer2 = UnresolvedType.forName(outerClassName);
                    return outer2.resolve(getResolvedTypeX().getWorld());
                }
            }
        }
        int lastDollar = this.className.lastIndexOf(36);
        if (lastDollar == -1) {
            return null;
        }
        String superClassName = this.className.substring(0, lastDollar);
        UnresolvedType outer3 = UnresolvedType.forName(superClassName);
        return outer3.resolve(getResolvedTypeX().getWorld());
    }

    private void ensureGenericInfoProcessed() {
        if ((this.bitflag & 16) != 0) {
            return;
        }
        this.bitflag |= 16;
        Signature sigAttr = AttributeUtils.getSignatureAttribute(this.javaClass.getAttributes());
        this.declaredSignature = sigAttr == null ? null : sigAttr.getSignature();
        if (this.declaredSignature != null) {
            this.isGenericType = this.declaredSignature.charAt(0) == '<';
        }
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isGeneric() {
        ensureGenericInfoProcessed();
        return this.isGenericType;
    }

    public String toString() {
        return this.javaClass == null ? "BcelObjectType" : "BcelObjectTypeFor:" + this.className;
    }

    public void evictWeavingState() {
        if (!getResolvedTypeX().getWorld().couldIncrementalCompileFollow() && this.javaClass != null) {
            ensureAnnotationsUnpacked();
            ensureGenericInfoProcessed();
            getDeclaredInterfaces();
            getDeclaredFields();
            getDeclaredMethods();
            if (getResolvedTypeX().getWorld().isXnoInline()) {
                this.lazyClassGen = null;
            }
            if (this.weaverState != null) {
                this.weaverState.setReweavable(false);
                this.weaverState.setUnwovenClassFileData(null);
            }
            for (int i = this.methods.length - 1; i >= 0; i--) {
                this.methods[i].evictWeavingState();
            }
            for (int i2 = this.fields.length - 1; i2 >= 0; i2--) {
                this.fields[i2].evictWeavingState();
            }
            this.javaClass = null;
            this.artificial = true;
        }
    }

    public void weavingCompleted() {
        this.hasBeenWoven = true;
        if (getResolvedTypeX().getWorld().isRunMinimalMemory()) {
            evictWeavingState();
        }
        if (getSourceContext() != null && !getResolvedTypeX().isAspect()) {
            getSourceContext().tidy();
        }
    }

    @Override // org.aspectj.weaver.AbstractReferenceTypeDelegate, org.aspectj.weaver.ReferenceTypeDelegate
    public boolean hasBeenWoven() {
        return this.hasBeenWoven;
    }

    @Override // org.aspectj.weaver.AbstractReferenceTypeDelegate, org.aspectj.weaver.ReferenceTypeDelegate
    public boolean copySourceContext() {
        return false;
    }

    public void setExposedToWeaver(boolean b) {
        this.exposedToWeaver = b;
    }

    @Override // org.aspectj.weaver.AbstractReferenceTypeDelegate, org.aspectj.weaver.ReferenceTypeDelegate
    public int getCompilerVersion() {
        return this.wvInfo.getMajorVersion();
    }

    @Override // org.aspectj.weaver.AbstractReferenceTypeDelegate, org.aspectj.weaver.ReferenceTypeDelegate
    public void ensureConsistent() {
        this.superTypeReference.clear();
        this.superInterfaceReferences.clear();
    }

    @Override // org.aspectj.weaver.AbstractReferenceTypeDelegate, org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isWeavable() {
        return true;
    }
}
