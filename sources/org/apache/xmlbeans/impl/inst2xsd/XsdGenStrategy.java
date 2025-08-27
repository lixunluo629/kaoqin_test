package org.apache.xmlbeans.impl.inst2xsd;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.inst2xsd.util.TypeSystemHolder;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/inst2xsd/XsdGenStrategy.class */
public interface XsdGenStrategy {
    void processDoc(XmlObject[] xmlObjectArr, Inst2XsdOptions inst2XsdOptions, TypeSystemHolder typeSystemHolder);
}
