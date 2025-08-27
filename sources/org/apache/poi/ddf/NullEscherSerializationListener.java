package org.apache.poi.ddf;

/* loaded from: poi-3.17.jar:org/apache/poi/ddf/NullEscherSerializationListener.class */
public class NullEscherSerializationListener implements EscherSerializationListener {
    @Override // org.apache.poi.ddf.EscherSerializationListener
    public void beforeRecordSerialize(int offset, short recordId, EscherRecord record) {
    }

    @Override // org.apache.poi.ddf.EscherSerializationListener
    public void afterRecordSerialize(int offset, short recordId, int size, EscherRecord record) {
    }
}
