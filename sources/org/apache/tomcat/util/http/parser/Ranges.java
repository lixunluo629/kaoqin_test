package org.apache.tomcat.util.http.parser;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/http/parser/Ranges.class */
public class Ranges {
    private final String units;
    private final List<Entry> entries;

    private Ranges(String units, List<Entry> entries) {
        this.units = units;
        this.entries = Collections.unmodifiableList(entries);
    }

    public List<Entry> getEntries() {
        return this.entries;
    }

    public String getUnits() {
        return this.units;
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/http/parser/Ranges$Entry.class */
    public static class Entry {
        private final long start;
        private final long end;

        public Entry(long start, long end) {
            this.start = start;
            this.end = end;
        }

        public long getStart() {
            return this.start;
        }

        public long getEnd() {
            return this.end;
        }
    }

    public static Ranges parse(StringReader input) throws IOException {
        SkipResult skipResult;
        String units = HttpParser.readToken(input);
        if (units == null || units.length() == 0 || HttpParser.skipConstant(input, SymbolConstants.EQUAL_SYMBOL) == SkipResult.NOT_FOUND) {
            return null;
        }
        List<Entry> entries = new ArrayList<>();
        do {
            long start = HttpParser.readLong(input);
            if (HttpParser.skipConstant(input, "-") == SkipResult.NOT_FOUND) {
                return null;
            }
            long end = HttpParser.readLong(input);
            if (start == -1 && end == -1) {
                return null;
            }
            entries.add(new Entry(start, end));
            skipResult = HttpParser.skipConstant(input, ",");
            if (skipResult == SkipResult.NOT_FOUND) {
                return null;
            }
        } while (skipResult == SkipResult.FOUND);
        if (entries.size() == 0) {
            return null;
        }
        return new Ranges(units, entries);
    }
}
