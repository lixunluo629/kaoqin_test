package com.drew.metadata.exif;

import com.drew.lang.annotations.NotNull;
import java.util.HashMap;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/exif/ExifThumbnailDirectory.class */
public class ExifThumbnailDirectory extends ExifDirectoryBase {
    public static final int TAG_THUMBNAIL_OFFSET = 513;
    public static final int TAG_THUMBNAIL_LENGTH = 514;

    @Deprecated
    public static final int TAG_THUMBNAIL_COMPRESSION = 259;

    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        addExifTagNames(_tagNameMap);
        _tagNameMap.put(513, "Thumbnail Offset");
        _tagNameMap.put(514, "Thumbnail Length");
    }

    public ExifThumbnailDirectory() {
        setDescriptor(new ExifThumbnailDescriptor(this));
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    public String getName() {
        return "Exif Thumbnail";
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    protected HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
