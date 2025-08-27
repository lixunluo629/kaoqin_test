package org.apache.poi.hssf.util;

import org.apache.poi.ss.SpreadsheetVersion;

@Deprecated
/* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/AreaReference.class */
public final class AreaReference extends org.apache.poi.ss.util.AreaReference {
    public AreaReference(String reference) {
        super(reference, SpreadsheetVersion.EXCEL97);
    }

    public AreaReference(CellReference topLeft, CellReference botRight) {
        super(topLeft, botRight);
    }
}
