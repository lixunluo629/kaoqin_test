package org.springframework.data.redis.connection.jedis;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
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
import org.springframework.data.redis.connection.jedis.JedisClusterConnection;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.Protocol;
import redis.clients.util.Pool;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/jedis/JedisConnectionFactory.class */
public class JedisConnectionFactory implements InitializingBean, DisposableBean, RedisConnectionFactory {
    private static final Log log = LogFactory.getLog(JedisConnectionFactory.class);
    private static final ExceptionTranslationStrategy EXCEPTION_TRANSLATION = new PassThroughExceptionTranslationStrategy(JedisConverters.exceptionConverter());
    private static final Method SET_TIMEOUT_METHOD;
    private static final Method GET_TIMEOUT_METHOD;
    private JedisShardInfo shardInfo;
    private String hostName;
    private int port;
    private int timeout;
    private String password;
    private boolean usePool;
    private boolean useSsl;
    private Pool<Jedis> pool;
    private JedisPoolConfig poolConfig;
    private int dbIndex;
    private String clientName;
    private boolean convertPipelineAndTxResults;
    private RedisSentinelConfiguration sentinelConfig;
    private RedisClusterConfiguration clusterConfig;
    private JedisCluster cluster;
    private ClusterCommandExecutor clusterCommandExecutor;

    static {
        Method setTimeoutMethodCandidate = ReflectionUtils.findMethod(JedisShardInfo.class, "setTimeout", Integer.TYPE);
        if (setTimeoutMethodCandidate == null) {
            setTimeoutMethodCandidate = ReflectionUtils.findMethod(JedisShardInfo.class, "setSoTimeout", Integer.TYPE);
        }
        SET_TIMEOUT_METHOD = setTimeoutMethodCandidate;
        Method getTimeoutMethodCandidate = ReflectionUtils.findMethod(JedisShardInfo.class, "getTimeout");
        if (getTimeoutMethodCandidate == null) {
            getTimeoutMethodCandidate = ReflectionUtils.findMethod(JedisShardInfo.class, "getSoTimeout");
        }
        GET_TIMEOUT_METHOD = getTimeoutMethodCandidate;
    }

    public JedisConnectionFactory() {
        this.hostName = "localhost";
        this.port = Protocol.DEFAULT_PORT;
        this.timeout = 2000;
        this.usePool = true;
        this.useSsl = false;
        this.poolConfig = new JedisPoolConfig();
        this.dbIndex = 0;
        this.convertPipelineAndTxResults = true;
    }

    public JedisConnectionFactory(JedisShardInfo shardInfo) {
        this.hostName = "localhost";
        this.port = Protocol.DEFAULT_PORT;
        this.timeout = 2000;
        this.usePool = true;
        this.useSsl = false;
        this.poolConfig = new JedisPoolConfig();
        this.dbIndex = 0;
        this.convertPipelineAndTxResults = true;
        setShardInfo(shardInfo);
    }

    public JedisConnectionFactory(JedisPoolConfig poolConfig) {
        this((RedisSentinelConfiguration) null, poolConfig);
    }

    public JedisConnectionFactory(RedisSentinelConfiguration sentinelConfig) {
        this(sentinelConfig, (JedisPoolConfig) null);
    }

    public JedisConnectionFactory(RedisSentinelConfiguration sentinelConfig, JedisPoolConfig poolConfig) {
        this.hostName = "localhost";
        this.port = Protocol.DEFAULT_PORT;
        this.timeout = 2000;
        this.usePool = true;
        this.useSsl = false;
        this.poolConfig = new JedisPoolConfig();
        this.dbIndex = 0;
        this.convertPipelineAndTxResults = true;
        this.sentinelConfig = sentinelConfig;
        this.poolConfig = poolConfig != null ? poolConfig : new JedisPoolConfig();
    }

    public JedisConnectionFactory(RedisClusterConfiguration clusterConfig) {
        this.hostName = "localhost";
        this.port = Protocol.DEFAULT_PORT;
        this.timeout = 2000;
        this.usePool = true;
        this.useSsl = false;
        this.poolConfig = new JedisPoolConfig();
        this.dbIndex = 0;
        this.convertPipelineAndTxResults = true;
        this.clusterConfig = clusterConfig;
    }

