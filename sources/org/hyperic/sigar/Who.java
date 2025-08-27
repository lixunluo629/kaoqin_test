package org.hyperic.sigar;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/Who.class */
public class Who implements Serializable {
    private static final long serialVersionUID = 4241;
    String user = null;
    String device = null;
    String host = null;
    long time = 0;

    public native void gather(Sigar sigar) throws SigarException;

    static Who fetch(Sigar sigar) throws SigarException {
        Who who = new Who();
        who.gather(sigar);
        return who;
    }

    public String getUser() {
        return this.user;
    }

    public String getDevice() {
        return this.device;
    }

    public String getHost() {
        return this.host;
    }

    public long getTime() {
        return this.time;
    }

    void copyTo(Who copy) {
        copy.user = this.user;
        copy.device = this.device;
        copy.host = this.host;
        copy.time = this.time;
    }

    public Map toMap() {
        Map map = new HashMap();
        String struser = String.valueOf(this.user);
        if (!"-1".equals(struser)) {
            map.put("User", struser);
        }
        String strdevice = String.valueOf(this.device);
        if (!"-1".equals(strdevice)) {
            map.put("Device", strdevice);
        }
        String strhost = String.valueOf(this.host);
        if (!"-1".equals(strhost)) {
            map.put("Host", strhost);
        }
        String strtime = String.valueOf(this.time);
        if (!"-1".equals(strtime)) {
            map.put("Time", strtime);
        }
        return map;
    }

    public String toString() {
        return toMap().toString();
    }
}
