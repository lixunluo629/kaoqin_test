package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.NotImplementedFunctionException;
import org.apache.poi.ss.formula.eval.ValueEval;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/NotImplementedFunction.class */
public final class NotImplementedFunction implements Function {
    private final String _functionName;

    protected NotImplementedFunction() {
        this._functionName = getClass().getName();
    }

    public NotImplementedFunction(String name) {
        this._functionName = name;
    }

    @Override // org.apache.poi.ss.formula.functions.Function
    public ValueEval evaluate(ValueEval[] operands, int srcRow, int srcCol) {
        throw new NotImplementedFunctionException(this._functionName);
    }

    public String getFunctionName() {
        return this._functionName;
    }
}
