package org.hyperic.sigar;

import org.hyperic.jni.ArchLoader;
import org.hyperic.jni.ArchLoaderException;
import org.hyperic.jni.ArchName;
import org.hyperic.jni.ArchNotSupportedException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/SigarLoader.class */
public class SigarLoader extends ArchLoader {
    public static final String PROP_SIGAR_JAR_NAME = "sigar.jar.name";
    private static String location = null;
    private static String nativeName = null;
    static Class class$org$hyperic$sigar$Sigar;

    public SigarLoader(Class loaderClass) {
        super(loaderClass);
    }

    @Override // org.hyperic.jni.ArchLoader
    public String getArchLibName() throws ArchNotSupportedException {
        return new StringBuffer().append(getName()).append("-").append(ArchName.getName()).toString();
    }

    @Override // org.hyperic.jni.ArchLoader
    public String getDefaultLibName() throws ArchNotSupportedException {
        return getArchLibName();
    }

    @Override // org.hyperic.jni.ArchLoader
    protected void systemLoadLibrary(String name) {
        System.loadLibrary(name);
    }

    @Override // org.hyperic.jni.ArchLoader
    protected void systemLoad(String name) {
        System.load(name);
    }

    @Override // org.hyperic.jni.ArchLoader
    public String getJarName() {
        return System.getProperty(PROP_SIGAR_JAR_NAME, super.getJarName());
    }

    public static void setSigarJarName(String jarName) {
        System.setProperty(PROP_SIGAR_JAR_NAME, jarName);
    }

    public static String getSigarJarName() {
        return System.getProperty(PROP_SIGAR_JAR_NAME);
    }

    public static synchronized String getLocation() throws Throwable {
        Class clsClass$;
        if (location == null) {
            if (class$org$hyperic$sigar$Sigar == null) {
                clsClass$ = class$("org.hyperic.sigar.Sigar");
                class$org$hyperic$sigar$Sigar = clsClass$;
            } else {
                clsClass$ = class$org$hyperic$sigar$Sigar;
            }
            SigarLoader loader = new SigarLoader(clsClass$);
            try {
                location = loader.findJarPath(getSigarJarName());
            } catch (ArchLoaderException e) {
                location = ".";
            }
        }
        return location;
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    public static synchronized String getNativeLibraryName() throws Throwable {
        Class clsClass$;
        if (nativeName == null) {
            if (class$org$hyperic$sigar$Sigar == null) {
                clsClass$ = class$("org.hyperic.sigar.Sigar");
                class$org$hyperic$sigar$Sigar = clsClass$;
            } else {
                clsClass$ = class$org$hyperic$sigar$Sigar;
            }
            SigarLoader loader = new SigarLoader(clsClass$);
            try {
                nativeName = loader.getLibraryName();
            } catch (ArchNotSupportedException e) {
                nativeName = null;
            }
        }
        return nativeName;
    }
}
