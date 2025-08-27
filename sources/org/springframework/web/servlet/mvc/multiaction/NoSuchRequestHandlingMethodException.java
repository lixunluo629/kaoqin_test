package org.springframework.web.servlet.mvc.multiaction;

import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.style.StylerUtils;
import org.springframework.web.util.UrlPathHelper;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/multiaction/NoSuchRequestHandlingMethodException.class */
public class NoSuchRequestHandlingMethodException extends ServletException {
    private String methodName;

    public NoSuchRequestHandlingMethodException(HttpServletRequest request) {
        this(new UrlPathHelper().getRequestUri(request), request.getMethod(), request.getParameterMap());
    }

    public NoSuchRequestHandlingMethodException(String urlPath, String method, Map<String, String[]> parameterMap) {
        super("No matching handler method found for servlet request: path '" + urlPath + "', method '" + method + "', parameters " + StylerUtils.style(parameterMap));
    }

    public NoSuchRequestHandlingMethodException(String methodName, Class<?> controllerClass) {
        super("No request handling method with name '" + methodName + "' in class [" + controllerClass.getName() + "]");
        this.methodName = methodName;
    }

    public String getMethodName() {
        return this.methodName;
    }
}
