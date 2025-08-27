package com.itextpdf.io.font;

import com.itextpdf.io.font.cmap.AbstractCMap;
import com.itextpdf.io.font.cmap.CMapByteCid;
import com.itextpdf.io.font.cmap.CMapCidByte;
import com.itextpdf.io.font.cmap.CMapCidUni;
import com.itextpdf.io.font.cmap.CMapLocationResource;
import com.itextpdf.io.font.cmap.CMapParser;
import com.itextpdf.io.font.cmap.CMapUniCid;
import com.itextpdf.io.font.constants.FontResources;
import com.itextpdf.io.util.IntHashtable;
import com.itextpdf.io.util.ResourceUtil;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/FontCache.class */
public class FontCache {
    private static final String CJK_REGISTRY_FILENAME = "cjk_registry.properties";
    private static final String FONTS_PROP = "fonts";
    private static final String REGISTRY_PROP = "Registry";
    private static final String W_PROP = "W";
    private static final String W2_PROP = "W2";
    private static final Map<String, Map<String, Object>> allCidFonts = new LinkedHashMap();
    private static final Map<String, Set<String>> registryNames = new HashMap();
    private static Map<FontCacheKey, FontProgram> fontCache = new ConcurrentHashMap();

    static {
        try {
            loadRegistry();
            for (String font : registryNames.get(FONTS_PROP)) {
                allCidFonts.put(font, readFontProperties(font));
            }
        } catch (Exception e) {
        }
    }

    protected static boolean isPredefinedCidFont(String fontName) {
        if (!registryNames.containsKey(FONTS_PROP) || !registryNames.get(FONTS_PROP).contains(fontName)) {
            return false;
        }
        return true;
    }

    public static String getCompatibleCidFont(String cmap) {
        for (Map.Entry<String, Set<String>> e : registryNames.entrySet()) {
            if (e.getValue().contains(cmap)) {
                String registry = e.getKey();
                for (Map.Entry<String, Map<String, Object>> e1 : allCidFonts.entrySet()) {
                    if (registry.equals(e1.getValue().get(REGISTRY_PROP))) {
                        return e1.getKey();
                    }
                }
            }
        }
        return null;
    }

    public static Set<String> getCompatibleCmaps(String fontName) {
        Map<String, Object> cidFonts = getAllPredefinedCidFonts().get(fontName);
        if (cidFonts == null) {
            return null;
        }
        String registry = (String) cidFonts.get(REGISTRY_PROP);
        return registryNames.get(registry);
    }

    public static Map<String, Map<String, Object>> getAllPredefinedCidFonts() {
        return allCidFonts;
    }

    public static Map<String, Set<String>> getRegistryNames() {
        return registryNames;
    }

    public static CMapCidUni getCid2UniCmap(String uniMap) {
        CMapCidUni cidUni = new CMapCidUni();
        return (CMapCidUni) parseCmap(uniMap, cidUni);
    }

    public static CMapUniCid getUni2CidCmap(String uniMap) {
        CMapUniCid uniCid = new CMapUniCid();
        return (CMapUniCid) parseCmap(uniMap, uniCid);
    }

    public static CMapByteCid getByte2CidCmap(String cmap) {
        CMapByteCid uniCid = new CMapByteCid();
        return (CMapByteCid) parseCmap(cmap, uniCid);
    }

    public static CMapCidByte getCid2Byte(String cmap) {
        CMapCidByte cidByte = new CMapCidByte();
        return (CMapCidByte) parseCmap(cmap, cidByte);
    }

    public static void clearSavedFonts() {
        fontCache.clear();
    }

    public static FontProgram getFont(String fontName) {
        return fontCache.get(FontCacheKey.create(fontName));
    }

    static FontProgram getFont(FontCacheKey key) {
        return fontCache.get(key);
    }

    public static FontProgram saveFont(FontProgram font, String fontName) {
        return saveFont(font, FontCacheKey.create(fontName));
    }

    static FontProgram saveFont(FontProgram font, FontCacheKey key) {
        FontProgram fontFound = fontCache.get(key);
        if (fontFound != null) {
            return fontFound;
        }
        fontCache.put(key, font);
        return font;
    }

    private static void loadRegistry() throws IOException {
        InputStream resource = ResourceUtil.getResourceStream("com/itextpdf/io/font/cmap/cjk_registry.properties");
        try {
            Properties p = new Properties();
            p.load(resource);
            for (Map.Entry<Object, Object> entry : p.entrySet()) {
                String value = (String) entry.getValue();
                String[] splitValue = value.split(SymbolConstants.SPACE_SYMBOL);
                Set<String> set = new HashSet<>();
                for (String s : splitValue) {
                    if (s.length() != 0) {
                        set.add(s);
                    }
                }
                registryNames.put((String) entry.getKey(), set);
            }
        } finally {
            if (resource != null) {
                resource.close();
            }
        }
    }

    private static Map<String, Object> readFontProperties(String name) throws IOException {
        InputStream resource = ResourceUtil.getResourceStream(FontResources.CMAPS + name + ".properties");
        try {
            Properties p = new Properties();
            p.load(resource);
            Map<String, Object> fontProperties = new HashMap<>();
            for (Map.Entry<Object, Object> entry : p.entrySet()) {
                fontProperties.put((String) entry.getKey(), entry.getValue());
            }
            fontProperties.put(W_PROP, createMetric((String) fontProperties.get(W_PROP)));
            fontProperties.put(W2_PROP, createMetric((String) fontProperties.get(W2_PROP)));
            if (resource != null) {
                resource.close();
            }
            return fontProperties;
        } catch (Throwable th) {
            if (resource != null) {
                resource.close();
            }
            throw th;
        }
    }

    private static IntHashtable createMetric(String s) throws NumberFormatException {
        IntHashtable h = new IntHashtable();
        StringTokenizer tk2 = new StringTokenizer(s);
        while (tk2.hasMoreTokens()) {
            int n1 = Integer.parseInt(tk2.nextToken());
            h.put(n1, Integer.parseInt(tk2.nextToken()));
        }
        return h;
    }

    private static <T extends AbstractCMap> T parseCmap(String name, T cmap) {
        try {
            CMapParser.parseCid(name, cmap, new CMapLocationResource());
            return cmap;
        } catch (IOException e) {
            throw new com.itextpdf.io.IOException("I/O exception.", (Throwable) e);
        }
    }
}
