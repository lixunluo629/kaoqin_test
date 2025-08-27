package org.springframework.data.keyvalue.core;

import org.springframework.data.keyvalue.core.query.KeyValueQuery;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.Assert;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/SpelCriteriaAccessor.class */
class SpelCriteriaAccessor implements CriteriaAccessor<SpelCriteria> {
    private final SpelExpressionParser parser;

    @Override // org.springframework.data.keyvalue.core.CriteriaAccessor
    public /* bridge */ /* synthetic */ SpelCriteria resolve(KeyValueQuery keyValueQuery) {
        return resolve((KeyValueQuery<?>) keyValueQuery);
    }

    public SpelCriteriaAccessor(SpelExpressionParser parser) {
        Assert.notNull(parser, "SpelExpressionParser must not be null!");
        this.parser = parser;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.data.keyvalue.core.CriteriaAccessor
    public SpelCriteria resolve(KeyValueQuery<?> query) {
        if (query.getCriteria() == null) {
            return null;
        }
        if (query.getCriteria() instanceof SpelExpression) {
            return new SpelCriteria((SpelExpression) query.getCriteria());
        }
        if (query.getCriteria() instanceof String) {
            return new SpelCriteria(this.parser.parseRaw((String) query.getCriteria()));
        }
        if (query.getCriteria() instanceof SpelCriteria) {
            return (SpelCriteria) query.getCriteria();
        }
        throw new IllegalArgumentException("Cannot create SpelCriteria for " + query.getCriteria());
    }
}
