package org.hyperic.sigar.test;

import org.hyperic.sigar.SigarException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestResourceLimit.class */
public class TestResourceLimit extends SigarTestCase {
    public TestResourceLimit(String name) {
        super(name);
    }

    public void testResourceLimit() throws SigarException {
        getSigar().getResourceLimit();
    }
}
