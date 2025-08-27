package org.apache.xmlbeans.impl.tool;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.common.IOUtil;
import org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument;
import org.apache.xmlbeans.impl.xb.substwsdl.TImport;
import org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.IncludeDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/SchemaCopy.class */
public class SchemaCopy {
    private static final XmlOptions loadOptions = new XmlOptions().setLoadSubstituteNamespaces(Collections.singletonMap("http://schemas.xmlsoap.org/wsdl/", "http://www.apache.org/internal/xmlbeans/wsdlsubst"));

    public static void printUsage() {
        System.out.println("Copies the XML schema at the specified URL to the specified file.");
        System.out.println("Usage: scopy sourceurl [targetfile]");
        System.out.println("    sourceurl - The URL at which the schema is located.");
        System.out.println("    targetfile - The file to which the schema should be copied.");
        System.out.println();
    }

    public static void main(String[] args) throws MalformedURLException {
        URI target;
        if (args.length < 1 || args.length > 2) {
            printUsage();
            return;
        }
        try {
            if (args[0].compareToIgnoreCase("-usage") == 0) {
                printUsage();
                return;
            }
            URI source = new URI(args[0]);
            source.toURL();
            if (args.length < 2) {
                try {
                    URI dir = new File(".").getCanonicalFile().toURI();
                    String lastPart = source.getPath();
                    target = CodeGenUtil.resolve(dir, URI.create(lastPart.substring(lastPart.lastIndexOf(47) + 1)));
                } catch (Exception e) {
                    System.err.println("Cannot canonicalize current directory");
                    return;
                }
            } else {
                try {
                    target = new URI(args[1]);
                    if (!target.isAbsolute()) {
                        target = null;
                    } else if (!target.getScheme().equals("file")) {
                        target = null;
                    }
                } catch (Exception e2) {
                    target = null;
                }
                if (target == null) {
                    try {
                        target = new File(target).getCanonicalFile().toURI();
                    } catch (Exception e3) {
                        System.err.println("Cannot canonicalize current directory");
                        return;
                    }
                }
            }
            Map thingsToCopy = findAllRelative(source, target);
            copyAll(thingsToCopy, true);
        } catch (Exception e4) {
            System.err.println("Badly formed URL " + ((Object) null));
        }
    }

    private static void copyAll(Map uriMap, boolean stdout) {
        for (URI source : uriMap.keySet()) {
            URI target = (URI) uriMap.get(source);
            try {
                IOUtil.copyCompletely(source, target);
                if (stdout) {
                    System.out.println("Copied " + source + " -> " + target);
                }
            } catch (Exception e) {
                if (stdout) {
                    System.out.println("Could not copy " + source + " -> " + target);
                }
            }
        }
    }

    public static Map findAllRelative(URI source, URI target) throws MalformedURLException {
        Map result = new LinkedHashMap();
        result.put(source, target);
        LinkedList process = new LinkedList();
        process.add(source);
        while (!process.isEmpty()) {
            URI nextSource = (URI) process.removeFirst();
            URI nextTarget = (URI) result.get(nextSource);
            Map nextResults = findRelativeInOne(nextSource, nextTarget);
            for (URI newSource : nextResults.keySet()) {
                if (!result.containsKey(newSource)) {
                    result.put(newSource, nextResults.get(newSource));
                    process.add(newSource);
                }
            }
        }
        return result;
    }

    private static Map findRelativeInOne(URI source, URI target) throws MalformedURLException {
        try {
            URL sourceURL = source.toURL();
            XmlObject xobj = XmlObject.Factory.parse(sourceURL, loadOptions);
            XmlCursor xcur = xobj.newCursor();
            xcur.toFirstChild();
            Map result = new LinkedHashMap();
            if (xobj instanceof SchemaDocument) {
                putMappingsFromSchema(result, source, target, ((SchemaDocument) xobj).getSchema());
            } else if (xobj instanceof DefinitionsDocument) {
                putMappingsFromWsdl(result, source, target, ((DefinitionsDocument) xobj).getDefinitions());
            }
            return result;
        } catch (Exception e) {
            return Collections.EMPTY_MAP;
        }
    }

    private static void putNewMapping(Map result, URI origSource, URI origTarget, String literalURI) {
        if (literalURI == null) {
            return;
        }
        try {
            URI newRelative = new URI(literalURI);
            if (newRelative.isAbsolute()) {
                return;
            }
            URI newSource = CodeGenUtil.resolve(origSource, newRelative);
            URI newTarget = CodeGenUtil.resolve(origTarget, newRelative);
            result.put(newSource, newTarget);
        } catch (URISyntaxException e) {
        }
    }

    private static void putMappingsFromSchema(Map result, URI source, URI target, SchemaDocument.Schema schema) {
        ImportDocument.Import[] imports = schema.getImportArray();
        for (ImportDocument.Import r0 : imports) {
            putNewMapping(result, source, target, r0.getSchemaLocation());
        }
        IncludeDocument.Include[] includes = schema.getIncludeArray();
        for (IncludeDocument.Include include : includes) {
            putNewMapping(result, source, target, include.getSchemaLocation());
        }
    }

    private static void putMappingsFromWsdl(Map result, URI source, URI target, DefinitionsDocument.Definitions wdoc) {
        XmlObject[] types = wdoc.getTypesArray();
        for (XmlObject xmlObject : types) {
            SchemaDocument.Schema[] schemas = (SchemaDocument.Schema[]) xmlObject.selectPath("declare namespace xs='http://www.w3.org/2001/XMLSchema' xs:schema");
            for (SchemaDocument.Schema schema : schemas) {
                putMappingsFromSchema(result, source, target, schema);
            }
        }
        TImport[] imports = wdoc.getImportArray();
        for (TImport tImport : imports) {
            putNewMapping(result, source, target, tImport.getLocation());
        }
    }
}
