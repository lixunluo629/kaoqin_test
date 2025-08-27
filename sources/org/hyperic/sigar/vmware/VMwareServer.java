package org.hyperic.sigar.vmware;

import java.util.List;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/vmware/VMwareServer.class */
public class VMwareServer extends VMwareObject {
    @Override // org.hyperic.sigar.vmware.VMwareObject
    native void destroy();

    private native void create();

    public native boolean connect(ConnectParams connectParams) throws VMwareException;

    public native void disconnect();

    public native boolean isConnected();

    public native boolean isRegistered(String str) throws VMwareException;

    public native List getRegisteredVmNames() throws VMwareException;

    public native String getResource(String str) throws VMwareException;

    public native String exec(String str) throws VMwareException;

    public VMwareServer() {
        create();
    }
}
