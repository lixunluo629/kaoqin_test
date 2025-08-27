package org.springframework.data.redis.connection.lettuce;

import com.lambdaworks.redis.RedisCommandExecutionException;
import com.lambdaworks.redis.RedisCommandInterruptedException;
import com.lambdaworks.redis.RedisCommandTimeoutException;
import com.lambdaworks.redis.RedisConnectionException;
import com.lambdaworks.redis.RedisException;
import io.netty.channel.ChannelException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/lettuce/LettuceExceptionConverter.class */
public class LettuceExceptionConverter implements Converter<Exception, DataAccessException> {
    @Override // org.springframework.core.convert.converter.Converter
    public DataAccessException convert(Exception ex) {
        if ((ex instanceof ExecutionException) || (ex instanceof RedisCommandExecutionException)) {
            if (ex.getCause() != ex && (ex.getCause() instanceof Exception)) {
                return convert((Exception) ex.getCause());
            }
            return new RedisSystemException("Error in execution", ex);
        }
        if (ex instanceof DataAccessException) {
            return (DataAccessException) ex;
        }
        if (ex instanceof RedisCommandInterruptedException) {
            return new RedisSystemException("Redis command interrupted", ex);
        }
        if ((ex instanceof ChannelException) || (ex instanceof RedisConnectionException)) {
            return new RedisConnectionFailureException("Redis connection failed", ex);
        }
        if ((ex instanceof TimeoutException) || (ex instanceof RedisCommandTimeoutException)) {
            return new QueryTimeoutException("Redis command timed out", ex);
        }
        if (ex instanceof RedisException) {
            return new RedisSystemException("Redis exception", ex);
        }
        return null;
    }
}
