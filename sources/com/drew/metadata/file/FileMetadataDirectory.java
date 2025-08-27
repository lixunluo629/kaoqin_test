package com.drew.metadata.file;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import java.util.HashMap;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/file/FileMetadataDirectory.class */
public class FileMetadataDirectory extends Directory {
    public static final int TAG_FILE_NAME = 1;
    public static final int TAG_FILE_SIZE = 2;
    public static final int TAG_FILE_MODIFIED_DATE = 3;

    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(1, "File Name");
        _tagNameMap.put(2, "File Size");
        _tagNameMap.put(3, "File Modified Date");
    }

    public FileMetadataDirectory() {
        setDescriptor(new FileMetadataDescriptor(this));
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    public String getName() {
        return "File";
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    protected HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
