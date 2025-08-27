package ch.qos.logback.core.hook;

import ch.qos.logback.core.Context;
import ch.qos.logback.core.ContextBase;
import ch.qos.logback.core.spi.ContextAwareBase;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/hook/ShutdownHookBase.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/hook/ShutdownHookBase.class */
public abstract class ShutdownHookBase extends ContextAwareBase implements ShutdownHook {
    protected void stop() {
        addInfo("Logback context being closed via shutdown hook");
        Context hookContext = getContext();
        if (hookContext instanceof ContextBase) {
            ContextBase context = (ContextBase) hookContext;
            context.stop();
        }
    }
}
