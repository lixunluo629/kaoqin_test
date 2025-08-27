package org.hyperic.sigar;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/NetInfo.class */
public class NetInfo implements Serializable {
    private static final long serialVersionUID = 9427;
    String defaultGateway = null;
    String hostName = null;
    String domainName = null;
    String primaryDns = null;
    String secondaryDns = null;

    public native void gather(Sigar sigar) throws SigarException;

    static NetInfo fetch(Sigar sigar) throws SigarException {
        NetInfo netInfo = new NetInfo();
        netInfo.gather(sigar);
        return netInfo;
    }

    public String getDefaultGateway() {
        return this.defaultGateway;
    }

    public String getHostName() {
        return this.hostName;
    }

    public String getDomainName() {
        return this.domainName;
    }

    public String getPrimaryDns() {
        return this.primaryDns;
    }

    public String getSecondaryDns() {
        return this.secondaryDns;
    }

    void copyTo(NetInfo copy) {
        copy.defaultGateway = this.defaultGateway;
        copy.hostName = this.hostName;
        copy.domainName = this.domainName;
        copy.primaryDns = this.primaryDns;
        copy.secondaryDns = this.secondaryDns;
    }

    public Map toMap() {
        Map map = new HashMap();
        String strdefaultGateway = String.valueOf(this.defaultGateway);
        if (!"-1".equals(strdefaultGateway)) {
            map.put("DefaultGateway", strdefaultGateway);
        }
        String strhostName = String.valueOf(this.hostName);
        if (!"-1".equals(strhostName)) {
            map.put("HostName", strhostName);
        }
        String strdomainName = String.valueOf(this.domainName);
        if (!"-1".equals(strdomainName)) {
            map.put("DomainName", strdomainName);
        }
        String strprimaryDns = String.valueOf(this.primaryDns);
        if (!"-1".equals(strprimaryDns)) {
            map.put("PrimaryDns", strprimaryDns);
        }
        String strsecondaryDns = String.valueOf(this.secondaryDns);
        if (!"-1".equals(strsecondaryDns)) {
            map.put("SecondaryDns", strsecondaryDns);
        }
        return map;
    }

    public String toString() {
        return toMap().toString();
    }
}
