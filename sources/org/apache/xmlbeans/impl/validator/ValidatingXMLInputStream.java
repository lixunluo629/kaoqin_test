package org.apache.xmlbeans.impl.validator;

import java.util.AbstractCollection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.XMLStreamValidationException;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.common.GenericXmlInputStream;
import org.apache.xmlbeans.impl.common.ValidatorListener;
import org.apache.xmlbeans.impl.common.XMLNameHelper;
import org.apache.xmlbeans.impl.common.XmlWhitespace;
import org.apache.xmlbeans.impl.schema.BuiltinSchemaTypeSystem;
import org.apache.xmlbeans.xml.stream.Attribute;
import org.apache.xmlbeans.xml.stream.AttributeIterator;
import org.apache.xmlbeans.xml.stream.CharacterData;
import org.apache.xmlbeans.xml.stream.StartElement;
import org.apache.xmlbeans.xml.stream.XMLEvent;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLName;
import org.apache.xmlbeans.xml.stream.XMLStreamException;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/validator/ValidatingXMLInputStream.class */
public final class ValidatingXMLInputStream extends GenericXmlInputStream implements ValidatorListener.Event {
    private XMLStreamValidationException _exception;
    private XMLInputStream _source;
    private Validator _validator;
    private StringBuffer _text = new StringBuffer();
    private boolean _finished;
    private String _xsiType;
    private String _xsiNil;
    private String _xsiLoc;
    private String _xsiNoLoc;
    private XMLName _name;
    private StartElement _startElement;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ValidatingXMLInputStream.class.desiredAssertionStatus();
    }

    public ValidatingXMLInputStream(XMLInputStream xis, SchemaTypeLoader typeLoader, SchemaType sType, XmlOptions options) throws XMLStreamException {
        SchemaType docType;
        this._source = xis;
        XmlOptions options2 = XmlOptions.maskNull(options);
        SchemaType type = (SchemaType) options2.get(XmlOptions.DOCUMENT_TYPE);
        type = type == null ? sType : type;
        if (type == null) {
            type = BuiltinSchemaTypeSystem.ST_ANY_TYPE;
            XMLInputStream xis2 = xis.getSubStream();
            if (xis2.skip(2) && (docType = typeLoader.findDocumentType(XMLNameHelper.getQName(xis2.next().getName()))) != null) {
                type = docType;
            }
            xis2.close();
        }
        this._validator = new Validator(type, null, typeLoader, options2, new ExceptionXmlErrorListener());
        nextEvent(1);
    }

    @Override // org.apache.xmlbeans.impl.common.GenericXmlInputStream
    protected XMLEvent nextEvent() throws XMLStreamException {
        XMLEvent e = this._source.next();
        if (e == null) {
            if (!this._finished) {
                flushText();
                nextEvent(2);
                this._finished = true;
            }
        } else {
            switch (e.getType()) {
                case 2:
                    StartElement se = (StartElement) e;
                    flushText();
                    this._startElement = se;
                    AttributeIterator attrs = se.getAttributes();
                    while (attrs.hasNext()) {
                        Attribute attr = attrs.next();
                        XMLName attrName = attr.getName();
                        if ("http://www.w3.org/2001/XMLSchema-instance".equals(attrName.getNamespaceUri())) {
                            String local = attrName.getLocalName();
                            if (local.equals("type")) {
                                this._xsiType = attr.getValue();
                            } else if (local.equals("nil")) {
                                this._xsiNil = attr.getValue();
                            } else if (local.equals("schemaLocation")) {
                                this._xsiLoc = attr.getValue();
                            } else if (local.equals("noNamespaceSchemaLocation")) {
                                this._xsiNoLoc = attr.getValue();
                            }
                        }
                    }
                    this._name = e.getName();
                    nextEvent(1);
                    AttributeIterator attrs2 = se.getAttributes();
                    while (attrs2.hasNext()) {
                        Attribute attr2 = attrs2.next();
                        XMLName attrName2 = attr2.getName();
                        if ("http://www.w3.org/2001/XMLSchema-instance".equals(attrName2.getNamespaceUri())) {
                            String local2 = attrName2.getLocalName();
                            if (!local2.equals("type") && !local2.equals("nil") && !local2.equals("schemaLocation") && !local2.equals("noNamespaceSchemaLocation")) {
                            }
                        }
                        this._text.append(attr2.getValue());
                        this._name = attr2.getName();
                        nextEvent(4);
                    }
                    clearText();
                    this._startElement = null;
                    break;
                case 4:
                    flushText();
                    nextEvent(2);
                    break;
                case 16:
                case 64:
                    CharacterData cd = (CharacterData) e;
                    if (cd.hasContent()) {
                        this._text.append(cd.getContent());
                        break;
                    }
                    break;
            }
        }
        return e;
    }

    private void clearText() {
        this._text.delete(0, this._text.length());
    }

    private void flushText() throws XMLStreamException {
        if (this._text.length() > 0) {
            nextEvent(3);
            clearText();
        }
    }

    @Override // org.apache.xmlbeans.impl.common.PrefixResolver
    public String getNamespaceForPrefix(String prefix) {
        Map map;
        if (this._startElement == null || (map = this._startElement.getNamespaceMap()) == null) {
            return null;
        }
        return (String) map.get(prefix);
    }

    @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
    public XmlCursor getLocationAsCursor() {
        return null;
    }

    @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
    public Location getLocation() {
        try {
            final org.apache.xmlbeans.xml.stream.Location xeLoc = this._source.peek().getLocation();
            if (xeLoc == null) {
                return null;
            }
            Location loc = new Location() { // from class: org.apache.xmlbeans.impl.validator.ValidatingXMLInputStream.1
                @Override // javax.xml.stream.Location
                public int getLineNumber() {
                    return xeLoc.getLineNumber();
                }

                @Override // javax.xml.stream.Location
                public int getColumnNumber() {
                    return xeLoc.getColumnNumber();
                }

                @Override // javax.xml.stream.Location
                public int getCharacterOffset() {
                    return -1;
                }

                @Override // javax.xml.stream.Location
                public String getPublicId() {
                    return xeLoc.getPublicId();
                }

                @Override // javax.xml.stream.Location
                public String getSystemId() {
                    return xeLoc.getSystemId();
                }
            };
            return loc;
        } catch (XMLStreamException e) {
            return null;
        }
    }

    @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
    public String getXsiType() {
        return this._xsiType;
    }

    @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
    public String getXsiNil() {
        return this._xsiNil;
    }

    @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
    public String getXsiLoc() {
        return this._xsiLoc;
    }

    @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
    public String getXsiNoLoc() {
        return this._xsiNoLoc;
    }

    @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
    public QName getName() {
        return XMLNameHelper.getQName(this._name);
    }

    @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
    public String getText() {
        return this._text.toString();
    }

    @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
    public String getText(int wsr) {
        return XmlWhitespace.collapse(this._text.toString(), wsr);
    }

    @Override // org.apache.xmlbeans.impl.common.ValidatorListener.Event
    public boolean textIsWhitespace() {
        for (int i = 0; i < this._text.length(); i++) {
            switch (this._text.charAt(i)) {
                case '\t':
                case '\n':
                case '\r':
                case ' ':
                default:
                    return false;
            }
        }
        return true;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/validator/ValidatingXMLInputStream$ExceptionXmlErrorListener.class */
    private final class ExceptionXmlErrorListener extends AbstractCollection {
        static final /* synthetic */ boolean $assertionsDisabled;

        private ExceptionXmlErrorListener() {
        }

        static {
            $assertionsDisabled = !ValidatingXMLInputStream.class.desiredAssertionStatus();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean add(Object o) {
            if (!$assertionsDisabled && ValidatingXMLInputStream.this._exception != null) {
                throw new AssertionError();
            }
            ValidatingXMLInputStream.this._exception = new XMLStreamValidationException((XmlError) o);
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator iterator() {
            return Collections.EMPTY_LIST.iterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return 0;
        }
    }

    private void nextEvent(int kind) throws XMLStreamException {
        if (!$assertionsDisabled && this._exception != null) {
            throw new AssertionError();
        }
        this._validator.nextEvent(kind, this);
        if (this._exception != null) {
            throw this._exception;
        }
    }
}
