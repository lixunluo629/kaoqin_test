package org.apache.poi.dev;

import ch.qos.logback.classic.net.SyslogAppender;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.openxml4j.opc.internal.ZipHelper;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/dev/OOXMLPrettyPrint.class */
public class OOXMLPrettyPrint {
    private final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    private final DocumentBuilder documentBuilder;

    public OOXMLPrettyPrint() throws ParserConfigurationException {
        ZipSecureFile.setMinInflateRatio(1.0E-5d);
        this.documentBuilder = this.documentBuilderFactory.newDocumentBuilder();
    }

    public static void main(String[] args) throws Exception {
        if (args.length <= 1 || args.length % 2 != 0) {
            System.err.println("Use:");
            System.err.println("\tjava OOXMLPrettyPrint [<filename> <outfilename>] ...");
            System.exit(1);
        }
        for (int i = 0; i < args.length; i += 2) {
            File f = new File(args[i]);
            if (!f.exists()) {
                System.err.println("Error, file not found!");
                System.err.println(SyslogAppender.DEFAULT_STACKTRACE_PATTERN + f);
                System.exit(2);
            }
            handleFile(f, new File(args[i + 1]));
        }
        System.out.println("Done.");
    }

    private static void handleFile(File file, File outFile) throws NotOfficeXmlFileException, TransformerException, ParserConfigurationException, IOException {
        System.out.println("Reading zip-file " + file + " and writing pretty-printed XML to " + outFile);
        ZipFile zipFile = ZipHelper.openZipFile(file);
        try {
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outFile)));
            try {
                new OOXMLPrettyPrint().handle(zipFile, out);
                out.close();
            } catch (Throwable th) {
                out.close();
                throw th;
            }
        } finally {
            zipFile.close();
            System.out.println();
        }
    }

    private void handle(ZipFile file, ZipOutputStream out) throws TransformerException, IOException {
        Enumeration<? extends ZipEntry> entries = file.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            String name = entry.getName();
            out.putNextEntry(new ZipEntry(name));
            try {
                try {
                    if (name.endsWith(".xml") || name.endsWith(".rels")) {
                        Document document = this.documentBuilder.parse(new InputSource(file.getInputStream(entry)));
                        document.setXmlStandalone(true);
                        pretty(document, out, 2);
                    } else {
                        System.out.println("Not pretty-printing non-XML file " + name);
                        IOUtils.copy(file.getInputStream(entry), out);
                    }
                    System.out.print(".");
                } catch (Exception e) {
                    throw new IOException("While handling entry " + name, e);
                }
            } finally {
                out.closeEntry();
            }
        }
    }

    private static void pretty(Document document, OutputStream outputStream, int indent) throws TransformerException, TransformerFactoryConfigurationError, IllegalArgumentException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty("encoding", "UTF-8");
        if (indent > 0) {
            transformer.setOutputProperty("indent", CustomBooleanEditor.VALUE_YES);
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", Integer.toString(indent));
        }
        Result result = new StreamResult(outputStream);
        Source source = new DOMSource(document);
        transformer.transform(source, result);
    }
}
