package org.apache.ibatis.ognl;

import org.springframework.aop.framework.autoproxy.target.QuickTargetSourceCreator;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ASTRemainder.class */
class ASTRemainder extends NumericExpression {
    public ASTRemainder(int id) {
        super(id);
    }

    public ASTRemainder(OgnlParser p, int id) {
        super(p, id);
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
        Object v1 = this._children[0].getValue(context, source);
        Object v2 = this._children[1].getValue(context, source);
        return OgnlOps.remainder(v1, v2);
    }

    @Override // org.apache.ibatis.ognl.ExpressionNode
    public String getExpressionOperator(int index) {
        return QuickTargetSourceCreator.PREFIX_THREAD_LOCAL;
    }
}
