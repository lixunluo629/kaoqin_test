package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;
import com.itextpdf.io.font.constants.FontStretches;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import org.apache.poi.ddf.EscherProperties;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/exif/makernotes/CasioType1MakernoteDescriptor.class */
public class CasioType1MakernoteDescriptor extends TagDescriptor<CasioType1MakernoteDirectory> {
    public CasioType1MakernoteDescriptor(@NotNull CasioType1MakernoteDirectory directory) {
        super(directory);
    }

    @Override // com.drew.metadata.TagDescriptor
    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 1:
                return getRecordingModeDescription();
            case 2:
                return getQualityDescription();
            case 3:
                return getFocusingModeDescription();
            case 4:
                return getFlashModeDescription();
            case 5:
                return getFlashIntensityDescription();
            case 6:
                return getObjectDistanceDescription();
            case 7:
                return getWhiteBalanceDescription();
            case 8:
            case 9:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            default:
                return super.getDescription(tagType);
            case 10:
                return getDigitalZoomDescription();
            case 11:
                return getSharpnessDescription();
            case 12:
                return getContrastDescription();
            case 13:
                return getSaturationDescription();
            case 20:
                return getCcdSensitivityDescription();
        }
    }

    @Nullable
    public String getCcdSensitivityDescription() {
        Integer value = ((CasioType1MakernoteDirectory) this._directory).getInteger(20);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 64:
                return FontStretches.NORMAL;
            case 80:
                return "Normal (ISO 80 equivalent)";
            case 100:
                return "High";
            case 125:
                return "+1.0";
            case EscherProperties.GEOTEXT__TIGHTORTRACK /* 244 */:
                return "+3.0";
            case EscherProperties.GEOTEXT__BOLDFONT /* 250 */:
                return "+2.0";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getSaturationDescription() {
        return getIndexedDescription(13, FontStretches.NORMAL, "Low", "High");
    }

    @Nullable
    public String getContrastDescription() {
        return getIndexedDescription(12, FontStretches.NORMAL, "Low", "High");
    }

    @Nullable
    public String getSharpnessDescription() {
        return getIndexedDescription(11, FontStretches.NORMAL, "Soft", "Hard");
    }

    @Nullable
    public String getDigitalZoomDescription() {
        Integer value = ((CasioType1MakernoteDirectory) this._directory).getInteger(10);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 65536:
                return "No digital zoom";
            case 65537:
                return "2x digital zoom";
            case 131072:
                return "2x digital zoom";
            case 262144:
                return "4x digital zoom";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getWhiteBalanceDescription() {
        Integer value = ((CasioType1MakernoteDirectory) this._directory).getInteger(7);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 1:
                return "Auto";
            case 2:
                return "Tungsten";
            case 3:
                return "Daylight";
            case 4:
                return "Florescent";
            case 5:
                return "Shade";
            case 129:
                return "Manual";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getObjectDistanceDescription() {
        Integer value = ((CasioType1MakernoteDirectory) this._directory).getInteger(6);
        if (value == null) {
            return null;
        }
        return getFocalLengthDescription(value.intValue());
    }

    @Nullable
    public String getFlashIntensityDescription() {
        Integer value = ((CasioType1MakernoteDirectory) this._directory).getInteger(5);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 11:
                return "Weak";
            case 12:
            case 14:
            default:
                return "Unknown (" + value + ")";
            case 13:
                return FontStretches.NORMAL;
            case 15:
                return StandardRoles.STRONG;
        }
    }

    @Nullable
    public String getFlashModeDescription() {
        return getIndexedDescription(4, 1, "Auto", "On", "Off", "Red eye reduction");
    }

    @Nullable
    public String getFocusingModeDescription() {
        return getIndexedDescription(3, 2, "Macro", "Auto focus", "Manual focus", "Infinity");
    }

    @Nullable
    public String getQualityDescription() {
        return getIndexedDescription(2, 1, "Economy", FontStretches.NORMAL, "Fine");
    }

    @Nullable
    public String getRecordingModeDescription() {
        return getIndexedDescription(1, 1, "Single shutter", "Panorama", "Night scene", "Portrait", "Landscape");
    }
}
