package redis.clients.jedis;

import java.net.URI;
import java.util.concurrent.atomic.AtomicReference;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import redis.clients.jedis.exceptions.InvalidURIException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.JedisURIHelper;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/JedisFactory.class */
class JedisFactory implements PooledObjectFactory<Jedis> {
    private final AtomicReference<HostAndPort> hostAndPort = new AtomicReference<>();
    private final int connectionTimeout;
    private final int soTimeout;
    private final String password;
    private final int database;
    private final String clientName;
    private final boolean ssl;
    private final SSLSocketFactory sslSocketFactory;
    private SSLParameters sslParameters;
    private HostnameVerifier hostnameVerifier;

    public JedisFactory(String host, int port, int connectionTimeout, int soTimeout, String password, int database, String clientName, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        this.hostAndPort.set(new HostAndPort(host, port));
        this.connectionTimeout = connectionTimeout;
        this.soTimeout = soTimeout;
        this.password = password;
        this.database = database;
        this.clientName = clientName;
        this.ssl = ssl;
        this.sslSocketFactory = sslSocketFactory;
        this.sslParameters = sslParameters;
        this.hostnameVerifier = hostnameVerifier;
    }

    public JedisFactory(URI uri, int connectionTimeout, int soTimeout, String clientName, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        if (!JedisURIHelper.isValid(uri)) {
            throw new InvalidURIException(String.format("Cannot open Redis connection due invalid URI. %s", uri.toString()));
        }
        this.hostAndPort.set(new HostAndPort(uri.getHost(), uri.getPort()));
        this.connectionTimeout = connectionTimeout;
        this.soTimeout = soTimeout;
        this.password = JedisURIHelper.getPassword(uri);
        this.database = JedisURIHelper.getDBIndex(uri);
        this.clientName = clientName;
        this.ssl = ssl;
        this.sslSocketFactory = sslSocketFactory;
        this.sslParameters = sslParameters;
        this.hostnameVerifier = hostnameVerifier;
    }

    public void setHostAndPort(HostAndPort hostAndPort) {
        this.hostAndPort.set(hostAndPort);
    }

    @Override // org.apache.commons.pool2.PooledObjectFactory
    public void activateObject(PooledObject<Jedis> pooledJedis) throws Exception {
        BinaryJedis jedis = pooledJedis.getObject();
        if (jedis.getDB().longValue() != this.database) {
            jedis.select(this.database);
        }
    }

    @Override // org.apache.commons.pool2.PooledObjectFactory
    public void destroyObject(PooledObject<Jedis> pooledJedis) throws Exception {
        BinaryJedis jedis = pooledJedis.getObject();
        if (jedis.isConnected()) {
            try {
                try {
                    jedis.quit();
                } catch (Exception e) {
                    return;
                }
            } catch (Exception e2) {
            }
            jedis.disconnect();
        }
    }

    @Override // org.apache.commons.pool2.PooledObjectFactory
    public PooledObject<Jedis> makeObject() throws Exception {
        HostAndPort hostAndPort = this.hostAndPort.get();
        Jedis jedis = new Jedis(hostAndPort.getHost(), hostAndPort.getPort(), this.connectionTimeout, this.soTimeout, this.ssl, this.sslSocketFactory, this.sslParameters, this.hostnameVerifier);
        try {
            jedis.connect();
            if (this.password != null) {
                jedis.auth(this.password);
            }
            if (this.database != 0) {
                jedis.select(this.database);
            }
            if (this.clientName != null) {
                jedis.clientSetname(this.clientName);
            }
            return new DefaultPooledObject(jedis);
        } catch (JedisException je) {
            jedis.close();
            throw je;
        }
    }

    @Override // org.apache.commons.pool2.PooledObjectFactory
    public void passivateObject(PooledObject<Jedis> pooledJedis) throws Exception {
    }

    @Override // org.apache.commons.pool2.PooledObjectFactory
    public boolean validateObject(PooledObject<Jedis> pooledJedis) {
        BinaryJedis jedis = pooledJedis.getObject();
        try {
            HostAndPort hostAndPort = this.hostAndPort.get();
            String connectionHost = jedis.getClient().getHost();
            int connectionPort = jedis.getClient().getPort();
            if (hostAndPort.getHost().equals(connectionHost) && hostAndPort.getPort() == connectionPort && jedis.isConnected()) {
                if (jedis.ping().equals(LettuceConnectionFactory.PING_REPLY)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
