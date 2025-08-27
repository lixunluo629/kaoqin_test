package org.hyperic.sigar.vmware;

import java.io.File;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarLoader;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/vmware/VMwareObject.class */
abstract class VMwareObject {
    public static final boolean LOADED = loadLibraries();
    int ptr = 0;
    long ptr64 = 0;

    private static native boolean init(String str);

    abstract void destroy();

    VMwareObject() {
    }

    private static boolean loadLibraries() {
        if (!SigarLoader.IS_LINUX && !SigarLoader.IS_WIN32) {
            return false;
        }
        try {
            Sigar.load();
            String lib = VMControlLibrary.getSharedLibrary();
            if (lib == null) {
                return false;
            }
            if (SigarLoader.IS_WIN32) {
                File root = new File(lib).getParentFile();
                String[] libs = {"libeay32.dll", "ssleay32.dll"};
                for (String str : libs) {
                    File ssllib = new File(root, str);
                    if (!ssllib.exists()) {
                        return false;
                    }
                    try {
                        System.load(ssllib.getPath());
                    } catch (UnsatisfiedLinkError e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
            return init(lib);
        } catch (Exception e2) {
            return false;
        }
    }

    public void dispose() {
        if (this.ptr != 0 || this.ptr64 != 0) {
            destroy();
            this.ptr = 0;
            this.ptr64 = 0L;
        }
    }

    protected void finalize() {
        dispose();
    }
}
