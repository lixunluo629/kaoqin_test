package org.springframework.data.auditing;

import java.util.Arrays;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mapping.context.MappingContextIsNewStrategyFactory;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.support.IsNewStrategy;
import org.springframework.data.support.IsNewStrategyFactory;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/auditing/IsNewAwareAuditingHandler.class */
public class IsNewAwareAuditingHandler extends AuditingHandler {
    private final IsNewStrategyFactory isNewStrategyFactory;

    @Deprecated
    public IsNewAwareAuditingHandler(MappingContext<? extends PersistentEntity<?, ?>, ? extends PersistentProperty<?>> mappingContext) {
        this(new PersistentEntities(Arrays.asList(mappingContext)));
    }

    public IsNewAwareAuditingHandler(PersistentEntities entities) {
        super(entities);
        this.isNewStrategyFactory = new MappingContextIsNewStrategyFactory(entities);
    }

    public void markAudited(Object object) {
        if (!isAuditable(object)) {
            return;
        }
        IsNewStrategy strategy = this.isNewStrategyFactory.getIsNewStrategy(object.getClass());
        if (strategy.isNew(object)) {
            markCreated(object);
        } else {
            markModified(object);
        }
    }
}
