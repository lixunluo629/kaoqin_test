package org.apache.xmlbeans.impl.tool;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/PrettyPrinter.class */
public class PrettyPrinter {
    private static final int DEFAULT_INDENT = 2;

    public static void printUsage() {
        System.out.println("Pretty prints XML files.");
        System.out.println("Usage: xpretty [switches] file.xml");
        System.out.println("Switches:");
        System.out.println("    -indent #   use the given indent");
        System.out.println("    -license prints license information");
    }

    public static void main(String[] args) throws NumberFormatException {
        int indent;
        Set flags = new HashSet();
        flags.add("h");
        flags.add("help");
        flags.add("usage");
        flags.add("license");
        flags.add("version");
        CommandLine cl = new CommandLine(args, flags, Collections.singleton("indent"));
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
        String indentStr = cl.getOpt("indent");
        if (indentStr == null) {
            indent = 2;
        } else {
            indent = Integer.parseInt(indentStr);
        }
        File[] files = cl.getFiles();
        for (int i = 0; i < files.length; i++) {
            try {
                XmlObject doc = XmlObject.Factory.parse(files[i], new XmlOptions().setLoadLineNumbers());
                try {
                    doc.save(System.out, new XmlOptions().setSavePrettyPrint().setSavePrettyPrintIndent(indent));
                } catch (IOException e) {
                    System.err.println("Unable to pretty print " + files[i] + ": " + e.getMessage());
                }
            } catch (Exception e2) {
                System.err.println(files[i] + " not loadable: " + e2.getMessage());
            }
        }
    }

    public static String indent(String xmldoc) throws XmlException, IOException {
        StringWriter sw = new StringWriter();
        XmlObject doc = XmlObject.Factory.parse(xmldoc, new XmlOptions().setLoadLineNumbers());
        doc.save(sw, new XmlOptions().setSavePrettyPrint().setSavePrettyPrintIndent(2));
        sw.close();
        return sw.getBuffer().toString();
    }
}
