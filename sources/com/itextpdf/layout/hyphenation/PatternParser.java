package com.itextpdf.layout.hyphenation;

import com.itextpdf.io.util.ResourceUtil;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.apache.xmlbeans.impl.common.XMLBeansConstants;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/hyphenation/PatternParser.class */
public class PatternParser extends DefaultHandler {
    private XMLReader parser;
    private int currElement;
    private IPatternConsumer consumer;
    private StringBuilder token;
    private ArrayList exception;
    private char hyphenChar;
    private String errMsg;
    private boolean hasClasses;
    static final int ELEM_CLASSES = 1;
    static final int ELEM_EXCEPTIONS = 2;
    static final int ELEM_PATTERNS = 3;
    static final int ELEM_HYPHEN = 4;

    private PatternParser() {
        this.token = new StringBuilder();
        this.parser = createParser();
        this.parser.setContentHandler(this);
        this.parser.setErrorHandler(this);
        this.hyphenChar = '-';
    }

    public PatternParser(IPatternConsumer consumer) throws HyphenationException {
        this();
        this.consumer = consumer;
    }

    public void parse(String filename) throws SAXException, HyphenationException, IOException {
        parse(new FileInputStream(filename), filename);
    }

    public void parse(InputStream stream, String name) throws SAXException, HyphenationException, IOException {
        InputSource source = new InputSource(stream);
        source.setSystemId(name);
        try {
            this.parser.parse(source);
        } catch (FileNotFoundException fnfe) {
            throw new HyphenationException("File not found: " + fnfe.getMessage());
        } catch (IOException ioe) {
            throw new HyphenationException(ioe.getMessage());
        } catch (SAXException e) {
            throw new HyphenationException(this.errMsg);
        }
    }

