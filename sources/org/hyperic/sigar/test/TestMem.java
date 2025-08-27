package org.hyperic.sigar.test;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestMem.class */
public class TestMem extends SigarTestCase {
    public TestMem(String name) {
        super(name);
    }

    public void testCreate() throws Exception {
        Sigar sigar = getSigar();
        Mem mem = sigar.getMem();
        assertGtZeroTrace("Total", mem.getTotal());
        assertGtZeroTrace("Used", mem.getUsed());
        traceln(new StringBuffer().append("UsedPercent=").append(mem.getUsedPercent()).toString());
        assertGtZeroTrace("(long)UsedPercent", (long) mem.getUsedPercent());
        assertTrue(mem.getUsedPercent() <= 100.0d);
        traceln(new StringBuffer().append("FreePercent=").append(mem.getFreePercent()).toString());
        assertGtEqZeroTrace("(long)FreePercent", (long) mem.getFreePercent());
        assertTrue(mem.getFreePercent() < 100.0d);
        assertGtZeroTrace("Free", mem.getFree());
        assertGtZeroTrace("ActualUsed", mem.getActualUsed());
        assertGtZeroTrace("ActualFree", mem.getActualFree());
        assertGtZeroTrace("Ram", mem.getRam());
        assertTrue(mem.getRam() % 8 == 0);
    }
}
