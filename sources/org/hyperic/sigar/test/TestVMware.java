package org.hyperic.sigar.test;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.io.File;
import java.io.IOException;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.vmware.VMControlLibrary;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestVMware.class */
public class TestVMware extends SigarTestCase {
    public TestVMware(String name) {
        super(name);
    }

    public void testVMware() throws SigarException, InterruptedException {
        File build = new File(JsonPOJOBuilder.DEFAULT_BUILD_METHOD);
        if (!build.exists()) {
            return;
        }
        try {
            VMControlLibrary.link(build.getPath());
        } catch (IOException e) {
            traceln(e.getMessage());
        }
        traceln(new StringBuffer().append("vmware support=").append(VMControlLibrary.isLoaded()).toString());
    }
}
