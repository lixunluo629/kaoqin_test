package org.aspectj.weaver.bcel;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import org.aspectj.apache.bcel.classfile.annotation.AnnotationGen;
import org.aspectj.apache.bcel.classfile.annotation.NameValuePair;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/FakeAnnotation.class */
public class FakeAnnotation extends AnnotationGen {
    private String name;
    private String sig;
    private boolean isRuntimeVisible;

    public FakeAnnotation(String name, String sig, boolean isRuntimeVisible) {
        super(null, null, true, null);
        this.name = name;
        this.sig = sig;
        this.isRuntimeVisible = isRuntimeVisible;
    }

    @Override // org.aspectj.apache.bcel.classfile.annotation.AnnotationGen
    public String getTypeName() {
        return this.name;
    }

    @Override // org.aspectj.apache.bcel.classfile.annotation.AnnotationGen
    public String getTypeSignature() {
        return this.sig;
    }

    @Override // org.aspectj.apache.bcel.classfile.annotation.AnnotationGen
    public void addElementNameValuePair(NameValuePair evp) {
    }

    @Override // org.aspectj.apache.bcel.classfile.annotation.AnnotationGen
    public void dump(DataOutputStream dos) throws IOException {
    }

    @Override // org.aspectj.apache.bcel.classfile.annotation.AnnotationGen
    public int getTypeIndex() {
        return 0;
    }

    @Override // org.aspectj.apache.bcel.classfile.annotation.AnnotationGen
    public List getValues() {
        return null;
    }

    @Override // org.aspectj.apache.bcel.classfile.annotation.AnnotationGen
    public boolean isRuntimeVisible() {
        return this.isRuntimeVisible;
    }

    protected void setIsRuntimeVisible(boolean b) {
    }

    @Override // org.aspectj.apache.bcel.classfile.annotation.AnnotationGen
    public String toShortString() {
        return "@" + this.name;
    }

    @Override // org.aspectj.apache.bcel.classfile.annotation.AnnotationGen
    public String toString() {
        return this.name;
    }
}
