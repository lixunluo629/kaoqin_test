package org.apache.poi.ss.formula.udf;

import org.apache.poi.ss.formula.atp.AnalysisToolPak;
import org.apache.poi.ss.formula.functions.FreeRefFunction;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/udf/UDFFinder.class */
public interface UDFFinder {

    @Deprecated
    public static final UDFFinder DEFAULT = new AggregatingUDFFinder(AnalysisToolPak.instance);

    FreeRefFunction findFunction(String str);
}
