package org.apache.poi.hpsf;

import org.apache.poi.util.Internal;
import org.apache.poi.util.LittleEndianByteArrayInputStream;

@Internal
/* loaded from: poi-3.17.jar:org/apache/poi/hpsf/Date.class */
class Date {
    private static final int SIZE = 8;
    private final byte[] _value = new byte[8];

    Date() {
    }

    void read(LittleEndianByteArrayInputStream lei) {
        lei.readFully(this._value);
    }
}
