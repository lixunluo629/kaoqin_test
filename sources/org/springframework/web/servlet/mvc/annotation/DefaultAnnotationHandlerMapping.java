package org.springframework.web.servlet.mvc.annotation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.handler.AbstractDetectingUrlHandlerMapping;
import org.springframework.web.servlet.mvc.Controller;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/annotation/DefaultAnnotationHandlerMapping.class */
public class DefaultAnnotationHandlerMapping extends AbstractDetectingUrlHandlerMapping {
    static final String USE_DEFAULT_SUFFIX_PATTERN = DefaultAnnotationHandlerMapping.class.getName() + ".useDefaultSuffixPattern";
    private boolean useDefaultSuffixPattern = true;
    private final Map<Class<?>, RequestMapping> cachedMappings = new HashMap();

    public void setUseDefaultSuffixPattern(boolean useDefaultSuffixPattern) {
        this.useDefaultSuffixPattern = useDefaultSuffixPattern;
    }

    @Override // org.springframework.web.servlet.handler.AbstractDetectingUrlHandlerMapping
    protected String[] determineUrlsForHandler(String beanName) throws SecurityException, IllegalArgumentException {
        ApplicationContext context = getApplicationContext();
        Class<?> handlerType = context.getType(beanName);
        RequestMapping mapping = (RequestMapping) context.findAnnotationOnBean(beanName, RequestMapping.class);
        if (mapping != null) {
            this.cachedMappings.put(handlerType, mapping);
            Set<String> urls = new LinkedHashSet<>();
            String[] typeLevelPatterns = mapping.value();
            if (typeLevelPatterns.length > 0) {
                String[] methodLevelPatterns = determineUrlsForHandlerMethods(handlerType, true);
                for (String typeLevelPattern : typeLevelPatterns) {
                    if (!typeLevelPattern.startsWith("/")) {
                        typeLevelPattern = "/" + typeLevelPattern;
                    }
                    boolean hasEmptyMethodLevelMappings = false;
                    for (String methodLevelPattern : methodLevelPatterns) {
                        if (methodLevelPattern == null) {
                            hasEmptyMethodLevelMappings = true;
                        } else {
                            String combinedPattern = getPathMatcher().combine(typeLevelPattern, methodLevelPattern);
                            addUrlsForPath(urls, combinedPattern);
                        }
                    }
                    if (hasEmptyMethodLevelMappings || Controller.class.isAssignableFrom(handlerType)) {
                        addUrlsForPath(urls, typeLevelPattern);
                    }
                }
                return StringUtils.toStringArray(urls);
            }
            return determineUrlsForHandlerMethods(handlerType, false);
        }
        if (AnnotationUtils.findAnnotation(handlerType, org.springframework.stereotype.Controller.class) != null) {
            return determineUrlsForHandlerMethods(handlerType, false);
        }
        return null;
    }

    protected String[] determineUrlsForHandlerMethods(Class<?> handlerType, final boolean hasTypeLevelMapping) throws SecurityException, IllegalArgumentException {
        String[] subclassResult = determineUrlsForHandlerMethods(handlerType);
        if (subclassResult != null) {
            return subclassResult;
        }
        final Set<String> urls = new LinkedHashSet<>();
        Set<Class<?>> handlerTypes = new LinkedHashSet<>();
        handlerTypes.add(handlerType);
        handlerTypes.addAll(Arrays.asList(handlerType.getInterfaces()));
        for (Class<?> currentHandlerType : handlerTypes) {
            ReflectionUtils.doWithMethods(currentHandlerType, new ReflectionUtils.MethodCallback() { // from class: org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping.1
                @Override // org.springframework.util.ReflectionUtils.MethodCallback
                public void doWith(Method method) {
                    RequestMapping mapping = (RequestMapping) AnnotationUtils.findAnnotation(method, RequestMapping.class);
                    if (mapping != null) {
                        String[] mappedPatterns = mapping.value();
                        if (mappedPatterns.length > 0) {
                            for (String mappedPattern : mappedPatterns) {
                                if (!hasTypeLevelMapping && !mappedPattern.startsWith("/")) {
                                    mappedPattern = "/" + mappedPattern;
                                }
                                DefaultAnnotationHandlerMapping.this.addUrlsForPath(urls, mappedPattern);
                            }
                            return;
                        }
                        if (hasTypeLevelMapping) {
                            urls.add(null);
                        }
                    }
                }
            }, ReflectionUtils.USER_DECLARED_METHODS);
        }
        return StringUtils.toStringArray(urls);
    }

    protected String[] determineUrlsForHandlerMethods(Class<?> handlerType) {
        return null;
    }

    protected void addUrlsForPath(Set<String> urls, String path) {
        urls.add(path);
        if (this.useDefaultSuffixPattern && path.indexOf(46) == -1 && !path.endsWith("/")) {
            urls.add(path + ".*");
            urls.add(path + "/");
        }
    }

    @Override // org.springframework.web.servlet.handler.AbstractUrlHandlerMapping
    protected void validateHandler(Object handler, HttpServletRequest request) throws Exception {
        RequestMapping mapping = this.cachedMappings.get(handler.getClass());
        if (mapping == null) {
            mapping = (RequestMapping) AnnotationUtils.findAnnotation(handler.getClass(), RequestMapping.class);
        }
        if (mapping != null) {
            validateMapping(mapping, request);
        }
        request.setAttribute(USE_DEFAULT_SUFFIX_PATTERN, Boolean.valueOf(this.useDefaultSuffixPattern));
    }

    protected void validateMapping(RequestMapping mapping, HttpServletRequest request) throws Exception {
        RequestMethod[] mappedMethods = mapping.method();
        if (!ServletAnnotationMappingUtils.checkRequestMethod(mappedMethods, request)) {
            String[] supportedMethods = new String[mappedMethods.length];
            for (int i = 0; i < mappedMethods.length; i++) {
                supportedMethods[i] = mappedMethods[i].name();
            }
            throw new HttpRequestMethodNotSupportedException(request.getMethod(), supportedMethods);
        }
        String[] mappedParams = mapping.params();
        if (!ServletAnnotationMappingUtils.checkParameters(mappedParams, request)) {
            throw new UnsatisfiedServletRequestParameterException(mappedParams, request.getParameterMap());
        }
        String[] mappedHeaders = mapping.headers();
        if (!ServletAnnotationMappingUtils.checkHeaders(mappedHeaders, request)) {
            throw new ServletRequestBindingException("Header conditions \"" + StringUtils.arrayToDelimitedString(mappedHeaders, ", ") + "\" not met for actual request");
        }
    }

    @Override // org.springframework.web.servlet.handler.AbstractUrlHandlerMapping
    protected boolean supportsTypeLevelMappings() {
        return true;
    }
}
