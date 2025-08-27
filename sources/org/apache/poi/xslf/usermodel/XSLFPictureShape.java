package org.apache.poi.xslf.usermodel;

import java.awt.Insets;
import java.net.URI;
import javax.xml.namespace.QName;
import org.apache.poi.POIXMLException;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.openxml4j.opc.PackageRelationshipTypes;
import org.apache.poi.sl.usermodel.PictureShape;
import org.apache.poi.sl.usermodel.Placeholder;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.drawingml.x2006.main.CTBlip;
import org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtension;
import org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPresetGeometry2D;
import org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect;
import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.STShapeType;
import org.openxmlformats.schemas.presentationml.x2006.main.CTApplicationNonVisualDrawingProps;
import org.openxmlformats.schemas.presentationml.x2006.main.CTPicture;
import org.openxmlformats.schemas.presentationml.x2006.main.CTPictureNonVisual;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xslf/usermodel/XSLFPictureShape.class */
public class XSLFPictureShape extends XSLFSimpleShape implements PictureShape<XSLFShape, XSLFTextParagraph> {
    private XSLFPictureData _data;

    XSLFPictureShape(CTPicture shape, XSLFSheet sheet) {
        super(shape, sheet);
    }

    static CTPicture prototype(int shapeId, String rel) {
        CTPicture ct = CTPicture.Factory.newInstance();
        CTPictureNonVisual nvSpPr = ct.addNewNvPicPr();
        CTNonVisualDrawingProps cnv = nvSpPr.addNewCNvPr();
        cnv.setName("Picture " + shapeId);
        cnv.setId(shapeId + 1);
        nvSpPr.addNewCNvPicPr().addNewPicLocks().setNoChangeAspect(true);
        nvSpPr.addNewNvPr();
        CTBlipFillProperties blipFill = ct.addNewBlipFill();
        CTBlip blip = blipFill.addNewBlip();
        blip.setEmbed(rel);
        blipFill.addNewStretch().addNewFillRect();
        CTShapeProperties spPr = ct.addNewSpPr();
        CTPresetGeometry2D prst = spPr.addNewPrstGeom();
        prst.setPrst(STShapeType.RECT);
        prst.addNewAvLst();
        return ct;
    }

    public boolean isExternalLinkedPicture() {
        if (getBlipId() == null && getBlipLink() != null) {
            return true;
        }
        return false;
    }

    @Override // org.apache.poi.sl.usermodel.PictureShape
    public XSLFPictureData getPictureData() {
        if (this._data == null) {
            String blipId = getBlipId();
            if (blipId == null) {
                return null;
            }
            PackagePart p = getSheet().getPackagePart();
            PackageRelationship rel = p.getRelationship(blipId);
            if (rel != null) {
                try {
                    PackagePart imgPart = p.getRelatedPart(rel);
                    this._data = new XSLFPictureData(imgPart);
                } catch (Exception e) {
                    throw new POIXMLException(e);
                }
            }
        }
        return this._data;
    }

    @Override // org.apache.poi.xslf.usermodel.XSLFSimpleShape, org.apache.poi.xslf.usermodel.XSLFShape, org.apache.poi.sl.usermodel.SimpleShape
    public void setPlaceholder(Placeholder placeholder) {
        super.setPlaceholder(placeholder);
    }

    public URI getPictureLink() {
        String rId;
        if (getBlipId() != null || (rId = getBlipLink()) == null) {
            return null;
        }
        PackagePart p = getSheet().getPackagePart();
        PackageRelationship rel = p.getRelationship(rId);
        if (rel != null) {
            return rel.getTargetURI();
        }
        return null;
    }

    protected CTBlipFillProperties getBlipFill() {
        CTPicture ct = (CTPicture) getXmlObject();
        CTBlipFillProperties bfp = ct.getBlipFill();
        if (bfp != null) {
            return bfp;
        }
        XmlObject xo = selectProperty(XmlObject.class, "declare namespace p='http://schemas.openxmlformats.org/presentationml/2006/main'; declare namespace mc='http://schemas.openxmlformats.org/markup-compatibility/2006' .//mc:Fallback/p:blipFill");
        try {
            return CTPicture.Factory.parse(xo.getDomNode()).getBlipFill();
        } catch (XmlException e) {
            return null;
        }
    }

    protected CTBlip getBlip() {
        return getBlipFill().getBlip();
    }

    protected String getBlipLink() {
        String link = getBlip().getLink();
        if (link.isEmpty()) {
            return null;
        }
        return link;
    }

    protected String getBlipId() {
        String id = getBlip().getEmbed();
        if (id.isEmpty()) {
            return null;
        }
        return id;
    }

    @Override // org.apache.poi.sl.usermodel.PictureShape
    public Insets getClipping() {
        CTRelativeRect r = getBlipFill().getSrcRect();
        if (r == null) {
            return null;
        }
        return new Insets(r.getT(), r.getL(), r.getB(), r.getR());
    }

    @Override // org.apache.poi.xslf.usermodel.XSLFSimpleShape, org.apache.poi.xslf.usermodel.XSLFShape
    void copy(XSLFShape sh) throws InvalidOperationException {
        super.copy(sh);
        XSLFPictureShape p = (XSLFPictureShape) sh;
        String blipId = p.getBlipId();
        String relId = getSheet().importBlip(blipId, p.getSheet().getPackagePart());
        CTPicture ct = (CTPicture) getXmlObject();
        CTBlip blip = getBlipFill().getBlip();
        blip.setEmbed(relId);
        CTApplicationNonVisualDrawingProps nvPr = ct.getNvPicPr().getNvPr();
        if (nvPr.isSetCustDataLst()) {
            nvPr.unsetCustDataLst();
        }
        if (blip.isSetExtLst()) {
            CTOfficeArtExtensionList extLst = blip.getExtLst();
            CTOfficeArtExtension[] arr$ = extLst.getExtArray();
            for (CTOfficeArtExtension ext : arr$) {
                XmlObject[] obj = ext.selectPath("declare namespace a14='http://schemas.microsoft.com/office/drawing/2010/main' $this//a14:imgProps/a14:imgLayer");
                if (obj != null && obj.length == 1) {
                    XmlCursor c = obj[0].newCursor();
                    String id = c.getAttributeText(new QName(PackageRelationshipTypes.CORE_PROPERTIES_ECMA376_NS, "embed"));
                    String newId = getSheet().importBlip(id, p.getSheet().getPackagePart());
                    c.setAttributeText(new QName(PackageRelationshipTypes.CORE_PROPERTIES_ECMA376_NS, "embed"), newId);
                    c.dispose();
                }
            }
        }
    }
}
