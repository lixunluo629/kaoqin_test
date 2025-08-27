package org.hyperic.sigar.vmware;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.hyperic.sigar.OperatingSystem;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/vmware/VM.class */
public class VM extends VMwareObject {
    public static final int EXECUTION_STATE_ON = 1;
    public static final int EXECUTION_STATE_OFF = 2;
    public static final int EXECUTION_STATE_SUSPENDED = 3;
    public static final int EXECUTION_STATE_STUCK = 4;
    public static final int EXECUTION_STATE_UNKNOWN = 5;
    public static final int POWEROP_MODE_HARD = 1;
    public static final int POWEROP_MODE_SOFT = 2;
    public static final int POWEROP_MODE_TRYSOFT = 3;
    private static final int POWEROP_MODE_DEFAULT = 3;
    public static final int PRODUCT_WS = 1;
    public static final int PRODUCT_GSX = 2;
    public static final int PRODUCT_ESX = 3;
    public static final int PRODUCT_SERVER = 4;
    public static final int PRODUCT_UNKNOWN = 5;
    public static final String SERVER = "Server";
    public static final int PLATFORM_WINDOWS = 1;
    public static final int PLATFORM_LINUX = 2;
    public static final int PLATFORM_VMNIX = 3;
    public static final int PLATFORM_UNKNOWN = 4;
    public static final int PRODINFO_PRODUCT = 1;
    public static final int PRODINFO_PLATFORM = 2;
    public static final int PRODINFO_BUILD = 3;
    public static final int PRODINFO_VERSION_MAJOR = 4;
    public static final int PRODINFO_VERSION_MINOR = 5;
    public static final int PRODINFO_VERSION_REVISION = 6;
    public static final int PERM_READ = 4;
    public static final int PERM_WRITE = 2;
    public static final int PERM_EXECUTE = 1;
    public static final String[] EXECUTION_STATES = {"INVALID", "ON", "OFF", "SUSPENDED", "STUCK", "UNKNOWN"};
    public static final String GSX = "GSX";
    public static final String ESX = "ESX";
    public static final String[] PRODUCTS = {"INVALID", "Workstation", GSX, ESX, "Server", "UNKNOWN"};
    public static final String[] PLATFORMS = {"INVALID", "Windows", OperatingSystem.NAME_LINUX, "VmNix", "UNKNOWN"};

    @Override // org.hyperic.sigar.vmware.VMwareObject
    native void destroy();

    private native void create();

    private native void connect(ConnectParams connectParams, String str, int i) throws VMwareException;

    public native void disconnect();

    public native boolean isConnected();

    public native int getExecutionState() throws VMwareException;

    public native int getRemoteConnections() throws VMwareException;

    public native int getUptime() throws VMwareException;

    public native int getHeartbeat() throws VMwareException;

    public native int getToolsLastActive() throws VMwareException;

    public native String getRunAsUser() throws VMwareException;

    public native int getPermissions() throws VMwareException;

    public native String getConfig(String str) throws VMwareException;

    public native String getResource(String str) throws VMwareException;

    public native String getGuestInfo(String str) throws VMwareException;

    public native void setGuestInfo(String str, String str2) throws VMwareException;

    public native int getProductInfo(int i) throws VMwareException;

    public native long getPid() throws VMwareException;

    public native int getId() throws VMwareException;

    public native void start(int i) throws VMwareException;

    public native void stop(int i) throws VMwareException;

    public native void reset(int i) throws VMwareException;

    public native void suspend(int i) throws VMwareException;

    private native void createNamedSnapshot(String str, String str2, boolean z, boolean z2) throws VMwareException;

    private native void createDefaultSnapshot() throws VMwareException;

    public native void revertToSnapshot() throws VMwareException;

    public native void removeAllSnapshots() throws VMwareException;

    public native boolean hasSnapshot() throws VMwareException;

    public native void saveScreenshot(String str) throws VMwareException;

