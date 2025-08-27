package org.apache.commons.lang;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/SerializationUtils.class */
public class SerializationUtils {
    public static Object clone(Serializable object) {
        return deserialize(serialize(object));
    }

    public static void serialize(Serializable obj, OutputStream outputStream) throws IOException {
        if (outputStream == null) {
            throw new IllegalArgumentException("The OutputStream must not be null");
        }
        ObjectOutputStream out = null;
        try {
            try {
                out = new ObjectOutputStream(outputStream);
                out.writeObject(obj);
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                    }
                }
            } catch (IOException ex) {
                throw new SerializationException(ex);
            }
        } catch (Throwable th) {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e2) {
                    throw th;
                }
            }
            throw th;
        }
    }

    public static byte[] serialize(Serializable obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        serialize(obj, baos);
        return baos.toByteArray();
    }

    public static Object deserialize(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("The InputStream must not be null");
        }
        ObjectInputStream in = null;
        try {
            try {
                try {
                    in = new ObjectInputStream(inputStream);
                    Object object = in.readObject();
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                        }
                    }
                    return object;
                } catch (ClassNotFoundException ex) {
                    throw new SerializationException(ex);
                }
            } catch (IOException ex2) {
                throw new SerializationException(ex2);
            }
        } catch (Throwable th) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e2) {
                    throw th;
                }
            }
            throw th;
        }
    }

    public static Object deserialize(byte[] objectData) {
        if (objectData == null) {
            throw new IllegalArgumentException("The byte[] must not be null");
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
        return deserialize(bais);
    }
}
