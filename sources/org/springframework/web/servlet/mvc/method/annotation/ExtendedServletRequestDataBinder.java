package org.springframework.web.servlet.mvc.method.annotation;

import java.util.Map;
import javax.servlet.ServletRequest;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.HandlerMapping;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/ExtendedServletRequestDataBinder.class */
public class ExtendedServletRequestDataBinder extends ServletRequestDataBinder {
    public ExtendedServletRequestDataBinder(Object target) {
        super(target);
    }

    public ExtendedServletRequestDataBinder(Object target, String objectName) {
        super(target, objectName);
    }

    @Override // org.springframework.web.bind.ServletRequestDataBinder
    protected void addBindValues(MutablePropertyValues mpvs, ServletRequest request) {
        String attr = HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;
        Map<String, String> uriVars = (Map) request.getAttribute(attr);
        if (uriVars != null) {
            for (Map.Entry<String, String> entry : uriVars.entrySet()) {
                if (mpvs.contains(entry.getKey())) {
                    if (logger.isWarnEnabled()) {
                        logger.warn("Skipping URI variable '" + entry.getKey() + "' since the request contains a bind value with the same name.");
                    }
                } else {
                    mpvs.addPropertyValue(entry.getKey(), entry.getValue());
                }
            }
        }
    }
}
