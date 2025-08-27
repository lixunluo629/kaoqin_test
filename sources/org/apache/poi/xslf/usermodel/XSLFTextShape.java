package org.apache.poi.xslf.usermodel;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.POIXMLException;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.apache.poi.sl.draw.DrawFactory;
import org.apache.poi.sl.draw.DrawTextShape;
import org.apache.poi.sl.usermodel.Insets2D;
import org.apache.poi.sl.usermodel.Placeholder;
import org.apache.poi.sl.usermodel.TextShape;
import org.apache.poi.sl.usermodel.VerticalAlignment;
import org.apache.poi.util.Units;
import org.apache.poi.xslf.model.PropertyFetcher;
import org.apache.poi.xslf.model.TextBodyPropertyFetcher;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextWrappingType;
import org.openxmlformats.schemas.presentationml.x2006.main.CTPlaceholder;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xslf/usermodel/XSLFTextShape.class */
public abstract class XSLFTextShape extends XSLFSimpleShape implements TextShape<XSLFShape, XSLFTextParagraph> {
    private final List<XSLFTextParagraph> _paragraphs;
    static final /* synthetic */ boolean $assertionsDisabled;

    protected abstract CTTextBody getTextBody(boolean z);

    static {
        $assertionsDisabled = !XSLFTextShape.class.desiredAssertionStatus();
    }

    XSLFTextShape(XmlObject shape, XSLFSheet sheet) {
        super(shape, sheet);
        this._paragraphs = new ArrayList();
        CTTextBody txBody = getTextBody(false);
        if (txBody != null) {
            CTTextParagraph[] arr$ = txBody.getPArray();
            for (CTTextParagraph p : arr$) {
                this._paragraphs.add(newTextParagraph(p));
            }
        }
    }

    @Override // java.lang.Iterable
    public Iterator<XSLFTextParagraph> iterator() {
        return getTextParagraphs().iterator();
    }

    @Override // org.apache.poi.sl.usermodel.TextShape
    public String getText() {
        StringBuilder out = new StringBuilder();
        for (XSLFTextParagraph p : this._paragraphs) {
            if (out.length() > 0) {
                out.append('\n');
            }
            out.append(p.getText());
        }
        return out.toString();
    }

    public void clearText() {
        this._paragraphs.clear();
        CTTextBody txBody = getTextBody(true);
        txBody.setPArray(null);
    }

    @Override // org.apache.poi.sl.usermodel.TextShape
    public XSLFTextRun setText(String text) {
        if (!this._paragraphs.isEmpty()) {
            CTTextBody txBody = getTextBody(false);
            int cntPs = txBody.sizeOfPArray();
            for (int i = cntPs; i > 1; i--) {
                txBody.removeP(i - 1);
                this._paragraphs.remove(i - 1);
            }
            this._paragraphs.get(0).clearButKeepProperties();
        }
        return appendText(text, false);
    }

    @Override // org.apache.poi.sl.usermodel.TextShape
    public XSLFTextRun appendText(String text, boolean newParagraph) {
        boolean firstPara;
        XSLFTextParagraph para;
        CTTextParagraph ctp;
        CTTextCharacterProperties unexpectedRPr;
        if (text == null) {
            return null;
        }
        CTTextParagraphProperties otherPPr = null;
        CTTextCharacterProperties otherRPr = null;
        if (this._paragraphs.isEmpty()) {
            firstPara = false;
            para = null;
        } else {
            firstPara = !newParagraph;
            para = this._paragraphs.get(this._paragraphs.size() - 1);
            CTTextParagraph ctp2 = para.getXmlObject();
            otherPPr = ctp2.getPPr();
            List<XSLFTextRun> runs = para.getTextRuns();
            if (!runs.isEmpty()) {
                XSLFTextRun r0 = runs.get(runs.size() - 1);
                otherRPr = r0.getRPr(false);
                if (otherRPr == null) {
                    otherRPr = ctp2.getEndParaRPr();
                }
            }
        }
        XSLFTextRun run = null;
        String[] arr$ = text.split("\\r\\n?|\\n");
        for (String lineTxt : arr$) {
            if (!firstPara) {
                if (para != null && (unexpectedRPr = (ctp = para.getXmlObject()).getEndParaRPr()) != null && unexpectedRPr != otherRPr) {
                    ctp.unsetEndParaRPr();
                }
                para = addNewTextParagraph();
                if (otherPPr != null) {
                    para.getXmlObject().setPPr(otherPPr);
                }
            }
            boolean firstRun = true;
            String[] arr$2 = lineTxt.split("[\u000b]");
            for (String runText : arr$2) {
                if (!firstRun) {
                    para.addLineBreak();
                }
                run = para.addNewTextRun();
                run.setText(runText);
                if (otherRPr != null) {
                    run.getRPr(true).set(otherRPr);
                }
                firstRun = false;
            }
            firstPara = false;
        }
        if ($assertionsDisabled || run != null) {
            return run;
        }
        throw new AssertionError();
    }

