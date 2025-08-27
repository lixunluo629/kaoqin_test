package org.hyperic.sigar.test;

import ch.qos.logback.classic.net.SyslogAppender;
import java.util.Date;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Who;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestWho.class */
public class TestWho extends SigarTestCase {
    public TestWho(String name) {
        super(name);
    }

    public void testWho() throws SigarException {
        traceln("");
        Who[] who = getSigar().getWhoList();
        for (int i = 0; i < who.length; i++) {
            String host = who[i].getHost();
            if (host.length() != 0) {
                host = new StringBuffer().append("(").append(host).append(")").toString();
            }
            traceln(new StringBuffer().append(who[i].getUser()).append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN).append(who[i].getDevice()).append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN).append(new Date(who[i].getTime() * 1000)).append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN).append(host).toString());
            assertLengthTrace("user", who[i].getUser());
        }
    }
}
