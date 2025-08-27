package com.itextpdf.io.font;

import com.itextpdf.io.font.constants.StandardFontFamilies;
import com.itextpdf.io.util.FileUtil;
import com.itextpdf.io.util.MessageFormatUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/FontRegisterProvider.class */
class FontRegisterProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) FontRegisterProvider.class);
    private final Map<String, String> fontNames = new HashMap();
    private final Map<String, List<String>> fontFamilies = new HashMap();

    FontRegisterProvider() {
        registerStandardFonts();
        registerStandardFontFamilies();
    }

    FontProgram getFont(String fontName, int style) throws IOException {
        return getFont(fontName, style, true);
    }

    FontProgram getFont(String fontName, int style, boolean cached) throws IOException {
        if (fontName == null) {
            return null;
        }
        String lowerCaseFontName = fontName.toLowerCase();
        List<String> family = !lowerCaseFontName.equalsIgnoreCase("Times-Roman") ? this.fontFamilies.get(lowerCaseFontName) : this.fontFamilies.get(StandardFontFamilies.TIMES.toLowerCase());
        if (family != null) {
            synchronized (family) {
                int s = style == -1 ? 0 : style;
                Iterator<String> it = family.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    String f = it.next();
                    String lcf = f.toLowerCase();
                    int fs = 0;
                    if (lcf.contains("bold")) {
                        fs = 0 | 1;
                    }
                    if (lcf.contains("italic") || lcf.contains("oblique")) {
                        fs |= 2;
                    }
                    if ((s & 3) == fs) {
                        fontName = f;
                        break;
                    }
                }
            }
        }
        return getFontProgram(fontName, cached);
    }

    protected void registerStandardFonts() {
        this.fontNames.put("Courier".toLowerCase(), "Courier");
        this.fontNames.put("Courier-Bold".toLowerCase(), "Courier-Bold");
        this.fontNames.put("Courier-Oblique".toLowerCase(), "Courier-Oblique");
        this.fontNames.put("Courier-BoldOblique".toLowerCase(), "Courier-BoldOblique");
        this.fontNames.put("Helvetica".toLowerCase(), "Helvetica");
        this.fontNames.put("Helvetica-Bold".toLowerCase(), "Helvetica-Bold");
        this.fontNames.put("Helvetica-Oblique".toLowerCase(), "Helvetica-Oblique");
        this.fontNames.put("Helvetica-BoldOblique".toLowerCase(), "Helvetica-BoldOblique");
        this.fontNames.put("Symbol".toLowerCase(), "Symbol");
        this.fontNames.put("Times-Roman".toLowerCase(), "Times-Roman");
        this.fontNames.put("Times-Bold".toLowerCase(), "Times-Bold");
        this.fontNames.put("Times-Italic".toLowerCase(), "Times-Italic");
        this.fontNames.put("Times-BoldItalic".toLowerCase(), "Times-BoldItalic");
        this.fontNames.put("ZapfDingbats".toLowerCase(), "ZapfDingbats");
    }

    protected void registerStandardFontFamilies() {
        List<String> family = new ArrayList<>();
        family.add("Courier");
        family.add("Courier-Bold");
        family.add("Courier-Oblique");
        family.add("Courier-BoldOblique");
        this.fontFamilies.put("Courier".toLowerCase(), family);
        List<String> family2 = new ArrayList<>();
        family2.add("Helvetica");
        family2.add("Helvetica-Bold");
        family2.add("Helvetica-Oblique");
        family2.add("Helvetica-BoldOblique");
        this.fontFamilies.put("Helvetica".toLowerCase(), family2);
        List<String> family3 = new ArrayList<>();
        family3.add("Symbol");
        this.fontFamilies.put("Symbol".toLowerCase(), family3);
        List<String> family4 = new ArrayList<>();
        family4.add("Times-Roman");
        family4.add("Times-Bold");
        family4.add("Times-Italic");
        family4.add("Times-BoldItalic");
        this.fontFamilies.put(StandardFontFamilies.TIMES.toLowerCase(), family4);
        List<String> family5 = new ArrayList<>();
        family5.add("ZapfDingbats");
        this.fontFamilies.put("ZapfDingbats".toLowerCase(), family5);
    }

    protected FontProgram getFontProgram(String fontName, boolean cached) throws IOException {
        FontProgram fontProgram = null;
        String fontName2 = this.fontNames.get(fontName.toLowerCase());
        if (fontName2 != null) {
            fontProgram = FontProgramFactory.createFont(fontName2, cached);
        }
        return fontProgram;
    }

    void registerFontFamily(String familyName, String fullName, String path) {
        List<String> family;
        if (path != null) {
            this.fontNames.put(fullName, path);
        }
        synchronized (this.fontFamilies) {
            family = this.fontFamilies.get(familyName);
            if (family == null) {
                family = new ArrayList();
                this.fontFamilies.put(familyName, family);
            }
        }
        synchronized (family) {
            if (!family.contains(fullName)) {
                int fullNameLength = fullName.length();
                boolean inserted = false;
                int j = 0;
                while (true) {
                    if (j >= family.size()) {
                        break;
                    }
                    if (family.get(j).length() < fullNameLength) {
                        j++;
                    } else {
                        family.add(j, fullName);
                        inserted = true;
                        break;
                    }
                }
                if (!inserted) {
                    family.add(fullName);
                    String newFullName = fullName.toLowerCase();
                    if (newFullName.endsWith("regular")) {
                        family.add(0, fullName.substring(0, newFullName.substring(0, newFullName.length() - 7).trim().length()));
                    }
                }
            }
        }
    }

    void registerFont(String path) {
        registerFont(path, null);
    }

    void registerFont(String path, String alias) {
        try {
            if (path.toLowerCase().endsWith(".ttf") || path.toLowerCase().endsWith(".otf") || path.toLowerCase().indexOf(".ttc,") > 0) {
                FontProgramDescriptor descriptor = FontProgramDescriptorFactory.fetchDescriptor(path);
                this.fontNames.put(descriptor.getFontNameLowerCase(), path);
                if (alias != null) {
                    String lcAlias = alias.toLowerCase();
                    this.fontNames.put(lcAlias, path);
                    if (lcAlias.endsWith("regular")) {
                        saveCopyOfRegularFont(lcAlias, path);
                    }
                }
                for (String name : descriptor.getFullNameAllLangs()) {
                    this.fontNames.put(name, path);
                    if (name.endsWith("regular")) {
                        saveCopyOfRegularFont(name, path);
                    }
                }
                if (descriptor.getFamilyNameEnglishOpenType() != null) {
                    for (String fullName : descriptor.getFullNamesEnglishOpenType()) {
                        registerFontFamily(descriptor.getFamilyNameEnglishOpenType(), fullName, null);
                    }
                }
            } else if (path.toLowerCase().endsWith(".ttc")) {
                TrueTypeCollection ttc = new TrueTypeCollection(path);
                for (int i = 0; i < ttc.getTTCSize(); i++) {
                    String fullPath = path + "," + i;
                    if (alias != null) {
                        registerFont(fullPath, alias + "," + i);
                    } else {
                        registerFont(fullPath);
                    }
                }
            } else if (path.toLowerCase().endsWith(".afm") || path.toLowerCase().endsWith(".pfm")) {
                FontProgramDescriptor descriptor2 = FontProgramDescriptorFactory.fetchDescriptor(path);
                registerFontFamily(descriptor2.getFamilyNameLowerCase(), descriptor2.getFullNameLowerCase(), null);
                this.fontNames.put(descriptor2.getFontNameLowerCase(), path);
                this.fontNames.put(descriptor2.getFullNameLowerCase(), path);
            }
            LOGGER.trace(MessageFormatUtil.format("Registered {0}", path));
        } catch (IOException e) {
            throw new com.itextpdf.io.IOException(e);
        }
    }

    boolean saveCopyOfRegularFont(String regularFontName, String path) {
        String alias = regularFontName.substring(0, regularFontName.length() - 7).trim();
        if (!this.fontNames.containsKey(alias)) {
            this.fontNames.put(alias, path);
            return true;
        }
        return false;
    }

    int registerFontDirectory(String dir) {
        return registerFontDirectory(dir, false);
    }

    int registerFontDirectory(String dir, boolean scanSubdirectories) {
        String[] files;
        LOGGER.debug(MessageFormatUtil.format("Registering directory {0}, looking for fonts", dir));
        int count = 0;
        try {
            files = FileUtil.listFilesInDirectory(dir, scanSubdirectories);
        } catch (Exception e) {
        }
        if (files == null) {
            return 0;
        }
        for (String file : files) {
            try {
                String suffix = file.length() < 4 ? null : file.substring(file.length() - 4).toLowerCase();
                if (".afm".equals(suffix) || ".pfm".equals(suffix)) {
                    String pfb = file.substring(0, file.length() - 4) + ".pfb";
                    if (FileUtil.fileExists(pfb)) {
                        registerFont(file, null);
                        count++;
                    }
                } else if (".ttf".equals(suffix) || ".otf".equals(suffix) || ".ttc".equals(suffix)) {
                    registerFont(file, null);
                    count++;
                }
            } catch (Exception e2) {
            }
        }
        return count;
    }

    int registerSystemFontDirectories() {
        int count = 0;
        String[] withSubDirs = {FileUtil.getFontsDir(), "/usr/share/X11/fonts", "/usr/X/lib/X11/fonts", "/usr/openwin/lib/X11/fonts", "/usr/share/fonts", "/usr/X11R6/lib/X11/fonts"};
        for (String directory : withSubDirs) {
            count += registerFontDirectory(directory, true);
        }
        String[] withoutSubDirs = {"/Library/Fonts", "/System/Library/Fonts"};
        for (String directory2 : withoutSubDirs) {
            count += registerFontDirectory(directory2, false);
        }
        return count;
    }

    Set<String> getRegisteredFonts() {
        return this.fontNames.keySet();
    }

    Set<String> getRegisteredFontFamilies() {
        return this.fontFamilies.keySet();
    }

    boolean isRegisteredFont(String fontname) {
        return this.fontNames.containsKey(fontname.toLowerCase());
    }

    public void clearRegisteredFonts() {
        this.fontNames.clear();
        registerStandardFonts();
    }

    public void clearRegisteredFontFamilies() {
        this.fontFamilies.clear();
        registerStandardFontFamilies();
    }
}
