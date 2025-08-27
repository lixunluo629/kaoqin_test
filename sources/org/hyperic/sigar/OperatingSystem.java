package org.hyperic.sigar;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/OperatingSystem.class */
public class OperatingSystem extends SysInfo {
    public static final String NAME_NETWARE = "NetWare";
    public static final String[] NAMES;
    public static final boolean IS_WIN32;
    private static final Map supportedPlatforms;
    private static OperatingSystem instance;
    private String dataModel;
    private String cpuEndian;
    public static final String NAME_LINUX = "Linux";
    public static final String NAME_SOLARIS = "Solaris";
    public static final String NAME_HPUX = "HPUX";
    public static final String NAME_AIX = "AIX";
    public static final String NAME_MACOSX = "MacOSX";
    public static final String NAME_FREEBSD = "FreeBSD";
    public static final String NAME_OPENBSD = "OpenBSD";
    public static final String NAME_NETBSD = "NetBSD";
    public static final String[] UNIX_NAMES = {NAME_LINUX, NAME_SOLARIS, NAME_HPUX, NAME_AIX, NAME_MACOSX, NAME_FREEBSD, NAME_OPENBSD, NAME_NETBSD};
    public static final String NAME_WIN32 = "Win32";
    public static final String[] WIN32_NAMES = {NAME_WIN32};

    static {
        IS_WIN32 = System.getProperty("os.name").indexOf("Windows") != -1;
        supportedPlatforms = new HashMap();
        int len = UNIX_NAMES.length + WIN32_NAMES.length;
        String[] all = new String[len];
        System.arraycopy(UNIX_NAMES, 0, all, 0, UNIX_NAMES.length);
        all[len - 1] = NAME_WIN32;
        NAMES = all;
        for (int i = 0; i < NAMES.length; i++) {
            supportedPlatforms.put(NAMES[i], Boolean.TRUE);
        }
        instance = null;
    }

    public static boolean isSupported(String name) {
        return supportedPlatforms.get(name) == Boolean.TRUE;
    }

    public static boolean isWin32(String name) {
        return NAME_WIN32.equals(name);
    }

    private OperatingSystem() {
    }

    public static synchronized OperatingSystem getInstance() {
        if (instance == null) {
            Sigar sigar = new Sigar();
            OperatingSystem os = new OperatingSystem();
            try {
                try {
                    os.gather(sigar);
                    sigar.close();
                    Properties props = System.getProperties();
                    os.dataModel = props.getProperty("sun.arch.data.model");
                    os.cpuEndian = props.getProperty("sun.cpu.endian");
                    instance = os;
                } catch (SigarException e) {
                    throw new IllegalStateException(e.getMessage());
                }
            } catch (Throwable th) {
                sigar.close();
                throw th;
            }
        }
        return instance;
    }

    public String getDataModel() {
        return this.dataModel;
    }

    public String getCpuEndian() {
        return this.cpuEndian;
    }

    public static void main(String[] args) {
        System.out.println(new StringBuffer().append("all..............").append(Arrays.asList(NAMES)).toString());
        OperatingSystem os = getInstance();
        System.out.println(new StringBuffer().append("description......").append(os.getDescription()).toString());
        System.out.println(new StringBuffer().append("name.............").append(os.name).toString());
        System.out.println(new StringBuffer().append("version..........").append(os.version).toString());
        System.out.println(new StringBuffer().append("arch.............").append(os.arch).toString());
        System.out.println(new StringBuffer().append("patch level......").append(os.patchLevel).toString());
        System.out.println(new StringBuffer().append("vendor...........").append(os.vendor).toString());
        System.out.println(new StringBuffer().append("vendor name......").append(os.vendorName).toString());
        System.out.println(new StringBuffer().append("vendor version...").append(os.vendorVersion).toString());
    }
}
