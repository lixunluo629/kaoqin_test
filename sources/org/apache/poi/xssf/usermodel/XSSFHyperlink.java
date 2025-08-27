package org.apache.poi.xssf.usermodel;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.Internal;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlink;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/usermodel/XSSFHyperlink.class */
public class XSSFHyperlink implements Hyperlink {
    private final HyperlinkType _type;
    private final PackageRelationship _externalRel;
    private final CTHyperlink _ctHyperlink;
    private String _location;

    protected XSSFHyperlink(HyperlinkType type) {
        this._type = type;
        this._ctHyperlink = CTHyperlink.Factory.newInstance();
        this._externalRel = null;
    }

    protected XSSFHyperlink(CTHyperlink ctHyperlink, PackageRelationship hyperlinkRel) {
        this._ctHyperlink = ctHyperlink;
        this._externalRel = hyperlinkRel;
        if (this._externalRel == null) {
            if (ctHyperlink.getLocation() != null) {
                this._type = HyperlinkType.DOCUMENT;
                this._location = ctHyperlink.getLocation();
                return;
            } else {
                if (ctHyperlink.getId() != null) {
                    throw new IllegalStateException("The hyperlink for cell " + ctHyperlink.getRef() + " references relation " + ctHyperlink.getId() + ", but that didn't exist!");
                }
                this._type = HyperlinkType.DOCUMENT;
                return;
            }
        }
        URI target = this._externalRel.getTargetURI();
        this._location = target.toString();
        if (ctHyperlink.getLocation() != null) {
            this._location += "#" + ctHyperlink.getLocation();
        }
        if (this._location.startsWith("http://") || this._location.startsWith("https://") || this._location.startsWith("ftp://")) {
            this._type = HyperlinkType.URL;
        } else if (this._location.startsWith("mailto:")) {
            this._type = HyperlinkType.EMAIL;
        } else {
            this._type = HyperlinkType.FILE;
        }
    }

    @Internal
    public XSSFHyperlink(Hyperlink other) {
        if (other instanceof XSSFHyperlink) {
            XSSFHyperlink xlink = (XSSFHyperlink) other;
            this._type = xlink.getTypeEnum();
            this._location = xlink._location;
            this._externalRel = xlink._externalRel;
            this._ctHyperlink = (CTHyperlink) xlink._ctHyperlink.copy();
            return;
        }
        this._type = other.getTypeEnum();
        this._location = other.getAddress();
        this._externalRel = null;
        this._ctHyperlink = CTHyperlink.Factory.newInstance();
        setCellReference(new CellReference(other.getFirstRow(), other.getFirstColumn()));
    }

    @Internal
    public CTHyperlink getCTHyperlink() {
        return this._ctHyperlink;
    }

    public boolean needsRelationToo() {
        return this._type != HyperlinkType.DOCUMENT;
    }

    protected void generateRelationIfNeeded(PackagePart sheetPart) {
        if (this._externalRel == null && needsRelationToo()) {
            PackageRelationship rel = sheetPart.addExternalRelationship(this._location, XSSFRelation.SHEET_HYPERLINKS.getRelation());
            this._ctHyperlink.setId(rel.getId());
        }
    }

    @Override // org.apache.poi.common.usermodel.Hyperlink
    public int getType() {
        return this._type.getCode();
    }

    @Override // org.apache.poi.common.usermodel.Hyperlink
    public HyperlinkType getTypeEnum() {
        return this._type;
    }

    public String getCellRef() {
        return this._ctHyperlink.getRef();
    }

    @Override // org.apache.poi.common.usermodel.Hyperlink
    public String getAddress() {
        return this._location;
    }

    @Override // org.apache.poi.common.usermodel.Hyperlink
    public String getLabel() {
        return this._ctHyperlink.getDisplay();
    }

    public String getLocation() {
        return this._ctHyperlink.getLocation();
    }

    @Override // org.apache.poi.common.usermodel.Hyperlink
    public void setLabel(String label) {
        this._ctHyperlink.setDisplay(label);
    }

    public void setLocation(String location) {
        this._ctHyperlink.setLocation(location);
    }

    @Override // org.apache.poi.common.usermodel.Hyperlink
    public void setAddress(String address) {
        validate(address);
        this._location = address;
        if (this._type == HyperlinkType.DOCUMENT) {
            setLocation(address);
        }
    }

    private void validate(String address) {
        switch (this._type) {
            case EMAIL:
            case FILE:
            case URL:
                try {
                    new URI(address);
                    return;
                } catch (URISyntaxException e) {
                    throw new IllegalArgumentException("Address of hyperlink must be a valid URI", e);
                }
            case DOCUMENT:
                return;
            default:
                throw new IllegalStateException("Invalid Hyperlink type: " + this._type);
        }
    }

    @Internal
    public void setCellReference(String ref) {
        this._ctHyperlink.setRef(ref);
    }

    @Internal
    public void setCellReference(CellReference ref) {
        setCellReference(ref.formatAsString());
    }

    private CellReference buildCellReference() {
        String ref = this._ctHyperlink.getRef();
        if (ref == null) {
            ref = "A1";
        }
        return new CellReference(ref);
    }

    @Override // org.apache.poi.ss.usermodel.Hyperlink
    public int getFirstColumn() {
        return buildCellReference().getCol();
    }

    @Override // org.apache.poi.ss.usermodel.Hyperlink
    public int getLastColumn() {
        return buildCellReference().getCol();
    }

    @Override // org.apache.poi.ss.usermodel.Hyperlink
    public int getFirstRow() {
        return buildCellReference().getRow();
    }

    @Override // org.apache.poi.ss.usermodel.Hyperlink
    public int getLastRow() {
        return buildCellReference().getRow();
    }

    @Override // org.apache.poi.ss.usermodel.Hyperlink
    public void setFirstColumn(int col) {
        setCellReference(new CellReference(getFirstRow(), col));
    }

    @Override // org.apache.poi.ss.usermodel.Hyperlink
    public void setLastColumn(int col) {
        setFirstColumn(col);
    }

    @Override // org.apache.poi.ss.usermodel.Hyperlink
    public void setFirstRow(int row) {
        setCellReference(new CellReference(row, getFirstColumn()));
    }

    @Override // org.apache.poi.ss.usermodel.Hyperlink
    public void setLastRow(int row) {
        setFirstRow(row);
    }

    public String getTooltip() {
        return this._ctHyperlink.getTooltip();
    }

    public void setTooltip(String text) {
        this._ctHyperlink.setTooltip(text);
    }
}
