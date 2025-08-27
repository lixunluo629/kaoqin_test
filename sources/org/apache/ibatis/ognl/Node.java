package org.apache.ibatis.ognl;

import org.apache.ibatis.ognl.enhance.ExpressionAccessor;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/Node.class */
public interface Node extends JavaSource {
    void jjtOpen();

    void jjtClose();

    void jjtSetParent(Node node);

    Node jjtGetParent();

    void jjtAddChild(Node node, int i);

    Node jjtGetChild(int i);

    int jjtGetNumChildren();

    Object getValue(OgnlContext ognlContext, Object obj) throws OgnlException;

    void setValue(OgnlContext ognlContext, Object obj, Object obj2) throws OgnlException;

    ExpressionAccessor getAccessor();

    void setAccessor(ExpressionAccessor expressionAccessor);
}
