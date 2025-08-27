package org.hibernate.validator.internal.engine.groups;

import javax.validation.groups.Default;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/groups/Group.class */
public class Group {
    public static final Group DEFAULT_GROUP = new Group(Default.class);
    private Class<?> group;

    public Group(Class<?> group) {
        this.group = group;
    }

    public Class<?> getDefiningClass() {
        return this.group;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Group group1 = (Group) o;
        if (this.group != null) {
            if (!this.group.equals(group1.group)) {
                return false;
            }
            return true;
        }
        if (group1.group != null) {
            return false;
        }
        return true;
    }

    public boolean isDefaultGroup() {
        return getDefiningClass().getName().equals(Default.class.getName());
    }

    public int hashCode() {
        if (this.group != null) {
            return this.group.hashCode();
        }
        return 0;
    }

    public String toString() {
        return "Group{group=" + this.group.getName() + '}';
    }
}
