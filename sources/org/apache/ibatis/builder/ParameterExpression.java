package org.apache.ibatis.builder;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.HashMap;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/builder/ParameterExpression.class */
public class ParameterExpression extends HashMap<String, String> {
    private static final long serialVersionUID = -2417552199605158680L;

    public ParameterExpression(String expression) {
        parse(expression);
    }

    private void parse(String expression) {
        int p = skipWS(expression, 0);
        if (expression.charAt(p) == '(') {
            expression(expression, p + 1);
        } else {
            property(expression, p);
        }
    }

    private void expression(String expression, int left) {
        int match = 1;
        int right = left + 1;
        while (match > 0) {
            if (expression.charAt(right) == ')') {
                match--;
            } else if (expression.charAt(right) == '(') {
                match++;
            }
            right++;
        }
        put("expression", expression.substring(left, right - 1));
        jdbcTypeOpt(expression, right);
    }

    private void property(String expression, int left) {
        if (left < expression.length()) {
            int right = skipUntil(expression, left, ",:");
            put(BeanDefinitionParserDelegate.PROPERTY_ELEMENT, trimmedStr(expression, left, right));
            jdbcTypeOpt(expression, right);
        }
    }

    private int skipWS(String expression, int p) {
        for (int i = p; i < expression.length(); i++) {
            if (expression.charAt(i) > ' ') {
                return i;
            }
        }
        return expression.length();
    }

    private int skipUntil(String expression, int p, String endChars) {
        for (int i = p; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (endChars.indexOf(c) > -1) {
                return i;
            }
        }
        return expression.length();
    }

    private void jdbcTypeOpt(String expression, int p) {
        int p2 = skipWS(expression, p);
        if (p2 < expression.length()) {
            if (expression.charAt(p2) == ':') {
                jdbcType(expression, p2 + 1);
            } else {
                if (expression.charAt(p2) == ',') {
                    option(expression, p2 + 1);
                    return;
                }
                throw new BuilderException("Parsing error in {" + expression + "} in position " + p2);
            }
        }
    }

    private void jdbcType(String expression, int p) {
        int left = skipWS(expression, p);
        int right = skipUntil(expression, left, ",");
        if (right > left) {
            put("jdbcType", trimmedStr(expression, left, right));
            option(expression, right + 1);
            return;
        }
        throw new BuilderException("Parsing error in {" + expression + "} in position " + p);
    }

    private void option(String expression, int p) {
        int left = skipWS(expression, p);
        if (left < expression.length()) {
            int right = skipUntil(expression, left, SymbolConstants.EQUAL_SYMBOL);
            String name = trimmedStr(expression, left, right);
            int left2 = right + 1;
            int right2 = skipUntil(expression, left2, ",");
            String value = trimmedStr(expression, left2, right2);
            put(name, value);
            option(expression, right2 + 1);
        }
    }

    private String trimmedStr(String str, int start, int end) {
        while (str.charAt(start) <= ' ') {
            start++;
        }
        while (str.charAt(end - 1) <= ' ') {
            end--;
        }
        return start >= end ? "" : str.substring(start, end);
    }
}
