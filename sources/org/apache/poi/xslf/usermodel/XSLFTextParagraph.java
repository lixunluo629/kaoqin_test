package org.apache.poi.xslf.usermodel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.sl.draw.DrawPaint;
import org.apache.poi.sl.usermodel.AutoNumberingScheme;
import org.apache.poi.sl.usermodel.PaintStyle;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.util.Internal;
import org.apache.poi.util.Units;
import org.apache.poi.xslf.model.ParagraphPropertyFetcher;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.drawingml.x2006.main.CTColor;
import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextAutonumberBullet;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizePercent;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizePoint;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharBullet;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextField;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextLineBreak;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStopList;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextAlignType;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextAutonumberScheme;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextFontAlignType;
import org.openxmlformats.schemas.presentationml.x2006.main.CTPlaceholder;
import org.springframework.beans.PropertyAccessor;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xslf/usermodel/XSLFTextParagraph.class */
public class XSLFTextParagraph implements TextParagraph<XSLFShape, XSLFTextParagraph, XSLFTextRun> {
    private final CTTextParagraph _p;
    private final List<XSLFTextRun> _runs = new ArrayList();
    private final XSLFTextShape _shape;

    XSLFTextParagraph(CTTextParagraph p, XSLFTextShape shape) {
        this._p = p;
        this._shape = shape;
        XmlCursor c = this._p.newCursor();
        try {
            if (c.toFirstChild()) {
                do {
                    XmlObject r = c.getObject();
                    if (r instanceof CTTextLineBreak) {
                        this._runs.add(new XSLFLineBreak((CTTextLineBreak) r, this));
                    } else if ((r instanceof CTRegularTextRun) || (r instanceof CTTextField)) {
                        this._runs.add(new XSLFTextRun(r, this));
                    }
                } while (c.toNextSibling());
            }
        } finally {
            c.dispose();
        }
    }

    public String getText() {
        StringBuilder out = new StringBuilder();
        for (XSLFTextRun r : this._runs) {
            out.append(r.getRawText());
        }
        return out.toString();
    }

    String getRenderableText() {
        StringBuilder out = new StringBuilder();
        for (XSLFTextRun r : this._runs) {
            out.append(r.getRenderableText());
        }
        return out.toString();
    }

    @Internal
    public CTTextParagraph getXmlObject() {
        return this._p;
    }

    @Override // org.apache.poi.sl.usermodel.TextParagraph
    public XSLFTextShape getParentShape() {
        return this._shape;
    }

    @Override // org.apache.poi.sl.usermodel.TextParagraph
    public List<XSLFTextRun> getTextRuns() {
        return this._runs;
    }

    @Override // java.lang.Iterable
    public Iterator<XSLFTextRun> iterator() {
        return this._runs.iterator();
    }

    public XSLFTextRun addNewTextRun() {
        CTRegularTextRun r = this._p.addNewR();
        CTTextCharacterProperties rPr = r.addNewRPr();
        rPr.setLang("en-US");
        XSLFTextRun run = newTextRun(r);
        this._runs.add(run);
        return run;
    }

    public XSLFTextRun addLineBreak() {
        XSLFLineBreak run = new XSLFLineBreak(this._p.addNewBr(), this);
        CTTextCharacterProperties brProps = run.getRPr(true);
        if (this._runs.size() > 0) {
            CTTextCharacterProperties prevRun = this._runs.get(this._runs.size() - 1).getRPr(true);
            brProps.set(prevRun);
        }
        this._runs.add(run);
        return run;
    }

    @Override // org.apache.poi.sl.usermodel.TextParagraph
    public TextParagraph.TextAlign getTextAlign() {
        ParagraphPropertyFetcher<TextParagraph.TextAlign> fetcher = new ParagraphPropertyFetcher<TextParagraph.TextAlign>(getIndentLevel()) { // from class: org.apache.poi.xslf.usermodel.XSLFTextParagraph.1
            @Override // org.apache.poi.xslf.model.ParagraphPropertyFetcher
            public boolean fetch(CTTextParagraphProperties props) {
                if (props.isSetAlgn()) {
                    TextParagraph.TextAlign val = TextParagraph.TextAlign.values()[props.getAlgn().intValue() - 1];
                    setValue(val);
                    return true;
                }
                return false;
            }
        };
        fetchParagraphProperty(fetcher);
        return fetcher.getValue();
    }

    @Override // org.apache.poi.sl.usermodel.TextParagraph
    public void setTextAlign(TextParagraph.TextAlign align) {
        CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
        if (align == null) {
            if (pr.isSetAlgn()) {
                pr.unsetAlgn();
                return;
            }
            return;
        }
        pr.setAlgn(STTextAlignType.Enum.forInt(align.ordinal() + 1));
    }

