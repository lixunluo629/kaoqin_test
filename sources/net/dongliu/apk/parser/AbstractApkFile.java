package net.dongliu.apk.parser;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.dongliu.apk.parser.bean.ApkMeta;
import net.dongliu.apk.parser.bean.ApkSignStatus;
import net.dongliu.apk.parser.bean.ApkSigner;
import net.dongliu.apk.parser.bean.ApkV2Signer;
import net.dongliu.apk.parser.bean.CertificateMeta;
import net.dongliu.apk.parser.bean.DexClass;
import net.dongliu.apk.parser.bean.Icon;
import net.dongliu.apk.parser.bean.IconPath;
import net.dongliu.apk.parser.exception.ParserException;
import net.dongliu.apk.parser.parser.ApkMetaTranslator;
import net.dongliu.apk.parser.parser.ApkSignBlockParser;
import net.dongliu.apk.parser.parser.BinaryXmlParser;
import net.dongliu.apk.parser.parser.CertificateMetas;
import net.dongliu.apk.parser.parser.CertificateParser;
import net.dongliu.apk.parser.parser.CompositeXmlStreamer;
import net.dongliu.apk.parser.parser.DexParser;
import net.dongliu.apk.parser.parser.ResourceTableParser;
import net.dongliu.apk.parser.parser.XmlStreamer;
import net.dongliu.apk.parser.parser.XmlTranslator;
import net.dongliu.apk.parser.struct.AndroidConstants;
import net.dongliu.apk.parser.struct.resource.ResourceTable;
import net.dongliu.apk.parser.struct.signingv2.ApkSigningBlock;
import net.dongliu.apk.parser.struct.signingv2.SignerBlock;
import net.dongliu.apk.parser.struct.zip.EOCD;
import net.dongliu.apk.parser.utils.Buffers;
import net.dongliu.apk.parser.utils.Unsigned;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/AbstractApkFile.class */
public abstract class AbstractApkFile implements Closeable {
    private DexClass[] dexClasses;
    private boolean resourceTableParsed;
    private ResourceTable resourceTable;
    private Set<Locale> locales;
    private boolean manifestParsed;
    private String manifestXml;
    private ApkMeta apkMeta;
    private List<IconPath> iconPaths;
    private List<ApkSigner> apkSigners;
    private List<ApkV2Signer> apkV2Signers;
    private static final Locale DEFAULT_LOCALE = Locale.US;
    private Locale preferredLocale = DEFAULT_LOCALE;

    protected abstract List<CertificateFile> getAllCertificateData() throws IOException;

    public abstract byte[] getFileData(String str) throws IOException;

    protected abstract ByteBuffer fileData() throws IOException;

    @Deprecated
    public abstract ApkSignStatus verifyApk() throws IOException;

    public String getManifestXml() throws IOException {
        parseManifest();
        return this.manifestXml;
    }

    public ApkMeta getApkMeta() throws IOException {
        parseManifest();
        return this.apkMeta;
    }

    public Set<Locale> getLocales() throws IOException {
        parseResourceTable();
        return this.locales;
    }

    @Deprecated
    public List<CertificateMeta> getCertificateMetaList() throws IOException, CertificateException {
        if (this.apkSigners == null) {
            parseCertificates();
        }
        if (this.apkSigners.isEmpty()) {
            throw new ParserException("ApkFile certificate not found");
        }
        return this.apkSigners.get(0).getCertificateMetas();
    }

    @Deprecated
    public Map<String, List<CertificateMeta>> getAllCertificateMetas() throws IOException, CertificateException {
        List<ApkSigner> apkSigners = getApkSingers();
        Map<String, List<CertificateMeta>> map = new LinkedHashMap<>();
        for (ApkSigner apkSigner : apkSigners) {
            map.put(apkSigner.getPath(), apkSigner.getCertificateMetas());
        }
        return map;
    }

    public List<ApkSigner> getApkSingers() throws IOException, CertificateException {
        if (this.apkSigners == null) {
            parseCertificates();
        }
        return this.apkSigners;
    }

