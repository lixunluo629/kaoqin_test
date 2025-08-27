package org.hyperic.sigar.test;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarNotImplementedException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestLoadAverage.class */
public class TestLoadAverage extends SigarTestCase {
    public TestLoadAverage(String name) {
        super(name);
    }

    public void testCreate() throws Exception {
        Sigar sigar = getSigar();
        try {
            double[] loadavg = sigar.getLoadAverage();
            assertTrue(loadavg.length == 3);
            traceln(new StringBuffer().append("1min=").append(loadavg[0]).toString());
            traceln(new StringBuffer().append("5min=").append(loadavg[1]).toString());
            traceln(new StringBuffer().append("15min=").append(loadavg[2]).toString());
        } catch (SigarNotImplementedException e) {
        }
    }
}
