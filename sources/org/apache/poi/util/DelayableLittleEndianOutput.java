package org.apache.poi.util;

/* loaded from: poi-3.17.jar:org/apache/poi/util/DelayableLittleEndianOutput.class */
public interface DelayableLittleEndianOutput extends LittleEndianOutput {
    LittleEndianOutput createDelayedOutput(int i);
}
