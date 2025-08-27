package org.apache.poi.ss.formula;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Table;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/FormulaParsingWorkbook.class */
public interface FormulaParsingWorkbook {
    EvaluationName getName(String str, int i);

    Name createName();

    Table getTable(String str);

    Ptg getNameXPtg(String str, SheetIdentifier sheetIdentifier);

    Ptg get3DReferencePtg(CellReference cellReference, SheetIdentifier sheetIdentifier);

    Ptg get3DReferencePtg(AreaReference areaReference, SheetIdentifier sheetIdentifier);

    int getExternalSheetIndex(String str);

    int getExternalSheetIndex(String str, String str2);

    SpreadsheetVersion getSpreadsheetVersion();
}
