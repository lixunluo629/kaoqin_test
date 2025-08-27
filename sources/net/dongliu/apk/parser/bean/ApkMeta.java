package net.dongliu.apk.parser.bean;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/bean/ApkMeta.class */
public class ApkMeta {
    private String packageName;
    private String label;
    private String icon;
    private String versionName;
    private Long versionCode;
    private String installLocation;
    private String minSdkVersion;
    private String targetSdkVersion;

    @Nullable
    private String maxSdkVersion;
    private GlEsVersion glEsVersion;
    private boolean anyDensity;
    private boolean smallScreens;
    private boolean normalScreens;
    private boolean largeScreens;
    private List<String> usesPermissions = new ArrayList();
    private List<UseFeature> usesFeatures = new ArrayList();
    private List<Permission> permissions = new ArrayList();

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionName() {
        return this.versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Long getVersionCode() {
        return this.versionCode;
    }

    public void setVersionCode(Long versionCode) {
        this.versionCode = versionCode;
    }

    public String getMinSdkVersion() {
        return this.minSdkVersion;
    }

    public void setMinSdkVersion(String minSdkVersion) {
        this.minSdkVersion = minSdkVersion;
    }

    public String getTargetSdkVersion() {
        return this.targetSdkVersion;
    }

    public void setTargetSdkVersion(String targetSdkVersion) {
        this.targetSdkVersion = targetSdkVersion;
    }

    @Nullable
    public String getMaxSdkVersion() {
        return this.maxSdkVersion;
    }

    public void setMaxSdkVersion(@Nullable String maxSdkVersion) {
        this.maxSdkVersion = maxSdkVersion;
    }

    public List<String> getUsesPermissions() {
        return this.usesPermissions;
    }

    public void addUsesPermission(String permission) {
        this.usesPermissions.add(permission);
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return this.label;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isAnyDensity() {
        return this.anyDensity;
    }

    public void setAnyDensity(boolean anyDensity) {
        this.anyDensity = anyDensity;
    }

    public boolean isSmallScreens() {
        return this.smallScreens;
    }

    public void setSmallScreens(boolean smallScreens) {
        this.smallScreens = smallScreens;
    }

    public boolean isNormalScreens() {
        return this.normalScreens;
    }

    public void setNormalScreens(boolean normalScreens) {
        this.normalScreens = normalScreens;
    }

    public boolean isLargeScreens() {
        return this.largeScreens;
    }

    public void setLargeScreens(boolean largeScreens) {
        this.largeScreens = largeScreens;
    }

    public GlEsVersion getGlEsVersion() {
        return this.glEsVersion;
    }

    public void setGlEsVersion(GlEsVersion glEsVersion) {
        this.glEsVersion = glEsVersion;
    }

    public List<UseFeature> getUsesFeatures() {
        return this.usesFeatures;
    }

    public void addUseFeatures(UseFeature useFeature) {
        this.usesFeatures.add(useFeature);
    }

    public String getInstallLocation() {
        return this.installLocation;
    }

    public void setInstallLocation(String installLocation) {
        this.installLocation = installLocation;
    }

    public void addPermission(Permission permission) {
        this.permissions.add(permission);
    }

    public List<Permission> getPermissions() {
        return this.permissions;
    }

    public String toString() {
        return "packageName: \t" + this.packageName + "\nlabel: \t" + this.label + "\nicon: \t" + this.icon + "\nversionName: \t" + this.versionName + "\nversionCode: \t" + this.versionCode + "\nminSdkVersion: \t" + this.minSdkVersion + "\ntargetSdkVersion: \t" + this.targetSdkVersion + "\nmaxSdkVersion: \t" + this.maxSdkVersion;
    }
}
