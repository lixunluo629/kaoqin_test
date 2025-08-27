package org.hibernate.validator.internal.engine.groups;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.validation.GroupSequence;
import javax.validation.groups.Default;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/groups/ValidationOrderGenerator.class */
public class ValidationOrderGenerator {
    private static final Log log = LoggerFactory.make();
    private final ConcurrentMap<Class<?>, Sequence> resolvedSequences = new ConcurrentHashMap();
    private final DefaultValidationOrder validationOrderForDefaultGroup = new DefaultValidationOrder();

    public ValidationOrderGenerator() {
        this.validationOrderForDefaultGroup.insertGroup(Group.DEFAULT_GROUP);
    }

    public ValidationOrder getValidationOrder(Class<?> group, boolean expand) {
        if (expand) {
            return getValidationOrder(Arrays.asList(group));
        }
        DefaultValidationOrder validationOrder = new DefaultValidationOrder();
        validationOrder.insertGroup(new Group(group));
        return validationOrder;
    }

    public ValidationOrder getValidationOrder(Collection<Class<?>> groups) {
        if (groups == null || groups.size() == 0) {
            throw log.getAtLeastOneGroupHasToBeSpecifiedException();
        }
        if (groups.size() == 1 && groups.contains(Default.class)) {
            return this.validationOrderForDefaultGroup;
        }
        for (Class<?> clazz : groups) {
            if (!clazz.isInterface()) {
                throw log.getGroupHasToBeAnInterfaceException(clazz.getName());
            }
        }
        DefaultValidationOrder validationOrder = new DefaultValidationOrder();
        for (Class<?> clazz2 : groups) {
            if (Default.class.equals(clazz2)) {
                validationOrder.insertGroup(Group.DEFAULT_GROUP);
            } else if (isGroupSequence(clazz2)) {
                insertSequence(clazz2, ((GroupSequence) clazz2.getAnnotation(GroupSequence.class)).value(), true, validationOrder);
            } else {
                Group group = new Group(clazz2);
                validationOrder.insertGroup(group);
                insertInheritedGroups(clazz2, validationOrder);
            }
        }
        return validationOrder;
    }

    public ValidationOrder getDefaultValidationOrder(Class<?> clazz, List<Class<?>> defaultGroupSequence) {
        DefaultValidationOrder validationOrder = new DefaultValidationOrder();
        insertSequence(clazz, (Class[]) defaultGroupSequence.toArray(new Class[0]), false, validationOrder);
        return validationOrder;
    }

    private boolean isGroupSequence(Class<?> clazz) {
        return clazz.getAnnotation(GroupSequence.class) != null;
    }

    private void insertInheritedGroups(Class<?> clazz, DefaultValidationOrder chain) {
        for (Class<?> inheritedGroup : clazz.getInterfaces()) {
            Group group = new Group(inheritedGroup);
            chain.insertGroup(group);
            insertInheritedGroups(inheritedGroup, chain);
        }
    }

    private void insertSequence(Class<?> sequenceClass, Class<?>[] sequenceElements, boolean cache, DefaultValidationOrder validationOrder) {
        Sequence cachedResolvedSequence;
        Sequence sequence = cache ? this.resolvedSequences.get(sequenceClass) : null;
        if (sequence == null) {
            sequence = resolveSequence(sequenceClass, sequenceElements, new ArrayList());
            sequence.expandInheritedGroups();
            if (cache && (cachedResolvedSequence = this.resolvedSequences.putIfAbsent(sequenceClass, sequence)) != null) {
                sequence = cachedResolvedSequence;
            }
        }
        validationOrder.insertSequence(sequence);
    }

    private Sequence resolveSequence(Class<?> sequenceClass, Class<?>[] sequenceElements, List<Class<?>> processedSequences) {
        if (processedSequences.contains(sequenceClass)) {
            throw log.getCyclicDependencyInGroupsDefinitionException();
        }
        processedSequences.add(sequenceClass);
        List<Group> resolvedSequenceGroups = new ArrayList<>();
        for (Class<?> clazz : sequenceElements) {
            if (isGroupSequence(clazz)) {
                Sequence tmpSequence = resolveSequence(clazz, ((GroupSequence) clazz.getAnnotation(GroupSequence.class)).value(), processedSequences);
                addGroups(resolvedSequenceGroups, tmpSequence.getComposingGroups());
            } else {
                List<Group> list = new ArrayList<>();
                list.add(new Group(clazz));
                addGroups(resolvedSequenceGroups, list);
            }
        }
        return new Sequence(sequenceClass, resolvedSequenceGroups);
    }

    private void addGroups(List<Group> resolvedGroupSequence, List<Group> groups) {
        for (Group tmpGroup : groups) {
            if (resolvedGroupSequence.contains(tmpGroup) && resolvedGroupSequence.indexOf(tmpGroup) < resolvedGroupSequence.size() - 1) {
                throw log.getUnableToExpandGroupSequenceException();
            }
            resolvedGroupSequence.add(tmpGroup);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ValidationOrderGenerator");
        sb.append("{resolvedSequences=").append(this.resolvedSequences);
        sb.append('}');
        return sb.toString();
    }
}
