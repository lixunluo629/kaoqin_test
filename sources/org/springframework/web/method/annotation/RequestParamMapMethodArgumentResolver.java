package org.springframework.web.method.annotation;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.core.MethodParameter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/method/annotation/RequestParamMapMethodArgumentResolver.class */
public class RequestParamMapMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override // org.springframework.web.method.support.HandlerMethodArgumentResolver
    public boolean supportsParameter(MethodParameter parameter) {
        RequestParam requestParam = (RequestParam) parameter.getParameterAnnotation(RequestParam.class);
        return (requestParam == null || !Map.class.isAssignableFrom(parameter.getParameterType()) || StringUtils.hasText(requestParam.name())) ? false : true;
    }

    @Override // org.springframework.web.method.support.HandlerMethodArgumentResolver
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Map<String, String[]> parameterMap = webRequest.getParameterMap();
        if (MultiValueMap.class.isAssignableFrom(parameter.getParameterType())) {
            MultiValueMap<String, String> result = new LinkedMultiValueMap<>(parameterMap.size());
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                for (String value : entry.getValue()) {
                    result.add(entry.getKey(), value);
                }
            }
            return result;
        }
        Map<String, String> result2 = new LinkedHashMap<>(parameterMap.size());
        for (Map.Entry<String, String[]> entry2 : parameterMap.entrySet()) {
            if (entry2.getValue().length > 0) {
                result2.put(entry2.getKey(), entry2.getValue()[0]);
            }
        }
        return result2;
    }
}
