package org.springframework.core;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/SpringVersion.class */
public class SpringVersion {
    public static String getVersion() {
        Package pkg = SpringVersion.class.getPackage();
        if (pkg != null) {
            return pkg.getImplementationVersion();
        }
        return null;
    }
}
