package org.apache.poi.xssf.usermodel;

import org.apache.poi.hssf.util.HSSFColor;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/usermodel/DefaultIndexedColorMap.class */
public class DefaultIndexedColorMap implements IndexedColorMap {
    @Override // org.apache.poi.xssf.usermodel.IndexedColorMap
    public byte[] getRGB(int index) {
        return getDefaultRGB(index);
    }

    public static byte[] getDefaultRGB(int index) {
        HSSFColor hssfColor = HSSFColor.getIndexHash().get(Integer.valueOf(index));
        if (hssfColor == null) {
            return null;
        }
        short[] rgbShort = hssfColor.getTriplet();
        return new byte[]{(byte) rgbShort[0], (byte) rgbShort[1], (byte) rgbShort[2]};
    }
}
