package com.itextpdf.io.codec.brotli.dec;

import java.nio.ByteBuffer;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/codec/brotli/dec/Dictionary.class */
public final class Dictionary {
    private static volatile ByteBuffer data;
    static final int[] OFFSETS_BY_LENGTH = {0, 0, 0, 0, 0, 4096, 9216, 21504, 35840, 44032, 53248, 63488, 74752, 87040, 93696, 100864, 104704, 106752, 108928, 113536, 115968, 118528, 119872, 121280, 122016};
    static final int[] SIZE_BITS_BY_LENGTH = {0, 0, 0, 0, 10, 10, 11, 11, 10, 10, 10, 10, 10, 9, 9, 8, 7, 7, 8, 7, 7, 6, 6, 5, 5};
    static final int MIN_WORD_LENGTH = 4;
    static final int MAX_WORD_LENGTH = 24;
    static final int MAX_TRANSFORMED_WORD_LENGTH = 37;

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/codec/brotli/dec/Dictionary$DataLoader.class */
    private static class DataLoader {
        static final boolean OK;

        private DataLoader() {
        }

        static {
            boolean ok = true;
            try {
                Class.forName(Dictionary.class.getPackage().getName() + ".DictionaryData");
            } catch (Throwable th) {
                ok = false;
            }
            OK = ok;
        }
    }

    public static void setData(ByteBuffer data2) {
        data = data2;
    }

    public static ByteBuffer getData() {
        if (data != null) {
            return data;
        }
        if (!DataLoader.OK) {
            throw new BrotliRuntimeException("brotli dictionary is not set");
        }
        return data;
    }
}
