package org.hyperic.sigar.test;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.Sigar;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestCpuInfo.class */
public class TestCpuInfo extends SigarTestCase {
    public TestCpuInfo(String name) {
        super(name);
    }

    public void testCreate() throws Exception {
        Sigar sigar = getSigar();
        CpuInfo[] infos = sigar.getCpuInfoList();
        for (int i = 0; i < infos.length; i++) {
            CpuInfo info = infos[i];
            traceln(new StringBuffer().append("num=").append(i).toString());
            traceln(new StringBuffer().append("vendor=").append(info.getVendor()).toString());
            traceln(new StringBuffer().append("model=").append(info.getModel()).toString());
            traceln(new StringBuffer().append("mhz=").append(info.getMhz()).toString());
            traceln(new StringBuffer().append("cache size=").append(info.getCacheSize()).toString());
            assertGtZeroTrace("totalSockets", info.getTotalSockets());
            assertGtZeroTrace("totalCores", info.getTotalCores());
            assertTrue(info.getTotalSockets() <= info.getTotalCores());
        }
        int mhz = infos[0].getMhz();
        int current = sigar.getCpuInfoList()[0].getMhz();
        assertEquals(new StringBuffer().append("Mhz=").append(current).append("/").append(mhz).toString(), current, mhz);
    }
}
