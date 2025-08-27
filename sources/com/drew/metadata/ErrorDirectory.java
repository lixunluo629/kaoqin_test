package com.drew.metadata;

import com.drew.lang.annotations.NotNull;
import java.util.HashMap;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/ErrorDirectory.class */
public final class ErrorDirectory extends Directory {
    public ErrorDirectory() {
    }

    public ErrorDirectory(String error) {
        super.addError(error);
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    public String getName() {
        return "Error";
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    protected HashMap<Integer, String> getTagNameMap() {
        return new HashMap<>();
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    public String getTagName(int tagType) {
        return "";
    }

    @Override // com.drew.metadata.Directory
    public boolean hasTagName(int tagType) {
        return false;
    }

    @Override // com.drew.metadata.Directory
    public void setObject(int tagType, @NotNull Object value) {
        throw new UnsupportedOperationException(String.format("Cannot add value to %s.", ErrorDirectory.class.getName()));
    }
}
