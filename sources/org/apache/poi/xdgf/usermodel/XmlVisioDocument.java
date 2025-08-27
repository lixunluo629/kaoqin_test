package org.apache.poi.xdgf.usermodel;

import com.microsoft.schemas.office.visio.x2012.main.VisioDocumentDocument1;
import com.microsoft.schemas.office.visio.x2012.main.VisioDocumentType;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.POIXMLException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackageRelationshipTypes;
import org.apache.poi.util.PackageHelper;
import org.apache.xmlbeans.XmlException;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xdgf/usermodel/XmlVisioDocument.class */
public class XmlVisioDocument extends POIXMLDocument {
    protected XDGFPages _pages;
    protected XDGFMasters _masters;
    protected XDGFDocument _document;

    public XmlVisioDocument(OPCPackage pkg) throws IOException {
        super(pkg, PackageRelationshipTypes.VISIO_CORE_DOCUMENT);
        try {
            VisioDocumentType document = VisioDocumentDocument1.Factory.parse(getPackagePart().getInputStream()).getVisioDocument();
            this._document = new XDGFDocument(document);
            load(new XDGFFactory(this._document));
        } catch (IOException e) {
            throw new POIXMLException(e);
        } catch (XmlException e2) {
            throw new POIXMLException(e2);
        }
    }

    public XmlVisioDocument(InputStream is) throws IOException {
        this(PackageHelper.open(is));
    }

    @Override // org.apache.poi.POIXMLDocumentPart
    protected void onDocumentRead() throws IOException {
        for (POIXMLDocumentPart part : getRelations()) {
            if (part instanceof XDGFPages) {
                this._pages = (XDGFPages) part;
            } else if (part instanceof XDGFMasters) {
                this._masters = (XDGFMasters) part;
            }
        }
        if (this._masters != null) {
            this._masters.onDocumentRead();
        }
        this._pages.onDocumentRead();
    }

    @Override // org.apache.poi.POIXMLDocument
    public List<PackagePart> getAllEmbedds() throws OpenXML4JException {
        return new ArrayList();
    }

    public Collection<XDGFPage> getPages() {
        return this._pages.getPageList();
    }

    public XDGFStyleSheet getStyleById(long id) {
        return this._document.getStyleById(id);
    }
}
