package org.springframework.data.redis.connection.convert;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.FutureResult;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/convert/TransactionResultConverter.class */
public class TransactionResultConverter<T> implements Converter<List<Object>, List<Object>> {
    private Queue<FutureResult<T>> txResults;
    private Converter<Exception, DataAccessException> exceptionConverter;

    public TransactionResultConverter(Queue<FutureResult<T>> txResults, Converter<Exception, DataAccessException> exceptionConverter) {
        this.txResults = new LinkedList();
        this.txResults = txResults;
        this.exceptionConverter = exceptionConverter;
    }

    @Override // org.springframework.core.convert.converter.Converter
    public List<Object> convert(List<Object> execResults) {
        if (execResults == null) {
            return null;
        }
        if (execResults.size() != this.txResults.size()) {
            throw new IllegalArgumentException("Incorrect number of transaction results. Expected: " + this.txResults.size() + " Actual: " + execResults.size());
        }
        List<Object> convertedResults = new ArrayList<>();
        for (Object result : execResults) {
            FutureResult<T> futureResult = this.txResults.remove();
            if (result instanceof Exception) {
                throw this.exceptionConverter.convert((Exception) result);
            }
            if (!futureResult.isStatus()) {
                convertedResults.add(futureResult.convert(result));
            }
        }
        return convertedResults;
    }
}
