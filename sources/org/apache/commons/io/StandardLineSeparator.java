package org.apache.commons.io;

import java.nio.charset.Charset;
import java.util.Objects;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/StandardLineSeparator.class */
public enum StandardLineSeparator {
    CR("\r"),
    CRLF("\r\n"),
    LF(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);

    private final String lineSeparator;

    StandardLineSeparator(String lineSeparator) {
        this.lineSeparator = (String) Objects.requireNonNull(lineSeparator, "lineSeparator");
    }

    public byte[] getBytes(Charset charset) {
        return this.lineSeparator.getBytes(charset);
    }

    public String getString() {
        return this.lineSeparator;
    }
}