    @Override // org.apache.poi.sl.usermodel.TextShape
    public List<XSLFTextParagraph> getTextParagraphs() {
        return this._paragraphs;
    }

    public XSLFTextParagraph addNewTextParagraph() {
        CTTextParagraph p;
        CTTextBody txBody = getTextBody(false);
        if (txBody == null) {
            p = getTextBody(true).getPArray(0);
            p.removeR(0);
        } else {
            p = txBody.addNewP();
        }
        XSLFTextParagraph paragraph = newTextParagraph(p);
        this._paragraphs.add(paragraph);
        return paragraph;
    }

    public void setVerticalAlignment(VerticalAlignment anchor) {
        CTTextBodyProperties bodyPr = getTextBodyPr(true);
        if (bodyPr != null) {
            if (anchor == null) {
                if (bodyPr.isSetAnchor()) {
                    bodyPr.unsetAnchor();
                    return;
                }
                return;
            }
            bodyPr.setAnchor(STTextAnchoringType.Enum.forInt(anchor.ordinal() + 1));
        }
    }

    public VerticalAlignment getVerticalAlignment() {
        PropertyFetcher<VerticalAlignment> fetcher = new TextBodyPropertyFetcher<VerticalAlignment>() { // from class: org.apache.poi.xslf.usermodel.XSLFTextShape.1
            @Override // org.apache.poi.xslf.model.TextBodyPropertyFetcher
            public boolean fetch(CTTextBodyProperties props) {
                if (props.isSetAnchor()) {
                    int val = props.getAnchor().intValue();
                    setValue(VerticalAlignment.values()[val - 1]);
                    return true;
                }
                return false;
            }
        };
        fetchShapeProperty(fetcher);
        return fetcher.getValue() == null ? VerticalAlignment.TOP : fetcher.getValue();
    }

    @Override // org.apache.poi.sl.usermodel.TextShape
    public void setHorizontalCentered(Boolean isCentered) {
        CTTextBodyProperties bodyPr = getTextBodyPr(true);
        if (bodyPr != null) {
            if (isCentered == null) {
                if (bodyPr.isSetAnchorCtr()) {
                    bodyPr.unsetAnchorCtr();
                    return;
                }
                return;
            }
            bodyPr.setAnchorCtr(isCentered.booleanValue());
        }
    }

    @Override // org.apache.poi.sl.usermodel.TextShape
    public boolean isHorizontalCentered() {
        PropertyFetcher<Boolean> fetcher = new TextBodyPropertyFetcher<Boolean>() { // from class: org.apache.poi.xslf.usermodel.XSLFTextShape.2
            @Override // org.apache.poi.xslf.model.TextBodyPropertyFetcher
            public boolean fetch(CTTextBodyProperties props) {
                if (props.isSetAnchorCtr()) {
                    setValue(Boolean.valueOf(props.getAnchorCtr()));
                    return true;
                }
                return false;
            }
        };
        fetchShapeProperty(fetcher);
        if (fetcher.getValue() == null) {
            return false;
        }
        return fetcher.getValue().booleanValue();
    }

    public void setTextDirection(TextShape.TextDirection orientation) {
        CTTextBodyProperties bodyPr = getTextBodyPr(true);
        if (bodyPr != null) {
            if (orientation == null) {
                if (bodyPr.isSetVert()) {
                    bodyPr.unsetVert();
                    return;
                }
                return;
            }
            bodyPr.setVert(STTextVerticalType.Enum.forInt(orientation.ordinal() + 1));
        }
    }

    public TextShape.TextDirection getTextDirection() {
        STTextVerticalType.Enum val;
        CTTextBodyProperties bodyPr = getTextBodyPr();
        if (bodyPr != null && (val = bodyPr.getVert()) != null) {
            switch (val.intValue()) {
                case 1:
                default:
                    return TextShape.TextDirection.HORIZONTAL;
                case 2:
                case 5:
                case 6:
                    return TextShape.TextDirection.VERTICAL;
                case 3:
                    return TextShape.TextDirection.VERTICAL_270;
                case 4:
                case 7:
                    return TextShape.TextDirection.STACKED;
            }
        }
        return TextShape.TextDirection.HORIZONTAL;
    }

