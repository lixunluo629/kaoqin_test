package org.hyperic.jni;

import org.hyperic.sigar.OperatingSystem;

/* loaded from: sigar-1.6.4.jar:org/hyperic/jni/ArchName.class */
public class ArchName {
    static boolean useDmalloc;

    static {
        useDmalloc = System.getProperty("jni.dmalloc") != null;
    }

    public static String getName() throws ArchNotSupportedException {
        String name = getArchName();
        if (useDmalloc) {
            name = new StringBuffer().append(name).append("-dmalloc").toString();
        }
        return name;
    }

    public static boolean is64() {
        return "64".equals(System.getProperty("sun.arch.data.model"));
    }

    private static String getArchName() throws ArchNotSupportedException {
        String name = System.getProperty("os.name");
        String arch = System.getProperty("os.arch");
        String version = System.getProperty("os.version");
        String majorVersion = version.substring(0, 1);
        if (arch.endsWith("86")) {
            arch = "x86";
        }
        if (name.equals(OperatingSystem.NAME_LINUX)) {
            return new StringBuffer().append(arch).append("-linux").toString();
        }
        if (name.indexOf("Windows") > -1) {
            return new StringBuffer().append(arch).append("-winnt").toString();
        }
        if (name.equals("SunOS")) {
            if (arch.startsWith("sparcv") && is64()) {
                arch = "sparc64";
            }
            return new StringBuffer().append(arch).append("-solaris").toString();
        }
        if (name.equals("HP-UX")) {
            if (arch.startsWith("IA64")) {
                arch = "ia64";
            } else {
                arch = "pa";
                if (is64()) {
                    arch = new StringBuffer().append(arch).append("64").toString();
                }
            }
            if (version.indexOf("11") > -1) {
                return new StringBuffer().append(arch).append("-hpux-11").toString();
            }
        } else {
            if (name.equals(OperatingSystem.NAME_AIX)) {
                if (majorVersion.equals("6")) {
                    majorVersion = "5";
                }
                return new StringBuffer().append(arch).append("-aix-").append(majorVersion).toString();
            }
            if (name.equals("Mac OS X") || name.equals("Darwin")) {
                if (is64()) {
                    return "universal64-macosx";
                }
                return "universal-macosx";
            }
            if (name.equals(OperatingSystem.NAME_FREEBSD)) {
                return new StringBuffer().append(arch).append("-freebsd-").append(majorVersion).toString();
            }
            if (name.equals(OperatingSystem.NAME_OPENBSD)) {
                return new StringBuffer().append(arch).append("-openbsd-").append(majorVersion).toString();
            }
            if (name.equals(OperatingSystem.NAME_NETBSD)) {
                return new StringBuffer().append(arch).append("-netbsd-").append(majorVersion).toString();
            }
            if (name.equals("OSF1")) {
                return new StringBuffer().append("alpha-osf1-").append(majorVersion).toString();
            }
            if (name.equals(OperatingSystem.NAME_NETWARE)) {
                return new StringBuffer().append("x86-netware-").append(majorVersion).toString();
            }
        }
        String desc = new StringBuffer().append(arch).append("-").append(name).append("-").append(version).toString();
        throw new ArchNotSupportedException(new StringBuffer().append("platform (").append(desc).append(") not supported").toString());
    }

    private ArchName() {
    }
}
