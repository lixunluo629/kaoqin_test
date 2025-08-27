package org.terracotta.offheapstore.disk.persistent;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/disk/persistent/Persistent.class */
public interface Persistent {
    void flush() throws IOException;

    void close() throws IOException;

    void persist(ObjectOutput objectOutput) throws IOException;

    void bootstrap(ObjectInput objectInput) throws IOException;
}
