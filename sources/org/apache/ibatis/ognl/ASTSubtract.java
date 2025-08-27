package org.apache.ibatis.ognl;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ASTSubtract.class */
class ASTSubtract extends NumericExpression {
    public ASTSubtract(int id) {
        super(id);
    }

    public ASTSubtract(OgnlParser p, int id) {
        super(p, id);
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
        Object v1 = this._children[0].getValue(context, source);
        Object v2 = this._children[1].getValue(context, source);
        return OgnlOps.subtract(v1, v2);
    }

    @Override // org.apache.ibatis.ognl.ExpressionNode
    public String getExpressionOperator(int index) {
        return "-";
    }
}
