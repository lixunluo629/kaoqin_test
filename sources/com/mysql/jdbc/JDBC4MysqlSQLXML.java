package com.mysql.jdbc;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.SQLXML;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stax.StAXResult;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import net.dongliu.apk.parser.struct.xml.XmlCData;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/JDBC4MysqlSQLXML.class */
public class JDBC4MysqlSQLXML implements SQLXML {
    private XMLInputFactory inputFactory;
    private XMLOutputFactory outputFactory;
    private String stringRep;
    private ResultSetInternalMethods owningResultSet;
    private int columnIndexOfXml;
    private boolean workingWithResult;
    private DOMResult asDOMResult;
    private SAXResult asSAXResult;
    private SimpleSaxToReader saxToReaderConverter;
    private StringWriter asStringWriter;
    private ByteArrayOutputStream asByteArrayOutputStream;
    private ExceptionInterceptor exceptionInterceptor;
    private boolean isClosed = false;
    private boolean fromResultSet = true;

    protected JDBC4MysqlSQLXML(ResultSetInternalMethods owner, int index, ExceptionInterceptor exceptionInterceptor) {
        this.owningResultSet = owner;
        this.columnIndexOfXml = index;
        this.exceptionInterceptor = exceptionInterceptor;
    }

    protected JDBC4MysqlSQLXML(ExceptionInterceptor exceptionInterceptor) {
        this.exceptionInterceptor = exceptionInterceptor;
    }

    @Override // java.sql.SQLXML
    public synchronized void free() throws SQLException {
        this.stringRep = null;
        this.asDOMResult = null;
        this.asSAXResult = null;
        this.inputFactory = null;
        this.outputFactory = null;
        this.owningResultSet = null;
        this.workingWithResult = false;
        this.isClosed = true;
    }

    @Override // java.sql.SQLXML
    public synchronized String getString() throws SQLException {
        checkClosed();
        checkWorkingWithResult();
        if (this.fromResultSet) {
            return this.owningResultSet.getString(this.columnIndexOfXml);
        }
        return this.stringRep;
    }

    private synchronized void checkClosed() throws SQLException {
        if (this.isClosed) {
            throw SQLError.createSQLException("SQLXMLInstance has been free()d", this.exceptionInterceptor);
        }
    }

