package org.springframework.boot.autoconfigure.session;

import java.util.Locale;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;

@EnableConfigurationProperties({SessionProperties.class})
@AutoConfigureAfter({DataSourceAutoConfiguration.class, HazelcastAutoConfiguration.class, JdbcTemplateAutoConfiguration.class, MongoAutoConfiguration.class, RedisAutoConfiguration.class})
@ConditionalOnMissingBean({SessionRepository.class})
@ConditionalOnWebApplication
@Import({SessionConfigurationImportSelector.class, SessionRepositoryValidator.class})
@Configuration
@ConditionalOnClass({Session.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/session/SessionAutoConfiguration.class */
public class SessionAutoConfiguration {

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/session/SessionAutoConfiguration$SessionConfigurationImportSelector.class */
    static class SessionConfigurationImportSelector implements ImportSelector {
        SessionConfigurationImportSelector() {
        }

        @Override // org.springframework.context.annotation.ImportSelector
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            StoreType[] types = StoreType.values();
            String[] imports = new String[types.length];
            for (int i = 0; i < types.length; i++) {
                imports[i] = SessionStoreMappings.getConfigurationClass(types[i]);
            }
            return imports;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/session/SessionAutoConfiguration$SessionRepositoryValidator.class */
    static class SessionRepositoryValidator {
        private SessionProperties sessionProperties;
        private ObjectProvider<SessionRepository<?>> sessionRepositoryProvider;

        SessionRepositoryValidator(SessionProperties sessionProperties, ObjectProvider<SessionRepository<?>> sessionRepositoryProvider) {
            this.sessionProperties = sessionProperties;
            this.sessionRepositoryProvider = sessionRepositoryProvider;
        }

        @PostConstruct
        public void checkSessionRepository() {
            StoreType storeType = this.sessionProperties.getStoreType();
            if (storeType != StoreType.NONE && this.sessionRepositoryProvider.getIfAvailable() == null) {
                if (storeType != null) {
                    throw new IllegalArgumentException("No session repository could be auto-configured, check your configuration (session store type is '" + storeType.name().toLowerCase(Locale.ENGLISH) + "')");
                }
                throw new IllegalArgumentException("No Spring Session store is configured: set the 'spring.session.store-type' property");
            }
        }
    }
}
