package org.springframework.data.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.data.annotation.Transient;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/AbstractAggregateRoot.class */
public class AbstractAggregateRoot {

    @Transient
    private final transient List<Object> domainEvents = new ArrayList();

    protected <T> T registerEvent(T event) {
        Assert.notNull(event, "Domain event must not be null!");
        this.domainEvents.add(event);
        return event;
    }

    @AfterDomainEventPublication
    protected void clearDomainEvents() {
        this.domainEvents.clear();
    }

    @DomainEvents
    protected Collection<Object> domainEvents() {
        return Collections.unmodifiableList(this.domainEvents);
    }

    @JsonIgnore
    @Deprecated
    public List<Object> getDomainEvents() {
        return (List) domainEvents();
    }
}
