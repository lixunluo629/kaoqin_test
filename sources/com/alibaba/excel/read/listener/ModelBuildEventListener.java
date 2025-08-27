package com.alibaba.excel.read.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.enums.HeadKindEnum;
import com.alibaba.excel.event.AbstractIgnoreExceptionReadListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.read.metadata.holder.ReadHolder;
import com.alibaba.excel.read.metadata.property.ExcelReadHeadProperty;
import com.alibaba.excel.util.ConverterUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.sf.cglib.beans.BeanMap;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/read/listener/ModelBuildEventListener.class */
public class ModelBuildEventListener extends AbstractIgnoreExceptionReadListener<Map<Integer, CellData>> {
    @Override // com.alibaba.excel.read.listener.ReadListener
    public void invokeHead(Map<Integer, CellData> cellDataMap, AnalysisContext context) {
    }

    @Override // com.alibaba.excel.read.listener.ReadListener
    public void invoke(Map<Integer, CellData> cellDataMap, AnalysisContext context) {
        ReadHolder currentReadHolder = context.currentReadHolder();
        if (HeadKindEnum.CLASS.equals(currentReadHolder.excelReadHeadProperty().getHeadKind())) {
            context.readRowHolder().setCurrentRowAnalysisResult(buildUserModel(cellDataMap, currentReadHolder, context));
        } else {
            context.readRowHolder().setCurrentRowAnalysisResult(buildStringList(cellDataMap, currentReadHolder, context));
        }
    }

    private Object buildStringList(Map<Integer, CellData> cellDataMap, ReadHolder currentReadHolder, AnalysisContext context) {
        int index = 0;
        if (context.readWorkbookHolder().getDefaultReturnMap().booleanValue()) {
            Map<Integer, String> map = new LinkedHashMap<>(((cellDataMap.size() * 4) / 3) + 1);
            for (Map.Entry<Integer, CellData> entry : cellDataMap.entrySet()) {
                Integer key = entry.getKey();
                CellData cellData = entry.getValue();
                while (index < key.intValue()) {
                    map.put(Integer.valueOf(index), null);
                    index++;
                }
                index++;
                if (cellData.getType() == CellDataTypeEnum.EMPTY) {
                    map.put(key, null);
                } else {
                    map.put(key, (String) ConverterUtils.convertToJavaObject(cellData, null, null, currentReadHolder.converterMap(), currentReadHolder.globalConfiguration(), context.readRowHolder().getRowIndex(), key));
                }
            }
            return map;
        }
        List<String> list = new ArrayList<>();
        for (Map.Entry<Integer, CellData> entry2 : cellDataMap.entrySet()) {
            Integer key2 = entry2.getKey();
            CellData cellData2 = entry2.getValue();
            while (index < key2.intValue()) {
                list.add(null);
                index++;
            }
            index++;
            if (cellData2.getType() == CellDataTypeEnum.EMPTY) {
                list.add(null);
            } else {
                list.add((String) ConverterUtils.convertToJavaObject(cellData2, null, null, currentReadHolder.converterMap(), currentReadHolder.globalConfiguration(), context.readRowHolder().getRowIndex(), key2));
            }
        }
        return list;
    }

    private Object buildUserModel(Map<Integer, CellData> cellDataMap, ReadHolder currentReadHolder, AnalysisContext context) throws IllegalAccessException, InstantiationException {
        ExcelReadHeadProperty excelReadHeadProperty = currentReadHolder.excelReadHeadProperty();
        try {
            Object resultModel = excelReadHeadProperty.getHeadClazz().newInstance();
            Map<Integer, Head> headMap = excelReadHeadProperty.getHeadMap();
            Map<String, Object> map = new HashMap<>(((headMap.size() * 4) / 3) + 1);
            Map<Integer, ExcelContentProperty> contentPropertyMap = excelReadHeadProperty.getContentPropertyMap();
            for (Map.Entry<Integer, Head> entry : headMap.entrySet()) {
                Integer index = entry.getKey();
                if (cellDataMap.containsKey(index)) {
                    CellData cellData = cellDataMap.get(index);
                    if (cellData.getType() != CellDataTypeEnum.EMPTY) {
                        ExcelContentProperty excelContentProperty = contentPropertyMap.get(index);
                        Object value = ConverterUtils.convertToJavaObject(cellData, excelContentProperty.getField(), excelContentProperty, currentReadHolder.converterMap(), currentReadHolder.globalConfiguration(), context.readRowHolder().getRowIndex(), index);
                        if (value != null) {
                            map.put(excelContentProperty.getField().getName(), value);
                        }
                    }
                }
            }
            BeanMap.create(resultModel).putAll(map);
            return resultModel;
        } catch (Exception e) {
            throw new ExcelDataConvertException(context.readRowHolder().getRowIndex(), 0, new CellData(CellDataTypeEnum.EMPTY), null, "Can not instance class: " + excelReadHeadProperty.getHeadClazz().getName(), e);
        }
    }

    @Override // com.alibaba.excel.read.listener.ReadListener
    public void doAfterAllAnalysed(AnalysisContext context) {
    }
}
