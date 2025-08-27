package redis.clients.jedis;

import java.net.URI;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import redis.clients.jedis.exceptions.InvalidURIException;
import redis.clients.util.JedisURIHelper;
import redis.clients.util.ShardInfo;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/JedisShardInfo.class */
public class JedisShardInfo extends ShardInfo<Jedis> {
    private int connectionTimeout;
    private int soTimeout;
    private String host;
    private int port;
    private String password;
    private String name;
    private int db;
    private boolean ssl;
    private SSLSocketFactory sslSocketFactory;
    private SSLParameters sslParameters;
    private HostnameVerifier hostnameVerifier;

    public JedisShardInfo(String host) {
        super(1);
        this.password = null;
        this.name = null;
        this.db = 0;
        URI uri = URI.create(host);
        if (JedisURIHelper.isValid(uri)) {
            this.host = uri.getHost();
            this.port = uri.getPort();
            this.password = JedisURIHelper.getPassword(uri);
            this.db = JedisURIHelper.getDBIndex(uri);
            this.ssl = uri.getScheme().equals("rediss");
            return;
        }
        this.host = host;
        this.port = Protocol.DEFAULT_PORT;
    }

    public JedisShardInfo(String host, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(1);
        this.password = null;
        this.name = null;
        this.db = 0;
        URI uri = URI.create(host);
        if (JedisURIHelper.isValid(uri)) {
            this.host = uri.getHost();
            this.port = uri.getPort();
            this.password = JedisURIHelper.getPassword(uri);
            this.db = JedisURIHelper.getDBIndex(uri);
            this.ssl = uri.getScheme().equals("rediss");
            this.sslSocketFactory = sslSocketFactory;
            this.sslParameters = sslParameters;
            this.hostnameVerifier = hostnameVerifier;
            return;
        }
        this.host = host;
        this.port = Protocol.DEFAULT_PORT;
    }

    public JedisShardInfo(String host, String name) {
        this(host, Protocol.DEFAULT_PORT, name);
    }

    public JedisShardInfo(String host, int port) {
        this(host, port, 2000);
    }

    public JedisShardInfo(String host, int port, boolean ssl) {
        this(host, port, 2000, 2000, 1, ssl);
    }

