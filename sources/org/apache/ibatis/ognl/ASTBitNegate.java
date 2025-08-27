package org.apache.ibatis.ognl;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ASTBitNegate.class */
class ASTBitNegate extends NumericExpression {
    public ASTBitNegate(int id) {
        super(id);
    }

    public ASTBitNegate(OgnlParser p, int id) {
        super(p, id);
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
        return OgnlOps.bitNegate(this._children[0].getValue(context, source));
    }

    @Override // org.apache.ibatis.ognl.ExpressionNode, org.apache.ibatis.ognl.SimpleNode
    public String toString() {
        return "~" + this._children[0];
    }

    @Override // org.apache.ibatis.ognl.NumericExpression, org.apache.ibatis.ognl.ExpressionNode, org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toGetSourceString(OgnlContext context, Object target) {
        String source = this._children[0].toGetSourceString(context, target);
        if (!ASTBitNegate.class.isInstance(this._children[0])) {
            return "~(" + super.coerceToNumeric(source, context, this._children[0]) + ")";
        }
        return "~(" + source + ")";
    }
}
