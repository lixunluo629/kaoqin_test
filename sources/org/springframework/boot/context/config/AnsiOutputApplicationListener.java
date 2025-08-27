package org.springframework.boot.context.config;

import java.util.Locale;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/config/AnsiOutputApplicationListener.class */
public class AnsiOutputApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered {
    @Override // org.springframework.context.ApplicationListener
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(event.getEnvironment(), "spring.output.ansi.");
        if (resolver.containsProperty("enabled")) {
            String enabled = resolver.getProperty("enabled");
            AnsiOutput.setEnabled((AnsiOutput.Enabled) Enum.valueOf(AnsiOutput.Enabled.class, enabled.toUpperCase(Locale.ENGLISH)));
        }
        if (resolver.containsProperty("console-available")) {
            AnsiOutput.setConsoleAvailable((Boolean) resolver.getProperty("console-available", Boolean.class));
        }
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return -2147483637;
    }
}
