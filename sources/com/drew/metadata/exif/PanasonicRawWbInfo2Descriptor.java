package com.drew.metadata.exif;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/exif/PanasonicRawWbInfo2Descriptor.class */
public class PanasonicRawWbInfo2Descriptor extends TagDescriptor<PanasonicRawWbInfo2Directory> {
    public PanasonicRawWbInfo2Descriptor(@NotNull PanasonicRawWbInfo2Directory directory) {
        super(directory);
    }

    @Override // com.drew.metadata.TagDescriptor
    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 1:
            case 5:
            case 9:
            case 13:
            case 17:
            case 21:
            case 25:
                return getWbTypeDescription(tagType);
            case 2:
            case 3:
            case 4:
            case 6:
            case 7:
            case 8:
            case 10:
            case 11:
            case 12:
            case 14:
            case 15:
            case 16:
            case 18:
            case 19:
            case 20:
            case 22:
            case 23:
            case 24:
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getWbTypeDescription(int tagType) {
        Integer wbtype = ((PanasonicRawWbInfo2Directory) this._directory).getInteger(tagType);
        if (wbtype == null) {
            return null;
        }
        return super.getLightSourceDescription(wbtype.shortValue());
    }
}
