package redis.clients.jedis;

import java.net.URI;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.JedisURIHelper;
import redis.clients.util.Pool;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/JedisPool.class */
public class JedisPool extends Pool<Jedis> {
    public JedisPool() {
        this("localhost", Protocol.DEFAULT_PORT);
    }

    public JedisPool(GenericObjectPoolConfig poolConfig, String host) {
        this(poolConfig, host, Protocol.DEFAULT_PORT, 2000, (String) null, 0, (String) null);
    }

    public JedisPool(String host, int port) {
        this(new GenericObjectPoolConfig(), host, port, 2000, (String) null, 0, (String) null);
    }

    public JedisPool(String host) {
        URI uri = URI.create(host);
        if (JedisURIHelper.isValid(uri)) {
            String h = uri.getHost();
            int port = uri.getPort();
            String password = JedisURIHelper.getPassword(uri);
            int database = JedisURIHelper.getDBIndex(uri);
            boolean ssl = uri.getScheme().equals("rediss");
            this.internalPool = new GenericObjectPool<>(new JedisFactory(h, port, 2000, 2000, password, database, null, ssl, null, null, null), new GenericObjectPoolConfig());
            return;
        }
        this.internalPool = new GenericObjectPool<>(new JedisFactory(host, Protocol.DEFAULT_PORT, 2000, 2000, null, 0, null, false, null, null, null), new GenericObjectPoolConfig());
    }

    public JedisPool(String host, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        URI uri = URI.create(host);
        if (JedisURIHelper.isValid(uri)) {
            String h = uri.getHost();
            int port = uri.getPort();
            String password = JedisURIHelper.getPassword(uri);
            int database = JedisURIHelper.getDBIndex(uri);
            boolean ssl = uri.getScheme().equals("rediss");
            this.internalPool = new GenericObjectPool<>(new JedisFactory(h, port, 2000, 2000, password, database, null, ssl, sslSocketFactory, sslParameters, hostnameVerifier), new GenericObjectPoolConfig());
            return;
        }
        this.internalPool = new GenericObjectPool<>(new JedisFactory(host, Protocol.DEFAULT_PORT, 2000, 2000, null, 0, null, false, null, null, null), new GenericObjectPoolConfig());
    }

    public JedisPool(URI uri) {
        this(new GenericObjectPoolConfig(), uri, 2000);
    }

    public JedisPool(URI uri, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        this(new GenericObjectPoolConfig(), uri, 2000, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public JedisPool(URI uri, int timeout) {
        this(new GenericObjectPoolConfig(), uri, timeout);
    }

    public JedisPool(URI uri, int timeout, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        this(new GenericObjectPoolConfig(), uri, timeout, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public JedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password) {
        this(poolConfig, host, port, timeout, password, 0, (String) null);
    }

    public JedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, boolean ssl) {
        this(poolConfig, host, port, timeout, password, 0, (String) null, ssl);
    }

    public JedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        this(poolConfig, host, port, timeout, password, 0, null, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public JedisPool(GenericObjectPoolConfig poolConfig, String host, int port) {
        this(poolConfig, host, port, 2000, (String) null, 0, (String) null);
    }

    public JedisPool(GenericObjectPoolConfig poolConfig, String host, int port, boolean ssl) {
        this(poolConfig, host, port, 2000, (String) null, 0, (String) null, ssl);
    }

    public JedisPool(GenericObjectPoolConfig poolConfig, String host, int port, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        this(poolConfig, host, port, 2000, null, 0, null, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public JedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout) {
        this(poolConfig, host, port, timeout, (String) null, 0, (String) null);
    }

    public JedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, boolean ssl) {
        this(poolConfig, host, port, timeout, (String) null, 0, (String) null, ssl);
    }

    public JedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        this(poolConfig, host, port, timeout, null, 0, null, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public JedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, int database) {
        this(poolConfig, host, port, timeout, password, database, (String) null);
    }

    public JedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, int database, boolean ssl) {
        this(poolConfig, host, port, timeout, password, database, (String) null, ssl);
    }

    public JedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, int database, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        this(poolConfig, host, port, timeout, password, database, null, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public JedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, int database, String clientName) {
        this(poolConfig, host, port, timeout, timeout, password, database, clientName, false, null, null, null);
    }

    public JedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, int database, String clientName, boolean ssl) {
        this(poolConfig, host, port, timeout, timeout, password, database, clientName, ssl, null, null, null);
    }

    public JedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, int database, String clientName, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        this(poolConfig, host, port, timeout, timeout, password, database, clientName, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public JedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int connectionTimeout, int soTimeout, String password, int database, String clientName, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(poolConfig, new JedisFactory(host, port, connectionTimeout, soTimeout, password, database, clientName, ssl, sslSocketFactory, sslParameters, hostnameVerifier));
    }

    public JedisPool(GenericObjectPoolConfig poolConfig, URI uri) {
        this(poolConfig, uri, 2000);
    }

    public JedisPool(GenericObjectPoolConfig poolConfig, URI uri, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        this(poolConfig, uri, 2000, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public JedisPool(GenericObjectPoolConfig poolConfig, URI uri, int timeout) {
        this(poolConfig, uri, timeout, timeout);
    }

    public JedisPool(GenericObjectPoolConfig poolConfig, URI uri, int timeout, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        this(poolConfig, uri, timeout, timeout, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public JedisPool(GenericObjectPoolConfig poolConfig, URI uri, int connectionTimeout, int soTimeout) {
        super(poolConfig, new JedisFactory(uri, connectionTimeout, soTimeout, null, false, null, null, null));
    }

    public JedisPool(GenericObjectPoolConfig poolConfig, URI uri, int connectionTimeout, int soTimeout, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(poolConfig, new JedisFactory(uri, connectionTimeout, soTimeout, null, uri.getScheme() != null && uri.getScheme().equals("rediss"), sslSocketFactory, sslParameters, hostnameVerifier));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // redis.clients.util.Pool
    public Jedis getResource() {
        Jedis jedis = (Jedis) super.getResource();
        jedis.setDataSource(this);
        return jedis;
    }

    @Override // redis.clients.util.Pool
    @Deprecated
    public void returnBrokenResource(Jedis resource) {
        if (resource != null) {
            returnBrokenResourceObject(resource);
        }
    }

    @Override // redis.clients.util.Pool
    @Deprecated
    public void returnResource(Jedis resource) {
        if (resource != null) {
            try {
                resource.resetState();
                returnResourceObject(resource);
            } catch (Exception e) {
                returnBrokenResource(resource);
                throw new JedisException("Resource is returned to the pool as broken", e);
            }
        }
    }
}
