package net.sf.cglib.core;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* loaded from: cglib-3.1.jar:net/sf/cglib/core/MethodWrapper.class */
public class MethodWrapper {
    private static final MethodWrapperKey KEY_FACTORY;
    static Class class$net$sf$cglib$core$MethodWrapper$MethodWrapperKey;

    /* loaded from: cglib-3.1.jar:net/sf/cglib/core/MethodWrapper$MethodWrapperKey.class */
    public interface MethodWrapperKey {
        Object newInstance(String str, String[] strArr, String str2);
    }

    static {
        Class clsClass$;
        if (class$net$sf$cglib$core$MethodWrapper$MethodWrapperKey == null) {
            clsClass$ = class$("net.sf.cglib.core.MethodWrapper$MethodWrapperKey");
            class$net$sf$cglib$core$MethodWrapper$MethodWrapperKey = clsClass$;
        } else {
            clsClass$ = class$net$sf$cglib$core$MethodWrapper$MethodWrapperKey;
        }
        KEY_FACTORY = (MethodWrapperKey) KeyFactory.create(clsClass$);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    private MethodWrapper() {
    }

    public static Object create(Method method) {
        return KEY_FACTORY.newInstance(method.getName(), ReflectUtils.getNames(method.getParameterTypes()), method.getReturnType().getName());
    }

    public static Set createSet(Collection methods) {
        Set set = new HashSet();
        Iterator it = methods.iterator();
        while (it.hasNext()) {
            set.add(create((Method) it.next()));
        }
        return set;
    }
}
