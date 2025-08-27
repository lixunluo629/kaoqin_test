package org.ehcache.impl.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.ehcache.impl.internal.util.ByteBufferInputStream;
import org.ehcache.spi.persistence.StateHolder;
import org.ehcache.spi.persistence.StateRepository;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.serialization.SerializerException;
import org.ehcache.spi.serialization.StatefulSerializer;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/serialization/CompactJavaSerializer.class */
public class CompactJavaSerializer<T> implements StatefulSerializer<T> {
    private volatile StateHolder<Integer, ObjectStreamClass> readLookup;
    private final ConcurrentMap<Integer, ObjectStreamClass> readLookupLocalCache = new ConcurrentHashMap();
    private final ConcurrentMap<SerializableDataKey, Integer> writeLookup = new ConcurrentHashMap();
    private final Lock lock = new ReentrantLock();
    private int nextStreamIndex = 0;
    private final transient ClassLoader loader;

    public CompactJavaSerializer(ClassLoader loader) {
        this.loader = loader;
    }

    public static <T> Class<? extends Serializer<T>> asTypedSerializer() {
        return CompactJavaSerializer.class;
    }

    @Override // org.ehcache.spi.serialization.StatefulSerializer
    public void init(StateRepository stateRepository) {
        this.readLookup = stateRepository.getPersistentStateHolder("CompactJavaSerializer-ObjectStreamClassIndex", Integer.class, ObjectStreamClass.class);
        loadMappingsInWriteContext(this.readLookup.entrySet(), true);
    }

