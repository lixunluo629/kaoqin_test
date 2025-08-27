package org.hibernate.validator.internal.engine.groups;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/groups/GroupWithInheritance.class */
public class GroupWithInheritance implements Iterable<Group> {
    private final Set<Group> groups;

    public GroupWithInheritance(Set<Group> groups) {
        this.groups = Collections.unmodifiableSet(groups);
    }

    @Override // java.lang.Iterable
    public Iterator<Group> iterator() {
        return this.groups.iterator();
    }
}
