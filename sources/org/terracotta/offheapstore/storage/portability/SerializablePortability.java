package org.terracotta.offheapstore.storage.portability;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.terracotta.offheapstore.util.ByteBufferInputStream;
import org.terracotta.offheapstore.util.FindbugsSuppressWarnings;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/portability/SerializablePortability.class */
public class SerializablePortability implements Portability<Serializable> {
    protected int nextStreamIndex;
    protected final ConcurrentMap<Object, Object> lookup;
    private final ClassLoader loader;

    public SerializablePortability() {
        this(null);
    }

    public SerializablePortability(ClassLoader loader) {
        this.nextStreamIndex = 0;
        this.lookup = new ConcurrentHashMap();
        this.loader = loader;
    }

    @Override // org.terracotta.offheapstore.storage.portability.Portability
    public ByteBuffer encode(Serializable object) throws IOException {
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
            throw new AssertionError(e);
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.terracotta.offheapstore.storage.portability.Portability
    public Serializable decode(ByteBuffer buffer) throws IOException {
        try {
            ObjectInputStream oin = getObjectInputStream(new ByteBufferInputStream(buffer));
            try {
                Serializable serializable = (Serializable) oin.readObject();
                oin.close();
                return serializable;
            } catch (Throwable th) {
                oin.close();
                throw th;
            }
        } catch (IOException e) {
            throw new AssertionError(e);
        } catch (ClassNotFoundException e2) {
            throw new AssertionError(e2);
        }
    }

    public ObjectOutputStream getObjectOutputStream(OutputStream out) throws IOException {
        return new OOS(out);
    }

    public ObjectInputStream getObjectInputStream(InputStream input) throws IOException {
        return new OIS(input, this.loader);
    }

    @Override // org.terracotta.offheapstore.storage.portability.Portability
    public boolean equals(Object value, ByteBuffer readBuffer) {
        return value.equals(decode(readBuffer));
    }

    @FindbugsSuppressWarnings({"JLM_JSR166_UTILCONCURRENT_MONITORENTER"})
    protected int getOrAddMapping(ObjectStreamClass desc) throws IOException {
        SerializableDataKey probe = new SerializableDataKey(desc, false);
        Integer rep = (Integer) this.lookup.get(probe);
        if (rep == null) {
            synchronized (this.lookup) {
                rep = (Integer) this.lookup.get(probe);
                if (rep == null) {
                    ObjectStreamClass disconnected = disconnect(desc);
                    SerializableDataKey key = new SerializableDataKey(disconnected, true);
                    int i = this.nextStreamIndex;
                    this.nextStreamIndex = i + 1;
                    rep = Integer.valueOf(i);
                    ObjectStreamClass existingOsc = (ObjectStreamClass) this.lookup.putIfAbsent(rep, disconnected);
                    if (existingOsc != null) {
                        throw new AssertionError("Existing mapping for this index detected : " + rep + " => " + existingOsc.getName());
                    }
                    Integer existingRep = (Integer) this.lookup.putIfAbsent(key, rep);
                    if (existingRep != null) {
                        throw new AssertionError("Existing mapping to this type detected : " + existingRep + " => " + disconnected.getName());
                    }
                    addedMapping(rep, disconnected);
                }
            }
        }
        return rep.intValue();
    }

    protected void addedMapping(Integer rep, ObjectStreamClass disconnected) {
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/portability/SerializablePortability$OOS.class */
    class OOS extends ObjectOutputStream {
        public OOS(OutputStream out) throws IOException {
            super(out);
        }

        @Override // java.io.ObjectOutputStream
        protected void writeClassDescriptor(ObjectStreamClass desc) throws IOException {
            writeInt(SerializablePortability.this.getOrAddMapping(desc));
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/portability/SerializablePortability$OIS.class */
    class OIS extends ObjectInputStream {
        private final ClassLoader loader;

        public OIS(InputStream in, ClassLoader loader) throws IOException {
            super(in);
            this.loader = loader;
        }

        @Override // java.io.ObjectInputStream
        protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
            return (ObjectStreamClass) SerializablePortability.this.lookup.get(Integer.valueOf(readInt()));
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

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/portability/SerializablePortability$SerializableDataKey.class */
    protected static class SerializableDataKey {
        private final ObjectStreamClass osc;
        private final int hashCode;
        private transient WeakReference<Class<?>> klazz;

        public SerializableDataKey(ObjectStreamClass desc, boolean store) throws IOException {
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
                return SerializablePortability.equals(this, (SerializableDataKey) o);
            }
            return false;
        }

        public int hashCode() {
            return this.hashCode;
        }

        public Class<?> forClass() {
            if (this.klazz == null) {
                return null;
            }
            return this.klazz.get();
        }

        public void setClass(Class<?> clazz) {
            this.klazz = new WeakReference<>(clazz);
        }

        public ObjectStreamClass getObjectStreamClass() {
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

    protected static ObjectStreamClass disconnect(ObjectStreamClass desc) {
        try {
            ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(getSerializedForm(desc))) { // from class: org.terracotta.offheapstore.storage.portability.SerializablePortability.1
                @Override // java.io.ObjectInputStream
                protected Class<?> resolveClass(ObjectStreamClass osc) throws IOException, ClassNotFoundException {
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
