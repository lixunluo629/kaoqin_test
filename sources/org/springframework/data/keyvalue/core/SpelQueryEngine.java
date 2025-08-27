package org.springframework.data.keyvalue.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.data.keyvalue.core.KeyValueAdapter;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/SpelQueryEngine.class */
class SpelQueryEngine<T extends KeyValueAdapter> extends QueryEngine<KeyValueAdapter, SpelCriteria, Comparator<?>> {
    private static final SpelExpressionParser PARSER = new SpelExpressionParser();

    public SpelQueryEngine() {
        super(new SpelCriteriaAccessor(PARSER), new SpelSortAccessor(PARSER));
    }

    @Override // org.springframework.data.keyvalue.core.QueryEngine
    public Collection<?> execute(SpelCriteria criteria, Comparator<?> sort, int offset, int rows, Serializable keyspace) {
        return sortAndFilterMatchingRange(getAdapter().getAllOf(keyspace), criteria, sort, offset, rows);
    }

    @Override // org.springframework.data.keyvalue.core.QueryEngine
    public long count(SpelCriteria criteria, Serializable keyspace) {
        return filterMatchingRange(getAdapter().getAllOf(keyspace), criteria, -1, -1).size();
    }

    private List<?> sortAndFilterMatchingRange(Iterable<?> source, SpelCriteria criteria, Comparator sort, int offset, int rows) {
        List<?> tmp = IterableConverter.toList(source);
        if (sort != null) {
            Collections.sort(tmp, sort);
        }
        return filterMatchingRange(tmp, criteria, offset, rows);
    }

    private static <S> List<S> filterMatchingRange(Iterable<S> source, SpelCriteria criteria, int offset, int rows) {
        List<S> result = new ArrayList<>();
        boolean compareOffsetAndRows = 0 < offset || 0 <= rows;
        int remainingRows = rows;
        int curPos = 0;
        for (S candidate : source) {
            boolean matches = criteria == null;
            if (!matches) {
                try {
                    matches = ((Boolean) criteria.getExpression().getValue(criteria.getContext(), candidate, Boolean.class)).booleanValue();
                } catch (SpelEvaluationException e) {
                    criteria.getContext().setVariable("it", candidate);
                    matches = criteria.getExpression().getValue(criteria.getContext()) == null ? false : ((Boolean) criteria.getExpression().getValue(criteria.getContext(), Boolean.class)).booleanValue();
                }
            }
            if (matches) {
                if (compareOffsetAndRows) {
                    if (curPos >= offset && rows > 0) {
                        result.add(candidate);
                        remainingRows--;
                        if (remainingRows <= 0) {
                            break;
                        }
                    }
                    curPos++;
                } else {
                    result.add(candidate);
                }
            }
        }
        return result;
    }
}
