package org.springframework.web.servlet.view.tiles2;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.tiles.TilesException;
import org.apache.tiles.preparer.NoSuchPreparerException;
import org.apache.tiles.preparer.PreparerException;
import org.apache.tiles.preparer.ViewPreparer;
import org.springframework.web.context.WebApplicationContext;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/tiles2/SimpleSpringPreparerFactory.class */
public class SimpleSpringPreparerFactory extends AbstractSpringPreparerFactory {
    private final Map<String, ViewPreparer> sharedPreparers = new ConcurrentHashMap(16);

    @Override // org.springframework.web.servlet.view.tiles2.AbstractSpringPreparerFactory
    protected ViewPreparer getPreparer(String name, WebApplicationContext context) throws TilesException {
        ViewPreparer preparer = this.sharedPreparers.get(name);
        if (preparer == null) {
            synchronized (this.sharedPreparers) {
                preparer = this.sharedPreparers.get(name);
                if (preparer == null) {
                    try {
                        Class<?> beanClass = context.getClassLoader().loadClass(name);
                        if (!ViewPreparer.class.isAssignableFrom(beanClass)) {
                            throw new PreparerException("Invalid preparer class [" + name + "]: does not implement ViewPreparer interface");
                        }
                        preparer = (ViewPreparer) context.getAutowireCapableBeanFactory().createBean(beanClass);
                        this.sharedPreparers.put(name, preparer);
                    } catch (ClassNotFoundException ex) {
                        throw new NoSuchPreparerException("Preparer class [" + name + "] not found", ex);
                    }
                }
            }
        }
        return preparer;
    }
}
