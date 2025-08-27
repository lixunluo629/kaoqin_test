package org.hyperic.sigar;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/ProcCred.class */
public class ProcCred implements Serializable {
    private static final long serialVersionUID = 3062;
    long uid = 0;
    long gid = 0;
    long euid = 0;
    long egid = 0;

    public native void gather(Sigar sigar, long j) throws SigarException;

    static ProcCred fetch(Sigar sigar, long pid) throws SigarException {
        ProcCred procCred = new ProcCred();
        procCred.gather(sigar, pid);
        return procCred;
    }

    public long getUid() {
        return this.uid;
    }

    public long getGid() {
        return this.gid;
    }

    public long getEuid() {
        return this.euid;
    }

    public long getEgid() {
        return this.egid;
    }

    void copyTo(ProcCred copy) {
        copy.uid = this.uid;
        copy.gid = this.gid;
        copy.euid = this.euid;
        copy.egid = this.egid;
    }

    public Map toMap() {
        Map map = new HashMap();
        String struid = String.valueOf(this.uid);
        if (!"-1".equals(struid)) {
            map.put("Uid", struid);
        }
        String strgid = String.valueOf(this.gid);
        if (!"-1".equals(strgid)) {
            map.put("Gid", strgid);
        }
        String streuid = String.valueOf(this.euid);
        if (!"-1".equals(streuid)) {
            map.put("Euid", streuid);
        }
        String stregid = String.valueOf(this.egid);
        if (!"-1".equals(stregid)) {
            map.put("Egid", stregid);
        }
        return map;
    }

    public String toString() {
        return toMap().toString();
    }
}
