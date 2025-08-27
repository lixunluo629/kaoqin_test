package org.springframework.web.cors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/cors/CorsConfiguration.class */
public class CorsConfiguration {
    public static final String ALL = "*";
    private static final List<HttpMethod> DEFAULT_METHODS;
    private List<String> allowedOrigins;
    private List<String> allowedMethods;
    private List<HttpMethod> resolvedMethods;
    private List<String> allowedHeaders;
    private List<String> exposedHeaders;
    private Boolean allowCredentials;
    private Long maxAge;

    static {
        List<HttpMethod> rawMethods = new ArrayList<>(2);
        rawMethods.add(HttpMethod.GET);
        rawMethods.add(HttpMethod.HEAD);
        DEFAULT_METHODS = Collections.unmodifiableList(rawMethods);
    }

    public CorsConfiguration() {
        this.resolvedMethods = DEFAULT_METHODS;
    }

    public CorsConfiguration(CorsConfiguration other) {
        this.resolvedMethods = DEFAULT_METHODS;
        this.allowedOrigins = other.allowedOrigins;
        this.allowedMethods = other.allowedMethods;
        this.resolvedMethods = other.resolvedMethods;
        this.allowedHeaders = other.allowedHeaders;
        this.exposedHeaders = other.exposedHeaders;
        this.allowCredentials = other.allowCredentials;
        this.maxAge = other.maxAge;
    }