    @Override // org.ehcache.spi.serialization.Serializer
    public ByteBuffer serialize(T object) throws IOException, SerializerException {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ObjectOutputStream oout = getObjectOutputStream(bout);
            try {
                oout.writeObject(object);
                oout.close();
                return ByteBuffer.wrap(bout.toByteArray());
            } catch (Throwable th) {
                oout.close();
                throw th;
            }
        } catch (IOException e) {
            throw new SerializerException(e);
        }
    }

    @Override // org.ehcache.spi.serialization.Serializer
    public T read(ByteBuffer byteBuffer) throws IOException, ClassNotFoundException, SerializerException {
        try {
            ObjectInputStream objectInputStream = getObjectInputStream(new ByteBufferInputStream(byteBuffer));
            try {
                T t = (T) objectInputStream.readObject();
                objectInputStream.close();
                return t;
            } catch (Throwable th) {
                objectInputStream.close();
                throw th;
            }
        } catch (IOException e) {
            throw new SerializerException(e);
        }
    }

    private ObjectOutputStream getObjectOutputStream(OutputStream out) throws IOException {
        return new OOS(out);
    }

    private ObjectInputStream getObjectInputStream(InputStream input) throws IOException {
        return new OIS(input, this.loader);
    }

    @Override // org.ehcache.spi.serialization.Serializer
    public boolean equals(T object, ByteBuffer binary) throws ClassNotFoundException, SerializerException {
        return object.equals(read(binary));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getOrAddMapping(ObjectStreamClass desc) throws IOException {
        SerializableDataKey probe = new SerializableDataKey(desc, false);
        Integer rep = this.writeLookup.get(probe);
        if (rep != null) {
            return rep.intValue();
        }
        this.lock.lock();
        try {
            int iAddMappingUnderLock = addMappingUnderLock(desc, probe);
            this.lock.unlock();
            return iAddMappingUnderLock;
        } catch (Throwable th) {
            this.lock.unlock();
            throw th;
        }
    }

    private int addMappingUnderLock(ObjectStreamClass desc, SerializableDataKey probe) throws IOException {
        ObjectStreamClass disconnected = disconnect(desc);
        SerializableDataKey key = new SerializableDataKey(disconnected, true);
        while (true) {
            Integer rep = this.writeLookup.get(probe);
            if (rep != null) {
                return rep.intValue();
            }
            int i = this.nextStreamIndex;
            this.nextStreamIndex = i + 1;
            Integer rep2 = Integer.valueOf(i);
            ObjectStreamClass existingOsc = this.readLookup.putIfAbsent(rep2, disconnected);
            if (existingOsc == null) {
                this.writeLookup.put(key, rep2);
                this.readLookupLocalCache.put(rep2, disconnected);
                return rep2.intValue();
            }
            ObjectStreamClass discOsc = disconnect(existingOsc);
            this.writeLookup.put(new SerializableDataKey(discOsc, true), rep2);
            this.readLookupLocalCache.put(rep2, discOsc);
        }
    }

    private void loadMappingsInWriteContext(Set<Map.Entry<Integer, ObjectStreamClass>> entries, boolean throwOnFailedPutIfAbsent) {
        for (Map.Entry<Integer, ObjectStreamClass> entry : entries) {
            Integer index = entry.getKey();
            ObjectStreamClass discOsc = disconnect(entry.getValue());
            this.readLookupLocalCache.put(index, discOsc);
            if (this.writeLookup.putIfAbsent(new SerializableDataKey(discOsc, true), index) != null && throwOnFailedPutIfAbsent) {
                throw new AssertionError("Corrupted data " + this.readLookup);
            }
            if (this.nextStreamIndex < index.intValue() + 1) {
                this.nextStreamIndex = index.intValue() + 1;
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/serialization/CompactJavaSerializer$OOS.class */
    class OOS extends ObjectOutputStream {
        OOS(OutputStream out) throws IOException {
            super(out);
        }

        @Override // java.io.ObjectOutputStream
        protected void writeClassDescriptor(ObjectStreamClass desc) throws IOException {
            writeInt(CompactJavaSerializer.this.getOrAddMapping(desc));
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/serialization/CompactJavaSerializer$OIS.class */
    class OIS extends ObjectInputStream {
        private final ClassLoader loader;

        OIS(InputStream in, ClassLoader loader) throws IOException {
            super(in);
            this.loader = loader;
        }

        @Override // java.io.ObjectInputStream
        protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
            int key = readInt();
            ObjectStreamClass objectStreamClass = (ObjectStreamClass) CompactJavaSerializer.this.readLookupLocalCache.get(Integer.valueOf(key));
            if (objectStreamClass == null) {
                ObjectStreamClass objectStreamClass2 = (ObjectStreamClass) CompactJavaSerializer.this.readLookup.get(Integer.valueOf(key));
                ObjectStreamClass discOsc = CompactJavaSerializer.disconnect(objectStreamClass2);
                CompactJavaSerializer.this.readLookupLocalCache.put(Integer.valueOf(key), discOsc);
                CompactJavaSerializer.this.writeLookup.putIfAbsent(new SerializableDataKey(discOsc, true), Integer.valueOf(key));
                return objectStreamClass2;
            }
            return objectStreamClass;
        }

        @Override // java.io.ObjectInputStream
        protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
            try {
                ClassLoader cl = this.loader == null ? Thread.currentThread().getContextClassLoader() : this.loader;
                if (cl == null) {
                    return super.resolveClass(desc);
                }
                try {
                    return Class.forName(desc.getName(), false, cl);
                } catch (ClassNotFoundException e) {
                    return super.resolveClass(desc);
                }
            } catch (SecurityException e2) {
                return super.resolveClass(desc);
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/serialization/CompactJavaSerializer$SerializableDataKey.class */
    private static class SerializableDataKey {
        private final ObjectStreamClass osc;
        private final int hashCode;
        private transient WeakReference<Class<?>> klazz;

        SerializableDataKey(ObjectStreamClass desc, boolean store) {
            Class<?> forClass = desc.forClass();
            if (forClass != null) {
                if (store) {
                    throw new AssertionError("Must not store ObjectStreamClass instances with strong references to classes");
                }
                if (ObjectStreamClass.lookup(forClass) == desc) {
                    this.klazz = new WeakReference<>(forClass);
                }
            }
            this.hashCode = ((3 * desc.getName().hashCode()) ^ (7 * ((int) (desc.getSerialVersionUID() >>> 32)))) ^ (11 * ((int) desc.getSerialVersionUID()));
            this.osc = desc;
        }

        public boolean equals(Object o) {
            if (o instanceof SerializableDataKey) {
                return CompactJavaSerializer.equals(this, (SerializableDataKey) o);
            }
            return false;
        }

        public int hashCode() {
            return this.hashCode;
        }

        Class<?> forClass() {
            if (this.klazz == null) {
                return null;
            }
            return this.klazz.get();
        }

        public void setClass(Class<?> clazz) {
            this.klazz = new WeakReference<>(clazz);
        }

        ObjectStreamClass getObjectStreamClass() {
            return this.osc;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean equals(SerializableDataKey k1, SerializableDataKey k2) {
        Class<?> k1Clazz = k1.forClass();
        Class<?> k2Clazz = k2.forClass();
        if (k1Clazz != null && k2Clazz != null) {
            return k1Clazz == k2Clazz;
        }
        if (equals(k1.getObjectStreamClass(), k2.getObjectStreamClass())) {
            if (k1Clazz != null) {
                k2.setClass(k1Clazz);
                return true;
            }
            if (k2Clazz != null) {
                k1.setClass(k2Clazz);
                return true;
            }
            return true;
        }
        return false;
    }

    private static boolean equals(ObjectStreamClass osc1, ObjectStreamClass osc2) {
        if (osc1 == osc2) {
            return true;
        }
        if (osc1.getName().equals(osc2.getName()) && osc1.getSerialVersionUID() == osc2.getSerialVersionUID() && osc1.getFields().length == osc2.getFields().length) {
            try {
                return Arrays.equals(getSerializedForm(osc1), getSerializedForm(osc2));
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ObjectStreamClass disconnect(ObjectStreamClass desc) {
        try {
            ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(getSerializedForm(desc))) { // from class: org.ehcache.impl.serialization.CompactJavaSerializer.1
                @Override // java.io.ObjectInputStream
                protected Class<?> resolveClass(ObjectStreamClass osc) {
                    return null;
                }
            };
            return (ObjectStreamClass) oin.readObject();
        } catch (IOException e) {
            throw new AssertionError(e);
        } catch (ClassNotFoundException e2) {
            throw new AssertionError(e2);
        }
    }

    private static byte[] getSerializedForm(ObjectStreamClass desc) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oout = new ObjectOutputStream(bout);
            try {
                oout.writeObject(desc);
                oout.close();
                return bout.toByteArray();
            } catch (Throwable th) {
                oout.close();
                throw th;
            }
        } finally {
            bout.close();
        }
    }
}
