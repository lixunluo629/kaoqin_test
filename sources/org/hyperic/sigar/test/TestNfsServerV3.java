package org.hyperic.sigar.test;

import org.hyperic.sigar.NfsServerV3;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestNfsServerV3.class */
public class TestNfsServerV3 extends SigarTestCase {
    public TestNfsServerV3(String name) {
        super(name);
    }

    public void testCreate() throws Exception {
        Sigar sigar = getSigar();
        try {
            NfsServerV3 nfs = sigar.getNfsServerV3();
            traceMethods(nfs);
        } catch (SigarException e) {
        }
    }
}
