package org.hyperic.sigar.vmware;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.dongliu.apk.parser.struct.AndroidConstants;
import org.hyperic.jni.ArchName;
import org.hyperic.sigar.SigarLoader;
import org.hyperic.sigar.win32.RegistryKey;
import org.hyperic.sigar.win32.Win32Exception;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/vmware/VMControlLibrary.class */
public class VMControlLibrary {
    public static final String REGISTRY_ROOT = "SOFTWARE\\VMware, Inc.";
    public static final String PROP_VMCONTROL_SHLIB = "vmcontrol.shlib";
    private static final String VMCONTROL;
    private static final String VMCONTROL_DLL;
    private static final String VMCONTROL_SO;
    private static final String VMCONTROL_OBJ;
    private static final String GCC;
    private static final String TAR;
    private static final String LIBSSL;
    private static final String LIBCRYPTO;
    private static boolean isDebug;
    private static final boolean IS64 = ArchName.is64();
    private static final String VMWARE_LIB = getProperty("lib.vmware", getVMwareLib().getPath());
    private static final String VMCONTROL_TAR = getProperty("control.tar", new StringBuffer().append(VMWARE_LIB).append("/perl/control.tar").toString());

    static {
        VMCONTROL = new StringBuffer().append("vmcontrol").append(IS64 ? "64" : "").toString();
        VMCONTROL_DLL = new StringBuffer().append(VMCONTROL).append("lib.dll").toString();
        VMCONTROL_SO = new StringBuffer().append(VMCONTROL).append(".so").toString();
        VMCONTROL_OBJ = getProperty("vmcontrol.o", new StringBuffer().append("control-only/").append(VMCONTROL).append(".o").toString());
        GCC = getProperty("bin.gcc", "/usr/bin/gcc");
        TAR = getProperty("bin.tar", "/bin/tar");
        LIBSSL = getProperty("libssl", "libssl.so.0.9.7");
        LIBCRYPTO = getProperty("libcrypto", "libcrypto.so.0.9.7");
        isDebug = false;
    }

    private static String getProperty(String key, String defval) {
        return System.getProperty(new StringBuffer().append("vmcontrol.").append(key).toString(), defval);
    }

    private static File getVMwareLib() {
        String[] locations = {"/usr/lib/vmware", "/usr/local/lib/vmware"};
        for (String str : locations) {
            File lib = new File(str);
            if (lib.exists()) {
                return lib;
            }
        }
        for (String str2 : locations) {
            File lib2 = new File(new StringBuffer().append(str2).append("-api").toString());
            if (lib2.exists()) {
                return lib2;
            }
        }
        return new File(locations[0]);
    }

    private static File getLib(String name) {
        File lib = new File(VMWARE_LIB, new StringBuffer().append(AndroidConstants.LIB_PREFIX).append(name).toString());
        if (lib.isDirectory()) {
            lib = new File(lib, name);
        }
        return lib;
    }

    private static File getLibSSL() {
        return getLib(LIBSSL);
    }

    private static File getLibCrypto() {
        return getLib(LIBCRYPTO);
    }

    private static String toString(String[] args) {
        StringBuffer cmd = new StringBuffer();
        for (String str : args) {
            if (cmd.length() != 0) {
                cmd.append(' ');
            }
            cmd.append("'").append(str).append("'");
        }
        return cmd.toString();
    }

    private static void exec(String[] args) throws InterruptedException, IOException {
        int exitVal;
        Process proc = Runtime.getRuntime().exec(args);
        try {
            exitVal = proc.waitFor();
        } catch (InterruptedException e) {
        }
        if (exitVal != 0) {
            String msg = new StringBuffer().append("exec(").append(toString(args)).append(") failed: ").append(exitVal).toString();
            throw new IOException(msg);
        }
        if (isDebug) {
            System.out.println(new StringBuffer().append("exec(").append(toString(args)).append(") OK").toString());
        }
    }

    public static String getSharedLibrary() {
        return System.getProperty(PROP_VMCONTROL_SHLIB);
    }

    public static void setSharedLibrary(String lib) {
        System.setProperty(PROP_VMCONTROL_SHLIB, lib);
    }

    public static void link() throws InterruptedException, IOException {
        link(VMCONTROL_SO);
    }

