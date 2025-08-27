package com.drew.metadata.png;

import com.drew.imaging.png.PngColorType;
import com.drew.lang.KeyValuePair;
import com.drew.lang.SequentialByteArrayReader;
import com.drew.lang.SequentialReader;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;
import java.io.IOException;
import java.util.List;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/png/PngDescriptor.class */
public class PngDescriptor extends TagDescriptor<PngDirectory> {
    public PngDescriptor(@NotNull PngDirectory directory) {
        super(directory);
    }

    @Override // com.drew.metadata.TagDescriptor
    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 4:
                return getColorTypeDescription();
            case 5:
                return getCompressionTypeDescription();
            case 6:
                return getFilterMethodDescription();
            case 7:
                return getInterlaceMethodDescription();
            case 8:
            case 11:
            case 12:
            case 14:
            case 16:
            case 17:
            default:
                return super.getDescription(tagType);
            case 9:
                return getPaletteHasTransparencyDescription();
            case 10:
                return getIsSrgbColorSpaceDescription();
            case 13:
                return getTextualDataDescription();
            case 15:
                return getBackgroundColorDescription();
            case 18:
                return getUnitSpecifierDescription();
        }
    }

    @Nullable
    public String getColorTypeDescription() {
        PngColorType colorType;
        Integer value = ((PngDirectory) this._directory).getInteger(4);
        if (value == null || (colorType = PngColorType.fromNumericValue(value.intValue())) == null) {
            return null;
        }
        return colorType.getDescription();
    }

    @Nullable
    public String getCompressionTypeDescription() {
        return getIndexedDescription(5, "Deflate");
    }

    @Nullable
    public String getFilterMethodDescription() {
        return getIndexedDescription(6, "Adaptive");
    }

    @Nullable
    public String getInterlaceMethodDescription() {
        return getIndexedDescription(7, "No Interlace", "Adam7 Interlace");
    }

    @Nullable
    public String getPaletteHasTransparencyDescription() {
        return getIndexedDescription(9, null, "Yes");
    }

    @Nullable
    public String getIsSrgbColorSpaceDescription() {
        return getIndexedDescription(10, "Perceptual", "Relative Colorimetric", "Saturation", "Absolute Colorimetric");
    }

    @Nullable
    public String getUnitSpecifierDescription() {
        return getIndexedDescription(18, "Unspecified", "Metres");
    }

    @Nullable
    public String getTextualDataDescription() {
        Object object = ((PngDirectory) this._directory).getObject(13);
        if (object == null) {
            return null;
        }
        List<KeyValuePair> keyValues = (List) object;
        StringBuilder sb = new StringBuilder();
        for (KeyValuePair keyValue : keyValues) {
            if (sb.length() != 0) {
                sb.append('\n');
            }
            sb.append(String.format("%s: %s", keyValue.getKey(), keyValue.getValue()));
        }
        return sb.toString();
    }

    @Nullable
    public String getBackgroundColorDescription() {
        byte[] bytes = ((PngDirectory) this._directory).getByteArray(15);
        Integer colorType = ((PngDirectory) this._directory).getInteger(4);
        if (bytes == null || colorType == null) {
            return null;
        }
        SequentialReader reader = new SequentialByteArrayReader(bytes);
        try {
            switch (colorType.intValue()) {
                case 0:
                case 4:
                    return String.format("Greyscale Level %d", Integer.valueOf(reader.getUInt16()));
                case 1:
                case 5:
                default:
                    return null;
                case 2:
                case 6:
                    return String.format("R %d, G %d, B %d", Integer.valueOf(reader.getUInt16()), Integer.valueOf(reader.getUInt16()), Integer.valueOf(reader.getUInt16()));
                case 3:
                    return String.format("Palette Index %d", Short.valueOf(reader.getUInt8()));
            }
        } catch (IOException e) {
            return null;
        }
    }
}
