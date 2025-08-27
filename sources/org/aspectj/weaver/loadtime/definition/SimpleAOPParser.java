package org.aspectj.weaver.loadtime.definition;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import org.aspectj.util.LangUtil;
import org.aspectj.weaver.loadtime.definition.Definition;
import org.xml.sax.SAXException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/loadtime/definition/SimpleAOPParser.class */
public class SimpleAOPParser {
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
    private static final String WITHIN_ATTRIBUTE = "within";
    private static final String EXPRESSION_ATTRIBUTE = "expression";
    private static final String DECLARE_ANNOTATION = "declare-annotation";
    private static final String ANNONATION_TAG = "annotation";
    private static final String ANNO_KIND_TYPE = "type";
    private static final String ANNO_KIND_METHOD = "method";
    private static final String ANNO_KIND_FIELD = "field";
    private static final String BEFORE_ELEMENT = "before";
    private static final String AFTER_ELEMENT = "after";
    private static final String AROUND_ELEMENT = "around";
    private final Definition m_definition = new Definition();
    private boolean m_inAspectJ;
    private boolean m_inWeaver;
    private boolean m_inAspects;
    private Definition.ConcreteAspect m_lastConcreteAspect;

    private SimpleAOPParser() {
    }

    public static Definition parse(URL url) throws Exception {
        InputStream in = url.openStream();
        LightXMLParser xml = new LightXMLParser();
        xml.parseFromReader(new InputStreamReader(in));
        SimpleAOPParser sap = new SimpleAOPParser();
        traverse(sap, xml);
        return sap.m_definition;
    }

