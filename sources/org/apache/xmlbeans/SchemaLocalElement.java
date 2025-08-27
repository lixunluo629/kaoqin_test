package org.apache.xmlbeans;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/SchemaLocalElement.class */
public interface SchemaLocalElement extends SchemaField, SchemaAnnotated {
    boolean blockExtension();

    boolean blockRestriction();

    boolean blockSubstitution();

    boolean isAbstract();

    SchemaIdentityConstraint[] getIdentityConstraints();
}
