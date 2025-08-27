package com.drew.metadata.exif;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/exif/PanasonicRawIFD0Descriptor.class */
public class PanasonicRawIFD0Descriptor extends TagDescriptor<PanasonicRawIFD0Directory> {
    public PanasonicRawIFD0Descriptor(@NotNull PanasonicRawIFD0Directory directory) {
        super(directory);
    }

    @Override // com.drew.metadata.TagDescriptor
    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 1:
                return getVersionBytesDescription(1, 2);
            case 274:
                return getOrientationDescription(274);
            default:
                return super.getDescription(tagType);
        }
    }
}
