package org.apache.xmlbeans.impl.util;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.List;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/util/Diff.class */
public class Diff {
    public static void readersAsText(Reader r1, String name1, Reader r2, String name2, List diffs) throws IOException {
        String line2;
        LineNumberReader reader1 = new LineNumberReader(r1);
        LineNumberReader reader2 = new LineNumberReader(r2);
        String line1 = reader1.readLine();
        String line = reader2.readLine();
        while (true) {
            line2 = line;
            if (line1 == null || line2 == null) {
                break;
            }
            if (!line1.equals(line2)) {
                diffs.add("File \"" + name1 + "\" and file \"" + name2 + "\" differ at line " + reader1.getLineNumber() + ":" + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR + line1 + "\n========\n" + line2);
                break;
            } else {
                line1 = reader1.readLine();
                line = reader2.readLine();
            }
        }
        if (line1 == null && line2 != null) {
            diffs.add("File \"" + name2 + "\" has extra lines at line " + reader2.getLineNumber() + ":\n" + line2);
        }
        if (line1 != null && line2 == null) {
            diffs.add("File \"" + name1 + "\" has extra lines at line " + reader1.getLineNumber() + ":\n" + line1);
        }
    }
}
