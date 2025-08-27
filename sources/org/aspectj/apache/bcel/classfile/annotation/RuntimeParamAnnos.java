package org.aspectj.apache.bcel.classfile.annotation;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.aspectj.apache.bcel.classfile.Attribute;
import org.aspectj.apache.bcel.classfile.ConstantPool;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/annotation/RuntimeParamAnnos.class */
public abstract class RuntimeParamAnnos extends Attribute {
    private List<AnnotationGen[]> parameterAnnotations;
    private boolean visible;
    private boolean inflated;
    private byte[] annotation_data;

    public RuntimeParamAnnos(byte b, boolean z, int i, int i2, ConstantPool constantPool) {
        super(b, i, i2, constantPool);
        this.inflated = false;
        this.visible = z;
        this.parameterAnnotations = new ArrayList();
    }

    public RuntimeParamAnnos(byte b, boolean z, int i, int i2, byte[] bArr, ConstantPool constantPool) {
        super(b, i, i2, constantPool);
        this.inflated = false;
        this.visible = z;
        this.parameterAnnotations = new ArrayList();
        this.annotation_data = bArr;
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        super.dump(dataOutputStream);
        writeAnnotations(dataOutputStream);
    }

    public Attribute copy(ConstantPool constantPool) {
        throw new RuntimeException("Not implemented yet!");
    }

    public List<AnnotationGen[]> getParameterAnnotations() throws IOException {
        if (!this.inflated) {
            inflate();
        }
        return this.parameterAnnotations;
    }

    public AnnotationGen[] getAnnotationsOnParameter(int i) throws IOException {
        if (!this.inflated) {
            inflate();
        }
        return i >= this.parameterAnnotations.size() ? AnnotationGen.NO_ANNOTATIONS : this.parameterAnnotations.get(i);
    }

    public boolean areVisible() {
        return this.visible;
    }

    protected void readParameterAnnotations(DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        this.annotation_data = new byte[this.length];
        dataInputStream.readFully(this.annotation_data, 0, this.length);
    }

    private void inflate() throws IOException {
        try {
            DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(this.annotation_data));
            int unsignedByte = dataInputStream.readUnsignedByte();
            if (unsignedByte > 0) {
                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < unsignedByte; i++) {
                    int unsignedShort = dataInputStream.readUnsignedShort();
                    if (unsignedShort == 0) {
                        arrayList.add(AnnotationGen.NO_ANNOTATIONS);
                    } else {
                        AnnotationGen[] annotationGenArr = new AnnotationGen[unsignedShort];
                        for (int i2 = 0; i2 < unsignedShort; i2++) {
                            annotationGenArr[i2] = AnnotationGen.read(dataInputStream, getConstantPool(), this.visible);
                        }
                        arrayList.add(annotationGenArr);
                    }
                }
                this.parameterAnnotations = arrayList;
            }
            this.inflated = true;
        } catch (IOException e) {
            throw new RuntimeException("Unabled to inflate annotation data, badly formed?");
        }
    }

    protected void writeAnnotations(DataOutputStream dataOutputStream) throws IOException {
        if (!this.inflated) {
            dataOutputStream.write(this.annotation_data, 0, this.length);
            return;
        }
        dataOutputStream.writeByte(this.parameterAnnotations.size());
        for (int i = 0; i < this.parameterAnnotations.size(); i++) {
            AnnotationGen[] annotationGenArr = this.parameterAnnotations.get(i);
            dataOutputStream.writeShort(annotationGenArr.length);
            for (AnnotationGen annotationGen : annotationGenArr) {
                annotationGen.dump(dataOutputStream);
            }
        }
    }

    public boolean isInflated() {
        return this.inflated;
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public String toString() {
        return "Runtime" + (this.visible ? "Visible" : "Invisible") + "ParameterAnnotations [" + (this.inflated ? "inflated" : "not yet inflated") + "]";
    }
}