    public JedisConnectionFactory(RedisClusterConfiguration clusterConfig, JedisPoolConfig poolConfig) {
        this.hostName = "localhost";
        this.port = Protocol.DEFAULT_PORT;
        this.timeout = 2000;
        this.usePool = true;
        this.useSsl = false;
        this.poolConfig = new JedisPoolConfig();
        this.dbIndex = 0;
        this.convertPipelineAndTxResults = true;
        this.clusterConfig = clusterConfig;
        this.poolConfig = poolConfig;
    }

    protected Jedis fetchJedisConnector() {
        try {
            if (this.usePool && this.pool != null) {
                return this.pool.getResource();
            }
            Jedis jedis = new Jedis(getShardInfo());
            jedis.connect();
            potentiallySetClientName(jedis);
            return jedis;
        } catch (Exception ex) {
            throw new RedisConnectionFailureException("Cannot get Jedis connection", ex);
        }
    }

    protected JedisConnection postProcessConnection(JedisConnection connection) {
        return connection;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        if (this.shardInfo == null) {
            this.shardInfo = new JedisShardInfo(this.hostName, this.port);
            if (StringUtils.hasLength(this.password)) {
                this.shardInfo.setPassword(this.password);
            }
            if (this.timeout > 0) {
                setTimeoutOn(this.shardInfo, this.timeout);
            }
        }
        if (this.usePool && this.clusterConfig == null) {
            this.pool = createPool();
        }
        if (this.clusterConfig != null) {
            this.cluster = createCluster();
        }
    }

    private Pool<Jedis> createPool() {
        if (isRedisSentinelAware()) {
            return createRedisSentinelPool(this.sentinelConfig);
        }
        return createRedisPool();
    }

    protected Pool<Jedis> createRedisSentinelPool(RedisSentinelConfiguration config) {
        return new JedisSentinelPool(config.getMaster().getName(), convertToJedisSentinelSet(config.getSentinels()), getPoolConfig() != null ? getPoolConfig() : new JedisPoolConfig(), getTimeoutFrom(getShardInfo()), getShardInfo().getPassword(), getDatabase(), this.clientName);
    }

    protected Pool<Jedis> createRedisPool() {
        return new JedisPool(getPoolConfig(), getShardInfo().getHost(), getShardInfo().getPort(), getTimeoutFrom(getShardInfo()), getShardInfo().getPassword(), getDatabase(), this.clientName, this.useSsl);
    }

    private JedisCluster createCluster() {
        JedisCluster cluster = createCluster(this.clusterConfig, getPoolConfig());
        JedisClusterConnection.JedisClusterTopologyProvider topologyProvider = new JedisClusterConnection.JedisClusterTopologyProvider(cluster);
        this.clusterCommandExecutor = new ClusterCommandExecutor(topologyProvider, new JedisClusterConnection.JedisClusterNodeResourceProvider(cluster, topologyProvider), EXCEPTION_TRANSLATION);
        return cluster;
    }

