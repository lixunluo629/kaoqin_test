package org.apache.poi.hpsf;

import java.io.UnsupportedEncodingException;
import org.apache.poi.util.LittleEndianByteArrayInputStream;
import org.apache.poi.util.Removal;

@Removal(version = "3.18")
@Deprecated
/* loaded from: poi-3.17.jar:org/apache/poi/hpsf/MutableProperty.class */
public class MutableProperty extends Property {
    public MutableProperty() {
    }

    public MutableProperty(Property p) {
        super(p);
    }

    public MutableProperty(long id, long type, Object value) {
        super(id, type, value);
    }

    public MutableProperty(long id, byte[] src, long offset, int length, int codepage) throws UnsupportedEncodingException {
        super(id, src, offset, length, codepage);
    }

    public MutableProperty(long id, LittleEndianByteArrayInputStream leis, int length, int codepage) throws UnsupportedEncodingException {
        super(id, leis, length, codepage);
    }
}
