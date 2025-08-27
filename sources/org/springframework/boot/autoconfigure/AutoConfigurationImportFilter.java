package org.springframework.boot.autoconfigure;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/AutoConfigurationImportFilter.class */
public interface AutoConfigurationImportFilter {
    boolean[] match(String[] strArr, AutoConfigurationMetadata autoConfigurationMetadata);
}
