package org.apache.ibatis.ognl;

import org.apache.ibatis.ognl.enhance.UnsupportedCompilationException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/BooleanExpression.class */
public abstract class BooleanExpression extends ExpressionNode implements NodeType {
    protected Class _getterClass;

    public BooleanExpression(int id) {
        super(id);
    }

    public BooleanExpression(OgnlParser p, int id) {
        super(p, id);
    }

    public Class getGetterClass() {
        return this._getterClass;
    }

    @Override // org.apache.ibatis.ognl.NodeType
    public Class getSetterClass() {
        return null;
    }

    @Override // org.apache.ibatis.ognl.ExpressionNode, org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toGetSourceString(OgnlContext context, Object target) {
        try {
            Object value = getValueBody(context, target);
            if ((value == null || !Boolean.class.isAssignableFrom(value.getClass())) && value != null) {
                this._getterClass = value.getClass();
            } else {
                this._getterClass = Boolean.TYPE;
            }
            String ret = super.toGetSourceString(context, target);
            if ("(false)".equals(ret)) {
                return "false";
            }
            if ("(true)".equals(ret)) {
                return "true";
            }
            return ret;
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new UnsupportedCompilationException("evaluation resulted in null expression.");
        } catch (Throwable t) {
            throw OgnlOps.castToRuntime(t);
        }
    }
}
