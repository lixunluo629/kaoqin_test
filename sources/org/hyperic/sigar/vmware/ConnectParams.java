package org.hyperic.sigar.vmware;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/vmware/ConnectParams.class */
public class ConnectParams extends VMwareObject {
    private native void create(String str, int i, String str2, String str3);

    @Override // org.hyperic.sigar.vmware.VMwareObject
    native void destroy();

    public ConnectParams() {
        this(null, 0, null, null);
    }

    public ConnectParams(String host, int port, String user, String pass) {
        create(host, port, user, pass);
    }
}
