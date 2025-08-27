package org.apache.coyote.http11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpUpgradeHandler;
import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.CompressionConfig;
import org.apache.coyote.Processor;
import org.apache.coyote.UpgradeProtocol;
import org.apache.coyote.UpgradeToken;
import org.apache.coyote.http11.upgrade.InternalHttpUpgradeHandler;
import org.apache.coyote.http11.upgrade.UpgradeProcessorExternal;
import org.apache.coyote.http11.upgrade.UpgradeProcessorInternal;
import org.apache.tomcat.util.buf.StringUtils;
import org.apache.tomcat.util.net.AbstractEndpoint;
import org.apache.tomcat.util.net.SSLHostConfig;
import org.apache.tomcat.util.net.SocketWrapperBase;
import org.apache.tomcat.util.res.StringManager;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http11/AbstractHttp11Protocol.class */
public abstract class AbstractHttp11Protocol<S> extends AbstractProtocol<S> {
    protected static final StringManager sm = StringManager.getManager((Class<?>) AbstractHttp11Protocol.class);
    private final CompressionConfig compressionConfig;
    private String relaxedPathChars;
    private String relaxedQueryChars;
    private boolean allowHostHeaderMismatch;
    private boolean rejectIllegalHeaderName;
    private int maxSavePostSize;
    private int maxHttpHeaderSize;
    private int connectionUploadTimeout;
    private boolean disableUploadTimeout;
    private String restrictedUserAgents;
    private String server;
    private boolean serverRemoveAppProvidedValues;
    private int maxTrailerSize;
    private int maxExtensionSize;
    private int maxSwallowSize;
    private boolean secure;
    private Set<String> allowedTrailerHeaders;
    private final List<UpgradeProtocol> upgradeProtocols;
    private final Map<String, UpgradeProtocol> httpUpgradeProtocols;
    private final Map<String, UpgradeProtocol> negotiatedProtocols;
    private SSLHostConfig defaultSSLHostConfig;

    public AbstractHttp11Protocol(AbstractEndpoint<S> endpoint) {
        super(endpoint);
        this.compressionConfig = new CompressionConfig();
        this.relaxedPathChars = null;
        this.relaxedQueryChars = null;
        this.allowHostHeaderMismatch = true;
        this.rejectIllegalHeaderName = false;
        this.maxSavePostSize = 4096;
        this.maxHttpHeaderSize = 8192;
        this.connectionUploadTimeout = 300000;
        this.disableUploadTimeout = true;
        this.restrictedUserAgents = null;
        this.serverRemoveAppProvidedValues = false;
        this.maxTrailerSize = 8192;
        this.maxExtensionSize = 8192;
        this.maxSwallowSize = 2097152;
        this.allowedTrailerHeaders = Collections.newSetFromMap(new ConcurrentHashMap());
        this.upgradeProtocols = new ArrayList();
        this.httpUpgradeProtocols = new HashMap();
        this.negotiatedProtocols = new HashMap();
        this.defaultSSLHostConfig = null;
        setConnectionTimeout(60000);
        AbstractProtocol.ConnectionHandler<S> cHandler = new AbstractProtocol.ConnectionHandler<>(this);
        setHandler(cHandler);
        getEndpoint().setHandler(cHandler);
    }

    @Override // org.apache.coyote.AbstractProtocol, org.apache.coyote.ProtocolHandler
    public void init() throws Exception {
        for (UpgradeProtocol upgradeProtocol : this.upgradeProtocols) {
            configureUpgradeProtocol(upgradeProtocol);
        }
        super.init();
    }

    @Override // org.apache.coyote.AbstractProtocol
    protected String getProtocolName() {
        return "Http";
    }

    @Override // org.apache.coyote.AbstractProtocol
    protected AbstractEndpoint<S> getEndpoint() {
        return super.getEndpoint();
    }

    public String getRelaxedPathChars() {
        return this.relaxedPathChars;
    }

