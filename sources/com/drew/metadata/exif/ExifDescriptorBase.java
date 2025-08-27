package com.drew.metadata.exif;

import com.drew.imaging.PhotographicConversions;
import com.drew.lang.ByteArrayReader;
import com.drew.lang.Rational;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.Directory;
import com.drew.metadata.TagDescriptor;
import com.drew.metadata.exif.makernotes.FujifilmMakernoteDirectory;
import com.drew.metadata.exif.makernotes.PanasonicMakernoteDirectory;
import com.itextpdf.io.codec.TIFFConstants;
import com.itextpdf.io.font.constants.FontStretches;
import com.itextpdf.io.image.PngImageHelper;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.httpclient.auth.NTLM;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.util.CodePageUtil;
import org.aspectj.weaver.Dump;
import org.springframework.beans.PropertyAccessor;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/exif/ExifDescriptorBase.class */
public abstract class ExifDescriptorBase<T extends Directory> extends TagDescriptor<T> {
    private final boolean _allowDecimalRepresentationOfRationals = true;

    public ExifDescriptorBase(@NotNull T directory) {
        super(directory);
        this._allowDecimalRepresentationOfRationals = true;
    }

    @Override // com.drew.metadata.TagDescriptor
    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 1:
                return getInteropIndexDescription();
            case 2:
                return getInteropVersionDescription();
            case 254:
                return getNewSubfileTypeDescription();
            case 255:
                return getSubfileTypeDescription();
            case 256:
                return getImageWidthDescription();
            case 257:
                return getImageHeightDescription();
            case 258:
                return getBitsPerSampleDescription();
            case 259:
                return getCompressionDescription();
            case 262:
                return getPhotometricInterpretationDescription();
            case 263:
                return getThresholdingDescription();
            case 266:
                return getFillOrderDescription();
            case 274:
                return getOrientationDescription();
            case 277:
                return getSamplesPerPixelDescription();
            case 278:
                return getRowsPerStripDescription();
            case 279:
                return getStripByteCountsDescription();
            case 282:
                return getXResolutionDescription();
            case 283:
                return getYResolutionDescription();
            case 284:
                return getPlanarConfigurationDescription();
            case 296:
                return getResolutionDescription();
            case 512:
                return getJpegProcDescription();
            case 530:
                return getYCbCrSubsamplingDescription();
            case 531:
                return getYCbCrPositioningDescription();
            case 532:
                return getReferenceBlackWhiteDescription();
            case ExifDirectoryBase.TAG_CFA_PATTERN_2 /* 33422 */:
                return getCfaPattern2Description();
            case ExifDirectoryBase.TAG_EXPOSURE_TIME /* 33434 */:
                return getExposureTimeDescription();
            case ExifDirectoryBase.TAG_FNUMBER /* 33437 */:
                return getFNumberDescription();
            case ExifDirectoryBase.TAG_EXPOSURE_PROGRAM /* 34850 */:
                return getExposureProgramDescription();
            case ExifDirectoryBase.TAG_ISO_EQUIVALENT /* 34855 */:
                return getIsoEquivalentDescription();
            case ExifDirectoryBase.TAG_SENSITIVITY_TYPE /* 34864 */:
                return getSensitivityTypeRangeDescription();
            case 36864:
                return getExifVersionDescription();
            case ExifDirectoryBase.TAG_COMPONENTS_CONFIGURATION /* 37121 */:
                return getComponentConfigurationDescription();
            case ExifDirectoryBase.TAG_COMPRESSED_AVERAGE_BITS_PER_PIXEL /* 37122 */:
                return getCompressedAverageBitsPerPixelDescription();
            case ExifDirectoryBase.TAG_SHUTTER_SPEED /* 37377 */:
                return getShutterSpeedDescription();
            case ExifDirectoryBase.TAG_APERTURE /* 37378 */:
                return getApertureValueDescription();
            case ExifDirectoryBase.TAG_EXPOSURE_BIAS /* 37380 */:
                return getExposureBiasDescription();
            case ExifDirectoryBase.TAG_MAX_APERTURE /* 37381 */:
                return getMaxApertureValueDescription();
            case ExifDirectoryBase.TAG_SUBJECT_DISTANCE /* 37382 */:
                return getSubjectDistanceDescription();
            case ExifDirectoryBase.TAG_METERING_MODE /* 37383 */:
                return getMeteringModeDescription();
            case 37384:
                return getWhiteBalanceDescription();
            case ExifDirectoryBase.TAG_FLASH /* 37385 */:
                return getFlashDescription();
            case ExifDirectoryBase.TAG_FOCAL_LENGTH /* 37386 */:
                return getFocalLengthDescription();
            case ExifDirectoryBase.TAG_USER_COMMENT /* 37510 */:
                return getUserCommentDescription();
            case ExifDirectoryBase.TAG_WIN_TITLE /* 40091 */:
                return getWindowsTitleDescription();
            case ExifDirectoryBase.TAG_WIN_COMMENT /* 40092 */:
                return getWindowsCommentDescription();
            case ExifDirectoryBase.TAG_WIN_AUTHOR /* 40093 */:
                return getWindowsAuthorDescription();
            case ExifDirectoryBase.TAG_WIN_KEYWORDS /* 40094 */:
                return getWindowsKeywordsDescription();
            case ExifDirectoryBase.TAG_WIN_SUBJECT /* 40095 */:
                return getWindowsSubjectDescription();
            case 40960:
                return getFlashPixVersionDescription();
            case 40961:
                return getColorSpaceDescription();
            case 40962:
                return getExifImageWidthDescription();
            case 40963:
                return getExifImageHeightDescription();
            case ExifDirectoryBase.TAG_FOCAL_PLANE_X_RESOLUTION /* 41486 */:
                return getFocalPlaneXResolutionDescription();
            case ExifDirectoryBase.TAG_FOCAL_PLANE_Y_RESOLUTION /* 41487 */:
                return getFocalPlaneYResolutionDescription();
            case ExifDirectoryBase.TAG_FOCAL_PLANE_RESOLUTION_UNIT /* 41488 */:
                return getFocalPlaneResolutionUnitDescription();
            case ExifDirectoryBase.TAG_SENSING_METHOD /* 41495 */:
                return getSensingMethodDescription();
            case ExifDirectoryBase.TAG_FILE_SOURCE /* 41728 */:
                return getFileSourceDescription();
            case ExifDirectoryBase.TAG_SCENE_TYPE /* 41729 */:
                return getSceneTypeDescription();
            case ExifDirectoryBase.TAG_CFA_PATTERN /* 41730 */:
                return getCfaPatternDescription();
            case ExifDirectoryBase.TAG_CUSTOM_RENDERED /* 41985 */:
                return getCustomRenderedDescription();
            case ExifDirectoryBase.TAG_EXPOSURE_MODE /* 41986 */:
                return getExposureModeDescription();
            case ExifDirectoryBase.TAG_WHITE_BALANCE_MODE /* 41987 */:
                return getWhiteBalanceModeDescription();
            case ExifDirectoryBase.TAG_DIGITAL_ZOOM_RATIO /* 41988 */:
                return getDigitalZoomRatioDescription();
            case ExifDirectoryBase.TAG_35MM_FILM_EQUIV_FOCAL_LENGTH /* 41989 */:
                return get35mmFilmEquivFocalLengthDescription();
            case ExifDirectoryBase.TAG_SCENE_CAPTURE_TYPE /* 41990 */:
                return getSceneCaptureTypeDescription();
            case ExifDirectoryBase.TAG_GAIN_CONTROL /* 41991 */:
                return getGainControlDescription();
            case ExifDirectoryBase.TAG_CONTRAST /* 41992 */:
                return getContrastDescription();
            case ExifDirectoryBase.TAG_SATURATION /* 41993 */:
                return getSaturationDescription();
            case ExifDirectoryBase.TAG_SHARPNESS /* 41994 */:
                return getSharpnessDescription();
            case ExifDirectoryBase.TAG_SUBJECT_DISTANCE_RANGE /* 41996 */:
                return getSubjectDistanceRangeDescription();
            case ExifDirectoryBase.TAG_LENS_SPECIFICATION /* 42034 */:
                return getLensSpecificationDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getInteropVersionDescription() {
        return getVersionBytesDescription(2, 2);
    }

