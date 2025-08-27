package org.apache.poi.xdgf.usermodel;

import com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType;
import com.microsoft.schemas.office.visio.x2012.main.TextType;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.poi.POIXMLException;
import org.apache.poi.util.Internal;
import org.apache.poi.xdgf.exceptions.XDGFException;
import org.apache.poi.xdgf.usermodel.section.CombinedIterable;
import org.apache.poi.xdgf.usermodel.section.GeometrySection;
import org.apache.poi.xdgf.usermodel.section.XDGFSection;
import org.apache.poi.xdgf.usermodel.shape.ShapeVisitor;
import org.apache.poi.xdgf.usermodel.shape.exceptions.StopVisitingThisBranch;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xdgf/usermodel/XDGFShape.class */
public class XDGFShape extends XDGFSheet {
    XDGFBaseContents _parentPage;
    XDGFShape _parent;
    XDGFMaster _master;
    XDGFShape _masterShape;
    XDGFText _text;
    List<XDGFShape> _shapes;
    Double _pinX;
    Double _pinY;
    Double _width;
    Double _height;
    Double _locPinX;
    Double _locPinY;
    Double _beginX;
    Double _beginY;
    Double _endX;
    Double _endY;
    Double _angle;
    Double _rotationXAngle;
    Double _rotationYAngle;
    Double _rotationZAngle;
    Boolean _flipX;
    Boolean _flipY;
    Double _txtPinX;
    Double _txtPinY;
    Double _txtLocPinX;
    Double _txtLocPinY;
    Double _txtAngle;
    Double _txtWidth;
    Double _txtHeight;

    public XDGFShape(ShapeSheetType shapeSheet, XDGFBaseContents parentPage, XDGFDocument document) {
        this(null, shapeSheet, parentPage, document);
    }

    public XDGFShape(XDGFShape parent, ShapeSheetType shapeSheet, XDGFBaseContents parentPage, XDGFDocument document) {
        super(shapeSheet, document);
        this._master = null;
        this._masterShape = null;
        this._text = null;
        this._shapes = null;
        this._pinX = null;
        this._pinY = null;
        this._width = null;
        this._height = null;
        this._locPinX = null;
        this._locPinY = null;
        this._beginX = null;
        this._beginY = null;
        this._endX = null;
        this._endY = null;
        this._angle = null;
        this._rotationXAngle = null;
        this._rotationYAngle = null;
        this._rotationZAngle = null;
        this._flipX = null;
        this._flipY = null;
        this._txtPinX = null;
        this._txtPinY = null;
        this._txtLocPinX = null;
        this._txtLocPinY = null;
        this._txtAngle = null;
        this._txtWidth = null;
        this._txtHeight = null;
        this._parent = parent;
        this._parentPage = parentPage;
        TextType text = shapeSheet.getText();
        if (text != null) {
            this._text = new XDGFText(text, this);
        }
        if (shapeSheet.isSetShapes()) {
            this._shapes = new ArrayList();
            ShapeSheetType[] arr$ = shapeSheet.getShapes().getShapeArray();
            for (ShapeSheetType shape : arr$) {
                this._shapes.add(new XDGFShape(this, shape, parentPage, document));
            }
        }
        readProperties();
    }

    public String toString() {
        if (this._parentPage instanceof XDGFMasterContents) {
            return this._parentPage + ": <Shape ID=\"" + getID() + "\">";
        }
        return "<Shape ID=\"" + getID() + "\">";
    }

