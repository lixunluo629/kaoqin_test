package org.aspectj.weaver;

import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/NewParentTypeMunger.class */
public class NewParentTypeMunger extends ResolvedTypeMunger {
    ResolvedType newParent;
    ResolvedType declaringType;
    private boolean isMixin;
    private volatile int hashCode;

    public NewParentTypeMunger(ResolvedType newParent, ResolvedType declaringType) {
        super(Parent, null);
        this.hashCode = 0;
        this.newParent = newParent;
        this.declaringType = declaringType;
        this.isMixin = false;
    }

    @Override // org.aspectj.weaver.ResolvedTypeMunger
    public void write(CompressingDataOutputStream s) throws IOException {
        throw new RuntimeException("unimplemented");
    }

    public ResolvedType getNewParent() {
        return this.newParent;
    }

    public boolean equals(Object other) {
        if (!(other instanceof NewParentTypeMunger)) {
            return false;
        }
        NewParentTypeMunger o = (NewParentTypeMunger) other;
        return this.newParent.equals(o.newParent) && this.isMixin == o.isMixin;
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            int result = (37 * 17) + this.newParent.hashCode();
            this.hashCode = (37 * result) + (this.isMixin ? 0 : 1);
        }
        return this.hashCode;
    }

    @Override // org.aspectj.weaver.ResolvedTypeMunger
    public ResolvedType getDeclaringType() {
        return this.declaringType;
    }

    public void setIsMixin(boolean b) {
        this.isMixin = true;
    }

    public boolean isMixin() {
        return this.isMixin;
    }
}
