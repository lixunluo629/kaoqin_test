package org.apache.xmlbeans.impl.inst2xsd;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import io.swagger.models.properties.StringProperty;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.inst2xsd.util.TypeSystemHolder;
import org.apache.xmlbeans.impl.tool.CommandLine;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.springframework.beans.factory.xml.DelegatingEntityResolver;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/inst2xsd/Inst2Xsd.class */
public class Inst2Xsd {
    public static void main(String[] args) throws NumberFormatException {
        if (args == null || args.length == 0) {
            printHelp();
            System.exit(0);
            return;
        }
        Set flags = new HashSet();
        flags.add("h");
        flags.add("help");
        flags.add("usage");
        flags.add("license");
        flags.add("version");
        flags.add("verbose");
        flags.add("validate");
        Set opts = new HashSet();
        opts.add("design");
        opts.add("simple-content-types");
        opts.add("enumerations");
        opts.add("outDir");
        opts.add("outPrefix");
        CommandLine cl = new CommandLine(args, flags, opts);
        Inst2XsdOptions inst2XsdOptions = new Inst2XsdOptions();
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
        if (cl.getOpt("h") != null || cl.getOpt("help") != null || cl.getOpt("usage") != null) {
            printHelp();
            System.exit(0);
            return;
        }
        String[] badopts = cl.getBadOpts();
        if (badopts.length > 0) {
            for (String str : badopts) {
                System.out.println("Unrecognized option: " + str);
            }
            printHelp();
            System.exit(0);
            return;
        }
        String design = cl.getOpt("design");
        if (design != null) {
            if (design.equals("vb")) {
                inst2XsdOptions.setDesign(3);
            } else if (design.equals("rd")) {
                inst2XsdOptions.setDesign(1);
            } else if (design.equals("ss")) {
                inst2XsdOptions.setDesign(2);
            } else {
                printHelp();
                System.exit(0);
                return;
            }
        }
        String simpleContent = cl.getOpt("simple-content-types");
        if (simpleContent != null) {
            if (simpleContent.equals("smart")) {
                inst2XsdOptions.setSimpleContentTypes(1);
            } else if (simpleContent.equals(StringProperty.TYPE)) {
                inst2XsdOptions.setSimpleContentTypes(2);
            } else {
                printHelp();
                System.exit(0);
                return;
            }
        }
        String enumerations = cl.getOpt("enumerations");
        if (enumerations != null) {
            if (enumerations.equals("never")) {
                inst2XsdOptions.setUseEnumerations(1);
            } else {
                try {
                    int intVal = Integer.parseInt(enumerations);
                    inst2XsdOptions.setUseEnumerations(intVal);
                } catch (NumberFormatException e) {
                    printHelp();
                    System.exit(0);
                    return;
                }
            }
        }
        File outDir = new File(cl.getOpt("outDir") == null ? "." : cl.getOpt("outDir"));
        String outPrefix = cl.getOpt("outPrefix");
        if (outPrefix == null) {
            outPrefix = "schema";
        }
        inst2XsdOptions.setVerbose(cl.getOpt("verbose") != null);
        boolean validate = cl.getOpt("validate") != null;
        File[] xmlFiles = cl.filesEndingWith(".xml");
        XmlObject[] xmlInstances = new XmlObject[xmlFiles.length];
        if (xmlInstances.length == 0) {
            printHelp();
            System.exit(0);
            return;
        }
        for (int i = 0; i < xmlFiles.length; i++) {
            try {
                xmlInstances[i] = XmlObject.Factory.parse(xmlFiles[i]);
            } catch (IOException e2) {
                System.err.println("Could not read file: '" + xmlFiles[i].getName() + "'. " + e2.getMessage());
                return;
            } catch (XmlException e3) {
                System.err.println("Invalid xml file: '" + xmlFiles[i].getName() + "'. " + e3.getMessage());
                return;
            }
        }
        SchemaDocument[] schemaDocs = inst2xsd(xmlInstances, inst2XsdOptions);
        for (int i2 = 0; i2 < schemaDocs.length; i2++) {
            try {
                SchemaDocument schema = schemaDocs[i2];
                if (inst2XsdOptions.isVerbose()) {
                    System.out.println("----------------------\n\n" + schema);
                }
                schema.save(new File(outDir, outPrefix + i2 + DelegatingEntityResolver.XSD_SUFFIX), new XmlOptions().setSavePrettyPrint());
            } catch (IOException e4) {
                System.err.println("Could not write file: '" + outDir + File.pathSeparator + outPrefix + i2 + DelegatingEntityResolver.XSD_SUFFIX + "'. " + e4.getMessage());
                return;
            }
        }
        if (validate) {
            validateInstances(schemaDocs, xmlInstances);
        }
    }

