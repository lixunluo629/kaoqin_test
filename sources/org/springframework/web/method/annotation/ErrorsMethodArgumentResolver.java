package org.springframework.web.method.annotation;

import java.util.ArrayList;
import org.springframework.core.MethodParameter;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/method/annotation/ErrorsMethodArgumentResolver.class */
public class ErrorsMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override // org.springframework.web.method.support.HandlerMethodArgumentResolver
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> paramType = parameter.getParameterType();
        return Errors.class.isAssignableFrom(paramType);
    }

    @Override // org.springframework.web.method.support.HandlerMethodArgumentResolver
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        ModelMap model = mavContainer.getModel();
        if (model.size() > 0) {
            int lastIndex = model.size() - 1;
            String lastKey = (String) new ArrayList(model.keySet()).get(lastIndex);
            if (lastKey.startsWith(BindingResult.MODEL_KEY_PREFIX)) {
                return model.get(lastKey);
            }
        }
        throw new IllegalStateException("An Errors/BindingResult argument is expected to be declared immediately after the model attribute, the @RequestBody or the @RequestPart arguments to which they apply: " + parameter.getMethod());
    }
}
