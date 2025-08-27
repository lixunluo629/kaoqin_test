package org.apache.ibatis.ognl;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/InappropriateExpressionException.class */
public class InappropriateExpressionException extends OgnlException {
    public InappropriateExpressionException(Node tree) {
        super("Inappropriate OGNL expression: " + tree);
    }
}
