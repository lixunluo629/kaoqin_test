package org.apache.poi.hpsf;

import org.apache.poi.util.Internal;
import org.apache.poi.util.LittleEndianByteArrayInputStream;

@Internal
/* loaded from: poi-3.17.jar:org/apache/poi/hpsf/VersionedStream.class */
class VersionedStream {
    private final GUID _versionGuid = new GUID();
    private final IndirectPropertyName _streamName = new IndirectPropertyName();

    VersionedStream() {
    }

    void read(LittleEndianByteArrayInputStream lei) {
        this._versionGuid.read(lei);
        this._streamName.read(lei);
    }
}
