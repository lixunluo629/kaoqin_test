package org.bouncycastle.asn1.x509;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/x509/NameConstraintValidator.class */
public interface NameConstraintValidator {
    void checkPermitted(GeneralName generalName) throws NameConstraintValidatorException;

    void checkExcluded(GeneralName generalName) throws NameConstraintValidatorException;

    void intersectPermittedSubtree(GeneralSubtree generalSubtree);

    void intersectPermittedSubtree(GeneralSubtree[] generalSubtreeArr);

    void intersectEmptyPermittedSubtree(int i);

    void addExcludedSubtree(GeneralSubtree generalSubtree);
}