    private void parseCertificates() throws IOException, CertificateException {
        this.apkSigners = new ArrayList();
        for (CertificateFile file : getAllCertificateData()) {
            CertificateParser parser = CertificateParser.getInstance(file.getData());
            List<CertificateMeta> certificateMetas = parser.parse();
            this.apkSigners.add(new ApkSigner(file.getPath(), certificateMetas));
        }
    }

    public List<ApkV2Signer> getApkV2Singers() throws IOException, CertificateException {
        if (this.apkV2Signers == null) {
            parseApkSigningBlock();
        }
        return this.apkV2Signers;
    }

    private void parseApkSigningBlock() throws IOException, CertificateException {
        List<ApkV2Signer> list = new ArrayList<>();
        ByteBuffer apkSignBlockBuf = findApkSignBlock();
        if (apkSignBlockBuf != null) {
            ApkSignBlockParser parser = new ApkSignBlockParser(apkSignBlockBuf);
            ApkSigningBlock apkSigningBlock = parser.parse();
            for (SignerBlock signerBlock : apkSigningBlock.getSignerBlocks()) {
                List<X509Certificate> certificates = signerBlock.getCertificates();
                List<CertificateMeta> certificateMetas = CertificateMetas.from(certificates);
                ApkV2Signer apkV2Signer = new ApkV2Signer(certificateMetas);
                list.add(apkV2Signer);
            }
        }
        this.apkV2Signers = list;
    }

    /* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/AbstractApkFile$CertificateFile.class */
    protected static class CertificateFile {
        private String path;
        private byte[] data;

        public CertificateFile(String path, byte[] data) {
            this.path = path;
            this.data = data;
        }

        public String getPath() {
            return this.path;
        }

        public byte[] getData() {
            return this.data;
        }
    }

    private void parseManifest() throws IOException {
        if (this.manifestParsed) {
            return;
        }
        parseResourceTable();
        XmlTranslator xmlTranslator = new XmlTranslator();
        ApkMetaTranslator apkTranslator = new ApkMetaTranslator(this.resourceTable, this.preferredLocale);
        XmlStreamer xmlStreamer = new CompositeXmlStreamer(xmlTranslator, apkTranslator);
        byte[] data = getFileData(AndroidConstants.MANIFEST_FILE);
        if (data == null) {
            throw new ParserException("Manifest file not found");
        }
        transBinaryXml(data, xmlStreamer);
        this.manifestXml = xmlTranslator.getXml();
        this.apkMeta = apkTranslator.getApkMeta();
        this.iconPaths = apkTranslator.getIconPaths();
        this.manifestParsed = true;
    }

    public String transBinaryXml(String path) throws IOException {
        byte[] data = getFileData(path);
        if (data == null) {
            return null;
        }
        parseResourceTable();
        XmlTranslator xmlTranslator = new XmlTranslator();
        transBinaryXml(data, xmlTranslator);
        return xmlTranslator.getXml();
    }

    private void transBinaryXml(byte[] data, XmlStreamer xmlStreamer) throws IOException {
        parseResourceTable();
        ByteBuffer buffer = ByteBuffer.wrap(data);
        BinaryXmlParser binaryXmlParser = new BinaryXmlParser(buffer, this.resourceTable);
        binaryXmlParser.setLocale(this.preferredLocale);
        binaryXmlParser.setXmlStreamer(xmlStreamer);
        binaryXmlParser.parse();
    }

    public Icon getIconFile() throws IOException {
        ApkMeta apkMeta = getApkMeta();
        String iconPath = apkMeta.getIcon();
        if (iconPath == null) {
            return null;
        }
        return new Icon(iconPath, 0, getFileData(iconPath));
    }

    public List<IconPath> getIconPaths() throws IOException {
        parseManifest();
        return this.iconPaths;
    }

    public List<Icon> getIconFiles() throws IOException {
        List<IconPath> iconPaths = getIconPaths();
        List<Icon> icons = new ArrayList<>(iconPaths.size());
        for (IconPath iconPath : iconPaths) {
            Icon icon = new Icon(iconPath.getPath(), iconPath.getDensity(), getFileData(iconPath.getPath()));
            icons.add(icon);
        }
        return icons;
    }