    protected void readProperties() {
        this._pinX = XDGFCell.maybeGetDouble(this._cells, "PinX");
        this._pinY = XDGFCell.maybeGetDouble(this._cells, "PinY");
        this._width = XDGFCell.maybeGetDouble(this._cells, "Width");
        this._height = XDGFCell.maybeGetDouble(this._cells, "Height");
        this._locPinX = XDGFCell.maybeGetDouble(this._cells, "LocPinX");
        this._locPinY = XDGFCell.maybeGetDouble(this._cells, "LocPinY");
        this._beginX = XDGFCell.maybeGetDouble(this._cells, "BeginX");
        this._beginY = XDGFCell.maybeGetDouble(this._cells, "BeginY");
        this._endX = XDGFCell.maybeGetDouble(this._cells, "EndX");
        this._endY = XDGFCell.maybeGetDouble(this._cells, "EndY");
        this._angle = XDGFCell.maybeGetDouble(this._cells, "Angle");
        this._rotationXAngle = XDGFCell.maybeGetDouble(this._cells, "RotationXAngle");
        this._rotationYAngle = XDGFCell.maybeGetDouble(this._cells, "RotationYAngle");
        this._rotationZAngle = XDGFCell.maybeGetDouble(this._cells, "RotationZAngle");
        this._flipX = XDGFCell.maybeGetBoolean(this._cells, "FlipX");
        this._flipY = XDGFCell.maybeGetBoolean(this._cells, "FlipY");
        this._txtPinX = XDGFCell.maybeGetDouble(this._cells, "TxtPinX");
        this._txtPinY = XDGFCell.maybeGetDouble(this._cells, "TxtPinY");
        this._txtLocPinX = XDGFCell.maybeGetDouble(this._cells, "TxtLocPinX");
        this._txtLocPinY = XDGFCell.maybeGetDouble(this._cells, "TxtLocPinY");
        this._txtWidth = XDGFCell.maybeGetDouble(this._cells, "TxtWidth");
        this._txtHeight = XDGFCell.maybeGetDouble(this._cells, "TxtHeight");
        this._txtAngle = XDGFCell.maybeGetDouble(this._cells, "TxtAngle");
    }

    protected void setupMaster(XDGFPageContents pageContents, XDGFMasterContents master) {
        ShapeSheetType obj = getXmlObject();
        if (obj.isSetMaster()) {
            this._master = pageContents.getMasterById(obj.getMaster());
            if (this._master == null) {
                throw XDGFException.error("refers to non-existant master " + obj.getMaster(), this);
            }
            Collection<XDGFShape> masterShapes = this._master.getContent().getTopLevelShapes();
            switch (masterShapes.size()) {
                case 0:
                    throw XDGFException.error("Could not retrieve master shape from " + this._master, this);
                case 1:
                    this._masterShape = masterShapes.iterator().next();
                    break;
            }
        } else if (obj.isSetMasterShape()) {
            this._masterShape = master.getShapeById(obj.getMasterShape());
            if (this._masterShape == null) {
                throw XDGFException.error("refers to non-existant master shape " + obj.getMasterShape(), this);
            }
        }
        setupSectionMasters();
        if (this._shapes != null) {
            for (XDGFShape shape : this._shapes) {
                shape.setupMaster(pageContents, this._master == null ? master : this._master.getContent());
            }
        }
    }

    protected void setupSectionMasters() {
        if (this._masterShape == null) {
            return;
        }
        try {
            for (Map.Entry<String, XDGFSection> section : this._sections.entrySet()) {
                XDGFSection master = this._masterShape.getSection(section.getKey());
                if (master != null) {
                    section.getValue().setupMaster(master);
                }
            }
            for (Map.Entry<Long, GeometrySection> section2 : this._geometry.entrySet()) {
                GeometrySection master2 = this._masterShape.getGeometryByIdx(section2.getKey().longValue());
                if (master2 != null) {
                    section2.getValue().setupMaster(master2);
                }
            }
        } catch (POIXMLException e) {
            throw XDGFException.wrap(toString(), e);
        }
    }

    @Override // org.apache.poi.xdgf.usermodel.XDGFSheet
    @Internal
    public ShapeSheetType getXmlObject() {
        return (ShapeSheetType) this._sheet;
    }

    public long getID() {
        return getXmlObject().getID();
    }

    public String getType() {
        return getXmlObject().getType();
    }

    public String getTextAsString() {
        XDGFText text = getText();
        if (text == null) {
            return "";
        }
        return text.getTextContent();
    }

