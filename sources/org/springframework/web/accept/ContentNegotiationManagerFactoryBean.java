package org.springframework.web.accept;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.ServletContextAware;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/accept/ContentNegotiationManagerFactoryBean.class */
public class ContentNegotiationManagerFactoryBean implements FactoryBean<ContentNegotiationManager>, ServletContextAware, InitializingBean {
    private Boolean useJaf;
    private ContentNegotiationStrategy defaultNegotiationStrategy;
    private ContentNegotiationManager contentNegotiationManager;
    private ServletContext servletContext;
    private boolean favorPathExtension = true;
    private boolean favorParameter = false;
    private boolean ignoreAcceptHeader = false;
    private Map<String, MediaType> mediaTypes = new HashMap();
    private boolean ignoreUnknownPathExtensions = true;
    private String parameterName = "format";

    public void setFavorPathExtension(boolean favorPathExtension) {
        this.favorPathExtension = favorPathExtension;
    }

    public void setMediaTypes(Properties mediaTypes) {
        if (!CollectionUtils.isEmpty(mediaTypes)) {
            for (Map.Entry<Object, Object> entry : mediaTypes.entrySet()) {
                String extension = ((String) entry.getKey()).toLowerCase(Locale.ENGLISH);
                MediaType mediaType = MediaType.valueOf((String) entry.getValue());
                this.mediaTypes.put(extension, mediaType);
            }
        }
    }

    public void addMediaType(String fileExtension, MediaType mediaType) {
        this.mediaTypes.put(fileExtension, mediaType);
    }

    public void addMediaTypes(Map<String, MediaType> mediaTypes) {
        if (mediaTypes != null) {
            this.mediaTypes.putAll(mediaTypes);
        }
    }

    public void setIgnoreUnknownPathExtensions(boolean ignore) {
        this.ignoreUnknownPathExtensions = ignore;
    }

    public void setUseJaf(boolean useJaf) {
        this.useJaf = Boolean.valueOf(useJaf);
    }

    private boolean isUseJafTurnedOff() {
        return (this.useJaf == null || this.useJaf.booleanValue()) ? false : true;
    }

    public void setFavorParameter(boolean favorParameter) {
        this.favorParameter = favorParameter;
    }

    public void setParameterName(String parameterName) {
        Assert.notNull(parameterName, "parameterName is required");
        this.parameterName = parameterName;
    }

    public void setIgnoreAcceptHeader(boolean ignoreAcceptHeader) {
        this.ignoreAcceptHeader = ignoreAcceptHeader;
    }

    public void setDefaultContentType(MediaType contentType) {
        this.defaultNegotiationStrategy = new FixedContentNegotiationStrategy(contentType);
    }

    public void setDefaultContentTypeStrategy(ContentNegotiationStrategy strategy) {
        this.defaultNegotiationStrategy = strategy;
    }

    @Override // org.springframework.web.context.ServletContextAware
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        PathExtensionContentNegotiationStrategy strategy;
        List<ContentNegotiationStrategy> strategies = new ArrayList<>();
        if (this.favorPathExtension) {
            if (this.servletContext != null && !isUseJafTurnedOff()) {
                strategy = new ServletPathExtensionContentNegotiationStrategy(this.servletContext, this.mediaTypes);
            } else {
                strategy = new PathExtensionContentNegotiationStrategy(this.mediaTypes);
            }
            strategy.setIgnoreUnknownExtensions(this.ignoreUnknownPathExtensions);
            if (this.useJaf != null) {
                strategy.setUseJaf(this.useJaf.booleanValue());
            }
            strategies.add(strategy);
        }
        if (this.favorParameter) {
            ParameterContentNegotiationStrategy strategy2 = new ParameterContentNegotiationStrategy(this.mediaTypes);
            strategy2.setParameterName(this.parameterName);
            strategies.add(strategy2);
        }
        if (!this.ignoreAcceptHeader) {
            strategies.add(new HeaderContentNegotiationStrategy());
        }
        if (this.defaultNegotiationStrategy != null) {
            strategies.add(this.defaultNegotiationStrategy);
        }
        this.contentNegotiationManager = new ContentNegotiationManager(strategies);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.beans.factory.FactoryBean
    public ContentNegotiationManager getObject() {
        return this.contentNegotiationManager;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Class<?> getObjectType() {
        return ContentNegotiationManager.class;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public boolean isSingleton() {
        return true;
    }
}
