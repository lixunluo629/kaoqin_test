package org.apache.poi.ss.formula.eval;

import java.util.ArrayList;
import java.util.List;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/eval/RefListEval.class */
public class RefListEval implements ValueEval {
    private final List<ValueEval> list = new ArrayList();

    public RefListEval(ValueEval v1, ValueEval v2) {
        add(v1);
        add(v2);
    }

    private void add(ValueEval v) {
        if (v instanceof RefListEval) {
            this.list.addAll(((RefListEval) v).list);
        } else {
            this.list.add(v);
        }
    }

    public List<ValueEval> getList() {
        return this.list;
    }
}
