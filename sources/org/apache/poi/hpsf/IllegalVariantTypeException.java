package org.apache.poi.hpsf;

import org.apache.poi.util.HexDump;

/* loaded from: poi-3.17.jar:org/apache/poi/hpsf/IllegalVariantTypeException.class */
public class IllegalVariantTypeException extends VariantTypeException {
    public IllegalVariantTypeException(long variantType, Object value, String msg) {
        super(variantType, value, msg);
    }

    public IllegalVariantTypeException(long variantType, Object value) {
        this(variantType, value, "The variant type " + variantType + " (" + Variant.getVariantName(variantType) + ", " + HexDump.toHex(variantType) + ") is illegal in this context.");
    }
}
