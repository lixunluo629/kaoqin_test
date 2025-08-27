package com.alibaba.excel.write.property;

import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.converters.ConverterKeyBuild;
import com.alibaba.excel.converters.DefaultConverterLoader;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.enums.HeadKindEnum;
import com.alibaba.excel.metadata.CellRange;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.Holder;
import com.alibaba.excel.metadata.property.ColumnWidthProperty;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.metadata.property.ExcelHeadProperty;
import com.alibaba.excel.metadata.property.RowHeightProperty;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/property/ExcelWriteHeadProperty.class */
public class ExcelWriteHeadProperty extends ExcelHeadProperty {
    private RowHeightProperty headRowHeightProperty;
    private RowHeightProperty contentRowHeightProperty;

    public ExcelWriteHeadProperty(Holder holder, Class headClazz, List<List<String>> head, Boolean convertAllFiled) {
        super(holder, headClazz, head, convertAllFiled);
        if (getHeadKind() != HeadKindEnum.CLASS) {
            return;
        }
        this.headRowHeightProperty = RowHeightProperty.build((HeadRowHeight) headClazz.getAnnotation(HeadRowHeight.class));
        this.contentRowHeightProperty = RowHeightProperty.build((ContentRowHeight) headClazz.getAnnotation(ContentRowHeight.class));
        ColumnWidth parentColumnWidth = (ColumnWidth) headClazz.getAnnotation(ColumnWidth.class);
        for (Map.Entry<Integer, ExcelContentProperty> entry : getContentPropertyMap().entrySet()) {
            Integer index = entry.getKey();
            ExcelContentProperty excelContentPropertyData = entry.getValue();
            Field field = excelContentPropertyData.getField();
            Head headData = getHeadMap().get(index);
            ColumnWidth columnWidth = (ColumnWidth) field.getAnnotation(ColumnWidth.class);
            headData.setColumnWidthProperty(ColumnWidthProperty.build(columnWidth == null ? parentColumnWidth : columnWidth));
            if (excelContentPropertyData.getConverter() == null) {
                NumberFormat numberFormat = (NumberFormat) field.getAnnotation(NumberFormat.class);
                if (numberFormat != null) {
                    excelContentPropertyData.setConverter(DefaultConverterLoader.loadAllConverter().get(ConverterKeyBuild.buildKey(field.getType(), CellDataTypeEnum.STRING)));
                }
            }
        }
    }

    public RowHeightProperty getHeadRowHeightProperty() {
        return this.headRowHeightProperty;
    }

    public void setHeadRowHeightProperty(RowHeightProperty headRowHeightProperty) {
        this.headRowHeightProperty = headRowHeightProperty;
    }

    public RowHeightProperty getContentRowHeightProperty() {
        return this.contentRowHeightProperty;
    }

    public void setContentRowHeightProperty(RowHeightProperty contentRowHeightProperty) {
        this.contentRowHeightProperty = contentRowHeightProperty;
    }

    public List<CellRange> headCellRangeList() {
        List<CellRange> cellRangeList = new ArrayList<>();
        Set<String> alreadyRangeSet = new HashSet<>();
        List<Head> headList = new ArrayList<>(getHeadMap().values());
        for (int i = 0; i < headList.size(); i++) {
            Head head = headList.get(i);
            List<String> headNameList = head.getHeadNameList();
            for (int j = 0; j < headNameList.size(); j++) {
                if (!alreadyRangeSet.contains(i + "-" + j)) {
                    alreadyRangeSet.add(i + "-" + j);
                    String headName = headNameList.get(j);
                    int lastCol = i;
                    int lastRow = j;
                    for (int k = i + 1; k < headList.size() && headList.get(k).getHeadNameList().get(j).equals(headName); k++) {
                        alreadyRangeSet.add(k + "-" + j);
                        lastCol = k;
                    }
                    Set<String> tempAlreadyRangeSet = new HashSet<>();
                    for (int k2 = j + 1; k2 < headNameList.size(); k2++) {
                        for (int l = i; l <= lastCol; l++) {
                            if (!headList.get(l).getHeadNameList().get(k2).equals(headName)) {
                                break;
                            }
                            tempAlreadyRangeSet.add(l + "-" + k2);
                        }
                        lastRow = k2;
                        alreadyRangeSet.addAll(tempAlreadyRangeSet);
                    }
                    if (j != lastRow || i != lastCol) {
                        cellRangeList.add(new CellRange(j, lastRow, i, lastCol));
                    }
                }
            }
        }
        return cellRangeList;
    }
}