    @Nullable
    public String getInteropIndexDescription() {
        String value = this._directory.getString(1);
        if (value == null) {
            return null;
        }
        return "R98".equalsIgnoreCase(value.trim()) ? "Recommended Exif Interoperability Rules (ExifR98)" : "Unknown (" + value + ")";
    }

    @Nullable
    public String getReferenceBlackWhiteDescription() {
        int[] ints = this._directory.getIntArray(532);
        if (ints == null || ints.length < 6) {
            Object o = this._directory.getObject(532);
            if (o != null && (o instanceof long[])) {
                long[] longs = (long[]) o;
                if (longs.length < 6) {
                    return null;
                }
                ints = new int[longs.length];
                for (int i = 0; i < longs.length; i++) {
                    ints[i] = (int) longs[i];
                }
            } else {
                return null;
            }
        }
        int blackR = ints[0];
        int whiteR = ints[1];
        int blackG = ints[2];
        int whiteG = ints[3];
        int blackB = ints[4];
        int whiteB = ints[5];
        return String.format("[%d,%d,%d] [%d,%d,%d]", Integer.valueOf(blackR), Integer.valueOf(blackG), Integer.valueOf(blackB), Integer.valueOf(whiteR), Integer.valueOf(whiteG), Integer.valueOf(whiteB));
    }

