package org.springframework.web.servlet.mvc.annotation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.core.ExceptionDepthComparator;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.http.converter.xml.XmlAwareFormHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/annotation/AnnotationMethodHandlerExceptionResolver.class */
public class AnnotationMethodHandlerExceptionResolver extends AbstractHandlerExceptionResolver {
    private static final Method NO_METHOD_FOUND = ClassUtils.getMethodIfAvailable(System.class, "currentTimeMillis", new Class[0]);
    private WebArgumentResolver[] customArgumentResolvers;
    private final Map<Class<?>, Map<Class<? extends Throwable>, Method>> exceptionHandlerCache = new ConcurrentHashMap(64);
    private HttpMessageConverter<?>[] messageConverters = {new ByteArrayHttpMessageConverter(), new StringHttpMessageConverter(), new SourceHttpMessageConverter(), new XmlAwareFormHttpMessageConverter()};

    public void setCustomArgumentResolver(WebArgumentResolver argumentResolver) {
        this.customArgumentResolvers = new WebArgumentResolver[]{argumentResolver};
    }

    public void setCustomArgumentResolvers(WebArgumentResolver[] argumentResolvers) {
        this.customArgumentResolvers = argumentResolvers;
    }

    public void setMessageConverters(HttpMessageConverter<?>[] messageConverters) {
        this.messageConverters = messageConverters;
    }

