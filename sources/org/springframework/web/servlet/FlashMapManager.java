package org.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/FlashMapManager.class */
public interface FlashMapManager {
    FlashMap retrieveAndUpdate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    void saveOutputFlashMap(FlashMap flashMap, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
}
