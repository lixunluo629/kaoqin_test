package org.apache.xmlbeans.impl.jam.internal.javadoc;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.ProgramElementDoc;
import org.apache.xmlbeans.impl.jam.internal.TigerDelegate;
import org.apache.xmlbeans.impl.jam.internal.elements.ElementContext;
import org.apache.xmlbeans.impl.jam.mutable.MAnnotatedElement;
import org.apache.xmlbeans.impl.jam.mutable.MClass;
import org.apache.xmlbeans.impl.jam.provider.JamLogger;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/javadoc/JavadocTigerDelegate.class */
public abstract class JavadocTigerDelegate extends TigerDelegate {
    private static final String JAVADOC_DELEGATE_IMPL = "org.apache.xmlbeans.impl.jam.internal.javadoc.JavadocTigerDelegateImpl_150";
    public static final String ANNOTATION_DEFAULTS_DISABLED_PROPERTY = "ANNOTATION_DEFAULTS_DISABLED_PROPERTY";

    public abstract boolean isEnum(ClassDoc classDoc);

    @Override // org.apache.xmlbeans.impl.jam.internal.TigerDelegate
    public abstract void init(JamLogger jamLogger);

    public abstract void populateAnnotationTypeIfNecessary(ClassDoc classDoc, MClass mClass, JavadocClassBuilder javadocClassBuilder);

    public abstract void extractAnnotations(MAnnotatedElement mAnnotatedElement, ProgramElementDoc programElementDoc);

    public abstract void extractAnnotations(MAnnotatedElement mAnnotatedElement, ExecutableMemberDoc executableMemberDoc, Parameter parameter);

    public static JavadocTigerDelegate create(JamLogger logger) {
        if (!isTigerJavadocAvailable(logger)) {
            return null;
        }
        try {
            JavadocTigerDelegate out = (JavadocTigerDelegate) Class.forName(JAVADOC_DELEGATE_IMPL).newInstance();
            out.init(logger);
            return out;
        } catch (ClassNotFoundException e) {
            issue14BuildWarning(e, logger);
            return null;
        } catch (IllegalAccessException e2) {
            logger.error(e2);
            return null;
        } catch (InstantiationException e3) {
            logger.error(e3);
            return null;
        }
    }

    public static JavadocTigerDelegate create(ElementContext ctx) {
        if (!isTigerJavadocAvailable(ctx.getLogger())) {
            return null;
        }
        try {
            JavadocTigerDelegate out = (JavadocTigerDelegate) Class.forName(JAVADOC_DELEGATE_IMPL).newInstance();
            out.init(ctx);
            return out;
        } catch (ClassNotFoundException e) {
            ctx.getLogger().error(e);
            return null;
        } catch (IllegalAccessException e2) {
            ctx.getLogger().error(e2);
            return null;
        } catch (InstantiationException e3) {
            ctx.getLogger().error(e3);
            return null;
        }
    }
}
