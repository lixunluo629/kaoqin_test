package org.apache.poi.xwpf.usermodel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.DigestException;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.xml.namespace.QName;
import org.apache.commons.compress.compressors.bzip2.BZip2Constants;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.poifs.crypt.CryptoFunctions;
import org.apache.poi.poifs.crypt.HashAlgorithm;
import org.apache.xmlbeans.XmlOptions;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocProtect;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSettings;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTZoom;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STAlgClass;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STAlgType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STCryptProv;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STDocProtect;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.SettingsDocument;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xwpf/usermodel/XWPFSettings.class */
public class XWPFSettings extends POIXMLDocumentPart {
    private CTSettings ctSettings;

    public XWPFSettings(PackagePart part) throws IOException {
        super(part);
    }

    public XWPFSettings() {
        this.ctSettings = CTSettings.Factory.newInstance();
    }

    @Override // org.apache.poi.POIXMLDocumentPart
    protected void onDocumentRead() throws IOException {
        super.onDocumentRead();
        readFrom(getPackagePart().getInputStream());
    }

    public long getZoomPercent() {
        CTZoom zoom;
        if (!this.ctSettings.isSetZoom()) {
            zoom = this.ctSettings.addNewZoom();
        } else {
            zoom = this.ctSettings.getZoom();
        }
        BigInteger percent = zoom.getPercent();
        if (percent == null) {
            return 100L;
        }
        return percent.longValue();
    }

    public void setZoomPercent(long zoomPercent) {
        if (!this.ctSettings.isSetZoom()) {
            this.ctSettings.addNewZoom();
        }
        CTZoom zoom = this.ctSettings.getZoom();
        zoom.setPercent(BigInteger.valueOf(zoomPercent));
    }

    public boolean isEnforcedWith() {
        CTDocProtect ctDocProtect = this.ctSettings.getDocumentProtection();
        if (ctDocProtect == null) {
            return false;
        }
        return ctDocProtect.getEnforcement().equals(STOnOff.X_1);
    }

    public boolean isEnforcedWith(STDocProtect.Enum editValue) {
        CTDocProtect ctDocProtect = this.ctSettings.getDocumentProtection();
        return ctDocProtect != null && ctDocProtect.getEnforcement().equals(STOnOff.X_1) && ctDocProtect.getEdit().equals(editValue);
    }

    public void setEnforcementEditValue(STDocProtect.Enum editValue) {
        safeGetDocumentProtection().setEnforcement(STOnOff.X_1);
        safeGetDocumentProtection().setEdit(editValue);
    }

    public void setEnforcementEditValue(STDocProtect.Enum editValue, String password, HashAlgorithm hashAlgo) {
        STCryptProv.Enum providerType;
        int sid;
        safeGetDocumentProtection().setEnforcement(STOnOff.X_1);
        safeGetDocumentProtection().setEdit(editValue);
        if (password == null) {
            if (safeGetDocumentProtection().isSetCryptProviderType()) {
                safeGetDocumentProtection().unsetCryptProviderType();
            }
            if (safeGetDocumentProtection().isSetCryptAlgorithmClass()) {
                safeGetDocumentProtection().unsetCryptAlgorithmClass();
            }
            if (safeGetDocumentProtection().isSetCryptAlgorithmType()) {
                safeGetDocumentProtection().unsetCryptAlgorithmType();
            }
            if (safeGetDocumentProtection().isSetCryptAlgorithmSid()) {
                safeGetDocumentProtection().unsetCryptAlgorithmSid();
            }
            if (safeGetDocumentProtection().isSetSalt()) {
                safeGetDocumentProtection().unsetSalt();
            }
            if (safeGetDocumentProtection().isSetCryptSpinCount()) {
                safeGetDocumentProtection().unsetCryptSpinCount();
            }
            if (safeGetDocumentProtection().isSetHash()) {
                safeGetDocumentProtection().unsetHash();
                return;
            }
            return;
        }
        if (hashAlgo == null) {
            hashAlgo = HashAlgorithm.sha1;
        }
        switch (hashAlgo) {
            case md2:
                providerType = STCryptProv.RSA_FULL;
                sid = 1;
                break;
            case md4:
                providerType = STCryptProv.RSA_FULL;
                sid = 2;
                break;
            case md5:
                providerType = STCryptProv.RSA_FULL;
                sid = 3;
                break;
            case sha1:
                providerType = STCryptProv.RSA_FULL;
                sid = 4;
                break;
            case sha256:
                providerType = STCryptProv.RSA_AES;
                sid = 12;
                break;
            case sha384:
                providerType = STCryptProv.RSA_AES;
                sid = 13;
                break;
            case sha512:
                providerType = STCryptProv.RSA_AES;
                sid = 14;
                break;
            default:
                throw new EncryptedDocumentException("Hash algorithm '" + hashAlgo + "' is not supported for document write protection.");
        }
        SecureRandom random = new SecureRandom();
        byte[] salt = random.generateSeed(16);
        String legacyHash = CryptoFunctions.xorHashPasswordReversed(password);
        byte[] hash = CryptoFunctions.hashPassword(legacyHash, hashAlgo, salt, BZip2Constants.BASEBLOCKSIZE, false);
        safeGetDocumentProtection().setSalt(salt);
        safeGetDocumentProtection().setHash(hash);
        safeGetDocumentProtection().setCryptSpinCount(BigInteger.valueOf(BZip2Constants.BASEBLOCKSIZE));
        safeGetDocumentProtection().setCryptAlgorithmType(STAlgType.TYPE_ANY);
        safeGetDocumentProtection().setCryptAlgorithmClass(STAlgClass.HASH);
        safeGetDocumentProtection().setCryptProviderType(providerType);
        safeGetDocumentProtection().setCryptAlgorithmSid(BigInteger.valueOf(sid));
    }

