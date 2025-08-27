package org.apache.ibatis.ognl;

import org.apache.ibatis.ognl.enhance.ExpressionCompiler;
import org.apache.ibatis.ognl.enhance.UnsupportedCompilationException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ASTAnd.class */
public class ASTAnd extends BooleanExpression {
    public ASTAnd(int id) {
        super(id);
    }

    public ASTAnd(OgnlParser p, int id) {
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
            if (i != last && !OgnlOps.booleanValue(result)) {
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
            if (!OgnlOps.booleanValue(v)) {
                return;
            }
        }
        this._children[last].setValue(context, target, value);
    }

    @Override // org.apache.ibatis.ognl.ExpressionNode
    public String getExpressionOperator(int index) {
        return "&&";
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
            if (!OgnlOps.booleanValue(context.getCurrentObject())) {
                throw new UnsupportedCompilationException("And expression can't be compiled until all conditions are true.");
            }
            if (!OgnlRuntime.isBoolean(first) && !context.getCurrentType().isPrimitive()) {
                first = OgnlRuntime.getCompiler().createLocalReference(context, first, context.getCurrentType());
            }
            String second = OgnlRuntime.getChildSource(context, target, this._children[1]);
            if (!OgnlRuntime.isBoolean(second) && !context.getCurrentType().isPrimitive()) {
                second = OgnlRuntime.getCompiler().createLocalReference(context, second, context.getCurrentType());
            }
            String result = "(ognl.OgnlOps.booleanValue(" + first + ")";
            String result2 = ((((result + " ? ") + " ($w) (" + second + ")") + " : ") + " ($w) (" + first + ")") + ")";
            context.setCurrentObject(target);
            context.setCurrentType(Object.class);
            return result2;
        } catch (NullPointerException e) {
            throw new UnsupportedCompilationException("evaluation resulted in null expression.");
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
            if (!OgnlOps.booleanValue(this._children[0].getValue(context, target))) {
                throw new UnsupportedCompilationException("And expression can't be compiled until all conditions are true.");
            }
            String first = ExpressionCompiler.getRootExpression(this._children[0], context.getRoot(), context) + pre + this._children[0].toGetSourceString(context, target);
            this._children[1].getValue(context, target);
            String second = ExpressionCompiler.getRootExpression(this._children[1], context.getRoot(), context) + pre + this._children[1].toSetSourceString(context, target);
            String result = !OgnlRuntime.isBoolean(first) ? "if(ognl.OgnlOps.booleanValue(" + first + ")){" : "if(" + first + "){";
            String result2 = (result + second) + "; } ";
            context.setCurrentObject(target);
            context.setCurrentType(Object.class);
            return result2;
        } catch (Throwable t) {
            throw OgnlOps.castToRuntime(t);
        }
    }
}
