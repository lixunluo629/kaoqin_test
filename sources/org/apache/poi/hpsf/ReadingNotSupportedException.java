package org.apache.poi.hpsf;

/* loaded from: poi-3.17.jar:org/apache/poi/hpsf/ReadingNotSupportedException.class */
public class ReadingNotSupportedException extends UnsupportedVariantTypeException {
    public ReadingNotSupportedException(long variantType, Object value) {
        super(variantType, value);
    }
}
