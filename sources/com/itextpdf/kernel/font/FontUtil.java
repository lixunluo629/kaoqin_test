package com.itextpdf.kernel.font;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.font.FontCache;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.cmap.CMapLocationFromBytes;
import com.itextpdf.io.font.cmap.CMapParser;
import com.itextpdf.io.font.cmap.CMapToUnicode;
import com.itextpdf.io.font.cmap.CMapUniCid;
import com.itextpdf.io.font.cmap.ICMapLocation;
import com.itextpdf.io.util.IntHashtable;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfStream;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/font/FontUtil.class */
class FontUtil {
    private static final HashMap<String, CMapToUnicode> uniMaps = new HashMap<>();

    FontUtil() {
    }

    static CMapToUnicode processToUnicode(PdfObject toUnicode) {
        CMapToUnicode cMapToUnicode = null;
        if (toUnicode instanceof PdfStream) {
            try {
                byte[] uniBytes = ((PdfStream) toUnicode).getBytes();
                ICMapLocation lb = new CMapLocationFromBytes(uniBytes);
                cMapToUnicode = new CMapToUnicode();
                CMapParser.parseCid("", cMapToUnicode, lb);
            } catch (Exception e) {
                Logger logger = LoggerFactory.getLogger((Class<?>) CMapToUnicode.class);
                logger.error(LogMessageConstant.UNKNOWN_ERROR_WHILE_PROCESSING_CMAP);
                cMapToUnicode = CMapToUnicode.EmptyCMapToUnicodeMap;
            }
        } else if (PdfName.IdentityH.equals(toUnicode)) {
            cMapToUnicode = CMapToUnicode.getIdentity();
        }
        return cMapToUnicode;
    }

    static CMapToUnicode getToUnicodeFromUniMap(String uniMap) {
        CMapToUnicode toUnicode;
        if (uniMap == null) {
            return null;
        }
        synchronized (uniMaps) {
            if (uniMaps.containsKey(uniMap)) {
                return uniMaps.get(uniMap);
            }
            if (PdfEncodings.IDENTITY_H.equals(uniMap)) {
                toUnicode = CMapToUnicode.getIdentity();
            } else {
                CMapUniCid uni = FontCache.getUni2CidCmap(uniMap);
                if (uni == null) {
                    return null;
                }
                toUnicode = uni.exportToUnicode();
            }
            uniMaps.put(uniMap, toUnicode);
            return toUnicode;
        }
    }

    static String createRandomFontName() {
        StringBuilder s = new StringBuilder("");
        for (int k = 0; k < 7; k++) {
            s.append((char) ((Math.random() * 26.0d) + 65.0d));
        }
        return s.toString();
    }

    static int[] convertSimpleWidthsArray(PdfArray widthsArray, int first, int missingWidth) {
        int[] res = new int[256];
        for (int i = 0; i < res.length; i++) {
            res[i] = missingWidth;
        }
        if (widthsArray == null) {
            Logger logger = LoggerFactory.getLogger((Class<?>) FontUtil.class);
            logger.warn(LogMessageConstant.FONT_DICTIONARY_WITH_NO_WIDTHS);
            return res;
        }
        for (int i2 = 0; i2 < widthsArray.size() && first + i2 < 256; i2++) {
            PdfNumber number = widthsArray.getAsNumber(i2);
            res[first + i2] = number != null ? number.intValue() : missingWidth;
        }
        return res;
    }

    static IntHashtable convertCompositeWidthsArray(PdfArray widthsArray) {
        IntHashtable res = new IntHashtable();
        if (widthsArray == null) {
            return res;
        }
        int k = 0;
        while (k < widthsArray.size()) {
            int c1 = widthsArray.getAsNumber(k).intValue();
            int k2 = k + 1;
            PdfObject obj = widthsArray.get(k2);
            if (obj.isArray()) {
                PdfArray subWidths = (PdfArray) obj;
                for (int j = 0; j < subWidths.size(); j++) {
                    int c2 = subWidths.getAsNumber(j).intValue();
                    int i = c1;
                    c1++;
                    res.put(i, c2);
                }
            } else {
                int c22 = ((PdfNumber) obj).intValue();
                k2++;
                int w = widthsArray.getAsNumber(k2).intValue();
                while (c1 <= c22) {
                    res.put(c1, w);
                    c1++;
                }
            }
            k = k2 + 1;
        }
        return res;
    }
}
