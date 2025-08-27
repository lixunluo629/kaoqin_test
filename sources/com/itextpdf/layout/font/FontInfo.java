package com.itextpdf.layout.font;

import com.itextpdf.io.font.FontCacheKey;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramDescriptor;
import com.itextpdf.io.font.FontProgramDescriptorFactory;
import com.itextpdf.io.util.ArrayUtil;
import com.itextpdf.io.util.MessageFormatUtil;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/font/FontInfo.class */
public final class FontInfo {
    private static final Map<FontCacheKey, FontProgramDescriptor> fontNamesCache = new ConcurrentHashMap();
    private final String fontName;
    private final byte[] fontData;
    private final FontProgramDescriptor descriptor;
    private final Range range;
    private final int hash;
    private final String encoding;
    private final String alias;

    private FontInfo(String fontName, byte[] fontData, String encoding, FontProgramDescriptor descriptor, Range unicodeRange, String alias) {
        this.fontName = fontName;
        this.fontData = fontData;
        this.encoding = encoding;
        this.descriptor = descriptor;
        this.range = unicodeRange != null ? unicodeRange : RangeBuilder.getFullRange();
        this.alias = alias != null ? alias.toLowerCase() : null;
        this.hash = calculateHashCode(this.fontName, this.fontData, this.encoding, this.range);
    }

    public static FontInfo create(FontInfo fontInfo, String alias, Range range) {
        return new FontInfo(fontInfo.fontName, fontInfo.fontData, fontInfo.encoding, fontInfo.descriptor, range, alias);
    }

    public static FontInfo create(FontInfo fontInfo, String alias) {
        return create(fontInfo, alias, (Range) null);
    }

    public static FontInfo create(FontProgram fontProgram, String encoding, String alias, Range range) {
        FontProgramDescriptor descriptor = FontProgramDescriptorFactory.fetchDescriptor(fontProgram);
        return new FontInfo(descriptor.getFontName(), null, encoding, descriptor, range, alias);
    }

    public static FontInfo create(FontProgram fontProgram, String encoding, String alias) {
        return create(fontProgram, encoding, alias, (Range) null);
    }

    static FontInfo create(String fontName, String encoding, String alias, Range range) {
        FontCacheKey cacheKey = FontCacheKey.create(fontName);
        FontProgramDescriptor descriptor = getFontNamesFromCache(cacheKey);
        if (descriptor == null) {
            descriptor = FontProgramDescriptorFactory.fetchDescriptor(fontName);
            putFontNamesToCache(cacheKey, descriptor);
        }
        if (descriptor != null) {
            return new FontInfo(fontName, null, encoding, descriptor, range, alias);
        }
        return null;
    }

    static FontInfo create(byte[] fontProgram, String encoding, String alias, Range range) {
        FontCacheKey cacheKey = FontCacheKey.create(fontProgram);
        FontProgramDescriptor descriptor = getFontNamesFromCache(cacheKey);
        if (descriptor == null) {
            descriptor = FontProgramDescriptorFactory.fetchDescriptor(fontProgram);
            putFontNamesToCache(cacheKey, descriptor);
        }
        if (descriptor != null) {
            return new FontInfo(null, fontProgram, encoding, descriptor, range, alias);
        }
        return null;
    }

    public FontProgramDescriptor getDescriptor() {
        return this.descriptor;
    }

    public Range getFontUnicodeRange() {
        return this.range;
    }

    public String getFontName() {
        return this.fontName;
    }

    public byte[] getFontData() {
        return this.fontData;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public String getAlias() {
        return this.alias;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FontInfo)) {
            return false;
        }
        FontInfo that = (FontInfo) o;
        if (this.fontName == null ? that.fontName == null : this.fontName.equals(that.fontName)) {
            if (this.range.equals(that.range) && Arrays.equals(this.fontData, that.fontData) && (this.encoding == null ? that.encoding == null : this.encoding.equals(that.encoding))) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return this.hash;
    }

    public String toString() {
        String name = this.descriptor.getFontName();
        if (name.length() > 0) {
            if (this.encoding != null) {
                return MessageFormatUtil.format("{0}+{1}", name, this.encoding);
            }
            return name;
        }
        return super.toString();
    }

    private static int calculateHashCode(String fontName, byte[] bytes, String encoding, Range range) {
        int result = fontName != null ? fontName.hashCode() : 0;
        return (31 * ((31 * ((31 * result) + ArrayUtil.hashCode(bytes))) + (encoding != null ? encoding.hashCode() : 0))) + range.hashCode();
    }

    private static FontProgramDescriptor getFontNamesFromCache(FontCacheKey key) {
        return fontNamesCache.get(key);
    }

    private static void putFontNamesToCache(FontCacheKey key, FontProgramDescriptor descriptor) {
        if (descriptor != null) {
            fontNamesCache.put(key, descriptor);
        }
    }
}
