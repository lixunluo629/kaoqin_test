package org.apache.ibatis.ognl;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.apache.ibatis.ognl.enhance.ExpressionCompiler;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/NumericExpression.class */
public abstract class NumericExpression extends ExpressionNode implements NodeType {
    protected Class _getterClass;

    public NumericExpression(int id) {
        super(id);
    }

    public NumericExpression(OgnlParser p, int id) {
        super(p, id);
    }

    @Override // org.apache.ibatis.ognl.NodeType
    public Class getGetterClass() {
        if (this._getterClass != null) {
            return this._getterClass;
        }
        return Double.TYPE;
    }

    @Override // org.apache.ibatis.ognl.NodeType
    public Class getSetterClass() {
        return null;
    }

    @Override // org.apache.ibatis.ognl.ExpressionNode, org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toGetSourceString(OgnlContext context, Object target) {
        String result = "";
        try {
            Object value = getValueBody(context, target);
            if (value != null) {
                this._getterClass = value.getClass();
            }
            for (int i = 0; i < this._children.length; i++) {
                if (i > 0) {
                    result = result + SymbolConstants.SPACE_SYMBOL + getExpressionOperator(i) + SymbolConstants.SPACE_SYMBOL;
                }
                String str = OgnlRuntime.getChildSource(context, target, this._children[i]);
                result = result + coerceToNumeric(str, context, this._children[i]);
            }
            return result;
        } catch (Throwable t) {
            throw OgnlOps.castToRuntime(t);
        }
    }

    public String coerceToNumeric(String source, OgnlContext context, Node child) {
        String ret = source;
        Object value = context.getCurrentObject();
        if (ASTConst.class.isInstance(child) && value != null) {
            return value.toString();
        }
        if (context.getCurrentType() != null && !context.getCurrentType().isPrimitive() && context.getCurrentObject() != null && Number.class.isInstance(context.getCurrentObject())) {
            ret = ("((" + ExpressionCompiler.getCastString(context.getCurrentObject().getClass()) + ")" + ret + ")") + "." + OgnlRuntime.getNumericValueGetter(context.getCurrentObject().getClass());
        } else if (context.getCurrentType() != null && context.getCurrentType().isPrimitive() && (ASTConst.class.isInstance(child) || NumericExpression.class.isInstance(child))) {
            ret = ret + OgnlRuntime.getNumericLiteral(context.getCurrentType());
        } else if (context.getCurrentType() != null && String.class.isAssignableFrom(context.getCurrentType())) {
            ret = "Double.parseDouble(" + ret + ")";
            context.setCurrentType(Double.TYPE);
        }
        if (NumericExpression.class.isInstance(child)) {
            ret = "(" + ret + ")";
        }
        return ret;
    }
}
