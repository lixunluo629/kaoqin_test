package org.hyperic.sigar.cmd;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarLoader;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/Runner.class */
public class Runner {
    private static HashMap wantedJars = new HashMap();
    private static final String JAR_EXT = ".jar";
    static Class class$java$net$URL;
    static Class class$java$net$URLClassLoader;
    static Class array$Ljava$lang$String;

    static {
        wantedJars.put("junit", Boolean.FALSE);
        wantedJars.put("log4j", Boolean.FALSE);
    }

    private static void printMissingJars() {
        for (Map.Entry entry : wantedJars.entrySet()) {
            String jar = (String) entry.getKey();
            if (wantedJars.get(jar) == Boolean.FALSE) {
                System.out.println(new StringBuffer().append("Unable to locate: ").append(jar).append(".jar").toString());
            }
        }
    }

    private static boolean missingJars() {
        for (Map.Entry entry : wantedJars.entrySet()) {
            String jar = (String) entry.getKey();
            if (wantedJars.get(jar) == Boolean.FALSE) {
                return true;
            }
        }
        return false;
    }

    public static URL[] getLibJars(String dir) throws Exception {
        File[] jars = new File(dir).listFiles(new FileFilter() { // from class: org.hyperic.sigar.cmd.Runner.1
            @Override // java.io.FileFilter
            public boolean accept(File file) {
                String name;
                String name2 = file.getName();
                int jarIx = name2.indexOf(".jar");
                if (jarIx == -1) {
                    return false;
                }
                int ix = name2.indexOf(45);
                if (ix != -1) {
                    name = name2.substring(0, ix);
                } else {
                    name = name2.substring(0, jarIx);
                }
                if (Runner.wantedJars.get(name) != null) {
                    Runner.wantedJars.put(name, Boolean.TRUE);
                    return true;
                }
                return false;
            }
        });
        if (jars == null) {
            return new URL[0];
        }
        URL[] urls = new URL[jars.length];
        for (int i = 0; i < jars.length; i++) {
            URL url = new URL("jar", (String) null, new StringBuffer().append(ResourceUtils.FILE_URL_PREFIX).append(jars[i].getAbsolutePath()).append(ResourceUtils.JAR_URL_SEPARATOR).toString());
            urls[i] = url;
        }
        return urls;
    }

    private static void addURLs(URL[] jars) throws Exception {
        Class clsClass$;
        Class<?> clsClass$2;
        URLClassLoader loader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
        if (class$java$net$URLClassLoader == null) {
            clsClass$ = class$("java.net.URLClassLoader");
            class$java$net$URLClassLoader = clsClass$;
        } else {
            clsClass$ = class$java$net$URLClassLoader;
        }
        Class<?>[] clsArr = new Class[1];
        if (class$java$net$URL == null) {
            clsClass$2 = class$("java.net.URL");
            class$java$net$URL = clsClass$2;
        } else {
            clsClass$2 = class$java$net$URL;
        }
        clsArr[0] = clsClass$2;
        Method addURL = clsClass$.getDeclaredMethod("addURL", clsArr);
        addURL.setAccessible(true);
        for (URL url : jars) {
            addURL.invoke(loader, url);
        }
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    private static boolean addJarDir(String dir) throws Exception {
        URL[] jars = getLibJars(dir);
        addURLs(jars);
        return !missingJars();
    }

    private static String getenv(String key) {
        try {
            return System.getenv("ANT_HOME");
        } catch (Error e) {
            Sigar sigar = new Sigar();
            try {
                String procEnv = sigar.getProcEnv(ClassUtils.CGLIB_CLASS_SEPARATOR, "ANT_HOME");
                sigar.close();
                return procEnv;
            } catch (Exception e2) {
                sigar.close();
                return null;
            } catch (Throwable th) {
                sigar.close();
                throw th;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Class<?> clsClass$;
        String home;
        if (args.length < 1) {
            args = new String[]{"Shell"};
        } else if (Character.isLowerCase(args[0].charAt(0))) {
            String[] nargs = new String[args.length + 1];
            System.arraycopy(args, 0, nargs, 1, args.length);
            nargs[0] = "Shell";
            args = nargs;
        }
        String name = args[0];
        String[] pargs = new String[args.length - 1];
        System.arraycopy(args, 1, pargs, 0, args.length - 1);
        String sigarLib = SigarLoader.getLocation();
        String[] dirs = {sigarLib, "lib", "."};
        for (int i = 0; i < dirs.length && !addJarDir(dirs[i]); i++) {
        }
        if (missingJars()) {
            File[] subdirs = new File(".").listFiles(new FileFilter() { // from class: org.hyperic.sigar.cmd.Runner.2
                @Override // java.io.FileFilter
                public boolean accept(File file) {
                    return file.isDirectory();
                }
            });
            for (File file : subdirs) {
                File lib = new File(file, "lib");
                if (lib.exists() && addJarDir(lib.getAbsolutePath())) {
                    break;
                }
            }
            if (missingJars() && (home = getenv("ANT_HOME")) != null) {
                addJarDir(new StringBuffer().append(home).append("/lib").toString());
            }
        }
        Class cmd = null;
        String[] packages = {"org.hyperic.sigar.cmd.", "org.hyperic.sigar.test.", "org.hyperic.sigar.", "org.hyperic.sigar.win32.", "org.hyperic.sigar.jmx."};
        for (String str : packages) {
            try {
                cmd = Class.forName(new StringBuffer().append(str).append(name).toString());
                break;
            } catch (ClassNotFoundException e) {
            }
        }
        if (cmd == null) {
            System.out.println(new StringBuffer().append("Unknown command: ").append(args[0]).toString());
            return;
        }
        Class cls = cmd;
        Class<?>[] clsArr = new Class[1];
        if (array$Ljava$lang$String == null) {
            clsClass$ = class$("[Ljava.lang.String;");
            array$Ljava$lang$String = clsClass$;
        } else {
            clsClass$ = array$Ljava$lang$String;
        }
        clsArr[0] = clsClass$;
        Method main = cls.getMethod("main", clsArr);
        try {
            main.invoke(null, pargs);
        } catch (InvocationTargetException e2) {
            Throwable t = e2.getTargetException();
            if (t instanceof NoClassDefFoundError) {
                System.out.println(new StringBuffer().append("Class Not Found: ").append(t.getMessage()).toString());
                printMissingJars();
            } else {
                t.printStackTrace();
            }
        }
    }
}
