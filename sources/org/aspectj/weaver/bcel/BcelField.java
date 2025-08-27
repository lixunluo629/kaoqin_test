package org.aspectj.weaver.bcel;

import java.util.List;
import org.aspectj.apache.bcel.classfile.Attribute;
import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.aspectj.apache.bcel.classfile.Field;
import org.aspectj.apache.bcel.classfile.Synthetic;
import org.aspectj.apache.bcel.classfile.annotation.AnnotationGen;
import org.aspectj.apache.bcel.generic.FieldGen;
import org.aspectj.bridge.AbortException;
import org.aspectj.util.GenericSignature;
import org.aspectj.util.GenericSignatureParser;
import org.aspectj.weaver.AjAttribute;
import org.aspectj.weaver.AnnotationAJ;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ResolvedMemberImpl;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.World;
import org.aspectj.weaver.bcel.BcelGenericSignatureToTypeXConverter;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/BcelField.class */
final class BcelField extends ResolvedMemberImpl {
    public static int AccSynthetic = 4096;
    private Field field;
    private boolean isAjSynthetic;
    private boolean isSynthetic;
    private AnnotationAJ[] annotations;
    private final World world;
    private final BcelObjectType bcelObjectType;
    private UnresolvedType genericFieldType;
    private boolean unpackedGenericSignature;
    private boolean annotationsOnFieldObjectAreOutOfDate;

    BcelField(BcelObjectType declaringType, Field field) throws AbortException {
        super(FIELD, declaringType.getResolvedTypeX(), field.getModifiers(), field.getName(), field.getSignature());
        this.isSynthetic = false;
        this.genericFieldType = null;
        this.unpackedGenericSignature = false;
        this.annotationsOnFieldObjectAreOutOfDate = false;
        this.field = field;
        this.world = declaringType.getResolvedTypeX().getWorld();
        this.bcelObjectType = declaringType;
        unpackAttributes(this.world);
        this.checkedExceptions = UnresolvedType.NONE;
    }

    BcelField(String declaringTypeName, Field field, World world) throws AbortException {
        super(FIELD, UnresolvedType.forName(declaringTypeName), field.getModifiers(), field.getName(), field.getSignature());
        this.isSynthetic = false;
        this.genericFieldType = null;
        this.unpackedGenericSignature = false;
        this.annotationsOnFieldObjectAreOutOfDate = false;
        this.field = field;
        this.world = world;
        this.bcelObjectType = null;
        unpackAttributes(world);
        this.checkedExceptions = UnresolvedType.NONE;
    }

