package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/exif/makernotes/KyoceraMakernoteDescriptor.class */
public class KyoceraMakernoteDescriptor extends TagDescriptor<KyoceraMakernoteDirectory> {
    public KyoceraMakernoteDescriptor(@NotNull KyoceraMakernoteDirectory directory) {
        super(directory);
    }

    @Override // com.drew.metadata.TagDescriptor
    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 1:
                return getProprietaryThumbnailDataDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getProprietaryThumbnailDataDescription() {
        return getByteLengthDescription(1);
    }
}
