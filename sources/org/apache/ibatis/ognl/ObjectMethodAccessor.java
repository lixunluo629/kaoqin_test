package org.apache.ibatis.ognl;

import java.util.List;
import java.util.Map;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ObjectMethodAccessor.class */
public class ObjectMethodAccessor implements MethodAccessor {
    @Override // org.apache.ibatis.ognl.MethodAccessor
    public Object callStaticMethod(Map context, Class targetClass, String methodName, Object[] args) throws MethodFailedException {
        List methods = OgnlRuntime.getMethods(targetClass, methodName, true);
        return OgnlRuntime.callAppropriateMethod((OgnlContext) context, targetClass, null, methodName, null, methods, args);
    }

    @Override // org.apache.ibatis.ognl.MethodAccessor
    public Object callMethod(Map context, Object target, String methodName, Object[] args) throws MethodFailedException {
        Class targetClass = target == null ? null : target.getClass();
        List methods = OgnlRuntime.getMethods(targetClass, methodName, false);
        if (methods == null || methods.size() == 0) {
            methods = OgnlRuntime.getMethods(targetClass, methodName, true);
        }
        return OgnlRuntime.callAppropriateMethod((OgnlContext) context, target, target, methodName, null, methods, args);
    }
}