    private static void printHelp() {
        System.out.println("Generates XMLSchema from instance xml documents.");
        System.out.println("Usage: inst2xsd [opts] [instance.xml]*");
        System.out.println("Options include:");
        System.out.println("    -design [rd|ss|vb] - XMLSchema design type");
        System.out.println("             rd  - Russian Doll Design - local elements and local types");
        System.out.println("             ss  - Salami Slice Design - global elements and local types");
        System.out.println("             vb  - Venetian Blind Design (default) - local elements and global complex types");
        System.out.println("    -simple-content-types [smart|string] - Simple content types detection (leaf text). Smart is the default");
        System.out.println("    -enumerations [never|NUMBER] - Use enumerations. Default value is 10.");
        System.out.println("    -outDir [dir] - Directory for output files. Default is '.'");
        System.out.println("    -outPrefix [file_name_prefix] - Prefix for output file names. Default is 'schema'");
        System.out.println("    -validate - Validates input instances agaist generated schemas.");
        System.out.println("    -verbose - print more informational messages");
        System.out.println("    -license - print license information");
        System.out.println("    -help - help imformation");
    }

    private Inst2Xsd() {
    }

    public static SchemaDocument[] inst2xsd(Reader[] instReaders, Inst2XsdOptions options) throws XmlException, IOException {
        XmlObject[] instances = new XmlObject[instReaders.length];
        for (int i = 0; i < instReaders.length; i++) {
            instances[i] = XmlObject.Factory.parse(instReaders[i]);
        }
        return inst2xsd(instances, options);
    }

    public static SchemaDocument[] inst2xsd(XmlObject[] instances, Inst2XsdOptions options) {
        XsdGenStrategy strategy;
        if (options == null) {
            options = new Inst2XsdOptions();
        }
        TypeSystemHolder typeSystemHolder = new TypeSystemHolder();
        switch (options.getDesign()) {
            case 1:
                strategy = new RussianDollStrategy();
                break;
            case 2:
                strategy = new SalamiSliceStrategy();
                break;
            case 3:
                strategy = new VenetianBlindStrategy();
                break;
            default:
                throw new IllegalArgumentException("Unknown design.");
        }
        strategy.processDoc(instances, options, typeSystemHolder);
        if (options.isVerbose()) {
            System.out.println("typeSystemHolder.toString(): " + typeSystemHolder);
        }
        SchemaDocument[] sDocs = typeSystemHolder.getSchemaDocuments();
        return sDocs;
    }

    private static boolean validateInstances(SchemaDocument[] sDocs, XmlObject[] instances) {
        Collection<XmlError> compErrors = new ArrayList();
        XmlOptions schemaOptions = new XmlOptions();
        schemaOptions.setErrorListener(compErrors);
        try {
            SchemaTypeLoader sLoader = XmlBeans.loadXsd(sDocs, schemaOptions);
            System.out.println("\n-------------------");
            boolean result = true;
            for (int i = 0; i < instances.length; i++) {
                try {
                    XmlObject xobj = sLoader.parse(instances[i].newXMLStreamReader(), (SchemaType) null, new XmlOptions().setLoadLineNumbers());
                    Collection<XmlError> errors = new ArrayList();
                    if (xobj.schemaType() == XmlObject.type) {
                        System.out.println(instances[i].documentProperties().getSourceName() + " NOT valid.  ");
                        System.out.println("  Document type not found.");
                        result = false;
                    } else if (xobj.validate(new XmlOptions().setErrorListener(errors))) {
                        System.out.println("Instance[" + i + "] valid - " + instances[i].documentProperties().getSourceName());
                    } else {
                        System.out.println("Instance[" + i + "] NOT valid - " + instances[i].documentProperties().getSourceName());
                        for (XmlError xe : errors) {
                            System.out.println(xe.getLine() + ":" + xe.getColumn() + SymbolConstants.SPACE_SYMBOL + xe.getMessage());
                        }
                        result = false;
                    }
                } catch (XmlException e) {
                    System.out.println("Error:\n" + instances[i].documentProperties().getSourceName() + " not loadable: " + e);
                    e.printStackTrace(System.out);
                    result = false;
                }
            }
            return result;
        } catch (Exception e2) {
            if (compErrors.isEmpty() || !(e2 instanceof XmlException)) {
                e2.printStackTrace(System.out);
            }
            System.out.println("\n-------------------\n\nInvalid schemas.");
            for (XmlError xe2 : compErrors) {
                System.out.println(xe2.getLine() + ":" + xe2.getColumn() + SymbolConstants.SPACE_SYMBOL + xe2.getMessage());
            }
            return false;
        }
    }
}