    public void setRelaxedPathChars(String relaxedPathChars) {
        this.relaxedPathChars = relaxedPathChars;
    }

    public String getRelaxedQueryChars() {
        return this.relaxedQueryChars;
    }

    public void setRelaxedQueryChars(String relaxedQueryChars) {
        this.relaxedQueryChars = relaxedQueryChars;
    }

    public boolean getAllowHostHeaderMismatch() {
        return this.allowHostHeaderMismatch;
    }

    public void setAllowHostHeaderMismatch(boolean allowHostHeaderMismatch) {
        this.allowHostHeaderMismatch = allowHostHeaderMismatch;
    }

    public boolean getRejectIllegalHeaderName() {
        return this.rejectIllegalHeaderName;
    }

    public void setRejectIllegalHeaderName(boolean rejectIllegalHeaderName) {
        this.rejectIllegalHeaderName = rejectIllegalHeaderName;
    }

    public int getMaxSavePostSize() {
        return this.maxSavePostSize;
    }

    public void setMaxSavePostSize(int valueI) {
        this.maxSavePostSize = valueI;
    }

    public int getMaxHttpHeaderSize() {
        return this.maxHttpHeaderSize;
    }

    public void setMaxHttpHeaderSize(int valueI) {
        this.maxHttpHeaderSize = valueI;
    }

    public int getConnectionUploadTimeout() {
        return this.connectionUploadTimeout;
    }

    public void setConnectionUploadTimeout(int i) {
        this.connectionUploadTimeout = i;
    }

    public boolean getDisableUploadTimeout() {
        return this.disableUploadTimeout;
    }

    public void setDisableUploadTimeout(boolean isDisabled) {
        this.disableUploadTimeout = isDisabled;
    }

    public String getCompression() {
        return this.compressionConfig.getCompression();
    }

    public void setCompression(String valueS) {
        this.compressionConfig.setCompression(valueS);
    }

    public String getNoCompressionUserAgents() {
        return this.compressionConfig.getNoCompressionUserAgents();
    }

    public void setNoCompressionUserAgents(String valueS) {
        this.compressionConfig.setNoCompressionUserAgents(valueS);
    }

    @Deprecated
    public String getCompressableMimeType() {
        return getCompressibleMimeType();
    }

    @Deprecated
    public void setCompressableMimeType(String valueS) {
        setCompressibleMimeType(valueS);
    }

    @Deprecated
    public String[] getCompressableMimeTypes() {
        return getCompressibleMimeTypes();
    }

    public String getCompressibleMimeType() {
        return this.compressionConfig.getCompressibleMimeType();
    }

    public void setCompressibleMimeType(String valueS) {
        this.compressionConfig.setCompressibleMimeType(valueS);
    }

    public String[] getCompressibleMimeTypes() {
        return this.compressionConfig.getCompressibleMimeTypes();
    }

    public int getCompressionMinSize() {
        return this.compressionConfig.getCompressionMinSize();
    }

    public void setCompressionMinSize(int valueI) {
        this.compressionConfig.setCompressionMinSize(valueI);
    }

    public String getRestrictedUserAgents() {
        return this.restrictedUserAgents;
    }

    public void setRestrictedUserAgents(String valueS) {
        this.restrictedUserAgents = valueS;
    }

