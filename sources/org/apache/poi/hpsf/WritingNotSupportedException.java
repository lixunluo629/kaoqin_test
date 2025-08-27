package org.apache.poi.hpsf;

/* loaded from: poi-3.17.jar:org/apache/poi/hpsf/WritingNotSupportedException.class */
public class WritingNotSupportedException extends UnsupportedVariantTypeException {
    public WritingNotSupportedException(long variantType, Object value) {
        super(variantType, value);
    }
}
