package org.apache.xmlbeans.impl.values;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlLanguage;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/XmlLanguageImpl.class */
public class XmlLanguageImpl extends JavaStringHolderEx implements XmlLanguage {
    public XmlLanguageImpl() {
        super(XmlLanguage.type, false);
    }

    public XmlLanguageImpl(SchemaType type, boolean complex) {
        super(type, complex);
    }
}
