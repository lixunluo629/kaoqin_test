package com.drew.metadata.webp;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/webp/WebpDescriptor.class */
public class WebpDescriptor extends TagDescriptor<WebpDirectory> {
    public WebpDescriptor(@NotNull WebpDirectory directory) {
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
