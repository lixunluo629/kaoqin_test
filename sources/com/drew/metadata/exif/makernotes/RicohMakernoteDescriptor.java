package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/exif/makernotes/RicohMakernoteDescriptor.class */
public class RicohMakernoteDescriptor extends TagDescriptor<RicohMakernoteDirectory> {
    public RicohMakernoteDescriptor(@NotNull RicohMakernoteDirectory directory) {
        super(directory);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:3:0x0001. Please report as an issue. */
    @Override // com.drew.metadata.TagDescriptor
    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
        }
        return super.getDescription(tagType);
    }
}
