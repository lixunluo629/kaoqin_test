package org.springframework.boot;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/ExitCodeExceptionMapper.class */
public interface ExitCodeExceptionMapper {
    int getExitCode(Throwable th);
}
