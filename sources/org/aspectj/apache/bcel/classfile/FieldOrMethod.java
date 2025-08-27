package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.aspectj.apache.bcel.classfile.annotation.AnnotationGen;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeAnnos;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/FieldOrMethod.class */
public abstract class FieldOrMethod extends Modifiers implements Node {
    protected int nameIndex;
    protected int signatureIndex;
    protected Attribute[] attributes;
    protected ConstantPool cpool;
    private String name;
    private String signature;
    private AnnotationGen[] annotations;
    private String signatureAttributeString;
    private boolean searchedForSignatureAttribute;

    protected FieldOrMethod() {
        this.signatureAttributeString = null;
        this.searchedForSignatureAttribute = false;
    }

    protected FieldOrMethod(FieldOrMethod fieldOrMethod) {
        this(fieldOrMethod.getModifiers(), fieldOrMethod.getNameIndex(), fieldOrMethod.getSignatureIndex(), fieldOrMethod.getAttributes(), fieldOrMethod.getConstantPool());
    }

    protected FieldOrMethod(DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        this(dataInputStream.readUnsignedShort(), dataInputStream.readUnsignedShort(), dataInputStream.readUnsignedShort(), null, constantPool);
        this.attributes = AttributeUtils.readAttributes(dataInputStream, constantPool);
    }

    protected FieldOrMethod(int i, int i2, int i3, Attribute[] attributeArr, ConstantPool constantPool) {
        this.signatureAttributeString = null;
        this.searchedForSignatureAttribute = false;
        this.modifiers = i;
        this.nameIndex = i2;
        this.signatureIndex = i3;
        this.cpool = constantPool;
        this.attributes = attributeArr;
    }

    public void setAttributes(Attribute[] attributeArr) {
        this.attributes = attributeArr;
    }

    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(this.modifiers);
        dataOutputStream.writeShort(this.nameIndex);
        dataOutputStream.writeShort(this.signatureIndex);
        AttributeUtils.writeAttributes(this.attributes, dataOutputStream);
    }

    public final Attribute[] getAttributes() {
        return this.attributes;
    }

    public final ConstantPool getConstantPool() {
        return this.cpool;
    }

    public final int getNameIndex() {
        return this.nameIndex;
    }

    public final int getSignatureIndex() {
        return this.signatureIndex;
    }

    public final String getName() {
        if (this.name == null) {
            this.name = ((ConstantUtf8) this.cpool.getConstant(this.nameIndex, (byte) 1)).getValue();
        }
        return this.name;
    }

    public final String getSignature() {
        if (this.signature == null) {
            this.signature = ((ConstantUtf8) this.cpool.getConstant(this.signatureIndex, (byte) 1)).getValue();
        }
        return this.signature;
    }

    public final String getDeclaredSignature() {
        return getGenericSignature() != null ? getGenericSignature() : getSignature();
    }

    public AnnotationGen[] getAnnotations() {
        if (this.annotations == null) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < this.attributes.length; i++) {
                Attribute attribute = this.attributes[i];
                if (attribute instanceof RuntimeAnnos) {
                    arrayList.addAll(((RuntimeAnnos) attribute).getAnnotations());
                }
            }
            if (arrayList.size() == 0) {
                this.annotations = AnnotationGen.NO_ANNOTATIONS;
            } else {
                this.annotations = (AnnotationGen[]) arrayList.toArray(new AnnotationGen[0]);
            }
        }
        return this.annotations;
    }

    public final String getGenericSignature() {
        if (!this.searchedForSignatureAttribute) {
            Signature signatureAttribute = AttributeUtils.getSignatureAttribute(this.attributes);
            this.signatureAttributeString = signatureAttribute == null ? null : signatureAttribute.getSignature();
            this.searchedForSignatureAttribute = true;
        }
        return this.signatureAttributeString;
    }
}