    private synchronized void checkWorkingWithResult() throws SQLException {
        if (this.workingWithResult) {
            throw SQLError.createSQLException("Can't perform requested operation after getResult() has been called to write XML data", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
        }
    }

    @Override // java.sql.SQLXML
    public synchronized void setString(String str) throws SQLException {
        checkClosed();
        checkWorkingWithResult();
        this.stringRep = str;
        this.fromResultSet = false;
    }

    public synchronized boolean isEmpty() throws SQLException {
        checkClosed();
        checkWorkingWithResult();
        if (this.fromResultSet) {
            return false;
        }
        return this.stringRep == null || this.stringRep.length() == 0;
    }

    @Override // java.sql.SQLXML
    public synchronized InputStream getBinaryStream() throws SQLException {
        checkClosed();
        checkWorkingWithResult();
        return this.owningResultSet.getBinaryStream(this.columnIndexOfXml);
    }

    @Override // java.sql.SQLXML
    public synchronized Reader getCharacterStream() throws SQLException {
        checkClosed();
        checkWorkingWithResult();
        return this.owningResultSet.getCharacterStream(this.columnIndexOfXml);
    }

    @Override // java.sql.SQLXML
    public synchronized <T extends Source> T getSource(Class<T> clazz) throws SQLException {
        InputSource inputSource;
        InputSource inputSource2;
        Reader reader;
        Reader reader2;
        checkClosed();
        checkWorkingWithResult();
        if (clazz == null || clazz.equals(SAXSource.class)) {
            if (this.fromResultSet) {
                inputSource = new InputSource(this.owningResultSet.getCharacterStream(this.columnIndexOfXml));
            } else {
                inputSource = new InputSource(new StringReader(this.stringRep));
            }
            return new SAXSource(inputSource);
        }
        if (clazz.equals(DOMSource.class)) {
            try {
                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                builderFactory.setNamespaceAware(true);
                DocumentBuilder builder = builderFactory.newDocumentBuilder();
                if (this.fromResultSet) {
                    inputSource2 = new InputSource(this.owningResultSet.getCharacterStream(this.columnIndexOfXml));
                } else {
                    inputSource2 = new InputSource(new StringReader(this.stringRep));
                }
                return new DOMSource(builder.parse(inputSource2));
            } catch (Throwable t) {
                SQLException sqlEx = SQLError.createSQLException(t.getMessage(), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
                sqlEx.initCause(t);
                throw sqlEx;
            }
        }
        if (clazz.equals(StreamSource.class)) {
            if (this.fromResultSet) {
                reader2 = this.owningResultSet.getCharacterStream(this.columnIndexOfXml);
            } else {
                reader2 = new StringReader(this.stringRep);
            }
            return new StreamSource(reader2);
        }
        if (clazz.equals(StAXSource.class)) {
            try {
                if (this.fromResultSet) {
                    reader = this.owningResultSet.getCharacterStream(this.columnIndexOfXml);
                } else {
                    reader = new StringReader(this.stringRep);
                }
                return new StAXSource(this.inputFactory.createXMLStreamReader(reader));
            } catch (XMLStreamException ex) {
                SQLException sqlEx2 = SQLError.createSQLException(ex.getMessage(), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
                sqlEx2.initCause(ex);
                throw sqlEx2;
            }
        }
        throw SQLError.createSQLException("XML Source of type \"" + clazz.toString() + "\" Not supported.", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
    }

    @Override // java.sql.SQLXML
    public synchronized OutputStream setBinaryStream() throws SQLException {
        checkClosed();
        checkWorkingWithResult();
        this.workingWithResult = true;
        return setBinaryStreamInternal();
    }

    private synchronized OutputStream setBinaryStreamInternal() throws SQLException {
        this.asByteArrayOutputStream = new ByteArrayOutputStream();
        return this.asByteArrayOutputStream;
    }

    @Override // java.sql.SQLXML
    public synchronized Writer setCharacterStream() throws SQLException {
        checkClosed();
        checkWorkingWithResult();
        this.workingWithResult = true;
        return setCharacterStreamInternal();
    }

    private synchronized Writer setCharacterStreamInternal() throws SQLException {
        this.asStringWriter = new StringWriter();
        return this.asStringWriter;
    }

    @Override // java.sql.SQLXML
    public synchronized <T extends Result> T setResult(Class<T> clazz) throws SQLException {
        checkClosed();
        checkWorkingWithResult();
        this.workingWithResult = true;
        this.asDOMResult = null;
        this.asSAXResult = null;
        this.saxToReaderConverter = null;
        this.stringRep = null;
        this.asStringWriter = null;
        this.asByteArrayOutputStream = null;
        if (clazz == null || clazz.equals(SAXResult.class)) {
            this.saxToReaderConverter = new SimpleSaxToReader();
            this.asSAXResult = new SAXResult(this.saxToReaderConverter);
            return this.asSAXResult;
        }
        if (clazz.equals(DOMResult.class)) {
            this.asDOMResult = new DOMResult();
            return this.asDOMResult;
        }
        if (clazz.equals(StreamResult.class)) {
            return new StreamResult(setCharacterStreamInternal());
        }
        if (clazz.equals(StAXResult.class)) {
            try {
                if (this.outputFactory == null) {
                    this.outputFactory = XMLOutputFactory.newInstance();
                }
                return new StAXResult(this.outputFactory.createXMLEventWriter(setCharacterStreamInternal()));
            } catch (XMLStreamException ex) {
                SQLException sqlEx = SQLError.createSQLException(ex.getMessage(), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
                sqlEx.initCause(ex);
                throw sqlEx;
            }
        }
        throw SQLError.createSQLException("XML Result of type \"" + clazz.toString() + "\" Not supported.", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0040, code lost:
    
        r9 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0032, code lost:
    
        r0 = r0.getEncoding();
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x003d, code lost:
    
        if (r0 == null) goto L11;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.io.Reader binaryInputStreamStreamToReader(java.io.ByteArrayOutputStream r8) {
        /*
            r7 = this;
            java.lang.String r0 = "UTF-8"
            r9 = r0
            java.io.ByteArrayInputStream r0 = new java.io.ByteArrayInputStream     // Catch: java.lang.Throwable -> L49 java.io.UnsupportedEncodingException -> L5e
            r1 = r0
            r2 = r8
            byte[] r2 = r2.toByteArray()     // Catch: java.lang.Throwable -> L49 java.io.UnsupportedEncodingException -> L5e
            r1.<init>(r2)     // Catch: java.lang.Throwable -> L49 java.io.UnsupportedEncodingException -> L5e
            r10 = r0
            r0 = r7
            javax.xml.stream.XMLInputFactory r0 = r0.inputFactory     // Catch: java.lang.Throwable -> L49 java.io.UnsupportedEncodingException -> L5e
            r1 = r10
            javax.xml.stream.XMLStreamReader r0 = r0.createXMLStreamReader(r1)     // Catch: java.lang.Throwable -> L49 java.io.UnsupportedEncodingException -> L5e
            r11 = r0
            r0 = 0
            r12 = r0
        L1c:
            r0 = r11
            int r0 = r0.next()     // Catch: java.lang.Throwable -> L49 java.io.UnsupportedEncodingException -> L5e
            r1 = r0
            r12 = r1
            r1 = 8
            if (r0 == r1) goto L46
            r0 = r12
            r1 = 7
            if (r0 != r1) goto L1c
            r0 = r11
            java.lang.String r0 = r0.getEncoding()     // Catch: java.lang.Throwable -> L49 java.io.UnsupportedEncodingException -> L5e
            r13 = r0
            r0 = r13
            if (r0 == 0) goto L46
            r0 = r13
            r9 = r0
            goto L46
        L46:
            goto L4a
        L49:
            r10 = move-exception
        L4a:
            java.io.StringReader r0 = new java.io.StringReader     // Catch: java.io.UnsupportedEncodingException -> L5e
            r1 = r0
            java.lang.String r2 = new java.lang.String     // Catch: java.io.UnsupportedEncodingException -> L5e
            r3 = r2
            r4 = r8
            byte[] r4 = r4.toByteArray()     // Catch: java.io.UnsupportedEncodingException -> L5e
            r5 = r9
            r3.<init>(r4, r5)     // Catch: java.io.UnsupportedEncodingException -> L5e
            r1.<init>(r2)     // Catch: java.io.UnsupportedEncodingException -> L5e
            return r0
        L5e:
            r9 = move-exception
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            r1 = r0
            r2 = r9
            r1.<init>(r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.JDBC4MysqlSQLXML.binaryInputStreamStreamToReader(java.io.ByteArrayOutputStream):java.io.Reader");
    }

    protected String readerToString(Reader reader) throws SQLException, IOException {
        StringBuilder buf = new StringBuilder();
        char[] charBuf = new char[512];
        while (true) {
            try {
                int charsRead = reader.read(charBuf);
                if (charsRead != -1) {
                    buf.append(charBuf, 0, charsRead);
                } else {
                    return buf.toString();
                }
            } catch (IOException ioEx) {
                SQLException sqlEx = SQLError.createSQLException(ioEx.getMessage(), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
                sqlEx.initCause(ioEx);
                throw sqlEx;
            }
        }
    }

    protected synchronized Reader serializeAsCharacterStream() throws SQLException {
        checkClosed();
        if (this.workingWithResult) {
            if (this.stringRep != null) {
                return new StringReader(this.stringRep);
            }
            if (this.asDOMResult != null) {
                return new StringReader(domSourceToString());
            }
            if (this.asStringWriter != null) {
                return new StringReader(this.asStringWriter.toString());
            }
            if (this.asSAXResult != null) {
                return this.saxToReaderConverter.toReader();
            }
            if (this.asByteArrayOutputStream != null) {
                return binaryInputStreamStreamToReader(this.asByteArrayOutputStream);
            }
        }
        return this.owningResultSet.getCharacterStream(this.columnIndexOfXml);
    }

    protected String domSourceToString() throws SQLException {
        try {
            DOMSource source = new DOMSource(this.asDOMResult.getNode());
            Transformer identity = TransformerFactory.newInstance().newTransformer();
            StringWriter stringOut = new StringWriter();
            Result result = new StreamResult(stringOut);
            identity.transform(source, result);
            return stringOut.toString();
        } catch (Throwable t) {
            SQLException sqlEx = SQLError.createSQLException(t.getMessage(), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
            sqlEx.initCause(t);
            throw sqlEx;
        }
    }

    protected synchronized String serializeAsString() throws SQLException {
        checkClosed();
        if (this.workingWithResult) {
            if (this.stringRep != null) {
                return this.stringRep;
            }
            if (this.asDOMResult != null) {
                return domSourceToString();
            }
            if (this.asStringWriter != null) {
                return this.asStringWriter.toString();
            }
            if (this.asSAXResult != null) {
                return readerToString(this.saxToReaderConverter.toReader());
            }
            if (this.asByteArrayOutputStream != null) {
                return readerToString(binaryInputStreamStreamToReader(this.asByteArrayOutputStream));
            }
        }
        return this.owningResultSet.getString(this.columnIndexOfXml);
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/JDBC4MysqlSQLXML$SimpleSaxToReader.class */
    class SimpleSaxToReader extends DefaultHandler {
        StringBuilder buf = new StringBuilder();
        private boolean inCDATA = false;

        SimpleSaxToReader() {
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startDocument() throws SAXException {
            this.buf.append("<?xml version='1.0' encoding='UTF-8'?>");
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void endDocument() throws SAXException {
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startElement(String namespaceURI, String sName, String qName, Attributes attrs) throws SAXException {
            this.buf.append("<");
            this.buf.append(qName);
            if (attrs != null) {
                for (int i = 0; i < attrs.getLength(); i++) {
                    this.buf.append(SymbolConstants.SPACE_SYMBOL);
                    this.buf.append(attrs.getQName(i)).append("=\"");
                    escapeCharsForXml(attrs.getValue(i), true);
                    this.buf.append(SymbolConstants.QUOTES_SYMBOL);
                }
            }
            this.buf.append(">");
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void characters(char[] buf, int offset, int len) throws SAXException {
            if (!this.inCDATA) {
                escapeCharsForXml(buf, offset, len, false);
            } else {
                this.buf.append(buf, offset, len);
            }
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void ignorableWhitespace(char[] ch2, int start, int length) throws SAXException {
            characters(ch2, start, length);
        }

        public void startCDATA() throws SAXException {
            this.buf.append(XmlCData.CDATA_START);
            this.inCDATA = true;
        }

        public void endCDATA() throws SAXException {
            this.inCDATA = false;
            this.buf.append(XmlCData.CDATA_END);
        }

        public void comment(char[] ch2, int start, int length) throws SAXException {
            this.buf.append("<!--");
            for (int i = 0; i < length; i++) {
                this.buf.append(ch2[start + i]);
            }
            this.buf.append("-->");
        }

        Reader toReader() {
            return new StringReader(this.buf.toString());
        }

        private void escapeCharsForXml(String str, boolean isAttributeData) {
            if (str == null) {
                return;
            }
            int strLen = str.length();
            for (int i = 0; i < strLen; i++) {
                escapeCharsForXml(str.charAt(i), isAttributeData);
            }
        }

        private void escapeCharsForXml(char[] buf, int offset, int len, boolean isAttributeData) {
            if (buf == null) {
                return;
            }
            for (int i = 0; i < len; i++) {
                escapeCharsForXml(buf[offset + i], isAttributeData);
            }
        }

        private void escapeCharsForXml(char c, boolean isAttributeData) {
            switch (c) {
                case '\r':
                    this.buf.append("&#xD;");
                    break;
                case '\"':
                    if (!isAttributeData) {
                        this.buf.append(SymbolConstants.QUOTES_SYMBOL);
                        break;
                    } else {
                        this.buf.append("&quot;");
                        break;
                    }
                case '&':
                    this.buf.append("&amp;");
                    break;
                case '<':
                    this.buf.append("&lt;");
                    break;
                case '>':
                    this.buf.append("&gt;");
                    break;
                default:
                    if ((c >= 1 && c <= 31 && c != '\t' && c != '\n') || ((c >= 127 && c <= 159) || c == 8232 || (isAttributeData && (c == '\t' || c == '\n')))) {
                        this.buf.append("&#x");
                        this.buf.append(Integer.toHexString(c).toUpperCase());
                        this.buf.append(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
                        break;
                    } else {
                        this.buf.append(c);
                        break;
                    }
            }
        }
    }
}
