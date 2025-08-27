package org.apache.xmlbeans.impl.jam.xml;

import java.io.Writer;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.xmlbeans.impl.jam.JAnnotatedElement;
import org.apache.xmlbeans.impl.jam.JAnnotation;
import org.apache.xmlbeans.impl.jam.JAnnotationValue;
import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.JComment;
import org.apache.xmlbeans.impl.jam.JConstructor;
import org.apache.xmlbeans.impl.jam.JField;
import org.apache.xmlbeans.impl.jam.JInvokable;
import org.apache.xmlbeans.impl.jam.JMethod;
import org.apache.xmlbeans.impl.jam.JParameter;
import org.apache.xmlbeans.impl.jam.JSourcePosition;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/xml/JamXmlWriter.class */
class JamXmlWriter implements JamXmlElements {
    private XMLStreamWriter mOut;
    private boolean mInBody = false;
    private boolean mWriteSourceURI = false;

    public JamXmlWriter(Writer out) throws XMLStreamException {
        if (out == null) {
            throw new IllegalArgumentException("null out");
        }
        this.mOut = XMLOutputFactory.newInstance().createXMLStreamWriter(out);
    }

    public JamXmlWriter(XMLStreamWriter out) {
        if (out == null) {
            throw new IllegalArgumentException("null out");
        }
        this.mOut = out;
    }

    public void begin() throws XMLStreamException {
        if (this.mInBody) {
            throw new XMLStreamException("begin() already called");
        }
        this.mOut.writeStartElement(JamXmlElements.JAMSERVICE);
        this.mInBody = true;
    }

    public void end() throws XMLStreamException {
        if (!this.mInBody) {
            throw new XMLStreamException("begin() never called");
        }
        this.mOut.writeEndElement();
        this.mInBody = false;
    }

    public void write(JClass clazz) throws XMLStreamException {
        assertStarted();
        this.mOut.writeStartElement("class");
        writeValueElement("name", clazz.getFieldDescriptor());
        writeValueElement(JamXmlElements.ISINTERFACE, clazz.isInterface());
        writeModifiers(clazz.getModifiers());
        JClass sc = clazz.getSuperclass();
        if (sc != null) {
            writeValueElement(JamXmlElements.SUPERCLASS, sc.getFieldDescriptor());
        }
        writeClassList(JamXmlElements.INTERFACE, clazz.getInterfaces());
        JField[] f = clazz.getDeclaredFields();
        for (JField jField : f) {
            write(jField);
        }
        JConstructor[] c = clazz.getConstructors();
        for (JConstructor jConstructor : c) {
            write(jConstructor);
        }
        JMethod[] m = clazz.getDeclaredMethods();
        for (JMethod jMethod : m) {
            write(jMethod);
        }
        writeAnnotatedElement(clazz);
        this.mOut.writeEndElement();
    }

    private void write(JMethod method) throws XMLStreamException {
        this.mOut.writeStartElement(JamXmlElements.METHOD);
        writeValueElement("name", method.getSimpleName());
        writeValueElement(JamXmlElements.RETURNTYPE, method.getReturnType().getFieldDescriptor());
        writeInvokable(method);
        this.mOut.writeEndElement();
    }

    private void write(JConstructor ctor) throws XMLStreamException {
        this.mOut.writeStartElement("constructor");
        writeInvokable(ctor);
        this.mOut.writeEndElement();
    }

    private void write(JField field) throws XMLStreamException {
        this.mOut.writeStartElement(JamXmlElements.FIELD);
        writeValueElement("name", field.getSimpleName());
        writeModifiers(field.getModifiers());
        writeValueElement("type", field.getType().getFieldDescriptor());
        writeAnnotatedElement(field);
        this.mOut.writeEndElement();
    }

