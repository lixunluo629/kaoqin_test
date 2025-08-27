package org.apache.poi.openxml4j.opc;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.w3c.dom.Document;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/openxml4j/opc/StreamHelper.class */
public final class StreamHelper {
    private static final TransformerFactory transformerFactory = TransformerFactory.newInstance();

    private StreamHelper() {
    }

    private static synchronized Transformer getIdentityTransformer() throws TransformerException {
        return transformerFactory.newTransformer();
    }

    public static boolean saveXmlInStream(Document xmlContent, OutputStream outStream) throws TransformerException, IllegalArgumentException {
        try {
            Transformer trans = getIdentityTransformer();
            Source xmlSource = new DOMSource(xmlContent);
            Result outputTarget = new StreamResult(new FilterOutputStream(outStream) { // from class: org.apache.poi.openxml4j.opc.StreamHelper.1
                @Override // java.io.FilterOutputStream, java.io.OutputStream
                public void write(byte[] b, int off, int len) throws IOException {
                    this.out.write(b, off, len);
                }

                @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
                public void close() throws IOException {
                    this.out.flush();
                }
            });
            trans.setOutputProperty("encoding", "UTF-8");
            trans.setOutputProperty("indent", "no");
            trans.setOutputProperty("standalone", CustomBooleanEditor.VALUE_YES);
            trans.transform(xmlSource, outputTarget);
            return true;
        } catch (TransformerException e) {
            return false;
        }
    }

    public static boolean copyStream(InputStream inStream, OutputStream outStream) throws IOException {
        try {
            byte[] buffer = new byte[1024];
            while (true) {
                int bytesRead = inStream.read(buffer);
                if (bytesRead >= 0) {
                    outStream.write(buffer, 0, bytesRead);
                } else {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }
}