    public boolean hasText() {
        return (this._text == null && (this._masterShape == null || this._masterShape._text == null)) ? false : true;
    }

    @Override // org.apache.poi.xdgf.usermodel.XDGFSheet
    public XDGFCell getCell(String cellName) {
        XDGFCell _cell = super.getCell(cellName);
        if (_cell == null && this._masterShape != null) {
            _cell = this._masterShape.getCell(cellName);
        }
        return _cell;
    }

    public GeometrySection getGeometryByIdx(long idx) {
        return this._geometry.get(Long.valueOf(idx));
    }

    public List<XDGFShape> getShapes() {
        return this._shapes;
    }

    public String getName() {
        String name = getXmlObject().getName();
        if (name == null) {
            return "";
        }
        return name;
    }

    public String getShapeType() {
        String type = getXmlObject().getType();
        if (type == null) {
            return "";
        }
        return type;
    }

    public String getSymbolName() {
        String name;
        if (this._master == null || (name = this._master.getName()) == null) {
            return "";
        }
        return name;
    }

    public XDGFShape getMasterShape() {
        return this._masterShape;
    }

    public XDGFShape getParentShape() {
        return this._parent;
    }

    public XDGFShape getTopmostParentShape() {
        XDGFShape top = null;
        if (this._parent != null) {
            top = this._parent.getTopmostParentShape();
            if (top == null) {
                top = this._parent;
            }
        }
        return top;
    }

    public boolean hasMaster() {
        return this._master != null;
    }

    public boolean hasMasterShape() {
        return this._masterShape != null;
    }

    public boolean hasParent() {
        return this._parent != null;
    }

    public boolean hasShapes() {
        return this._shapes != null;
    }

    public boolean isTopmost() {
        return this._parent == null;
    }

    public boolean isShape1D() {
        return getBeginX() != null;
    }

    public boolean isDeleted() {
        if (getXmlObject().isSetDel()) {
            return getXmlObject().getDel();
        }
        return false;
    }

    public XDGFText getText() {
        if (this._text == null && this._masterShape != null) {
            return this._masterShape.getText();
        }
        return this._text;
    }

    public Double getPinX() {
        if (this._pinX == null && this._masterShape != null) {
            return this._masterShape.getPinX();
        }
        if (this._pinX == null) {
            throw XDGFException.error("PinX not set!", this);
        }
        return this._pinX;
    }

    public Double getPinY() {
        if (this._pinY == null && this._masterShape != null) {
            return this._masterShape.getPinY();
        }
        if (this._pinY == null) {
            throw XDGFException.error("PinY not specified!", this);
        }
        return this._pinY;
    }

    public Double getWidth() {
        if (this._width == null && this._masterShape != null) {
            return this._masterShape.getWidth();
        }
        if (this._width == null) {
            throw XDGFException.error("Width not specified!", this);
        }
        return this._width;
    }

    public Double getHeight() {
        if (this._height == null && this._masterShape != null) {
            return this._masterShape.getHeight();
        }
        if (this._height == null) {
            throw XDGFException.error("Height not specified!", this);
        }
        return this._height;
    }

    public Double getLocPinX() {
        if (this._locPinX == null && this._masterShape != null) {
            return this._masterShape.getLocPinX();
        }
        if (this._locPinX == null) {
            throw XDGFException.error("LocPinX not specified!", this);
        }
        return this._locPinX;
    }

    public Double getLocPinY() {
        if (this._locPinY == null && this._masterShape != null) {
            return this._masterShape.getLocPinY();
        }
        if (this._locPinY == null) {
            throw XDGFException.error("LocPinY not specified!", this);
        }
        return this._locPinY;
    }

    public Double getBeginX() {
        if (this._beginX == null && this._masterShape != null) {
            return this._masterShape.getBeginX();
        }
        return this._beginX;
    }

    public Double getBeginY() {
        if (this._beginY == null && this._masterShape != null) {
            return this._masterShape.getBeginY();
        }
        return this._beginY;
    }

