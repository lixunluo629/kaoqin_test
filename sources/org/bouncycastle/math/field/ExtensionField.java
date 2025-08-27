package org.bouncycastle.math.field;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/field/ExtensionField.class */
public interface ExtensionField extends FiniteField {
    FiniteField getSubfield();

    int getDegree();
}