    @Override // org.apache.poi.sl.usermodel.TextParagraph
    public TextParagraph.FontAlign getFontAlign() {
        ParagraphPropertyFetcher<TextParagraph.FontAlign> fetcher = new ParagraphPropertyFetcher<TextParagraph.FontAlign>(getIndentLevel()) { // from class: org.apache.poi.xslf.usermodel.XSLFTextParagraph.2
            @Override // org.apache.poi.xslf.model.ParagraphPropertyFetcher
            public boolean fetch(CTTextParagraphProperties props) {
                if (props.isSetFontAlgn()) {
                    TextParagraph.FontAlign val = TextParagraph.FontAlign.values()[props.getFontAlgn().intValue() - 1];
                    setValue(val);
                    return true;
                }
                return false;
            }
        };
        fetchParagraphProperty(fetcher);
        return fetcher.getValue();
    }

    public void setFontAlign(TextParagraph.FontAlign align) {
        CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
        if (align == null) {
            if (pr.isSetFontAlgn()) {
                pr.unsetFontAlgn();
                return;
            }
            return;
        }
        pr.setFontAlgn(STTextFontAlignType.Enum.forInt(align.ordinal() + 1));
    }

    public String getBulletFont() {
        ParagraphPropertyFetcher<String> fetcher = new ParagraphPropertyFetcher<String>(getIndentLevel()) { // from class: org.apache.poi.xslf.usermodel.XSLFTextParagraph.3
            @Override // org.apache.poi.xslf.model.ParagraphPropertyFetcher
            public boolean fetch(CTTextParagraphProperties props) {
                if (props.isSetBuFont()) {
                    setValue(props.getBuFont().getTypeface());
                    return true;
                }
                return false;
            }
        };
        fetchParagraphProperty(fetcher);
        return fetcher.getValue();
    }

    public void setBulletFont(String typeface) {
        CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
        CTTextFont font = pr.isSetBuFont() ? pr.getBuFont() : pr.addNewBuFont();
        font.setTypeface(typeface);
    }

    public String getBulletCharacter() {
        ParagraphPropertyFetcher<String> fetcher = new ParagraphPropertyFetcher<String>(getIndentLevel()) { // from class: org.apache.poi.xslf.usermodel.XSLFTextParagraph.4
            @Override // org.apache.poi.xslf.model.ParagraphPropertyFetcher
            public boolean fetch(CTTextParagraphProperties props) {
                if (props.isSetBuChar()) {
                    setValue(props.getBuChar().getChar());
                    return true;
                }
                return false;
            }
        };
        fetchParagraphProperty(fetcher);
        return fetcher.getValue();
    }

    public void setBulletCharacter(String str) {
        CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
        CTTextCharBullet c = pr.isSetBuChar() ? pr.getBuChar() : pr.addNewBuChar();
        c.setChar(str);
    }

    public PaintStyle getBulletFontColor() {
        final XSLFTheme theme = getParentShape().getSheet().getTheme();
        ParagraphPropertyFetcher<Color> fetcher = new ParagraphPropertyFetcher<Color>(getIndentLevel()) { // from class: org.apache.poi.xslf.usermodel.XSLFTextParagraph.5
            @Override // org.apache.poi.xslf.model.ParagraphPropertyFetcher
            public boolean fetch(CTTextParagraphProperties props) {
                if (props.isSetBuClr()) {
                    XSLFColor c = new XSLFColor(props.getBuClr(), theme, null);
                    setValue(c.getColor());
                    return true;
                }
                return false;
            }
        };
        fetchParagraphProperty(fetcher);
        Color col = fetcher.getValue();
        if (col == null) {
            return null;
        }
        return DrawPaint.createSolidPaint(col);
    }

    public void setBulletFontColor(Color color) {
        setBulletFontColor(DrawPaint.createSolidPaint(color));
    }

    public void setBulletFontColor(PaintStyle color) {
        if (!(color instanceof PaintStyle.SolidPaint)) {
            throw new IllegalArgumentException("Currently XSLF only supports SolidPaint");
        }
        PaintStyle.SolidPaint sp = (PaintStyle.SolidPaint) color;
        Color col = DrawPaint.applyColorTransform(sp.getSolidColor());
        CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
        CTColor c = pr.isSetBuClr() ? pr.getBuClr() : pr.addNewBuClr();
        CTSRgbColor clr = c.isSetSrgbClr() ? c.getSrgbClr() : c.addNewSrgbClr();
        clr.setVal(new byte[]{(byte) col.getRed(), (byte) col.getGreen(), (byte) col.getBlue()});
    }

