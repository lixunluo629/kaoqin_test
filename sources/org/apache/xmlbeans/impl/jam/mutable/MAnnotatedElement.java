package org.apache.xmlbeans.impl.jam.mutable;

import org.apache.xmlbeans.impl.jam.JAnnotatedElement;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/mutable/MAnnotatedElement.class */
public interface MAnnotatedElement extends MElement, JAnnotatedElement {
    MAnnotation findOrCreateAnnotation(String str);

    MAnnotation[] getMutableAnnotations();

    MAnnotation getMutableAnnotation(String str);

    MAnnotation addLiteralAnnotation(String str);

    MComment getMutableComment();

    MComment createComment();

    void removeComment();
}
