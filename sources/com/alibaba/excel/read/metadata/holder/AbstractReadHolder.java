package com.alibaba.excel.read.metadata.holder;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ConverterKeyBuild;
import com.alibaba.excel.converters.DefaultConverterLoader;
import com.alibaba.excel.enums.HeadKindEnum;
import com.alibaba.excel.enums.HolderEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.exception.ExcelAnalysisStopException;
import com.alibaba.excel.metadata.AbstractHolder;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.read.listener.ModelBuildEventListener;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.listener.ReadListenerRegistryCenter;
import com.alibaba.excel.read.listener.event.AnalysisFinishEvent;
import com.alibaba.excel.read.metadata.ReadBasicParameter;
import com.alibaba.excel.read.metadata.ReadWorkbook;
import com.alibaba.excel.read.metadata.property.ExcelReadHeadProperty;
import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.excel.util.ConverterUtils;
import com.alibaba.excel.util.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/read/metadata/holder/AbstractReadHolder.class */
public abstract class AbstractReadHolder extends AbstractHolder implements ReadHolder, ReadListenerRegistryCenter {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) AbstractReadHolder.class);
    private Integer headRowNumber;
    private ExcelReadHeadProperty excelReadHeadProperty;
    private List<ReadListener> readListenerList;

    public AbstractReadHolder(ReadBasicParameter readBasicParameter, AbstractReadHolder parentAbstractReadHolder, Boolean convertAllFiled) {
        Boolean useDefaultListener;
        super(readBasicParameter, parentAbstractReadHolder);
        if (readBasicParameter.getUse1904windowing() == null && parentAbstractReadHolder != null) {
            getGlobalConfiguration().setUse1904windowing(parentAbstractReadHolder.getGlobalConfiguration().getUse1904windowing());
        } else {
            getGlobalConfiguration().setUse1904windowing(readBasicParameter.getUse1904windowing());
        }
        this.excelReadHeadProperty = new ExcelReadHeadProperty(this, getClazz(), getHead(), convertAllFiled);
        if (readBasicParameter.getHeadRowNumber() == null) {
            if (parentAbstractReadHolder == null) {
                if (this.excelReadHeadProperty.hasHead()) {
                    this.headRowNumber = Integer.valueOf(this.excelReadHeadProperty.getHeadRowNumber());
                } else {
                    this.headRowNumber = 1;
                }
            } else {
                this.headRowNumber = parentAbstractReadHolder.getHeadRowNumber();
            }
        } else {
            this.headRowNumber = readBasicParameter.getHeadRowNumber();
        }
        if (parentAbstractReadHolder == null) {
            this.readListenerList = new ArrayList();
        } else {
            this.readListenerList = new ArrayList(parentAbstractReadHolder.getReadListenerList());
        }
        if (HolderEnum.WORKBOOK.equals(holderType()) && ((useDefaultListener = ((ReadWorkbook) readBasicParameter).getUseDefaultListener()) == null || useDefaultListener.booleanValue())) {
            this.readListenerList.add(new ModelBuildEventListener());
        }
        if (readBasicParameter.getCustomReadListenerList() != null && !readBasicParameter.getCustomReadListenerList().isEmpty()) {
            this.readListenerList.addAll(readBasicParameter.getCustomReadListenerList());
        }
        if (parentAbstractReadHolder == null) {
            setConverterMap(DefaultConverterLoader.loadDefaultReadConverter());
        } else {
            setConverterMap(new HashMap(parentAbstractReadHolder.getConverterMap()));
        }
        if (readBasicParameter.getCustomConverterList() != null && !readBasicParameter.getCustomConverterList().isEmpty()) {
            for (Converter converter : readBasicParameter.getCustomConverterList()) {
                getConverterMap().put(ConverterKeyBuild.buildKey(converter.supportJavaTypeKey(), converter.supportExcelTypeKey()), converter);
            }
        }
    }

    @Override // com.alibaba.excel.read.listener.ReadListenerRegistryCenter
    public void register(AnalysisEventListener listener) {
        this.readListenerList.add(listener);
    }

    @Override // com.alibaba.excel.read.listener.ReadListenerRegistryCenter
    public void notifyEndOneRow(AnalysisFinishEvent event, AnalysisContext analysisContext) {
        Map<Integer, CellData> cellDataMap = event.getAnalysisResult();
        if (CollectionUtils.isEmpty(cellDataMap)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.warn("Empty row!");
            }
            if (analysisContext.readWorkbookHolder().getIgnoreEmptyRow().booleanValue()) {
                return;
            }
        }
        ReadRowHolder readRowHolder = analysisContext.readRowHolder();
        readRowHolder.setCurrentRowAnalysisResult(cellDataMap);
        int rowIndex = readRowHolder.getRowIndex().intValue();
        int currentheadRowNumber = analysisContext.readSheetHolder().getHeadRowNumber().intValue();
        if (rowIndex >= currentheadRowNumber) {
            for (ReadListener readListener : analysisContext.currentReadHolder().readListenerList()) {
                try {
                    readListener.invoke(readRowHolder.getCurrentRowAnalysisResult(), analysisContext);
                    if (!readListener.hasNext(analysisContext)) {
                        throw new ExcelAnalysisStopException();
                    }
                } catch (Exception e) {
                    for (ReadListener readListenerException : analysisContext.currentReadHolder().readListenerList()) {
                        try {
                            readListenerException.onException(e, analysisContext);
                        } catch (Exception exception) {
                            throw new ExcelAnalysisException(exception.getMessage(), exception);
                        }
                    }
                    return;
                }
            }
            return;
        }
        if (currentheadRowNumber == rowIndex + 1) {
            buildHead(analysisContext, cellDataMap);
        }
        for (ReadListener readListener2 : analysisContext.currentReadHolder().readListenerList()) {
            try {
                readListener2.invokeHead(cellDataMap, analysisContext);
                if (!readListener2.hasNext(analysisContext)) {
                    throw new ExcelAnalysisStopException();
                }
            } catch (Exception e2) {
                for (ReadListener readListenerException2 : analysisContext.currentReadHolder().readListenerList()) {
                    try {
                        readListenerException2.onException(e2, analysisContext);
                    } catch (Exception exception2) {
                        throw new ExcelAnalysisException(exception2.getMessage(), exception2);
                    }
                }
                return;
            }
        }
    }

    @Override // com.alibaba.excel.read.listener.ReadListenerRegistryCenter
    public void notifyAfterAllAnalysed(AnalysisContext analysisContext) {
        for (ReadListener readListener : this.readListenerList) {
            readListener.doAfterAllAnalysed(analysisContext);
        }
    }

    private void buildHead(AnalysisContext analysisContext, Map<Integer, CellData> cellDataMap) {
        if (!HeadKindEnum.CLASS.equals(analysisContext.currentReadHolder().excelReadHeadProperty().getHeadKind())) {
            return;
        }
        Map<Integer, String> dataMap = ConverterUtils.convertToStringMap(cellDataMap, analysisContext);
        ExcelReadHeadProperty excelHeadPropertyData = analysisContext.readSheetHolder().excelReadHeadProperty();
        Map<Integer, Head> headMapData = excelHeadPropertyData.getHeadMap();
        Map<Integer, ExcelContentProperty> contentPropertyMapData = excelHeadPropertyData.getContentPropertyMap();
        Map<Integer, Head> tmpHeadMap = new HashMap<>(((headMapData.size() * 4) / 3) + 1);
        Map<Integer, ExcelContentProperty> tmpContentPropertyMap = new HashMap<>(((contentPropertyMapData.size() * 4) / 3) + 1);
        for (Map.Entry<Integer, Head> entry : headMapData.entrySet()) {
            Head headData = entry.getValue();
            if (headData.getForceIndex().booleanValue() || !headData.getForceName().booleanValue()) {
                tmpHeadMap.put(entry.getKey(), headData);
                tmpContentPropertyMap.put(entry.getKey(), contentPropertyMapData.get(entry.getKey()));
            } else {
                List<String> headNameList = headData.getHeadNameList();
                String headName = headNameList.get(headNameList.size() - 1);
                Iterator<Map.Entry<Integer, String>> it = dataMap.entrySet().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Map.Entry<Integer, String> stringEntry = it.next();
                    if (stringEntry != null) {
                        String headString = stringEntry.getValue();
                        Integer stringKey = stringEntry.getKey();
                        if (!StringUtils.isEmpty(headString)) {
                            if (analysisContext.currentReadHolder().globalConfiguration().getAutoTrim().booleanValue()) {
                                headString = headString.trim();
                            }
                            if (headName.equals(headString)) {
                                headData.setColumnIndex(stringKey);
                                tmpHeadMap.put(stringKey, headData);
                                tmpContentPropertyMap.put(stringKey, contentPropertyMapData.get(entry.getKey()));
                                break;
                            }
                        } else {
                            continue;
                        }
                    }
                }
            }
        }
        excelHeadPropertyData.setHeadMap(tmpHeadMap);
        excelHeadPropertyData.setContentPropertyMap(tmpContentPropertyMap);
    }

    public List<ReadListener> getReadListenerList() {
        return this.readListenerList;
    }

    public void setReadListenerList(List<ReadListener> readListenerList) {
        this.readListenerList = readListenerList;
    }

    public ExcelReadHeadProperty getExcelReadHeadProperty() {
        return this.excelReadHeadProperty;
    }

    public void setExcelReadHeadProperty(ExcelReadHeadProperty excelReadHeadProperty) {
        this.excelReadHeadProperty = excelReadHeadProperty;
    }

    public Integer getHeadRowNumber() {
        return this.headRowNumber;
    }

    public void setHeadRowNumber(Integer headRowNumber) {
        this.headRowNumber = headRowNumber;
    }

    @Override // com.alibaba.excel.read.metadata.holder.ReadHolder
    public List<ReadListener> readListenerList() {
        return getReadListenerList();
    }

    @Override // com.alibaba.excel.read.metadata.holder.ReadHolder
    public ExcelReadHeadProperty excelReadHeadProperty() {
        return getExcelReadHeadProperty();
    }
}