    public native void deviceConnect(String str) throws VMwareException;

    public native void deviceDisconnect(String str) throws VMwareException;

    public native boolean deviceIsConnected(String str) throws VMwareException;

    public void connect(ConnectParams params, String config, boolean mks) throws VMwareException {
        connect(params, config, mks ? 1 : 0);
    }

    public void connect(ConnectParams params, String config) throws VMwareException {
        connect(params, config, 0);
    }

    public String getPermissionsString() {
        char[] perms = {'-', '-', '-'};
        try {
            int bits = getPermissions();
            if ((bits & 4) != 0) {
                perms[0] = 'r';
            }
            if ((bits & 2) != 0) {
                perms[1] = 'w';
            }
            if ((bits & 1) != 0) {
                perms[2] = 'x';
            }
        } catch (VMwareException e) {
        }
        return new String(perms);
    }

    private boolean checkPermission(int perm) {
        try {
            return (getPermissions() & perm) != 0;
        } catch (VMwareException e) {
            return false;
        }
    }

    public boolean canRead() {
        return checkPermission(4);
    }

    public boolean canWrite() {
        return checkPermission(2);
    }

    public boolean canExecute() {
        return checkPermission(1);
    }

    public String getVersion() throws VMwareException {
        return new StringBuffer().append(getProductInfo(4)).append(".").append(getProductInfo(5)).toString();
    }

    public String getFullVersion() throws VMwareException {
        return new StringBuffer().append(getVersion()).append(".").append(getProductInfo(6)).append(".").append(getProductInfo(3)).toString();
    }

    private String getConfigEx(String key) {
        try {
            return getConfig(key);
        } catch (VMwareException e) {
            return null;
        }
    }

    public String getDisplayName() {
        return getConfigEx("displayName");
    }

    public String getGuestOS() {
        return getConfigEx("guestOS");
    }

    public String getMemSize() {
        return getConfigEx("memsize");
    }

    public String getProductName() {
        try {
            int info = getProductInfo(1);
            return new StringBuffer().append(PRODUCTS[info]).append(SymbolConstants.SPACE_SYMBOL).append(getVersion()).toString();
        } catch (VMwareException e) {
            return null;
        }
    }

    public String getProductPlatform() {
        try {
            int info = getProductInfo(2);
            return PLATFORMS[info];
        } catch (VMwareException e) {
            return null;
        }
    }

    private boolean isState(int state) {
        try {
            return getExecutionState() == state;
        } catch (VMwareException e) {
            return false;
        }
    }

    public boolean isOn() {
        return isState(1);
    }

    public boolean isOff() {
        return isState(2);
    }

    public boolean isSuspended() {
        return isState(3);
    }

    public boolean isStuck() {
        return isState(4);
    }

    public boolean isESX() {
        try {
            return getProductInfo(1) == 3;
        } catch (VMwareException e) {
            return false;
        }
    }

    public boolean isGSX() {
        try {
            return getProductInfo(1) == 2;
        } catch (VMwareException e) {
            return false;
        }
    }

    public void start() throws VMwareException {
        start(3);
    }

    public void stop() throws VMwareException {
        stop(3);
    }

    public void reset() throws VMwareException {
        reset(3);
    }

    public void suspend() throws VMwareException {
        suspend(3);
    }

    public void resume(int mode) throws VMwareException {
        int state = getExecutionState();
        if (state != 3) {
            throw new VMwareException(new StringBuffer().append("VM state is not suspended: ").append(EXECUTION_STATES[state]).toString());
        }
        start(mode);
    }

    public void resume() throws VMwareException {
        resume(3);
    }

    public void createSnapshot(String name, String description, boolean quiesce, boolean memory) throws VMwareException {
        if (isESX()) {
            createNamedSnapshot(name, description, quiesce, memory);
        } else {
            createDefaultSnapshot();
        }
    }

    public VM() {
        create();
    }
}
