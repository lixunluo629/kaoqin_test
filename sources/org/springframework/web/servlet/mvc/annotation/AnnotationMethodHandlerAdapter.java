package org.springframework.web.servlet.mvc.annotation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.Ordered;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.http.converter.xml.XmlAwareFormHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.support.HandlerMethodInvoker;
import org.springframework.web.bind.annotation.support.HandlerMethodResolver;
import org.springframework.web.bind.support.DefaultSessionAttributeStore;
import org.springframework.web.bind.support.SessionAttributeStore;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestScope;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.multiaction.InternalPathMethodNameResolver;
import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.support.WebContentGenerator;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/annotation/AnnotationMethodHandlerAdapter.class */
public class AnnotationMethodHandlerAdapter extends WebContentGenerator implements HandlerAdapter, Ordered, BeanFactoryAware {
    public static final String PAGE_NOT_FOUND_LOG_CATEGORY = "org.springframework.web.servlet.PageNotFound";
    protected static final Log pageNotFoundLogger = LogFactory.getLog("org.springframework.web.servlet.PageNotFound");
    private UrlPathHelper urlPathHelper;
    private PathMatcher pathMatcher;
    private MethodNameResolver methodNameResolver;
    private WebBindingInitializer webBindingInitializer;
    private SessionAttributeStore sessionAttributeStore;
    private int cacheSecondsForSessionAttributeHandlers;
    private boolean synchronizeOnSession;
    private ParameterNameDiscoverer parameterNameDiscoverer;
    private WebArgumentResolver[] customArgumentResolvers;
    private ModelAndViewResolver[] customModelAndViewResolvers;
    private HttpMessageConverter<?>[] messageConverters;
    private int order;
    private ConfigurableBeanFactory beanFactory;
    private BeanExpressionContext expressionContext;
    private final Map<Class<?>, ServletHandlerMethodResolver> methodResolverCache;
    private final Map<Class<?>, Boolean> sessionAnnotatedClassesCache;

    public AnnotationMethodHandlerAdapter() {
        super(false);
        this.urlPathHelper = new UrlPathHelper();
        this.pathMatcher = new AntPathMatcher();
        this.methodNameResolver = new InternalPathMethodNameResolver();
        this.sessionAttributeStore = new DefaultSessionAttributeStore();
        this.cacheSecondsForSessionAttributeHandlers = 0;
        this.synchronizeOnSession = false;
        this.parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
        this.order = Integer.MAX_VALUE;
        this.methodResolverCache = new ConcurrentHashMap(64);
        this.sessionAnnotatedClassesCache = new ConcurrentHashMap(64);
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        stringHttpMessageConverter.setWriteAcceptCharset(false);
        this.messageConverters = new HttpMessageConverter[]{new ByteArrayHttpMessageConverter(), stringHttpMessageConverter, new SourceHttpMessageConverter(), new XmlAwareFormHttpMessageConverter()};
    }

    public void setAlwaysUseFullPath(boolean alwaysUseFullPath) {
        this.urlPathHelper.setAlwaysUseFullPath(alwaysUseFullPath);
    }

    public void setUrlDecode(boolean urlDecode) {
        this.urlPathHelper.setUrlDecode(urlDecode);
    }

