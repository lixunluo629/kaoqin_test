package org.aspectj.weaver.loadtime.definition;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;
import org.aspectj.util.LangUtil;
import org.aspectj.weaver.loadtime.definition.Definition;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/loadtime/definition/DocumentParser.class */
public class DocumentParser extends DefaultHandler {
    private static final String DTD_PUBLIC_ID = "-//AspectJ//DTD 1.5.0//EN";
    private static final String DTD_PUBLIC_ID_ALIAS = "-//AspectJ//DTD//EN";
    private static final String ASPECTJ_ELEMENT = "aspectj";
    private static final String WEAVER_ELEMENT = "weaver";
    private static final String DUMP_ELEMENT = "dump";
    private static final String DUMP_BEFOREANDAFTER_ATTRIBUTE = "beforeandafter";
    private static final String DUMP_PERCLASSLOADERDIR_ATTRIBUTE = "perclassloaderdumpdir";
    private static final String INCLUDE_ELEMENT = "include";
    private static final String EXCLUDE_ELEMENT = "exclude";
    private static final String OPTIONS_ATTRIBUTE = "options";
    private static final String ASPECTS_ELEMENT = "aspects";
    private static final String ASPECT_ELEMENT = "aspect";
    private static final String CONCRETE_ASPECT_ELEMENT = "concrete-aspect";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String SCOPE_ATTRIBUTE = "scope";
    private static final String REQUIRES_ATTRIBUTE = "requires";
    private static final String EXTEND_ATTRIBUTE = "extends";
    private static final String PRECEDENCE_ATTRIBUTE = "precedence";
    private static final String PERCLAUSE_ATTRIBUTE = "perclause";
    private static final String POINTCUT_ELEMENT = "pointcut";
    private static final String BEFORE_ELEMENT = "before";
    private static final String AFTER_ELEMENT = "after";
    private static final String AFTER_RETURNING_ELEMENT = "after-returning";
    private static final String AFTER_THROWING_ELEMENT = "after-throwing";
    private static final String AROUND_ELEMENT = "around";
    private static final String WITHIN_ATTRIBUTE = "within";
    private static final String EXPRESSION_ATTRIBUTE = "expression";
    private static final String DECLARE_ANNOTATION_ELEMENT = "declare-annotation";
    private final Definition definition = new Definition();
    private boolean inAspectJ;
    private boolean inWeaver;
    private boolean inAspects;
    private Definition.ConcreteAspect activeConcreteAspectDefinition;
    private static Hashtable<String, Definition> parsedFiles = new Hashtable<>();
    private static boolean CACHE;
    private static final boolean LIGHTPARSER;

    static {
        boolean value = false;
        try {
            value = System.getProperty("org.aspectj.weaver.loadtime.configuration.cache", "true").equalsIgnoreCase("true");
        } catch (Throwable t) {
            t.printStackTrace();
        }
        CACHE = value;
        boolean value2 = false;
        try {
            value2 = System.getProperty("org.aspectj.weaver.loadtime.configuration.lightxmlparser", "false").equalsIgnoreCase("true");
        } catch (Throwable t2) {
            t2.printStackTrace();
        }
        LIGHTPARSER = value2;
    }

    private DocumentParser() {
    }

    public static Definition parse(URL url) throws Exception {
        Definition def;
        if (CACHE && parsedFiles.containsKey(url.toString())) {
            return parsedFiles.get(url.toString());
        }
        if (LIGHTPARSER) {
            def = SimpleAOPParser.parse(url);
        } else {
            def = saxParsing(url);
        }
        if (CACHE && def.getAspectClassNames().size() > 0) {
            parsedFiles.put(url.toString(), def);
        }
        return def;
    }

    private static Definition saxParsing(URL url) throws ParserConfigurationException, SAXException, IOException {
        DocumentParser parser = new DocumentParser();
        XMLReader xmlReader = getXMLReader();
        xmlReader.setContentHandler(parser);
        xmlReader.setErrorHandler(parser);
        try {
            xmlReader.setFeature("http://xml.org/sax/features/validation", false);
        } catch (SAXException e) {
        }
        try {
            xmlReader.setFeature("http://xml.org/sax/features/external-general-entities", false);
        } catch (SAXException e2) {
        }
        try {
            xmlReader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        } catch (SAXException e3) {
        }
        xmlReader.setEntityResolver(parser);
        InputStream in = url.openStream();
        xmlReader.parse(new InputSource(in));
        return parser.definition;
    }

