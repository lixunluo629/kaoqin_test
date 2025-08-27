package org.aspectj.weaver.patterns;

import io.swagger.models.properties.StringProperty;
import java.util.ArrayList;
import java.util.List;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.ISourceContext;
import org.springframework.beans.PropertyAccessor;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/BasicTokenSource.class */
public class BasicTokenSource implements ITokenSource {
    private int index = 0;
    private IToken[] tokens;
    private ISourceContext sourceContext;

    public BasicTokenSource(IToken[] tokens, ISourceContext sourceContext) {
        this.tokens = tokens;
        this.sourceContext = sourceContext;
    }

    @Override // org.aspectj.weaver.patterns.ITokenSource
    public int getIndex() {
        return this.index;
    }

    @Override // org.aspectj.weaver.patterns.ITokenSource
    public void setIndex(int newIndex) {
        this.index = newIndex;
    }

    @Override // org.aspectj.weaver.patterns.ITokenSource
    public IToken next() {
        try {
            IToken[] iTokenArr = this.tokens;
            int i = this.index;
            this.index = i + 1;
            return iTokenArr[i];
        } catch (ArrayIndexOutOfBoundsException e) {
            return IToken.EOF;
        }
    }

    @Override // org.aspectj.weaver.patterns.ITokenSource
    public IToken peek() {
        try {
            return this.tokens[this.index];
        } catch (ArrayIndexOutOfBoundsException e) {
            return IToken.EOF;
        }
    }

    @Override // org.aspectj.weaver.patterns.ITokenSource
    public IToken peek(int offset) {
        try {
            return this.tokens[this.index + offset];
        } catch (ArrayIndexOutOfBoundsException e) {
            return IToken.EOF;
        }
    }

    public String toString() {
        IToken t;
        StringBuffer buf = new StringBuffer();
        buf.append(PropertyAccessor.PROPERTY_KEY_PREFIX);
        for (int i = 0; i < this.tokens.length && (t = this.tokens[i]) != null; i++) {
            if (i > 0) {
                buf.append(", ");
            }
            buf.append(t.toString());
        }
        buf.append("]");
        return buf.toString();
    }

    public static ITokenSource makeTokenSource(String input, ISourceContext context) {
        char[] chars = input.toCharArray();
        int i = 0;
        List<BasicToken> tokens = new ArrayList<>();
        while (i < chars.length) {
            int i2 = i;
            i++;
            char ch2 = chars[i2];
            switch (ch2) {
                case '\t':
                case '\n':
                case '\r':
                case ' ':
                case '!':
                case '(':
                case ')':
                case '*':
                case '+':
                case ',':
                case ':':
                case '<':
                case '=':
                case '>':
                case '?':
                case '@':
                case '[':
                case ']':
                    tokens.add(BasicToken.makeOperator(makeString(ch2), i - 1, i - 1));
                    continue;
                case '\"':
                    int start0 = i - 1;
                    while (i < chars.length && chars[i] != '\"') {
                        i++;
                    }
                    i++;
                    tokens.add(BasicToken.makeLiteral(new String(chars, start0 + 1, (i - start0) - 2), StringProperty.TYPE, start0, i - 1));
                    continue;
                case '&':
                    if (i + 1 > chars.length || chars[i] == '&') {
                        break;
                    } else {
                        tokens.add(BasicToken.makeOperator(makeString(ch2), i - 1, i - 1));
                    }
                    break;
                case '.':
                    if (i + 2 <= chars.length) {
                        char nextChar1 = chars[i];
                        char nextChar2 = chars[i + 1];
                        if (ch2 == nextChar1 && ch2 == nextChar2) {
                            tokens.add(BasicToken.makeIdentifier("...", i - 1, i + 1));
                            i += 2;
                        } else {
                            tokens.add(BasicToken.makeOperator(makeString(ch2), i - 1, i - 1));
                        }
                    } else {
                        tokens.add(BasicToken.makeOperator(makeString(ch2), i - 1, i - 1));
                        continue;
                    }
                    break;
                case '|':
                    break;
                default:
                    int start = i - 1;
                    while (i < chars.length && Character.isJavaIdentifierPart(chars[i])) {
                        i++;
                    }
                    tokens.add(BasicToken.makeIdentifier(new String(chars, start, i - start), start, i - 1));
                    continue;
            }
            if (i == chars.length) {
                throw new BCException("bad " + ch2);
            }
            i++;
            char nextChar = chars[i];
            if (nextChar == ch2) {
                tokens.add(BasicToken.makeOperator(makeString(ch2, 2), i - 2, i - 1));
            } else {
                throw new RuntimeException("bad " + ch2);
            }
        }
        return new BasicTokenSource((IToken[]) tokens.toArray(new IToken[tokens.size()]), context);
    }

    private static String makeString(char ch2) {
        return Character.toString(ch2);
    }

    private static String makeString(char ch2, int count) {
        char[] chars = new char[count];
        for (int i = 0; i < count; i++) {
            chars[i] = ch2;
        }
        return new String(chars);
    }

    @Override // org.aspectj.weaver.patterns.ITokenSource
    public ISourceContext getSourceContext() {
        return this.sourceContext;
    }

    public void setSourceContext(ISourceContext context) {
        this.sourceContext = context;
    }
}
