package org.hyperic.sigar.test;

import org.hyperic.sigar.NfsServerV2;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestNfsServerV2.class */
public class TestNfsServerV2 extends SigarTestCase {
    public TestNfsServerV2(String name) {
        super(name);
    }

    public void testCreate() throws Exception {
        Sigar sigar = getSigar();
        try {
            NfsServerV2 nfs = sigar.getNfsServerV2();
            traceMethods(nfs);
        } catch (SigarException e) {
        }
    }
}
