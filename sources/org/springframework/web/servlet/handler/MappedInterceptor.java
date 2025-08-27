package org.springframework.web.servlet.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PathMatcher;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/handler/MappedInterceptor.class */
public final class MappedInterceptor implements HandlerInterceptor {
    private final String[] includePatterns;
    private final String[] excludePatterns;
    private final HandlerInterceptor interceptor;
    private PathMatcher pathMatcher;

    public MappedInterceptor(String[] includePatterns, HandlerInterceptor interceptor) {
        this(includePatterns, (String[]) null, interceptor);
    }

    public MappedInterceptor(String[] includePatterns, String[] excludePatterns, HandlerInterceptor interceptor) {
        this.includePatterns = includePatterns;
        this.excludePatterns = excludePatterns;
        this.interceptor = interceptor;
    }

    public MappedInterceptor(String[] includePatterns, WebRequestInterceptor interceptor) {
        this(includePatterns, (String[]) null, interceptor);
    }

    public MappedInterceptor(String[] includePatterns, String[] excludePatterns, WebRequestInterceptor interceptor) {
        this(includePatterns, excludePatterns, new WebRequestHandlerInterceptorAdapter(interceptor));
    }

    public void setPathMatcher(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }

    public PathMatcher getPathMatcher() {
        return this.pathMatcher;
    }

    public String[] getPathPatterns() {
        return this.includePatterns;
    }

    public HandlerInterceptor getInterceptor() {
        return this.interceptor;
    }

    public boolean matches(String lookupPath, PathMatcher pathMatcher) {
        PathMatcher pathMatcherToUse = this.pathMatcher != null ? this.pathMatcher : pathMatcher;
        if (!ObjectUtils.isEmpty((Object[]) this.excludePatterns)) {
            for (String pattern : this.excludePatterns) {
                if (pathMatcherToUse.match(pattern, lookupPath)) {
                    return false;
                }
            }
        }
        if (ObjectUtils.isEmpty((Object[]) this.includePatterns)) {
            return true;
        }
        for (String pattern2 : this.includePatterns) {
            if (pathMatcherToUse.match(pattern2, lookupPath)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.springframework.web.servlet.HandlerInterceptor
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return this.interceptor.preHandle(request, response, handler);
    }

    @Override // org.springframework.web.servlet.HandlerInterceptor
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        this.interceptor.postHandle(request, response, handler, modelAndView);
    }

    @Override // org.springframework.web.servlet.HandlerInterceptor
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        this.interceptor.afterCompletion(request, response, handler, ex);
    }
}
