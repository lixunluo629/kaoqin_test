package org.apache.ibatis.javassist.util.proxy;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/util/proxy/ProxyObject.class */
public interface ProxyObject extends Proxy {
    @Override // org.apache.ibatis.javassist.util.proxy.Proxy
    void setHandler(MethodHandler methodHandler);

    MethodHandler getHandler();
}
