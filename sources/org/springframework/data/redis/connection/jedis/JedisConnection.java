package org.springframework.data.redis.connection.jedis;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.ExceptionTranslationStrategy;
import org.springframework.data.redis.FallbackExceptionTranslationStrategy;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.AbstractRedisConnection;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.FutureResult;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.RedisListCommands;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPipelineException;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.RedisSubscribedConnectionException;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.data.redis.connection.Subscription;
import org.springframework.data.redis.connection.convert.Converters;
import org.springframework.data.redis.connection.convert.ListConverter;
import org.springframework.data.redis.connection.convert.TransactionResultConverter;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.KeyBoundCursor;
import org.springframework.data.redis.core.ScanCursor;
import org.springframework.data.redis.core.ScanIteration;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.Builder;
import redis.clients.jedis.Client;
import redis.clients.jedis.Connection;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.Queable;
import redis.clients.jedis.Response;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.ZParams;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.util.Pool;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/jedis/JedisConnection.class */
public class JedisConnection extends AbstractRedisConnection {
    private static final Method SEND_COMMAND;
    private static final Method GET_RESPONSE;
    private static final String SHUTDOWN_SCRIPT = "return redis.call('SHUTDOWN','%s')";
    private final Jedis jedis;
    private final Client client;
    private Transaction transaction;
    private final Pool<Jedis> pool;
    private boolean broken;
    private volatile JedisSubscription subscription;
    private volatile Pipeline pipeline;
    private final int dbIndex;
    private final String clientName;
    private boolean convertPipelineAndTxResults;
    private List<FutureResult<Response<?>>> pipelinedResults;
    private Queue<FutureResult<Response<?>>> txResults;
    private static final ExceptionTranslationStrategy EXCEPTION_TRANSLATION = new FallbackExceptionTranslationStrategy(JedisConverters.exceptionConverter());
    private static final Field CLIENT_FIELD = ReflectionUtils.findField(BinaryJedis.class, "client", Client.class);

