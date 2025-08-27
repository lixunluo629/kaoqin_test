package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx;
import org.apache.xmlbeans.impl.xb.xsdschema.NamespaceList;
import org.apache.xmlbeans.impl.xb.xsdschema.Wildcard;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/WildcardImpl.class */
public class WildcardImpl extends AnnotatedImpl implements Wildcard {
    private static final long serialVersionUID = 1;
    private static final QName NAMESPACE$0 = new QName("", "namespace");
    private static final QName PROCESSCONTENTS$2 = new QName("", "processContents");

    public WildcardImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Wildcard
    public Object getNamespace() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(NAMESPACE$0);
            if (target == null) {
                target = (SimpleValue) get_default_attribute_value(NAMESPACE$0);
            }
            if (target == null) {
                return null;
            }
            return target.getObjectValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Wildcard
    public NamespaceList xgetNamespace() {
        NamespaceList namespaceList;
        synchronized (monitor()) {
            check_orphaned();
            NamespaceList target = (NamespaceList) get_store().find_attribute_user(NAMESPACE$0);
            if (target == null) {
                target = (NamespaceList) get_default_attribute_value(NAMESPACE$0);
            }
            namespaceList = target;
        }
        return namespaceList;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Wildcard
    public boolean isSetNamespace() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NAMESPACE$0) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Wildcard
    public void setNamespace(Object namespace) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(NAMESPACE$0);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(NAMESPACE$0);
            }
            target.setObjectValue(namespace);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Wildcard
    public void xsetNamespace(NamespaceList namespace) {
        synchronized (monitor()) {
            check_orphaned();
            NamespaceList target = (NamespaceList) get_store().find_attribute_user(NAMESPACE$0);
            if (target == null) {
                target = (NamespaceList) get_store().add_attribute_user(NAMESPACE$0);
            }
            target.set(namespace);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Wildcard
    public void unsetNamespace() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NAMESPACE$0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Wildcard
    public Wildcard.ProcessContents.Enum getProcessContents() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(PROCESSCONTENTS$2);
            if (target == null) {
                target = (SimpleValue) get_default_attribute_value(PROCESSCONTENTS$2);
            }
            if (target == null) {
                return null;
            }
            return (Wildcard.ProcessContents.Enum) target.getEnumValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Wildcard
    public Wildcard.ProcessContents xgetProcessContents() {
        Wildcard.ProcessContents processContents;
        synchronized (monitor()) {
            check_orphaned();
            Wildcard.ProcessContents target = (Wildcard.ProcessContents) get_store().find_attribute_user(PROCESSCONTENTS$2);
            if (target == null) {
                target = (Wildcard.ProcessContents) get_default_attribute_value(PROCESSCONTENTS$2);
            }
            processContents = target;
        }
        return processContents;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Wildcard
    public boolean isSetProcessContents() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(PROCESSCONTENTS$2) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Wildcard
    public void setProcessContents(Wildcard.ProcessContents.Enum processContents) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(PROCESSCONTENTS$2);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(PROCESSCONTENTS$2);
            }
            target.setEnumValue(processContents);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Wildcard
    public void xsetProcessContents(Wildcard.ProcessContents processContents) {
        synchronized (monitor()) {
            check_orphaned();
            Wildcard.ProcessContents target = (Wildcard.ProcessContents) get_store().find_attribute_user(PROCESSCONTENTS$2);
            if (target == null) {
                target = (Wildcard.ProcessContents) get_store().add_attribute_user(PROCESSCONTENTS$2);
            }
            target.set(processContents);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Wildcard
    public void unsetProcessContents() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROCESSCONTENTS$2);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/WildcardImpl$ProcessContentsImpl.class */
    public static class ProcessContentsImpl extends JavaStringEnumerationHolderEx implements Wildcard.ProcessContents {
        private static final long serialVersionUID = 1;

        public ProcessContentsImpl(SchemaType sType) {
            super(sType, false);
        }

        protected ProcessContentsImpl(SchemaType sType, boolean b) {
            super(sType, b);
        }
    }
}
