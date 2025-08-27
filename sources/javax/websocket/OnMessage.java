package javax.websocket;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: tomcat-embed-websocket-8.5.43.jar:javax/websocket/OnMessage.class */
public @interface OnMessage {
    long maxMessageSize() default -1;
}
