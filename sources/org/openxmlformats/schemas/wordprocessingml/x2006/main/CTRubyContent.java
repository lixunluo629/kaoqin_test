package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTRubyContent.class */
public interface CTRubyContent extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTRubyContent.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctrubycontent1d33type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTRubyContent$Factory.class */
    public static final class Factory {
        public static CTRubyContent newInstance() {
            return (CTRubyContent) POIXMLTypeLoader.newInstance(CTRubyContent.type, null);
        }

        public static CTRubyContent newInstance(XmlOptions xmlOptions) {
            return (CTRubyContent) POIXMLTypeLoader.newInstance(CTRubyContent.type, xmlOptions);
        }

        public static CTRubyContent parse(String str) throws XmlException {
            return (CTRubyContent) POIXMLTypeLoader.parse(str, CTRubyContent.type, (XmlOptions) null);
        }

        public static CTRubyContent parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTRubyContent) POIXMLTypeLoader.parse(str, CTRubyContent.type, xmlOptions);
        }

        public static CTRubyContent parse(File file) throws XmlException, IOException {
            return (CTRubyContent) POIXMLTypeLoader.parse(file, CTRubyContent.type, (XmlOptions) null);
        }

        public static CTRubyContent parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRubyContent) POIXMLTypeLoader.parse(file, CTRubyContent.type, xmlOptions);
        }

        public static CTRubyContent parse(URL url) throws XmlException, IOException {
            return (CTRubyContent) POIXMLTypeLoader.parse(url, CTRubyContent.type, (XmlOptions) null);
        }

        public static CTRubyContent parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRubyContent) POIXMLTypeLoader.parse(url, CTRubyContent.type, xmlOptions);
        }

        public static CTRubyContent parse(InputStream inputStream) throws XmlException, IOException {
            return (CTRubyContent) POIXMLTypeLoader.parse(inputStream, CTRubyContent.type, (XmlOptions) null);
        }

        public static CTRubyContent parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRubyContent) POIXMLTypeLoader.parse(inputStream, CTRubyContent.type, xmlOptions);
        }

        public static CTRubyContent parse(Reader reader) throws XmlException, IOException {
            return (CTRubyContent) POIXMLTypeLoader.parse(reader, CTRubyContent.type, (XmlOptions) null);
        }

        public static CTRubyContent parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRubyContent) POIXMLTypeLoader.parse(reader, CTRubyContent.type, xmlOptions);
        }

        public static CTRubyContent parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTRubyContent) POIXMLTypeLoader.parse(xMLStreamReader, CTRubyContent.type, (XmlOptions) null);
        }

        public static CTRubyContent parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTRubyContent) POIXMLTypeLoader.parse(xMLStreamReader, CTRubyContent.type, xmlOptions);
        }

        public static CTRubyContent parse(Node node) throws XmlException {
            return (CTRubyContent) POIXMLTypeLoader.parse(node, CTRubyContent.type, (XmlOptions) null);
        }

        public static CTRubyContent parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTRubyContent) POIXMLTypeLoader.parse(node, CTRubyContent.type, xmlOptions);
        }

        public static CTRubyContent parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTRubyContent) POIXMLTypeLoader.parse(xMLInputStream, CTRubyContent.type, (XmlOptions) null);
        }

        public static CTRubyContent parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTRubyContent) POIXMLTypeLoader.parse(xMLInputStream, CTRubyContent.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTRubyContent.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTRubyContent.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTR> getRList();

    CTR[] getRArray();

    CTR getRArray(int i);

    int sizeOfRArray();

    void setRArray(CTR[] ctrArr);

    void setRArray(int i, CTR ctr);

    CTR insertNewR(int i);

    CTR addNewR();

    void removeR(int i);

    List<CTProofErr> getProofErrList();

    CTProofErr[] getProofErrArray();

    CTProofErr getProofErrArray(int i);

    int sizeOfProofErrArray();

    void setProofErrArray(CTProofErr[] cTProofErrArr);

    void setProofErrArray(int i, CTProofErr cTProofErr);

    CTProofErr insertNewProofErr(int i);

    CTProofErr addNewProofErr();

    void removeProofErr(int i);

    List<CTPermStart> getPermStartList();

    CTPermStart[] getPermStartArray();

    CTPermStart getPermStartArray(int i);

    int sizeOfPermStartArray();

    void setPermStartArray(CTPermStart[] cTPermStartArr);

    void setPermStartArray(int i, CTPermStart cTPermStart);

    CTPermStart insertNewPermStart(int i);

    CTPermStart addNewPermStart();

    void removePermStart(int i);

    List<CTPerm> getPermEndList();

    CTPerm[] getPermEndArray();

    CTPerm getPermEndArray(int i);

    int sizeOfPermEndArray();

    void setPermEndArray(CTPerm[] cTPermArr);

    void setPermEndArray(int i, CTPerm cTPerm);

    CTPerm insertNewPermEnd(int i);

    CTPerm addNewPermEnd();

    void removePermEnd(int i);

    List<CTBookmark> getBookmarkStartList();

    CTBookmark[] getBookmarkStartArray();

    CTBookmark getBookmarkStartArray(int i);

    int sizeOfBookmarkStartArray();

    void setBookmarkStartArray(CTBookmark[] cTBookmarkArr);

    void setBookmarkStartArray(int i, CTBookmark cTBookmark);

    CTBookmark insertNewBookmarkStart(int i);

    CTBookmark addNewBookmarkStart();

    void removeBookmarkStart(int i);

    List<CTMarkupRange> getBookmarkEndList();

    CTMarkupRange[] getBookmarkEndArray();

    CTMarkupRange getBookmarkEndArray(int i);

    int sizeOfBookmarkEndArray();

    void setBookmarkEndArray(CTMarkupRange[] cTMarkupRangeArr);

    void setBookmarkEndArray(int i, CTMarkupRange cTMarkupRange);

    CTMarkupRange insertNewBookmarkEnd(int i);

    CTMarkupRange addNewBookmarkEnd();

    void removeBookmarkEnd(int i);

    List<CTMoveBookmark> getMoveFromRangeStartList();

    CTMoveBookmark[] getMoveFromRangeStartArray();

    CTMoveBookmark getMoveFromRangeStartArray(int i);

    int sizeOfMoveFromRangeStartArray();

    void setMoveFromRangeStartArray(CTMoveBookmark[] cTMoveBookmarkArr);

    void setMoveFromRangeStartArray(int i, CTMoveBookmark cTMoveBookmark);

    CTMoveBookmark insertNewMoveFromRangeStart(int i);

    CTMoveBookmark addNewMoveFromRangeStart();

    void removeMoveFromRangeStart(int i);

    List<CTMarkupRange> getMoveFromRangeEndList();

    CTMarkupRange[] getMoveFromRangeEndArray();

    CTMarkupRange getMoveFromRangeEndArray(int i);

    int sizeOfMoveFromRangeEndArray();

    void setMoveFromRangeEndArray(CTMarkupRange[] cTMarkupRangeArr);

    void setMoveFromRangeEndArray(int i, CTMarkupRange cTMarkupRange);

    CTMarkupRange insertNewMoveFromRangeEnd(int i);

    CTMarkupRange addNewMoveFromRangeEnd();

    void removeMoveFromRangeEnd(int i);

    List<CTMoveBookmark> getMoveToRangeStartList();

    CTMoveBookmark[] getMoveToRangeStartArray();

    CTMoveBookmark getMoveToRangeStartArray(int i);

    int sizeOfMoveToRangeStartArray();

    void setMoveToRangeStartArray(CTMoveBookmark[] cTMoveBookmarkArr);

    void setMoveToRangeStartArray(int i, CTMoveBookmark cTMoveBookmark);

    CTMoveBookmark insertNewMoveToRangeStart(int i);

    CTMoveBookmark addNewMoveToRangeStart();

    void removeMoveToRangeStart(int i);

    List<CTMarkupRange> getMoveToRangeEndList();

    CTMarkupRange[] getMoveToRangeEndArray();

    CTMarkupRange getMoveToRangeEndArray(int i);

    int sizeOfMoveToRangeEndArray();

    void setMoveToRangeEndArray(CTMarkupRange[] cTMarkupRangeArr);

    void setMoveToRangeEndArray(int i, CTMarkupRange cTMarkupRange);

    CTMarkupRange insertNewMoveToRangeEnd(int i);

    CTMarkupRange addNewMoveToRangeEnd();

    void removeMoveToRangeEnd(int i);

    List<CTMarkupRange> getCommentRangeStartList();

    CTMarkupRange[] getCommentRangeStartArray();

    CTMarkupRange getCommentRangeStartArray(int i);

    int sizeOfCommentRangeStartArray();

    void setCommentRangeStartArray(CTMarkupRange[] cTMarkupRangeArr);

    void setCommentRangeStartArray(int i, CTMarkupRange cTMarkupRange);

    CTMarkupRange insertNewCommentRangeStart(int i);

    CTMarkupRange addNewCommentRangeStart();

    void removeCommentRangeStart(int i);

    List<CTMarkupRange> getCommentRangeEndList();

    CTMarkupRange[] getCommentRangeEndArray();

    CTMarkupRange getCommentRangeEndArray(int i);

    int sizeOfCommentRangeEndArray();

    void setCommentRangeEndArray(CTMarkupRange[] cTMarkupRangeArr);

    void setCommentRangeEndArray(int i, CTMarkupRange cTMarkupRange);

    CTMarkupRange insertNewCommentRangeEnd(int i);

    CTMarkupRange addNewCommentRangeEnd();

    void removeCommentRangeEnd(int i);

    List<CTTrackChange> getCustomXmlInsRangeStartList();

    CTTrackChange[] getCustomXmlInsRangeStartArray();

    CTTrackChange getCustomXmlInsRangeStartArray(int i);

    int sizeOfCustomXmlInsRangeStartArray();

    void setCustomXmlInsRangeStartArray(CTTrackChange[] cTTrackChangeArr);

    void setCustomXmlInsRangeStartArray(int i, CTTrackChange cTTrackChange);

    CTTrackChange insertNewCustomXmlInsRangeStart(int i);

    CTTrackChange addNewCustomXmlInsRangeStart();

    void removeCustomXmlInsRangeStart(int i);

    List<CTMarkup> getCustomXmlInsRangeEndList();

    CTMarkup[] getCustomXmlInsRangeEndArray();

    CTMarkup getCustomXmlInsRangeEndArray(int i);

    int sizeOfCustomXmlInsRangeEndArray();

    void setCustomXmlInsRangeEndArray(CTMarkup[] cTMarkupArr);

    void setCustomXmlInsRangeEndArray(int i, CTMarkup cTMarkup);

    CTMarkup insertNewCustomXmlInsRangeEnd(int i);

    CTMarkup addNewCustomXmlInsRangeEnd();

    void removeCustomXmlInsRangeEnd(int i);

    List<CTTrackChange> getCustomXmlDelRangeStartList();

    CTTrackChange[] getCustomXmlDelRangeStartArray();

    CTTrackChange getCustomXmlDelRangeStartArray(int i);

    int sizeOfCustomXmlDelRangeStartArray();

    void setCustomXmlDelRangeStartArray(CTTrackChange[] cTTrackChangeArr);

    void setCustomXmlDelRangeStartArray(int i, CTTrackChange cTTrackChange);

    CTTrackChange insertNewCustomXmlDelRangeStart(int i);

    CTTrackChange addNewCustomXmlDelRangeStart();

    void removeCustomXmlDelRangeStart(int i);

    List<CTMarkup> getCustomXmlDelRangeEndList();

    CTMarkup[] getCustomXmlDelRangeEndArray();

    CTMarkup getCustomXmlDelRangeEndArray(int i);

    int sizeOfCustomXmlDelRangeEndArray();

    void setCustomXmlDelRangeEndArray(CTMarkup[] cTMarkupArr);

    void setCustomXmlDelRangeEndArray(int i, CTMarkup cTMarkup);

    CTMarkup insertNewCustomXmlDelRangeEnd(int i);

    CTMarkup addNewCustomXmlDelRangeEnd();

    void removeCustomXmlDelRangeEnd(int i);

    List<CTTrackChange> getCustomXmlMoveFromRangeStartList();

    CTTrackChange[] getCustomXmlMoveFromRangeStartArray();

    CTTrackChange getCustomXmlMoveFromRangeStartArray(int i);

    int sizeOfCustomXmlMoveFromRangeStartArray();

    void setCustomXmlMoveFromRangeStartArray(CTTrackChange[] cTTrackChangeArr);

    void setCustomXmlMoveFromRangeStartArray(int i, CTTrackChange cTTrackChange);

    CTTrackChange insertNewCustomXmlMoveFromRangeStart(int i);

    CTTrackChange addNewCustomXmlMoveFromRangeStart();

    void removeCustomXmlMoveFromRangeStart(int i);

    List<CTMarkup> getCustomXmlMoveFromRangeEndList();

    CTMarkup[] getCustomXmlMoveFromRangeEndArray();

    CTMarkup getCustomXmlMoveFromRangeEndArray(int i);

    int sizeOfCustomXmlMoveFromRangeEndArray();

    void setCustomXmlMoveFromRangeEndArray(CTMarkup[] cTMarkupArr);

    void setCustomXmlMoveFromRangeEndArray(int i, CTMarkup cTMarkup);

    CTMarkup insertNewCustomXmlMoveFromRangeEnd(int i);

    CTMarkup addNewCustomXmlMoveFromRangeEnd();

    void removeCustomXmlMoveFromRangeEnd(int i);

    List<CTTrackChange> getCustomXmlMoveToRangeStartList();

    CTTrackChange[] getCustomXmlMoveToRangeStartArray();

    CTTrackChange getCustomXmlMoveToRangeStartArray(int i);

    int sizeOfCustomXmlMoveToRangeStartArray();

    void setCustomXmlMoveToRangeStartArray(CTTrackChange[] cTTrackChangeArr);

    void setCustomXmlMoveToRangeStartArray(int i, CTTrackChange cTTrackChange);

    CTTrackChange insertNewCustomXmlMoveToRangeStart(int i);

    CTTrackChange addNewCustomXmlMoveToRangeStart();

    void removeCustomXmlMoveToRangeStart(int i);

    List<CTMarkup> getCustomXmlMoveToRangeEndList();

    CTMarkup[] getCustomXmlMoveToRangeEndArray();

    CTMarkup getCustomXmlMoveToRangeEndArray(int i);

    int sizeOfCustomXmlMoveToRangeEndArray();

    void setCustomXmlMoveToRangeEndArray(CTMarkup[] cTMarkupArr);

    void setCustomXmlMoveToRangeEndArray(int i, CTMarkup cTMarkup);

    CTMarkup insertNewCustomXmlMoveToRangeEnd(int i);

    CTMarkup addNewCustomXmlMoveToRangeEnd();

    void removeCustomXmlMoveToRangeEnd(int i);

    List<CTRunTrackChange> getInsList();

    CTRunTrackChange[] getInsArray();

    CTRunTrackChange getInsArray(int i);

    int sizeOfInsArray();

    void setInsArray(CTRunTrackChange[] cTRunTrackChangeArr);

    void setInsArray(int i, CTRunTrackChange cTRunTrackChange);

    CTRunTrackChange insertNewIns(int i);

    CTRunTrackChange addNewIns();

    void removeIns(int i);

    List<CTRunTrackChange> getDelList();

    CTRunTrackChange[] getDelArray();

    CTRunTrackChange getDelArray(int i);

    int sizeOfDelArray();

    void setDelArray(CTRunTrackChange[] cTRunTrackChangeArr);

    void setDelArray(int i, CTRunTrackChange cTRunTrackChange);

    CTRunTrackChange insertNewDel(int i);

    CTRunTrackChange addNewDel();

    void removeDel(int i);

    List<CTRunTrackChange> getMoveFromList();

    CTRunTrackChange[] getMoveFromArray();

    CTRunTrackChange getMoveFromArray(int i);

    int sizeOfMoveFromArray();

    void setMoveFromArray(CTRunTrackChange[] cTRunTrackChangeArr);

    void setMoveFromArray(int i, CTRunTrackChange cTRunTrackChange);

    CTRunTrackChange insertNewMoveFrom(int i);

    CTRunTrackChange addNewMoveFrom();

    void removeMoveFrom(int i);

    List<CTRunTrackChange> getMoveToList();

    CTRunTrackChange[] getMoveToArray();

    CTRunTrackChange getMoveToArray(int i);

    int sizeOfMoveToArray();

    void setMoveToArray(CTRunTrackChange[] cTRunTrackChangeArr);

    void setMoveToArray(int i, CTRunTrackChange cTRunTrackChange);

    CTRunTrackChange insertNewMoveTo(int i);

    CTRunTrackChange addNewMoveTo();

    void removeMoveTo(int i);

    List<CTOMathPara> getOMathParaList();

    CTOMathPara[] getOMathParaArray();

    CTOMathPara getOMathParaArray(int i);

    int sizeOfOMathParaArray();

    void setOMathParaArray(CTOMathPara[] cTOMathParaArr);

    void setOMathParaArray(int i, CTOMathPara cTOMathPara);

    CTOMathPara insertNewOMathPara(int i);

    CTOMathPara addNewOMathPara();

    void removeOMathPara(int i);

    List<CTOMath> getOMathList();

    CTOMath[] getOMathArray();

    CTOMath getOMathArray(int i);

    int sizeOfOMathArray();

    void setOMathArray(CTOMath[] cTOMathArr);

    void setOMathArray(int i, CTOMath cTOMath);

    CTOMath insertNewOMath(int i);

    CTOMath addNewOMath();

    void removeOMath(int i);
}