    public Double getEndX() {
        if (this._endX == null && this._masterShape != null) {
            return this._masterShape.getEndX();
        }
        return this._endX;
    }

    public Double getEndY() {
        if (this._endY == null && this._masterShape != null) {
            return this._masterShape.getEndY();
        }
        return this._endY;
    }

    public Double getAngle() {
        if (this._angle == null && this._masterShape != null) {
            return this._masterShape.getAngle();
        }
        return this._angle;
    }

    public Boolean getFlipX() {
        if (this._flipX == null && this._masterShape != null) {
            return this._masterShape.getFlipX();
        }
        return this._flipX;
    }

    public Boolean getFlipY() {
        if (this._flipY == null && this._masterShape != null) {
            return this._masterShape.getFlipY();
        }
        return this._flipY;
    }

    public Double getTxtPinX() {
        if (this._txtPinX == null && this._masterShape != null && this._masterShape._txtPinX != null) {
            return this._masterShape._txtPinX;
        }
        if (this._txtPinX == null) {
            return Double.valueOf(getWidth().doubleValue() * 0.5d);
        }
        return this._txtPinX;
    }

    public Double getTxtPinY() {
        if (this._txtLocPinY == null && this._masterShape != null && this._masterShape._txtLocPinY != null) {
            return this._masterShape._txtLocPinY;
        }
        if (this._txtPinY == null) {
            return Double.valueOf(getHeight().doubleValue() * 0.5d);
        }
        return this._txtPinY;
    }

    public Double getTxtLocPinX() {
        if (this._txtLocPinX == null && this._masterShape != null && this._masterShape._txtLocPinX != null) {
            return this._masterShape._txtLocPinX;
        }
        if (this._txtLocPinX == null) {
            return Double.valueOf(getTxtWidth().doubleValue() * 0.5d);
        }
        return this._txtLocPinX;
    }

    public Double getTxtLocPinY() {
        if (this._txtLocPinY == null && this._masterShape != null && this._masterShape._txtLocPinY != null) {
            return this._masterShape._txtLocPinY;
        }
        if (this._txtLocPinY == null) {
            return Double.valueOf(getTxtHeight().doubleValue() * 0.5d);
        }
        return this._txtLocPinY;
    }

    public Double getTxtAngle() {
        if (this._txtAngle == null && this._masterShape != null) {
            return this._masterShape.getTxtAngle();
        }
        return this._txtAngle;
    }

    public Double getTxtWidth() {
        if (this._txtWidth == null && this._masterShape != null && this._masterShape._txtWidth != null) {
            return this._masterShape._txtWidth;
        }
        if (this._txtWidth == null) {
            return getWidth();
        }
        return this._txtWidth;
    }

    public Double getTxtHeight() {
        if (this._txtHeight == null && this._masterShape != null && this._masterShape._txtHeight != null) {
            return this._masterShape._txtHeight;
        }
        if (this._txtHeight == null) {
            return getHeight();
        }
        return this._txtHeight;
    }

    @Override // org.apache.poi.xdgf.usermodel.XDGFSheet
    public Integer getLineCap() {
        Integer lineCap = super.getLineCap();
        if (lineCap != null) {
            return lineCap;
        }
        if (this._masterShape != null) {
            return this._masterShape.getLineCap();
        }
        return this._document.getDefaultLineStyle().getLineCap();
    }

    @Override // org.apache.poi.xdgf.usermodel.XDGFSheet
    public Color getLineColor() {
        Color lineColor = super.getLineColor();
        if (lineColor != null) {
            return lineColor;
        }
        if (this._masterShape != null) {
            return this._masterShape.getLineColor();
        }
        return this._document.getDefaultLineStyle().getLineColor();
    }

    @Override // org.apache.poi.xdgf.usermodel.XDGFSheet
    public Integer getLinePattern() {
        Integer linePattern = super.getLinePattern();
        if (linePattern != null) {
            return linePattern;
        }
        if (this._masterShape != null) {
            return this._masterShape.getLinePattern();
        }
        return this._document.getDefaultLineStyle().getLinePattern();
    }

