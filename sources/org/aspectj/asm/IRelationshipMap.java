package org.aspectj.asm;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import org.aspectj.asm.IRelationship;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/asm/IRelationshipMap.class */
public interface IRelationshipMap extends Serializable {
    List<IRelationship> get(IProgramElement iProgramElement);

    List<IRelationship> get(String str);

    IRelationship get(IProgramElement iProgramElement, IRelationship.Kind kind, String str, boolean z, boolean z2);

    IRelationship get(IProgramElement iProgramElement, IRelationship.Kind kind, String str);

    IRelationship get(String str, IRelationship.Kind kind, String str2, boolean z, boolean z2);

    void put(IProgramElement iProgramElement, IRelationship iRelationship);

    void put(String str, IRelationship iRelationship);

    boolean remove(String str, IRelationship iRelationship);

    void removeAll(String str);

    void clear();

    Set<String> getEntries();
}
