package org.apache.poi.ss.extractor;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/extractor/ExcelExtractor.class */
public interface ExcelExtractor {
    void setIncludeSheetNames(boolean z);

    void setFormulasNotResults(boolean z);

    void setIncludeHeadersFooters(boolean z);

    void setIncludeCellComments(boolean z);

    String getText();
}