    public Double getBulletFontSize() {
        ParagraphPropertyFetcher<Double> fetcher = new ParagraphPropertyFetcher<Double>(getIndentLevel()) { // from class: org.apache.poi.xslf.usermodel.XSLFTextParagraph.6
            @Override // org.apache.poi.xslf.model.ParagraphPropertyFetcher
            public boolean fetch(CTTextParagraphProperties props) {
                if (props.isSetBuSzPct()) {
                    setValue(Double.valueOf(props.getBuSzPct().getVal() * 0.001d));
                    return true;
                }
                if (props.isSetBuSzPts()) {
                    setValue(Double.valueOf((-props.getBuSzPts().getVal()) * 0.01d));
                    return true;
                }
                return false;
            }
        };
        fetchParagraphProperty(fetcher);
        return fetcher.getValue();
    }

    public void setBulletFontSize(double bulletSize) {
        CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
        if (bulletSize >= 0.0d) {
            CTTextBulletSizePercent pt = pr.isSetBuSzPct() ? pr.getBuSzPct() : pr.addNewBuSzPct();
            pt.setVal((int) (bulletSize * 1000.0d));
            if (pr.isSetBuSzPts()) {
                pr.unsetBuSzPts();
                return;
            }
            return;
        }
        CTTextBulletSizePoint pt2 = pr.isSetBuSzPts() ? pr.getBuSzPts() : pr.addNewBuSzPts();
        pt2.setVal((int) ((-bulletSize) * 100.0d));
        if (pr.isSetBuSzPct()) {
            pr.unsetBuSzPct();
        }
    }

    public AutoNumberingScheme getAutoNumberingScheme() {
        ParagraphPropertyFetcher<AutoNumberingScheme> fetcher = new ParagraphPropertyFetcher<AutoNumberingScheme>(getIndentLevel()) { // from class: org.apache.poi.xslf.usermodel.XSLFTextParagraph.7
            @Override // org.apache.poi.xslf.model.ParagraphPropertyFetcher
            public boolean fetch(CTTextParagraphProperties props) {
                AutoNumberingScheme ans;
                if (props.isSetBuAutoNum() && (ans = AutoNumberingScheme.forOoxmlID(props.getBuAutoNum().getType().intValue())) != null) {
                    setValue(ans);
                    return true;
                }
                return false;
            }
        };
        fetchParagraphProperty(fetcher);
        return fetcher.getValue();
    }

    public Integer getAutoNumberingStartAt() {
        ParagraphPropertyFetcher<Integer> fetcher = new ParagraphPropertyFetcher<Integer>(getIndentLevel()) { // from class: org.apache.poi.xslf.usermodel.XSLFTextParagraph.8
            @Override // org.apache.poi.xslf.model.ParagraphPropertyFetcher
            public boolean fetch(CTTextParagraphProperties props) {
                if (props.isSetBuAutoNum() && props.getBuAutoNum().isSetStartAt()) {
                    setValue(Integer.valueOf(props.getBuAutoNum().getStartAt()));
                    return true;
                }
                return false;
            }
        };
        fetchParagraphProperty(fetcher);
        return fetcher.getValue();
    }

    @Override // org.apache.poi.sl.usermodel.TextParagraph
    public void setIndent(Double indent) {
        if (indent != null || this._p.isSetPPr()) {
            CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
            if (indent == null) {
                if (pr.isSetIndent()) {
                    pr.unsetIndent();
                    return;
                }
                return;
            }
            pr.setIndent(Units.toEMU(indent.doubleValue()));
        }
    }

    @Override // org.apache.poi.sl.usermodel.TextParagraph
    public Double getIndent() {
        ParagraphPropertyFetcher<Double> fetcher = new ParagraphPropertyFetcher<Double>(getIndentLevel()) { // from class: org.apache.poi.xslf.usermodel.XSLFTextParagraph.9
            @Override // org.apache.poi.xslf.model.ParagraphPropertyFetcher
            public boolean fetch(CTTextParagraphProperties props) {
                if (props.isSetIndent()) {
                    setValue(Double.valueOf(Units.toPoints(props.getIndent())));
                    return true;
                }
                return false;
            }
        };
        fetchParagraphProperty(fetcher);
        return fetcher.getValue();
    }

    @Override // org.apache.poi.sl.usermodel.TextParagraph
    public void setLeftMargin(Double leftMargin) {
        if (leftMargin != null || this._p.isSetPPr()) {
            CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
            if (leftMargin == null) {
                if (pr.isSetMarL()) {
                    pr.unsetMarL();
                    return;
                }
                return;
            }
            pr.setMarL(Units.toEMU(leftMargin.doubleValue()));
        }
    }

    @Override // org.apache.poi.sl.usermodel.TextParagraph
    public Double getLeftMargin() {
        ParagraphPropertyFetcher<Double> fetcher = new ParagraphPropertyFetcher<Double>(getIndentLevel()) { // from class: org.apache.poi.xslf.usermodel.XSLFTextParagraph.10
            @Override // org.apache.poi.xslf.model.ParagraphPropertyFetcher
            public boolean fetch(CTTextParagraphProperties props) {
                if (props.isSetMarL()) {
                    double val = Units.toPoints(props.getMarL());
                    setValue(Double.valueOf(val));
                    return true;
                }
                return false;
            }
        };
        fetchParagraphProperty(fetcher);
        return fetcher.getValue();
    }

