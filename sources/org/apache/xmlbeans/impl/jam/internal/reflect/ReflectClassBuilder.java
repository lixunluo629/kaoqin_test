package org.apache.xmlbeans.impl.jam.internal.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.apache.xmlbeans.impl.jam.internal.elements.ElementContext;
import org.apache.xmlbeans.impl.jam.mutable.MClass;
import org.apache.xmlbeans.impl.jam.mutable.MConstructor;
import org.apache.xmlbeans.impl.jam.mutable.MField;
import org.apache.xmlbeans.impl.jam.mutable.MInvokable;
import org.apache.xmlbeans.impl.jam.mutable.MMethod;
import org.apache.xmlbeans.impl.jam.mutable.MParameter;
import org.apache.xmlbeans.impl.jam.provider.JamClassBuilder;
import org.apache.xmlbeans.impl.jam.provider.JamClassPopulator;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/reflect/ReflectClassBuilder.class */
public class ReflectClassBuilder extends JamClassBuilder implements JamClassPopulator {
    private ClassLoader mLoader;
    private ReflectTigerDelegate mTigerDelegate = null;

    public ReflectClassBuilder(ClassLoader rcl) {
        if (rcl == null) {
            throw new IllegalArgumentException("null rcl");
        }
        this.mLoader = rcl;
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamClassBuilder
    public void init(ElementContext ctx) {
        super.init(ctx);
        initDelegate(ctx);
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamClassBuilder
    public MClass build(String packageName, String className) throws ClassNotFoundException {
        assertInitialized();
        if (getLogger().isVerbose(this)) {
            getLogger().verbose("trying to build '" + packageName + "' '" + className + "'");
        }
        try {
            String loadme = packageName.trim().length() > 0 ? packageName + '.' + className : className;
            Class rclass = this.mLoader.loadClass(loadme);
            MClass out = createClassToBuild(packageName, className, null, this);
            out.setArtifact(rclass);
            return out;
        } catch (ClassNotFoundException cnfe) {
            getLogger().verbose(cnfe, this);
            return null;
        }
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamClassPopulator
    public void populate(MClass dest) throws SecurityException {
        assertInitialized();
        Class src = (Class) dest.getArtifact();
        dest.setModifiers(src.getModifiers());
        dest.setIsInterface(src.isInterface());
        if (this.mTigerDelegate != null) {
            dest.setIsEnumType(this.mTigerDelegate.isEnum(src));
        }
        Class s = src.getSuperclass();
        if (s != null) {
            dest.setSuperclass(s.getName());
        }
        Class[] ints = src.getInterfaces();
        for (Class cls : ints) {
            dest.addInterface(cls.getName());
        }
        Field[] fields = null;
        try {
            fields = src.getFields();
        } catch (Exception e) {
        }
        if (fields != null) {
            for (Field field : fields) {
                populate(dest.addNewField(), field);
            }
        }
        Method[] methods = src.getDeclaredMethods();
        for (Method method : methods) {
            populate(dest.addNewMethod(), method);
        }
        if (this.mTigerDelegate != null) {
            this.mTigerDelegate.populateAnnotationTypeIfNecessary(src, dest, this);
        }
        Constructor[] ctors = src.getDeclaredConstructors();
        for (Constructor constructor : ctors) {
            populate(dest.addNewConstructor(), constructor);
        }
        if (this.mTigerDelegate != null) {
            this.mTigerDelegate.extractAnnotations(dest, src);
        }
        Class[] inners = src.getDeclaredClasses();
        if (inners != null) {
            for (int i = 0; i < inners.length; i++) {
                if (this.mTigerDelegate == null || (this.mTigerDelegate.getEnclosingConstructor(inners[i]) == null && this.mTigerDelegate.getEnclosingMethod(inners[i]) == null)) {
                    String simpleName = inners[i].getName();
                    int lastDollar = simpleName.lastIndexOf(36);
                    String simpleName2 = simpleName.substring(lastDollar + 1);
                    char first = simpleName2.charAt(0);
                    if ('0' > first || first > '9') {
                        MClass inner = dest.addNewInnerClass(simpleName2);
                        inner.setArtifact(inners[i]);
                        populate(inner);
                    }
                }
            }
        }
    }

    private void initDelegate(ElementContext ctx) {
        this.mTigerDelegate = ReflectTigerDelegate.create(ctx);
    }

    private void populate(MField dest, Field src) {
        dest.setArtifact(src);
        dest.setSimpleName(src.getName());
        dest.setType(src.getType().getName());
        dest.setModifiers(src.getModifiers());
        if (this.mTigerDelegate != null) {
            this.mTigerDelegate.extractAnnotations(dest, src);
        }
    }

    private void populate(MConstructor dest, Constructor src) {
        dest.setArtifact(src);
        dest.setSimpleName(src.getName());
        dest.setModifiers(src.getModifiers());
        Class[] exceptions = src.getExceptionTypes();
        addThrows(dest, exceptions);
        Class[] paramTypes = src.getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            MParameter p = addParameter(dest, i, paramTypes[i]);
            if (this.mTigerDelegate != null) {
                this.mTigerDelegate.extractAnnotations(p, src, i);
            }
        }
        if (this.mTigerDelegate != null) {
            this.mTigerDelegate.extractAnnotations(dest, src);
        }
    }

    private void populate(MMethod dest, Method src) {
        dest.setArtifact(src);
        dest.setSimpleName(src.getName());
        dest.setModifiers(src.getModifiers());
        dest.setReturnType(src.getReturnType().getName());
        Class[] exceptions = src.getExceptionTypes();
        addThrows(dest, exceptions);
        Class[] paramTypes = src.getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            MParameter p = addParameter(dest, i, paramTypes[i]);
            if (this.mTigerDelegate != null) {
                this.mTigerDelegate.extractAnnotations(p, src, i);
            }
        }
        if (this.mTigerDelegate != null) {
            this.mTigerDelegate.extractAnnotations(dest, src);
        }
    }

    private void addThrows(MInvokable dest, Class[] exceptionTypes) {
        for (Class cls : exceptionTypes) {
            dest.addException(cls.getName());
        }
    }

    private MParameter addParameter(MInvokable dest, int paramNum, Class paramType) {
        MParameter p = dest.addNewParameter();
        p.setSimpleName("param" + paramNum);
        p.setType(paramType.getName());
        return p;
    }
}
