package org.apache.poi.ss.formula;

import org.apache.poi.ss.formula.ptg.NamePtg;
import org.apache.poi.ss.formula.ptg.Ptg;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/EvaluationName.class */
public interface EvaluationName {
    String getNameText();

    boolean isFunctionName();

    boolean hasFormula();

    Ptg[] getNameDefinition();

    boolean isRange();

    NamePtg createPtg();
}