    public String getServer() {
        return this.server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public boolean getServerRemoveAppProvidedValues() {
        return this.serverRemoveAppProvidedValues;
    }

    public void setServerRemoveAppProvidedValues(boolean serverRemoveAppProvidedValues) {
        this.serverRemoveAppProvidedValues = serverRemoveAppProvidedValues;
    }

    public int getMaxTrailerSize() {
        return this.maxTrailerSize;
    }

    public void setMaxTrailerSize(int maxTrailerSize) {
        this.maxTrailerSize = maxTrailerSize;
    }

    public int getMaxExtensionSize() {
        return this.maxExtensionSize;
    }

    public void setMaxExtensionSize(int maxExtensionSize) {
        this.maxExtensionSize = maxExtensionSize;
    }

    public int getMaxSwallowSize() {
        return this.maxSwallowSize;
    }

    public void setMaxSwallowSize(int maxSwallowSize) {
        this.maxSwallowSize = maxSwallowSize;
    }

    public boolean getSecure() {
        return this.secure;
    }

    public void setSecure(boolean b) {
        this.secure = b;
    }

    public void setAllowedTrailerHeaders(String commaSeparatedHeaders) {
        Set<String> toRemove = new HashSet<>();
        toRemove.addAll(this.allowedTrailerHeaders);
        if (commaSeparatedHeaders != null) {
            String[] headers = commaSeparatedHeaders.split(",");
            for (String header : headers) {
                String trimmedHeader = header.trim().toLowerCase(Locale.ENGLISH);
                if (toRemove.contains(trimmedHeader)) {
                    toRemove.remove(trimmedHeader);
                } else {
                    this.allowedTrailerHeaders.add(trimmedHeader);
                }
            }
            this.allowedTrailerHeaders.removeAll(toRemove);
        }
    }

    public String getAllowedTrailerHeaders() {
        List<String> copy = new ArrayList<>(this.allowedTrailerHeaders.size());
        copy.addAll(this.allowedTrailerHeaders);
        return StringUtils.join(copy);
    }

    public void addAllowedTrailerHeader(String header) {
        if (header != null) {
            this.allowedTrailerHeaders.add(header.trim().toLowerCase(Locale.ENGLISH));
        }
    }

    public void removeAllowedTrailerHeader(String header) {
        if (header != null) {
            this.allowedTrailerHeaders.remove(header.trim().toLowerCase(Locale.ENGLISH));
        }
    }

    @Override // org.apache.coyote.ProtocolHandler
    public void addUpgradeProtocol(UpgradeProtocol upgradeProtocol) {
        this.upgradeProtocols.add(upgradeProtocol);
    }

    @Override // org.apache.coyote.ProtocolHandler
    public UpgradeProtocol[] findUpgradeProtocols() {
        return (UpgradeProtocol[]) this.upgradeProtocols.toArray(new UpgradeProtocol[0]);
    }

    private void configureUpgradeProtocol(UpgradeProtocol upgradeProtocol) {
        String httpUpgradeName = upgradeProtocol.getHttpUpgradeName(getEndpoint().isSSLEnabled());
        boolean httpUpgradeConfigured = false;
        if (httpUpgradeName != null && httpUpgradeName.length() > 0) {
            this.httpUpgradeProtocols.put(httpUpgradeName, upgradeProtocol);
            httpUpgradeConfigured = true;
            getLog().info(sm.getString("abstractHttp11Protocol.httpUpgradeConfigured", getName(), httpUpgradeName));
        }
        String alpnName = upgradeProtocol.getAlpnName();
        if (alpnName != null && alpnName.length() > 0) {
            if (getEndpoint().isAlpnSupported()) {
                this.negotiatedProtocols.put(alpnName, upgradeProtocol);
                getEndpoint().addNegotiatedProtocol(alpnName);
                getLog().info(sm.getString("abstractHttp11Protocol.alpnConfigured", getName(), alpnName));
            } else if (!httpUpgradeConfigured) {
                getLog().error(sm.getString("abstractHttp11Protocol.alpnWithNoAlpn", upgradeProtocol.getClass().getName(), alpnName, getName()));
            }
        }
    }

    @Override // org.apache.coyote.AbstractProtocol
    public UpgradeProtocol getNegotiatedProtocol(String negotiatedName) {
        return this.negotiatedProtocols.get(negotiatedName);
    }

    @Override // org.apache.coyote.AbstractProtocol
    public UpgradeProtocol getUpgradeProtocol(String upgradedName) {
        return this.httpUpgradeProtocols.get(upgradedName);
    }

    public boolean isSSLEnabled() {
        return getEndpoint().isSSLEnabled();
    }

    public void setSSLEnabled(boolean SSLEnabled) {
        getEndpoint().setSSLEnabled(SSLEnabled);
    }

    public boolean getUseSendfile() {
        return getEndpoint().getUseSendfile();
    }

    public void setUseSendfile(boolean useSendfile) {
        getEndpoint().setUseSendfile(useSendfile);
    }

    public int getMaxKeepAliveRequests() {
        return getEndpoint().getMaxKeepAliveRequests();
    }

    public void setMaxKeepAliveRequests(int mkar) {
        getEndpoint().setMaxKeepAliveRequests(mkar);
    }

    public String getDefaultSSLHostConfigName() {
        return getEndpoint().getDefaultSSLHostConfigName();
    }

    public void setDefaultSSLHostConfigName(String defaultSSLHostConfigName) {
        getEndpoint().setDefaultSSLHostConfigName(defaultSSLHostConfigName);
        if (this.defaultSSLHostConfig != null) {
            this.defaultSSLHostConfig.setHostName(defaultSSLHostConfigName);
        }
    }

    @Override // org.apache.coyote.ProtocolHandler
    public void addSslHostConfig(SSLHostConfig sslHostConfig) throws IllegalArgumentException {
        getEndpoint().addSslHostConfig(sslHostConfig);
    }

    @Override // org.apache.coyote.ProtocolHandler
    public SSLHostConfig[] findSslHostConfigs() {
        return getEndpoint().findSslHostConfigs();
    }

    public void reloadSslHostConfigs() throws IllegalArgumentException {
        getEndpoint().reloadSslHostConfigs();
    }

    public void reloadSslHostConfig(String hostName) throws IllegalArgumentException {
        getEndpoint().reloadSslHostConfig(hostName);
    }

    private void registerDefaultSSLHostConfig() throws IllegalArgumentException {
        if (this.defaultSSLHostConfig == null) {
            SSLHostConfig[] arr$ = findSslHostConfigs();
            int len$ = arr$.length;
            int i$ = 0;
            while (true) {
                if (i$ >= len$) {
                    break;
                }
                SSLHostConfig sslHostConfig = arr$[i$];
                if (!getDefaultSSLHostConfigName().equals(sslHostConfig.getHostName())) {
                    i$++;
                } else {
                    this.defaultSSLHostConfig = sslHostConfig;
                    break;
                }
            }
            if (this.defaultSSLHostConfig == null) {
                this.defaultSSLHostConfig = new SSLHostConfig();
                this.defaultSSLHostConfig.setHostName(getDefaultSSLHostConfigName());
                getEndpoint().addSslHostConfig(this.defaultSSLHostConfig);
            }
        }
    }

    public String getSslEnabledProtocols() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return StringUtils.join(this.defaultSSLHostConfig.getEnabledProtocols());
    }

