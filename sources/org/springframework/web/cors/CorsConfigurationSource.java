package org.springframework.web.cors;

import javax.servlet.http.HttpServletRequest;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/cors/CorsConfigurationSource.class */
public interface CorsConfigurationSource {
    CorsConfiguration getCorsConfiguration(HttpServletRequest httpServletRequest);
}
