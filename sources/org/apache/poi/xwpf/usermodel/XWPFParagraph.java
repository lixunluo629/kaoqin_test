package org.apache.poi.xwpf.usermodel;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.util.Internal;
import org.apache.poi.wp.usermodel.Paragraph;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTInd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumLvl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPBdr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtBlock;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextAlignment;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STLineSpacingRule;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTextAlignment;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xwpf/usermodel/XWPFParagraph.class */
public class XWPFParagraph implements IBodyElement, IRunBody, ISDTContents, Paragraph {
    private final CTP paragraph;
    protected IBody part;
    protected XWPFDocument document;
    protected List<XWPFRun> runs;
    protected List<IRunElement> iruns;
    private StringBuffer footnoteText = new StringBuffer();

    public XWPFParagraph(CTP prgrph, IBody part) {
        this.paragraph = prgrph;
        this.part = part;
        this.document = part.getXWPFDocument();
        if (this.document == null) {
            throw new NullPointerException();
        }
        this.runs = new ArrayList();
        this.iruns = new ArrayList();
        buildRunsInOrderFromXml(this.paragraph);
        for (XWPFRun run : this.runs) {
            CTR r = run.getCTR();
            XmlCursor c = r.newCursor();
            c.selectPath("child::*");
            while (c.toNextSelection()) {
                XmlObject o = c.getObject();
                if (o instanceof CTFtnEdnRef) {
                    CTFtnEdnRef ftn = (CTFtnEdnRef) o;
                    this.footnoteText.append(" [").append(ftn.getId()).append(": ");
                    XWPFFootnote footnote = ftn.getDomNode().getLocalName().equals("footnoteReference") ? this.document.getFootnoteByID(ftn.getId().intValue()) : this.document.getEndnoteByID(ftn.getId().intValue());
                    boolean first = true;
                    for (XWPFParagraph p : footnote.getParagraphs()) {
                        if (!first) {
                            this.footnoteText.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
                        }
                        first = false;
                        this.footnoteText.append(p.getText());
                    }
                    this.footnoteText.append("] ");
                }
            }
            c.dispose();
        }
    }

    private void buildRunsInOrderFromXml(XmlObject object) {
        XmlCursor c = object.newCursor();
        c.selectPath("child::*");
        while (c.toNextSelection()) {
            XmlObject o = c.getObject();
            if (o instanceof CTR) {
                XWPFRun r = new XWPFRun((CTR) o, this);
                this.runs.add(r);
                this.iruns.add(r);
            }
            if (o instanceof CTHyperlink) {
                CTHyperlink link = (CTHyperlink) o;
                CTR[] arr$ = link.getRArray();
                for (CTR r2 : arr$) {
                    XWPFHyperlinkRun hr = new XWPFHyperlinkRun(link, r2, this);
                    this.runs.add(hr);
                    this.iruns.add(hr);
                }
            }
            if (o instanceof CTSimpleField) {
                CTSimpleField field = (CTSimpleField) o;
                CTR[] arr$2 = field.getRArray();
                for (CTR r3 : arr$2) {
                    XWPFFieldRun fr = new XWPFFieldRun(field, r3, this);
                    this.runs.add(fr);
                    this.iruns.add(fr);
                }
            }
            if (o instanceof CTSdtBlock) {
                XWPFSDT cc = new XWPFSDT((CTSdtBlock) o, this.part);
                this.iruns.add(cc);
            }
            if (o instanceof CTSdtRun) {
                XWPFSDT cc2 = new XWPFSDT((CTSdtRun) o, this.part);
                this.iruns.add(cc2);
            }
            if (o instanceof CTRunTrackChange) {
                CTR[] arr$3 = ((CTRunTrackChange) o).getRArray();
                for (CTR r4 : arr$3) {
                    XWPFRun cr = new XWPFRun(r4, this);
                    this.runs.add(cr);
                    this.iruns.add(cr);
                }
            }
            if (o instanceof CTSmartTagRun) {
                buildRunsInOrderFromXml(o);
            }
        }
        c.dispose();
    }

