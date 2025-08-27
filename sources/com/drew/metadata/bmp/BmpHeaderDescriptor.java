package com.drew.metadata.bmp;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/bmp/BmpHeaderDescriptor.class */
public class BmpHeaderDescriptor extends TagDescriptor<BmpHeaderDirectory> {
    public BmpHeaderDescriptor(@NotNull BmpHeaderDirectory directory) {
        super(directory);
    }

    @Override // com.drew.metadata.TagDescriptor
    public String getDescription(int tagType) {
        switch (tagType) {
            case 5:
                return getCompressionDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getCompressionDescription() {
        Integer headerSize;
        try {
            Integer value = ((BmpHeaderDirectory) this._directory).getInteger(5);
            if (value == null || (headerSize = ((BmpHeaderDirectory) this._directory).getInteger(-1)) == null) {
                return null;
            }
            switch (value.intValue()) {
                case 3:
                    if (headerSize.intValue() == 64) {
                        break;
                    }
                    break;
                case 4:
                    if (headerSize.intValue() == 64) {
                        break;
                    }
                    break;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
