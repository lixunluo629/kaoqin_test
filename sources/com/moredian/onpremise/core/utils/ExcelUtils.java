package com.moredian.onpremise.core.utils;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFPrintSetup;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/utils/ExcelUtils.class */
public class ExcelUtils {

    @Value("${onpremise.save.excel.path}")
    private static String saveExcelPath;
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) ExcelUtils.class);
    private static short defaultColumnWidth = 5;
    private static short defaultColumnHeight = 15;

    public static String exportExcel(String filePath, String fileName, String sheetName, List<String> tabList, List<String> tabList2, List<List<String>> dataList, List<String> desList) {
        try {
            File dest = new File(filePath);
            if (!dest.exists()) {
                dest.mkdirs();
            }
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet(sheetName);
            sheet.setDefaultRowHeightInPoints(defaultColumnHeight);
            sheet.setDefaultColumnWidth(defaultColumnWidth);
            HSSFRow row = sheet.createRow(0);
            HSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            for (int i = 0; i < tabList.size(); i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(tabList.get(i));
                cell.setCellStyle(style);
            }
            HSSFRow row2 = sheet.createRow(1);
            for (int i2 = 0; i2 < tabList2.size(); i2++) {
                HSSFCell cell2 = row2.createCell(i2);
                cell2.setCellValue(tabList2.get(i2));
                cell2.setCellStyle(style);
            }
            for (int i3 = 0; i3 < dataList.size(); i3++) {
                List<String> detailList = dataList.get(i3);
                HSSFRow row3 = sheet.createRow(i3 + 2);
                for (int j = 0; j < detailList.size(); j++) {
                    HSSFCell cell3 = row3.createCell(j);
                    cell3.setCellValue(detailList.get(j));
                    cell3.setCellStyle(style);
                }
            }
            HSSFRow row4 = sheet.createRow(dataList.size() + 2 + 1);
            for (int j2 = 0; j2 < desList.size(); j2++) {
                HSSFCell cell4 = row4.createCell(j2 + 2);
                cell4.setCellValue(desList.get(j2));
                cell4.setCellStyle(style);
            }
            FileOutputStream os = new FileOutputStream(filePath + fileName);
            wb.write(os);
        } catch (IOException e) {
            logger.error("excel export error:{}", (Throwable) e);
        }
        return filePath + fileName;
    }

    public static String exportExcel(String filePath, String fileName, Map<String, List<List<String>>> sheets) throws IOException, IllegalArgumentException {
        try {
            File dest = new File(filePath);
            if (!dest.exists()) {
                dest.mkdirs();
            }
            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFCellStyle cellStyle = wb.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            for (String sheetName : sheets.keySet()) {
                XSSFSheet excelSheet = wb.createSheet(sheetName);
                excelSheet.setDefaultRowHeightInPoints(defaultColumnHeight);
                excelSheet.setDefaultColumnWidth(defaultColumnWidth);
                List<List<String>> rows = sheets.get(sheetName);
                int rowIndex = 0;
                for (List<String> eachRow : rows) {
                    int i = rowIndex;
                    rowIndex++;
                    XSSFRow excelRow = excelSheet.createRow(i);
                    int cellIndex = 0;
                    for (String eachCol : eachRow) {
                        if (eachCol != null) {
                            int i2 = cellIndex;
                            cellIndex++;
                            XSSFCell excelCell = excelRow.createCell(i2);
                            if (eachCol.startsWith(SymbolConstants.EQUAL_SYMBOL)) {
                                excelCell.setCellFormula(eachCol.substring(1));
                                excelCell.setCellType(CellType.FORMULA);
                            } else {
                                excelCell.setCellValue(eachCol);
                            }
                            if (StringUtils.isBlank(eachCol)) {
                                CellRangeAddress region = new CellRangeAddress(excelRow.getRowNum() - 1, excelRow.getRowNum(), excelCell.getColumnIndex(), excelCell.getColumnIndex());
                                excelSheet.addMergedRegion(region);
                            }
                            if (eachCol.indexOf(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR) > 0) {
                                cellStyle.setWrapText(true);
                            }
                            excelCell.setCellStyle(cellStyle);
                        }
                    }
                }
                XSSFPrintSetup printSetup = excelSheet.getPrintSetup();
                printSetup.setLandscape(true);
            }
            FileOutputStream os = new FileOutputStream(filePath + fileName);
            wb.setForceFormulaRecalculation(true);
            wb.write(os);
            os.close();
        } catch (IOException e) {
            logger.error("excel export error:{}", (Throwable) e);
        }
        return filePath + fileName;
    }
}
