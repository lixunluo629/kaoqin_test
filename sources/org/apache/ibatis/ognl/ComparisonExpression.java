package org.apache.ibatis.ognl;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.apache.ibatis.ognl.enhance.UnsupportedCompilationException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ComparisonExpression.class */
public abstract class ComparisonExpression extends BooleanExpression {
    public abstract String getComparisonFunction();

    public ComparisonExpression(int id) {
        super(id);
    }

    public ComparisonExpression(OgnlParser p, int id) {
        super(p, id);
    }

    @Override // org.apache.ibatis.ognl.BooleanExpression, org.apache.ibatis.ognl.ExpressionNode, org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toGetSourceString(OgnlContext context, Object target) {
        if (target == null) {
            throw new UnsupportedCompilationException("Current target is null, can't compile.");
        }
        try {
            Object value = getValueBody(context, target);
            if ((value == null || !Boolean.class.isAssignableFrom(value.getClass())) && value != null) {
                this._getterClass = value.getClass();
            } else {
                this._getterClass = Boolean.TYPE;
            }
            OgnlRuntime.getChildSource(context, target, this._children[0]);
            OgnlRuntime.getChildSource(context, target, this._children[1]);
            boolean conversion = OgnlRuntime.shouldConvertNumericTypes(context);
            String result = conversion ? "(" + getComparisonFunction() + "( ($w) (" : "(";
            String result2 = (result + OgnlRuntime.getChildSource(context, target, this._children[0], conversion) + SymbolConstants.SPACE_SYMBOL + (conversion ? "), ($w) " : getExpressionOperator(0)) + SymbolConstants.SPACE_SYMBOL + OgnlRuntime.getChildSource(context, target, this._children[1], conversion)) + (conversion ? ")" : "");
            context.setCurrentType(Boolean.TYPE);
            return result2 + ")";
        } catch (NullPointerException e) {
            throw new UnsupportedCompilationException("evaluation resulted in null expression.");
        } catch (Throwable t) {
            throw OgnlOps.castToRuntime(t);
        }
    }
}
