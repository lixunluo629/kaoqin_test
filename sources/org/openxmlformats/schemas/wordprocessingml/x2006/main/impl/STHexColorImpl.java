package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlUnionImpl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHexColor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHexColorAuto;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHexColorRGB;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/impl/STHexColorImpl.class */
public class STHexColorImpl extends XmlUnionImpl implements STHexColor, STHexColorAuto, STHexColorRGB {
    public STHexColorImpl(SchemaType schemaType) {
        super(schemaType, false);
    }

    protected STHexColorImpl(SchemaType schemaType, boolean z) {
        super(schemaType, z);
    }
}
