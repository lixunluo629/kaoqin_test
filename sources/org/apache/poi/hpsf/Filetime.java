package org.apache.poi.hpsf;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.LittleEndianByteArrayInputStream;

/* loaded from: poi-3.17.jar:org/apache/poi/hpsf/Filetime.class */
public class Filetime {
    private static final long EPOCH_DIFF = -11644473600000L;
    private static final int SIZE = 8;
    private static final long UINT_MASK = 4294967295L;
    private static final long NANO_100 = 10000;
    private int _dwHighDateTime;
    private int _dwLowDateTime;

    Filetime() {
    }

    Filetime(int low, int high) {
        this._dwLowDateTime = low;
        this._dwHighDateTime = high;
    }

    Filetime(java.util.Date date) {
        long filetime = dateToFileTime(date);
        this._dwHighDateTime = (int) ((filetime >>> 32) & 4294967295L);
        this._dwLowDateTime = (int) (filetime & 4294967295L);
    }

    void read(LittleEndianByteArrayInputStream lei) {
        this._dwLowDateTime = lei.readInt();
        this._dwHighDateTime = lei.readInt();
    }

    long getHigh() {
        return this._dwHighDateTime;
    }

    long getLow() {
        return this._dwLowDateTime;
    }

    byte[] toByteArray() {
        byte[] result = new byte[8];
        LittleEndian.putInt(result, 0, this._dwLowDateTime);
        LittleEndian.putInt(result, 4, this._dwHighDateTime);
        return result;
    }

    int write(OutputStream out) throws IOException {
        LittleEndian.putInt(this._dwLowDateTime, out);
        LittleEndian.putInt(this._dwHighDateTime, out);
        return 8;
    }

    java.util.Date getJavaValue() {
        long l = (this._dwHighDateTime << 32) | (this._dwLowDateTime & 4294967295L);
        return filetimeToDate(l);
    }

    public static java.util.Date filetimeToDate(long filetime) {
        long ms_since_16010101 = filetime / 10000;
        long ms_since_19700101 = ms_since_16010101 + EPOCH_DIFF;
        return new java.util.Date(ms_since_19700101);
    }

    public static long dateToFileTime(java.util.Date date) {
        long ms_since_19700101 = date.getTime();
        long ms_since_16010101 = ms_since_19700101 - EPOCH_DIFF;
        return ms_since_16010101 * 10000;
    }

    public static boolean isUndefined(java.util.Date date) {
        return date == null || dateToFileTime(date) == 0;
    }
}
