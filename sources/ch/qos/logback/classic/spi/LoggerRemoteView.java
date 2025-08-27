package ch.qos.logback.classic.spi;

import ch.qos.logback.classic.LoggerContext;
import java.io.Serializable;

/* JADX WARN: Classes with same name are omitted:
  logback-classic-1.2.3.jar:ch/qos/logback/classic/spi/LoggerRemoteView.class
 */
/* loaded from: logback-classic-1.2.3.jar.bak:ch/qos/logback/classic/spi/LoggerRemoteView.class */
public class LoggerRemoteView implements Serializable {
    private static final long serialVersionUID = 5028223666108713696L;
    final LoggerContextVO loggerContextView;
    final String name;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !LoggerRemoteView.class.desiredAssertionStatus();
    }

    public LoggerRemoteView(String name, LoggerContext lc) {
        this.name = name;
        if (!$assertionsDisabled && lc.getLoggerContextRemoteView() == null) {
            throw new AssertionError();
        }
        this.loggerContextView = lc.getLoggerContextRemoteView();
    }

    public LoggerContextVO getLoggerContextView() {
        return this.loggerContextView;
    }

    public String getName() {
        return this.name;
    }
}
