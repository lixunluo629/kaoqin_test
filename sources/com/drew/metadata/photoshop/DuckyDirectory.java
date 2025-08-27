package com.drew.metadata.photoshop;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import com.drew.metadata.TagDescriptor;
import java.util.HashMap;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/photoshop/DuckyDirectory.class */
public class DuckyDirectory extends Directory {
    public static final int TAG_QUALITY = 1;
    public static final int TAG_COMMENT = 2;
    public static final int TAG_COPYRIGHT = 3;

    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(1, "Quality");
        _tagNameMap.put(2, "Comment");
        _tagNameMap.put(3, "Copyright");
    }

    public DuckyDirectory() {
        setDescriptor(new TagDescriptor(this));
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    public String getName() {
        return "Ducky";
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    protected HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
