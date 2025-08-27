package com.alibaba.excel.support;

import com.alibaba.excel.exception.ExcelCommonException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.EmptyFileException;
import org.apache.poi.poifs.filesystem.FileMagic;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/support/ExcelTypeEnum.class */
public enum ExcelTypeEnum {
    XLS(".xls"),
    XLSX(".xlsx");

    private String value;

    ExcelTypeEnum(String value) {
        setValue(value);
    }

    public static ExcelTypeEnum valueOf(File file, InputStream inputStream, ExcelTypeEnum excelType) throws EmptyFileException, IOException, CloneNotSupportedException {
        FileMagic fileMagic;
        try {
            if (file != null) {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                try {
                    fileMagic = FileMagic.valueOf(bufferedInputStream);
                    bufferedInputStream.close();
                    if (!FileMagic.OLE2.equals(fileMagic) && !FileMagic.OOXML.equals(fileMagic)) {
                        String fileName = file.getName();
                        if (fileName.endsWith(XLSX.getValue())) {
                            return XLSX;
                        }
                        if (fileName.endsWith(XLS.getValue())) {
                            return XLS;
                        }
                        throw new ExcelCommonException("Unknown excel type.");
                    }
                } catch (Throwable th) {
                    bufferedInputStream.close();
                    throw th;
                }
            } else {
                fileMagic = FileMagic.valueOf(inputStream);
            }
            if (FileMagic.OLE2.equals(fileMagic)) {
                return XLS;
            }
            if (FileMagic.OOXML.equals(fileMagic)) {
                return XLSX;
            }
            if (excelType != null) {
                return excelType;
            }
            throw new ExcelCommonException("Convert excel format exception.You can try specifying the 'excelType' yourself");
        } catch (IOException e) {
            if (excelType != null) {
                return excelType;
            }
            throw new ExcelCommonException("Convert excel format exception.You can try specifying the 'excelType' yourself", e);
        }
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
