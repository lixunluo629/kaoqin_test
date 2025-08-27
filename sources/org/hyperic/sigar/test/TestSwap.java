package org.hyperic.sigar.test;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.Swap;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestSwap.class */
public class TestSwap extends SigarTestCase {
    public TestSwap(String name) {
        super(name);
    }

    public void testCreate() throws Exception {
        Sigar sigar = getSigar();
        Swap swap = sigar.getSwap();
        assertGtEqZeroTrace("Total", swap.getTotal());
        assertGtEqZeroTrace("Used", swap.getUsed());
        assertGtEqZeroTrace("Free", swap.getFree());
        assertEqualsTrace("Total-Used==Free", swap.getTotal() - swap.getUsed(), swap.getFree());
        traceln(new StringBuffer().append("PageIn=").append(swap.getPageIn()).toString());
        traceln(new StringBuffer().append("PageOut=").append(swap.getPageOut()).toString());
    }
}
