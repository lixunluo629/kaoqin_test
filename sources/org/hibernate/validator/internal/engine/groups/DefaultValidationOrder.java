package org.hibernate.validator.internal.engine.groups;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.validation.GroupDefinitionException;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/groups/DefaultValidationOrder.class */
public final class DefaultValidationOrder implements ValidationOrder {
    private static final Log log = LoggerFactory.make();
    private List<Group> groupList = CollectionHelper.newArrayList();
    private Map<Class<?>, Sequence> sequenceMap = CollectionHelper.newHashMap();

    @Override // org.hibernate.validator.internal.engine.groups.ValidationOrder
    public Iterator<Group> getGroupIterator() {
        return this.groupList.iterator();
    }

    @Override // org.hibernate.validator.internal.engine.groups.ValidationOrder
    public Iterator<Sequence> getSequenceIterator() {
        return this.sequenceMap.values().iterator();
    }

    public void insertGroup(Group group) {
        if (!this.groupList.contains(group)) {
            this.groupList.add(group);
        }
    }

    public void insertSequence(Sequence sequence) {
        if (sequence != null && !this.sequenceMap.containsKey(sequence.getDefiningClass())) {
            this.sequenceMap.put(sequence.getDefiningClass(), sequence);
        }
    }

    public String toString() {
        return "ValidationOrder{groupList=" + this.groupList + ", sequenceMap=" + this.sequenceMap + '}';
    }

    @Override // org.hibernate.validator.internal.engine.groups.ValidationOrder
    public void assertDefaultGroupSequenceIsExpandable(List<Class<?>> defaultGroupSequence) throws GroupDefinitionException {
        for (Map.Entry<Class<?>, Sequence> entry : this.sequenceMap.entrySet()) {
            List<Group> sequenceGroups = entry.getValue().getComposingGroups();
            int defaultGroupIndex = sequenceGroups.indexOf(Group.DEFAULT_GROUP);
            if (defaultGroupIndex != -1) {
                List<Group> defaultGroupList = buildTempGroupList(defaultGroupSequence);
                ensureDefaultGroupSequenceIsExpandable(sequenceGroups, defaultGroupList, defaultGroupIndex);
            }
        }
    }

    private void ensureDefaultGroupSequenceIsExpandable(List<Group> groupList, List<Group> defaultGroupList, int defaultGroupIndex) {
        int index;
        for (int i = 0; i < defaultGroupList.size(); i++) {
            Group group = defaultGroupList.get(i);
            if (!Group.DEFAULT_GROUP.equals(group) && (index = groupList.indexOf(group)) != -1 && ((i != 0 || index != defaultGroupIndex - 1) && (i != defaultGroupList.size() - 1 || index != defaultGroupIndex + 1))) {
                throw log.getUnableToExpandDefaultGroupListException(defaultGroupList, groupList);
            }
        }
    }

    private List<Group> buildTempGroupList(List<Class<?>> defaultGroupSequence) {
        List<Group> groups = new ArrayList<>();
        for (Class<?> clazz : defaultGroupSequence) {
            Group g = new Group(clazz);
            groups.add(g);
        }
        return groups;
    }
}
