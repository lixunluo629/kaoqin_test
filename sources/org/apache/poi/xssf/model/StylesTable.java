package org.apache.poi.xssf.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.poi.ddf.EscherProperties;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.FontFamily;
import org.apache.poi.ss.usermodel.FontScheme;
import org.apache.poi.ss.usermodel.TableStyle;
import org.apache.poi.util.Internal;
import org.apache.poi.xssf.usermodel.CustomIndexedColorMap;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.IndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFBuiltinTableStyle;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFactory;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.poi.xssf.usermodel.XSSFTableStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellFill;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorders;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellStyleXfs;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellXfs;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxf;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxfs;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFills;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFont;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFonts;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmt;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmts;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTStylesheet;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyles;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXf;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STPatternType;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.StyleSheetDocument;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/model/StylesTable.class */
public class StylesTable extends POIXMLDocumentPart {
    private final SortedMap<Short, String> numberFormats;
    private final List<XSSFFont> fonts;
    private final List<XSSFCellFill> fills;
    private final List<XSSFCellBorder> borders;
    private final List<CTXf> styleXfs;
    private final List<CTXf> xfs;
    private final List<CTDxf> dxfs;
    private final Map<String, TableStyle> tableStyles;
    private IndexedColorMap indexedColors;
    public static final int FIRST_CUSTOM_STYLE_ID = 165;
    private static final int MAXIMUM_STYLE_ID = SpreadsheetVersion.EXCEL2007.getMaxCellStyles();
    private static final short FIRST_USER_DEFINED_NUMBER_FORMAT_ID = 164;
    private int MAXIMUM_NUMBER_OF_DATA_FORMATS;
    private StyleSheetDocument doc;
    private XSSFWorkbook workbook;
    private ThemesTable theme;

    public void setMaxNumberOfDataFormats(int num) {
        if (num < getNumDataFormats()) {
            if (num < 0) {
                throw new IllegalArgumentException("Maximum Number of Data Formats must be greater than or equal to 0");
            }
            throw new IllegalStateException("Cannot set the maximum number of data formats less than the current quantity.Data formats must be explicitly removed (via StylesTable.removeNumberFormat) before the limit can be decreased.");
        }
        this.MAXIMUM_NUMBER_OF_DATA_FORMATS = num;
    }

    public int getMaxNumberOfDataFormats() {
        return this.MAXIMUM_NUMBER_OF_DATA_FORMATS;
    }

    public StylesTable() {
        this.numberFormats = new TreeMap();
        this.fonts = new ArrayList();
        this.fills = new ArrayList();
        this.borders = new ArrayList();
        this.styleXfs = new ArrayList();
        this.xfs = new ArrayList();
        this.dxfs = new ArrayList();
        this.tableStyles = new HashMap();
        this.indexedColors = new DefaultIndexedColorMap();
        this.MAXIMUM_NUMBER_OF_DATA_FORMATS = EscherProperties.GEOTEXT__BOLDFONT;
        this.doc = StyleSheetDocument.Factory.newInstance();
        this.doc.addNewStyleSheet();
        initialize();
    }

    public StylesTable(PackagePart part) throws IOException {
        super(part);
        this.numberFormats = new TreeMap();
        this.fonts = new ArrayList();
        this.fills = new ArrayList();
        this.borders = new ArrayList();
        this.styleXfs = new ArrayList();
        this.xfs = new ArrayList();
        this.dxfs = new ArrayList();
        this.tableStyles = new HashMap();
        this.indexedColors = new DefaultIndexedColorMap();
        this.MAXIMUM_NUMBER_OF_DATA_FORMATS = EscherProperties.GEOTEXT__BOLDFONT;
        readFrom(part.getInputStream());
    }

    public void setWorkbook(XSSFWorkbook wb) {
        this.workbook = wb;
    }

    public ThemesTable getTheme() {
        return this.theme;
    }

    public void setTheme(ThemesTable theme) {
        this.theme = theme;
        if (theme != null) {
            theme.setColorMap(getIndexedColors());
        }
        for (XSSFFont font : this.fonts) {
            font.setThemesTable(theme);
        }
        for (XSSFCellBorder border : this.borders) {
            border.setThemesTable(theme);
        }
    }