    @Override // org.apache.poi.sl.usermodel.TextParagraph
    public void setRightMargin(Double rightMargin) {
        if (rightMargin != null || this._p.isSetPPr()) {
            CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
            if (rightMargin == null) {
                if (pr.isSetMarR()) {
                    pr.unsetMarR();
                    return;
                }
                return;
            }
            pr.setMarR(Units.toEMU(rightMargin.doubleValue()));
        }
    }

    @Override // org.apache.poi.sl.usermodel.TextParagraph
    public Double getRightMargin() {
        ParagraphPropertyFetcher<Double> fetcher = new ParagraphPropertyFetcher<Double>(getIndentLevel()) { // from class: org.apache.poi.xslf.usermodel.XSLFTextParagraph.11
            @Override // org.apache.poi.xslf.model.ParagraphPropertyFetcher
            public boolean fetch(CTTextParagraphProperties props) {
                if (props.isSetMarR()) {
                    double val = Units.toPoints(props.getMarR());
                    setValue(Double.valueOf(val));
                    return true;
                }
                return false;
            }
        };
        fetchParagraphProperty(fetcher);
        return fetcher.getValue();
    }

    @Override // org.apache.poi.sl.usermodel.TextParagraph
    public Double getDefaultTabSize() {
        ParagraphPropertyFetcher<Double> fetcher = new ParagraphPropertyFetcher<Double>(getIndentLevel()) { // from class: org.apache.poi.xslf.usermodel.XSLFTextParagraph.12
            @Override // org.apache.poi.xslf.model.ParagraphPropertyFetcher
            public boolean fetch(CTTextParagraphProperties props) {
                if (props.isSetDefTabSz()) {
                    double val = Units.toPoints(props.getDefTabSz());
                    setValue(Double.valueOf(val));
                    return true;
                }
                return false;
            }
        };
        fetchParagraphProperty(fetcher);
        return fetcher.getValue();
    }

    public double getTabStop(final int idx) {
        ParagraphPropertyFetcher<Double> fetcher = new ParagraphPropertyFetcher<Double>(getIndentLevel()) { // from class: org.apache.poi.xslf.usermodel.XSLFTextParagraph.13
            @Override // org.apache.poi.xslf.model.ParagraphPropertyFetcher
            public boolean fetch(CTTextParagraphProperties props) {
                if (props.isSetTabLst()) {
                    CTTextTabStopList tabStops = props.getTabLst();
                    if (idx < tabStops.sizeOfTabArray()) {
                        CTTextTabStop ts = tabStops.getTabArray(idx);
                        double val = Units.toPoints(ts.getPos());
                        setValue(Double.valueOf(val));
                        return true;
                    }
                    return false;
                }
                return false;
            }
        };
        fetchParagraphProperty(fetcher);
        if (fetcher.getValue() == null) {
            return 0.0d;
        }
        return fetcher.getValue().doubleValue();
    }

    public void addTabStop(double value) {
        CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
        CTTextTabStopList tabStops = pr.isSetTabLst() ? pr.getTabLst() : pr.addNewTabLst();
        tabStops.addNewTab().setPos(Units.toEMU(value));
    }

    @Override // org.apache.poi.sl.usermodel.TextParagraph
    public void setLineSpacing(Double lineSpacing) {
        if (lineSpacing != null || this._p.isSetPPr()) {
            CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
            if (lineSpacing == null) {
                if (pr.isSetLnSpc()) {
                    pr.unsetLnSpc();
                    return;
                }
                return;
            }
            CTTextSpacing spc = pr.isSetLnSpc() ? pr.getLnSpc() : pr.addNewLnSpc();
            if (lineSpacing.doubleValue() >= 0.0d) {
                (spc.isSetSpcPct() ? spc.getSpcPct() : spc.addNewSpcPct()).setVal((int) (lineSpacing.doubleValue() * 1000.0d));
                if (spc.isSetSpcPts()) {
                    spc.unsetSpcPts();
                    return;
                }
                return;
            }
            (spc.isSetSpcPts() ? spc.getSpcPts() : spc.addNewSpcPts()).setVal((int) ((-lineSpacing.doubleValue()) * 100.0d));
            if (spc.isSetSpcPct()) {
                spc.unsetSpcPct();
            }
        }
    }

