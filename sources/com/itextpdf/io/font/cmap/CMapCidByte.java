package com.itextpdf.io.font.cmap;

import com.itextpdf.io.util.IntHashtable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/cmap/CMapCidByte.class */
public class CMapCidByte extends AbstractCMap {
    private static final long serialVersionUID = 4956059671207068672L;
    private Map<Integer, byte[]> map = new HashMap();
    private final byte[] EMPTY = new byte[0];
    private List<byte[]> codeSpaceRanges = new ArrayList();

    @Override // com.itextpdf.io.font.cmap.AbstractCMap
    void addChar(String mark, CMapObject code) {
        if (code.isNumber()) {
            byte[] ser = decodeStringToByte(mark);
            this.map.put(Integer.valueOf(((Integer) code.getValue()).intValue()), ser);
        }
    }

    public byte[] lookup(int cid) {
        byte[] ser = this.map.get(Integer.valueOf(cid));
        if (ser == null) {
            return this.EMPTY;
        }
        return ser;
    }

    public IntHashtable getReversMap() {
        IntHashtable code2cid = new IntHashtable(this.map.size());
        Iterator<Integer> it = this.map.keySet().iterator();
        while (it.hasNext()) {
            int cid = it.next().intValue();
            byte[] bytes = this.map.get(Integer.valueOf(cid));
            int byteCode = 0;
            for (byte b : bytes) {
                byteCode = (byteCode << 8) + (b & 255);
            }
            code2cid.put(byteCode, cid);
        }
        return code2cid;
    }

    public List<byte[]> getCodeSpaceRanges() {
        return this.codeSpaceRanges;
    }

    @Override // com.itextpdf.io.font.cmap.AbstractCMap
    void addCodeSpaceRange(byte[] low, byte[] high) {
        this.codeSpaceRanges.add(low);
        this.codeSpaceRanges.add(high);
    }
}
