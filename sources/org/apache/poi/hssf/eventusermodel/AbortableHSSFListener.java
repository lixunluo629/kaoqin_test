package org.apache.poi.hssf.eventusermodel;

import org.apache.poi.hssf.record.Record;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/eventusermodel/AbortableHSSFListener.class */
public abstract class AbortableHSSFListener implements HSSFListener {
    public abstract short abortableProcessRecord(Record record) throws HSSFUserException;

    @Override // org.apache.poi.hssf.eventusermodel.HSSFListener
    public void processRecord(Record record) {
    }
}
