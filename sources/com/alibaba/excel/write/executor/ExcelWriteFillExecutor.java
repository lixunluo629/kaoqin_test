package com.alibaba.excel.write.executor;

import com.alibaba.excel.context.WriteContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.enums.WriteDirectionEnum;
import com.alibaba.excel.enums.WriteTemplateAnalysisCellTypeEnum;
import com.alibaba.excel.exception.ExcelGenerateException;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.util.WriteHandlerUtils;
import com.alibaba.excel.write.metadata.fill.AnalysisCell;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.cglib.beans.BeanMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/executor/ExcelWriteFillExecutor.class */
public class ExcelWriteFillExecutor extends AbstractExcelWriteExecutor {
    private static final String ESCAPE_FILL_PREFIX = "\\\\\\{";
    private static final String ESCAPE_FILL_SUFFIX = "\\\\\\}";
    private static final String FILL_PREFIX = "{";
    private static final String FILL_SUFFIX = "}";
    private static final char IGNORE_CHAR = '\\';
    private static final String COLLECTION_PREFIX = ".";
    private Map<Integer, List<AnalysisCell>> templateAnalysisCache;
    private Map<Integer, List<AnalysisCell>> templateCollectionAnalysisCache;
    private Map<Integer, Map<AnalysisCell, CellStyle>> collectionFieldStyleCache;
    private Map<Integer, Short> collectionRowHeightCache;
    private Map<Integer, Map<AnalysisCell, Integer>> collectionLastIndexCache;
    private Map<Integer, Integer> relativeRowIndexMap;

    public ExcelWriteFillExecutor(WriteContext writeContext) {
        super(writeContext);
        this.templateAnalysisCache = new HashMap(8);
        this.templateCollectionAnalysisCache = new HashMap(8);
        this.collectionFieldStyleCache = new HashMap(8);
        this.collectionRowHeightCache = new HashMap(8);
        this.collectionLastIndexCache = new HashMap(8);
        this.relativeRowIndexMap = new HashMap(8);
    }

    public void fill(Object data, FillConfig fillConfig) {
        if (fillConfig == null) {
            fillConfig = FillConfig.builder().build(true);
        }
        fillConfig.init();
        if (data instanceof Collection) {
            List<AnalysisCell> analysisCellList = readTemplateData(this.templateCollectionAnalysisCache);
            Collection collectionData = (Collection) data;
            if (CollectionUtils.isEmpty((Collection<?>) collectionData)) {
                return;
            }
            Iterator iterator = collectionData.iterator();
            if (WriteDirectionEnum.VERTICAL.equals(fillConfig.getDirection()) && fillConfig.getForceNewRow().booleanValue()) {
                shiftRows(collectionData.size(), analysisCellList);
            }
            while (iterator.hasNext()) {
                doFill(analysisCellList, iterator.next(), fillConfig, getRelativeRowIndex());
            }
            return;
        }
        doFill(readTemplateData(this.templateAnalysisCache), data, fillConfig, null);
    }

    private void shiftRows(int size, List<AnalysisCell> analysisCellList) {
        Integer lastRowIndex;
        if (CollectionUtils.isEmpty(analysisCellList)) {
            return;
        }
        int maxRowIndex = 0;
        Integer sheetNo = this.writeContext.writeSheetHolder().getSheetNo();
        Map<AnalysisCell, Integer> collectionLastIndexMap = this.collectionLastIndexCache.get(sheetNo);
        for (AnalysisCell analysisCell : analysisCellList) {
            if (collectionLastIndexMap != null && (lastRowIndex = collectionLastIndexMap.get(analysisCell)) != null) {
                if (lastRowIndex.intValue() > maxRowIndex) {
                    maxRowIndex = lastRowIndex.intValue();
                }
            } else if (analysisCell.getRowIndex() > maxRowIndex) {
                maxRowIndex = analysisCell.getRowIndex();
            }
        }
        Sheet cachedSheet = this.writeContext.writeSheetHolder().getCachedSheet();
        int lastRowIndex2 = cachedSheet.getLastRowNum();
        if (maxRowIndex >= lastRowIndex2) {
            return;
        }
        Sheet sheet = this.writeContext.writeSheetHolder().getCachedSheet();
        int number = size;
        if (collectionLastIndexMap == null) {
            number--;
        }
        if (number <= 0) {
            return;
        }
        sheet.shiftRows(maxRowIndex + 1, lastRowIndex2, number, true, false);
        for (AnalysisCell analysisCell2 : this.templateAnalysisCache.get(this.writeContext.writeSheetHolder().getSheetNo())) {
            if (analysisCell2.getRowIndex() > maxRowIndex) {
                analysisCell2.setRowIndex(analysisCell2.getRowIndex() + number);
            }
        }
    }

