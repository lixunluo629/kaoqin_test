package org.hyperic.sigar.test;

import org.hyperic.sigar.CurrentProcessSummary;
import org.hyperic.sigar.ProcStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestProcStat.class */
public class TestProcStat extends SigarTestCase {
    public TestProcStat(String name) {
        super(name);
    }

    public void testCreate() throws Exception {
        Sigar sigar = getSigar();
        ProcStat stat = sigar.getProcStat();
        sigar.getProcList();
        assertTrue(stat.getTotal() > 1);
        traceln(stat.toString());
        SigarProxy proxy = SigarProxyCache.newInstance(getSigar());
        traceln(CurrentProcessSummary.get(proxy).toString());
    }
}
