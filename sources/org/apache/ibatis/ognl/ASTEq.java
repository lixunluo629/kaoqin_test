package org.apache.ibatis.ognl;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ASTEq.class */
class ASTEq extends ComparisonExpression {
    public ASTEq(int id) {
        super(id);
    }

    public ASTEq(OgnlParser p, int id) {
        super(p, id);
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
        Object v1 = this._children[0].getValue(context, source);
        Object v2 = this._children[1].getValue(context, source);
        return OgnlOps.equal(v1, v2) ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override // org.apache.ibatis.ognl.ExpressionNode
    public String getExpressionOperator(int index) {
        return "==";
    }

    @Override // org.apache.ibatis.ognl.ComparisonExpression
    public String getComparisonFunction() {
        return "org.apache.ibatis.ognl.OgnlOps.equal";
    }
}
