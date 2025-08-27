package org.springframework.web.bind;

import javax.servlet.ServletRequest;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.web.util.WebUtils;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/bind/ServletRequestParameterPropertyValues.class */
public class ServletRequestParameterPropertyValues extends MutablePropertyValues {
    public static final String DEFAULT_PREFIX_SEPARATOR = "_";

    public ServletRequestParameterPropertyValues(ServletRequest request) {
        this(request, null, null);
    }

    public ServletRequestParameterPropertyValues(ServletRequest request, String prefix) {
        this(request, prefix, "_");
    }

    public ServletRequestParameterPropertyValues(ServletRequest request, String prefix, String prefixSeparator) {
        super(WebUtils.getParametersStartingWith(request, prefix != null ? prefix + prefixSeparator : null));
    }
}
