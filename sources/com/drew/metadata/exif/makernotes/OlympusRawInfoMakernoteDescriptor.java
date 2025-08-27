package com.drew.metadata.exif.makernotes;

import com.drew.lang.Rational;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.aspectj.weaver.Dump;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/exif/makernotes/OlympusRawInfoMakernoteDescriptor.class */
public class OlympusRawInfoMakernoteDescriptor extends TagDescriptor<OlympusRawInfoMakernoteDirectory> {
    public OlympusRawInfoMakernoteDescriptor(@NotNull OlympusRawInfoMakernoteDirectory directory) {
        super(directory);
    }

    @Override // com.drew.metadata.TagDescriptor
    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 0:
                return getVersionBytesDescription(0, 4);
            case 512:
                return getColorMatrix2Description();
            case 1537:
                return getYCbCrCoefficientsDescription();
            case 4096:
                return getOlympusLightSourceDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getColorMatrix2Description() {
        int[] values = ((OlympusRawInfoMakernoteDirectory) this._directory).getIntArray(512);
        if (values == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            sb.append((int) ((short) values[i]));
            if (i < values.length - 1) {
                sb.append(SymbolConstants.SPACE_SYMBOL);
            }
        }
        if (sb.length() == 0) {
            return null;
        }
        return sb.toString();
    }

    @Nullable
    public String getYCbCrCoefficientsDescription() {
        int[] values = ((OlympusRawInfoMakernoteDirectory) this._directory).getIntArray(1537);
        if (values == null) {
            return null;
        }
        Rational[] ret = new Rational[values.length / 2];
        for (int i = 0; i < values.length / 2; i++) {
            ret[i] = new Rational((short) values[2 * i], (short) values[(2 * i) + 1]);
        }
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < ret.length; i2++) {
            sb.append(ret[i2].doubleValue());
            if (i2 < ret.length - 1) {
                sb.append(SymbolConstants.SPACE_SYMBOL);
            }
        }
        if (sb.length() == 0) {
            return null;
        }
        return sb.toString();
    }

    @Nullable
    public String getOlympusLightSourceDescription() {
        Integer value = ((OlympusRawInfoMakernoteDirectory) this._directory).getInteger(4096);
        if (value == null) {
            return null;
        }
        switch (value.shortValue()) {
            case 0:
                return Dump.UNKNOWN_FILENAME;
            case 16:
                return "Shade";
            case 17:
                return "Cloudy";
            case 18:
                return "Fine Weather";
            case 20:
                return "Tungsten (Incandescent)";
            case 22:
                return "Evening Sunlight";
            case 33:
                return "Daylight Fluorescent";
            case 34:
                return "Day White Fluorescent";
            case 35:
                return "Cool White Fluorescent";
            case 36:
                return "White Fluorescent";
            case 256:
                return "One Touch White Balance";
            case 512:
                return "Custom 1-4";
            default:
                return "Unknown (" + value + ")";
        }
    }
}
