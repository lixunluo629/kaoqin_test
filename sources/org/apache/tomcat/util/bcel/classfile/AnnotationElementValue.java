package org.apache.tomcat.util.bcel.classfile;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/bcel/classfile/AnnotationElementValue.class */
public class AnnotationElementValue extends ElementValue {
    private final AnnotationEntry annotationEntry;

    AnnotationElementValue(int type, AnnotationEntry annotationEntry, ConstantPool cpool) {
        super(type, cpool);
        if (type != 64) {
            throw new RuntimeException("Only element values of type annotation can be built with this ctor - type specified: " + type);
        }
        this.annotationEntry = annotationEntry;
    }

    @Override // org.apache.tomcat.util.bcel.classfile.ElementValue
    public String stringifyValue() {
        return this.annotationEntry.toString();
    }

    public AnnotationEntry getAnnotationEntry() {
        return this.annotationEntry;
    }
}
