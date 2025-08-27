package org.springframework.web.servlet.mvc.method.annotation;

import java.io.InputStream;
import java.io.Reader;
import java.security.Principal;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.lang.UsesJava8;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.support.RequestContextUtils;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/ServletRequestMethodArgumentResolver.class */
public class ServletRequestMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override // org.springframework.web.method.support.HandlerMethodArgumentResolver
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> paramType = parameter.getParameterType();
        return WebRequest.class.isAssignableFrom(paramType) || ServletRequest.class.isAssignableFrom(paramType) || MultipartRequest.class.isAssignableFrom(paramType) || HttpSession.class.isAssignableFrom(paramType) || Principal.class.isAssignableFrom(paramType) || InputStream.class.isAssignableFrom(paramType) || Reader.class.isAssignableFrom(paramType) || HttpMethod.class == paramType || Locale.class == paramType || TimeZone.class == paramType || "java.time.ZoneId".equals(paramType.getName());
    }

    @Override // org.springframework.web.method.support.HandlerMethodArgumentResolver
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Class<?> paramType = parameter.getParameterType();
        if (WebRequest.class.isAssignableFrom(paramType)) {
            if (!paramType.isInstance(webRequest)) {
                throw new IllegalStateException("Current request is not of type [" + paramType.getName() + "]: " + webRequest);
            }
            return webRequest;
        }
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest(HttpServletRequest.class);
        if (ServletRequest.class.isAssignableFrom(paramType) || MultipartRequest.class.isAssignableFrom(paramType)) {
            Object nativeRequest = webRequest.getNativeRequest(paramType);
            if (nativeRequest == null) {
                throw new IllegalStateException("Current request is not of type [" + paramType.getName() + "]: " + request);
            }
            return nativeRequest;
        }
        if (HttpSession.class.isAssignableFrom(paramType)) {
            HttpSession session = request.getSession();
            if (session != null && !paramType.isInstance(session)) {
                throw new IllegalStateException("Current session is not of type [" + paramType.getName() + "]: " + session);
            }
            return session;
        }
        if (InputStream.class.isAssignableFrom(paramType)) {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null && !paramType.isInstance(inputStream)) {
                throw new IllegalStateException("Request input stream is not of type [" + paramType.getName() + "]: " + inputStream);
            }
            return inputStream;
        }
        if (Reader.class.isAssignableFrom(paramType)) {
            Reader reader = request.getReader();
            if (reader != null && !paramType.isInstance(reader)) {
                throw new IllegalStateException("Request body reader is not of type [" + paramType.getName() + "]: " + reader);
            }
            return reader;
        }
        if (Principal.class.isAssignableFrom(paramType)) {
            Principal userPrincipal = request.getUserPrincipal();
            if (userPrincipal != null && !paramType.isInstance(userPrincipal)) {
                throw new IllegalStateException("Current user principal is not of type [" + paramType.getName() + "]: " + userPrincipal);
            }
            return userPrincipal;
        }
        if (HttpMethod.class == paramType) {
            return HttpMethod.resolve(request.getMethod());
        }
        if (Locale.class == paramType) {
            return RequestContextUtils.getLocale(request);
        }
        if (TimeZone.class == paramType) {
            TimeZone timeZone = RequestContextUtils.getTimeZone(request);
            return timeZone != null ? timeZone : TimeZone.getDefault();
        }
        if ("java.time.ZoneId".equals(paramType.getName())) {
            return ZoneIdResolver.resolveZoneId(request);
        }
        throw new UnsupportedOperationException("Unknown parameter type [" + paramType.getName() + "] in " + parameter.getMethod());
    }

    @UsesJava8
    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/ServletRequestMethodArgumentResolver$ZoneIdResolver.class */
    private static class ZoneIdResolver {
        private ZoneIdResolver() {
        }

        public static Object resolveZoneId(HttpServletRequest request) {
            TimeZone timeZone = RequestContextUtils.getTimeZone(request);
            return timeZone != null ? timeZone.toZoneId() : ZoneId.systemDefault();
        }
    }
}
