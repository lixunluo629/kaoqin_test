package com.itextpdf.io.font;

import com.drew.metadata.adobe.AdobeJpegReader;
import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.font.cmap.CMapCidByte;
import com.itextpdf.io.font.cmap.CMapCidUni;
import com.itextpdf.io.font.cmap.CMapLocationFromBytes;
import com.itextpdf.io.font.cmap.CMapParser;
import com.itextpdf.io.source.ByteBuffer;
import com.itextpdf.io.util.IntHashtable;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.slf4j.LoggerFactory;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/CMapEncoding.class */
public class CMapEncoding implements Serializable {
    private static final List<byte[]> IDENTITY_H_V_CODESPACE_RANGES = Arrays.asList(new byte[]{new byte[]{0, 0}, new byte[]{-1, -1}});
    private static final long serialVersionUID = 2418291066110642993L;
    private String cmap;
    private String uniMap;
    private boolean isDirect;
    private CMapCidUni cid2Uni;
    private CMapCidByte cid2Code;
    private IntHashtable code2Cid;
    private List<byte[]> codeSpaceRanges;

    public CMapEncoding(String cmap) {
        this.cmap = cmap;
        if (cmap.equals(PdfEncodings.IDENTITY_H) || cmap.equals(PdfEncodings.IDENTITY_V)) {
            this.isDirect = true;
        }
        this.codeSpaceRanges = IDENTITY_H_V_CODESPACE_RANGES;
    }

    public CMapEncoding(String cmap, String uniMap) {
        this.cmap = cmap;
        this.uniMap = uniMap;
        if (cmap.equals(PdfEncodings.IDENTITY_H) || cmap.equals(PdfEncodings.IDENTITY_V)) {
            this.cid2Uni = FontCache.getCid2UniCmap(uniMap);
            this.isDirect = true;
            this.codeSpaceRanges = IDENTITY_H_V_CODESPACE_RANGES;
        } else {
            this.cid2Code = FontCache.getCid2Byte(cmap);
            this.code2Cid = this.cid2Code.getReversMap();
            this.codeSpaceRanges = this.cid2Code.getCodeSpaceRanges();
        }
    }

    public CMapEncoding(String cmap, byte[] cmapBytes) {
        this.cmap = cmap;
        this.cid2Code = new CMapCidByte();
        try {
            CMapParser.parseCid(cmap, this.cid2Code, new CMapLocationFromBytes(cmapBytes));
            this.code2Cid = this.cid2Code.getReversMap();
            this.codeSpaceRanges = this.cid2Code.getCodeSpaceRanges();
        } catch (IOException e) {
            LoggerFactory.getLogger(getClass()).error(LogMessageConstant.FAILED_TO_PARSE_ENCODING_STREAM);
        }
    }

    public boolean isDirect() {
        return this.isDirect;
    }

    public boolean hasUniMap() {
        return this.uniMap != null && this.uniMap.length() > 0;
    }

    public String getRegistry() {
        if (isDirect()) {
            return AdobeJpegReader.PREAMBLE;
        }
        return this.cid2Code.getRegistry();
    }

    public String getOrdering() {
        if (isDirect()) {
            return "Identity";
        }
        return this.cid2Code.getOrdering();
    }

    public int getSupplement() {
        if (isDirect()) {
            return 0;
        }
        return this.cid2Code.getSupplement();
    }

    public String getUniMapName() {
        return this.uniMap;
    }

    public String getCmapName() {
        return this.cmap;
    }

    public boolean isBuiltWith(String cmap) {
        return Objects.equals(cmap, this.cmap);
    }

    @Deprecated
    public int getCmapCode(int cid) {
        if (this.isDirect) {
            return cid;
        }
        return toInteger(this.cid2Code.lookup(cid));
    }

    public byte[] getCmapBytes(int cid) {
        int length = getCmapBytesLength(cid);
        byte[] result = new byte[length];
        fillCmapBytes(cid, result, 0);
        return result;
    }

    public int fillCmapBytes(int cid, byte[] array, int offset) {
        if (this.isDirect) {
            int offset2 = offset + 1;
            array[offset] = (byte) ((cid & 65280) >> 8);
            offset = offset2 + 1;
            array[offset2] = (byte) (cid & 255);
        } else {
            byte[] bytes = this.cid2Code.lookup(cid);
            for (byte b : bytes) {
                int i = offset;
                offset++;
                array[i] = b;
            }
        }
        return offset;
    }

    public void fillCmapBytes(int cid, ByteBuffer buffer) {
        if (this.isDirect) {
            buffer.append((byte) ((cid & 65280) >> 8));
            buffer.append((byte) (cid & 255));
        } else {
            byte[] bytes = this.cid2Code.lookup(cid);
            buffer.append(bytes);
        }
    }

    public int getCmapBytesLength(int cid) {
        if (this.isDirect) {
            return 2;
        }
        return this.cid2Code.lookup(cid).length;
    }

    public int getCidCode(int cmapCode) {
        if (this.isDirect) {
            return cmapCode;
        }
        return this.code2Cid.get(cmapCode);
    }

    public boolean containsCodeInCodeSpaceRange(int code, int length) {
        for (int i = 0; i < this.codeSpaceRanges.size(); i += 2) {
            if (length == this.codeSpaceRanges.get(i).length) {
                int mask = 255;
                int totalShift = 0;
                byte[] low = this.codeSpaceRanges.get(i);
                byte[] high = this.codeSpaceRanges.get(i + 1);
                boolean fitsIntoRange = true;
                int ind = length - 1;
                while (ind >= 0) {
                    int actualByteValue = (code & mask) >> totalShift;
                    if (actualByteValue < (255 & low[ind]) || actualByteValue > (255 & high[ind])) {
                        fitsIntoRange = false;
                    }
                    ind--;
                    totalShift += 8;
                    mask <<= 8;
                }
                if (fitsIntoRange) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int toInteger(byte[] bytes) {
        int result = 0;
        for (byte b : bytes) {
            result = (result << 8) + (b & 255);
        }
        return result;
    }
}
