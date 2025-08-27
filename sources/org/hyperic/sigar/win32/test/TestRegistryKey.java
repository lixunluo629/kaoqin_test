package org.hyperic.sigar.win32.test;

import java.util.ArrayList;
import org.hyperic.sigar.test.SigarTestCase;
import org.hyperic.sigar.win32.RegistryKey;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/win32/test/TestRegistryKey.class */
public class TestRegistryKey extends SigarTestCase {
    private static final boolean TEST_WRITE = false;

    public TestRegistryKey(String name) {
        super(name);
    }

    public void testRegistryRead() throws Exception {
        RegistryKey software = RegistryKey.LocalMachine.openSubKey("SOFTWARE");
        String[] keys = software.getSubKeyNames();
        assertTrue(keys.length > 0);
        software.close();
    }

    public void testHardwareValues() throws Exception {
        RegistryKey hw = RegistryKey.LocalMachine.openSubKey("HARDWARE\\DESCRIPTION\\System");
        try {
            ArrayList values = new ArrayList();
            hw.getMultiStringValue("SystemBiosVersion", values);
            assertGtZeroTrace("SystemBiosVersion.size()", values.size());
            traceln(new StringBuffer().append("SystemBiosVersion=").append(values).toString());
        } catch (Exception e) {
        }
        RegistryKey cpu0 = hw.openSubKey("CentralProcessor\\0");
        String cpu = cpu0.getStringValue("ProcessorNameString");
        assertLengthTrace("cpu0", cpu);
        cpu0.close();
        hw.close();
    }

    public void testSoftwareValues() throws Exception {
        RegistryKey ms = RegistryKey.LocalMachine.openSubKey("SOFTWARE\\Microsoft");
        RegistryKey msmq = null;
        try {
            msmq = ms.openSubKey("MSMQ\\Parameters");
        } catch (Exception e) {
        }
        if (msmq != null) {
            traceln("MSMQ...");
            if (msmq.getSubKeyNames().length > 0) {
                try {
                    String build = msmq.getStringValue("CurrentBuild");
                    assertLengthTrace("CurrentBuild", build);
                    int id = msmq.getIntValue("SeqID");
                    assertGtZeroTrace("SeqID", id);
                } catch (Exception e2) {
                }
            }
            msmq.close();
        }
        RegistryKey sql = null;
        try {
            sql = ms.openSubKey("Microsoft SQL Server\\MSSQL.1\\Setup");
        } catch (Exception e3) {
        }
        if (sql != null) {
            traceln("MsSQL...");
            try {
                String edition = sql.getStringValue("Edition");
                assertLengthTrace("Edition", edition);
            } catch (Exception e4) {
            }
            sql.close();
        }
        ms.close();
        try {
            RegistryKey tc = RegistryKey.LocalMachine.openSubKey("SOFTWARE\\Apache Software Foundation\\Procrun 2.0\\Tomcat6\\Parameters\\Java");
            traceln("Tomcat6...");
            ArrayList values = new ArrayList();
            tc.getMultiStringValue("Options", values);
            assertGtZeroTrace("Options.size()", values.size());
            traceln(new StringBuffer().append("Options=").append(values).toString());
            tc.close();
        } catch (Exception e5) {
        }
    }

    public void testRegistryWrite() throws Exception {
    }
}
