package org.aspectj.apache.bcel.classfile.annotation;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.aspectj.apache.bcel.classfile.Attribute;
import org.aspectj.apache.bcel.classfile.ConstantPool;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/annotation/RuntimeAnnos.class */
public abstract class RuntimeAnnos extends Attribute {
    private List<AnnotationGen> annotations;
    private boolean visible;
    private boolean inflated;
    private byte[] annotation_data;

    public RuntimeAnnos(byte b, boolean z, int i, int i2, ConstantPool constantPool) {
        super(b, i, i2, constantPool);
        this.inflated = false;
        this.visible = z;
        this.annotations = new ArrayList();
    }

    public RuntimeAnnos(byte b, boolean z, int i, int i2, byte[] bArr, ConstantPool constantPool) {
        super(b, i, i2, constantPool);
        this.inflated = false;
        this.visible = z;
        this.annotations = new ArrayList();
        this.annotation_data = bArr;
    }

    public List<AnnotationGen> getAnnotations() throws IOException {
        if (!this.inflated) {
            inflate();
        }
        return this.annotations;
    }

    public boolean areVisible() {
        return this.visible;
    }

    protected void readAnnotations(DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        this.annotation_data = new byte[this.length];
        dataInputStream.readFully(this.annotation_data, 0, this.length);
    }

    protected void writeAnnotations(DataOutputStream dataOutputStream) throws IOException {
        if (!this.inflated) {
            dataOutputStream.write(this.annotation_data, 0, this.length);
            return;
        }
        dataOutputStream.writeShort(this.annotations.size());
        Iterator<AnnotationGen> it = this.annotations.iterator();
        while (it.hasNext()) {
            it.next().dump(dataOutputStream);
        }
    }

    private void inflate() throws IOException {
        try {
            DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(this.annotation_data));
            int unsignedShort = dataInputStream.readUnsignedShort();
            if (unsignedShort > 0) {
                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < unsignedShort; i++) {
                    arrayList.add(AnnotationGen.read(dataInputStream, getConstantPool(), this.visible));
                }
                this.annotations = arrayList;
            }
            dataInputStream.close();
            this.inflated = true;
        } catch (IOException e) {
            throw new RuntimeException("Unabled to inflate annotation data, badly formed? ");
        }
    }

    public boolean isInflated() {
        return this.inflated;
    }
}
