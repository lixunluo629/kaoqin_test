package org.apache.poi.hpsf;

/* loaded from: poi-3.17.jar:org/apache/poi/hpsf/VariantTypeException.class */
public abstract class VariantTypeException extends HPSFException {
    private Object value;
    private long variantType;

    public VariantTypeException(long variantType, Object value, String msg) {
        super(msg);
        this.variantType = variantType;
        this.value = value;
    }

    public long getVariantType() {
        return this.variantType;
    }

    public Object getValue() {
        return this.value;
    }
}
