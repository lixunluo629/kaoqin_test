package com.itextpdf.layout.font;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.util.FileUtil;
import com.itextpdf.kernel.font.Type3Font;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/font/FontSet.class */
public final class FontSet {
    private static final AtomicLong lastId = new AtomicLong();
    private final Set<FontInfo> fonts = new LinkedHashSet();
    private final Map<FontInfo, FontProgram> fontPrograms = new HashMap();
    private final long id = lastId.incrementAndGet();

    public int addDirectory(String dir, boolean scanSubdirectories) {
        int count = 0;
        String[] files = FileUtil.listFilesInDirectory(dir, scanSubdirectories);
        if (files == null) {
            return 0;
        }
        for (String file : files) {
            try {
                String suffix = file.length() < 4 ? null : file.substring(file.length() - 4).toLowerCase();
                if (".afm".equals(suffix) || ".pfm".equals(suffix)) {
                    String pfb = file.substring(0, file.length() - 4) + ".pfb";
                    if (FileUtil.fileExists(pfb) && addFont(file)) {
                        count++;
                    }
                } else if ((".ttf".equals(suffix) || ".otf".equals(suffix) || ".ttc".equals(suffix)) && addFont(file)) {
                    count++;
                }
            } catch (Exception e) {
            }
        }
        return count;
    }

    public int addDirectory(String dir) {
        return addDirectory(dir, false);
    }

    public boolean addFont(FontProgram fontProgram, String encoding, String alias, Range unicodeRange) {
        if (fontProgram == null) {
            return false;
        }
        if (fontProgram instanceof Type3Font) {
            Logger logger = LoggerFactory.getLogger((Class<?>) FontSet.class);
            logger.error(LogMessageConstant.TYPE3_FONT_CANNOT_BE_ADDED);
            return false;
        }
        FontInfo fi = FontInfo.create(fontProgram, encoding, alias, unicodeRange);
        if (addFont(fi)) {
            this.fontPrograms.put(fi, fontProgram);
            return true;
        }
        return false;
    }

    public boolean addFont(FontProgram fontProgram, String encoding, String alias) {
        return addFont(fontProgram, encoding, alias, (Range) null);
    }

    public boolean addFont(FontProgram fontProgram, String encoding) {
        return addFont(fontProgram, encoding, (String) null);
    }

    public boolean addFont(String fontPath, String encoding, String alias, Range unicodeRange) {
        return addFont(FontInfo.create(fontPath, encoding, alias, unicodeRange));
    }

    public boolean addFont(String fontPath, String encoding, String alias) {
        return addFont(fontPath, encoding, alias, (Range) null);
    }

    public boolean addFont(String fontPath, String encoding) {
        return addFont(FontInfo.create(fontPath, encoding, (String) null, (Range) null));
    }

    public boolean addFont(byte[] fontData, String encoding, String alias, Range unicodeRange) {
        return addFont(FontInfo.create(fontData, encoding, alias, unicodeRange));
    }

    public boolean addFont(byte[] fontData, String encoding, String alias) {
        return addFont(fontData, encoding, alias, (Range) null);
    }

    public boolean addFont(byte[] fontData, String encoding) {
        return addFont(FontInfo.create(fontData, encoding, (String) null, (Range) null));
    }

    public boolean addFont(String fontPath) {
        return addFont(fontPath, (String) null, (String) null);
    }

    public boolean addFont(byte[] fontData) {
        return addFont(fontData, (String) null, (String) null);
    }

    public boolean addFont(FontInfo fontInfo, String alias, Range unicodeRange) {
        return addFont(FontInfo.create(fontInfo, alias, unicodeRange));
    }

    public boolean addFont(FontInfo fontInfo, String alias) {
        return addFont(fontInfo, alias, (Range) null);
    }

    public final boolean addFont(FontInfo fontInfo) {
        if (fontInfo != null && !this.fonts.contains(fontInfo)) {
            this.fonts.add(fontInfo);
            return true;
        }
        return false;
    }

    public boolean contains(String fontName) {
        if (fontName == null || fontName.length() == 0) {
            return false;
        }
        String fontName2 = fontName.toLowerCase();
        for (FontInfo fi : getFonts()) {
            if (fontName2.equals(fi.getDescriptor().getFullNameLowerCase()) || fontName2.equals(fi.getDescriptor().getFontNameLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public Collection<FontInfo> get(String fontName) {
        if (fontName == null || fontName.length() == 0) {
            return Collections.emptyList();
        }
        String fontName2 = fontName.toLowerCase();
        List<FontInfo> list = new ArrayList<>();
        for (FontInfo fi : getFonts()) {
            if (fontName2.equals(fi.getDescriptor().getFullNameLowerCase()) || fontName2.equals(fi.getDescriptor().getFontNameLowerCase())) {
                list.add(fi);
            }
        }
        return list;
    }

    public Collection<FontInfo> getFonts() {
        return getFonts(null);
    }

    public Collection<FontInfo> getFonts(FontSet tempFonts) {
        return new FontSetCollection(this.fonts, tempFonts != null ? tempFonts.fonts : null);
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return this.fonts.size();
    }

    long getId() {
        return this.id;
    }

    FontProgram getFontProgram(FontInfo fontInfo) {
        return this.fontPrograms.get(fontInfo);
    }
}
