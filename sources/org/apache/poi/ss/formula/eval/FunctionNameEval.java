package org.apache.poi.ss.formula.eval;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/eval/FunctionNameEval.class */
public final class FunctionNameEval implements ValueEval {
    private final String _functionName;

    public FunctionNameEval(String functionName) {
        this._functionName = functionName;
    }

    public String getFunctionName() {
        return this._functionName;
    }

    public String toString() {
        return getClass().getName() + " [" + this._functionName + "]";
    }
}
