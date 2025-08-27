package org.apache.xmlbeans.impl.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.springframework.beans.factory.xml.DelegatingEntityResolver;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/InstanceValidator.class */
public class InstanceValidator {
    public static void printUsage() {
        System.out.println("Validates the specified instance against the specified schema.");
        System.out.println("Contrast with the svalidate tool, which validates using a stream.");
        System.out.println("Usage: validate [-dl] [-nopvr] [-noupa] [-license] schema.xsd instance.xml");
        System.out.println("Options:");
        System.out.println("    -dl - permit network downloads for imports and includes (default is off)");
        System.out.println("    -noupa - do not enforce the unique particle attribution rule");
        System.out.println("    -nopvr - do not enforce the particle valid (restriction) rule");
        System.out.println("    -strict - performs strict(er) validation");
        System.out.println("    -partial - allow partial schema type system");
        System.out.println("    -license - prints license information");
    }

    public static void main(String[] args) {
        System.exit(extraMain(args));
    }

    public static int extraMain(String[] args) {
        Set flags = new HashSet();
        flags.add("h");
        flags.add("help");
        flags.add("usage");
        flags.add("license");
        flags.add("version");
        flags.add("dl");
        flags.add("noupa");
        flags.add("nopvr");
        flags.add("strict");
        flags.add("partial");
        CommandLine cl = new CommandLine(args, flags, Collections.EMPTY_SET);
        if (cl.getOpt("h") != null || cl.getOpt("help") != null || cl.getOpt("usage") != null || args.length < 1) {
            printUsage();
            return 0;
        }
        String[] badopts = cl.getBadOpts();
        if (badopts.length > 0) {
            for (String str : badopts) {
                System.out.println("Unrecognized option: " + str);
            }
            printUsage();
            return 0;
        }
        if (cl.getOpt("license") != null) {
            CommandLine.printLicense();
            return 0;
        }
        if (cl.getOpt("version") != null) {
            CommandLine.printVersion();
            return 0;
        }
        if (cl.args().length == 0) {
            return 0;
        }
        boolean dl = cl.getOpt("dl") != null;
        boolean nopvr = cl.getOpt("nopvr") != null;
        boolean noupa = cl.getOpt("noupa") != null;
        boolean strict = cl.getOpt("strict") != null;
        boolean partial = cl.getOpt("partial") != null;
        File[] schemaFiles = cl.filesEndingWith(DelegatingEntityResolver.XSD_SUFFIX);
        File[] instanceFiles = cl.filesEndingWith(".xml");
        File[] jarFiles = cl.filesEndingWith(".jar");
        List sdocs = new ArrayList();
        for (int i = 0; i < schemaFiles.length; i++) {
            try {
                sdocs.add(XmlObject.Factory.parse(schemaFiles[i], new XmlOptions().setLoadLineNumbers().setLoadMessageDigest()));
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
        if (partial) {
            schemaOptions.put("COMPILE_PARTIAL_TYPESYSTEM");
        }
        if (jarFiles != null && jarFiles.length > 0) {
            sLoader = XmlBeans.typeLoaderForResource(XmlBeans.resourceLoaderForPath(jarFiles));
        }
        int returnCode = 0;
        if (schemas != null) {
            try {
                if (schemas.length > 0) {
                    sLoader = XmlBeans.compileXsd(schemas, sLoader, schemaOptions);
                }
            } catch (Exception e2) {
                if (compErrors.isEmpty() || !(e2 instanceof XmlException)) {
                    e2.printStackTrace(System.err);
                }
                System.out.println("Schema invalid:" + (partial ? " couldn't recover from errors" : ""));
                Iterator i2 = compErrors.iterator();
                while (i2.hasNext()) {
                    System.out.println(i2.next());
                }
                return 10;
            }
        }
        if (partial && !compErrors.isEmpty()) {
            returnCode = 11;
            System.out.println("Schema invalid: partial schema type system recovered");
            Iterator i3 = compErrors.iterator();
            while (i3.hasNext()) {
                System.out.println(i3.next());
            }
        }
        if (sLoader == null) {
            sLoader = XmlBeans.getContextTypeLoader();
        }
        for (int i4 = 0; i4 < instanceFiles.length; i4++) {
            try {
                XmlObject xobj = sLoader.parse(instanceFiles[i4], (SchemaType) null, new XmlOptions().setLoadLineNumbers(XmlOptions.LOAD_LINE_NUMBERS_END_ELEMENT));
                Collection errors = new ArrayList();
                if (xobj.schemaType() == XmlObject.type) {
                    System.out.println(instanceFiles[i4] + " NOT valid.  ");
                    System.out.println("  Document type not found.");
                } else if (xobj.validate(strict ? new XmlOptions().setErrorListener(errors).setValidateStrict() : new XmlOptions().setErrorListener(errors))) {
                    System.out.println(instanceFiles[i4] + " valid.");
                } else {
                    returnCode = 1;
                    System.out.println(instanceFiles[i4] + " NOT valid.");
                    Iterator it = errors.iterator();
                    while (it.hasNext()) {
                        System.out.println(it.next());
                    }
                }
            } catch (Exception e3) {
                System.err.println(instanceFiles[i4] + " not loadable: " + e3);
                e3.printStackTrace(System.err);
            }
        }
        return returnCode;
    }
}
