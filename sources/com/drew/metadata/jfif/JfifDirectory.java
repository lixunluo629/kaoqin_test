package com.drew.metadata.jfif;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import com.drew.metadata.MetadataException;
import java.util.HashMap;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/jfif/JfifDirectory.class */
public class JfifDirectory extends Directory {
    public static final int TAG_VERSION = 5;
    public static final int TAG_UNITS = 7;
    public static final int TAG_RESX = 8;
    public static final int TAG_RESY = 10;
    public static final int TAG_THUMB_WIDTH = 12;
    public static final int TAG_THUMB_HEIGHT = 13;

    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(5, "Version");
        _tagNameMap.put(7, "Resolution Units");
        _tagNameMap.put(10, "Y Resolution");
        _tagNameMap.put(8, "X Resolution");
        _tagNameMap.put(12, "Thumbnail Width Pixels");
        _tagNameMap.put(13, "Thumbnail Height Pixels");
    }

    public JfifDirectory() {
        setDescriptor(new JfifDescriptor(this));
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    public String getName() {
        return JfifReader.PREAMBLE;
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    protected HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }

    public int getVersion() throws MetadataException {
        return getInt(5);
    }

    public int getResUnits() throws MetadataException {
        return getInt(7);
    }

    @Deprecated
    public int getImageWidth() throws MetadataException {
        return getInt(10);
    }

    public int getResY() throws MetadataException {
        return getInt(10);
    }

    @Deprecated
    public int getImageHeight() throws MetadataException {
        return getInt(8);
    }

    public int getResX() throws MetadataException {
        return getInt(8);
    }
}
