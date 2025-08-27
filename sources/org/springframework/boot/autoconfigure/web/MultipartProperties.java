package org.springframework.boot.autoconfigure.web;

import javax.servlet.MultipartConfigElement;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.util.StringUtils;

@ConfigurationProperties(prefix = "spring.http.multipart", ignoreUnknownFields = false)
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/MultipartProperties.class */
public class MultipartProperties {
    private String location;
    private boolean enabled = true;
    private String maxFileSize = "1MB";
    private String maxRequestSize = "10MB";
    private String fileSizeThreshold = "0";
    private boolean resolveLazily = false;

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMaxFileSize() {
        return this.maxFileSize;
    }

    public void setMaxFileSize(String maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public String getMaxRequestSize() {
        return this.maxRequestSize;
    }

    public void setMaxRequestSize(String maxRequestSize) {
        this.maxRequestSize = maxRequestSize;
    }

    public String getFileSizeThreshold() {
        return this.fileSizeThreshold;
    }

    public void setFileSizeThreshold(String fileSizeThreshold) {
        this.fileSizeThreshold = fileSizeThreshold;
    }

    public boolean isResolveLazily() {
        return this.resolveLazily;
    }

    public void setResolveLazily(boolean resolveLazily) {
        this.resolveLazily = resolveLazily;
    }

    public MultipartConfigElement createMultipartConfig() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        if (StringUtils.hasText(this.fileSizeThreshold)) {
            factory.setFileSizeThreshold(this.fileSizeThreshold);
        }
        if (StringUtils.hasText(this.location)) {
            factory.setLocation(this.location);
        }
        if (StringUtils.hasText(this.maxRequestSize)) {
            factory.setMaxRequestSize(this.maxRequestSize);
        }
        if (StringUtils.hasText(this.maxFileSize)) {
            factory.setMaxFileSize(this.maxFileSize);
        }
        return factory.createMultipartConfig();
    }
}
