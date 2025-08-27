package org.apache.ibatis.ognl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.apache.ibatis.ognl.enhance.UnsupportedCompilationException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ASTSelectLast.class */
class ASTSelectLast extends SimpleNode {
    public ASTSelectLast(int id) {
        super(id);
    }

    public ASTSelectLast(OgnlParser p, int id) {
        super(p, id);
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
        Node expr = this._children[0];
        List answer = new ArrayList();
        ElementsAccessor elementsAccessor = OgnlRuntime.getElementsAccessor(OgnlRuntime.getTargetClass(source));
        Enumeration e = elementsAccessor.getElements(source);
        while (e.hasMoreElements()) {
            Object next = e.nextElement();
            if (OgnlOps.booleanValue(expr.getValue(context, next))) {
                answer.clear();
                answer.add(next);
            }
        }
        return answer;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    public String toString() {
        return "{$ " + this._children[0] + " }";
    }

    @Override // org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toGetSourceString(OgnlContext context, Object target) {
        throw new UnsupportedCompilationException("Eval expressions not supported as native java yet.");
    }

    @Override // org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toSetSourceString(OgnlContext context, Object target) {
        throw new UnsupportedCompilationException("Eval expressions not supported as native java yet.");
    }
}
