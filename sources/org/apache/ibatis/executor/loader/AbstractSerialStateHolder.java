package org.apache.ibatis.executor.loader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.StreamCorruptedException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.executor.loader.ResultLoaderMap;
import org.apache.ibatis.reflection.factory.ObjectFactory;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/loader/AbstractSerialStateHolder.class */
public abstract class AbstractSerialStateHolder implements Externalizable {
    private static final long serialVersionUID = 8940388717901644661L;
    private static final ThreadLocal<ObjectOutputStream> stream = new ThreadLocal<>();
    private byte[] userBeanBytes;
    private Object userBean;
    private Map<String, ResultLoaderMap.LoadPair> unloadedProperties;
    private ObjectFactory objectFactory;
    private Class<?>[] constructorArgTypes;
    private Object[] constructorArgs;

    protected abstract Object createDeserializationProxy(Object obj, Map<String, ResultLoaderMap.LoadPair> map, ObjectFactory objectFactory, List<Class<?>> list, List<Object> list2);

    public AbstractSerialStateHolder() {
        this.userBeanBytes = new byte[0];
    }

    public AbstractSerialStateHolder(Object userBean, Map<String, ResultLoaderMap.LoadPair> unloadedProperties, ObjectFactory objectFactory, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
        this.userBeanBytes = new byte[0];
        this.userBean = userBean;
        this.unloadedProperties = new HashMap(unloadedProperties);
        this.objectFactory = objectFactory;
        this.constructorArgTypes = (Class[]) constructorArgTypes.toArray(new Class[constructorArgTypes.size()]);
        this.constructorArgs = constructorArgs.toArray(new Object[constructorArgs.size()]);
    }

    @Override // java.io.Externalizable
    public final void writeExternal(ObjectOutput out) throws IOException {
        boolean firstRound = false;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream os = stream.get();
        if (os == null) {
            os = new ObjectOutputStream(baos);
            firstRound = true;
            stream.set(os);
        }
        os.writeObject(this.userBean);
        os.writeObject(this.unloadedProperties);
        os.writeObject(this.objectFactory);
        os.writeObject(this.constructorArgTypes);
        os.writeObject(this.constructorArgs);
        byte[] bytes = baos.toByteArray();
        out.writeObject(bytes);
        if (firstRound) {
            stream.remove();
        }
    }

    @Override // java.io.Externalizable
    public final void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
        Object data = in.readObject();
        if (data.getClass().isArray()) {
            this.userBeanBytes = (byte[]) data;
        } else {
            this.userBean = data;
        }
    }

    protected final Object readResolve() throws ObjectStreamException {
        if (this.userBean != null && this.userBeanBytes.length == 0) {
            return this.userBean;
        }
        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(this.userBeanBytes));
            this.userBean = in.readObject();
            this.unloadedProperties = (Map) in.readObject();
            this.objectFactory = (ObjectFactory) in.readObject();
            this.constructorArgTypes = (Class[]) in.readObject();
            this.constructorArgs = (Object[]) in.readObject();
            Map<String, ResultLoaderMap.LoadPair> arrayProps = new HashMap<>(this.unloadedProperties);
            List<Class<?>> arrayTypes = Arrays.asList(this.constructorArgTypes);
            List<Object> arrayValues = Arrays.asList(this.constructorArgs);
            return createDeserializationProxy(this.userBean, arrayProps, this.objectFactory, arrayTypes, arrayValues);
        } catch (IOException ex) {
            throw ((ObjectStreamException) new StreamCorruptedException().initCause(ex));
        } catch (ClassNotFoundException ex2) {
            throw ((ObjectStreamException) new InvalidClassException(ex2.getLocalizedMessage()).initCause(ex2));
        }
    }
}
