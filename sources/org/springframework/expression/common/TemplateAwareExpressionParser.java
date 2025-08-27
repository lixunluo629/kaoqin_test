package org.springframework.expression.common;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.ParserContext;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/common/TemplateAwareExpressionParser.class */
public abstract class TemplateAwareExpressionParser implements ExpressionParser {
    private static final ParserContext NON_TEMPLATE_PARSER_CONTEXT = new ParserContext() { // from class: org.springframework.expression.common.TemplateAwareExpressionParser.1
        @Override // org.springframework.expression.ParserContext
        public String getExpressionPrefix() {
            return null;
        }

        @Override // org.springframework.expression.ParserContext
        public String getExpressionSuffix() {
            return null;
        }

        @Override // org.springframework.expression.ParserContext
        public boolean isTemplate() {
            return false;
        }
    };

    protected abstract Expression doParseExpression(String str, ParserContext parserContext) throws ParseException;

    @Override // org.springframework.expression.ExpressionParser
    public Expression parseExpression(String expressionString) throws ParseException {
        return parseExpression(expressionString, NON_TEMPLATE_PARSER_CONTEXT);
    }

    @Override // org.springframework.expression.ExpressionParser
    public Expression parseExpression(String expressionString, ParserContext context) throws ParseException {
        if (context == null) {
            context = NON_TEMPLATE_PARSER_CONTEXT;
        }
        if (context.isTemplate()) {
            return parseTemplate(expressionString, context);
        }
        return doParseExpression(expressionString, context);
    }

    private Expression parseTemplate(String expressionString, ParserContext context) throws ParseException {
        if (expressionString.isEmpty()) {
            return new LiteralExpression("");
        }
        Expression[] expressions = parseExpressions(expressionString, context);
        if (expressions.length == 1) {
            return expressions[0];
        }
        return new CompositeStringExpression(expressionString, expressions);
    }

    private Expression[] parseExpressions(String expressionString, ParserContext context) throws ParseException {
        List<Expression> expressions = new LinkedList<>();
        String prefix = context.getExpressionPrefix();
        String suffix = context.getExpressionSuffix();
        int length = 0;
        while (true) {
            int startIdx = length;
            if (startIdx < expressionString.length()) {
                int prefixIndex = expressionString.indexOf(prefix, startIdx);
                if (prefixIndex >= startIdx) {
                    if (prefixIndex > startIdx) {
                        expressions.add(new LiteralExpression(expressionString.substring(startIdx, prefixIndex)));
                    }
                    int afterPrefixIndex = prefixIndex + prefix.length();
                    int suffixIndex = skipToCorrectEndSuffix(suffix, expressionString, afterPrefixIndex);
                    if (suffixIndex == -1) {
                        throw new ParseException(expressionString, prefixIndex, "No ending suffix '" + suffix + "' for expression starting at character " + prefixIndex + ": " + expressionString.substring(prefixIndex));
                    }
                    if (suffixIndex == afterPrefixIndex) {
                        throw new ParseException(expressionString, prefixIndex, "No expression defined within delimiter '" + prefix + suffix + "' at character " + prefixIndex);
                    }
                    String expr = expressionString.substring(prefixIndex + prefix.length(), suffixIndex).trim();
                    if (expr.isEmpty()) {
                        throw new ParseException(expressionString, prefixIndex, "No expression defined within delimiter '" + prefix + suffix + "' at character " + prefixIndex);
                    }
                    expressions.add(doParseExpression(expr, context));
                    length = suffixIndex + suffix.length();
                } else {
                    expressions.add(new LiteralExpression(expressionString.substring(startIdx)));
                    length = expressionString.length();
                }
            } else {
                return (Expression[]) expressions.toArray(new Expression[expressions.size()]);
            }
        }
    }

    private boolean isSuffixHere(String expressionString, int pos, String suffix) {
        int suffixPosition = 0;
        for (int i = 0; i < suffix.length() && pos < expressionString.length(); i++) {
            int i2 = pos;
            pos++;
            int i3 = suffixPosition;
            suffixPosition++;
            if (expressionString.charAt(i2) != suffix.charAt(i3)) {
                return false;
            }
        }
        if (suffixPosition != suffix.length()) {
            return false;
        }
        return true;
    }

    private int skipToCorrectEndSuffix(String suffix, String expressionString, int afterPrefixIndex) throws ParseException {
        int pos = afterPrefixIndex;
        int maxlen = expressionString.length();
        int nextSuffix = expressionString.indexOf(suffix, afterPrefixIndex);
        if (nextSuffix == -1) {
            return -1;
        }
        Stack<Bracket> stack = new Stack<>();
        while (pos < maxlen && (!isSuffixHere(expressionString, pos, suffix) || !stack.isEmpty())) {
            char ch2 = expressionString.charAt(pos);
            switch (ch2) {
                case '\"':
                case '\'':
                    int endLiteral = expressionString.indexOf(ch2, pos + 1);
                    if (endLiteral == -1) {
                        throw new ParseException(expressionString, pos, "Found non terminating string literal starting at position " + pos);
                    }
                    pos = endLiteral;
                    break;
                case '(':
                case '[':
                case '{':
                    stack.push(new Bracket(ch2, pos));
                    break;
                case ')':
                case ']':
                case '}':
                    if (stack.isEmpty()) {
                        throw new ParseException(expressionString, pos, "Found closing '" + ch2 + "' at position " + pos + " without an opening '" + Bracket.theOpenBracketFor(ch2) + "'");
                    }
                    Bracket p = stack.pop();
                    if (!p.compatibleWithCloseBracket(ch2)) {
                        throw new ParseException(expressionString, pos, "Found closing '" + ch2 + "' at position " + pos + " but most recent opening is '" + p.bracket + "' at position " + p.pos);
                    }
                    break;
            }
            pos++;
        }
        if (!stack.isEmpty()) {
            Bracket p2 = stack.pop();
            throw new ParseException(expressionString, p2.pos, "Missing closing '" + Bracket.theCloseBracketFor(p2.bracket) + "' for '" + p2.bracket + "' at position " + p2.pos);
        }
        if (!isSuffixHere(expressionString, pos, suffix)) {
            return -1;
        }
        return pos;
    }

    /* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/common/TemplateAwareExpressionParser$Bracket.class */
    private static class Bracket {
        char bracket;
        int pos;

        Bracket(char bracket, int pos) {
            this.bracket = bracket;
            this.pos = pos;
        }

        boolean compatibleWithCloseBracket(char closeBracket) {
            return this.bracket == '{' ? closeBracket == '}' : this.bracket == '[' ? closeBracket == ']' : closeBracket == ')';
        }

        static char theOpenBracketFor(char closeBracket) {
            if (closeBracket == '}') {
                return '{';
            }
            if (closeBracket == ']') {
                return '[';
            }
            return '(';
        }

        static char theCloseBracketFor(char openBracket) {
            if (openBracket == '{') {
                return '}';
            }
            if (openBracket == '[') {
                return ']';
            }
            return ')';
        }
    }
}
