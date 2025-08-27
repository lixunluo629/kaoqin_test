package com.alibaba.excel.write.metadata.holder;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ConverterKeyBuild;
import com.alibaba.excel.converters.DefaultConverterLoader;
import com.alibaba.excel.enums.HeadKindEnum;
import com.alibaba.excel.event.NotRepeatExecutor;
import com.alibaba.excel.event.Order;
import com.alibaba.excel.metadata.AbstractHolder;
import com.alibaba.excel.metadata.Font;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.TableStyle;
import com.alibaba.excel.metadata.property.RowHeightProperty;
import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.DefaultWriteHandlerLoader;
import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.handler.WorkbookWriteHandler;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteBasicParameter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.property.ExcelWriteHeadProperty;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.AbstractHeadColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.row.SimpleRowHeightStyleStrategy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/metadata/holder/AbstractWriteHolder.class */
public abstract class AbstractWriteHolder extends AbstractHolder implements WriteHolder {
    private Boolean needHead;
    private Integer relativeHeadRowIndex;
    private ExcelWriteHeadProperty excelWriteHeadProperty;
    private Map<Class<? extends WriteHandler>, List<WriteHandler>> writeHandlerMap;
    private Boolean useDefaultStyle;
    private Boolean automaticMergeHead;
    private Collection<Integer> excludeColumnIndexes;
    private Collection<String> excludeColumnFiledNames;
    private Collection<Integer> includeColumnIndexes;
    private Collection<String> includeColumnFiledNames;