    @Override // org.apache.poi.xdgf.usermodel.XDGFSheet
    public Double getLineWeight() {
        Double lineWeight = super.getLineWeight();
        if (lineWeight != null) {
            return lineWeight;
        }
        if (this._masterShape != null) {
            return this._masterShape.getLineWeight();
        }
        return this._document.getDefaultLineStyle().getLineWeight();
    }

    @Override // org.apache.poi.xdgf.usermodel.XDGFSheet
    public Color getFontColor() {
        Color fontColor = super.getFontColor();
        if (fontColor != null) {
            return fontColor;
        }
        if (this._masterShape != null) {
            return this._masterShape.getFontColor();
        }
        return this._document.getDefaultTextStyle().getFontColor();
    }

    @Override // org.apache.poi.xdgf.usermodel.XDGFSheet
    public Double getFontSize() {
        Double fontSize = super.getFontSize();
        if (fontSize != null) {
            return fontSize;
        }
        if (this._masterShape != null) {
            return this._masterShape.getFontSize();
        }
        return this._document.getDefaultTextStyle().getFontSize();
    }

    public Stroke getStroke() {
        int cap;
        float lineWeight = getLineWeight().floatValue();
        switch (getLineCap().intValue()) {
            case 0:
                cap = 1;
                break;
            case 1:
                cap = 2;
                break;
            case 2:
                cap = 0;
                break;
            default:
                throw new POIXMLException("Invalid line cap specified");
        }
        float[] dash = null;
        switch (getLinePattern().intValue()) {
            case 0:
            case 1:
                break;
            case 2:
                dash = new float[]{5.0f, 3.0f};
                break;
            case 3:
                dash = new float[]{1.0f, 4.0f};
                break;
            case 4:
                dash = new float[]{6.0f, 3.0f, 1.0f, 3.0f};
                break;
            case 5:
                dash = new float[]{6.0f, 3.0f, 1.0f, 3.0f, 1.0f, 3.0f};
                break;
            case 6:
                dash = new float[]{1.0f, 3.0f, 6.0f, 3.0f, 6.0f, 3.0f};
                break;
            case 7:
                dash = new float[]{15.0f, 3.0f, 6.0f, 3.0f};
                break;
            case 8:
                dash = new float[]{6.0f, 3.0f, 6.0f, 3.0f};
                break;
            case 9:
                dash = new float[]{3.0f, 2.0f};
                break;
            case 10:
                dash = new float[]{1.0f, 2.0f};
                break;
            case 11:
                dash = new float[]{3.0f, 2.0f, 1.0f, 2.0f};
                break;
            case 12:
                dash = new float[]{3.0f, 2.0f, 1.0f, 2.0f, 1.0f};
                break;
            case 13:
                dash = new float[]{1.0f, 2.0f, 3.0f, 2.0f, 3.0f, 2.0f};
                break;
            case 14:
                dash = new float[]{3.0f, 2.0f, 7.0f, 2.0f};
                break;
            case 15:
                dash = new float[]{7.0f, 2.0f, 3.0f, 2.0f, 3.0f, 2.0f};
                break;
            case 16:
                dash = new float[]{12.0f, 6.0f};
                break;
            case 17:
                dash = new float[]{1.0f, 6.0f};
                break;
            case 18:
                dash = new float[]{1.0f, 6.0f, 12.0f, 6.0f};
                break;
            case 19:
                dash = new float[]{1.0f, 6.0f, 1.0f, 6.0f, 12.0f, 6.0f};
                break;
            case 20:
                dash = new float[]{1.0f, 6.0f, 12.0f, 6.0f, 12.0f, 6.0f};
                break;
            case 21:
                dash = new float[]{30.0f, 6.0f, 12.0f, 6.0f};
                break;
            case 22:
                dash = new float[]{30.0f, 6.0f, 12.0f, 6.0f, 12.0f, 6.0f};
                break;
            case 23:
                dash = new float[]{1.0f};
                break;
            case 254:
                throw new POIXMLException("Unsupported line pattern value");
            default:
                throw new POIXMLException("Invalid line pattern value");
        }
        if (dash != null) {
            for (int i = 0; i < dash.length; i++) {
                float[] fArr = dash;
                int i2 = i;
                fArr[i2] = fArr[i2] * lineWeight;
            }
        }
        return new BasicStroke(lineWeight, cap, 0, 10.0f, dash, 0.0f);
    }

