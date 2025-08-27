package ch.qos.logback.core.joran.util.beans;

import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.ContextAwareBase;
import java.util.HashMap;
import java.util.Map;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/joran/util/beans/BeanDescriptionCache.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/joran/util/beans/BeanDescriptionCache.class */
public class BeanDescriptionCache extends ContextAwareBase {
    private Map<Class<?>, BeanDescription> classToBeanDescription = new HashMap();
    private BeanDescriptionFactory beanDescriptionFactory;

    public BeanDescriptionCache(Context context) {
        setContext(context);
    }

    private BeanDescriptionFactory getBeanDescriptionFactory() {
        if (this.beanDescriptionFactory == null) {
            this.beanDescriptionFactory = new BeanDescriptionFactory(getContext());
        }
        return this.beanDescriptionFactory;
    }

    public BeanDescription getBeanDescription(Class<?> clazz) {
        if (!this.classToBeanDescription.containsKey(clazz)) {
            BeanDescription beanDescription = getBeanDescriptionFactory().create(clazz);
            this.classToBeanDescription.put(clazz, beanDescription);
        }
        return this.classToBeanDescription.get(clazz);
    }
}
