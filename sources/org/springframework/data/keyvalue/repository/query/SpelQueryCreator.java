package org.springframework.data.keyvalue.repository.query;

import java.util.Iterator;
import org.springframework.beans.PropertyAccessor;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Sort;
import org.springframework.data.keyvalue.core.query.KeyValueQuery;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.PartTree;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/repository/query/SpelQueryCreator.class */
public class SpelQueryCreator extends AbstractQueryCreator<KeyValueQuery<SpelExpression>, String> {
    private static final SpelExpressionParser PARSER = new SpelExpressionParser();
    private final SpelExpression expression;

    @Override // org.springframework.data.repository.query.parser.AbstractQueryCreator
    protected /* bridge */ /* synthetic */ String and(Part part, String str, Iterator it) {
        return and2(part, str, (Iterator<Object>) it);
    }

    @Override // org.springframework.data.repository.query.parser.AbstractQueryCreator
    protected /* bridge */ /* synthetic */ String create(Part part, Iterator it) {
        return create(part, (Iterator<Object>) it);
    }

    public SpelQueryCreator(PartTree tree, ParameterAccessor parameters) {
        super(tree, parameters);
        this.expression = toPredicateExpression(tree);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.data.repository.query.parser.AbstractQueryCreator
    protected String create(Part part, Iterator<Object> iterator) {
        return "";
    }

    /* renamed from: and, reason: avoid collision after fix types in other method */
    protected String and2(Part part, String base, Iterator<Object> iterator) {
        return "";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.data.repository.query.parser.AbstractQueryCreator
    public String or(String base, String criteria) {
        return "";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.data.repository.query.parser.AbstractQueryCreator
    public KeyValueQuery<SpelExpression> complete(String criteria, Sort sort) {
        KeyValueQuery<SpelExpression> query = new KeyValueQuery<>(this.expression);
        if (sort != null) {
            query.orderBy(sort);
        }
        return query;
    }

    protected SpelExpression toPredicateExpression(PartTree tree) {
        int parameterIndex = 0;
        StringBuilder sb = new StringBuilder();
        Iterator<PartTree.OrPart> orPartIter = tree.iterator();
        while (orPartIter.hasNext()) {
            int partCnt = 0;
            StringBuilder partBuilder = new StringBuilder();
            PartTree.OrPart orPart = orPartIter.next();
            Iterator<Part> partIter = orPart.iterator();
            while (partIter.hasNext()) {
                Part part = partIter.next();
                if (!requiresInverseLookup(part)) {
                    partBuilder.append("#it?.");
                    partBuilder.append(part.getProperty().toDotPath().replace(".", "?."));
                }
                if (!part.shouldIgnoreCase().equals(Part.IgnoreCaseType.NEVER)) {
                    throw new InvalidDataAccessApiUsageException("Ignore case not supported!");
                }
                switch (part.getType()) {
                    case TRUE:
                        partBuilder.append("?.equals(true)");
                        break;
                    case FALSE:
                        partBuilder.append("?.equals(false)");
                        break;
                    case SIMPLE_PROPERTY:
                        int i = parameterIndex;
                        parameterIndex++;
                        partBuilder.append("?.equals(").append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(i).append("])");
                        break;
                    case IS_NULL:
                        partBuilder.append(" == null");
                        break;
                    case IS_NOT_NULL:
                        partBuilder.append(" != null");
                        break;
                    case LIKE:
                        int i2 = parameterIndex;
                        parameterIndex++;
                        partBuilder.append("?.contains(").append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(i2).append("])");
                        break;
                    case STARTING_WITH:
                        int i3 = parameterIndex;
                        parameterIndex++;
                        partBuilder.append("?.startsWith(").append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(i3).append("])");
                        break;
                    case AFTER:
                    case GREATER_THAN:
                        int i4 = parameterIndex;
                        parameterIndex++;
                        partBuilder.append(">").append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(i4).append("]");
                        break;
                    case GREATER_THAN_EQUAL:
                        int i5 = parameterIndex;
                        parameterIndex++;
                        partBuilder.append(">=").append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(i5).append("]");
                        break;
                    case BEFORE:
                    case LESS_THAN:
                        int i6 = parameterIndex;
                        parameterIndex++;
                        partBuilder.append("<").append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(i6).append("]");
                        break;
                    case LESS_THAN_EQUAL:
                        int i7 = parameterIndex;
                        parameterIndex++;
                        partBuilder.append("<=").append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(i7).append("]");
                        break;
                    case ENDING_WITH:
                        int i8 = parameterIndex;
                        parameterIndex++;
                        partBuilder.append("?.endsWith(").append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(i8).append("])");
                        break;
                    case BETWEEN:
                        int index = partBuilder.lastIndexOf("#it?.");
                        partBuilder.insert(index, "(");
                        int i9 = parameterIndex;
                        int parameterIndex2 = parameterIndex + 1;
                        partBuilder.append(">").append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(i9).append("]");
                        partBuilder.append("&&");
                        partBuilder.append("#it?.");
                        partBuilder.append(part.getProperty().toDotPath().replace(".", "?."));
                        parameterIndex = parameterIndex2 + 1;
                        partBuilder.append("<").append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(parameterIndex2).append("]");
                        partBuilder.append(")");
                        break;
                    case REGEX:
                        int i10 = parameterIndex;
                        parameterIndex++;
                        partBuilder.append(" matches ").append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(i10).append("]");
                        break;
                    case IN:
                        int i11 = parameterIndex;
                        parameterIndex++;
                        partBuilder.append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(i11).append("].contains(");
                        partBuilder.append("#it?.");
                        partBuilder.append(part.getProperty().toDotPath().replace(".", "?."));
                        partBuilder.append(")");
                        break;
                    case CONTAINING:
                    case NOT_CONTAINING:
                    case NEGATING_SIMPLE_PROPERTY:
                    case EXISTS:
                    default:
                        throw new InvalidDataAccessApiUsageException(String.format("Found invalid part '%s' in query", part.getType()));
                }
                if (partIter.hasNext()) {
                    partBuilder.append("&&");
                }
                partCnt++;
            }
            if (partCnt > 1) {
                sb.append("(").append((CharSequence) partBuilder).append(")");
            } else {
                sb.append((CharSequence) partBuilder);
            }
            if (orPartIter.hasNext()) {
                sb.append("||");
            }
        }
        return PARSER.parseRaw(sb.toString());
    }

    private static boolean requiresInverseLookup(Part part) {
        return part.getType() == Part.Type.IN;
    }
}
