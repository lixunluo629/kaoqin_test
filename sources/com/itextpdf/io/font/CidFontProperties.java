package com.itextpdf.io.font;

import com.itextpdf.io.font.constants.FontResources;
import com.itextpdf.io.util.IntHashtable;
import com.itextpdf.io.util.ResourceUtil;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/CidFontProperties.class */
public class CidFontProperties {
    private static final Map<String, Map<String, Object>> allFonts = new HashMap();
    private static final Map<String, Set<String>> registryNames = new HashMap();

    static {
        try {
            loadRegistry();
            for (String font : registryNames.get("fonts")) {
                allFonts.put(font, readFontProperties(font));
            }
        } catch (Exception e) {
        }
    }

    public static boolean isCidFont(String fontName, String enc) {
        if (!registryNames.containsKey("fonts") || !registryNames.get("fonts").contains(fontName)) {
            return false;
        }
        if (enc.equals(PdfEncodings.IDENTITY_H) || enc.equals(PdfEncodings.IDENTITY_V)) {
            return true;
        }
        String registry = (String) allFonts.get(fontName).get("Registry");
        Set<String> encodings = registryNames.get(registry);
        return encodings != null && encodings.contains(enc);
    }

    public static String getCompatibleFont(String enc) {
        for (Map.Entry<String, Set<String>> e : registryNames.entrySet()) {
            if (e.getValue().contains(enc)) {
                String registry = e.getKey();
                for (Map.Entry<String, Map<String, Object>> e1 : allFonts.entrySet()) {
                    if (registry.equals(e1.getValue().get("Registry"))) {
                        return e1.getKey();
                    }
                }
            }
        }
        return null;
    }

    public static Map<String, Map<String, Object>> getAllFonts() {
        return allFonts;
    }

    public static Map<String, Set<String>> getRegistryNames() {
        return registryNames;
    }

    private static void loadRegistry() throws IOException {
        InputStream resource = ResourceUtil.getResourceStream("com/itextpdf/io/font/cmap/cjk_registry.properties");
        Properties p = new Properties();
        p.load(resource);
        resource.close();
        for (Object key : p.keySet()) {
            String value = p.getProperty((String) key);
            String[] sp = value.split(SymbolConstants.SPACE_SYMBOL);
            Set<String> hs = new HashSet<>();
            for (String s : sp) {
                if (s.length() > 0) {
                    hs.add(s);
                }
            }
            registryNames.put((String) key, hs);
        }
    }

    private static Map<String, Object> readFontProperties(String name) throws IOException, NumberFormatException {
        InputStream resource = ResourceUtil.getResourceStream(FontResources.CMAPS + (name + ".properties"));
        Properties p = new Properties();
        p.load(resource);
        resource.close();
        Object W = createMetric(p.getProperty("W"));
        p.remove("W");
        Object W2 = createMetric(p.getProperty("W2"));
        p.remove("W2");
        Map<String, Object> map = new HashMap<>();
        for (Object obj : p.keySet()) {
            map.put((String) obj, p.getProperty((String) obj));
        }
        map.put("W", W);
        map.put("W2", W2);
        return map;
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
}