    private void writeInvokable(JInvokable ji) throws XMLStreamException {
        writeModifiers(ji.getModifiers());
        JParameter[] params = ji.getParameters();
        for (int i = 0; i < params.length; i++) {
            this.mOut.writeStartElement(JamXmlElements.PARAMETER);
            writeValueElement("name", params[i].getSimpleName());
            writeValueElement("type", params[i].getType().getFieldDescriptor());
            writeAnnotatedElement(params[i]);
            this.mOut.writeEndElement();
        }
        writeAnnotatedElement(ji);
    }

    private void writeClassList(String elementName, JClass[] clazzes) throws XMLStreamException {
        for (JClass jClass : clazzes) {
            this.mOut.writeStartElement(elementName);
            this.mOut.writeCharacters(jClass.getFieldDescriptor());
            this.mOut.writeEndElement();
        }
    }

    private void writeModifiers(int mods) throws XMLStreamException {
        this.mOut.writeStartElement(JamXmlElements.MODIFIERS);
        this.mOut.writeCharacters(String.valueOf(mods));
        this.mOut.writeEndElement();
    }

    private void writeValueElement(String elementName, boolean b) throws XMLStreamException {
        this.mOut.writeStartElement(elementName);
        this.mOut.writeCharacters(String.valueOf(b));
        this.mOut.writeEndElement();
    }

    private void writeValueElement(String elementName, int x) throws XMLStreamException {
        this.mOut.writeStartElement(elementName);
        this.mOut.writeCharacters(String.valueOf(x));
        this.mOut.writeEndElement();
    }

    private void writeValueElement(String elementName, String val) throws XMLStreamException {
        this.mOut.writeStartElement(elementName);
        this.mOut.writeCharacters(val);
        this.mOut.writeEndElement();
    }

    private void writeValueElement(String elementName, String[] vals) throws XMLStreamException {
        for (String str : vals) {
            writeValueElement(elementName, str);
        }
    }

    private void writeAnnotatedElement(JAnnotatedElement ae) throws XMLStreamException {
        String text;
        JAnnotation[] anns = ae.getAnnotations();
        for (JAnnotation jAnnotation : anns) {
            writeAnnotation(jAnnotation);
        }
        JComment jc = ae.getComment();
        if (jc != null && (text = jc.getText()) != null && text.trim().length() > 0) {
            this.mOut.writeStartElement("comment");
            this.mOut.writeCData(jc.getText());
            this.mOut.writeEndElement();
        }
        JSourcePosition pos = ae.getSourcePosition();
        if (pos != null) {
            this.mOut.writeStartElement(JamXmlElements.SOURCEPOSITION);
            if (pos.getLine() != -1) {
                writeValueElement(JamXmlElements.LINE, pos.getLine());
            }
            if (pos.getColumn() != -1) {
                writeValueElement(JamXmlElements.COLUMN, pos.getColumn());
            }
            if (this.mWriteSourceURI && pos.getSourceURI() != null) {
                writeValueElement(JamXmlElements.SOURCEURI, pos.getSourceURI().toString());
            }
            this.mOut.writeEndElement();
        }
    }

    private void writeAnnotation(JAnnotation ann) throws XMLStreamException {
        this.mOut.writeStartElement(JamXmlElements.ANNOTATION);
        writeValueElement("name", ann.getQualifiedName());
        JAnnotationValue[] values = ann.getValues();
        for (JAnnotationValue jAnnotationValue : values) {
            writeAnnotationValue(jAnnotationValue);
        }
        this.mOut.writeEndElement();
    }

    private void writeAnnotationValue(JAnnotationValue val) throws XMLStreamException {
        this.mOut.writeStartElement(JamXmlElements.ANNOTATIONVALUE);
        writeValueElement("name", val.getName());
        writeValueElement("type", val.getType().getFieldDescriptor());
        if (val.getType().isArrayType()) {
            writeValueElement("value", val.asStringArray());
        } else {
            writeValueElement("value", val.asString());
        }
        this.mOut.writeEndElement();
    }

    private void assertStarted() throws XMLStreamException {
        if (!this.mInBody) {
            throw new XMLStreamException("begin() not called");
        }
    }
}
