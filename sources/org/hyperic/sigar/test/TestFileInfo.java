package org.hyperic.sigar.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import org.hyperic.sigar.DirStat;
import org.hyperic.sigar.FileInfo;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarNotImplementedException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestFileInfo.class */
public class TestFileInfo extends SigarTestCase {
    public TestFileInfo(String name) {
        super(name);
    }

    private void getFileInfo(Sigar sigar, String file) throws SigarException {
        traceln(new StringBuffer().append("Entry=").append(file).toString());
        FileInfo info = sigar.getFileInfo(file);
        assertGtEqZeroTrace("Permisions", info.getPermissions());
        assertTrueTrace("Permissions", info.getPermissionsString());
        assertGtEqZeroTrace("Mode", info.getMode());
        assertTrueTrace("Type", info.getTypeString());
        assertGtEqZeroTrace("Size", info.getSize());
        assertGtEqZeroTrace("Uid", info.getUid());
        assertGtEqZeroTrace("Gid", info.getUid());
        assertGtEqZeroTrace("Inode", info.getInode());
        traceln(new StringBuffer().append("Device=").append(info.getDevice()).toString());
        assertGtEqZeroTrace("Nlink", info.getNlink());
        assertGtEqZeroTrace("Atime", info.getAtime());
        traceln(new Date(info.getAtime()).toString());
        assertGtZeroTrace("Mtime", info.getMtime());
        traceln(new Date(info.getMtime()).toString());
        assertGtZeroTrace("Ctime", info.getCtime());
        traceln(new Date(info.getCtime()).toString());
        if (info.getType() == 2) {
            try {
                DirStat stats = sigar.getDirStat(file);
                assertEqualsTrace("Total", new File(file).list().length, stats.getTotal());
                assertGtEqZeroTrace("Files", stats.getFiles());
                assertGtEqZeroTrace("Subdirs", stats.getSubdirs());
            } catch (SigarNotImplementedException e) {
            }
        } else {
            try {
                sigar.getDirStat(file);
                assertTrue(false);
            } catch (SigarException e2) {
                assertTrue(true);
            }
        }
        sigar.getLinkInfo(file);
    }

    public void testCreate() throws Exception {
        Sigar sigar = getSigar();
        File dir = new File(System.getProperty("user.dir"));
        String[] entries = dir.list();
        for (String file : entries) {
            File testFile = new File(dir, file);
            if (testFile.exists() && testFile.canRead() && !testFile.isHidden()) {
                traceln(new StringBuffer().append(file).append(":").toString());
                getFileInfo(sigar, testFile.getAbsolutePath());
            }
        }
        try {
            getFileInfo(sigar, "NO SUCH FILE");
            assertTrue(false);
        } catch (SigarNotImplementedException e) {
        } catch (SigarException e2) {
            traceln(new StringBuffer().append("NO SUCH FILE").append(": ").append(e2.getMessage()).toString());
            assertTrue(true);
        }
        File tmp = File.createTempFile("sigar-", "");
        String file2 = tmp.getAbsolutePath();
        tmp.deleteOnExit();
        traceln(new StringBuffer().append("TMP=").append(file2).toString());
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e3) {
        }
        try {
            FileInfo info = sigar.getFileInfo(file2);
            FileOutputStream os = null;
            try {
                try {
                    os = new FileOutputStream(file2);
                    os.write(1);
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e4) {
                        }
                    }
                    tmp.setReadOnly();
                    boolean changed = info.changed();
                    traceln(info.diff());
                    assertTrue(info.getPreviousInfo().getSize() != info.getSize());
                    assertTrue(changed);
                } catch (Throwable th) {
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e5) {
                        }
                    }
                    throw th;
                }
            } catch (IOException ioe) {
                throw ioe;
            }
        } catch (SigarNotImplementedException e6) {
        } catch (SigarException e7) {
            throw e7;
        }
    }
}
