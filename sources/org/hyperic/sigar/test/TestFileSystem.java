package org.hyperic.sigar.test;

import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemMap;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.springframework.aop.framework.autoproxy.target.QuickTargetSourceCreator;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestFileSystem.class */
public class TestFileSystem extends SigarTestCase {
    public TestFileSystem(String name) {
        super(name);
    }

    public void testCreate() throws Exception {
        Sigar sigar = getSigar();
        FileSystem[] fslist = sigar.getFileSystemList();
        FileSystemMap mounts = sigar.getFileSystemMap();
        String dir = System.getProperty("user.home");
        assertTrueTrace(new StringBuffer().append("\nMountPoint for ").append(dir).toString(), mounts.getMountPoint(dir).getDirName());
        for (FileSystem fs : fslist) {
            assertTrue(mounts.getFileSystem(fs.getDirName()) != null);
            assertLengthTrace("DevName", fs.getDevName());
            assertLengthTrace("DirName", fs.getDirName());
            assertLengthTrace("TypeName", fs.getTypeName());
            assertLengthTrace("SysTypeName", fs.getSysTypeName());
            traceln(new StringBuffer().append("Options=").append(fs.getOptions()).toString());
            try {
                FileSystemUsage usage = sigar.getFileSystemUsage(fs.getDirName());
                switch (fs.getType()) {
                    case 2:
                        assertGtZeroTrace("  Total", usage.getTotal());
                        assertGtEqZeroTrace("  Free", usage.getFree());
                        assertGtEqZeroTrace("  Avail", usage.getAvail());
                        assertGtEqZeroTrace("   Used", usage.getUsed());
                        double usePercent = usage.getUsePercent() * 100.0d;
                        traceln(new StringBuffer().append("  Usage=").append(usePercent).append(QuickTargetSourceCreator.PREFIX_THREAD_LOCAL).toString());
                        assertTrue(usePercent <= 100.0d);
                        break;
                }
                traceln(new StringBuffer().append("  DiskReads=").append(usage.getDiskReads()).toString());
                traceln(new StringBuffer().append("  DiskWrites=").append(usage.getDiskWrites()).toString());
            } catch (SigarException e) {
                if (fs.getType() == 2) {
                    throw e;
                }
            }
        }
        try {
            sigar.getFileSystemUsage("T O T A L L Y B O G U S");
            assertTrue(false);
        } catch (SigarException e2) {
            assertTrue(true);
        }
    }
}
