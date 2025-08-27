package com.itextpdf.io.font.cmap;

import com.itextpdf.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Font;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/cmap/CMapByteCid.class */
public class CMapByteCid extends AbstractCMap {
    private static final long serialVersionUID = 8843696844192313477L;
    private List<int[]> planes = new ArrayList();

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/cmap/CMapByteCid$Cursor.class */
    protected static class Cursor {
        public int offset;
        public int length;

        public Cursor(int offset, int length) {
            this.offset = offset;
            this.length = length;
        }
    }

    public CMapByteCid() {
        this.planes.add(new int[256]);
    }

    @Override // com.itextpdf.io.font.cmap.AbstractCMap
    void addChar(String mark, CMapObject code) {
        if (code.isNumber()) {
            encodeSequence(decodeStringToByte(mark), ((Integer) code.getValue()).intValue());
        }
    }

    public String decodeSequence(byte[] cidBytes, int offset, int length) {
        StringBuilder sb = new StringBuilder();
        Cursor cursor = new Cursor(offset, length);
        while (true) {
            int cid = decodeSingle(cidBytes, cursor);
            if (cid >= 0) {
                sb.append((char) cid);
            } else {
                return sb.toString();
            }
        }
    }

    protected int decodeSingle(byte[] cidBytes, Cursor cursor) {
        int end = cursor.offset + cursor.length;
        int i = 0;
        while (true) {
            int currentPlane = i;
            if (cursor.offset < end) {
                int i2 = cursor.offset;
                cursor.offset = i2 + 1;
                int one = cidBytes[i2] & 255;
                cursor.length--;
                int[] plane = this.planes.get(currentPlane);
                int cid = plane[one];
                if ((cid & 32768) == 0) {
                    return cid;
                }
                i = cid & Font.COLOR_NORMAL;
            } else {
                return -1;
            }
        }
    }

    private void encodeSequence(byte[] seq, int cid) {
        int size = seq.length - 1;
        int nextPlane = 0;
        for (int idx = 0; idx < size; idx++) {
            int[] plane = this.planes.get(nextPlane);
            int one = seq[idx] & 255;
            int c = plane[one];
            if (c != 0 && (c & 32768) == 0) {
                throw new IOException("Inconsistent mapping.");
            }
            if (c == 0) {
                this.planes.add(new int[256]);
                c = (this.planes.size() - 1) | 32768;
                plane[one] = c;
            }
            nextPlane = c & Font.COLOR_NORMAL;
        }
        int[] plane2 = this.planes.get(nextPlane);
        int one2 = seq[size] & 255;
        if ((plane2[one2] & 32768) != 0) {
            throw new IOException("Inconsistent mapping.");
        }
        plane2[one2] = cid;
    }
}
