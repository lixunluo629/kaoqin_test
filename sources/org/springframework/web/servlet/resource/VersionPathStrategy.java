package org.springframework.web.servlet.resource;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/resource/VersionPathStrategy.class */
public interface VersionPathStrategy {
    String extractVersion(String str);

    String removeVersion(String str, String str2);

    String addVersion(String str, String str2);
}
