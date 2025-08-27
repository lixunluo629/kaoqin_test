package org.apache.ibatis.ognl;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ExpressionSyntaxException.class */
public class ExpressionSyntaxException extends OgnlException {
    public ExpressionSyntaxException(String expression, Throwable reason) {
        super("Malformed OGNL expression: " + expression, reason);
    }
}
