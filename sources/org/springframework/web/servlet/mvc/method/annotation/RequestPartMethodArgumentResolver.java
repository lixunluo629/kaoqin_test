package org.springframework.web.servlet.mvc.method.annotation;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.multipart.support.MultipartResolutionDelegate;
import org.springframework.web.multipart.support.RequestPartServletServerHttpRequest;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/RequestPartMethodArgumentResolver.class */
public class RequestPartMethodArgumentResolver extends AbstractMessageConverterMethodArgumentResolver {
    public RequestPartMethodArgumentResolver(List<HttpMessageConverter<?>> messageConverters) {
        super(messageConverters);
    }

    public RequestPartMethodArgumentResolver(List<HttpMessageConverter<?>> messageConverters, List<Object> requestResponseBodyAdvice) {
        super(messageConverters, requestResponseBodyAdvice);
    }

    @Override // org.springframework.web.method.support.HandlerMethodArgumentResolver
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(RequestPart.class)) {
            return true;
        }
        if (parameter.hasParameterAnnotation(RequestParam.class)) {
            return false;
        }
        return MultipartResolutionDelegate.isMultipartArgument(parameter.nestedIfOptional());
    }

    @Override // org.springframework.web.method.support.HandlerMethodArgumentResolver
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest request, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest servletRequest = (HttpServletRequest) request.getNativeRequest(HttpServletRequest.class);
        RequestPart requestPart = (RequestPart) parameter.getParameterAnnotation(RequestPart.class);
        boolean isRequired = (requestPart == null || requestPart.required()) && !parameter.isOptional();
        String name = getPartName(parameter, requestPart);
        MethodParameter parameter2 = parameter.nestedIfOptional();
        Object arg = null;
        Object mpArg = MultipartResolutionDelegate.resolveMultipartArgument(name, parameter2, servletRequest);
        if (mpArg != MultipartResolutionDelegate.UNRESOLVABLE) {
            arg = mpArg;
        } else {
            try {
                HttpInputMessage inputMessage = new RequestPartServletServerHttpRequest(servletRequest, name);
                arg = readWithMessageConverters(inputMessage, parameter2, parameter2.getNestedGenericParameterType());
                WebDataBinder binder = binderFactory.createBinder(request, arg, name);
                if (arg != null) {
                    validateIfApplicable(binder, parameter2);
                    if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter2)) {
                        throw new MethodArgumentNotValidException(parameter2, binder.getBindingResult());
                    }
                }
                mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
            } catch (MultipartException ex) {
                if (isRequired) {
                    throw ex;
                }
            } catch (MissingServletRequestPartException ex2) {
                if (isRequired) {
                    throw ex2;
                }
            }
        }
        if (arg == null && isRequired) {
            if (!MultipartResolutionDelegate.isMultipartRequest(servletRequest)) {
                throw new MultipartException("Current request is not a multipart request");
            }
            throw new MissingServletRequestPartException(name);
        }
        return adaptArgumentIfNecessary(arg, parameter2);
    }

    private String getPartName(MethodParameter methodParam, RequestPart requestPart) {
        String partName = requestPart != null ? requestPart.name() : "";
        if (partName.isEmpty()) {
            partName = methodParam.getParameterName();
            if (partName == null) {
                throw new IllegalArgumentException("Request part name for argument type [" + methodParam.getNestedParameterType().getName() + "] not specified, and parameter name information not found in class file either.");
            }
        }
        return partName;
    }
}