    @Override // org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Method handlerMethod;
        if (handler != null && (handlerMethod = findBestExceptionHandlerMethod(handler, ex)) != null) {
            ServletWebRequest webRequest = new ServletWebRequest(request, response);
            try {
                Object[] args = resolveHandlerArguments(handlerMethod, handler, webRequest, ex);
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Invoking request handler method: " + handlerMethod);
                }
                Object retVal = doInvokeMethod(handlerMethod, handler, args);
                return getModelAndView(handlerMethod, retVal, webRequest);
            } catch (Exception invocationEx) {
                this.logger.error("Invoking request method resulted in exception : " + handlerMethod, invocationEx);
                return null;
            }
        }
        return null;
    }

    private Method findBestExceptionHandlerMethod(Object handler, Exception thrownException) throws SecurityException, IllegalArgumentException {
        final Class<?> handlerType = ClassUtils.getUserClass(handler);
        final Class<?> cls = thrownException.getClass();
        Map<Class<? extends Throwable>, Method> handlers = this.exceptionHandlerCache.get(handlerType);
        if (handlers != null) {
            Method handlerMethod = handlers.get(cls);
            if (handlerMethod != null) {
                if (handlerMethod == NO_METHOD_FOUND) {
                    return null;
                }
                return handlerMethod;
            }
        } else {
            handlers = new ConcurrentHashMap(16);
            this.exceptionHandlerCache.put(handlerType, handlers);
        }
        final Map<Class<? extends Throwable>, Method> matchedHandlers = new HashMap<>();
        ReflectionUtils.doWithMethods(handlerType, new ReflectionUtils.MethodCallback() { // from class: org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerExceptionResolver.1
            @Override // org.springframework.util.ReflectionUtils.MethodCallback
            public void doWith(Method method) {
                Method method2 = ClassUtils.getMostSpecificMethod(method, handlerType);
                List<Class<? extends Throwable>> handledExceptions = AnnotationMethodHandlerExceptionResolver.this.getHandledExceptions(method2);
                for (Class<? extends Throwable> handledException : handledExceptions) {
                    if (handledException.isAssignableFrom(cls)) {
                        if (!matchedHandlers.containsKey(handledException)) {
                            matchedHandlers.put(handledException, method2);
                        } else {
                            Method oldMappedMethod = (Method) matchedHandlers.get(handledException);
                            if (!oldMappedMethod.equals(method2)) {
                                throw new IllegalStateException("Ambiguous exception handler mapped for " + handledException + "]: {" + oldMappedMethod + ", " + method2 + "}.");
                            }
                        }
                    }
                }
            }
        });
        Method handlerMethod2 = getBestMatchingMethod(matchedHandlers, thrownException);
        handlers.put(cls, handlerMethod2 == null ? NO_METHOD_FOUND : handlerMethod2);
        return handlerMethod2;
    }

    protected List<Class<? extends Throwable>> getHandledExceptions(Method method) {
        ArrayList arrayList = new ArrayList();
        ExceptionHandler exceptionHandler = (ExceptionHandler) AnnotationUtils.findAnnotation(method, ExceptionHandler.class);
        if (exceptionHandler != null) {
            if (!ObjectUtils.isEmpty((Object[]) exceptionHandler.value())) {
                arrayList.addAll(Arrays.asList(exceptionHandler.value()));
            } else {
                for (Class<?> param : method.getParameterTypes()) {
                    if (Throwable.class.isAssignableFrom(param)) {
                        arrayList.add(param);
                    }
                }
            }
        }
        return arrayList;
    }

    private Method getBestMatchingMethod(Map<Class<? extends Throwable>, Method> resolverMethods, Exception thrownException) {
        if (resolverMethods.isEmpty()) {
            return null;
        }
        Class<? extends Throwable> closestMatch = ExceptionDepthComparator.findClosestMatch(resolverMethods.keySet(), thrownException);
        Method method = resolverMethods.get(closestMatch);
        if (method == null || NO_METHOD_FOUND == method) {
            return null;
        }
        return method;
    }

    private Object[] resolveHandlerArguments(Method handlerMethod, Object handler, NativeWebRequest webRequest, Exception thrownException) throws Exception {
        Class<?>[] paramTypes = handlerMethod.getParameterTypes();
        Object[] args = new Object[paramTypes.length];
        Class<?> handlerType = handler.getClass();
        for (int i = 0; i < args.length; i++) {
            MethodParameter methodParam = new SynthesizingMethodParameter(handlerMethod, i);
            GenericTypeResolver.resolveParameterType(methodParam, handlerType);
            Class<?> paramType = methodParam.getParameterType();
            Object argValue = resolveCommonArgument(methodParam, webRequest, thrownException);
            if (argValue != WebArgumentResolver.UNRESOLVED) {
                args[i] = argValue;
            } else {
                throw new IllegalStateException("Unsupported argument [" + paramType.getName() + "] for @ExceptionHandler method: " + handlerMethod);
            }
        }
        return args;
    }

    protected Object resolveCommonArgument(MethodParameter methodParameter, NativeWebRequest webRequest, Exception thrownException) throws Exception {
        if (this.customArgumentResolvers != null) {
            for (WebArgumentResolver argumentResolver : this.customArgumentResolvers) {
                Object value = argumentResolver.resolveArgument(methodParameter, webRequest);
                if (value != WebArgumentResolver.UNRESOLVED) {
                    return value;
                }
            }
        }
        Class<?> paramType = methodParameter.getParameterType();
        Object value2 = resolveStandardArgument(paramType, webRequest, thrownException);
        if (value2 != WebArgumentResolver.UNRESOLVED && !ClassUtils.isAssignableValue(paramType, value2)) {
            throw new IllegalStateException("Standard argument type [" + paramType.getName() + "] resolved to incompatible value of type [" + (value2 != null ? value2.getClass() : null) + "]. Consider declaring the argument type in a less specific fashion.");
        }
        return value2;
    }

    protected Object resolveStandardArgument(Class<?> parameterType, NativeWebRequest webRequest, Exception thrownException) throws Exception {
        if (parameterType.isInstance(thrownException)) {
            return thrownException;
        }
        if (WebRequest.class.isAssignableFrom(parameterType)) {
            return webRequest;
        }
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = (HttpServletResponse) webRequest.getNativeResponse(HttpServletResponse.class);
        if (ServletRequest.class.isAssignableFrom(parameterType)) {
            return request;
        }
        if (ServletResponse.class.isAssignableFrom(parameterType)) {
            return response;
        }
        if (HttpSession.class.isAssignableFrom(parameterType)) {
            return request.getSession();
        }
        if (Principal.class.isAssignableFrom(parameterType)) {
            return request.getUserPrincipal();
        }
        if (Locale.class == parameterType) {
            return RequestContextUtils.getLocale(request);
        }
        if (InputStream.class.isAssignableFrom(parameterType)) {
            return request.getInputStream();
        }
        if (Reader.class.isAssignableFrom(parameterType)) {
            return request.getReader();
        }
        if (OutputStream.class.isAssignableFrom(parameterType)) {
            return response.getOutputStream();
        }
        if (Writer.class.isAssignableFrom(parameterType)) {
            return response.getWriter();
        }
        return WebArgumentResolver.UNRESOLVED;
    }

    private Object doInvokeMethod(Method method, Object target, Object[] args) throws Exception {
        ReflectionUtils.makeAccessible(method);
        try {
            return method.invoke(target, args);
        } catch (InvocationTargetException ex) {
            ReflectionUtils.rethrowException(ex.getTargetException());
            throw new IllegalStateException("Should never get here");
        }
    }

    private ModelAndView getModelAndView(Method handlerMethod, Object returnValue, ServletWebRequest webRequest) throws Exception {
        ResponseStatus responseStatus = (ResponseStatus) AnnotatedElementUtils.findMergedAnnotation(handlerMethod, ResponseStatus.class);
        if (responseStatus != null) {
            HttpStatus statusCode = responseStatus.code();
            String reason = responseStatus.reason();
            if (!StringUtils.hasText(reason)) {
                webRequest.getResponse().setStatus(statusCode.value());
            } else {
                webRequest.getResponse().sendError(statusCode.value(), reason);
            }
        }
        if (returnValue != null && AnnotationUtils.findAnnotation(handlerMethod, ResponseBody.class) != null) {
            return handleResponseBody(returnValue, webRequest);
        }
        if (returnValue instanceof ModelAndView) {
            return (ModelAndView) returnValue;
        }
        if (returnValue instanceof Model) {
            return new ModelAndView().addAllObjects(((Model) returnValue).asMap());
        }
        if (returnValue instanceof Map) {
            return new ModelAndView().addAllObjects((Map) returnValue);
        }
        if (returnValue instanceof View) {
            return new ModelAndView((View) returnValue);
        }
        if (returnValue instanceof String) {
            return new ModelAndView((String) returnValue);
        }
        if (returnValue == null) {
            return new ModelAndView();
        }
        throw new IllegalArgumentException("Invalid handler method return value: " + returnValue);
    }

    private ModelAndView handleResponseBody(Object returnValue, ServletWebRequest webRequest) throws ServletException, IOException, HttpMessageNotWritableException {
        HttpInputMessage inputMessage = new ServletServerHttpRequest(webRequest.getRequest());
        List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
        if (acceptedMediaTypes.isEmpty()) {
            acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
        }
        MediaType.sortByQualityValue(acceptedMediaTypes);
        HttpOutputMessage outputMessage = new ServletServerHttpResponse(webRequest.getResponse());
        Class<?> returnValueType = returnValue.getClass();
        if (this.messageConverters != null) {
            for (MediaType acceptedMediaType : acceptedMediaTypes) {
                for (HttpMessageConverter messageConverter : this.messageConverters) {
                    if (messageConverter.canWrite(returnValueType, acceptedMediaType)) {
                        messageConverter.write(returnValue, acceptedMediaType, outputMessage);
                        return new ModelAndView();
                    }
                }
            }
        }
        if (this.logger.isWarnEnabled()) {
            this.logger.warn("Could not find HttpMessageConverter that supports return type [" + returnValueType + "] and " + acceptedMediaTypes);
            return null;
        }
        return null;
    }
}
