package org.springframework.boot.autoconfigure;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.validation.Validation;
import org.apache.catalina.mbeans.MBeanFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;

@Order(-2147483627)
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/BackgroundPreinitializer.class */
public class BackgroundPreinitializer implements ApplicationListener<SpringApplicationEvent> {
    private static final AtomicBoolean preinitializationStarted = new AtomicBoolean(false);
    private static final CountDownLatch preinitializationComplete = new CountDownLatch(1);

    @Override // org.springframework.context.ApplicationListener
    public void onApplicationEvent(SpringApplicationEvent event) throws InterruptedException {
        if ((event instanceof ApplicationEnvironmentPreparedEvent) && preinitializationStarted.compareAndSet(false, true)) {
            performPreinitialization();
        }
        if (((event instanceof ApplicationReadyEvent) || (event instanceof ApplicationFailedEvent)) && preinitializationStarted.get()) {
            try {
                preinitializationComplete.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void performPreinitialization() {
        try {
            Thread thread = new Thread(new Runnable() { // from class: org.springframework.boot.autoconfigure.BackgroundPreinitializer.1
                @Override // java.lang.Runnable
                public void run() {
                    runSafely(new MessageConverterInitializer());
                    runSafely(new MBeanFactoryInitializer());
                    runSafely(new ValidationInitializer());
                    runSafely(new JacksonInitializer());
                    runSafely(new ConversionServiceInitializer());
                    BackgroundPreinitializer.preinitializationComplete.countDown();
                }

                public void runSafely(Runnable runnable) {
                    try {
                        runnable.run();
                    } catch (Throwable th) {
                    }
                }
            }, "background-preinit");
            thread.start();
        } catch (Exception e) {
            preinitializationComplete.countDown();
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/BackgroundPreinitializer$MessageConverterInitializer.class */
    private static class MessageConverterInitializer implements Runnable {
        private MessageConverterInitializer() {
        }

        @Override // java.lang.Runnable
        public void run() {
            new AllEncompassingFormHttpMessageConverter();
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/BackgroundPreinitializer$MBeanFactoryInitializer.class */
    private static class MBeanFactoryInitializer implements Runnable {
        private MBeanFactoryInitializer() {
        }

        @Override // java.lang.Runnable
        public void run() {
            new MBeanFactory();
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/BackgroundPreinitializer$ValidationInitializer.class */
    private static class ValidationInitializer implements Runnable {
        private ValidationInitializer() {
        }

        @Override // java.lang.Runnable
        public void run() {
            Validation.byDefaultProvider().configure();
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/BackgroundPreinitializer$JacksonInitializer.class */
    private static class JacksonInitializer implements Runnable {
        private JacksonInitializer() {
        }

        @Override // java.lang.Runnable
        public void run() {
            Jackson2ObjectMapperBuilder.json().build();
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/BackgroundPreinitializer$ConversionServiceInitializer.class */
    private static class ConversionServiceInitializer implements Runnable {
        private ConversionServiceInitializer() {
        }

        @Override // java.lang.Runnable
        public void run() {
            new DefaultFormattingConversionService();
        }
    }
}
