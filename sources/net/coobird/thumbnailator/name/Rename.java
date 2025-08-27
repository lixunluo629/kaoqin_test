package net.coobird.thumbnailator.name;

import net.coobird.thumbnailator.ThumbnailParameter;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/name/Rename.class */
public abstract class Rename {
    public static final Rename NO_CHANGE = new Rename() { // from class: net.coobird.thumbnailator.name.Rename.1
        @Override // net.coobird.thumbnailator.name.Rename
        public String apply(String str, ThumbnailParameter thumbnailParameter) {
            return str;
        }
    };
    public static final Rename PREFIX_DOT_THUMBNAIL = new Rename() { // from class: net.coobird.thumbnailator.name.Rename.2
        @Override // net.coobird.thumbnailator.name.Rename
        public String apply(String str, ThumbnailParameter thumbnailParameter) {
            return appendPrefix(str, "thumbnail.");
        }
    };
    public static final Rename PREFIX_HYPHEN_THUMBNAIL = new Rename() { // from class: net.coobird.thumbnailator.name.Rename.3
        @Override // net.coobird.thumbnailator.name.Rename
        public String apply(String str, ThumbnailParameter thumbnailParameter) {
            return appendPrefix(str, "thumbnail-");
        }
    };

    @Deprecated
    public static final Rename PREFIX_HYPTHEN_THUMBNAIL = PREFIX_HYPHEN_THUMBNAIL;
    public static final Rename SUFFIX_DOT_THUMBNAIL = new Rename() { // from class: net.coobird.thumbnailator.name.Rename.4
        @Override // net.coobird.thumbnailator.name.Rename
        public String apply(String str, ThumbnailParameter thumbnailParameter) {
            return appendSuffix(str, ".thumbnail");
        }
    };
    public static final Rename SUFFIX_HYPHEN_THUMBNAIL = new Rename() { // from class: net.coobird.thumbnailator.name.Rename.5
        @Override // net.coobird.thumbnailator.name.Rename
        public String apply(String str, ThumbnailParameter thumbnailParameter) {
            return appendSuffix(str, "-thumbnail");
        }
    };

    @Deprecated
    public static final Rename SUFFIX_HYPTHEN_THUMBNAIL = SUFFIX_HYPHEN_THUMBNAIL;

    public abstract String apply(String str, ThumbnailParameter thumbnailParameter);

    protected Rename() {
    }

    protected String appendSuffix(String str, String str2) {
        String str3;
        int iLastIndexOf = str.lastIndexOf(46);
        if (iLastIndexOf != -1) {
            str3 = (str.substring(0, iLastIndexOf) + str2) + str.substring(iLastIndexOf);
        } else {
            str3 = str + str2;
        }
        return str3;
    }

    protected String appendPrefix(String str, String str2) {
        return str2 + str;
    }
}
