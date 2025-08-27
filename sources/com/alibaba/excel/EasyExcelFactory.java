package com.alibaba.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.event.WriteHandler;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.builder.ExcelWriterTableBuilder;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/EasyExcelFactory.class */
public class EasyExcelFactory {
    @Deprecated
    public static List<Object> read(InputStream in, Sheet sheet) {
        final List<Object> rows = new ArrayList<>();
        new ExcelReader(in, (Object) null, (AnalysisEventListener) new AnalysisEventListener<Object>() { // from class: com.alibaba.excel.EasyExcelFactory.1
            @Override // com.alibaba.excel.read.listener.ReadListener
            public void invoke(Object object, AnalysisContext context) {
                rows.add(object);
            }

            @Override // com.alibaba.excel.read.listener.ReadListener
            public void doAfterAllAnalysed(AnalysisContext context) {
            }
        }, false).read(sheet);
        return rows;
    }

    @Deprecated
    public static void readBySax(InputStream in, Sheet sheet, AnalysisEventListener listener) {
        new ExcelReader(in, null, listener).read(sheet);
    }

    @Deprecated
    public static ExcelReader getReader(InputStream in, AnalysisEventListener listener) {
        return new ExcelReader(in, null, listener);
    }

    @Deprecated
    public static ExcelWriter getWriter(OutputStream outputStream) {
        return write().file(outputStream).autoCloseStream(Boolean.FALSE).convertAllFiled(Boolean.FALSE).build();
    }

    @Deprecated
    public static ExcelWriter getWriter(OutputStream outputStream, ExcelTypeEnum typeEnum, boolean needHead) {
        return write().file(outputStream).excelType(typeEnum).needHead(Boolean.valueOf(needHead)).autoCloseStream(Boolean.FALSE).convertAllFiled(Boolean.FALSE).build();
    }

    @Deprecated
    public static ExcelWriter getWriterWithTemp(InputStream temp, OutputStream outputStream, ExcelTypeEnum typeEnum, boolean needHead) {
        return write().withTemplate(temp).file(outputStream).excelType(typeEnum).needHead(Boolean.valueOf(needHead)).autoCloseStream(Boolean.FALSE).convertAllFiled(Boolean.FALSE).build();
    }

    @Deprecated
    public static ExcelWriter getWriterWithTempAndHandler(InputStream temp, OutputStream outputStream, ExcelTypeEnum typeEnum, boolean needHead, WriteHandler handler) {
        return write().withTemplate(temp).file(outputStream).excelType(typeEnum).needHead(Boolean.valueOf(needHead)).registerWriteHandler(handler).autoCloseStream(Boolean.FALSE).convertAllFiled(Boolean.FALSE).build();
    }

    public static ExcelWriterBuilder write() {
        return new ExcelWriterBuilder();
    }

    public static ExcelWriterBuilder write(File file) {
        return write(file, (Class) null);
    }

    public static ExcelWriterBuilder write(File file, Class head) {
        ExcelWriterBuilder excelWriterBuilder = new ExcelWriterBuilder();
        excelWriterBuilder.file(file);
        if (head != null) {
            excelWriterBuilder.head(head);
        }
        return excelWriterBuilder;
    }

    public static ExcelWriterBuilder write(String pathName) {
        return write(pathName, (Class) null);
    }

    public static ExcelWriterBuilder write(String pathName, Class head) {
        ExcelWriterBuilder excelWriterBuilder = new ExcelWriterBuilder();
        excelWriterBuilder.file(pathName);
        if (head != null) {
            excelWriterBuilder.head(head);
        }
        return excelWriterBuilder;
    }

    public static ExcelWriterBuilder write(OutputStream outputStream) {
        return write(outputStream, (Class) null);
    }

    public static ExcelWriterBuilder write(OutputStream outputStream, Class head) {
        ExcelWriterBuilder excelWriterBuilder = new ExcelWriterBuilder();
        excelWriterBuilder.file(outputStream);
        if (head != null) {
            excelWriterBuilder.head(head);
        }
        return excelWriterBuilder;
    }

    public static ExcelWriterSheetBuilder writerSheet() {
        return writerSheet(null, null);
    }

