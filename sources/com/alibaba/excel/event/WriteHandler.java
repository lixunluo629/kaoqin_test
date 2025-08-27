package com.alibaba.excel.event;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

@Deprecated
/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/event/WriteHandler.class */
public interface WriteHandler {
    void sheet(int i, Sheet sheet);

    void row(int i, Row row);

    void cell(int i, Cell cell);
}
