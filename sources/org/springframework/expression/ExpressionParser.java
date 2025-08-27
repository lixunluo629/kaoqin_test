package org.springframework.expression;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/ExpressionParser.class */
public interface ExpressionParser {
    Expression parseExpression(String str) throws ParseException;

    Expression parseExpression(String str, ParserContext parserContext) throws ParseException;
}
