package org.hyperic.sigar.ptql;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hyperic.sigar.util.ReferenceMap;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/ptql/StringPattern.class */
public class StringPattern {
    private static Map patterns = ReferenceMap.synchronizedMap();

    public static boolean matches(String source, String regex) {
        Pattern pattern = (Pattern) patterns.get(regex);
        if (pattern == null) {
            pattern = Pattern.compile(regex);
            patterns.put(regex, pattern);
        }
        Matcher matcher = pattern.matcher(source);
        return matcher.find();
    }
}
