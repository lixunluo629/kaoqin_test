package com.itextpdf.io.source;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.BufferUnderflowException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/source/ByteBufferRandomAccessSource.class */
class ByteBufferRandomAccessSource implements IRandomAccessSource, Serializable {
    private static final long serialVersionUID = -1477190062876186034L;
    private transient java.nio.ByteBuffer byteBuffer;
    private byte[] bufferMirror;
    public static final boolean UNMAP_SUPPORTED;
    private static final BufferCleaner CLEANER;

    public ByteBufferRandomAccessSource(java.nio.ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public int get(long position) throws IOException {
        if (position > 2147483647L) {
            throw new IllegalArgumentException("Position must be less than Integer.MAX_VALUE");
        }
        try {
            if (position >= this.byteBuffer.limit()) {
                return -1;
            }
            byte b = this.byteBuffer.get((int) position);
            return b & 255;
        } catch (BufferUnderflowException e) {
            return -1;
        }
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public int get(long position, byte[] bytes, int off, int len) throws IOException {
        if (position > 2147483647L) {
            throw new IllegalArgumentException("Position must be less than Integer.MAX_VALUE");
        }
        if (position >= this.byteBuffer.limit()) {
            return -1;
        }
        this.byteBuffer.position((int) position);
        int bytesFromThisBuffer = Math.min(len, this.byteBuffer.remaining());
        this.byteBuffer.get(bytes, off, bytesFromThisBuffer);
        return bytesFromThisBuffer;
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public long length() {
        return this.byteBuffer.limit();
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public void close() throws IOException {
        clean(this.byteBuffer);
    }

    static {
        Object hack = AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: com.itextpdf.io.source.ByteBufferRandomAccessSource.1
            @Override // java.security.PrivilegedAction
            public Object run() {
                return ByteBufferRandomAccessSource.unmapHackImpl();
            }
        });
        if (hack instanceof BufferCleaner) {
            CLEANER = (BufferCleaner) hack;
            UNMAP_SUPPORTED = true;
        } else {
            CLEANER = null;
            UNMAP_SUPPORTED = false;
        }
    }

    private static boolean clean(final java.nio.ByteBuffer buffer) {
        if (buffer == null || !buffer.isDirect()) {
            return false;
        }
        Boolean b = (Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: com.itextpdf.io.source.ByteBufferRandomAccessSource.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Boolean run() throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
                Boolean success = Boolean.FALSE;
                try {
                    if (ByteBufferRandomAccessSource.UNMAP_SUPPORTED) {
                        ByteBufferRandomAccessSource.CLEANER.freeBuffer(buffer.toString(), buffer);
                    } else {
                        Method getCleanerMethod = buffer.getClass().getMethod("cleaner", (Class[]) null);
                        getCleanerMethod.setAccessible(true);
                        Object cleaner = getCleanerMethod.invoke(buffer, (Object[]) null);
                        Method clean = cleaner.getClass().getMethod("clean", (Class[]) null);
                        clean.invoke(cleaner, (Object[]) null);
                    }
                    success = Boolean.TRUE;
                } catch (Exception e) {
                    Logger logger = LoggerFactory.getLogger((Class<?>) ByteBufferRandomAccessSource.class);
                    logger.debug(e.getMessage());
                }
                return success;
            }
        });
        return b.booleanValue();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        if (this.byteBuffer != null && this.byteBuffer.hasArray()) {
            throw new NotSerializableException(this.byteBuffer.getClass().toString());
        }
        if (this.byteBuffer != null) {
            this.bufferMirror = this.byteBuffer.array();
        }
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        if (this.bufferMirror != null) {
            this.byteBuffer = java.nio.ByteBuffer.wrap(this.bufferMirror);
            this.bufferMirror = null;
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/source/ByteBufferRandomAccessSource$BufferCleaner.class */
    private static class BufferCleaner {
        Class<?> unmappableBufferClass;
        final Method method;
        final Object theUnsafe;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ByteBufferRandomAccessSource.class.desiredAssertionStatus();
        }

        BufferCleaner(Class<?> unmappableBufferClass, Method method, Object theUnsafe) {
            this.unmappableBufferClass = unmappableBufferClass;
            this.method = method;
            this.theUnsafe = theUnsafe;
        }

        void freeBuffer(String resourceDescription, final java.nio.ByteBuffer buffer) throws IOException {
            if (!$assertionsDisabled && !Objects.equals(Void.TYPE, this.method.getReturnType())) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this.method.getParameterTypes().length != 1) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && !Objects.equals(java.nio.ByteBuffer.class, this.method.getParameterTypes()[0])) {
                throw new AssertionError();
            }
            if (!buffer.isDirect()) {
                throw new IllegalArgumentException("unmapping only works with direct buffers");
            }
            if (!this.unmappableBufferClass.isInstance(buffer)) {
                throw new IllegalArgumentException("buffer is not an instance of " + this.unmappableBufferClass.getName());
            }
            Throwable error = (Throwable) AccessController.doPrivileged(new PrivilegedAction<Throwable>() { // from class: com.itextpdf.io.source.ByteBufferRandomAccessSource.BufferCleaner.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public Throwable run() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                    try {
                        BufferCleaner.this.method.invoke(BufferCleaner.this.theUnsafe, buffer);
                        return null;
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        return e;
                    }
                }
            });
            if (error != null) {
                throw new IOException("Unable to unmap the mapped buffer: " + resourceDescription, error);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Object unmapHackImpl() throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, ClassNotFoundException, SecurityException, IllegalArgumentException {
        try {
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            Method method = unsafeClass.getDeclaredMethod("invokeCleaner", java.nio.ByteBuffer.class);
            Field f = unsafeClass.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            Object theUnsafe = f.get(null);
            return new BufferCleaner(java.nio.ByteBuffer.class, method, theUnsafe);
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
