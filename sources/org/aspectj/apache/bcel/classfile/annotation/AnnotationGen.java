package org.aspectj.apache.bcel.classfile.annotation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.aspectj.apache.bcel.classfile.ConstantUtf8;
import org.aspectj.apache.bcel.classfile.Utility;
import org.aspectj.apache.bcel.generic.ObjectType;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/annotation/AnnotationGen.class */
public class AnnotationGen {
    public static final AnnotationGen[] NO_ANNOTATIONS = new AnnotationGen[0];
    private int typeIndex;
    private List<NameValuePair> pairs;
    private ConstantPool cpool;
    private boolean isRuntimeVisible;

    public AnnotationGen(AnnotationGen annotationGen, ConstantPool constantPool, boolean z) {
        this.pairs = Collections.emptyList();
        this.isRuntimeVisible = false;
        this.cpool = constantPool;
        if (z) {
            this.typeIndex = constantPool.addUtf8(annotationGen.getTypeSignature());
        } else {
            this.typeIndex = annotationGen.getTypeIndex();
        }
        this.isRuntimeVisible = annotationGen.isRuntimeVisible();
        this.pairs = copyValues(annotationGen.getValues(), constantPool, z);
    }

    private List<NameValuePair> copyValues(List<NameValuePair> list, ConstantPool constantPool, boolean z) {
        ArrayList arrayList = new ArrayList();
        Iterator<NameValuePair> it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(new NameValuePair(it.next(), constantPool, z));
        }
        return arrayList;
    }

    private AnnotationGen(ConstantPool constantPool) {
        this.pairs = Collections.emptyList();
        this.isRuntimeVisible = false;
        this.cpool = constantPool;
    }

    public AnnotationGen(ObjectType objectType, List<NameValuePair> list, boolean z, ConstantPool constantPool) {
        this.pairs = Collections.emptyList();
        this.isRuntimeVisible = false;
        this.cpool = constantPool;
        if (objectType != null) {
            this.typeIndex = constantPool.addUtf8(objectType.getSignature());
        }
        this.pairs = list;
        this.isRuntimeVisible = z;
    }

    public static AnnotationGen read(DataInputStream dataInputStream, ConstantPool constantPool, boolean z) throws IOException {
        AnnotationGen annotationGen = new AnnotationGen(constantPool);
        annotationGen.typeIndex = dataInputStream.readUnsignedShort();
        int unsignedShort = dataInputStream.readUnsignedShort();
        for (int i = 0; i < unsignedShort; i++) {
            annotationGen.addElementNameValuePair(new NameValuePair(dataInputStream.readUnsignedShort(), ElementValue.readElementValue(dataInputStream, constantPool), constantPool));
        }
        annotationGen.isRuntimeVisible(z);
        return annotationGen;
    }

    public void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(this.typeIndex);
        dataOutputStream.writeShort(this.pairs.size());
        for (int i = 0; i < this.pairs.size(); i++) {
            this.pairs.get(i).dump(dataOutputStream);
        }
    }

    public void addElementNameValuePair(NameValuePair nameValuePair) {
        if (this.pairs == Collections.EMPTY_LIST) {
            this.pairs = new ArrayList();
        }
        this.pairs.add(nameValuePair);
    }

    public int getTypeIndex() {
        return this.typeIndex;
    }

    public String getTypeSignature() {
        return ((ConstantUtf8) this.cpool.getConstant(this.typeIndex)).getValue();
    }

    public String getTypeName() {
        return Utility.signatureToString(getTypeSignature());
    }

    public List<NameValuePair> getValues() {
        return this.pairs;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("AnnotationGen:[" + getTypeName() + " #" + this.pairs.size() + " {");
        for (int i = 0; i < this.pairs.size(); i++) {
            stringBuffer.append(this.pairs.get(i));
            if (i + 1 < this.pairs.size()) {
                stringBuffer.append(",");
            }
        }
        stringBuffer.append("}]");
        return stringBuffer.toString();
    }

    public String toShortString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("@").append(getTypeName());
        if (this.pairs.size() != 0) {
            stringBuffer.append("(");
            for (int i = 0; i < this.pairs.size(); i++) {
                stringBuffer.append(this.pairs.get(i));
                if (i + 1 < this.pairs.size()) {
                    stringBuffer.append(",");
                }
            }
            stringBuffer.append(")");
        }
        return stringBuffer.toString();
    }

    private void isRuntimeVisible(boolean z) {
        this.isRuntimeVisible = z;
    }

    public boolean isRuntimeVisible() {
        return this.isRuntimeVisible;
    }

    public boolean hasNameValuePair(String str, String str2) {
        for (NameValuePair nameValuePair : this.pairs) {
            if (nameValuePair.getNameString().equals(str) && nameValuePair.getValue().stringifyValue().equals(str2)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasNamedValue(String str) {
        Iterator<NameValuePair> it = this.pairs.iterator();
        while (it.hasNext()) {
            if (it.next().getNameString().equals(str)) {
                return true;
            }
        }
        return false;
    }
}