    @Override // org.apache.poi.sl.usermodel.TextParagraph
    public Double getLineSpacing() {
        CTTextNormalAutofit normAutofit;
        ParagraphPropertyFetcher<Double> fetcher = new ParagraphPropertyFetcher<Double>(getIndentLevel()) { // from class: org.apache.poi.xslf.usermodel.XSLFTextParagraph.14
            @Override // org.apache.poi.xslf.model.ParagraphPropertyFetcher
            public boolean fetch(CTTextParagraphProperties props) {
                if (props.isSetLnSpc()) {
                    CTTextSpacing spc = props.getLnSpc();
                    if (!spc.isSetSpcPct()) {
                        if (spc.isSetSpcPts()) {
                            setValue(Double.valueOf((-spc.getSpcPts().getVal()) * 0.01d));
                            return true;
                        }
                        return true;
                    }
                    setValue(Double.valueOf(spc.getSpcPct().getVal() * 0.001d));
                    return true;
                }
                return false;
            }
        };
        fetchParagraphProperty(fetcher);
        Double lnSpc = fetcher.getValue();
        if (lnSpc != null && lnSpc.doubleValue() > 0.0d && (normAutofit = getParentShape().getTextBodyPr().getNormAutofit()) != null) {
            double scale = 1.0d - (normAutofit.getLnSpcReduction() / 100000.0d);
            lnSpc = Double.valueOf(lnSpc.doubleValue() * scale);
        }
        return lnSpc;
    }

    @Override // org.apache.poi.sl.usermodel.TextParagraph
    public void setSpaceBefore(Double spaceBefore) {
        if (spaceBefore == null && !this._p.isSetPPr()) {
            return;
        }
        if (spaceBefore == null) {
            if (this._p.getPPr().isSetSpcBef()) {
                this._p.getPPr().unsetSpcBef();
            }
        } else {
            CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
            CTTextSpacing spc = CTTextSpacing.Factory.newInstance();
            if (spaceBefore.doubleValue() >= 0.0d) {
                spc.addNewSpcPct().setVal((int) (spaceBefore.doubleValue() * 1000.0d));
            } else {
                spc.addNewSpcPts().setVal((int) ((-spaceBefore.doubleValue()) * 100.0d));
            }
            pr.setSpcBef(spc);
        }
    }

    @Override // org.apache.poi.sl.usermodel.TextParagraph
    public Double getSpaceBefore() {
        ParagraphPropertyFetcher<Double> fetcher = new ParagraphPropertyFetcher<Double>(getIndentLevel()) { // from class: org.apache.poi.xslf.usermodel.XSLFTextParagraph.15
            @Override // org.apache.poi.xslf.model.ParagraphPropertyFetcher
            public boolean fetch(CTTextParagraphProperties props) {
                if (props.isSetSpcBef()) {
                    CTTextSpacing spc = props.getSpcBef();
                    if (!spc.isSetSpcPct()) {
                        if (spc.isSetSpcPts()) {
                            setValue(Double.valueOf((-spc.getSpcPts().getVal()) * 0.01d));
                            return true;
                        }
                        return true;
                    }
                    setValue(Double.valueOf(spc.getSpcPct().getVal() * 0.001d));
                    return true;
                }
                return false;
            }
        };
        fetchParagraphProperty(fetcher);
        return fetcher.getValue();
    }

    @Override // org.apache.poi.sl.usermodel.TextParagraph
    public void setSpaceAfter(Double spaceAfter) {
        if (spaceAfter == null && !this._p.isSetPPr()) {
            return;
        }
        if (spaceAfter == null) {
            if (this._p.getPPr().isSetSpcAft()) {
                this._p.getPPr().unsetSpcAft();
            }
        } else {
            CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
            CTTextSpacing spc = CTTextSpacing.Factory.newInstance();
            if (spaceAfter.doubleValue() >= 0.0d) {
                spc.addNewSpcPct().setVal((int) (spaceAfter.doubleValue() * 1000.0d));
            } else {
                spc.addNewSpcPts().setVal((int) ((-spaceAfter.doubleValue()) * 100.0d));
            }
            pr.setSpcAft(spc);
        }
    }

    @Override // org.apache.poi.sl.usermodel.TextParagraph
    public Double getSpaceAfter() {
        ParagraphPropertyFetcher<Double> fetcher = new ParagraphPropertyFetcher<Double>(getIndentLevel()) { // from class: org.apache.poi.xslf.usermodel.XSLFTextParagraph.16
            @Override // org.apache.poi.xslf.model.ParagraphPropertyFetcher
            public boolean fetch(CTTextParagraphProperties props) {
                if (props.isSetSpcAft()) {
                    CTTextSpacing spc = props.getSpcAft();
                    if (!spc.isSetSpcPct()) {
                        if (spc.isSetSpcPts()) {
                            setValue(Double.valueOf((-spc.getSpcPts().getVal()) * 0.01d));
                            return true;
                        }
                        return true;
                    }
                    setValue(Double.valueOf(spc.getSpcPct().getVal() * 0.001d));
                    return true;
                }
                return false;
            }
        };
        fetchParagraphProperty(fetcher);
        return fetcher.getValue();
    }

