package org.apache.xmlbeans.impl.jam.internal.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.apache.xmlbeans.impl.jam.internal.TigerDelegate;
import org.apache.xmlbeans.impl.jam.internal.elements.ElementContext;
import org.apache.xmlbeans.impl.jam.mutable.MClass;
import org.apache.xmlbeans.impl.jam.mutable.MConstructor;
import org.apache.xmlbeans.impl.jam.mutable.MField;
import org.apache.xmlbeans.impl.jam.mutable.MMember;
import org.apache.xmlbeans.impl.jam.mutable.MParameter;
import org.apache.xmlbeans.impl.jam.provider.JamLogger;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/reflect/ReflectTigerDelegate.class */
public abstract class ReflectTigerDelegate extends TigerDelegate {
    private static final String IMPL_NAME = "org.apache.xmlbeans.impl.jam.internal.reflect.ReflectTigerDelegateImpl_150";

    public abstract void populateAnnotationTypeIfNecessary(Class cls, MClass mClass, ReflectClassBuilder reflectClassBuilder);

    public abstract boolean isEnum(Class cls);

    public abstract Constructor getEnclosingConstructor(Class cls);

    public abstract Method getEnclosingMethod(Class cls);

    public abstract void extractAnnotations(MMember mMember, Method method);

    public abstract void extractAnnotations(MConstructor mConstructor, Constructor constructor);

    public abstract void extractAnnotations(MField mField, Field field);

    public abstract void extractAnnotations(MClass mClass, Class cls);

    public abstract void extractAnnotations(MParameter mParameter, Method method, int i);

    public abstract void extractAnnotations(MParameter mParameter, Constructor constructor, int i);

    public static ReflectTigerDelegate create(JamLogger logger) {
        if (!isTigerReflectionAvailable(logger)) {
            return null;
        }
        try {
            ReflectTigerDelegate out = (ReflectTigerDelegate) Class.forName(IMPL_NAME).newInstance();
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

    public static ReflectTigerDelegate create(ElementContext ctx) {
        if (!isTigerReflectionAvailable(ctx.getLogger())) {
            return null;
        }
        try {
            ReflectTigerDelegate out = (ReflectTigerDelegate) Class.forName(IMPL_NAME).newInstance();
            out.init(ctx);
            return out;
        } catch (ClassNotFoundException e) {
            issue14BuildWarning(e, ctx.getLogger());
            return null;
        } catch (IllegalAccessException e2) {
            ctx.getLogger().error(e2);
            return null;
        } catch (InstantiationException e3) {
            ctx.getLogger().error(e3);
            return null;
        }
    }

    protected ReflectTigerDelegate() {
    }
}
