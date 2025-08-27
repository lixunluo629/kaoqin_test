package org.apache.xmlbeans;

import java.util.Set;
import javax.xml.namespace.QName;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/QNameSetSpecification.class */
public interface QNameSetSpecification {
    boolean contains(QName qName);

    boolean isAll();

    boolean isEmpty();

    boolean containsAll(QNameSetSpecification qNameSetSpecification);

    boolean isDisjoint(QNameSetSpecification qNameSetSpecification);

    QNameSet intersect(QNameSetSpecification qNameSetSpecification);

    QNameSet union(QNameSetSpecification qNameSetSpecification);

    QNameSet inverse();

    Set excludedURIs();

    Set includedURIs();

    Set excludedQNamesInIncludedURIs();

    Set includedQNamesInExcludedURIs();
}
