package org.apache.ibatis.cache.decorators;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.util.concurrent.locks.ReadWriteLock;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheException;
import org.apache.ibatis.io.Resources;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/cache/decorators/SerializedCache.class */
public class SerializedCache implements Cache {
    private final Cache delegate;

    public SerializedCache(Cache delegate) {
        this.delegate = delegate;
    }

    @Override // org.apache.ibatis.cache.Cache
    public String getId() {
        return this.delegate.getId();
    }

    @Override // org.apache.ibatis.cache.Cache
    public int getSize() {
        return this.delegate.getSize();
    }

    @Override // org.apache.ibatis.cache.Cache
    public void putObject(Object key, Object object) {
        if (object == null || (object instanceof Serializable)) {
            this.delegate.putObject(key, serialize((Serializable) object));
            return;
        }
        throw new CacheException("SharedCache failed to make a copy of a non-serializable object: " + object);
    }

    @Override // org.apache.ibatis.cache.Cache
    public Object getObject(Object key) {
        Object object = this.delegate.getObject(key);
        if (object == null) {
            return null;
        }
        return deserialize((byte[]) object);
    }

    @Override // org.apache.ibatis.cache.Cache
    public Object removeObject(Object key) {
        return this.delegate.removeObject(key);
    }

    @Override // org.apache.ibatis.cache.Cache
    public void clear() {
        this.delegate.clear();
    }

    @Override // org.apache.ibatis.cache.Cache
    public ReadWriteLock getReadWriteLock() {
        return null;
    }

    public int hashCode() {
        return this.delegate.hashCode();
    }

    public boolean equals(Object obj) {
        return this.delegate.equals(obj);
    }

    private byte[] serialize(Serializable value) throws IOException {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(value);
            oos.flush();
            oos.close();
            return bos.toByteArray();
        } catch (Exception e) {
            throw new CacheException("Error serializing object.  Cause: " + e, e);
        }
    }

    private Serializable deserialize(byte[] value) throws IOException {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(value);
            ObjectInputStream ois = new CustomObjectInputStream(bis);
            Serializable result = (Serializable) ois.readObject();
            ois.close();
            return result;
        } catch (Exception e) {
            throw new CacheException("Error deserializing object.  Cause: " + e, e);
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/cache/decorators/SerializedCache$CustomObjectInputStream.class */
    public static class CustomObjectInputStream extends ObjectInputStream {
        public CustomObjectInputStream(InputStream in) throws IOException {
            super(in);
        }

        @Override // java.io.ObjectInputStream
        protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
            return Resources.classForName(desc.getName());
        }
    }
}