    public void setAllowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins != null ? new ArrayList(allowedOrigins) : null;
    }

    public List<String> getAllowedOrigins() {
        return this.allowedOrigins;
    }

    public void addAllowedOrigin(String origin) {
        if (this.allowedOrigins == null) {
            this.allowedOrigins = new ArrayList(4);
        }
        this.allowedOrigins.add(origin);
    }

    public void setAllowedMethods(List<String> allowedMethods) {
        this.allowedMethods = allowedMethods != null ? new ArrayList(allowedMethods) : null;
        if (!CollectionUtils.isEmpty(allowedMethods)) {
            this.resolvedMethods = new ArrayList(allowedMethods.size());
            for (String method : allowedMethods) {
                if ("*".equals(method)) {
                    this.resolvedMethods = null;
                    return;
                }
                this.resolvedMethods.add(HttpMethod.resolve(method));
            }
            return;
        }
        this.resolvedMethods = DEFAULT_METHODS;
    }

    public List<String> getAllowedMethods() {
        return this.allowedMethods;
    }

    public void addAllowedMethod(HttpMethod method) {
        if (method != null) {
            addAllowedMethod(method.name());
        }
    }

    public void addAllowedMethod(String method) {
        if (StringUtils.hasText(method)) {
            if (this.allowedMethods == null) {
                this.allowedMethods = new ArrayList(4);
                this.resolvedMethods = new ArrayList(4);
            }
            this.allowedMethods.add(method);
            if ("*".equals(method)) {
                this.resolvedMethods = null;
            } else if (this.resolvedMethods != null) {
                this.resolvedMethods.add(HttpMethod.resolve(method));
            }
        }
    }

    public void setAllowedHeaders(List<String> allowedHeaders) {
        this.allowedHeaders = allowedHeaders != null ? new ArrayList(allowedHeaders) : null;
    }

    public List<String> getAllowedHeaders() {
        return this.allowedHeaders;
    }

    public void addAllowedHeader(String allowedHeader) {
        if (this.allowedHeaders == null) {
            this.allowedHeaders = new ArrayList(4);
        }
        this.allowedHeaders.add(allowedHeader);
    }

    public void setExposedHeaders(List<String> exposedHeaders) {
        if (exposedHeaders != null && exposedHeaders.contains("*")) {
            throw new IllegalArgumentException("'*' is not a valid exposed header value");
        }
        this.exposedHeaders = exposedHeaders != null ? new ArrayList(exposedHeaders) : null;
    }

    public List<String> getExposedHeaders() {
        return this.exposedHeaders;
    }

    public void addExposedHeader(String exposedHeader) {
        if ("*".equals(exposedHeader)) {
            throw new IllegalArgumentException("'*' is not a valid exposed header value");
        }
        if (this.exposedHeaders == null) {
            this.exposedHeaders = new ArrayList(4);
        }
        this.exposedHeaders.add(exposedHeader);
    }

    public void setAllowCredentials(Boolean allowCredentials) {
        this.allowCredentials = allowCredentials;
    }

    public Boolean getAllowCredentials() {
        return this.allowCredentials;
    }

    public void setMaxAge(Long maxAge) {
        this.maxAge = maxAge;
    }

    public Long getMaxAge() {
        return this.maxAge;
    }

    public CorsConfiguration applyPermitDefaultValues() {
        if (this.allowedOrigins == null) {
            addAllowedOrigin("*");
        }
        if (this.allowedMethods == null) {
            setAllowedMethods(Arrays.asList(HttpMethod.GET.name(), HttpMethod.HEAD.name(), HttpMethod.POST.name()));
        }
        if (this.allowedHeaders == null) {
            addAllowedHeader("*");
        }
        if (this.allowCredentials == null) {
            setAllowCredentials(true);
        }
        if (this.maxAge == null) {
            setMaxAge(Long.valueOf(CrossOrigin.DEFAULT_MAX_AGE));
        }
        return this;
    }

    public CorsConfiguration combine(CorsConfiguration other) {
        if (other == null) {
            return this;
        }
        CorsConfiguration config = new CorsConfiguration(this);
        config.setAllowedOrigins(combine(getAllowedOrigins(), other.getAllowedOrigins()));
        config.setAllowedMethods(combine(getAllowedMethods(), other.getAllowedMethods()));
        config.setAllowedHeaders(combine(getAllowedHeaders(), other.getAllowedHeaders()));
        config.setExposedHeaders(combine(getExposedHeaders(), other.getExposedHeaders()));
        Boolean allowCredentials = other.getAllowCredentials();
        if (allowCredentials != null) {
            config.setAllowCredentials(allowCredentials);
        }
        Long maxAge = other.getMaxAge();
        if (maxAge != null) {
            config.setMaxAge(maxAge);
        }
        return config;
    }

    private List<String> combine(List<String> source, List<String> other) {
        if (other == null || other.contains("*")) {
            return source;
        }
        if (source == null || source.contains("*")) {
            return other;
        }
        Set<String> combined = new LinkedHashSet<>(source);
        combined.addAll(other);
        return new ArrayList(combined);
    }

    public String checkOrigin(String requestOrigin) {
        if (!StringUtils.hasText(requestOrigin) || ObjectUtils.isEmpty(this.allowedOrigins)) {
            return null;
        }
        if (this.allowedOrigins.contains("*")) {
            if (this.allowCredentials != Boolean.TRUE) {
                return "*";
            }
            return requestOrigin;
        }
        for (String allowedOrigin : this.allowedOrigins) {
            if (requestOrigin.equalsIgnoreCase(allowedOrigin)) {
                return requestOrigin;
            }
        }
        return null;
    }

    public List<HttpMethod> checkHttpMethod(HttpMethod requestMethod) {
        if (requestMethod == null) {
            return null;
        }
        if (this.resolvedMethods == null) {
            return Collections.singletonList(requestMethod);
        }
        if (this.resolvedMethods.contains(requestMethod)) {
            return this.resolvedMethods;
        }
        return null;
    }

    public List<String> checkHeaders(List<String> requestHeaders) {
        if (requestHeaders == null) {
            return null;
        }
        if (requestHeaders.isEmpty()) {
            return Collections.emptyList();
        }
        if (ObjectUtils.isEmpty(this.allowedHeaders)) {
            return null;
        }
        boolean allowAnyHeader = this.allowedHeaders.contains("*");
        List<String> result = new ArrayList<>(requestHeaders.size());
        for (String requestHeader : requestHeaders) {
            if (StringUtils.hasText(requestHeader)) {
                String requestHeader2 = requestHeader.trim();
                if (allowAnyHeader) {
                    result.add(requestHeader2);
                } else {
                    Iterator<String> it = this.allowedHeaders.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            String allowedHeader = it.next();
                            if (requestHeader2.equalsIgnoreCase(allowedHeader)) {
                                result.add(requestHeader2);
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }
}
