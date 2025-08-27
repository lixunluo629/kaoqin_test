package org.springframework.http.client.support;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/client/support/ProxyFactoryBean.class */
public class ProxyFactoryBean implements FactoryBean<Proxy>, InitializingBean {
    private String hostname;
    private Proxy proxy;
    private Proxy.Type type = Proxy.Type.HTTP;
    private int port = -1;

    public void setType(Proxy.Type type) {
        this.type = type;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws IllegalArgumentException {
        Assert.notNull(this.type, "'type' must not be null");
        Assert.hasLength(this.hostname, "'hostname' must not be empty");
        if (this.port < 0 || this.port > 65535) {
            throw new IllegalArgumentException("'port' value out of range: " + this.port);
        }
        SocketAddress socketAddress = new InetSocketAddress(this.hostname, this.port);
        this.proxy = new Proxy(this.type, socketAddress);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.beans.factory.FactoryBean
    public Proxy getObject() {
        return this.proxy;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Class<?> getObjectType() {
        return Proxy.class;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public boolean isSingleton() {
        return true;
    }
}
