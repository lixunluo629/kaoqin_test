package org.apache.ibatis.parsing;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/parsing/GenericTokenParser.class */
public class GenericTokenParser {
    private final String openToken;
    private final String closeToken;
    private final TokenHandler handler;

    public GenericTokenParser(String openToken, String closeToken, TokenHandler handler) {
        this.openToken = openToken;
        this.closeToken = closeToken;
        this.handler = handler;
    }

    public String parse(String text) {
        int end;
        int length;
        if (text == null || text.isEmpty()) {
            return "";
        }
        int start = text.indexOf(this.openToken, 0);
        if (start == -1) {
            return text;
        }
        char[] src = text.toCharArray();
        int offset = 0;
        StringBuilder builder = new StringBuilder();
        StringBuilder expression = null;
        while (start > -1) {
            if (start > 0 && src[start - 1] == '\\') {
                builder.append(src, offset, (start - offset) - 1).append(this.openToken);
                length = start + this.openToken.length();
            } else {
                if (expression == null) {
                    expression = new StringBuilder();
                } else {
                    expression.setLength(0);
                }
                builder.append(src, offset, start - offset);
                int offset2 = start + this.openToken.length();
                int iIndexOf = text.indexOf(this.closeToken, offset2);
                while (true) {
                    end = iIndexOf;
                    if (end <= -1) {
                        break;
                    }
                    if (end <= offset2 || src[end - 1] != '\\') {
                        break;
                    }
                    expression.append(src, offset2, (end - offset2) - 1).append(this.closeToken);
                    offset2 = end + this.closeToken.length();
                    iIndexOf = text.indexOf(this.closeToken, offset2);
                }
                expression.append(src, offset2, end - offset2);
                int length2 = end + this.closeToken.length();
                if (end == -1) {
                    builder.append(src, start, src.length - start);
                    length = src.length;
                } else {
                    builder.append(this.handler.handleToken(expression.toString()));
                    length = end + this.closeToken.length();
                }
            }
            offset = length;
            start = text.indexOf(this.openToken, offset);
        }
        if (offset < src.length) {
            builder.append(src, offset, src.length - offset);
        }
        return builder.toString();
    }
}
