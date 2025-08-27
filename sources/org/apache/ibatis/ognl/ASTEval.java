package org.apache.ibatis.ognl;

import org.apache.ibatis.ognl.enhance.UnsupportedCompilationException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ASTEval.class */
class ASTEval extends SimpleNode {
    public ASTEval(int id) {
        super(id);
    }

    public ASTEval(OgnlParser p, int id) {
        super(p, id);
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
        Object expr = this._children[0].getValue(context, source);
        Object previousRoot = context.getRoot();
        Object source2 = this._children[1].getValue(context, source);
        Node node = expr instanceof Node ? (Node) expr : (Node) Ognl.parseExpression(expr.toString());
        try {
            context.setRoot(source2);
            Object result = node.getValue(context, source2);
            context.setRoot(previousRoot);
            return result;
        } catch (Throwable th) {
            context.setRoot(previousRoot);
            throw th;
        }
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    protected void setValueBody(OgnlContext context, Object target, Object value) throws OgnlException {
        Object expr = this._children[0].getValue(context, target);
        Object previousRoot = context.getRoot();
        Object target2 = this._children[1].getValue(context, target);
        Node node = expr instanceof Node ? (Node) expr : (Node) Ognl.parseExpression(expr.toString());
        try {
            context.setRoot(target2);
            node.setValue(context, target2, value);
            context.setRoot(previousRoot);
        } catch (Throwable th) {
            context.setRoot(previousRoot);
            throw th;
        }
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    public boolean isEvalChain(OgnlContext context) throws OgnlException {
        return true;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    public String toString() {
        return "(" + this._children[0] + ")(" + this._children[1] + ")";
    }

    @Override // org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toGetSourceString(OgnlContext context, Object target) {
        throw new UnsupportedCompilationException("Eval expressions not supported as native java yet.");
    }

    @Override // org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toSetSourceString(OgnlContext context, Object target) {
        throw new UnsupportedCompilationException("Map expressions not supported as native java yet.");
    }
}
