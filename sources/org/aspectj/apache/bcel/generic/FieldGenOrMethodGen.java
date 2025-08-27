package org.aspectj.apache.bcel.generic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.aspectj.apache.bcel.classfile.Attribute;
import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.aspectj.apache.bcel.classfile.Modifiers;
import org.aspectj.apache.bcel.classfile.Utility;
import org.aspectj.apache.bcel.classfile.annotation.AnnotationGen;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeAnnos;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/FieldGenOrMethodGen.class */
public abstract class FieldGenOrMethodGen extends Modifiers {
    protected String name;
    protected Type type;
    protected ConstantPool cp;
    private ArrayList<Attribute> attributeList = new ArrayList<>();
    protected ArrayList<AnnotationGen> annotationList = new ArrayList<>();

    protected FieldGenOrMethodGen() {
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public ConstantPool getConstantPool() {
        return this.cp;
    }

    public void setConstantPool(ConstantPool constantPool) {
        this.cp = constantPool;
    }

    public void addAttribute(Attribute attribute) {
        this.attributeList.add(attribute);
    }

    public void removeAttribute(Attribute attribute) {
        this.attributeList.remove(attribute);
    }

    public void removeAttributes() {
        this.attributeList.clear();
    }

    public List<AnnotationGen> getAnnotations() {
        return this.annotationList;
    }

    public void addAnnotation(AnnotationGen annotationGen) {
        this.annotationList.add(annotationGen);
    }

    public void removeAnnotation(AnnotationGen annotationGen) {
        this.annotationList.remove(annotationGen);
    }

    public void removeAnnotations() {
        this.annotationList.clear();
    }

    public List<Attribute> getAttributes() {
        return this.attributeList;
    }

    public Attribute[] getAttributesImmutable() {
        Attribute[] attributeArr = new Attribute[this.attributeList.size()];
        this.attributeList.toArray(attributeArr);
        return attributeArr;
    }

    protected void addAnnotationsAsAttribute(ConstantPool constantPool) throws IOException {
        Collection<RuntimeAnnos> annotationAttributes = Utility.getAnnotationAttributes(constantPool, this.annotationList);
        if (annotationAttributes != null) {
            Iterator<RuntimeAnnos> it = annotationAttributes.iterator();
            while (it.hasNext()) {
                addAttribute(it.next());
            }
        }
    }

    public abstract String getSignature();
}
