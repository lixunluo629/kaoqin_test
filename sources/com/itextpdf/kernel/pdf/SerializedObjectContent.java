package com.itextpdf.kernel.pdf;

import java.util.Arrays;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/SerializedObjectContent.class */
class SerializedObjectContent {
    private final byte[] serializedContent;
    private final int hash;

    SerializedObjectContent(byte[] serializedContent) {
        this.serializedContent = serializedContent;
        this.hash = calculateHash(serializedContent);
    }

    public boolean equals(Object obj) {
        return (obj instanceof SerializedObjectContent) && hashCode() == obj.hashCode() && Arrays.equals(this.serializedContent, ((SerializedObjectContent) obj).serializedContent);
    }

    public int hashCode() {
        return this.hash;
    }

    private static int calculateHash(byte[] b) {
        int hash = 0;
        for (byte b2 : b) {
            hash = (hash * 31) + (b2 & 255);
        }
        return hash;
    }
}
