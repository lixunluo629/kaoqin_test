package com.sun.jna;

import org.hyperic.sigar.OperatingSystem;

/* loaded from: jna-3.0.9.jar:com/sun/jna/Platform.class */
public final class Platform {
    private static final int UNSPECIFIED = -1;
    private static final int MAC = 0;
    private static final int LINUX = 1;
    private static final int WINDOWS = 2;
    private static final int SOLARIS = 3;
    private static final int FREEBSD = 4;
    private static final int OPENBSD = 5;
    private static final int WINDOWSCE = 6;
    private static final int osType;

    static {
        String osName = System.getProperty("os.name");
        if (osName.startsWith(OperatingSystem.NAME_LINUX)) {
            osType = 1;
            return;
        }
        if (osName.startsWith("Mac") || osName.startsWith("Darwin")) {
            osType = 0;
            return;
        }
        if (osName.startsWith("Windows CE")) {
            osType = 6;
            return;
        }
        if (osName.startsWith("Windows")) {
            osType = 2;
            return;
        }
        if (osName.startsWith(OperatingSystem.NAME_SOLARIS) || osName.startsWith("SunOS")) {
            osType = 3;
            return;
        }
        if (osName.startsWith(OperatingSystem.NAME_FREEBSD)) {
            osType = 4;
        } else if (osName.startsWith(OperatingSystem.NAME_OPENBSD)) {
            osType = 5;
        } else {
            osType = -1;
        }
    }

    private Platform() {
    }

    public static final boolean isMac() {
        return osType == 0;
    }

    public static final boolean isLinux() {
        return osType == 1;
    }

    public static final boolean isWindowsCE() {
        return osType == 6;
    }

    public static final boolean isWindows() {
        return osType == 2 || osType == 6;
    }

    public static final boolean isSolaris() {
        return osType == 3;
    }

    public static final boolean isFreeBSD() {
        return osType == 4;
    }

    public static final boolean isOpenBSD() {
        return osType == 5;
    }

    public static final boolean isX11() {
        return (isWindows() || isMac()) ? false : true;
    }

    public static final boolean deleteNativeLibraryAfterVMExit() {
        return osType == 2;
    }

    public static final boolean hasRuntimeExec() {
        if (isWindowsCE() && "J9".equals(System.getProperty("java.vm.name"))) {
            return false;
        }
        return true;
    }
}
