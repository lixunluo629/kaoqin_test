package org.springframework.objenesis.instantiator.android;

import java.io.ObjectStreamClass;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.objenesis.instantiator.ObjectInstantiator;
import org.springframework.objenesis.instantiator.annotations.Instantiator;
import org.springframework.objenesis.instantiator.annotations.Typology;

@Instantiator(Typology.SERIALIZATION)
/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/objenesis/instantiator/android/AndroidSerializationInstantiator.class */
public class AndroidSerializationInstantiator<T> implements ObjectInstantiator<T> {
    private final Class<T> type;
    private final ObjectStreamClass objectStreamClass;
    private final Method newInstanceMethod = getNewInstanceMethod();

    public AndroidSerializationInstantiator(Class<T> type) throws NoSuchMethodException, SecurityException {
        this.type = type;
        try {
            Method m = ObjectStreamClass.class.getMethod("lookupAny", Class.class);
            try {
                this.objectStreamClass = (ObjectStreamClass) m.invoke(null, type);
            } catch (IllegalAccessException e) {
                throw new ObjenesisException(e);
            } catch (InvocationTargetException e2) {
                throw new ObjenesisException(e2);
            }
        } catch (NoSuchMethodException e3) {
            throw new ObjenesisException(e3);
        }
    }

    @Override // org.springframework.objenesis.instantiator.ObjectInstantiator
    public T newInstance() {
        try {
            return this.type.cast(this.newInstanceMethod.invoke(this.objectStreamClass, this.type));
        } catch (IllegalAccessException e) {
            throw new ObjenesisException(e);
        } catch (IllegalArgumentException e2) {
            throw new ObjenesisException(e2);
        } catch (InvocationTargetException e3) {
            throw new ObjenesisException(e3);
        }
    }

    private static Method getNewInstanceMethod() throws NoSuchMethodException, SecurityException {
        try {
            Method newInstanceMethod = ObjectStreamClass.class.getDeclaredMethod("newInstance", Class.class);
            newInstanceMethod.setAccessible(true);
            return newInstanceMethod;
        } catch (NoSuchMethodException e) {
            throw new ObjenesisException(e);
        } catch (RuntimeException e2) {
            throw new ObjenesisException(e2);
        }
    }
}
