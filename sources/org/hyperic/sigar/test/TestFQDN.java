package org.hyperic.sigar.test;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestFQDN.class */
public class TestFQDN extends SigarTestCase {
    public TestFQDN(String name) {
        super(name);
    }

    public void testCreate() throws Exception {
        String fqdn = getSigar().getFQDN();
        traceln(new StringBuffer().append("fqdn=").append(fqdn).toString());
        boolean validFQDN = fqdn.indexOf(".") > 0;
        assertTrue(validFQDN);
    }
}
