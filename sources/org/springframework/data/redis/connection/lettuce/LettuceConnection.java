package org.springframework.data.redis.connection.lettuce;

import com.lambdaworks.redis.AbstractRedisClient;
import com.lambdaworks.redis.GeoArgs;
import com.lambdaworks.redis.GeoCoordinates;
import com.lambdaworks.redis.GeoWithin;
import com.lambdaworks.redis.KeyScanCursor;
import com.lambdaworks.redis.KeyValue;
import com.lambdaworks.redis.LettuceFutures;
import com.lambdaworks.redis.MapScanCursor;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisException;
import com.lambdaworks.redis.RedisFuture;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.ScanArgs;
import com.lambdaworks.redis.ScoredValue;
import com.lambdaworks.redis.ScoredValueScanCursor;
import com.lambdaworks.redis.SortArgs;
import com.lambdaworks.redis.ValueScanCursor;
import com.lambdaworks.redis.ZStoreArgs;
import com.lambdaworks.redis.api.StatefulConnection;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.cluster.api.StatefulRedisClusterConnection;
import com.lambdaworks.redis.cluster.api.async.RedisClusterAsyncCommands;
import com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands;
import com.lambdaworks.redis.codec.ByteArrayCodec;
import com.lambdaworks.redis.codec.RedisCodec;
import com.lambdaworks.redis.output.BooleanOutput;
import com.lambdaworks.redis.output.ByteArrayOutput;
import com.lambdaworks.redis.output.CommandOutput;
import com.lambdaworks.redis.output.DateOutput;
import com.lambdaworks.redis.output.DoubleOutput;
import com.lambdaworks.redis.output.IntegerOutput;
import com.lambdaworks.redis.output.KeyListOutput;
import com.lambdaworks.redis.output.KeyValueOutput;
import com.lambdaworks.redis.output.MapOutput;
import com.lambdaworks.redis.output.MultiOutput;
import com.lambdaworks.redis.output.StatusOutput;
import com.lambdaworks.redis.output.ValueListOutput;
import com.lambdaworks.redis.output.ValueOutput;
import com.lambdaworks.redis.output.ValueSetOutput;
import com.lambdaworks.redis.protocol.Command;
import com.lambdaworks.redis.protocol.CommandArgs;
import com.lambdaworks.redis.protocol.CommandType;
import com.lambdaworks.redis.protocol.RedisCommand;
import com.lambdaworks.redis.pubsub.StatefulRedisPubSubConnection;
import com.lambdaworks.redis.sentinel.api.StatefulRedisSentinelConnection;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.QueryTimeoutException;
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
import org.springframework.data.redis.connection.RedisSentinelConnection;
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
import org.springframework.util.ObjectUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/lettuce/LettuceConnection.class */
public class LettuceConnection extends AbstractRedisConnection {
    static final RedisCodec<byte[], byte[]> CODEC = ByteArrayCodec.INSTANCE;
    private static final ExceptionTranslationStrategy EXCEPTION_TRANSLATION = new FallbackExceptionTranslationStrategy(LettuceConverters.exceptionConverter());
    private static final TypeHints typeHints = new TypeHints();
    private final int defaultDbIndex;
    private int dbIndex;
    private final StatefulConnection<byte[], byte[]> asyncSharedConn;
    private StatefulConnection<byte[], byte[]> asyncDedicatedConn;
    private final long timeout;
    private boolean isClosed;
    private boolean isMulti;
    private boolean isPipelined;
    private List<LettuceResult> ppline;
    private Queue<FutureResult<?>> txResults;
    private AbstractRedisClient client;
    private volatile LettuceSubscription subscription;
    private LettucePool pool;
    private boolean broken;
    private boolean convertPipelineAndTxResults;

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/lettuce/LettuceConnection$LettuceResult.class */
    private class LettuceResult extends FutureResult<RedisCommand<?, ?, ?>> {
        public <T> LettuceResult(Future<T> resultHolder, Converter<T, ?> converter) {
            super((RedisCommand) resultHolder, converter);
        }

        public LettuceResult(Future resultHolder) {
            super((RedisCommand) resultHolder);
        }

