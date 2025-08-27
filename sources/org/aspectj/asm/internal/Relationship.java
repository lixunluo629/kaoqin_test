package org.aspectj.asm.internal;

import java.util.List;
import org.aspectj.asm.IRelationship;
import org.aspectj.weaver.model.AsmRelationshipProvider;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/asm/internal/Relationship.class */
public class Relationship implements IRelationship {
    private static final long serialVersionUID = 3855166397957609120L;
    private String name;
    private IRelationship.Kind kind;
    private boolean isAffects;
    private String sourceHandle;
    private List<String> targets;
    private boolean hasRuntimeTest;

    public Relationship(String name, IRelationship.Kind kind, String sourceHandle, List<String> targets, boolean runtimeTest) {
        this.name = name;
        this.isAffects = name.equals(AsmRelationshipProvider.ADVISES) || name.equals(AsmRelationshipProvider.DECLARES_ON) || name.equals(AsmRelationshipProvider.SOFTENS) || name.equals(AsmRelationshipProvider.MATCHED_BY) || name.equals(AsmRelationshipProvider.INTER_TYPE_DECLARES) || name.equals(AsmRelationshipProvider.ANNOTATES);
        this.kind = kind;
        this.sourceHandle = sourceHandle;
        this.targets = targets;
        this.hasRuntimeTest = runtimeTest;
    }

    @Override // org.aspectj.asm.IRelationship
    public String getName() {
        return this.name;
    }

    @Override // org.aspectj.asm.IRelationship
    public IRelationship.Kind getKind() {
        return this.kind;
    }

    public String toString() {
        return this.name;
    }

    @Override // org.aspectj.asm.IRelationship
    public String getSourceHandle() {
        return this.sourceHandle;
    }

    @Override // org.aspectj.asm.IRelationship
    public List<String> getTargets() {
        return this.targets;
    }

    @Override // org.aspectj.asm.IRelationship
    public void addTarget(String handle) {
        if (this.targets.contains(handle)) {
            return;
        }
        this.targets.add(handle);
    }

    @Override // org.aspectj.asm.IRelationship
    public boolean hasRuntimeTest() {
        return this.hasRuntimeTest;
    }

    @Override // org.aspectj.asm.IRelationship
    public boolean isAffects() {
        return this.isAffects;
    }
}
