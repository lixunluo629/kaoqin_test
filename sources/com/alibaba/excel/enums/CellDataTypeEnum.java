package com.alibaba.excel.enums;

import com.alibaba.excel.constant.ExcelXmlConstants;
import com.alibaba.excel.util.StringUtils;
import java.util.HashMap;
import java.util.Map;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/enums/CellDataTypeEnum.class */
public enum CellDataTypeEnum {
    STRING,
    DIRECT_STRING,
    NUMBER,
    BOOLEAN,
    EMPTY,
    ERROR,
    IMAGE;

    private static final Map<String, CellDataTypeEnum> TYPE_ROUTING_MAP = new HashMap(16);

    static {
        TYPE_ROUTING_MAP.put(ExcelXmlConstants.CELL_DATA_FORMAT_TAG, STRING);
        TYPE_ROUTING_MAP.put("str", DIRECT_STRING);
        TYPE_ROUTING_MAP.put("inlineStr", STRING);
        TYPE_ROUTING_MAP.put("e", ERROR);
        TYPE_ROUTING_MAP.put("b", BOOLEAN);
        TYPE_ROUTING_MAP.put("n", NUMBER);
    }

    public static CellDataTypeEnum buildFromCellType(String cellType) {
        if (StringUtils.isEmpty(cellType)) {
            return EMPTY;
        }
        return TYPE_ROUTING_MAP.get(cellType);
    }
}
