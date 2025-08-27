package org.hyperic.sigar.win32;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.File;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/win32/Win32.class */
public abstract class Win32 {
    public static final String EXE_EXT = ".exe";

    public static native String findExecutable(String str) throws SigarException;

    static {
        try {
            Sigar.load();
        } catch (SigarException e) {
        }
    }

    public static String findScriptExecutable(String name) {
        int ix = name.lastIndexOf(".");
        if (ix == -1) {
            return null;
        }
        String ext = name.substring(ix + 1);
        if (ext.equals("exe") || ext.equals("bat") || ext.equals("com")) {
            return null;
        }
        try {
            String exe = findExecutable(new File(name).getAbsolutePath());
            if (exe == null) {
                return null;
            }
            String exe2 = exe.toLowerCase();
            String name2 = name.toLowerCase();
            if (exe2.equals(name2) || exe2.endsWith(name2)) {
                return null;
            }
            File file = new File(exe2);
            if (file.getName().equals("wscript.exe")) {
                exe2 = new StringBuffer().append(file.getParent()).append(File.separator).append("cscript.exe").toString();
            }
            return exe2;
        } catch (SigarException e) {
            return null;
        }
    }

    public static FileVersion getFileVersion(String name) {
        FileVersion version = new FileVersion();
        if (version.gather(name)) {
            return version;
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < args.length; i++) {
            String file = new File(args[i]).getAbsoluteFile().toString();
            String exe = findScriptExecutable(file);
            if (exe != null) {
                System.out.println(new StringBuffer().append(args[i]).append(SymbolConstants.EQUAL_SYMBOL).append(exe).toString());
            }
        }
    }
}
