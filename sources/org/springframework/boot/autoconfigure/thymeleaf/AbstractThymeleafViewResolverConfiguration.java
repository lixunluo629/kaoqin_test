package org.springframework.boot.autoconfigure.thymeleaf;

import java.util.LinkedHashMap;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.MimeType;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/thymeleaf/AbstractThymeleafViewResolverConfiguration.class */
abstract class AbstractThymeleafViewResolverConfiguration {
    private final ThymeleafProperties properties;
    private final SpringTemplateEngine templateEngine;

    protected abstract void configureTemplateEngine(ThymeleafViewResolver thymeleafViewResolver, SpringTemplateEngine springTemplateEngine);

    protected AbstractThymeleafViewResolverConfiguration(ThymeleafProperties properties, SpringTemplateEngine templateEngine) {
        this.properties = properties;
        this.templateEngine = templateEngine;
    }

    @ConditionalOnMissingBean(name = {"thymeleafViewResolver"})
    @ConditionalOnProperty(name = {"spring.thymeleaf.enabled"}, matchIfMissing = true)
    @Bean
    public ThymeleafViewResolver thymeleafViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        configureTemplateEngine(resolver, this.templateEngine);
        resolver.setCharacterEncoding(this.properties.getEncoding().name());
        resolver.setContentType(appendCharset(this.properties.getContentType(), resolver.getCharacterEncoding()));
        resolver.setExcludedViewNames(this.properties.getExcludedViewNames());
        resolver.setViewNames(this.properties.getViewNames());
        resolver.setOrder(SecurityProperties.BASIC_AUTH_ORDER);
        resolver.setCache(this.properties.isCache());
        return resolver;
    }

    private String appendCharset(MimeType type, String charset) {
        if (type.getCharset() != null) {
            return type.toString();
        }
        LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
        parameters.put("charset", charset);
        parameters.putAll(type.getParameters());
        return new MimeType(type, parameters).toString();
    }
}
