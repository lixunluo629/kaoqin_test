package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcChain;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CalcChainDocument;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CalcChainDocumentImpl.class */
public class CalcChainDocumentImpl extends XmlComplexContentImpl implements CalcChainDocument {
    private static final QName CALCCHAIN$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "calcChain");

    public CalcChainDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CalcChainDocument
    public CTCalcChain getCalcChain() {
        synchronized (monitor()) {
            check_orphaned();
            CTCalcChain cTCalcChain = (CTCalcChain) get_store().find_element_user(CALCCHAIN$0, 0);
            if (cTCalcChain == null) {
                return null;
            }
            return cTCalcChain;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CalcChainDocument
    public void setCalcChain(CTCalcChain cTCalcChain) {
        synchronized (monitor()) {
            check_orphaned();
            CTCalcChain cTCalcChain2 = (CTCalcChain) get_store().find_element_user(CALCCHAIN$0, 0);
            if (cTCalcChain2 == null) {
                cTCalcChain2 = (CTCalcChain) get_store().add_element_user(CALCCHAIN$0);
            }
            cTCalcChain2.set(cTCalcChain);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CalcChainDocument
    public CTCalcChain addNewCalcChain() {
        CTCalcChain cTCalcChain;
        synchronized (monitor()) {
            check_orphaned();
            cTCalcChain = (CTCalcChain) get_store().add_element_user(CALCCHAIN$0);
        }
        return cTCalcChain;
    }
}
