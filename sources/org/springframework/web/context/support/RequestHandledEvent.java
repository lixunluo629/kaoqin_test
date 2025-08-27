package org.springframework.web.context.support;

import org.springframework.context.ApplicationEvent;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/support/RequestHandledEvent.class */
public class RequestHandledEvent extends ApplicationEvent {
    private String sessionId;
    private String userName;
    private final long processingTimeMillis;
    private Throwable failureCause;

    public RequestHandledEvent(Object source, String sessionId, String userName, long processingTimeMillis) {
        super(source);
        this.sessionId = sessionId;
        this.userName = userName;
        this.processingTimeMillis = processingTimeMillis;
    }

    public RequestHandledEvent(Object source, String sessionId, String userName, long processingTimeMillis, Throwable failureCause) {
        this(source, sessionId, userName, processingTimeMillis);
        this.failureCause = failureCause;
    }

    public long getProcessingTimeMillis() {
        return this.processingTimeMillis;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public String getUserName() {
        return this.userName;
    }

    public boolean wasFailure() {
        return this.failureCause != null;
    }

    public Throwable getFailureCause() {
        return this.failureCause;
    }

    public String getShortDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("session=[").append(this.sessionId).append("]; ");
        sb.append("user=[").append(this.userName).append("]; ");
        return sb.toString();
    }

    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("session=[").append(this.sessionId).append("]; ");
        sb.append("user=[").append(this.userName).append("]; ");
        sb.append("time=[").append(this.processingTimeMillis).append("ms]; ");
        sb.append("status=[");
        if (!wasFailure()) {
            sb.append("OK");
        } else {
            sb.append("failed: ").append(this.failureCause);
        }
        sb.append(']');
        return sb.toString();
    }

    @Override // java.util.EventObject
    public String toString() {
        return "RequestHandledEvent: " + getDescription();
    }
}
