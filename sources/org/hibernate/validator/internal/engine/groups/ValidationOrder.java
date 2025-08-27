package org.hibernate.validator.internal.engine.groups;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.validation.GroupDefinitionException;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/groups/ValidationOrder.class */
public interface ValidationOrder {
    public static final ValidationOrder DEFAULT_SEQUENCE = new DefaultValidationOrder();

    Iterator<Group> getGroupIterator();

    Iterator<Sequence> getSequenceIterator();

    void assertDefaultGroupSequenceIsExpandable(List<Class<?>> list) throws GroupDefinitionException;

    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/groups/ValidationOrder$DefaultValidationOrder.class */
    public static class DefaultValidationOrder implements ValidationOrder {
        private final List<Sequence> defaultSequences;

        private DefaultValidationOrder() {
            this.defaultSequences = Collections.singletonList(Sequence.DEFAULT);
        }

        @Override // org.hibernate.validator.internal.engine.groups.ValidationOrder
        public Iterator<Group> getGroupIterator() {
            return Collections.emptyList().iterator();
        }

        @Override // org.hibernate.validator.internal.engine.groups.ValidationOrder
        public Iterator<Sequence> getSequenceIterator() {
            return this.defaultSequences.iterator();
        }

        @Override // org.hibernate.validator.internal.engine.groups.ValidationOrder
        public void assertDefaultGroupSequenceIsExpandable(List<Class<?>> defaultGroupSequence) throws GroupDefinitionException {
        }
    }
}
