package org.apache.poi.hssf.record;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/BiffHeaderInput.class */
public interface BiffHeaderInput {
    int readRecordSID();

    int readDataSize();

    int available();
}
