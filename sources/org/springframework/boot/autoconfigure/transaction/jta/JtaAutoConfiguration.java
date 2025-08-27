package org.springframework.boot.autoconfigure.transaction.jta;

import javax.transaction.Transaction;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@AutoConfigureBefore({XADataSourceAutoConfiguration.class, ActiveMQAutoConfiguration.class, ArtemisAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableConfigurationProperties({JtaProperties.class})
@ConditionalOnClass({Transaction.class})
@ConditionalOnProperty(prefix = "spring.jta", value = {"enabled"}, matchIfMissing = true)
@Import({JndiJtaConfiguration.class, BitronixJtaConfiguration.class, AtomikosJtaConfiguration.class, NarayanaJtaConfiguration.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/transaction/jta/JtaAutoConfiguration.class */
public class JtaAutoConfiguration {
}
