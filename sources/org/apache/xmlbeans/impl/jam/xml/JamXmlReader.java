package org.apache.xmlbeans.impl.jam.xml;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.internal.CachedClassBuilder;
import org.apache.xmlbeans.impl.jam.internal.elements.ClassImpl;
import org.apache.xmlbeans.impl.jam.internal.elements.ElementContext;
import org.apache.xmlbeans.impl.jam.mutable.MAnnotatedElement;
import org.apache.xmlbeans.impl.jam.mutable.MAnnotation;
import org.apache.xmlbeans.impl.jam.mutable.MClass;
import org.apache.xmlbeans.impl.jam.mutable.MConstructor;
import org.apache.xmlbeans.impl.jam.mutable.MField;
import org.apache.xmlbeans.impl.jam.mutable.MInvokable;
import org.apache.xmlbeans.impl.jam.mutable.MMethod;
import org.apache.xmlbeans.impl.jam.mutable.MParameter;
import org.apache.xmlbeans.impl.jam.mutable.MSourcePosition;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/xml/JamXmlReader.class */
class JamXmlReader implements JamXmlElements {
    private XMLStreamReader mIn;
    private CachedClassBuilder mCache;
    private ElementContext mContext;

    public JamXmlReader(CachedClassBuilder cache, InputStream in, ElementContext ctx) throws XMLStreamException {
        this(cache, XMLInputFactory.newInstance().createXMLStreamReader(in), ctx);
    }

    public JamXmlReader(CachedClassBuilder cache, Reader in, ElementContext ctx) throws XMLStreamException {
        this(cache, XMLInputFactory.newInstance().createXMLStreamReader(in), ctx);
    }

    public JamXmlReader(CachedClassBuilder cache, XMLStreamReader in, ElementContext ctx) {
        if (cache == null) {
            throw new IllegalArgumentException("null cache");
        }
        if (in == null) {
            throw new IllegalArgumentException("null cache");
        }
        if (ctx == null) {
            throw new IllegalArgumentException("null ctx");
        }
        this.mIn = in;
        this.mCache = cache;
        this.mContext = ctx;
    }

    public void read() throws XMLStreamException {
        nextElement();
        assertStart(JamXmlElements.JAMSERVICE);
        nextElement();
        while ("class".equals(getElementName())) {
            readClass();
        }
        assertEnd(JamXmlElements.JAMSERVICE);
    }

    private void readClass() throws XMLStreamException {
        assertStart("class");
        nextElement();
        String clazzName = assertCurrentString("name");
        int dot = clazzName.lastIndexOf(46);
        String pkgName = "";
        if (dot != -1) {
            pkgName = clazzName.substring(0, dot);
            clazzName = clazzName.substring(dot + 1);
        }
        MClass clazz = this.mCache.createClassToBuild(pkgName, clazzName, null);
        clazz.setIsInterface(assertCurrentBoolean(JamXmlElements.ISINTERFACE));
        clazz.setModifiers(assertCurrentInt(JamXmlElements.MODIFIERS));
        String supername = checkCurrentString(JamXmlElements.SUPERCLASS);
        if (supername != null) {
            clazz.setSuperclass(supername);
        }
        while (true) {
            String supername2 = checkCurrentString(JamXmlElements.INTERFACE);
            if (supername2 == null) {
                break;
            } else {
                clazz.addInterface(supername2);
            }
        }
        while (JamXmlElements.FIELD.equals(getElementName())) {
            readField(clazz);
        }
        while ("constructor".equals(getElementName())) {
            readConstructor(clazz);
        }
        while (JamXmlElements.METHOD.equals(getElementName())) {
            readMethod(clazz);
        }
        readAnnotatedElement(clazz);
        assertEnd("class");
        ((ClassImpl) clazz).setState(6);
        nextElement();
    }

    private void readField(MClass clazz) throws XMLStreamException {
        assertStart(JamXmlElements.FIELD);
        MField field = clazz.addNewField();
        nextElement();
        field.setSimpleName(assertCurrentString("name"));
        field.setModifiers(assertCurrentInt(JamXmlElements.MODIFIERS));
        field.setType(assertCurrentString("type"));
        readAnnotatedElement(field);
        assertEnd(JamXmlElements.FIELD);
        nextElement();
    }

    private void readConstructor(MClass clazz) throws XMLStreamException {
        assertStart("constructor");
        MConstructor ctor = clazz.addNewConstructor();
        nextElement();
        readInvokableContents(ctor);
        assertEnd("constructor");
        nextElement();
    }

    private void readMethod(MClass clazz) throws XMLStreamException {
        assertStart(JamXmlElements.METHOD);
        MMethod method = clazz.addNewMethod();
        nextElement();
        method.setSimpleName(assertCurrentString("name"));
        method.setReturnType(assertCurrentString(JamXmlElements.RETURNTYPE));
        readInvokableContents(method);
        assertEnd(JamXmlElements.METHOD);
        nextElement();
    }

