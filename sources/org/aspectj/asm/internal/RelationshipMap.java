package org.aspectj.asm.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.aspectj.asm.IProgramElement;
import org.aspectj.asm.IRelationship;
import org.aspectj.asm.IRelationshipMap;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/asm/internal/RelationshipMap.class */
public class RelationshipMap extends HashMap<String, List<IRelationship>> implements IRelationshipMap {
    private static final long serialVersionUID = 496638323566589643L;

    @Override // org.aspectj.asm.IRelationshipMap
    public List<IRelationship> get(String handle) {
        List<IRelationship> relationships = (List) super.get((Object) handle);
        if (relationships == null) {
            return null;
        }
        return relationships;
    }

    @Override // org.aspectj.asm.IRelationshipMap
    public List<IRelationship> get(IProgramElement source) {
        return get(source.getHandleIdentifier());
    }

    @Override // org.aspectj.asm.IRelationshipMap
    public IRelationship get(String source, IRelationship.Kind kind, String relationshipName, boolean runtimeTest, boolean createIfMissing) {
        List<IRelationship> relationships = get(source);
        if (relationships == null) {
            if (!createIfMissing) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            Relationship relationship = new Relationship(relationshipName, kind, source, new ArrayList(), runtimeTest);
            arrayList.add(relationship);
            super.put((RelationshipMap) source, (String) arrayList);
            return relationship;
        }
        for (IRelationship curr : relationships) {
            if (curr.getKind() == kind && curr.getName().equals(relationshipName) && curr.hasRuntimeTest() == runtimeTest) {
                return curr;
            }
        }
        if (createIfMissing) {
            IRelationship rel = new Relationship(relationshipName, kind, source, new ArrayList(), runtimeTest);
            relationships.add(rel);
            return rel;
        }
        return null;
    }

    @Override // org.aspectj.asm.IRelationshipMap
    public IRelationship get(IProgramElement source, IRelationship.Kind kind, String relationshipName, boolean runtimeTest, boolean createIfMissing) {
        return get(source.getHandleIdentifier(), kind, relationshipName, runtimeTest, createIfMissing);
    }

    @Override // org.aspectj.asm.IRelationshipMap
    public IRelationship get(IProgramElement source, IRelationship.Kind kind, String relationshipName) {
        return get(source, kind, relationshipName, false, true);
    }

    @Override // org.aspectj.asm.IRelationshipMap
    public boolean remove(String source, IRelationship relationship) {
        List<IRelationship> list = (List) super.get((Object) source);
        if (list != null) {
            return list.remove(relationship);
        }
        return false;
    }

    @Override // org.aspectj.asm.IRelationshipMap
    public void removeAll(String source) {
        super.remove(source);
    }

    @Override // org.aspectj.asm.IRelationshipMap
    public void put(String source, IRelationship relationship) {
        List<IRelationship> existingRelationships = (List) super.get((Object) source);
        if (existingRelationships == null) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(relationship);
            super.put((RelationshipMap) source, (String) arrayList);
            return;
        }
        boolean matched = false;
        for (IRelationship existingRelationship : existingRelationships) {
            if (existingRelationship.getName().equals(relationship.getName()) && existingRelationship.getKind() == relationship.getKind()) {
                existingRelationship.getTargets().addAll(relationship.getTargets());
                matched = true;
            }
        }
        if (matched) {
            System.err.println("matched = true");
        }
        if (matched) {
            existingRelationships.add(relationship);
        }
    }

    @Override // org.aspectj.asm.IRelationshipMap
    public void put(IProgramElement source, IRelationship relationship) {
        put(source.getHandleIdentifier(), relationship);
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map, org.aspectj.asm.IRelationshipMap
    public void clear() {
        super.clear();
    }

    @Override // org.aspectj.asm.IRelationshipMap
    public Set<String> getEntries() {
        return keySet();
    }
}
