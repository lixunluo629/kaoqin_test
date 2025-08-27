package org.apache.tomcat.util.http.parser;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.httpclient.auth.AuthPolicy;
import org.apache.tomcat.util.res.StringManager;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/http/parser/Authorization.class */
public class Authorization {
    private static final StringManager sm = StringManager.getManager((Class<?>) Authorization.class);
    private static final Integer FIELD_TYPE_TOKEN = 0;
    private static final Integer FIELD_TYPE_QUOTED_STRING = 1;
    private static final Integer FIELD_TYPE_TOKEN_OR_QUOTED_STRING = 2;
    private static final Integer FIELD_TYPE_LHEX = 3;
    private static final Integer FIELD_TYPE_QUOTED_TOKEN = 4;
    private static final Map<String, Integer> fieldTypes = new HashMap();

    static {
        fieldTypes.put("username", FIELD_TYPE_QUOTED_STRING);
        fieldTypes.put("realm", FIELD_TYPE_QUOTED_STRING);
        fieldTypes.put("nonce", FIELD_TYPE_QUOTED_STRING);
        fieldTypes.put("digest-uri", FIELD_TYPE_QUOTED_STRING);
        fieldTypes.put("response", FIELD_TYPE_LHEX);
        fieldTypes.put("algorithm", FIELD_TYPE_QUOTED_TOKEN);
        fieldTypes.put("cnonce", FIELD_TYPE_QUOTED_STRING);
        fieldTypes.put("opaque", FIELD_TYPE_QUOTED_STRING);
        fieldTypes.put("qop", FIELD_TYPE_QUOTED_TOKEN);
        fieldTypes.put("nc", FIELD_TYPE_LHEX);
    }

    public static Map<String, String> parseAuthorizationDigest(StringReader input) throws IOException, IllegalArgumentException {
        String value;
        Map<String, String> result = new HashMap<>();
        if (HttpParser.skipConstant(input, AuthPolicy.DIGEST) != SkipResult.FOUND) {
            return null;
        }
        String field = HttpParser.readToken(input);
        if (field == null) {
            return null;
        }
        while (!field.equals("")) {
            if (HttpParser.skipConstant(input, SymbolConstants.EQUAL_SYMBOL) != SkipResult.FOUND) {
                return null;
            }
            Integer type = fieldTypes.get(field.toLowerCase(Locale.ENGLISH));
            if (type == null) {
                type = FIELD_TYPE_TOKEN_OR_QUOTED_STRING;
            }
            switch (type.intValue()) {
                case 0:
                    value = HttpParser.readToken(input);
                    break;
                case 1:
                    value = HttpParser.readQuotedString(input, false);
                    break;
                case 2:
                    value = HttpParser.readTokenOrQuotedString(input, false);
                    break;
                case 3:
                    value = HttpParser.readLhex(input);
                    break;
                case 4:
                    value = HttpParser.readQuotedToken(input);
                    break;
                default:
                    throw new IllegalArgumentException(sm.getString("authorization.unknownType", type));
            }
            if (value == null) {
                return null;
            }
            result.put(field, value);
            if (HttpParser.skipConstant(input, ",") == SkipResult.NOT_FOUND) {
                return null;
            }
            field = HttpParser.readToken(input);
            if (field == null) {
                return null;
            }
        }
        return result;
    }
}
