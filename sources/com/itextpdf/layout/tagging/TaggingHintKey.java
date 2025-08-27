package com.itextpdf.layout.tagging;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/tagging/TaggingHintKey.class */
public final class TaggingHintKey {
    private IAccessibleElement elem;
    private boolean isArtifact;
    private boolean isFinished;
    private String overriddenRole;
    private boolean elementBasedFinishingOnly;

    TaggingHintKey(IAccessibleElement elem, boolean createdElementBased) {
        this.elem = elem;
        this.elementBasedFinishingOnly = createdElementBased;
    }

    public IAccessibleElement getAccessibleElement() {
        return this.elem;
    }

    boolean isFinished() {
        return this.isFinished;
    }

    void setFinished() {
        this.isFinished = true;
    }

    boolean isArtifact() {
        return this.isArtifact;
    }

    void setArtifact() {
        this.isArtifact = true;
    }

    String getOverriddenRole() {
        return this.overriddenRole;
    }

    void setOverriddenRole(String overriddenRole) {
        this.overriddenRole = overriddenRole;
    }

    boolean isElementBasedFinishingOnly() {
        return this.elementBasedFinishingOnly;
    }
}
