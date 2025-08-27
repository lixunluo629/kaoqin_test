package org.springframework.web.servlet.view.tiles2;

import org.apache.tiles.TilesException;
import org.apache.tiles.preparer.ViewPreparer;
import org.springframework.web.context.WebApplicationContext;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/tiles2/SpringBeanPreparerFactory.class */
public class SpringBeanPreparerFactory extends AbstractSpringPreparerFactory {
    @Override // org.springframework.web.servlet.view.tiles2.AbstractSpringPreparerFactory
    protected ViewPreparer getPreparer(String name, WebApplicationContext context) throws TilesException {
        return (ViewPreparer) context.getBean(name, ViewPreparer.class);
    }
}
