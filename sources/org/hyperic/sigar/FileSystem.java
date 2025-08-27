package org.hyperic.sigar;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/FileSystem.class */
public class FileSystem implements Serializable {
    private static final long serialVersionUID = 9641;
    String dirName = null;
    String devName = null;
    String typeName = null;
    String sysTypeName = null;
    String options = null;
    int type = 0;
    long flags = 0;
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_NONE = 1;
    public static final int TYPE_LOCAL_DISK = 2;
    public static final int TYPE_NETWORK = 3;
    public static final int TYPE_RAM_DISK = 4;
    public static final int TYPE_CDROM = 5;
    public static final int TYPE_SWAP = 6;

    public native void gather(Sigar sigar) throws SigarException;

    static FileSystem fetch(Sigar sigar) throws SigarException {
        FileSystem fileSystem = new FileSystem();
        fileSystem.gather(sigar);
        return fileSystem;
    }

    public String getDirName() {
        return this.dirName;
    }

    public String getDevName() {
        return this.devName;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public String getSysTypeName() {
        return this.sysTypeName;
    }

    public String getOptions() {
        return this.options;
    }

    public int getType() {
        return this.type;
    }

    public long getFlags() {
        return this.flags;
    }

    void copyTo(FileSystem copy) {
        copy.dirName = this.dirName;
        copy.devName = this.devName;
        copy.typeName = this.typeName;
        copy.sysTypeName = this.sysTypeName;
        copy.options = this.options;
        copy.type = this.type;
        copy.flags = this.flags;
    }

    public String toString() {
        return getDirName();
    }

    public Map toMap() {
        Map map = new HashMap();
        String strdirName = String.valueOf(this.dirName);
        if (!"-1".equals(strdirName)) {
            map.put("DirName", strdirName);
        }
        String strdevName = String.valueOf(this.devName);
        if (!"-1".equals(strdevName)) {
            map.put("DevName", strdevName);
        }
        String strtypeName = String.valueOf(this.typeName);
        if (!"-1".equals(strtypeName)) {
            map.put("TypeName", strtypeName);
        }
        String strsysTypeName = String.valueOf(this.sysTypeName);
        if (!"-1".equals(strsysTypeName)) {
            map.put("SysTypeName", strsysTypeName);
        }
        String stroptions = String.valueOf(this.options);
        if (!"-1".equals(stroptions)) {
            map.put("Options", stroptions);
        }
        String strtype = String.valueOf(this.type);
        if (!"-1".equals(strtype)) {
            map.put("Type", strtype);
        }
        String strflags = String.valueOf(this.flags);
        if (!"-1".equals(strflags)) {
            map.put("Flags", strflags);
        }
        return map;
    }
}