    public boolean validateProtectionPassword(String password) throws DigestException {
        HashAlgorithm hashAlgo;
        BigInteger sid = safeGetDocumentProtection().getCryptAlgorithmSid();
        byte[] hash = safeGetDocumentProtection().getHash();
        byte[] salt = safeGetDocumentProtection().getSalt();
        BigInteger spinCount = safeGetDocumentProtection().getCryptSpinCount();
        if (sid == null || hash == null || salt == null || spinCount == null) {
            return false;
        }
        switch (sid.intValue()) {
            case 1:
                hashAlgo = HashAlgorithm.md2;
                break;
            case 2:
                hashAlgo = HashAlgorithm.md4;
                break;
            case 3:
                hashAlgo = HashAlgorithm.md5;
                break;
            case 4:
                hashAlgo = HashAlgorithm.sha1;
                break;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            default:
                return false;
            case 12:
                hashAlgo = HashAlgorithm.sha256;
                break;
            case 13:
                hashAlgo = HashAlgorithm.sha384;
                break;
            case 14:
                hashAlgo = HashAlgorithm.sha512;
                break;
        }
        String legacyHash = CryptoFunctions.xorHashPasswordReversed(password);
        byte[] hash2 = CryptoFunctions.hashPassword(legacyHash, hashAlgo, salt, spinCount.intValue(), false);
        return Arrays.equals(hash, hash2);
    }

    public void removeEnforcement() {
        safeGetDocumentProtection().setEnforcement(STOnOff.X_0);
    }

    public void setUpdateFields() {
        CTOnOff onOff = CTOnOff.Factory.newInstance();
        onOff.setVal(STOnOff.TRUE);
        this.ctSettings.setUpdateFields(onOff);
    }

    boolean isUpdateFields() {
        return this.ctSettings.isSetUpdateFields() && this.ctSettings.getUpdateFields().getVal() == STOnOff.TRUE;
    }

    public boolean isTrackRevisions() {
        return this.ctSettings.isSetTrackRevisions();
    }

    public void setTrackRevisions(boolean enable) {
        if (enable) {
            if (!this.ctSettings.isSetTrackRevisions()) {
                this.ctSettings.addNewTrackRevisions();
            }
        } else if (this.ctSettings.isSetTrackRevisions()) {
            this.ctSettings.unsetTrackRevisions();
        }
    }

    @Override // org.apache.poi.POIXMLDocumentPart
    protected void commit() throws IOException {
        if (this.ctSettings == null) {
            throw new IllegalStateException("Unable to write out settings that were never read in!");
        }
        XmlOptions xmlOptions = new XmlOptions(POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
        xmlOptions.setSaveSyntheticDocumentElement(new QName(CTSettings.type.getName().getNamespaceURI(), "settings"));
        PackagePart part = getPackagePart();
        OutputStream out = part.getOutputStream();
        this.ctSettings.save(out, xmlOptions);
        out.close();
    }

    private CTDocProtect safeGetDocumentProtection() {
        CTDocProtect documentProtection = this.ctSettings.getDocumentProtection();
        if (documentProtection == null) {
            CTDocProtect documentProtection2 = CTDocProtect.Factory.newInstance();
            this.ctSettings.setDocumentProtection(documentProtection2);
        }
        return this.ctSettings.getDocumentProtection();
    }

    private void readFrom(InputStream inputStream) {
        try {
            this.ctSettings = SettingsDocument.Factory.parse(inputStream, POIXMLTypeLoader.DEFAULT_XML_OPTIONS).getSettings();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
