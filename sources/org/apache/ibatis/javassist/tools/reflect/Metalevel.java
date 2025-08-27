package org.apache.ibatis.javassist.tools.reflect;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/tools/reflect/Metalevel.class */
public interface Metalevel {
    ClassMetaobject _getClass();

    Metaobject _getMetaobject();

    void _setMetaobject(Metaobject metaobject);
}
