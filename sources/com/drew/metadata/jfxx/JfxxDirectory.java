package com.drew.metadata.jfxx;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import com.drew.metadata.MetadataException;
import java.util.HashMap;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/jfxx/JfxxDirectory.class */
public class JfxxDirectory extends Directory {
    public static final int TAG_EXTENSION_CODE = 5;

    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(5, "Extension Code");
    }

    public JfxxDirectory() {
        setDescriptor(new JfxxDescriptor(this));
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    public String getName() {
        return JfxxReader.PREAMBLE;
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    protected HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }

    public int getExtensionCode() throws MetadataException {
        return getInt(5);
    }
}
