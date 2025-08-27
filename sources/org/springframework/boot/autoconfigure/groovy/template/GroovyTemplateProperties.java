package org.springframework.boot.autoconfigure.groovy.template;

import org.springframework.boot.autoconfigure.template.AbstractTemplateViewResolverProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.groovy.template", ignoreUnknownFields = true)
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/groovy/template/GroovyTemplateProperties.class */
public class GroovyTemplateProperties extends AbstractTemplateViewResolverProperties {
    public static final String DEFAULT_RESOURCE_LOADER_PATH = "classpath:/templates/";
    public static final String DEFAULT_PREFIX = "";
    public static final String DEFAULT_SUFFIX = ".tpl";
    public static final String DEFAULT_REQUEST_CONTEXT_ATTRIBUTE = "spring";
    private String resourceLoaderPath;

    public GroovyTemplateProperties() {
        super("", DEFAULT_SUFFIX);
        this.resourceLoaderPath = "classpath:/templates/";
        setRequestContextAttribute("spring");
    }

    public String getResourceLoaderPath() {
        return this.resourceLoaderPath;
    }

    public void setResourceLoaderPath(String resourceLoaderPath) {
        this.resourceLoaderPath = resourceLoaderPath;
    }
}
