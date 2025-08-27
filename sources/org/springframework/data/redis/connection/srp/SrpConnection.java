package org.springframework.data.redis.connection.srp;

import com.google.common.base.Charsets;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.DataAccessException;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.ExceptionTranslationStrategy;
import org.springframework.data.redis.FallbackExceptionTranslationStrategy;
import org.springframework.data.redis.RedisConnectionFailureException;
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
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.util.Assert;
import redis.Command;
import redis.client.RedisClient;
import redis.client.RedisException;
import redis.reply.MultiBulkReply;
import redis.reply.Reply;

@Deprecated
/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/srp/SrpConnection.class */
public class SrpConnection extends AbstractRedisConnection {
    private static final ExceptionTranslationStrategy EXCEPTION_TRANSLATION = new FallbackExceptionTranslationStrategy(SrpConverters.exceptionConverter());
    private static final Object[] EMPTY_PARAMS_ARRAY = new Object[0];
    private static final byte[] WITHSCORES = "WITHSCORES".getBytes(Charsets.UTF_8);
    private static final byte[] BY = "BY".getBytes(Charsets.UTF_8);
    private static final byte[] GET = "GET".getBytes(Charsets.UTF_8);
    private static final byte[] ALPHA = "ALPHA".getBytes(Charsets.UTF_8);
    private static final byte[] STORE = "STORE".getBytes(Charsets.UTF_8);
    private final RedisClient client;
    private final BlockingQueue<SrpConnection> queue;
    private boolean isClosed;
    private boolean isMulti;
    private boolean pipelineRequested;
    private RedisClient.Pipeline pipeline;
    private PipelineTracker callback;
    private PipelineTracker txTracker;
    private volatile SrpSubscription subscription;
    private boolean convertPipelineAndTxResults;

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/srp/SrpConnection$PipelineTracker.class */
    private class PipelineTracker implements FutureCallback<Reply> {
        private final List<Object> results = Collections.synchronizedList(new ArrayList());
        private final Queue<FutureResult> futureResults = new LinkedList();
        private boolean convertResults;

        public PipelineTracker(boolean convertResults) {
            this.convertResults = convertResults;
        }

        @Override // com.google.common.util.concurrent.FutureCallback
        public void onSuccess(Reply result) {
            this.results.add(result.data());
        }

        @Override // com.google.common.util.concurrent.FutureCallback
        public void onFailure(Throwable t) {
            this.results.add(t);
        }

        public List<Object> complete() {
            int txResults = 0;
            List<ListenableFuture<? extends Reply>> futures = new ArrayList<>();
            for (FutureResult future : this.futureResults) {
                if (future instanceof SrpTxResult) {
                    txResults++;
                } else {
                    ListenableFuture<? extends Reply> f = (ListenableFuture) future.getResultHolder();
                    futures.add(f);
                }
            }
            try {
                Futures.successfulAsList(futures).get();
            } catch (Exception e) {
            }
            if (this.futureResults.size() != this.results.size() + txResults) {
                throw new RedisPipelineException("Received a different number of results than expected. Expected: " + this.futureResults.size(), this.results);
            }
            List<Object> convertedResults = new ArrayList<>();
            int i = 0;
            for (FutureResult future2 : this.futureResults) {
                if (future2 instanceof SrpTxResult) {
                    PipelineTracker txTracker = ((SrpTxResult) future2).getResultHolder();
                    if (txTracker != null) {
                        convertedResults.add(SrpConnection.this.getPipelinedResults(txTracker, true));
                    } else {
                        convertedResults.add(null);
                    }
                } else {
                    Object result = this.results.get(i);
                    if ((result instanceof Exception) || !this.convertResults) {
                        convertedResults.add(result);
                    } else if (!future2.isStatus()) {
                        convertedResults.add(future2.convert(result));
                    }
                    i++;
                }
            }
            return convertedResults;
        }

        public void addCommand(FutureResult result) {
            this.futureResults.add(result);
            if (!(result instanceof SrpTxResult) && result.getResultHolder() != null) {
                Futures.addCallback(((SrpGenericResult) result).getResultHolder(), this);
            }
        }

