package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/exif/makernotes/SonyType6MakernoteDescriptor.class */
public class SonyType6MakernoteDescriptor extends TagDescriptor<SonyType6MakernoteDirectory> {
    public SonyType6MakernoteDescriptor(@NotNull SonyType6MakernoteDirectory directory) {
        super(directory);
    }

    @Override // com.drew.metadata.TagDescriptor
    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 8192:
                return getMakernoteThumbVersionDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getMakernoteThumbVersionDescription() {
        return getVersionBytesDescription(8192, 2);
    }
}
