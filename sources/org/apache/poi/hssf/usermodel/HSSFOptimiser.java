package org.apache.poi.hssf.usermodel;

import java.util.HashSet;
import java.util.Iterator;
import org.apache.poi.hssf.record.ExtendedFormatRecord;
import org.apache.poi.hssf.record.FontRecord;
import org.apache.poi.hssf.record.common.UnicodeString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/usermodel/HSSFOptimiser.class */
public class HSSFOptimiser {
    public static void optimiseFonts(HSSFWorkbook workbook) {
        short[] newPos = new short[workbook.getWorkbook().getNumberOfFontRecords() + 1];
        boolean[] zapRecords = new boolean[newPos.length];
        for (int i = 0; i < newPos.length; i++) {
            newPos[i] = (short) i;
            zapRecords[i] = false;
        }
        FontRecord[] frecs = new FontRecord[newPos.length];
        for (int i2 = 0; i2 < newPos.length; i2++) {
            if (i2 != 4) {
                frecs[i2] = workbook.getWorkbook().getFontRecordAt(i2);
            }
        }
        for (int i3 = 5; i3 < newPos.length; i3++) {
            int earlierDuplicate = -1;
            for (int j = 0; j < i3 && earlierDuplicate == -1; j++) {
                if (j != 4) {
                    FontRecord frCheck = workbook.getWorkbook().getFontRecordAt(j);
                    if (frCheck.sameProperties(frecs[i3])) {
                        earlierDuplicate = j;
                    }
                }
            }
            if (earlierDuplicate != -1) {
                newPos[i3] = (short) earlierDuplicate;
                zapRecords[i3] = true;
            }
        }
        for (int i4 = 5; i4 < newPos.length; i4++) {
            short preDeletePos = newPos[i4];
            short newPosition = preDeletePos;
            for (int j2 = 0; j2 < preDeletePos; j2++) {
                if (zapRecords[j2]) {
                    newPosition = (short) (newPosition - 1);
                }
            }
            newPos[i4] = newPosition;
        }
        for (int i5 = 5; i5 < newPos.length; i5++) {
            if (zapRecords[i5]) {
                workbook.getWorkbook().removeFontRecord(frecs[i5]);
            }
        }
        workbook.resetFontCache();
        for (int i6 = 0; i6 < workbook.getWorkbook().getNumExFormats(); i6++) {
            ExtendedFormatRecord xfr = workbook.getWorkbook().getExFormatAt(i6);
            xfr.setFontIndex(newPos[xfr.getFontIndex()]);
        }
        HashSet<UnicodeString> doneUnicodeStrings = new HashSet<>();
        for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
            HSSFSheet s = workbook.getSheetAt(sheetNum);
            Iterator i$ = s.iterator();
            while (i$.hasNext()) {
                Row row = i$.next();
                for (Cell cell : row) {
                    if (cell.getCellTypeEnum() == CellType.STRING) {
                        HSSFRichTextString rtr = (HSSFRichTextString) cell.getRichStringCellValue();
                        UnicodeString u = rtr.getRawUnicodeString();
                        if (!doneUnicodeStrings.contains(u)) {
                            short s2 = 5;
                            while (true) {
                                short i7 = s2;
                                if (i7 >= newPos.length) {
                                    break;
                                }
                                if (i7 != newPos[i7]) {
                                    u.swapFontUse(i7, newPos[i7]);
                                }
                                s2 = (short) (i7 + 1);
                            }
                            doneUnicodeStrings.add(u);
                        }
                    }
                }
            }
        }
    }

    public static void optimiseCellStyles(HSSFWorkbook workbook) {
        short[] newPos = new short[workbook.getWorkbook().getNumExFormats()];
        boolean[] isUsed = new boolean[newPos.length];
        boolean[] zapRecords = new boolean[newPos.length];
        for (int i = 0; i < newPos.length; i++) {
            isUsed[i] = false;
            newPos[i] = (short) i;
            zapRecords[i] = false;
        }
        ExtendedFormatRecord[] xfrs = new ExtendedFormatRecord[newPos.length];
        for (int i2 = 0; i2 < newPos.length; i2++) {
            xfrs[i2] = workbook.getWorkbook().getExFormatAt(i2);
        }
        for (int i3 = 21; i3 < newPos.length; i3++) {
            int earlierDuplicate = -1;
            for (int j = 0; j < i3 && earlierDuplicate == -1; j++) {
                ExtendedFormatRecord xfCheck = workbook.getWorkbook().getExFormatAt(j);
                if (xfCheck.equals(xfrs[i3])) {
                    earlierDuplicate = j;
                }
            }
            if (earlierDuplicate != -1) {
                newPos[i3] = (short) earlierDuplicate;
                zapRecords[i3] = true;
            }
            if (earlierDuplicate != -1) {
                isUsed[earlierDuplicate] = true;
            }
        }
        for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
            HSSFSheet s = workbook.getSheetAt(sheetNum);
            Iterator i$ = s.iterator();
            while (i$.hasNext()) {
                Row row = i$.next();
                for (Cell cellI : row) {
                    short oldXf = ((HSSFCell) cellI).getCellValueRecord().getXFIndex();
                    isUsed[oldXf] = true;
                }
            }
        }
        for (int i4 = 21; i4 < isUsed.length; i4++) {
            if (!isUsed[i4]) {
                zapRecords[i4] = true;
                newPos[i4] = 0;
            }
        }
        for (int i5 = 21; i5 < newPos.length; i5++) {
            short preDeletePos = newPos[i5];
            short newPosition = preDeletePos;
            for (int j2 = 0; j2 < preDeletePos; j2++) {
                if (zapRecords[j2]) {
                    newPosition = (short) (newPosition - 1);
                }
            }
            newPos[i5] = newPosition;
        }
        int max = newPos.length;
        int removed = 0;
        int i6 = 21;
        while (i6 < max) {
            if (zapRecords[i6 + removed]) {
                workbook.getWorkbook().removeExFormatRecord(i6);
                i6--;
                max--;
                removed++;
            }
            i6++;
        }
        for (int sheetNum2 = 0; sheetNum2 < workbook.getNumberOfSheets(); sheetNum2++) {
            HSSFSheet s2 = workbook.getSheetAt(sheetNum2);
            Iterator i$2 = s2.iterator();
            while (i$2.hasNext()) {
                Row row2 = i$2.next();
                for (Cell cellI2 : row2) {
                    HSSFCell cell = (HSSFCell) cellI2;
                    short oldXf2 = cell.getCellValueRecord().getXFIndex();
                    HSSFCellStyle newStyle = workbook.getCellStyleAt((int) newPos[oldXf2]);
                    cell.setCellStyle(newStyle);
                }
            }
        }
    }
}
