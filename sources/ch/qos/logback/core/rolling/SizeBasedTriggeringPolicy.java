package ch.qos.logback.core.rolling;

import ch.qos.logback.core.util.DefaultInvocationGate;
import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.util.InvocationGate;
import java.io.File;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/rolling/SizeBasedTriggeringPolicy.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/rolling/SizeBasedTriggeringPolicy.class */
public class SizeBasedTriggeringPolicy<E> extends TriggeringPolicyBase<E> {
    public static final String SEE_SIZE_FORMAT = "http://logback.qos.ch/codes.html#sbtp_size_format";
    public static final long DEFAULT_MAX_FILE_SIZE = 10485760;
    FileSize maxFileSize = new FileSize(DEFAULT_MAX_FILE_SIZE);
    InvocationGate invocationGate = new DefaultInvocationGate();

    @Override // ch.qos.logback.core.rolling.TriggeringPolicy
    public boolean isTriggeringEvent(File activeFile, E event) {
        long now = System.currentTimeMillis();
        return !this.invocationGate.isTooSoon(now) && activeFile.length() >= this.maxFileSize.getSize();
    }

    public void setMaxFileSize(FileSize aMaxFileSize) {
        this.maxFileSize = aMaxFileSize;
    }
}
