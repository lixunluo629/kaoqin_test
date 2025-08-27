package org.springframework.boot.autoconfigure;

import java.util.Set;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/AutoConfigurationMetadata.class */
public interface AutoConfigurationMetadata {
    boolean wasProcessed(String str);

    Integer getInteger(String str, String str2);

    Integer getInteger(String str, String str2, Integer num);

    Set<String> getSet(String str, String str2);

    Set<String> getSet(String str, String str2, Set<String> set);

    String get(String str, String str2);

    String get(String str, String str2, String str3);
}
