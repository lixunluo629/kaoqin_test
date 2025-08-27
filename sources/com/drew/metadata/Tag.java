package com.drew.metadata;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import org.springframework.beans.PropertyAccessor;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/Tag.class */
public class Tag {
    private final int _tagType;

    @NotNull
    private final Directory _directory;

    public Tag(int tagType, @NotNull Directory directory) {
        this._tagType = tagType;
        this._directory = directory;
    }

    public int getTagType() {
        return this._tagType;
    }

    @NotNull
    public String getTagTypeHex() {
        return String.format("0x%04x", Integer.valueOf(this._tagType));
    }

    @Nullable
    public String getDescription() {
        return this._directory.getDescription(this._tagType);
    }

    public boolean hasTagName() {
        return this._directory.hasTagName(this._tagType);
    }

    @NotNull
    public String getTagName() {
        return this._directory.getTagName(this._tagType);
    }

    @NotNull
    public String getDirectoryName() {
        return this._directory.getName();
    }

    @NotNull
    public String toString() {
        String description = getDescription();
        if (description == null) {
            description = this._directory.getString(getTagType()) + " (unable to formulate description)";
        }
        return PropertyAccessor.PROPERTY_KEY_PREFIX + this._directory.getName() + "] " + getTagName() + " - " + description;
    }
}
