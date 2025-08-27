package org.apache.commons.httpclient.methods.multipart;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/methods/multipart/FilePart.class */
public class FilePart extends PartBase {
    public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
    public static final String DEFAULT_CHARSET = "ISO-8859-1";
    public static final String DEFAULT_TRANSFER_ENCODING = "binary";
    private static final Log LOG;
    protected static final String FILE_NAME = "; filename=";
    private static final byte[] FILE_NAME_BYTES;
    private PartSource source;
    static Class class$org$apache$commons$httpclient$methods$multipart$FilePart;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$methods$multipart$FilePart == null) {
            clsClass$ = class$("org.apache.commons.httpclient.methods.multipart.FilePart");
            class$org$apache$commons$httpclient$methods$multipart$FilePart = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$methods$multipart$FilePart;
        }
        LOG = LogFactory.getLog(clsClass$);
        FILE_NAME_BYTES = EncodingUtil.getAsciiBytes(FILE_NAME);
    }

    public FilePart(String name, PartSource partSource, String contentType, String charset) {
        super(name, contentType == null ? "application/octet-stream" : contentType, charset == null ? "ISO-8859-1" : charset, "binary");
        if (partSource == null) {
            throw new IllegalArgumentException("Source may not be null");
        }
        this.source = partSource;
    }

    public FilePart(String name, PartSource partSource) {
        this(name, partSource, (String) null, (String) null);
    }

    public FilePart(String name, File file) throws FileNotFoundException {
        this(name, new FilePartSource(file), (String) null, (String) null);
    }

    public FilePart(String name, File file, String contentType, String charset) throws FileNotFoundException {
        this(name, new FilePartSource(file), contentType, charset);
    }

    public FilePart(String name, String fileName, File file) throws FileNotFoundException {
        this(name, new FilePartSource(fileName, file), (String) null, (String) null);
    }

    public FilePart(String name, String fileName, File file, String contentType, String charset) throws FileNotFoundException {
        this(name, new FilePartSource(fileName, file), contentType, charset);
    }

    @Override // org.apache.commons.httpclient.methods.multipart.Part
    protected void sendDispositionHeader(OutputStream out) throws IOException {
        LOG.trace("enter sendDispositionHeader(OutputStream out)");
        super.sendDispositionHeader(out);
        String filename = this.source.getFileName();
        if (filename != null) {
            out.write(FILE_NAME_BYTES);
            out.write(QUOTE_BYTES);
            out.write(EncodingUtil.getAsciiBytes(filename));
            out.write(QUOTE_BYTES);
        }
    }

    @Override // org.apache.commons.httpclient.methods.multipart.Part
    protected void sendData(OutputStream out) throws IOException {
        LOG.trace("enter sendData(OutputStream out)");
        if (lengthOfData() == 0) {
            LOG.debug("No data to send.");
            return;
        }
        byte[] tmp = new byte[4096];
        InputStream instream = this.source.createInputStream();
        while (true) {
            try {
                int len = instream.read(tmp);
                if (len >= 0) {
                    out.write(tmp, 0, len);
                } else {
                    return;
                }
            } finally {
                instream.close();
            }
        }
    }

    protected PartSource getSource() {
        LOG.trace("enter getSource()");
        return this.source;
    }

    @Override // org.apache.commons.httpclient.methods.multipart.Part
    protected long lengthOfData() throws IOException {
        LOG.trace("enter lengthOfData()");
        return this.source.getLength();
    }
}
