package org.apache.ibatis.ognl;

import java.util.HashMap;
import java.util.Map;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/DefaultClassResolver.class */
public class DefaultClassResolver implements ClassResolver {
    private Map classes = new HashMap(101);

    @Override // org.apache.ibatis.ognl.ClassResolver
    public Class classForName(String className, Map context) throws ClassNotFoundException {
        Class cls = (Class) this.classes.get(className);
        Class result = cls;
        if (cls == null) {
            try {
                result = Class.forName(className);
            } catch (ClassNotFoundException e) {
                if (className.indexOf(46) == -1) {
                    result = Class.forName("java.lang." + className);
                    this.classes.put("java.lang." + className, result);
                }
            }
            this.classes.put(className, result);
        }
        return result;
    }
}
