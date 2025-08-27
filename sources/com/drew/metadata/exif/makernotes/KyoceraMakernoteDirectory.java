package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import java.util.HashMap;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/exif/makernotes/KyoceraMakernoteDirectory.class */
public class KyoceraMakernoteDirectory extends Directory {
    public static final int TAG_PROPRIETARY_THUMBNAIL = 1;
    public static final int TAG_PRINT_IMAGE_MATCHING_INFO = 3584;

    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(1, "Proprietary Thumbnail Format Data");
        _tagNameMap.put(3584, "Print Image Matching (PIM) Info");
    }

    public KyoceraMakernoteDirectory() {
        setDescriptor(new KyoceraMakernoteDescriptor(this));
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    public String getName() {
        return "Kyocera/Contax Makernote";
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    protected HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
