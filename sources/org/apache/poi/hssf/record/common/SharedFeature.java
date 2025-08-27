package org.apache.poi.hssf.record.common;

import org.apache.poi.util.LittleEndianOutput;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/common/SharedFeature.class */
public interface SharedFeature {
    String toString();

    void serialize(LittleEndianOutput littleEndianOutput);

    int getDataSize();
}
