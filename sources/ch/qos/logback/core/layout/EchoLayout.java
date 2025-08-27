package ch.qos.logback.core.layout;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/layout/EchoLayout.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/layout/EchoLayout.class */
public class EchoLayout<E> extends LayoutBase<E> {
    @Override // ch.qos.logback.core.Layout
    public String doLayout(E event) {
        return event + CoreConstants.LINE_SEPARATOR;
    }
}
