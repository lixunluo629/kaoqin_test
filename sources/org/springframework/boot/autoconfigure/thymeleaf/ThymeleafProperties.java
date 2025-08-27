package org.springframework.boot.autoconfigure.thymeleaf;

import java.nio.charset.Charset;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.MimeType;

@ConfigurationProperties(prefix = "spring.thymeleaf")
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/thymeleaf/ThymeleafProperties.class */
public class ThymeleafProperties {
    private static final Charset DEFAULT_ENCODING = Charset.forName("UTF-8");
    private static final MimeType DEFAULT_CONTENT_TYPE = MimeType.valueOf("text/html");
    public static final String DEFAULT_PREFIX = "classpath:/templates/";
    public static final String DEFAULT_SUFFIX = ".html";
    private Integer templateResolverOrder;
    private String[] viewNames;
    private String[] excludedViewNames;
    private boolean checkTemplate = true;
    private boolean checkTemplateLocation = true;
    private String prefix = "classpath:/templates/";
    private String suffix = ".html";
    private String mode = "HTML5";
    private Charset encoding = DEFAULT_ENCODING;
    private MimeType contentType = DEFAULT_CONTENT_TYPE;
    private boolean cache = true;
    private boolean enabled = true;

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isCheckTemplate() {
        return this.checkTemplate;
    }

    public void setCheckTemplate(boolean checkTemplate) {
        this.checkTemplate = checkTemplate;
    }

    public boolean isCheckTemplateLocation() {
        return this.checkTemplateLocation;
    }

    public void setCheckTemplateLocation(boolean checkTemplateLocation) {
        this.checkTemplateLocation = checkTemplateLocation;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getMode() {
        return this.mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Charset getEncoding() {
        return this.encoding;
    }

    public void setEncoding(Charset encoding) {
        this.encoding = encoding;
    }

    public MimeType getContentType() {
        return this.contentType;
    }

    public void setContentType(MimeType contentType) {
        this.contentType = contentType;
    }

    public boolean isCache() {
        return this.cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    public Integer getTemplateResolverOrder() {
        return this.templateResolverOrder;
    }

    public void setTemplateResolverOrder(Integer templateResolverOrder) {
        this.templateResolverOrder = templateResolverOrder;
    }

    public String[] getExcludedViewNames() {
        return this.excludedViewNames;
    }

    public void setExcludedViewNames(String[] excludedViewNames) {
        this.excludedViewNames = excludedViewNames;
    }

    public String[] getViewNames() {
        return this.viewNames;
    }

    public void setViewNames(String[] viewNames) {
        this.viewNames = viewNames;
    }
}
