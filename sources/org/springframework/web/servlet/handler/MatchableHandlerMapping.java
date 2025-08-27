package org.springframework.web.servlet.handler;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.HandlerMapping;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/handler/MatchableHandlerMapping.class */
public interface MatchableHandlerMapping extends HandlerMapping {
    RequestMatchResult match(HttpServletRequest httpServletRequest, String str);
}
