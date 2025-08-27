package org.apache.ibatis.javassist.tools.reflect;

import org.apache.ibatis.javassist.CannotCompileException;
import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.NotFoundException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/tools/reflect/Loader.class */
public class Loader extends org.apache.ibatis.javassist.Loader {
    protected Reflection reflection;

    public static void main(String[] args) throws Throwable {
        Loader cl = new Loader();
        cl.run(args);
    }

    public Loader() throws NotFoundException, CannotCompileException {
        delegateLoadingOf("org.apache.ibatis.javassist.tools.reflect.Loader");
        this.reflection = new Reflection();
        ClassPool pool = ClassPool.getDefault();
        addTranslator(pool, this.reflection);
    }

    public boolean makeReflective(String clazz, String metaobject, String metaclass) throws NotFoundException, CannotCompileException {
        return this.reflection.makeReflective(clazz, metaobject, metaclass);
    }
}
