package com.drew.metadata.exif;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/exif/ExifSubIFDDirectory.class */
public class ExifSubIFDDirectory extends ExifDirectoryBase {
    public static final int TAG_INTEROP_OFFSET = 40965;

    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    public ExifSubIFDDirectory() {
        setDescriptor(new ExifSubIFDDescriptor(this));
    }

    static {
        addExifTagNames(_tagNameMap);
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    public String getName() {
        return "Exif SubIFD";
    }

    @Override // com.drew.metadata.Directory
    @NotNull
    protected HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }

    @Nullable
    public Date getDateOriginal() {
        return getDateOriginal(null);
    }

    @Nullable
    public Date getDateOriginal(@Nullable TimeZone timeZone) {
        return getDate(ExifDirectoryBase.TAG_DATETIME_ORIGINAL, getString(ExifDirectoryBase.TAG_SUBSECOND_TIME_ORIGINAL), timeZone);
    }

    @Nullable
    public Date getDateDigitized() {
        return getDateDigitized(null);
    }

    @Nullable
    public Date getDateDigitized(@Nullable TimeZone timeZone) {
        return getDate(ExifDirectoryBase.TAG_DATETIME_DIGITIZED, getString(ExifDirectoryBase.TAG_SUBSECOND_TIME_DIGITIZED), timeZone);
    }
}
