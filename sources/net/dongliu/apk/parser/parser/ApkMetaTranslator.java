package net.dongliu.apk.parser.parser;

import com.moredian.onpremise.core.common.constants.UpgradeConstants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.dongliu.apk.parser.bean.ApkMeta;
import net.dongliu.apk.parser.bean.GlEsVersion;
import net.dongliu.apk.parser.bean.IconPath;
import net.dongliu.apk.parser.bean.Permission;
import net.dongliu.apk.parser.bean.UseFeature;
import net.dongliu.apk.parser.struct.ResourceValue;
import net.dongliu.apk.parser.struct.resource.ResourceEntry;
import net.dongliu.apk.parser.struct.resource.ResourceTable;
import net.dongliu.apk.parser.struct.resource.Type;
import net.dongliu.apk.parser.struct.xml.Attribute;
import net.dongliu.apk.parser.struct.xml.Attributes;
import net.dongliu.apk.parser.struct.xml.XmlCData;
import net.dongliu.apk.parser.struct.xml.XmlNamespaceEndTag;
import net.dongliu.apk.parser.struct.xml.XmlNamespaceStartTag;
import net.dongliu.apk.parser.struct.xml.XmlNodeEndTag;
import net.dongliu.apk.parser.struct.xml.XmlNodeStartTag;
import org.bouncycastle.jcajce.util.AnnotatedPrivateKey;
import org.springframework.validation.DefaultBindingErrorProcessor;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/parser/ApkMetaTranslator.class */
public class ApkMetaTranslator implements XmlStreamer {
    private String[] tagStack = new String[100];
    private int depth = 0;
    private ApkMeta apkMeta = new ApkMeta();
    private List<IconPath> iconPaths = Collections.emptyList();

    @Nonnull
    private ResourceTable resourceTable;

    @Nullable
    private Locale locale;

    public ApkMetaTranslator(@Nonnull ResourceTable resourceTable, @Nullable Locale locale) {
        this.resourceTable = (ResourceTable) Objects.requireNonNull(resourceTable);
        this.locale = locale;
    }

