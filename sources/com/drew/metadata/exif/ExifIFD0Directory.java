package com.drew.metadata.exif;

import com.drew.lang.annotations.NotNull;
import java.util.HashMap;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/exif/ExifIFD0Directory.class */
public class ExifIFD0Directory extends ExifDirectoryBase {
    public static final int TAG_EXIF_SUB_IFD_OFFSET = 34665;
    public static final int TAG_GPS_INFO_OFFSET = 34853;

    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    public ExifIFD0Directory() {
        setDescriptor(new ExifIFD0Descriptor(this));
    }

    static {
        addExifTagNames(_tagNameMap);
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    public String getName() {
        return "Exif IFD0";
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    protected HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
