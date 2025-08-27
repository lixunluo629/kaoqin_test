package com.drew.metadata.gif;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/gif/GifAnimationDescriptor.class */
public class GifAnimationDescriptor extends TagDescriptor<GifAnimationDirectory> {
    public GifAnimationDescriptor(@NotNull GifAnimationDirectory directory) {
        super(directory);
    }

    @Override // com.drew.metadata.TagDescriptor
    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 1:
                return getIterationCountDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getIterationCountDescription() {
        Integer count = ((GifAnimationDirectory) this._directory).getInteger(1);
        if (count == null) {
            return null;
        }
        return count.intValue() == 0 ? "Infinite" : count.intValue() == 1 ? "Once" : count.intValue() == 2 ? "Twice" : count.toString() + " times";
    }
}
