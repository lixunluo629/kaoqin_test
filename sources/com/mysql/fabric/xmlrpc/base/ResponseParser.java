package com.mysql.fabric.xmlrpc.base;

import com.moredian.onpremise.core.common.constants.Constants;
import io.netty.handler.codec.http.HttpHeaders;
import io.swagger.models.properties.StringProperty;
import java.util.Stack;
import org.apache.xmlbeans.XmlErrorCodes;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/fabric/xmlrpc/base/ResponseParser.class */
public class ResponseParser extends DefaultHandler {
    private MethodResponse resp = null;
    Stack<Object> elNames = new Stack<>();
    Stack<Object> objects = new Stack<>();

    public MethodResponse getMethodResponse() {
        return this.resp;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName != null) {
            this.elNames.push(qName);
            if (qName.equals("methodResponse")) {
                this.objects.push(new MethodResponse());
                return;
            }
            if (qName.equals("params")) {
                this.objects.push(new Params());
                return;
            }
            if (qName.equals("param")) {
                this.objects.push(new Param());
                return;
            }
            if (qName.equals("value")) {
                this.objects.push(new Value());
                return;
            }
            if (qName.equals("array")) {
                this.objects.push(new Array());
                return;
            }
            if (qName.equals("data")) {
                this.objects.push(new Data());
                return;
            }
            if (qName.equals("struct")) {
                this.objects.push(new Struct());
            } else if (qName.equals(Constants.SYNC_MEMBER_COUNT_KEY)) {
                this.objects.push(new Member());
            } else if (qName.equals("fault")) {
                this.objects.push(new Fault());
            }
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String uri, String localName, String qName) throws SAXException {
        String thisElement = (String) this.elNames.pop();
        if (thisElement != null) {
            if (thisElement.equals("methodResponse")) {
                this.resp = (MethodResponse) this.objects.pop();
                return;
            }
            if (thisElement.equals("params")) {
                Params pms = (Params) this.objects.pop();
                MethodResponse parent = (MethodResponse) this.objects.peek();
                parent.setParams(pms);
                return;
            }
            if (thisElement.equals("param")) {
                Param p = (Param) this.objects.pop();
                Params parent2 = (Params) this.objects.peek();
                parent2.addParam(p);
                return;
            }
            if (thisElement.equals("value")) {
                Value v = (Value) this.objects.pop();
                Object parent3 = this.objects.peek();
                if (parent3 instanceof Data) {
                    ((Data) parent3).addValue(v);
                    return;
                }
                if (parent3 instanceof Param) {
                    ((Param) parent3).setValue(v);
                    return;
                } else if (parent3 instanceof Member) {
                    ((Member) parent3).setValue(v);
                    return;
                } else {
                    if (parent3 instanceof Fault) {
                        ((Fault) parent3).setValue(v);
                        return;
                    }
                    return;
                }
            }
            if (thisElement.equals("array")) {
                Array a = (Array) this.objects.pop();
                Value parent4 = (Value) this.objects.peek();
                parent4.setArray(a);
                return;
            }
            if (thisElement.equals("data")) {
                Data d = (Data) this.objects.pop();
                Array parent5 = (Array) this.objects.peek();
                parent5.setData(d);
                return;
            }
            if (thisElement.equals("struct")) {
                Struct s = (Struct) this.objects.pop();
                Value parent6 = (Value) this.objects.peek();
                parent6.setStruct(s);
            } else if (thisElement.equals(Constants.SYNC_MEMBER_COUNT_KEY)) {
                Member m = (Member) this.objects.pop();
                Struct parent7 = (Struct) this.objects.peek();
                parent7.addMember(m);
            } else if (thisElement.equals("fault")) {
                Fault f = (Fault) this.objects.pop();
                MethodResponse parent8 = (MethodResponse) this.objects.peek();
                parent8.setFault(f);
            }
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] ch2, int start, int length) throws SAXException {
        try {
            String thisElement = (String) this.elNames.peek();
            if (thisElement != null) {
                if (thisElement.equals("name")) {
                    ((Member) this.objects.peek()).setName(new String(ch2, start, length));
                } else if (thisElement.equals("value")) {
                    ((Value) this.objects.peek()).appendString(new String(ch2, start, length));
                } else if (thisElement.equals("i4") || thisElement.equals(XmlErrorCodes.INT)) {
                    ((Value) this.objects.peek()).setInt(new String(ch2, start, length));
                } else if (thisElement.equals("boolean")) {
                    ((Value) this.objects.peek()).setBoolean(new String(ch2, start, length));
                } else if (thisElement.equals(StringProperty.TYPE)) {
                    ((Value) this.objects.peek()).appendString(new String(ch2, start, length));
                } else if (thisElement.equals(XmlErrorCodes.DOUBLE)) {
                    ((Value) this.objects.peek()).setDouble(new String(ch2, start, length));
                } else if (thisElement.equals("dateTime.iso8601")) {
                    ((Value) this.objects.peek()).setDateTime(new String(ch2, start, length));
                } else if (thisElement.equals(HttpHeaders.Values.BASE64)) {
                    ((Value) this.objects.peek()).setBase64(new String(ch2, start, length).getBytes());
                }
            }
        } catch (Exception e) {
            throw new SAXParseException(e.getMessage(), null, e);
        }
    }
}
