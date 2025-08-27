package org.apache.poi.xslf.usermodel;

import java.net.URI;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackagePartName;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.openxml4j.opc.TargetMode;
import org.apache.poi.sl.usermodel.Hyperlink;
import org.apache.poi.sl.usermodel.Slide;
import org.apache.poi.util.Internal;
import org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xslf/usermodel/XSLFHyperlink.class */
public class XSLFHyperlink implements Hyperlink<XSLFShape, XSLFTextParagraph> {
    final XSLFSheet _sheet;
    final CTHyperlink _link;

    XSLFHyperlink(CTHyperlink link, XSLFSheet sheet) {
        this._sheet = sheet;
        this._link = link;
    }

    @Internal
    public CTHyperlink getXmlObject() {
        return this._link;
    }

    @Override // org.apache.poi.common.usermodel.Hyperlink
    public void setAddress(String address) throws InvalidOperationException {
        linkToUrl(address);
    }

    @Override // org.apache.poi.common.usermodel.Hyperlink
    public String getAddress() {
        String id = this._link.getId();
        if (id == null || "".equals(id)) {
            return this._link.getAction();
        }
        URI targetURI = this._sheet.getPackagePart().getRelationship(id).getTargetURI();
        return targetURI.toASCIIString();
    }

    @Override // org.apache.poi.common.usermodel.Hyperlink
    public String getLabel() {
        return this._link.getTooltip();
    }

    @Override // org.apache.poi.common.usermodel.Hyperlink
    public void setLabel(String label) {
        this._link.setTooltip(label);
    }

    @Override // org.apache.poi.common.usermodel.Hyperlink
    public int getType() {
        return getTypeEnum().getCode();
    }

    @Override // org.apache.poi.common.usermodel.Hyperlink
    public HyperlinkType getTypeEnum() {
        String action = this._link.getAction();
        if (action == null) {
            action = "";
        }
        if (action.equals("ppaction://hlinksldjump") || action.startsWith("ppaction://hlinkshowjump")) {
            return HyperlinkType.DOCUMENT;
        }
        String address = getAddress();
        if (address == null) {
            address = "";
        }
        if (address.startsWith("mailto:")) {
            return HyperlinkType.EMAIL;
        }
        return HyperlinkType.URL;
    }

    @Override // org.apache.poi.sl.usermodel.Hyperlink
    public void linkToEmail(String emailAddress) throws InvalidOperationException {
        linkToExternal("mailto:" + emailAddress);
        setLabel(emailAddress);
    }

    @Override // org.apache.poi.sl.usermodel.Hyperlink
    public void linkToUrl(String url) throws InvalidOperationException {
        linkToExternal(url);
        setLabel(url);
    }

    private void linkToExternal(String url) throws InvalidOperationException {
        PackagePart thisPP = this._sheet.getPackagePart();
        if (this._link.isSetId() && !this._link.getId().isEmpty()) {
            thisPP.removeRelationship(this._link.getId());
        }
        PackageRelationship rel = thisPP.addExternalRelationship(url, XSLFRelation.HYPERLINK.getRelation());
        this._link.setId(rel.getId());
        if (this._link.isSetAction()) {
            this._link.unsetAction();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.poi.sl.usermodel.Hyperlink
    public void linkToSlide(Slide<XSLFShape, XSLFTextParagraph> slide) throws InvalidOperationException {
        PackagePart thisPP = this._sheet.getPackagePart();
        PackagePartName otherPPN = ((XSLFSheet) slide).getPackagePart().getPartName();
        if (this._link.isSetId() && !this._link.getId().isEmpty()) {
            thisPP.removeRelationship(this._link.getId());
        }
        PackageRelationship rel = thisPP.addRelationship(otherPPN, TargetMode.INTERNAL, XSLFRelation.SLIDE.getRelation());
        this._link.setId(rel.getId());
        this._link.setAction("ppaction://hlinksldjump");
    }

    @Override // org.apache.poi.sl.usermodel.Hyperlink
    public void linkToNextSlide() throws InvalidOperationException {
        linkToRelativeSlide("nextslide");
    }

    @Override // org.apache.poi.sl.usermodel.Hyperlink
    public void linkToPreviousSlide() throws InvalidOperationException {
        linkToRelativeSlide("previousslide");
    }

    @Override // org.apache.poi.sl.usermodel.Hyperlink
    public void linkToFirstSlide() throws InvalidOperationException {
        linkToRelativeSlide("firstslide");
    }

    @Override // org.apache.poi.sl.usermodel.Hyperlink
    public void linkToLastSlide() throws InvalidOperationException {
        linkToRelativeSlide("lastslide");
    }

    private void linkToRelativeSlide(String jump) throws InvalidOperationException {
        PackagePart thisPP = this._sheet.getPackagePart();
        if (this._link.isSetId() && !this._link.getId().isEmpty()) {
            thisPP.removeRelationship(this._link.getId());
        }
        this._link.setId("");
        this._link.setAction("ppaction://hlinkshowjump?jump=" + jump);
    }
}
