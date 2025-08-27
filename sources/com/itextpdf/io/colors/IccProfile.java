package com.itextpdf.io.colors;

import com.itextpdf.io.IOException;
import com.itextpdf.io.source.RandomAccessFileOrArray;
import com.itextpdf.io.source.RandomAccessSourceFactory;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/colors/IccProfile.class */
public class IccProfile implements Serializable {
    private static final long serialVersionUID = -7466035855770591929L;
    protected byte[] data;
    protected int numComponents;
    private static Map<String, Integer> cstags = new HashMap();

    static {
        cstags.put("XYZ ", 3);
        cstags.put("Lab ", 3);
        cstags.put("Luv ", 3);
        cstags.put("YCbr", 3);
        cstags.put("Yxy ", 3);
        cstags.put("RGB ", 3);
        cstags.put("GRAY", 1);
        cstags.put("HSV ", 3);
        cstags.put("HLS ", 3);
        cstags.put("CMYK", 4);
        cstags.put("CMY ", 3);
        cstags.put("2CLR", 2);
        cstags.put("3CLR", 3);
        cstags.put("4CLR", 4);
        cstags.put("5CLR", 5);
        cstags.put("6CLR", 6);
        cstags.put("7CLR", 7);
        cstags.put("8CLR", 8);
        cstags.put("9CLR", 9);
        cstags.put("ACLR", 10);
        cstags.put("BCLR", 11);
        cstags.put("CCLR", 12);
        cstags.put("DCLR", 13);
        cstags.put("ECLR", 14);
        cstags.put("FCLR", 15);
    }

    protected IccProfile() {
    }

    public static IccProfile getInstance(byte[] data, int numComponents) {
        if (data.length < 128 || data[36] != 97 || data[37] != 99 || data[38] != 115 || data[39] != 112) {
            throw new IOException(IOException.InvalidIccProfile);
        }
        IccProfile icc = new IccProfile();
        icc.data = data;
        Integer cs = getIccNumberOfComponents(data);
        int nc = cs == null ? 0 : cs.intValue();
        icc.numComponents = nc;
        if (nc != numComponents) {
            throw new IOException(IOException.IccProfileContains0ComponentsWhileImageDataContains1Components).setMessageParams(Integer.valueOf(nc), Integer.valueOf(numComponents));
        }
        return icc;
    }

    public static IccProfile getInstance(byte[] data) {
        Integer cs = getIccNumberOfComponents(data);
        int numComponents = cs == null ? 0 : cs.intValue();
        return getInstance(data, numComponents);
    }

    public static IccProfile getInstance(RandomAccessFileOrArray file) {
        try {
            byte[] head = new byte[128];
            int remain = head.length;
            int ptr = 0;
            while (remain > 0) {
                int n = file.read(head, ptr, remain);
                if (n < 0) {
                    throw new IOException(IOException.InvalidIccProfile);
                }
                remain -= n;
                ptr += n;
            }
            if (head[36] != 97 || head[37] != 99 || head[38] != 115 || head[39] != 112) {
                throw new IOException(IOException.InvalidIccProfile);
            }
            int remain2 = ((head[0] & 255) << 24) | ((head[1] & 255) << 16) | ((head[2] & 255) << 8) | (head[3] & 255);
            byte[] icc = new byte[remain2];
            System.arraycopy(head, 0, icc, 0, head.length);
            int remain3 = remain2 - head.length;
            int ptr2 = head.length;
            while (remain3 > 0) {
                int n2 = file.read(icc, ptr2, remain3);
                if (n2 < 0) {
                    throw new IOException(IOException.InvalidIccProfile);
                }
                remain3 -= n2;
                ptr2 += n2;
            }
            return getInstance(icc);
        } catch (Exception ex) {
            throw new IOException(IOException.InvalidIccProfile, (Throwable) ex);
        }
    }

    public static IccProfile getInstance(InputStream stream) {
        try {
            RandomAccessFileOrArray raf = new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(stream));
            return getInstance(raf);
        } catch (java.io.IOException e) {
            throw new IOException(IOException.InvalidIccProfile, (Throwable) e);
        }
    }

    public static IccProfile getInstance(String filename) {
        try {
            RandomAccessFileOrArray raf = new RandomAccessFileOrArray(new RandomAccessSourceFactory().createBestSource(filename));
            return getInstance(raf);
        } catch (java.io.IOException e) {
            throw new IOException(IOException.InvalidIccProfile, (Throwable) e);
        }
    }

    public static String getIccColorSpaceName(byte[] data) {
        try {
            String colorSpace = new String(data, 16, 4, "US-ASCII");
            return colorSpace;
        } catch (UnsupportedEncodingException e) {
            throw new IOException(IOException.InvalidIccProfile, (Throwable) e);
        }
    }

    public static String getIccDeviceClass(byte[] data) {
        try {
            String deviceClass = new String(data, 12, 4, "US-ASCII");
            return deviceClass;
        } catch (UnsupportedEncodingException e) {
            throw new IOException(IOException.InvalidIccProfile, (Throwable) e);
        }
    }

    public static Integer getIccNumberOfComponents(byte[] data) {
        return cstags.get(getIccColorSpaceName(data));
    }

    public byte[] getData() {
        return this.data;
    }

    public int getNumComponents() {
        return this.numComponents;
    }
}
