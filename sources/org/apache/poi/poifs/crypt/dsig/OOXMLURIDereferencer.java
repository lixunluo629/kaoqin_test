package org.apache.poi.poifs.crypt.dsig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import javax.xml.crypto.Data;
import javax.xml.crypto.OctetStreamData;
import javax.xml.crypto.URIDereferencer;
import javax.xml.crypto.URIReference;
import javax.xml.crypto.URIReferenceException;
import javax.xml.crypto.XMLCryptoContext;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackagePartName;
import org.apache.poi.openxml4j.opc.PackagingURIHelper;
import org.apache.poi.poifs.crypt.dsig.SignatureConfig;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/poifs/crypt/dsig/OOXMLURIDereferencer.class */
public class OOXMLURIDereferencer implements URIDereferencer, SignatureConfig.SignatureConfigurable {
    private static final POILogger LOG = POILogFactory.getLogger((Class<?>) OOXMLURIDereferencer.class);
    private SignatureConfig signatureConfig;
    private URIDereferencer baseUriDereferencer;

    @Override // org.apache.poi.poifs.crypt.dsig.SignatureConfig.SignatureConfigurable
    public void setSignatureConfig(SignatureConfig signatureConfig) {
        this.signatureConfig = signatureConfig;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.xml.crypto.URIReferenceException */
    public Data dereference(URIReference uriReference, XMLCryptoContext context) throws IOException, URIReferenceException {
        if (this.baseUriDereferencer == null) {
            this.baseUriDereferencer = this.signatureConfig.getSignatureFactory().getURIDereferencer();
        }
        if (null == uriReference) {
            throw new NullPointerException("URIReference cannot be null");
        }
        if (null == context) {
            throw new NullPointerException("XMLCrytoContext cannot be null");
        }
        try {
            URI uri = new URI(uriReference.getURI());
            PackagePart part = findPart(uri);
            if (part == null) {
                LOG.log(1, "cannot resolve, delegating to base DOM URI dereferencer", uri);
                return this.baseUriDereferencer.dereference(uriReference, context);
            }
            try {
                InputStream dataStream = part.getInputStream();
                if (part.getPartName().toString().endsWith(".rels")) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    while (true) {
                        int ch2 = dataStream.read();
                        if (ch2 == -1) {
                            break;
                        }
                        if (ch2 != 10 && ch2 != 13) {
                            bos.write(ch2);
                        }
                    }
                    dataStream = new ByteArrayInputStream(bos.toByteArray());
                }
                return new OctetStreamData(dataStream, uri.toString(), (String) null);
            } catch (IOException e) {
                throw new URIReferenceException("I/O error: " + e.getMessage(), e);
            }
        } catch (URISyntaxException e2) {
            throw new URIReferenceException("could not URL decode the uri: " + uriReference.getURI(), e2);
        }
    }

    private PackagePart findPart(URI uri) {
        LOG.log(1, "dereference", uri);
        String path = uri.getPath();
        if (path == null || "".equals(path)) {
            LOG.log(1, "illegal part name (expected)", uri);
            return null;
        }
        try {
            PackagePartName ppn = PackagingURIHelper.createPartName(path);
            return this.signatureConfig.getOpcPackage().getPart(ppn);
        } catch (InvalidFormatException e) {
            LOG.log(5, "illegal part name (not expected)", uri);
            return null;
        }
    }
}
