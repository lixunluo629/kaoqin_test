package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx;
import org.apache.xmlbeans.impl.values.XmlListImpl;
import org.apache.xmlbeans.impl.values.XmlUnionImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.NamespaceList;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/NamespaceListImpl.class */
public class NamespaceListImpl extends XmlUnionImpl implements NamespaceList, NamespaceList.Member, NamespaceList.Member2 {
    private static final long serialVersionUID = 1;

    public NamespaceListImpl(SchemaType sType) {
        super(sType, false);
    }

    protected NamespaceListImpl(SchemaType sType, boolean b) {
        super(sType, b);
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/NamespaceListImpl$MemberImpl.class */
    public static class MemberImpl extends JavaStringEnumerationHolderEx implements NamespaceList.Member {
        private static final long serialVersionUID = 1;

        public MemberImpl(SchemaType sType) {
            super(sType, false);
        }

        protected MemberImpl(SchemaType sType, boolean b) {
            super(sType, b);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/NamespaceListImpl$MemberImpl2.class */
    public static class MemberImpl2 extends XmlListImpl implements NamespaceList.Member2 {
        private static final long serialVersionUID = 1;

        public MemberImpl2(SchemaType sType) {
            super(sType, false);
        }

        protected MemberImpl2(SchemaType sType, boolean b) {
            super(sType, b);
        }

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/NamespaceListImpl$MemberImpl2$ItemImpl.class */
        public static class ItemImpl extends XmlUnionImpl implements NamespaceList.Member2.Item, XmlAnyURI, NamespaceList.Member2.Item.Member {
            private static final long serialVersionUID = 1;

            public ItemImpl(SchemaType sType) {
                super(sType, false);
            }

            protected ItemImpl(SchemaType sType, boolean b) {
                super(sType, b);
            }

            /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/NamespaceListImpl$MemberImpl2$ItemImpl$MemberImpl.class */
            public static class MemberImpl extends JavaStringEnumerationHolderEx implements NamespaceList.Member2.Item.Member {
                private static final long serialVersionUID = 1;

                public MemberImpl(SchemaType sType) {
                    super(sType, false);
                }

                protected MemberImpl(SchemaType sType, boolean b) {
                    super(sType, b);
                }
            }
        }
    }
}
