package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import java.util.HashMap;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/exif/makernotes/AppleMakernoteDirectory.class */
public class AppleMakernoteDirectory extends Directory {
    public static final int TAG_RUN_TIME = 3;
    public static final int TAG_HDR_IMAGE_TYPE = 10;
    public static final int TAG_BURST_UUID = 11;

    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(3, "Run Time");
        _tagNameMap.put(10, "HDR Image Type");
        _tagNameMap.put(11, "Burst UUID");
    }

    public AppleMakernoteDirectory() {
        setDescriptor(new AppleMakernoteDescriptor(this));
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    public String getName() {
        return "Apple Makernote";
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    protected HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
