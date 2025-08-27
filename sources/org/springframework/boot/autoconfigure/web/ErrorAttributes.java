package org.springframework.boot.autoconfigure.web;

import java.util.Map;
import org.springframework.web.context.request.RequestAttributes;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/ErrorAttributes.class */
public interface ErrorAttributes {
    Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean z);

    Throwable getError(RequestAttributes requestAttributes);
}
