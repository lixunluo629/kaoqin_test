package org.springframework.http.server;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/server/ServerHttpAsyncRequestControl.class */
public interface ServerHttpAsyncRequestControl {
    void start();

    void start(long j);

    boolean isStarted();

    void complete();

    boolean isCompleted();
}
