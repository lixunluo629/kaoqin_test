package org.apache.xmlbeans.impl.tool;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.stream.Location;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlOptionsBean;
import org.apache.xmlbeans.impl.common.StaxHelper;
import org.apache.xmlbeans.impl.validator.ValidatingXMLStreamReader;
import org.springframework.beans.factory.xml.DelegatingEntityResolver;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/StreamInstanceValidator.class */
public class StreamInstanceValidator {
    public static void printUsage() {
        System.out.println("Validates the specified instance against the specified schema.");
        System.out.println("A streaming validation useful for validating very large instance ");
        System.out.println("documents with less memory. Contrast with the validate tool.");
        System.out.println("Usage: svalidate [-dl] [-nopvr] [-noupa] [-license] schema.xsd instance.xml");
        System.out.println("Options:");
        System.out.println("    -dl - permit network downloads for imports and includes (default is off)");
        System.out.println("    -noupa - do not enforce the unique particle attribution rule");
        System.out.println("    -nopvr - do not enforce the particle valid (restriction) rule");
        System.out.println("    -license - prints license information");
    }

    public static void main(String[] args) throws IOException {
        Set flags = new HashSet();
        flags.add("h");
        flags.add("help");
        flags.add("usage");
        flags.add("license");
        flags.add("version");
        flags.add("dl");
        flags.add("noupr");
        flags.add("noupa");
        CommandLine cl = new CommandLine(args, flags, Collections.EMPTY_SET);
        if (cl.getOpt("h") != null || cl.getOpt("help") != null || cl.getOpt("usage") != null || args.length < 1) {
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
        boolean dl = cl.getOpt("dl") != null;
        boolean nopvr = cl.getOpt("nopvr") != null;
        boolean noupa = cl.getOpt("noupa") != null;
        File[] schemaFiles = cl.filesEndingWith(DelegatingEntityResolver.XSD_SUFFIX);
        File[] instanceFiles = cl.filesEndingWith(".xml");
        File[] jarFiles = cl.filesEndingWith(".jar");
        List sdocs = new ArrayList();
        XmlOptions options = new XmlOptions().setLoadLineNumbers();
        for (int i = 0; i < schemaFiles.length; i++) {
            try {
                sdocs.add(XmlObject.Factory.parse(schemaFiles[i], options.setLoadMessageDigest()));
            } catch (Exception e) {
                System.err.println(schemaFiles[i] + " not loadable: " + e);
            }
        }
        XmlObject[] schemas = (XmlObject[]) sdocs.toArray(new XmlObject[0]);
        SchemaTypeLoader sLoader = null;
        Collection compErrors = new ArrayList();
        XmlOptions schemaOptions = new XmlOptions();
        schemaOptions.setErrorListener(compErrors);
        if (dl) {
            schemaOptions.setCompileDownloadUrls();
        }
        if (nopvr) {
            schemaOptions.setCompileNoPvrRule();
        }
        if (noupa) {
            schemaOptions.setCompileNoUpaRule();
        }
        if (jarFiles != null && jarFiles.length > 0) {
            sLoader = XmlBeans.typeLoaderForResource(XmlBeans.resourceLoaderForPath(jarFiles));
        }
        if (schemas != null) {
            try {
                if (schemas.length > 0) {
                    sLoader = XmlBeans.compileXsd(schemas, sLoader, schemaOptions);
                }
            } catch (Exception e2) {
                if (compErrors.isEmpty() || !(e2 instanceof XmlException)) {
                    e2.printStackTrace(System.err);
                }
                System.out.println("Schema invalid");
                Iterator i2 = compErrors.iterator();
                while (i2.hasNext()) {
                    System.out.println(i2.next());
                }
                return;
            }
        }
        validateFiles(instanceFiles, sLoader, options);
    }

    public static void validateFiles(File[] instanceFiles, SchemaTypeLoader sLoader, XmlOptions options) throws IOException {
        ValidatingXMLStreamReader vsr = new ValidatingXMLStreamReader();
        Collection<XmlError> errors = new ArrayList();
        for (File file : instanceFiles) {
            String path = file.getPath();
            long time = 0;
            errors.clear();
            try {
                XMLInputFactory xmlInputFactory = StaxHelper.newXMLInputFactory(new XmlOptionsBean(options));
                FileInputStream fis = new FileInputStream(file);
                XMLStreamReader rdr = xmlInputFactory.createXMLStreamReader(path, fis);
                while (!rdr.isStartElement()) {
                    rdr.next();
                }
                long time2 = System.currentTimeMillis();
                vsr.init(rdr, true, null, sLoader, options, errors);
                while (vsr.hasNext()) {
                    vsr.next();
                }
                time = System.currentTimeMillis() - time2;
                vsr.close();
                fis.close();
            } catch (XMLStreamException xse) {
                Location loc = xse.getLocation();
                errors.add(XmlError.forLocation(xse.getMessage(), path, loc.getLineNumber(), loc.getColumnNumber(), loc.getCharacterOffset()));
            } catch (Exception e) {
                System.err.println("error for file: " + file + ": " + e);
                e.printStackTrace(System.err);
            }
            if (errors.isEmpty()) {
                System.out.println(file + " valid. (" + time + " ms)");
            } else {
                System.out.println(file + " NOT valid (" + time + " ms):");
                for (XmlError err : errors) {
                    System.out.println(stringFromError(err, path));
                }
            }
        }
    }

    private static String stringFromError(XmlError err, String path) {
        String s = XmlError.severityAsString(err.getSeverity()) + ": " + path + ":" + err.getLine() + ":" + err.getColumn() + SymbolConstants.SPACE_SYMBOL + err.getMessage() + SymbolConstants.SPACE_SYMBOL;
        return s;
    }
}
