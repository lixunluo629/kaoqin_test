package org.springframework.boot.admin;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/admin/SpringApplicationAdminMXBean.class */
public interface SpringApplicationAdminMXBean {
    boolean isReady();

    boolean isEmbeddedWebApplication();

    String getProperty(String str);

    void shutdown();
}
