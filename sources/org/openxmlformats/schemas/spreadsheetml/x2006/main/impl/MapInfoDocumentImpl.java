package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMapInfo;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.MapInfoDocument;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/MapInfoDocumentImpl.class */
public class MapInfoDocumentImpl extends XmlComplexContentImpl implements MapInfoDocument {
    private static final QName MAPINFO$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "MapInfo");

    public MapInfoDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.MapInfoDocument
    public CTMapInfo getMapInfo() {
        synchronized (monitor()) {
            check_orphaned();
            CTMapInfo cTMapInfo = (CTMapInfo) get_store().find_element_user(MAPINFO$0, 0);
            if (cTMapInfo == null) {
                return null;
            }
            return cTMapInfo;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.MapInfoDocument
    public void setMapInfo(CTMapInfo cTMapInfo) {
        synchronized (monitor()) {
            check_orphaned();
            CTMapInfo cTMapInfo2 = (CTMapInfo) get_store().find_element_user(MAPINFO$0, 0);
            if (cTMapInfo2 == null) {
                cTMapInfo2 = (CTMapInfo) get_store().add_element_user(MAPINFO$0);
            }
            cTMapInfo2.set(cTMapInfo);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.MapInfoDocument
    public CTMapInfo addNewMapInfo() {
        CTMapInfo cTMapInfo;
        synchronized (monitor()) {
            check_orphaned();
            cTMapInfo = (CTMapInfo) get_store().add_element_user(MAPINFO$0);
        }
        return cTMapInfo;
    }
}