    private void doFill(List<AnalysisCell> analysisCellList, Object oneRowData, FillConfig fillConfig, Integer relativeRowIndex) {
        Map dataMap;
        if (oneRowData instanceof Map) {
            dataMap = (Map) oneRowData;
        } else {
            dataMap = BeanMap.create(oneRowData);
        }
        WriteSheetHolder writeSheetHolder = this.writeContext.writeSheetHolder();
        Map<String, ExcelContentProperty> fieldNameContentPropertyMap = this.writeContext.currentWriteHolder().excelWriteHeadProperty().getFieldNameContentPropertyMap();
        for (AnalysisCell analysisCell : analysisCellList) {
            Cell cell = getOneCell(analysisCell, fillConfig);
            if (analysisCell.getOnlyOneVariable().booleanValue()) {
                String variable = analysisCell.getVariableList().get(0);
                if (!this.writeContext.currentWriteHolder().ignore(variable, Integer.valueOf(analysisCell.getColumnIndex())) && dataMap.containsKey(variable)) {
                    Object value = dataMap.get(variable);
                    WriteHandlerUtils.afterCellDispose(this.writeContext, converterAndSet(writeSheetHolder, value == null ? null : value.getClass(), cell, value, fieldNameContentPropertyMap.get(variable)), cell, (Head) null, relativeRowIndex, Boolean.FALSE);
                }
            } else {
                StringBuilder cellValueBuild = new StringBuilder();
                int index = 0;
                List<CellData> cellDataList = new ArrayList<>();
                for (String variable2 : analysisCell.getVariableList()) {
                    int i = index;
                    index++;
                    cellValueBuild.append(analysisCell.getPrepareDataList().get(i));
                    if (!this.writeContext.currentWriteHolder().ignore(variable2, Integer.valueOf(analysisCell.getColumnIndex())) && dataMap.containsKey(variable2)) {
                        Object value2 = dataMap.get(variable2);
                        CellData cellData = convert(writeSheetHolder, value2 == null ? null : value2.getClass(), cell, value2, fieldNameContentPropertyMap.get(variable2));
                        cellDataList.add(cellData);
                        CellDataTypeEnum type = cellData.getType();
                        if (type != null) {
                            switch (type) {
                                case STRING:
                                    cellValueBuild.append(cellData.getStringValue());
                                    break;
                                case BOOLEAN:
                                    cellValueBuild.append(cellData.getBooleanValue());
                                    break;
                                case NUMBER:
                                    cellValueBuild.append(cellData.getNumberValue());
                                    break;
                            }
                        }
                    }
                }
                cellValueBuild.append(analysisCell.getPrepareDataList().get(index));
                cell.setCellValue(cellValueBuild.toString());
                WriteHandlerUtils.afterCellDispose(this.writeContext, cellDataList, cell, (Head) null, relativeRowIndex, Boolean.FALSE);
            }
        }
    }

    private Integer getRelativeRowIndex() {
        Integer sheetNo = this.writeContext.writeSheetHolder().getSheetNo();
        Integer relativeRowIndex = this.relativeRowIndexMap.get(sheetNo);
        Integer relativeRowIndex2 = relativeRowIndex == null ? 0 : Integer.valueOf(relativeRowIndex.intValue() + 1);
        this.relativeRowIndexMap.put(sheetNo, relativeRowIndex2);
        return relativeRowIndex2;
    }

