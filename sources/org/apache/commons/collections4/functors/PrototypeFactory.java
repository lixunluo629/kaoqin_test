package org.apache.commons.collections4.functors;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.FunctorException;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/functors/PrototypeFactory.class */
public class PrototypeFactory {
    public static <T> Factory<T> prototypeFactory(T prototype) throws NoSuchMethodException, SecurityException {
        if (prototype == null) {
            return ConstantFactory.constantFactory(null);
        }
        try {
            Method method = prototype.getClass().getMethod("clone", (Class[]) null);
            return new PrototypeCloneFactory(prototype, method);
        } catch (NoSuchMethodException e) {
            try {
                prototype.getClass().getConstructor(prototype.getClass());
                return new InstantiateFactory(prototype.getClass(), new Class[]{prototype.getClass()}, new Object[]{prototype});
            } catch (NoSuchMethodException e2) {
                if (prototype instanceof Serializable) {
                    return new PrototypeSerializationFactory((Serializable) prototype);
                }
                throw new IllegalArgumentException("The prototype must be cloneable via a public clone method");
            }
        }
    }

    private PrototypeFactory() {
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/functors/PrototypeFactory$PrototypeCloneFactory.class */
    static class PrototypeCloneFactory<T> implements Factory<T> {
        private final T iPrototype;
        private transient Method iCloneMethod;

        private PrototypeCloneFactory(T prototype, Method method) {
            this.iPrototype = prototype;
            this.iCloneMethod = method;
        }

        private void findCloneMethod() {
            try {
                this.iCloneMethod = this.iPrototype.getClass().getMethod("clone", (Class[]) null);
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException("PrototypeCloneFactory: The clone method must exist and be public ");
            }
        }

        @Override // org.apache.commons.collections4.Factory
        public T create() {
            if (this.iCloneMethod == null) {
                findCloneMethod();
            }
            try {
                return (T) this.iCloneMethod.invoke(this.iPrototype, (Object[]) null);
            } catch (IllegalAccessException e) {
                throw new FunctorException("PrototypeCloneFactory: Clone method must be public", e);
            } catch (InvocationTargetException e2) {
                throw new FunctorException("PrototypeCloneFactory: Clone method threw an exception", e2);
            }
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/functors/PrototypeFactory$PrototypeSerializationFactory.class */
    static class PrototypeSerializationFactory<T extends Serializable> implements Factory<T> {
        private final T iPrototype;

        private PrototypeSerializationFactory(T prototype) {
            this.iPrototype = prototype;
        }

        @Override // org.apache.commons.collections4.Factory
        public T create() throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
            ByteArrayInputStream bais = null;
            try {
                try {
                    ObjectOutputStream out = new ObjectOutputStream(baos);
                    out.writeObject(this.iPrototype);
                    bais = new ByteArrayInputStream(baos.toByteArray());
                    ObjectInputStream in = new ObjectInputStream(bais);
                    T t = (T) in.readObject();
                    if (bais != null) {
                        try {
                            bais.close();
                        } catch (IOException e) {
                        }
                    }
                    try {
                        baos.close();
                    } catch (IOException e2) {
                    }
                    return t;
                } catch (IOException ex) {
                    throw new FunctorException(ex);
                } catch (ClassNotFoundException ex2) {
                    throw new FunctorException(ex2);
                }
            } catch (Throwable th) {
                if (bais != null) {
                    try {
                        bais.close();
                    } catch (IOException e3) {
                        baos.close();
                        throw th;
                    }
                }
                try {
                    baos.close();
                } catch (IOException e4) {
                }
                throw th;
            }
        }
    }
}
