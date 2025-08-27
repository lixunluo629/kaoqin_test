package org.apache.poi.ddf;

/* loaded from: poi-3.17.jar:org/apache/poi/ddf/EscherSerializationListener.class */
public interface EscherSerializationListener {
    void beforeRecordSerialize(int i, short s, EscherRecord escherRecord);

    void afterRecordSerialize(int i, short s, int i2, EscherRecord escherRecord);
}
