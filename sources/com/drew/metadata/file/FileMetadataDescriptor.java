package com.drew.metadata.file;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/file/FileMetadataDescriptor.class */
public class FileMetadataDescriptor extends TagDescriptor<FileMetadataDirectory> {
    public FileMetadataDescriptor(@NotNull FileMetadataDirectory directory) {
        super(directory);
    }

    @Override // com.drew.metadata.TagDescriptor
    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 2:
                return getFileSizeDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    private String getFileSizeDescription() {
        Long size = ((FileMetadataDirectory) this._directory).getLongObject(2);
        if (size == null) {
            return null;
        }
        return Long.toString(size.longValue()) + " bytes";
    }
}
