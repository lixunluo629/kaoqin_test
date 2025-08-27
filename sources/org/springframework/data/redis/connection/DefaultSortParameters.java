package org.springframework.data.redis.connection;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.redis.connection.SortParameters;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/DefaultSortParameters.class */
public class DefaultSortParameters implements SortParameters {
    private byte[] byPattern;
    private SortParameters.Range limit;
    private final List<byte[]> getPattern;
    private SortParameters.Order order;
    private Boolean alphabetic;

    public DefaultSortParameters() {
        this(null, null, (byte[][]) null, null, null);
    }

    public DefaultSortParameters(SortParameters.Range limit, SortParameters.Order order, Boolean alphabetic) {
        this(null, limit, (byte[][]) null, order, alphabetic);
    }

    public DefaultSortParameters(byte[] byPattern, SortParameters.Range limit, byte[][] getPattern, SortParameters.Order order, Boolean alphabetic) {
        this.getPattern = new ArrayList(4);
        this.byPattern = byPattern;
        this.limit = limit;
        this.order = order;
        this.alphabetic = alphabetic;
        setGetPattern(getPattern);
    }

    @Override // org.springframework.data.redis.connection.SortParameters
    public byte[] getByPattern() {
        return this.byPattern;
    }

    public void setByPattern(byte[] byPattern) {
        this.byPattern = byPattern;
    }

    @Override // org.springframework.data.redis.connection.SortParameters
    public SortParameters.Range getLimit() {
        return this.limit;
    }

    public void setLimit(SortParameters.Range limit) {
        this.limit = limit;
    }

    @Override // org.springframework.data.redis.connection.SortParameters
    public byte[][] getGetPattern() {
        return (byte[][]) this.getPattern.toArray((Object[]) new byte[this.getPattern.size()]);
    }

    public void addGetPattern(byte[] gPattern) {
        this.getPattern.add(gPattern);
    }

    public void setGetPattern(byte[][] gPattern) {
        this.getPattern.clear();
        if (gPattern == null) {
            return;
        }
        for (byte[] bs : gPattern) {
            this.getPattern.add(bs);
        }
    }

    @Override // org.springframework.data.redis.connection.SortParameters
    public SortParameters.Order getOrder() {
        return this.order;
    }

    public void setOrder(SortParameters.Order order) {
        this.order = order;
    }

    @Override // org.springframework.data.redis.connection.SortParameters
    public Boolean isAlphabetic() {
        return this.alphabetic;
    }

    public void setAlphabetic(Boolean alphabetic) {
        this.alphabetic = alphabetic;
    }

    public DefaultSortParameters order(SortParameters.Order order) {
        setOrder(order);
        return this;
    }

    public DefaultSortParameters alpha() {
        setAlphabetic(true);
        return this;
    }

    public DefaultSortParameters asc() {
        setOrder(SortParameters.Order.ASC);
        return this;
    }

    public DefaultSortParameters desc() {
        setOrder(SortParameters.Order.DESC);
        return this;
    }

    public DefaultSortParameters numeric() {
        setAlphabetic(false);
        return this;
    }

    public DefaultSortParameters get(byte[] pattern) {
        addGetPattern(pattern);
        return this;
    }

    public DefaultSortParameters by(byte[] pattern) {
        setByPattern(pattern);
        return this;
    }

    public DefaultSortParameters limit(long start, long count) {
        setLimit(new SortParameters.Range(start, count));
        return this;
    }
}
