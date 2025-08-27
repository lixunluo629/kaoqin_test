package org.apache.poi.ss.formula;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/ExternSheetReferenceToken.class */
public interface ExternSheetReferenceToken {
    int getExternSheetIndex();

    String format2DRefAsString();
}