    static {
        Class<?> clsForName;
        ReflectionUtils.makeAccessible(CLIENT_FIELD);
        try {
            if (ClassUtils.isPresent("redis.clients.jedis.ProtocolCommand", null)) {
                clsForName = ClassUtils.forName("redis.clients.jedis.ProtocolCommand", null);
            } else {
                clsForName = ClassUtils.forName("redis.clients.jedis.Protocol$Command", null);
            }
            Class<?> commandType = clsForName;
            SEND_COMMAND = ReflectionUtils.findMethod(Connection.class, "sendCommand", commandType, byte[][].class);
            ReflectionUtils.makeAccessible(SEND_COMMAND);
            GET_RESPONSE = ReflectionUtils.findMethod(Queable.class, "getResponse", Builder.class);
            ReflectionUtils.makeAccessible(GET_RESPONSE);
        } catch (Exception e) {
            throw new NoClassDefFoundError("Could not find required flavor of command required by 'redis.clients.jedis.Connection#sendCommand'.");
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/jedis/JedisConnection$JedisResult.class */
    private class JedisResult extends FutureResult<Response<?>> {
        public <T> JedisResult(Response<T> resultHolder, Converter<T, ?> converter) {
            super(resultHolder, converter);
        }

        public <T> JedisResult(Response<T> resultHolder) {
            super(resultHolder);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // org.springframework.data.redis.connection.FutureResult
        public Object get() {
            if (JedisConnection.this.convertPipelineAndTxResults && this.converter != null) {
                return this.converter.convert2(((Response) this.resultHolder).get());
            }
            return ((Response) this.resultHolder).get();
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/jedis/JedisConnection$JedisStatusResult.class */
    private class JedisStatusResult extends JedisResult {
        public JedisStatusResult(Response<?> resultHolder) {
            super(resultHolder);
            setStatus(true);
        }
    }

    public JedisConnection(Jedis jedis) {
        this(jedis, null, 0);
    }

    public JedisConnection(Jedis jedis, Pool<Jedis> pool, int dbIndex) {
        this(jedis, pool, dbIndex, null);
    }

    protected JedisConnection(Jedis jedis, Pool<Jedis> pool, int dbIndex, String clientName) throws DataAccessException {
        this.broken = false;
        this.convertPipelineAndTxResults = true;
        this.pipelinedResults = new ArrayList();
        this.txResults = new LinkedList();
        this.client = (Client) ReflectionUtils.getField(CLIENT_FIELD, jedis);
        this.jedis = jedis;
        this.pool = pool;
        this.dbIndex = dbIndex;
        this.clientName = clientName;
        if (dbIndex != jedis.getDB().longValue()) {
            try {
                select(dbIndex);
            } catch (DataAccessException ex) {
                close();
                throw ex;
            }
        }
    }

    protected DataAccessException convertJedisAccessException(Exception ex) {
        if (ex instanceof NullPointerException) {
            this.broken = true;
        }
        DataAccessException exception = EXCEPTION_TRANSLATION.translate(ex);
        if (exception instanceof RedisConnectionFailureException) {
            this.broken = true;
        }
        return exception != null ? exception : new RedisSystemException(ex.getMessage(), ex);
    }

    @Override // org.springframework.data.redis.connection.RedisCommands
    public Object execute(String command, byte[]... args) {
        Assert.hasText(command, "a valid command needs to be specified");
        try {
            List<byte[]> mArgs = new ArrayList<>();
            if (!ObjectUtils.isEmpty((Object[]) args)) {
                Collections.addAll(mArgs, args);
            }
            ReflectionUtils.invokeMethod(SEND_COMMAND, this.client, Protocol.Command.valueOf(command.trim().toUpperCase()), mArgs.toArray((Object[]) new byte[mArgs.size()]));
            if (isQueueing() || isPipelined()) {
                Object target = isPipelined() ? this.pipeline : this.transaction;
                Response<Object> result = (Response) ReflectionUtils.invokeMethod(GET_RESPONSE, target, new Builder<Object>() { // from class: org.springframework.data.redis.connection.jedis.JedisConnection.1
                    @Override // redis.clients.jedis.Builder
                    public Object build(Object data) {
                        return data;
                    }

                    public String toString() {
                        return "Object";
                    }
                });
                if (isPipelined()) {
                    pipeline(new JedisResult(result));
                    return null;
                }
                transaction(new JedisResult(result));
                return null;
            }
            return this.client.getOne();
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.AbstractRedisConnection, org.springframework.data.redis.connection.RedisConnection
    public void close() throws DataAccessException {
        super.close();
        if (this.pool != null) {
            if (this.broken) {
                this.pool.returnBrokenResource(this.jedis);
                return;
            } else {
                this.jedis.close();
                return;
            }
        }
        Exception exc = null;
        if (isQueueing()) {
            try {
                this.client.quit();
            } catch (Exception e) {
            }
            try {
                this.client.disconnect();
                return;
            } catch (Exception e2) {
                return;
            }
        }
        try {
            this.jedis.quit();
        } catch (Exception ex) {
            exc = ex;
        }
        try {
            this.jedis.disconnect();
        } catch (Exception ex2) {
            exc = ex2;
        }
        if (exc != null) {
            throw convertJedisAccessException(exc);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public Jedis getNativeConnection() {
        return this.jedis;
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public boolean isClosed() {
        try {
            return !this.jedis.isConnected();
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public boolean isQueueing() {
        return this.client.isInMulti();
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public boolean isPipelined() {
        return this.pipeline != null;
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public void openPipeline() {
        if (this.pipeline == null) {
            this.pipeline = this.jedis.pipelined();
        }
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public List<Object> closePipeline() {
        if (this.pipeline != null) {
            try {
                return convertPipelineResults();
            } finally {
                this.pipeline = null;
                this.pipelinedResults.clear();
            }
        }
        return Collections.emptyList();
    }

    private List<Object> convertPipelineResults() {
        List<Object> results = new ArrayList<>();
        this.pipeline.sync();
        Exception cause = null;
        for (FutureResult<Response<?>> result : this.pipelinedResults) {
            try {
                Object data = result.get();
                if (!this.convertPipelineAndTxResults || !result.isStatus()) {
                    results.add(data);
                }
            } catch (DataAccessException e) {
                if (cause == null) {
                    cause = e;
                }
                results.add(e);
            } catch (JedisDataException e2) {
                Exception dataAccessException = convertJedisAccessException(e2);
                if (cause == null) {
                    cause = dataAccessException;
                }
                results.add(dataAccessException);
            }
        }
        if (cause != null) {
            throw new RedisPipelineException(cause, results);
        }
        return results;
    }

    private void doPipelined(Response<?> response) {
        pipeline(new JedisStatusResult(response));
    }

    private void pipeline(FutureResult<Response<?>> result) {
        if (isQueueing()) {
            transaction(result);
        } else {
            this.pipelinedResults.add(result);
        }
    }

    private void doQueued(Response<?> response) {
        transaction(new JedisStatusResult(response));
    }

    private void transaction(FutureResult<Response<?>> result) {
        this.txResults.add(result);
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public List<byte[]> sort(byte[] key, SortParameters params) {
        SortingParams sortParams = JedisConverters.toSortingParams(params);
        try {
            if (isPipelined()) {
                if (sortParams != null) {
                    pipeline(new JedisResult(this.pipeline.sort(key, sortParams)));
                    return null;
                }
                pipeline(new JedisResult(this.pipeline.sort(key)));
                return null;
            }
            if (!isQueueing()) {
                return sortParams != null ? this.jedis.sort(key, sortParams) : this.jedis.sort(key);
            }
            if (sortParams != null) {
                transaction(new JedisResult(this.transaction.sort(key, sortParams)));
                return null;
            }
            transaction(new JedisResult(this.transaction.sort(key)));
            return null;
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long sort(byte[] key, SortParameters params, byte[] storeKey) {
        SortingParams sortParams = JedisConverters.toSortingParams(params);
        try {
            if (isPipelined()) {
                if (sortParams != null) {
                    pipeline(new JedisResult(this.pipeline.sort(key, sortParams, storeKey)));
                    return null;
                }
                pipeline(new JedisResult(this.pipeline.sort(key, storeKey)));
                return null;
            }
            if (!isQueueing()) {
                return sortParams != null ? this.jedis.sort(key, sortParams, storeKey) : this.jedis.sort(key, storeKey);
            }
            if (sortParams != null) {
                transaction(new JedisResult(this.transaction.sort(key, sortParams, storeKey)));
                return null;
            }
            transaction(new JedisResult(this.transaction.sort(key, storeKey)));
            return null;
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public Long dbSize() {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.dbSize()));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.dbSize()));
                return null;
            }
            return this.jedis.dbSize();
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void flushDb() {
        try {
            if (isPipelined()) {
                pipeline(new JedisStatusResult(this.pipeline.flushDB()));
            } else if (isQueueing()) {
                transaction(new JedisStatusResult(this.transaction.flushDB()));
            } else {
                this.jedis.flushDB();
            }
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void flushAll() {
        try {
            if (isPipelined()) {
                pipeline(new JedisStatusResult(this.pipeline.flushAll()));
            } else if (isQueueing()) {
                transaction(new JedisResult(this.transaction.flushAll()));
            } else {
                this.jedis.flushAll();
            }
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void bgSave() {
        try {
            if (isPipelined()) {
                pipeline(new JedisStatusResult(this.pipeline.bgsave()));
            } else if (isQueueing()) {
                transaction(new JedisStatusResult(this.transaction.bgsave()));
            } else {
                this.jedis.bgsave();
            }
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void bgReWriteAof() {
        try {
            if (isPipelined()) {
                pipeline(new JedisStatusResult(this.pipeline.bgrewriteaof()));
            } else if (isQueueing()) {
                transaction(new JedisStatusResult(this.transaction.bgrewriteaof()));
            } else {
                this.jedis.bgrewriteaof();
            }
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    @Deprecated
    public void bgWriteAof() {
        bgReWriteAof();
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void save() {
        try {
            if (isPipelined()) {
                pipeline(new JedisStatusResult(this.pipeline.save()));
            } else if (isQueueing()) {
                transaction(new JedisStatusResult(this.transaction.save()));
            } else {
                this.jedis.save();
            }
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public List<String> getConfig(String param) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.configGet(param)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.configGet(param)));
                return null;
            }
            return this.jedis.configGet(param);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public Properties info() {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.info(), JedisConverters.stringToProps()));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.info(), JedisConverters.stringToProps()));
                return null;
            }
            return JedisConverters.toProperties(this.jedis.info());
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public Properties info(String section) {
        if (isPipelined()) {
            throw new UnsupportedOperationException();
        }
        if (isQueueing()) {
            throw new UnsupportedOperationException();
        }
        try {
            return JedisConverters.toProperties(this.jedis.info(section));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public Long lastSave() {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.lastsave()));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.lastsave()));
                return null;
            }
            return this.jedis.lastsave();
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void setConfig(String param, String value) {
        try {
            if (isPipelined()) {
                pipeline(new JedisStatusResult(this.pipeline.configSet(param, value)));
            } else if (isQueueing()) {
                transaction(new JedisStatusResult(this.transaction.configSet(param, value)));
            } else {
                this.jedis.configSet(param, value);
            }
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void resetConfigStats() {
        try {
            if (isPipelined()) {
                pipeline(new JedisStatusResult(this.pipeline.configResetStat()));
            } else if (isQueueing()) {
                transaction(new JedisStatusResult(this.transaction.configResetStat()));
            } else {
                this.jedis.configResetStat();
            }
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void shutdown() {
        try {
            if (isPipelined()) {
                pipeline(new JedisStatusResult(this.pipeline.shutdown()));
            } else if (isQueueing()) {
                transaction(new JedisStatusResult(this.transaction.shutdown()));
            } else {
                this.jedis.shutdown();
            }
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v2, types: [byte[], byte[][]] */
    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void shutdown(RedisServerCommands.ShutdownOption option) {
        if (option == null) {
            shutdown();
        } else {
            eval(String.format(SHUTDOWN_SCRIPT, option.name()).getBytes(), ReturnType.STATUS, 0, new byte[0]);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionCommands
    public byte[] echo(byte[] message) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.echo(message)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.echo(message)));
                return null;
            }
            return this.jedis.echo(message);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionCommands
    public String ping() {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.ping()));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.ping()));
                return null;
            }
            return this.jedis.ping();
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long del(byte[]... keys) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.del(keys)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.del(keys)));
                return null;
            }
            return this.jedis.del(keys);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisTxCommands
    public void discard() {
        try {
            try {
                if (isPipelined()) {
                    pipeline(new JedisStatusResult(this.pipeline.discard()));
                    this.txResults.clear();
                    this.transaction = null;
                } else {
                    this.transaction.discard();
                    this.txResults.clear();
                    this.transaction = null;
                }
            } catch (Exception ex) {
                throw convertJedisAccessException(ex);
            }
        } catch (Throwable th) {
            this.txResults.clear();
            this.transaction = null;
            throw th;
        }
    }

    @Override // org.springframework.data.redis.connection.RedisTxCommands
    public List<Object> exec() {
        try {
            try {
                if (isPipelined()) {
                    pipeline(new JedisResult(this.pipeline.exec(), new TransactionResultConverter(new LinkedList(this.txResults), JedisConverters.exceptionConverter())));
                    this.txResults.clear();
                    this.transaction = null;
                    return null;
                }
                if (this.transaction == null) {
                    throw new InvalidDataAccessApiUsageException("No ongoing transaction. Did you forget to call multi?");
                }
                List<Object> results = this.transaction.exec();
                return (!this.convertPipelineAndTxResults || CollectionUtils.isEmpty(results)) ? results : new TransactionResultConverter(this.txResults, JedisConverters.exceptionConverter()).convert2(results);
            } catch (Exception ex) {
                throw convertJedisAccessException(ex);
            }
        } finally {
            this.txResults.clear();
            this.transaction = null;
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean exists(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.exists(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.exists(key)));
                return null;
            }
            return this.jedis.exists(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean expire(byte[] key, long seconds) {
        Assert.notNull(key, "Key must not be null!");
        if (seconds > 2147483647L) {
            return pExpire(key, TimeUnit.SECONDS.toMillis(seconds));
        }
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.expire(key, (int) seconds), JedisConverters.longToBoolean()));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.expire(key, (int) seconds), JedisConverters.longToBoolean()));
                return null;
            }
            return JedisConverters.toBoolean(this.jedis.expire(key, (int) seconds));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean expireAt(byte[] key, long unixTime) {
        Assert.notNull(key, "Key must not be null!");
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.expireAt(key, unixTime), JedisConverters.longToBoolean()));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.expireAt(key, unixTime), JedisConverters.longToBoolean()));
                return null;
            }
            return JedisConverters.toBoolean(this.jedis.expireAt(key, unixTime));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Set<byte[]> keys(byte[] pattern) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.keys(pattern)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.keys(pattern)));
                return null;
            }
            return this.jedis.keys(pattern);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisTxCommands
    public void multi() {
        if (isQueueing()) {
            return;
        }
        try {
            if (isPipelined()) {
                this.pipeline.multi();
            } else {
                this.transaction = this.jedis.multi();
            }
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean persist(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.persist(key), JedisConverters.longToBoolean()));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.persist(key), JedisConverters.longToBoolean()));
                return null;
            }
            return JedisConverters.toBoolean(this.jedis.persist(key));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean move(byte[] key, int dbIndex) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.move(key, dbIndex), JedisConverters.longToBoolean()));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.move(key, dbIndex), JedisConverters.longToBoolean()));
                return null;
            }
            return JedisConverters.toBoolean(this.jedis.move(key, dbIndex));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public byte[] randomKey() {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.randomKeyBinary()));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.randomKeyBinary()));
                return null;
            }
            return this.jedis.randomBinaryKey();
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public void rename(byte[] oldName, byte[] newName) {
        try {
            if (isPipelined()) {
                pipeline(new JedisStatusResult(this.pipeline.rename(oldName, newName)));
            } else if (isQueueing()) {
                transaction(new JedisStatusResult(this.transaction.rename(oldName, newName)));
            } else {
                this.jedis.rename(oldName, newName);
            }
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean renameNX(byte[] oldName, byte[] newName) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.renamenx(oldName, newName), JedisConverters.longToBoolean()));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.renamenx(oldName, newName), JedisConverters.longToBoolean()));
                return null;
            }
            return JedisConverters.toBoolean(this.jedis.renamenx(oldName, newName));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionCommands
    public void select(int dbIndex) {
        try {
            if (isPipelined()) {
                pipeline(new JedisStatusResult(this.pipeline.select(dbIndex)));
            } else if (isQueueing()) {
                transaction(new JedisStatusResult(this.transaction.select(dbIndex)));
            } else {
                this.jedis.select(dbIndex);
            }
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long ttl(byte[] key) {
        Assert.notNull(key, "Key must not be null!");
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.ttl(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.ttl(key)));
                return null;
            }
            return this.jedis.ttl(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long ttl(byte[] key, TimeUnit timeUnit) {
        Assert.notNull(key, "Key must not be null!");
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.ttl(key), Converters.secondsToTimeUnit(timeUnit)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.ttl(key), Converters.secondsToTimeUnit(timeUnit)));
                return null;
            }
            return Long.valueOf(Converters.secondsToTimeUnit(this.jedis.ttl(key).longValue(), timeUnit));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean pExpire(byte[] key, long millis) {
        Assert.notNull(key, "Key must not be null!");
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.pexpire(key, millis), JedisConverters.longToBoolean()));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.pexpire(key, millis), JedisConverters.longToBoolean()));
                return null;
            }
            return JedisConverters.toBoolean(this.jedis.pexpire(key, millis));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean pExpireAt(byte[] key, long unixTimeInMillis) {
        Assert.notNull(key, "Key must not be null!");
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.pexpireAt(key, unixTimeInMillis), JedisConverters.longToBoolean()));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.pexpireAt(key, unixTimeInMillis), JedisConverters.longToBoolean()));
                return null;
            }
            return JedisConverters.toBoolean(this.jedis.pexpireAt(key, unixTimeInMillis));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long pTtl(byte[] key) {
        Assert.notNull(key, "Key must not be null!");
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.pttl(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.pttl(key)));
                return null;
            }
            return this.jedis.pttl(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long pTtl(byte[] key, TimeUnit timeUnit) {
        Assert.notNull(key, "Key must not be null!");
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.pttl(key), Converters.millisecondsToTimeUnit(timeUnit)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.pttl(key), Converters.millisecondsToTimeUnit(timeUnit)));
                return null;
            }
            return Long.valueOf(Converters.millisecondsToTimeUnit(this.jedis.pttl(key).longValue(), timeUnit));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public byte[] dump(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.dump(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.dump(key)));
                return null;
            }
            return this.jedis.dump(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public void restore(byte[] key, long ttlInMillis, byte[] serializedValue) {
        if (ttlInMillis > 2147483647L) {
            throw new IllegalArgumentException("TtlInMillis must be less than Integer.MAX_VALUE for restore in Jedis.");
        }
        try {
            if (isPipelined()) {
                pipeline(new JedisStatusResult(this.pipeline.restore(key, (int) ttlInMillis, serializedValue)));
            } else if (isQueueing()) {
                transaction(new JedisStatusResult(this.transaction.restore(key, (int) ttlInMillis, serializedValue)));
            } else {
                this.jedis.restore(key, (int) ttlInMillis, serializedValue);
            }
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public DataType type(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.type(key), JedisConverters.stringToDataType()));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.type(key), JedisConverters.stringToDataType()));
                return null;
            }
            return JedisConverters.toDataType(this.jedis.type(key));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisTxCommands
    public void unwatch() {
        try {
            this.jedis.unwatch();
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v4, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r5v1, types: [byte[], byte[][]] */
    @Override // org.springframework.data.redis.connection.RedisTxCommands
    public void watch(byte[]... keys) {
        if (isQueueing()) {
            throw new UnsupportedOperationException();
        }
        try {
            for (byte[] key : keys) {
                if (isPipelined()) {
                    pipeline(new JedisStatusResult(this.pipeline.watch((byte[][]) new byte[]{key})));
                } else {
                    this.jedis.watch((byte[][]) new byte[]{key});
                }
            }
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public byte[] get(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.get(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.get(key)));
                return null;
            }
            return this.jedis.get(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void set(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new JedisStatusResult(this.pipeline.set(key, value)));
            } else if (isQueueing()) {
                transaction(new JedisStatusResult(this.transaction.set(key, value)));
            } else {
                this.jedis.set(key, value);
            }
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void set(byte[] key, byte[] value, Expiration expiration, RedisStringCommands.SetOption option) {
        if (expiration == null || expiration.isPersistent()) {
            if (option == null || ObjectUtils.nullSafeEquals(RedisStringCommands.SetOption.UPSERT, option)) {
                set(key, value);
                return;
            }
            try {
                byte[] nxxx = JedisConverters.toSetCommandNxXxArgument(option);
                if (isPipelined()) {
                    pipeline(new JedisStatusResult(this.pipeline.set(key, value, nxxx)));
                    return;
                } else if (isQueueing()) {
                    transaction(new JedisStatusResult(this.transaction.set(key, value, nxxx)));
                    return;
                } else {
                    this.jedis.set(key, value, nxxx);
                    return;
                }
            } catch (Exception ex) {
                throw convertJedisAccessException(ex);
            }
        }
        if (option == null || ObjectUtils.nullSafeEquals(RedisStringCommands.SetOption.UPSERT, option)) {
            if (ObjectUtils.nullSafeEquals(TimeUnit.MILLISECONDS, expiration.getTimeUnit())) {
                pSetEx(key, expiration.getExpirationTime(), value);
                return;
            } else {
                setEx(key, expiration.getExpirationTime(), value);
                return;
            }
        }
        byte[] nxxx2 = JedisConverters.toSetCommandNxXxArgument(option);
        byte[] expx = JedisConverters.toSetCommandExPxArgument(expiration);
        try {
            if (isPipelined()) {
                if (expiration.getExpirationTime() > 2147483647L) {
                    throw new IllegalArgumentException("Expiration.expirationTime must be less than Integer.MAX_VALUE for pipeline in Jedis.");
                }
                pipeline(new JedisStatusResult(this.pipeline.set(key, value, nxxx2, expx, (int) expiration.getExpirationTime())));
            } else if (!isQueueing()) {
                this.jedis.set(key, value, nxxx2, expx, expiration.getExpirationTime());
            } else {
                if (expiration.getExpirationTime() > 2147483647L) {
                    throw new IllegalArgumentException("Expiration.expirationTime must be less than Integer.MAX_VALUE for transactions in Jedis.");
                }
                transaction(new JedisStatusResult(this.transaction.set(key, value, nxxx2, expx, (int) expiration.getExpirationTime())));
            }
        } catch (Exception ex2) {
            throw convertJedisAccessException(ex2);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public byte[] getSet(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.getSet(key, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.getSet(key, value)));
                return null;
            }
            return this.jedis.getSet(key, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long append(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.append(key, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.append(key, value)));
                return null;
            }
            return this.jedis.append(key, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public List<byte[]> mGet(byte[]... keys) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.mget(keys)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.mget(keys)));
                return null;
            }
            return this.jedis.mget(keys);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void mSet(Map<byte[], byte[]> tuples) {
        try {
            if (isPipelined()) {
                pipeline(new JedisStatusResult(this.pipeline.mset(JedisConverters.toByteArrays(tuples))));
            } else if (isQueueing()) {
                transaction(new JedisStatusResult(this.transaction.mset(JedisConverters.toByteArrays(tuples))));
            } else {
                this.jedis.mset(JedisConverters.toByteArrays(tuples));
            }
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Boolean mSetNX(Map<byte[], byte[]> tuples) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.msetnx(JedisConverters.toByteArrays(tuples)), JedisConverters.longToBoolean()));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.msetnx(JedisConverters.toByteArrays(tuples)), JedisConverters.longToBoolean()));
                return null;
            }
            return JedisConverters.toBoolean(this.jedis.msetnx(JedisConverters.toByteArrays(tuples)));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void setEx(byte[] key, long time, byte[] value) {
        if (time > 2147483647L) {
            throw new IllegalArgumentException("Time must be less than Integer.MAX_VALUE for setEx in Jedis.");
        }
        try {
            if (isPipelined()) {
                pipeline(new JedisStatusResult(this.pipeline.setex(key, (int) time, value)));
            } else if (isQueueing()) {
                transaction(new JedisStatusResult(this.transaction.setex(key, (int) time, value)));
            } else {
                this.jedis.setex(key, (int) time, value);
            }
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void pSetEx(byte[] key, long milliseconds, byte[] value) {
        try {
            if (isPipelined()) {
                doPipelined(this.pipeline.psetex(key, milliseconds, value));
            } else if (isQueueing()) {
                doQueued(this.transaction.psetex(key, milliseconds, value));
            } else {
                this.jedis.psetex(key, milliseconds, value);
            }
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Boolean setNX(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.setnx(key, value), JedisConverters.longToBoolean()));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.setnx(key, value), JedisConverters.longToBoolean()));
                return null;
            }
            return JedisConverters.toBoolean(this.jedis.setnx(key, value));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public byte[] getRange(byte[] key, long start, long end) {
        if (start > 2147483647L || end > 2147483647L) {
            throw new IllegalArgumentException("Start and end must be less than Integer.MAX_VALUE for getRange in Jedis.");
        }
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.substr(key, (int) start, (int) end), JedisConverters.stringToBytes()));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.substr(key, (int) start, (int) end), JedisConverters.stringToBytes()));
                return null;
            }
            return this.jedis.substr(key, (int) start, (int) end);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long decr(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.decr(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.decr(key)));
                return null;
            }
            return this.jedis.decr(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long decrBy(byte[] key, long value) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.decrBy(key, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.decrBy(key, value)));
                return null;
            }
            return this.jedis.decrBy(key, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long incr(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.incr(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.incr(key)));
                return null;
            }
            return this.jedis.incr(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long incrBy(byte[] key, long value) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.incrBy(key, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.incrBy(key, value)));
                return null;
            }
            return this.jedis.incrBy(key, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Double incrBy(byte[] key, double value) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.incrByFloat(key, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.incrByFloat(key, value)));
                return null;
            }
            return this.jedis.incrByFloat(key, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Boolean getBit(byte[] key, long offset) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.getbit(key, offset)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.getbit(key, offset)));
                return null;
            }
            Object getBit = this.jedis.getbit(key, offset);
            if (getBit instanceof Long) {
                return ((Long) getBit).longValue() == 0 ? Boolean.FALSE : Boolean.TRUE;
            }
            return (Boolean) getBit;
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Boolean setBit(byte[] key, long offset, boolean value) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.setbit(key, offset, JedisConverters.toBit(Boolean.valueOf(value)))));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.setbit(key, offset, JedisConverters.toBit(Boolean.valueOf(value)))));
                return null;
            }
            return this.jedis.setbit(key, offset, JedisConverters.toBit(Boolean.valueOf(value)));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void setRange(byte[] key, byte[] value, long start) {
        try {
            if (isPipelined()) {
                pipeline(new JedisStatusResult(this.pipeline.setrange(key, start, value)));
            } else if (isQueueing()) {
                transaction(new JedisStatusResult(this.transaction.setrange(key, start, value)));
            } else {
                this.jedis.setrange(key, start, value);
            }
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long strLen(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.strlen(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.strlen(key)));
                return null;
            }
            return this.jedis.strlen(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long bitCount(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.bitcount(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.bitcount(key)));
                return null;
            }
            return this.jedis.bitcount(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long bitCount(byte[] key, long begin, long end) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.bitcount(key, begin, end)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.bitcount(key, begin, end)));
                return null;
            }
            return this.jedis.bitcount(key, begin, end);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long bitOp(RedisStringCommands.BitOperation op, byte[] destination, byte[]... keys) {
        if (op == RedisStringCommands.BitOperation.NOT && keys.length > 1) {
            throw new UnsupportedOperationException("Bitop NOT should only be performed against one key");
        }
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.bitop(JedisConverters.toBitOp(op), destination, keys)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.bitop(JedisConverters.toBitOp(op), destination, keys)));
                return null;
            }
            return this.jedis.bitop(JedisConverters.toBitOp(op), destination, keys);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lPush(byte[] key, byte[]... values) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.lpush(key, values)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.lpush(key, values)));
                return null;
            }
            return this.jedis.lpush(key, values);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long rPush(byte[] key, byte[]... values) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.rpush(key, values)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.rpush(key, values)));
                return null;
            }
            return this.jedis.rpush(key, values);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public List<byte[]> bLPop(int timeout, byte[]... keys) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.blpop(bXPopArgs(timeout, keys))));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.blpop(bXPopArgs(timeout, keys))));
                return null;
            }
            return this.jedis.blpop(timeout, keys);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public List<byte[]> bRPop(int timeout, byte[]... keys) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.brpop(bXPopArgs(timeout, keys))));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.brpop(bXPopArgs(timeout, keys))));
                return null;
            }
            return this.jedis.brpop(timeout, keys);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public byte[] lIndex(byte[] key, long index) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.lindex(key, index)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.lindex(key, index)));
                return null;
            }
            return this.jedis.lindex(key, index);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lInsert(byte[] key, RedisListCommands.Position where, byte[] pivot, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.linsert(key, JedisConverters.toListPosition(where), pivot, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.linsert(key, JedisConverters.toListPosition(where), pivot, value)));
                return null;
            }
            return this.jedis.linsert(key, JedisConverters.toListPosition(where), pivot, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lLen(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.llen(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.llen(key)));
                return null;
            }
            return this.jedis.llen(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public byte[] lPop(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.lpop(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.lpop(key)));
                return null;
            }
            return this.jedis.lpop(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public List<byte[]> lRange(byte[] key, long start, long end) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.lrange(key, start, end)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.lrange(key, start, end)));
                return null;
            }
            return this.jedis.lrange(key, start, end);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lRem(byte[] key, long count, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.lrem(key, count, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.lrem(key, count, value)));
                return null;
            }
            return this.jedis.lrem(key, count, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public void lSet(byte[] key, long index, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new JedisStatusResult(this.pipeline.lset(key, index, value)));
            } else if (isQueueing()) {
                transaction(new JedisStatusResult(this.transaction.lset(key, index, value)));
            } else {
                this.jedis.lset(key, index, value);
            }
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public void lTrim(byte[] key, long start, long end) {
        try {
            if (isPipelined()) {
                pipeline(new JedisStatusResult(this.pipeline.ltrim(key, start, end)));
            } else if (isQueueing()) {
                transaction(new JedisStatusResult(this.transaction.ltrim(key, start, end)));
            } else {
                this.jedis.ltrim(key, start, end);
            }
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public byte[] rPop(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.rpop(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.rpop(key)));
                return null;
            }
            return this.jedis.rpop(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public byte[] rPopLPush(byte[] srcKey, byte[] dstKey) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.rpoplpush(srcKey, dstKey)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.rpoplpush(srcKey, dstKey)));
                return null;
            }
            return this.jedis.rpoplpush(srcKey, dstKey);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public byte[] bRPopLPush(int timeout, byte[] srcKey, byte[] dstKey) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.brpoplpush(srcKey, dstKey, timeout)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.brpoplpush(srcKey, dstKey, timeout)));
                return null;
            }
            return this.jedis.brpoplpush(srcKey, dstKey, timeout);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r6v1, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r6v3, types: [byte[], byte[][]] */
    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lPushX(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.lpushx(key, (byte[][]) new byte[]{value})));
                return null;
            }
            if (!isQueueing()) {
                return this.jedis.lpushx(key, (byte[][]) new byte[]{value});
            }
            transaction(new JedisResult(this.transaction.lpushx(key, (byte[][]) new byte[]{value})));
            return null;
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r6v1, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r6v3, types: [byte[], byte[][]] */
    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long rPushX(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.rpushx(key, (byte[][]) new byte[]{value})));
                return null;
            }
            if (!isQueueing()) {
                return this.jedis.rpushx(key, (byte[][]) new byte[]{value});
            }
            transaction(new JedisResult(this.transaction.rpushx(key, (byte[][]) new byte[]{value})));
            return null;
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sAdd(byte[] key, byte[]... values) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.sadd(key, values)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.sadd(key, values)));
                return null;
            }
            return this.jedis.sadd(key, values);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sCard(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.scard(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.scard(key)));
                return null;
            }
            return this.jedis.scard(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Set<byte[]> sDiff(byte[]... keys) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.sdiff(keys)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.sdiff(keys)));
                return null;
            }
            return this.jedis.sdiff(keys);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sDiffStore(byte[] destKey, byte[]... keys) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.sdiffstore(destKey, keys)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.sdiffstore(destKey, keys)));
                return null;
            }
            return this.jedis.sdiffstore(destKey, keys);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Set<byte[]> sInter(byte[]... keys) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.sinter(keys)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.sinter(keys)));
                return null;
            }
            return this.jedis.sinter(keys);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sInterStore(byte[] destKey, byte[]... keys) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.sinterstore(destKey, keys)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.sinterstore(destKey, keys)));
                return null;
            }
            return this.jedis.sinterstore(destKey, keys);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Boolean sIsMember(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.sismember(key, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.sismember(key, value)));
                return null;
            }
            return this.jedis.sismember(key, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Set<byte[]> sMembers(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.smembers(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.smembers(key)));
                return null;
            }
            return this.jedis.smembers(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Boolean sMove(byte[] srcKey, byte[] destKey, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.smove(srcKey, destKey, value), JedisConverters.longToBoolean()));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.smove(srcKey, destKey, value), JedisConverters.longToBoolean()));
                return null;
            }
            return JedisConverters.toBoolean(this.jedis.smove(srcKey, destKey, value));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public byte[] sPop(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.spop(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.spop(key)));
                return null;
            }
            return this.jedis.spop(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public byte[] sRandMember(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.srandmember(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.srandmember(key)));
                return null;
            }
            return this.jedis.srandmember(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public List<byte[]> sRandMember(byte[] key, long count) {
        if (count > 2147483647L) {
            throw new IllegalArgumentException("Count must be less than Integer.MAX_VALUE for sRandMember in Jedis.");
        }
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.srandmember(key, (int) count)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.srandmember(key, (int) count)));
                return null;
            }
            return this.jedis.srandmember(key, (int) count);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sRem(byte[] key, byte[]... values) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.srem(key, values)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.srem(key, values)));
                return null;
            }
            return this.jedis.srem(key, values);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Set<byte[]> sUnion(byte[]... keys) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.sunion(keys)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.sunion(keys)));
                return null;
            }
            return this.jedis.sunion(keys);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sUnionStore(byte[] destKey, byte[]... keys) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.sunionstore(destKey, keys)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.sunionstore(destKey, keys)));
                return null;
            }
            return this.jedis.sunionstore(destKey, keys);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Boolean zAdd(byte[] key, double score, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.zadd(key, score, value), JedisConverters.longToBoolean()));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.zadd(key, score, value), JedisConverters.longToBoolean()));
                return null;
            }
            return JedisConverters.toBoolean(this.jedis.zadd(key, score, value));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zAdd(byte[] key, Set<RedisZSetCommands.Tuple> tuples) {
        Map<byte[], Double> mappedTuples = JedisConverters.toTupleMap(tuples);
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.zadd(key, mappedTuples)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.zadd(key, mappedTuples)));
                return null;
            }
            return this.jedis.zadd(key, mappedTuples);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zCard(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.zcard(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.zcard(key)));
                return null;
            }
            return this.jedis.zcard(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zCount(byte[] key, double min, double max) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.zcount(key, min, max)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.zcount(key, min, max)));
                return null;
            }
            return this.jedis.zcount(key, min, max);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zCount(byte[] key, RedisZSetCommands.Range range) {
        if (isPipelined() || isQueueing()) {
            throw new UnsupportedOperationException("ZCOUNT not implemented in jedis for binary protocol on transaction and pipeline");
        }
        byte[] min = JedisConverters.boundaryToBytesForZRange(range.getMin(), JedisConverters.NEGATIVE_INFINITY_BYTES);
        byte[] max = JedisConverters.boundaryToBytesForZRange(range.getMax(), JedisConverters.POSITIVE_INFINITY_BYTES);
        return this.jedis.zcount(key, min, max);
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Double zIncrBy(byte[] key, double increment, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.zincrby(key, increment, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.zincrby(key, increment, value)));
                return null;
            }
            return this.jedis.zincrby(key, increment, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zInterStore(byte[] destKey, RedisZSetCommands.Aggregate aggregate, int[] weights, byte[]... sets) {
        try {
            ZParams zparams = new ZParams().weights(weights).aggregate(ZParams.Aggregate.valueOf(aggregate.name()));
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.zinterstore(destKey, zparams, sets)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.zinterstore(destKey, zparams, sets)));
                return null;
            }
            return this.jedis.zinterstore(destKey, zparams, sets);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zInterStore(byte[] destKey, byte[]... sets) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.zinterstore(destKey, sets)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.zinterstore(destKey, sets)));
                return null;
            }
            return this.jedis.zinterstore(destKey, sets);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRange(byte[] key, long start, long end) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.zrange(key, start, end)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.zrange(key, start, end)));
                return null;
            }
            return this.jedis.zrange(key, start, end);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRangeWithScores(byte[] key, long start, long end) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.zrangeWithScores(key, start, end), JedisConverters.tupleSetToTupleSet()));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.zrangeWithScores(key, start, end), JedisConverters.tupleSetToTupleSet()));
                return null;
            }
            return JedisConverters.toTupleSet(this.jedis.zrangeWithScores(key, start, end));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByLex(byte[] key) {
        return zRangeByLex(key, RedisZSetCommands.Range.unbounded());
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByLex(byte[] key, RedisZSetCommands.Range range) {
        return zRangeByLex(key, range, null);
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByLex(byte[] key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        Assert.notNull(range, "Range cannot be null for ZRANGEBYLEX.");
        byte[] min = JedisConverters.boundaryToBytesForZRangeByLex(range.getMin(), JedisConverters.MINUS_BYTES);
        byte[] max = JedisConverters.boundaryToBytesForZRangeByLex(range.getMax(), JedisConverters.PLUS_BYTES);
        try {
            if (isPipelined()) {
                if (limit != null) {
                    pipeline(new JedisResult(this.pipeline.zrangeByLex(key, min, max, limit.getOffset(), limit.getCount())));
                    return null;
                }
                pipeline(new JedisResult(this.pipeline.zrangeByLex(key, min, max)));
                return null;
            }
            if (isQueueing()) {
                if (limit != null) {
                    transaction(new JedisResult(this.transaction.zrangeByLex(key, min, max, limit.getOffset(), limit.getCount())));
                    return null;
                }
                transaction(new JedisResult(this.transaction.zrangeByLex(key, min, max)));
                return null;
            }
            if (limit != null) {
                return this.jedis.zrangeByLex(key, min, max, limit.getOffset(), limit.getCount());
            }
            return this.jedis.zrangeByLex(key, min, max);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByScore(byte[] key, double min, double max) {
        return zRangeByScore(key, new RedisZSetCommands.Range().gte(Double.valueOf(min)).lte(Double.valueOf(max)));
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByScore(byte[] key, RedisZSetCommands.Range range) {
        return zRangeByScore(key, range, (RedisZSetCommands.Limit) null);
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByScore(byte[] key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        Assert.notNull(range, "Range cannot be null for ZRANGEBYSCORE.");
        byte[] min = JedisConverters.boundaryToBytesForZRange(range.getMin(), JedisConverters.NEGATIVE_INFINITY_BYTES);
        byte[] max = JedisConverters.boundaryToBytesForZRange(range.getMax(), JedisConverters.POSITIVE_INFINITY_BYTES);
        try {
            if (isPipelined()) {
                if (limit != null) {
                    pipeline(new JedisResult(this.pipeline.zrangeByScore(key, min, max, limit.getOffset(), limit.getCount())));
                    return null;
                }
                pipeline(new JedisResult(this.pipeline.zrangeByScore(key, min, max)));
                return null;
            }
            if (isQueueing()) {
                if (limit != null) {
                    transaction(new JedisResult(this.transaction.zrangeByScore(key, min, max, limit.getOffset(), limit.getCount())));
                    return null;
                }
                transaction(new JedisResult(this.transaction.zrangeByScore(key, min, max)));
                return null;
            }
            if (limit != null) {
                return this.jedis.zrangeByScore(key, min, max, limit.getOffset(), limit.getCount());
            }
            return this.jedis.zrangeByScore(key, min, max);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRangeByScoreWithScores(byte[] key, double min, double max) {
        return zRangeByScoreWithScores(key, new RedisZSetCommands.Range().gte(Double.valueOf(min)).lte(Double.valueOf(max)));
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRangeByScoreWithScores(byte[] key, RedisZSetCommands.Range range) {
        return zRangeByScoreWithScores(key, range, (RedisZSetCommands.Limit) null);
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRangeByScoreWithScores(byte[] key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        Assert.notNull(range, "Range cannot be null for ZRANGEBYSCOREWITHSCORES.");
        byte[] min = JedisConverters.boundaryToBytesForZRange(range.getMin(), JedisConverters.NEGATIVE_INFINITY_BYTES);
        byte[] max = JedisConverters.boundaryToBytesForZRange(range.getMax(), JedisConverters.POSITIVE_INFINITY_BYTES);
        try {
            if (isPipelined()) {
                if (limit != null) {
                    pipeline(new JedisResult(this.pipeline.zrangeByScoreWithScores(key, min, max, limit.getOffset(), limit.getCount()), JedisConverters.tupleSetToTupleSet()));
                    return null;
                }
                pipeline(new JedisResult(this.pipeline.zrangeByScoreWithScores(key, min, max), JedisConverters.tupleSetToTupleSet()));
                return null;
            }
            if (isQueueing()) {
                if (limit != null) {
                    transaction(new JedisResult(this.transaction.zrangeByScoreWithScores(key, min, max, limit.getOffset(), limit.getCount()), JedisConverters.tupleSetToTupleSet()));
                    return null;
                }
                transaction(new JedisResult(this.transaction.zrangeByScoreWithScores(key, min, max), JedisConverters.tupleSetToTupleSet()));
                return null;
            }
            if (limit != null) {
                return JedisConverters.toTupleSet(this.jedis.zrangeByScoreWithScores(key, min, max, limit.getOffset(), limit.getCount()));
            }
            return JedisConverters.toTupleSet(this.jedis.zrangeByScoreWithScores(key, min, max));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRevRangeWithScores(byte[] key, long start, long end) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.zrevrangeWithScores(key, start, end), JedisConverters.tupleSetToTupleSet()));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.zrevrangeWithScores(key, start, end), JedisConverters.tupleSetToTupleSet()));
                return null;
            }
            return JedisConverters.toTupleSet(this.jedis.zrevrangeWithScores(key, start, end));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByScore(byte[] key, double min, double max, long offset, long count) {
        return zRangeByScore(key, new RedisZSetCommands.Range().gte(Double.valueOf(min)).lte(Double.valueOf(max)), new RedisZSetCommands.Limit().offset(Long.valueOf(offset).intValue()).count(Long.valueOf(count).intValue()));
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRangeByScoreWithScores(byte[] key, double min, double max, long offset, long count) {
        return zRangeByScoreWithScores(key, new RedisZSetCommands.Range().gte(Double.valueOf(min)).lte(Double.valueOf(max)), new RedisZSetCommands.Limit().offset(Long.valueOf(offset).intValue()).count(Long.valueOf(count).intValue()));
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRevRangeByScore(byte[] key, double min, double max, long offset, long count) {
        return zRevRangeByScore(key, new RedisZSetCommands.Range().gte(Double.valueOf(min)).lte(Double.valueOf(max)), new RedisZSetCommands.Limit().offset(Long.valueOf(offset).intValue()).count(Long.valueOf(count).intValue()));
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRevRangeByScore(byte[] key, double min, double max) {
        return zRevRangeByScore(key, new RedisZSetCommands.Range().gte(Double.valueOf(min)).lte(Double.valueOf(max)));
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRevRangeByScore(byte[] key, RedisZSetCommands.Range range) {
        return zRevRangeByScore(key, range, (RedisZSetCommands.Limit) null);
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRevRangeByScore(byte[] key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        Assert.notNull(range, "Range cannot be null for ZREVRANGEBYSCORE.");
        byte[] min = JedisConverters.boundaryToBytesForZRange(range.getMin(), JedisConverters.NEGATIVE_INFINITY_BYTES);
        byte[] max = JedisConverters.boundaryToBytesForZRange(range.getMax(), JedisConverters.POSITIVE_INFINITY_BYTES);
        try {
            if (isPipelined()) {
                if (limit != null) {
                    pipeline(new JedisResult(this.pipeline.zrevrangeByScore(key, max, min, limit.getOffset(), limit.getCount())));
                    return null;
                }
                pipeline(new JedisResult(this.pipeline.zrevrangeByScore(key, max, min)));
                return null;
            }
            if (isQueueing()) {
                if (limit != null) {
                    transaction(new JedisResult(this.transaction.zrevrangeByScore(key, max, min, limit.getOffset(), limit.getCount())));
                    return null;
                }
                transaction(new JedisResult(this.transaction.zrevrangeByScore(key, max, min)));
                return null;
            }
            if (limit != null) {
                return this.jedis.zrevrangeByScore(key, max, min, limit.getOffset(), limit.getCount());
            }
            return this.jedis.zrevrangeByScore(key, max, min);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRevRangeByScoreWithScores(byte[] key, double min, double max, long offset, long count) {
        return zRevRangeByScoreWithScores(key, new RedisZSetCommands.Range().gte(Double.valueOf(min)).lte(Double.valueOf(max)), new RedisZSetCommands.Limit().offset(Long.valueOf(offset).intValue()).count(Long.valueOf(count).intValue()));
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRevRangeByScoreWithScores(byte[] key, RedisZSetCommands.Range range) {
        return zRevRangeByScoreWithScores(key, range, (RedisZSetCommands.Limit) null);
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRevRangeByScoreWithScores(byte[] key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        Assert.notNull(range, "Range cannot be null for ZREVRANGEBYSCOREWITHSCORES.");
        byte[] min = JedisConverters.boundaryToBytesForZRange(range.getMin(), JedisConverters.NEGATIVE_INFINITY_BYTES);
        byte[] max = JedisConverters.boundaryToBytesForZRange(range.getMax(), JedisConverters.POSITIVE_INFINITY_BYTES);
        try {
            if (isPipelined()) {
                if (limit != null) {
                    pipeline(new JedisResult(this.pipeline.zrevrangeByScoreWithScores(key, max, min, limit.getOffset(), limit.getCount()), JedisConverters.tupleSetToTupleSet()));
                    return null;
                }
                pipeline(new JedisResult(this.pipeline.zrevrangeByScoreWithScores(key, max, min), JedisConverters.tupleSetToTupleSet()));
                return null;
            }
            if (isQueueing()) {
                if (limit != null) {
                    transaction(new JedisResult(this.transaction.zrevrangeByScoreWithScores(key, max, min, limit.getOffset(), limit.getCount()), JedisConverters.tupleSetToTupleSet()));
                    return null;
                }
                transaction(new JedisResult(this.transaction.zrevrangeByScoreWithScores(key, max, min), JedisConverters.tupleSetToTupleSet()));
                return null;
            }
            if (limit != null) {
                return JedisConverters.toTupleSet(this.jedis.zrevrangeByScoreWithScores(key, max, min, limit.getOffset(), limit.getCount()));
            }
            return JedisConverters.toTupleSet(this.jedis.zrevrangeByScoreWithScores(key, max, min));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRevRangeByScoreWithScores(byte[] key, double min, double max) {
        return zRevRangeByScoreWithScores(key, new RedisZSetCommands.Range().gte(Double.valueOf(min)).lte(Double.valueOf(max)), (RedisZSetCommands.Limit) null);
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRank(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.zrank(key, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.zrank(key, value)));
                return null;
            }
            return this.jedis.zrank(key, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRem(byte[] key, byte[]... values) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.zrem(key, values)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.zrem(key, values)));
                return null;
            }
            return this.jedis.zrem(key, values);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRemRange(byte[] key, long start, long end) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.zremrangeByRank(key, start, end)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.zremrangeByRank(key, start, end)));
                return null;
            }
            return this.jedis.zremrangeByRank(key, start, end);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRemRangeByScore(byte[] key, double min, double max) {
        return zRemRangeByScore(key, new RedisZSetCommands.Range().gte(Double.valueOf(min)).lte(Double.valueOf(max)));
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRemRangeByScore(byte[] key, RedisZSetCommands.Range range) {
        Assert.notNull(range, "Range cannot be null for ZREMRANGEBYSCORE.");
        byte[] min = JedisConverters.boundaryToBytesForZRange(range.getMin(), JedisConverters.NEGATIVE_INFINITY_BYTES);
        byte[] max = JedisConverters.boundaryToBytesForZRange(range.getMax(), JedisConverters.POSITIVE_INFINITY_BYTES);
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.zremrangeByScore(key, min, max)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.zremrangeByScore(key, min, max)));
                return null;
            }
            return this.jedis.zremrangeByScore(key, min, max);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRevRange(byte[] key, long start, long end) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.zrevrange(key, start, end)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.zrevrange(key, start, end)));
                return null;
            }
            return this.jedis.zrevrange(key, start, end);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRevRank(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.zrevrank(key, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.zrevrank(key, value)));
                return null;
            }
            return this.jedis.zrevrank(key, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Double zScore(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.zscore(key, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.zscore(key, value)));
                return null;
            }
            return this.jedis.zscore(key, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zUnionStore(byte[] destKey, RedisZSetCommands.Aggregate aggregate, int[] weights, byte[]... sets) {
        try {
            ZParams zparams = new ZParams().weights(weights).aggregate(ZParams.Aggregate.valueOf(aggregate.name()));
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.zunionstore(destKey, zparams, sets)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.zunionstore(destKey, zparams, sets)));
                return null;
            }
            return this.jedis.zunionstore(destKey, zparams, sets);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zUnionStore(byte[] destKey, byte[]... sets) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.zunionstore(destKey, sets)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.zunionstore(destKey, sets)));
                return null;
            }
            return this.jedis.zunionstore(destKey, sets);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Boolean hSet(byte[] key, byte[] field, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.hset(key, field, value), JedisConverters.longToBoolean()));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.hset(key, field, value), JedisConverters.longToBoolean()));
                return null;
            }
            return JedisConverters.toBoolean(this.jedis.hset(key, field, value));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Boolean hSetNX(byte[] key, byte[] field, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.hsetnx(key, field, value), JedisConverters.longToBoolean()));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.hsetnx(key, field, value), JedisConverters.longToBoolean()));
                return null;
            }
            return JedisConverters.toBoolean(this.jedis.hsetnx(key, field, value));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Long hDel(byte[] key, byte[]... fields) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.hdel(key, fields)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.hdel(key, fields)));
                return null;
            }
            return this.jedis.hdel(key, fields);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Boolean hExists(byte[] key, byte[] field) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.hexists(key, field)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.hexists(key, field)));
                return null;
            }
            return this.jedis.hexists(key, field);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public byte[] hGet(byte[] key, byte[] field) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.hget(key, field)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.hget(key, field)));
                return null;
            }
            return this.jedis.hget(key, field);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Map<byte[], byte[]> hGetAll(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.hgetAll(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.hgetAll(key)));
                return null;
            }
            return this.jedis.hgetAll(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Long hIncrBy(byte[] key, byte[] field, long delta) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.hincrBy(key, field, delta)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.hincrBy(key, field, delta)));
                return null;
            }
            return this.jedis.hincrBy(key, field, delta);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Double hIncrBy(byte[] key, byte[] field, double delta) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.hincrByFloat(key, field, delta)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.hincrByFloat(key, field, delta)));
                return null;
            }
            return this.jedis.hincrByFloat(key, field, delta);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Set<byte[]> hKeys(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.hkeys(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.hkeys(key)));
                return null;
            }
            return this.jedis.hkeys(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Long hLen(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.hlen(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.hlen(key)));
                return null;
            }
            return this.jedis.hlen(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public List<byte[]> hMGet(byte[] key, byte[]... fields) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.hmget(key, fields)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.hmget(key, fields)));
                return null;
            }
            return this.jedis.hmget(key, fields);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public void hMSet(byte[] key, Map<byte[], byte[]> tuple) {
        try {
            if (isPipelined()) {
                pipeline(new JedisStatusResult(this.pipeline.hmset(key, tuple)));
            } else if (isQueueing()) {
                transaction(new JedisStatusResult(this.transaction.hmset(key, tuple)));
            } else {
                this.jedis.hmset(key, tuple);
            }
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public List<byte[]> hVals(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.hvals(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.hvals(key)));
                return null;
            }
            return this.jedis.hvals(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisPubSubCommands
    public Long publish(byte[] channel, byte[] message) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.publish(channel, message)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.publish(channel, message)));
                return null;
            }
            return this.jedis.publish(channel, message);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisPubSubCommands
    public Subscription getSubscription() {
        return this.subscription;
    }

    @Override // org.springframework.data.redis.connection.RedisPubSubCommands
    public boolean isSubscribed() {
        return this.subscription != null && this.subscription.isAlive();
    }

    @Override // org.springframework.data.redis.connection.RedisPubSubCommands
    public void pSubscribe(MessageListener listener, byte[]... patterns) {
        if (isSubscribed()) {
            throw new RedisSubscribedConnectionException("Connection already subscribed; use the connection Subscription to cancel or add new channels");
        }
        if (isQueueing()) {
            throw new UnsupportedOperationException();
        }
        if (isPipelined()) {
            throw new UnsupportedOperationException();
        }
        try {
            BinaryJedisPubSub jedisPubSub = new JedisMessageListener(listener);
            this.subscription = new JedisSubscription(listener, jedisPubSub, (byte[][]) null, patterns);
            this.jedis.psubscribe(jedisPubSub, patterns);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisPubSubCommands
    public void subscribe(MessageListener listener, byte[]... channels) {
        if (isSubscribed()) {
            throw new RedisSubscribedConnectionException("Connection already subscribed; use the connection Subscription to cancel or add new channels");
        }
        if (isQueueing()) {
            throw new UnsupportedOperationException();
        }
        if (isPipelined()) {
            throw new UnsupportedOperationException();
        }
        try {
            BinaryJedisPubSub jedisPubSub = new JedisMessageListener(listener);
            this.subscription = new JedisSubscription(listener, jedisPubSub, channels, (byte[][]) null);
            this.jedis.subscribe(jedisPubSub, channels);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoAdd(byte[] key, Point point, byte[] member) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(point, "Point must not be null!");
        Assert.notNull(member, "Member must not be null!");
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.geoadd(key, point.getX(), point.getY(), member)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.geoadd(key, point.getX(), point.getY(), member)));
                return null;
            }
            return this.jedis.geoadd(key, point.getX(), point.getY(), member);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoAdd(byte[] key, RedisGeoCommands.GeoLocation<byte[]> location) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(location, "Location must not be null!");
        return geoAdd(key, location.getPoint(), location.getName());
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoAdd(byte[] key, Map<byte[], Point> memberCoordinateMap) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(memberCoordinateMap, "MemberCoordinateMap must not be null!");
        Map<byte[], GeoCoordinate> redisGeoCoordinateMap = new HashMap<>();
        for (byte[] mapKey : memberCoordinateMap.keySet()) {
            redisGeoCoordinateMap.put(mapKey, JedisConverters.toGeoCoordinate(memberCoordinateMap.get(mapKey)));
        }
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.geoadd(key, redisGeoCoordinateMap)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.geoadd(key, redisGeoCoordinateMap)));
                return null;
            }
            return this.jedis.geoadd(key, redisGeoCoordinateMap);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoAdd(byte[] key, Iterable<RedisGeoCommands.GeoLocation<byte[]>> locations) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(locations, "Locations must not be null!");
        Map<byte[], GeoCoordinate> redisGeoCoordinateMap = new HashMap<>();
        for (RedisGeoCommands.GeoLocation<byte[]> location : locations) {
            redisGeoCoordinateMap.put(location.getName(), JedisConverters.toGeoCoordinate(location.getPoint()));
        }
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.geoadd(key, redisGeoCoordinateMap)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.geoadd(key, redisGeoCoordinateMap)));
                return null;
            }
            return this.jedis.geoadd(key, redisGeoCoordinateMap);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Distance geoDist(byte[] key, byte[] member1, byte[] member2) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(member1, "Member1 must not be null!");
        Assert.notNull(member2, "Member2 must not be null!");
        Converter<Double, Distance> distanceConverter = JedisConverters.distanceConverterForMetric(RedisGeoCommands.DistanceUnit.METERS);
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.geodist(key, member1, member2), distanceConverter));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.geodist(key, member1, member2), distanceConverter));
                return null;
            }
            return distanceConverter.convert2(this.jedis.geodist(key, member1, member2));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Distance geoDist(byte[] key, byte[] member1, byte[] member2, Metric metric) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(member1, "Member1 must not be null!");
        Assert.notNull(member2, "Member2 must not be null!");
        Assert.notNull(metric, "Metric must not be null!");
        GeoUnit geoUnit = JedisConverters.toGeoUnit(metric);
        Converter<Double, Distance> distanceConverter = JedisConverters.distanceConverterForMetric(metric);
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.geodist(key, member1, member2, geoUnit), distanceConverter));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.geodist(key, member1, member2, geoUnit), distanceConverter));
                return null;
            }
            return distanceConverter.convert2(this.jedis.geodist(key, member1, member2, geoUnit));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public List<String> geoHash(byte[] key, byte[]... members) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(members, "Members must not be null!");
        Assert.noNullElements(members, "Members must not contain null!");
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.geohash(key, members), JedisConverters.bytesListToStringListConverter()));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.geohash(key, members), JedisConverters.bytesListToStringListConverter()));
                return null;
            }
            return JedisConverters.bytesListToStringListConverter().convert(this.jedis.geohash(key, members));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public List<Point> geoPos(byte[] key, byte[]... members) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(members, "Members must not be null!");
        Assert.noNullElements(members, "Members must not contain null!");
        ListConverter<GeoCoordinate, Point> converter = JedisConverters.geoCoordinateToPointConverter();
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.geopos(key, members), converter));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.geopos(key, members), converter));
                return null;
            }
            return converter.convert(this.jedis.geopos(key, members));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> geoRadius(byte[] key, Circle within) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(within, "Within must not be null!");
        Converter<List<GeoRadiusResponse>, GeoResults<RedisGeoCommands.GeoLocation<byte[]>>> converter = JedisConverters.geoRadiusResponseToGeoResultsConverter(within.getRadius().getMetric());
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.georadius(key, within.getCenter().getX(), within.getCenter().getY(), within.getRadius().getValue(), JedisConverters.toGeoUnit(within.getRadius().getMetric())), converter));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.georadius(key, within.getCenter().getX(), within.getCenter().getY(), within.getRadius().getValue(), JedisConverters.toGeoUnit(within.getRadius().getMetric())), converter));
                return null;
            }
            return converter.convert2(this.jedis.georadius(key, within.getCenter().getX(), within.getCenter().getY(), within.getRadius().getValue(), JedisConverters.toGeoUnit(within.getRadius().getMetric())));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> geoRadius(byte[] key, Circle within, RedisGeoCommands.GeoRadiusCommandArgs args) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(within, "Within must not be null!");
        Assert.notNull(args, "Args must not be null!");
        GeoRadiusParam geoRadiusParam = JedisConverters.toGeoRadiusParam(args);
        Converter<List<GeoRadiusResponse>, GeoResults<RedisGeoCommands.GeoLocation<byte[]>>> converter = JedisConverters.geoRadiusResponseToGeoResultsConverter(within.getRadius().getMetric());
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.georadius(key, within.getCenter().getX(), within.getCenter().getY(), within.getRadius().getValue(), JedisConverters.toGeoUnit(within.getRadius().getMetric()), geoRadiusParam), converter));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.georadius(key, within.getCenter().getX(), within.getCenter().getY(), within.getRadius().getValue(), JedisConverters.toGeoUnit(within.getRadius().getMetric()), geoRadiusParam), converter));
                return null;
            }
            return converter.convert2(this.jedis.georadius(key, within.getCenter().getX(), within.getCenter().getY(), within.getRadius().getValue(), JedisConverters.toGeoUnit(within.getRadius().getMetric()), geoRadiusParam));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, double radius) {
        return geoRadiusByMember(key, member, new Distance(radius, RedisGeoCommands.DistanceUnit.METERS));
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, Distance radius) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(member, "Member must not be null!");
        Assert.notNull(radius, "Radius must not be null!");
        GeoUnit geoUnit = JedisConverters.toGeoUnit(radius.getMetric());
        Converter<List<GeoRadiusResponse>, GeoResults<RedisGeoCommands.GeoLocation<byte[]>>> converter = JedisConverters.geoRadiusResponseToGeoResultsConverter(radius.getMetric());
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.georadiusByMember(key, member, radius.getValue(), geoUnit), converter));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.georadiusByMember(key, member, radius.getValue(), geoUnit), converter));
                return null;
            }
            return converter.convert2(this.jedis.georadiusByMember(key, member, radius.getValue(), geoUnit));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, Distance radius, RedisGeoCommands.GeoRadiusCommandArgs args) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(member, "Member must not be null!");
        Assert.notNull(radius, "Radius must not be null!");
        Assert.notNull(args, "Args must not be null!");
        GeoUnit geoUnit = JedisConverters.toGeoUnit(radius.getMetric());
        Converter<List<GeoRadiusResponse>, GeoResults<RedisGeoCommands.GeoLocation<byte[]>>> converter = JedisConverters.geoRadiusResponseToGeoResultsConverter(radius.getMetric());
        GeoRadiusParam geoRadiusParam = JedisConverters.toGeoRadiusParam(args);
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.georadiusByMember(key, member, radius.getValue(), geoUnit, geoRadiusParam), converter));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.georadiusByMember(key, member, radius.getValue(), geoUnit, geoRadiusParam), converter));
                return null;
            }
            return converter.convert2(this.jedis.georadiusByMember(key, member, radius.getValue(), geoUnit, geoRadiusParam));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoRemove(byte[] key, byte[]... members) {
        return zRem(key, members);
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public void scriptFlush() {
        if (isQueueing()) {
            throw new UnsupportedOperationException();
        }
        if (isPipelined()) {
            throw new UnsupportedOperationException();
        }
        try {
            this.jedis.scriptFlush();
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public void scriptKill() {
        if (isQueueing()) {
            throw new UnsupportedOperationException();
        }
        if (isPipelined()) {
            throw new UnsupportedOperationException();
        }
        try {
            this.jedis.scriptKill();
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public String scriptLoad(byte[] script) {
        if (isQueueing()) {
            throw new UnsupportedOperationException();
        }
        if (isPipelined()) {
            throw new UnsupportedOperationException();
        }
        try {
            return JedisConverters.toString(this.jedis.scriptLoad(script));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public List<Boolean> scriptExists(String... scriptSha1) {
        if (isQueueing()) {
            throw new UnsupportedOperationException();
        }
        if (isPipelined()) {
            throw new UnsupportedOperationException();
        }
        try {
            return this.jedis.scriptExists(scriptSha1);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public <T> T eval(byte[] bArr, ReturnType returnType, int i, byte[]... bArr2) {
        if (isQueueing()) {
            throw new UnsupportedOperationException();
        }
        if (isPipelined()) {
            throw new UnsupportedOperationException();
        }
        try {
            return (T) new JedisScriptReturnConverter(returnType).convert2(this.jedis.eval(bArr, JedisConverters.toBytes(Integer.valueOf(i)), bArr2));
        } catch (Exception e) {
            throw convertJedisAccessException(e);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public <T> T evalSha(String str, ReturnType returnType, int i, byte[]... bArr) {
        return (T) evalSha(JedisConverters.toBytes(str), returnType, i, bArr);
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public <T> T evalSha(byte[] bArr, ReturnType returnType, int i, byte[]... bArr2) {
        if (isQueueing()) {
            throw new UnsupportedOperationException();
        }
        if (isPipelined()) {
            throw new UnsupportedOperationException();
        }
        try {
            return (T) new JedisScriptReturnConverter(returnType).convert2(this.jedis.evalsha(bArr, i, bArr2));
        } catch (Exception e) {
            throw convertJedisAccessException(e);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public Long time() {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.time(), JedisConverters.toTimeConverter()));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.time(), JedisConverters.toTimeConverter()));
                return null;
            }
            return JedisConverters.toTimeConverter().convert2(this.jedis.time());
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void killClient(String host, int port) {
        Assert.hasText(host, "Host for 'CLIENT KILL' must not be 'null' or 'empty'.");
        if (isQueueing() || isPipelined()) {
            throw new UnsupportedOperationException("'CLIENT KILL' is not supported in transaction / pipline mode.");
        }
        try {
            this.jedis.clientKill(String.format("%s:%s", host, Integer.valueOf(port)));
        } catch (Exception e) {
            throw convertJedisAccessException(e);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void slaveOf(String host, int port) {
        Assert.hasText(host, "Host must not be null for 'SLAVEOF' command.");
        if (isQueueing() || isPipelined()) {
            throw new UnsupportedOperationException("'SLAVEOF' cannot be called in pipline / transaction mode.");
        }
        try {
            this.jedis.slaveof(host, port);
        } catch (Exception e) {
            throw convertJedisAccessException(e);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void setClientName(byte[] name) {
        if (isPipelined() || isQueueing()) {
            throw new UnsupportedOperationException("'CLIENT SETNAME' is not suppored in transacton / pipeline mode.");
        }
        this.jedis.clientSetname(name);
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public String getClientName() {
        if (isPipelined() || isQueueing()) {
            throw new UnsupportedOperationException();
        }
        return this.jedis.clientGetname();
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public List<RedisClientInfo> getClientList() {
        if (isQueueing() || isPipelined()) {
            throw new UnsupportedOperationException("'CLIENT LIST' is not supported in in pipeline / multi mode.");
        }
        return JedisConverters.toListOfRedisClientInformation(this.jedis.clientList());
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void slaveOfNoOne() {
        if (isQueueing() || isPipelined()) {
            throw new UnsupportedOperationException("'SLAVEOF' cannot be called in pipline / transaction mode.");
        }
        try {
            this.jedis.slaveofNoOne();
        } catch (Exception e) {
            throw convertJedisAccessException(e);
        }
    }

    public Cursor<byte[]> scan() {
        return scan(ScanOptions.NONE);
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Cursor<byte[]> scan(ScanOptions options) {
        return scan(0L, options != null ? options : ScanOptions.NONE);
    }

    public Cursor<byte[]> scan(long cursorId, ScanOptions options) {
        return new ScanCursor<byte[]>(cursorId, options) { // from class: org.springframework.data.redis.connection.jedis.JedisConnection.2
            @Override // org.springframework.data.redis.core.ScanCursor
            protected ScanIteration<byte[]> doScan(long cursorId2, ScanOptions options2) {
                if (JedisConnection.this.isQueueing() || JedisConnection.this.isPipelined()) {
                    throw new UnsupportedOperationException("'SCAN' cannot be called in pipeline / transaction mode.");
                }
                ScanParams params = JedisConverters.toScanParams(options2);
                ScanResult<String> result = JedisConnection.this.jedis.scan(Long.toString(cursorId2), params);
                return new ScanIteration<>(Long.valueOf(result.getStringCursor()).longValue(), JedisConverters.stringListToByteList().convert(result.getResult()));
            }

            @Override // org.springframework.data.redis.core.ScanCursor
            protected void doClose() throws DataAccessException {
                JedisConnection.this.close();
            }
        }.open();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Cursor<RedisZSetCommands.Tuple> zScan(byte[] key, ScanOptions options) {
        return zScan(key, 0L, options);
    }

    public Cursor<RedisZSetCommands.Tuple> zScan(byte[] key, Long cursorId, ScanOptions options) {
        return new KeyBoundCursor<RedisZSetCommands.Tuple>(key, cursorId.longValue(), options) { // from class: org.springframework.data.redis.connection.jedis.JedisConnection.3
            @Override // org.springframework.data.redis.core.KeyBoundCursor
            protected ScanIteration<RedisZSetCommands.Tuple> doScan(byte[] key2, long cursorId2, ScanOptions options2) {
                if (JedisConnection.this.isQueueing() || JedisConnection.this.isPipelined()) {
                    throw new UnsupportedOperationException("'ZSCAN' cannot be called in pipeline / transaction mode.");
                }
                ScanParams params = JedisConverters.toScanParams(options2);
                ScanResult<Tuple> result = JedisConnection.this.jedis.zscan(key2, JedisConverters.toBytes(Long.valueOf(cursorId2)), params);
                return new ScanIteration<>(Long.valueOf(result.getStringCursor()).longValue(), JedisConverters.tuplesToTuples().convert(result.getResult()));
            }

            @Override // org.springframework.data.redis.core.ScanCursor
            protected void doClose() throws DataAccessException {
                JedisConnection.this.close();
            }
        }.open();
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Cursor<byte[]> sScan(byte[] key, ScanOptions options) {
        return sScan(key, 0L, options);
    }

    public Cursor<byte[]> sScan(byte[] key, long cursorId, ScanOptions options) {
        return new KeyBoundCursor<byte[]>(key, cursorId, options) { // from class: org.springframework.data.redis.connection.jedis.JedisConnection.4
            @Override // org.springframework.data.redis.core.KeyBoundCursor
            protected ScanIteration<byte[]> doScan(byte[] key2, long cursorId2, ScanOptions options2) {
                if (JedisConnection.this.isQueueing() || JedisConnection.this.isPipelined()) {
                    throw new UnsupportedOperationException("'SSCAN' cannot be called in pipeline / transaction mode.");
                }
                ScanParams params = JedisConverters.toScanParams(options2);
                ScanResult<byte[]> result = JedisConnection.this.jedis.sscan(key2, JedisConverters.toBytes(Long.valueOf(cursorId2)), params);
                return new ScanIteration<>(Long.valueOf(result.getStringCursor()).longValue(), result.getResult());
            }

            @Override // org.springframework.data.redis.core.ScanCursor
            protected void doClose() throws DataAccessException {
                JedisConnection.this.close();
            }
        }.open();
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Cursor<Map.Entry<byte[], byte[]>> hScan(byte[] key, ScanOptions options) {
        return hScan(key, 0L, options);
    }

    public Cursor<Map.Entry<byte[], byte[]>> hScan(byte[] key, long cursorId, ScanOptions options) {
        return new KeyBoundCursor<Map.Entry<byte[], byte[]>>(key, cursorId, options) { // from class: org.springframework.data.redis.connection.jedis.JedisConnection.5
            @Override // org.springframework.data.redis.core.KeyBoundCursor
            protected ScanIteration<Map.Entry<byte[], byte[]>> doScan(byte[] key2, long cursorId2, ScanOptions options2) {
                if (JedisConnection.this.isQueueing() || JedisConnection.this.isPipelined()) {
                    throw new UnsupportedOperationException("'HSCAN' cannot be called in pipeline / transaction mode.");
                }
                ScanParams params = JedisConverters.toScanParams(options2);
                ScanResult<Map.Entry<byte[], byte[]>> result = JedisConnection.this.jedis.hscan(key2, JedisConverters.toBytes(Long.valueOf(cursorId2)), params);
                return new ScanIteration<>(Long.valueOf(result.getStringCursor()).longValue(), result.getResult());
            }

            @Override // org.springframework.data.redis.core.ScanCursor
            protected void doClose() throws DataAccessException {
                JedisConnection.this.close();
            }
        }.open();
    }

    public void setConvertPipelineAndTxResults(boolean convertPipelineAndTxResults) {
        this.convertPipelineAndTxResults = convertPipelineAndTxResults;
    }

    private byte[][] bXPopArgs(int timeout, byte[]... keys) {
        List<byte[]> args = new ArrayList<>();
        for (byte[] arg : keys) {
            args.add(arg);
        }
        args.add(Protocol.toByteArray(timeout));
        return (byte[][]) args.toArray((Object[]) new byte[args.size()]);
    }

    @Override // org.springframework.data.redis.connection.AbstractRedisConnection
    protected boolean isActive(RedisNode node) {
        if (node == null) {
            return false;
        }
        Jedis temp = null;
        try {
            temp = getJedis(node);
            temp.connect();
            boolean zEqualsIgnoreCase = temp.ping().equalsIgnoreCase("pong");
            if (temp != null) {
                temp.disconnect();
                temp.close();
            }
            return zEqualsIgnoreCase;
        } catch (Exception e) {
            if (temp != null) {
                temp.disconnect();
                temp.close();
            }
            return false;
        } catch (Throwable th) {
            if (temp != null) {
                temp.disconnect();
                temp.close();
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.data.redis.connection.AbstractRedisConnection
    public JedisSentinelConnection getSentinelConnection(RedisNode sentinel) {
        return new JedisSentinelConnection(getJedis(sentinel));
    }

    protected Jedis getJedis(RedisNode node) {
        Jedis jedis = new Jedis(node.getHost(), node.getPort().intValue());
        if (StringUtils.hasText(this.clientName)) {
            jedis.clientSetname(this.clientName);
        }
        return jedis;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByScore(byte[] key, String min, String max) {
        try {
            String keyStr = new String(key, "UTF-8");
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.zrangeByScore(keyStr, min, max)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.zrangeByScore(keyStr, min, max)));
                return null;
            }
            return JedisConverters.stringSetToByteSet().convert(this.jedis.zrangeByScore(keyStr, min, max));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByScore(byte[] key, String min, String max, long offset, long count) {
        if (offset > 2147483647L || count > 2147483647L) {
            throw new IllegalArgumentException("Offset and count must be less than Integer.MAX_VALUE for zRangeByScore in Jedis.");
        }
        try {
            String keyStr = new String(key, "UTF-8");
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.zrangeByScore(keyStr, min, max, (int) offset, (int) count)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.zrangeByScore(keyStr, min, max, (int) offset, (int) count)));
                return null;
            }
            return JedisConverters.stringSetToByteSet().convert(this.jedis.zrangeByScore(keyStr, min, max, (int) offset, (int) count));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.HyperLogLogCommands
    public Long pfAdd(byte[] key, byte[]... values) {
        Assert.notEmpty(values, "PFADD requires at least one non 'null' value.");
        Assert.noNullElements(values, "Values for PFADD must not contain 'null'.");
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.pfadd(key, values)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.pfadd(key, values)));
                return null;
            }
            return this.jedis.pfadd(key, values);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.HyperLogLogCommands
    public Long pfCount(byte[]... keys) {
        Assert.notEmpty(keys, "PFCOUNT requires at least one non 'null' key.");
        Assert.noNullElements(keys, "Keys for PFOUNT must not contain 'null'.");
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.pfcount(keys)));
                return null;
            }
            if (isQueueing()) {
                transaction(new JedisResult(this.transaction.pfcount(keys)));
                return null;
            }
            return this.jedis.pfcount(keys);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.HyperLogLogCommands
    public void pfMerge(byte[] destinationKey, byte[]... sourceKeys) {
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.pfmerge(destinationKey, sourceKeys)));
            } else if (isQueueing()) {
                transaction(new JedisResult(this.transaction.pfmerge(destinationKey, sourceKeys)));
            } else {
                this.jedis.pfmerge(destinationKey, sourceKeys);
            }
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void migrate(byte[] key, RedisNode target, int dbIndex, RedisServerCommands.MigrateOption option) {
        migrate(key, target, dbIndex, option, Long.MAX_VALUE);
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void migrate(byte[] key, RedisNode target, int dbIndex, RedisServerCommands.MigrateOption option, long timeout) {
        int timeoutToUse = timeout <= 2147483647L ? (int) timeout : Integer.MAX_VALUE;
        try {
            if (isPipelined()) {
                pipeline(new JedisResult(this.pipeline.migrate(JedisConverters.toBytes(target.getHost()), target.getPort().intValue(), key, dbIndex, timeoutToUse)));
            } else if (isQueueing()) {
                transaction(new JedisResult(this.transaction.migrate(JedisConverters.toBytes(target.getHost()), target.getPort().intValue(), key, dbIndex, timeoutToUse)));
            } else {
                this.jedis.migrate(JedisConverters.toBytes(target.getHost()), target.getPort().intValue(), key, dbIndex, timeoutToUse);
            }
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }
}
