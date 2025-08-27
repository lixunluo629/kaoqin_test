package org.aspectj.weaver.bcel;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.ibatis.javassist.bytecode.AnnotationDefaultAttribute;
import org.apache.ibatis.javassist.bytecode.SyntheticAttribute;
import org.aspectj.apache.bcel.classfile.AnnotationDefault;
import org.aspectj.apache.bcel.classfile.Attribute;
import org.aspectj.apache.bcel.classfile.ExceptionTable;
import org.aspectj.apache.bcel.classfile.JavaClass;
import org.aspectj.apache.bcel.classfile.LineNumber;
import org.aspectj.apache.bcel.classfile.LineNumberTable;
import org.aspectj.apache.bcel.classfile.LocalVariable;
import org.aspectj.apache.bcel.classfile.LocalVariableTable;
import org.aspectj.apache.bcel.classfile.Method;
import org.aspectj.apache.bcel.classfile.annotation.AnnotationGen;
import org.aspectj.apache.bcel.classfile.annotation.NameValuePair;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.SourceLocation;
import org.aspectj.util.GenericSignature;
import org.aspectj.util.GenericSignatureParser;
import org.aspectj.weaver.AjAttribute;
import org.aspectj.weaver.AnnotationAJ;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.MemberKind;
import org.aspectj.weaver.ResolvedMemberImpl;
import org.aspectj.weaver.ResolvedPointcutDefinition;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.ShadowMunger;
import org.aspectj.weaver.TypeVariable;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.World;
import org.aspectj.weaver.bcel.BcelGenericSignatureToTypeXConverter;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/BcelMethod.class */
class BcelMethod extends ResolvedMemberImpl {
    private Method method;
    private ShadowMunger associatedShadowMunger;
    private ResolvedPointcutDefinition preResolvedPointcut;
    private AjAttribute.EffectiveSignatureAttribute effectiveSignature;
    private AjAttribute.MethodDeclarationLineNumberAttribute declarationLineNumber;
    private final BcelObjectType bcelObjectType;
    private int bitflags;
    private static final int KNOW_IF_SYNTHETIC = 1;
    private static final int PARAMETER_NAMES_INITIALIZED = 2;
    private static final int CAN_BE_PARAMETERIZED = 4;
    private static final int UNPACKED_GENERIC_SIGNATURE = 8;
    private static final int IS_AJ_SYNTHETIC = 64;
    private static final int IS_SYNTHETIC = 128;
    private static final int IS_SYNTHETIC_INVERSE = 32639;
    private static final int HAS_ANNOTATIONS = 1024;
    private static final int HAVE_DETERMINED_ANNOTATIONS = 2048;
    private UnresolvedType genericReturnType;
    private UnresolvedType[] genericParameterTypes;
    public static final AnnotationAJ[] NO_PARAMETER_ANNOTATIONS = new AnnotationAJ[0];

    BcelMethod(BcelObjectType declaringType, Method method) throws AbortException {
        super(method.getName().equals("<init>") ? CONSTRUCTOR : method.getName().equals("<clinit>") ? STATIC_INITIALIZATION : METHOD, declaringType.getResolvedTypeX(), method.getModifiers(), method.getName(), method.getSignature());
        this.genericReturnType = null;
        this.genericParameterTypes = null;
        this.method = method;
        this.sourceContext = declaringType.getResolvedTypeX().getSourceContext();
        this.bcelObjectType = declaringType;
        unpackJavaAttributes();
        unpackAjAttributes(this.bcelObjectType.getWorld());
    }

    BcelMethod(BcelObjectType declaringType, Method method, List<AjAttribute> attributes) {
        super(method.getName().equals("<init>") ? CONSTRUCTOR : method.getName().equals("<clinit>") ? STATIC_INITIALIZATION : METHOD, declaringType.getResolvedTypeX(), method.getModifiers(), method.getName(), method.getSignature());
        this.genericReturnType = null;
        this.genericParameterTypes = null;
        this.method = method;
        this.sourceContext = declaringType.getResolvedTypeX().getSourceContext();
        this.bcelObjectType = declaringType;
        unpackJavaAttributes();
        processAttributes(this.bcelObjectType.getWorld(), attributes);
    }

