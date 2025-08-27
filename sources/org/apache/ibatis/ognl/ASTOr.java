package org.apache.ibatis.ognl;

import org.apache.ibatis.ognl.enhance.ExpressionCompiler;
import org.apache.ibatis.ognl.enhance.UnsupportedCompilationException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ASTOr.class */
public class ASTOr extends BooleanExpression {
    public ASTOr(int id) {
        super(id);
    }

    public ASTOr(OgnlParser p, int id) {
        super(p, id);
    }

    @Override // org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.Node
    public void jjtClose() {
        flattenTree();
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
        Object result = null;
        int last = this._children.length - 1;
        for (int i = 0; i <= last; i++) {
            result = this._children[i].getValue(context, source);
            if (i != last && OgnlOps.booleanValue(result)) {
                break;
            }
        }
        return result;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    protected void setValueBody(OgnlContext context, Object target, Object value) throws OgnlException {
        int last = this._children.length - 1;
        for (int i = 0; i < last; i++) {
            Object v = this._children[i].getValue(context, target);
            if (OgnlOps.booleanValue(v)) {
                return;
            }
        }
        this._children[last].setValue(context, target, value);
    }

    @Override // org.apache.ibatis.ognl.ExpressionNode
    public String getExpressionOperator(int index) {
        return "||";
    }

    @Override // org.apache.ibatis.ognl.BooleanExpression, org.apache.ibatis.ognl.NodeType
    public Class getGetterClass() {
        return null;
    }

    @Override // org.apache.ibatis.ognl.BooleanExpression, org.apache.ibatis.ognl.ExpressionNode, org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toGetSourceString(OgnlContext context, Object target) {
        if (this._children.length != 2) {
            throw new UnsupportedCompilationException("Can only compile boolean expressions with two children.");
        }
        try {
            String first = OgnlRuntime.getChildSource(context, target, this._children[0]);
            if (!OgnlRuntime.isBoolean(first)) {
                first = OgnlRuntime.getCompiler().createLocalReference(context, first, context.getCurrentType());
            }
            Class firstType = context.getCurrentType();
            String second = OgnlRuntime.getChildSource(context, target, this._children[1]);
            if (!OgnlRuntime.isBoolean(second)) {
                second = OgnlRuntime.getCompiler().createLocalReference(context, second, context.getCurrentType());
            }
            Class secondType = context.getCurrentType();
            boolean mismatched = (firstType.isPrimitive() && !secondType.isPrimitive()) || (!firstType.isPrimitive() && secondType.isPrimitive());
            String result = "(org.apache.ibatis.ognl.OgnlOps.booleanValue(" + first + ")";
            String result2 = ((((result + " ? ") + (mismatched ? " ($w) " : "") + first) + " : ") + (mismatched ? " ($w) " : "") + second) + ")";
            context.setCurrentObject(target);
            context.setCurrentType(Boolean.TYPE);
            return result2;
        } catch (Throwable t) {
            throw OgnlOps.castToRuntime(t);
        }
    }

    @Override // org.apache.ibatis.ognl.ExpressionNode, org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toSetSourceString(OgnlContext context, Object target) {
        if (this._children.length != 2) {
            throw new UnsupportedCompilationException("Can only compile boolean expressions with two children.");
        }
        String pre = (String) context.get("_currentChain");
        if (pre == null) {
            pre = "";
        }
        try {
            this._children[0].getValue(context, target);
            String first = ExpressionCompiler.getRootExpression(this._children[0], context.getRoot(), context) + pre + this._children[0].toGetSourceString(context, target);
            if (!OgnlRuntime.isBoolean(first)) {
                first = OgnlRuntime.getCompiler().createLocalReference(context, first, Object.class);
            }
            this._children[1].getValue(context, target);
            String second = ExpressionCompiler.getRootExpression(this._children[1], context.getRoot(), context) + pre + this._children[1].toSetSourceString(context, target);
            if (!OgnlRuntime.isBoolean(second)) {
                second = OgnlRuntime.getCompiler().createLocalReference(context, second, context.getCurrentType());
            }
            String result = "org.apache.ibatis.ognl.OgnlOps.booleanValue(" + first + ")";
            String result2 = (((result + " ? ") + first) + " : ") + second;
            context.setCurrentObject(target);
            context.setCurrentType(Boolean.TYPE);
            return result2;
        } catch (Throwable t) {
            throw OgnlOps.castToRuntime(t);
        }
    }
}