        @Override // org.springframework.data.redis.connection.FutureResult
        public Object get() {
            try {
                if (LettuceConnection.this.convertPipelineAndTxResults && this.converter != null) {
                    return this.converter.convert2(((RedisCommand) this.resultHolder).getOutput().get());
                }
                return ((RedisCommand) this.resultHolder).getOutput().get();
            } catch (Exception e) {
                DataAccessException translated = LettuceConnection.EXCEPTION_TRANSLATION.translate(e);
                if (translated != null) {
                    throw translated;
                }
                throw new RedisSystemException(e.getMessage(), e);
            }
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/lettuce/LettuceConnection$LettuceStatusResult.class */
    private class LettuceStatusResult extends LettuceResult {
        public LettuceStatusResult(Future resultHolder) {
            super(resultHolder);
            setStatus(true);
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/lettuce/LettuceConnection$LettuceTxResult.class */
    private class LettuceTxResult extends FutureResult<Object> {
        public LettuceTxResult(Object resultHolder, Converter<?, ?> converter) {
            super(resultHolder, converter);
        }

        public LettuceTxResult(Object resultHolder) {
            super(resultHolder);
        }

        @Override // org.springframework.data.redis.connection.FutureResult
        public Object get() {
            if (LettuceConnection.this.convertPipelineAndTxResults && this.converter != null) {
                return this.converter.convert2(this.resultHolder);
            }
            return this.resultHolder;
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/lettuce/LettuceConnection$LettuceTxStatusResult.class */
    private class LettuceTxStatusResult extends LettuceTxResult {
        public LettuceTxStatusResult(Object resultHolder) {
            super(resultHolder);
            setStatus(true);
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/lettuce/LettuceConnection$LettuceTransactionResultConverter.class */
    private class LettuceTransactionResultConverter<T> extends TransactionResultConverter<T> {
        public LettuceTransactionResultConverter(Queue<FutureResult<T>> txResults, Converter<Exception, DataAccessException> exceptionConverter) {
            super(txResults, exceptionConverter);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.springframework.data.redis.connection.convert.TransactionResultConverter, org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: merged with bridge method [inline-methods] */
        public List<Object> convert2(List<Object> execResults) {
            if (execResults.isEmpty()) {
                return null;
            }
            return super.convert2(execResults);
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/lettuce/LettuceConnection$LettuceEvalResultsConverter.class */
    private class LettuceEvalResultsConverter<T> implements Converter<Object, T> {
        private ReturnType returnType;

        public LettuceEvalResultsConverter(ReturnType returnType) {
            this.returnType = returnType;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert */
        public T convert2(Object obj) {
            if (this.returnType == ReturnType.MULTI) {
                List resultList = (List) obj;
                for (Object obj2 : resultList) {
                    if (obj2 instanceof Exception) {
                        throw LettuceConnection.this.convertLettuceAccessException((Exception) obj2);
                    }
                }
            }
            return obj;
        }
    }

    public LettuceConnection(long timeout, RedisClient client) {
        this(null, timeout, client, null);
    }

    public LettuceConnection(long timeout, RedisClient client, LettucePool pool) {
        this(null, timeout, client, pool);
    }

    public LettuceConnection(StatefulRedisConnection<byte[], byte[]> sharedConnection, long timeout, RedisClient client) {
        this(sharedConnection, timeout, client, null);
    }

    public LettuceConnection(StatefulRedisConnection<byte[], byte[]> sharedConnection, long timeout, RedisClient client, LettucePool pool) {
        this(sharedConnection, timeout, client, pool, 0);
    }

    public LettuceConnection(StatefulRedisConnection<byte[], byte[]> sharedConnection, long timeout, AbstractRedisClient client, LettucePool pool, int defaultDbIndex) {
        this.isClosed = false;
        this.isMulti = false;
        this.isPipelined = false;
        this.txResults = new LinkedList();
        this.broken = false;
        this.convertPipelineAndTxResults = true;
        this.asyncSharedConn = sharedConnection;
        this.timeout = timeout;
        this.client = client;
        this.pool = pool;
        this.defaultDbIndex = defaultDbIndex;
        this.dbIndex = this.defaultDbIndex;
    }

    protected DataAccessException convertLettuceAccessException(Exception ex) {
        DataAccessException exception = EXCEPTION_TRANSLATION.translate(ex);
        if (exception instanceof RedisConnectionFailureException) {
            this.broken = true;
        }
        return exception != null ? exception : new RedisSystemException(ex.getMessage(), ex);
    }

    private Object await(RedisFuture<?> cmd) {
        if (this.isMulti) {
            return null;
        }
        return LettuceFutures.awaitOrCancel(cmd, this.timeout, TimeUnit.MILLISECONDS);
    }

    @Override // org.springframework.data.redis.connection.RedisCommands
    public Object execute(String command, byte[]... args) {
        return execute(command, null, args);
    }

    public Object execute(String command, CommandOutput commandOutputTypeHint, byte[]... args) {
        Assert.hasText(command, "a valid command needs to be specified");
        try {
            String name = command.trim().toUpperCase();
            CommandType commandType = CommandType.valueOf(name);
            validateCommandIfRunningInTransactionMode(commandType, args);
            CommandArgs<byte[], byte[]> cmdArg = new CommandArgs<>(CODEC);
            if (!ObjectUtils.isEmpty((Object[]) args)) {
                cmdArg.addKeys(args);
            }
            RedisClusterAsyncCommands<byte[], byte[]> connectionImpl = getAsyncConnection();
            CommandOutput expectedOutput = commandOutputTypeHint != null ? commandOutputTypeHint : typeHints.getTypeHint(commandType);
            Command cmd = new Command(commandType, expectedOutput, cmdArg);
            if (isPipelined()) {
                pipeline(new LettuceResult(connectionImpl.dispatch(cmd.getType(), cmd.getOutput(), cmd.getArgs())));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(connectionImpl.dispatch(cmd.getType(), cmd.getOutput(), cmd.getArgs())));
                return null;
            }
            return await(connectionImpl.dispatch(cmd.getType(), cmd.getOutput(), cmd.getArgs()));
        } catch (RedisException ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    private void returnDedicatedAsyncConnection() {
        if (this.pool != null) {
            if (!this.broken) {
                this.pool.returnResource(this.asyncDedicatedConn);
            } else {
                this.pool.returnBrokenResource(this.asyncDedicatedConn);
            }
            this.asyncDedicatedConn = null;
            return;
        }
        try {
            this.asyncDedicatedConn.close();
        } catch (RuntimeException ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.AbstractRedisConnection, org.springframework.data.redis.connection.RedisConnection
    public void close() throws DataAccessException {
        super.close();
        this.isClosed = true;
        if (this.asyncDedicatedConn != null) {
            returnDedicatedAsyncConnection();
        }
        if (this.subscription != null) {
            if (this.subscription.isAlive()) {
                this.subscription.doClose();
            }
            this.subscription = null;
        }
        this.dbIndex = this.defaultDbIndex;
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public boolean isClosed() {
        return this.isClosed && !isSubscribed();
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public RedisClusterAsyncCommands<byte[], byte[]> getNativeConnection() {
        return this.subscription != null ? this.subscription.pubsub.async() : getAsyncConnection();
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public boolean isQueueing() {
        return this.isMulti;
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public boolean isPipelined() {
        return this.isPipelined;
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public void openPipeline() {
        if (!this.isPipelined) {
            this.isPipelined = true;
            this.ppline = new ArrayList();
        }
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public List<Object> closePipeline() {
        if (this.isPipelined) {
            this.isPipelined = false;
            List<RedisCommand<?, ?, ?>> futures = new ArrayList<>();
            Iterator<LettuceResult> it = this.ppline.iterator();
            while (it.hasNext()) {
                futures.add(it.next().getResultHolder());
            }
            try {
                boolean done = LettuceFutures.awaitAll(this.timeout, TimeUnit.MILLISECONDS, (Future[]) futures.toArray(new RedisFuture[futures.size()]));
                List<Object> results = new ArrayList<>(futures.size());
                Exception problem = null;
                if (done) {
                    for (LettuceResult result : this.ppline) {
                        if (result.getResultHolder().getOutput().hasError()) {
                            Exception err = new InvalidDataAccessApiUsageException(result.getResultHolder().getOutput().getError());
                            if (problem == null) {
                                problem = err;
                            }
                            results.add(err);
                        } else if (!this.convertPipelineAndTxResults || !result.isStatus()) {
                            try {
                                results.add(result.get());
                            } catch (DataAccessException e) {
                                if (problem == null) {
                                    problem = e;
                                }
                                results.add(e);
                            }
                        }
                    }
                }
                this.ppline.clear();
                if (problem != null) {
                    throw new RedisPipelineException(problem, results);
                }
                if (done) {
                    return results;
                }
                throw new RedisPipelineException(new QueryTimeoutException("Redis command timed out"));
            } catch (Exception e2) {
                throw new RedisPipelineException(e2);
            }
        }
        return Collections.emptyList();
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public List<byte[]> sort(byte[] key, SortParameters params) {
        SortArgs args = LettuceConverters.toSortArgs(params);
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().sort(key, args)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().sort(key, args)));
                return null;
            }
            return getConnection().sort(key, args);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long sort(byte[] key, SortParameters params, byte[] sortKey) {
        SortArgs args = LettuceConverters.toSortArgs(params);
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().sortStore(key, args, sortKey)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().sortStore(key, args, sortKey)));
                return null;
            }
            return getConnection().sortStore(key, args, sortKey);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public Long dbSize() {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().dbsize()));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().dbsize()));
                return null;
            }
            return getConnection().dbsize();
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public void flushDb() {
        try {
            if (isPipelined()) {
                pipeline(new LettuceStatusResult(getAsyncConnection().flushdb()));
            } else if (isQueueing()) {
                transaction(new LettuceTxStatusResult(getConnection().flushdb()));
            } else {
                getConnection().flushdb();
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public void flushAll() {
        try {
            if (isPipelined()) {
                pipeline(new LettuceStatusResult(getAsyncConnection().flushall()));
            } else if (isQueueing()) {
                transaction(new LettuceTxStatusResult(getConnection().flushall()));
            } else {
                getConnection().flushall();
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void bgSave() {
        try {
            if (isPipelined()) {
                pipeline(new LettuceStatusResult(getAsyncConnection().bgsave()));
            } else if (isQueueing()) {
                transaction(new LettuceTxStatusResult(getConnection().bgsave()));
            } else {
                getConnection().bgsave();
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void bgReWriteAof() {
        try {
            if (isPipelined()) {
                pipeline(new LettuceStatusResult(getAsyncConnection().bgrewriteaof()));
            } else if (isQueueing()) {
                transaction(new LettuceTxStatusResult(getConnection().bgrewriteaof()));
            } else {
                getConnection().bgrewriteaof();
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
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
                pipeline(new LettuceStatusResult(getAsyncConnection().save()));
            } else if (isQueueing()) {
                transaction(new LettuceTxStatusResult(getConnection().save()));
            } else {
                getConnection().save();
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public List<String> getConfig(String param) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().configGet(param)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().configGet(param)));
                return null;
            }
            return getConnection().configGet(param);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public Properties info() {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().info(), LettuceConverters.stringToProps()));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().info(), LettuceConverters.stringToProps()));
                return null;
            }
            return LettuceConverters.toProperties(getConnection().info());
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public Properties info(String section) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().info(section), LettuceConverters.stringToProps()));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().info(section), LettuceConverters.stringToProps()));
                return null;
            }
            return LettuceConverters.toProperties(getConnection().info(section));
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public Long lastSave() {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().lastsave(), LettuceConverters.dateToLong()));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().lastsave(), LettuceConverters.dateToLong()));
                return null;
            }
            return LettuceConverters.toLong(getConnection().lastsave());
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public void setConfig(String param, String value) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceStatusResult(getAsyncConnection().configSet(param, value)));
            } else if (isQueueing()) {
                transaction(new LettuceTxStatusResult(getConnection().configSet(param, value)));
            } else {
                getConnection().configSet(param, value);
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public void resetConfigStats() {
        try {
            if (isPipelined()) {
                pipeline(new LettuceStatusResult(getAsyncConnection().configResetstat()));
            } else if (isQueueing()) {
                transaction(new LettuceTxStatusResult(getConnection().configResetstat()));
            } else {
                getConnection().configResetstat();
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void shutdown() {
        try {
            if (isPipelined()) {
                getAsyncConnection().shutdown(true);
            } else {
                getConnection().shutdown(true);
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void shutdown(RedisServerCommands.ShutdownOption option) {
        if (option == null) {
            shutdown();
            return;
        }
        boolean save = RedisServerCommands.ShutdownOption.SAVE.equals(option);
        try {
            if (isPipelined()) {
                getAsyncConnection().shutdown(save);
            } else {
                getConnection().shutdown(save);
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionCommands
    public byte[] echo(byte[] message) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().echo(message)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().echo(message)));
                return null;
            }
            return (byte[]) getConnection().echo(message);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public String ping() {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().ping()));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().ping()));
                return null;
            }
            return getConnection().ping();
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long del(byte[]... keys) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().del(keys)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().del(keys)));
                return null;
            }
            return getConnection().del(keys);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisTxCommands
    public void discard() {
        this.isMulti = false;
        try {
            try {
                if (isPipelined()) {
                    pipeline(new LettuceStatusResult(getAsyncDedicatedConnection().discard()));
                    this.txResults.clear();
                } else {
                    getDedicatedConnection().discard();
                    this.txResults.clear();
                }
            } catch (Exception ex) {
                throw convertLettuceAccessException(ex);
            }
        } catch (Throwable th) {
            this.txResults.clear();
            throw th;
        }
    }

    @Override // org.springframework.data.redis.connection.RedisTxCommands
    public List<Object> exec() {
        this.isMulti = false;
        try {
            try {
                if (isPipelined()) {
                    pipeline(new LettuceResult(getAsyncDedicatedConnection().exec(), new LettuceTransactionResultConverter(new LinkedList(this.txResults), LettuceConverters.exceptionConverter())));
                    this.txResults.clear();
                    return null;
                }
                List<Object> results = getDedicatedConnection().exec();
                return this.convertPipelineAndTxResults ? new LettuceTransactionResultConverter(this.txResults, LettuceConverters.exceptionConverter()).convert2(results) : results;
            } catch (Exception ex) {
                throw convertLettuceAccessException(ex);
            }
        } finally {
            this.txResults.clear();
        }
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r5v2, types: [byte[], java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r5v5, types: [byte[], java.lang.Object[]] */
    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean exists(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().exists((Object[]) new byte[]{key}), LettuceConverters.longToBooleanConverter()));
                return null;
            }
            if (!isQueueing()) {
                return LettuceConverters.longToBooleanConverter().convert2(getConnection().exists((Object[]) new byte[]{key}));
            }
            transaction(new LettuceResult(getAsyncConnection().exists((Object[]) new byte[]{key}), LettuceConverters.longToBooleanConverter()));
            return null;
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean expire(byte[] key, long seconds) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().expire(key, seconds)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().expire(key, seconds)));
                return null;
            }
            return getConnection().expire(key, seconds);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean expireAt(byte[] key, long unixTime) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().expireat(key, unixTime)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().expireat(key, unixTime)));
                return null;
            }
            return getConnection().expireat(key, unixTime);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean pExpire(byte[] key, long millis) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().pexpire(key, millis)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().pexpire(key, millis)));
                return null;
            }
            return getConnection().pexpire(key, millis);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean pExpireAt(byte[] key, long unixTimeInMillis) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().pexpireat(key, unixTimeInMillis)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().pexpireat(key, unixTimeInMillis)));
                return null;
            }
            return getConnection().pexpireat(key, unixTimeInMillis);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long pTtl(byte[] key) {
        Assert.notNull(key, "Key must not be null!");
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().pttl(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().pttl(key)));
                return null;
            }
            return getConnection().pttl(key);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long pTtl(byte[] key, TimeUnit timeUnit) {
        Assert.notNull(key, "Key must not be null!");
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().pttl(key), Converters.millisecondsToTimeUnit(timeUnit)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().pttl(key), Converters.millisecondsToTimeUnit(timeUnit)));
                return null;
            }
            return Long.valueOf(Converters.millisecondsToTimeUnit(getConnection().pttl(key).longValue(), timeUnit));
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public byte[] dump(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().dump(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().dump(key)));
                return null;
            }
            return getConnection().dump(key);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public void restore(byte[] key, long ttlInMillis, byte[] serializedValue) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceStatusResult(getAsyncConnection().restore(key, ttlInMillis, serializedValue)));
            } else if (isQueueing()) {
                transaction(new LettuceTxStatusResult(getConnection().restore(key, ttlInMillis, serializedValue)));
            } else {
                getConnection().restore(key, ttlInMillis, serializedValue);
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Set<byte[]> keys(byte[] pattern) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().keys(pattern), LettuceConverters.bytesListToBytesSet()));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().keys(pattern), LettuceConverters.bytesListToBytesSet()));
                return null;
            }
            return LettuceConverters.toBytesSet(getConnection().keys(pattern));
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public void multi() {
        if (isQueueing()) {
            return;
        }
        this.isMulti = true;
        try {
            if (isPipelined()) {
                getAsyncDedicatedConnection().multi();
            } else {
                getDedicatedConnection().multi();
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean persist(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().persist(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().persist(key)));
                return null;
            }
            return getConnection().persist(key);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean move(byte[] key, int dbIndex) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().move(key, dbIndex)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().move(key, dbIndex)));
                return null;
            }
            return getConnection().move(key, dbIndex);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public byte[] randomKey() {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().randomkey()));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().randomkey()));
                return null;
            }
            return (byte[]) getConnection().randomkey();
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public void rename(byte[] oldName, byte[] newName) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceStatusResult(getAsyncConnection().rename(oldName, newName)));
            } else if (isQueueing()) {
                transaction(new LettuceTxStatusResult(getConnection().rename(oldName, newName)));
            } else {
                getConnection().rename(oldName, newName);
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean renameNX(byte[] oldName, byte[] newName) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().renamenx(oldName, newName)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().renamenx(oldName, newName)));
                return null;
            }
            return getConnection().renamenx(oldName, newName);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public void select(int dbIndex) {
        if (this.asyncSharedConn != null) {
            throw new UnsupportedOperationException("Selecting a new database not supported due to shared connection. Use separate ConnectionFactorys to work with multiple databases");
        }
        if (isPipelined()) {
            throw new UnsupportedOperationException("Lettuce blocks for #select");
        }
        try {
            this.dbIndex = dbIndex;
            if (isQueueing()) {
                transaction(new LettuceTxStatusResult(getAsyncConnection().select(dbIndex)));
            } else {
                getConnection().select(dbIndex);
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long ttl(byte[] key) {
        Assert.notNull(key, "Key must not be null!");
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().ttl(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().ttl(key)));
                return null;
            }
            return getConnection().ttl(key);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long ttl(byte[] key, TimeUnit timeUnit) {
        Assert.notNull(key, "Key must not be null!");
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().ttl(key), Converters.secondsToTimeUnit(timeUnit)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().ttl(key), Converters.secondsToTimeUnit(timeUnit)));
                return null;
            }
            return Long.valueOf(Converters.secondsToTimeUnit(getConnection().ttl(key).longValue(), timeUnit));
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public DataType type(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().type(key), LettuceConverters.stringToDataType()));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().type(key), LettuceConverters.stringToDataType()));
                return null;
            }
            return LettuceConverters.toDataType(getConnection().type(key));
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public void unwatch() {
        try {
            if (isPipelined()) {
                pipeline(new LettuceStatusResult(getAsyncDedicatedConnection().unwatch()));
            } else if (isQueueing()) {
                transaction(new LettuceTxStatusResult(getDedicatedConnection().unwatch()));
            } else {
                getDedicatedConnection().unwatch();
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public void watch(byte[]... keys) {
        if (isQueueing()) {
            throw new UnsupportedOperationException();
        }
        try {
            if (isPipelined()) {
                pipeline(new LettuceStatusResult(getAsyncDedicatedConnection().watch(keys)));
            } else if (isQueueing()) {
                transaction(new LettuceTxStatusResult(getDedicatedConnection().watch(new Object[0])));
            } else {
                getDedicatedConnection().watch(keys);
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public byte[] get(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().get(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().get(key)));
                return null;
            }
            return (byte[]) getConnection().get(key);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void set(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceStatusResult(getAsyncConnection().set(key, value)));
            } else if (isQueueing()) {
                transaction(new LettuceTxStatusResult(getConnection().set(key, value)));
            } else {
                getConnection().set(key, value);
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void set(byte[] key, byte[] value, Expiration expiration, RedisStringCommands.SetOption option) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceStatusResult(getAsyncConnection().set(key, value, LettuceConverters.toSetArgs(expiration, option))));
            } else if (isQueueing()) {
                transaction(new LettuceTxStatusResult(getConnection().set(key, value, LettuceConverters.toSetArgs(expiration, option))));
            } else {
                getConnection().set(key, value, LettuceConverters.toSetArgs(expiration, option));
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public byte[] getSet(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().getset(key, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().getset(key, value)));
                return null;
            }
            return (byte[]) getConnection().getset(key, value);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long append(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().append(key, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().append(key, value)));
                return null;
            }
            return getConnection().append(key, value);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public List<byte[]> mGet(byte[]... keys) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().mget(keys)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().mget(keys)));
                return null;
            }
            return getConnection().mget(keys);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public void mSet(Map<byte[], byte[]> tuples) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceStatusResult(getAsyncConnection().mset(tuples)));
            } else if (isQueueing()) {
                transaction(new LettuceTxStatusResult(getConnection().mset(tuples)));
            } else {
                getConnection().mset(tuples);
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public Boolean mSetNX(Map<byte[], byte[]> tuples) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().msetnx(tuples)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().msetnx(tuples)));
                return null;
            }
            return getConnection().msetnx(tuples);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void setEx(byte[] key, long time, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceStatusResult(getAsyncConnection().setex(key, time, value)));
            } else if (isQueueing()) {
                transaction(new LettuceTxStatusResult(getConnection().setex(key, time, value)));
            } else {
                getConnection().setex(key, time, value);
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void pSetEx(byte[] key, long milliseconds, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceStatusResult(getAsyncConnection().psetex(key, milliseconds, value)));
            } else if (isQueueing()) {
                transaction(new LettuceTxStatusResult(getConnection().psetex(key, milliseconds, value)));
            } else {
                getConnection().psetex(key, milliseconds, value);
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Boolean setNX(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().setnx(key, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().setnx(key, value)));
                return null;
            }
            return getConnection().setnx(key, value);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public byte[] getRange(byte[] key, long start, long end) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().getrange(key, start, end)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().getrange(key, start, end)));
                return null;
            }
            return (byte[]) getConnection().getrange(key, start, end);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long decr(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().decr(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().decr(key)));
                return null;
            }
            return getConnection().decr(key);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long decrBy(byte[] key, long value) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().decrby(key, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().decrby(key, value)));
                return null;
            }
            return getConnection().decrby(key, value);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long incr(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().incr(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().incr(key)));
                return null;
            }
            return getConnection().incr(key);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long incrBy(byte[] key, long value) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().incrby(key, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().incrby(key, value)));
                return null;
            }
            return getConnection().incrby(key, value);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Double incrBy(byte[] key, double value) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().incrbyfloat(key, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().incrbyfloat(key, value)));
                return null;
            }
            return getConnection().incrbyfloat(key, value);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Boolean getBit(byte[] key, long offset) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().getbit(key, offset), LettuceConverters.longToBoolean()));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().getbit(key, offset), LettuceConverters.longToBoolean()));
                return null;
            }
            return LettuceConverters.toBoolean(getConnection().getbit(key, offset));
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Boolean setBit(byte[] key, long offset, boolean value) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().setbit(key, offset, LettuceConverters.toInt(value)), LettuceConverters.longToBooleanConverter()));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().setbit(key, offset, LettuceConverters.toInt(value)), LettuceConverters.longToBooleanConverter()));
                return null;
            }
            return LettuceConverters.longToBooleanConverter().convert2(getConnection().setbit(key, offset, LettuceConverters.toInt(value)));
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void setRange(byte[] key, byte[] value, long start) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceStatusResult(getAsyncConnection().setrange(key, start, value)));
            } else if (isQueueing()) {
                transaction(new LettuceTxStatusResult(getConnection().setrange(key, start, value)));
            } else {
                getConnection().setrange(key, start, value);
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long strLen(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().strlen(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().strlen(key)));
                return null;
            }
            return getConnection().strlen(key);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long bitCount(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().bitcount(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().bitcount(key)));
                return null;
            }
            return getConnection().bitcount(key);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long bitCount(byte[] key, long begin, long end) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().bitcount(key, begin, end)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().bitcount(key, begin, end)));
                return null;
            }
            return getConnection().bitcount(key, begin, end);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long bitOp(RedisStringCommands.BitOperation op, byte[] destination, byte[]... keys) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(asyncBitOp(op, destination, keys)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(syncBitOp(op, destination, keys)));
                return null;
            }
            return syncBitOp(op, destination, keys);
        } catch (UnsupportedOperationException ex) {
            throw ex;
        } catch (Exception ex2) {
            throw convertLettuceAccessException(ex2);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lPush(byte[] key, byte[]... values) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().lpush(key, values)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().lpush(key, values)));
                return null;
            }
            return getConnection().lpush(key, values);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long rPush(byte[] key, byte[]... values) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().rpush(key, values)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().rpush(key, values)));
                return null;
            }
            return getConnection().rpush(key, values);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public List<byte[]> bLPop(int timeout, byte[]... keys) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncDedicatedConnection().blpop(timeout, keys), LettuceConverters.keyValueToBytesList()));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getDedicatedConnection().blpop(timeout, keys), LettuceConverters.keyValueToBytesList()));
                return null;
            }
            return LettuceConverters.toBytesList((KeyValue<byte[], byte[]>) getDedicatedConnection().blpop(timeout, keys));
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public List<byte[]> bRPop(int timeout, byte[]... keys) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncDedicatedConnection().brpop(timeout, keys), LettuceConverters.keyValueToBytesList()));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getDedicatedConnection().brpop(timeout, keys), LettuceConverters.keyValueToBytesList()));
                return null;
            }
            return LettuceConverters.toBytesList((KeyValue<byte[], byte[]>) getDedicatedConnection().brpop(timeout, keys));
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public byte[] lIndex(byte[] key, long index) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().lindex(key, index)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().lindex(key, index)));
                return null;
            }
            return (byte[]) getConnection().lindex(key, index);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lInsert(byte[] key, RedisListCommands.Position where, byte[] pivot, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().linsert(key, LettuceConverters.toBoolean(where), pivot, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().linsert(key, LettuceConverters.toBoolean(where), pivot, value)));
                return null;
            }
            return getConnection().linsert(key, LettuceConverters.toBoolean(where), pivot, value);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lLen(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().llen(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().llen(key)));
                return null;
            }
            return getConnection().llen(key);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public byte[] lPop(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().lpop(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().lpop(key)));
                return null;
            }
            return (byte[]) getConnection().lpop(key);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public List<byte[]> lRange(byte[] key, long start, long end) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().lrange(key, start, end)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().lrange(key, start, end)));
                return null;
            }
            return getConnection().lrange(key, start, end);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lRem(byte[] key, long count, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().lrem(key, count, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().lrem(key, count, value)));
                return null;
            }
            return getConnection().lrem(key, count, value);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public void lSet(byte[] key, long index, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceStatusResult(getAsyncConnection().lset(key, index, value)));
            } else if (isQueueing()) {
                transaction(new LettuceTxStatusResult(getConnection().lset(key, index, value)));
            } else {
                getConnection().lset(key, index, value);
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public void lTrim(byte[] key, long start, long end) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceStatusResult(getAsyncConnection().ltrim(key, start, end)));
            } else if (isQueueing()) {
                transaction(new LettuceTxStatusResult(getConnection().ltrim(key, start, end)));
            } else {
                getConnection().ltrim(key, start, end);
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public byte[] rPop(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().rpop(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().rpop(key)));
                return null;
            }
            return (byte[]) getConnection().rpop(key);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public byte[] rPopLPush(byte[] srcKey, byte[] dstKey) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().rpoplpush(srcKey, dstKey)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().rpoplpush(srcKey, dstKey)));
                return null;
            }
            return (byte[]) getConnection().rpoplpush(srcKey, dstKey);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public byte[] bRPopLPush(int timeout, byte[] srcKey, byte[] dstKey) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncDedicatedConnection().brpoplpush(timeout, srcKey, dstKey)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getDedicatedConnection().brpoplpush(timeout, srcKey, dstKey)));
                return null;
            }
            return (byte[]) getDedicatedConnection().brpoplpush(timeout, srcKey, dstKey);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lPushX(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().lpushx(key, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().lpushx(key, value)));
                return null;
            }
            return getConnection().lpushx(key, value);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long rPushX(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().rpushx(key, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().rpushx(key, value)));
                return null;
            }
            return getConnection().rpushx(key, value);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sAdd(byte[] key, byte[]... values) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().sadd(key, values)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().sadd(key, values)));
                return null;
            }
            return getConnection().sadd(key, values);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sCard(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().scard(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().scard(key)));
                return null;
            }
            return getConnection().scard(key);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public Set<byte[]> sDiff(byte[]... keys) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().sdiff(keys)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().sdiff(keys)));
                return null;
            }
            return getConnection().sdiff(keys);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public Long sDiffStore(byte[] destKey, byte[]... keys) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().sdiffstore(destKey, keys)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().sdiffstore(destKey, keys)));
                return null;
            }
            return getConnection().sdiffstore(destKey, keys);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public Set<byte[]> sInter(byte[]... keys) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().sinter(keys)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().sinter(keys)));
                return null;
            }
            return getConnection().sinter(keys);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public Long sInterStore(byte[] destKey, byte[]... keys) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().sinterstore(destKey, keys)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().sinterstore(destKey, keys)));
                return null;
            }
            return getConnection().sinterstore(destKey, keys);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Boolean sIsMember(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().sismember(key, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().sismember(key, value)));
                return null;
            }
            return getConnection().sismember(key, value);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Set<byte[]> sMembers(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().smembers(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().smembers(key)));
                return null;
            }
            return getConnection().smembers(key);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public Boolean sMove(byte[] srcKey, byte[] destKey, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().smove(srcKey, destKey, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().smove(srcKey, destKey, value)));
                return null;
            }
            return getConnection().smove(srcKey, destKey, value);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public byte[] sPop(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().spop(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().spop(key)));
                return null;
            }
            return (byte[]) getConnection().spop(key);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public byte[] sRandMember(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().srandmember(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().srandmember(key)));
                return null;
            }
            return (byte[]) getConnection().srandmember(key);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public List<byte[]> sRandMember(byte[] key, long count) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().srandmember(key, count), LettuceConverters.bytesCollectionToBytesList()));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().srandmember(key, count), LettuceConverters.bytesCollectionToBytesList()));
                return null;
            }
            return LettuceConverters.toBytesList(getConnection().srandmember(key, count));
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sRem(byte[] key, byte[]... values) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().srem(key, values)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().srem(key, values)));
                return null;
            }
            return getConnection().srem(key, values);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public Set<byte[]> sUnion(byte[]... keys) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().sunion(keys)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().sunion(keys)));
                return null;
            }
            return getConnection().sunion(keys);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public Long sUnionStore(byte[] destKey, byte[]... keys) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().sunionstore(destKey, keys)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().sunionstore(destKey, keys)));
                return null;
            }
            return getConnection().sunionstore(destKey, keys);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Boolean zAdd(byte[] key, double score, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().zadd(key, score, value), LettuceConverters.longToBoolean()));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().zadd(key, score, value), LettuceConverters.longToBoolean()));
                return null;
            }
            return LettuceConverters.toBoolean(getConnection().zadd(key, score, value));
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zAdd(byte[] key, Set<RedisZSetCommands.Tuple> tuples) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().zadd(key, LettuceConverters.toObjects(tuples).toArray())));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().zadd(key, LettuceConverters.toObjects(tuples).toArray())));
                return null;
            }
            return getConnection().zadd(key, LettuceConverters.toObjects(tuples).toArray());
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zCard(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().zcard(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().zcard(key)));
                return null;
            }
            return getConnection().zcard(key);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zCount(byte[] key, double min, double max) {
        return zCount(key, new RedisZSetCommands.Range().gte(Double.valueOf(min)).lte(Double.valueOf(max)));
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zCount(byte[] key, RedisZSetCommands.Range range) {
        Assert.notNull(range, "Range for ZCOUNT must not be null!");
        String min = LettuceConverters.boundaryToStringForZRange(range.getMin(), "-inf");
        String max = LettuceConverters.boundaryToStringForZRange(range.getMax(), "+inf");
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().zcount(key, min, max)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().zcount(key, min, max)));
                return null;
            }
            return getConnection().zcount(key, min, max);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Double zIncrBy(byte[] key, double increment, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().zincrby(key, increment, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().zincrby(key, increment, value)));
                return null;
            }
            return getConnection().zincrby(key, increment, value);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zInterStore(byte[] destKey, RedisZSetCommands.Aggregate aggregate, int[] weights, byte[]... sets) {
        ZStoreArgs storeArgs = zStoreArgs(aggregate, weights);
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().zinterstore(destKey, storeArgs, sets)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().zinterstore(destKey, storeArgs, sets)));
                return null;
            }
            return getConnection().zinterstore(destKey, storeArgs, sets);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zInterStore(byte[] destKey, byte[]... sets) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().zinterstore(destKey, sets)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().zinterstore(destKey, sets)));
                return null;
            }
            return getConnection().zinterstore(destKey, sets);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRange(byte[] key, long start, long end) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().zrange(key, start, end), LettuceConverters.bytesListToBytesSet()));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().zrange(key, start, end), LettuceConverters.bytesListToBytesSet()));
                return null;
            }
            return LettuceConverters.toBytesSet(getConnection().zrange(key, start, end));
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRangeWithScores(byte[] key, long start, long end) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().zrangeWithScores(key, start, end), LettuceConverters.scoredValuesToTupleSet()));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().zrangeWithScores(key, start, end), LettuceConverters.scoredValuesToTupleSet()));
                return null;
            }
            return LettuceConverters.toTupleSet(getConnection().zrangeWithScores(key, start, end));
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
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
        Assert.notNull(range, "Range for ZRANGEBYSCORE must not be null!");
        String min = LettuceConverters.boundaryToStringForZRange(range.getMin(), "-inf");
        String max = LettuceConverters.boundaryToStringForZRange(range.getMax(), "+inf");
        try {
            if (isPipelined()) {
                if (limit != null) {
                    pipeline(new LettuceResult(getAsyncConnection().zrangebyscore(key, min, max, limit.getOffset(), limit.getCount()), LettuceConverters.bytesListToBytesSet()));
                    return null;
                }
                pipeline(new LettuceResult(getAsyncConnection().zrangebyscore(key, min, max), LettuceConverters.bytesListToBytesSet()));
                return null;
            }
            if (isQueueing()) {
                if (limit != null) {
                    transaction(new LettuceTxResult(getConnection().zrangebyscore(key, min, max, limit.getOffset(), limit.getCount()), LettuceConverters.bytesListToBytesSet()));
                    return null;
                }
                transaction(new LettuceTxResult(getConnection().zrangebyscore(key, min, max), LettuceConverters.bytesListToBytesSet()));
                return null;
            }
            if (limit != null) {
                return LettuceConverters.toBytesSet(getConnection().zrangebyscore(key, min, max, limit.getOffset(), limit.getCount()));
            }
            return LettuceConverters.toBytesSet(getConnection().zrangebyscore(key, min, max));
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
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
        Assert.notNull(range, "Range for ZRANGEBYSCOREWITHSCORES must not be null!");
        String min = LettuceConverters.boundaryToStringForZRange(range.getMin(), "-inf");
        String max = LettuceConverters.boundaryToStringForZRange(range.getMax(), "+inf");
        try {
            if (isPipelined()) {
                if (limit != null) {
                    pipeline(new LettuceResult(getAsyncConnection().zrangebyscoreWithScores(key, min, max, limit.getOffset(), limit.getCount()), LettuceConverters.scoredValuesToTupleSet()));
                    return null;
                }
                pipeline(new LettuceResult(getAsyncConnection().zrangebyscoreWithScores(key, min, max), LettuceConverters.scoredValuesToTupleSet()));
                return null;
            }
            if (isQueueing()) {
                if (limit != null) {
                    transaction(new LettuceTxResult(getConnection().zrangebyscoreWithScores(key, min, max, limit.getOffset(), limit.getCount()), LettuceConverters.scoredValuesToTupleSet()));
                    return null;
                }
                transaction(new LettuceTxResult(getConnection().zrangebyscoreWithScores(key, min, max), LettuceConverters.scoredValuesToTupleSet()));
                return null;
            }
            if (limit != null) {
                return LettuceConverters.toTupleSet(getConnection().zrangebyscoreWithScores(key, min, max, limit.getOffset(), limit.getCount()));
            }
            return LettuceConverters.toTupleSet(getConnection().zrangebyscoreWithScores(key, min, max));
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRevRangeWithScores(byte[] key, long start, long end) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().zrevrangeWithScores(key, start, end), LettuceConverters.scoredValuesToTupleSet()));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().zrevrangeWithScores(key, start, end), LettuceConverters.scoredValuesToTupleSet()));
                return null;
            }
            return LettuceConverters.toTupleSet(getConnection().zrevrangeWithScores(key, start, end));
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
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
    public Set<byte[]> zRevRangeByScore(byte[] key, RedisZSetCommands.Range range) {
        return zRevRangeByScore(key, range, (RedisZSetCommands.Limit) null);
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRevRangeByScore(byte[] key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        Assert.notNull(range, "Range for ZREVRANGEBYSCORE must not be null!");
        String min = LettuceConverters.boundaryToStringForZRange(range.getMin(), "-inf");
        String max = LettuceConverters.boundaryToStringForZRange(range.getMax(), "+inf");
        try {
            if (isPipelined()) {
                if (limit != null) {
                    pipeline(new LettuceResult(getAsyncConnection().zrevrangebyscore(key, max, min, limit.getOffset(), limit.getCount()), LettuceConverters.bytesListToBytesSet()));
                    return null;
                }
                pipeline(new LettuceResult(getAsyncConnection().zrevrangebyscore(key, max, min), LettuceConverters.bytesListToBytesSet()));
                return null;
            }
            if (isQueueing()) {
                if (limit != null) {
                    transaction(new LettuceTxResult(getConnection().zrevrangebyscore(key, max, min, limit.getOffset(), limit.getCount()), LettuceConverters.bytesListToBytesSet()));
                    return null;
                }
                transaction(new LettuceTxResult(getConnection().zrevrangebyscore(key, max, min), LettuceConverters.bytesListToBytesSet()));
                return null;
            }
            if (limit != null) {
                return LettuceConverters.toBytesSet(getConnection().zrevrangebyscore(key, max, min, limit.getOffset(), limit.getCount()));
            }
            return LettuceConverters.toBytesSet(getConnection().zrevrangebyscore(key, max, min));
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRevRangeByScore(byte[] key, double min, double max) {
        return zRevRangeByScore(key, new RedisZSetCommands.Range().gte(Double.valueOf(min)).lte(Double.valueOf(max)));
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
        Assert.notNull(range, "Range for ZREVRANGEBYSCOREWITHSCORES must not be null!");
        String min = LettuceConverters.boundaryToStringForZRange(range.getMin(), "-inf");
        String max = LettuceConverters.boundaryToStringForZRange(range.getMax(), "+inf");
        try {
            if (isPipelined()) {
                if (limit != null) {
                    pipeline(new LettuceResult(getAsyncConnection().zrevrangebyscoreWithScores(key, max, min, limit.getOffset(), limit.getCount()), LettuceConverters.scoredValuesToTupleSet()));
                    return null;
                }
                pipeline(new LettuceResult(getAsyncConnection().zrevrangebyscoreWithScores(key, max, min), LettuceConverters.scoredValuesToTupleSet()));
                return null;
            }
            if (isQueueing()) {
                if (limit != null) {
                    transaction(new LettuceTxResult(getConnection().zrevrangebyscoreWithScores(key, max, min, limit.getOffset(), limit.getCount()), LettuceConverters.scoredValuesToTupleSet()));
                    return null;
                }
                transaction(new LettuceTxResult(getConnection().zrevrangebyscoreWithScores(key, max, min), LettuceConverters.scoredValuesToTupleSet()));
                return null;
            }
            if (limit != null) {
                return LettuceConverters.toTupleSet(getConnection().zrevrangebyscoreWithScores(key, max, min, limit.getOffset(), limit.getCount()));
            }
            return LettuceConverters.toTupleSet(getConnection().zrevrangebyscoreWithScores(key, max, min));
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRevRangeByScoreWithScores(byte[] key, double min, double max) {
        return zRevRangeByScoreWithScores(key, new RedisZSetCommands.Range().gte(Double.valueOf(min)).lte(Double.valueOf(max)));
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRank(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().zrank(key, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().zrank(key, value)));
                return null;
            }
            return getConnection().zrank(key, value);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRem(byte[] key, byte[]... values) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().zrem(key, values)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().zrem(key, values)));
                return null;
            }
            return getConnection().zrem(key, values);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRemRange(byte[] key, long start, long end) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().zremrangebyrank(key, start, end)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().zremrangebyrank(key, start, end)));
                return null;
            }
            return getConnection().zremrangebyrank(key, start, end);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRemRangeByScore(byte[] key, double min, double max) {
        return zRemRangeByScore(key, new RedisZSetCommands.Range().gte(Double.valueOf(min)).lte(Double.valueOf(max)));
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRemRangeByScore(byte[] key, RedisZSetCommands.Range range) {
        Assert.notNull(range, "Range for ZREMRANGEBYSCORE must not be null!");
        String min = LettuceConverters.boundaryToStringForZRange(range.getMin(), "-inf");
        String max = LettuceConverters.boundaryToStringForZRange(range.getMax(), "+inf");
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().zremrangebyscore(key, min, max)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().zremrangebyscore(key, min, max)));
                return null;
            }
            return getConnection().zremrangebyscore(key, min, max);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRevRange(byte[] key, long start, long end) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().zrevrange(key, start, end), LettuceConverters.bytesListToBytesSet()));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().zrevrange(key, start, end), LettuceConverters.bytesListToBytesSet()));
                return null;
            }
            return LettuceConverters.toBytesSet(getConnection().zrevrange(key, start, end));
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRevRank(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().zrevrank(key, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().zrevrank(key, value)));
                return null;
            }
            return getConnection().zrevrank(key, value);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Double zScore(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().zscore(key, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().zscore(key, value)));
                return null;
            }
            return getConnection().zscore(key, value);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zUnionStore(byte[] destKey, RedisZSetCommands.Aggregate aggregate, int[] weights, byte[]... sets) {
        ZStoreArgs storeArgs = zStoreArgs(aggregate, weights);
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().zunionstore(destKey, storeArgs, sets)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().zunionstore(destKey, storeArgs, sets)));
                return null;
            }
            return getConnection().zunionstore(destKey, storeArgs, sets);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zUnionStore(byte[] destKey, byte[]... sets) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().zunionstore(destKey, sets)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().zunionstore(destKey, sets)));
                return null;
            }
            return getConnection().zunionstore(destKey, sets);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Boolean hSet(byte[] key, byte[] field, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().hset(key, field, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().hset(key, field, value)));
                return null;
            }
            return getConnection().hset(key, field, value);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Boolean hSetNX(byte[] key, byte[] field, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().hsetnx(key, field, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().hsetnx(key, field, value)));
                return null;
            }
            return getConnection().hsetnx(key, field, value);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Long hDel(byte[] key, byte[]... fields) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().hdel(key, fields)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().hdel(key, fields)));
                return null;
            }
            return getConnection().hdel(key, fields);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Boolean hExists(byte[] key, byte[] field) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().hexists(key, field)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().hexists(key, field)));
                return null;
            }
            return getConnection().hexists(key, field);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public byte[] hGet(byte[] key, byte[] field) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().hget(key, field)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().hget(key, field)));
                return null;
            }
            return (byte[]) getConnection().hget(key, field);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Map<byte[], byte[]> hGetAll(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().hgetall(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().hgetall(key)));
                return null;
            }
            return getConnection().hgetall(key);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Long hIncrBy(byte[] key, byte[] field, long delta) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().hincrby(key, field, delta)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().hincrby(key, field, delta)));
                return null;
            }
            return getConnection().hincrby(key, field, delta);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Double hIncrBy(byte[] key, byte[] field, double delta) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().hincrbyfloat(key, field, delta)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().hincrbyfloat(key, field, delta)));
                return null;
            }
            return getConnection().hincrbyfloat(key, field, delta);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Set<byte[]> hKeys(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().hkeys(key), LettuceConverters.bytesListToBytesSet()));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().hkeys(key), LettuceConverters.bytesListToBytesSet()));
                return null;
            }
            return LettuceConverters.toBytesSet(getConnection().hkeys(key));
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Long hLen(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().hlen(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().hlen(key)));
                return null;
            }
            return getConnection().hlen(key);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public List<byte[]> hMGet(byte[] key, byte[]... fields) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().hmget(key, fields)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().hmget(key, fields)));
                return null;
            }
            return getConnection().hmget(key, fields);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public void hMSet(byte[] key, Map<byte[], byte[]> tuple) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceStatusResult(getAsyncConnection().hmset(key, tuple)));
            } else if (isQueueing()) {
                transaction(new LettuceTxStatusResult(getConnection().hmset(key, tuple)));
            } else {
                getConnection().hmset(key, tuple);
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public List<byte[]> hVals(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().hvals(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().hvals(key)));
                return null;
            }
            return getConnection().hvals(key);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public void scriptFlush() {
        try {
            if (isPipelined()) {
                pipeline(new LettuceStatusResult(getAsyncConnection().scriptFlush()));
            } else if (isQueueing()) {
                transaction(new LettuceTxStatusResult(getConnection().scriptFlush()));
            } else {
                getConnection().scriptFlush();
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public void scriptKill() {
        if (isQueueing()) {
            throw new UnsupportedOperationException("Script kill not permitted in a transaction");
        }
        try {
            if (isPipelined()) {
                pipeline(new LettuceStatusResult(getAsyncConnection().scriptKill()));
            } else if (isQueueing()) {
                transaction(new LettuceTxStatusResult(getConnection().scriptKill()));
            } else {
                getConnection().scriptKill();
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public String scriptLoad(byte[] script) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().scriptLoad(script)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().scriptLoad(script)));
                return null;
            }
            return getConnection().scriptLoad(script);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public List<Boolean> scriptExists(String... scriptSha1) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().scriptExists(scriptSha1)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().scriptExists(scriptSha1)));
                return null;
            }
            return getConnection().scriptExists(scriptSha1);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public <T> T eval(byte[] bArr, ReturnType returnType, int i, byte[]... bArr2) {
        try {
            byte[][] bArrExtractScriptKeys = extractScriptKeys(i, bArr2);
            byte[][] bArrExtractScriptArgs = extractScriptArgs(i, bArr2);
            String string = LettuceConverters.toString(bArr);
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().eval(string, LettuceConverters.toScriptOutputType(returnType), bArrExtractScriptKeys, bArrExtractScriptArgs), new LettuceEvalResultsConverter(returnType)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().eval(string, LettuceConverters.toScriptOutputType(returnType), bArrExtractScriptKeys, bArrExtractScriptArgs), new LettuceEvalResultsConverter(returnType)));
                return null;
            }
            return (T) new LettuceEvalResultsConverter(returnType).convert2(getConnection().eval(string, LettuceConverters.toScriptOutputType(returnType), bArrExtractScriptKeys, bArrExtractScriptArgs));
        } catch (Exception e) {
            throw convertLettuceAccessException(e);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public <T> T evalSha(String str, ReturnType returnType, int i, byte[]... bArr) {
        try {
            byte[][] bArrExtractScriptKeys = extractScriptKeys(i, bArr);
            byte[][] bArrExtractScriptArgs = extractScriptArgs(i, bArr);
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().evalsha(str, LettuceConverters.toScriptOutputType(returnType), bArrExtractScriptKeys, bArrExtractScriptArgs), new LettuceEvalResultsConverter(returnType)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().evalsha(str, LettuceConverters.toScriptOutputType(returnType), bArrExtractScriptKeys, bArrExtractScriptArgs), new LettuceEvalResultsConverter(returnType)));
                return null;
            }
            return (T) new LettuceEvalResultsConverter(returnType).convert2(getConnection().evalsha(str, LettuceConverters.toScriptOutputType(returnType), bArrExtractScriptKeys, bArrExtractScriptArgs));
        } catch (Exception e) {
            throw convertLettuceAccessException(e);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public <T> T evalSha(byte[] bArr, ReturnType returnType, int i, byte[]... bArr2) {
        return (T) evalSha(LettuceConverters.toString(bArr), returnType, i, bArr2);
    }

    @Override // org.springframework.data.redis.connection.RedisPubSubCommands
    public Long publish(byte[] channel, byte[] message) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().publish(channel, message)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().publish(channel, message)));
                return null;
            }
            return getConnection().publish(channel, message);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
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
        checkSubscription();
        if (isQueueing()) {
            throw new UnsupportedOperationException();
        }
        if (isPipelined()) {
            throw new UnsupportedOperationException();
        }
        try {
            this.subscription = new LettuceSubscription(listener, switchToPubSub());
            this.subscription.pSubscribe(patterns);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisPubSubCommands
    public void subscribe(MessageListener listener, byte[]... channels) {
        checkSubscription();
        if (isPipelined()) {
            throw new UnsupportedOperationException();
        }
        try {
            this.subscription = new LettuceSubscription(listener, switchToPubSub());
            this.subscription.subscribe(channels);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoAdd(byte[] key, Point point, byte[] member) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(point, "Point must not be null!");
        Assert.notNull(member, "Member must not be null!");
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().geoadd(key, point.getX(), point.getY(), member)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().geoadd(key, point.getX(), point.getY(), member)));
                return null;
            }
            return getConnection().geoadd(key, point.getX(), point.getY(), member);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoAdd(byte[] key, RedisGeoCommands.GeoLocation<byte[]> location) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(location, "Location must not be null!");
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().geoadd(key, location.getPoint().getX(), location.getPoint().getY(), location.getName())));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().geoadd(key, location.getPoint().getX(), location.getPoint().getY(), location.getName())));
                return null;
            }
            return getConnection().geoadd(key, location.getPoint().getX(), location.getPoint().getY(), location.getName());
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoAdd(byte[] key, Map<byte[], Point> memberCoordinateMap) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(memberCoordinateMap, "MemberCoordinateMap must not be null!");
        List<Object> values = new ArrayList<>();
        for (Map.Entry<byte[], Point> entry : memberCoordinateMap.entrySet()) {
            values.add(Double.valueOf(entry.getValue().getX()));
            values.add(Double.valueOf(entry.getValue().getY()));
            values.add(entry.getKey());
        }
        return geoAdd(key, (Collection<Object>) values);
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoAdd(byte[] key, Iterable<RedisGeoCommands.GeoLocation<byte[]>> locations) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(locations, "Locations must not be null!");
        List<Object> values = new ArrayList<>();
        for (RedisGeoCommands.GeoLocation<byte[]> location : locations) {
            values.add(Double.valueOf(location.getPoint().getX()));
            values.add(Double.valueOf(location.getPoint().getY()));
            values.add(location.getName());
        }
        return geoAdd(key, (Collection<Object>) values);
    }

    private Long geoAdd(byte[] key, Collection<Object> values) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().geoadd(key, values.toArray())));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().geoadd(key, values.toArray())));
                return null;
            }
            return getConnection().geoadd(key, values.toArray());
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Distance geoDist(byte[] key, byte[] member1, byte[] member2) {
        return geoDist(key, member1, member2, RedisGeoCommands.DistanceUnit.METERS);
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Distance geoDist(byte[] key, byte[] member1, byte[] member2, Metric metric) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(member1, "Member1 must not be null!");
        Assert.notNull(member2, "Member2 must not be null!");
        Assert.notNull(metric, "Metric must not be null!");
        GeoArgs.Unit geoUnit = LettuceConverters.toGeoArgsUnit(metric);
        Converter<Double, Distance> distanceConverter = LettuceConverters.distanceConverterForMetric(metric);
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().geodist(key, member1, member2, geoUnit), distanceConverter));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().geodist(key, member1, member2, geoUnit), distanceConverter));
                return null;
            }
            return distanceConverter.convert2(getConnection().geodist(key, member1, member2, geoUnit));
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public List<String> geoHash(byte[] key, byte[]... members) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(members, "Members must not be null!");
        Assert.noNullElements(members, "Members must not contain null!");
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().geohash(key, members)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().geohash(key, members)));
                return null;
            }
            return getConnection().geohash(key, members);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public List<Point> geoPos(byte[] key, byte[]... members) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(members, "Members must not be null!");
        Assert.noNullElements(members, "Members must not contain null!");
        ListConverter<GeoCoordinates, Point> converter = LettuceConverters.geoCoordinatesToPointConverter();
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().geopos(key, members), converter));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().geopos(key, members), converter));
                return null;
            }
            return converter.convert(getConnection().geopos(key, members));
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> geoRadius(byte[] key, Circle within) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(within, "Within must not be null!");
        Converter<Set<byte[]>, GeoResults<RedisGeoCommands.GeoLocation<byte[]>>> geoResultsConverter = LettuceConverters.bytesSetToGeoResultsConverter();
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().georadius(key, within.getCenter().getX(), within.getCenter().getY(), within.getRadius().getValue(), LettuceConverters.toGeoArgsUnit(within.getRadius().getMetric())), geoResultsConverter));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().georadius(key, within.getCenter().getX(), within.getCenter().getY(), within.getRadius().getValue(), LettuceConverters.toGeoArgsUnit(within.getRadius().getMetric())), geoResultsConverter));
                return null;
            }
            return geoResultsConverter.convert2(getConnection().georadius(key, within.getCenter().getX(), within.getCenter().getY(), within.getRadius().getValue(), LettuceConverters.toGeoArgsUnit(within.getRadius().getMetric())));
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> geoRadius(byte[] key, Circle within, RedisGeoCommands.GeoRadiusCommandArgs args) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(within, "Within must not be null!");
        Assert.notNull(args, "Args must not be null!");
        GeoArgs geoArgs = LettuceConverters.toGeoArgs(args);
        Converter<List<GeoWithin<byte[]>>, GeoResults<RedisGeoCommands.GeoLocation<byte[]>>> geoResultsConverter = LettuceConverters.geoRadiusResponseToGeoResultsConverter(within.getRadius().getMetric());
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().georadius(key, within.getCenter().getX(), within.getCenter().getY(), within.getRadius().getValue(), LettuceConverters.toGeoArgsUnit(within.getRadius().getMetric()), geoArgs), geoResultsConverter));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().georadius(key, within.getCenter().getX(), within.getCenter().getY(), within.getRadius().getValue(), LettuceConverters.toGeoArgsUnit(within.getRadius().getMetric()), geoArgs), geoResultsConverter));
                return null;
            }
            return geoResultsConverter.convert2(getConnection().georadius(key, within.getCenter().getX(), within.getCenter().getY(), within.getRadius().getValue(), LettuceConverters.toGeoArgsUnit(within.getRadius().getMetric()), geoArgs));
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
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
        GeoArgs.Unit geoUnit = LettuceConverters.toGeoArgsUnit(radius.getMetric());
        Converter<Set<byte[]>, GeoResults<RedisGeoCommands.GeoLocation<byte[]>>> converter = LettuceConverters.bytesSetToGeoResultsConverter();
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().georadiusbymember(key, member, radius.getValue(), geoUnit), converter));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().georadiusbymember(key, member, radius.getValue(), geoUnit), converter));
                return null;
            }
            return converter.convert2(getConnection().georadiusbymember(key, member, radius.getValue(), geoUnit));
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, Distance radius, RedisGeoCommands.GeoRadiusCommandArgs args) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(member, "Member must not be null!");
        Assert.notNull(radius, "Radius must not be null!");
        Assert.notNull(args, "Args must not be null!");
        GeoArgs.Unit geoUnit = LettuceConverters.toGeoArgsUnit(radius.getMetric());
        GeoArgs geoArgs = LettuceConverters.toGeoArgs(args);
        Converter<List<GeoWithin<byte[]>>, GeoResults<RedisGeoCommands.GeoLocation<byte[]>>> geoResultsConverter = LettuceConverters.geoRadiusResponseToGeoResultsConverter(radius.getMetric());
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().georadiusbymember(key, member, radius.getValue(), geoUnit, geoArgs), geoResultsConverter));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().georadiusbymember(key, member, radius.getValue(), geoUnit, geoArgs), geoResultsConverter));
                return null;
            }
            return geoResultsConverter.convert2(getConnection().georadiusbymember(key, member, radius.getValue(), geoUnit, geoArgs));
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoRemove(byte[] key, byte[]... values) {
        return zRem(key, values);
    }

    public Long time() {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().time(), LettuceConverters.toTimeConverter()));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().time(), LettuceConverters.toTimeConverter()));
                return null;
            }
            return LettuceConverters.toTimeConverter().convert2(getConnection().time());
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void killClient(String host, int port) {
        Assert.hasText(host, "Host for 'CLIENT KILL' must not be 'null' or 'empty'.");
        String client = String.format("%s:%s", host, Integer.valueOf(port));
        try {
            if (isPipelined()) {
                pipeline(new LettuceStatusResult(getAsyncConnection().clientKill(client)));
            } else {
                getConnection().clientKill(client);
            }
        } catch (Exception e) {
            convertLettuceAccessException(e);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void setClientName(byte[] name) {
        if (isQueueing()) {
            pipeline(new LettuceStatusResult(getAsyncConnection().clientSetname(name)));
        } else if (isQueueing()) {
            transaction(new LettuceTxResult(getConnection().clientSetname(name)));
        } else {
            getAsyncConnection().clientSetname(name);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void slaveOf(String host, int port) {
        Assert.hasText(host, "Host must not be null for 'SLAVEOF' command.");
        try {
            if (isPipelined()) {
                pipeline(new LettuceStatusResult(getAsyncConnection().slaveof(host, port)));
            } else if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().slaveof(host, port)));
            } else {
                getConnection().slaveof(host, port);
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public String getClientName() {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().clientGetname(), LettuceConverters.bytesToString()));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().clientGetname(), LettuceConverters.bytesToString()));
                return null;
            }
            return LettuceConverters.toString((byte[]) getConnection().clientGetname());
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public List<RedisClientInfo> getClientList() {
        if (isPipelined()) {
            throw new UnsupportedOperationException("Cannot be called in pipeline mode.");
        }
        if (isQueueing()) {
            transaction(new LettuceTxResult(getAsyncConnection().clientList(), LettuceConverters.stringToRedisClientListConverter()));
            return null;
        }
        return LettuceConverters.toListOfRedisClientInformation(getConnection().clientList());
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void slaveOfNoOne() {
        try {
            if (isPipelined()) {
                pipeline(new LettuceStatusResult(getAsyncConnection().slaveofNoOne()));
            } else if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().slaveofNoOne()));
            } else {
                getConnection().slaveofNoOne();
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public Cursor<byte[]> scan() {
        return scan(0L, ScanOptions.NONE);
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Cursor<byte[]> scan(ScanOptions options) {
        return scan(0L, options != null ? options : ScanOptions.NONE);
    }

    public Cursor<byte[]> scan(long cursorId, ScanOptions options) {
        return new ScanCursor<byte[]>(cursorId, options) { // from class: org.springframework.data.redis.connection.lettuce.LettuceConnection.1
            @Override // org.springframework.data.redis.core.ScanCursor
            protected ScanIteration<byte[]> doScan(long cursorId2, ScanOptions options2) {
                if (!LettuceConnection.this.isQueueing() && !LettuceConnection.this.isPipelined()) {
                    com.lambdaworks.redis.ScanCursor scanCursor = LettuceConnection.this.getScanCursor(cursorId2);
                    ScanArgs scanArgs = LettuceConnection.this.getScanArgs(options2);
                    KeyScanCursor<byte[]> keyScanCursor = LettuceConnection.this.getConnection().scan(scanCursor, scanArgs);
                    String nextCursorId = keyScanCursor.getCursor();
                    List<byte[]> keys = keyScanCursor.getKeys();
                    return new ScanIteration<>(Long.valueOf(nextCursorId).longValue(), keys);
                }
                throw new UnsupportedOperationException("'SCAN' cannot be called in pipeline / transaction mode.");
            }

            @Override // org.springframework.data.redis.core.ScanCursor
            protected void doClose() throws DataAccessException {
                LettuceConnection.this.close();
            }
        }.open();
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Cursor<Map.Entry<byte[], byte[]>> hScan(byte[] key, ScanOptions options) {
        return hScan(key, 0L, options);
    }

    public Cursor<Map.Entry<byte[], byte[]>> hScan(byte[] key, long cursorId, ScanOptions options) {
        return new KeyBoundCursor<Map.Entry<byte[], byte[]>>(key, cursorId, options) { // from class: org.springframework.data.redis.connection.lettuce.LettuceConnection.2
            @Override // org.springframework.data.redis.core.KeyBoundCursor
            protected ScanIteration<Map.Entry<byte[], byte[]>> doScan(byte[] key2, long cursorId2, ScanOptions options2) {
                if (!LettuceConnection.this.isQueueing() && !LettuceConnection.this.isPipelined()) {
                    com.lambdaworks.redis.ScanCursor scanCursor = LettuceConnection.this.getScanCursor(cursorId2);
                    ScanArgs scanArgs = LettuceConnection.this.getScanArgs(options2);
                    MapScanCursor<byte[], byte[]> mapScanCursor = LettuceConnection.this.getConnection().hscan(key2, scanCursor, scanArgs);
                    String nextCursorId = mapScanCursor.getCursor();
                    Map<byte[], byte[]> values = mapScanCursor.getMap();
                    return new ScanIteration<>(Long.valueOf(nextCursorId).longValue(), values.entrySet());
                }
                throw new UnsupportedOperationException("'HSCAN' cannot be called in pipeline / transaction mode.");
            }

            @Override // org.springframework.data.redis.core.ScanCursor
            protected void doClose() throws DataAccessException {
                LettuceConnection.this.close();
            }
        }.open();
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Cursor<byte[]> sScan(byte[] key, ScanOptions options) {
        return sScan(key, 0L, options);
    }

    public Cursor<byte[]> sScan(byte[] key, long cursorId, ScanOptions options) {
        return new KeyBoundCursor<byte[]>(key, cursorId, options) { // from class: org.springframework.data.redis.connection.lettuce.LettuceConnection.3
            @Override // org.springframework.data.redis.core.KeyBoundCursor
            protected ScanIteration<byte[]> doScan(byte[] key2, long cursorId2, ScanOptions options2) {
                if (!LettuceConnection.this.isQueueing() && !LettuceConnection.this.isPipelined()) {
                    com.lambdaworks.redis.ScanCursor scanCursor = LettuceConnection.this.getScanCursor(cursorId2);
                    ScanArgs scanArgs = LettuceConnection.this.getScanArgs(options2);
                    ValueScanCursor<byte[]> valueScanCursor = LettuceConnection.this.getConnection().sscan(key2, scanCursor, scanArgs);
                    String nextCursorId = valueScanCursor.getCursor();
                    List<byte[]> values = (List) LettuceConnection.this.failsafeReadScanValues(valueScanCursor.getValues(), null);
                    return new ScanIteration<>(Long.valueOf(nextCursorId).longValue(), values);
                }
                throw new UnsupportedOperationException("'SSCAN' cannot be called in pipeline / transaction mode.");
            }

            @Override // org.springframework.data.redis.core.ScanCursor
            protected void doClose() throws DataAccessException {
                LettuceConnection.this.close();
            }
        }.open();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Cursor<RedisZSetCommands.Tuple> zScan(byte[] key, ScanOptions options) {
        return zScan(key, 0L, options);
    }

    public Cursor<RedisZSetCommands.Tuple> zScan(byte[] key, long cursorId, ScanOptions options) {
        return new KeyBoundCursor<RedisZSetCommands.Tuple>(key, cursorId, options) { // from class: org.springframework.data.redis.connection.lettuce.LettuceConnection.4
            @Override // org.springframework.data.redis.core.KeyBoundCursor
            protected ScanIteration<RedisZSetCommands.Tuple> doScan(byte[] key2, long cursorId2, ScanOptions options2) {
                if (!LettuceConnection.this.isQueueing() && !LettuceConnection.this.isPipelined()) {
                    com.lambdaworks.redis.ScanCursor scanCursor = LettuceConnection.this.getScanCursor(cursorId2);
                    ScanArgs scanArgs = LettuceConnection.this.getScanArgs(options2);
                    ScoredValueScanCursor<byte[]> scoredValueScanCursor = LettuceConnection.this.getConnection().zscan(key2, scanCursor, scanArgs);
                    String nextCursorId = scoredValueScanCursor.getCursor();
                    List<ScoredValue<byte[]>> result = scoredValueScanCursor.getValues();
                    List<RedisZSetCommands.Tuple> values = (List) LettuceConnection.this.failsafeReadScanValues(result, LettuceConverters.scoredValuesToTupleList());
                    return new ScanIteration<>(Long.valueOf(nextCursorId).longValue(), values);
                }
                throw new UnsupportedOperationException("'ZSCAN' cannot be called in pipeline / transaction mode.");
            }

            @Override // org.springframework.data.redis.core.ScanCursor
            protected void doClose() throws DataAccessException {
                LettuceConnection.this.close();
            }
        }.open();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public <T> T failsafeReadScanValues(List<?> list, Converter converter) {
        if (converter == null) {
            return list;
        }
        try {
            return (T) converter.convert2(list);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public void setConvertPipelineAndTxResults(boolean convertPipelineAndTxResults) {
        this.convertPipelineAndTxResults = convertPipelineAndTxResults;
    }

    private void checkSubscription() {
        if (isSubscribed()) {
            throw new RedisSubscribedConnectionException("Connection already subscribed; use the connection Subscription to cancel or add new channels");
        }
    }

    private StatefulRedisPubSubConnection<byte[], byte[]> switchToPubSub() throws DataAccessException {
        close();
        return this.client.connectPubSub(CODEC);
    }

    private void pipeline(LettuceResult result) {
        if (isQueueing()) {
            transaction(result);
        } else {
            this.ppline.add(result);
        }
    }

    private void transaction(FutureResult<?> result) {
        this.txResults.add(result);
    }

    private RedisClusterAsyncCommands<byte[], byte[]> getAsyncConnection() {
        if (isQueueing()) {
            return getAsyncDedicatedConnection();
        }
        if (this.asyncSharedConn != null && (this.asyncSharedConn instanceof StatefulRedisConnection)) {
            return this.asyncSharedConn.async();
        }
        return getAsyncDedicatedConnection();
    }

    protected RedisClusterCommands<byte[], byte[]> getConnection() {
        if (isQueueing()) {
            return getDedicatedConnection();
        }
        if (this.asyncSharedConn != null) {
            if (this.asyncSharedConn instanceof StatefulRedisConnection) {
                return this.asyncSharedConn.sync();
            }
            if (this.asyncSharedConn instanceof StatefulRedisClusterConnection) {
                return this.asyncSharedConn.sync();
            }
        }
        return getDedicatedConnection();
    }

    protected RedisClusterAsyncCommands<byte[], byte[]> getAsyncDedicatedConnection() {
        if (this.asyncDedicatedConn == null) {
            this.asyncDedicatedConn = doGetAsyncDedicatedConnection();
            if (this.pool == null && (this.asyncDedicatedConn instanceof StatefulRedisConnection)) {
                this.asyncDedicatedConn.sync().select(this.dbIndex);
            }
        }
        if (this.asyncDedicatedConn instanceof StatefulRedisConnection) {
            return this.asyncDedicatedConn.async();
        }
        if (this.asyncDedicatedConn instanceof StatefulRedisClusterConnection) {
            return this.asyncDedicatedConn.async();
        }
        throw new IllegalStateException(String.format("%s is not a supported connection type.", this.asyncDedicatedConn.getClass().getName()));
    }

    protected StatefulConnection<byte[], byte[]> doGetAsyncDedicatedConnection() {
        if (this.pool != null) {
            return this.pool.getResource();
        }
        return this.client.connect(CODEC);
    }

    private RedisClusterCommands<byte[], byte[]> getDedicatedConnection() {
        if (this.asyncDedicatedConn == null) {
            this.asyncDedicatedConn = doGetAsyncDedicatedConnection();
            if (this.pool == null && (this.asyncDedicatedConn instanceof StatefulRedisConnection)) {
                this.asyncDedicatedConn.sync().select(this.dbIndex);
            }
        }
        if (this.asyncDedicatedConn instanceof StatefulRedisConnection) {
            return this.asyncDedicatedConn.sync();
        }
        if (this.asyncDedicatedConn instanceof StatefulRedisClusterConnection) {
            return this.asyncDedicatedConn.sync();
        }
        throw new IllegalStateException(String.format("%s is not a supported connection type.", this.asyncDedicatedConn.getClass().getName()));
    }

    private Future<Long> asyncBitOp(RedisStringCommands.BitOperation op, byte[] destination, byte[]... keys) {
        switch (op) {
            case AND:
                return getAsyncConnection().bitopAnd(destination, keys);
            case OR:
                return getAsyncConnection().bitopOr(destination, keys);
            case XOR:
                return getAsyncConnection().bitopXor(destination, keys);
            case NOT:
                if (keys.length != 1) {
                    throw new UnsupportedOperationException("Bitop NOT should only be performed against one key");
                }
                return getAsyncConnection().bitopNot(destination, keys[0]);
            default:
                throw new UnsupportedOperationException("Bit operation " + op + " is not supported");
        }
    }

    private Long syncBitOp(RedisStringCommands.BitOperation op, byte[] destination, byte[]... keys) {
        switch (op) {
            case AND:
                return getConnection().bitopAnd(destination, keys);
            case OR:
                return getConnection().bitopOr(destination, keys);
            case XOR:
                return getConnection().bitopXor(destination, keys);
            case NOT:
                if (keys.length != 1) {
                    throw new UnsupportedOperationException("Bitop NOT should only be performed against one key");
                }
                return getConnection().bitopNot(destination, keys[0]);
            default:
                throw new UnsupportedOperationException("Bit operation " + op + " is not supported");
        }
    }

    private byte[][] extractScriptKeys(int numKeys, byte[]... keysAndArgs) {
        if (numKeys > 0) {
            return (byte[][]) Arrays.copyOfRange(keysAndArgs, 0, numKeys);
        }
        return new byte[0][0];
    }

    private byte[][] extractScriptArgs(int numKeys, byte[]... keysAndArgs) {
        if (keysAndArgs.length > numKeys) {
            return (byte[][]) Arrays.copyOfRange(keysAndArgs, numKeys, keysAndArgs.length);
        }
        return new byte[0][0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.lambdaworks.redis.ScanCursor getScanCursor(long cursorId) {
        return com.lambdaworks.redis.ScanCursor.of(Long.toString(cursorId));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ScanArgs getScanArgs(ScanOptions options) {
        if (options == null) {
            return null;
        }
        ScanArgs scanArgs = new ScanArgs();
        if (options.getPattern() != null) {
            scanArgs.match(options.getPattern());
        }
        if (options.getCount() != null) {
            scanArgs.limit(options.getCount().longValue());
        }
        return scanArgs;
    }

    private ZStoreArgs zStoreArgs(RedisZSetCommands.Aggregate aggregate, int[] weights) {
        ZStoreArgs args = new ZStoreArgs();
        if (aggregate != null) {
            switch (aggregate) {
                case MIN:
                    args.min();
                    break;
                case MAX:
                    args.max();
                    break;
                default:
                    args.sum();
                    break;
            }
        }
        long[] lg = new long[weights.length];
        for (int i = 0; i < lg.length; i++) {
            lg[i] = weights[i];
        }
        args.weights(lg);
        return args;
    }

    private void validateCommandIfRunningInTransactionMode(CommandType cmd, byte[]... args) {
        if (isQueueing()) {
            validateCommand(cmd, args);
        }
    }

    private void validateCommand(CommandType cmd, byte[]... args) {
        int length;
        org.springframework.data.redis.core.RedisCommand redisCommand = org.springframework.data.redis.core.RedisCommand.failsafeCommandLookup(cmd.name());
        if (!org.springframework.data.redis.core.RedisCommand.UNKNOWN.equals(redisCommand) && redisCommand.requiresArguments()) {
            if (args != null) {
                try {
                    length = args.length;
                } catch (IllegalArgumentException e) {
                    throw new InvalidDataAccessApiUsageException(String.format("Validation failed for %s command.", cmd), e);
                }
            } else {
                length = 0;
            }
            redisCommand.validateArgumentCount(length);
        }
    }

    @Override // org.springframework.data.redis.connection.AbstractRedisConnection
    protected boolean isActive(RedisNode node) {
        if (node == null) {
            return false;
        }
        StatefulRedisConnection<String, String> connection = null;
        try {
            connection = this.client.connect(getRedisURI(node));
            boolean zEqualsIgnoreCase = connection.sync().ping().equalsIgnoreCase("pong");
            if (connection != null) {
                connection.close();
            }
            return zEqualsIgnoreCase;
        } catch (Exception e) {
            if (connection != null) {
                connection.close();
            }
            return false;
        } catch (Throwable th) {
            if (connection != null) {
                connection.close();
            }
            throw th;
        }
    }

    private RedisURI getRedisURI(RedisNode node) {
        return RedisURI.Builder.redis(node.getHost(), node.getPort().intValue()).build();
    }

    @Override // org.springframework.data.redis.connection.AbstractRedisConnection
    protected RedisSentinelConnection getSentinelConnection(RedisNode sentinel) {
        StatefulRedisSentinelConnection<String, String> connection = this.client.connectSentinel(getRedisURI(sentinel));
        return new LettuceSentinelConnection(connection);
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/lettuce/LettuceConnection$TypeHints.class */
    static class TypeHints {
        private static final Map<CommandType, Class<? extends CommandOutput>> COMMAND_OUTPUT_TYPE_MAPPING = new HashMap();
        private static final Map<Class<?>, Constructor<CommandOutput>> CONSTRUCTORS = new ConcurrentHashMap();

        TypeHints() {
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.BITCOUNT, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.BITOP, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.DBSIZE, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.DECR, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.DECRBY, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.DEL, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.GETBIT, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.HDEL, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.HINCRBY, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.HLEN, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.INCR, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.INCRBY, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.LINSERT, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.LLEN, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.LPUSH, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.LPUSHX, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.LREM, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.PTTL, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.PUBLISH, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.RPUSH, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.RPUSHX, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SADD, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SCARD, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SDIFFSTORE, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SETBIT, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SETRANGE, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SINTERSTORE, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SREM, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SUNIONSTORE, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.STRLEN, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.TTL, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZADD, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZCOUNT, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZINTERSTORE, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZRANK, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZREM, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZREMRANGEBYRANK, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZREMRANGEBYSCORE, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZREVRANK, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZUNIONSTORE, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.PFCOUNT, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.PFMERGE, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.PFADD, IntegerOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.HINCRBYFLOAT, DoubleOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.INCRBYFLOAT, DoubleOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.MGET, ValueListOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZINCRBY, DoubleOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZSCORE, DoubleOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.HGETALL, MapOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.HKEYS, KeyListOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.KEYS, KeyListOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.BRPOP, KeyValueOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.BRPOPLPUSH, ValueOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ECHO, ValueOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.GET, ValueOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.GETRANGE, ValueOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.GETSET, ValueOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.HGET, ValueOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.LINDEX, ValueOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.LPOP, ValueOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.RANDOMKEY, ValueOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.RENAME, ValueOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.RPOP, ValueOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.RPOPLPUSH, ValueOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SPOP, ValueOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SRANDMEMBER, ValueOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.BGREWRITEAOF, StatusOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.BGSAVE, StatusOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.CLIENT, StatusOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.DEBUG, StatusOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.DISCARD, StatusOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.FLUSHALL, StatusOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.FLUSHDB, StatusOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.HMSET, StatusOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.INFO, StatusOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.LSET, StatusOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.LTRIM, StatusOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.MIGRATE, StatusOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.MSET, StatusOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.QUIT, StatusOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.RESTORE, StatusOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SAVE, StatusOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SELECT, StatusOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SET, StatusOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SETEX, StatusOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SHUTDOWN, StatusOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SLAVEOF, StatusOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SYNC, StatusOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.TYPE, StatusOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.WATCH, StatusOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.UNWATCH, StatusOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.HMGET, ValueListOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.MGET, ValueListOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.HVALS, ValueListOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.LRANGE, ValueListOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SORT, ValueListOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZRANGE, ValueListOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZRANGEBYSCORE, ValueListOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZREVRANGE, ValueListOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZREVRANGEBYSCORE, ValueListOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.EXISTS, BooleanOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.EXPIRE, BooleanOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.EXPIREAT, BooleanOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.HEXISTS, BooleanOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.HSET, BooleanOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.HSETNX, BooleanOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.MOVE, BooleanOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.MSETNX, BooleanOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.PERSIST, BooleanOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.PEXPIRE, BooleanOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.PEXPIREAT, BooleanOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.RENAMENX, BooleanOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SETNX, BooleanOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SISMEMBER, BooleanOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SMOVE, BooleanOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.EXEC, MultiOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.MULTI, MultiOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.LASTSAVE, DateOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SDIFF, ValueSetOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SINTER, ValueSetOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SMEMBERS, ValueSetOutput.class);
            COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SUNION, ValueSetOutput.class);
        }

        public CommandOutput getTypeHint(CommandType type) {
            return getTypeHint(type, new ByteArrayOutput(LettuceConnection.CODEC));
        }

        public CommandOutput getTypeHint(CommandType type, CommandOutput defaultType) {
            if (type == null || !COMMAND_OUTPUT_TYPE_MAPPING.containsKey(type)) {
                return defaultType;
            }
            CommandOutput<?, ?, ?> outputType = instanciateCommandOutput(COMMAND_OUTPUT_TYPE_MAPPING.get(type));
            return outputType != null ? outputType : defaultType;
        }

        private CommandOutput<?, ?, ?> instanciateCommandOutput(Class<? extends CommandOutput> type) {
            Assert.notNull(type, "Cannot create instance for 'null' type.");
            Constructor<CommandOutput> constructor = CONSTRUCTORS.get(type);
            if (constructor == null) {
                constructor = ClassUtils.getConstructorIfAvailable(type, RedisCodec.class);
                CONSTRUCTORS.put(type, constructor);
            }
            return (CommandOutput) BeanUtils.instantiateClass(constructor, LettuceConnection.CODEC);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByScore(byte[] key, String min, String max) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().zrangebyscore(key, min, max), LettuceConverters.bytesListToBytesSet()));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().zrangebyscore(key, min, max), LettuceConverters.bytesListToBytesSet()));
                return null;
            }
            return LettuceConverters.toBytesSet(getConnection().zrangebyscore(key, min, max));
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByScore(byte[] key, String min, String max, long offset, long count) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().zrangebyscore(key, min, max, offset, count), LettuceConverters.bytesListToBytesSet()));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().zrangebyscore(key, min, max, offset, count), LettuceConverters.bytesListToBytesSet()));
                return null;
            }
            return LettuceConverters.toBytesSet(getConnection().zrangebyscore(key, min, max, offset, count));
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.HyperLogLogCommands
    public Long pfAdd(byte[] key, byte[]... values) {
        Assert.notEmpty(values, "PFADD requires at least one non 'null' value.");
        Assert.noNullElements(values, "Values for PFADD must not contain 'null'.");
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().pfadd(key, values)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceResult(getAsyncConnection().pfadd(key, values)));
                return null;
            }
            return getConnection().pfadd(key, values);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public Long pfCount(byte[]... keys) {
        Assert.notEmpty(keys, "PFCOUNT requires at least one non 'null' key.");
        Assert.noNullElements(keys, "Keys for PFCOUNT must not contain 'null'.");
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().pfcount(keys)));
                return null;
            }
            if (isQueueing()) {
                transaction(new LettuceResult(getAsyncConnection().pfcount(keys)));
                return null;
            }
            return getConnection().pfcount(keys);
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    public void pfMerge(byte[] destinationKey, byte[]... sourceKeys) {
        Assert.notEmpty(sourceKeys, "PFMERGE requires at least one non 'null' source key.");
        Assert.noNullElements(sourceKeys, "source key for PFMERGE must not contain 'null'.");
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().pfmerge(destinationKey, sourceKeys)));
            } else if (isQueueing()) {
                transaction(new LettuceResult(getAsyncConnection().pfmerge(destinationKey, sourceKeys)));
            } else {
                getConnection().pfmerge(destinationKey, sourceKeys);
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
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
        String min = LettuceConverters.boundaryToBytesForZRangeByLex(range.getMin(), LettuceConverters.MINUS_BYTES);
        String max = LettuceConverters.boundaryToBytesForZRangeByLex(range.getMax(), LettuceConverters.PLUS_BYTES);
        try {
            if (isPipelined()) {
                if (limit != null) {
                    pipeline(new LettuceResult(getAsyncConnection().zrangebylex(key, min, max, limit.getOffset(), limit.getCount()), LettuceConverters.bytesListToBytesSet()));
                    return null;
                }
                pipeline(new LettuceResult(getAsyncConnection().zrangebylex(key, min, max), LettuceConverters.bytesListToBytesSet()));
                return null;
            }
            if (isQueueing()) {
                if (limit != null) {
                    transaction(new LettuceTxResult(getConnection().zrangebylex(key, min, max, limit.getOffset(), limit.getCount()), LettuceConverters.bytesListToBytesSet()));
                    return null;
                }
                transaction(new LettuceTxResult(getConnection().zrangebylex(key, min, max), LettuceConverters.bytesListToBytesSet()));
                return null;
            }
            if (limit != null) {
                return LettuceConverters.bytesListToBytesSet().convert2(getConnection().zrangebylex(key, min, max, limit.getOffset(), limit.getCount()));
            }
            return LettuceConverters.bytesListToBytesSet().convert2(getConnection().zrangebylex(key, min, max));
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void migrate(byte[] key, RedisNode target, int dbIndex, RedisServerCommands.MigrateOption option) {
        migrate(key, target, dbIndex, option, Long.MAX_VALUE);
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void migrate(byte[] key, RedisNode target, int dbIndex, RedisServerCommands.MigrateOption option, long timeout) {
        try {
            if (isPipelined()) {
                pipeline(new LettuceResult(getAsyncConnection().migrate(target.getHost(), target.getPort().intValue(), key, dbIndex, timeout)));
            } else if (isQueueing()) {
                transaction(new LettuceTxResult(getConnection().migrate(target.getHost(), target.getPort().intValue(), key, dbIndex, timeout)));
            } else {
                getConnection().migrate(target.getHost(), target.getPort().intValue(), key, dbIndex, timeout);
            }
        } catch (Exception ex) {
            throw convertLettuceAccessException(ex);
        }
    }
}
