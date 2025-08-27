package org.apache.poi.dev;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.poi.util.XMLHelper;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;
import org.bouncycastle.i18n.TextBundle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/* loaded from: poi-3.17.jar:org/apache/poi/dev/RecordGenerator.class */
public class RecordGenerator {
    public static void main(String[] args) throws Exception {
        Class.forName("org.apache.poi.generator.FieldIterator");
        if (args.length != 4) {
            System.out.println("Usage:");
            System.out.println("  java org.apache.poi.hssf.util.RecordGenerator RECORD_DEFINTIONS RECORD_STYLES DEST_SRC_PATH TEST_SRC_PATH");
        } else {
            generateRecords(args[0], args[1], args[2], args[3]);
        }
    }

    private static void generateRecords(String defintionsDir, String recordStyleDir, String destSrcPathDir, String testSrcPathDir) throws Exception {
        File[] definitionsFiles = new File(defintionsDir).listFiles();
        if (definitionsFiles == null) {
            System.err.println(defintionsDir + " is not a directory.");
            return;
        }
        for (File file : definitionsFiles) {
            if (file.isFile() && (file.getName().endsWith("_record.xml") || file.getName().endsWith("_type.xml"))) {
                DocumentBuilderFactory factory = XMLHelper.getDocumentBuilderFactory();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(file);
                Element record = document.getDocumentElement();
                String extendstg = record.getElementsByTagName("extends").item(0).getFirstChild().getNodeValue();
                String suffix = record.getElementsByTagName("suffix").item(0).getFirstChild().getNodeValue();
                String recordName = record.getAttributes().getNamedItem("name").getNodeValue();
                String packageName = record.getAttributes().getNamedItem("package").getNodeValue().replace('.', '/');
                String destinationPath = destSrcPathDir + "/" + packageName;
                File destinationPathFile = new File(destinationPath);
                if (!destinationPathFile.mkdirs()) {
                    throw new IOException("Could not create directory " + destinationPathFile);
                }
                System.out.println("Created destination directory: " + destinationPath);
                String destinationFilepath = destinationPath + "/" + recordName + suffix + ".java";
                transform(file, new File(destinationFilepath), new File(recordStyleDir + "/" + extendstg.toLowerCase(Locale.ROOT) + ".xsl"));
                System.out.println("Generated " + suffix + ": " + destinationFilepath);
                String destinationPath2 = testSrcPathDir + "/" + packageName;
                File destinationPathFile2 = new File(destinationPath2);
                if (!destinationPathFile2.mkdirs()) {
                    throw new IOException("Could not create directory " + destinationPathFile2);
                }
                System.out.println("Created destination directory: " + destinationPath2);
                String destinationFilepath2 = destinationPath2 + "/Test" + recordName + suffix + ".java";
                if (!new File(destinationFilepath2).exists()) {
                    String temp = recordStyleDir + "/" + extendstg.toLowerCase(Locale.ROOT) + "_test.xsl";
                    transform(file, new File(destinationFilepath2), new File(temp));
                    System.out.println("Generated test: " + destinationFilepath2);
                } else {
                    System.out.println("Skipped test generation: " + destinationFilepath2);
                }
            }
        }
    }

    private static void transform(File in, File out, File xslt) throws TransformerException, TransformerFactoryConfigurationError, FileNotFoundException {
        StreamSource ss = new StreamSource(xslt);
        TransformerFactory tf = TransformerFactory.newInstance();
        try {
            Transformer t = tf.newTransformer(ss);
            Properties p = new Properties();
            p.setProperty(JamXmlElements.METHOD, TextBundle.TEXT_ENTRY);
            t.setOutputProperties(p);
            Result result = new StreamResult(out);
            t.transform(new StreamSource(in), result);
        } catch (TransformerException ex) {
            System.err.println("Error compiling XSL style sheet " + xslt);
            throw ex;
        }
    }
}
