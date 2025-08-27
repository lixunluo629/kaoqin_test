package org.apache.poi.ss.formula.udf;

import java.util.HashMap;
import org.apache.poi.ss.formula.functions.FreeRefFunction;
import org.apache.poi.util.Internal;

@Internal
/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/udf/IndexedUDFFinder.class */
public class IndexedUDFFinder extends AggregatingUDFFinder {
    private final HashMap<Integer, String> _funcMap;

    public IndexedUDFFinder(UDFFinder... usedToolPacks) {
        super(usedToolPacks);
        this._funcMap = new HashMap<>();
    }

    @Override // org.apache.poi.ss.formula.udf.AggregatingUDFFinder, org.apache.poi.ss.formula.udf.UDFFinder
    public FreeRefFunction findFunction(String name) {
        FreeRefFunction func = super.findFunction(name);
        if (func != null) {
            int idx = getFunctionIndex(name);
            this._funcMap.put(Integer.valueOf(idx), name);
        }
        return func;
    }

    public String getFunctionName(int idx) {
        return this._funcMap.get(Integer.valueOf(idx));
    }

    public int getFunctionIndex(String name) {
        return name.hashCode();
    }
}
