package org.hyperic.sigar.test;

import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarNotImplementedException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestNetIf.class */
public class TestNetIf extends SigarTestCase {
    public TestNetIf(String name) {
        super(name);
    }

    private void getNetIflist(Sigar sigar, boolean getStats) throws Exception {
        String[] ifNames = sigar.getNetInterfaceList();
        for (String name : ifNames) {
            NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name);
            traceln(new StringBuffer().append("name=").append(name).toString());
            assertTrueTrace("Address", ifconfig.getAddress());
            assertTrueTrace("Netmask", ifconfig.getNetmask());
            if (getStats) {
                if ((ifconfig.getFlags() & 1) <= 0) {
                    traceln("!IFF_UP...skipping getNetInterfaceStat");
                } else {
                    try {
                        NetInterfaceStat ifstat = sigar.getNetInterfaceStat(name);
                        assertGtEqZeroTrace("RxPackets", ifstat.getRxPackets());
                        assertGtEqZeroTrace("TxPackets", ifstat.getTxPackets());
                        traceMethods(ifstat);
                    } catch (SigarNotImplementedException e) {
                    } catch (SigarException e2) {
                        if (name.indexOf(58) == -1) {
                            fail(new StringBuffer().append("getNetInterfaceStat(").append(name).append("): ").append(e2.getMessage()).toString());
                        }
                    }
                }
            }
        }
    }

    private void getGarbage(Sigar sigar) {
        try {
            traceln("testing bogus getNetInterfaceStat");
            sigar.getNetInterfaceStat("were switching to night vision");
            fail("switched to night vision");
        } catch (SigarException e) {
        }
        try {
            traceln("testing bogus getNetInterfaceConfig");
            sigar.getNetInterfaceConfig("happy meal");
            fail("unexpected treat in happy meal");
        } catch (SigarException e2) {
        }
    }

    public void testCreate() throws Exception {
        Sigar sigar = getSigar();
        getNetIflist(sigar, false);
        getNetIflist(sigar, false);
        getNetIflist(sigar, true);
        traceln(new StringBuffer().append("Default IP=").append(sigar.getNetInterfaceConfig().getAddress()).toString());
        getGarbage(sigar);
    }
}
