package javax.el;

import java.lang.reflect.Method;

/* loaded from: tomcat-embed-el-8.5.43.jar:javax/el/FunctionMapper.class */
public abstract class FunctionMapper {
    public abstract Method resolveFunction(String str, String str2);

    public void mapFunction(String prefix, String localName, Method method) {
    }
}
