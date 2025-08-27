package org.apache.juli;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/juli/VerbatimFormatter.class */
public class VerbatimFormatter extends Formatter {
    @Override // java.util.logging.Formatter
    public String format(LogRecord record) {
        return record.getMessage() + System.lineSeparator();
    }
}
