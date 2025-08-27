package org.apache.poi.hssf.eventmodel;

import org.apache.poi.hssf.record.Record;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/eventmodel/ERFListener.class */
public interface ERFListener {
    boolean processRecord(Record record);
}
