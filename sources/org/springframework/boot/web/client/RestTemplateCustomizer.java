package org.springframework.boot.web.client;

import org.springframework.web.client.RestTemplate;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/web/client/RestTemplateCustomizer.class */
public interface RestTemplateCustomizer {
    void customize(RestTemplate restTemplate);
}