    public void setSslEnabledProtocols(String enabledProtocols) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setProtocols(enabledProtocols);
    }

    public String getSSLProtocol() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return StringUtils.join(this.defaultSSLHostConfig.getEnabledProtocols());
    }

    public void setSSLProtocol(String sslProtocol) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setProtocols(sslProtocol);
    }

    public String getKeystoreFile() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getCertificateKeystoreFile();
    }

    public void setKeystoreFile(String keystoreFile) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setCertificateKeystoreFile(keystoreFile);
    }

    public String getSSLCertificateChainFile() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getCertificateChainFile();
    }

    public void setSSLCertificateChainFile(String certificateChainFile) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setCertificateChainFile(certificateChainFile);
    }

    public String getSSLCertificateFile() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getCertificateFile();
    }

    public void setSSLCertificateFile(String certificateFile) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setCertificateFile(certificateFile);
    }

    public String getSSLCertificateKeyFile() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getCertificateKeyFile();
    }

    public void setSSLCertificateKeyFile(String certificateKeyFile) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setCertificateKeyFile(certificateKeyFile);
    }

    public String getAlgorithm() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getKeyManagerAlgorithm();
    }

    public void setAlgorithm(String keyManagerAlgorithm) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setKeyManagerAlgorithm(keyManagerAlgorithm);
    }

    public String getClientAuth() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getCertificateVerificationAsString();
    }

    public void setClientAuth(String certificateVerification) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setCertificateVerification(certificateVerification);
    }

    public String getSSLVerifyClient() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getCertificateVerificationAsString();
    }

    public void setSSLVerifyClient(String certificateVerification) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setCertificateVerification(certificateVerification);
    }

    public int getTrustMaxCertLength() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getCertificateVerificationDepth();
    }

    public void setTrustMaxCertLength(int certificateVerificationDepth) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setCertificateVerificationDepth(certificateVerificationDepth);
    }

    public int getSSLVerifyDepth() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getCertificateVerificationDepth();
    }

    public void setSSLVerifyDepth(int certificateVerificationDepth) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setCertificateVerificationDepth(certificateVerificationDepth);
    }

    public String getUseServerCipherSuitesOrder() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getHonorCipherOrder();
    }

    public void setUseServerCipherSuitesOrder(String honorCipherOrder) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setHonorCipherOrder(honorCipherOrder);
    }

    public String getSSLHonorCipherOrder() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getHonorCipherOrder();
    }

    public void setSSLHonorCipherOrder(String honorCipherOrder) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setHonorCipherOrder(honorCipherOrder);
    }

    public String getCiphers() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getCiphers();
    }

    public void setCiphers(String ciphers) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setCiphers(ciphers);
    }

    public String getSSLCipherSuite() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getCiphers();
    }

    public void setSSLCipherSuite(String ciphers) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setCiphers(ciphers);
    }

    public String getKeystorePass() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getCertificateKeystorePassword();
    }

    public void setKeystorePass(String certificateKeystorePassword) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setCertificateKeystorePassword(certificateKeystorePassword);
    }

    public String getKeyPass() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getCertificateKeyPassword();
    }

    public void setKeyPass(String certificateKeyPassword) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setCertificateKeyPassword(certificateKeyPassword);
    }

    public String getSSLPassword() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getCertificateKeyPassword();
    }

    public void setSSLPassword(String certificateKeyPassword) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setCertificateKeyPassword(certificateKeyPassword);
    }

    public String getCrlFile() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getCertificateRevocationListFile();
    }

    public void setCrlFile(String certificateRevocationListFile) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setCertificateRevocationListFile(certificateRevocationListFile);
    }

    public String getSSLCARevocationFile() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getCertificateRevocationListFile();
    }

    public void setSSLCARevocationFile(String certificateRevocationListFile) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setCertificateRevocationListFile(certificateRevocationListFile);
    }

    public String getSSLCARevocationPath() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getCertificateRevocationListPath();
    }

    public void setSSLCARevocationPath(String certificateRevocationListPath) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setCertificateRevocationListPath(certificateRevocationListPath);
    }

    public String getKeystoreType() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getCertificateKeystoreType();
    }

    public void setKeystoreType(String certificateKeystoreType) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setCertificateKeystoreType(certificateKeystoreType);
    }

    public String getKeystoreProvider() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getCertificateKeystoreProvider();
    }

    public void setKeystoreProvider(String certificateKeystoreProvider) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setCertificateKeystoreProvider(certificateKeystoreProvider);
    }

    public String getKeyAlias() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getCertificateKeyAlias();
    }

    public void setKeyAlias(String certificateKeyAlias) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setCertificateKeyAlias(certificateKeyAlias);
    }

    public String getTruststoreAlgorithm() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getTruststoreAlgorithm();
    }

    public void setTruststoreAlgorithm(String truststoreAlgorithm) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setTruststoreAlgorithm(truststoreAlgorithm);
    }

    public String getTruststoreFile() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getTruststoreFile();
    }

    public void setTruststoreFile(String truststoreFile) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setTruststoreFile(truststoreFile);
    }

    public String getTruststorePass() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getTruststorePassword();
    }

    public void setTruststorePass(String truststorePassword) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setTruststorePassword(truststorePassword);
    }

    public String getTruststoreType() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getTruststoreType();
    }

    public void setTruststoreType(String truststoreType) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setTruststoreType(truststoreType);
    }

    public String getTruststoreProvider() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getTruststoreProvider();
    }

    public void setTruststoreProvider(String truststoreProvider) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setTruststoreProvider(truststoreProvider);
    }

    public String getSslProtocol() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getSslProtocol();
    }

    public void setSslProtocol(String sslProtocol) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setSslProtocol(sslProtocol);
    }

    public int getSessionCacheSize() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getSessionCacheSize();
    }

    public void setSessionCacheSize(int sessionCacheSize) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setSessionCacheSize(sessionCacheSize);
    }

    public int getSessionTimeout() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getSessionTimeout();
    }

    public void setSessionTimeout(int sessionTimeout) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setSessionTimeout(sessionTimeout);
    }

    public String getSSLCACertificatePath() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getCaCertificatePath();
    }

    public void setSSLCACertificatePath(String caCertificatePath) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setCaCertificatePath(caCertificatePath);
    }

    public String getSSLCACertificateFile() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getCaCertificateFile();
    }

    public void setSSLCACertificateFile(String caCertificateFile) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setCaCertificateFile(caCertificateFile);
    }

    public boolean getSSLDisableCompression() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getDisableCompression();
    }

    public void setSSLDisableCompression(boolean disableCompression) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setDisableCompression(disableCompression);
    }

    public boolean getSSLDisableSessionTickets() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getDisableSessionTickets();
    }

    public void setSSLDisableSessionTickets(boolean disableSessionTickets) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setDisableSessionTickets(disableSessionTickets);
    }

    public String getTrustManagerClassName() throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        return this.defaultSSLHostConfig.getTrustManagerClassName();
    }

    public void setTrustManagerClassName(String trustManagerClassName) throws IllegalArgumentException {
        registerDefaultSSLHostConfig();
        this.defaultSSLHostConfig.setTrustManagerClassName(trustManagerClassName);
    }

    @Override // org.apache.coyote.AbstractProtocol
    protected Processor createProcessor() {
        Http11Processor processor = new Http11Processor(getMaxHttpHeaderSize(), getAllowHostHeaderMismatch(), getRejectIllegalHeaderName(), getEndpoint(), getMaxTrailerSize(), this.allowedTrailerHeaders, getMaxExtensionSize(), getMaxSwallowSize(), this.httpUpgradeProtocols, getSendReasonPhrase(), this.relaxedPathChars, this.relaxedQueryChars);
        processor.setAdapter(getAdapter());
        processor.setMaxKeepAliveRequests(getMaxKeepAliveRequests());
        processor.setConnectionUploadTimeout(getConnectionUploadTimeout());
        processor.setDisableUploadTimeout(getDisableUploadTimeout());
        processor.setCompressionMinSize(getCompressionMinSize());
        processor.setCompression(getCompression());
        processor.setNoCompressionUserAgents(getNoCompressionUserAgents());
        processor.setCompressibleMimeTypes(getCompressibleMimeTypes());
        processor.setRestrictedUserAgents(getRestrictedUserAgents());
        processor.setMaxSavePostSize(getMaxSavePostSize());
        processor.setServer(getServer());
        processor.setServerRemoveAppProvidedValues(getServerRemoveAppProvidedValues());
        return processor;
    }

    @Override // org.apache.coyote.AbstractProtocol
    protected Processor createUpgradeProcessor(SocketWrapperBase<?> socket, UpgradeToken upgradeToken) {
        HttpUpgradeHandler httpUpgradeHandler = upgradeToken.getHttpUpgradeHandler();
        if (httpUpgradeHandler instanceof InternalHttpUpgradeHandler) {
            return new UpgradeProcessorInternal(socket, upgradeToken);
        }
        return new UpgradeProcessorExternal(socket, upgradeToken);
    }
}
