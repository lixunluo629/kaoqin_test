package org.springframework.boot.autoconfigure.session;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.session.SessionRepository;
import org.springframework.session.jdbc.config.annotation.web.http.JdbcHttpSessionConfiguration;

@Configuration
@ConditionalOnClass({JdbcTemplate.class})
@ConditionalOnMissingBean({SessionRepository.class})
@ConditionalOnBean({DataSource.class})
@Conditional({SessionCondition.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/session/JdbcSessionConfiguration.class */
class JdbcSessionConfiguration {
    JdbcSessionConfiguration() {
    }

    @ConditionalOnMissingBean
    @Bean
    public JdbcSessionDatabaseInitializer jdbcSessionDatabaseInitializer(DataSource dataSource, ResourceLoader resourceLoader, SessionProperties properties) {
        return new JdbcSessionDatabaseInitializer(dataSource, resourceLoader, properties);
    }

    @Configuration
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/session/JdbcSessionConfiguration$SpringBootJdbcHttpSessionConfiguration.class */
    public static class SpringBootJdbcHttpSessionConfiguration extends JdbcHttpSessionConfiguration {
        @Autowired
        public void customize(SessionProperties sessionProperties) {
            Integer timeout = sessionProperties.getTimeout();
            if (timeout != null) {
                setMaxInactiveIntervalInSeconds(timeout);
            }
            setTableName(sessionProperties.getJdbc().getTableName());
        }
    }
}
