package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlNonNegativeInteger;
import org.apache.xmlbeans.impl.values.JavaIntegerHolderEx;
import org.apache.xmlbeans.impl.values.XmlUnionImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.All;
import org.apache.xmlbeans.impl.xb.xsdschema.AllNNI;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/AllImpl.class */
public class AllImpl extends ExplicitGroupImpl implements All {
    private static final long serialVersionUID = 1;

    public AllImpl(SchemaType sType) {
        super(sType);
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/AllImpl$MinOccursImpl.class */
    public static class MinOccursImpl extends JavaIntegerHolderEx implements All.MinOccurs {
        private static final long serialVersionUID = 1;

        public MinOccursImpl(SchemaType sType) {
            super(sType, false);
        }

        protected MinOccursImpl(SchemaType sType, boolean b) {
            super(sType, b);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/AllImpl$MaxOccursImpl.class */
    public static class MaxOccursImpl extends XmlUnionImpl implements All.MaxOccurs, XmlNonNegativeInteger, AllNNI.Member {
        private static final long serialVersionUID = 1;

        public MaxOccursImpl(SchemaType sType) {
            super(sType, false);
        }

        protected MaxOccursImpl(SchemaType sType, boolean b) {
            super(sType, b);
        }
    }
}
