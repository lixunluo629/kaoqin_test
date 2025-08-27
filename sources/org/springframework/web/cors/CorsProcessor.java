package org.springframework.web.cors;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/cors/CorsProcessor.class */
public interface CorsProcessor {
    boolean processRequest(CorsConfiguration corsConfiguration, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException;
}
