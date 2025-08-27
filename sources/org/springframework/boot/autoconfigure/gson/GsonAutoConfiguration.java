package org.springframework.boot.autoconfigure.gson;

import com.google.gson.Gson;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({Gson.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/gson/GsonAutoConfiguration.class */
public class GsonAutoConfiguration {
    @ConditionalOnMissingBean
    @Bean
    public Gson gson() {
        return new Gson();
    }
}