    private void startElement(String qName, Map attrMap) throws Exception {
        if (ASPECT_ELEMENT.equals(qName)) {
            String name = (String) attrMap.get("name");
            String scopePattern = replaceXmlAnd((String) attrMap.get("scope"));
            String requiredType = (String) attrMap.get(REQUIRES_ATTRIBUTE);
            if (!isNull(name)) {
                this.m_definition.getAspectClassNames().add(name);
                if (scopePattern != null) {
                    this.m_definition.addScopedAspect(name, scopePattern);
                }
                if (requiredType != null) {
                    this.m_definition.setAspectRequires(name, requiredType);
                    return;
                }
                return;
            }
            return;
        }
        if (WEAVER_ELEMENT.equals(qName)) {
            String options = (String) attrMap.get(OPTIONS_ATTRIBUTE);
            if (!isNull(options)) {
                this.m_definition.appendWeaverOptions(options);
            }
            this.m_inWeaver = true;
            return;
        }
        if (CONCRETE_ASPECT_ELEMENT.equals(qName)) {
            String name2 = (String) attrMap.get("name");
            String extend = (String) attrMap.get(EXTEND_ATTRIBUTE);
            String precedence = (String) attrMap.get("precedence");
            String perclause = (String) attrMap.get(PERCLAUSE_ATTRIBUTE);
            if (!isNull(name2)) {
                this.m_lastConcreteAspect = new Definition.ConcreteAspect(name2, extend, precedence, perclause);
                this.m_definition.getConcreteAspects().add(this.m_lastConcreteAspect);
                return;
            }
            return;
        }
        if (POINTCUT_ELEMENT.equals(qName) && this.m_lastConcreteAspect != null) {
            String name3 = (String) attrMap.get("name");
            String expression = (String) attrMap.get(EXPRESSION_ATTRIBUTE);
            if (!isNull(name3) && !isNull(expression)) {
                this.m_lastConcreteAspect.pointcuts.add(new Definition.Pointcut(name3, replaceXmlAnd(expression)));
                return;
            }
            return;
        }
        if (ASPECTJ_ELEMENT.equals(qName)) {
            if (this.m_inAspectJ) {
                throw new Exception("Found nested <aspectj> element");
            }
            this.m_inAspectJ = true;
            return;
        }
        if (ASPECTS_ELEMENT.equals(qName)) {
            this.m_inAspects = true;
            return;
        }
        if (INCLUDE_ELEMENT.equals(qName) && this.m_inWeaver) {
            String typePattern = getWithinAttribute(attrMap);
            if (!isNull(typePattern)) {
                this.m_definition.getIncludePatterns().add(typePattern);
                return;
            }
            return;
        }
        if (EXCLUDE_ELEMENT.equals(qName) && this.m_inWeaver) {
            String typePattern2 = getWithinAttribute(attrMap);
            if (!isNull(typePattern2)) {
                this.m_definition.getExcludePatterns().add(typePattern2);
                return;
            }
            return;
        }
        if ("dump".equals(qName) && this.m_inWeaver) {
            String typePattern3 = getWithinAttribute(attrMap);
            if (!isNull(typePattern3)) {
                this.m_definition.getDumpPatterns().add(typePattern3);
            }
            String beforeAndAfter = (String) attrMap.get(DUMP_BEFOREANDAFTER_ATTRIBUTE);
            if (isTrue(beforeAndAfter)) {
                this.m_definition.setDumpBefore(true);
            }
            String perWeaverDumpDir = (String) attrMap.get(DUMP_PERCLASSLOADERDIR_ATTRIBUTE);
            if (isTrue(perWeaverDumpDir)) {
                this.m_definition.setCreateDumpDirPerClassloader(true);
                return;
            }
            return;
        }
        if (EXCLUDE_ELEMENT.equals(qName) && this.m_inAspects) {
            String typePattern4 = getWithinAttribute(attrMap);
            if (!isNull(typePattern4)) {
                this.m_definition.getAspectExcludePatterns().add(typePattern4);
                return;
            }
            return;
        }
        if (INCLUDE_ELEMENT.equals(qName) && this.m_inAspects) {
            String typePattern5 = getWithinAttribute(attrMap);
            if (!isNull(typePattern5)) {
                this.m_definition.getAspectIncludePatterns().add(typePattern5);
                return;
            }
            return;
        }
        if (DECLARE_ANNOTATION.equals(qName) && this.m_inAspects) {
            String anno = (String) attrMap.get("annotation");
            if (!isNull(anno)) {
                String pattern = (String) attrMap.get("field");
                if (pattern != null) {
                    this.m_lastConcreteAspect.declareAnnotations.add(new Definition.DeclareAnnotation(Definition.DeclareAnnotationKind.Field, pattern, anno));
                    return;
                }
                String pattern2 = (String) attrMap.get("method");
                if (pattern2 != null) {
                    this.m_lastConcreteAspect.declareAnnotations.add(new Definition.DeclareAnnotation(Definition.DeclareAnnotationKind.Method, pattern2, anno));
                    return;
                }
                String pattern3 = (String) attrMap.get("type");
                if (pattern3 != null) {
                    this.m_lastConcreteAspect.declareAnnotations.add(new Definition.DeclareAnnotation(Definition.DeclareAnnotationKind.Type, pattern3, anno));
                    return;
                }
                return;
            }
            return;
        }
        if (BEFORE_ELEMENT.equals(qName) && this.m_inAspects) {
            String pointcut = (String) attrMap.get(POINTCUT_ELEMENT);
            String adviceClass = (String) attrMap.get("invokeClass");
            String adviceMethod = (String) attrMap.get("invokeMethod");
            if (!isNull(pointcut) && !isNull(adviceClass) && !isNull(adviceMethod)) {
                this.m_lastConcreteAspect.pointcutsAndAdvice.add(new Definition.PointcutAndAdvice(Definition.AdviceKind.Before, replaceXmlAnd(pointcut), adviceClass, adviceMethod));
                return;
            }
            throw new SAXException("Badly formed <before> element");
        }
        if (AFTER_ELEMENT.equals(qName) && this.m_inAspects) {
            String pointcut2 = (String) attrMap.get(POINTCUT_ELEMENT);
            String adviceClass2 = (String) attrMap.get("invokeClass");
            String adviceMethod2 = (String) attrMap.get("invokeMethod");
            if (!isNull(pointcut2) && !isNull(adviceClass2) && !isNull(adviceMethod2)) {
                this.m_lastConcreteAspect.pointcutsAndAdvice.add(new Definition.PointcutAndAdvice(Definition.AdviceKind.After, replaceXmlAnd(pointcut2), adviceClass2, adviceMethod2));
                return;
            }
            throw new SAXException("Badly formed <after> element");
        }
        if (AROUND_ELEMENT.equals(qName) && this.m_inAspects) {
            String pointcut3 = (String) attrMap.get(POINTCUT_ELEMENT);
            String adviceClass3 = (String) attrMap.get("invokeClass");
            String adviceMethod3 = (String) attrMap.get("invokeMethod");
            if (!isNull(pointcut3) && !isNull(adviceClass3) && !isNull(adviceMethod3)) {
                this.m_lastConcreteAspect.pointcutsAndAdvice.add(new Definition.PointcutAndAdvice(Definition.AdviceKind.Around, replaceXmlAnd(pointcut3), adviceClass3, adviceMethod3));
                return;
            }
            return;
        }
        throw new Exception("Unknown element while parsing <aspectj> element: " + qName);
    }

    private void endElement(String qName) throws Exception {
        if (CONCRETE_ASPECT_ELEMENT.equals(qName)) {
            this.m_lastConcreteAspect = null;
            return;
        }
        if (ASPECTJ_ELEMENT.equals(qName)) {
            this.m_inAspectJ = false;
        } else if (WEAVER_ELEMENT.equals(qName)) {
            this.m_inWeaver = false;
        } else if (ASPECTS_ELEMENT.equals(qName)) {
            this.m_inAspects = false;
        }
    }

    private String getWithinAttribute(Map attributes) {
        return replaceXmlAnd((String) attributes.get(WITHIN_ATTRIBUTE));
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

    private static void traverse(SimpleAOPParser sap, LightXMLParser xml) throws Exception {
        sap.startElement(xml.getName(), xml.getAttributes());
        ArrayList childrens = xml.getChildrens();
        for (int i = 0; i < childrens.size(); i++) {
            LightXMLParser child = (LightXMLParser) childrens.get(i);
            traverse(sap, child);
        }
        sap.endElement(xml.getName());
    }
}
