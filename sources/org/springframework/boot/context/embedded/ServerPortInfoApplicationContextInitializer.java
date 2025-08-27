package org.springframework.boot.context.embedded;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.util.StringUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/ServerPortInfoApplicationContextInitializer.class */
public class ServerPortInfoApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override // org.springframework.context.ApplicationContextInitializer
    public void initialize(ConfigurableApplicationContext applicationContext) {
        applicationContext.addApplicationListener(new ApplicationListener<EmbeddedServletContainerInitializedEvent>() { // from class: org.springframework.boot.context.embedded.ServerPortInfoApplicationContextInitializer.1
            @Override // org.springframework.context.ApplicationListener
            public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
                ServerPortInfoApplicationContextInitializer.this.onApplicationEvent(event);
            }
        });
    }

    protected void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
        String propertyName = getPropertyName(event.getApplicationContext());
        setPortProperty(event.getApplicationContext(), propertyName, event.getEmbeddedServletContainer().getPort());
    }

    protected String getPropertyName(EmbeddedWebApplicationContext context) {
        String name = context.getNamespace();
        if (StringUtils.isEmpty(name)) {
            name = "server";
        }
        return "local." + name + ".port";
    }

    private void setPortProperty(ApplicationContext context, String propertyName, int port) {
        if (context instanceof ConfigurableApplicationContext) {
            setPortProperty(((ConfigurableApplicationContext) context).getEnvironment(), propertyName, port);
        }
        if (context.getParent() != null) {
            setPortProperty(context.getParent(), propertyName, port);
        }
    }

    private void setPortProperty(ConfigurableEnvironment environment, String propertyName, int port) {
        MutablePropertySources sources = environment.getPropertySources();
        PropertySource<?> source = sources.get("server.ports");
        if (source == null) {
            source = new MapPropertySource("server.ports", new HashMap());
            sources.addFirst(source);
        }
        ((Map) source.getSource()).put(propertyName, Integer.valueOf(port));
    }
}