    @Internal
    public CTP getCTP() {
        return this.paragraph;
    }

    public List<XWPFRun> getRuns() {
        return Collections.unmodifiableList(this.runs);
    }

    public List<IRunElement> getIRuns() {
        return Collections.unmodifiableList(this.iruns);
    }

    public boolean isEmpty() {
        return !this.paragraph.getDomNode().hasChildNodes();
    }

    @Override // org.apache.poi.xwpf.usermodel.IRunBody
    public XWPFDocument getDocument() {
        return this.document;
    }

    public String getText() {
        StringBuffer out = new StringBuffer();
        for (IRunElement run : this.iruns) {
            if (run instanceof XWPFRun) {
                XWPFRun xRun = (XWPFRun) run;
                if (!xRun.getCTR().isSetRsidDel()) {
                    out.append(xRun);
                }
            } else if (run instanceof XWPFSDT) {
                out.append(((XWPFSDT) run).getContent().getText());
            } else {
                out.append(run);
            }
        }
        out.append(this.footnoteText);
        return out.toString();
    }

    public String getStyleID() {
        if (this.paragraph.getPPr() != null && this.paragraph.getPPr().getPStyle() != null && this.paragraph.getPPr().getPStyle().getVal() != null) {
            return this.paragraph.getPPr().getPStyle().getVal();
        }
        return null;
    }

    public BigInteger getNumID() {
        if (this.paragraph.getPPr() != null && this.paragraph.getPPr().getNumPr() != null && this.paragraph.getPPr().getNumPr().getNumId() != null) {
            return this.paragraph.getPPr().getNumPr().getNumId().getVal();
        }
        return null;
    }

    public void setNumID(BigInteger numPos) {
        if (this.paragraph.getPPr() == null) {
            this.paragraph.addNewPPr();
        }
        if (this.paragraph.getPPr().getNumPr() == null) {
            this.paragraph.getPPr().addNewNumPr();
        }
        if (this.paragraph.getPPr().getNumPr().getNumId() == null) {
            this.paragraph.getPPr().getNumPr().addNewNumId();
        }
        this.paragraph.getPPr().getNumPr().getNumId().setVal(numPos);
    }

    public BigInteger getNumIlvl() {
        if (this.paragraph.getPPr() != null && this.paragraph.getPPr().getNumPr() != null && this.paragraph.getPPr().getNumPr().getIlvl() != null) {
            return this.paragraph.getPPr().getNumPr().getIlvl().getVal();
        }
        return null;
    }

    public String getNumFmt() {
        XWPFNum num;
        BigInteger numID = getNumID();
        XWPFNumbering numbering = this.document.getNumbering();
        if (numID != null && numbering != null && (num = numbering.getNum(numID)) != null) {
            BigInteger ilvl = getNumIlvl();
            BigInteger abstractNumId = num.getCTNum().getAbstractNumId().getVal();
            CTAbstractNum anum = numbering.getAbstractNum(abstractNumId).getAbstractNum();
            CTLvl level = null;
            int i = 0;
            while (true) {
                if (i >= anum.sizeOfLvlArray()) {
                    break;
                }
                CTLvl lvl = anum.getLvlArray(i);
                if (!lvl.getIlvl().equals(ilvl)) {
                    i++;
                } else {
                    level = lvl;
                    break;
                }
            }
            if (level != null && level.getNumFmt() != null && level.getNumFmt().getVal() != null) {
                return level.getNumFmt().getVal().toString();
            }
            return null;
        }
        return null;
    }