    static XMLReader createParser() throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(false);
            factory.setFeature(XMLBeansConstants.FEATURE_LOAD_EXTERNAL_DTD, false);
            return factory.newSAXParser().getXMLReader();
        } catch (Exception e) {
            throw new RuntimeException("Couldn't create XMLReader: " + e.getMessage());
        }
    }

    private String readToken(StringBuilder chars) {
        boolean space = false;
        int i = 0;
        while (i < chars.length() && Character.isWhitespace(chars.charAt(i))) {
            space = true;
            i++;
        }
        if (space) {
            for (int countr = i; countr < chars.length(); countr++) {
                chars.setCharAt(countr - i, chars.charAt(countr));
            }
            chars.setLength(chars.length() - i);
            if (this.token.length() > 0) {
                String word = this.token.toString();
                this.token.setLength(0);
                return word;
            }
        }
        boolean space2 = false;
        int i2 = 0;
        while (true) {
            if (i2 >= chars.length()) {
                break;
            }
            if (!Character.isWhitespace(chars.charAt(i2))) {
                i2++;
            } else {
                space2 = true;
                break;
            }
        }
        this.token.append(chars.toString().substring(0, i2));
        for (int countr2 = i2; countr2 < chars.length(); countr2++) {
            chars.setCharAt(countr2 - i2, chars.charAt(countr2));
        }
        chars.setLength(chars.length() - i2);
        if (space2) {
            String word2 = this.token.toString();
            this.token.setLength(0);
            return word2;
        }
        this.token.append((CharSequence) chars);
        return null;
    }

    private static String getPattern(String word) {
        StringBuilder pat = new StringBuilder();
        int len = word.length();
        for (int i = 0; i < len; i++) {
            if (!Character.isDigit(word.charAt(i))) {
                pat.append(word.charAt(i));
            }
        }
        return pat.toString();
    }

    private ArrayList normalizeException(ArrayList ex) {
        ArrayList res = new ArrayList();
        for (int i = 0; i < ex.size(); i++) {
            Object item = ex.get(i);
            if (item instanceof String) {
                String str = (String) item;
                StringBuilder buf = new StringBuilder();
                for (int j = 0; j < str.length(); j++) {
                    char c = str.charAt(j);
                    if (c != this.hyphenChar) {
                        buf.append(c);
                    } else {
                        res.add(buf.toString());
                        buf.setLength(0);
                        char[] h = {this.hyphenChar};
                        res.add(new Hyphen(new String(h), null, null));
                    }
                }
                if (buf.length() > 0) {
                    res.add(buf.toString());
                }
            } else {
                res.add(item);
            }
        }
        return res;
    }

    private String getExceptionWord(ArrayList ex) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < ex.size(); i++) {
            Object item = ex.get(i);
            if (item instanceof String) {
                res.append((String) item);
            } else if (((Hyphen) item).noBreak != null) {
                res.append(((Hyphen) item).noBreak);
            }
        }
        return res.toString();
    }

    private static String getInterletterValues(String pat) {
        StringBuilder il = new StringBuilder();
        String word = pat + "a";
        int len = word.length();
        int i = 0;
        while (i < len) {
            char c = word.charAt(i);
            if (Character.isDigit(c)) {
                il.append(c);
                i++;
            } else {
                il.append('0');
            }
            i++;
        }
        return il.toString();
    }

    protected void getExternalClasses() throws SAXException {
        XMLReader mainParser = this.parser;
        this.parser = createParser();
        this.parser.setContentHandler(this);
        this.parser.setErrorHandler(this);
        InputStream stream = ResourceUtil.getResourceStream("com/itextpdf/hyph/external/classes.xml");
        InputSource source = new InputSource(stream);
        try {
            try {
                this.parser.parse(source);
                this.parser = mainParser;
            } catch (IOException ioe) {
                throw new SAXException(ioe.getMessage());
            }
        } catch (Throwable th) {
            this.parser = mainParser;
            throw th;
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String uri, String local, String raw, Attributes attrs) throws SAXException {
        if (local.equals("hyphen-char")) {
            String h = attrs.getValue("value");
            if (h != null && h.length() == 1) {
                this.hyphenChar = h.charAt(0);
            }
        } else if (local.equals("classes")) {
            this.currElement = 1;
        } else if (local.equals("patterns")) {
            if (!this.hasClasses) {
                getExternalClasses();
            }
            this.currElement = 3;
        } else if (local.equals("exceptions")) {
            if (!this.hasClasses) {
                getExternalClasses();
            }
            this.currElement = 2;
            this.exception = new ArrayList();
        } else if (local.equals("hyphen")) {
            if (this.token.length() > 0) {
                this.exception.add(this.token.toString());
            }
            this.exception.add(new Hyphen(attrs.getValue("pre"), attrs.getValue("no"), attrs.getValue("post")));
            this.currElement = 4;
        }
        this.token.setLength(0);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String uri, String local, String raw) {
        if (this.token.length() > 0) {
            String word = this.token.toString();
            switch (this.currElement) {
                case 1:
                    this.consumer.addClass(word);
                    break;
                case 2:
                    this.exception.add(word);
                    this.exception = normalizeException(this.exception);
                    this.consumer.addException(getExceptionWord(this.exception), (ArrayList) this.exception.clone());
                    break;
                case 3:
                    this.consumer.addPattern(getPattern(word), getInterletterValues(word));
                    break;
            }
            if (this.currElement != 4) {
                this.token.setLength(0);
            }
        }
        if (this.currElement == 1) {
            this.hasClasses = true;
        }
        if (this.currElement == 4) {
            this.currElement = 2;
        } else {
            this.currElement = 0;
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] ch2, int start, int length) {
        StringBuilder chars = new StringBuilder(length);
        chars.append(ch2, start, length);
        String token = readToken(chars);
        while (true) {
            String word = token;
            if (word != null) {
                switch (this.currElement) {
                    case 1:
                        this.consumer.addClass(word);
                        break;
                    case 2:
                        this.exception.add(word);
                        this.exception = normalizeException(this.exception);
                        this.consumer.addException(getExceptionWord(this.exception), (ArrayList) this.exception.clone());
                        this.exception.clear();
                        break;
                    case 3:
                        this.consumer.addPattern(getPattern(word), getInterletterValues(word));
                        break;
                }
                token = readToken(chars);
            } else {
                return;
            }
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ErrorHandler
    public void warning(SAXParseException ex) {
        this.errMsg = "[Warning] " + getLocationString(ex) + ": " + ex.getMessage();
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ErrorHandler
    public void error(SAXParseException ex) {
        this.errMsg = "[Error] " + getLocationString(ex) + ": " + ex.getMessage();
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ErrorHandler
    public void fatalError(SAXParseException ex) throws SAXException {
        this.errMsg = "[Fatal Error] " + getLocationString(ex) + ": " + ex.getMessage();
        throw ex;
    }

    private String getLocationString(SAXParseException ex) {
        StringBuilder str = new StringBuilder();
        String systemId = ex.getSystemId();
        if (systemId != null) {
            int index = systemId.lastIndexOf(47);
            if (index != -1) {
                systemId = systemId.substring(index + 1);
            }
            str.append(systemId);
        }
        str.append(':');
        str.append(ex.getLineNumber());
        str.append(':');
        str.append(ex.getColumnNumber());
        return str.toString();
    }
}
