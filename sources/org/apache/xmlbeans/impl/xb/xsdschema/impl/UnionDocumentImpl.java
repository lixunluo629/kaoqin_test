package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.values.XmlListImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.LocalSimpleType;
import org.apache.xmlbeans.impl.xb.xsdschema.UnionDocument;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/UnionDocumentImpl.class */
public class UnionDocumentImpl extends XmlComplexContentImpl implements UnionDocument {
    private static final long serialVersionUID = 1;
    private static final QName UNION$0 = new QName("http://www.w3.org/2001/XMLSchema", XmlErrorCodes.UNION);

    public UnionDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.UnionDocument
    public UnionDocument.Union getUnion() {
        synchronized (monitor()) {
            check_orphaned();
            UnionDocument.Union target = (UnionDocument.Union) get_store().find_element_user(UNION$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.UnionDocument
    public void setUnion(UnionDocument.Union union) {
        generatedSetterHelperImpl(union, UNION$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.UnionDocument
    public UnionDocument.Union addNewUnion() {
        UnionDocument.Union target;
        synchronized (monitor()) {
            check_orphaned();
            target = (UnionDocument.Union) get_store().add_element_user(UNION$0);
        }
        return target;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/UnionDocumentImpl$UnionImpl.class */
    public static class UnionImpl extends AnnotatedImpl implements UnionDocument.Union {
        private static final long serialVersionUID = 1;
        private static final QName SIMPLETYPE$0 = new QName("http://www.w3.org/2001/XMLSchema", "simpleType");
        private static final QName MEMBERTYPES$2 = new QName("", "memberTypes");

        public UnionImpl(SchemaType sType) {
            super(sType);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.UnionDocument.Union
        public LocalSimpleType[] getSimpleTypeArray() {
            LocalSimpleType[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(SIMPLETYPE$0, targetList);
                result = new LocalSimpleType[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.UnionDocument.Union
        public LocalSimpleType getSimpleTypeArray(int i) {
            LocalSimpleType target;
            synchronized (monitor()) {
                check_orphaned();
                target = (LocalSimpleType) get_store().find_element_user(SIMPLETYPE$0, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.UnionDocument.Union
        public int sizeOfSimpleTypeArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(SIMPLETYPE$0);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.UnionDocument.Union
        public void setSimpleTypeArray(LocalSimpleType[] simpleTypeArray) {
            check_orphaned();
            arraySetterHelper(simpleTypeArray, SIMPLETYPE$0);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.UnionDocument.Union
        public void setSimpleTypeArray(int i, LocalSimpleType simpleType) {
            generatedSetterHelperImpl(simpleType, SIMPLETYPE$0, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.UnionDocument.Union
        public LocalSimpleType insertNewSimpleType(int i) {
            LocalSimpleType target;
            synchronized (monitor()) {
                check_orphaned();
                target = (LocalSimpleType) get_store().insert_element_user(SIMPLETYPE$0, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.UnionDocument.Union
        public LocalSimpleType addNewSimpleType() {
            LocalSimpleType target;
            synchronized (monitor()) {
                check_orphaned();
                target = (LocalSimpleType) get_store().add_element_user(SIMPLETYPE$0);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.UnionDocument.Union
        public void removeSimpleType(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(SIMPLETYPE$0, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.UnionDocument.Union
        public List getMemberTypes() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(MEMBERTYPES$2);
                if (target == null) {
                    return null;
                }
                return target.getListValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.UnionDocument.Union
        public UnionDocument.Union.MemberTypes xgetMemberTypes() {
            UnionDocument.Union.MemberTypes target;
            synchronized (monitor()) {
                check_orphaned();
                target = (UnionDocument.Union.MemberTypes) get_store().find_attribute_user(MEMBERTYPES$2);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.UnionDocument.Union
        public boolean isSetMemberTypes() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().find_attribute_user(MEMBERTYPES$2) != null;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.UnionDocument.Union
        public void setMemberTypes(List memberTypes) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(MEMBERTYPES$2);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(MEMBERTYPES$2);
                }
                target.setListValue(memberTypes);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.UnionDocument.Union
        public void xsetMemberTypes(UnionDocument.Union.MemberTypes memberTypes) {
            synchronized (monitor()) {
                check_orphaned();
                UnionDocument.Union.MemberTypes target = (UnionDocument.Union.MemberTypes) get_store().find_attribute_user(MEMBERTYPES$2);
                if (target == null) {
                    target = (UnionDocument.Union.MemberTypes) get_store().add_attribute_user(MEMBERTYPES$2);
                }
                target.set(memberTypes);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.UnionDocument.Union
        public void unsetMemberTypes() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_attribute(MEMBERTYPES$2);
            }
        }

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/UnionDocumentImpl$UnionImpl$MemberTypesImpl.class */
        public static class MemberTypesImpl extends XmlListImpl implements UnionDocument.Union.MemberTypes {
            private static final long serialVersionUID = 1;

            public MemberTypesImpl(SchemaType sType) {
                super(sType, false);
            }

            protected MemberTypesImpl(SchemaType sType, boolean b) {
                super(sType, b);
            }
        }
    }
}
