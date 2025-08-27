package org.apache.commons.httpclient;

import com.moredian.onpremise.core.common.constants.SymbolConstants;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/StatusLine.class */
public class StatusLine {
    private final String statusLine;
    private final String httpVersion;
    private final int statusCode;
    private final String reasonPhrase;

    public StatusLine(String statusLine) throws HttpException {
        int length = statusLine.length();
        int at = 0;
        int start = 0;
        while (Character.isWhitespace(statusLine.charAt(at))) {
            try {
                at++;
                start++;
            } catch (StringIndexOutOfBoundsException e) {
                throw new HttpException(new StringBuffer().append("Status-Line '").append(statusLine).append("' is not valid").toString());
            }
        }
        int i = at;
        int at2 = at + 4;
        if (!"HTTP".equals(statusLine.substring(i, at2))) {
            throw new HttpException(new StringBuffer().append("Status-Line '").append(statusLine).append("' does not start with HTTP").toString());
        }
        int at3 = statusLine.indexOf(SymbolConstants.SPACE_SYMBOL, at2);
        if (at3 <= 0) {
            throw new ProtocolException(new StringBuffer().append("Unable to parse HTTP-Version from the status line: '").append(statusLine).append("'").toString());
        }
        this.httpVersion = statusLine.substring(start, at3).toUpperCase();
        while (statusLine.charAt(at3) == ' ') {
            at3++;
        }
        int to = statusLine.indexOf(SymbolConstants.SPACE_SYMBOL, at3);
        to = to < 0 ? length : to;
        try {
            this.statusCode = Integer.parseInt(statusLine.substring(at3, to));
            int at4 = to + 1;
            if (at4 < length) {
                this.reasonPhrase = statusLine.substring(at4).trim();
            } else {
                this.reasonPhrase = "";
            }
            this.statusLine = statusLine;
        } catch (NumberFormatException e2) {
            throw new ProtocolException(new StringBuffer().append("Unable to parse status code from status line: '").append(statusLine).append("'").toString());
        }
    }

    public final int getStatusCode() {
        return this.statusCode;
    }

    public final String getHttpVersion() {
        return this.httpVersion;
    }

    public final String getReasonPhrase() {
        return this.reasonPhrase;
    }

    public final String toString() {
        return this.statusLine;
    }

    public static boolean startsWithHTTP(String s) {
        int at = 0;
        while (Character.isWhitespace(s.charAt(at))) {
            try {
                at++;
            } catch (StringIndexOutOfBoundsException e) {
                return false;
            }
        }
        return "HTTP".equals(s.substring(at, at + 4));
    }
}
