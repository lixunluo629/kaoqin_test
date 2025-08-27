package org.apache.poi.xssf.binary;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.util.Internal;
import org.apache.poi.util.LittleEndian;
import org.xml.sax.SAXException;

@Internal
/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/binary/XSSFBSharedStringsTable.class */
public class XSSFBSharedStringsTable {
    private int count;
    private int uniqueCount;
    private List<String> strings = new ArrayList();

    public XSSFBSharedStringsTable(OPCPackage pkg) throws SAXException, IOException {
        ArrayList<PackagePart> parts = pkg.getPartsByContentType(XSSFBRelation.SHARED_STRINGS_BINARY.getContentType());
        if (parts.size() > 0) {
            PackagePart sstPart = parts.get(0);
            readFrom(sstPart.getInputStream());
        }
    }

    XSSFBSharedStringsTable(PackagePart part) throws SAXException, IOException {
        readFrom(part.getInputStream());
    }

    private void readFrom(InputStream inputStream) throws IOException {
        SSTBinaryReader reader = new SSTBinaryReader(inputStream);
        reader.parse();
    }

    public List<String> getItems() {
        List<String> ret = new ArrayList<>(this.strings.size());
        ret.addAll(this.strings);
        return ret;
    }

    public String getEntryAt(int i) {
        return this.strings.get(i);
    }

    public int getCount() {
        return this.count;
    }

    public int getUniqueCount() {
        return this.uniqueCount;
    }

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/binary/XSSFBSharedStringsTable$SSTBinaryReader.class */
    private class SSTBinaryReader extends XSSFBParser {
        SSTBinaryReader(InputStream is) {
            super(is);
        }

        @Override // org.apache.poi.xssf.binary.XSSFBParser
        public void handleRecord(int recordType, byte[] data) throws XSSFBParseException {
            XSSFBRecordType type = XSSFBRecordType.lookup(recordType);
            switch (type) {
                case BrtSstItem:
                    XSSFBRichStr rstr = XSSFBRichStr.build(data, 0);
                    XSSFBSharedStringsTable.this.strings.add(rstr.getString());
                    break;
                case BrtBeginSst:
                    XSSFBSharedStringsTable.this.count = XSSFBUtils.castToInt(LittleEndian.getUInt(data, 0));
                    XSSFBSharedStringsTable.this.uniqueCount = XSSFBUtils.castToInt(LittleEndian.getUInt(data, 4));
                    break;
            }
        }
    }
}
