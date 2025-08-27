package org.springframework.data.redis.connection.srp;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.ExceptionTranslationStrategy;
import org.springframework.data.redis.PassThroughExceptionTranslationStrategy;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConnection;
import redis.clients.jedis.Protocol;

@Deprecated
/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/srp/SrpConnectionFactory.class */
public class SrpConnectionFactory implements InitializingBean, DisposableBean, RedisConnectionFactory {
    private static final ExceptionTranslationStrategy EXCEPTION_TRANSLATION = new PassThroughExceptionTranslationStrategy(SrpConverters.exceptionConverter());
    private String hostName;
    private int port;
    private BlockingQueue<SrpConnection> trackedConnections;
    private boolean convertPipelineAndTxResults;
    private String password;

    public SrpConnectionFactory() {
        this.hostName = "localhost";
        this.port = Protocol.DEFAULT_PORT;
        this.trackedConnections = new ArrayBlockingQueue(50);
        this.convertPipelineAndTxResults = true;
    }

    public SrpConnectionFactory(String host, int port) {
        this.hostName = "localhost";
        this.port = Protocol.DEFAULT_PORT;
        this.trackedConnections = new ArrayBlockingQueue(50);
        this.convertPipelineAndTxResults = true;
        this.hostName = host;
        this.port = port;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() {
        SrpConnection con;
        do {
            con = this.trackedConnections.poll();
            if (con != null && !con.isClosed()) {
                try {
                    con.close();
                } catch (Exception e) {
                }
            }
        } while (con != null);
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionFactory
    public RedisConnection getConnection() {
        SrpConnection connection = this.password != null ? new SrpConnection(this.hostName, this.port, this.password, this.trackedConnections) : new SrpConnection(this.hostName, this.port, this.trackedConnections);
        connection.setConvertPipelineAndTxResults(this.convertPipelineAndTxResults);
        return connection;
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionFactory
    public RedisClusterConnection getClusterConnection() {
        throw new UnsupportedOperationException("Srp does not support Redis Cluster.");
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

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionFactory
    public boolean getConvertPipelineAndTxResults() {
        return this.convertPipelineAndTxResults;
    }

    public void setConvertPipelineAndTxResults(boolean convertPipelineAndTxResults) {
        this.convertPipelineAndTxResults = convertPipelineAndTxResults;
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionFactory
    public RedisSentinelConnection getSentinelConnection() {
        throw new UnsupportedOperationException();
    }
}
