package org.apache.ibatis.ognl;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ASTNegate.class */
class ASTNegate extends NumericExpression {
    public ASTNegate(int id) {
        super(id);
    }

    public ASTNegate(OgnlParser p, int id) {
        super(p, id);
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
        return OgnlOps.negate(this._children[0].getValue(context, source));
    }

    @Override // org.apache.ibatis.ognl.ExpressionNode, org.apache.ibatis.ognl.SimpleNode
    public String toString() {
        return "-" + this._children[0];
    }

    @Override // org.apache.ibatis.ognl.NumericExpression, org.apache.ibatis.ognl.ExpressionNode, org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toGetSourceString(OgnlContext context, Object target) {
        String source = this._children[0].toGetSourceString(context, target);
        if (!ASTNegate.class.isInstance(this._children[0])) {
            return "-" + source;
        }
        return "-(" + source + ")";
    }

    @Override // org.apache.ibatis.ognl.ExpressionNode, org.apache.ibatis.ognl.SimpleNode
    public boolean isOperation(OgnlContext context) throws OgnlException {
        if (this._children.length == 1) {
            SimpleNode child = (SimpleNode) this._children[0];
            return child.isOperation(context) || !child.isConstant(context);
        }
        return super.isOperation(context);
    }
}
