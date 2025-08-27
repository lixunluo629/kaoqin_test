package org.springframework.data.redis.connection.lettuce;

import com.lambdaworks.redis.AbstractRedisClient;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisException;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.cluster.RedisClusterClient;
import com.lambdaworks.redis.resource.ClientResources;
import com.lambdaworks.redis.sentinel.api.StatefulRedisSentinelConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.redis.ExceptionTranslationStrategy;
import org.springframework.data.redis.PassThroughExceptionTranslationStrategy;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.ClusterCommandExecutor;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisSentinelConnection;
import org.springframework.data.redis.connection.lettuce.LettuceClusterConnection;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Protocol;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/lettuce/LettuceConnectionFactory.class */
public class LettuceConnectionFactory implements InitializingBean, DisposableBean, RedisConnectionFactory {
    public static final String PING_REPLY = "PONG";
    private static final ExceptionTranslationStrategy EXCEPTION_TRANSLATION = new PassThroughExceptionTranslationStrategy(LettuceConverters.exceptionConverter());
    private final Log log;
    private String hostName;
    private int port;
    private AbstractRedisClient client;
    private long timeout;
    private long shutdownTimeout;
    private boolean validateConnection;
    private boolean shareNativeConnection;
    private StatefulRedisConnection<byte[], byte[]> connection;
    private LettucePool pool;
    private int dbIndex;
    private final Object connectionMonitor;
    private String password;
    private boolean convertPipelineAndTxResults;
    private RedisSentinelConfiguration sentinelConfiguration;
    private RedisClusterConfiguration clusterConfiguration;
    private ClusterCommandExecutor clusterCommandExecutor;
    private ClientResources clientResources;
    private boolean useSsl;
    private boolean verifyPeer;
    private boolean startTls;

    public LettuceConnectionFactory() {
        this.log = LogFactory.getLog(getClass());
        this.hostName = "localhost";
        this.port = Protocol.DEFAULT_PORT;
        this.timeout = TimeUnit.MILLISECONDS.convert(60L, TimeUnit.SECONDS);
        this.shutdownTimeout = TimeUnit.MILLISECONDS.convert(2L, TimeUnit.SECONDS);
        this.validateConnection = false;
        this.shareNativeConnection = true;
        this.dbIndex = 0;
        this.connectionMonitor = new Object();
        this.convertPipelineAndTxResults = true;
        this.useSsl = false;
        this.verifyPeer = true;
        this.startTls = false;
    }

    public LettuceConnectionFactory(String host, int port) {
        this.log = LogFactory.getLog(getClass());
        this.hostName = "localhost";
        this.port = Protocol.DEFAULT_PORT;
        this.timeout = TimeUnit.MILLISECONDS.convert(60L, TimeUnit.SECONDS);
        this.shutdownTimeout = TimeUnit.MILLISECONDS.convert(2L, TimeUnit.SECONDS);
        this.validateConnection = false;
        this.shareNativeConnection = true;
        this.dbIndex = 0;
        this.connectionMonitor = new Object();
        this.convertPipelineAndTxResults = true;
        this.useSsl = false;
        this.verifyPeer = true;
        this.startTls = false;
        this.hostName = host;
        this.port = port;
    }

    public LettuceConnectionFactory(RedisSentinelConfiguration sentinelConfiguration) {
        this.log = LogFactory.getLog(getClass());
        this.hostName = "localhost";
        this.port = Protocol.DEFAULT_PORT;
        this.timeout = TimeUnit.MILLISECONDS.convert(60L, TimeUnit.SECONDS);
        this.shutdownTimeout = TimeUnit.MILLISECONDS.convert(2L, TimeUnit.SECONDS);
        this.validateConnection = false;
        this.shareNativeConnection = true;
        this.dbIndex = 0;
        this.connectionMonitor = new Object();
        this.convertPipelineAndTxResults = true;
        this.useSsl = false;
        this.verifyPeer = true;
        this.startTls = false;
        this.sentinelConfiguration = sentinelConfiguration;
    }

