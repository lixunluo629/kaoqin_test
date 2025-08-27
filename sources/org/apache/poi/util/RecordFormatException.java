package org.apache.poi.util;

/* loaded from: poi-3.17.jar:org/apache/poi/util/RecordFormatException.class */
public class RecordFormatException extends RuntimeException {
    public RecordFormatException(String exception) {
        super(exception);
    }

    public RecordFormatException(String exception, Throwable thr) {
        super(exception, thr);
    }

    public RecordFormatException(Throwable thr) {
        super(thr);
    }

    public static void check(boolean assertTrue, String message) {
        if (!assertTrue) {
            throw new RecordFormatException(message);
        }
    }
}
