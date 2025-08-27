package org.apache.ibatis.ognl.enhance;

import org.apache.ibatis.ognl.OgnlContext;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/enhance/ContextClassLoader.class */
public class ContextClassLoader extends ClassLoader {
    private OgnlContext context;

    public ContextClassLoader(ClassLoader parentClassLoader, OgnlContext context) {
        super(parentClassLoader);
        this.context = context;
    }

    @Override // java.lang.ClassLoader
    protected Class findClass(String name) throws ClassNotFoundException {
        if (this.context != null && this.context.getClassResolver() != null) {
            return this.context.getClassResolver().classForName(name, this.context);
        }
        return super.findClass(name);
    }
}
