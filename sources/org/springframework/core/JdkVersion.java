package org.springframework.core;

@Deprecated
/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/JdkVersion.class */
public abstract class JdkVersion {
    public static final int JAVA_13 = 0;
    public static final int JAVA_14 = 1;
    public static final int JAVA_15 = 2;
    public static final int JAVA_16 = 3;
    public static final int JAVA_17 = 4;
    public static final int JAVA_18 = 5;
    public static final int JAVA_19 = 6;
    private static final String javaVersion = System.getProperty("java.version");
    private static final int majorJavaVersion;

    static {
        if (javaVersion.contains("1.9.")) {
            majorJavaVersion = 6;
            return;
        }
        if (javaVersion.contains("1.8.")) {
            majorJavaVersion = 5;
        } else if (javaVersion.contains("1.7.")) {
            majorJavaVersion = 4;
        } else {
            majorJavaVersion = 3;
        }
    }

    public static String getJavaVersion() {
        return javaVersion;
    }

    public static int getMajorJavaVersion() {
        return majorJavaVersion;
    }
}
