package org.springframework.boot.autoconfigure.mustache;

import org.springframework.boot.autoconfigure.template.AbstractTemplateViewResolverProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.mustache")
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/mustache/MustacheProperties.class */
public class MustacheProperties extends AbstractTemplateViewResolverProperties {
    public static final String DEFAULT_PREFIX = "classpath:/templates/";
    public static final String DEFAULT_SUFFIX = ".html";
    private String prefix;
    private String suffix;

    public MustacheProperties() {
        super("classpath:/templates/", ".html");
        this.prefix = "classpath:/templates/";
        this.suffix = ".html";
    }

    @Override // org.springframework.boot.autoconfigure.template.AbstractTemplateViewResolverProperties
    public String getPrefix() {
        return this.prefix;
    }

    @Override // org.springframework.boot.autoconfigure.template.AbstractTemplateViewResolverProperties
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override // org.springframework.boot.autoconfigure.template.AbstractTemplateViewResolverProperties
    public String getSuffix() {
        return this.suffix;
    }

    @Override // org.springframework.boot.autoconfigure.template.AbstractTemplateViewResolverProperties
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
