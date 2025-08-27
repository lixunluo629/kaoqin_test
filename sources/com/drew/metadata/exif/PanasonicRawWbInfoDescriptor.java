package com.drew.metadata.exif;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/exif/PanasonicRawWbInfoDescriptor.class */
public class PanasonicRawWbInfoDescriptor extends TagDescriptor<PanasonicRawWbInfoDirectory> {
    public PanasonicRawWbInfoDescriptor(@NotNull PanasonicRawWbInfoDirectory directory) {
        super(directory);
    }

    @Override // com.drew.metadata.TagDescriptor
    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 1:
            case 4:
            case 7:
            case 10:
            case 13:
            case 16:
            case 19:
                return getWbTypeDescription(tagType);
            case 2:
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 11:
            case 12:
            case 14:
            case 15:
            case 17:
            case 18:
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getWbTypeDescription(int tagType) {
        Integer wbtype = ((PanasonicRawWbInfoDirectory) this._directory).getInteger(tagType);
        if (wbtype == null) {
            return null;
        }
        return super.getLightSourceDescription(wbtype.shortValue());
    }
}