    public AbstractWriteHolder(WriteBasicParameter writeBasicParameter, AbstractWriteHolder parentAbstractWriteHolder, Boolean convertAllFiled) {
        super(writeBasicParameter, parentAbstractWriteHolder);
        if (writeBasicParameter.getUse1904windowing() == null) {
            if (parentAbstractWriteHolder == null) {
                getGlobalConfiguration().setUse1904windowing(Boolean.FALSE);
            } else {
                getGlobalConfiguration().setUse1904windowing(parentAbstractWriteHolder.getGlobalConfiguration().getUse1904windowing());
            }
        } else {
            getGlobalConfiguration().setUse1904windowing(writeBasicParameter.getUse1904windowing());
        }
        if (writeBasicParameter.getNeedHead() == null) {
            if (parentAbstractWriteHolder == null) {
                this.needHead = Boolean.TRUE;
            } else {
                this.needHead = parentAbstractWriteHolder.getNeedHead();
            }
        } else {
            this.needHead = writeBasicParameter.getNeedHead();
        }
        if (writeBasicParameter.getRelativeHeadRowIndex() == null) {
            if (parentAbstractWriteHolder == null) {
                this.relativeHeadRowIndex = 0;
            } else {
                this.relativeHeadRowIndex = parentAbstractWriteHolder.getRelativeHeadRowIndex();
            }
        } else {
            this.relativeHeadRowIndex = writeBasicParameter.getRelativeHeadRowIndex();
        }
        if (writeBasicParameter.getUseDefaultStyle() == null) {
            if (parentAbstractWriteHolder == null) {
                this.useDefaultStyle = Boolean.TRUE;
            } else {
                this.useDefaultStyle = parentAbstractWriteHolder.getUseDefaultStyle();
            }
        } else {
            this.useDefaultStyle = writeBasicParameter.getUseDefaultStyle();
        }
        if (writeBasicParameter.getAutomaticMergeHead() == null) {
            if (parentAbstractWriteHolder == null) {
                this.automaticMergeHead = Boolean.TRUE;
            } else {
                this.automaticMergeHead = parentAbstractWriteHolder.getAutomaticMergeHead();
            }
        } else {
            this.automaticMergeHead = writeBasicParameter.getAutomaticMergeHead();
        }
        if (writeBasicParameter.getExcludeColumnFiledNames() == null && parentAbstractWriteHolder != null) {
            this.excludeColumnFiledNames = parentAbstractWriteHolder.getExcludeColumnFiledNames();
        } else {
            this.excludeColumnFiledNames = writeBasicParameter.getExcludeColumnFiledNames();
        }
        if (writeBasicParameter.getExcludeColumnIndexes() == null && parentAbstractWriteHolder != null) {
            this.excludeColumnIndexes = parentAbstractWriteHolder.getExcludeColumnIndexes();
        } else {
            this.excludeColumnIndexes = writeBasicParameter.getExcludeColumnIndexes();
        }
        if (writeBasicParameter.getIncludeColumnFiledNames() == null && parentAbstractWriteHolder != null) {
            this.includeColumnFiledNames = parentAbstractWriteHolder.getIncludeColumnFiledNames();
        } else {
            this.includeColumnFiledNames = writeBasicParameter.getIncludeColumnFiledNames();
        }
        if (writeBasicParameter.getIncludeColumnIndexes() == null && parentAbstractWriteHolder != null) {
            this.includeColumnIndexes = parentAbstractWriteHolder.getIncludeColumnIndexes();
        } else {
            this.includeColumnIndexes = writeBasicParameter.getIncludeColumnIndexes();
        }
        this.excelWriteHeadProperty = new ExcelWriteHeadProperty(this, getClazz(), getHead(), convertAllFiled);
        compatibleOldCode(writeBasicParameter);
        List<WriteHandler> handlerList = new ArrayList<>();
        initAnnotationConfig(handlerList);
        if (writeBasicParameter.getCustomWriteHandlerList() != null && !writeBasicParameter.getCustomWriteHandlerList().isEmpty()) {
            handlerList.addAll(writeBasicParameter.getCustomWriteHandlerList());
        }
        Map<Class<? extends WriteHandler>, List<WriteHandler>> parentWriteHandlerMap = null;
        if (parentAbstractWriteHolder != null) {
            parentWriteHandlerMap = parentAbstractWriteHolder.getWriteHandlerMap();
        } else {
            handlerList.addAll(DefaultWriteHandlerLoader.loadDefaultHandler(this.useDefaultStyle));
        }
        this.writeHandlerMap = sortAndClearUpHandler(handlerList, parentWriteHandlerMap);
        if (parentAbstractWriteHolder == null) {
            setConverterMap(DefaultConverterLoader.loadDefaultWriteConverter());
        } else {
            setConverterMap(new HashMap(parentAbstractWriteHolder.getConverterMap()));
        }
        if (writeBasicParameter.getCustomConverterList() != null && !writeBasicParameter.getCustomConverterList().isEmpty()) {
            for (Converter converter : writeBasicParameter.getCustomConverterList()) {
                getConverterMap().put(ConverterKeyBuild.buildKey(converter.supportJavaTypeKey()), converter);
            }
        }
    }

    @Deprecated
    private void compatibleOldCode(WriteBasicParameter writeBasicParameter) {
        switch (holderType()) {
            case SHEET:
                compatibleOldCodeCreateRowCellStyleStrategy(writeBasicParameter, ((WriteSheet) writeBasicParameter).getTableStyle());
                compatibleOldCodeCreateHeadColumnWidthStyleStrategy(writeBasicParameter, ((WriteSheet) writeBasicParameter).getColumnWidthMap());
                break;
            case TABLE:
                compatibleOldCodeCreateRowCellStyleStrategy(writeBasicParameter, ((WriteTable) writeBasicParameter).getTableStyle());
                break;
        }
    }

    @Deprecated
    private void compatibleOldCodeCreateRowCellStyleStrategy(WriteBasicParameter writeBasicParameter, TableStyle tableStyle) {
        if (tableStyle == null) {
            return;
        }
        if (writeBasicParameter.getCustomWriteHandlerList() == null) {
            writeBasicParameter.setCustomWriteHandlerList(new ArrayList());
        }
        writeBasicParameter.getCustomWriteHandlerList().add(new HorizontalCellStyleStrategy(buildWriteCellStyle(tableStyle.getTableHeadFont(), tableStyle.getTableHeadBackGroundColor()), buildWriteCellStyle(tableStyle.getTableContentFont(), tableStyle.getTableContentBackGroundColor())));
    }

