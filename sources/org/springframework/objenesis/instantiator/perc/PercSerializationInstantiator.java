package org.springframework.objenesis.instantiator.perc;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.objenesis.instantiator.ObjectInstantiator;
import org.springframework.objenesis.instantiator.annotations.Instantiator;
import org.springframework.objenesis.instantiator.annotations.Typology;

@Instantiator(Typology.SERIALIZATION)
/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/objenesis/instantiator/perc/PercSerializationInstantiator.class */
public class PercSerializationInstantiator<T> implements ObjectInstantiator<T> {
    private Object[] typeArgs;
    private final Method newInstanceMethod;

    public PercSerializationInstantiator(Class<T> type) throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException, SecurityException, IllegalArgumentException, InvocationTargetException {
        Class<T> superclass = type;
        while (true) {
            Class<T> cls = superclass;
            if (Serializable.class.isAssignableFrom(cls)) {
                superclass = cls.getSuperclass();
            } else {
                try {
                    Class<?> percMethodClass = Class.forName("COM.newmonics.PercClassLoader.Method");
                    this.newInstanceMethod = ObjectInputStream.class.getDeclaredMethod("noArgConstruct", Class.class, Object.class, percMethodClass);
                    this.newInstanceMethod.setAccessible(true);
                    Class<?> percClassClass = Class.forName("COM.newmonics.PercClassLoader.PercClass");
                    Method getPercClassMethod = percClassClass.getDeclaredMethod("getPercClass", Class.class);
                    Object someObject = getPercClassMethod.invoke(null, cls);
                    Method findMethodMethod = someObject.getClass().getDeclaredMethod("findMethod", String.class);
                    Object percMethod = findMethodMethod.invoke(someObject, "<init>()V");
                    this.typeArgs = new Object[]{cls, type, percMethod};
                    return;
                } catch (ClassNotFoundException e) {
                    throw new ObjenesisException(e);
                } catch (IllegalAccessException e2) {
                    throw new ObjenesisException(e2);
                } catch (NoSuchMethodException e3) {
                    throw new ObjenesisException(e3);
                } catch (InvocationTargetException e4) {
                    throw new ObjenesisException(e4);
                }
            }
        }
    }

    @Override // org.springframework.objenesis.instantiator.ObjectInstantiator
    public T newInstance() {
        try {
            return (T) this.newInstanceMethod.invoke(null, this.typeArgs);
        } catch (IllegalAccessException e) {
            throw new ObjenesisException(e);
        } catch (InvocationTargetException e2) {
            throw new ObjenesisException(e2);
        }
    }
}
