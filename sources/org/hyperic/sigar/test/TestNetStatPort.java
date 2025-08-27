package org.hyperic.sigar.test;

import java.net.InetAddress;
import java.util.ArrayList;
import org.hyperic.sigar.NetConnection;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarNotImplementedException;
import org.hyperic.sigar.SigarPermissionDeniedException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestNetStatPort.class */
public class TestNetStatPort extends SigarTestCase {
    public TestNetStatPort(String name) {
        super(name);
    }

    private void netstat(Sigar sigar, String addr, long port) throws Exception {
        InetAddress address = InetAddress.getByName(addr);
        traceln("");
        traceln(new StringBuffer().append("using address=").append(address).append(":").append(port).toString());
        try {
            NetStat netstat = sigar.getNetStat(address.getAddress(), port);
            assertGtEqZeroTrace("AllOutbound", netstat.getAllOutboundTotal());
            assertGtEqZeroTrace("Outbound", netstat.getTcpOutboundTotal());
            assertGtEqZeroTrace("Inbound", netstat.getTcpInboundTotal());
            assertGtEqZeroTrace("AllInbound", netstat.getAllInboundTotal());
            int[] states = netstat.getTcpStates();
            for (int i = 0; i < 14; i++) {
                assertGtEqZeroTrace(NetConnection.getStateString(i), states[i]);
            }
        } catch (SigarNotImplementedException e) {
        } catch (SigarPermissionDeniedException e2) {
        }
    }

    public void testCreate() throws Exception {
        Sigar sigar = getSigar();
        NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(null);
        ArrayList addrs = new ArrayList();
        addrs.add(ifconfig.getAddress());
        addrs.add(NetFlags.LOOPBACK_ADDRESS);
        if (JDK_14_COMPAT) {
            addrs.add(NetFlags.LOOPBACK_ADDRESS_V6);
        }
        for (int i = 0; i < addrs.size(); i++) {
            String addr = (String) addrs.get(i);
            netstat(sigar, addr, 22L);
        }
    }
}
