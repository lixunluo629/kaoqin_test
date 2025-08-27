package org.hyperic.sigar.win32.test;

import java.util.List;
import org.hyperic.sigar.test.SigarTestCase;
import org.hyperic.sigar.win32.Service;
import org.hyperic.sigar.win32.ServiceConfig;
import org.hyperic.sigar.win32.Win32Exception;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/win32/test/TestService.class */
public class TestService extends SigarTestCase {
    private static final String TEST_NAME = "MyTestService";
    private static final String PREFIX = "sigar.test.service.";
    private static final boolean TEST_CREATE = "true".equals(System.getProperty("sigar.test.service.create"));
    private static final boolean TEST_DELETE = "true".equals(System.getProperty("sigar.test.service.delete"));

    public TestService(String name) {
        super(name);
    }

    public void testServiceOpen() throws Exception {
        Service service = new Service("Eventlog");
        service.getConfig();
        service.close();
        try {
            new Service("DOESNOTEXIST");
            assertTrue(false);
        } catch (Win32Exception e) {
            traceln(new StringBuffer().append("DOESNOTEXIST").append(": ").append(e.getMessage()).toString());
            assertTrue(true);
        }
    }

    public void testServiceNames() throws Exception {
        List services = Service.getServiceNames();
        assertGtZeroTrace("getServiceNames", services.size());
        String[] ptql = {"Service.Name.ct=Ev", "Service.Path.ew=.exe"};
        for (int i = 0; i < ptql.length; i++) {
            List services2 = Service.getServiceNames(getSigar(), ptql[i]);
            assertGtZeroTrace(ptql[i], services2.size());
        }
        String[] invalid = {"State.Name.ct=Ev", "Service.Invalid.ew=.exe", "-"};
        for (int i2 = 0; i2 < invalid.length; i2++) {
            try {
                Service.getServiceNames(getSigar(), invalid[i2]);
                fail(new StringBuffer().append("'").append(invalid[i2]).append("' did not throw Exception").toString());
            } catch (Exception e) {
            }
        }
    }

    public void testServiceConfig() throws Exception {
        List configs = Service.getServiceConfigs(getSigar(), "svchost.exe");
        assertGtZeroTrace("getServiceConfigs", configs.size());
    }

    public void testServiceCreateDelete() throws Exception {
        if (!TEST_CREATE) {
            return;
        }
        ServiceConfig config = new ServiceConfig(TEST_NAME);
        config.setStartType(3);
        config.setDisplayName("My Test Service");
        config.setDescription(new StringBuffer().append("A Description of ").append(config.getDisplayName()).toString());
        config.setPath("C:\\Program Files\\My Test 1.0\\mytest.exe");
        Service.create(config);
    }

    public void testDeleteService() throws Exception {
        if (!TEST_DELETE) {
            return;
        }
        Service service = new Service(TEST_NAME);
        service.delete();
    }
}
