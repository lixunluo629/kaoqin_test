package org.springframework.boot.autoconfigure.data.mongo;

import com.mongodb.MongoClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.MongoRepositoryConfigurationExtension;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean;

@Configuration
@ConditionalOnClass({MongoClient.class, MongoRepository.class})
@AutoConfigureAfter({MongoDataAutoConfiguration.class})
@ConditionalOnMissingBean({MongoRepositoryFactoryBean.class, MongoRepositoryConfigurationExtension.class})
@ConditionalOnProperty(prefix = "spring.data.mongodb.repositories", name = {"enabled"}, havingValue = "true", matchIfMissing = true)
@Import({MongoRepositoriesAutoConfigureRegistrar.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/data/mongo/MongoRepositoriesAutoConfiguration.class */
public class MongoRepositoriesAutoConfiguration {
}
