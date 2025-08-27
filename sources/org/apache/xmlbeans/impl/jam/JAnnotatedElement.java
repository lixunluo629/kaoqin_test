package org.apache.xmlbeans.impl.jam;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/JAnnotatedElement.class */
public interface JAnnotatedElement extends JElement {
    JAnnotation[] getAnnotations();

    JAnnotation getAnnotation(Class cls);

    Object getAnnotationProxy(Class cls);

    JAnnotation getAnnotation(String str);

    JAnnotationValue getAnnotationValue(String str);

    JComment getComment();

    JAnnotation[] getAllJavadocTags();
}
