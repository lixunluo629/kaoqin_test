package org.springframework.boot;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/SpringBootVersion.class */
public final class SpringBootVersion {
    private SpringBootVersion() {
    }

    public static String getVersion() {
        Package pkg = SpringApplication.class.getPackage();
        if (pkg != null) {
            return pkg.getImplementationVersion();
        }
        return null;
    }
}