    private void readSourcePosition(MAnnotatedElement element) throws XMLStreamException {
        assertStart(JamXmlElements.SOURCEPOSITION);
        MSourcePosition pos = element.createSourcePosition();
        nextElement();
        if (JamXmlElements.LINE.equals(getElementName())) {
            pos.setLine(assertCurrentInt(JamXmlElements.LINE));
        }
        if (JamXmlElements.COLUMN.equals(getElementName())) {
            pos.setColumn(assertCurrentInt(JamXmlElements.COLUMN));
        }
        if (JamXmlElements.SOURCEURI.equals(getElementName())) {
            try {
                pos.setSourceURI(new URI(assertCurrentString(JamXmlElements.SOURCEURI)));
            } catch (URISyntaxException use) {
                throw new XMLStreamException(use);
            }
        }
        assertEnd(JamXmlElements.SOURCEPOSITION);
        nextElement();
    }

    private void readInvokableContents(MInvokable out) throws XMLStreamException {
        out.setModifiers(assertCurrentInt(JamXmlElements.MODIFIERS));
        while (JamXmlElements.PARAMETER.equals(getElementName())) {
            nextElement();
            MParameter param = out.addNewParameter();
            param.setSimpleName(assertCurrentString("name"));
            param.setType(assertCurrentString("type"));
            readAnnotatedElement(param);
            assertEnd(JamXmlElements.PARAMETER);
            nextElement();
        }
        readAnnotatedElement(out);
    }

    private void readAnnotatedElement(MAnnotatedElement element) throws XMLStreamException {
        while (JamXmlElements.ANNOTATION.equals(getElementName())) {
            nextElement();
            MAnnotation ann = element.addLiteralAnnotation(assertCurrentString("name"));
            while (JamXmlElements.ANNOTATIONVALUE.equals(getElementName())) {
                nextElement();
                String name = assertCurrentString("name");
                String type = assertCurrentString("type");
                JClass jclass = this.mContext.getClassLoader().loadClass(type);
                if (jclass.isArrayType()) {
                    Collection list = new ArrayList();
                    while ("value".equals(getElementName())) {
                        String value = assertCurrentString("value");
                        list.add(value);
                    }
                    String[] vals = new String[list.size()];
                    list.toArray(vals);
                    ann.setSimpleValue(name, vals, jclass);
                } else {
                    String value2 = assertCurrentString("value");
                    ann.setSimpleValue(name, value2, jclass);
                }
                assertEnd(JamXmlElements.ANNOTATIONVALUE);
                nextElement();
            }
            assertEnd(JamXmlElements.ANNOTATION);
            nextElement();
        }
        if ("comment".equals(getElementName())) {
            element.createComment().setText(this.mIn.getElementText());
            assertEnd("comment");
            nextElement();
        }
        if (JamXmlElements.SOURCEPOSITION.equals(getElementName())) {
            readSourcePosition(element);
        }
    }

    private void assertStart(String named) throws XMLStreamException {
        if (!this.mIn.isStartElement() || !named.equals(getElementName())) {
            error("expected to get a <" + named + ">, ");
        }
    }

    private void assertEnd(String named) throws XMLStreamException {
        if (!this.mIn.isEndElement() || !named.equals(getElementName())) {
            error("expected to get a </" + named + ">, ");
        }
    }

    private String checkCurrentString(String named) throws XMLStreamException {
        if (named.equals(getElementName())) {
            String val = this.mIn.getElementText();
            assertEnd(named);
            nextElement();
            return val;
        }
        return null;
    }

    private String assertCurrentString(String named) throws XMLStreamException {
        assertStart(named);
        String val = this.mIn.getElementText();
        assertEnd(named);
        nextElement();
        return val;
    }

    private int assertCurrentInt(String named) throws XMLStreamException {
        assertStart(named);
        String val = this.mIn.getElementText();
        assertEnd(named);
        nextElement();
        return Integer.valueOf(val).intValue();
    }

    private boolean assertCurrentBoolean(String named) throws XMLStreamException {
        assertStart(named);
        String val = this.mIn.getElementText();
        assertEnd(named);
        nextElement();
        return Boolean.valueOf(val).booleanValue();
    }

    private void error(String message) throws XMLStreamException {
        StringWriter out = new StringWriter();
        out.write("<");
        out.write(this.mIn.getLocalName());
        out.write("> line:");
        out.write("" + this.mIn.getLocation().getLineNumber());
        out.write(" col:");
        out.write("" + this.mIn.getLocation().getColumnNumber());
        out.write("]");
        throw new XMLStreamException(message + ":\n " + out.toString());
    }

    private void nextElement() throws XMLStreamException {
        while (this.mIn.next() != -1) {
            if (this.mIn.isEndElement() || this.mIn.isStartElement()) {
                return;
            }
        }
        throw new XMLStreamException("Unexpected end of file");
    }

    private String getElementName() {
        return this.mIn.getLocalName();
    }
}
