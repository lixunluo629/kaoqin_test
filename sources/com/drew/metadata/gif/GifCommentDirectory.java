package com.drew.metadata.gif;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import com.drew.metadata.StringValue;
import java.util.HashMap;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/gif/GifCommentDirectory.class */
public class GifCommentDirectory extends Directory {
    public static final int TAG_COMMENT = 1;

    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(1, "Comment");
    }

    public GifCommentDirectory(StringValue comment) {
        setDescriptor(new GifCommentDescriptor(this));
        setStringValue(1, comment);
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    public String getName() {
        return "GIF Comment";
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    protected HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
