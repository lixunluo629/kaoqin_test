package org.springframework.boot.autoconfigure.orm.jpa;

import org.springframework.util.ClassUtils;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/orm/jpa/HibernateVersion.class */
enum HibernateVersion {
    V4,
    V5;

    private static final String HIBERNATE_5_CLASS = "org.hibernate.boot.model.naming.PhysicalNamingStrategy";
    private static HibernateVersion running;

    public static HibernateVersion getRunning() {
        if (running == null) {
            setRunning(ClassUtils.isPresent(HIBERNATE_5_CLASS, null) ? V5 : V4);
        }
        return running;
    }

    static void setRunning(HibernateVersion running2) {
        running = running2;
    }
}
