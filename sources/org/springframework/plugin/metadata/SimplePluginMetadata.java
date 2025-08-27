package org.springframework.plugin.metadata;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-plugin-metadata-1.2.0.RELEASE.jar:org/springframework/plugin/metadata/SimplePluginMetadata.class */
public class SimplePluginMetadata implements PluginMetadata {
    private final String name;
    private final String version;

    public SimplePluginMetadata(String name, String version) {
        Assert.hasText(name, "Name must not be null or empty!");
        Assert.hasText(version, "Version must not be null or empty!");
        this.name = name;
        this.version = version;
    }

    @Override // org.springframework.plugin.metadata.PluginMetadata
    public String getName() {
        return this.name;
    }

    @Override // org.springframework.plugin.metadata.PluginMetadata
    public String getVersion() {
        return this.version;
    }

    public String toString() {
        return String.format("%s:%s", getName(), getVersion());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PluginMetadata)) {
            return false;
        }
        PluginMetadata that = (PluginMetadata) obj;
        boolean sameName = ObjectUtils.nullSafeEquals(getName(), that.getName());
        boolean sameVersion = ObjectUtils.nullSafeEquals(getVersion(), that.getVersion());
        return sameName && sameVersion;
    }

    public int hashCode() {
        return ObjectUtils.nullSafeHashCode(this.name) + ObjectUtils.nullSafeHashCode(this.version);
    }
}
