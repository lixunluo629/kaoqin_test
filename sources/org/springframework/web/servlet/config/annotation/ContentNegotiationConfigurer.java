package org.springframework.web.servlet.config.annotation;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
import org.springframework.web.accept.ContentNegotiationStrategy;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/config/annotation/ContentNegotiationConfigurer.class */
public class ContentNegotiationConfigurer {
    private final ContentNegotiationManagerFactoryBean factory = new ContentNegotiationManagerFactoryBean();
    private final Map<String, MediaType> mediaTypes = new HashMap();

    public ContentNegotiationConfigurer(ServletContext servletContext) {
        this.factory.setServletContext(servletContext);
    }

    public ContentNegotiationConfigurer favorPathExtension(boolean favorPathExtension) {
        this.factory.setFavorPathExtension(favorPathExtension);
        return this;
    }

    public ContentNegotiationConfigurer mediaType(String extension, MediaType mediaType) {
        this.mediaTypes.put(extension, mediaType);
        return this;
    }

    public ContentNegotiationConfigurer mediaTypes(Map<String, MediaType> mediaTypes) {
        if (mediaTypes != null) {
            this.mediaTypes.putAll(mediaTypes);
        }
        return this;
    }

    public ContentNegotiationConfigurer replaceMediaTypes(Map<String, MediaType> mediaTypes) {
        this.mediaTypes.clear();
        mediaTypes(mediaTypes);
        return this;
    }

    public ContentNegotiationConfigurer ignoreUnknownPathExtensions(boolean ignore) {
        this.factory.setIgnoreUnknownPathExtensions(ignore);
        return this;
    }

    public ContentNegotiationConfigurer useJaf(boolean useJaf) {
        this.factory.setUseJaf(useJaf);
        return this;
    }

    public ContentNegotiationConfigurer favorParameter(boolean favorParameter) {
        this.factory.setFavorParameter(favorParameter);
        return this;
    }

    public ContentNegotiationConfigurer parameterName(String parameterName) {
        this.factory.setParameterName(parameterName);
        return this;
    }

    public ContentNegotiationConfigurer ignoreAcceptHeader(boolean ignoreAcceptHeader) {
        this.factory.setIgnoreAcceptHeader(ignoreAcceptHeader);
        return this;
    }

    public ContentNegotiationConfigurer defaultContentType(MediaType defaultContentType) {
        this.factory.setDefaultContentType(defaultContentType);
        return this;
    }

    public ContentNegotiationConfigurer defaultContentTypeStrategy(ContentNegotiationStrategy defaultStrategy) {
        this.factory.setDefaultContentTypeStrategy(defaultStrategy);
        return this;
    }

    protected ContentNegotiationManager buildContentNegotiationManager() {
        this.factory.addMediaTypes(this.mediaTypes);
        this.factory.afterPropertiesSet();
        return this.factory.getObject();
    }

    @Deprecated
    protected ContentNegotiationManager getContentNegotiationManager() throws Exception {
        return buildContentNegotiationManager();
    }
}
