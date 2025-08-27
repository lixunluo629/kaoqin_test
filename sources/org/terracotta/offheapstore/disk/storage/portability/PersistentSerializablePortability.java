package org.terracotta.offheapstore.disk.storage.portability;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.util.Map;
import org.terracotta.offheapstore.disk.persistent.PersistentPortability;
import org.terracotta.offheapstore.storage.portability.SerializablePortability;
import org.terracotta.offheapstore.util.FindbugsSuppressWarnings;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/disk/storage/portability/PersistentSerializablePortability.class */
public class PersistentSerializablePortability extends SerializablePortability implements PersistentPortability<Serializable> {
    private static final int MAGIC = -17973521;

    public PersistentSerializablePortability() {
    }

    public PersistentSerializablePortability(ClassLoader classLoader) {
        super(classLoader);
    }

    @Override // org.terracotta.offheapstore.disk.persistent.Persistent
    public void flush() throws IOException {
    }

    @Override // org.terracotta.offheapstore.disk.persistent.Persistent
    public void close() throws IOException {
    }

    @Override // org.terracotta.offheapstore.disk.persistent.Persistent
    @FindbugsSuppressWarnings({"JLM_JSR166_UTILCONCURRENT_MONITORENTER"})
    public void persist(ObjectOutput output) throws IOException {
        output.writeInt(MAGIC);
        synchronized (this.lookup) {
            for (Map.Entry<Object, Object> e : this.lookup.entrySet()) {
                if (e.getKey() instanceof Integer) {
                    output.writeInt(((Integer) e.getKey()).intValue());
                    output.writeObject(e.getValue());
                }
            }
            output.writeInt(-1);
        }
    }

    @Override // org.terracotta.offheapstore.disk.persistent.Persistent
    @FindbugsSuppressWarnings({"JLM_JSR166_UTILCONCURRENT_MONITORENTER"})
    public void bootstrap(ObjectInput input) throws IOException {
        if (input.readInt() != MAGIC) {
            throw new IOException("Wrong magic number");
        }
        synchronized (this.lookup) {
            int nextIndex = 0;
            while (true) {
                int representation = input.readInt();
                if (representation == -1) {
                    if (this.nextStreamIndex == 0) {
                        this.nextStreamIndex = nextIndex;
                    } else if (this.nextStreamIndex != nextIndex) {
                        throw new IOException("Cannot bootstrap already used instance");
                    }
                } else {
                    try {
                        ObjectStreamClass osc = (ObjectStreamClass) input.readObject();
                        SerializablePortability.SerializableDataKey key = new SerializablePortability.SerializableDataKey(disconnect(osc), true);
                        ObjectStreamClass oldOsc = (ObjectStreamClass) this.lookup.putIfAbsent(Integer.valueOf(representation), osc);
                        Integer oldRep = (Integer) this.lookup.putIfAbsent(key, Integer.valueOf(representation));
                        if (oldRep != null && !oldRep.equals(Integer.valueOf(representation))) {
                            throw new IOException("Existing colliding class mapping detected");
                        }
                        if (oldOsc != null && !oldOsc.getName().equals(osc.getName())) {
                            throw new IOException("Existing colliding class mapping detected");
                        }
                        nextIndex = Math.max(nextIndex, representation + 1);
                    } catch (ClassNotFoundException e) {
                        throw new IOException(e);
                    }
                }
            }
        }
    }
}