    private Cell getOneCell(AnalysisCell analysisCell, FillConfig fillConfig) {
        Integer lastRowIndex;
        Integer lastColumnIndex;
        Sheet cachedSheet = this.writeContext.writeSheetHolder().getCachedSheet();
        if (WriteTemplateAnalysisCellTypeEnum.COMMON.equals(analysisCell.getCellType())) {
            return cachedSheet.getRow(analysisCell.getRowIndex()).getCell(analysisCell.getColumnIndex());
        }
        Integer sheetNo = this.writeContext.writeSheetHolder().getSheetNo();
        Sheet sheet = this.writeContext.writeSheetHolder().getSheet();
        Map<AnalysisCell, Integer> collectionLastIndexMap = this.collectionLastIndexCache.get(sheetNo);
        if (collectionLastIndexMap == null) {
            collectionLastIndexMap = new HashMap(16);
            this.collectionLastIndexCache.put(sheetNo, collectionLastIndexMap);
        }
        boolean isOriginalCell = false;
        switch (fillConfig.getDirection()) {
            case VERTICAL:
                Integer lastRowIndex2 = collectionLastIndexMap.get(analysisCell);
                if (lastRowIndex2 == null) {
                    lastRowIndex = Integer.valueOf(analysisCell.getRowIndex());
                    collectionLastIndexMap.put(analysisCell, lastRowIndex);
                    isOriginalCell = true;
                } else {
                    Integer numValueOf = Integer.valueOf(lastRowIndex2.intValue() + 1);
                    lastRowIndex = numValueOf;
                    collectionLastIndexMap.put(analysisCell, numValueOf);
                }
                lastColumnIndex = Integer.valueOf(analysisCell.getColumnIndex());
                break;
            case HORIZONTAL:
                lastRowIndex = Integer.valueOf(analysisCell.getRowIndex());
                Integer lastColumnIndex2 = collectionLastIndexMap.get(analysisCell);
                if (lastColumnIndex2 == null) {
                    lastColumnIndex = Integer.valueOf(analysisCell.getColumnIndex());
                    collectionLastIndexMap.put(analysisCell, lastColumnIndex);
                    isOriginalCell = true;
                    break;
                } else {
                    Integer numValueOf2 = Integer.valueOf(lastColumnIndex2.intValue() + 1);
                    lastColumnIndex = numValueOf2;
                    collectionLastIndexMap.put(analysisCell, numValueOf2);
                    break;
                }
            default:
                throw new ExcelGenerateException("The wrong direction.");
        }
        Row row = sheet.getRow(lastRowIndex.intValue());
        if (row == null) {
            row = cachedSheet.getRow(lastRowIndex.intValue());
            if (row == null) {
                WriteHandlerUtils.beforeRowCreate(this.writeContext, lastRowIndex, null, Boolean.FALSE);
                if (fillConfig.getForceNewRow().booleanValue()) {
                    row = cachedSheet.createRow(lastRowIndex.intValue());
                } else {
                    row = sheet.createRow(lastRowIndex.intValue());
                }
                checkRowHeight(analysisCell, fillConfig, isOriginalCell, row, sheetNo);
                WriteHandlerUtils.afterRowCreate(this.writeContext, row, null, Boolean.FALSE);
            } else {
                checkRowHeight(analysisCell, fillConfig, isOriginalCell, row, sheetNo);
            }
        }
        Cell cell = row.getCell(lastColumnIndex.intValue());
        if (cell == null) {
            WriteHandlerUtils.beforeCellCreate(this.writeContext, row, null, lastColumnIndex, null, Boolean.FALSE);
            cell = row.createCell(lastColumnIndex.intValue());
            WriteHandlerUtils.afterCellCreate(this.writeContext, cell, null, null, Boolean.FALSE);
        }
        Map<AnalysisCell, CellStyle> collectionFieldStyleMap = this.collectionFieldStyleCache.get(sheetNo);
        if (collectionFieldStyleMap == null) {
            collectionFieldStyleMap = new HashMap(16);
            this.collectionFieldStyleCache.put(sheetNo, collectionFieldStyleMap);
        }
        if (isOriginalCell) {
            collectionFieldStyleMap.put(analysisCell, cell.getCellStyle());
        } else {
            CellStyle cellStyle = collectionFieldStyleMap.get(analysisCell);
            if (cellStyle != null) {
                cell.setCellStyle(cellStyle);
            }
        }
        return cell;
    }

    private void checkRowHeight(AnalysisCell analysisCell, FillConfig fillConfig, boolean isOriginalCell, Row row, Integer sheetNo) {
        if (!analysisCell.getFirstColumn().booleanValue() || !WriteDirectionEnum.VERTICAL.equals(fillConfig.getDirection())) {
            return;
        }
        if (isOriginalCell) {
            this.collectionRowHeightCache.put(sheetNo, Short.valueOf(row.getHeight()));
            return;
        }
        Short rowHeight = this.collectionRowHeightCache.get(sheetNo);
        if (rowHeight != null) {
            row.setHeight(rowHeight.shortValue());
        }
    }