    @Override // org.apache.poi.sl.usermodel.TextShape
    public Double getTextRotation() {
        CTTextBodyProperties bodyPr = getTextBodyPr();
        if (bodyPr != null && bodyPr.isSetRot()) {
            return Double.valueOf(bodyPr.getRot() / 60000.0d);
        }
        return null;
    }

    @Override // org.apache.poi.sl.usermodel.TextShape
    public void setTextRotation(Double rotation) {
        CTTextBodyProperties bodyPr = getTextBodyPr(true);
        if (bodyPr != null) {
            bodyPr.setRot((int) (rotation.doubleValue() * 60000.0d));
        }
    }

    public double getBottomInset() {
        PropertyFetcher<Double> fetcher = new TextBodyPropertyFetcher<Double>() { // from class: org.apache.poi.xslf.usermodel.XSLFTextShape.3
            @Override // org.apache.poi.xslf.model.TextBodyPropertyFetcher
            public boolean fetch(CTTextBodyProperties props) {
                if (props.isSetBIns()) {
                    double val = Units.toPoints(props.getBIns());
                    setValue(Double.valueOf(val));
                    return true;
                }
                return false;
            }
        };
        fetchShapeProperty(fetcher);
        if (fetcher.getValue() == null) {
            return 3.6d;
        }
        return fetcher.getValue().doubleValue();
    }

    public double getLeftInset() {
        PropertyFetcher<Double> fetcher = new TextBodyPropertyFetcher<Double>() { // from class: org.apache.poi.xslf.usermodel.XSLFTextShape.4
            @Override // org.apache.poi.xslf.model.TextBodyPropertyFetcher
            public boolean fetch(CTTextBodyProperties props) {
                if (props.isSetLIns()) {
                    double val = Units.toPoints(props.getLIns());
                    setValue(Double.valueOf(val));
                    return true;
                }
                return false;
            }
        };
        fetchShapeProperty(fetcher);
        if (fetcher.getValue() == null) {
            return 7.2d;
        }
        return fetcher.getValue().doubleValue();
    }

    public double getRightInset() {
        PropertyFetcher<Double> fetcher = new TextBodyPropertyFetcher<Double>() { // from class: org.apache.poi.xslf.usermodel.XSLFTextShape.5
            @Override // org.apache.poi.xslf.model.TextBodyPropertyFetcher
            public boolean fetch(CTTextBodyProperties props) {
                if (props.isSetRIns()) {
                    double val = Units.toPoints(props.getRIns());
                    setValue(Double.valueOf(val));
                    return true;
                }
                return false;
            }
        };
        fetchShapeProperty(fetcher);
        if (fetcher.getValue() == null) {
            return 7.2d;
        }
        return fetcher.getValue().doubleValue();
    }

    public double getTopInset() {
        PropertyFetcher<Double> fetcher = new TextBodyPropertyFetcher<Double>() { // from class: org.apache.poi.xslf.usermodel.XSLFTextShape.6
            @Override // org.apache.poi.xslf.model.TextBodyPropertyFetcher
            public boolean fetch(CTTextBodyProperties props) {
                if (props.isSetTIns()) {
                    double val = Units.toPoints(props.getTIns());
                    setValue(Double.valueOf(val));
                    return true;
                }
                return false;
            }
        };
        fetchShapeProperty(fetcher);
        if (fetcher.getValue() == null) {
            return 3.6d;
        }
        return fetcher.getValue().doubleValue();
    }

    public void setBottomInset(double margin) {
        CTTextBodyProperties bodyPr = getTextBodyPr(true);
        if (bodyPr != null) {
            if (margin == -1.0d) {
                bodyPr.unsetBIns();
            } else {
                bodyPr.setBIns(Units.toEMU(margin));
            }
        }
    }

    public void setLeftInset(double margin) {
        CTTextBodyProperties bodyPr = getTextBodyPr(true);
        if (bodyPr != null) {
            if (margin == -1.0d) {
                bodyPr.unsetLIns();
            } else {
                bodyPr.setLIns(Units.toEMU(margin));
            }
        }
    }

