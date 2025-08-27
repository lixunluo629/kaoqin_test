package com.drew.metadata.exif;

import com.drew.lang.annotations.NotNull;
import java.util.HashMap;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/exif/ExifInteropDirectory.class */
public class ExifInteropDirectory extends ExifDirectoryBase {

    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        addExifTagNames(_tagNameMap);
    }

    public ExifInteropDirectory() {
        setDescriptor(new ExifInteropDescriptor(this));
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    public String getName() {
        return "Interoperability";
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    protected HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