    protected JedisCluster createCluster(RedisClusterConfiguration clusterConfig, GenericObjectPoolConfig poolConfig) {
        Assert.notNull(clusterConfig, "Cluster configuration must not be null!");
        Set<HostAndPort> hostAndPort = new HashSet<>();
        for (RedisNode node : clusterConfig.getClusterNodes()) {
            hostAndPort.add(new HostAndPort(node.getHost(), node.getPort().intValue()));
        }
        int redirects = clusterConfig.getMaxRedirects() != null ? clusterConfig.getMaxRedirects().intValue() : 5;
        return StringUtils.hasText(getPassword()) ? new JedisCluster(hostAndPort, this.timeout, this.timeout, redirects, this.password, poolConfig) : new JedisCluster(hostAndPort, this.timeout, redirects, poolConfig);
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() {
        if (this.usePool && this.pool != null) {
            try {
                this.pool.destroy();
            } catch (Exception ex) {
                log.warn("Cannot properly close Jedis pool", ex);
            }
            this.pool = null;
        }
        if (this.cluster != null) {
            try {
                this.cluster.close();
            } catch (Exception ex2) {
                log.warn("Cannot properly close Jedis cluster", ex2);
            }
            try {
                this.clusterCommandExecutor.destroy();
            } catch (Exception ex3) {
                log.warn("Cannot properly close cluster command executor", ex3);
            }
        }
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionFactory
    public RedisConnection getConnection() {
        if (this.cluster != null) {
            return getClusterConnection();
        }
        Jedis jedis = fetchJedisConnector();
        JedisConnection connection = this.usePool ? new JedisConnection(jedis, this.pool, this.dbIndex, this.clientName) : new JedisConnection(jedis, null, this.dbIndex, this.clientName);
        connection.setConvertPipelineAndTxResults(this.convertPipelineAndTxResults);
        return postProcessConnection(connection);
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionFactory
    public RedisClusterConnection getClusterConnection() {
        if (this.cluster == null) {
            throw new InvalidDataAccessApiUsageException("Cluster is not configured!");
        }
        return new JedisClusterConnection(this.cluster, this.clusterCommandExecutor);
    }

    @Override // org.springframework.dao.support.PersistenceExceptionTranslator
    public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
        return EXCEPTION_TRANSLATION.translate(ex);
    }

    public String getHostName() {
        return this.hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setUseSsl(boolean useSsl) {
        this.useSsl = useSsl;
    }

    public boolean isUseSsl() {
        return this.useSsl;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public JedisShardInfo getShardInfo() {
        return this.shardInfo;
    }

    public void setShardInfo(JedisShardInfo shardInfo) {
        this.shardInfo = shardInfo;
        if (shardInfo != null) {
            setUseSsl(shardInfo.getSsl());
        }
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean getUsePool() {
        if (isRedisSentinelAware()) {
            return true;
        }
        return this.usePool;
    }

    public void setUsePool(boolean usePool) {
        if (isRedisSentinelAware() && !usePool) {
            throw new IllegalStateException("Jedis requires pooling for Redis Sentinel use!");
        }
        this.usePool = usePool;
    }

    public JedisPoolConfig getPoolConfig() {
        return this.poolConfig;
    }

    public void setPoolConfig(JedisPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }

    public int getDatabase() {
        return this.dbIndex;
    }

    public void setDatabase(int index) {
        Assert.isTrue(index >= 0, "invalid DB index (a positive index required)");
        this.dbIndex = index;
    }

    public String getClientName() {
        return this.clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionFactory
    public boolean getConvertPipelineAndTxResults() {
        return this.convertPipelineAndTxResults;
    }

    public void setConvertPipelineAndTxResults(boolean convertPipelineAndTxResults) {
        this.convertPipelineAndTxResults = convertPipelineAndTxResults;
    }

    public boolean isRedisSentinelAware() {
        return this.sentinelConfig != null;
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionFactory
    public RedisSentinelConnection getSentinelConnection() {
        if (!isRedisSentinelAware()) {
            throw new InvalidDataAccessResourceUsageException("No Sentinels configured");
        }
        return new JedisSentinelConnection(getActiveSentinel());
    }

    private Jedis getActiveSentinel() {
        Assert.notNull(this.sentinelConfig, "SentinelConfig must not be null!");
        for (RedisNode node : this.sentinelConfig.getSentinels()) {
            Jedis jedis = new Jedis(node.getHost(), node.getPort().intValue());
            if (jedis.ping().equalsIgnoreCase("pong")) {
                potentiallySetClientName(jedis);
                return jedis;
            }
        }
        throw new InvalidDataAccessResourceUsageException("No Sentinel found");
    }

    private Set<String> convertToJedisSentinelSet(Collection<RedisNode> nodes) {
        if (CollectionUtils.isEmpty(nodes)) {
            return Collections.emptySet();
        }
        Set<String> convertedNodes = new LinkedHashSet<>(nodes.size());
        for (RedisNode node : nodes) {
            if (node != null) {
                convertedNodes.add(node.asString());
            }
        }
        return convertedNodes;
    }

    private void potentiallySetClientName(Jedis jedis) {
        if (StringUtils.hasText(this.clientName)) {
            jedis.clientSetname(this.clientName);
        }
    }

    private void setTimeoutOn(JedisShardInfo shardInfo, int timeout) {
        ReflectionUtils.invokeMethod(SET_TIMEOUT_METHOD, shardInfo, Integer.valueOf(timeout));
    }

    private int getTimeoutFrom(JedisShardInfo shardInfo) {
        return ((Integer) ReflectionUtils.invokeMethod(GET_TIMEOUT_METHOD, shardInfo)).intValue();
    }
}
