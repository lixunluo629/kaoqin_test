package org.springframework.boot.autoconfigure.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.ResolvableType;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/logging/AutoConfigurationReportLoggingInitializer.class */
public class AutoConfigurationReportLoggingInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private final Log logger = LogFactory.getLog(getClass());
    private ConfigurableApplicationContext applicationContext;
    private ConditionEvaluationReport report;

    @Override // org.springframework.context.ApplicationContextInitializer
    public void initialize(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        applicationContext.addApplicationListener(new AutoConfigurationReportListener());
        if (applicationContext instanceof GenericApplicationContext) {
            this.report = ConditionEvaluationReport.get(this.applicationContext.getBeanFactory());
        }
    }

    protected void onApplicationEvent(ApplicationEvent event) {
        ConfigurableApplicationContext initializerApplicationContext = this.applicationContext;
        if (event instanceof ContextRefreshedEvent) {
            if (((ApplicationContextEvent) event).getApplicationContext() == initializerApplicationContext) {
                logAutoConfigurationReport();
            }
        } else if ((event instanceof ApplicationFailedEvent) && ((ApplicationFailedEvent) event).getApplicationContext() == initializerApplicationContext) {
            logAutoConfigurationReport(true);
        }
    }

    private void logAutoConfigurationReport() {
        logAutoConfigurationReport(!this.applicationContext.isActive());
    }

    public void logAutoConfigurationReport(boolean isCrashReport) {
        if (this.report == null) {
            if (this.applicationContext == null) {
                this.logger.info("Unable to provide auto-configuration report due to missing ApplicationContext");
                return;
            }
            this.report = ConditionEvaluationReport.get(this.applicationContext.getBeanFactory());
        }
        if (!this.report.getConditionAndOutcomesBySource().isEmpty()) {
            if (isCrashReport && this.logger.isInfoEnabled() && !this.logger.isDebugEnabled()) {
                this.logger.info(String.format("%n%nError starting ApplicationContext. To display the auto-configuration report re-run your application with 'debug' enabled.", new Object[0]));
            }
            if (this.logger.isDebugEnabled()) {
                this.logger.debug(new ConditionEvaluationReportMessage(this.report));
            }
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/logging/AutoConfigurationReportLoggingInitializer$AutoConfigurationReportListener.class */
    private class AutoConfigurationReportListener implements GenericApplicationListener {
        private AutoConfigurationReportListener() {
        }

        @Override // org.springframework.core.Ordered
        public int getOrder() {
            return Integer.MAX_VALUE;
        }

        @Override // org.springframework.context.event.GenericApplicationListener
        public boolean supportsEventType(ResolvableType resolvableType) {
            Class<?> type = resolvableType.getRawClass();
            if (type == null) {
                return false;
            }
            return ContextRefreshedEvent.class.isAssignableFrom(type) || ApplicationFailedEvent.class.isAssignableFrom(type);
        }

        @Override // org.springframework.context.event.GenericApplicationListener
        public boolean supportsSourceType(Class<?> sourceType) {
            return true;
        }

        @Override // org.springframework.context.ApplicationListener
        public void onApplicationEvent(ApplicationEvent event) {
            AutoConfigurationReportLoggingInitializer.this.onApplicationEvent(event);
        }
    }
}
