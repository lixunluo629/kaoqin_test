package org.aspectj.runtime.internal;

import org.aspectj.runtime.CFlow;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/runtime/internal/CFlowPlusState.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/runtime/internal/CFlowPlusState.class */
public class CFlowPlusState extends CFlow {
    private Object[] state;

    public CFlowPlusState(Object[] state) {
        this.state = state;
    }

    public CFlowPlusState(Object[] state, Object _aspect) {
        super(_aspect);
        this.state = state;
    }

    @Override // org.aspectj.runtime.CFlow
    public Object get(int index) {
        return this.state[index];
    }
}
