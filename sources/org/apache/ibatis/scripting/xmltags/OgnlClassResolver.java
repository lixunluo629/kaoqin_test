package org.apache.ibatis.scripting.xmltags;

import java.util.HashMap;
import java.util.Map;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.ognl.ClassResolver;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/xmltags/OgnlClassResolver.class */
public class OgnlClassResolver implements ClassResolver {
    private final Map<String, Class<?>> classes = new HashMap(101);

    @Override // org.apache.ibatis.ognl.ClassResolver
    public Class classForName(String className, Map context) throws ClassNotFoundException {
        Class<?> cls = this.classes.get(className);
        Class<?> result = cls;
        if (cls == null) {
            try {
                result = Resources.classForName(className);
            } catch (ClassNotFoundException e) {
                if (className.indexOf(46) == -1) {
                    result = Resources.classForName("java.lang." + className);
                    this.classes.put("java.lang." + className, result);
                }
            }
            this.classes.put(className, result);
        }
        return result;
    }
}
