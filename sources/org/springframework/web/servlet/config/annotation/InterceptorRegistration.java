package org.springframework.web.servlet.config.annotation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.MappedInterceptor;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/config/annotation/InterceptorRegistration.class */
public class InterceptorRegistration {
    private final HandlerInterceptor interceptor;
    private PathMatcher pathMatcher;
    private final List<String> includePatterns = new ArrayList();
    private final List<String> excludePatterns = new ArrayList();
    private int order = 0;

    public InterceptorRegistration(HandlerInterceptor interceptor) {
        Assert.notNull(interceptor, "Interceptor is required");
        this.interceptor = interceptor;
    }

    public InterceptorRegistration addPathPatterns(String... patterns) {
        this.includePatterns.addAll(Arrays.asList(patterns));
        return this;
    }

    public InterceptorRegistration excludePathPatterns(String... patterns) {
        this.excludePatterns.addAll(Arrays.asList(patterns));
        return this;
    }

    public InterceptorRegistration pathMatcher(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
        return this;
    }

    public InterceptorRegistration order(int order) {
        this.order = order;
        return this;
    }

    protected Object getInterceptor() {
        if (this.includePatterns.isEmpty() && this.excludePatterns.isEmpty()) {
            return this.interceptor;
        }
        String[] include = StringUtils.toStringArray(this.includePatterns);
        String[] exclude = StringUtils.toStringArray(this.excludePatterns);
        MappedInterceptor mappedInterceptor = new MappedInterceptor(include, exclude, this.interceptor);
        if (this.pathMatcher != null) {
            mappedInterceptor.setPathMatcher(this.pathMatcher);
        }
        return mappedInterceptor;
    }

    Ordered toOrdered() {
        return new Ordered() { // from class: org.springframework.web.servlet.config.annotation.InterceptorRegistration.1
            @Override // org.springframework.core.Ordered
            public int getOrder() {
                return InterceptorRegistration.this.order;
            }
        };
    }
}