    public LettuceConnectionFactory(RedisClusterConfiguration clusterConfig) {
        this.log = LogFactory.getLog(getClass());
        this.hostName = "localhost";
        this.port = Protocol.DEFAULT_PORT;
        this.timeout = TimeUnit.MILLISECONDS.convert(60L, TimeUnit.SECONDS);
        this.shutdownTimeout = TimeUnit.MILLISECONDS.convert(2L, TimeUnit.SECONDS);
        this.validateConnection = false;
        this.shareNativeConnection = true;
        this.dbIndex = 0;
        this.connectionMonitor = new Object();
        this.convertPipelineAndTxResults = true;
        this.useSsl = false;
        this.verifyPeer = true;
        this.startTls = false;
        this.clusterConfiguration = clusterConfig;
    }

    public LettuceConnectionFactory(LettucePool pool) {
        this.log = LogFactory.getLog(getClass());
        this.hostName = "localhost";
        this.port = Protocol.DEFAULT_PORT;
        this.timeout = TimeUnit.MILLISECONDS.convert(60L, TimeUnit.SECONDS);
        this.shutdownTimeout = TimeUnit.MILLISECONDS.convert(2L, TimeUnit.SECONDS);
        this.validateConnection = false;
        this.shareNativeConnection = true;
        this.dbIndex = 0;
        this.connectionMonitor = new Object();
        this.convertPipelineAndTxResults = true;
        this.useSsl = false;
        this.verifyPeer = true;
        this.startTls = false;
        this.pool = pool;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        this.client = createRedisClient();
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() {
        resetConnection();
        try {
            this.client.shutdown(this.shutdownTimeout, this.shutdownTimeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            if (this.log.isWarnEnabled()) {
                this.log.warn((this.client != null ? ClassUtils.getShortName(this.client.getClass()) : "LettuceClient") + " did not shut down gracefully.", e);
            }
        }
        if (this.clusterCommandExecutor != null) {
            try {
                this.clusterCommandExecutor.destroy();
            } catch (Exception ex) {
                this.log.warn("Cannot properly close cluster command executor", ex);
            }
        }
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionFactory
    public RedisConnection getConnection() {
        if (isClusterAware()) {
            return getClusterConnection();
        }
        LettuceConnection connection = new LettuceConnection(getSharedConnection(), this.timeout, this.client, this.pool, this.dbIndex);
        connection.setConvertPipelineAndTxResults(this.convertPipelineAndTxResults);
        return connection;
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionFactory
    public RedisClusterConnection getClusterConnection() {
        if (!isClusterAware()) {
            throw new InvalidDataAccessApiUsageException("Cluster is not configured!");
        }
        return new LettuceClusterConnection(this.client, getTimeout(), this.clusterCommandExecutor);
    }

    public void initConnection() {
        synchronized (this.connectionMonitor) {
            if (this.connection != null) {
                resetConnection();
            }
            this.connection = createLettuceConnector();
        }
    }

    public void resetConnection() {
        synchronized (this.connectionMonitor) {
            if (this.connection != null) {
                this.connection.close();
            }
            this.connection = null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x003a A[Catch: all -> 0x004e, TryCatch #1 {, blocks: (B:4:0x0007, B:6:0x0015, B:11:0x003a, B:13:0x004a, B:8:0x002a), top: B:23:0x0007, inners: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void validateConnection() {
        /*
            r4 = this;
            r0 = r4
            java.lang.Object r0 = r0.connectionMonitor
            r1 = r0
            r5 = r1
            monitor-enter(r0)
            r0 = 0
            r6 = r0
            r0 = r4
            com.lambdaworks.redis.api.StatefulRedisConnection<byte[], byte[]> r0 = r0.connection     // Catch: java.lang.Throwable -> L4e
            boolean r0 = r0.isOpen()     // Catch: java.lang.Throwable -> L4e
            if (r0 == 0) goto L36
            r0 = r4
            com.lambdaworks.redis.api.StatefulRedisConnection<byte[], byte[]> r0 = r0.connection     // Catch: java.lang.Exception -> L29 java.lang.Throwable -> L4e
            com.lambdaworks.redis.api.sync.RedisCommands r0 = r0.sync()     // Catch: java.lang.Exception -> L29 java.lang.Throwable -> L4e
            java.lang.String r0 = r0.ping()     // Catch: java.lang.Exception -> L29 java.lang.Throwable -> L4e
            r0 = 1
            r6 = r0
            goto L36
        L29:
            r7 = move-exception
            r0 = r4
            org.apache.commons.logging.Log r0 = r0.log     // Catch: java.lang.Throwable -> L4e
            java.lang.String r1 = "Validation failed"
            r2 = r7
            r0.debug(r1, r2)     // Catch: java.lang.Throwable -> L4e
        L36:
            r0 = r6
            if (r0 != 0) goto L49
            r0 = r4
            org.apache.commons.logging.Log r0 = r0.log     // Catch: java.lang.Throwable -> L4e
            java.lang.String r1 = "Validation of shared connection failed. Creating a new connection."
            r0.warn(r1)     // Catch: java.lang.Throwable -> L4e
            r0 = r4
            r0.initConnection()     // Catch: java.lang.Throwable -> L4e
        L49:
            r0 = r5
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L4e
            goto L55
        L4e:
            r8 = move-exception
            r0 = r5
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L4e
            r0 = r8
            throw r0
        L55:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory.validateConnection():void");
    }

    @Override // org.springframework.dao.support.PersistenceExceptionTranslator
    public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
        return EXCEPTION_TRANSLATION.translate(ex);
    }

    public String getHostName() {
        return this.hostName;
    }

    public void setHostName(String host) {
        this.hostName = host;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public long getTimeout() {
        return this.timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public void setUseSsl(boolean useSsl) {
        this.useSsl = useSsl;
    }

    public boolean isUseSsl() {
        return this.useSsl;
    }

    public void setVerifyPeer(boolean verifyPeer) {
        this.verifyPeer = verifyPeer;
    }

    public boolean isVerifyPeer() {
        return this.verifyPeer;
    }

    public boolean isStartTls() {
        return this.startTls;
    }

    public void setStartTls(boolean startTls) {
        this.startTls = startTls;
    }

    public boolean getValidateConnection() {
        return this.validateConnection;
    }

    public void setValidateConnection(boolean validateConnection) {
        this.validateConnection = validateConnection;
    }

    public boolean getShareNativeConnection() {
        return this.shareNativeConnection;
    }

    public void setShareNativeConnection(boolean shareNativeConnection) {
        this.shareNativeConnection = shareNativeConnection;
    }

    public int getDatabase() {
        return this.dbIndex;
    }

    public void setDatabase(int index) {
        Assert.isTrue(index >= 0, "invalid DB index (a positive index required)");
        this.dbIndex = index;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getShutdownTimeout() {
        return this.shutdownTimeout;
    }

    public void setShutdownTimeout(long shutdownTimeout) {
        this.shutdownTimeout = shutdownTimeout;
    }

    public ClientResources getClientResources() {
        return this.clientResources;
    }

    public void setClientResources(ClientResources clientResources) {
        this.clientResources = clientResources;
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionFactory
    public boolean getConvertPipelineAndTxResults() {
        return this.convertPipelineAndTxResults;
    }

    public void setConvertPipelineAndTxResults(boolean convertPipelineAndTxResults) {
        this.convertPipelineAndTxResults = convertPipelineAndTxResults;
    }

    public boolean isRedisSentinelAware() {
        return this.sentinelConfiguration != null;
    }

    public boolean isClusterAware() {
        return this.clusterConfiguration != null;
    }

    protected StatefulRedisConnection<byte[], byte[]> getSharedConnection() {
        StatefulRedisConnection<byte[], byte[]> statefulRedisConnection;
        if (this.shareNativeConnection) {
            synchronized (this.connectionMonitor) {
                if (this.connection == null) {
                    initConnection();
                }
                if (this.validateConnection) {
                    validateConnection();
                }
                statefulRedisConnection = this.connection;
            }
            return statefulRedisConnection;
        }
        return null;
    }

    protected StatefulRedisConnection<byte[], byte[]> createLettuceConnector() {
        StatefulRedisConnection<byte[], byte[]> connection;
        try {
            if (this.client instanceof RedisClient) {
                connection = this.client.connect(LettuceConnection.CODEC);
                if (this.dbIndex > 0) {
                    connection.sync().select(this.dbIndex);
                }
            } else {
                connection = null;
            }
            return connection;
        } catch (RedisException e) {
            throw new RedisConnectionFailureException("Unable to connect to Redis on " + getHostName() + ":" + getPort(), e);
        }
    }

    private AbstractRedisClient createRedisClient() {
        RedisClusterClient redisClusterClientCreate;
        if (isRedisSentinelAware()) {
            RedisURI redisURI = getSentinelRedisURI();
            if (this.clientResources == null) {
                return RedisClient.create(redisURI);
            }
            return RedisClient.create(this.clientResources, redisURI);
        }
        if (isClusterAware()) {
            List<RedisURI> initialUris = new ArrayList<>();
            for (RedisNode node : this.clusterConfiguration.getClusterNodes()) {
                initialUris.add(createRedisURIAndApplySettings(node.getHost(), node.getPort().intValue()));
            }
            if (this.clientResources != null) {
                redisClusterClientCreate = RedisClusterClient.create(this.clientResources, initialUris);
            } else {
                redisClusterClientCreate = RedisClusterClient.create(initialUris);
            }
            RedisClusterClient clusterClient = redisClusterClientCreate;
            this.clusterCommandExecutor = new ClusterCommandExecutor(new LettuceClusterConnection.LettuceClusterTopologyProvider(clusterClient), new LettuceClusterConnection.LettuceClusterNodeResourceProvider(clusterClient), EXCEPTION_TRANSLATION);
            return clusterClient;
        }
        if (this.pool != null) {
            return this.pool.mo7763getClient();
        }
        RedisURI uri = createRedisURIAndApplySettings(this.hostName, this.port);
        return this.clientResources != null ? RedisClient.create(this.clientResources, uri) : RedisClient.create(uri);
    }

    private RedisURI getSentinelRedisURI() {
        RedisURI redisUri = LettuceConverters.sentinelConfigurationToRedisURI(this.sentinelConfiguration);
        if (StringUtils.hasText(this.password)) {
            redisUri.setPassword(this.password);
        }
        redisUri.setTimeout(this.timeout);
        redisUri.setUnit(TimeUnit.MILLISECONDS);
        return redisUri;
    }

    private RedisURI createRedisURIAndApplySettings(String host, int port) {
        RedisURI.Builder builder = RedisURI.Builder.redis(host, port);
        if (StringUtils.hasText(this.password)) {
            builder.withPassword(this.password);
        }
        builder.withSsl(this.useSsl);
        builder.withVerifyPeer(this.verifyPeer);
        builder.withStartTls(this.startTls);
        builder.withTimeout(this.timeout, TimeUnit.MILLISECONDS);
        return builder.build();
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionFactory
    public RedisSentinelConnection getSentinelConnection() {
        if (!(this.client instanceof RedisClient)) {
            throw new InvalidDataAccessResourceUsageException("Unable to connect to sentinels using " + this.client.getClass());
        }
        return new LettuceSentinelConnection((StatefulRedisSentinelConnection<String, String>) this.client.connectSentinel());
    }
}
