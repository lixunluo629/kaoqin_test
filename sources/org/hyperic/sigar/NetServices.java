package org.hyperic.sigar;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/NetServices.class */
public class NetServices {
    private static NetServices instance;
    private Sigar sigar = new Sigar();

    private NetServices() {
    }

    protected void finalize() {
        this.sigar.close();
    }

    private static NetServices getInstance() {
        if (instance == null) {
            instance = new NetServices();
        }
        return instance;
    }

    private static String getServiceName(int protocol, long port) {
        return getInstance().sigar.getNetServicesName(protocol, port);
    }

    public static String getName(String protocol, long port) {
        if (protocol.equals("tcp")) {
            return getTcpName(port);
        }
        if (protocol.equals("udp")) {
            return getUdpName(port);
        }
        return String.valueOf(port);
    }

    public static String getTcpName(long port) {
        return getServiceName(16, port);
    }

    public static String getUdpName(long port) {
        return getServiceName(32, port);
    }
}
