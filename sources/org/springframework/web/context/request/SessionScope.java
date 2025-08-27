package org.springframework.web.context.request;

import org.springframework.beans.factory.ObjectFactory;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/request/SessionScope.class */
public class SessionScope extends AbstractRequestAttributesScope {
    private final int scope;

    public SessionScope() {
        this.scope = 1;
    }

    public SessionScope(boolean globalSession) {
        this.scope = globalSession ? 2 : 1;
    }

    @Override // org.springframework.web.context.request.AbstractRequestAttributesScope
    protected int getScope() {
        return this.scope;
    }

    @Override // org.springframework.beans.factory.config.Scope
    public String getConversationId() {
        return RequestContextHolder.currentRequestAttributes().getSessionId();
    }

    @Override // org.springframework.web.context.request.AbstractRequestAttributesScope, org.springframework.beans.factory.config.Scope
    public Object get(String name, ObjectFactory<?> objectFactory) {
        Object obj;
        Object mutex = RequestContextHolder.currentRequestAttributes().getSessionMutex();
        synchronized (mutex) {
            obj = super.get(name, objectFactory);
        }
        return obj;
    }

    @Override // org.springframework.web.context.request.AbstractRequestAttributesScope, org.springframework.beans.factory.config.Scope
    public Object remove(String name) {
        Object objRemove;
        Object mutex = RequestContextHolder.currentRequestAttributes().getSessionMutex();
        synchronized (mutex) {
            objRemove = super.remove(name);
        }
        return objRemove;
    }
}