    public void setRightInset(double margin) {
        CTTextBodyProperties bodyPr = getTextBodyPr(true);
        if (bodyPr != null) {
            if (margin == -1.0d) {
                bodyPr.unsetRIns();
            } else {
                bodyPr.setRIns(Units.toEMU(margin));
            }
        }
    }

    public void setTopInset(double margin) {
        CTTextBodyProperties bodyPr = getTextBodyPr(true);
        if (bodyPr != null) {
            if (margin == -1.0d) {
                bodyPr.unsetTIns();
            } else {
                bodyPr.setTIns(Units.toEMU(margin));
            }
        }
    }

    @Override // org.apache.poi.sl.usermodel.TextShape
    public Insets2D getInsets() {
        Insets2D insets = new Insets2D(getTopInset(), getLeftInset(), getBottomInset(), getRightInset());
        return insets;
    }

    @Override // org.apache.poi.sl.usermodel.TextShape
    public void setInsets(Insets2D insets) {
        setTopInset(insets.top);
        setLeftInset(insets.left);
        setBottomInset(insets.bottom);
        setRightInset(insets.right);
    }

    @Override // org.apache.poi.sl.usermodel.TextShape
    public boolean getWordWrap() {
        PropertyFetcher<Boolean> fetcher = new TextBodyPropertyFetcher<Boolean>() { // from class: org.apache.poi.xslf.usermodel.XSLFTextShape.7
            @Override // org.apache.poi.xslf.model.TextBodyPropertyFetcher
            public boolean fetch(CTTextBodyProperties props) {
                if (props.isSetWrap()) {
                    setValue(Boolean.valueOf(props.getWrap() == STTextWrappingType.SQUARE));
                    return true;
                }
                return false;
            }
        };
        fetchShapeProperty(fetcher);
        if (fetcher.getValue() == null) {
            return true;
        }
        return fetcher.getValue().booleanValue();
    }

    @Override // org.apache.poi.sl.usermodel.TextShape
    public void setWordWrap(boolean wrap) {
        CTTextBodyProperties bodyPr = getTextBodyPr(true);
        if (bodyPr != null) {
            bodyPr.setWrap(wrap ? STTextWrappingType.SQUARE : STTextWrappingType.NONE);
        }
    }

    public void setTextAutofit(TextShape.TextAutofit value) {
        CTTextBodyProperties bodyPr = getTextBodyPr(true);
        if (bodyPr != null) {
            if (bodyPr.isSetSpAutoFit()) {
                bodyPr.unsetSpAutoFit();
            }
            if (bodyPr.isSetNoAutofit()) {
                bodyPr.unsetNoAutofit();
            }
            if (bodyPr.isSetNormAutofit()) {
                bodyPr.unsetNormAutofit();
            }
            switch (value) {
                case NONE:
                    bodyPr.addNewNoAutofit();
                    break;
                case NORMAL:
                    bodyPr.addNewNormAutofit();
                    break;
                case SHAPE:
                    bodyPr.addNewSpAutoFit();
                    break;
            }
        }
    }

    public TextShape.TextAutofit getTextAutofit() {
        CTTextBodyProperties bodyPr = getTextBodyPr();
        if (bodyPr != null) {
            if (bodyPr.isSetNoAutofit()) {
                return TextShape.TextAutofit.NONE;
            }
            if (bodyPr.isSetNormAutofit()) {
                return TextShape.TextAutofit.NORMAL;
            }
            if (bodyPr.isSetSpAutoFit()) {
                return TextShape.TextAutofit.SHAPE;
            }
        }
        return TextShape.TextAutofit.NORMAL;
    }

    protected CTTextBodyProperties getTextBodyPr() {
        return getTextBodyPr(false);
    }

    protected CTTextBodyProperties getTextBodyPr(boolean create) {
        CTTextBody textBody = getTextBody(create);
        if (textBody == null) {
            return null;
        }
        CTTextBodyProperties textBodyPr = textBody.getBodyPr();
        if (textBodyPr == null && create) {
            textBodyPr = textBody.addNewBodyPr();
        }
        return textBodyPr;
    }

    @Override // org.apache.poi.xslf.usermodel.XSLFSimpleShape, org.apache.poi.xslf.usermodel.XSLFShape, org.apache.poi.sl.usermodel.SimpleShape
    public void setPlaceholder(Placeholder placeholder) {
        super.setPlaceholder(placeholder);
    }

    public Placeholder getTextType() {
        CTPlaceholder ph = getCTPlaceholder();
        if (ph == null) {
            return null;
        }
        int val = ph.getType().intValue();
        return Placeholder.lookupOoxml(val);
    }

