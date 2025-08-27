package org.springframework.boot.web.servlet;

import java.util.Locale;
import javax.servlet.MultipartConfigElement;
import org.springframework.util.Assert;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/web/servlet/MultipartConfigFactory.class */
public class MultipartConfigFactory {
    private String location;
    private long maxFileSize = -1;
    private long maxRequestSize = -1;
    private int fileSizeThreshold = 0;

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMaxFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public void setMaxFileSize(String maxFileSize) {
        this.maxFileSize = parseSize(maxFileSize);
    }

    public void setMaxRequestSize(long maxRequestSize) {
        this.maxRequestSize = maxRequestSize;
    }

    public void setMaxRequestSize(String maxRequestSize) {
        this.maxRequestSize = parseSize(maxRequestSize);
    }

    public void setFileSizeThreshold(int fileSizeThreshold) {
        this.fileSizeThreshold = fileSizeThreshold;
    }

    public void setFileSizeThreshold(String fileSizeThreshold) {
        this.fileSizeThreshold = (int) parseSize(fileSizeThreshold);
    }

    private long parseSize(String size) {
        Assert.hasLength(size, "Size must not be empty");
        String size2 = size.toUpperCase(Locale.ENGLISH);
        if (size2.endsWith("KB")) {
            return Long.valueOf(size2.substring(0, size2.length() - 2)).longValue() * 1024;
        }
        if (size2.endsWith("MB")) {
            return Long.valueOf(size2.substring(0, size2.length() - 2)).longValue() * 1024 * 1024;
        }
        return Long.valueOf(size2).longValue();
    }

    public MultipartConfigElement createMultipartConfig() {
        return new MultipartConfigElement(this.location, this.maxFileSize, this.maxRequestSize, this.fileSizeThreshold);
    }
}
