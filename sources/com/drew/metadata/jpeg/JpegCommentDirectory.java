package com.drew.metadata.jpeg;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import java.util.HashMap;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/jpeg/JpegCommentDirectory.class */
public class JpegCommentDirectory extends Directory {
    public static final int TAG_COMMENT = 0;

    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(0, "JPEG Comment");
    }

    public JpegCommentDirectory() {
        setDescriptor(new JpegCommentDescriptor(this));
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    public String getName() {
        return "JpegComment";
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    protected HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
