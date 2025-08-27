package org.springframework.data.redis.core.convert;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.ibatis.ognl.OgnlContext;
import org.springframework.data.keyvalue.core.mapping.KeyValuePersistentEntity;
import org.springframework.data.redis.core.index.ConfigurableIndexDefinitionProvider;
import org.springframework.data.redis.core.index.IndexDefinition;
import org.springframework.data.redis.core.index.SpelIndexDefinition;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.data.util.TypeInformation;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/SpelIndexResolver.class */
public class SpelIndexResolver implements IndexResolver {
    private final ConfigurableIndexDefinitionProvider settings;
    private final SpelExpressionParser parser;
    private final RedisMappingContext mappingContext;
    private BeanResolver beanResolver;
    private Map<SpelIndexDefinition, Expression> expressionCache;

    public SpelIndexResolver(RedisMappingContext mappingContext) {
        this(mappingContext, new SpelExpressionParser());
    }

    public SpelIndexResolver(RedisMappingContext mappingContext, SpelExpressionParser parser) {
        Assert.notNull(mappingContext, "RedisMappingContext must not be null!");
        Assert.notNull(parser, "SpelExpressionParser must not be null!");
        this.mappingContext = mappingContext;
        this.settings = mappingContext.getMappingConfiguration().getIndexConfiguration();
        this.expressionCache = new HashMap();
        this.parser = parser;
    }

    @Override // org.springframework.data.redis.core.convert.IndexResolver
    public Set<IndexedData> resolveIndexesFor(TypeInformation<?> typeInformation, Object value) throws EvaluationException {
        if (value == null) {
            return Collections.emptySet();
        }
        KeyValuePersistentEntity<?> entity = this.mappingContext.getPersistentEntity(typeInformation);
        if (entity == null) {
            return Collections.emptySet();
        }
        String keyspace = entity.getKeySpace();
        Set<IndexedData> indexes = new HashSet<>();
        for (IndexDefinition setting : this.settings.getIndexDefinitionsFor(keyspace)) {
            if (setting instanceof SpelIndexDefinition) {
                Expression expression = getAndCacheIfAbsent((SpelIndexDefinition) setting);
                StandardEvaluationContext context = new StandardEvaluationContext();
                context.setRootObject(value);
                context.setVariable(OgnlContext.THIS_CONTEXT_KEY, value);
                if (this.beanResolver != null) {
                    context.setBeanResolver(this.beanResolver);
                }
                Object index = expression.getValue((EvaluationContext) context);
                if (index != null) {
                    indexes.add(new SimpleIndexedPropertyValue(keyspace, setting.getIndexName(), index));
                }
            }
        }
        return indexes;
    }

    @Override // org.springframework.data.redis.core.convert.IndexResolver
    public Set<IndexedData> resolveIndexesFor(String keyspace, String path, TypeInformation<?> typeInformation, Object value) {
        return Collections.emptySet();
    }

    private Expression getAndCacheIfAbsent(SpelIndexDefinition indexDefinition) {
        if (this.expressionCache.containsKey(indexDefinition)) {
            return this.expressionCache.get(indexDefinition);
        }
        Expression expression = this.parser.parseExpression(indexDefinition.getExpression());
        this.expressionCache.put(indexDefinition, expression);
        return expression;
    }

    public void setBeanResolver(BeanResolver beanResolver) {
        this.beanResolver = beanResolver;
    }
}
