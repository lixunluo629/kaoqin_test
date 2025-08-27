package org.hyperic.sigar.test;

import org.hyperic.sigar.NetRoute;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarNotImplementedException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestNetRoute.class */
public class TestNetRoute extends SigarTestCase {
    public TestNetRoute(String name) {
        super(name);
    }

    public void testNetRoute() throws SigarException {
        try {
            NetRoute[] routes = getSigar().getNetRouteList();
            assertTrue(routes.length > 0);
            for (NetRoute netRoute : routes) {
            }
        } catch (SigarNotImplementedException e) {
        }
    }
}
