package org.apache.ibatis.scripting.xmltags;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.builder.BuilderException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/xmltags/ExpressionEvaluator.class */
public class ExpressionEvaluator {
    public boolean evaluateBoolean(String expression, Object parameterObject) {
        Object value = OgnlCache.getValue(expression, parameterObject);
        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        }
        return value instanceof Number ? new BigDecimal(String.valueOf(value)).compareTo(BigDecimal.ZERO) != 0 : value != null;
    }

    public Iterable<?> evaluateIterable(String expression, Object parameterObject) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        Object value = OgnlCache.getValue(expression, parameterObject);
        if (value == null) {
            throw new BuilderException("The expression '" + expression + "' evaluated to a null value.");
        }
        if (value instanceof Iterable) {
            return (Iterable) value;
        }
        if (value.getClass().isArray()) {
            int size = Array.getLength(value);
            List<Object> answer = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                Object o = Array.get(value, i);
                answer.add(o);
            }
            return answer;
        }
        if (value instanceof Map) {
            return ((Map) value).entrySet();
        }
        throw new BuilderException("Error evaluating expression '" + expression + "'.  Return value (" + value + ") was not iterable.");
    }
}
