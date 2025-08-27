package org.springframework.boot.context;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/ContextIdApplicationContextInitializer.class */
public class ContextIdApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {
    private static final String NAME_PATTERN = "${spring.application.name:${vcap.application.name:${spring.config.name:application}}}";
    private static final String INDEX_PATTERN = "${vcap.application.instance_index:${spring.application.index:${server.port:${PORT:null}}}}";
    private final String name;
    private int order;

    public ContextIdApplicationContextInitializer() {
        this(NAME_PATTERN);
    }

    public ContextIdApplicationContextInitializer(String name) {
        this.order = 2147483637;
        this.name = name;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return this.order;
    }

    @Override // org.springframework.context.ApplicationContextInitializer
    public void initialize(ConfigurableApplicationContext applicationContext) {
        applicationContext.setId(getApplicationId(applicationContext.getEnvironment()));
    }

    private String getApplicationId(ConfigurableEnvironment environment) {
        String name = environment.resolvePlaceholders(this.name);
        String index = environment.resolvePlaceholders(INDEX_PATTERN);
        String profiles = StringUtils.arrayToCommaDelimitedString(environment.getActiveProfiles());
        if (StringUtils.hasText(profiles)) {
            name = name + ":" + profiles;
        }
        if (!"null".equals(index)) {
            name = name + ":" + index;
        }
        return name;
    }
}
