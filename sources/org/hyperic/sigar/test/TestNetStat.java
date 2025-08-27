package org.hyperic.sigar.test;

import org.hyperic.sigar.NetConnection;
import org.hyperic.sigar.NetStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarNotImplementedException;
import org.hyperic.sigar.SigarPermissionDeniedException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestNetStat.class */
public class TestNetStat extends SigarTestCase {
    public TestNetStat(String name) {
        super(name);
    }

    public void testCreate() throws Exception {
        Sigar sigar = getSigar();
        try {
            NetStat netstat = sigar.getNetStat();
            traceln("");
            assertGtEqZeroTrace("Outbound", netstat.getTcpOutboundTotal());
            assertGtEqZeroTrace("Inbound", netstat.getTcpInboundTotal());
            int[] states = netstat.getTcpStates();
            for (int i = 0; i < 14; i++) {
                assertGtEqZeroTrace(NetConnection.getStateString(i), states[i]);
            }
        } catch (SigarNotImplementedException e) {
        } catch (SigarPermissionDeniedException e2) {
        }
    }
}
