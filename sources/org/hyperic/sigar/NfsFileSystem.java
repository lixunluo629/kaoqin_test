package org.hyperic.sigar;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/NfsFileSystem.class */
public class NfsFileSystem extends FileSystem implements Serializable {
    private static final long serialVersionUID = 607239;
    private static final int NFS_PROGRAM = 100003;
    String hostname = null;

    public String getHostname() throws UnknownHostException {
        String dev;
        int ix;
        if (this.hostname == null && (ix = (dev = getDevName()).indexOf(":")) != -1) {
            String host = dev.substring(0, ix);
            try {
                InetAddress addr = InetAddress.getByName(host);
                this.hostname = addr.getHostAddress();
            } catch (UnknownHostException e) {
                this.hostname = host;
            }
        }
        return this.hostname;
    }

    public boolean ping() throws UnknownHostException {
        String hostname = getHostname();
        return RPC.ping(hostname, 16, 100003L, 2L) == 0 || RPC.ping(hostname, 32, 100003L, 2L) == 0;
    }

    public String getUnreachableMessage() {
        return new StringBuffer().append(getDevName()).append(" nfs server unreachable").toString();
    }

    public NfsUnreachableException getUnreachableException() {
        return new NfsUnreachableException(getUnreachableMessage());
    }

    public static void main(String[] args) throws Exception {
        Sigar.load();
        System.out.println(RPC.ping(args[0], "nfs"));
    }
}
