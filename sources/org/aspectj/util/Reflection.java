package org.aspectj.util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/util/Reflection.class */
public class Reflection {
    public static final Class<?>[] MAIN_PARM_TYPES = {String[].class};

    private Reflection() {
    }

    public static Object invokestaticN(Class<?> class_, String name, Object[] args) {
        return invokeN(class_, name, null, args);
    }

    public static Object invoke(Class<?> class_, Object target, String name, Object arg1, Object arg2) {
        return invokeN(class_, name, target, new Object[]{arg1, arg2});
    }

    public static Object invoke(Class<?> class_, Object target, String name, Object arg1, Object arg2, Object arg3) {
        return invokeN(class_, name, target, new Object[]{arg1, arg2, arg3});
    }

    public static Object invokeN(Class<?> class_, String name, Object target, Object[] args) throws SecurityException {
        Method meth = getMatchingMethod(class_, name, args);
        try {
            return meth.invoke(target, args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.toString());
        } catch (InvocationTargetException e2) {
            Throwable t = e2.getTargetException();
            if (t instanceof Error) {
                throw ((Error) t);
            }
            if (t instanceof RuntimeException) {
                throw ((RuntimeException) t);
            }
            t.printStackTrace();
            throw new RuntimeException(t.toString());
        }
    }

    public static Method getMatchingMethod(Class<?> class_, String name, Object[] args) throws SecurityException {
        Method[] meths = class_.getMethods();
        for (Method meth : meths) {
            if (meth.getName().equals(name) && isCompatible(meth, args)) {
                return meth;
            }
        }
        return null;
    }

    private static boolean isCompatible(Method meth, Object[] args) {
        return meth.getParameterTypes().length == args.length;
    }

    public static Object getStaticField(Class<?> class_, String name) {
        try {
            return class_.getField(name).get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("unimplemented");
        } catch (NoSuchFieldException e2) {
            throw new RuntimeException("unimplemented");
        }
    }

    public static void runMainInSameVM(String classpath, String className, String[] args) throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException, SecurityException, IllegalArgumentException, InvocationTargetException {
        LangUtil.throwIaxIfNull(className, "class name");
        if (LangUtil.isEmpty(classpath)) {
            Class<?> mainClass = Class.forName(className);
            runMainInSameVM(mainClass, args);
            return;
        }
        ArrayList<File> dirs = new ArrayList<>();
        ArrayList<File> libs = new ArrayList<>();
        ArrayList<URL> urls = new ArrayList<>();
        String[] entries = LangUtil.splitClasspath(classpath);
        for (int i = 0; i < entries.length; i++) {
            String entry = entries[i];
            URL url = makeURL(entry);
            if (null != url) {
                urls.add(url);
            }
            File file = new File(entries[i]);
            if (FileUtil.isZipFile(file)) {
                libs.add(file);
            } else if (file.isDirectory()) {
                dirs.add(file);
            }
        }
        File[] dirRa = (File[]) dirs.toArray(new File[0]);
        File[] libRa = (File[]) libs.toArray(new File[0]);
        URL[] urlRa = (URL[]) urls.toArray(new URL[0]);
        runMainInSameVM(urlRa, libRa, dirRa, className, args);
    }

    public static void runMainInSameVM(URL[] urls, File[] libs, File[] dirs, String className, String[] args) throws IllegalAccessException, NoSuchMethodException, SecurityException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        LangUtil.throwIaxIfNull(className, "class name");
        LangUtil.throwIaxIfNotAssignable((Object[]) libs, (Class<?>) File.class, "jars");
        LangUtil.throwIaxIfNotAssignable((Object[]) dirs, (Class<?>) File.class, "dirs");
        URL[] libUrls = FileUtil.getFileURLs(libs);
        if (!LangUtil.isEmpty(libUrls)) {
            if (!LangUtil.isEmpty(urls)) {
                URL[] temp = new URL[libUrls.length + urls.length];
                System.arraycopy(urls, 0, temp, 0, urls.length);
                System.arraycopy(urls, 0, temp, libUrls.length, urls.length);
                urls = temp;
            } else {
                urls = libUrls;
            }
        }
        UtilClassLoader loader = new UtilClassLoader(urls, dirs);
        try {
            Class<?> targetClass = loader.loadClass(className);
            Method main = targetClass.getMethod("main", MAIN_PARM_TYPES);
            main.invoke(null, args);
        } catch (ClassNotFoundException e) {
            String s = "unable to load class " + className + " using class loader " + loader;
            throw new ClassNotFoundException(s);
        }
    }

    public static void runMainInSameVM(Class<?> mainClass, String[] args) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        LangUtil.throwIaxIfNull(mainClass, "main class");
        Method main = mainClass.getMethod("main", MAIN_PARM_TYPES);
        main.invoke(null, args);
    }

    private static URL makeURL(String s) {
        try {
            return new URL(s);
        } catch (Throwable th) {
            return null;
        }
    }
}
