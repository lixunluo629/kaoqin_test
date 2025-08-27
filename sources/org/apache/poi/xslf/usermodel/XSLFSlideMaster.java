package org.apache.poi.xslf.usermodel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.sl.usermodel.MasterSheet;
import org.apache.poi.sl.usermodel.Placeholder;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle;
import org.openxmlformats.schemas.presentationml.x2006.main.CTBackground;
import org.openxmlformats.schemas.presentationml.x2006.main.CTPlaceholder;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMaster;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterTextStyles;
import org.openxmlformats.schemas.presentationml.x2006.main.SldMasterDocument;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xslf/usermodel/XSLFSlideMaster.class */
public class XSLFSlideMaster extends XSLFSheet implements MasterSheet<XSLFShape, XSLFTextParagraph> {
    private CTSlideMaster _slide;
    private Map<String, XSLFSlideLayout> _layouts;
    private XSLFTheme _theme;

    XSLFSlideMaster() {
        this._slide = CTSlideMaster.Factory.newInstance();
    }

    protected XSLFSlideMaster(PackagePart part) throws XmlException, IOException {
        super(part);
        SldMasterDocument doc = SldMasterDocument.Factory.parse(getPackagePart().getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
        this._slide = doc.getSldMaster();
        setCommonSlideData(this._slide.getCSld());
    }

    @Override // org.apache.poi.xslf.usermodel.XSLFSheet
    public CTSlideMaster getXmlObject() {
        return this._slide;
    }

    @Override // org.apache.poi.xslf.usermodel.XSLFSheet
    protected String getRootElementName() {
        return "sldMaster";
    }

    @Override // org.apache.poi.sl.usermodel.Sheet
    public XSLFSlideMaster getMasterSheet() {
        return null;
    }

    private Map<String, XSLFSlideLayout> getLayouts() {
        if (this._layouts == null) {
            this._layouts = new HashMap();
            for (POIXMLDocumentPart p : getRelations()) {
                if (p instanceof XSLFSlideLayout) {
                    XSLFSlideLayout layout = (XSLFSlideLayout) p;
                    this._layouts.put(layout.getName().toLowerCase(Locale.ROOT), layout);
                }
            }
        }
        return this._layouts;
    }

    public XSLFSlideLayout[] getSlideLayouts() {
        return (XSLFSlideLayout[]) getLayouts().values().toArray(new XSLFSlideLayout[this._layouts.size()]);
    }

    public XSLFSlideLayout getLayout(SlideLayout type) {
        for (XSLFSlideLayout layout : getLayouts().values()) {
            if (layout.getType() == type) {
                return layout;
            }
        }
        return null;
    }

    public XSLFSlideLayout getLayout(String name) {
        return getLayouts().get(name.toLowerCase(Locale.ROOT));
    }

    @Override // org.apache.poi.xslf.usermodel.XSLFSheet
    public XSLFTheme getTheme() {
        if (this._theme == null) {
            Iterator i$ = getRelations().iterator();
            while (true) {
                if (!i$.hasNext()) {
                    break;
                }
                POIXMLDocumentPart p = i$.next();
                if (p instanceof XSLFTheme) {
                    this._theme = (XSLFTheme) p;
                    CTColorMapping cmap = this._slide.getClrMap();
                    if (cmap != null) {
                        this._theme.initColorMap(cmap);
                    }
                }
            }
        }
        return this._theme;
    }

    protected CTTextListStyle getTextProperties(Placeholder textType) {
        CTTextListStyle props;
        CTSlideMasterTextStyles txStyles = getXmlObject().getTxStyles();
        switch (textType) {
            case TITLE:
            case CENTERED_TITLE:
            case SUBTITLE:
                props = txStyles.getTitleStyle();
                break;
            case BODY:
                props = txStyles.getBodyStyle();
                break;
            default:
                props = txStyles.getOtherStyle();
                break;
        }
        return props;
    }

    @Override // org.apache.poi.xslf.usermodel.XSLFSheet
    protected boolean canDraw(XSLFShape shape) {
        if (shape instanceof XSLFSimpleShape) {
            XSLFSimpleShape txt = (XSLFSimpleShape) shape;
            CTPlaceholder ph = txt.getCTPlaceholder();
            if (ph != null) {
                return false;
            }
            return true;
        }
        return true;
    }

    @Override // org.apache.poi.xslf.usermodel.XSLFSheet, org.apache.poi.sl.usermodel.Sheet
    public XSLFBackground getBackground() {
        CTBackground bg = this._slide.getCSld().getBg();
        if (bg != null) {
            return new XSLFBackground(bg, this);
        }
        return null;
    }
}
