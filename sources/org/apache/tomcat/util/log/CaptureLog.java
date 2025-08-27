package org.apache.tomcat.util.log;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/log/CaptureLog.class */
class CaptureLog {
    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private final PrintStream ps = new PrintStream(this.baos);

    protected CaptureLog() {
    }

    protected PrintStream getStream() {
        return this.ps;
    }

    protected void reset() {
        this.baos.reset();
    }

    protected String getCapture() {
        return this.baos.toString();
    }
}
