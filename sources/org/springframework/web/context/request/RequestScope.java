package org.springframework.web.context.request;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/request/RequestScope.class */
public class RequestScope extends AbstractRequestAttributesScope {
    @Override // org.springframework.web.context.request.AbstractRequestAttributesScope
    protected int getScope() {
        return 0;
    }

    @Override // org.springframework.beans.factory.config.Scope
    public String getConversationId() {
        return null;
    }
}