    @Nullable
    public String getYResolutionDescription() {
        Rational value = this._directory.getRational(283);
        if (value == null) {
            return null;
        }
        String unit = getResolutionDescription();
        Object[] objArr = new Object[2];
        objArr[0] = value.toSimpleString(true);
        objArr[1] = unit == null ? "unit" : unit.toLowerCase();
        return String.format("%s dots per %s", objArr);
    }

    @Nullable
    public String getXResolutionDescription() {
        Rational value = this._directory.getRational(282);
        if (value == null) {
            return null;
        }
        String unit = getResolutionDescription();
        Object[] objArr = new Object[2];
        objArr[0] = value.toSimpleString(true);
        objArr[1] = unit == null ? "unit" : unit.toLowerCase();
        return String.format("%s dots per %s", objArr);
    }

    @Nullable
    public String getYCbCrPositioningDescription() {
        return getIndexedDescription(531, 1, "Center of pixel array", "Datum point");
    }

    @Nullable
    public String getOrientationDescription() {
        return super.getOrientationDescription(274);
    }

    @Nullable
    public String getResolutionDescription() {
        return getIndexedDescription(296, 1, "(No unit)", "Inch", "cm");
    }

    @Nullable
    private String getUnicodeDescription(int tag) {
        byte[] bytes = this._directory.getByteArray(tag);
        if (bytes == null) {
            return null;
        }
        try {
            return new String(bytes, "UTF-16LE").trim();
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    @Nullable
    public String getWindowsAuthorDescription() {
        return getUnicodeDescription(ExifDirectoryBase.TAG_WIN_AUTHOR);
    }

    @Nullable
    public String getWindowsCommentDescription() {
        return getUnicodeDescription(ExifDirectoryBase.TAG_WIN_COMMENT);
    }

    @Nullable
    public String getWindowsKeywordsDescription() {
        return getUnicodeDescription(ExifDirectoryBase.TAG_WIN_KEYWORDS);
    }

    @Nullable
    public String getWindowsTitleDescription() {
        return getUnicodeDescription(ExifDirectoryBase.TAG_WIN_TITLE);
    }

    @Nullable
    public String getWindowsSubjectDescription() {
        return getUnicodeDescription(ExifDirectoryBase.TAG_WIN_SUBJECT);
    }

    @Nullable
    public String getYCbCrSubsamplingDescription() {
        int[] positions = this._directory.getIntArray(530);
        if (positions == null || positions.length < 2) {
            return null;
        }
        if (positions[0] == 2 && positions[1] == 1) {
            return "YCbCr4:2:2";
        }
        if (positions[0] == 2 && positions[1] == 2) {
            return "YCbCr4:2:0";
        }
        return "(Unknown)";
    }

    @Nullable
    public String getPlanarConfigurationDescription() {
        return getIndexedDescription(284, 1, "Chunky (contiguous for each subsampling pixel)", "Separate (Y-plane/Cb-plane/Cr-plane format)");
    }

    @Nullable
    public String getSamplesPerPixelDescription() {
        String value = this._directory.getString(277);
        if (value == null) {
            return null;
        }
        return value + " samples/pixel";
    }

    @Nullable
    public String getRowsPerStripDescription() {
        String value = this._directory.getString(278);
        if (value == null) {
            return null;
        }
        return value + " rows/strip";
    }

    @Nullable
    public String getStripByteCountsDescription() {
        String value = this._directory.getString(279);
        if (value == null) {
            return null;
        }
        return value + " bytes";
    }

    @Nullable
    public String getPhotometricInterpretationDescription() {
        Integer value = this._directory.getInteger(262);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return "WhiteIsZero";
            case 1:
                return "BlackIsZero";
            case 2:
                return "RGB";
            case 3:
                return "RGB Palette";
            case 4:
                return "Transparency Mask";
            case 5:
                return "CMYK";
            case 6:
                return "YCbCr";
            case 8:
                return "CIELab";
            case 9:
                return "ICCLab";
            case 10:
                return "ITULab";
            case 32803:
                return "Color Filter Array";
            case TIFFConstants.PHOTOMETRIC_LOGL /* 32844 */:
                return "Pixar LogL";
            case TIFFConstants.PHOTOMETRIC_LOGLUV /* 32845 */:
                return "Pixar LogLuv";
            case 32892:
                return "Linear Raw";
            default:
                return "Unknown colour space";
        }
    }

    @Nullable
    public String getBitsPerSampleDescription() {
        String value = this._directory.getString(258);
        if (value == null) {
            return null;
        }
        return value + " bits/component/pixel";
    }

    @Nullable
    public String getImageWidthDescription() {
        String value = this._directory.getString(256);
        if (value == null) {
            return null;
        }
        return value + " pixels";
    }

    @Nullable
    public String getImageHeightDescription() {
        String value = this._directory.getString(257);
        if (value == null) {
            return null;
        }
        return value + " pixels";
    }

    @Nullable
    public String getNewSubfileTypeDescription() {
        return getIndexedDescription(254, 0, "Full-resolution image", "Reduced-resolution image", "Single page of multi-page image", "Single page of multi-page reduced-resolution image", "Transparency mask", "Transparency mask of reduced-resolution image", "Transparency mask of multi-page image", "Transparency mask of reduced-resolution multi-page image");
    }

    @Nullable
    public String getSubfileTypeDescription() {
        return getIndexedDescription(255, 1, "Full-resolution image", "Reduced-resolution image", "Single page of multi-page image");
    }

    @Nullable
    public String getThresholdingDescription() {
        return getIndexedDescription(263, 1, "No dithering or halftoning", "Ordered dither or halftone", "Randomized dither");
    }

    @Nullable
    public String getFillOrderDescription() {
        return getIndexedDescription(266, 1, FontStretches.NORMAL, "Reversed");
    }

    @Nullable
    public String getSubjectDistanceRangeDescription() {
        return getIndexedDescription(ExifDirectoryBase.TAG_SUBJECT_DISTANCE_RANGE, Dump.UNKNOWN_FILENAME, "Macro", "Close view", "Distant view");
    }

    @Nullable
    public String getSensitivityTypeRangeDescription() {
        return getIndexedDescription(ExifDirectoryBase.TAG_SENSITIVITY_TYPE, Dump.UNKNOWN_FILENAME, "Standard Output Sensitivity", "Recommended Exposure Index", "ISO Speed", "Standard Output Sensitivity and Recommended Exposure Index", "Standard Output Sensitivity and ISO Speed", "Recommended Exposure Index and ISO Speed", "Standard Output Sensitivity, Recommended Exposure Index and ISO Speed");
    }

    @Nullable
    public String getLensSpecificationDescription() {
        return getLensSpecificationDescription(ExifDirectoryBase.TAG_LENS_SPECIFICATION);
    }

    @Nullable
    public String getSharpnessDescription() {
        return getIndexedDescription(ExifDirectoryBase.TAG_SHARPNESS, "None", "Low", "Hard");
    }

    @Nullable
    public String getSaturationDescription() {
        return getIndexedDescription(ExifDirectoryBase.TAG_SATURATION, "None", "Low saturation", "High saturation");
    }

    @Nullable
    public String getContrastDescription() {
        return getIndexedDescription(ExifDirectoryBase.TAG_CONTRAST, "None", "Soft", "Hard");
    }

    @Nullable
    public String getGainControlDescription() {
        return getIndexedDescription(ExifDirectoryBase.TAG_GAIN_CONTROL, "None", "Low gain up", "Low gain down", "High gain up", "High gain down");
    }

    @Nullable
    public String getSceneCaptureTypeDescription() {
        return getIndexedDescription(ExifDirectoryBase.TAG_SCENE_CAPTURE_TYPE, "Standard", "Landscape", "Portrait", "Night scene");
    }

    @Nullable
    public String get35mmFilmEquivFocalLengthDescription() {
        Integer value = this._directory.getInteger(ExifDirectoryBase.TAG_35MM_FILM_EQUIV_FOCAL_LENGTH);
        if (value == null) {
            return null;
        }
        return value.intValue() == 0 ? Dump.UNKNOWN_FILENAME : getFocalLengthDescription(value.intValue());
    }

    @Nullable
    public String getDigitalZoomRatioDescription() {
        Rational value = this._directory.getRational(ExifDirectoryBase.TAG_DIGITAL_ZOOM_RATIO);
        if (value == null) {
            return null;
        }
        return value.getNumerator() == 0 ? "Digital zoom not used" : new DecimalFormat("0.#").format(value.doubleValue());
    }

    @Nullable
    public String getWhiteBalanceModeDescription() {
        return getIndexedDescription(ExifDirectoryBase.TAG_WHITE_BALANCE_MODE, "Auto white balance", "Manual white balance");
    }

    @Nullable
    public String getExposureModeDescription() {
        return getIndexedDescription(ExifDirectoryBase.TAG_EXPOSURE_MODE, "Auto exposure", "Manual exposure", "Auto bracket");
    }

    @Nullable
    public String getCustomRenderedDescription() {
        return getIndexedDescription(ExifDirectoryBase.TAG_CUSTOM_RENDERED, "Normal process", "Custom process");
    }

    @Nullable
    public String getUserCommentDescription() {
        byte[] commentBytes = this._directory.getByteArray(ExifDirectoryBase.TAG_USER_COMMENT);
        if (commentBytes == null) {
            return null;
        }
        if (commentBytes.length == 0) {
            return "";
        }
        Map<String, String> encodingMap = new HashMap<>();
        encodingMap.put(NTLM.DEFAULT_CHARSET, System.getProperty("file.encoding"));
        encodingMap.put("UNICODE", "UTF-16LE");
        encodingMap.put("JIS", "Shift-JIS");
        try {
            if (commentBytes.length >= 10) {
                String firstTenBytesString = new String(commentBytes, 0, 10);
                for (Map.Entry<String, String> pair : encodingMap.entrySet()) {
                    String encodingName = pair.getKey();
                    String charset = pair.getValue();
                    if (firstTenBytesString.startsWith(encodingName)) {
                        for (int j = encodingName.length(); j < 10; j++) {
                            byte b = commentBytes[j];
                            if (b != 0 && b != 32) {
                                return new String(commentBytes, j, commentBytes.length - j, charset).trim();
                            }
                        }
                        return new String(commentBytes, 10, commentBytes.length - 10, charset).trim();
                    }
                }
            }
            return new String(commentBytes, System.getProperty("file.encoding")).trim();
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    @Nullable
    public String getIsoEquivalentDescription() {
        Integer isoEquiv = this._directory.getInteger(ExifDirectoryBase.TAG_ISO_EQUIVALENT);
        if (isoEquiv != null) {
            return Integer.toString(isoEquiv.intValue());
        }
        return null;
    }

    @Nullable
    public String getExifVersionDescription() {
        return getVersionBytesDescription(36864, 2);
    }

    @Nullable
    public String getFlashPixVersionDescription() {
        return getVersionBytesDescription(40960, 2);
    }

    @Nullable
    public String getSceneTypeDescription() {
        return getIndexedDescription(ExifDirectoryBase.TAG_SCENE_TYPE, 1, "Directly photographed image");
    }

    @Nullable
    public String getCfaPatternDescription() {
        return formatCFAPattern(decodeCfaPattern(ExifDirectoryBase.TAG_CFA_PATTERN));
    }

    @Nullable
    public String getCfaPattern2Description() {
        byte[] values = this._directory.getByteArray(ExifDirectoryBase.TAG_CFA_PATTERN_2);
        if (values == null) {
            return null;
        }
        int[] repeatPattern = this._directory.getIntArray(ExifDirectoryBase.TAG_CFA_REPEAT_PATTERN_DIM);
        if (repeatPattern == null) {
            return String.format("Repeat Pattern not found for CFAPattern (%s)", super.getDescription(ExifDirectoryBase.TAG_CFA_PATTERN_2));
        }
        if (repeatPattern.length == 2 && values.length == repeatPattern[0] * repeatPattern[1]) {
            int[] intpattern = new int[2 + values.length];
            intpattern[0] = repeatPattern[0];
            intpattern[1] = repeatPattern[1];
            for (int i = 0; i < values.length; i++) {
                intpattern[i + 2] = values[i] & 255;
            }
            return formatCFAPattern(intpattern);
        }
        return String.format("Unknown Pattern (%s)", super.getDescription(ExifDirectoryBase.TAG_CFA_PATTERN_2));
    }

    @Nullable
    private static String formatCFAPattern(@Nullable int[] pattern) {
        if (pattern == null) {
            return null;
        }
        if (pattern.length < 2) {
            return "<truncated data>";
        }
        if (pattern[0] == 0 && pattern[1] == 0) {
            return "<zero pattern size>";
        }
        int end = 2 + (pattern[0] * pattern[1]);
        if (end > pattern.length) {
            return "<invalid pattern size>";
        }
        String[] cfaColors = {"Red", "Green", "Blue", "Cyan", "Magenta", "Yellow", "White"};
        StringBuilder ret = new StringBuilder();
        ret.append(PropertyAccessor.PROPERTY_KEY_PREFIX);
        for (int pos = 2; pos < end; pos++) {
            if (pattern[pos] <= cfaColors.length - 1) {
                ret.append(cfaColors[pattern[pos]]);
            } else {
                ret.append(Dump.UNKNOWN_FILENAME);
            }
            if ((pos - 2) % pattern[1] == 0) {
                ret.append(",");
            } else if (pos != end - 1) {
                ret.append("][");
            }
        }
        ret.append("]");
        return ret.toString();
    }

    @Nullable
    private int[] decodeCfaPattern(int tagType) {
        byte[] values = this._directory.getByteArray(tagType);
        if (values == null) {
            return null;
        }
        if (values.length < 4) {
            int[] ret = new int[values.length];
            for (int i = 0; i < values.length; i++) {
                ret[i] = values[i];
            }
            return ret;
        }
        int[] ret2 = new int[values.length - 2];
        try {
            ByteArrayReader reader = new ByteArrayReader(values);
            short item0 = reader.getInt16(0);
            short item1 = reader.getInt16(2);
            Boolean copyArray = false;
            int end = 2 + (item0 * item1);
            if (end > values.length) {
                reader.setMotorolaByteOrder(!reader.isMotorolaByteOrder());
                item0 = reader.getInt16(0);
                item1 = reader.getInt16(2);
                if (values.length >= 2 + (item0 * item1)) {
                    copyArray = true;
                }
            } else {
                copyArray = true;
            }
            if (copyArray.booleanValue()) {
                ret2[0] = item0;
                ret2[1] = item1;
                for (int i2 = 4; i2 < values.length; i2++) {
                    ret2[i2 - 2] = reader.getInt8(i2);
                }
            }
        } catch (IOException ex) {
            this._directory.addError("IO exception processing data: " + ex.getMessage());
        }
        return ret2;
    }

    @Nullable
    public String getFileSourceDescription() {
        return getIndexedDescription(ExifDirectoryBase.TAG_FILE_SOURCE, 1, "Film Scanner", "Reflection Print Scanner", "Digital Still Camera (DSC)");
    }

    @Nullable
    public String getExposureBiasDescription() {
        Rational value = this._directory.getRational(ExifDirectoryBase.TAG_EXPOSURE_BIAS);
        if (value == null) {
            return null;
        }
        return value.toSimpleString(true) + " EV";
    }

    @Nullable
    public String getMaxApertureValueDescription() {
        Double aperture = this._directory.getDoubleObject(ExifDirectoryBase.TAG_MAX_APERTURE);
        if (aperture == null) {
            return null;
        }
        double fStop = PhotographicConversions.apertureToFStop(aperture.doubleValue());
        return getFStopDescription(fStop);
    }

    @Nullable
    public String getApertureValueDescription() {
        Double aperture = this._directory.getDoubleObject(ExifDirectoryBase.TAG_APERTURE);
        if (aperture == null) {
            return null;
        }
        double fStop = PhotographicConversions.apertureToFStop(aperture.doubleValue());
        return getFStopDescription(fStop);
    }

    @Nullable
    public String getExposureProgramDescription() {
        return getIndexedDescription(ExifDirectoryBase.TAG_EXPOSURE_PROGRAM, 1, "Manual control", "Program normal", "Aperture priority", "Shutter priority", "Program creative (slow program)", "Program action (high-speed program)", "Portrait mode", "Landscape mode");
    }

    @Nullable
    public String getFocalPlaneXResolutionDescription() {
        Rational rational = this._directory.getRational(ExifDirectoryBase.TAG_FOCAL_PLANE_X_RESOLUTION);
        if (rational == null) {
            return null;
        }
        String unit = getFocalPlaneResolutionUnitDescription();
        return rational.getReciprocal().toSimpleString(true) + (unit == null ? "" : SymbolConstants.SPACE_SYMBOL + unit.toLowerCase());
    }

    @Nullable
    public String getFocalPlaneYResolutionDescription() {
        Rational rational = this._directory.getRational(ExifDirectoryBase.TAG_FOCAL_PLANE_Y_RESOLUTION);
        if (rational == null) {
            return null;
        }
        String unit = getFocalPlaneResolutionUnitDescription();
        return rational.getReciprocal().toSimpleString(true) + (unit == null ? "" : SymbolConstants.SPACE_SYMBOL + unit.toLowerCase());
    }

    @Nullable
    public String getFocalPlaneResolutionUnitDescription() {
        return getIndexedDescription(ExifDirectoryBase.TAG_FOCAL_PLANE_RESOLUTION_UNIT, 1, "(No unit)", "Inches", "cm");
    }

    @Nullable
    public String getExifImageWidthDescription() {
        Integer value = this._directory.getInteger(40962);
        if (value == null) {
            return null;
        }
        return value + " pixels";
    }

    @Nullable
    public String getExifImageHeightDescription() {
        Integer value = this._directory.getInteger(40963);
        if (value == null) {
            return null;
        }
        return value + " pixels";
    }

    @Nullable
    public String getColorSpaceDescription() {
        Integer value = this._directory.getInteger(40961);
        if (value == null) {
            return null;
        }
        if (value.intValue() == 1) {
            return PngImageHelper.sRGB;
        }
        if (value.intValue() == 65535) {
            return "Undefined";
        }
        return "Unknown (" + value + ")";
    }

    @Nullable
    public String getFocalLengthDescription() {
        Rational value = this._directory.getRational(ExifDirectoryBase.TAG_FOCAL_LENGTH);
        if (value == null) {
            return null;
        }
        return getFocalLengthDescription(value.doubleValue());
    }

    @Nullable
    public String getFlashDescription() {
        Integer value = this._directory.getInteger(ExifDirectoryBase.TAG_FLASH);
        if (value == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if ((value.intValue() & 1) != 0) {
            sb.append("Flash fired");
        } else {
            sb.append("Flash did not fire");
        }
        if ((value.intValue() & 4) != 0) {
            if ((value.intValue() & 2) != 0) {
                sb.append(", return detected");
            } else {
                sb.append(", return not detected");
            }
        }
        if ((value.intValue() & 16) != 0) {
            sb.append(", auto");
        }
        if ((value.intValue() & 64) != 0) {
            sb.append(", red-eye reduction");
        }
        return sb.toString();
    }

    @Nullable
    public String getWhiteBalanceDescription() {
        Integer value = this._directory.getInteger(37384);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return Dump.UNKNOWN_FILENAME;
            case 1:
                return "Daylight";
            case 2:
                return "Florescent";
            case 3:
                return "Tungsten";
            case 4:
                return "Flash";
            case 9:
                return "Fine Weather";
            case 10:
                return "Cloudy";
            case 11:
                return "Shade";
            case 12:
                return "Daylight Fluorescent";
            case 13:
                return "Day White Fluorescent";
            case 14:
                return "Cool White Fluorescent";
            case 15:
                return "White Fluorescent";
            case 16:
                return "Warm White Fluorescent";
            case 17:
                return "Standard light";
            case 18:
                return "Standard light (B)";
            case 19:
                return "Standard light (C)";
            case 20:
                return "D55";
            case 21:
                return "D65";
            case 22:
                return "D75";
            case 23:
                return "D50";
            case 24:
                return "Studio Tungsten";
            case 255:
                return "(Other)";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getMeteringModeDescription() {
        Integer value = this._directory.getInteger(ExifDirectoryBase.TAG_METERING_MODE);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return Dump.UNKNOWN_FILENAME;
            case 1:
                return "Average";
            case 2:
                return "Center weighted average";
            case 3:
                return "Spot";
            case 4:
                return "Multi-spot";
            case 5:
                return "Multi-segment";
            case 6:
                return "Partial";
            case 255:
                return "(Other)";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getCompressionDescription() {
        Integer value = this._directory.getInteger(259);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 1:
                return "Uncompressed";
            case 2:
                return "CCITT 1D";
            case 3:
                return "T4/Group 3 Fax";
            case 4:
                return "T6/Group 4 Fax";
            case 5:
                return "LZW";
            case 6:
                return "JPEG (old-style)";
            case 7:
                return "JPEG";
            case 8:
                return "Adobe Deflate";
            case 9:
                return "JBIG B&W";
            case 10:
                return "JBIG Color";
            case 99:
                return "JPEG";
            case 262:
                return "Kodak 262";
            case TIFFConstants.COMPRESSION_NEXT /* 32766 */:
                return "Next";
            case Font.COLOR_NORMAL /* 32767 */:
                return "Sony ARW Compressed";
            case 32769:
                return "Packed RAW";
            case FujifilmMakernoteDirectory.TAG_ORDER_NUMBER /* 32770 */:
                return "Samsung SRW Compressed";
            case 32771:
                return "CCIRLEW";
            case PanasonicMakernoteDirectory.TAG_WB_RED_LEVEL /* 32772 */:
                return "Samsung SRW Compressed 2";
            case 32773:
                return "PackBits";
            case TIFFConstants.COMPRESSION_THUNDERSCAN /* 32809 */:
                return "Thunderscan";
            case 32867:
                return "Kodak KDC Compressed";
            case TIFFConstants.COMPRESSION_IT8CTPAD /* 32895 */:
                return "IT8CTPAD";
            case TIFFConstants.COMPRESSION_IT8LW /* 32896 */:
                return "IT8LW";
            case TIFFConstants.COMPRESSION_IT8MP /* 32897 */:
                return "IT8MP";
            case TIFFConstants.COMPRESSION_IT8BL /* 32898 */:
                return "IT8BL";
            case TIFFConstants.COMPRESSION_PIXARFILM /* 32908 */:
                return "PixarFilm";
            case TIFFConstants.COMPRESSION_PIXARLOG /* 32909 */:
                return "PixarLog";
            case TIFFConstants.COMPRESSION_DEFLATE /* 32946 */:
                return "Deflate";
            case TIFFConstants.COMPRESSION_DCS /* 32947 */:
                return "DCS";
            case TIFFConstants.COMPRESSION_JBIG /* 34661 */:
                return "JBIG";
            case TIFFConstants.COMPRESSION_SGILOG /* 34676 */:
                return "SGILog";
            case TIFFConstants.COMPRESSION_SGILOG24 /* 34677 */:
                return "SGILog24";
            case 34712:
                return "JPEG 2000";
            case 34713:
                return "Nikon NEF Compressed";
            case 34715:
                return "JBIG2 TIFF FX";
            case 34718:
                return "Microsoft Document Imaging (MDI) Binary Level Codec";
            case 34719:
                return "Microsoft Document Imaging (MDI) Progressive Transform Codec";
            case 34720:
                return "Microsoft Document Imaging (MDI) Vector";
            case 34892:
                return "Lossy JPEG";
            case CodePageUtil.CP_US_ASCII2 /* 65000 */:
                return "Kodak DCR Compressed";
            case 65535:
                return "Pentax PEF Compressed";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getSubjectDistanceDescription() {
        Rational value = this._directory.getRational(ExifDirectoryBase.TAG_SUBJECT_DISTANCE);
        if (value == null) {
            return null;
        }
        DecimalFormat formatter = new DecimalFormat("0.0##");
        return formatter.format(value.doubleValue()) + " metres";
    }

    @Nullable
    public String getCompressedAverageBitsPerPixelDescription() {
        Rational value = this._directory.getRational(ExifDirectoryBase.TAG_COMPRESSED_AVERAGE_BITS_PER_PIXEL);
        if (value == null) {
            return null;
        }
        String ratio = value.toSimpleString(true);
        return (value.isInteger() && value.intValue() == 1) ? ratio + " bit/pixel" : ratio + " bits/pixel";
    }

    @Nullable
    public String getExposureTimeDescription() {
        String value = this._directory.getString(ExifDirectoryBase.TAG_EXPOSURE_TIME);
        if (value == null) {
            return null;
        }
        return value + " sec";
    }

    @Nullable
    public String getShutterSpeedDescription() {
        return super.getShutterSpeedDescription(ExifDirectoryBase.TAG_SHUTTER_SPEED);
    }

    @Nullable
    public String getFNumberDescription() {
        Rational value = this._directory.getRational(ExifDirectoryBase.TAG_FNUMBER);
        if (value == null) {
            return null;
        }
        return getFStopDescription(value.doubleValue());
    }

    @Nullable
    public String getSensingMethodDescription() {
        return getIndexedDescription(ExifDirectoryBase.TAG_SENSING_METHOD, 1, "(Not defined)", "One-chip color area sensor", "Two-chip color area sensor", "Three-chip color area sensor", "Color sequential area sensor", null, "Trilinear sensor", "Color sequential linear sensor");
    }

    @Nullable
    public String getComponentConfigurationDescription() {
        int[] components = this._directory.getIntArray(ExifDirectoryBase.TAG_COMPONENTS_CONFIGURATION);
        if (components == null) {
            return null;
        }
        String[] componentStrings = {"", "Y", "Cb", "Cr", "R", "G", "B"};
        StringBuilder componentConfig = new StringBuilder();
        for (int i = 0; i < Math.min(4, components.length); i++) {
            int j = components[i];
            if (j > 0 && j < componentStrings.length) {
                componentConfig.append(componentStrings[j]);
            }
        }
        return componentConfig.toString();
    }

    @Nullable
    public String getJpegProcDescription() {
        Integer value = this._directory.getInteger(512);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 1:
                return "Baseline";
            case 14:
                return "Lossless";
            default:
                return "Unknown (" + value + ")";
        }
    }
}
