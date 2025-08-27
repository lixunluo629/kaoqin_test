package com.itextpdf.io.font;

import com.itextpdf.io.font.constants.FontResources;
import com.itextpdf.io.util.ResourceUtil;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/AdobeGlyphList.class */
public class AdobeGlyphList {
    private static Map<Integer, String> unicode2names = new HashMap();
    private static Map<String, Integer> names2unicode = new HashMap();

    static {
        InputStream resource = null;
        try {
            try {
                InputStream resource2 = ResourceUtil.getResourceStream(FontResources.ADOBE_GLYPH_LIST);
                if (resource2 == null) {
                    throw new Exception("com/itextpdf/io/font/AdobeGlyphList.txt not found as resource.");
                }
                byte[] buf = new byte[1024];
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                while (true) {
                    int size = resource2.read(buf);
                    if (size < 0) {
                        break;
                    } else {
                        stream.write(buf, 0, size);
                    }
                }
                resource2.close();
                InputStream resource3 = null;
                String s = PdfEncodings.convertToString(stream.toByteArray(), null);
                StringTokenizer tk2 = new StringTokenizer(s, "\r\n");
                while (tk2.hasMoreTokens()) {
                    String line = tk2.nextToken();
                    if (!line.startsWith("#")) {
                        StringTokenizer t2 = new StringTokenizer(line, " ;\r\n\t\f");
                        if (t2.hasMoreTokens()) {
                            String name = t2.nextToken();
                            if (t2.hasMoreTokens()) {
                                String hex = t2.nextToken();
                                if (!t2.hasMoreTokens()) {
                                    int num = Integer.parseInt(hex, 16);
                                    unicode2names.put(Integer.valueOf(num), name);
                                    names2unicode.put(name, Integer.valueOf(num));
                                }
                            }
                        }
                    }
                }
                if (0 != 0) {
                    try {
                        resource3.close();
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e2) {
                System.err.println("AdobeGlyphList.txt loading error: " + e2.getMessage());
                if (0 != 0) {
                    try {
                        resource.close();
                    } catch (Exception e3) {
                    }
                }
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    resource.close();
                } catch (Exception e4) {
                }
            }
            throw th;
        }
    }

    public static int nameToUnicode(String name) {
        int v = -1;
        if (names2unicode.containsKey(name)) {
            v = names2unicode.get(name).intValue();
        }
        if (v == -1 && name.length() == 7 && name.toLowerCase().startsWith("uni")) {
            try {
                return Integer.parseInt(name.substring(3), 16);
            } catch (Exception e) {
            }
        }
        return v;
    }

    public static String unicodeToName(int num) {
        return unicode2names.get(Integer.valueOf(num));
    }

    public static int getNameToUnicodeLength() {
        return names2unicode.size();
    }

    public static int getUnicodeToNameLength() {
        return unicode2names.size();
    }
}
