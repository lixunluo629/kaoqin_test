package org.apache.xmlbeans.impl.validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.common.ValidatorListener;
import org.apache.xmlbeans.impl.common.XmlWhitespace;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/validator/ValidatingXMLStreamReader.class */
public class ValidatingXMLStreamReader extends StreamReaderDelegate implements XMLStreamReader {
    public static final String OPTION_ATTTRIBUTE_VALIDATION_COMPAT_MODE = "OPTION_ATTTRIBUTE_VALIDATION_COMPAT_MODE";
    private static final String URI_XSI = "http://www.w3.org/2001/XMLSchema-instance";
    private static final QName XSI_TYPE;
    private static final QName XSI_NIL;
    private static final QName XSI_SL;
    private static final QName XSI_NSL;
    private SchemaType _contentType;
    private SchemaTypeLoader _stl;
    private XmlOptions _options;
    private Collection _errorListener;
    protected Validator _validator;
    private int _state;
    private List _attNamesList;
    private List _attValuesList;
    private SchemaType _xsiType;
    private int _depth;
    static final /* synthetic */ boolean $assertionsDisabled;
    private final int STATE_FIRSTEVENT = 0;
    private final int STATE_VALIDATING = 1;
    private final int STATE_ATTBUFFERING = 2;
    private final int STATE_ERROR = 3;
    private final ElementEventImpl _elemEvent = new ElementEventImpl();
    private final AttributeEventImpl _attEvent = new AttributeEventImpl();
    private final SimpleEventImpl _simpleEvent = new SimpleEventImpl();
    private PackTextXmlStreamReader _packTextXmlStreamReader = new PackTextXmlStreamReader();

    static {
        $assertionsDisabled = !ValidatingXMLStreamReader.class.desiredAssertionStatus();
        XSI_TYPE = new QName(URI_XSI, "type");
        XSI_NIL = new QName(URI_XSI, "nil");
        XSI_SL = new QName(URI_XSI, "schemaLocation");
        XSI_NSL = new QName(URI_XSI, "noNamespaceSchemaLocation");
    }

