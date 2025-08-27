package com.drew.metadata.webp;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import java.util.HashMap;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/webp/WebpDirectory.class */
public class WebpDirectory extends Directory {
    public static final int TAG_IMAGE_HEIGHT = 1;
    public static final int TAG_IMAGE_WIDTH = 2;
    public static final int TAG_HAS_ALPHA = 3;
    public static final int TAG_IS_ANIMATION = 4;

    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(1, "Image Height");
        _tagNameMap.put(2, "Image Width");
        _tagNameMap.put(3, "Has Alpha");
        _tagNameMap.put(4, "Is Animation");
    }

    public WebpDirectory() {
        setDescriptor(new WebpDescriptor(this));
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    public String getName() {
        return "WebP";
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    protected HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