    public void ensureThemesTable() {
        if (this.theme != null) {
            return;
        }
        setTheme((ThemesTable) this.workbook.createRelationship(XSSFRelation.THEME, XSSFFactory.getInstance()));
    }

    public void readFrom(InputStream is) throws IOException {
        try {
            this.doc = StyleSheetDocument.Factory.parse(is, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
            CTStylesheet styleSheet = this.doc.getStyleSheet();
            IndexedColorMap customColors = CustomIndexedColorMap.fromColors(styleSheet.getColors());
            if (customColors != null) {
                this.indexedColors = customColors;
            }
            CTNumFmts ctfmts = styleSheet.getNumFmts();
            if (ctfmts != null) {
                CTNumFmt[] arr$ = ctfmts.getNumFmtArray();
                for (CTNumFmt nfmt : arr$) {
                    short formatId = (short) nfmt.getNumFmtId();
                    this.numberFormats.put(Short.valueOf(formatId), nfmt.getFormatCode());
                }
            }
            CTFonts ctfonts = styleSheet.getFonts();
            if (ctfonts != null) {
                int idx = 0;
                CTFont[] arr$2 = ctfonts.getFontArray();
                for (CTFont font : arr$2) {
                    XSSFFont f = new XSSFFont(font, idx, this.indexedColors);
                    this.fonts.add(f);
                    idx++;
                }
            }
            CTFills ctfills = styleSheet.getFills();
            if (ctfills != null) {
                CTFill[] arr$3 = ctfills.getFillArray();
                for (CTFill fill : arr$3) {
                    this.fills.add(new XSSFCellFill(fill, this.indexedColors));
                }
            }
            CTBorders ctborders = styleSheet.getBorders();
            if (ctborders != null) {
                CTBorder[] arr$4 = ctborders.getBorderArray();
                for (CTBorder border : arr$4) {
                    this.borders.add(new XSSFCellBorder(border, this.indexedColors));
                }
            }
            CTCellXfs cellXfs = styleSheet.getCellXfs();
            if (cellXfs != null) {
                this.xfs.addAll(Arrays.asList(cellXfs.getXfArray()));
            }
            CTCellStyleXfs cellStyleXfs = styleSheet.getCellStyleXfs();
            if (cellStyleXfs != null) {
                this.styleXfs.addAll(Arrays.asList(cellStyleXfs.getXfArray()));
            }
            CTDxfs styleDxfs = styleSheet.getDxfs();
            if (styleDxfs != null) {
                this.dxfs.addAll(Arrays.asList(styleDxfs.getDxfArray()));
            }
            CTTableStyles ctTableStyles = styleSheet.getTableStyles();
            if (ctTableStyles != null) {
                int idx2 = 0;
                for (CTTableStyle style : Arrays.asList(ctTableStyles.getTableStyleArray())) {
                    this.tableStyles.put(style.getName(), new XSSFTableStyle(idx2, styleDxfs, style, this.indexedColors));
                    idx2++;
                }
            }
        } catch (XmlException e) {
            throw new IOException(e.getLocalizedMessage());
        }
    }

    public String getNumberFormatAt(short fmtId) {
        return this.numberFormats.get(Short.valueOf(fmtId));
    }

    private short getNumberFormatId(String fmt) {
        for (Map.Entry<Short, String> numFmt : this.numberFormats.entrySet()) {
            if (numFmt.getValue().equals(fmt)) {
                return numFmt.getKey().shortValue();
            }
        }
        throw new IllegalStateException("Number format not in style table: " + fmt);
    }

    public int putNumberFormat(String fmt) {
        short formatIndex;
        if (this.numberFormats.containsValue(fmt)) {
            try {
                return getNumberFormatId(fmt);
            } catch (IllegalStateException e) {
                throw new IllegalStateException("Found the format, but couldn't figure out where - should never happen!");
            }
        }
        if (this.numberFormats.size() >= this.MAXIMUM_NUMBER_OF_DATA_FORMATS) {
            throw new IllegalStateException("The maximum number of Data Formats was exceeded. You can define up to " + this.MAXIMUM_NUMBER_OF_DATA_FORMATS + " formats in a .xlsx Workbook.");
        }
        if (this.numberFormats.isEmpty()) {
            formatIndex = 164;
        } else {
            short nextKey = (short) (this.numberFormats.lastKey().shortValue() + 1);
            if (nextKey < 0) {
                throw new IllegalStateException("Cowardly avoiding creating a number format with a negative id.This is probably due to arithmetic overflow.");
            }
            formatIndex = (short) Math.max((int) nextKey, 164);
        }
        this.numberFormats.put(Short.valueOf(formatIndex), fmt);
        return formatIndex;
    }

    public void putNumberFormat(short index, String fmt) {
        this.numberFormats.put(Short.valueOf(index), fmt);
    }

    public boolean removeNumberFormat(short index) {
        String fmt = this.numberFormats.remove(Short.valueOf(index));
        boolean removed = fmt != null;
        if (removed) {
            for (CTXf style : this.xfs) {
                if (style.isSetNumFmtId() && style.getNumFmtId() == index) {
                    style.unsetApplyNumberFormat();
                    style.unsetNumFmtId();
                }
            }
        }
        return removed;
    }

    public boolean removeNumberFormat(String fmt) {
        short id = getNumberFormatId(fmt);
        return removeNumberFormat(id);
    }

    public XSSFFont getFontAt(int idx) {
        return this.fonts.get(idx);
    }

    public int putFont(XSSFFont font, boolean forceRegistration) {
        int idx = -1;
        if (!forceRegistration) {
            idx = this.fonts.indexOf(font);
        }
        if (idx != -1) {
            return idx;
        }
        int idx2 = this.fonts.size();
        this.fonts.add(font);
        return idx2;
    }

    public int putFont(XSSFFont font) {
        return putFont(font, false);
    }

    public XSSFCellStyle getStyleAt(int idx) {
        int styleXfId = 0;
        if (idx < 0 || idx >= this.xfs.size()) {
            return null;
        }
        if (this.xfs.get(idx).getXfId() > 0) {
            styleXfId = (int) this.xfs.get(idx).getXfId();
        }
        return new XSSFCellStyle(idx, styleXfId, this, this.theme);
    }

    public int putStyle(XSSFCellStyle style) {
        CTXf mainXF = style.getCoreXf();
        if (!this.xfs.contains(mainXF)) {
            this.xfs.add(mainXF);
        }
        return this.xfs.indexOf(mainXF);
    }

    public XSSFCellBorder getBorderAt(int idx) {
        return this.borders.get(idx);
    }

    public int putBorder(XSSFCellBorder border) {
        int idx = this.borders.indexOf(border);
        if (idx != -1) {
            return idx;
        }
        this.borders.add(border);
        border.setThemesTable(this.theme);
        return this.borders.size() - 1;
    }

    public XSSFCellFill getFillAt(int idx) {
        return this.fills.get(idx);
    }

    public List<XSSFCellBorder> getBorders() {
        return Collections.unmodifiableList(this.borders);
    }

    public List<XSSFCellFill> getFills() {
        return Collections.unmodifiableList(this.fills);
    }

    public List<XSSFFont> getFonts() {
        return Collections.unmodifiableList(this.fonts);
    }

    public Map<Short, String> getNumberFormats() {
        return Collections.unmodifiableMap(this.numberFormats);
    }

    public int putFill(XSSFCellFill fill) {
        int idx = this.fills.indexOf(fill);
        if (idx != -1) {
            return idx;
        }
        this.fills.add(fill);
        return this.fills.size() - 1;
    }

    @Internal
    public CTXf getCellXfAt(int idx) {
        return this.xfs.get(idx);
    }

    @Internal
    public int putCellXf(CTXf cellXf) {
        this.xfs.add(cellXf);
        return this.xfs.size();
    }

    @Internal
    public void replaceCellXfAt(int idx, CTXf cellXf) {
        this.xfs.set(idx, cellXf);
    }

    @Internal
    public CTXf getCellStyleXfAt(int idx) {
        try {
            return this.styleXfs.get(idx);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Internal
    public int putCellStyleXf(CTXf cellStyleXf) {
        this.styleXfs.add(cellStyleXf);
        return this.styleXfs.size();
    }

    @Internal
    protected void replaceCellStyleXfAt(int idx, CTXf cellStyleXf) {
        this.styleXfs.set(idx, cellStyleXf);
    }

    public int getNumCellStyles() {
        return this.xfs.size();
    }

    public int getNumDataFormats() {
        return this.numberFormats.size();
    }

    @Internal
    int _getXfsSize() {
        return this.xfs.size();
    }

    @Internal
    public int _getStyleXfsSize() {
        return this.styleXfs.size();
    }

    @Internal
    public CTStylesheet getCTStylesheet() {
        return this.doc.getStyleSheet();
    }

    @Internal
    public int _getDXfsSize() {
        return this.dxfs.size();
    }

    public void writeTo(OutputStream out) throws IOException {
        CTStylesheet styleSheet = this.doc.getStyleSheet();
        CTNumFmts formats = CTNumFmts.Factory.newInstance();
        formats.setCount(this.numberFormats.size());
        for (Map.Entry<Short, String> entry : this.numberFormats.entrySet()) {
            CTNumFmt ctFmt = formats.addNewNumFmt();
            ctFmt.setNumFmtId(entry.getKey().shortValue());
            ctFmt.setFormatCode(entry.getValue());
        }
        styleSheet.setNumFmts(formats);
        CTFonts ctFonts = styleSheet.getFonts();
        if (ctFonts == null) {
            ctFonts = CTFonts.Factory.newInstance();
        }
        ctFonts.setCount(this.fonts.size());
        CTFont[] ctfnt = new CTFont[this.fonts.size()];
        int idx = 0;
        for (XSSFFont f : this.fonts) {
            int i = idx;
            idx++;
            ctfnt[i] = f.getCTFont();
        }
        ctFonts.setFontArray(ctfnt);
        styleSheet.setFonts(ctFonts);
        CTFills ctFills = styleSheet.getFills();
        if (ctFills == null) {
            ctFills = CTFills.Factory.newInstance();
        }
        ctFills.setCount(this.fills.size());
        CTFill[] ctf = new CTFill[this.fills.size()];
        int idx2 = 0;
        for (XSSFCellFill f2 : this.fills) {
            int i2 = idx2;
            idx2++;
            ctf[i2] = f2.getCTFill();
        }
        ctFills.setFillArray(ctf);
        styleSheet.setFills(ctFills);
        CTBorders ctBorders = styleSheet.getBorders();
        if (ctBorders == null) {
            ctBorders = CTBorders.Factory.newInstance();
        }
        ctBorders.setCount(this.borders.size());
        CTBorder[] ctb = new CTBorder[this.borders.size()];
        int idx3 = 0;
        for (XSSFCellBorder b : this.borders) {
            int i3 = idx3;
            idx3++;
            ctb[i3] = b.getCTBorder();
        }
        ctBorders.setBorderArray(ctb);
        styleSheet.setBorders(ctBorders);
        if (this.xfs.size() > 0) {
            CTCellXfs ctXfs = styleSheet.getCellXfs();
            if (ctXfs == null) {
                ctXfs = CTCellXfs.Factory.newInstance();
            }
            ctXfs.setCount(this.xfs.size());
            ctXfs.setXfArray((CTXf[]) this.xfs.toArray(new CTXf[this.xfs.size()]));
            styleSheet.setCellXfs(ctXfs);
        }
        if (this.styleXfs.size() > 0) {
            CTCellStyleXfs ctSXfs = styleSheet.getCellStyleXfs();
            if (ctSXfs == null) {
                ctSXfs = CTCellStyleXfs.Factory.newInstance();
            }
            ctSXfs.setCount(this.styleXfs.size());
            ctSXfs.setXfArray((CTXf[]) this.styleXfs.toArray(new CTXf[this.styleXfs.size()]));
            styleSheet.setCellStyleXfs(ctSXfs);
        }
        if (this.dxfs.size() > 0) {
            CTDxfs ctDxfs = styleSheet.getDxfs();
            if (ctDxfs == null) {
                ctDxfs = CTDxfs.Factory.newInstance();
            }
            ctDxfs.setCount(this.dxfs.size());
            ctDxfs.setDxfArray((CTDxf[]) this.dxfs.toArray(new CTDxf[this.dxfs.size()]));
            styleSheet.setDxfs(ctDxfs);
        }
        this.doc.save(out, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
    }

    @Override // org.apache.poi.POIXMLDocumentPart
    protected void commit() throws IOException {
        PackagePart part = getPackagePart();
        OutputStream out = part.getOutputStream();
        writeTo(out);
        out.close();
    }

    private void initialize() {
        XSSFFont xssfFont = createDefaultFont();
        this.fonts.add(xssfFont);
        CTFill[] ctFill = createDefaultFills();
        this.fills.add(new XSSFCellFill(ctFill[0], this.indexedColors));
        this.fills.add(new XSSFCellFill(ctFill[1], this.indexedColors));
        CTBorder ctBorder = createDefaultBorder();
        this.borders.add(new XSSFCellBorder(ctBorder));
        CTXf styleXf = createDefaultXf();
        this.styleXfs.add(styleXf);
        CTXf xf = createDefaultXf();
        xf.setXfId(0L);
        this.xfs.add(xf);
    }

    private static CTXf createDefaultXf() {
        CTXf ctXf = CTXf.Factory.newInstance();
        ctXf.setNumFmtId(0L);
        ctXf.setFontId(0L);
        ctXf.setFillId(0L);
        ctXf.setBorderId(0L);
        return ctXf;
    }

    private static CTBorder createDefaultBorder() {
        CTBorder ctBorder = CTBorder.Factory.newInstance();
        ctBorder.addNewBottom();
        ctBorder.addNewTop();
        ctBorder.addNewLeft();
        ctBorder.addNewRight();
        ctBorder.addNewDiagonal();
        return ctBorder;
    }

    private static CTFill[] createDefaultFills() {
        CTFill[] ctFill = {CTFill.Factory.newInstance(), CTFill.Factory.newInstance()};
        ctFill[0].addNewPatternFill().setPatternType(STPatternType.NONE);
        ctFill[1].addNewPatternFill().setPatternType(STPatternType.DARK_GRAY);
        return ctFill;
    }

    private static XSSFFont createDefaultFont() {
        CTFont ctFont = CTFont.Factory.newInstance();
        XSSFFont xssfFont = new XSSFFont(ctFont, 0, null);
        xssfFont.setFontHeightInPoints((short) 11);
        xssfFont.setColor(XSSFFont.DEFAULT_FONT_COLOR);
        xssfFont.setFontName(XSSFFont.DEFAULT_FONT_NAME);
        xssfFont.setFamily(FontFamily.SWISS);
        xssfFont.setScheme(FontScheme.MINOR);
        return xssfFont;
    }

    @Internal
    public CTDxf getDxfAt(int idx) {
        return this.dxfs.get(idx);
    }

    @Internal
    public int putDxf(CTDxf dxf) {
        this.dxfs.add(dxf);
        return this.dxfs.size();
    }

    public TableStyle getExplicitTableStyle(String name) {
        return this.tableStyles.get(name);
    }

    public Set<String> getExplicitTableStyleNames() {
        return this.tableStyles.keySet();
    }

    public TableStyle getTableStyle(String name) {
        if (name == null) {
            return null;
        }
        try {
            return XSSFBuiltinTableStyle.valueOf(name).getStyle();
        } catch (IllegalArgumentException e) {
            return getExplicitTableStyle(name);
        }
    }

    public XSSFCellStyle createCellStyle() {
        if (getNumCellStyles() > MAXIMUM_STYLE_ID) {
            throw new IllegalStateException("The maximum number of Cell Styles was exceeded. You can define up to " + MAXIMUM_STYLE_ID + " style in a .xlsx Workbook");
        }
        int xfSize = this.styleXfs.size();
        CTXf xf = CTXf.Factory.newInstance();
        xf.setNumFmtId(0L);
        xf.setFontId(0L);
        xf.setFillId(0L);
        xf.setBorderId(0L);
        xf.setXfId(0L);
        int indexXf = putCellXf(xf);
        return new XSSFCellStyle(indexXf - 1, xfSize - 1, this, this.theme);
    }

    public XSSFFont findFont(boolean bold, short color, short fontHeight, String name, boolean italic, boolean strikeout, short typeOffset, byte underline) {
        for (XSSFFont font : this.fonts) {
            if (font.getBold() == bold && font.getColor() == color && font.getFontHeight() == fontHeight && font.getFontName().equals(name) && font.getItalic() == italic && font.getStrikeout() == strikeout && font.getTypeOffset() == typeOffset && font.getUnderline() == underline) {
                return font;
            }
        }
        return null;
    }

    public IndexedColorMap getIndexedColors() {
        return this.indexedColors;
    }
}
