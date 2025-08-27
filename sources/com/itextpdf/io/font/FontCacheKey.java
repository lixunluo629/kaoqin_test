package com.itextpdf.io.font;

import java.util.Arrays;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/FontCacheKey.class */
public abstract class FontCacheKey {
    public static FontCacheKey create(String fontName) {
        return new FontCacheStringKey(fontName);
    }

    public static FontCacheKey create(String fontName, int ttcIndex) {
        return new FontCacheTtcKey(fontName, ttcIndex);
    }

    public static FontCacheKey create(byte[] fontProgram) {
        return new FontCacheBytesKey(fontProgram);
    }

    public static FontCacheKey create(byte[] fontProgram, int ttcIndex) {
        return new FontCacheTtcKey(fontProgram, ttcIndex);
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/FontCacheKey$FontCacheStringKey.class */
    private static class FontCacheStringKey extends FontCacheKey {
        private String fontName;

        FontCacheStringKey(String fontName) {
            this.fontName = fontName;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            FontCacheStringKey that = (FontCacheStringKey) o;
            return this.fontName != null ? this.fontName.equals(that.fontName) : that.fontName == null;
        }

        public int hashCode() {
            if (this.fontName != null) {
                return this.fontName.hashCode();
            }
            return 0;
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/FontCacheKey$FontCacheBytesKey.class */
    private static class FontCacheBytesKey extends FontCacheKey {
        private byte[] firstFontBytes;
        private int fontLength;
        private int hashcode;

        FontCacheBytesKey(byte[] fontBytes) {
            if (fontBytes != null) {
                this.firstFontBytes = fontBytes.length > 10000 ? Arrays.copyOf(fontBytes, 10000) : fontBytes;
                this.fontLength = fontBytes.length;
            }
            this.hashcode = calcHashCode();
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            FontCacheBytesKey that = (FontCacheBytesKey) o;
            if (this.fontLength != that.fontLength) {
                return false;
            }
            return Arrays.equals(this.firstFontBytes, that.firstFontBytes);
        }

        public int hashCode() {
            return this.hashcode;
        }

        private int calcHashCode() {
            int result = Arrays.hashCode(this.firstFontBytes);
            return (31 * result) + this.fontLength;
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/FontCacheKey$FontCacheTtcKey.class */
    private static class FontCacheTtcKey extends FontCacheKey {
        private FontCacheKey ttcKey;
        private int ttcIndex;

        FontCacheTtcKey(String fontName, int ttcIndex) {
            this.ttcKey = new FontCacheStringKey(fontName);
            this.ttcIndex = ttcIndex;
        }

        FontCacheTtcKey(byte[] fontBytes, int ttcIndex) {
            this.ttcKey = new FontCacheBytesKey(fontBytes);
            this.ttcIndex = ttcIndex;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            FontCacheTtcKey that = (FontCacheTtcKey) o;
            if (this.ttcIndex != that.ttcIndex) {
                return false;
            }
            return this.ttcKey.equals(that.ttcKey);
        }

        public int hashCode() {
            int result = this.ttcKey.hashCode();
            return (31 * result) + this.ttcIndex;
        }
    }
}
