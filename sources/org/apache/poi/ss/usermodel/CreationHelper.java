package org.apache.poi.ss.usermodel;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/CreationHelper.class */
public interface CreationHelper {
    RichTextString createRichTextString(String str);

    DataFormat createDataFormat();

    Hyperlink createHyperlink(HyperlinkType hyperlinkType);

    FormulaEvaluator createFormulaEvaluator();

    ExtendedColor createExtendedColor();

    ClientAnchor createClientAnchor();

    AreaReference createAreaReference(String str);

    AreaReference createAreaReference(CellReference cellReference, CellReference cellReference2);
}
