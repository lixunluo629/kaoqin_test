package org.springframework.boot;

import java.io.PrintStream;
import org.springframework.core.env.Environment;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/Banner.class */
public interface Banner {

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/Banner$Mode.class */
    public enum Mode {
        OFF,
        CONSOLE,
        LOG
    }

    void printBanner(Environment environment, Class<?> cls, PrintStream printStream);
}
