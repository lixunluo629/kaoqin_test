package org.hyperic.sigar.test;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarNotImplementedException;
import org.hyperic.sigar.SigarPermissionDeniedException;
import org.hyperic.sigar.Tcp;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestTcpStat.class */
public class TestTcpStat extends SigarTestCase {
    public TestTcpStat(String name) {
        super(name);
    }

    public void testCreate() throws Exception {
        Sigar sigar = getSigar();
        try {
            Tcp tcp = sigar.getTcp();
            traceln("");
            assertValidFieldTrace("ActiveOpens", tcp.getActiveOpens());
            assertValidFieldTrace("PassiveOpens", tcp.getPassiveOpens());
            assertValidFieldTrace("AttemptFails", tcp.getAttemptFails());
            assertValidFieldTrace("EstabResets", tcp.getEstabResets());
            assertValidFieldTrace("CurrEstab", tcp.getCurrEstab());
            assertValidFieldTrace("InSegs", tcp.getInSegs());
            assertValidFieldTrace("OutSegs", tcp.getOutSegs());
            assertValidFieldTrace("RetransSegs", tcp.getRetransSegs());
            assertValidFieldTrace("OutRsts", tcp.getOutRsts());
        } catch (SigarNotImplementedException e) {
        } catch (SigarPermissionDeniedException e2) {
        }
    }
}
