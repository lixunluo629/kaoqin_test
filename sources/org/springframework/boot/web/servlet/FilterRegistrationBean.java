package org.springframework.boot.web.servlet;

import javax.servlet.Filter;
import org.springframework.util.Assert;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/web/servlet/FilterRegistrationBean.class */
public class FilterRegistrationBean extends AbstractFilterRegistrationBean {
    public static final int REQUEST_WRAPPER_FILTER_MAX_ORDER = 0;
    private Filter filter;

    public FilterRegistrationBean() {
        super(new ServletRegistrationBean[0]);
    }

    public FilterRegistrationBean(Filter filter, ServletRegistrationBean... servletRegistrationBeans) {
        super(servletRegistrationBeans);
        Assert.notNull(filter, "Filter must not be null");
        this.filter = filter;
    }

    @Override // org.springframework.boot.web.servlet.AbstractFilterRegistrationBean
    public Filter getFilter() {
        return this.filter;
    }

    public void setFilter(Filter filter) {
        Assert.notNull(filter, "Filter must not be null");
        this.filter = filter;
    }
}
