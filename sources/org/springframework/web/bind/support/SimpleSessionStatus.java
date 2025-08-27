package org.springframework.web.bind.support;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/bind/support/SimpleSessionStatus.class */
public class SimpleSessionStatus implements SessionStatus {
    private boolean complete = false;

    @Override // org.springframework.web.bind.support.SessionStatus
    public void setComplete() {
        this.complete = true;
    }

    @Override // org.springframework.web.bind.support.SessionStatus
    public boolean isComplete() {
        return this.complete;
    }
}
