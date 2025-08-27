package org.springframework.boot.autoconfigure.websocket;

import java.lang.reflect.Constructor;
import org.apache.catalina.Context;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/websocket/TomcatWebSocketContainerCustomizer.class */
public class TomcatWebSocketContainerCustomizer extends WebSocketContainerCustomizer<TomcatEmbeddedServletContainerFactory> {
    private static final String TOMCAT_7_LISTENER_TYPE = "org.apache.catalina.deploy.ApplicationListener";
    private static final String TOMCAT_8_LISTENER_TYPE = "org.apache.tomcat.util.descriptor.web.ApplicationListener";
    private static final String WS_LISTENER = "org.apache.tomcat.websocket.server.WsContextListener";

    @Override // org.springframework.boot.autoconfigure.websocket.WebSocketContainerCustomizer
    public void doCustomize(TomcatEmbeddedServletContainerFactory tomcatContainer) {
        tomcatContainer.addContextCustomizers(new TomcatContextCustomizer() { // from class: org.springframework.boot.autoconfigure.websocket.TomcatWebSocketContainerCustomizer.1
            @Override // org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer
            public void customize(Context context) throws BeanInstantiationException {
                TomcatWebSocketContainerCustomizer.this.addListener(context, TomcatWebSocketContainerCustomizer.this.findListenerType());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Class<?> findListenerType() {
        if (ClassUtils.isPresent(TOMCAT_7_LISTENER_TYPE, null)) {
            return ClassUtils.resolveClassName(TOMCAT_7_LISTENER_TYPE, null);
        }
        if (ClassUtils.isPresent(TOMCAT_8_LISTENER_TYPE, null)) {
            return ClassUtils.resolveClassName(TOMCAT_8_LISTENER_TYPE, null);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addListener(Context context, Class<?> listenerType) throws BeanInstantiationException {
        Class<?> cls = context.getClass();
        if (listenerType == null) {
            ReflectionUtils.invokeMethod(ClassUtils.getMethod(cls, "addApplicationListener", String.class), context, WS_LISTENER);
            return;
        }
        Constructor<?> constructor = ClassUtils.getConstructorIfAvailable(listenerType, String.class, Boolean.TYPE);
        Object instance = BeanUtils.instantiateClass(constructor, WS_LISTENER, false);
        ReflectionUtils.invokeMethod(ClassUtils.getMethod(cls, "addApplicationListener", listenerType), context, instance);
    }
}
