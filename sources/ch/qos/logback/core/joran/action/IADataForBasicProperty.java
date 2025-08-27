package ch.qos.logback.core.joran.action;

import ch.qos.logback.core.joran.util.PropertySetter;
import ch.qos.logback.core.util.AggregationType;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/joran/action/IADataForBasicProperty.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/joran/action/IADataForBasicProperty.class */
class IADataForBasicProperty {
    final PropertySetter parentBean;
    final AggregationType aggregationType;
    final String propertyName;
    boolean inError;

    IADataForBasicProperty(PropertySetter parentBean, AggregationType aggregationType, String propertyName) {
        this.parentBean = parentBean;
        this.aggregationType = aggregationType;
        this.propertyName = propertyName;
    }
}