    private static XMLReader getXMLReader() throws ParserConfigurationException, SAXException {
        XMLReader xmlReader;
        try {
            xmlReader = XMLReaderFactory.createXMLReader();
        } catch (SAXException e) {
            xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
        }
        return xmlReader;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.EntityResolver
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
        if (publicId.equals(DTD_PUBLIC_ID) || publicId.equals(DTD_PUBLIC_ID_ALIAS)) {
            InputStream in = DocumentParser.class.getResourceAsStream("/aspectj_1_5_0.dtd");
            if (in == null) {
                System.err.println("AspectJ - WARN - could not read DTD " + publicId);
                return null;
            }
            return new InputSource(in);
        }
        System.err.println("AspectJ - WARN - unknown DTD " + publicId + " - consider using " + DTD_PUBLIC_ID);
        return null;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (ASPECT_ELEMENT.equals(qName)) {
            String name = attributes.getValue("name");
            String scopePattern = replaceXmlAnd(attributes.getValue("scope"));
            String requiredType = attributes.getValue(REQUIRES_ATTRIBUTE);
            if (!isNull(name)) {
                this.definition.getAspectClassNames().add(name);
                if (scopePattern != null) {
                    this.definition.addScopedAspect(name, scopePattern);
                }
                if (requiredType != null) {
                    this.definition.setAspectRequires(name, requiredType);
                }
            }
        } else if (WEAVER_ELEMENT.equals(qName)) {
            String options = attributes.getValue(OPTIONS_ATTRIBUTE);
            if (!isNull(options)) {
                this.definition.appendWeaverOptions(options);
            }
            this.inWeaver = true;
        } else if (CONCRETE_ASPECT_ELEMENT.equals(qName)) {
            String name2 = attributes.getValue("name");
            String extend = attributes.getValue(EXTEND_ATTRIBUTE);
            String precedence = attributes.getValue("precedence");
            String perclause = attributes.getValue(PERCLAUSE_ATTRIBUTE);
            if (!isNull(name2)) {
                this.activeConcreteAspectDefinition = new Definition.ConcreteAspect(name2, extend, precedence, perclause);
                this.definition.getConcreteAspects().add(this.activeConcreteAspectDefinition);
            }
        } else if (POINTCUT_ELEMENT.equals(qName) && this.activeConcreteAspectDefinition != null) {
            String name3 = attributes.getValue("name");
            String expression = attributes.getValue(EXPRESSION_ATTRIBUTE);
            if (!isNull(name3) && !isNull(expression)) {
                this.activeConcreteAspectDefinition.pointcuts.add(new Definition.Pointcut(name3, replaceXmlAnd(expression)));
            }
        } else if (DECLARE_ANNOTATION_ELEMENT.equals(qName) && this.activeConcreteAspectDefinition != null) {
            String methodSig = attributes.getValue(JamXmlElements.METHOD);
            String fieldSig = attributes.getValue(JamXmlElements.FIELD);
            String typePat = attributes.getValue("type");
            String anno = attributes.getValue(JamXmlElements.ANNOTATION);
            if (isNull(anno)) {
                throw new SAXException("Badly formed <declare-annotation> element, 'annotation' value is missing");
            }
            if (isNull(methodSig) && isNull(fieldSig) && isNull(typePat)) {
                throw new SAXException("Badly formed <declare-annotation> element, need one of 'method'/'field'/'type' specified");
            }
            if (!isNull(methodSig)) {
                this.activeConcreteAspectDefinition.declareAnnotations.add(new Definition.DeclareAnnotation(Definition.DeclareAnnotationKind.Method, methodSig, anno));
            } else if (!isNull(fieldSig)) {
                this.activeConcreteAspectDefinition.declareAnnotations.add(new Definition.DeclareAnnotation(Definition.DeclareAnnotationKind.Field, fieldSig, anno));
            } else if (!isNull(typePat)) {
                this.activeConcreteAspectDefinition.declareAnnotations.add(new Definition.DeclareAnnotation(Definition.DeclareAnnotationKind.Type, typePat, anno));
            }
        } else if (BEFORE_ELEMENT.equals(qName) && this.activeConcreteAspectDefinition != null) {
            String pointcut = attributes.getValue(POINTCUT_ELEMENT);
            String adviceClass = attributes.getValue("invokeClass");
            String adviceMethod = attributes.getValue("invokeMethod");
            if (!isNull(pointcut) && !isNull(adviceClass) && !isNull(adviceMethod)) {
                this.activeConcreteAspectDefinition.pointcutsAndAdvice.add(new Definition.PointcutAndAdvice(Definition.AdviceKind.Before, replaceXmlAnd(pointcut), adviceClass, adviceMethod));
            } else {
                throw new SAXException("Badly formed <before> element");
            }
        } else if (AFTER_ELEMENT.equals(qName) && this.activeConcreteAspectDefinition != null) {
            String pointcut2 = attributes.getValue(POINTCUT_ELEMENT);
            String adviceClass2 = attributes.getValue("invokeClass");
            String adviceMethod2 = attributes.getValue("invokeMethod");
            if (!isNull(pointcut2) && !isNull(adviceClass2) && !isNull(adviceMethod2)) {
                this.activeConcreteAspectDefinition.pointcutsAndAdvice.add(new Definition.PointcutAndAdvice(Definition.AdviceKind.After, replaceXmlAnd(pointcut2), adviceClass2, adviceMethod2));
            } else {
                throw new SAXException("Badly formed <after> element");
            }
        } else if (AROUND_ELEMENT.equals(qName) && this.activeConcreteAspectDefinition != null) {
            String pointcut3 = attributes.getValue(POINTCUT_ELEMENT);
            String adviceClass3 = attributes.getValue("invokeClass");
            String adviceMethod3 = attributes.getValue("invokeMethod");
            if (!isNull(pointcut3) && !isNull(adviceClass3) && !isNull(adviceMethod3)) {
                this.activeConcreteAspectDefinition.pointcutsAndAdvice.add(new Definition.PointcutAndAdvice(Definition.AdviceKind.Around, replaceXmlAnd(pointcut3), adviceClass3, adviceMethod3));
            } else {
                throw new SAXException("Badly formed <before> element");
            }
        } else if (ASPECTJ_ELEMENT.equals(qName)) {
            if (this.inAspectJ) {
                throw new SAXException("Found nested <aspectj> element");
            }
            this.inAspectJ = true;
        } else if (ASPECTS_ELEMENT.equals(qName)) {
            this.inAspects = true;
        } else if (INCLUDE_ELEMENT.equals(qName) && this.inWeaver) {
            String typePattern = getWithinAttribute(attributes);
            if (!isNull(typePattern)) {
                this.definition.getIncludePatterns().add(typePattern);
            }
        } else if (EXCLUDE_ELEMENT.equals(qName) && this.inWeaver) {
            String typePattern2 = getWithinAttribute(attributes);
            if (!isNull(typePattern2)) {
                this.definition.getExcludePatterns().add(typePattern2);
            }
        } else if ("dump".equals(qName) && this.inWeaver) {
            String typePattern3 = getWithinAttribute(attributes);
            if (!isNull(typePattern3)) {
                this.definition.getDumpPatterns().add(typePattern3);
            }
            String beforeAndAfter = attributes.getValue(DUMP_BEFOREANDAFTER_ATTRIBUTE);
            if (isTrue(beforeAndAfter)) {
                this.definition.setDumpBefore(true);
            }
            String perWeaverDumpDir = attributes.getValue(DUMP_PERCLASSLOADERDIR_ATTRIBUTE);
            if (isTrue(perWeaverDumpDir)) {
                this.definition.setCreateDumpDirPerClassloader(true);
            }
        } else if (EXCLUDE_ELEMENT.equals(qName) && this.inAspects) {
            String typePattern4 = getWithinAttribute(attributes);
            if (!isNull(typePattern4)) {
                this.definition.getAspectExcludePatterns().add(typePattern4);
            }
        } else if (INCLUDE_ELEMENT.equals(qName) && this.inAspects) {
            String typePattern5 = getWithinAttribute(attributes);
            if (!isNull(typePattern5)) {
                this.definition.getAspectIncludePatterns().add(typePattern5);
            }
        } else {
            throw new SAXException("Unknown element while parsing <aspectj> element: " + qName);
        }
        super.startElement(uri, localName, qName, attributes);
    }

