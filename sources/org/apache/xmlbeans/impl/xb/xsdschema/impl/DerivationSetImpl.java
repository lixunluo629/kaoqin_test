package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx;
import org.apache.xmlbeans.impl.values.XmlListImpl;
import org.apache.xmlbeans.impl.values.XmlUnionImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.DerivationSet;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/DerivationSetImpl.class */
public class DerivationSetImpl extends XmlUnionImpl implements DerivationSet, DerivationSet.Member, DerivationSet.Member2 {
    private static final long serialVersionUID = 1;

    public DerivationSetImpl(SchemaType sType) {
        super(sType, false);
    }

    protected DerivationSetImpl(SchemaType sType, boolean b) {
        super(sType, b);
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/DerivationSetImpl$MemberImpl.class */
    public static class MemberImpl extends JavaStringEnumerationHolderEx implements DerivationSet.Member {
        private static final long serialVersionUID = 1;

        public MemberImpl(SchemaType sType) {
            super(sType, false);
        }

        protected MemberImpl(SchemaType sType, boolean b) {
            super(sType, b);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/DerivationSetImpl$MemberImpl2.class */
    public static class MemberImpl2 extends XmlListImpl implements DerivationSet.Member2 {
        private static final long serialVersionUID = 1;

        public MemberImpl2(SchemaType sType) {
            super(sType, false);
        }

        protected MemberImpl2(SchemaType sType, boolean b) {
            super(sType, b);
        }
    }
}
