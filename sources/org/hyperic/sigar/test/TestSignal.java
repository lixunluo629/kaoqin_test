package org.hyperic.sigar.test;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.hyperic.sigar.Sigar;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestSignal.class */
public class TestSignal extends SigarTestCase {
    public TestSignal(String name) {
        super(name);
    }

    public void testCreate() throws Exception {
        String[] signals = {"HUP", "INT", "KILL", "QUIT", "TERM", "USR1", "USR2"};
        for (String sig : signals) {
            traceln(new StringBuffer().append(sig).append(SymbolConstants.EQUAL_SYMBOL).append(Sigar.getSigNum(sig)).toString());
        }
    }
}
