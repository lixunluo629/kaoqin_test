package ch.qos.logback.core.joran.action;

import ch.qos.logback.core.joran.spi.ElementPath;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import org.xml.sax.Attributes;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/joran/action/ImplicitAction.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/joran/action/ImplicitAction.class */
public abstract class ImplicitAction extends Action {
    public abstract boolean isApplicable(ElementPath elementPath, Attributes attributes, InterpretationContext interpretationContext);
}