    public static ExcelWriterSheetBuilder writerSheet(Integer sheetNo) {
        return writerSheet(sheetNo, null);
    }

    public static ExcelWriterSheetBuilder writerSheet(String sheetName) {
        return writerSheet(null, sheetName);
    }

    public static ExcelWriterSheetBuilder writerSheet(Integer sheetNo, String sheetName) {
        ExcelWriterSheetBuilder excelWriterSheetBuilder = new ExcelWriterSheetBuilder();
        if (sheetNo != null) {
            excelWriterSheetBuilder.sheetNo(sheetNo);
        }
        if (sheetName != null) {
            excelWriterSheetBuilder.sheetName(sheetName);
        }
        return excelWriterSheetBuilder;
    }

    public static ExcelWriterTableBuilder writerTable() {
        return writerTable(null);
    }

    public static ExcelWriterTableBuilder writerTable(Integer tableNo) {
        ExcelWriterTableBuilder excelWriterTableBuilder = new ExcelWriterTableBuilder();
        if (tableNo != null) {
            excelWriterTableBuilder.tableNo(tableNo);
        }
        return excelWriterTableBuilder;
    }

    public static ExcelReaderBuilder read() {
        return new ExcelReaderBuilder();
    }

    public static ExcelReaderBuilder read(File file) {
        return read(file, (Class) null, (ReadListener) null);
    }

    public static ExcelReaderBuilder read(File file, ReadListener readListener) {
        return read(file, (Class) null, readListener);
    }

    public static ExcelReaderBuilder read(File file, Class head, ReadListener readListener) {
        ExcelReaderBuilder excelReaderBuilder = new ExcelReaderBuilder();
        excelReaderBuilder.file(file);
        if (head != null) {
            excelReaderBuilder.head(head);
        }
        if (readListener != null) {
            excelReaderBuilder.registerReadListener(readListener);
        }
        return excelReaderBuilder;
    }

    public static ExcelReaderBuilder read(String pathName) {
        return read(pathName, (Class) null, (ReadListener) null);
    }

    public static ExcelReaderBuilder read(String pathName, ReadListener readListener) {
        return read(pathName, (Class) null, readListener);
    }

    public static ExcelReaderBuilder read(String pathName, Class head, ReadListener readListener) {
        ExcelReaderBuilder excelReaderBuilder = new ExcelReaderBuilder();
        excelReaderBuilder.file(pathName);
        if (head != null) {
            excelReaderBuilder.head(head);
        }
        if (readListener != null) {
            excelReaderBuilder.registerReadListener(readListener);
        }
        return excelReaderBuilder;
    }

    public static ExcelReaderBuilder read(InputStream inputStream) {
        return read(inputStream, (Class) null, (ReadListener) null);
    }

    public static ExcelReaderBuilder read(InputStream inputStream, ReadListener readListener) {
        return read(inputStream, (Class) null, readListener);
    }

    public static ExcelReaderBuilder read(InputStream inputStream, Class head, ReadListener readListener) {
        ExcelReaderBuilder excelReaderBuilder = new ExcelReaderBuilder();
        excelReaderBuilder.file(inputStream);
        if (head != null) {
            excelReaderBuilder.head(head);
        }
        if (readListener != null) {
            excelReaderBuilder.registerReadListener(readListener);
        }
        return excelReaderBuilder;
    }

    public static ExcelReaderSheetBuilder readSheet() {
        return readSheet(null, null);
    }

    public static ExcelReaderSheetBuilder readSheet(Integer sheetNo) {
        return readSheet(sheetNo, null);
    }

    public static ExcelReaderSheetBuilder readSheet(String sheetName) {
        return readSheet(null, sheetName);
    }

    public static ExcelReaderSheetBuilder readSheet(Integer sheetNo, String sheetName) {
        ExcelReaderSheetBuilder excelReaderSheetBuilder = new ExcelReaderSheetBuilder();
        if (sheetNo != null) {
            excelReaderSheetBuilder.sheetNo(sheetNo);
        }
        if (sheetName != null) {
            excelReaderSheetBuilder.sheetName(sheetName);
        }
        return excelReaderSheetBuilder;
    }
}
