package net.sf.cglib.core;

/* loaded from: cglib-3.1.jar:net/sf/cglib/core/ClassesKey.class */
public class ClassesKey {
    private static final Key FACTORY;
    static Class class$net$sf$cglib$core$ClassesKey$Key;

    /* loaded from: cglib-3.1.jar:net/sf/cglib/core/ClassesKey$Key.class */
    interface Key {
        Object newInstance(Object[] objArr);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$net$sf$cglib$core$ClassesKey$Key == null) {
            clsClass$ = class$("net.sf.cglib.core.ClassesKey$Key");
            class$net$sf$cglib$core$ClassesKey$Key = clsClass$;
        } else {
            clsClass$ = class$net$sf$cglib$core$ClassesKey$Key;
        }
        FACTORY = (Key) KeyFactory.create(clsClass$, KeyFactory.OBJECT_BY_CLASS);
    }

    private ClassesKey() {
    }

    public static Object create(Object[] array) {
        return FACTORY.newInstance(array);
    }
}