    @Override // net.dongliu.apk.parser.parser.XmlStreamer
    public void onStartTag(XmlNodeStartTag xmlNodeStartTag) {
        Attributes attributes;
        attributes = xmlNodeStartTag.getAttributes();
        switch (xmlNodeStartTag.getName()) {
            case "application":
                String label = attributes.getString(AnnotatedPrivateKey.LABEL);
                if (label != null) {
                    this.apkMeta.setLabel(label);
                }
                Attribute iconAttr = attributes.get("icon");
                if (iconAttr != null) {
                    ResourceValue resourceValue = iconAttr.getTypedValue();
                    if (resourceValue != null && (resourceValue instanceof ResourceValue.ReferenceResourceValue)) {
                        long resourceId = ((ResourceValue.ReferenceResourceValue) resourceValue).getReferenceResourceId();
                        List<ResourceTable.Resource> resources = this.resourceTable.getResourcesById(resourceId);
                        if (!resources.isEmpty()) {
                            List<IconPath> icons = new ArrayList<>();
                            boolean hasDefault = false;
                            for (ResourceTable.Resource resource : resources) {
                                Type type = resource.getType();
                                ResourceEntry resourceEntry = resource.getResourceEntry();
                                String path = resourceEntry.toStringValue(this.resourceTable, this.locale);
                                if (type.getDensity() == 0) {
                                    hasDefault = true;
                                    this.apkMeta.setIcon(path);
                                }
                                IconPath iconPath = new IconPath(path, type.getDensity());
                                icons.add(iconPath);
                            }
                            if (!hasDefault) {
                                this.apkMeta.setIcon(icons.get(0).getPath());
                            }
                            this.iconPaths = icons;
                            break;
                        }
                    } else {
                        String value = iconAttr.getValue();
                        if (value != null) {
                            this.apkMeta.setIcon(value);
                            IconPath iconPath2 = new IconPath(value, 0);
                            this.iconPaths = Collections.singletonList(iconPath2);
                            break;
                        }
                    }
                }
                break;
            case "manifest":
                this.apkMeta.setPackageName(attributes.getString("package"));
                this.apkMeta.setVersionName(attributes.getString("versionName"));
                this.apkMeta.setVersionCode(attributes.getLong(UpgradeConstants.UPGRADE_PARAM_VERSION_CODE_KEY));
                String installLocation = attributes.getString("installLocation");
                if (installLocation != null) {
                    this.apkMeta.setInstallLocation(installLocation);
                    break;
                }
                break;
            case "uses-sdk":
                this.apkMeta.setMinSdkVersion(attributes.getString("minSdkVersion"));
                this.apkMeta.setTargetSdkVersion(attributes.getString("targetSdkVersion"));
                this.apkMeta.setMaxSdkVersion(attributes.getString("maxSdkVersion"));
                break;
            case "supports-screens":
                this.apkMeta.setAnyDensity(attributes.getBoolean("anyDensity", false));
                this.apkMeta.setSmallScreens(attributes.getBoolean("smallScreens", false));
                this.apkMeta.setNormalScreens(attributes.getBoolean("normalScreens", false));
                this.apkMeta.setLargeScreens(attributes.getBoolean("largeScreens", false));
                break;
            case "uses-feature":
                String name = attributes.getString("name");
                boolean required = attributes.getBoolean(DefaultBindingErrorProcessor.MISSING_FIELD_ERROR_CODE, false);
                if (name != null) {
                    UseFeature useFeature = new UseFeature();
                    useFeature.setName(name);
                    useFeature.setRequired(required);
                    this.apkMeta.addUseFeatures(useFeature);
                    break;
                } else {
                    Integer gl = attributes.getInt("glEsVersion");
                    if (gl != null) {
                        int v = gl.intValue();
                        GlEsVersion glEsVersion = new GlEsVersion();
                        glEsVersion.setMajor(v >> 16);
                        glEsVersion.setMinor(v & 65535);
                        glEsVersion.setRequired(required);
                        this.apkMeta.setGlEsVersion(glEsVersion);
                        break;
                    }
                }
                break;
            case "uses-permission":
                this.apkMeta.addUsesPermission(attributes.getString("name"));
                break;
            case "permission":
                Permission permission = new Permission();
                permission.setName(attributes.getString("name"));
                permission.setLabel(attributes.getString(AnnotatedPrivateKey.LABEL));
                permission.setIcon(attributes.getString("icon"));
                permission.setGroup(attributes.getString("group"));
                permission.setDescription(attributes.getString("description"));
                String protectionLevel = attributes.getString("android:protectionLevel");
                if (protectionLevel != null) {
                    permission.setProtectionLevel(protectionLevel);
                }
                this.apkMeta.addPermission(permission);
                break;
        }
        String[] strArr = this.tagStack;
        int i = this.depth;
        this.depth = i + 1;
        strArr[i] = xmlNodeStartTag.getName();
    }

    @Override // net.dongliu.apk.parser.parser.XmlStreamer
    public void onEndTag(XmlNodeEndTag xmlNodeEndTag) {
        this.depth--;
    }

    @Override // net.dongliu.apk.parser.parser.XmlStreamer
    public void onCData(XmlCData xmlCData) {
    }

    @Override // net.dongliu.apk.parser.parser.XmlStreamer
    public void onNamespaceStart(XmlNamespaceStartTag tag) {
    }

    @Override // net.dongliu.apk.parser.parser.XmlStreamer
    public void onNamespaceEnd(XmlNamespaceEndTag tag) {
    }

    @Nonnull
    public ApkMeta getApkMeta() {
        return this.apkMeta;
    }

    @Nonnull
    public List<IconPath> getIconPaths() {
        return this.iconPaths;
    }

    private boolean matchTagPath(String... tags) {
        if (this.depth != tags.length + 1) {
            return false;
        }
        for (int i = 1; i < this.depth; i++) {
            if (!this.tagStack[i].equals(tags[i - 1])) {
                return false;
            }
        }
        return true;
    }

    private boolean matchLastTag(String tag) {
        return this.tagStack[this.depth - 1].endsWith(tag);
    }
}
