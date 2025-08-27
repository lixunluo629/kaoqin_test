package org.hyperic.sigar;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/ProcCredName.class */
public class ProcCredName implements Serializable {
    private static final long serialVersionUID = 2266;
    String user = null;
    String group = null;

    public native void gather(Sigar sigar, long j) throws SigarException;

    static ProcCredName fetch(Sigar sigar, long pid) throws SigarException {
        ProcCredName procCredName = new ProcCredName();
        procCredName.gather(sigar, pid);
        return procCredName;
    }

    public String getUser() {
        return this.user;
    }

    public String getGroup() {
        return this.group;
    }

    void copyTo(ProcCredName copy) {
        copy.user = this.user;
        copy.group = this.group;
    }

    public Map toMap() {
        Map map = new HashMap();
        String struser = String.valueOf(this.user);
        if (!"-1".equals(struser)) {
            map.put("User", struser);
        }
        String strgroup = String.valueOf(this.group);
        if (!"-1".equals(strgroup)) {
            map.put("Group", strgroup);
        }
        return map;
    }

    public String toString() {
        return toMap().toString();
    }
}