    public String getNumLevelText() {
        XWPFNum num;
        CTDecimalNumber ctDecimalNumber;
        BigInteger abstractNumId;
        XWPFAbstractNum xwpfAbstractNum;
        CTAbstractNum anum;
        BigInteger numID = getNumID();
        XWPFNumbering numbering = this.document.getNumbering();
        if (numID != null && numbering != null && (num = numbering.getNum(numID)) != null) {
            BigInteger ilvl = getNumIlvl();
            CTNum ctNum = num.getCTNum();
            if (ctNum == null || (ctDecimalNumber = ctNum.getAbstractNumId()) == null || (abstractNumId = ctDecimalNumber.getVal()) == null || (xwpfAbstractNum = numbering.getAbstractNum(abstractNumId)) == null || (anum = xwpfAbstractNum.getCTAbstractNum()) == null) {
                return null;
            }
            CTLvl level = null;
            int i = 0;
            while (true) {
                if (i < anum.sizeOfLvlArray()) {
                    CTLvl lvl = anum.getLvlArray(i);
                    if (lvl == null || lvl.getIlvl() == null || !lvl.getIlvl().equals(ilvl)) {
                        i++;
                    } else {
                        level = lvl;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (level != null && level.getLvlText() != null && level.getLvlText().getVal() != null) {
                return level.getLvlText().getVal().toString();
            }
            return null;
        }
        return null;
    }

    public BigInteger getNumStartOverride() {
        XWPFNum num;
        CTNum ctNum;
        BigInteger numID = getNumID();
        XWPFNumbering numbering = this.document.getNumbering();
        if (numID == null || numbering == null || (num = numbering.getNum(numID)) == null || (ctNum = num.getCTNum()) == null) {
            return null;
        }
        BigInteger ilvl = getNumIlvl();
        CTNumLvl level = null;
        int i = 0;
        while (true) {
            if (i < ctNum.sizeOfLvlOverrideArray()) {
                CTNumLvl ctNumLvl = ctNum.getLvlOverrideArray(i);
                if (ctNumLvl == null || ctNumLvl.getIlvl() == null || !ctNumLvl.getIlvl().equals(ilvl)) {
                    i++;
                } else {
                    level = ctNumLvl;
                    break;
                }
            } else {
                break;
            }
        }
        if (level != null && level.getStartOverride() != null) {
            return level.getStartOverride().getVal();
        }
        return null;
    }

    public String getParagraphText() {
        StringBuffer out = new StringBuffer();
        for (XWPFRun run : this.runs) {
            out.append(run);
        }
        return out.toString();
    }

    public String getPictureText() {
        StringBuffer out = new StringBuffer();
        for (XWPFRun run : this.runs) {
            out.append(run.getPictureText());
        }
        return out.toString();
    }

    public String getFootnoteText() {
        return this.footnoteText.toString();
    }

    public ParagraphAlignment getAlignment() {
        CTPPr pr = getCTPPr();
        return (pr == null || !pr.isSetJc()) ? ParagraphAlignment.LEFT : ParagraphAlignment.valueOf(pr.getJc().getVal().intValue());
    }

    public void setAlignment(ParagraphAlignment align) {
        CTPPr pr = getCTPPr();
        CTJc jc = pr.isSetJc() ? pr.getJc() : pr.addNewJc();
        STJc.Enum en = STJc.Enum.forInt(align.getValue());
        jc.setVal(en);
    }

    @Override // org.apache.poi.wp.usermodel.Paragraph
    public int getFontAlignment() {
        return getAlignment().getValue();
    }

    @Override // org.apache.poi.wp.usermodel.Paragraph
    public void setFontAlignment(int align) {
        ParagraphAlignment pAlign = ParagraphAlignment.valueOf(align);
        setAlignment(pAlign);
    }

    public TextAlignment getVerticalAlignment() {
        CTPPr pr = getCTPPr();
        return (pr == null || !pr.isSetTextAlignment()) ? TextAlignment.AUTO : TextAlignment.valueOf(pr.getTextAlignment().getVal().intValue());
    }

    public void setVerticalAlignment(TextAlignment valign) {
        CTPPr pr = getCTPPr();
        CTTextAlignment textAlignment = pr.isSetTextAlignment() ? pr.getTextAlignment() : pr.addNewTextAlignment();
        STTextAlignment.Enum en = STTextAlignment.Enum.forInt(valign.getValue());
        textAlignment.setVal(en);
    }

    public Borders getBorderTop() {
        CTPBdr border = getCTPBrd(false);
        CTBorder ct = null;
        if (border != null) {
            ct = border.getTop();
        }
        STBorder.Enum ptrn = ct != null ? ct.getVal() : STBorder.NONE;
        return Borders.valueOf(ptrn.intValue());
    }

    public void setBorderTop(Borders border) {
        CTPBdr ct = getCTPBrd(true);
        if (ct == null) {
            throw new RuntimeException("invalid paragraph state");
        }
        CTBorder pr = ct.isSetTop() ? ct.getTop() : ct.addNewTop();
        if (border.getValue() == Borders.NONE.getValue()) {
            ct.unsetTop();
        } else {
            pr.setVal(STBorder.Enum.forInt(border.getValue()));
        }
    }

    public Borders getBorderBottom() {
        CTPBdr border = getCTPBrd(false);
        CTBorder ct = null;
        if (border != null) {
            ct = border.getBottom();
        }
        STBorder.Enum ptrn = ct != null ? ct.getVal() : STBorder.NONE;
        return Borders.valueOf(ptrn.intValue());
    }

    public void setBorderBottom(Borders border) {
        CTPBdr ct = getCTPBrd(true);
        CTBorder pr = ct.isSetBottom() ? ct.getBottom() : ct.addNewBottom();
        if (border.getValue() == Borders.NONE.getValue()) {
            ct.unsetBottom();
        } else {
            pr.setVal(STBorder.Enum.forInt(border.getValue()));
        }
    }

    public Borders getBorderLeft() {
        CTPBdr border = getCTPBrd(false);
        CTBorder ct = null;
        if (border != null) {
            ct = border.getLeft();
        }
        STBorder.Enum ptrn = ct != null ? ct.getVal() : STBorder.NONE;
        return Borders.valueOf(ptrn.intValue());
    }

    public void setBorderLeft(Borders border) {
        CTPBdr ct = getCTPBrd(true);
        CTBorder pr = ct.isSetLeft() ? ct.getLeft() : ct.addNewLeft();
        if (border.getValue() == Borders.NONE.getValue()) {
            ct.unsetLeft();
        } else {
            pr.setVal(STBorder.Enum.forInt(border.getValue()));
        }
    }

    public Borders getBorderRight() {
        CTPBdr border = getCTPBrd(false);
        CTBorder ct = null;
        if (border != null) {
            ct = border.getRight();
        }
        STBorder.Enum ptrn = ct != null ? ct.getVal() : STBorder.NONE;
        return Borders.valueOf(ptrn.intValue());
    }

    public void setBorderRight(Borders border) {
        CTPBdr ct = getCTPBrd(true);
        CTBorder pr = ct.isSetRight() ? ct.getRight() : ct.addNewRight();
        if (border.getValue() == Borders.NONE.getValue()) {
            ct.unsetRight();
        } else {
            pr.setVal(STBorder.Enum.forInt(border.getValue()));
        }
    }

    public Borders getBorderBetween() {
        CTPBdr border = getCTPBrd(false);
        CTBorder ct = null;
        if (border != null) {
            ct = border.getBetween();
        }
        STBorder.Enum ptrn = ct != null ? ct.getVal() : STBorder.NONE;
        return Borders.valueOf(ptrn.intValue());
    }

    public void setBorderBetween(Borders border) {
        CTPBdr ct = getCTPBrd(true);
        CTBorder pr = ct.isSetBetween() ? ct.getBetween() : ct.addNewBetween();
        if (border.getValue() == Borders.NONE.getValue()) {
            ct.unsetBetween();
        } else {
            pr.setVal(STBorder.Enum.forInt(border.getValue()));
        }
    }

    public boolean isPageBreak() {
        CTPPr ppr = getCTPPr();
        CTOnOff ctPageBreak = ppr.isSetPageBreakBefore() ? ppr.getPageBreakBefore() : null;
        if (ctPageBreak == null) {
            return false;
        }
        return isTruelike(ctPageBreak.getVal(), false);
    }

    private static boolean isTruelike(STOnOff.Enum value, boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        switch (value.intValue()) {
        }
        return defaultValue;
    }

    public void setPageBreak(boolean pageBreak) {
        CTPPr ppr = getCTPPr();
        CTOnOff ctPageBreak = ppr.isSetPageBreakBefore() ? ppr.getPageBreakBefore() : ppr.addNewPageBreakBefore();
        if (pageBreak) {
            ctPageBreak.setVal(STOnOff.TRUE);
        } else {
            ctPageBreak.setVal(STOnOff.FALSE);
        }
    }

    public int getSpacingAfter() {
        CTSpacing spacing = getCTSpacing(false);
        if (spacing == null || !spacing.isSetAfter()) {
            return -1;
        }
        return spacing.getAfter().intValue();
    }

    public void setSpacingAfter(int spaces) {
        CTSpacing spacing = getCTSpacing(true);
        if (spacing != null) {
            BigInteger bi = new BigInteger("" + spaces);
            spacing.setAfter(bi);
        }
    }

    public int getSpacingAfterLines() {
        CTSpacing spacing = getCTSpacing(false);
        if (spacing == null || !spacing.isSetAfterLines()) {
            return -1;
        }
        return spacing.getAfterLines().intValue();
    }

    public void setSpacingAfterLines(int spaces) {
        CTSpacing spacing = getCTSpacing(true);
        BigInteger bi = new BigInteger("" + spaces);
        spacing.setAfterLines(bi);
    }

    public int getSpacingBefore() {
        CTSpacing spacing = getCTSpacing(false);
        if (spacing == null || !spacing.isSetBefore()) {
            return -1;
        }
        return spacing.getBefore().intValue();
    }

    public void setSpacingBefore(int spaces) {
        CTSpacing spacing = getCTSpacing(true);
        BigInteger bi = new BigInteger("" + spaces);
        spacing.setBefore(bi);
    }

    public int getSpacingBeforeLines() {
        CTSpacing spacing = getCTSpacing(false);
        if (spacing == null || !spacing.isSetBeforeLines()) {
            return -1;
        }
        return spacing.getBeforeLines().intValue();
    }

    public void setSpacingBeforeLines(int spaces) {
        CTSpacing spacing = getCTSpacing(true);
        BigInteger bi = new BigInteger("" + spaces);
        spacing.setBeforeLines(bi);
    }

    public LineSpacingRule getSpacingLineRule() {
        CTSpacing spacing = getCTSpacing(false);
        return (spacing == null || !spacing.isSetLineRule()) ? LineSpacingRule.AUTO : LineSpacingRule.valueOf(spacing.getLineRule().intValue());
    }

    public void setSpacingLineRule(LineSpacingRule rule) {
        CTSpacing spacing = getCTSpacing(true);
        spacing.setLineRule(STLineSpacingRule.Enum.forInt(rule.getValue()));
    }

    public double getSpacingBetween() {
        CTSpacing spacing = getCTSpacing(false);
        if (spacing == null || !spacing.isSetLine()) {
            return -1.0d;
        }
        if (spacing.getLineRule() == null || spacing.getLineRule() == STLineSpacingRule.AUTO) {
            BigInteger[] val = spacing.getLine().divideAndRemainder(BigInteger.valueOf(240L));
            return val[0].doubleValue() + (val[1].doubleValue() / 240.0d);
        }
        BigInteger[] val2 = spacing.getLine().divideAndRemainder(BigInteger.valueOf(20L));
        return val2[0].doubleValue() + (val2[1].doubleValue() / 20.0d);
    }

    public void setSpacingBetween(double spacing, LineSpacingRule rule) {
        CTSpacing ctSp = getCTSpacing(true);
        switch (rule) {
            case AUTO:
                ctSp.setLine(new BigInteger(String.valueOf(Math.round(spacing * 240.0d))));
                break;
            default:
                ctSp.setLine(new BigInteger(String.valueOf(Math.round(spacing * 20.0d))));
                break;
        }
        ctSp.setLineRule(STLineSpacingRule.Enum.forInt(rule.getValue()));
    }

    public void setSpacingBetween(double spacing) {
        setSpacingBetween(spacing, LineSpacingRule.AUTO);
    }

    public int getIndentationLeft() {
        CTInd indentation = getCTInd(false);
        if (indentation == null || !indentation.isSetLeft()) {
            return -1;
        }
        return indentation.getLeft().intValue();
    }

    public void setIndentationLeft(int indentation) {
        CTInd indent = getCTInd(true);
        BigInteger bi = new BigInteger("" + indentation);
        indent.setLeft(bi);
    }

    public int getIndentationRight() {
        CTInd indentation = getCTInd(false);
        if (indentation == null || !indentation.isSetRight()) {
            return -1;
        }
        return indentation.getRight().intValue();
    }

    public void setIndentationRight(int indentation) {
        CTInd indent = getCTInd(true);
        BigInteger bi = new BigInteger("" + indentation);
        indent.setRight(bi);
    }

    public int getIndentationHanging() {
        CTInd indentation = getCTInd(false);
        if (indentation == null || !indentation.isSetHanging()) {
            return -1;
        }
        return indentation.getHanging().intValue();
    }

    public void setIndentationHanging(int indentation) {
        CTInd indent = getCTInd(true);
        BigInteger bi = new BigInteger("" + indentation);
        indent.setHanging(bi);
    }

    public int getIndentationFirstLine() {
        CTInd indentation = getCTInd(false);
        if (indentation == null || !indentation.isSetFirstLine()) {
            return -1;
        }
        return indentation.getFirstLine().intValue();
    }

    public void setIndentationFirstLine(int indentation) {
        CTInd indent = getCTInd(true);
        BigInteger bi = new BigInteger("" + indentation);
        indent.setFirstLine(bi);
    }

    @Override // org.apache.poi.wp.usermodel.Paragraph
    public int getIndentFromLeft() {
        return getIndentationLeft();
    }

    @Override // org.apache.poi.wp.usermodel.Paragraph
    public void setIndentFromLeft(int dxaLeft) {
        setIndentationLeft(dxaLeft);
    }

    @Override // org.apache.poi.wp.usermodel.Paragraph
    public int getIndentFromRight() {
        return getIndentationRight();
    }

    @Override // org.apache.poi.wp.usermodel.Paragraph
    public void setIndentFromRight(int dxaRight) {
        setIndentationRight(dxaRight);
    }

    @Override // org.apache.poi.wp.usermodel.Paragraph
    public int getFirstLineIndent() {
        return getIndentationFirstLine();
    }

    @Override // org.apache.poi.wp.usermodel.Paragraph
    public void setFirstLineIndent(int first) {
        setIndentationFirstLine(first);
    }

    @Override // org.apache.poi.wp.usermodel.Paragraph
    public boolean isWordWrapped() {
        CTOnOff wordWrap = getCTPPr().isSetWordWrap() ? getCTPPr().getWordWrap() : null;
        if (wordWrap != null) {
            return wordWrap.getVal() == STOnOff.ON || wordWrap.getVal() == STOnOff.TRUE || wordWrap.getVal() == STOnOff.X_1;
        }
        return false;
    }

    @Override // org.apache.poi.wp.usermodel.Paragraph
    public void setWordWrapped(boolean wrap) {
        CTOnOff wordWrap = getCTPPr().isSetWordWrap() ? getCTPPr().getWordWrap() : getCTPPr().addNewWordWrap();
        if (wrap) {
            wordWrap.setVal(STOnOff.TRUE);
        } else {
            wordWrap.unsetVal();
        }
    }

    public boolean isWordWrap() {
        return isWordWrapped();
    }

    @Deprecated
    public void setWordWrap(boolean wrap) {
        setWordWrapped(wrap);
    }

    public String getStyle() {
        CTPPr pr = getCTPPr();
        CTString style = pr.isSetPStyle() ? pr.getPStyle() : null;
        if (style != null) {
            return style.getVal();
        }
        return null;
    }

    public void setStyle(String newStyle) {
        CTPPr pr = getCTPPr();
        CTString style = pr.getPStyle() != null ? pr.getPStyle() : pr.addNewPStyle();
        style.setVal(newStyle);
    }

    private CTPBdr getCTPBrd(boolean create) {
        CTPPr pr = getCTPPr();
        CTPBdr ct = pr.isSetPBdr() ? pr.getPBdr() : null;
        if (create && ct == null) {
            ct = pr.addNewPBdr();
        }
        return ct;
    }

    private CTSpacing getCTSpacing(boolean create) {
        CTPPr pr = getCTPPr();
        CTSpacing ct = pr.getSpacing() == null ? null : pr.getSpacing();
        if (create && ct == null) {
            ct = pr.addNewSpacing();
        }
        return ct;
    }

    private CTInd getCTInd(boolean create) {
        CTPPr pr = getCTPPr();
        CTInd ct = pr.getInd() == null ? null : pr.getInd();
        if (create && ct == null) {
            ct = pr.addNewInd();
        }
        return ct;
    }

    private CTPPr getCTPPr() {
        CTPPr pr = this.paragraph.getPPr() == null ? this.paragraph.addNewPPr() : this.paragraph.getPPr();
        return pr;
    }

    protected void addRun(CTR run) {
        int pos = this.paragraph.sizeOfRArray();
        this.paragraph.addNewR();
        this.paragraph.setRArray(pos, run);
    }

    public XWPFRun createRun() {
        XWPFRun xwpfRun = new XWPFRun(this.paragraph.addNewR(), (IRunBody) this);
        this.runs.add(xwpfRun);
        this.iruns.add(xwpfRun);
        return xwpfRun;
    }

    public XWPFRun insertNewRun(int pos) {
        if (pos >= 0 && pos <= this.runs.size()) {
            int rPos = 0;
            for (int i = 0; i < pos; i++) {
                XWPFRun currRun = this.runs.get(i);
                if (!(currRun instanceof XWPFHyperlinkRun) && !(currRun instanceof XWPFFieldRun)) {
                    rPos++;
                }
            }
            CTR ctRun = this.paragraph.insertNewR(rPos);
            XWPFRun newRun = new XWPFRun(ctRun, (IRunBody) this);
            int iPos = this.iruns.size();
            if (pos < this.runs.size()) {
                XWPFRun oldAtPos = this.runs.get(pos);
                int oldAt = this.iruns.indexOf(oldAtPos);
                if (oldAt != -1) {
                    iPos = oldAt;
                }
            }
            this.iruns.add(iPos, newRun);
            this.runs.add(pos, newRun);
            return newRun;
        }
        return null;
    }

    public TextSegement searchText(String searched, PositionInParagraph startPos) {
        int charPos;
        int startRun = startPos.getRun();
        int startText = startPos.getText();
        int startChar = startPos.getChar();
        int beginRunPos = 0;
        int candCharPos = 0;
        boolean newList = false;
        CTR[] rArray = this.paragraph.getRArray();
        for (int runPos = startRun; runPos < rArray.length; runPos++) {
            int beginTextPos = 0;
            int beginCharPos = 0;
            int textPos = 0;
            CTR ctRun = rArray[runPos];
            XmlCursor c = ctRun.newCursor();
            c.selectPath("./*");
            while (c.toNextSelection()) {
                try {
                    XmlObject o = c.getObject();
                    if (o instanceof CTText) {
                        if (textPos >= startText) {
                            String candidate = ((CTText) o).getStringValue();
                            if (runPos == startRun) {
                                charPos = startChar;
                            } else {
                                charPos = 0;
                            }
                            while (charPos < candidate.length()) {
                                if (candidate.charAt(charPos) == searched.charAt(0) && candCharPos == 0) {
                                    beginTextPos = textPos;
                                    beginCharPos = charPos;
                                    beginRunPos = runPos;
                                    newList = true;
                                }
                                if (candidate.charAt(charPos) == searched.charAt(candCharPos)) {
                                    if (candCharPos + 1 < searched.length()) {
                                        candCharPos++;
                                    } else if (newList) {
                                        TextSegement segement = new TextSegement();
                                        segement.setBeginRun(beginRunPos);
                                        segement.setBeginText(beginTextPos);
                                        segement.setBeginChar(beginCharPos);
                                        segement.setEndRun(runPos);
                                        segement.setEndText(textPos);
                                        segement.setEndChar(charPos);
                                        c.dispose();
                                        return segement;
                                    }
                                } else {
                                    candCharPos = 0;
                                }
                                charPos++;
                            }
                        }
                        textPos++;
                    } else if (o instanceof CTProofErr) {
                        c.removeXml();
                    } else if (!(o instanceof CTRPr)) {
                        candCharPos = 0;
                    }
                } finally {
                    c.dispose();
                }
            }
        }
        return null;
    }

    public String getText(TextSegement segment) {
        int runBegin = segment.getBeginRun();
        int textBegin = segment.getBeginText();
        int charBegin = segment.getBeginChar();
        int runEnd = segment.getEndRun();
        int textEnd = segment.getEndText();
        int charEnd = segment.getEndChar();
        StringBuilder out = new StringBuilder();
        CTR[] rArray = this.paragraph.getRArray();
        for (int i = runBegin; i <= runEnd; i++) {
            CTText[] tArray = rArray[i].getTArray();
            int startText = 0;
            int endText = tArray.length - 1;
            if (i == runBegin) {
                startText = textBegin;
            }
            if (i == runEnd) {
                endText = textEnd;
            }
            for (int j = startText; j <= endText; j++) {
                String tmpText = tArray[j].getStringValue();
                int startChar = 0;
                int endChar = tmpText.length() - 1;
                if (j == textBegin && i == runBegin) {
                    startChar = charBegin;
                }
                if (j == textEnd && i == runEnd) {
                    endChar = charEnd;
                }
                out.append(tmpText.substring(startChar, endChar + 1));
            }
        }
        return out.toString();
    }

    public boolean removeRun(int pos) {
        if (pos >= 0 && pos < this.runs.size()) {
            XWPFRun run = this.runs.get(pos);
            if ((run instanceof XWPFHyperlinkRun) || (run instanceof XWPFFieldRun)) {
                throw new IllegalArgumentException("Removing Field or Hyperlink runs not yet supported");
            }
            this.runs.remove(pos);
            this.iruns.remove(run);
            int rPos = 0;
            for (int i = 0; i < pos; i++) {
                XWPFRun currRun = this.runs.get(i);
                if (!(currRun instanceof XWPFHyperlinkRun) && !(currRun instanceof XWPFFieldRun)) {
                    rPos++;
                }
            }
            getCTP().removeR(rPos);
            return true;
        }
        return false;
    }

    @Override // org.apache.poi.xwpf.usermodel.IBodyElement
    public BodyElementType getElementType() {
        return BodyElementType.PARAGRAPH;
    }

    @Override // org.apache.poi.xwpf.usermodel.IBodyElement
    public IBody getBody() {
        return this.part;
    }

    @Override // org.apache.poi.xwpf.usermodel.IBodyElement, org.apache.poi.xwpf.usermodel.IRunBody
    public POIXMLDocumentPart getPart() {
        if (this.part != null) {
            return this.part.getPart();
        }
        return null;
    }

    @Override // org.apache.poi.xwpf.usermodel.IBodyElement
    public BodyType getPartType() {
        return this.part.getPartType();
    }

    public void addRun(XWPFRun r) {
        if (!this.runs.contains(r)) {
            this.runs.add(r);
        }
    }

    public XWPFRun getRun(CTR r) {
        for (int i = 0; i < getRuns().size(); i++) {
            if (getRuns().get(i).getCTR() == r) {
                return getRuns().get(i);
            }
        }
        return null;
    }
}
