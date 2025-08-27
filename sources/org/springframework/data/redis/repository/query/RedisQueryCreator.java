package org.springframework.data.redis.repository.query;

import java.util.Iterator;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.keyvalue.core.query.KeyValueQuery;
import org.springframework.data.redis.repository.query.RedisOperationChain;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.PartTree;
import org.springframework.util.CollectionUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/repository/query/RedisQueryCreator.class */
public class RedisQueryCreator extends AbstractQueryCreator<KeyValueQuery<RedisOperationChain>, RedisOperationChain> {
    @Override // org.springframework.data.repository.query.parser.AbstractQueryCreator
    protected /* bridge */ /* synthetic */ RedisOperationChain and(Part part, RedisOperationChain redisOperationChain, Iterator it) {
        return and2(part, redisOperationChain, (Iterator<Object>) it);
    }

    @Override // org.springframework.data.repository.query.parser.AbstractQueryCreator
    protected /* bridge */ /* synthetic */ RedisOperationChain create(Part part, Iterator it) {
        return create(part, (Iterator<Object>) it);
    }

    public RedisQueryCreator(PartTree tree, ParameterAccessor parameters) {
        super(tree, parameters);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.data.repository.query.parser.AbstractQueryCreator
    protected RedisOperationChain create(Part part, Iterator<Object> iterator) {
        return from(part, iterator, new RedisOperationChain());
    }

    private RedisOperationChain from(Part part, Iterator<Object> iterator, RedisOperationChain sink) {
        switch (part.getType()) {
            case SIMPLE_PROPERTY:
                sink.sismember(part.getProperty().toDotPath(), iterator.next());
                break;
            case WITHIN:
            case NEAR:
                sink.near(getNearPath(part, iterator));
                break;
            default:
                throw new IllegalArgumentException(part.getType() + "is not supported for redis query derivation");
        }
        return sink;
    }

    /* renamed from: and, reason: avoid collision after fix types in other method */
    protected RedisOperationChain and2(Part part, RedisOperationChain base, Iterator<Object> iterator) {
        return from(part, iterator, base);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.data.repository.query.parser.AbstractQueryCreator
    public RedisOperationChain or(RedisOperationChain base, RedisOperationChain criteria) {
        base.orSismember(criteria.getSismember());
        return base;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.data.repository.query.parser.AbstractQueryCreator
    public KeyValueQuery<RedisOperationChain> complete(RedisOperationChain criteria, Sort sort) {
        KeyValueQuery<RedisOperationChain> query = new KeyValueQuery<>(criteria);
        if (query.getCriteria() != null && !CollectionUtils.isEmpty(query.getCriteria().getSismember()) && !CollectionUtils.isEmpty(query.getCriteria().getOrSismember()) && query.getCriteria().getSismember().size() == 1 && query.getCriteria().getOrSismember().size() == 1) {
            query.getCriteria().getOrSismember().add(query.getCriteria().getSismember().iterator().next());
            query.getCriteria().getSismember().clear();
        }
        if (sort != null) {
            query.setSort(sort);
        }
        return query;
    }

    private RedisOperationChain.NearPath getNearPath(Part part, Iterator<Object> iterator) {
        Point point;
        Distance distance;
        Object o = iterator.next();
        if (o instanceof Circle) {
            point = ((Circle) o).getCenter();
            distance = ((Circle) o).getRadius();
        } else if (o instanceof Point) {
            point = (Point) o;
            if (!iterator.hasNext()) {
                throw new InvalidDataAccessApiUsageException("Expected to find distance value for geo query. Are you missing a parameter?");
            }
            Object distObject = iterator.next();
            if (distObject instanceof Distance) {
                distance = (Distance) distObject;
            } else if (distObject instanceof Number) {
                distance = new Distance(((Number) distObject).doubleValue(), Metrics.KILOMETERS);
            } else {
                throw new InvalidDataAccessApiUsageException(String.format("Expected to find Distance or Numeric value for geo query but was %s.", distObject.getClass()));
            }
        } else {
            throw new InvalidDataAccessApiUsageException(String.format("Expected to find a Circle or Point/Distance for geo query but was %s.", o.getClass()));
        }
        return new RedisOperationChain.NearPath(part.getProperty().toDotPath(), point, distance);
    }
}
