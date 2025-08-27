package org.apache.ibatis.ognl;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ASTBitAnd.class */
class ASTBitAnd extends NumericExpression {
    public ASTBitAnd(int id) {
        super(id);
    }

    public ASTBitAnd(OgnlParser p, int id) {
        super(p, id);
    }

    @Override // org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.Node
    public void jjtClose() {
        flattenTree();
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
        Object result = this._children[0].getValue(context, source);
        for (int i = 1; i < this._children.length; i++) {
            result = OgnlOps.binaryAnd(result, this._children[i].getValue(context, source));
        }
        return result;
    }

    @Override // org.apache.ibatis.ognl.ExpressionNode
    public String getExpressionOperator(int index) {
        return "&";
    }

    @Override // org.apache.ibatis.ognl.NumericExpression
    public String coerceToNumeric(String source, OgnlContext context, Node child) {
        return "(long)" + super.coerceToNumeric(source, context, child);
    }
}
