package com.drew.metadata.jpeg;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/jpeg/JpegCommentDescriptor.class */
public class JpegCommentDescriptor extends TagDescriptor<JpegCommentDirectory> {
    public JpegCommentDescriptor(@NotNull JpegCommentDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getJpegCommentDescription() {
        return ((JpegCommentDirectory) this._directory).getString(0);
    }
}
