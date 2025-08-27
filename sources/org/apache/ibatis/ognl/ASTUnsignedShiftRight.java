package org.apache.ibatis.ognl;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ASTUnsignedShiftRight.class */
class ASTUnsignedShiftRight extends NumericExpression {
    public ASTUnsignedShiftRight(int id) {
        super(id);
    }

    public ASTUnsignedShiftRight(OgnlParser p, int id) {
        super(p, id);
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
        Object v1 = this._children[0].getValue(context, source);
        Object v2 = this._children[1].getValue(context, source);
        return OgnlOps.unsignedShiftRight(v1, v2);
    }

    @Override // org.apache.ibatis.ognl.ExpressionNode
    public String getExpressionOperator(int index) {
        return ">>>";
    }

    @Override // org.apache.ibatis.ognl.NumericExpression, org.apache.ibatis.ognl.ExpressionNode, org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toGetSourceString(OgnlContext context, Object target) {
        try {
            String child1 = coerceToNumeric(OgnlRuntime.getChildSource(context, target, this._children[0]), context, this._children[0]);
            String child2 = coerceToNumeric(OgnlRuntime.getChildSource(context, target, this._children[1]), context, this._children[1]);
            Object v1 = this._children[0].getValue(context, target);
            int type = OgnlOps.getNumericType(v1);
            if (type <= 4) {
                child1 = "(int)" + child1;
                child2 = "(int)" + child2;
            }
            String result = child1 + " >>> " + child2;
            context.setCurrentType(Integer.TYPE);
            context.setCurrentObject(getValueBody(context, target));
            return result;
        } catch (Throwable t) {
            throw OgnlOps.castToRuntime(t);
        }
    }
}