    @Deprecated
    private WriteCellStyle buildWriteCellStyle(Font font, IndexedColors indexedColors) {
        WriteCellStyle writeCellStyle = new WriteCellStyle();
        if (indexedColors != null) {
            writeCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
            writeCellStyle.setFillForegroundColor(Short.valueOf(indexedColors.getIndex()));
        }
        if (font != null) {
            WriteFont writeFont = new WriteFont();
            writeFont.setFontName(font.getFontName());
            writeFont.setFontHeightInPoints(Short.valueOf(font.getFontHeightInPoints()));
            writeFont.setBold(Boolean.valueOf(font.isBold()));
            writeCellStyle.setWriteFont(writeFont);
        }
        return writeCellStyle;
    }

    @Deprecated
    private void compatibleOldCodeCreateHeadColumnWidthStyleStrategy(WriteBasicParameter writeBasicParameter, final Map<Integer, Integer> columnWidthMap) {
        if (columnWidthMap == null || columnWidthMap.isEmpty()) {
            return;
        }
        if (writeBasicParameter.getCustomWriteHandlerList() == null) {
            writeBasicParameter.setCustomWriteHandlerList(new ArrayList());
        }
        writeBasicParameter.getCustomWriteHandlerList().add(new AbstractHeadColumnWidthStyleStrategy() { // from class: com.alibaba.excel.write.metadata.holder.AbstractWriteHolder.1
            @Override // com.alibaba.excel.write.style.column.AbstractHeadColumnWidthStyleStrategy
            protected Integer columnWidth(Head head, Integer columnIndex) {
                if (columnWidthMap.containsKey(head.getColumnIndex())) {
                    return Integer.valueOf(((Integer) columnWidthMap.get(head.getColumnIndex())).intValue() / 256);
                }
                return 20;
            }
        });
    }