    @Override // org.apache.poi.sl.usermodel.TextParagraph
    public void setIndentLevel(int level) {
        CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
        pr.setLvl(level);
    }

    @Override // org.apache.poi.sl.usermodel.TextParagraph
    public int getIndentLevel() {
        CTTextParagraphProperties pr = this._p.getPPr();
        if (pr == null || !pr.isSetLvl()) {
            return 0;
        }
        return pr.getLvl();
    }

    public boolean isBullet() {
        ParagraphPropertyFetcher<Boolean> fetcher = new ParagraphPropertyFetcher<Boolean>(getIndentLevel()) { // from class: org.apache.poi.xslf.usermodel.XSLFTextParagraph.17
            @Override // org.apache.poi.xslf.model.ParagraphPropertyFetcher
            public boolean fetch(CTTextParagraphProperties props) {
                if (props.isSetBuNone()) {
                    setValue(false);
                    return true;
                }
                if (props.isSetBuFont() || props.isSetBuChar()) {
                    setValue(true);
                    return true;
                }
                return false;
            }
        };
        fetchParagraphProperty(fetcher);
        if (fetcher.getValue() == null) {
            return false;
        }
        return fetcher.getValue().booleanValue();
    }

    public void setBullet(boolean flag) {
        if (isBullet() == flag) {
            return;
        }
        CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
        if (flag) {
            pr.addNewBuFont().setTypeface(HSSFFont.FONT_ARIAL);
            pr.addNewBuChar().setChar("•");
            return;
        }
        if (pr.isSetBuFont()) {
            pr.unsetBuFont();
        }
        if (pr.isSetBuChar()) {
            pr.unsetBuChar();
        }
        if (pr.isSetBuAutoNum()) {
            pr.unsetBuAutoNum();
        }
        if (pr.isSetBuBlip()) {
            pr.unsetBuBlip();
        }
        if (pr.isSetBuClr()) {
            pr.unsetBuClr();
        }
        if (pr.isSetBuClrTx()) {
            pr.unsetBuClrTx();
        }
        if (pr.isSetBuFont()) {
            pr.unsetBuFont();
        }
        if (pr.isSetBuFontTx()) {
            pr.unsetBuFontTx();
        }
        if (pr.isSetBuSzPct()) {
            pr.unsetBuSzPct();
        }
        if (pr.isSetBuSzPts()) {
            pr.unsetBuSzPts();
        }
        if (pr.isSetBuSzTx()) {
            pr.unsetBuSzTx();
        }
        pr.addNewBuNone();
    }

    public void setBulletAutoNumber(AutoNumberingScheme scheme, int startAt) {
        if (startAt < 1) {
            throw new IllegalArgumentException("Start Number must be greater or equal that 1");
        }
        CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
        CTTextAutonumberBullet lst = pr.isSetBuAutoNum() ? pr.getBuAutoNum() : pr.addNewBuAutoNum();
        lst.setType(STTextAutonumberScheme.Enum.forInt(scheme.ooxmlId));
        lst.setStartAt(startAt);
    }

    public String toString() {
        return PropertyAccessor.PROPERTY_KEY_PREFIX + getClass() + "]" + getText();
    }

    CTTextParagraphProperties getDefaultMasterStyle() {
        String defaultStyleSelector;
        CTPlaceholder ph = this._shape.getCTPlaceholder();
        switch (ph == null ? -1 : ph.getType().intValue()) {
            case -1:
            case 5:
            case 6:
            case 7:
                defaultStyleSelector = "otherStyle";
                break;
            case 0:
            case 2:
            case 4:
            default:
                defaultStyleSelector = "bodyStyle";
                break;
            case 1:
            case 3:
                defaultStyleSelector = "titleStyle";
                break;
        }
        int level = getIndentLevel();
        XSLFSheet masterSheet = this._shape.getSheet();
        XSLFSheet xSLFSheet = masterSheet;
        while (true) {
            XSLFSheet m = xSLFSheet;
            if (m != null) {
                XmlObject xo = m.getXmlObject();
                XmlCursor cur = xo.newCursor();
                try {
                    cur.push();
                    if ((cur.toChild("http://schemas.openxmlformats.org/presentationml/2006/main", "txStyles") && cur.toChild("http://schemas.openxmlformats.org/presentationml/2006/main", defaultStyleSelector)) || (cur.pop() && cur.toChild("http://schemas.openxmlformats.org/presentationml/2006/main", "notesStyle"))) {
                        while (level >= 0) {
                            cur.push();
                            if (cur.toChild(XSSFRelation.NS_DRAWINGML, "lvl" + (level + 1) + "pPr")) {
                                CTTextParagraphProperties cTTextParagraphProperties = (CTTextParagraphProperties) cur.getObject();
                                cur.dispose();
                                return cTTextParagraphProperties;
                            }
                            cur.pop();
                            level--;
                        }
                    }
                    xSLFSheet = (XSLFSheet) m.getMasterSheet();
                } finally {
                    cur.dispose();
                }
            } else {
                return null;
            }
        }
    }

