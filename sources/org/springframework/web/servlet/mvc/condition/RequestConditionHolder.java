package org.springframework.web.servlet.mvc.condition;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Collection;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/condition/RequestConditionHolder.class */
public final class RequestConditionHolder extends AbstractRequestCondition<RequestConditionHolder> {
    private final RequestCondition<Object> condition;

    public RequestConditionHolder(RequestCondition<?> requestCondition) {
        this.condition = requestCondition;
    }

    public RequestCondition<?> getCondition() {
        return this.condition;
    }

    @Override // org.springframework.web.servlet.mvc.condition.AbstractRequestCondition
    protected Collection<?> getContent() {
        return this.condition != null ? Collections.singleton(this.condition) : Collections.emptyList();
    }

    @Override // org.springframework.web.servlet.mvc.condition.AbstractRequestCondition
    protected String getToStringInfix() {
        return SymbolConstants.SPACE_SYMBOL;
    }

    @Override // org.springframework.web.servlet.mvc.condition.RequestCondition
    public RequestConditionHolder combine(RequestConditionHolder other) {
        if (this.condition == null && other.condition == null) {
            return this;
        }
        if (this.condition == null) {
            return other;
        }
        if (other.condition == null) {
            return this;
        }
        assertEqualConditionTypes(other);
        RequestCondition<?> combined = (RequestCondition) this.condition.combine(other.condition);
        return new RequestConditionHolder(combined);
    }

    private void assertEqualConditionTypes(RequestConditionHolder other) {
        Class<?> clazz = this.condition.getClass();
        Class<?> otherClazz = other.condition.getClass();
        if (!clazz.equals(otherClazz)) {
            throw new ClassCastException("Incompatible request conditions: " + clazz + " and " + otherClazz);
        }
    }

    @Override // org.springframework.web.servlet.mvc.condition.RequestCondition
    public RequestConditionHolder getMatchingCondition(HttpServletRequest request) {
        if (this.condition == null) {
            return this;
        }
        RequestCondition<?> match = (RequestCondition) this.condition.getMatchingCondition(request);
        if (match != null) {
            return new RequestConditionHolder(match);
        }
        return null;
    }

    @Override // org.springframework.web.servlet.mvc.condition.RequestCondition
    public int compareTo(RequestConditionHolder other, HttpServletRequest request) {
        if (this.condition == null && other.condition == null) {
            return 0;
        }
        if (this.condition == null) {
            return 1;
        }
        if (other.condition == null) {
            return -1;
        }
        assertEqualConditionTypes(other);
        return this.condition.compareTo(other.condition, request);
    }
}