    public JedisShardInfo(String host, int port, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        this(host, port, 2000, 2000, 1, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public JedisShardInfo(String host, int port, String name) {
        this(host, port, 2000, name);
    }

    public JedisShardInfo(String host, int port, String name, boolean ssl) {
        this(host, port, 2000, name, ssl);
    }

    public JedisShardInfo(String host, int port, String name, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        this(host, port, 2000, name, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public JedisShardInfo(String host, int port, int timeout) {
        this(host, port, timeout, timeout, 1);
    }

    public JedisShardInfo(String host, int port, int timeout, boolean ssl) {
        this(host, port, timeout, timeout, 1, ssl);
    }

    public JedisShardInfo(String host, int port, int timeout, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        this(host, port, timeout, timeout, 1, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public JedisShardInfo(String host, int port, int timeout, String name) {
        this(host, port, timeout, timeout, 1);
        this.name = name;
    }

    public JedisShardInfo(String host, int port, int timeout, String name, boolean ssl) {
        this(host, port, timeout, timeout, 1);
        this.name = name;
        this.ssl = ssl;
    }

    public JedisShardInfo(String host, int port, int timeout, String name, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        this(host, port, timeout, timeout, 1);
        this.name = name;
        this.ssl = ssl;
        this.sslSocketFactory = sslSocketFactory;
        this.sslParameters = sslParameters;
        this.hostnameVerifier = hostnameVerifier;
    }

    public JedisShardInfo(String host, int port, int connectionTimeout, int soTimeout, int weight) {
        super(weight);
        this.password = null;
        this.name = null;
        this.db = 0;
        this.host = host;
        this.port = port;
        this.connectionTimeout = connectionTimeout;
        this.soTimeout = soTimeout;
    }

    public JedisShardInfo(String host, int port, int connectionTimeout, int soTimeout, int weight, boolean ssl) {
        super(weight);
        this.password = null;
        this.name = null;
        this.db = 0;
        this.host = host;
        this.port = port;
        this.connectionTimeout = connectionTimeout;
        this.soTimeout = soTimeout;
        this.ssl = ssl;
    }

    public JedisShardInfo(String host, int port, int connectionTimeout, int soTimeout, int weight, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(weight);
        this.password = null;
        this.name = null;
        this.db = 0;
        this.host = host;
        this.port = port;
        this.connectionTimeout = connectionTimeout;
        this.soTimeout = soTimeout;
        this.ssl = ssl;
        this.sslSocketFactory = sslSocketFactory;
        this.sslParameters = sslParameters;
        this.hostnameVerifier = hostnameVerifier;
    }

    public JedisShardInfo(String host, String name, int port, int timeout, int weight) {
        super(weight);
        this.password = null;
        this.name = null;
        this.db = 0;
        this.host = host;
        this.name = name;
        this.port = port;
        this.connectionTimeout = timeout;
        this.soTimeout = timeout;
    }

    public JedisShardInfo(String host, String name, int port, int timeout, int weight, boolean ssl) {
        super(weight);
        this.password = null;
        this.name = null;
        this.db = 0;
        this.host = host;
        this.name = name;
        this.port = port;
        this.connectionTimeout = timeout;
        this.soTimeout = timeout;
        this.ssl = ssl;
    }

    public JedisShardInfo(String host, String name, int port, int timeout, int weight, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(weight);
        this.password = null;
        this.name = null;
        this.db = 0;
        this.host = host;
        this.name = name;
        this.port = port;
        this.connectionTimeout = timeout;
        this.soTimeout = timeout;
        this.ssl = ssl;
        this.sslSocketFactory = sslSocketFactory;
        this.sslParameters = sslParameters;
        this.hostnameVerifier = hostnameVerifier;
    }

    public JedisShardInfo(URI uri) {
        super(1);
        this.password = null;
        this.name = null;
        this.db = 0;
        if (!JedisURIHelper.isValid(uri)) {
            throw new InvalidURIException(String.format("Cannot open Redis connection due invalid URI. %s", uri.toString()));
        }
        this.host = uri.getHost();
        this.port = uri.getPort();
        this.password = JedisURIHelper.getPassword(uri);
        this.db = JedisURIHelper.getDBIndex(uri);
        this.ssl = uri.getScheme().equals("rediss");
    }

    public JedisShardInfo(URI uri, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(1);
        this.password = null;
        this.name = null;
        this.db = 0;
        if (!JedisURIHelper.isValid(uri)) {
            throw new InvalidURIException(String.format("Cannot open Redis connection due invalid URI. %s", uri.toString()));
        }
        this.host = uri.getHost();
        this.port = uri.getPort();
        this.password = JedisURIHelper.getPassword(uri);
        this.db = JedisURIHelper.getDBIndex(uri);
        this.ssl = uri.getScheme().equals("rediss");
        this.sslSocketFactory = sslSocketFactory;
        this.sslParameters = sslParameters;
        this.hostnameVerifier = hostnameVerifier;
    }

    public String toString() {
        return this.host + ":" + this.port + "*" + getWeight();
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String auth) {
        this.password = auth;
    }

    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getSoTimeout() {
        return this.soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    @Override // redis.clients.util.ShardInfo
    public String getName() {
        return this.name;
    }

    public int getDb() {
        return this.db;
    }

    public boolean getSsl() {
        return this.ssl;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return this.sslSocketFactory;
    }

    public SSLParameters getSslParameters() {
        return this.sslParameters;
    }

    public HostnameVerifier getHostnameVerifier() {
        return this.hostnameVerifier;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // redis.clients.util.ShardInfo
    public Jedis createResource() {
        return new Jedis(this);
    }
}
