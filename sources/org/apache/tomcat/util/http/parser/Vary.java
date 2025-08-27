package org.apache.tomcat.util.http.parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.Locale;
import java.util.Set;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/http/parser/Vary.class */
public class Vary {
    private Vary() {
    }

    public static void parseVary(StringReader input, Set<String> result) throws IOException {
        while (true) {
            String fieldName = HttpParser.readToken(input);
            if (fieldName == null) {
                HttpParser.skipUntil(input, 0, ',');
            } else if (fieldName.length() != 0) {
                SkipResult skipResult = HttpParser.skipConstant(input, ",");
                if (skipResult == SkipResult.EOF) {
                    result.add(fieldName.toLowerCase(Locale.ENGLISH));
                    return;
                } else if (skipResult == SkipResult.FOUND) {
                    result.add(fieldName.toLowerCase(Locale.ENGLISH));
                } else {
                    HttpParser.skipUntil(input, 0, ',');
                }
            } else {
                return;
            }
        }
    }
}
