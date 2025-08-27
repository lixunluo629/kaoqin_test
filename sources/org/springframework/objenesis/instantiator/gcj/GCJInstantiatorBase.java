package org.springframework.objenesis.instantiator.gcj;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.objenesis.instantiator.ObjectInstantiator;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/objenesis/instantiator/gcj/GCJInstantiatorBase.class */
public abstract class GCJInstantiatorBase<T> implements ObjectInstantiator<T> {
    static Method newObjectMethod = null;
    static ObjectInputStream dummyStream;
    protected final Class<T> type;

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/objenesis/instantiator/gcj/GCJInstantiatorBase$DummyStream.class */
    private static class DummyStream extends ObjectInputStream {
    }

    @Override // org.springframework.objenesis.instantiator.ObjectInstantiator
    public abstract T newInstance();

    private static void initialize() {
        if (newObjectMethod == null) {
            try {
                newObjectMethod = ObjectInputStream.class.getDeclaredMethod("newObject", Class.class, Class.class);
                newObjectMethod.setAccessible(true);
                dummyStream = new DummyStream();
            } catch (IOException e) {
                throw new ObjenesisException(e);
            } catch (NoSuchMethodException e2) {
                throw new ObjenesisException(e2);
            } catch (RuntimeException e3) {
                throw new ObjenesisException(e3);
            }
        }
    }

    public GCJInstantiatorBase(Class<T> type) {
        this.type = type;
        initialize();
    }
}