    public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
        Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
        this.urlPathHelper = urlPathHelper;
    }

    public void setPathMatcher(PathMatcher pathMatcher) {
        Assert.notNull(pathMatcher, "PathMatcher must not be null");
        this.pathMatcher = pathMatcher;
    }

    public void setMethodNameResolver(MethodNameResolver methodNameResolver) {
        this.methodNameResolver = methodNameResolver;
    }

    public void setWebBindingInitializer(WebBindingInitializer webBindingInitializer) {
        this.webBindingInitializer = webBindingInitializer;
    }

    public void setSessionAttributeStore(SessionAttributeStore sessionAttributeStore) {
        Assert.notNull(sessionAttributeStore, "SessionAttributeStore must not be null");
        this.sessionAttributeStore = sessionAttributeStore;
    }

    public void setCacheSecondsForSessionAttributeHandlers(int cacheSecondsForSessionAttributeHandlers) {
        this.cacheSecondsForSessionAttributeHandlers = cacheSecondsForSessionAttributeHandlers;
    }

    public void setSynchronizeOnSession(boolean synchronizeOnSession) {
        this.synchronizeOnSession = synchronizeOnSession;
    }

    public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer) {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }

    public void setCustomArgumentResolver(WebArgumentResolver argumentResolver) {
        this.customArgumentResolvers = new WebArgumentResolver[]{argumentResolver};
    }

    public void setCustomArgumentResolvers(WebArgumentResolver... argumentResolvers) {
        this.customArgumentResolvers = argumentResolvers;
    }

    public void setCustomModelAndViewResolver(ModelAndViewResolver customModelAndViewResolver) {
        this.customModelAndViewResolvers = new ModelAndViewResolver[]{customModelAndViewResolver};
    }

    public void setCustomModelAndViewResolvers(ModelAndViewResolver... customModelAndViewResolvers) {
        this.customModelAndViewResolvers = customModelAndViewResolvers;
    }

    public void setMessageConverters(HttpMessageConverter<?>[] messageConverters) {
        this.messageConverters = messageConverters;
    }

    public HttpMessageConverter<?>[] getMessageConverters() {
        return this.messageConverters;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return this.order;
    }

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) {
        if (beanFactory instanceof ConfigurableBeanFactory) {
            this.beanFactory = (ConfigurableBeanFactory) beanFactory;
            this.expressionContext = new BeanExpressionContext(this.beanFactory, new RequestScope());
        }
    }

    @Override // org.springframework.web.servlet.HandlerAdapter
    public boolean supports(Object handler) {
        return getMethodResolver(handler).hasHandlerMethods();
    }

    @Override // org.springframework.web.servlet.HandlerAdapter
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session;
        ModelAndView modelAndViewInvokeHandlerMethod;
        Class<?> clazz = ClassUtils.getUserClass(handler);
        Boolean annotatedWithSessionAttributes = this.sessionAnnotatedClassesCache.get(clazz);
        if (annotatedWithSessionAttributes == null) {
            annotatedWithSessionAttributes = Boolean.valueOf(AnnotationUtils.findAnnotation(clazz, SessionAttributes.class) != null);
            this.sessionAnnotatedClassesCache.put(clazz, annotatedWithSessionAttributes);
        }
        if (annotatedWithSessionAttributes.booleanValue()) {
            checkAndPrepare(request, response, this.cacheSecondsForSessionAttributeHandlers, true);
        } else {
            checkAndPrepare(request, response, true);
        }
        if (this.synchronizeOnSession && (session = request.getSession(false)) != null) {
            Object mutex = WebUtils.getSessionMutex(session);
            synchronized (mutex) {
                modelAndViewInvokeHandlerMethod = invokeHandlerMethod(request, response, handler);
            }
            return modelAndViewInvokeHandlerMethod;
        }
        return invokeHandlerMethod(request, response, handler);
    }

    protected ModelAndView invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ServletHandlerMethodResolver methodResolver = getMethodResolver(handler);
        Method handlerMethod = methodResolver.resolveHandlerMethod(request);
        ServletHandlerMethodInvoker methodInvoker = new ServletHandlerMethodInvoker(methodResolver);
        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        ExtendedModelMap implicitModel = new BindingAwareModelMap();
        Object result = methodInvoker.invokeHandlerMethod(handlerMethod, handler, webRequest, implicitModel);
        ModelAndView mav = methodInvoker.getModelAndView(handlerMethod, handler.getClass(), result, implicitModel, webRequest);
        methodInvoker.updateModelAttributes(handler, mav != null ? mav.getModel() : null, implicitModel, webRequest);
        return mav;
    }

    @Override // org.springframework.web.servlet.HandlerAdapter
    public long getLastModified(HttpServletRequest request, Object handler) {
        return -1L;
    }

    private ServletHandlerMethodResolver getMethodResolver(Object handler) {
        Class<?> handlerClass = ClassUtils.getUserClass(handler);
        ServletHandlerMethodResolver resolver = this.methodResolverCache.get(handlerClass);
        if (resolver == null) {
            synchronized (this.methodResolverCache) {
                resolver = this.methodResolverCache.get(handlerClass);
                if (resolver == null) {
                    resolver = new ServletHandlerMethodResolver(handlerClass);
                    this.methodResolverCache.put(handlerClass, resolver);
                }
            }
        }
        return resolver;
    }

    protected ServletRequestDataBinder createBinder(HttpServletRequest request, Object target, String objectName) throws Exception {
        return new ServletRequestDataBinder(target, objectName);
    }

    protected HttpInputMessage createHttpInputMessage(HttpServletRequest servletRequest) throws Exception {
        return new ServletServerHttpRequest(servletRequest);
    }

    protected HttpOutputMessage createHttpOutputMessage(HttpServletResponse servletResponse) throws Exception {
        return new ServletServerHttpResponse(servletResponse);
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/annotation/AnnotationMethodHandlerAdapter$ServletHandlerMethodResolver.class */
    private class ServletHandlerMethodResolver extends HandlerMethodResolver {
        private final Map<Method, RequestMappingInfo> mappings;

        private ServletHandlerMethodResolver(Class<?> handlerType) {
            this.mappings = new HashMap();
            init(handlerType);
        }

        @Override // org.springframework.web.bind.annotation.support.HandlerMethodResolver
        protected boolean isHandlerMethod(Method method) {
            if (this.mappings.containsKey(method)) {
                return true;
            }
            RequestMapping mapping = (RequestMapping) AnnotationUtils.findAnnotation(method, RequestMapping.class);
            if (mapping != null) {
                String[] patterns = mapping.value();
                RequestMethod[] methods = new RequestMethod[0];
                String[] params = new String[0];
                String[] headers = new String[0];
                if (!hasTypeLevelMapping() || !Arrays.equals(mapping.method(), getTypeLevelMapping().method())) {
                    methods = mapping.method();
                }
                if (!hasTypeLevelMapping() || !Arrays.equals(mapping.params(), getTypeLevelMapping().params())) {
                    params = mapping.params();
                }
                if (!hasTypeLevelMapping() || !Arrays.equals(mapping.headers(), getTypeLevelMapping().headers())) {
                    headers = mapping.headers();
                }
                RequestMappingInfo mappingInfo = new RequestMappingInfo(patterns, methods, params, headers);
                this.mappings.put(method, mappingInfo);
                return true;
            }
            return false;
        }

        public Method resolveHandlerMethod(HttpServletRequest request) throws ServletException {
            String lookupPath = AnnotationMethodHandlerAdapter.this.urlPathHelper.getLookupPathForRequest(request);
            Comparator<String> pathComparator = AnnotationMethodHandlerAdapter.this.pathMatcher.getPatternComparator(lookupPath);
            Map<RequestSpecificMappingInfo, Method> targetHandlerMethods = new LinkedHashMap<>();
            Set<String> allowedMethods = new LinkedHashSet<>(7);
            String resolvedMethodName = null;
            for (Method handlerMethod : getHandlerMethods()) {
                RequestSpecificMappingInfo mappingInfo = new RequestSpecificMappingInfo(this.mappings.get(handlerMethod));
                boolean match = false;
                if (mappingInfo.hasPatterns()) {
                    String[] patterns = mappingInfo.getPatterns();
                    int length = patterns.length;
                    int i = 0;
                    while (true) {
                        if (i >= length) {
                            break;
                        }
                        String pattern = patterns[i];
                        if (!hasTypeLevelMapping() && !pattern.startsWith("/")) {
                            pattern = "/" + pattern;
                        }
                        String combinedPattern = getCombinedPattern(pattern, lookupPath, request);
                        if (combinedPattern != null) {
                            if (mappingInfo.matches(request)) {
                                match = true;
                                mappingInfo.addMatchedPattern(combinedPattern);
                            } else if (!mappingInfo.matchesRequestMethod(request)) {
                                allowedMethods.addAll(mappingInfo.methodNames());
                            }
                        }
                        i++;
                    }
                    mappingInfo.sortMatchedPatterns(pathComparator);
                } else if (useTypeLevelMapping(request)) {
                    String[] typeLevelPatterns = getTypeLevelMapping().value();
                    int length2 = typeLevelPatterns.length;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= length2) {
                            break;
                        }
                        String typeLevelPattern = typeLevelPatterns[i2];
                        if (!typeLevelPattern.startsWith("/")) {
                            typeLevelPattern = "/" + typeLevelPattern;
                        }
                        boolean useSuffixPattern = useSuffixPattern(request);
                        if (getMatchingPattern(typeLevelPattern, lookupPath, useSuffixPattern) != null) {
                            if (mappingInfo.matches(request)) {
                                match = true;
                                mappingInfo.addMatchedPattern(typeLevelPattern);
                            } else if (!mappingInfo.matchesRequestMethod(request)) {
                                allowedMethods.addAll(mappingInfo.methodNames());
                            }
                        }
                        i2++;
                    }
                    mappingInfo.sortMatchedPatterns(pathComparator);
                } else {
                    match = mappingInfo.matches(request);
                    if (match && mappingInfo.getMethodCount() == 0 && mappingInfo.getParamCount() == 0 && resolvedMethodName != null && !resolvedMethodName.equals(handlerMethod.getName())) {
                        match = false;
                    } else if (!mappingInfo.matchesRequestMethod(request)) {
                        allowedMethods.addAll(mappingInfo.methodNames());
                    }
                }
                if (match) {
                    Method oldMappedMethod = targetHandlerMethods.put(mappingInfo, handlerMethod);
                    if (oldMappedMethod != null && oldMappedMethod != handlerMethod) {
                        if (AnnotationMethodHandlerAdapter.this.methodNameResolver != null && !mappingInfo.hasPatterns() && !oldMappedMethod.getName().equals(handlerMethod.getName())) {
                            if (resolvedMethodName == null) {
                                resolvedMethodName = AnnotationMethodHandlerAdapter.this.methodNameResolver.getHandlerMethodName(request);
                            }
                            if (!resolvedMethodName.equals(oldMappedMethod.getName())) {
                                oldMappedMethod = null;
                            }
                            if (!resolvedMethodName.equals(handlerMethod.getName())) {
                                if (oldMappedMethod != null) {
                                    targetHandlerMethods.put(mappingInfo, oldMappedMethod);
                                    oldMappedMethod = null;
                                } else {
                                    targetHandlerMethods.remove(mappingInfo);
                                }
                            }
                        }
                        if (oldMappedMethod != null) {
                            throw new IllegalStateException("Ambiguous handler methods mapped for HTTP path '" + lookupPath + "': {" + oldMappedMethod + ", " + handlerMethod + "}. If you intend to handle the same path in multiple methods, then factor them out into a dedicated handler class with that path mapped at the type level!");
                        }
                    }
                }
            }
            if (!targetHandlerMethods.isEmpty()) {
                List<RequestSpecificMappingInfo> matches = new ArrayList<>(targetHandlerMethods.keySet());
                RequestSpecificMappingInfoComparator requestMappingInfoComparator = new RequestSpecificMappingInfoComparator(pathComparator, request);
                Collections.sort(matches, requestMappingInfoComparator);
                RequestSpecificMappingInfo bestMappingMatch = matches.get(0);
                String bestMatchedPath = bestMappingMatch.bestMatchedPattern();
                if (bestMatchedPath != null) {
                    extractHandlerMethodUriTemplates(bestMatchedPath, lookupPath, request);
                }
                return targetHandlerMethods.get(bestMappingMatch);
            }
            if (!allowedMethods.isEmpty()) {
                throw new HttpRequestMethodNotSupportedException(request.getMethod(), StringUtils.toStringArray(allowedMethods));
            }
            throw new NoSuchRequestHandlingMethodException(lookupPath, request.getMethod(), request.getParameterMap());
        }

        private boolean useTypeLevelMapping(HttpServletRequest request) {
            if (!hasTypeLevelMapping() || ObjectUtils.isEmpty((Object[]) getTypeLevelMapping().value())) {
                return false;
            }
            Object value = request.getAttribute(HandlerMapping.INTROSPECT_TYPE_LEVEL_MAPPING);
            return (value != null ? (Boolean) value : Boolean.TRUE).booleanValue();
        }

        private boolean useSuffixPattern(HttpServletRequest request) {
            Object value = request.getAttribute(DefaultAnnotationHandlerMapping.USE_DEFAULT_SUFFIX_PATTERN);
            return (value != null ? (Boolean) value : Boolean.TRUE).booleanValue();
        }

        private String getCombinedPattern(String methodLevelPattern, String lookupPath, HttpServletRequest request) {
            boolean useSuffixPattern = useSuffixPattern(request);
            if (useTypeLevelMapping(request)) {
                String[] typeLevelPatterns = getTypeLevelMapping().value();
                for (String typeLevelPattern : typeLevelPatterns) {
                    if (!typeLevelPattern.startsWith("/")) {
                        typeLevelPattern = "/" + typeLevelPattern;
                    }
                    String combinedPattern = AnnotationMethodHandlerAdapter.this.pathMatcher.combine(typeLevelPattern, methodLevelPattern);
                    String matchingPattern = getMatchingPattern(combinedPattern, lookupPath, useSuffixPattern);
                    if (matchingPattern != null) {
                        return matchingPattern;
                    }
                }
                return null;
            }
            String bestMatchingPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
            if (StringUtils.hasText(bestMatchingPattern) && bestMatchingPattern.endsWith("*")) {
                String combinedPattern2 = AnnotationMethodHandlerAdapter.this.pathMatcher.combine(bestMatchingPattern, methodLevelPattern);
                String matchingPattern2 = getMatchingPattern(combinedPattern2, lookupPath, useSuffixPattern);
                if (matchingPattern2 != null && !matchingPattern2.equals(bestMatchingPattern)) {
                    return matchingPattern2;
                }
            }
            return getMatchingPattern(methodLevelPattern, lookupPath, useSuffixPattern);
        }

        private String getMatchingPattern(String pattern, String lookupPath, boolean useSuffixPattern) {
            if (pattern.equals(lookupPath)) {
                return pattern;
            }
            boolean hasSuffix = pattern.indexOf(46) != -1;
            if (useSuffixPattern && !hasSuffix) {
                String patternWithSuffix = pattern + ".*";
                if (AnnotationMethodHandlerAdapter.this.pathMatcher.match(patternWithSuffix, lookupPath)) {
                    return patternWithSuffix;
                }
            }
            if (AnnotationMethodHandlerAdapter.this.pathMatcher.match(pattern, lookupPath)) {
                return pattern;
            }
            boolean endsWithSlash = pattern.endsWith("/");
            if (useSuffixPattern && !endsWithSlash) {
                String patternWithSlash = pattern + "/";
                if (AnnotationMethodHandlerAdapter.this.pathMatcher.match(patternWithSlash, lookupPath)) {
                    return patternWithSlash;
                }
                return null;
            }
            return null;
        }

        private void extractHandlerMethodUriTemplates(String mappedPattern, String lookupPath, HttpServletRequest request) {
            Map<String, String> variables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            int patternVariableCount = StringUtils.countOccurrencesOf(mappedPattern, "{");
            if ((variables == null || patternVariableCount != variables.size()) && AnnotationMethodHandlerAdapter.this.pathMatcher.match(mappedPattern, lookupPath)) {
                Map<String, String> decodedVariables = AnnotationMethodHandlerAdapter.this.urlPathHelper.decodePathVariables(request, AnnotationMethodHandlerAdapter.this.pathMatcher.extractUriTemplateVariables(mappedPattern, lookupPath));
                request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, decodedVariables);
            }
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/annotation/AnnotationMethodHandlerAdapter$ServletHandlerMethodInvoker.class */
    private class ServletHandlerMethodInvoker extends HandlerMethodInvoker {
        private boolean responseArgumentUsed;

        private ServletHandlerMethodInvoker(HandlerMethodResolver resolver) {
            super(resolver, AnnotationMethodHandlerAdapter.this.webBindingInitializer, AnnotationMethodHandlerAdapter.this.sessionAttributeStore, AnnotationMethodHandlerAdapter.this.parameterNameDiscoverer, AnnotationMethodHandlerAdapter.this.customArgumentResolvers, AnnotationMethodHandlerAdapter.this.messageConverters);
            this.responseArgumentUsed = false;
        }

        @Override // org.springframework.web.bind.annotation.support.HandlerMethodInvoker
        protected void raiseMissingParameterException(String paramName, Class<?> paramType) throws Exception {
            throw new MissingServletRequestParameterException(paramName, paramType.getSimpleName());
        }

        @Override // org.springframework.web.bind.annotation.support.HandlerMethodInvoker
        protected void raiseSessionRequiredException(String message) throws Exception {
            throw new HttpSessionRequiredException(message);
        }

        @Override // org.springframework.web.bind.annotation.support.HandlerMethodInvoker
        protected WebDataBinder createBinder(NativeWebRequest webRequest, Object target, String objectName) throws Exception {
            return AnnotationMethodHandlerAdapter.this.createBinder((HttpServletRequest) webRequest.getNativeRequest(HttpServletRequest.class), target, objectName);
        }

        @Override // org.springframework.web.bind.annotation.support.HandlerMethodInvoker
        protected void doBind(WebDataBinder binder, NativeWebRequest webRequest) throws Exception {
            ServletRequestDataBinder servletBinder = (ServletRequestDataBinder) binder;
            servletBinder.bind((ServletRequest) webRequest.getNativeRequest(ServletRequest.class));
        }

        @Override // org.springframework.web.bind.annotation.support.HandlerMethodInvoker
        protected HttpInputMessage createHttpInputMessage(NativeWebRequest webRequest) throws Exception {
            HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getNativeRequest(HttpServletRequest.class);
            return AnnotationMethodHandlerAdapter.this.createHttpInputMessage(servletRequest);
        }

        @Override // org.springframework.web.bind.annotation.support.HandlerMethodInvoker
        protected HttpOutputMessage createHttpOutputMessage(NativeWebRequest webRequest) throws Exception {
            HttpServletResponse servletResponse = (HttpServletResponse) webRequest.getNativeResponse();
            return AnnotationMethodHandlerAdapter.this.createHttpOutputMessage(servletResponse);
        }

        @Override // org.springframework.web.bind.annotation.support.HandlerMethodInvoker
        protected Object resolveDefaultValue(String value) {
            if (AnnotationMethodHandlerAdapter.this.beanFactory != null) {
                String placeholdersResolved = AnnotationMethodHandlerAdapter.this.beanFactory.resolveEmbeddedValue(value);
                BeanExpressionResolver exprResolver = AnnotationMethodHandlerAdapter.this.beanFactory.getBeanExpressionResolver();
                if (exprResolver == null) {
                    return value;
                }
                return exprResolver.evaluate(placeholdersResolved, AnnotationMethodHandlerAdapter.this.expressionContext);
            }
            return value;
        }

        @Override // org.springframework.web.bind.annotation.support.HandlerMethodInvoker
        protected Object resolveCookieValue(String cookieName, Class<?> paramType, NativeWebRequest webRequest) throws Exception {
            HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getNativeRequest(HttpServletRequest.class);
            Cookie cookieValue = WebUtils.getCookie(servletRequest, cookieName);
            if (Cookie.class.isAssignableFrom(paramType)) {
                return cookieValue;
            }
            if (cookieValue != null) {
                return AnnotationMethodHandlerAdapter.this.urlPathHelper.decodeRequestString(servletRequest, cookieValue.getValue());
            }
            return null;
        }

        @Override // org.springframework.web.bind.annotation.support.HandlerMethodInvoker
        protected String resolvePathVariable(String pathVarName, Class<?> paramType, NativeWebRequest webRequest) throws Exception {
            HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getNativeRequest(HttpServletRequest.class);
            Map<String, String> uriTemplateVariables = (Map) servletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            if (uriTemplateVariables == null || !uriTemplateVariables.containsKey(pathVarName)) {
                throw new IllegalStateException("Could not find @PathVariable [" + pathVarName + "] in @RequestMapping");
            }
            return uriTemplateVariables.get(pathVarName);
        }

        @Override // org.springframework.web.bind.annotation.support.HandlerMethodInvoker
        protected Object resolveStandardArgument(Class<?> parameterType, NativeWebRequest webRequest) throws Exception {
            HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest(HttpServletRequest.class);
            HttpServletResponse response = (HttpServletResponse) webRequest.getNativeResponse(HttpServletResponse.class);
            if (ServletRequest.class.isAssignableFrom(parameterType) || MultipartRequest.class.isAssignableFrom(parameterType)) {
                Object nativeRequest = webRequest.getNativeRequest(parameterType);
                if (nativeRequest == null) {
                    throw new IllegalStateException("Current request is not of type [" + parameterType.getName() + "]: " + request);
                }
                return nativeRequest;
            }
            if (ServletResponse.class.isAssignableFrom(parameterType)) {
                this.responseArgumentUsed = true;
                Object nativeResponse = webRequest.getNativeResponse(parameterType);
                if (nativeResponse == null) {
                    throw new IllegalStateException("Current response is not of type [" + parameterType.getName() + "]: " + response);
                }
                return nativeResponse;
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
                this.responseArgumentUsed = true;
                return response.getOutputStream();
            }
            if (Writer.class.isAssignableFrom(parameterType)) {
                this.responseArgumentUsed = true;
                return response.getWriter();
            }
            return super.resolveStandardArgument(parameterType, webRequest);
        }

        public ModelAndView getModelAndView(Method handlerMethod, Class<?> handlerType, Object returnValue, ExtendedModelMap implicitModel, ServletWebRequest webRequest) throws Exception {
            ResponseStatus responseStatus = (ResponseStatus) AnnotatedElementUtils.findMergedAnnotation(handlerMethod, ResponseStatus.class);
            if (responseStatus != null) {
                HttpStatus statusCode = responseStatus.code();
                String reason = responseStatus.reason();
                if (!StringUtils.hasText(reason)) {
                    webRequest.getResponse().setStatus(statusCode.value());
                } else {
                    webRequest.getResponse().sendError(statusCode.value(), reason);
                }
                webRequest.getRequest().setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, statusCode);
                this.responseArgumentUsed = true;
            }
            if (AnnotationMethodHandlerAdapter.this.customModelAndViewResolvers != null) {
                for (ModelAndViewResolver mavResolver : AnnotationMethodHandlerAdapter.this.customModelAndViewResolvers) {
                    ModelAndView mav = mavResolver.resolveModelAndView(handlerMethod, handlerType, returnValue, implicitModel, webRequest);
                    if (mav != ModelAndViewResolver.UNRESOLVED) {
                        return mav;
                    }
                }
            }
            if (returnValue instanceof HttpEntity) {
                handleHttpEntityResponse((HttpEntity) returnValue, webRequest);
                return null;
            }
            if (AnnotationUtils.findAnnotation(handlerMethod, ResponseBody.class) != null) {
                handleResponseBody(returnValue, webRequest);
                return null;
            }
            if (returnValue instanceof ModelAndView) {
                ModelAndView mav2 = (ModelAndView) returnValue;
                mav2.getModelMap().mergeAttributes(implicitModel);
                return mav2;
            }
            if (returnValue instanceof Model) {
                return new ModelAndView().addAllObjects(implicitModel).addAllObjects(((Model) returnValue).asMap());
            }
            if (returnValue instanceof View) {
                return new ModelAndView((View) returnValue).addAllObjects(implicitModel);
            }
            if (AnnotationUtils.findAnnotation(handlerMethod, ModelAttribute.class) != null) {
                addReturnValueAsModelAttribute(handlerMethod, handlerType, returnValue, implicitModel);
                return new ModelAndView().addAllObjects(implicitModel);
            }
            if (returnValue instanceof Map) {
                return new ModelAndView().addAllObjects(implicitModel).addAllObjects((Map) returnValue);
            }
            if (returnValue instanceof String) {
                return new ModelAndView((String) returnValue).addAllObjects(implicitModel);
            }
            if (returnValue == null) {
                if (this.responseArgumentUsed || webRequest.isNotModified()) {
                    return null;
                }
                return new ModelAndView().addAllObjects(implicitModel);
            }
            if (!BeanUtils.isSimpleProperty(returnValue.getClass())) {
                addReturnValueAsModelAttribute(handlerMethod, handlerType, returnValue, implicitModel);
                return new ModelAndView().addAllObjects(implicitModel);
            }
            throw new IllegalArgumentException("Invalid handler method return value: " + returnValue);
        }

        private void handleResponseBody(Object returnValue, ServletWebRequest webRequest) throws Exception {
            if (returnValue == null) {
                return;
            }
            HttpInputMessage inputMessage = createHttpInputMessage(webRequest);
            HttpOutputMessage outputMessage = createHttpOutputMessage(webRequest);
            writeWithMessageConverters(returnValue, inputMessage, outputMessage);
        }

        private void handleHttpEntityResponse(HttpEntity<?> responseEntity, ServletWebRequest webRequest) throws Exception {
            if (responseEntity == null) {
                return;
            }
            HttpInputMessage inputMessage = createHttpInputMessage(webRequest);
            HttpOutputMessage outputMessage = createHttpOutputMessage(webRequest);
            if ((responseEntity instanceof ResponseEntity) && (outputMessage instanceof ServerHttpResponse)) {
                ((ServerHttpResponse) outputMessage).setStatusCode(((ResponseEntity) responseEntity).getStatusCode());
            }
            HttpHeaders entityHeaders = responseEntity.getHeaders();
            if (!entityHeaders.isEmpty()) {
                outputMessage.getHeaders().putAll(entityHeaders);
            }
            Object body = responseEntity.getBody();
            if (body != null) {
                writeWithMessageConverters(body, inputMessage, outputMessage);
            } else {
                outputMessage.getBody();
            }
        }

        private void writeWithMessageConverters(Object returnValue, HttpInputMessage inputMessage, HttpOutputMessage outputMessage) throws HttpMediaTypeNotAcceptableException, IOException, HttpMessageNotWritableException {
            List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
            if (acceptedMediaTypes.isEmpty()) {
                acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
            }
            MediaType.sortByQualityValue(acceptedMediaTypes);
            Class<?> returnValueType = returnValue.getClass();
            List<MediaType> allSupportedMediaTypes = new ArrayList<>();
            if (AnnotationMethodHandlerAdapter.this.getMessageConverters() != null) {
                for (MediaType acceptedMediaType : acceptedMediaTypes) {
                    for (HttpMessageConverter messageConverter : AnnotationMethodHandlerAdapter.this.getMessageConverters()) {
                        if (messageConverter.canWrite(returnValueType, acceptedMediaType)) {
                            messageConverter.write(returnValue, acceptedMediaType, outputMessage);
                            if (AnnotationMethodHandlerAdapter.this.logger.isDebugEnabled()) {
                                MediaType contentType = outputMessage.getHeaders().getContentType();
                                if (contentType == null) {
                                    contentType = acceptedMediaType;
                                }
                                AnnotationMethodHandlerAdapter.this.logger.debug("Written [" + returnValue + "] as \"" + contentType + "\" using [" + messageConverter + "]");
                            }
                            this.responseArgumentUsed = true;
                            return;
                        }
                    }
                }
                for (HttpMessageConverter httpMessageConverter : AnnotationMethodHandlerAdapter.this.messageConverters) {
                    allSupportedMediaTypes.addAll(httpMessageConverter.getSupportedMediaTypes());
                }
            }
            throw new HttpMediaTypeNotAcceptableException(allSupportedMediaTypes);
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/annotation/AnnotationMethodHandlerAdapter$RequestMappingInfo.class */
    static class RequestMappingInfo {
        private final String[] patterns;
        private final RequestMethod[] methods;
        private final String[] params;
        private final String[] headers;

        RequestMappingInfo(String[] patterns, RequestMethod[] methods, String[] params, String[] headers) {
            this.patterns = patterns != null ? patterns : new String[0];
            this.methods = methods != null ? methods : new RequestMethod[0];
            this.params = params != null ? params : new String[0];
            this.headers = headers != null ? headers : new String[0];
        }

        public boolean hasPatterns() {
            return this.patterns.length > 0;
        }

        public String[] getPatterns() {
            return this.patterns;
        }

        public int getMethodCount() {
            return this.methods.length;
        }

        public int getParamCount() {
            return this.params.length;
        }

        public int getHeaderCount() {
            return this.headers.length;
        }

        public boolean matches(HttpServletRequest request) {
            return matchesRequestMethod(request) && matchesParameters(request) && matchesHeaders(request);
        }

        public boolean matchesHeaders(HttpServletRequest request) {
            return ServletAnnotationMappingUtils.checkHeaders(this.headers, request);
        }

        public boolean matchesParameters(HttpServletRequest request) {
            return ServletAnnotationMappingUtils.checkParameters(this.params, request);
        }

        public boolean matchesRequestMethod(HttpServletRequest request) {
            return ServletAnnotationMappingUtils.checkRequestMethod(this.methods, request);
        }

        public Set<String> methodNames() {
            Set<String> methodNames = new LinkedHashSet<>(this.methods.length);
            for (RequestMethod method : this.methods) {
                methodNames.add(method.name());
            }
            return methodNames;
        }

        public boolean equals(Object obj) {
            RequestMappingInfo other = (RequestMappingInfo) obj;
            return Arrays.equals(this.patterns, other.patterns) && Arrays.equals(this.methods, other.methods) && Arrays.equals(this.params, other.params) && Arrays.equals(this.headers, other.headers);
        }

        public int hashCode() {
            return (Arrays.hashCode(this.patterns) * 23) + (Arrays.hashCode(this.methods) * 29) + (Arrays.hashCode(this.params) * 31) + Arrays.hashCode(this.headers);
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append(Arrays.asList(this.patterns));
            if (this.methods.length > 0) {
                builder.append(',');
                builder.append(Arrays.asList(this.methods));
            }
            if (this.headers.length > 0) {
                builder.append(',');
                builder.append(Arrays.asList(this.headers));
            }
            if (this.params.length > 0) {
                builder.append(',');
                builder.append(Arrays.asList(this.params));
            }
            return builder.toString();
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/annotation/AnnotationMethodHandlerAdapter$RequestSpecificMappingInfo.class */
    static class RequestSpecificMappingInfo extends RequestMappingInfo {
        private final List<String> matchedPatterns;

        RequestSpecificMappingInfo(String[] patterns, RequestMethod[] methods, String[] params, String[] headers) {
            super(patterns, methods, params, headers);
            this.matchedPatterns = new ArrayList();
        }

        RequestSpecificMappingInfo(RequestMappingInfo other) {
            super(other.patterns, other.methods, other.params, other.headers);
            this.matchedPatterns = new ArrayList();
        }

        public void addMatchedPattern(String matchedPattern) {
            this.matchedPatterns.add(matchedPattern);
        }

        public void sortMatchedPatterns(Comparator<String> pathComparator) {
            Collections.sort(this.matchedPatterns, pathComparator);
        }

        public String bestMatchedPattern() {
            if (this.matchedPatterns.isEmpty()) {
                return null;
            }
            return this.matchedPatterns.get(0);
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/annotation/AnnotationMethodHandlerAdapter$RequestSpecificMappingInfoComparator.class */
    static class RequestSpecificMappingInfoComparator implements Comparator<RequestSpecificMappingInfo> {
        private final Comparator<String> pathComparator;
        private final ServerHttpRequest request;

        RequestSpecificMappingInfoComparator(Comparator<String> pathComparator, HttpServletRequest request) {
            this.pathComparator = pathComparator;
            this.request = new ServletServerHttpRequest(request);
        }

        @Override // java.util.Comparator
        public int compare(RequestSpecificMappingInfo info1, RequestSpecificMappingInfo info2) {
            int pathComparison = this.pathComparator.compare(info1.bestMatchedPattern(), info2.bestMatchedPattern());
            if (pathComparison != 0) {
                return pathComparison;
            }
            int info1ParamCount = info1.getParamCount();
            int info2ParamCount = info2.getParamCount();
            if (info1ParamCount != info2ParamCount) {
                return info2ParamCount - info1ParamCount;
            }
            int info1HeaderCount = info1.getHeaderCount();
            int info2HeaderCount = info2.getHeaderCount();
            if (info1HeaderCount != info2HeaderCount) {
                return info2HeaderCount - info1HeaderCount;
            }
            int acceptComparison = compareAcceptHeaders(info1, info2);
            if (acceptComparison != 0) {
                return acceptComparison;
            }
            int info1MethodCount = info1.getMethodCount();
            int info2MethodCount = info2.getMethodCount();
            if (info1MethodCount == 0 && info2MethodCount > 0) {
                return 1;
            }
            if (info2MethodCount == 0 && info1MethodCount > 0) {
                return -1;
            }
            if ((info1MethodCount == 1) && (info2MethodCount > 1)) {
                return -1;
            }
            if ((info2MethodCount == 1) & (info1MethodCount > 1)) {
                return 1;
            }
            return 0;
        }

        private int compareAcceptHeaders(RequestMappingInfo info1, RequestMappingInfo info2) {
            List<MediaType> requestAccepts = this.request.getHeaders().getAccept();
            MediaType.sortByQualityValue(requestAccepts);
            List<MediaType> info1Accepts = getAcceptHeaderValue(info1);
            List<MediaType> info2Accepts = getAcceptHeaderValue(info2);
            for (MediaType requestAccept : requestAccepts) {
                int pos1 = indexOfIncluded(info1Accepts, requestAccept);
                int pos2 = indexOfIncluded(info2Accepts, requestAccept);
                if (pos1 != pos2) {
                    return pos2 - pos1;
                }
            }
            return 0;
        }

        private int indexOfIncluded(List<MediaType> infoAccepts, MediaType requestAccept) {
            for (int i = 0; i < infoAccepts.size(); i++) {
                MediaType info1Accept = infoAccepts.get(i);
                if (requestAccept.includes(info1Accept)) {
                    return i;
                }
            }
            return -1;
        }

        private List<MediaType> getAcceptHeaderValue(RequestMappingInfo info) {
            for (String header : info.headers) {
                int separator = header.indexOf(61);
                if (separator != -1) {
                    String key = header.substring(0, separator);
                    String value = header.substring(separator + 1);
                    if ("Accept".equalsIgnoreCase(key)) {
                        return MediaType.parseMediaTypes(value);
                    }
                }
            }
            return Collections.emptyList();
        }
    }
}
