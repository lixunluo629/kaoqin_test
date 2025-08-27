package com.drew.imaging.png;

import com.drew.lang.annotations.NotNull;
import com.itextpdf.io.image.PngImageHelper;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.httpclient.auth.NTLM;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/imaging/png/PngChunkType.class */
public class PngChunkType {
    private static final Set<String> _identifiersAllowingMultiples;
    public static final PngChunkType IHDR;
    public static final PngChunkType PLTE;
    public static final PngChunkType IDAT;
    public static final PngChunkType IEND;
    public static final PngChunkType cHRM;
    public static final PngChunkType gAMA;
    public static final PngChunkType iCCP;
    public static final PngChunkType sBIT;
    public static final PngChunkType sRGB;
    public static final PngChunkType bKGD;
    public static final PngChunkType hIST;
    public static final PngChunkType tRNS;
    public static final PngChunkType pHYs;
    public static final PngChunkType sPLT;
    public static final PngChunkType tIME;
    public static final PngChunkType iTXt;
    public static final PngChunkType tEXt;
    public static final PngChunkType zTXt;
    private final byte[] _bytes;
    private final boolean _multipleAllowed;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PngChunkType.class.desiredAssertionStatus();
        _identifiersAllowingMultiples = new HashSet(Arrays.asList(PngImageHelper.IDAT, "sPLT", "iTXt", "tEXt", "zTXt"));
        try {
            IHDR = new PngChunkType(PngImageHelper.IHDR);
            PLTE = new PngChunkType(PngImageHelper.PLTE);
            IDAT = new PngChunkType(PngImageHelper.IDAT, true);
            IEND = new PngChunkType(PngImageHelper.IEND);
            cHRM = new PngChunkType(PngImageHelper.cHRM);
            gAMA = new PngChunkType(PngImageHelper.gAMA);
            iCCP = new PngChunkType(PngImageHelper.iCCP);
            sBIT = new PngChunkType("sBIT");
            sRGB = new PngChunkType(PngImageHelper.sRGB);
            bKGD = new PngChunkType("bKGD");
            hIST = new PngChunkType("hIST");
            tRNS = new PngChunkType(PngImageHelper.tRNS);
            pHYs = new PngChunkType(PngImageHelper.pHYs);
            sPLT = new PngChunkType("sPLT", true);
            tIME = new PngChunkType("tIME");
            iTXt = new PngChunkType("iTXt", true);
            tEXt = new PngChunkType("tEXt", true);
            zTXt = new PngChunkType("zTXt", true);
        } catch (PngProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public PngChunkType(@NotNull String identifier) throws PngProcessingException {
        this(identifier, false);
    }

    public PngChunkType(@NotNull String identifier, boolean multipleAllowed) throws PngProcessingException, UnsupportedEncodingException {
        this._multipleAllowed = multipleAllowed;
        try {
            byte[] bytes = identifier.getBytes(NTLM.DEFAULT_CHARSET);
            validateBytes(bytes);
            this._bytes = bytes;
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Unable to convert string code to bytes.");
        }
    }

    public PngChunkType(@NotNull byte[] bytes) throws PngProcessingException {
        validateBytes(bytes);
        this._bytes = bytes;
        this._multipleAllowed = _identifiersAllowingMultiples.contains(getIdentifier());
    }

    private static void validateBytes(byte[] bytes) throws PngProcessingException {
        if (bytes.length != 4) {
            throw new PngProcessingException("PNG chunk type identifier must be four bytes in length");
        }
        for (byte b : bytes) {
            if (!isValidByte(b)) {
                throw new PngProcessingException("PNG chunk type identifier may only contain alphabet characters");
            }
        }
    }

    public boolean isCritical() {
        return isUpperCase(this._bytes[0]);
    }

    public boolean isAncillary() {
        return !isCritical();
    }

    public boolean isPrivate() {
        return isUpperCase(this._bytes[1]);
    }

    public boolean isSafeToCopy() {
        return isLowerCase(this._bytes[3]);
    }

    public boolean areMultipleAllowed() {
        return this._multipleAllowed;
    }

    private static boolean isLowerCase(byte b) {
        return (b & 32) != 0;
    }

    private static boolean isUpperCase(byte b) {
        return (b & 32) == 0;
    }

    private static boolean isValidByte(byte b) {
        return (b >= 65 && b <= 90) || (b >= 97 && b <= 122);
    }

    public String getIdentifier() {
        try {
            return new String(this._bytes, NTLM.DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            if ($assertionsDisabled) {
                return "Invalid object instance";
            }
            throw new AssertionError();
        }
    }

    public String toString() {
        return getIdentifier();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PngChunkType that = (PngChunkType) o;
        return Arrays.equals(this._bytes, that._bytes);
    }

    public int hashCode() {
        return Arrays.hashCode(this._bytes);
    }
}
