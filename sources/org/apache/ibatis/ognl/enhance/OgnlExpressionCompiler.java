package org.apache.ibatis.ognl.enhance;

import java.lang.reflect.Method;
import org.apache.ibatis.ognl.Node;
import org.apache.ibatis.ognl.OgnlContext;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/enhance/OgnlExpressionCompiler.class */
public interface OgnlExpressionCompiler {
    public static final String ROOT_TYPE = "-ognl-root-type";

    void compileExpression(OgnlContext ognlContext, Node node, Object obj) throws Exception;

    String getClassName(Class cls);

    Class getInterfaceClass(Class cls);

    Class getSuperOrInterfaceClass(Method method, Class cls);

    Class getRootExpressionClass(Node node, OgnlContext ognlContext);

    String castExpression(OgnlContext ognlContext, Node node, String str);

    String createLocalReference(OgnlContext ognlContext, String str, Class cls);
}
