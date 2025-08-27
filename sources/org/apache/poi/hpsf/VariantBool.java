package org.apache.poi.hpsf;

import org.apache.poi.util.Internal;
import org.apache.poi.util.LittleEndianByteArrayInputStream;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

@Internal
/* loaded from: poi-3.17.jar:org/apache/poi/hpsf/VariantBool.class */
class VariantBool {
    private static final POILogger LOG = POILogFactory.getLogger((Class<?>) VariantBool.class);
    static final int SIZE = 2;
    private boolean _value;

    VariantBool() {
    }

    void read(LittleEndianByteArrayInputStream lei) {
        short value = lei.readShort();
        switch (value) {
            case -1:
                this._value = true;
                break;
            case 0:
                this._value = false;
                break;
            default:
                LOG.log(5, "VARIANT_BOOL value '" + ((int) value) + "' is incorrect");
                this._value = true;
                break;
        }
    }

    boolean getValue() {
        return this._value;
    }

    void setValue(boolean value) {
        this._value = value;
    }
}