    private static void linkWin32() {
        List dlls = new ArrayList();
        RegistryKey root = null;
        try {
            root = RegistryKey.LocalMachine.openSubKey(REGISTRY_ROOT);
            String[] keys = root.getSubKeyNames();
            for (String name : keys) {
                if (name.startsWith("VMware ")) {
                    RegistryKey subkey = null;
                    try {
                        subkey = root.openSubKey(name);
                        String path = subkey.getStringValue("InstallPath");
                        if (path != null) {
                            String path2 = path.trim();
                            if (path2.length() != 0) {
                                File dll = new File(new StringBuffer().append(path2).append(VMCONTROL_DLL).toString());
                                if (dll.exists()) {
                                    if (name.endsWith(" Server")) {
                                        dlls.add(0, dll.getPath());
                                    } else if (name.endsWith(" API")) {
                                        dlls.add(dll.getPath());
                                    }
                                }
                                if (subkey != null) {
                                    subkey.close();
                                }
                            } else if (subkey != null) {
                                subkey.close();
                            }
                        } else if (subkey != null) {
                            subkey.close();
                        }
                    } catch (Win32Exception e) {
                        if (subkey != null) {
                            subkey.close();
                        }
                    } catch (Throwable th) {
                        if (subkey != null) {
                            subkey.close();
                        }
                        throw th;
                    }
                }
            }
            if (root != null) {
                root.close();
            }
        } catch (Win32Exception e2) {
            if (root != null) {
                root.close();
            }
        } catch (Throwable th2) {
            if (root != null) {
                root.close();
            }
            throw th2;
        }
        if (dlls.size() != 0) {
            setSharedLibrary((String) dlls.get(0));
        }
    }

    public static void link(String name) throws InterruptedException, IOException {
        if (SigarLoader.IS_WIN32) {
            linkWin32();
            return;
        }
        File out = new File(name).getAbsoluteFile();
        if (out.isDirectory()) {
            out = new File(out, VMCONTROL_SO);
        }
        boolean exists = out.exists();
        if (exists) {
            setSharedLibrary(out.getPath());
            return;
        }
        if (!new File(VMCONTROL_TAR).exists()) {
            return;
        }
        File dir = out.getParentFile();
        if (!dir.isDirectory() || !dir.canWrite()) {
            throw new IOException(new StringBuffer().append("Cannot write to: ").append(dir).toString());
        }
        File obj = new File(dir, VMCONTROL_OBJ);
        if (!obj.exists()) {
            String[] extract_args = {TAR, "-xf", VMCONTROL_TAR, "-C", dir.toString(), VMCONTROL_OBJ};
            exec(extract_args);
        }
        List link_args = new ArrayList();
        link_args.add(GCC);
        link_args.add("-shared");
        link_args.add("-o");
        link_args.add(out.getPath());
        link_args.add(obj.getPath());
        if (IS64) {
            link_args.add("-lcrypto");
            link_args.add("-lssl");
        } else {
            File libssl = getLibSSL();
            File libcrypto = getLibCrypto();
            if (!libssl.exists()) {
                throw new FileNotFoundException(libssl.toString());
            }
            if (!new File(libssl.getParent(), "libc.so.6").exists()) {
                link_args.add("-Wl,-rpath");
                link_args.add(libssl.getParent());
                if (!libssl.getParent().equals(libcrypto.getParent())) {
                    link_args.add("-Wl,-rpath");
                    link_args.add(libcrypto.getParent());
                }
            }
            link_args.add(libssl.getPath());
            link_args.add(libcrypto.getPath());
        }
        exec((String[]) link_args.toArray(new String[0]));
        setSharedLibrary(out.getPath());
    }

    public static boolean isLoaded() {
        return VMwareObject.LOADED;
    }

    public static void main(String[] args) throws Exception {
        isDebug = true;
        if (args.length == 0) {
            link();
        } else {
            link(args[0]);
        }
        String shlib = getSharedLibrary();
        if (shlib == null) {
            System.out.println("No library found");
        } else {
            System.out.println(new StringBuffer().append("vmcontrol.shlib=").append(shlib).append(" (loaded=").append(isLoaded()).append(")").toString());
        }
    }
}