    private List<AnalysisCell> readTemplateData(Map<Integer, List<AnalysisCell>> analysisCache) {
        String preparedData;
        Integer sheetNo = this.writeContext.writeSheetHolder().getSheetNo();
        List<AnalysisCell> analysisCellList = analysisCache.get(sheetNo);
        if (analysisCellList != null) {
            return analysisCellList;
        }
        Sheet sheet = this.writeContext.writeSheetHolder().getCachedSheet();
        List<AnalysisCell> analysisCellList2 = new ArrayList<>();
        List<AnalysisCell> collectionAnalysisCellList = new ArrayList<>();
        Set<Integer> firstColumnCache = new HashSet<>();
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    if (cell != null && (preparedData = prepareData(cell, analysisCellList2, collectionAnalysisCellList, i, j, firstColumnCache)) != null) {
                        cell.setCellValue(preparedData);
                    }
                }
            }
        }
        this.templateAnalysisCache.put(sheetNo, analysisCellList2);
        this.templateCollectionAnalysisCache.put(sheetNo, collectionAnalysisCellList);
        return analysisCache.get(sheetNo);
    }

    private String prepareData(Cell cell, List<AnalysisCell> analysisCellList, List<AnalysisCell> collectionAnalysisCellList, int rowIndex, int columnIndex, Set<Integer> firstColumnCache) {
        int prefixIndex;
        if (!CellType.STRING.equals(cell.getCellTypeEnum())) {
            return null;
        }
        String value = cell.getStringCellValue();
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        StringBuilder preparedData = new StringBuilder();
        AnalysisCell analysisCell = null;
        int startIndex = 0;
        int length = value.length();
        int lastPrepareDataIndex = 0;
        loop0: while (startIndex < length && (prefixIndex = value.indexOf(FILL_PREFIX, startIndex)) >= 0) {
            if (prefixIndex != 0) {
                char prefixPrefixChar = value.charAt(prefixIndex - 1);
                if (prefixPrefixChar == '\\') {
                    startIndex = prefixIndex + 1;
                }
            }
            int suffixIndex = -1;
            while (suffixIndex == -1 && startIndex < length) {
                suffixIndex = value.indexOf("}", startIndex + 1);
                if (suffixIndex < 0) {
                    break loop0;
                }
                startIndex = suffixIndex + 1;
                char prefixSuffixChar = value.charAt(suffixIndex - 1);
                if (prefixSuffixChar == '\\') {
                    suffixIndex = -1;
                }
            }
            if (analysisCell == null) {
                analysisCell = initAnalysisCell(Integer.valueOf(rowIndex), Integer.valueOf(columnIndex));
            }
            String variable = value.substring(prefixIndex + 1, suffixIndex);
            if (!StringUtils.isEmpty(variable)) {
                if (variable.startsWith(".")) {
                    variable = variable.substring(1);
                    if (!StringUtils.isEmpty(variable)) {
                        analysisCell.setCellType(WriteTemplateAnalysisCellTypeEnum.COLLECTION);
                    }
                }
                analysisCell.getVariableList().add(variable);
                if (lastPrepareDataIndex == prefixIndex) {
                    analysisCell.getPrepareDataList().add("");
                } else {
                    String data = convertPrepareData(value.substring(lastPrepareDataIndex, prefixIndex));
                    preparedData.append(data);
                    analysisCell.getPrepareDataList().add(data);
                    analysisCell.setOnlyOneVariable(Boolean.FALSE);
                }
                lastPrepareDataIndex = suffixIndex + 1;
            }
        }
        return dealAnalysisCell(analysisCell, value, rowIndex, lastPrepareDataIndex, length, analysisCellList, collectionAnalysisCellList, firstColumnCache, preparedData);
    }

    private String dealAnalysisCell(AnalysisCell analysisCell, String value, int rowIndex, int lastPrepareDataIndex, int length, List<AnalysisCell> analysisCellList, List<AnalysisCell> collectionAnalysisCellList, Set<Integer> firstColumnCache, StringBuilder preparedData) {
        if (analysisCell != null) {
            if (lastPrepareDataIndex == length) {
                analysisCell.getPrepareDataList().add("");
            } else {
                analysisCell.getPrepareDataList().add(convertPrepareData(value.substring(lastPrepareDataIndex)));
                analysisCell.setOnlyOneVariable(Boolean.FALSE);
            }
            if (WriteTemplateAnalysisCellTypeEnum.COMMON.equals(analysisCell.getCellType())) {
                analysisCellList.add(analysisCell);
            } else {
                if (!firstColumnCache.contains(Integer.valueOf(rowIndex))) {
                    analysisCell.setFirstColumn(Boolean.TRUE);
                    firstColumnCache.add(Integer.valueOf(rowIndex));
                }
                collectionAnalysisCellList.add(analysisCell);
            }
            return preparedData.toString();
        }
        return null;
    }

    private AnalysisCell initAnalysisCell(Integer rowIndex, Integer columnIndex) {
        AnalysisCell analysisCell = new AnalysisCell();
        analysisCell.setRowIndex(rowIndex.intValue());
        analysisCell.setColumnIndex(columnIndex.intValue());
        analysisCell.setOnlyOneVariable(Boolean.TRUE);
        List<String> variableList = new ArrayList<>();
        analysisCell.setVariableList(variableList);
        List<String> prepareDataList = new ArrayList<>();
        analysisCell.setPrepareDataList(prepareDataList);
        analysisCell.setCellType(WriteTemplateAnalysisCellTypeEnum.COMMON);
        analysisCell.setFirstColumn(Boolean.FALSE);
        return analysisCell;
    }

    private String convertPrepareData(String prepareData) {
        return prepareData.replaceAll(ESCAPE_FILL_PREFIX, FILL_PREFIX).replaceAll(ESCAPE_FILL_SUFFIX, "}");
    }
}
