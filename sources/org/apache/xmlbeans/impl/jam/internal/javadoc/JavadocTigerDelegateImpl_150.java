package org.apache.xmlbeans.impl.jam.internal.javadoc;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.ProgramElementDoc;
import org.apache.xmlbeans.impl.jam.internal.elements.ElementContext;
import org.apache.xmlbeans.impl.jam.mutable.MAnnotatedElement;
import org.apache.xmlbeans.impl.jam.mutable.MClass;
import org.apache.xmlbeans.impl.jam.provider.JamLogger;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/javadoc/JavadocTigerDelegateImpl_150.class */
public final class JavadocTigerDelegateImpl_150 extends JavadocTigerDelegate {
    @Override // org.apache.xmlbeans.impl.jam.internal.TigerDelegate
    public void init(ElementContext ctx) {
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.javadoc.JavadocTigerDelegate, org.apache.xmlbeans.impl.jam.internal.TigerDelegate
    public void init(JamLogger logger) {
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.javadoc.JavadocTigerDelegate
    public void populateAnnotationTypeIfNecessary(ClassDoc cd, MClass clazz, JavadocClassBuilder builder) {
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.javadoc.JavadocTigerDelegate
    public void extractAnnotations(MAnnotatedElement dest, ProgramElementDoc src) {
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.javadoc.JavadocTigerDelegate
    public void extractAnnotations(MAnnotatedElement dest, ExecutableMemberDoc method, Parameter src) {
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.javadoc.JavadocTigerDelegate
    public boolean isEnum(ClassDoc cd) {
        return false;
    }
}
