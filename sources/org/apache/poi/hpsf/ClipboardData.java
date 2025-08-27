package org.apache.poi.hpsf;

import java.io.IOException;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.Internal;
import org.apache.poi.util.LittleEndianByteArrayInputStream;
import org.apache.poi.util.LittleEndianByteArrayOutputStream;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

@Internal
/* loaded from: poi-3.17.jar:org/apache/poi/hpsf/ClipboardData.class */
class ClipboardData {
    private static final POILogger LOG = POILogFactory.getLogger((Class<?>) ClipboardData.class);
    private int _format = 0;
    private byte[] _value;

    ClipboardData() {
    }

    void read(LittleEndianByteArrayInputStream lei) {
        int offset = lei.getReadIndex();
        int size = lei.readInt();
        if (size < 4) {
            String msg = "ClipboardData at offset " + offset + " size less than 4 bytes (doesn't even have format field!). Setting to format == 0 and hope for the best";
            LOG.log(5, msg);
            this._format = 0;
            this._value = new byte[0];
            return;
        }
        this._format = lei.readInt();
        this._value = new byte[size - 4];
        lei.readFully(this._value);
    }

    byte[] getValue() {
        return this._value;
    }

    byte[] toByteArray() throws IOException {
        byte[] result = new byte[8 + this._value.length];
        LittleEndianByteArrayOutputStream bos = new LittleEndianByteArrayOutputStream(result, 0);
        try {
            bos.writeInt(4 + this._value.length);
            bos.writeInt(this._format);
            bos.write(this._value);
            IOUtils.closeQuietly(bos);
            return result;
        } catch (Throwable th) {
            IOUtils.closeQuietly(bos);
            throw th;
        }
    }

    void setValue(byte[] value) {
        this._value = (byte[]) value.clone();
    }
}