    private <T> boolean fetchParagraphProperty(ParagraphPropertyFetcher<T> visitor) {
        boolean ok = false;
        XSLFTextShape shape = getParentShape();
        XSLFSheet sheet = shape.getSheet();
        if (this._p.isSetPPr()) {
            ok = visitor.fetch(this._p.getPPr());
        }
        if (ok) {
            return true;
        }
        boolean ok2 = shape.fetchShapeProperty(visitor);
        if (ok2) {
            return true;
        }
        CTPlaceholder ph = shape.getCTPlaceholder();
        if (ph == null) {
            XMLSlideShow ppt = sheet.getSlideShow();
            CTTextParagraphProperties themeProps = ppt.getDefaultParagraphStyle(getIndentLevel());
            if (themeProps != null) {
                ok2 = visitor.fetch(themeProps);
            }
        }
        if (ok2) {
            return true;
        }
        CTTextParagraphProperties defaultProps = getDefaultMasterStyle();
        if (defaultProps != null) {
            ok2 = visitor.fetch(defaultProps);
        }
        return ok2;
    }

    void copy(XSLFTextParagraph other) {
        if (other == this) {
            return;
        }
        CTTextParagraph thisP = getXmlObject();
        CTTextParagraph otherP = other.getXmlObject();
        if (thisP.isSetPPr()) {
            thisP.unsetPPr();
        }
        if (thisP.isSetEndParaRPr()) {
            thisP.unsetEndParaRPr();
        }
        this._runs.clear();
        for (int i = thisP.sizeOfBrArray(); i > 0; i--) {
            thisP.removeBr(i - 1);
        }
        for (int i2 = thisP.sizeOfRArray(); i2 > 0; i2--) {
            thisP.removeR(i2 - 1);
        }
        for (int i3 = thisP.sizeOfFldArray(); i3 > 0; i3--) {
            thisP.removeFld(i3 - 1);
        }
        XmlCursor thisC = thisP.newCursor();
        thisC.toEndToken();
        XmlCursor otherC = otherP.newCursor();
        otherC.copyXmlContents(thisC);
        otherC.dispose();
        thisC.dispose();
        List<XSLFTextRun> otherRs = other.getTextRuns();
        int i4 = 0;
        CTRegularTextRun[] arr$ = thisP.getRArray();
        for (CTRegularTextRun rtr : arr$) {
            XSLFTextRun run = newTextRun(rtr);
            int i5 = i4;
            i4++;
            run.copy(otherRs.get(i5));
            this._runs.add(run);
        }
        TextParagraph.TextAlign srcAlign = other.getTextAlign();
        if (srcAlign != getTextAlign()) {
            setTextAlign(srcAlign);
        }
        boolean isBullet = other.isBullet();
        if (isBullet != isBullet()) {
            setBullet(isBullet);
            if (isBullet) {
                String buFont = other.getBulletFont();
                if (buFont != null && !buFont.equals(getBulletFont())) {
                    setBulletFont(buFont);
                }
                String buChar = other.getBulletCharacter();
                if (buChar != null && !buChar.equals(getBulletCharacter())) {
                    setBulletCharacter(buChar);
                }
                PaintStyle buColor = other.getBulletFontColor();
                if (buColor != null && !buColor.equals(getBulletFontColor())) {
                    setBulletFontColor(buColor);
                }
                Double buSize = other.getBulletFontSize();
                if (!doubleEquals(buSize, getBulletFontSize())) {
                    setBulletFontSize(buSize.doubleValue());
                }
            }
        }
        Double leftMargin = other.getLeftMargin();
        if (!doubleEquals(leftMargin, getLeftMargin())) {
            setLeftMargin(leftMargin);
        }
        Double indent = other.getIndent();
        if (!doubleEquals(indent, getIndent())) {
            setIndent(indent);
        }
        Double spaceAfter = other.getSpaceAfter();
        if (!doubleEquals(spaceAfter, getSpaceAfter())) {
            setSpaceAfter(spaceAfter);
        }
        Double spaceBefore = other.getSpaceBefore();
        if (!doubleEquals(spaceBefore, getSpaceBefore())) {
            setSpaceBefore(spaceBefore);
        }
        Double lineSpacing = other.getLineSpacing();
        if (!doubleEquals(lineSpacing, getLineSpacing())) {
            setLineSpacing(lineSpacing);
        }
    }

    private static boolean doubleEquals(Double d1, Double d2) {
        return d1 == d2 || (d1 != null && d1.equals(d2));
    }

