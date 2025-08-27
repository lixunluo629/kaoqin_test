package org.apache.ibatis.ognl.enhance;

import org.apache.ibatis.ognl.Node;
import org.apache.ibatis.ognl.OgnlContext;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/enhance/ExpressionAccessor.class */
public interface ExpressionAccessor {
    Object get(OgnlContext ognlContext, Object obj);

    void set(OgnlContext ognlContext, Object obj, Object obj2);

    void setExpression(Node node);
}
