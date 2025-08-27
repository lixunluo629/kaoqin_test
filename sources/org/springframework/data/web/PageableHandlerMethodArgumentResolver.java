package org.springframework.data.web;

import java.lang.reflect.Method;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/PageableHandlerMethodArgumentResolver.class */
public class PageableHandlerMethodArgumentResolver implements PageableArgumentResolver {
    private static final String INVALID_DEFAULT_PAGE_SIZE = "Invalid default page size configured for method %s! Must not be less than one!";
    private static final String DEFAULT_PAGE_PARAMETER = "page";
    private static final String DEFAULT_SIZE_PARAMETER = "size";
    private static final String DEFAULT_PREFIX = "";
    private static final String DEFAULT_QUALIFIER_DELIMITER = "_";
    private static final int DEFAULT_MAX_PAGE_SIZE = 2000;
    private Pageable fallbackPageable;
    private SortArgumentResolver sortResolver;
    private String pageParameterName;
    private String sizeParameterName;
    private String prefix;
    private String qualifierDelimiter;
    private int maxPageSize;
    private boolean oneIndexedParameters;
    private static final SortHandlerMethodArgumentResolver DEFAULT_SORT_RESOLVER = new SortHandlerMethodArgumentResolver();
    static final Pageable DEFAULT_PAGE_REQUEST = new PageRequest(0, 20);

    public PageableHandlerMethodArgumentResolver() {
        this((SortArgumentResolver) null);
    }

    public PageableHandlerMethodArgumentResolver(SortHandlerMethodArgumentResolver sortResolver) {
        this((SortArgumentResolver) sortResolver);
    }

    public PageableHandlerMethodArgumentResolver(SortArgumentResolver sortResolver) {
        this.fallbackPageable = DEFAULT_PAGE_REQUEST;
        this.pageParameterName = "page";
        this.sizeParameterName = "size";
        this.prefix = "";
        this.qualifierDelimiter = "_";
        this.maxPageSize = 2000;
        this.oneIndexedParameters = false;
        this.sortResolver = sortResolver == null ? DEFAULT_SORT_RESOLVER : sortResolver;
    }

    public void setFallbackPageable(Pageable fallbackPageable) {
        this.fallbackPageable = fallbackPageable;
    }

    public boolean isFallbackPageable(Pageable pageable) {
        if (this.fallbackPageable == null) {
            return false;
        }
        return this.fallbackPageable.equals(pageable);
    }

    public void setMaxPageSize(int maxPageSize) {
        this.maxPageSize = maxPageSize;
    }

    protected int getMaxPageSize() {
        return this.maxPageSize;
    }

    public void setPageParameterName(String pageParameterName) {
        Assert.hasText(pageParameterName, "Page parameter name must not be null or empty!");
        this.pageParameterName = pageParameterName;
    }

    protected String getPageParameterName() {
        return this.pageParameterName;
    }

    public void setSizeParameterName(String sizeParameterName) {
        Assert.hasText(sizeParameterName, "Size parameter name must not be null or empty!");
        this.sizeParameterName = sizeParameterName;
    }

    protected String getSizeParameterName() {
        return this.sizeParameterName;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix == null ? "" : prefix;
    }

    public void setQualifierDelimiter(String qualifierDelimiter) {
        this.qualifierDelimiter = qualifierDelimiter == null ? "_" : qualifierDelimiter;
    }

    public void setOneIndexedParameters(boolean oneIndexedParameters) {
        this.oneIndexedParameters = oneIndexedParameters;
    }

    protected boolean isOneIndexedParameters() {
        return this.oneIndexedParameters;
    }

    @Override // org.springframework.web.method.support.HandlerMethodArgumentResolver
    public boolean supportsParameter(MethodParameter parameter) {
        return Pageable.class.equals(parameter.getParameterType());
    }

    @Override // org.springframework.web.method.support.HandlerMethodArgumentResolver
    public Pageable resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        SpringDataAnnotationUtils.assertPageableUniqueness(methodParameter);
        Pageable defaultOrFallback = getDefaultFromAnnotationOrFallback(methodParameter);
        String pageString = webRequest.getParameter(getParameterNameToUse(this.pageParameterName, methodParameter));
        String pageSizeString = webRequest.getParameter(getParameterNameToUse(this.sizeParameterName, methodParameter));
        boolean pageAndSizeGiven = StringUtils.hasText(pageString) && StringUtils.hasText(pageSizeString);
        if (!pageAndSizeGiven && defaultOrFallback == null) {
            return null;
        }
        int page = StringUtils.hasText(pageString) ? parseAndApplyBoundaries(pageString, Integer.MAX_VALUE, true) : defaultOrFallback.getPageNumber();
        int pageSize = StringUtils.hasText(pageSizeString) ? parseAndApplyBoundaries(pageSizeString, this.maxPageSize, false) : defaultOrFallback.getPageSize();
        int pageSize2 = pageSize < 1 ? defaultOrFallback.getPageSize() : pageSize;
        int pageSize3 = pageSize2 > this.maxPageSize ? this.maxPageSize : pageSize2;
        Sort sort = this.sortResolver.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
        return new PageRequest(page, pageSize3, (sort != null || defaultOrFallback == null) ? sort : defaultOrFallback.getSort());
    }

    protected String getParameterNameToUse(String source, MethodParameter parameter) {
        StringBuilder builder = new StringBuilder(this.prefix);
        if (parameter != null && parameter.hasParameterAnnotation(Qualifier.class)) {
            builder.append(((Qualifier) parameter.getParameterAnnotation(Qualifier.class)).value());
            builder.append(this.qualifierDelimiter);
        }
        return builder.append(source).toString();
    }

    private Pageable getDefaultFromAnnotationOrFallback(MethodParameter methodParameter) {
        if (methodParameter.hasParameterAnnotation(PageableDefault.class)) {
            return getDefaultPageRequestFrom(methodParameter);
        }
        return this.fallbackPageable;
    }

    private static Pageable getDefaultPageRequestFrom(MethodParameter parameter) {
        PageableDefault defaults = (PageableDefault) parameter.getParameterAnnotation(PageableDefault.class);
        Integer defaultPageNumber = Integer.valueOf(defaults.page());
        Integer defaultPageSize = (Integer) SpringDataAnnotationUtils.getSpecificPropertyOrDefaultFromValue(defaults, "size");
        if (defaultPageSize.intValue() < 1) {
            Method annotatedMethod = parameter.getMethod();
            throw new IllegalStateException(String.format(INVALID_DEFAULT_PAGE_SIZE, annotatedMethod));
        }
        if (defaults.sort().length == 0) {
            return new PageRequest(defaultPageNumber.intValue(), defaultPageSize.intValue());
        }
        return new PageRequest(defaultPageNumber.intValue(), defaultPageSize.intValue(), defaults.direction(), defaults.sort());
    }

    private int parseAndApplyBoundaries(String parameter, int upper, boolean shiftIndex) {
        try {
            int parsed = Integer.parseInt(parameter) - ((this.oneIndexedParameters && shiftIndex) ? 1 : 0);
            if (parsed < 0) {
                return 0;
            }
            return parsed > upper ? upper : parsed;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
