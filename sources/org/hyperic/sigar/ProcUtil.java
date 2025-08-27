package org.hyperic.sigar;

import java.io.File;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/ProcUtil.class */
public class ProcUtil {
    private static boolean isClassName(String name) {
        int len = name.length();
        if (len == 0) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            char c = name.charAt(i);
            if (c != '.' && !Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    public static String getJavaMainClass(SigarProxy sigar, long pid) throws SigarException {
        String[] args = sigar.getProcArgs(pid);
        for (int i = 1; i < args.length; i++) {
            String arg = args[i];
            if (isClassName(arg.trim())) {
                return arg;
            }
            if (arg.equals("-jar")) {
                File file = new File(args[i + 1]);
                if (!file.isAbsolute()) {
                    try {
                        String cwd = sigar.getProcExe(pid).getCwd();
                        file = new File(new StringBuffer().append(cwd).append(File.separator).append(file).toString());
                    } catch (SigarException e) {
                    }
                }
                if (file.exists()) {
                    JarFile jar = null;
                    try {
                        jar = new JarFile(file);
                        String value = jar.getManifest().getMainAttributes().getValue(Attributes.Name.MAIN_CLASS);
                        if (jar != null) {
                            try {
                                jar.close();
                            } catch (IOException e2) {
                            }
                        }
                        return value;
                    } catch (IOException e3) {
                        if (jar != null) {
                            try {
                                jar.close();
                            } catch (IOException e4) {
                            }
                        }
                    } catch (Throwable th) {
                        if (jar != null) {
                            try {
                                jar.close();
                            } catch (IOException e5) {
                            }
                        }
                        throw th;
                    }
                }
                return file.toString();
            }
        }
        return null;
    }

    public static String getDescription(SigarProxy sigar, long pid) throws SigarException {
        String[] args;
        ProcState state = sigar.getProcState(pid);
        String name = state.getName();
        try {
            args = sigar.getProcArgs(pid);
        } catch (SigarException e) {
            args = new String[0];
        }
        if (name.equals("java") || name.equals("javaw")) {
            String className = null;
            try {
                className = getJavaMainClass(sigar, pid);
            } catch (SigarException e2) {
            }
            if (className != null) {
                name = new StringBuffer().append(name).append(":").append(className).toString();
            }
        } else if (args.length != 0) {
            name = args[0];
        } else {
            try {
                String exe = sigar.getProcExe(pid).getName();
                if (exe.length() != 0) {
                    name = exe;
                }
            } catch (SigarException e3) {
            }
        }
        return name;
    }
}