    private String getWithinAttribute(Attributes attributes) {
        return replaceXmlAnd(attributes.getValue(WITHIN_ATTRIBUTE));
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (CONCRETE_ASPECT_ELEMENT.equals(qName)) {
            this.activeConcreteAspectDefinition = null;
        } else if (ASPECTJ_ELEMENT.equals(qName)) {
            this.inAspectJ = false;
        } else if (WEAVER_ELEMENT.equals(qName)) {
            this.inWeaver = false;
        } else if (ASPECTS_ELEMENT.equals(qName)) {
            this.inAspects = false;
        }
        super.endElement(uri, localName, qName);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ErrorHandler
    public void warning(SAXParseException e) throws SAXException {
        super.warning(e);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ErrorHandler
    public void error(SAXParseException e) throws SAXException {
        super.error(e);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ErrorHandler
    public void fatalError(SAXParseException e) throws SAXException {
        super.fatalError(e);
    }

    private static String replaceXmlAnd(String expression) {
        return LangUtil.replace(expression, " AND ", " && ");
    }

    private boolean isNull(String s) {
        return s == null || s.length() <= 0;
    }

    private boolean isTrue(String s) {
        return s != null && s.equals("true");
    }

    public static void deactivateCaching() {
        CACHE = false;
    }
}
