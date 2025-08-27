package org.apache.poi.xssf.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRst;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSst;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.SstDocument;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/model/SharedStringsTable.class */
public class SharedStringsTable extends POIXMLDocumentPart {
    private final List<CTRst> strings;
    private final Map<String, Integer> stmap;
    private int count;
    private int uniqueCount;
    private SstDocument _sstDoc;
    private static final XmlOptions options = new XmlOptions();

    static {
        options.put(XmlOptions.SAVE_INNER);
        options.put(XmlOptions.SAVE_AGGRESSIVE_NAMESPACES);
        options.put(XmlOptions.SAVE_USE_DEFAULT_NAMESPACE);
        options.setSaveImplicitNamespaces(Collections.singletonMap("", XSSFRelation.NS_SPREADSHEETML));
    }

    public SharedStringsTable() {
        this.strings = new ArrayList();
        this.stmap = new HashMap();
        this._sstDoc = SstDocument.Factory.newInstance();
        this._sstDoc.addNewSst();
    }

    public SharedStringsTable(PackagePart part) throws IOException {
        super(part);
        this.strings = new ArrayList();
        this.stmap = new HashMap();
        readFrom(part.getInputStream());
    }

    public void readFrom(InputStream is) throws IOException {
        try {
            int cnt = 0;
            this._sstDoc = SstDocument.Factory.parse(is, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
            CTSst sst = this._sstDoc.getSst();
            this.count = (int) sst.getCount();
            this.uniqueCount = (int) sst.getUniqueCount();
            CTRst[] arr$ = sst.getSiArray();
            for (CTRst st : arr$) {
                this.stmap.put(getKey(st), Integer.valueOf(cnt));
                this.strings.add(st);
                cnt++;
            }
        } catch (XmlException e) {
            throw new IOException("unable to parse shared strings table", e);
        }
    }

    private String getKey(CTRst st) {
        return st.xmlText(options);
    }

    public CTRst getEntryAt(int idx) {
        return this.strings.get(idx);
    }

    public int getCount() {
        return this.count;
    }

    public int getUniqueCount() {
        return this.uniqueCount;
    }

    public int addEntry(CTRst st) {
        String s = getKey(st);
        this.count++;
        if (this.stmap.containsKey(s)) {
            return this.stmap.get(s).intValue();
        }
        this.uniqueCount++;
        CTRst newSt = this._sstDoc.getSst().addNewSi();
        newSt.set(st);
        int idx = this.strings.size();
        this.stmap.put(s, Integer.valueOf(idx));
        this.strings.add(newSt);
        return idx;
    }

    public List<CTRst> getItems() {
        return Collections.unmodifiableList(this.strings);
    }

    public void writeTo(OutputStream out) throws IOException {
        XmlOptions xmlOptions = new XmlOptions(POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
        xmlOptions.setSaveCDataLengthThreshold(SchemaType.SIZE_BIG_INTEGER);
        xmlOptions.setSaveCDataEntityCountThreshold(-1);
        CTSst sst = this._sstDoc.getSst();
        sst.setCount(this.count);
        sst.setUniqueCount(this.uniqueCount);
        this._sstDoc.save(out, xmlOptions);
    }

    @Override // org.apache.poi.POIXMLDocumentPart
    protected void commit() throws IOException {
        PackagePart part = getPackagePart();
        OutputStream out = part.getOutputStream();
        writeTo(out);
        out.close();
    }
}
