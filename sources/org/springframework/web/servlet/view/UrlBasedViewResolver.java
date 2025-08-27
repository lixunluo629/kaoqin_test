package org.springframework.web.servlet.view;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import org.springframework.beans.BeanUtils;
import org.springframework.core.Ordered;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.servlet.View;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/UrlBasedViewResolver.class */
public class UrlBasedViewResolver extends AbstractCachingViewResolver implements Ordered {
    public static final String REDIRECT_URL_PREFIX = "redirect:";
    public static final String FORWARD_URL_PREFIX = "forward:";
    private Class<?> viewClass;
    private String contentType;
    private String[] redirectHosts;
    private String requestContextAttribute;
    private Boolean exposePathVariables;
    private Boolean exposeContextBeansAsAttributes;
    private String[] exposedContextBeanNames;
    private String[] viewNames;
    private String prefix = "";
    private String suffix = "";
    private boolean redirectContextRelative = true;
    private boolean redirectHttp10Compatible = true;
    private final Map<String, Object> staticAttributes = new HashMap();
    private int order = Integer.MAX_VALUE;

    public void setViewClass(Class<?> viewClass) {
        if (viewClass == null || !requiredViewClass().isAssignableFrom(viewClass)) {
            throw new IllegalArgumentException("Given view class [" + (viewClass != null ? viewClass.getName() : null) + "] is not of type [" + requiredViewClass().getName() + "]");
        }
        this.viewClass = viewClass;
    }

    protected Class<?> getViewClass() {
        return this.viewClass;
    }

    protected Class<?> requiredViewClass() {
        return AbstractUrlBasedView.class;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix != null ? prefix : "";
    }

    protected String getPrefix() {
        return this.prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix != null ? suffix : "";
    }

    protected String getSuffix() {
        return this.suffix;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    protected String getContentType() {
        return this.contentType;
    }

    public void setRedirectContextRelative(boolean redirectContextRelative) {
        this.redirectContextRelative = redirectContextRelative;
    }

    protected boolean isRedirectContextRelative() {
        return this.redirectContextRelative;
    }

    public void setRedirectHttp10Compatible(boolean redirectHttp10Compatible) {
        this.redirectHttp10Compatible = redirectHttp10Compatible;
    }

    protected boolean isRedirectHttp10Compatible() {
        return this.redirectHttp10Compatible;
    }

    public void setRedirectHosts(String... redirectHosts) {
        this.redirectHosts = redirectHosts;
    }

    public String[] getRedirectHosts() {
        return this.redirectHosts;
    }

    public void setRequestContextAttribute(String requestContextAttribute) {
        this.requestContextAttribute = requestContextAttribute;
    }

    protected String getRequestContextAttribute() {
        return this.requestContextAttribute;
    }

    public void setAttributes(Properties props) {
        CollectionUtils.mergePropertiesIntoMap(props, this.staticAttributes);
    }

    public void setAttributesMap(Map<String, ?> attributes) {
        if (attributes != null) {
            this.staticAttributes.putAll(attributes);
        }
    }

    public Map<String, Object> getAttributesMap() {
        return this.staticAttributes;
    }

    public void setExposePathVariables(Boolean exposePathVariables) {
        this.exposePathVariables = exposePathVariables;
    }

    protected Boolean getExposePathVariables() {
        return this.exposePathVariables;
    }

    public void setExposeContextBeansAsAttributes(boolean exposeContextBeansAsAttributes) {
        this.exposeContextBeansAsAttributes = Boolean.valueOf(exposeContextBeansAsAttributes);
    }

    protected Boolean getExposeContextBeansAsAttributes() {
        return this.exposeContextBeansAsAttributes;
    }

    public void setExposedContextBeanNames(String... exposedContextBeanNames) {
        this.exposedContextBeanNames = exposedContextBeanNames;
    }

    protected String[] getExposedContextBeanNames() {
        return this.exposedContextBeanNames;
    }

    public void setViewNames(String... viewNames) {
        this.viewNames = viewNames;
    }

    protected String[] getViewNames() {
        return this.viewNames;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return this.order;
    }

    @Override // org.springframework.context.support.ApplicationObjectSupport
    protected void initApplicationContext() {
        super.initApplicationContext();
        if (getViewClass() == null) {
            throw new IllegalArgumentException("Property 'viewClass' is required");
        }
    }

    @Override // org.springframework.web.servlet.view.AbstractCachingViewResolver
    protected Object getCacheKey(String viewName, Locale locale) {
        return viewName;
    }

    @Override // org.springframework.web.servlet.view.AbstractCachingViewResolver
    protected View createView(String viewName, Locale locale) throws Exception {
        if (!canHandle(viewName, locale)) {
            return null;
        }
        if (viewName.startsWith(REDIRECT_URL_PREFIX)) {
            String redirectUrl = viewName.substring(REDIRECT_URL_PREFIX.length());
            RedirectView view = new RedirectView(redirectUrl, isRedirectContextRelative(), isRedirectHttp10Compatible());
            view.setHosts(getRedirectHosts());
            return applyLifecycleMethods(REDIRECT_URL_PREFIX, view);
        }
        if (viewName.startsWith(FORWARD_URL_PREFIX)) {
            String forwardUrl = viewName.substring(FORWARD_URL_PREFIX.length());
            return new InternalResourceView(forwardUrl);
        }
        return super.createView(viewName, locale);
    }

    protected boolean canHandle(String viewName, Locale locale) {
        String[] viewNames = getViewNames();
        return viewNames == null || PatternMatchUtils.simpleMatch(viewNames, viewName);
    }

    @Override // org.springframework.web.servlet.view.AbstractCachingViewResolver
    protected View loadView(String viewName, Locale locale) throws Exception {
        AbstractUrlBasedView view = buildView(viewName);
        View result = applyLifecycleMethods(viewName, view);
        if (view.checkResource(locale)) {
            return result;
        }
        return null;
    }

    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        AbstractUrlBasedView view = (AbstractUrlBasedView) BeanUtils.instantiateClass(getViewClass());
        view.setUrl(getPrefix() + viewName + getSuffix());
        String contentType = getContentType();
        if (contentType != null) {
            view.setContentType(contentType);
        }
        view.setRequestContextAttribute(getRequestContextAttribute());
        view.setAttributesMap(getAttributesMap());
        Boolean exposePathVariables = getExposePathVariables();
        if (exposePathVariables != null) {
            view.setExposePathVariables(exposePathVariables.booleanValue());
        }
        Boolean exposeContextBeansAsAttributes = getExposeContextBeansAsAttributes();
        if (exposeContextBeansAsAttributes != null) {
            view.setExposeContextBeansAsAttributes(exposeContextBeansAsAttributes.booleanValue());
        }
        String[] exposedContextBeanNames = getExposedContextBeanNames();
        if (exposedContextBeanNames != null) {
            view.setExposedContextBeanNames(exposedContextBeanNames);
        }
        return view;
    }

    private View applyLifecycleMethods(String viewName, AbstractView view) {
        return (View) getApplicationContext().getAutowireCapableBeanFactory().initializeBean(view, viewName);
    }
}
