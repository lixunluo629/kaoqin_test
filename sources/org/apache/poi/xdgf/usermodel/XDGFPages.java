package org.apache.poi.xdgf.usermodel;

import com.microsoft.schemas.office.visio.x2012.main.PageType;
import com.microsoft.schemas.office.visio.x2012.main.PagesDocument;
import com.microsoft.schemas.office.visio.x2012.main.PagesType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.POIXMLException;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.util.Internal;
import org.apache.poi.xdgf.exceptions.XDGFException;
import org.apache.poi.xdgf.xml.XDGFXMLDocumentPart;
import org.apache.xmlbeans.XmlException;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xdgf/usermodel/XDGFPages.class */
public class XDGFPages extends XDGFXMLDocumentPart {
    PagesType _pagesObject;
    List<XDGFPage> _pages;

    public XDGFPages(PackagePart part, XDGFDocument document) {
        super(part, document);
        this._pages = new ArrayList();
    }

    @Internal
    PagesType getXmlObject() {
        return this._pagesObject;
    }

    @Override // org.apache.poi.POIXMLDocumentPart
    protected void onDocumentRead() {
        try {
            try {
                this._pagesObject = PagesDocument.Factory.parse(getPackagePart().getInputStream()).getPages();
                PageType[] arr$ = this._pagesObject.getPageArray();
                for (PageType pageSettings : arr$) {
                    String relId = pageSettings.getRel().getId();
                    POIXMLDocumentPart pageContentsPart = getRelationById(relId);
                    if (pageContentsPart == null) {
                        throw new POIXMLException("PageSettings relationship for " + relId + " not found");
                    }
                    if (!(pageContentsPart instanceof XDGFPageContents)) {
                        throw new POIXMLException("Unexpected pages relationship for " + relId + ": " + pageContentsPart);
                    }
                    XDGFPageContents contents = (XDGFPageContents) pageContentsPart;
                    XDGFPage page = new XDGFPage(pageSettings, contents, this._document, this);
                    contents.onDocumentRead();
                    this._pages.add(page);
                }
            } catch (IOException e) {
                throw new POIXMLException(e);
            } catch (XmlException e2) {
                throw new POIXMLException(e2);
            }
        } catch (POIXMLException e3) {
            throw XDGFException.wrap(this, e3);
        }
    }

    public List<XDGFPage> getPageList() {
        return Collections.unmodifiableList(this._pages);
    }
}
