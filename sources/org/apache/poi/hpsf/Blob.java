package org.apache.poi.hpsf;

import org.apache.poi.util.Internal;
import org.apache.poi.util.LittleEndianInput;

@Internal
/* loaded from: poi-3.17.jar:org/apache/poi/hpsf/Blob.class */
class Blob {
    private byte[] _value;

    Blob() {
    }

    void read(LittleEndianInput lei) {
        int size = lei.readInt();
        this._value = new byte[size];
        if (size > 0) {
            lei.readFully(this._value);
        }
    }
}
