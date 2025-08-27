package org.apache.ibatis.ognl;

import org.apache.ibatis.ognl.enhance.UnsupportedCompilationException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ASTTest.class */
class ASTTest extends ExpressionNode {
    public ASTTest(int id) {
        super(id);
    }

    public ASTTest(OgnlParser p, int id) {
        super(p, id);
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
        Object test = this._children[0].getValue(context, source);
        int branch = OgnlOps.booleanValue(test) ? 1 : 2;
        return this._children[branch].getValue(context, source);
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    protected void setValueBody(OgnlContext context, Object target, Object value) throws OgnlException {
        Object test = this._children[0].getValue(context, target);
        int branch = OgnlOps.booleanValue(test) ? 1 : 2;
        this._children[branch].setValue(context, target, value);
    }

    @Override // org.apache.ibatis.ognl.ExpressionNode
    public String getExpressionOperator(int index) {
        return index == 1 ? "?" : ":";
    }

    @Override // org.apache.ibatis.ognl.ExpressionNode, org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toGetSourceString(OgnlContext context, Object target) {
        if (target == null) {
            throw new UnsupportedCompilationException("evaluation resulted in null expression.");
        }
        if (this._children.length != 3) {
            throw new UnsupportedCompilationException("Can only compile test expressions with two children." + this._children.length);
        }
        try {
            String first = OgnlRuntime.getChildSource(context, target, this._children[0]);
            if (!OgnlRuntime.isBoolean(first) && !context.getCurrentType().isPrimitive()) {
                first = OgnlRuntime.getCompiler().createLocalReference(context, first, context.getCurrentType());
            }
            if (ExpressionNode.class.isInstance(this._children[0])) {
                first = "(" + first + ")";
            }
            String second = OgnlRuntime.getChildSource(context, target, this._children[1]);
            Class secondType = context.getCurrentType();
            if (!OgnlRuntime.isBoolean(second) && !context.getCurrentType().isPrimitive()) {
                second = OgnlRuntime.getCompiler().createLocalReference(context, second, context.getCurrentType());
            }
            if (ExpressionNode.class.isInstance(this._children[1])) {
                second = "(" + second + ")";
            }
            String third = OgnlRuntime.getChildSource(context, target, this._children[2]);
            Class thirdType = context.getCurrentType();
            if (!OgnlRuntime.isBoolean(third) && !context.getCurrentType().isPrimitive()) {
                third = OgnlRuntime.getCompiler().createLocalReference(context, third, context.getCurrentType());
            }
            if (ExpressionNode.class.isInstance(this._children[2])) {
                third = "(" + third + ")";
            }
            boolean mismatched = (secondType.isPrimitive() && !thirdType.isPrimitive()) || (!secondType.isPrimitive() && thirdType.isPrimitive());
            String result = "org.apache.ibatis.ognl.OgnlOps.booleanValue(" + first + ")";
            String result2 = (((result + " ? ") + (mismatched ? " ($w) " : "") + second) + " : ") + (mismatched ? " ($w) " : "") + third;
            context.setCurrentObject(target);
            context.setCurrentType(mismatched ? Object.class : secondType);
            return result2;
        } catch (NullPointerException e) {
            throw new UnsupportedCompilationException("evaluation resulted in null expression.");
        } catch (Throwable t) {
            throw OgnlOps.castToRuntime(t);
        }
    }
}