    public void init(XMLStreamReader xsr, boolean startWithCurrentEvent, SchemaType contentType, SchemaTypeLoader stl, XmlOptions options, Collection errorListener) {
        this._packTextXmlStreamReader.init(xsr);
        setParent(this._packTextXmlStreamReader);
        this._contentType = contentType;
        this._stl = stl;
        this._options = options;
        this._errorListener = errorListener;
        this._elemEvent.setXMLStreamReader(this._packTextXmlStreamReader);
        this._attEvent.setXMLStreamReader(this._packTextXmlStreamReader);
        this._simpleEvent.setXMLStreamReader(this._packTextXmlStreamReader);
        this._validator = null;
        this._state = 0;
        if (this._attNamesList != null) {
            this._attNamesList.clear();
            this._attValuesList.clear();
        }
        this._xsiType = null;
        this._depth = 0;
        if (startWithCurrentEvent) {
            int evType = getEventType();
            validate_event(evType);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/validator/ValidatingXMLStreamReader$PackTextXmlStreamReader.class */
    private static class PackTextXmlStreamReader extends StreamReaderDelegate implements XMLStreamReader {
        private boolean _hasBufferedText;
        private StringBuffer _buffer;
        private int _textEventType;
        static final /* synthetic */ boolean $assertionsDisabled;

        private PackTextXmlStreamReader() {
            this._buffer = new StringBuffer();
        }

        static {
            $assertionsDisabled = !ValidatingXMLStreamReader.class.desiredAssertionStatus();
        }

        void init(XMLStreamReader xmlstream) {
            setParent(xmlstream);
            this._hasBufferedText = false;
            this._buffer.delete(0, this._buffer.length());
        }

        @Override // javax.xml.stream.util.StreamReaderDelegate, javax.xml.stream.XMLStreamReader
        public int next() throws XMLStreamException {
            if (this._hasBufferedText) {
                clearBuffer();
                return super.getEventType();
            }
            int evType = super.next();
            if (evType == 4 || evType == 12 || evType == 6) {
                this._textEventType = evType;
                bufferText();
            }
            return evType;
        }

        private void clearBuffer() {
            this._buffer.delete(0, this._buffer.length());
            this._hasBufferedText = false;
        }

        private void bufferText() throws XMLStreamException {
            if (super.hasText()) {
                this._buffer.append(super.getText());
            }
            this._hasBufferedText = true;
            while (hasNext()) {
                int evType = super.next();
                switch (evType) {
                    case 4:
                    case 6:
                    case 12:
                        if (!super.hasText()) {
                            break;
                        } else {
                            this._buffer.append(super.getText());
                            break;
                        }
                    case 5:
                        break;
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    default:
                        return;
                }
            }
        }

        @Override // javax.xml.stream.util.StreamReaderDelegate, javax.xml.stream.XMLStreamReader
        public String getText() {
            if ($assertionsDisabled || this._hasBufferedText) {
                return this._buffer.toString();
            }
            throw new AssertionError();
        }

        @Override // javax.xml.stream.util.StreamReaderDelegate, javax.xml.stream.XMLStreamReader
        public int getTextLength() {
            if ($assertionsDisabled || this._hasBufferedText) {
                return this._buffer.length();
            }
            throw new AssertionError();
        }

        @Override // javax.xml.stream.util.StreamReaderDelegate, javax.xml.stream.XMLStreamReader
        public int getTextStart() {
            if ($assertionsDisabled || this._hasBufferedText) {
                return 0;
            }
            throw new AssertionError();
        }

        @Override // javax.xml.stream.util.StreamReaderDelegate, javax.xml.stream.XMLStreamReader
        public char[] getTextCharacters() {
            if ($assertionsDisabled || this._hasBufferedText) {
                return this._buffer.toString().toCharArray();
            }
            throw new AssertionError();
        }

        @Override // javax.xml.stream.util.StreamReaderDelegate, javax.xml.stream.XMLStreamReader
        public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length) {
            if (!$assertionsDisabled && !this._hasBufferedText) {
                throw new AssertionError();
            }
            this._buffer.getChars(sourceStart, sourceStart + length, target, targetStart);
            return length;
        }

        @Override // javax.xml.stream.util.StreamReaderDelegate, javax.xml.stream.XMLStreamReader
        public boolean isWhiteSpace() {
            if ($assertionsDisabled || this._hasBufferedText) {
                return XmlWhitespace.isAllSpace(this._buffer);
            }
            throw new AssertionError();
        }

        @Override // javax.xml.stream.util.StreamReaderDelegate, javax.xml.stream.XMLStreamReader
        public boolean hasText() {
            if (this._hasBufferedText) {
                return true;
            }
            return super.hasText();
        }

        @Override // javax.xml.stream.util.StreamReaderDelegate, javax.xml.stream.XMLStreamReader
        public int getEventType() {
            if (this._hasBufferedText) {
                return this._textEventType;
            }
            return super.getEventType();
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/validator/ValidatingXMLStreamReader$ElementEventImpl.class */
    private static class ElementEventImpl implements ValidatorListener.Event {
        private static final int BUF_LENGTH = 1024;
        private char[] _buf;
        private int _length;
        private boolean _supportForGetTextCharacters;
        private XMLStreamReader _xmlStream;

        private ElementEventImpl() {
            this._buf = new char[1024];
            this._supportForGetTextCharacters = true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setXMLStreamReader(XMLStreamReader xsr) {
            this._xmlStream = xsr;
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public XmlCursor getLocationAsCursor() {
            return null;
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public Location getLocation() {
            return this._xmlStream.getLocation();
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public String getXsiType() {
            return this._xmlStream.getAttributeValue(ValidatingXMLStreamReader.URI_XSI, "type");
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public String getXsiNil() {
            return this._xmlStream.getAttributeValue(ValidatingXMLStreamReader.URI_XSI, "nil");
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public String getXsiLoc() {
            return this._xmlStream.getAttributeValue(ValidatingXMLStreamReader.URI_XSI, "schemaLocation");
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public String getXsiNoLoc() {
            return this._xmlStream.getAttributeValue(ValidatingXMLStreamReader.URI_XSI, "noNamespaceSchemaLocation");
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public QName getName() {
            if (this._xmlStream.hasName()) {
                return new QName(this._xmlStream.getNamespaceURI(), this._xmlStream.getLocalName());
            }
            return null;
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public String getText() {
            this._length = 0;
            addTextToBuffer();
            return new String(this._buf, 0, this._length);
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public String getText(int wsr) {
            return XmlWhitespace.collapse(this._xmlStream.getText(), wsr);
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public boolean textIsWhitespace() {
            return this._xmlStream.isWhiteSpace();
        }

        @Override // org.apache.xmlbeans.impl.common.PrefixResolver
        public String getNamespaceForPrefix(String prefix) {
            return this._xmlStream.getNamespaceURI(prefix);
        }

        private void addTextToBuffer() {
            int textLength = this._xmlStream.getTextLength();
            ensureBufferLength(textLength);
            if (this._supportForGetTextCharacters) {
                try {
                    this._length = this._xmlStream.getTextCharacters(0, this._buf, this._length, textLength);
                } catch (Exception e) {
                    this._supportForGetTextCharacters = false;
                }
            }
            if (!this._supportForGetTextCharacters) {
                System.arraycopy(this._xmlStream.getTextCharacters(), this._xmlStream.getTextStart(), this._buf, this._length, textLength);
                this._length += textLength;
            }
        }

        private void ensureBufferLength(int lengthToAdd) {
            if (this._length + lengthToAdd > this._buf.length) {
                char[] newBuf = new char[this._length + lengthToAdd];
                if (this._length > 0) {
                    System.arraycopy(this._buf, 0, newBuf, 0, this._length);
                }
                this._buf = newBuf;
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/validator/ValidatingXMLStreamReader$AttributeEventImpl.class */
    private static final class AttributeEventImpl implements ValidatorListener.Event {
        private int _attIndex;
        private XMLStreamReader _xmlStream;
        static final /* synthetic */ boolean $assertionsDisabled;

        private AttributeEventImpl() {
        }

        static {
            $assertionsDisabled = !ValidatingXMLStreamReader.class.desiredAssertionStatus();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setXMLStreamReader(XMLStreamReader xsr) {
            this._xmlStream = xsr;
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public XmlCursor getLocationAsCursor() {
            return null;
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public Location getLocation() {
            return this._xmlStream.getLocation();
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public String getXsiType() {
            throw new IllegalStateException();
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public String getXsiNil() {
            throw new IllegalStateException();
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public String getXsiLoc() {
            throw new IllegalStateException();
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public String getXsiNoLoc() {
            throw new IllegalStateException();
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public QName getName() {
            if (!$assertionsDisabled && !this._xmlStream.isStartElement()) {
                throw new AssertionError("Not on Start Element.");
            }
            String uri = this._xmlStream.getAttributeNamespace(this._attIndex);
            QName qn = new QName(uri == null ? "" : uri, this._xmlStream.getAttributeLocalName(this._attIndex));
            return qn;
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public String getText() {
            if ($assertionsDisabled || this._xmlStream.isStartElement()) {
                return this._xmlStream.getAttributeValue(this._attIndex);
            }
            throw new AssertionError("Not on Start Element.");
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public String getText(int wsr) {
            if ($assertionsDisabled || this._xmlStream.isStartElement()) {
                return XmlWhitespace.collapse(this._xmlStream.getAttributeValue(this._attIndex), wsr);
            }
            throw new AssertionError("Not on Start Element.");
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public boolean textIsWhitespace() {
            throw new IllegalStateException();
        }

        @Override // org.apache.xmlbeans.impl.common.PrefixResolver
        public String getNamespaceForPrefix(String prefix) {
            if ($assertionsDisabled || this._xmlStream.isStartElement()) {
                return this._xmlStream.getNamespaceURI(prefix);
            }
            throw new AssertionError("Not on Start Element.");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setAttributeIndex(int attIndex) {
            this._attIndex = attIndex;
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/validator/ValidatingXMLStreamReader$SimpleEventImpl.class */
    private static final class SimpleEventImpl implements ValidatorListener.Event {
        private String _text;
        private QName _qname;
        private XMLStreamReader _xmlStream;

        private SimpleEventImpl() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setXMLStreamReader(XMLStreamReader xsr) {
            this._xmlStream = xsr;
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public XmlCursor getLocationAsCursor() {
            return null;
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public Location getLocation() {
            return this._xmlStream.getLocation();
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public String getXsiType() {
            return null;
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public String getXsiNil() {
            return null;
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public String getXsiLoc() {
            return null;
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public String getXsiNoLoc() {
            return null;
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public QName getName() {
            return this._qname;
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public String getText() {
            return this._text;
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public String getText(int wsr) {
            return XmlWhitespace.collapse(this._text, wsr);
        }

        @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
        public boolean textIsWhitespace() {
            return false;
        }

        @Override // org.apache.xmlbeans.impl.common.PrefixResolver
        public String getNamespaceForPrefix(String prefix) {
            return this._xmlStream.getNamespaceURI(prefix);
        }
    }

    @Override // javax.xml.stream.util.StreamReaderDelegate, javax.xml.stream.XMLStreamReader
    public Object getProperty(String s) throws IllegalArgumentException {
        return super.getProperty(s);
    }

    @Override // javax.xml.stream.util.StreamReaderDelegate, javax.xml.stream.XMLStreamReader
    public int next() throws XMLStreamException {
        int evType = super.next();
        validate_event(evType);
        return evType;
    }

    private void validate_event(int evType) {
        if (this._state == 3) {
            return;
        }
        if (this._depth < 0) {
            throw new IllegalArgumentException("ValidatingXMLStreamReader cannot go further than the subtree is was initialized on.");
        }
        switch (evType) {
            case 1:
                this._depth++;
                if (this._state == 2) {
                    pushBufferedAttributes();
                }
                if (this._validator == null) {
                    QName qname = new QName(getNamespaceURI(), getLocalName());
                    if (this._contentType == null) {
                        this._contentType = typeForGlobalElement(qname);
                    }
                    if (this._state != 3) {
                        initValidator(this._contentType);
                        this._validator.nextEvent(1, this._elemEvent);
                    } else {
                        return;
                    }
                }
                this._validator.nextEvent(1, this._elemEvent);
                int attCount = getAttributeCount();
                validate_attributes(attCount);
                return;
            case 2:
            case 8:
                this._depth--;
                if (this._state == 2) {
                    pushBufferedAttributes();
                }
                this._validator.nextEvent(2, this._elemEvent);
                return;
            case 3:
            case 5:
            case 6:
            case 9:
            case 11:
            case 13:
            case 14:
            case 15:
                return;
            case 4:
            case 12:
                if (this._state == 2) {
                    pushBufferedAttributes();
                }
                if (this._validator == null) {
                    if (this._contentType == null) {
                        if (!isWhiteSpace()) {
                            addError("No content type provided for validation of a content model.");
                            this._state = 3;
                            return;
                        }
                        return;
                    }
                    initValidator(this._contentType);
                    this._validator.nextEvent(1, this._simpleEvent);
                }
                this._validator.nextEvent(3, this._elemEvent);
                return;
            case 7:
                this._depth++;
                return;
            case 10:
                if (getAttributeCount() != 0) {
                    if (this._state == 0 || this._state == 2) {
                        for (int i = 0; i < getAttributeCount(); i++) {
                            QName qname2 = new QName(getAttributeNamespace(i), getAttributeLocalName(i));
                            if (qname2.equals(XSI_TYPE)) {
                                String xsiTypeValue = getAttributeValue(i);
                                String uri = super.getNamespaceURI(QNameHelper.getPrefixPart(xsiTypeValue));
                                QName xsiTypeQname = new QName(uri, QNameHelper.getLocalPart(xsiTypeValue));
                                this._xsiType = this._stl.findType(xsiTypeQname);
                            }
                            if (this._attNamesList == null) {
                                this._attNamesList = new ArrayList();
                                this._attValuesList = new ArrayList();
                            }
                            if (!isSpecialAttribute(qname2)) {
                                this._attNamesList.add(qname2);
                                this._attValuesList.add(getAttributeValue(i));
                            }
                        }
                        this._state = 2;
                        return;
                    }
                    throw new IllegalStateException("ATT event must be only at the beggining of the stream.");
                }
                return;
            default:
                throw new IllegalStateException("Unknown event type.");
        }
    }

    private void pushBufferedAttributes() {
        SchemaType validationType;
        if (this._xsiType != null) {
            if (this._contentType == null || this._contentType.isAssignableFrom(this._xsiType)) {
                validationType = this._xsiType;
            } else {
                addError("Specified type '" + this._contentType + "' not compatible with found xsi:type '" + this._xsiType + "'.");
                this._state = 3;
                return;
            }
        } else if (this._contentType != null) {
            validationType = this._contentType;
        } else if (this._attNamesList != null) {
            validationType = this._stl.findAttributeType((QName) this._attNamesList.get(0));
            if (validationType == null) {
                addError("A schema global attribute with name '" + this._attNamesList.get(0) + "' could not be found in the current schema type loader.");
                this._state = 3;
                return;
            }
        } else {
            addError("No content type provided for validation of a content model.");
            this._state = 3;
            return;
        }
        initValidator(validationType);
        this._validator.nextEvent(1, this._simpleEvent);
        validate_attributes(this._attNamesList.size());
        this._attNamesList = null;
        this._attValuesList = null;
        this._state = 1;
    }

    private boolean isSpecialAttribute(QName qn) {
        if (qn.getNamespaceURI().equals(URI_XSI)) {
            return qn.getLocalPart().equals(XSI_TYPE.getLocalPart()) || qn.getLocalPart().equals(XSI_NIL.getLocalPart()) || qn.getLocalPart().equals(XSI_SL.getLocalPart()) || qn.getLocalPart().equals(XSI_NSL.getLocalPart());
        }
        return false;
    }

    private void initValidator(SchemaType schemaType) {
        if (!$assertionsDisabled && schemaType == null) {
            throw new AssertionError();
        }
        this._validator = new Validator(schemaType, null, this._stl, this._options, this._errorListener);
    }

    private SchemaType typeForGlobalElement(QName qname) {
        if (!$assertionsDisabled && qname == null) {
            throw new AssertionError();
        }
        SchemaType docType = this._stl.findDocumentType(qname);
        if (docType == null) {
            addError("Schema document type not found for element '" + qname + "'.");
            this._state = 3;
        }
        return docType;
    }

    private void addError(String msg) {
        Location location = getLocation();
        if (location != null) {
            String source = location.getPublicId();
            if (source == null) {
                source = location.getSystemId();
            }
            this._errorListener.add(XmlError.forLocation(msg, source, location));
            return;
        }
        this._errorListener.add(XmlError.forMessage(msg));
    }

    protected void validate_attributes(int attCount) {
        for (int i = 0; i < attCount; i++) {
            validate_attribute(i);
        }
        if (this._options == null || !this._options.hasOption(OPTION_ATTTRIBUTE_VALIDATION_COMPAT_MODE)) {
            this._validator.nextEvent(5, this._simpleEvent);
        }
    }

    protected void validate_attribute(int attIndex) {
        ValidatorListener.Event event;
        if (this._attNamesList == null) {
            this._attEvent.setAttributeIndex(attIndex);
            QName qn = this._attEvent.getName();
            if (isSpecialAttribute(qn)) {
                return;
            } else {
                event = this._attEvent;
            }
        } else {
            this._simpleEvent._qname = (QName) this._attNamesList.get(attIndex);
            this._simpleEvent._text = (String) this._attValuesList.get(attIndex);
            event = this._simpleEvent;
        }
        this._validator.nextEvent(4, event);
    }

    public boolean isValid() {
        if (this._state == 3 || this._validator == null) {
            return false;
        }
        return this._validator.isValid();
    }
}
