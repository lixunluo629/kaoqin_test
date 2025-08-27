package ch.qos.logback.classic;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AsyncAppenderBase;

/* JADX WARN: Classes with same name are omitted:
  logback-classic-1.2.3.jar:ch/qos/logback/classic/AsyncAppender.class
 */
/* loaded from: logback-classic-1.2.3.jar.bak:ch/qos/logback/classic/AsyncAppender.class */
public class AsyncAppender extends AsyncAppenderBase<ILoggingEvent> {
    boolean includeCallerData = false;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // ch.qos.logback.core.AsyncAppenderBase
    public boolean isDiscardable(ILoggingEvent event) {
        Level level = event.getLevel();
        return level.toInt() <= 20000;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // ch.qos.logback.core.AsyncAppenderBase
    public void preprocess(ILoggingEvent eventObject) {
        eventObject.prepareForDeferredProcessing();
        if (this.includeCallerData) {
            eventObject.getCallerData();
        }
    }

    public boolean isIncludeCallerData() {
        return this.includeCallerData;
    }

    public void setIncludeCallerData(boolean includeCallerData) {
        this.includeCallerData = includeCallerData;
    }
}