    public DexClass[] getDexClasses() throws IOException {
        if (this.dexClasses == null) {
            parseDexFiles();
        }
        return this.dexClasses;
    }

    private DexClass[] mergeDexClasses(DexClass[] first, DexClass[] second) {
        DexClass[] result = new DexClass[first.length + second.length];
        System.arraycopy(first, 0, result, 0, first.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    private DexClass[] parseDexFile(String path) throws IOException {
        byte[] data = getFileData(path);
        if (data == null) {
            String msg = String.format("Dex file %s not found", path);
            throw new ParserException(msg);
        }
        ByteBuffer buffer = ByteBuffer.wrap(data);
        DexParser dexParser = new DexParser(buffer);
        return dexParser.parse();
    }

    private void parseDexFiles() throws IOException {
        this.dexClasses = parseDexFile(AndroidConstants.DEX_FILE);
        for (int i = 2; i < 1000; i++) {
            String path = String.format(AndroidConstants.DEX_ADDITIONAL, Integer.valueOf(i));
            try {
                DexClass[] classes = parseDexFile(path);
                this.dexClasses = mergeDexClasses(this.dexClasses, classes);
            } catch (ParserException e) {
                return;
            }
        }
    }

    private void parseResourceTable() throws IOException {
        if (this.resourceTableParsed) {
            return;
        }
        this.resourceTableParsed = true;
        byte[] data = getFileData(AndroidConstants.RESOURCE_FILE);
        if (data == null) {
            this.resourceTable = new ResourceTable();
            this.locales = Collections.emptySet();
            return;
        }
        ByteBuffer buffer = ByteBuffer.wrap(data);
        ResourceTableParser resourceTableParser = new ResourceTableParser(buffer);
        resourceTableParser.parse();
        this.resourceTable = resourceTableParser.getResourceTable();
        this.locales = resourceTableParser.getLocales();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.apkSigners = null;
        this.resourceTable = null;
        this.iconPaths = null;
    }

    public Locale getPreferredLocale() {
        return this.preferredLocale;
    }

    public void setPreferredLocale(Locale preferredLocale) {
        if (!Objects.equals(this.preferredLocale, preferredLocale)) {
            this.preferredLocale = preferredLocale;
            this.manifestXml = null;
            this.apkMeta = null;
        }
    }

    protected ByteBuffer findApkSignBlock() throws IOException {
        ByteBuffer buffer = fileData().order(ByteOrder.LITTLE_ENDIAN);
        int len = buffer.limit();
        if (len < 22) {
            throw new RuntimeException("Not zip file");
        }
        EOCD eocd = null;
        for (int i = len - 22; i > Math.max(0, len - 102400); i--) {
            int v = buffer.getInt(i);
            if (v == 101010256) {
                Buffers.position(buffer, i + 4);
                eocd = new EOCD();
                eocd.setDiskNum(Buffers.readUShort(buffer));
                eocd.setCdStartDisk(Buffers.readUShort(buffer));
                eocd.setCdRecordNum(Buffers.readUShort(buffer));
                eocd.setTotalCDRecordNum(Buffers.readUShort(buffer));
                eocd.setCdSize(Buffers.readUInt(buffer));
                eocd.setCdStart(Buffers.readUInt(buffer));
                eocd.setCommentLen(Buffers.readUShort(buffer));
            }
        }
        if (eocd == null) {
            return null;
        }
        long cdStart = eocd.getCdStart();
        Buffers.position(buffer, cdStart - 16);
        String magic = Buffers.readAsciiString(buffer, 16);
        if (!magic.equals(ApkSigningBlock.MAGIC)) {
            return null;
        }
        Buffers.position(buffer, cdStart - 24);
        int blockSize = Unsigned.ensureUInt(buffer.getLong());
        Buffers.position(buffer, (cdStart - blockSize) - 8);
        long size2 = Unsigned.ensureULong(buffer.getLong());
        if (blockSize != size2) {
            return null;
        }
        return Buffers.sliceAndSkip(buffer, blockSize - 16);
    }
}