    private void unpackAttributes(World world) throws AbortException {
        Attribute[] attrs = this.field.getAttributes();
        if (attrs != null && attrs.length > 0) {
            ISourceContext sourceContext = getSourceContext(world);
            List<AjAttribute> as = Utility.readAjAttributes(getDeclaringType().getClassName(), attrs, sourceContext, world, this.bcelObjectType != null ? this.bcelObjectType.getWeaverVersionAttribute() : AjAttribute.WeaverVersionInfo.CURRENT, new BcelConstantPoolReader(this.field.getConstantPool()));
            as.addAll(AtAjAttributes.readAj5FieldAttributes(this.field, this, world.resolve(getDeclaringType()), sourceContext, world.getMessageHandler()));
        }
        this.isAjSynthetic = false;
        for (int i = attrs.length - 1; i >= 0; i--) {
            if (attrs[i] instanceof Synthetic) {
                this.isSynthetic = true;
            }
        }
        if ((this.field.getModifiers() & AccSynthetic) != 0) {
            this.isSynthetic = true;
        }
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember
    public boolean isAjSynthetic() {
        return this.isAjSynthetic;
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember
    public boolean isSynthetic() {
        return this.isSynthetic;
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember, org.aspectj.weaver.AnnotatedElement
    public boolean hasAnnotation(UnresolvedType ofType) {
        ensureAnnotationTypesRetrieved();
        for (ResolvedType aType : this.annotationTypes) {
            if (aType.equals(ofType)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember, org.aspectj.weaver.AnnotatedElement
    public ResolvedType[] getAnnotationTypes() {
        ensureAnnotationTypesRetrieved();
        return this.annotationTypes;
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.MemberImpl, org.aspectj.weaver.Member
    public AnnotationAJ[] getAnnotations() {
        ensureAnnotationTypesRetrieved();
        return this.annotations;
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.AnnotatedElement
    public AnnotationAJ getAnnotationOfType(UnresolvedType ofType) {
        ensureAnnotationTypesRetrieved();
        for (AnnotationAJ annotation : this.annotations) {
            if (annotation.getTypeName().equals(ofType.getName())) {
                return annotation;
            }
        }
        return null;
    }

    private void ensureAnnotationTypesRetrieved() {
        if (this.annotationTypes == null) {
            AnnotationGen[] annos = this.field.getAnnotations();
            if (annos.length == 0) {
                this.annotationTypes = ResolvedType.EMPTY_ARRAY;
                this.annotations = AnnotationAJ.EMPTY_ARRAY;
                return;
            }
            int annosCount = annos.length;
            this.annotationTypes = new ResolvedType[annosCount];
            this.annotations = new AnnotationAJ[annosCount];
            for (int i = 0; i < annosCount; i++) {
                AnnotationGen anno = annos[i];
                this.annotations[i] = new BcelAnnotation(anno, this.world);
                this.annotationTypes[i] = this.annotations[i].getType();
            }
        }
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember
    public void addAnnotation(AnnotationAJ annotation) {
        ensureAnnotationTypesRetrieved();
        int len = this.annotations.length;
        AnnotationAJ[] ret = new AnnotationAJ[len + 1];
        System.arraycopy(this.annotations, 0, ret, 0, len);
        ret[len] = annotation;
        this.annotations = ret;
        ResolvedType[] newAnnotationTypes = new ResolvedType[len + 1];
        System.arraycopy(this.annotationTypes, 0, newAnnotationTypes, 0, len);
        newAnnotationTypes[len] = annotation.getType();
        this.annotationTypes = newAnnotationTypes;
        this.annotationsOnFieldObjectAreOutOfDate = true;
    }

    public void removeAnnotation(AnnotationAJ annotation) {
        ensureAnnotationTypesRetrieved();
        int len = this.annotations.length;
        AnnotationAJ[] ret = new AnnotationAJ[len - 1];
        int p = 0;
        for (AnnotationAJ anno : this.annotations) {
            if (!anno.getType().equals(annotation.getType())) {
                int i = p;
                p++;
                ret[i] = anno;
            }
        }
        this.annotations = ret;
        ResolvedType[] newAnnotationTypes = new ResolvedType[len - 1];
        int p2 = 0;
        for (ResolvedType anno2 : this.annotationTypes) {
            if (!anno2.equals(annotation.getType())) {
                int i2 = p2;
                p2++;
                newAnnotationTypes[i2] = anno2;
            }
        }
        this.annotationTypes = newAnnotationTypes;
        this.annotationsOnFieldObjectAreOutOfDate = true;
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.MemberImpl, org.aspectj.weaver.Member
    public UnresolvedType getGenericReturnType() {
        unpackGenericSignature();
        return this.genericFieldType;
    }

    public Field getFieldAsIs() {
        return this.field;
    }

    public Field getField(ConstantPool cpool) {
        if (!this.annotationsOnFieldObjectAreOutOfDate) {
            return this.field;
        }
        FieldGen newFieldGen = new FieldGen(this.field, cpool);
        newFieldGen.removeAnnotations();
        for (AnnotationAJ annotation : this.annotations) {
            newFieldGen.addAnnotation(new AnnotationGen(((BcelAnnotation) annotation).getBcelAnnotation(), cpool, true));
        }
        this.field = newFieldGen.getField();
        this.annotationsOnFieldObjectAreOutOfDate = false;
        return this.field;
    }

    private void unpackGenericSignature() {
        if (this.unpackedGenericSignature) {
            return;
        }
        if (!this.world.isInJava5Mode()) {
            this.genericFieldType = getReturnType();
            return;
        }
        this.unpackedGenericSignature = true;
        String gSig = this.field.getGenericSignature();
        if (gSig != null) {
            GenericSignature.FieldTypeSignature fts = new GenericSignatureParser().parseAsFieldSignature(gSig);
            GenericSignature.ClassSignature genericTypeSig = this.bcelObjectType.getGenericClassTypeSignature();
            GenericSignature.FormalTypeParameter[] parentFormals = this.bcelObjectType.getAllFormals();
            GenericSignature.FormalTypeParameter[] typeVars = genericTypeSig == null ? new GenericSignature.FormalTypeParameter[0] : genericTypeSig.formalTypeParameters;
            GenericSignature.FormalTypeParameter[] formals = new GenericSignature.FormalTypeParameter[parentFormals.length + typeVars.length];
            System.arraycopy(typeVars, 0, formals, 0, typeVars.length);
            System.arraycopy(parentFormals, 0, formals, typeVars.length, parentFormals.length);
            try {
                this.genericFieldType = BcelGenericSignatureToTypeXConverter.fieldTypeSignature2TypeX(fts, formals, this.world);
                return;
            } catch (BcelGenericSignatureToTypeXConverter.GenericSignatureFormatException e) {
                throw new IllegalStateException("While determing the generic field type of " + toString() + " with generic signature " + gSig + " the following error was detected: " + e.getMessage());
            }
        }
        this.genericFieldType = getReturnType();
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember
    public void evictWeavingState() throws AbortException {
        if (this.field != null) {
            unpackGenericSignature();
            unpackAttributes(this.world);
            ensureAnnotationTypesRetrieved();
            this.field = null;
        }
    }
}
