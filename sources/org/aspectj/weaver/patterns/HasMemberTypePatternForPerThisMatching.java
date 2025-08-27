package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.List;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ConcreteTypeMunger;
import org.aspectj.weaver.ResolvedType;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/HasMemberTypePatternForPerThisMatching.class */
public class HasMemberTypePatternForPerThisMatching extends HasMemberTypePattern {
    public HasMemberTypePatternForPerThisMatching(SignaturePattern aSignaturePattern) {
        super(aSignaturePattern);
    }

    @Override // org.aspectj.weaver.patterns.HasMemberTypePattern
    protected boolean hasMethod(ResolvedType type) {
        boolean b = super.hasMethod(type);
        if (b) {
            return true;
        }
        List<ConcreteTypeMunger> mungers = type.getInterTypeMungersIncludingSupers();
        if (mungers.size() != 0) {
            return true;
        }
        return false;
    }

    @Override // org.aspectj.weaver.patterns.HasMemberTypePattern, org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        throw new IllegalAccessError("Should never be called, these are transient and don't get serialized");
    }
}
