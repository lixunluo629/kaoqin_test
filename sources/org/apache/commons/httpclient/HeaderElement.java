package org.apache.commons.httpclient;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.httpclient.util.ParameterParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/HeaderElement.class */
public class HeaderElement extends NameValuePair {
    private static final Log LOG;
    private NameValuePair[] parameters;
    static Class class$org$apache$commons$httpclient$HeaderElement;

    public HeaderElement() {
        this((String) null, (String) null, (NameValuePair[]) null);
    }

    public HeaderElement(String name, String value) {
        this(name, value, (NameValuePair[]) null);
    }

    public HeaderElement(String name, String value, NameValuePair[] parameters) {
        super(name, value);
        this.parameters = null;
        this.parameters = parameters;
    }

    public HeaderElement(char[] chars, int offset, int length) {
        this();
        if (chars == null) {
            return;
        }
        ParameterParser parser = new ParameterParser();
        List params = parser.parse(chars, offset, length, ';');
        if (params.size() > 0) {
            NameValuePair element = (NameValuePair) params.remove(0);
            setName(element.getName());
            setValue(element.getValue());
            if (params.size() > 0) {
                this.parameters = (NameValuePair[]) params.toArray(new NameValuePair[params.size()]);
            }
        }
    }

    public HeaderElement(char[] chars) {
        this(chars, 0, chars.length);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$HeaderElement == null) {
            clsClass$ = class$("org.apache.commons.httpclient.HeaderElement");
            class$org$apache$commons$httpclient$HeaderElement = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$HeaderElement;
        }
        LOG = LogFactory.getLog(clsClass$);
    }

    public NameValuePair[] getParameters() {
        return this.parameters;
    }

    public static final HeaderElement[] parseElements(char[] headerValue) {
        LOG.trace("enter HeaderElement.parseElements(char[])");
        if (headerValue == null) {
            return new HeaderElement[0];
        }
        List elements = new ArrayList();
        int from = 0;
        int len = headerValue.length;
        boolean qouted = false;
        for (int i = 0; i < len; i++) {
            char ch2 = headerValue[i];
            if (ch2 == '\"') {
                qouted = !qouted;
            }
            HeaderElement element = null;
            if (!qouted && ch2 == ',') {
                element = new HeaderElement(headerValue, from, i);
                from = i + 1;
            } else if (i == len - 1) {
                element = new HeaderElement(headerValue, from, len);
            }
            if (element != null && element.getName() != null) {
                elements.add(element);
            }
        }
        return (HeaderElement[]) elements.toArray(new HeaderElement[elements.size()]);
    }

    public static final HeaderElement[] parseElements(String headerValue) {
        LOG.trace("enter HeaderElement.parseElements(String)");
        if (headerValue == null) {
            return new HeaderElement[0];
        }
        return parseElements(headerValue.toCharArray());
    }

    public static final HeaderElement[] parse(String headerValue) throws HttpException {
        LOG.trace("enter HeaderElement.parse(String)");
        if (headerValue == null) {
            return new HeaderElement[0];
        }
        return parseElements(headerValue.toCharArray());
    }

    public NameValuePair getParameterByName(String name) {
        LOG.trace("enter HeaderElement.getParameterByName(String)");
        if (name == null) {
            throw new IllegalArgumentException("Name may not be null");
        }
        NameValuePair found = null;
        NameValuePair[] parameters = getParameters();
        if (parameters != null) {
            int i = 0;
            while (true) {
                if (i >= parameters.length) {
                    break;
                }
                NameValuePair current = parameters[i];
                if (!current.getName().equalsIgnoreCase(name)) {
                    i++;
                } else {
                    found = current;
                    break;
                }
            }
        }
        return found;
    }
}
