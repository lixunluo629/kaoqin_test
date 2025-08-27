package org.apache.commons.httpclient.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.httpclient.NameValuePair;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/util/ParameterParser.class */
public class ParameterParser {
    private char[] chars = null;
    private int pos = 0;
    private int len = 0;
    private int i1 = 0;
    private int i2 = 0;

    private boolean hasChar() {
        return this.pos < this.len;
    }

    private String getToken(boolean quoted) {
        while (this.i1 < this.i2 && Character.isWhitespace(this.chars[this.i1])) {
            this.i1++;
        }
        while (this.i2 > this.i1 && Character.isWhitespace(this.chars[this.i2 - 1])) {
            this.i2--;
        }
        if (quoted && this.i2 - this.i1 >= 2 && this.chars[this.i1] == '\"' && this.chars[this.i2 - 1] == '\"') {
            this.i1++;
            this.i2--;
        }
        String result = null;
        if (this.i2 >= this.i1) {
            result = new String(this.chars, this.i1, this.i2 - this.i1);
        }
        return result;
    }

    private boolean isOneOf(char ch2, char[] charray) {
        boolean result = false;
        int i = 0;
        while (true) {
            if (i >= charray.length) {
                break;
            }
            if (ch2 != charray[i]) {
                i++;
            } else {
                result = true;
                break;
            }
        }
        return result;
    }

    private String parseToken(char[] terminators) {
        this.i1 = this.pos;
        this.i2 = this.pos;
        while (hasChar()) {
            char ch2 = this.chars[this.pos];
            if (isOneOf(ch2, terminators)) {
                break;
            }
            this.i2++;
            this.pos++;
        }
        return getToken(false);
    }

    private String parseQuotedToken(char[] terminators) {
        this.i1 = this.pos;
        this.i2 = this.pos;
        boolean quoted = false;
        boolean charEscaped = false;
        while (hasChar()) {
            char ch2 = this.chars[this.pos];
            if (!quoted && isOneOf(ch2, terminators)) {
                break;
            }
            if (!charEscaped && ch2 == '\"') {
                quoted = !quoted;
            }
            charEscaped = !charEscaped && ch2 == '\\';
            this.i2++;
            this.pos++;
        }
        return getToken(true);
    }

    public List parse(String str, char separator) {
        if (str == null) {
            return new ArrayList();
        }
        return parse(str.toCharArray(), separator);
    }

    public List parse(char[] chars, char separator) {
        if (chars == null) {
            return new ArrayList();
        }
        return parse(chars, 0, chars.length, separator);
    }

    public List parse(char[] chars, int offset, int length, char separator) {
        if (chars == null) {
            return new ArrayList();
        }
        List params = new ArrayList();
        this.chars = chars;
        this.pos = offset;
        this.len = length;
        while (hasChar()) {
            String paramName = parseToken(new char[]{'=', separator});
            String paramValue = null;
            if (hasChar() && chars[this.pos] == '=') {
                this.pos++;
                paramValue = parseQuotedToken(new char[]{separator});
            }
            if (hasChar() && chars[this.pos] == separator) {
                this.pos++;
            }
            if (paramName != null && (!paramName.equals("") || paramValue != null)) {
                params.add(new NameValuePair(paramName, paramValue));
            }
        }
        return params;
    }
}
