package org.hyperic.sigar;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/SysInfo.class */
public class SysInfo implements Serializable {
    private static final long serialVersionUID = 16002;
    String name = null;
    String version = null;
    String arch = null;
    String machine = null;
    String description = null;
    String patchLevel = null;
    String vendor = null;
    String vendorVersion = null;
    String vendorName = null;
    String vendorCodeName = null;

    public native void gather(Sigar sigar) throws SigarException;

    static SysInfo fetch(Sigar sigar) throws SigarException {
        SysInfo sysInfo = new SysInfo();
        sysInfo.gather(sigar);
        return sysInfo;
    }

    public String getName() {
        return this.name;
    }

    public String getVersion() {
        return this.version;
    }

    public String getArch() {
        return this.arch;
    }

    public String getMachine() {
        return this.machine;
    }

    public String getDescription() {
        return this.description;
    }

    public String getPatchLevel() {
        return this.patchLevel;
    }

    public String getVendor() {
        return this.vendor;
    }

    public String getVendorVersion() {
        return this.vendorVersion;
    }

    public String getVendorName() {
        return this.vendorName;
    }

    public String getVendorCodeName() {
        return this.vendorCodeName;
    }

    void copyTo(SysInfo copy) {
        copy.name = this.name;
        copy.version = this.version;
        copy.arch = this.arch;
        copy.machine = this.machine;
        copy.description = this.description;
        copy.patchLevel = this.patchLevel;
        copy.vendor = this.vendor;
        copy.vendorVersion = this.vendorVersion;
        copy.vendorName = this.vendorName;
        copy.vendorCodeName = this.vendorCodeName;
    }

    public Map toMap() {
        Map map = new HashMap();
        String strname = String.valueOf(this.name);
        if (!"-1".equals(strname)) {
            map.put("Name", strname);
        }
        String strversion = String.valueOf(this.version);
        if (!"-1".equals(strversion)) {
            map.put("Version", strversion);
        }
        String strarch = String.valueOf(this.arch);
        if (!"-1".equals(strarch)) {
            map.put("Arch", strarch);
        }
        String strmachine = String.valueOf(this.machine);
        if (!"-1".equals(strmachine)) {
            map.put("Machine", strmachine);
        }
        String strdescription = String.valueOf(this.description);
        if (!"-1".equals(strdescription)) {
            map.put("Description", strdescription);
        }
        String strpatchLevel = String.valueOf(this.patchLevel);
        if (!"-1".equals(strpatchLevel)) {
            map.put("PatchLevel", strpatchLevel);
        }
        String strvendor = String.valueOf(this.vendor);
        if (!"-1".equals(strvendor)) {
            map.put("Vendor", strvendor);
        }
        String strvendorVersion = String.valueOf(this.vendorVersion);
        if (!"-1".equals(strvendorVersion)) {
            map.put("VendorVersion", strvendorVersion);
        }
        String strvendorName = String.valueOf(this.vendorName);
        if (!"-1".equals(strvendorName)) {
            map.put("VendorName", strvendorName);
        }
        String strvendorCodeName = String.valueOf(this.vendorCodeName);
        if (!"-1".equals(strvendorCodeName)) {
            map.put("VendorCodeName", strvendorCodeName);
        }
        return map;
    }

    public String toString() {
        return toMap().toString();
    }
}
