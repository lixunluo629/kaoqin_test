package org.hyperic.sigar;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/NetInterfaceConfig.class */
public class NetInterfaceConfig implements Serializable {
    private static final long serialVersionUID = 15948;
    String name = null;
    String hwaddr = null;
    String type = null;
    String description = null;
    String address = null;
    String destination = null;
    String broadcast = null;
    String netmask = null;
    long flags = 0;
    long mtu = 0;
    long metric = 0;

    public native void gather(Sigar sigar, String str) throws SigarException;

    static NetInterfaceConfig fetch(Sigar sigar, String name) throws SigarException {
        NetInterfaceConfig netInterfaceConfig = new NetInterfaceConfig();
        netInterfaceConfig.gather(sigar, name);
        return netInterfaceConfig;
    }

    public String getName() {
        return this.name;
    }

    public String getHwaddr() {
        return this.hwaddr;
    }

    public String getType() {
        return this.type;
    }

    public String getDescription() {
        return this.description;
    }

    public String getAddress() {
        return this.address;
    }

    public String getDestination() {
        return this.destination;
    }

    public String getBroadcast() {
        return this.broadcast;
    }

    public String getNetmask() {
        return this.netmask;
    }

    public long getFlags() {
        return this.flags;
    }

    public long getMtu() {
        return this.mtu;
    }

    public long getMetric() {
        return this.metric;
    }

    void copyTo(NetInterfaceConfig copy) {
        copy.name = this.name;
        copy.hwaddr = this.hwaddr;
        copy.type = this.type;
        copy.description = this.description;
        copy.address = this.address;
        copy.destination = this.destination;
        copy.broadcast = this.broadcast;
        copy.netmask = this.netmask;
        copy.flags = this.flags;
        copy.mtu = this.mtu;
        copy.metric = this.metric;
    }

    public Map toMap() {
        Map map = new HashMap();
        String strname = String.valueOf(this.name);
        if (!"-1".equals(strname)) {
            map.put("Name", strname);
        }
        String strhwaddr = String.valueOf(this.hwaddr);
        if (!"-1".equals(strhwaddr)) {
            map.put("Hwaddr", strhwaddr);
        }
        String strtype = String.valueOf(this.type);
        if (!"-1".equals(strtype)) {
            map.put("Type", strtype);
        }
        String strdescription = String.valueOf(this.description);
        if (!"-1".equals(strdescription)) {
            map.put("Description", strdescription);
        }
        String straddress = String.valueOf(this.address);
        if (!"-1".equals(straddress)) {
            map.put("Address", straddress);
        }
        String strdestination = String.valueOf(this.destination);
        if (!"-1".equals(strdestination)) {
            map.put("Destination", strdestination);
        }
        String strbroadcast = String.valueOf(this.broadcast);
        if (!"-1".equals(strbroadcast)) {
            map.put("Broadcast", strbroadcast);
        }
        String strnetmask = String.valueOf(this.netmask);
        if (!"-1".equals(strnetmask)) {
            map.put("Netmask", strnetmask);
        }
        String strflags = String.valueOf(this.flags);
        if (!"-1".equals(strflags)) {
            map.put("Flags", strflags);
        }
        String strmtu = String.valueOf(this.mtu);
        if (!"-1".equals(strmtu)) {
            map.put("Mtu", strmtu);
        }
        String strmetric = String.valueOf(this.metric);
        if (!"-1".equals(strmetric)) {
            map.put("Metric", strmetric);
        }
        return map;
    }

    public String toString() {
        return toMap().toString();
    }
}
