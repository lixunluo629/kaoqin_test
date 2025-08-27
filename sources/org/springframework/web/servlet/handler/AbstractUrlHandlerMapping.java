package org.springframework.web.servlet.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.BeansException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/handler/AbstractUrlHandlerMapping.class */
public abstract class AbstractUrlHandlerMapping extends AbstractHandlerMapping implements MatchableHandlerMapping {
    private Object rootHandler;
    private boolean useTrailingSlashMatch = false;
    private boolean lazyInitHandlers = false;
    private final Map<String, Object> handlerMap = new LinkedHashMap();

    public void setRootHandler(Object rootHandler) {
        this.rootHandler = rootHandler;
    }

    public Object getRootHandler() {
        return this.rootHandler;
    }

    public void setUseTrailingSlashMatch(boolean useTrailingSlashMatch) {
        this.useTrailingSlashMatch = useTrailingSlashMatch;
    }

    public boolean useTrailingSlashMatch() {
        return this.useTrailingSlashMatch;
    }

    public void setLazyInitHandlers(boolean lazyInitHandlers) {
        this.lazyInitHandlers = lazyInitHandlers;
    }

    @Override // org.springframework.web.servlet.handler.AbstractHandlerMapping
    protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
        String lookupPath = getUrlPathHelper().getLookupPathForRequest(request);
        Object handler = lookupHandler(lookupPath, request);
        if (handler == null) {
            Object rawHandler = null;
            if ("/".equals(lookupPath)) {
                rawHandler = getRootHandler();
            }
            if (rawHandler == null) {
                rawHandler = getDefaultHandler();
            }
            if (rawHandler != null) {
                if (rawHandler instanceof String) {
                    String handlerName = (String) rawHandler;
                    rawHandler = getApplicationContext().getBean(handlerName);
                }
                validateHandler(rawHandler, request);
                handler = buildPathExposingHandler(rawHandler, lookupPath, lookupPath, null);
            }
        }
        if (handler != null && this.logger.isDebugEnabled()) {
            this.logger.debug("Mapping [" + lookupPath + "] to " + handler);
        } else if (handler == null && this.logger.isTraceEnabled()) {
            this.logger.trace("No handler mapping found for [" + lookupPath + "]");
        }
        return handler;
    }

    protected Object lookupHandler(String urlPath, HttpServletRequest request) throws Exception {
        Object handler = this.handlerMap.get(urlPath);
        if (handler != null) {
            if (handler instanceof String) {
                String handlerName = (String) handler;
                handler = getApplicationContext().getBean(handlerName);
            }
            validateHandler(handler, request);
            return buildPathExposingHandler(handler, urlPath, urlPath, null);
        }
        List<String> matchingPatterns = new ArrayList<>();
        for (String registeredPattern : this.handlerMap.keySet()) {
            if (getPathMatcher().match(registeredPattern, urlPath)) {
                matchingPatterns.add(registeredPattern);
            } else if (useTrailingSlashMatch() && !registeredPattern.endsWith("/") && getPathMatcher().match(registeredPattern + "/", urlPath)) {
                matchingPatterns.add(registeredPattern + "/");
            }
        }
        String bestMatch = null;
        Comparator<String> patternComparator = getPathMatcher().getPatternComparator(urlPath);
        if (!matchingPatterns.isEmpty()) {
            Collections.sort(matchingPatterns, patternComparator);
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Matching patterns for request [" + urlPath + "] are " + matchingPatterns);
            }
            bestMatch = matchingPatterns.get(0);
        }
        if (bestMatch != null) {
            Object handler2 = this.handlerMap.get(bestMatch);
            if (handler2 == null) {
                if (bestMatch.endsWith("/")) {
                    handler2 = this.handlerMap.get(bestMatch.substring(0, bestMatch.length() - 1));
                }
                if (handler2 == null) {
                    throw new IllegalStateException("Could not find handler for best pattern match [" + bestMatch + "]");
                }
            }
            if (handler2 instanceof String) {
                String handlerName2 = (String) handler2;
                handler2 = getApplicationContext().getBean(handlerName2);
            }
            validateHandler(handler2, request);
            String pathWithinMapping = getPathMatcher().extractPathWithinPattern(bestMatch, urlPath);
            Map<String, String> uriTemplateVariables = new LinkedHashMap<>();
            for (String matchingPattern : matchingPatterns) {
                if (patternComparator.compare(bestMatch, matchingPattern) == 0) {
                    Map<String, String> vars = getPathMatcher().extractUriTemplateVariables(matchingPattern, urlPath);
                    Map<String, String> decodedVars = getUrlPathHelper().decodePathVariables(request, vars);
                    uriTemplateVariables.putAll(decodedVars);
                }
            }
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("URI Template variables for request [" + urlPath + "] are " + uriTemplateVariables);
            }
            return buildPathExposingHandler(handler2, bestMatch, pathWithinMapping, uriTemplateVariables);
        }
        return null;
    }

    protected void validateHandler(Object handler, HttpServletRequest request) throws Exception {
    }

    protected Object buildPathExposingHandler(Object rawHandler, String bestMatchingPattern, String pathWithinMapping, Map<String, String> uriTemplateVariables) {
        HandlerExecutionChain chain = new HandlerExecutionChain(rawHandler);
        chain.addInterceptor(new PathExposingHandlerInterceptor(bestMatchingPattern, pathWithinMapping));
        if (!CollectionUtils.isEmpty(uriTemplateVariables)) {
            chain.addInterceptor(new UriTemplateVariablesHandlerInterceptor(uriTemplateVariables));
        }
        return chain;
    }

    protected void exposePathWithinMapping(String bestMatchingPattern, String pathWithinMapping, HttpServletRequest request) {
        request.setAttribute(BEST_MATCHING_PATTERN_ATTRIBUTE, bestMatchingPattern);
        request.setAttribute(PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, pathWithinMapping);
    }

    protected void exposeUriTemplateVariables(Map<String, String> uriTemplateVariables, HttpServletRequest request) {
        request.setAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE, uriTemplateVariables);
    }

    @Override // org.springframework.web.servlet.handler.MatchableHandlerMapping
    public RequestMatchResult match(HttpServletRequest request, String pattern) {
        String lookupPath = getUrlPathHelper().getLookupPathForRequest(request);
        if (getPathMatcher().match(pattern, lookupPath)) {
            return new RequestMatchResult(pattern, lookupPath, getPathMatcher());
        }
        if (useTrailingSlashMatch() && !pattern.endsWith("/") && getPathMatcher().match(pattern + "/", lookupPath)) {
            return new RequestMatchResult(pattern + "/", lookupPath, getPathMatcher());
        }
        return null;
    }

    protected void registerHandler(String[] urlPaths, String beanName) throws IllegalStateException, BeansException {
        Assert.notNull(urlPaths, "URL path array must not be null");
        for (String urlPath : urlPaths) {
            registerHandler(urlPath, beanName);
        }
    }

    protected void registerHandler(String urlPath, Object handler) throws IllegalStateException, BeansException {
        Assert.notNull(urlPath, "URL path must not be null");
        Assert.notNull(handler, "Handler object must not be null");
        Object resolvedHandler = handler;
        if (!this.lazyInitHandlers && (handler instanceof String)) {
            String handlerName = (String) handler;
            if (getApplicationContext().isSingleton(handlerName)) {
                resolvedHandler = getApplicationContext().getBean(handlerName);
            }
        }
        Object mappedHandler = this.handlerMap.get(urlPath);
        if (mappedHandler != null) {
            if (mappedHandler != resolvedHandler) {
                throw new IllegalStateException("Cannot map " + getHandlerDescription(handler) + " to URL path [" + urlPath + "]: There is already " + getHandlerDescription(mappedHandler) + " mapped.");
            }
            return;
        }
        if (urlPath.equals("/")) {
            if (this.logger.isInfoEnabled()) {
                this.logger.info("Root mapping to " + getHandlerDescription(handler));
            }
            setRootHandler(resolvedHandler);
        } else if (urlPath.equals(ScriptUtils.DEFAULT_BLOCK_COMMENT_START_DELIMITER)) {
            if (this.logger.isInfoEnabled()) {
                this.logger.info("Default mapping to " + getHandlerDescription(handler));
            }
            setDefaultHandler(resolvedHandler);
        } else {
            this.handlerMap.put(urlPath, resolvedHandler);
            if (this.logger.isInfoEnabled()) {
                this.logger.info("Mapped URL path [" + urlPath + "] onto " + getHandlerDescription(handler));
            }
        }
    }

    private String getHandlerDescription(Object handler) {
        return "handler " + (handler instanceof String ? "'" + handler + "'" : "of type [" + handler.getClass() + "]");
    }

    public final Map<String, Object> getHandlerMap() {
        return Collections.unmodifiableMap(this.handlerMap);
    }

    protected boolean supportsTypeLevelMappings() {
        return false;
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/handler/AbstractUrlHandlerMapping$PathExposingHandlerInterceptor.class */
    private class PathExposingHandlerInterceptor extends HandlerInterceptorAdapter {
        private final String bestMatchingPattern;
        private final String pathWithinMapping;

        public PathExposingHandlerInterceptor(String bestMatchingPattern, String pathWithinMapping) {
            this.bestMatchingPattern = bestMatchingPattern;
            this.pathWithinMapping = pathWithinMapping;
        }

        @Override // org.springframework.web.servlet.handler.HandlerInterceptorAdapter, org.springframework.web.servlet.HandlerInterceptor
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            AbstractUrlHandlerMapping.this.exposePathWithinMapping(this.bestMatchingPattern, this.pathWithinMapping, request);
            request.setAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE, handler);
            request.setAttribute(HandlerMapping.INTROSPECT_TYPE_LEVEL_MAPPING, Boolean.valueOf(AbstractUrlHandlerMapping.this.supportsTypeLevelMappings()));
            return true;
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/handler/AbstractUrlHandlerMapping$UriTemplateVariablesHandlerInterceptor.class */
    private class UriTemplateVariablesHandlerInterceptor extends HandlerInterceptorAdapter {
        private final Map<String, String> uriTemplateVariables;

        public UriTemplateVariablesHandlerInterceptor(Map<String, String> uriTemplateVariables) {
            this.uriTemplateVariables = uriTemplateVariables;
        }

        @Override // org.springframework.web.servlet.handler.HandlerInterceptorAdapter, org.springframework.web.servlet.HandlerInterceptor
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            AbstractUrlHandlerMapping.this.exposeUriTemplateVariables(this.uriTemplateVariables, request);
            return true;
        }
    }
}
