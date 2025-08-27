package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import java.util.HashMap;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/exif/makernotes/CasioType2MakernoteDirectory.class */
public class CasioType2MakernoteDirectory extends Directory {
    public static final int TAG_THUMBNAIL_DIMENSIONS = 2;
    public static final int TAG_THUMBNAIL_SIZE = 3;
    public static final int TAG_THUMBNAIL_OFFSET = 4;
    public static final int TAG_QUALITY_MODE = 8;
    public static final int TAG_IMAGE_SIZE = 9;
    public static final int TAG_FOCUS_MODE_1 = 13;
    public static final int TAG_ISO_SENSITIVITY = 20;
    public static final int TAG_WHITE_BALANCE_1 = 25;
    public static final int TAG_FOCAL_LENGTH = 29;
    public static final int TAG_SATURATION = 31;
    public static final int TAG_CONTRAST = 32;
    public static final int TAG_SHARPNESS = 33;
    public static final int TAG_PRINT_IMAGE_MATCHING_INFO = 3584;
    public static final int TAG_PREVIEW_THUMBNAIL = 8192;
    public static final int TAG_WHITE_BALANCE_BIAS = 8209;
    public static final int TAG_WHITE_BALANCE_2 = 8210;
    public static final int TAG_OBJECT_DISTANCE = 8226;
    public static final int TAG_FLASH_DISTANCE = 8244;
    public static final int TAG_RECORD_MODE = 12288;
    public static final int TAG_SELF_TIMER = 12289;
    public static final int TAG_QUALITY = 12290;
    public static final int TAG_FOCUS_MODE_2 = 12291;
    public static final int TAG_TIME_ZONE = 12294;
    public static final int TAG_BESTSHOT_MODE = 12295;
    public static final int TAG_CCD_ISO_SENSITIVITY = 12308;
    public static final int TAG_COLOUR_MODE = 12309;
    public static final int TAG_ENHANCEMENT = 12310;
    public static final int TAG_FILTER = 12311;

    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(2, "Thumbnail Dimensions");
        _tagNameMap.put(3, "Thumbnail Size");
        _tagNameMap.put(4, "Thumbnail Offset");
        _tagNameMap.put(8, "Quality Mode");
        _tagNameMap.put(9, "Image Size");
        _tagNameMap.put(13, "Focus Mode");
        _tagNameMap.put(20, "ISO Sensitivity");
        _tagNameMap.put(25, "White Balance");
        _tagNameMap.put(29, "Focal Length");
        _tagNameMap.put(31, "Saturation");
        _tagNameMap.put(32, "Contrast");
        _tagNameMap.put(33, "Sharpness");
        _tagNameMap.put(3584, "Print Image Matching (PIM) Info");
        _tagNameMap.put(8192, "Casio Preview Thumbnail");
        _tagNameMap.put(8209, "White Balance Bias");
        _tagNameMap.put(8210, "White Balance");
        _tagNameMap.put(8226, "Object Distance");
        _tagNameMap.put(Integer.valueOf(TAG_FLASH_DISTANCE), "Flash Distance");
        _tagNameMap.put(12288, "Record Mode");
        _tagNameMap.put(Integer.valueOf(TAG_SELF_TIMER), "Self Timer");
        _tagNameMap.put(Integer.valueOf(TAG_QUALITY), "Quality");
        _tagNameMap.put(Integer.valueOf(TAG_FOCUS_MODE_2), "Focus Mode");
        _tagNameMap.put(Integer.valueOf(TAG_TIME_ZONE), "Time Zone");
        _tagNameMap.put(Integer.valueOf(TAG_BESTSHOT_MODE), "BestShot Mode");
        _tagNameMap.put(Integer.valueOf(TAG_CCD_ISO_SENSITIVITY), "CCD ISO Sensitivity");
        _tagNameMap.put(Integer.valueOf(TAG_COLOUR_MODE), "Colour Mode");
        _tagNameMap.put(Integer.valueOf(TAG_ENHANCEMENT), "Enhancement");
        _tagNameMap.put(Integer.valueOf(TAG_FILTER), "Filter");
    }

    public CasioType2MakernoteDirectory() {
        setDescriptor(new CasioType2MakernoteDescriptor(this));
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    public String getName() {
        return "Casio Makernote";
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    protected HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