        public void close() {
            this.results.clear();
            this.futureResults.clear();
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/srp/SrpConnection$SrpGenericResult.class */
    private class SrpGenericResult extends FutureResult<ListenableFuture<? extends Reply>> {
        public SrpGenericResult(ListenableFuture<? extends Reply> resultHolder, Converter converter) {
            super(resultHolder, converter);
        }

        public SrpGenericResult(ListenableFuture<? extends Reply> resultHolder) {
            super(resultHolder);
        }

        @Override // org.springframework.data.redis.connection.FutureResult
        public Object get() {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/srp/SrpConnection$SrpResult.class */
    private class SrpResult extends SrpGenericResult {
        public <T> SrpResult(ListenableFuture<? extends Reply<T>> resultHolder, Converter<T, ?> converter) {
            super(resultHolder, converter);
        }

        public SrpResult(ListenableFuture<? extends Reply> resultHolder) {
            super(resultHolder);
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/srp/SrpConnection$SrpStatusResult.class */
    private class SrpStatusResult extends SrpResult {
        public SrpStatusResult(ListenableFuture<? extends Reply> resultHolder) {
            super(resultHolder);
            setStatus(true);
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/srp/SrpConnection$SrpTxResult.class */
    private class SrpTxResult extends FutureResult<PipelineTracker> {
        public SrpTxResult(PipelineTracker txTracker) {
            super(txTracker);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // org.springframework.data.redis.connection.FutureResult
        public List<Object> get() {
            if (this.resultHolder == 0) {
                return null;
            }
            return ((PipelineTracker) this.resultHolder).complete();
        }
    }

    SrpConnection(RedisClient client) {
        this.isClosed = false;
        this.isMulti = false;
        this.pipelineRequested = false;
        this.convertPipelineAndTxResults = true;
        Assert.notNull(client, "RedisClient must not be null!");
        this.client = client;
        this.queue = new ArrayBlockingQueue(50);
    }

    public SrpConnection(String host, int port, BlockingQueue<SrpConnection> queue) {
        this.isClosed = false;
        this.isMulti = false;
        this.pipelineRequested = false;
        this.convertPipelineAndTxResults = true;
        try {
            this.client = new RedisClient(host, port);
            this.queue = queue;
        } catch (RedisException e) {
            throw new RedisConnectionFailureException("Could not connect", e);
        } catch (IOException e2) {
            throw new RedisConnectionFailureException("Could not connect", e2);
        }
    }

    public SrpConnection(String host, int port, String password, BlockingQueue<SrpConnection> queue) {
        this(host, port, queue);
        try {
            this.client.auth(password);
        } catch (RedisException e) {
            throw new RedisConnectionFailureException("Could not connect", e);
        }
    }

    protected DataAccessException convertSrpAccessException(Exception ex) {
        return EXCEPTION_TRANSLATION.translate(ex);
    }

    @Override // org.springframework.data.redis.connection.RedisCommands
    public Object execute(String command, byte[]... args) {
        Assert.hasText(command, "a valid command needs to be specified");
        try {
            String name = command.trim().toUpperCase();
            Command cmd = new Command(name.getBytes(Charsets.UTF_8), args);
            if (isPipelined()) {
                pipeline(new SrpResult(this.client.pipeline(name, cmd)));
                return null;
            }
            return this.client.execute(name, cmd).data();
        } catch (RedisException ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.AbstractRedisConnection, org.springframework.data.redis.connection.RedisConnection
    public void close() throws DataAccessException {
        super.close();
        this.isClosed = true;
        this.queue.remove(this);
        if (this.subscription != null) {
            if (this.subscription.isAlive()) {
                this.subscription.doClose();
            }
            this.subscription = null;
        }
        try {
            this.client.close();
        } catch (IOException ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public boolean isClosed() {
        return this.isClosed;
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public RedisClient getNativeConnection() {
        return this.client;
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public boolean isQueueing() {
        return this.isMulti;
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public boolean isPipelined() {
        return this.pipelineRequested || this.txTracker != null;
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public void openPipeline() {
        this.pipelineRequested = true;
        initPipeline();
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public List<byte[]> sort(byte[] key, SortParameters params) {
        Object[] sort = sortParams(params);
        try {
            if (isPipelined()) {
                pipeline(new SrpGenericResult(this.pipeline.sort(key, sort), SrpConverters.repliesToBytesList()));
                return null;
            }
            return SrpConverters.toBytesList((Reply[]) this.client.sort(key, sort).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long sort(byte[] key, SortParameters params, byte[] sortKey) {
        Object[] sort = sortParams(params, sortKey);
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.sort(key, sort)));
                return null;
            }
            return (Long) this.client.sort(key, sort).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public Long dbSize() {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.dbsize()));
                return null;
            }
            return this.client.dbsize().data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void flushDb() {
        try {
            if (isPipelined()) {
                pipeline(new SrpStatusResult(this.pipeline.flushdb()));
            } else {
                this.client.flushdb();
            }
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void flushAll() {
        try {
            if (isPipelined()) {
                pipeline(new SrpStatusResult(this.pipeline.flushall()));
            } else {
                this.client.flushall();
            }
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void bgSave() {
        try {
            if (isPipelined()) {
                pipeline(new SrpStatusResult(this.pipeline.bgsave()));
            } else {
                this.client.bgsave();
            }
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void bgReWriteAof() {
        try {
            if (isPipelined()) {
                pipeline(new SrpStatusResult(this.pipeline.bgrewriteaof()));
            } else {
                this.client.bgrewriteaof();
            }
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
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
                pipeline(new SrpStatusResult(this.pipeline.save()));
            } else {
                this.client.save();
            }
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public List<String> getConfig(String param) {
        try {
            if (isPipelined()) {
                pipeline(new SrpGenericResult(this.pipeline.config_get(param), SrpConverters.repliesToStringList()));
                return null;
            }
            return SrpConverters.toStringList(this.client.config_get(param).data().toString());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public Properties info() {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.info((Object) null), SrpConverters.bytesToProperties()));
                return null;
            }
            return SrpConverters.toProperties(this.client.info((Object) null).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public Properties info(String section) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.info(section), SrpConverters.bytesToProperties()));
                return null;
            }
            return SrpConverters.toProperties(this.client.info(section).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public Long lastSave() {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.lastsave()));
                return null;
            }
            return this.client.lastsave().data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void setConfig(String param, String value) {
        try {
            if (isPipelined()) {
                pipeline(new SrpStatusResult(this.pipeline.config_set(param, value)));
            } else {
                this.client.config_set(param, value);
            }
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void resetConfigStats() {
        try {
            if (isPipelined()) {
                pipeline(new SrpStatusResult(this.pipeline.config_resetstat()));
            } else {
                this.client.config_resetstat();
            }
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void shutdown() {
        byte[] save = "SAVE".getBytes(Charsets.UTF_8);
        try {
            if (isPipelined()) {
                pipeline(new SrpStatusResult(this.pipeline.shutdown(save, (Object) null)));
            } else {
                this.client.shutdown(save, (Object) null);
            }
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void shutdown(RedisServerCommands.ShutdownOption option) {
        if (option == null) {
            shutdown();
            return;
        }
        byte[] save = option.name().getBytes(Charsets.UTF_8);
        try {
            if (isPipelined()) {
                pipeline(new SrpStatusResult(this.pipeline.shutdown(save, (Object) null)));
            } else {
                this.client.shutdown(save, (Object) null);
            }
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionCommands
    public byte[] echo(byte[] message) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.echo(message)));
                return null;
            }
            return this.client.echo(message).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionCommands
    public String ping() {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.ping()));
                return null;
            }
            return this.client.ping().data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long del(byte[]... keys) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.del(keys)));
                return null;
            }
            return this.client.del(keys).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisTxCommands
    public void discard() {
        this.isMulti = false;
        try {
            this.txTracker = null;
            this.client.discard();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisTxCommands
    public List<Object> exec() {
        this.isMulti = false;
        try {
            try {
                Future<Boolean> exec = this.client.exec();
                boolean resultsSet = exec.get().booleanValue();
                if (!resultsSet) {
                    if (this.pipelineRequested) {
                        pipeline(new SrpTxResult(null));
                    }
                    return null;
                }
                if (this.pipelineRequested) {
                    pipeline(new SrpTxResult(this.txTracker));
                    this.txTracker = null;
                    return null;
                }
                List<Object> listCloseTransaction = closeTransaction();
                this.txTracker = null;
                return listCloseTransaction;
            } catch (Exception ex) {
                throw convertSrpAccessException(ex);
            }
        } finally {
            this.txTracker = null;
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean exists(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.exists(key), SrpConverters.longToBoolean()));
                return null;
            }
            return SrpConverters.toBoolean(this.client.exists(key).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean expire(byte[] key, long seconds) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.expire(key, Long.valueOf(seconds)), SrpConverters.longToBoolean()));
                return null;
            }
            return SrpConverters.toBoolean(this.client.expire(key, Long.valueOf(seconds)).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean expireAt(byte[] key, long unixTime) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.expireat(key, Long.valueOf(unixTime)), SrpConverters.longToBoolean()));
                return null;
            }
            return SrpConverters.toBoolean(this.client.expireat(key, Long.valueOf(unixTime)).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean pExpire(byte[] key, long millis) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.pexpire(key, Long.valueOf(millis)), SrpConverters.longToBoolean()));
                return null;
            }
            return SrpConverters.toBoolean(this.client.pexpire(key, Long.valueOf(millis)).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean pExpireAt(byte[] key, long unixTimeInMillis) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.pexpireat(key, Long.valueOf(unixTimeInMillis)), SrpConverters.longToBoolean()));
                return null;
            }
            return SrpConverters.toBoolean(this.client.pexpireat(key, Long.valueOf(unixTimeInMillis)).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Set<byte[]> keys(byte[] pattern) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.keys(pattern), SrpConverters.repliesToBytesSet()));
                return null;
            }
            return SrpConverters.toBytesSet(this.client.keys(pattern).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisTxCommands
    public void multi() {
        if (isQueueing()) {
            return;
        }
        this.isMulti = true;
        initTxTracker();
        try {
            this.client.multi();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean persist(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.persist(key), SrpConverters.longToBoolean()));
                return null;
            }
            return SrpConverters.toBoolean(this.client.persist(key).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean move(byte[] key, int dbIndex) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.move(key, Integer.valueOf(dbIndex)), SrpConverters.longToBoolean()));
                return null;
            }
            return SrpConverters.toBoolean(this.client.move(key, Integer.valueOf(dbIndex)).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public byte[] randomKey() {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.randomkey()));
                return null;
            }
            return this.client.randomkey().data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public void rename(byte[] oldName, byte[] newName) {
        try {
            if (isPipelined()) {
                pipeline(new SrpStatusResult(this.pipeline.rename(oldName, newName)));
            } else {
                this.client.rename(oldName, newName);
            }
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean renameNX(byte[] oldName, byte[] newName) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.renamenx(oldName, newName), SrpConverters.longToBoolean()));
                return null;
            }
            return SrpConverters.toBoolean(this.client.renamenx(oldName, newName).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionCommands
    public void select(int dbIndex) {
        try {
            if (isPipelined()) {
                pipeline(new SrpStatusResult(this.pipeline.select(Integer.valueOf(dbIndex))));
            } else {
                this.client.select(Integer.valueOf(dbIndex));
            }
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long ttl(byte[] key) {
        Assert.notNull(key, "Key must not be null!");
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.ttl(key)));
                return null;
            }
            return this.client.ttl(key).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long ttl(byte[] key, TimeUnit timeUnit) {
        Assert.notNull(key, "Key must not be null!");
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.ttl(key), Converters.secondsToTimeUnit(timeUnit)));
                return null;
            }
            return Long.valueOf(Converters.secondsToTimeUnit(this.client.ttl(key).data().longValue(), timeUnit));
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long pTtl(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.pttl(key)));
                return null;
            }
            return this.client.pttl(key).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long pTtl(byte[] key, TimeUnit timeUnit) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.pttl(key), Converters.millisecondsToTimeUnit(timeUnit)));
                return null;
            }
            return Long.valueOf(Converters.millisecondsToTimeUnit(this.client.pttl(key).data().longValue(), timeUnit));
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public byte[] dump(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.dump(key)));
                return null;
            }
            return this.client.dump(key).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public void restore(byte[] key, long ttlInMillis, byte[] serializedValue) {
        try {
            if (isPipelined()) {
                pipeline(new SrpStatusResult(this.pipeline.restore(key, Long.valueOf(ttlInMillis), serializedValue)));
            } else {
                this.client.restore(key, Long.valueOf(ttlInMillis), serializedValue);
            }
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public DataType type(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.type(key), SrpConverters.stringToDataType()));
                return null;
            }
            return SrpConverters.toDataType(this.client.type(key).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisTxCommands
    public void unwatch() {
        try {
            if (isPipelined()) {
                pipeline(new SrpStatusResult(this.pipeline.unwatch()));
            } else {
                this.client.unwatch();
            }
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisTxCommands
    public void watch(byte[]... keys) {
        if (isQueueing()) {
            throw new UnsupportedOperationException();
        }
        try {
            if (isPipelined()) {
                pipeline(new SrpStatusResult(this.pipeline.watch(keys)));
            } else {
                this.client.watch(keys);
            }
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public byte[] get(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.get(key)));
                return null;
            }
            return this.client.get(key).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void set(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new SrpStatusResult(this.pipeline.set(key, value, new Object[0])));
            } else {
                this.client.set(key, value, new Object[0]);
            }
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void set(byte[] key, byte[] value, Expiration expiration, RedisStringCommands.SetOption option) {
        throw new UnsupportedOperationException("SET with options is not supported for Srp. Please use SETNX, SETEX, PSETEX.");
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public byte[] getSet(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.getset(key, value)));
                return null;
            }
            return this.client.getset(key, value).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long append(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.append(key, value)));
                return null;
            }
            return this.client.append(key, value).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public List<byte[]> mGet(byte[]... keys) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.mget(keys), SrpConverters.repliesToBytesList()));
                return null;
            }
            return SrpConverters.toBytesList(this.client.mget(keys).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void mSet(Map<byte[], byte[]> tuples) {
        try {
            if (isPipelined()) {
                pipeline(new SrpStatusResult(this.pipeline.mset(SrpConverters.toByteArrays(tuples))));
            } else {
                this.client.mset(SrpConverters.toByteArrays(tuples));
            }
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Boolean mSetNX(Map<byte[], byte[]> tuples) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.msetnx(SrpConverters.toByteArrays(tuples)), SrpConverters.longToBoolean()));
                return null;
            }
            return SrpConverters.toBoolean(this.client.msetnx(SrpConverters.toByteArrays(tuples)).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void setEx(byte[] key, long time, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new SrpStatusResult(this.pipeline.setex(key, Long.valueOf(time), value)));
            } else {
                this.client.setex(key, Long.valueOf(time), value);
            }
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void pSetEx(byte[] key, long milliseconds, byte[] value) {
        try {
            if (isPipelined()) {
                doPipelined(this.pipeline.psetex(key, Long.valueOf(milliseconds), value));
            } else {
                this.client.psetex(key, Long.valueOf(milliseconds), value);
            }
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Boolean setNX(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.setnx(key, value), SrpConverters.longToBoolean()));
                return null;
            }
            return SrpConverters.toBoolean(this.client.setnx(key, value).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public byte[] getRange(byte[] key, long start, long end) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.getrange(key, Long.valueOf(start), Long.valueOf(end))));
                return null;
            }
            return this.client.getrange(key, Long.valueOf(start), Long.valueOf(end)).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long decr(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.decr(key)));
                return null;
            }
            return this.client.decr(key).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long decrBy(byte[] key, long value) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.decrby(key, Long.valueOf(value))));
                return null;
            }
            return this.client.decrby(key, Long.valueOf(value)).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long incr(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.incr(key)));
                return null;
            }
            return this.client.incr(key).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long incrBy(byte[] key, long value) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.incrby(key, Long.valueOf(value))));
                return null;
            }
            return this.client.incrby(key, Long.valueOf(value)).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Double incrBy(byte[] key, double value) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.incrbyfloat(key, Double.valueOf(value)), SrpConverters.bytesToDouble()));
                return null;
            }
            return SrpConverters.toDouble(this.client.incrbyfloat(key, Double.valueOf(value)).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Boolean getBit(byte[] key, long offset) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.getbit(key, Long.valueOf(offset)), SrpConverters.longToBoolean()));
                return null;
            }
            return SrpConverters.toBoolean(this.client.getbit(key, Long.valueOf(offset)).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Boolean setBit(byte[] key, long offset, boolean value) {
        try {
            if (isPipelined()) {
                pipeline(new SrpGenericResult(this.pipeline.setbit(key, Long.valueOf(offset), SrpConverters.toBit(Boolean.valueOf(value))), SrpConverters.longToBooleanConverter()));
                return null;
            }
            return SrpConverters.toBoolean(this.client.setbit(key, Long.valueOf(offset), SrpConverters.toBit(Boolean.valueOf(value))));
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void setRange(byte[] key, byte[] value, long start) {
        try {
            if (isPipelined()) {
                pipeline(new SrpStatusResult(this.pipeline.setrange(key, Long.valueOf(start), value)));
            } else {
                this.client.setrange(key, Long.valueOf(start), value);
            }
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long strLen(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.strlen(key)));
                return null;
            }
            return this.client.strlen(key).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long bitCount(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.bitcount(key, 0, -1)));
                return null;
            }
            return this.client.bitcount(key, 0, -1).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long bitCount(byte[] key, long begin, long end) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.bitcount(key, Long.valueOf(begin), Long.valueOf(end))));
                return null;
            }
            return this.client.bitcount(key, Long.valueOf(begin), Long.valueOf(end)).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long bitOp(RedisStringCommands.BitOperation op, byte[] destination, byte[]... keys) {
        if (op == RedisStringCommands.BitOperation.NOT && keys.length > 1) {
            throw new UnsupportedOperationException("Bitop NOT should only be performed against one key");
        }
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.bitop(SrpConverters.toBytes(op), destination, keys)));
                return null;
            }
            return this.client.bitop(SrpConverters.toBytes(op), destination, keys).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lPush(byte[] key, byte[]... values) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.lpush(key, values)));
                return null;
            }
            return this.client.lpush(key, values).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long rPush(byte[] key, byte[]... values) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.rpush(key, values)));
                return null;
            }
            return this.client.rpush(key, values).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public List<byte[]> bLPop(int timeout, byte[]... keys) {
        Object[] args = popArgs(timeout, keys);
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.blpop(args), SrpConverters.repliesToBytesList()));
                return null;
            }
            return SrpConverters.toBytesList(this.client.blpop(args).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public List<byte[]> bRPop(int timeout, byte[]... keys) {
        Object[] args = popArgs(timeout, keys);
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.brpop(args), SrpConverters.repliesToBytesList()));
                return null;
            }
            return SrpConverters.toBytesList(this.client.brpop(args).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public byte[] lIndex(byte[] key, long index) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.lindex(key, Long.valueOf(index))));
                return null;
            }
            return this.client.lindex(key, Long.valueOf(index)).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lInsert(byte[] key, RedisListCommands.Position where, byte[] pivot, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.linsert(key, SrpConverters.toBytes(where), pivot, value)));
                return null;
            }
            return this.client.linsert(key, SrpConverters.toBytes(where), pivot, value).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lLen(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.llen(key)));
                return null;
            }
            return this.client.llen(key).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public byte[] lPop(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.lpop(key)));
                return null;
            }
            return this.client.lpop(key).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public List<byte[]> lRange(byte[] key, long start, long end) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.lrange(key, Long.valueOf(start), Long.valueOf(end)), SrpConverters.repliesToBytesList()));
                return null;
            }
            return SrpConverters.toBytesList(this.client.lrange(key, Long.valueOf(start), Long.valueOf(end)).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lRem(byte[] key, long count, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.lrem(key, Long.valueOf(count), value)));
                return null;
            }
            return this.client.lrem(key, Long.valueOf(count), value).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public void lSet(byte[] key, long index, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new SrpStatusResult(this.pipeline.lset(key, Long.valueOf(index), value)));
            } else {
                this.client.lset(key, Long.valueOf(index), value);
            }
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public void lTrim(byte[] key, long start, long end) {
        try {
            if (isPipelined()) {
                pipeline(new SrpStatusResult(this.pipeline.ltrim(key, Long.valueOf(start), Long.valueOf(end))));
            } else {
                this.client.ltrim(key, Long.valueOf(start), Long.valueOf(end));
            }
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public byte[] rPop(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.rpop(key)));
                return null;
            }
            return this.client.rpop(key).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public byte[] rPopLPush(byte[] srcKey, byte[] dstKey) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.rpoplpush(srcKey, dstKey)));
                return null;
            }
            return this.client.rpoplpush(srcKey, dstKey).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public byte[] bRPopLPush(int timeout, byte[] srcKey, byte[] dstKey) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.brpoplpush(srcKey, dstKey, Integer.valueOf(timeout))));
                return null;
            }
            return (byte[]) this.client.brpoplpush(srcKey, dstKey, Integer.valueOf(timeout)).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lPushX(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.lpushx(key, value)));
                return null;
            }
            return this.client.lpushx(key, value).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long rPushX(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.rpushx(key, value)));
                return null;
            }
            return this.client.rpushx(key, value).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sAdd(byte[] key, byte[]... values) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.sadd(key, values)));
                return null;
            }
            return this.client.sadd(key, values).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sCard(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.scard(key)));
                return null;
            }
            return this.client.scard(key).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Set<byte[]> sDiff(byte[]... keys) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.sdiff(keys), SrpConverters.repliesToBytesSet()));
                return null;
            }
            return SrpConverters.toBytesSet(this.client.sdiff(keys).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sDiffStore(byte[] destKey, byte[]... keys) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.sdiffstore(destKey, keys)));
                return null;
            }
            return this.client.sdiffstore(destKey, keys).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Set<byte[]> sInter(byte[]... keys) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.sinter(keys), SrpConverters.repliesToBytesSet()));
                return null;
            }
            return SrpConverters.toBytesSet(this.client.sinter(keys).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sInterStore(byte[] destKey, byte[]... keys) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.sinterstore(destKey, keys)));
                return null;
            }
            return this.client.sinterstore(destKey, keys).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Boolean sIsMember(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.sismember(key, value), SrpConverters.longToBoolean()));
                return null;
            }
            return SrpConverters.toBoolean(this.client.sismember(key, value).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Set<byte[]> sMembers(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.smembers(key), SrpConverters.repliesToBytesSet()));
                return null;
            }
            return SrpConverters.toBytesSet(this.client.smembers(key).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Boolean sMove(byte[] srcKey, byte[] destKey, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.smove(srcKey, destKey, value), SrpConverters.longToBoolean()));
                return null;
            }
            return SrpConverters.toBoolean(this.client.smove(srcKey, destKey, value).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public byte[] sPop(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.spop(key)));
                return null;
            }
            return this.client.spop(key).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public byte[] sRandMember(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.srandmember(key, (Object) null)));
                return null;
            }
            return (byte[]) this.client.srandmember(key, (Object) null).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public List<byte[]> sRandMember(byte[] key, long count) {
        try {
            if (isPipelined()) {
                pipeline(new SrpGenericResult(this.pipeline.srandmember(key, Long.valueOf(count)), SrpConverters.repliesToBytesList()));
                return null;
            }
            return SrpConverters.toBytesList(this.client.srandmember(key, Long.valueOf(count)).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sRem(byte[] key, byte[]... values) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.srem(key, values)));
                return null;
            }
            return this.client.srem(key, values).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Set<byte[]> sUnion(byte[]... keys) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.sunion(keys), SrpConverters.repliesToBytesSet()));
                return null;
            }
            return SrpConverters.toBytesSet(this.client.sunion(keys).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sUnionStore(byte[] destKey, byte[]... keys) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.sunionstore(destKey, keys)));
                return null;
            }
            return this.client.sunionstore(destKey, keys).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Boolean zAdd(byte[] key, double score, byte[] value) {
        try {
            if (!isPipelined()) {
                return SrpConverters.toBoolean(this.client.zadd(new Object[]{key, Double.valueOf(score), value}).data());
            }
            pipeline(new SrpResult(this.pipeline.zadd(new Object[]{key, Double.valueOf(score), value}), SrpConverters.longToBoolean()));
            return null;
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zAdd(byte[] key, Set<RedisZSetCommands.Tuple> tuples) {
        try {
            List<Object> args = new ArrayList<>();
            args.add(key);
            args.addAll(SrpConverters.toObjects(tuples));
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.zadd(args.toArray())));
                return null;
            }
            return this.client.zadd(args.toArray()).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zCard(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.zcard(key)));
                return null;
            }
            return this.client.zcard(key).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zCount(byte[] key, double min, double max) {
        return zCount(key, new RedisZSetCommands.Range().gte(Double.valueOf(min)).lte(Double.valueOf(max)));
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zCount(byte[] key, RedisZSetCommands.Range range) {
        byte[] min = SrpConverters.boundaryToBytesForZRange(range.getMin(), SrpConverters.toBytes("-inf"));
        byte[] max = SrpConverters.boundaryToBytesForZRange(range.getMax(), SrpConverters.toBytes("+inf"));
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.zcount(key, min, max)));
                return null;
            }
            return this.client.zcount(key, min, max).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Double zIncrBy(byte[] key, double increment, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.zincrby(key, Double.valueOf(increment), value), SrpConverters.bytesToDouble()));
                return null;
            }
            return SrpConverters.toDouble(this.client.zincrby(key, Double.valueOf(increment), value).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zInterStore(byte[] destKey, RedisZSetCommands.Aggregate aggregate, int[] weights, byte[]... sets) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zInterStore(byte[] destKey, byte[]... sets) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.zinterstore(destKey, Integer.valueOf(sets.length), sets)));
                return null;
            }
            return this.client.zinterstore(destKey, Integer.valueOf(sets.length), sets).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRange(byte[] key, long start, long end) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.zrange(key, Long.valueOf(start), Long.valueOf(end), (Object) null), SrpConverters.repliesToBytesSet()));
                return null;
            }
            return SrpConverters.toBytesSet(this.client.zrange(key, Long.valueOf(start), Long.valueOf(end), (Object) null).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRangeWithScores(byte[] key, long start, long end) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.zrange(key, Long.valueOf(start), Long.valueOf(end), WITHSCORES), SrpConverters.repliesToTupleSet()));
                return null;
            }
            return SrpConverters.toTupleSet(this.client.zrange(key, Long.valueOf(start), Long.valueOf(end), WITHSCORES).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
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
        byte[] min = SrpConverters.boundaryToBytesForZRange(range.getMin(), SrpConverters.toBytes("-inf"));
        byte[] max = SrpConverters.boundaryToBytesForZRange(range.getMax(), SrpConverters.toBytes("+inf"));
        Object[] params = limit != null ? limitParams(limit.getOffset(), limit.getCount()) : EMPTY_PARAMS_ARRAY;
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.zrangebyscore(key, min, max, (Object) null, params), SrpConverters.repliesToBytesSet()));
                return null;
            }
            return SrpConverters.toBytesSet(this.client.zrangebyscore(key, min, max, (Object) null, params).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
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
        byte[] min = SrpConverters.boundaryToBytesForZRange(range.getMin(), SrpConverters.toBytes("-inf"));
        byte[] max = SrpConverters.boundaryToBytesForZRange(range.getMax(), SrpConverters.toBytes("+inf"));
        Object[] params = limit != null ? limitParams(limit.getOffset(), limit.getCount()) : EMPTY_PARAMS_ARRAY;
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.zrangebyscore(key, min, max, WITHSCORES, params), SrpConverters.repliesToTupleSet()));
                return null;
            }
            return SrpConverters.toTupleSet(this.client.zrangebyscore(key, min, max, WITHSCORES, params).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRevRangeWithScores(byte[] key, long start, long end) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.zrevrange(key, Long.valueOf(start), Long.valueOf(end), WITHSCORES), SrpConverters.repliesToTupleSet()));
                return null;
            }
            return SrpConverters.toTupleSet(this.client.zrevrange(key, Long.valueOf(start), Long.valueOf(end), WITHSCORES).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
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
    public Set<RedisZSetCommands.Tuple> zRevRangeByScoreWithScores(byte[] key, double min, double max, long offset, long count) {
        return zRevRangeByScoreWithScores(key, new RedisZSetCommands.Range().gte(Double.valueOf(min)).lte(Double.valueOf(max)), new RedisZSetCommands.Limit().offset(Long.valueOf(offset).intValue()).count(Long.valueOf(count).intValue()));
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRevRangeByScoreWithScores(byte[] key, double min, double max) {
        return zRevRangeByScoreWithScores(key, new RedisZSetCommands.Range().gte(Double.valueOf(min)).lte(Double.valueOf(max)));
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRevRangeByScore(byte[] key, RedisZSetCommands.Range range) {
        return zRevRangeByScore(key, range, (RedisZSetCommands.Limit) null);
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRevRangeByScore(byte[] key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        Assert.notNull(range, "Range for ZRANGEBYSCOREWITHSCORES must not be null!");
        byte[] min = SrpConverters.boundaryToBytesForZRange(range.getMin(), SrpConverters.toBytes("-inf"));
        byte[] max = SrpConverters.boundaryToBytesForZRange(range.getMax(), SrpConverters.toBytes("+inf"));
        Object[] params = limit != null ? limitParams(limit.getOffset(), limit.getCount()) : EMPTY_PARAMS_ARRAY;
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.zrevrangebyscore(key, max, min, (Object) null, params), SrpConverters.repliesToBytesSet()));
                return null;
            }
            return SrpConverters.toBytesSet(this.client.zrevrangebyscore(key, max, min, (Object) null, params).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRevRangeByScoreWithScores(byte[] key, RedisZSetCommands.Range range) {
        return zRevRangeByScoreWithScores(key, range, (RedisZSetCommands.Limit) null);
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRevRangeByScoreWithScores(byte[] key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        Assert.notNull(range, "Range for ZRANGEBYSCOREWITHSCORES must not be null!");
        byte[] min = SrpConverters.boundaryToBytesForZRange(range.getMin(), SrpConverters.toBytes("-inf"));
        byte[] max = SrpConverters.boundaryToBytesForZRange(range.getMax(), SrpConverters.toBytes("+inf"));
        Object[] params = limit != null ? limitParams(limit.getOffset(), limit.getCount()) : EMPTY_PARAMS_ARRAY;
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.zrevrangebyscore(key, max, min, WITHSCORES, params), SrpConverters.repliesToTupleSet()));
                return null;
            }
            return SrpConverters.toTupleSet(this.client.zrevrangebyscore(key, max, min, WITHSCORES, params).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRank(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.zrank(key, value)));
                return null;
            }
            return (Long) this.client.zrank(key, value).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRem(byte[] key, byte[]... values) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.zrem(key, values)));
                return null;
            }
            return this.client.zrem(key, values).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRemRange(byte[] key, long start, long end) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.zremrangebyrank(key, Long.valueOf(start), Long.valueOf(end))));
                return null;
            }
            return this.client.zremrangebyrank(key, Long.valueOf(start), Long.valueOf(end)).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRemRangeByScore(byte[] key, double min, double max) {
        return zRemRangeByScore(key, new RedisZSetCommands.Range().gte(Double.valueOf(min)).lte(Double.valueOf(max)));
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRemRangeByScore(byte[] key, RedisZSetCommands.Range range) {
        byte[] min = SrpConverters.boundaryToBytesForZRange(range.getMin(), SrpConverters.toBytes("-inf"));
        byte[] max = SrpConverters.boundaryToBytesForZRange(range.getMax(), SrpConverters.toBytes("+inf"));
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.zremrangebyscore(key, min, max)));
                return null;
            }
            return this.client.zremrangebyscore(key, min, max).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRevRange(byte[] key, long start, long end) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.zrevrange(key, Long.valueOf(start), Long.valueOf(end), (Object) null), SrpConverters.repliesToBytesSet()));
                return null;
            }
            return SrpConverters.toBytesSet(this.client.zrevrange(key, Long.valueOf(start), Long.valueOf(end), (Object) null).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRevRank(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.zrevrank(key, value)));
                return null;
            }
            return (Long) this.client.zrevrank(key, value).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Double zScore(byte[] key, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.zscore(key, value), SrpConverters.bytesToDouble()));
                return null;
            }
            return SrpConverters.toDouble(this.client.zscore(key, value).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zUnionStore(byte[] destKey, RedisZSetCommands.Aggregate aggregate, int[] weights, byte[]... sets) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zUnionStore(byte[] destKey, byte[]... sets) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.zunionstore(destKey, Integer.valueOf(sets.length), sets)));
                return null;
            }
            return this.client.zunionstore(destKey, Integer.valueOf(sets.length), sets).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Boolean hSet(byte[] key, byte[] field, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.hset(key, field, value), SrpConverters.longToBoolean()));
                return null;
            }
            return SrpConverters.toBoolean(this.client.hset(key, field, value).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Boolean hSetNX(byte[] key, byte[] field, byte[] value) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.hsetnx(key, field, value), SrpConverters.longToBoolean()));
                return null;
            }
            return SrpConverters.toBoolean(this.client.hsetnx(key, field, value).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Long hDel(byte[] key, byte[]... fields) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.hdel(key, fields)));
                return null;
            }
            return this.client.hdel(key, fields).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Boolean hExists(byte[] key, byte[] field) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.hexists(key, field), SrpConverters.longToBoolean()));
                return null;
            }
            return SrpConverters.toBoolean(this.client.hexists(key, field).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public byte[] hGet(byte[] key, byte[] field) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.hget(key, field)));
                return null;
            }
            return this.client.hget(key, field).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Map<byte[], byte[]> hGetAll(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.hgetall(key), SrpConverters.repliesToBytesMap()));
                return null;
            }
            return SrpConverters.toBytesMap(this.client.hgetall(key).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Long hIncrBy(byte[] key, byte[] field, long delta) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.hincrby(key, field, Long.valueOf(delta))));
                return null;
            }
            return this.client.hincrby(key, field, Long.valueOf(delta)).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Double hIncrBy(byte[] key, byte[] field, double delta) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.hincrbyfloat(key, field, Double.valueOf(delta)), SrpConverters.bytesToDouble()));
                return null;
            }
            return SrpConverters.toDouble(this.client.hincrbyfloat(key, field, Double.valueOf(delta)).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Set<byte[]> hKeys(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.hkeys(key), SrpConverters.repliesToBytesSet()));
                return null;
            }
            return SrpConverters.toBytesSet(this.client.hkeys(key).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Long hLen(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.hlen(key)));
                return null;
            }
            return this.client.hlen(key).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public List<byte[]> hMGet(byte[] key, byte[]... fields) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.hmget(key, fields), SrpConverters.repliesToBytesList()));
                return null;
            }
            return SrpConverters.toBytesList(this.client.hmget(key, fields).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public void hMSet(byte[] key, Map<byte[], byte[]> tuple) {
        try {
            if (isPipelined()) {
                pipeline(new SrpStatusResult(this.pipeline.hmset(key, SrpConverters.toByteArrays(tuple))));
            } else {
                this.client.hmset(key, SrpConverters.toByteArrays(tuple));
            }
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public List<byte[]> hVals(byte[] key) {
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.hvals(key), SrpConverters.repliesToBytesList()));
                return null;
            }
            return SrpConverters.toBytesList(this.client.hvals(key).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public void scriptFlush() {
        try {
            if (isPipelined()) {
                pipeline(new SrpStatusResult(this.pipeline.script_flush()));
            } else {
                this.client.script_flush();
            }
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public void scriptKill() {
        if (isQueueing()) {
            throw new UnsupportedOperationException("Script kill not permitted in a transaction");
        }
        try {
            if (isPipelined()) {
                pipeline(new SrpStatusResult(this.pipeline.script_kill()));
            } else {
                this.client.script_kill();
            }
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public String scriptLoad(byte[] script) {
        try {
            if (isPipelined()) {
                pipeline(new SrpGenericResult(this.pipeline.script_load(script), SrpConverters.bytesToString()));
                return null;
            }
            return SrpConverters.toString((byte[]) this.client.script_load(script).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public List<Boolean> scriptExists(String... scriptSha1) {
        try {
            if (isPipelined()) {
                pipeline(new SrpGenericResult(this.pipeline.script_exists(scriptSha1), SrpConverters.repliesToBooleanList()));
                return null;
            }
            return SrpConverters.toBooleanList(this.client.script_exists_(scriptSha1).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public <T> T eval(byte[] bArr, ReturnType returnType, int i, byte[]... bArr2) {
        try {
            if (isPipelined()) {
                pipeline(new SrpGenericResult(this.pipeline.eval(bArr, Integer.valueOf(i), bArr2), new SrpScriptReturnConverter(returnType)));
                return null;
            }
            return (T) new SrpScriptReturnConverter(returnType).convert2(this.client.eval(bArr, Integer.valueOf(i), bArr2).data());
        } catch (Exception e) {
            throw convertSrpAccessException(e);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public <T> T evalSha(String str, ReturnType returnType, int i, byte[]... bArr) {
        try {
            if (isPipelined()) {
                pipeline(new SrpGenericResult(this.pipeline.evalsha(str, Integer.valueOf(i), bArr), new SrpScriptReturnConverter(returnType)));
                return null;
            }
            return (T) new SrpScriptReturnConverter(returnType).convert2(this.client.evalsha(str, Integer.valueOf(i), bArr).data());
        } catch (Exception e) {
            throw convertSrpAccessException(e);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public <T> T evalSha(byte[] bArr, ReturnType returnType, int i, byte[]... bArr2) {
        return (T) evalSha(SrpConverters.toString(bArr), returnType, i, bArr2);
    }

    @Override // org.springframework.data.redis.connection.RedisPubSubCommands
    public Long publish(byte[] channel, byte[] message) {
        if (isQueueing()) {
            throw new UnsupportedOperationException();
        }
        try {
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.publish(channel, message)));
                return null;
            }
            return this.client.publish(channel, message).data();
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
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
            this.subscription = new SrpSubscription(listener, this.client);
            this.subscription.pSubscribe(patterns);
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisPubSubCommands
    public void subscribe(MessageListener listener, byte[]... channels) {
        checkSubscription();
        if (isPipelined()) {
            throw new UnsupportedOperationException();
        }
        try {
            this.subscription = new SrpSubscription(listener, this.client);
            this.subscription.subscribe(channels);
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoAdd(byte[] key, Point point, byte[] member) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoAdd(byte[] key, RedisGeoCommands.GeoLocation<byte[]> location) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoAdd(byte[] key, Map<byte[], Point> memberCoordinateMap) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoAdd(byte[] key, Iterable<RedisGeoCommands.GeoLocation<byte[]>> locations) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Distance geoDist(byte[] key, byte[] member1, byte[] member2) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Distance geoDist(byte[] key, byte[] member1, byte[] member2, Metric metric) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public List<String> geoHash(byte[] key, byte[]... members) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public List<Point> geoPos(byte[] key, byte[]... members) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> geoRadius(byte[] key, Circle within) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> geoRadius(byte[] key, Circle within, RedisGeoCommands.GeoRadiusCommandArgs args) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, double radius) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, Distance radius) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, Distance radius, RedisGeoCommands.GeoRadiusCommandArgs param) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoRemove(byte[] key, byte[]... members) {
        throw new UnsupportedOperationException();
    }

    public void setConvertPipelineAndTxResults(boolean convertPipelineAndTxResults) {
        this.convertPipelineAndTxResults = convertPipelineAndTxResults;
    }

    private void checkSubscription() {
        if (isSubscribed()) {
            throw new RedisSubscribedConnectionException("Connection already subscribed; use the connection Subscription to cancel or add new channels");
        }
    }

    private void doPipelined(ListenableFuture<Reply> listenableFuture) {
        pipeline(new SrpStatusResult(listenableFuture));
    }

    private void pipeline(FutureResult<?> future) {
        if (isQueueing()) {
            this.txTracker.addCommand(future);
        } else {
            this.callback.addCommand(future);
        }
    }

    private void initPipeline() {
        if (this.pipeline == null) {
            this.callback = new PipelineTracker(this.convertPipelineAndTxResults);
            this.pipeline = this.client.pipeline();
        }
    }

    private void initTxTracker() {
        if (this.txTracker == null) {
            this.txTracker = new PipelineTracker(this.convertPipelineAndTxResults);
        }
        initPipeline();
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public List<Object> closePipeline() {
        this.pipelineRequested = false;
        List<Object> results = Collections.emptyList();
        if (this.pipeline != null) {
            this.pipeline = null;
            results = getPipelinedResults(this.callback, true);
            this.callback.close();
            this.callback = null;
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public Long time() {
        if (isPipelined()) {
            pipeline(new SrpGenericResult(this.pipeline.time(), SrpConverters.repliesToTimeAsLong()));
            return null;
        }
        MultiBulkReply reply = this.client.time();
        Assert.notNull(reply, "Received invalid result from server. MultiBulkReply must not be empty.");
        return SrpConverters.toTimeAsLong(reply.data());
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void killClient(String host, int port) {
        Assert.hasText(host, "Host for 'CLIENT KILL' must not be 'null' or 'empty'.");
        String client = String.format("%s:%s", host, Integer.valueOf(port));
        try {
            if (isPipelined()) {
                pipeline(new SrpStatusResult(this.pipeline.client_kill(client)));
            } else {
                this.client.client_kill(client);
            }
        } catch (Exception e) {
            throw convertSrpAccessException(e);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void setClientName(byte[] name) {
        try {
            if (isPipelined()) {
                pipeline(new SrpStatusResult(this.pipeline.client_setname(name)));
            } else {
                this.client.client_setname(name);
            }
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void slaveOf(String host, int port) {
        Assert.hasText(host, "Host must not be null for 'SLAVEOF' command.");
        try {
            if (isPipelined()) {
                pipeline(new SrpStatusResult(this.pipeline.slaveof(host, Integer.valueOf(port))));
            } else {
                this.client.slaveof(host, Integer.valueOf(port));
            }
        } catch (Exception e) {
            throw convertSrpAccessException(e);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public String getClientName() {
        try {
            if (isPipelined()) {
                pipeline(new SrpGenericResult(this.pipeline.client_getname(), SrpConverters.replyToString()));
                return null;
            }
            return SrpConverters.toString(this.client.client_getname());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public List<RedisClientInfo> getClientList() {
        if (isQueueing()) {
            throw new UnsupportedOperationException();
        }
        if (isPipelined()) {
            pipeline(new SrpGenericResult(this.pipeline.client_list(), SrpConverters.replyToListOfRedisClientInfo()));
            return null;
        }
        return SrpConverters.toListOfRedisClientInformation(this.client.client_list());
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void slaveOfNoOne() {
        try {
            if (isPipelined()) {
                pipeline(new SrpStatusResult(this.pipeline.slaveof("NO", "ONE")));
            } else {
                this.client.slaveof("NO", "ONE");
            }
        } catch (Exception e) {
            throw convertSrpAccessException(e);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Cursor<byte[]> scan(ScanOptions options) {
        throw new UnsupportedOperationException("'SCAN' command is not supported for Srp.");
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Cursor<RedisZSetCommands.Tuple> zScan(byte[] key, ScanOptions options) {
        throw new UnsupportedOperationException("'ZSCAN' command is not supported for Srp.");
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Cursor<byte[]> sScan(byte[] key, ScanOptions options) {
        throw new UnsupportedOperationException("'SSCAN' command is not supported for Srp.");
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Cursor<Map.Entry<byte[], byte[]>> hScan(byte[] key, ScanOptions options) {
        throw new UnsupportedOperationException("'HSCAN' command is not supported for Srp.");
    }

    private List<Object> closeTransaction() {
        List<Object> results = Collections.emptyList();
        if (this.txTracker != null) {
            results = getPipelinedResults(this.txTracker, false);
            this.txTracker.close();
            this.txTracker = null;
        }
        return results;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<Object> getPipelinedResults(PipelineTracker tracker, boolean throwPipelineException) {
        List<Object> execute = new ArrayList<>(tracker.complete());
        if (execute != null && !execute.isEmpty()) {
            Exception cause = null;
            for (int i = 0; i < execute.size(); i++) {
                Object object = execute.get(i);
                if (object instanceof Exception) {
                    Exception dataAccessException = convertSrpAccessException((Exception) object);
                    if (cause == null) {
                        cause = dataAccessException;
                    }
                    execute.set(i, dataAccessException);
                }
            }
            if (cause != null) {
                if (throwPipelineException) {
                    throw new RedisPipelineException(cause, execute);
                }
                throw convertSrpAccessException(cause);
            }
            return execute;
        }
        return Collections.emptyList();
    }

    private Object[] popArgs(int timeout, byte[]... keys) {
        int length = keys != null ? keys.length + 1 : 1;
        Object[] args = new Object[length];
        if (keys != null) {
            for (int i = 0; i < keys.length; i++) {
                args[i] = keys[i];
            }
        }
        args[length - 1] = String.valueOf(timeout).getBytes();
        return args;
    }

    private Object[] limitParams(long offset, long count) {
        return new Object[]{"LIMIT".getBytes(Charsets.UTF_8), String.valueOf(offset).getBytes(Charsets.UTF_8), String.valueOf(count).getBytes(Charsets.UTF_8)};
    }

    private byte[] limit(long offset, long count) {
        return ("LIMIT " + offset + SymbolConstants.SPACE_SYMBOL + count).getBytes(Charsets.UTF_8);
    }

    private Object[] sortParams(SortParameters params) {
        return sortParams(params, null);
    }

    private Object[] sortParams(SortParameters params, byte[] sortKey) {
        List<byte[]> arrays = new ArrayList<>();
        if (params != null) {
            if (params.getByPattern() != null) {
                arrays.add(BY);
                arrays.add(params.getByPattern());
            }
            if (params.getLimit() != null) {
                arrays.add(limit(params.getLimit().getStart(), params.getLimit().getCount()));
            }
            if (params.getGetPattern() != null) {
                byte[][] pattern = params.getGetPattern();
                for (byte[] bs : pattern) {
                    arrays.add(GET);
                    arrays.add(bs);
                }
            }
            if (params.getOrder() != null) {
                arrays.add(params.getOrder().name().getBytes(Charsets.UTF_8));
            }
            Boolean isAlpha = params.isAlphabetic();
            if (isAlpha != null && isAlpha.booleanValue()) {
                arrays.add(ALPHA);
            }
        }
        if (sortKey != null) {
            arrays.add(STORE);
            arrays.add(sortKey);
        }
        return arrays.toArray();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByScore(byte[] key, String min, String max) {
        try {
            String keyStr = new String(key, "UTF-8");
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.zrangebyscore(keyStr, min, max, (Object) null, EMPTY_PARAMS_ARRAY), SrpConverters.repliesToBytesSet()));
                return null;
            }
            return SrpConverters.toBytesSet(this.client.zrangebyscore(keyStr, min, max, (Object) null, EMPTY_PARAMS_ARRAY).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByScore(byte[] key, String min, String max, long offset, long count) {
        try {
            String keyStr = new String(key, "UTF-8");
            Object[] limit = limitParams(offset, count);
            if (isPipelined()) {
                pipeline(new SrpResult(this.pipeline.zrangebyscore(keyStr, min, max, (Object) null, limit), SrpConverters.repliesToBytesSet()));
                return null;
            }
            return SrpConverters.toBytesSet(this.client.zrangebyscore(keyStr, min, max, (Object) null, limit).data());
        } catch (Exception ex) {
            throw convertSrpAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.HyperLogLogCommands
    public Long pfAdd(byte[] key, byte[]... values) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.HyperLogLogCommands
    public Long pfCount(byte[]... keys) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.HyperLogLogCommands
    public void pfMerge(byte[] destinationKey, byte[]... sourceKeys) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByLex(byte[] key) {
        throw new UnsupportedOperationException("ZRANGEBYLEX is no supported for srp.");
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByLex(byte[] key, RedisZSetCommands.Range range) {
        throw new UnsupportedOperationException("ZRANGEBYLEX is no supported for srp.");
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByLex(byte[] key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        throw new UnsupportedOperationException("ZRANGEBYLEX is no supported for srp.");
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void migrate(byte[] key, RedisNode target, int dbIndex, RedisServerCommands.MigrateOption option) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void migrate(byte[] key, RedisNode target, int dbIndex, RedisServerCommands.MigrateOption option, long timeout) {
        throw new UnsupportedOperationException();
    }
}
