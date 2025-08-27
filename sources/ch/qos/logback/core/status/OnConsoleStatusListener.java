package ch.qos.logback.core.status;

import java.io.PrintStream;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/status/OnConsoleStatusListener.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/status/OnConsoleStatusListener.class */
public class OnConsoleStatusListener extends OnPrintStreamStatusListenerBase {
    @Override // ch.qos.logback.core.status.OnPrintStreamStatusListenerBase
    protected PrintStream getPrintStream() {
        return System.out;
    }
}
