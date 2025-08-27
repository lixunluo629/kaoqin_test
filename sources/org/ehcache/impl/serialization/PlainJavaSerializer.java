package org.ehcache.impl.serialization;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Proxy;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import org.apache.xmlbeans.XmlErrorCodes;
import org.ehcache.impl.internal.util.ByteBufferInputStream;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.serialization.SerializerException;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/serialization/PlainJavaSerializer.class */
public class PlainJavaSerializer<T> implements Serializer<T> {
    private final ClassLoader classLoader;

    public PlainJavaSerializer(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override // org.ehcache.spi.serialization.Serializer
    public ByteBuffer serialize(T object) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            try {
                ObjectOutputStream oout = new ObjectOutputStream(bout);
                oout.writeObject(object);
                try {
                    bout.close();
                    return ByteBuffer.wrap(bout.toByteArray());
                } catch (IOException e) {
                    throw new AssertionError(e);
                }
            } catch (IOException e2) {
                throw new SerializerException(e2);
            }
        } catch (Throwable th) {
            try {
                bout.close();
                throw th;
            } catch (IOException e3) {
                throw new AssertionError(e3);
            }
        }
    }

    @Override // org.ehcache.spi.serialization.Serializer
    public T read(ByteBuffer byteBuffer) throws ClassNotFoundException, SerializerException {
        ByteBufferInputStream byteBufferInputStream = new ByteBufferInputStream(byteBuffer);
        try {
            try {
                OIS ois = new OIS(byteBufferInputStream, this.classLoader);
                try {
                    T t = (T) ois.readObject();
                    ois.close();
                    try {
                        byteBufferInputStream.close();
                        return t;
                    } catch (IOException e) {
                        throw new AssertionError(e);
                    }
                } catch (Throwable th) {
                    ois.close();
                    throw th;
                }
            } catch (IOException e2) {
                throw new SerializerException(e2);
            }
        } catch (Throwable th2) {
            try {
                byteBufferInputStream.close();
                throw th2;
            } catch (IOException e3) {
                throw new AssertionError(e3);
            }
        }
    }

    @Override // org.ehcache.spi.serialization.Serializer
    public boolean equals(T object, ByteBuffer binary) throws ClassNotFoundException, SerializerException {
        return object.equals(read(binary));
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/serialization/PlainJavaSerializer$OIS.class */
    private static class OIS extends ObjectInputStream {
        private final ClassLoader classLoader;
        private static final Map<String, Class<?>> primitiveClasses = new HashMap();

        public OIS(InputStream in, ClassLoader classLoader) throws IOException {
            super(in);
            this.classLoader = classLoader;
        }

        @Override // java.io.ObjectInputStream
        protected Class<?> resolveClass(ObjectStreamClass desc) throws ClassNotFoundException, IOException {
            try {
                return Class.forName(desc.getName(), false, this.classLoader);
            } catch (ClassNotFoundException cnfe) {
                Class<?> primitive = primitiveClasses.get(desc.getName());
                if (primitive != null) {
                    return primitive;
                }
                throw cnfe;
            }
        }

        @Override // java.io.ObjectInputStream
        protected Class<?> resolveProxyClass(String[] interfaces) throws IOException, ClassNotFoundException {
            Class<?>[] interfaceClasses = new Class[interfaces.length];
            for (int i = 0; i < interfaces.length; i++) {
                interfaceClasses[i] = Class.forName(interfaces[i], false, this.classLoader);
            }
            return Proxy.getProxyClass(this.classLoader, interfaceClasses);
        }

        static {
            primitiveClasses.put("boolean", Boolean.TYPE);
            primitiveClasses.put("byte", Byte.TYPE);
            primitiveClasses.put("char", Character.TYPE);
            primitiveClasses.put(XmlErrorCodes.DOUBLE, Double.TYPE);
            primitiveClasses.put(XmlErrorCodes.FLOAT, Float.TYPE);
            primitiveClasses.put(XmlErrorCodes.INT, Integer.TYPE);
            primitiveClasses.put(XmlErrorCodes.LONG, Long.TYPE);
            primitiveClasses.put("short", Short.TYPE);
            primitiveClasses.put("void", Void.TYPE);
        }
    }
}
