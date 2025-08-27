package org.apache.xmlbeans.impl.tool;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.common.Sax2Dom;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.springframework.beans.factory.xml.DelegatingEntityResolver;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/TypeHierarchyPrinter.class */
public class TypeHierarchyPrinter {
    public static void printUsage() {
        System.out.println("Prints the inheritance hierarchy of types defined in a schema.\n");
        System.out.println("Usage: xsdtree [-noanon] [-nopvr] [-noupa] [-partial] [-license] schemafile.xsd*");
        System.out.println("    -noanon - Don't include anonymous types in the tree.");
        System.out.println("    -noupa - do not enforce the unique particle attribution rule");
        System.out.println("    -nopvr - do not enforce the particle valid (restriction) rule");
        System.out.println("    -partial - Print only part of the hierarchy.");
        System.out.println("    -license - prints license information");
        System.out.println("    schemafile.xsd - File containing the schema for which to print a tree.");
        System.out.println();
    }

    public static void main(String[] args) throws Exception {
        Set flags = new HashSet();
        flags.add("h");
        flags.add("help");
        flags.add("usage");
        flags.add("license");
        flags.add("version");
        flags.add("noanon");
        flags.add("noupr");
        flags.add("noupa");
        flags.add("partial");
        CommandLine cl = new CommandLine(args, flags, Collections.EMPTY_SET);
        if (cl.getOpt("h") != null || cl.getOpt("help") != null || cl.getOpt("usage") != null) {
            printUsage();
            System.exit(0);
            return;
        }
        String[] badopts = cl.getBadOpts();
        if (badopts.length > 0) {
            for (String str : badopts) {
                System.out.println("Unrecognized option: " + str);
            }
            printUsage();
            System.exit(0);
            return;
        }
        if (cl.getOpt("license") != null) {
            CommandLine.printLicense();
            System.exit(0);
            return;
        }
        if (cl.getOpt("version") != null) {
            CommandLine.printVersion();
            System.exit(0);
            return;
        }
        if (cl.args().length == 0) {
            printUsage();
            return;
        }
        boolean noanon = cl.getOpt("noanon") != null;
        boolean nopvr = cl.getOpt("nopvr") != null;
        boolean noupa = cl.getOpt("noupa") != null;
        boolean partial = cl.getOpt("partial") != null;
        File[] schemaFiles = cl.filesEndingWith(DelegatingEntityResolver.XSD_SUFFIX);
        File[] jarFiles = cl.filesEndingWith(".jar");
        List sdocs = new ArrayList();
        for (int i = 0; i < schemaFiles.length; i++) {
            try {
                sdocs.add(SchemaDocument.Factory.parse(schemaFiles[i], new XmlOptions().setLoadLineNumbers()));
            } catch (Exception e) {
                System.err.println(schemaFiles[i] + " not loadable: " + e);
            }
        }
        XmlObject[] schemas = (XmlObject[]) sdocs.toArray(new XmlObject[0]);
        SchemaTypeLoader linkTo = null;
        Collection compErrors = new ArrayList();
        XmlOptions schemaOptions = new XmlOptions();
        schemaOptions.setErrorListener(compErrors);
        schemaOptions.setCompileDownloadUrls();
        if (nopvr) {
            schemaOptions.setCompileNoPvrRule();
        }
        if (noupa) {
            schemaOptions.setCompileNoUpaRule();
        }
        if (partial) {
            schemaOptions.put("COMPILE_PARTIAL_TYPESYSTEM");
        }
        if (jarFiles != null && jarFiles.length > 0) {
            linkTo = XmlBeans.typeLoaderForResource(XmlBeans.resourceLoaderForPath(jarFiles));
        }
        try {
            SchemaTypeSystem typeSystem = XmlBeans.compileXsd(schemas, linkTo, schemaOptions);
            if (partial && !compErrors.isEmpty()) {
                System.out.println("Schema invalid: partial schema type system recovered");
                Iterator i2 = compErrors.iterator();
                while (i2.hasNext()) {
                    System.out.println(i2.next());
                }
            }
            Map prefixes = new HashMap();
            prefixes.put("http://www.w3.org/XML/1998/namespace", "xml");
            prefixes.put("http://www.w3.org/2001/XMLSchema", "xs");
            System.out.println("xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"");
            Map childTypes = new HashMap();
            List allSeenTypes = new ArrayList();
            allSeenTypes.addAll(Arrays.asList(typeSystem.documentTypes()));
            allSeenTypes.addAll(Arrays.asList(typeSystem.attributeTypes()));
            allSeenTypes.addAll(Arrays.asList(typeSystem.globalTypes()));
            for (int i3 = 0; i3 < allSeenTypes.size(); i3++) {
                SchemaType sType = (SchemaType) allSeenTypes.get(i3);
                if (!noanon) {
                    allSeenTypes.addAll(Arrays.asList(sType.getAnonymousTypes()));
                }
                if (!sType.isDocumentType() && !sType.isAttributeType() && sType != XmlObject.type) {
                    noteNamespace(prefixes, sType);
                    Collection children = (Collection) childTypes.get(sType.getBaseType());
                    if (children == null) {
                        children = new ArrayList();
                        childTypes.put(sType.getBaseType(), children);
                        if (sType.getBaseType().isBuiltinType()) {
                            allSeenTypes.add(sType.getBaseType());
                        }
                    }
                    children.add(sType);
                }
            }
            List typesToPrint = new ArrayList();
            typesToPrint.add(XmlObject.type);
            StringBuffer spaces = new StringBuffer();
            while (!typesToPrint.isEmpty()) {
                SchemaType sType2 = (SchemaType) typesToPrint.remove(typesToPrint.size() - 1);
                if (sType2 == null) {
                    spaces.setLength(Math.max(0, spaces.length() - 2));
                } else {
                    System.out.println(((Object) spaces) + "+-" + QNameHelper.readable(sType2, prefixes) + notes(sType2));
                    Collection children2 = (Collection) childTypes.get(sType2);
                    if (children2 != null && children2.size() > 0) {
                        spaces.append((typesToPrint.size() == 0 || typesToPrint.get(typesToPrint.size() - 1) == null) ? "  " : "| ");
                        typesToPrint.add(null);
                        typesToPrint.addAll(children2);
                    }
                }
            }
        } catch (XmlException e2) {
            System.out.println("Schema invalid:" + (partial ? " couldn't recover from errors" : ""));
            if (compErrors.isEmpty()) {
                System.out.println(e2.getMessage());
                return;
            }
            Iterator i4 = compErrors.iterator();
            while (i4.hasNext()) {
                System.out.println(i4.next());
            }
        }
    }

    private static String notes(SchemaType sType) {
        if (sType.isBuiltinType()) {
            return " (builtin)";
        }
        if (sType.isSimpleType()) {
            switch (sType.getSimpleVariety()) {
                case 2:
                    return " (union)";
                case 3:
                    return " (list)";
                default:
                    if (sType.getEnumerationValues() != null) {
                        return " (enumeration)";
                    }
                    return "";
            }
        }
        switch (sType.getContentType()) {
            case 2:
                return " (complex)";
            case 4:
                return " (mixed)";
            default:
                return "";
        }
    }

    private static void noteNamespace(Map prefixes, SchemaType sType) {
        String namespace = QNameHelper.namespace(sType);
        if (namespace.equals("") || prefixes.containsKey(namespace)) {
            return;
        }
        String base = QNameHelper.suggestPrefix(namespace);
        String result = base;
        int n = 0;
        while (prefixes.containsValue(result)) {
            result = base + n;
            n++;
        }
        prefixes.put(namespace, result);
        System.out.println(Sax2Dom.XMLNS_STRING + result + "=\"" + namespace + SymbolConstants.QUOTES_SYMBOL);
    }
}
