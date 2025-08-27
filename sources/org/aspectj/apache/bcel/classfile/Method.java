package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.aspectj.apache.bcel.classfile.annotation.AnnotationGen;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeInvisParamAnnos;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeVisParamAnnos;
import org.aspectj.apache.bcel.generic.Type;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/Method.class */
public final class Method extends FieldOrMethod {
    public static final AnnotationGen[][] NO_PARAMETER_ANNOTATIONS = new AnnotationGen[0];
    public static final Method[] NoMethods = new Method[0];
    private boolean parameterAnnotationsOutOfDate;
    private AnnotationGen[][] unpackedParameterAnnotations;

    private Method() {
        this.parameterAnnotationsOutOfDate = true;
        this.parameterAnnotationsOutOfDate = true;
    }

    public Method(Method method) {
        super(method);
        this.parameterAnnotationsOutOfDate = true;
        this.parameterAnnotationsOutOfDate = true;
    }

    Method(DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        super(dataInputStream, constantPool);
        this.parameterAnnotationsOutOfDate = true;
    }

    public Method(int i, int i2, int i3, Attribute[] attributeArr, ConstantPool constantPool) {
        super(i, i2, i3, attributeArr, constantPool);
        this.parameterAnnotationsOutOfDate = true;
        this.parameterAnnotationsOutOfDate = true;
    }

    @Override // org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitMethod(this);
    }

    @Override // org.aspectj.apache.bcel.classfile.FieldOrMethod
    public void setAttributes(Attribute[] attributeArr) {
        this.parameterAnnotationsOutOfDate = true;
        super.setAttributes(attributeArr);
    }

    public final Code getCode() {
        return AttributeUtils.getCodeAttribute(this.attributes);
    }

    public final ExceptionTable getExceptionTable() {
        return AttributeUtils.getExceptionTableAttribute(this.attributes);
    }

    public final LocalVariableTable getLocalVariableTable() {
        Code code = getCode();
        if (code != null) {
            return code.getLocalVariableTable();
        }
        return null;
    }

    public final LineNumberTable getLineNumberTable() {
        Code code = getCode();
        if (code != null) {
            return code.getLineNumberTable();
        }
        return null;
    }

    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer(Utility.methodSignatureToString(((ConstantUtf8) this.cpool.getConstant(this.signatureIndex, (byte) 1)).getValue(), ((ConstantUtf8) this.cpool.getConstant(this.nameIndex, (byte) 1)).getValue(), Utility.accessToString(this.modifiers), true, getLocalVariableTable()));
        for (int i = 0; i < this.attributes.length; i++) {
            Attribute attribute = this.attributes[i];
            if (!(attribute instanceof Code) && !(attribute instanceof ExceptionTable)) {
                stringBuffer.append(" [" + attribute.toString() + "]");
            }
        }
        ExceptionTable exceptionTable = getExceptionTable();
        if (exceptionTable != null) {
            String string = exceptionTable.toString();
            if (!string.equals("")) {
                stringBuffer.append("\n\t\tthrows " + string);
            }
        }
        return stringBuffer.toString();
    }

    public Type getReturnType() {
        return Type.getReturnType(getSignature());
    }

    public Type[] getArgumentTypes() {
        return Type.getArgumentTypes(getSignature());
    }

    private void ensureParameterAnnotationsUnpacked() {
        if (this.parameterAnnotationsOutOfDate) {
            this.parameterAnnotationsOutOfDate = false;
            int length = getArgumentTypes().length;
            if (length == 0) {
                this.unpackedParameterAnnotations = NO_PARAMETER_ANNOTATIONS;
                return;
            }
            RuntimeVisParamAnnos runtimeVisParamAnnos = null;
            RuntimeInvisParamAnnos runtimeInvisParamAnnos = null;
            for (Attribute attribute : getAttributes()) {
                if (attribute instanceof RuntimeVisParamAnnos) {
                    runtimeVisParamAnnos = (RuntimeVisParamAnnos) attribute;
                } else if (attribute instanceof RuntimeInvisParamAnnos) {
                    runtimeInvisParamAnnos = (RuntimeInvisParamAnnos) attribute;
                }
            }
            boolean z = false;
            if (runtimeInvisParamAnnos != null || runtimeVisParamAnnos != null) {
                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < length; i++) {
                    int length2 = 0;
                    AnnotationGen[] annotationsOnParameter = new AnnotationGen[0];
                    AnnotationGen[] annotationsOnParameter2 = new AnnotationGen[0];
                    if (runtimeVisParamAnnos != null) {
                        annotationsOnParameter = runtimeVisParamAnnos.getAnnotationsOnParameter(i);
                        length2 = 0 + annotationsOnParameter.length;
                    }
                    if (runtimeInvisParamAnnos != null) {
                        annotationsOnParameter2 = runtimeInvisParamAnnos.getAnnotationsOnParameter(i);
                        length2 += annotationsOnParameter2.length;
                    }
                    AnnotationGen[] annotationGenArr = AnnotationGen.NO_ANNOTATIONS;
                    if (length2 != 0) {
                        annotationGenArr = new AnnotationGen[annotationsOnParameter.length + annotationsOnParameter2.length];
                        System.arraycopy(annotationsOnParameter, 0, annotationGenArr, 0, annotationsOnParameter.length);
                        System.arraycopy(annotationsOnParameter2, 0, annotationGenArr, annotationsOnParameter.length, annotationsOnParameter2.length);
                        z = true;
                    }
                    arrayList.add(annotationGenArr);
                }
                if (z) {
                    this.unpackedParameterAnnotations = (AnnotationGen[][]) arrayList.toArray(new AnnotationGen[0]);
                    return;
                }
            }
            this.unpackedParameterAnnotations = NO_PARAMETER_ANNOTATIONS;
        }
    }

    public AnnotationGen[] getAnnotationsOnParameter(int i) {
        ensureParameterAnnotationsUnpacked();
        return this.unpackedParameterAnnotations == NO_PARAMETER_ANNOTATIONS ? AnnotationGen.NO_ANNOTATIONS : this.unpackedParameterAnnotations[i];
    }

    public AnnotationGen[][] getParameterAnnotations() {
        ensureParameterAnnotationsUnpacked();
        return this.unpackedParameterAnnotations;
    }
}