    @Override // org.apache.poi.sl.usermodel.TextParagraph
    public Double getDefaultFontSize() {
        CTTextParagraphProperties masterStyle;
        CTTextCharacterProperties endPr = this._p.getEndParaRPr();
        if ((endPr == null || !endPr.isSetSz()) && (masterStyle = getDefaultMasterStyle()) != null) {
            endPr = masterStyle.getDefRPr();
        }
        return Double.valueOf((endPr == null || !endPr.isSetSz()) ? 12.0d : endPr.getSz() / 100.0d);
    }

    @Override // org.apache.poi.sl.usermodel.TextParagraph
    public String getDefaultFontFamily() {
        return this._runs.isEmpty() ? HSSFFont.FONT_ARIAL : this._runs.get(0).getFontFamily();
    }

    @Override // org.apache.poi.sl.usermodel.TextParagraph
    public TextParagraph.BulletStyle getBulletStyle() {
        if (isBullet()) {
            return new TextParagraph.BulletStyle() { // from class: org.apache.poi.xslf.usermodel.XSLFTextParagraph.18
                @Override // org.apache.poi.sl.usermodel.TextParagraph.BulletStyle
                public String getBulletCharacter() {
                    return XSLFTextParagraph.this.getBulletCharacter();
                }

                @Override // org.apache.poi.sl.usermodel.TextParagraph.BulletStyle
                public String getBulletFont() {
                    return XSLFTextParagraph.this.getBulletFont();
                }

                @Override // org.apache.poi.sl.usermodel.TextParagraph.BulletStyle
                public Double getBulletFontSize() {
                    return XSLFTextParagraph.this.getBulletFontSize();
                }

                @Override // org.apache.poi.sl.usermodel.TextParagraph.BulletStyle
                public PaintStyle getBulletFontColor() {
                    return XSLFTextParagraph.this.getBulletFontColor();
                }

                @Override // org.apache.poi.sl.usermodel.TextParagraph.BulletStyle
                public void setBulletFontColor(Color color) {
                    setBulletFontColor(DrawPaint.createSolidPaint(color));
                }

                @Override // org.apache.poi.sl.usermodel.TextParagraph.BulletStyle
                public void setBulletFontColor(PaintStyle color) {
                    XSLFTextParagraph.this.setBulletFontColor(color);
                }

                @Override // org.apache.poi.sl.usermodel.TextParagraph.BulletStyle
                public AutoNumberingScheme getAutoNumberingScheme() {
                    return XSLFTextParagraph.this.getAutoNumberingScheme();
                }

                @Override // org.apache.poi.sl.usermodel.TextParagraph.BulletStyle
                public Integer getAutoNumberingStartAt() {
                    return XSLFTextParagraph.this.getAutoNumberingStartAt();
                }
            };
        }
        return null;
    }

    @Override // org.apache.poi.sl.usermodel.TextParagraph
    public void setBulletStyle(Object... styles) {
        if (styles.length == 0) {
            setBullet(false);
            return;
        }
        setBullet(true);
        for (Object ostyle : styles) {
            if (ostyle instanceof Number) {
                setBulletFontSize(((Number) ostyle).doubleValue());
            } else if (ostyle instanceof Color) {
                setBulletFontColor((Color) ostyle);
            } else if (ostyle instanceof Character) {
                setBulletCharacter(ostyle.toString());
            } else if (ostyle instanceof String) {
                setBulletFont((String) ostyle);
            } else if (ostyle instanceof AutoNumberingScheme) {
                setBulletAutoNumber((AutoNumberingScheme) ostyle, 0);
            }
        }
    }

    void clearButKeepProperties() {
        CTTextParagraph thisP = getXmlObject();
        for (int i = thisP.sizeOfBrArray(); i > 0; i--) {
            thisP.removeBr(i - 1);
        }
        for (int i2 = thisP.sizeOfFldArray(); i2 > 0; i2--) {
            thisP.removeFld(i2 - 1);
        }
        if (!this._runs.isEmpty()) {
            int size = this._runs.size();
            XSLFTextRun lastRun = this._runs.get(size - 1);
            CTTextCharacterProperties cpOther = lastRun.getRPr(false);
            if (cpOther != null) {
                if (thisP.isSetEndParaRPr()) {
                    thisP.unsetEndParaRPr();
                }
                CTTextCharacterProperties cp = thisP.addNewEndParaRPr();
                cp.set(cpOther);
            }
            for (int i3 = size; i3 > 0; i3--) {
                thisP.removeR(i3 - 1);
            }
            this._runs.clear();
        }
    }

    @Override // org.apache.poi.sl.usermodel.TextParagraph
    public boolean isHeaderOrFooter() {
        CTPlaceholder ph = this._shape.getCTPlaceholder();
        int phId = ph == null ? -1 : ph.getType().intValue();
        switch (phId) {
            case 5:
            case 6:
            case 7:
            case 8:
                return true;
            default:
                return false;
        }
    }

    protected XSLFTextRun newTextRun(CTRegularTextRun r) {
        return new XSLFTextRun(r, this);
    }
}
