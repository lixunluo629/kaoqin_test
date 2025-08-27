package org.hyperic.sigar;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/NetRoute.class */
public class NetRoute implements Serializable {
    private static final long serialVersionUID = 13039;
    String destination = null;
    String gateway = null;
    long flags = 0;
    long refcnt = 0;
    long use = 0;
    long metric = 0;
    String mask = null;
    long mtu = 0;
    long window = 0;
    long irtt = 0;
    String ifname = null;

    public native void gather(Sigar sigar) throws SigarException;

    static NetRoute fetch(Sigar sigar) throws SigarException {
        NetRoute netRoute = new NetRoute();
        netRoute.gather(sigar);
        return netRoute;
    }

    public String getDestination() {
        return this.destination;
    }

    public String getGateway() {
        return this.gateway;
    }

    public long getFlags() {
        return this.flags;
    }

    public long getRefcnt() {
        return this.refcnt;
    }

    public long getUse() {
        return this.use;
    }

    public long getMetric() {
        return this.metric;
    }

    public String getMask() {
        return this.mask;
    }

    public long getMtu() {
        return this.mtu;
    }

    public long getWindow() {
        return this.window;
    }

    public long getIrtt() {
        return this.irtt;
    }

    public String getIfname() {
        return this.ifname;
    }

    void copyTo(NetRoute copy) {
        copy.destination = this.destination;
        copy.gateway = this.gateway;
        copy.flags = this.flags;
        copy.refcnt = this.refcnt;
        copy.use = this.use;
        copy.metric = this.metric;
        copy.mask = this.mask;
        copy.mtu = this.mtu;
        copy.window = this.window;
        copy.irtt = this.irtt;
        copy.ifname = this.ifname;
    }

    public Map toMap() {
        Map map = new HashMap();
        String strdestination = String.valueOf(this.destination);
        if (!"-1".equals(strdestination)) {
            map.put("Destination", strdestination);
        }
        String strgateway = String.valueOf(this.gateway);
        if (!"-1".equals(strgateway)) {
            map.put("Gateway", strgateway);
        }
        String strflags = String.valueOf(this.flags);
        if (!"-1".equals(strflags)) {
            map.put("Flags", strflags);
        }
        String strrefcnt = String.valueOf(this.refcnt);
        if (!"-1".equals(strrefcnt)) {
            map.put("Refcnt", strrefcnt);
        }
        String struse = String.valueOf(this.use);
        if (!"-1".equals(struse)) {
            map.put("Use", struse);
        }
        String strmetric = String.valueOf(this.metric);
        if (!"-1".equals(strmetric)) {
            map.put("Metric", strmetric);
        }
        String strmask = String.valueOf(this.mask);
        if (!"-1".equals(strmask)) {
            map.put("Mask", strmask);
        }
        String strmtu = String.valueOf(this.mtu);
        if (!"-1".equals(strmtu)) {
            map.put("Mtu", strmtu);
        }
        String strwindow = String.valueOf(this.window);
        if (!"-1".equals(strwindow)) {
            map.put("Window", strwindow);
        }
        String strirtt = String.valueOf(this.irtt);
        if (!"-1".equals(strirtt)) {
            map.put("Irtt", strirtt);
        }
        String strifname = String.valueOf(this.ifname);
        if (!"-1".equals(strifname)) {
            map.put("Ifname", strifname);
        }
        return map;
    }

    public String toString() {
        return toMap().toString();
    }
}