    private void unpackJavaAttributes() {
        ExceptionTable exnTable = this.method.getExceptionTable();
        this.checkedExceptions = exnTable == null ? UnresolvedType.NONE : UnresolvedType.forNames(exnTable.getExceptionNames());
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember
    public String[] getParameterNames() {
        determineParameterNames();
        return super.getParameterNames();
    }

    public int getLineNumberOfFirstInstruction() {
        LineNumber[] lns;
        LineNumberTable lnt = this.method.getLineNumberTable();
        if (lnt == null || (lns = lnt.getLineNumberTable()) == null || lns.length == 0) {
            return -1;
        }
        return lns[0].getLineNumber();
    }

    public void determineParameterNames() {
        AnnotationGen a;
        if ((this.bitflags & 2) != 0) {
            return;
        }
        this.bitflags |= 2;
        LocalVariableTable varTable = this.method.getLocalVariableTable();
        int len = getArity();
        if (varTable == null) {
            AnnotationAJ[] annos = getAnnotations();
            if (annos != null && annos.length != 0) {
                AnnotationAJ[] axs = getAnnotations();
                for (AnnotationAJ annotationX : axs) {
                    String typename = annotationX.getTypeName();
                    if (typename.charAt(0) == 'o' && ((typename.equals("org.aspectj.lang.annotation.Pointcut") || typename.equals("org.aspectj.lang.annotation.Before") || typename.equals("org.aspectj.lang.annotation.Around") || typename.startsWith("org.aspectj.lang.annotation.After")) && (a = ((BcelAnnotation) annotationX).getBcelAnnotation()) != null)) {
                        List<NameValuePair> values = a.getValues();
                        for (NameValuePair nvPair : values) {
                            if (nvPair.getNameString().equals("argNames")) {
                                String argNames = nvPair.getValue().stringifyValue();
                                StringTokenizer argNameTokenizer = new StringTokenizer(argNames, " ,");
                                List<String> argsList = new ArrayList<>();
                                while (argNameTokenizer.hasMoreTokens()) {
                                    argsList.add(argNameTokenizer.nextToken());
                                }
                                int requiredCount = getParameterTypes().length;
                                while (argsList.size() < requiredCount) {
                                    argsList.add("arg" + argsList.size());
                                }
                                setParameterNames((String[]) argsList.toArray(new String[0]));
                                return;
                            }
                        }
                    }
                }
            }
            setParameterNames(Utility.makeArgNames(len));
            return;
        }
        UnresolvedType[] paramTypes = getParameterTypes();
        String[] paramNames = new String[len];
        int index = Modifier.isStatic(this.modifiers) ? 0 : 1;
        for (int i = 0; i < len; i++) {
            LocalVariable lv = varTable.getLocalVariable(index);
            if (lv == null) {
                paramNames[i] = "arg" + i;
            } else {
                paramNames[i] = lv.getName();
            }
            index += paramTypes[i].getSize();
        }
        setParameterNames(paramNames);
    }

    private void unpackAjAttributes(World world) throws AbortException {
        this.associatedShadowMunger = null;
        ResolvedType resolvedDeclaringType = getDeclaringType().resolve(world);
        AjAttribute.WeaverVersionInfo wvinfo = this.bcelObjectType.getWeaverVersionAttribute();
        List<AjAttribute> as = Utility.readAjAttributes(resolvedDeclaringType.getClassName(), this.method.getAttributes(), resolvedDeclaringType.getSourceContext(), world, wvinfo, new BcelConstantPoolReader(this.method.getConstantPool()));
        processAttributes(world, as);
        List<AjAttribute> as2 = AtAjAttributes.readAj5MethodAttributes(this.method, this, resolvedDeclaringType, this.preResolvedPointcut, resolvedDeclaringType.getSourceContext(), world.getMessageHandler());
        processAttributes(world, as2);
    }

    private void processAttributes(World world, List<AjAttribute> as) {
        for (AjAttribute attr : as) {
            if (attr instanceof AjAttribute.MethodDeclarationLineNumberAttribute) {
                this.declarationLineNumber = (AjAttribute.MethodDeclarationLineNumberAttribute) attr;
            } else if (attr instanceof AjAttribute.AdviceAttribute) {
                this.associatedShadowMunger = ((AjAttribute.AdviceAttribute) attr).reify(this, world, (ResolvedType) getDeclaringType());
            } else if (attr instanceof AjAttribute.AjSynthetic) {
                this.bitflags |= 64;
            } else if (attr instanceof AjAttribute.EffectiveSignatureAttribute) {
                this.effectiveSignature = (AjAttribute.EffectiveSignatureAttribute) attr;
            } else if (attr instanceof AjAttribute.PointcutDeclarationAttribute) {
                this.preResolvedPointcut = ((AjAttribute.PointcutDeclarationAttribute) attr).reify();
            } else {
                throw new BCException("weird method attribute " + attr);
            }
        }
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember
    public String getAnnotationDefaultValue() {
        Attribute[] attrs = this.method.getAttributes();
        for (Attribute attribute : attrs) {
            if (attribute.getName().equals(AnnotationDefaultAttribute.tag)) {
                AnnotationDefault def = (AnnotationDefault) attribute;
                return def.getElementValue().stringifyValue();
            }
        }
        return null;
    }

    public String[] getAttributeNames(boolean onlyIncludeAjOnes) {
        Attribute[] as = this.method.getAttributes();
        List<String> names = new ArrayList<>();
        for (int j = 0; j < as.length; j++) {
            if (!onlyIncludeAjOnes || as[j].getName().startsWith(AjAttribute.AttributePrefix)) {
                names.add(as[j].getName());
            }
        }
        return (String[]) names.toArray(new String[0]);
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember
    public boolean isAjSynthetic() {
        return (this.bitflags & 64) != 0;
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember
    public ShadowMunger getAssociatedShadowMunger() {
        return this.associatedShadowMunger;
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember
    public AjAttribute.EffectiveSignatureAttribute getEffectiveSignature() {
        return this.effectiveSignature;
    }

    public boolean hasDeclarationLineNumberInfo() {
        return this.declarationLineNumber != null;
    }

    public int getDeclarationLineNumber() {
        if (this.declarationLineNumber != null) {
            return this.declarationLineNumber.getLineNumber();
        }
        return -1;
    }

    public int getDeclarationOffset() {
        if (this.declarationLineNumber != null) {
            return this.declarationLineNumber.getOffset();
        }
        return -1;
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember
    public ISourceLocation getSourceLocation() {
        ISourceLocation ret = super.getSourceLocation();
        if ((ret == null || ret.getLine() == 0) && hasDeclarationLineNumberInfo()) {
            ISourceContext isc = getSourceContext();
            ret = isc != null ? isc.makeSourceLocation(getDeclarationLineNumber(), getDeclarationOffset()) : new SourceLocation(null, getDeclarationLineNumber());
        }
        return ret;
    }

    @Override // org.aspectj.weaver.MemberImpl, org.aspectj.weaver.Member
    public MemberKind getKind() {
        if (this.associatedShadowMunger != null) {
            return ADVICE;
        }
        return super.getKind();
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember, org.aspectj.weaver.AnnotatedElement
    public boolean hasAnnotation(UnresolvedType ofType) {
        ensureAnnotationsRetrieved();
        for (ResolvedType aType : this.annotationTypes) {
            if (aType.equals(ofType)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.MemberImpl, org.aspectj.weaver.Member
    public AnnotationAJ[] getAnnotations() {
        ensureAnnotationsRetrieved();
        if ((this.bitflags & 1024) != 0) {
            return this.annotations;
        }
        return AnnotationAJ.EMPTY_ARRAY;
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember, org.aspectj.weaver.AnnotatedElement
    public ResolvedType[] getAnnotationTypes() {
        ensureAnnotationsRetrieved();
        return this.annotationTypes;
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.AnnotatedElement
    public AnnotationAJ getAnnotationOfType(UnresolvedType ofType) {
        ensureAnnotationsRetrieved();
        if ((this.bitflags & 1024) == 0) {
            return null;
        }
        for (int i = 0; i < this.annotations.length; i++) {
            if (this.annotations[i].getTypeName().equals(ofType.getName())) {
                return this.annotations[i];
            }
        }
        return null;
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember
    public void addAnnotation(AnnotationAJ annotation) {
        ensureAnnotationsRetrieved();
        if ((this.bitflags & 1024) == 0) {
            this.annotations = new AnnotationAJ[1];
            this.annotations[0] = annotation;
            this.annotationTypes = new ResolvedType[1];
            this.annotationTypes[0] = annotation.getType();
        } else {
            int len = this.annotations.length;
            AnnotationAJ[] ret = new AnnotationAJ[len + 1];
            System.arraycopy(this.annotations, 0, ret, 0, len);
            ret[len] = annotation;
            this.annotations = ret;
            ResolvedType[] newAnnotationTypes = new ResolvedType[len + 1];
            System.arraycopy(this.annotationTypes, 0, newAnnotationTypes, 0, len);
            newAnnotationTypes[len] = annotation.getType();
            this.annotationTypes = newAnnotationTypes;
        }
        this.bitflags |= 1024;
    }

    public void removeAnnotation(ResolvedType annotationType) {
        ensureAnnotationsRetrieved();
        if ((this.bitflags & 1024) != 0) {
            int len = this.annotations.length;
            if (len == 1) {
                this.bitflags &= -1025;
                this.annotations = null;
                this.annotationTypes = null;
                return;
            }
            AnnotationAJ[] ret = new AnnotationAJ[len - 1];
            int p = 0;
            for (AnnotationAJ annotation : this.annotations) {
                if (!annotation.getType().equals(annotationType)) {
                    int i = p;
                    p++;
                    ret[i] = annotation;
                }
            }
            this.annotations = ret;
            ResolvedType[] newAnnotationTypes = new ResolvedType[len - 1];
            int p2 = 0;
            for (AnnotationAJ annotationAJ : this.annotations) {
                if (!annotationAJ.getType().equals(annotationType)) {
                    int i2 = p2;
                    p2++;
                    newAnnotationTypes[i2] = annotationType;
                }
            }
            this.annotationTypes = newAnnotationTypes;
        }
        this.bitflags |= 1024;
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [org.aspectj.weaver.AnnotationAJ[], org.aspectj.weaver.AnnotationAJ[][]] */
    public void addParameterAnnotation(int param, AnnotationAJ anno) {
        ensureParameterAnnotationsRetrieved();
        if (this.parameterAnnotations == NO_PARAMETER_ANNOTATIONXS) {
            this.parameterAnnotations = new AnnotationAJ[getArity()];
            for (int i = 0; i < getArity(); i++) {
                this.parameterAnnotations[i] = NO_PARAMETER_ANNOTATIONS;
            }
        }
        int existingCount = this.parameterAnnotations[param].length;
        if (existingCount == 0) {
            AnnotationAJ[] annoArray = {anno};
            this.parameterAnnotations[param] = annoArray;
        } else {
            AnnotationAJ[] newAnnoArray = new AnnotationAJ[existingCount + 1];
            System.arraycopy(this.parameterAnnotations[param], 0, newAnnoArray, 0, existingCount);
            newAnnoArray[existingCount] = anno;
            this.parameterAnnotations[param] = newAnnoArray;
        }
    }

    private void ensureAnnotationsRetrieved() {
        if (this.method == null || (this.bitflags & 2048) != 0) {
            return;
        }
        this.bitflags |= 2048;
        AnnotationGen[] annos = this.method.getAnnotations();
        if (annos.length == 0) {
            this.annotationTypes = ResolvedType.NONE;
            this.annotations = AnnotationAJ.EMPTY_ARRAY;
            return;
        }
        int annoCount = annos.length;
        this.annotationTypes = new ResolvedType[annoCount];
        this.annotations = new AnnotationAJ[annoCount];
        for (int i = 0; i < annoCount; i++) {
            AnnotationGen annotation = annos[i];
            this.annotations[i] = new BcelAnnotation(annotation, this.bcelObjectType.getWorld());
            this.annotationTypes[i] = this.annotations[i].getType();
        }
        this.bitflags |= 1024;
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [org.aspectj.weaver.AnnotationAJ[], org.aspectj.weaver.AnnotationAJ[][]] */
    /* JADX WARN: Type inference failed for: r1v6, types: [org.aspectj.weaver.ResolvedType[], org.aspectj.weaver.ResolvedType[][]] */
    private void ensureParameterAnnotationsRetrieved() {
        if (this.method == null) {
            return;
        }
        AnnotationGen[][] pAnns = this.method.getParameterAnnotations();
        if (this.parameterAnnotationTypes == null || pAnns.length != this.parameterAnnotationTypes.length) {
            if (pAnns == Method.NO_PARAMETER_ANNOTATIONS) {
                this.parameterAnnotationTypes = NO_PARAMETER_ANNOTATION_TYPES;
                this.parameterAnnotations = NO_PARAMETER_ANNOTATIONXS;
                return;
            }
            AnnotationGen[][] annos = this.method.getParameterAnnotations();
            this.parameterAnnotations = new AnnotationAJ[annos.length];
            this.parameterAnnotationTypes = new ResolvedType[annos.length];
            for (int i = 0; i < annos.length; i++) {
                AnnotationGen[] annosOnThisParam = annos[i];
                if (annos[i].length == 0) {
                    this.parameterAnnotations[i] = AnnotationAJ.EMPTY_ARRAY;
                    this.parameterAnnotationTypes[i] = ResolvedType.NONE;
                } else {
                    this.parameterAnnotations[i] = new AnnotationAJ[annosOnThisParam.length];
                    this.parameterAnnotationTypes[i] = new ResolvedType[annosOnThisParam.length];
                    for (int j = 0; j < annosOnThisParam.length; j++) {
                        this.parameterAnnotations[i][j] = new BcelAnnotation(annosOnThisParam[j], this.bcelObjectType.getWorld());
                        this.parameterAnnotationTypes[i][j] = this.bcelObjectType.getWorld().resolve(UnresolvedType.forSignature(annosOnThisParam[j].getTypeSignature()));
                    }
                }
            }
        }
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember
    public AnnotationAJ[][] getParameterAnnotations() {
        ensureParameterAnnotationsRetrieved();
        return this.parameterAnnotations;
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember
    public ResolvedType[][] getParameterAnnotationTypes() {
        ensureParameterAnnotationsRetrieved();
        return this.parameterAnnotationTypes;
    }

    @Override // org.aspectj.weaver.MemberImpl, org.aspectj.weaver.Member
    public boolean canBeParameterized() {
        unpackGenericSignature();
        return (this.bitflags & 4) != 0;
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.MemberImpl, org.aspectj.weaver.Member
    public UnresolvedType[] getGenericParameterTypes() {
        unpackGenericSignature();
        return this.genericParameterTypes;
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.MemberImpl, org.aspectj.weaver.Member
    public UnresolvedType getGenericReturnType() {
        unpackGenericSignature();
        return this.genericReturnType;
    }

    public Method getMethod() {
        return this.method;
    }

    private void unpackGenericSignature() {
        if ((this.bitflags & 8) != 0) {
            return;
        }
        this.bitflags |= 8;
        if (!this.bcelObjectType.getWorld().isInJava5Mode()) {
            this.genericReturnType = getReturnType();
            this.genericParameterTypes = getParameterTypes();
            return;
        }
        String gSig = this.method.getGenericSignature();
        if (gSig != null) {
            GenericSignature.MethodTypeSignature mSig = new GenericSignatureParser().parseAsMethodSignature(gSig);
            if (mSig.formalTypeParameters.length > 0) {
                this.bitflags |= 4;
            }
            this.typeVariables = new TypeVariable[mSig.formalTypeParameters.length];
            for (int i = 0; i < this.typeVariables.length; i++) {
                GenericSignature.FormalTypeParameter methodFtp = mSig.formalTypeParameters[i];
                try {
                    this.typeVariables[i] = BcelGenericSignatureToTypeXConverter.formalTypeParameter2TypeVariable(methodFtp, mSig.formalTypeParameters, this.bcelObjectType.getWorld());
                } catch (BcelGenericSignatureToTypeXConverter.GenericSignatureFormatException e) {
                    throw new IllegalStateException("While getting the type variables for method " + toString() + " with generic signature " + mSig + " the following error condition was detected: " + e.getMessage());
                }
            }
            GenericSignature.FormalTypeParameter[] parentFormals = this.bcelObjectType.getAllFormals();
            GenericSignature.FormalTypeParameter[] formals = new GenericSignature.FormalTypeParameter[parentFormals.length + mSig.formalTypeParameters.length];
            System.arraycopy(mSig.formalTypeParameters, 0, formals, 0, mSig.formalTypeParameters.length);
            System.arraycopy(parentFormals, 0, formals, mSig.formalTypeParameters.length, parentFormals.length);
            GenericSignature.TypeSignature returnTypeSignature = mSig.returnType;
            try {
                this.genericReturnType = BcelGenericSignatureToTypeXConverter.typeSignature2TypeX(returnTypeSignature, formals, this.bcelObjectType.getWorld());
                GenericSignature.TypeSignature[] paramTypeSigs = mSig.parameters;
                if (paramTypeSigs.length == 0) {
                    this.genericParameterTypes = UnresolvedType.NONE;
                } else {
                    this.genericParameterTypes = new UnresolvedType[paramTypeSigs.length];
                }
                for (int i2 = 0; i2 < paramTypeSigs.length; i2++) {
                    try {
                        this.genericParameterTypes[i2] = BcelGenericSignatureToTypeXConverter.typeSignature2TypeX(paramTypeSigs[i2], formals, this.bcelObjectType.getWorld());
                        if (paramTypeSigs[i2] instanceof GenericSignature.TypeVariableSignature) {
                            this.bitflags |= 4;
                        }
                    } catch (BcelGenericSignatureToTypeXConverter.GenericSignatureFormatException e2) {
                        throw new IllegalStateException("While determining the generic parameter types of " + toString() + " with generic signature " + gSig + " the following error was detected: " + e2.getMessage());
                    }
                }
                return;
            } catch (BcelGenericSignatureToTypeXConverter.GenericSignatureFormatException e3) {
                throw new IllegalStateException("While determing the generic return type of " + toString() + " with generic signature " + gSig + " the following error was detected: " + e3.getMessage());
            }
        }
        this.genericReturnType = getReturnType();
        this.genericParameterTypes = getParameterTypes();
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember
    public void evictWeavingState() {
        if (this.method != null) {
            unpackGenericSignature();
            unpackJavaAttributes();
            ensureAnnotationsRetrieved();
            ensureParameterAnnotationsRetrieved();
            determineParameterNames();
            this.method = null;
        }
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember
    public boolean isSynthetic() {
        if ((this.bitflags & 1) == 0) {
            workOutIfSynthetic();
        }
        return (this.bitflags & 128) != 0;
    }

    private void workOutIfSynthetic() {
        if ((this.bitflags & 1) != 0) {
            return;
        }
        this.bitflags |= 1;
        JavaClass jc = this.bcelObjectType.getJavaClass();
        this.bitflags &= IS_SYNTHETIC_INVERSE;
        if (jc == null) {
            return;
        }
        if (jc.getMajor() < 49) {
            String[] synthetics = getAttributeNames(false);
            if (synthetics != null) {
                for (String str : synthetics) {
                    if (str.equals(SyntheticAttribute.tag)) {
                        this.bitflags |= 128;
                        return;
                    }
                }
                return;
            }
            return;
        }
        if ((this.modifiers & 4096) != 0) {
            this.bitflags |= 128;
        }
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl
    public boolean isEquivalentTo(Object other) {
        if (!(other instanceof BcelMethod)) {
            return false;
        }
        BcelMethod o = (BcelMethod) other;
        return getMethod().getCode().getCodeString().equals(o.getMethod().getCode().getCodeString());
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember
    public boolean isDefaultConstructor() {
        boolean mightBe = !hasDeclarationLineNumberInfo() && this.name.equals("<init>") && this.parameterTypes.length == 0;
        if (mightBe) {
            return true;
        }
        return false;
    }
}
