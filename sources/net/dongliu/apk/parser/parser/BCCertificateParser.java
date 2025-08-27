package net.dongliu.apk.parser.parser;

import java.security.Provider;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.dongliu.apk.parser.bean.CertificateMeta;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Store;
import org.bouncycastle.util.StoreException;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/parser/BCCertificateParser.class */
class BCCertificateParser extends CertificateParser {
    private static final Provider provider = new BouncyCastleProvider();

    public BCCertificateParser(byte[] data) {
        super(data);
    }

    @Override // net.dongliu.apk.parser.parser.CertificateParser
    public List<CertificateMeta> parse() throws StoreException, CertificateException, IllegalArgumentException {
        try {
            CMSSignedData cmsSignedData = new CMSSignedData(this.data);
            Store<X509CertificateHolder> certStore = cmsSignedData.getCertificates();
            SignerInformationStore signerInfos = cmsSignedData.getSignerInfos();
            Collection<SignerInformation> signers = signerInfos.getSigners();
            List<X509Certificate> certificates = new ArrayList<>();
            for (SignerInformation signer : signers) {
                Collection<X509CertificateHolder> matches = certStore.getMatches(signer.getSID());
                for (X509CertificateHolder holder : matches) {
                    certificates.add(new JcaX509CertificateConverter().setProvider(provider).getCertificate(holder));
                }
            }
            return CertificateMetas.from(certificates);
        } catch (CMSException e) {
            throw new CertificateException(e);
        }
    }
}
