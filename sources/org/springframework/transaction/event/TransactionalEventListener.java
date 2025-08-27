package org.springframework.transaction.event;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.AliasFor;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EventListener
/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/event/TransactionalEventListener.class */
public @interface TransactionalEventListener {
    TransactionPhase phase() default TransactionPhase.AFTER_COMMIT;

    boolean fallbackExecution() default false;

    @AliasFor(annotation = EventListener.class, attribute = "classes")
    Class<?>[] value() default {};

    @AliasFor(annotation = EventListener.class, attribute = "classes")
    Class<?>[] classes() default {};

    String condition() default "";
}
