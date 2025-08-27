package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx;
import org.apache.xmlbeans.impl.values.XmlListImpl;
import org.apache.xmlbeans.impl.values.XmlUnionImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.BlockSet;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/BlockSetImpl.class */
public class BlockSetImpl extends XmlUnionImpl implements BlockSet, BlockSet.Member, BlockSet.Member2 {
    private static final long serialVersionUID = 1;

    public BlockSetImpl(SchemaType sType) {
        super(sType, false);
    }

    protected BlockSetImpl(SchemaType sType, boolean b) {
        super(sType, b);
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/BlockSetImpl$MemberImpl.class */
    public static class MemberImpl extends JavaStringEnumerationHolderEx implements BlockSet.Member {
        private static final long serialVersionUID = 1;

        public MemberImpl(SchemaType sType) {
            super(sType, false);
        }

        protected MemberImpl(SchemaType sType, boolean b) {
            super(sType, b);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/BlockSetImpl$MemberImpl2.class */
    public static class MemberImpl2 extends XmlListImpl implements BlockSet.Member2 {
        private static final long serialVersionUID = 1;

        public MemberImpl2(SchemaType sType) {
            super(sType, false);
        }

        protected MemberImpl2(SchemaType sType, boolean b) {
            super(sType, b);
        }

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/BlockSetImpl$MemberImpl2$ItemImpl.class */
        public static class ItemImpl extends JavaStringEnumerationHolderEx implements BlockSet.Member2.Item {
            private static final long serialVersionUID = 1;

            public ItemImpl(SchemaType sType) {
                super(sType, false);
            }

            protected ItemImpl(SchemaType sType, boolean b) {
                super(sType, b);
            }
        }
    }
}
