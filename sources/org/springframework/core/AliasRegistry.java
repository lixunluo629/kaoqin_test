package org.springframework.core;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/AliasRegistry.class */
public interface AliasRegistry {
    void registerAlias(String str, String str2);

    void removeAlias(String str);

    boolean isAlias(String str);

    String[] getAliases(String str);
}
