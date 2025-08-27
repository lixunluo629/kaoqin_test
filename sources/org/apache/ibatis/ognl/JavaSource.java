package org.apache.ibatis.ognl;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/JavaSource.class */
public interface JavaSource {
    String toGetSourceString(OgnlContext ognlContext, Object obj);

    String toSetSourceString(OgnlContext ognlContext, Object obj);
}
