package org.springframework.web.servlet.mvc.annotation;

import java.lang.reflect.Method;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.ModelAndView;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/annotation/ModelAndViewResolver.class */
public interface ModelAndViewResolver {
    public static final ModelAndView UNRESOLVED = new ModelAndView();

    ModelAndView resolveModelAndView(Method method, Class<?> cls, Object obj, ExtendedModelMap extendedModelMap, NativeWebRequest nativeWebRequest);
}
