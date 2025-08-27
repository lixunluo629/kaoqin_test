package org.apache.xmlbeans.impl.jam.internal.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.apache.xmlbeans.impl.jam.mutable.MClass;
import org.apache.xmlbeans.impl.jam.mutable.MConstructor;
import org.apache.xmlbeans.impl.jam.mutable.MField;
import org.apache.xmlbeans.impl.jam.mutable.MMember;
import org.apache.xmlbeans.impl.jam.mutable.MParameter;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/reflect/ReflectTigerDelegateImpl_150.class */
public final class ReflectTigerDelegateImpl_150 extends ReflectTigerDelegate {
    @Override // org.apache.xmlbeans.impl.jam.internal.reflect.ReflectTigerDelegate
    public void populateAnnotationTypeIfNecessary(Class cd, MClass clazz, ReflectClassBuilder builder) {
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.reflect.ReflectTigerDelegate
    public void extractAnnotations(MMember dest, Method src) {
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.reflect.ReflectTigerDelegate
    public void extractAnnotations(MConstructor dest, Constructor src) {
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.reflect.ReflectTigerDelegate
    public void extractAnnotations(MField dest, Field src) {
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.reflect.ReflectTigerDelegate
    public void extractAnnotations(MClass dest, Class src) {
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.reflect.ReflectTigerDelegate
    public void extractAnnotations(MParameter dest, Method src, int paramNum) {
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.reflect.ReflectTigerDelegate
    public void extractAnnotations(MParameter dest, Constructor src, int paramNum) {
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.reflect.ReflectTigerDelegate
    public boolean isEnum(Class clazz) {
        return false;
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.reflect.ReflectTigerDelegate
    public Constructor getEnclosingConstructor(Class clazz) {
        return null;
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.reflect.ReflectTigerDelegate
    public Method getEnclosingMethod(Class clazz) {
        return null;
    }
}
