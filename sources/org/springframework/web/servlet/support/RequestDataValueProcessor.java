package org.springframework.web.servlet.support;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/support/RequestDataValueProcessor.class */
public interface RequestDataValueProcessor {
    String processAction(HttpServletRequest httpServletRequest, String str, String str2);

    String processFormFieldValue(HttpServletRequest httpServletRequest, String str, String str2, String str3);

    Map<String, String> getExtraHiddenFields(HttpServletRequest httpServletRequest);

    String processUrl(HttpServletRequest httpServletRequest, String str);
}
