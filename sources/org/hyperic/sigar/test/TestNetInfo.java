package org.hyperic.sigar.test;

import org.hyperic.sigar.NetConnection;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInfo;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarPermissionDeniedException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestNetInfo.class */
public class TestNetInfo extends SigarTestCase {
    public TestNetInfo(String name) {
        super(name);
    }

    public void testNetInfo() throws SigarException {
        NetInfo info = getSigar().getNetInfo();
        NetInterfaceConfig config = getSigar().getNetInterfaceConfig(null);
        traceln("");
        traceln(info.toString());
        traceln(config.toString());
        try {
            NetConnection[] connections = getSigar().getNetConnectionList(18);
            for (NetConnection netConnection : connections) {
                long port = netConnection.getLocalPort();
                String listenAddress = getSigar().getNetListenAddress(port);
                if (NetFlags.isAnyAddress(listenAddress)) {
                    listenAddress = "*";
                }
                traceln(new StringBuffer().append("Listen ").append(listenAddress).append(":").append(port).toString());
            }
        } catch (SigarPermissionDeniedException e) {
        }
    }
}