    @Override // org.apache.poi.sl.usermodel.TextShape
    public double getTextHeight() {
        return getTextHeight(null);
    }

    @Override // org.apache.poi.sl.usermodel.TextShape
    public double getTextHeight(Graphics2D graphics) {
        DrawFactory drawFact = DrawFactory.getInstance(graphics);
        DrawTextShape dts = drawFact.getDrawable((TextShape<?, ?>) this);
        return dts.getTextHeight(graphics);
    }

    @Override // org.apache.poi.sl.usermodel.TextShape
    public Rectangle2D resizeToFitText() {
        return resizeToFitText(null);
    }

    @Override // org.apache.poi.sl.usermodel.TextShape
    public Rectangle2D resizeToFitText(Graphics2D graphics) {
        Rectangle2D anchor = getAnchor();
        if (anchor.getWidth() == 0.0d) {
            throw new POIXMLException("Anchor of the shape was not set.");
        }
        double height = getTextHeight(graphics);
        double height2 = height + 1.0d;
        Insets2D insets = getInsets();
        anchor.setRect(anchor.getX(), anchor.getY(), anchor.getWidth(), height2 + insets.top + insets.bottom);
        setAnchor(anchor);
        return anchor;
    }

    @Override // org.apache.poi.xslf.usermodel.XSLFSimpleShape, org.apache.poi.xslf.usermodel.XSLFShape
    void copy(XSLFShape other) throws InvalidOperationException {
        super.copy(other);
        XSLFTextShape otherTS = (XSLFTextShape) other;
        CTTextBody otherTB = otherTS.getTextBody(false);
        CTTextBody thisTB = getTextBody(true);
        if (otherTB == null) {
            return;
        }
        thisTB.setBodyPr((CTTextBodyProperties) otherTB.getBodyPr().copy());
        if (thisTB.isSetLstStyle()) {
            thisTB.unsetLstStyle();
        }
        if (otherTB.isSetLstStyle()) {
            thisTB.setLstStyle((CTTextListStyle) otherTB.getLstStyle().copy());
        }
        boolean srcWordWrap = otherTS.getWordWrap();
        if (srcWordWrap != getWordWrap()) {
            setWordWrap(srcWordWrap);
        }
        double leftInset = otherTS.getLeftInset();
        if (leftInset != getLeftInset()) {
            setLeftInset(leftInset);
        }
        double rightInset = otherTS.getRightInset();
        if (rightInset != getRightInset()) {
            setRightInset(rightInset);
        }
        double topInset = otherTS.getTopInset();
        if (topInset != getTopInset()) {
            setTopInset(topInset);
        }
        double bottomInset = otherTS.getBottomInset();
        if (bottomInset != getBottomInset()) {
            setBottomInset(bottomInset);
        }
        VerticalAlignment vAlign = otherTS.getVerticalAlignment();
        if (vAlign != getVerticalAlignment()) {
            setVerticalAlignment(vAlign);
        }
        clearText();
        for (XSLFTextParagraph srcP : otherTS.getTextParagraphs()) {
            XSLFTextParagraph tgtP = addNewTextParagraph();
            tgtP.copy(srcP);
        }
    }

    @Override // org.apache.poi.sl.usermodel.TextShape
    public void setTextPlaceholder(TextShape.TextPlaceholder placeholder) {
        switch (placeholder) {
            case NOTES:
            case HALF_BODY:
            case QUARTER_BODY:
            case BODY:
            default:
                setPlaceholder(Placeholder.BODY);
                break;
            case TITLE:
                setPlaceholder(Placeholder.TITLE);
                break;
            case CENTER_BODY:
                setPlaceholder(Placeholder.BODY);
                setHorizontalCentered(true);
                break;
            case CENTER_TITLE:
                setPlaceholder(Placeholder.CENTERED_TITLE);
                break;
            case OTHER:
                setPlaceholder(Placeholder.CONTENT);
                break;
        }
    }

    @Override // org.apache.poi.sl.usermodel.TextShape
    public TextShape.TextPlaceholder getTextPlaceholder() {
        Placeholder ph = getTextType();
        if (ph == null) {
            return TextShape.TextPlaceholder.BODY;
        }
        switch (ph) {
        }
        return TextShape.TextPlaceholder.BODY;
    }

    protected XSLFTextParagraph newTextParagraph(CTTextParagraph p) {
        return new XSLFTextParagraph(p, this);
    }
}
