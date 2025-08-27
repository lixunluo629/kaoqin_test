package org.apache.ibatis.ognl;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ASTNot.class */
class ASTNot extends BooleanExpression {
    public ASTNot(int id) {
        super(id);
    }

    public ASTNot(OgnlParser p, int id) {
        super(p, id);
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
        return OgnlOps.booleanValue(this._children[0].getValue(context, source)) ? Boolean.FALSE : Boolean.TRUE;
    }

    @Override // org.apache.ibatis.ognl.ExpressionNode
    public String getExpressionOperator(int index) {
        return "!";
    }

    @Override // org.apache.ibatis.ognl.BooleanExpression, org.apache.ibatis.ognl.ExpressionNode, org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toGetSourceString(OgnlContext context, Object target) {
        try {
            String srcString = super.toGetSourceString(context, target);
            if (srcString == null || srcString.trim().length() < 1) {
                srcString = "null";
            }
            context.setCurrentType(Boolean.TYPE);
            return "(! ognl.OgnlOps.booleanValue(" + srcString + ") )";
        } catch (Throwable t) {
            throw OgnlOps.castToRuntime(t);
        }
    }
}
