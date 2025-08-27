package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;
import com.moredian.onpremise.core.common.constants.SymbolConstants;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/exif/makernotes/OlympusImageProcessingMakernoteDescriptor.class */
public class OlympusImageProcessingMakernoteDescriptor extends TagDescriptor<OlympusImageProcessingMakernoteDirectory> {
    public OlympusImageProcessingMakernoteDescriptor(@NotNull OlympusImageProcessingMakernoteDirectory directory) {
        super(directory);
    }

    @Override // com.drew.metadata.TagDescriptor
    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 0:
                return getImageProcessingVersionDescription();
            case 512:
                return getColorMatrixDescription();
            case 4112:
                return getNoiseReduction2Description();
            case 4113:
                return getDistortionCorrection2Description();
            case 4114:
                return getShadingCompensation2Description();
            case 4124:
                return getMultipleExposureModeDescription();
            case OlympusImageProcessingMakernoteDirectory.TagAspectRatio /* 4370 */:
                return getAspectRatioDescription();
            case OlympusImageProcessingMakernoteDirectory.TagKeystoneCompensation /* 6400 */:
                return getKeystoneCompensationDescription();
            case OlympusImageProcessingMakernoteDirectory.TagKeystoneDirection /* 6401 */:
                return getKeystoneDirectionDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getImageProcessingVersionDescription() {
        return getVersionBytesDescription(0, 4);
    }

    @Nullable
    public String getColorMatrixDescription() {
        int[] obj = ((OlympusImageProcessingMakernoteDirectory) this._directory).getIntArray(512);
        if (obj == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < obj.length; i++) {
            if (i != 0) {
                sb.append(SymbolConstants.SPACE_SYMBOL);
            }
            sb.append((int) ((short) obj[i]));
        }
        return sb.toString();
    }

    @Nullable
    public String getNoiseReduction2Description() {
        Integer value = ((OlympusImageProcessingMakernoteDirectory) this._directory).getInteger(4112);
        if (value == null) {
            return null;
        }
        if (value.intValue() == 0) {
            return "(none)";
        }
        StringBuilder sb = new StringBuilder();
        short v = value.shortValue();
        if ((v & 1) != 0) {
            sb.append("Noise Reduction, ");
        }
        if (((v >> 1) & 1) != 0) {
            sb.append("Noise Filter, ");
        }
        if (((v >> 2) & 1) != 0) {
            sb.append("Noise Filter (ISO Boost), ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    @Nullable
    public String getDistortionCorrection2Description() {
        return getIndexedDescription(4113, "Off", "On");
    }

    @Nullable
    public String getShadingCompensation2Description() {
        return getIndexedDescription(4114, "Off", "On");
    }

    @Nullable
    public String getMultipleExposureModeDescription() {
        int[] values = ((OlympusImageProcessingMakernoteDirectory) this._directory).getIntArray(4124);
        if (values == null) {
            Integer value = ((OlympusImageProcessingMakernoteDirectory) this._directory).getInteger(4124);
            if (value == null) {
                return null;
            }
            values = new int[]{value.intValue()};
        }
        if (values.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        switch ((short) values[0]) {
            case 0:
                sb.append("Off");
                break;
            case 1:
            default:
                sb.append("Unknown (").append((int) ((short) values[0])).append(")");
                break;
            case 2:
                sb.append("On (2 frames)");
                break;
            case 3:
                sb.append("On (3 frames)");
                break;
        }
        if (values.length > 1) {
            sb.append("; ").append((int) ((short) values[1]));
        }
        return sb.toString();
    }

    @Nullable
    public String getAspectRatioDescription() {
        String ret;
        byte[] values = ((OlympusImageProcessingMakernoteDirectory) this._directory).getByteArray(OlympusImageProcessingMakernoteDirectory.TagAspectRatio);
        if (values == null || values.length < 2) {
            return null;
        }
        String join = String.format("%d %d", Byte.valueOf(values[0]), Byte.valueOf(values[1]));
        if (join.equals("1 1")) {
            ret = "4:3";
        } else if (join.equals("1 4")) {
            ret = "1:1";
        } else if (join.equals("2 1")) {
            ret = "3:2 (RAW)";
        } else if (join.equals("2 2")) {
            ret = "3:2";
        } else if (join.equals("3 1")) {
            ret = "16:9 (RAW)";
        } else if (join.equals("3 3")) {
            ret = "16:9";
        } else if (join.equals("4 1")) {
            ret = "1:1 (RAW)";
        } else if (join.equals("4 4")) {
            ret = "6:6";
        } else if (join.equals("5 5")) {
            ret = "5:4";
        } else if (join.equals("6 6")) {
            ret = "7:6";
        } else if (join.equals("7 7")) {
            ret = "6:5";
        } else if (join.equals("8 8")) {
            ret = "7:5";
        } else if (join.equals("9 1")) {
            ret = "3:4 (RAW)";
        } else if (join.equals("9 9")) {
            ret = "3:4";
        } else {
            ret = "Unknown (" + join + ")";
        }
        return ret;
    }

    @Nullable
    public String getKeystoneCompensationDescription() {
        String ret;
        byte[] values = ((OlympusImageProcessingMakernoteDirectory) this._directory).getByteArray(OlympusImageProcessingMakernoteDirectory.TagKeystoneCompensation);
        if (values == null || values.length < 2) {
            return null;
        }
        String join = String.format("%d %d", Byte.valueOf(values[0]), Byte.valueOf(values[1]));
        if (join.equals("0 0")) {
            ret = "Off";
        } else if (join.equals("0 1")) {
            ret = "On";
        } else {
            ret = "Unknown (" + join + ")";
        }
        return ret;
    }

    @Nullable
    public String getKeystoneDirectionDescription() {
        return getIndexedDescription(OlympusImageProcessingMakernoteDirectory.TagKeystoneDirection, "Vertical", "Horizontal");
    }
}
