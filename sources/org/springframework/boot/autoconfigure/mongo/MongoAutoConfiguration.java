package org.springframework.boot.autoconfigure.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import java.net.UnknownHostException;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@EnableConfigurationProperties({MongoProperties.class})
@Configuration
@ConditionalOnClass({MongoClient.class})
@ConditionalOnMissingBean(type = {"org.springframework.data.mongodb.MongoDbFactory"})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/mongo/MongoAutoConfiguration.class */
public class MongoAutoConfiguration {
    private final MongoProperties properties;
    private final MongoClientOptions options;
    private final Environment environment;
    private MongoClient mongo;

    public MongoAutoConfiguration(MongoProperties properties, ObjectProvider<MongoClientOptions> options, Environment environment) {
        this.properties = properties;
        this.options = options.getIfAvailable();
        this.environment = environment;
    }

    @PreDestroy
    public void close() {
        if (this.mongo != null) {
            this.mongo.close();
        }
    }

    @ConditionalOnMissingBean
    @Bean
    public MongoClient mongo() throws UnknownHostException {
        this.mongo = this.properties.createMongoClient(this.options, this.environment);
        return this.mongo;
    }
}
