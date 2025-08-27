package org.hyperic.sigar.test;

import java.util.Date;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.Uptime;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestUptime.class */
public class TestUptime extends SigarTestCase {
    public TestUptime(String name) {
        super(name);
    }

    public void testCreate() throws Exception {
        Sigar sigar = getSigar();
        Uptime uptime = sigar.getUptime();
        long now = System.currentTimeMillis();
        traceln(new StringBuffer().append("\nboottime=").append(new Date(now - (((long) uptime.getUptime()) * 1000))).toString());
        assertTrue(uptime.getUptime() > 0.0d);
    }
}
