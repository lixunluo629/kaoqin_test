package org.apache.poi.hssf.record.common;

import org.apache.poi.ss.util.CellRangeAddress;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/common/FutureRecord.class */
public interface FutureRecord {
    short getFutureRecordType();

    FtrHeader getFutureHeader();

    CellRangeAddress getAssociatedRange();
}
