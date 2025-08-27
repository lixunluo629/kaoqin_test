package org.springframework.web.servlet;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/View.class */
public interface View {
    public static final String RESPONSE_STATUS_ATTRIBUTE = View.class.getName() + ".responseStatus";
    public static final String PATH_VARIABLES = View.class.getName() + ".pathVariables";
    public static final String SELECTED_CONTENT_TYPE = View.class.getName() + ".selectedContentType";

    String getContentType();

    void render(Map<String, ?> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception;
}
