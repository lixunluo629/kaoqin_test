package org.aspectj.weaver;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/PersistenceSupport.class */
public class PersistenceSupport {
    public static void write(CompressingDataOutputStream stream, ISourceContext sourceContext) throws IOException {
        throw new IllegalStateException();
    }

    public static void write(CompressingDataOutputStream stream, Serializable serializableObject) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(stream);
        oos.writeObject(serializableObject);
        oos.flush();
    }
}
