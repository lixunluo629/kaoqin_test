package org.apache.commons.httpclient.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.util.ParameterParser;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/auth/AuthChallengeParser.class */
public final class AuthChallengeParser {
    public static String extractScheme(String challengeStr) throws MalformedChallengeException {
        String s;
        if (challengeStr == null) {
            throw new IllegalArgumentException("Challenge may not be null");
        }
        int idx = challengeStr.indexOf(32);
        if (idx == -1) {
            s = challengeStr;
        } else {
            s = challengeStr.substring(0, idx);
        }
        if (s.equals("")) {
            throw new MalformedChallengeException(new StringBuffer().append("Invalid challenge: ").append(challengeStr).toString());
        }
        return s.toLowerCase();
    }

    public static Map extractParams(String challengeStr) throws MalformedChallengeException {
        if (challengeStr == null) {
            throw new IllegalArgumentException("Challenge may not be null");
        }
        int idx = challengeStr.indexOf(32);
        if (idx == -1) {
            throw new MalformedChallengeException(new StringBuffer().append("Invalid challenge: ").append(challengeStr).toString());
        }
        Map map = new HashMap();
        ParameterParser parser = new ParameterParser();
        List params = parser.parse(challengeStr.substring(idx + 1, challengeStr.length()), ',');
        for (int i = 0; i < params.size(); i++) {
            NameValuePair param = (NameValuePair) params.get(i);
            map.put(param.getName().toLowerCase(), param.getValue());
        }
        return map;
    }

    public static Map parseChallenges(Header[] headers) throws MalformedChallengeException {
        if (headers == null) {
            throw new IllegalArgumentException("Array of challenges may not be null");
        }
        Map challengemap = new HashMap(headers.length);
        for (Header header : headers) {
            String challenge = header.getValue();
            String s = extractScheme(challenge);
            challengemap.put(s, challenge);
        }
        return challengemap;
    }
}