    protected void initAnnotationConfig(List<WriteHandler> handlerList) {
        if (!HeadKindEnum.CLASS.equals(getExcelWriteHeadProperty().getHeadKind())) {
            return;
        }
        Map<Integer, Head> headMap = getExcelWriteHeadProperty().getHeadMap();
        boolean hasColumnWidth = false;
        Iterator<Map.Entry<Integer, Head>> it = headMap.entrySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Map.Entry<Integer, Head> entry = it.next();
            if (entry.getValue().getColumnWidthProperty() != null) {
                hasColumnWidth = true;
                break;
            }
        }
        if (hasColumnWidth) {
            dealColumnWidth(handlerList);
        }
        dealRowHigh(handlerList);
    }

    private void dealRowHigh(List<WriteHandler> handlerList) {
        RowHeightProperty headRowHeightProperty = getExcelWriteHeadProperty().getHeadRowHeightProperty();
        RowHeightProperty contentRowHeightProperty = getExcelWriteHeadProperty().getContentRowHeightProperty();
        if (headRowHeightProperty == null && contentRowHeightProperty == null) {
            return;
        }
        Short headRowHeight = null;
        if (headRowHeightProperty != null) {
            headRowHeight = headRowHeightProperty.getHeight();
        }
        Short contentRowHeight = null;
        if (contentRowHeightProperty != null) {
            contentRowHeight = contentRowHeightProperty.getHeight();
        }
        handlerList.add(new SimpleRowHeightStyleStrategy(headRowHeight, contentRowHeight));
    }

    private void dealColumnWidth(List<WriteHandler> handlerList) {
        WriteHandler columnWidthStyleStrategy = new AbstractHeadColumnWidthStyleStrategy() { // from class: com.alibaba.excel.write.metadata.holder.AbstractWriteHolder.2
            @Override // com.alibaba.excel.write.style.column.AbstractHeadColumnWidthStyleStrategy
            protected Integer columnWidth(Head head, Integer columnIndex) {
                if (head != null && head.getColumnWidthProperty() != null) {
                    return head.getColumnWidthProperty().getWidth();
                }
                return null;
            }
        };
        handlerList.add(columnWidthStyleStrategy);
    }

    protected Map<Class<? extends WriteHandler>, List<WriteHandler>> sortAndClearUpHandler(List<WriteHandler> handlerList, Map<Class<? extends WriteHandler>, List<WriteHandler>> parentHandlerMap) {
        if (parentHandlerMap != null) {
            List<WriteHandler> parentWriteHandler = parentHandlerMap.get(WriteHandler.class);
            if (!CollectionUtils.isEmpty(parentWriteHandler)) {
                handlerList.addAll(parentWriteHandler);
            }
        }
        Map<Integer, List<WriteHandler>> orderExcelWriteHandlerMap = new TreeMap<>();
        for (WriteHandler handler : handlerList) {
            int order = Integer.MIN_VALUE;
            if (handler instanceof Order) {
                order = ((Order) handler).order();
            }
            if (orderExcelWriteHandlerMap.containsKey(Integer.valueOf(order))) {
                orderExcelWriteHandlerMap.get(Integer.valueOf(order)).add(handler);
            } else {
                List<WriteHandler> tempHandlerList = new ArrayList<>();
                tempHandlerList.add(handler);
                orderExcelWriteHandlerMap.put(Integer.valueOf(order), tempHandlerList);
            }
        }
        Set<String> alreadyExistedHandlerSet = new HashSet<>();
        List<WriteHandler> cleanUpHandlerList = new ArrayList<>();
        for (Map.Entry<Integer, List<WriteHandler>> entry : orderExcelWriteHandlerMap.entrySet()) {
            for (WriteHandler handler2 : entry.getValue()) {
                if (handler2 instanceof NotRepeatExecutor) {
                    String uniqueValue = ((NotRepeatExecutor) handler2).uniqueValue();
                    if (!alreadyExistedHandlerSet.contains(uniqueValue)) {
                        alreadyExistedHandlerSet.add(uniqueValue);
                    }
                }
                cleanUpHandlerList.add(handler2);
            }
        }
        Map<Class<? extends WriteHandler>, List<WriteHandler>> result = new HashMap<>(16);
        result.put(WriteHandler.class, new ArrayList<>());
        result.put(WorkbookWriteHandler.class, new ArrayList<>());
        result.put(SheetWriteHandler.class, new ArrayList<>());
        result.put(RowWriteHandler.class, new ArrayList<>());
        result.put(CellWriteHandler.class, new ArrayList<>());
        for (WriteHandler writeHandler : cleanUpHandlerList) {
            if (writeHandler instanceof CellWriteHandler) {
                result.get(CellWriteHandler.class).add(writeHandler);
            }
            if (writeHandler instanceof RowWriteHandler) {
                result.get(RowWriteHandler.class).add(writeHandler);
            }
            if (writeHandler instanceof SheetWriteHandler) {
                result.get(SheetWriteHandler.class).add(writeHandler);
            }
            if (writeHandler instanceof WorkbookWriteHandler) {
                result.get(WorkbookWriteHandler.class).add(writeHandler);
            }
            result.get(WriteHandler.class).add(writeHandler);
        }
        return result;
    }

    @Override // com.alibaba.excel.write.metadata.holder.WriteHolder
    public boolean ignore(String fieldName, Integer columnIndex) {
        if (fieldName != null) {
            if (this.includeColumnFiledNames != null && !this.includeColumnFiledNames.contains(fieldName)) {
                return true;
            }
            if (this.excludeColumnFiledNames != null && this.excludeColumnFiledNames.contains(fieldName)) {
                return true;
            }
        }
        if (columnIndex != null) {
            if (this.includeColumnIndexes != null && !this.includeColumnIndexes.contains(columnIndex)) {
                return true;
            }
            if (this.excludeColumnIndexes != null && this.excludeColumnIndexes.contains(columnIndex)) {
                return true;
            }
            return false;
        }
        return false;
    }

    public Boolean getNeedHead() {
        return this.needHead;
    }

    public void setNeedHead(Boolean needHead) {
        this.needHead = needHead;
    }

    public Map<Class<? extends WriteHandler>, List<WriteHandler>> getWriteHandlerMap() {
        return this.writeHandlerMap;
    }

    public void setWriteHandlerMap(Map<Class<? extends WriteHandler>, List<WriteHandler>> writeHandlerMap) {
        this.writeHandlerMap = writeHandlerMap;
    }

    public ExcelWriteHeadProperty getExcelWriteHeadProperty() {
        return this.excelWriteHeadProperty;
    }

    public void setExcelWriteHeadProperty(ExcelWriteHeadProperty excelWriteHeadProperty) {
        this.excelWriteHeadProperty = excelWriteHeadProperty;
    }

    public Integer getRelativeHeadRowIndex() {
        return this.relativeHeadRowIndex;
    }

    public void setRelativeHeadRowIndex(Integer relativeHeadRowIndex) {
        this.relativeHeadRowIndex = relativeHeadRowIndex;
    }

    public Boolean getUseDefaultStyle() {
        return this.useDefaultStyle;
    }

    public void setUseDefaultStyle(Boolean useDefaultStyle) {
        this.useDefaultStyle = useDefaultStyle;
    }

    public Boolean getAutomaticMergeHead() {
        return this.automaticMergeHead;
    }

    public void setAutomaticMergeHead(Boolean automaticMergeHead) {
        this.automaticMergeHead = automaticMergeHead;
    }

    public Collection<Integer> getExcludeColumnIndexes() {
        return this.excludeColumnIndexes;
    }

    public void setExcludeColumnIndexes(Collection<Integer> excludeColumnIndexes) {
        this.excludeColumnIndexes = excludeColumnIndexes;
    }

    public Collection<String> getExcludeColumnFiledNames() {
        return this.excludeColumnFiledNames;
    }

    public void setExcludeColumnFiledNames(Collection<String> excludeColumnFiledNames) {
        this.excludeColumnFiledNames = excludeColumnFiledNames;
    }

    public Collection<Integer> getIncludeColumnIndexes() {
        return this.includeColumnIndexes;
    }

    public void setIncludeColumnIndexes(Collection<Integer> includeColumnIndexes) {
        this.includeColumnIndexes = includeColumnIndexes;
    }

    public Collection<String> getIncludeColumnFiledNames() {
        return this.includeColumnFiledNames;
    }

    public void setIncludeColumnFiledNames(Collection<String> includeColumnFiledNames) {
        this.includeColumnFiledNames = includeColumnFiledNames;
    }

    @Override // com.alibaba.excel.write.metadata.holder.WriteHolder
    public ExcelWriteHeadProperty excelWriteHeadProperty() {
        return getExcelWriteHeadProperty();
    }

    @Override // com.alibaba.excel.write.metadata.holder.WriteHolder
    public Map<Class<? extends WriteHandler>, List<WriteHandler>> writeHandlerMap() {
        return getWriteHandlerMap();
    }

    @Override // com.alibaba.excel.write.metadata.holder.WriteHolder
    public boolean needHead() {
        return getNeedHead().booleanValue();
    }

    @Override // com.alibaba.excel.write.metadata.holder.WriteHolder
    public int relativeHeadRowIndex() {
        return getRelativeHeadRowIndex().intValue();
    }

    @Override // com.alibaba.excel.write.metadata.holder.WriteHolder
    public boolean automaticMergeHead() {
        return getAutomaticMergeHead().booleanValue();
    }
}
