package org.apache.poi.ss.util.cellwalk;

import org.apache.poi.ss.usermodel.Cell;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/util/cellwalk/CellHandler.class */
public interface CellHandler {
    void onCell(Cell cell, CellWalkContext cellWalkContext);
}
