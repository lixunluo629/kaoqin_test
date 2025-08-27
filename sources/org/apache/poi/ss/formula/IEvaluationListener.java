package org.apache.poi.ss.formula;

import org.apache.poi.ss.formula.eval.ValueEval;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/IEvaluationListener.class */
interface IEvaluationListener {

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/IEvaluationListener$ICacheEntry.class */
    public interface ICacheEntry {
        ValueEval getValue();
    }

    void onCacheHit(int i, int i2, int i3, ValueEval valueEval);

    void onReadPlainValue(int i, int i2, int i3, ICacheEntry iCacheEntry);

    void onStartEvaluate(EvaluationCell evaluationCell, ICacheEntry iCacheEntry);

    void onEndEvaluate(ICacheEntry iCacheEntry, ValueEval valueEval);

    void onClearWholeCache();

    void onClearCachedValue(ICacheEntry iCacheEntry);

    void sortDependentCachedValues(ICacheEntry[] iCacheEntryArr);

    void onClearDependentCachedValue(ICacheEntry iCacheEntry, int i);

    void onChangeFromBlankValue(int i, int i2, int i3, EvaluationCell evaluationCell, ICacheEntry iCacheEntry);
}
