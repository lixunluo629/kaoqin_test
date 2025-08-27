package org.hyperic.sigar.test;

import org.hyperic.sigar.Sigar;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestLog.class */
public class TestLog extends SigarTestCase {
    public TestLog(String name) {
        super(name);
    }

    public void testCreate() throws Exception {
        Sigar sigar = new Sigar();
        sigar.enableLogging(true);
        sigar.enableLogging(false);
        sigar.enableLogging(true);
        sigar.close();
    }
}
