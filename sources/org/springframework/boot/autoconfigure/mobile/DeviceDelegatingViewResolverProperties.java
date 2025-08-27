package org.springframework.boot.autoconfigure.mobile;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.mobile.devicedelegatingviewresolver")
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/mobile/DeviceDelegatingViewResolverProperties.class */
public class DeviceDelegatingViewResolverProperties {
    private boolean enableFallback;
    private String normalPrefix = "";
    private String normalSuffix = "";
    private String mobilePrefix = "mobile/";
    private String mobileSuffix = "";
    private String tabletPrefix = "tablet/";
    private String tabletSuffix = "";

    public void setEnableFallback(boolean enableFallback) {
        this.enableFallback = enableFallback;
    }

    public boolean isEnableFallback() {
        return this.enableFallback;
    }

    public String getNormalPrefix() {
        return this.normalPrefix;
    }

    public void setNormalPrefix(String normalPrefix) {
        this.normalPrefix = normalPrefix;
    }

    public String getNormalSuffix() {
        return this.normalSuffix;
    }

    public void setNormalSuffix(String normalSuffix) {
        this.normalSuffix = normalSuffix;
    }

    public String getMobilePrefix() {
        return this.mobilePrefix;
    }

    public void setMobilePrefix(String mobilePrefix) {
        this.mobilePrefix = mobilePrefix;
    }

    public String getMobileSuffix() {
        return this.mobileSuffix;
    }

    public void setMobileSuffix(String mobileSuffix) {
        this.mobileSuffix = mobileSuffix;
    }

    public String getTabletPrefix() {
        return this.tabletPrefix;
    }

    public void setTabletPrefix(String tabletPrefix) {
        this.tabletPrefix = tabletPrefix;
    }

    public String getTabletSuffix() {
        return this.tabletSuffix;
    }

    public void setTabletSuffix(String tabletSuffix) {
        this.tabletSuffix = tabletSuffix;
    }
}