    public Iterable<GeometrySection> getGeometrySections() {
        return new CombinedIterable(this._geometry, this._masterShape != null ? this._masterShape._geometry : null);
    }

    public Rectangle2D.Double getBounds() {
        return new Rectangle2D.Double(0.0d, 0.0d, getWidth().doubleValue(), getHeight().doubleValue());
    }

    public Path2D.Double getBoundsAsPath() {
        Double w = getWidth();
        Double h = getHeight();
        Path2D.Double bounds = new Path2D.Double();
        bounds.moveTo(0.0d, 0.0d);
        bounds.lineTo(w.doubleValue(), 0.0d);
        bounds.lineTo(w.doubleValue(), h.doubleValue());
        bounds.lineTo(0.0d, h.doubleValue());
        bounds.lineTo(0.0d, 0.0d);
        return bounds;
    }

    public Path2D.Double getPath() {
        for (GeometrySection geoSection : getGeometrySections()) {
            if (!geoSection.getNoShow().booleanValue()) {
                return geoSection.getPath(this);
            }
        }
        return null;
    }

    public boolean hasGeometry() {
        for (GeometrySection geoSection : getGeometrySections()) {
            if (!geoSection.getNoShow().booleanValue()) {
                return true;
            }
        }
        return false;
    }

    protected AffineTransform getParentTransform() {
        AffineTransform tr = new AffineTransform();
        Double locX = getLocPinX();
        Double locY = getLocPinY();
        Boolean flipX = getFlipX();
        Boolean flipY = getFlipY();
        Double angle = getAngle();
        tr.translate(-locX.doubleValue(), -locY.doubleValue());
        tr.translate(getPinX().doubleValue(), getPinY().doubleValue());
        if (angle != null && Math.abs(angle.doubleValue()) > 0.001d) {
            tr.rotate(angle.doubleValue(), locX.doubleValue(), locY.doubleValue());
        }
        if (flipX != null && flipX.booleanValue()) {
            tr.scale(-1.0d, 1.0d);
            tr.translate(-getWidth().doubleValue(), 0.0d);
        }
        if (flipY != null && flipY.booleanValue()) {
            tr.scale(1.0d, -1.0d);
            tr.translate(0.0d, -getHeight().doubleValue());
        }
        return tr;
    }

    public void visitShapes(ShapeVisitor visitor, AffineTransform tr, int level) {
        AffineTransform tr2 = (AffineTransform) tr.clone();
        tr2.concatenate(getParentTransform());
        try {
            if (visitor.accept(this)) {
                visitor.visit(this, tr2, level);
            }
            if (this._shapes != null) {
                for (XDGFShape shape : this._shapes) {
                    shape.visitShapes(visitor, tr2, level + 1);
                }
            }
        } catch (POIXMLException e) {
            throw XDGFException.wrap(toString(), e);
        } catch (StopVisitingThisBranch e2) {
        }
    }

    public void visitShapes(ShapeVisitor visitor, int level) {
        try {
            if (visitor.accept(this)) {
                visitor.visit(this, null, level);
            }
            if (this._shapes != null) {
                for (XDGFShape shape : this._shapes) {
                    shape.visitShapes(visitor, level + 1);
                }
            }
        } catch (POIXMLException e) {
            throw XDGFException.wrap(toString(), e);
        } catch (StopVisitingThisBranch e2) {
        }
    }
}
