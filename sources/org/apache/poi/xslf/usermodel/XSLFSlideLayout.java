package org.apache.poi.xslf.usermodel;

import java.io.IOException;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.sl.usermodel.MasterSheet;
import org.apache.poi.sl.usermodel.Placeholder;
import org.apache.poi.util.Internal;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.presentationml.x2006.main.CTBackground;
import org.openxmlformats.schemas.presentationml.x2006.main.CTPlaceholder;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayout;
import org.openxmlformats.schemas.presentationml.x2006.main.SldLayoutDocument;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xslf/usermodel/XSLFSlideLayout.class */
public class XSLFSlideLayout extends XSLFSheet implements MasterSheet<XSLFShape, XSLFTextParagraph> {
    private CTSlideLayout _layout;
    private XSLFSlideMaster _master;

    XSLFSlideLayout() {
        this._layout = CTSlideLayout.Factory.newInstance();
    }

    public XSLFSlideLayout(PackagePart part) throws XmlException, IOException {
        super(part);
        SldLayoutDocument doc = SldLayoutDocument.Factory.parse(getPackagePart().getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
        this._layout = doc.getSldLayout();
        setCommonSlideData(this._layout.getCSld());
    }

    public String getName() {
        return this._layout.getCSld().getName();
    }

    @Override // org.apache.poi.xslf.usermodel.XSLFSheet
    @Internal
    public CTSlideLayout getXmlObject() {
        return this._layout;
    }

    @Override // org.apache.poi.xslf.usermodel.XSLFSheet
    protected String getRootElementName() {
        return "sldLayout";
    }

    public XSLFSlideMaster getSlideMaster() {
        if (this._master == null) {
            for (POIXMLDocumentPart p : getRelations()) {
                if (p instanceof XSLFSlideMaster) {
                    this._master = (XSLFSlideMaster) p;
                }
            }
        }
        if (this._master == null) {
            throw new IllegalStateException("SlideMaster was not found for " + this);
        }
        return this._master;
    }

    @Override // org.apache.poi.sl.usermodel.Sheet
    public XSLFSlideMaster getMasterSheet() {
        return getSlideMaster();
    }

    @Override // org.apache.poi.xslf.usermodel.XSLFSheet
    public XSLFTheme getTheme() {
        return getSlideMaster().getTheme();
    }

    @Override // org.apache.poi.xslf.usermodel.XSLFSheet, org.apache.poi.sl.usermodel.Sheet
    public boolean getFollowMasterGraphics() {
        return this._layout.getShowMasterSp();
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
        CTBackground bg = this._layout.getCSld().getBg();
        if (bg != null) {
            return new XSLFBackground(bg, this);
        }
        return getMasterSheet().getBackground();
    }

    public void copyLayout(XSLFSlide slide) {
        XSLFTextShape tsh;
        Placeholder ph;
        for (XSLFShape sh : getShapes()) {
            if ((sh instanceof XSLFTextShape) && (ph = (tsh = (XSLFTextShape) sh).getTextType()) != null) {
                switch (ph) {
                    case DATETIME:
                    case SLIDE_NUMBER:
                    case FOOTER:
                        break;
                    default:
                        slide.getSpTree().addNewSp().set(tsh.getXmlObject().copy());
                        break;
                }
            }
        }
    }

    public SlideLayout getType() {
        int ordinal = this._layout.getType().intValue() - 1;
        return SlideLayout.values()[ordinal];
    }
}
