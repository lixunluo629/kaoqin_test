package ch.qos.logback.classic.layout;

import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;
import ch.qos.logback.core.util.CachingDateFormatter;
import com.moredian.onpremise.core.common.constants.SymbolConstants;

/* JADX WARN: Classes with same name are omitted:
  logback-classic-1.2.3.jar:ch/qos/logback/classic/layout/TTLLLayout.class
 */
/* loaded from: logback-classic-1.2.3.jar.bak:ch/qos/logback/classic/layout/TTLLLayout.class */
public class TTLLLayout extends LayoutBase<ILoggingEvent> {
    CachingDateFormatter cachingDateFormatter = new CachingDateFormatter("HH:mm:ss.SSS");
    ThrowableProxyConverter tpc = new ThrowableProxyConverter();

    @Override // ch.qos.logback.core.LayoutBase, ch.qos.logback.core.spi.LifeCycle
    public void start() {
        this.tpc.start();
        super.start();
    }

    @Override // ch.qos.logback.core.Layout
    public String doLayout(ILoggingEvent event) {
        if (!isStarted()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        long timestamp = event.getTimeStamp();
        sb.append(this.cachingDateFormatter.format(timestamp));
        sb.append(" [");
        sb.append(event.getThreadName());
        sb.append("] ");
        sb.append(event.getLevel().toString());
        sb.append(SymbolConstants.SPACE_SYMBOL);
        sb.append(event.getLoggerName());
        sb.append(" - ");
        sb.append(event.getFormattedMessage());
        sb.append(CoreConstants.LINE_SEPARATOR);
        IThrowableProxy tp = event.getThrowableProxy();
        if (tp != null) {
            String stackTrace = this.tpc.convert(event);
            sb.append(stackTrace);
        }
        return sb.toString();
    }
}
