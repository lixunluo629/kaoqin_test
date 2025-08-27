package org.bouncycastle.x509;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXParameters;
import java.security.cert.PolicyNode;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DEREnumerated;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.x509.AccessDescription;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.AuthorityInformationAccess;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.GeneralSubtree;
import org.bouncycastle.asn1.x509.IssuingDistributionPoint;
import org.bouncycastle.asn1.x509.NameConstraints;
import org.bouncycastle.asn1.x509.PolicyInformation;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.qualified.MonetaryValue;
import org.bouncycastle.asn1.x509.qualified.QCStatement;
import org.bouncycastle.i18n.ErrorBundle;
import org.bouncycastle.i18n.LocaleString;
import org.bouncycastle.i18n.filter.TrustedInput;
import org.bouncycastle.i18n.filter.UntrustedInput;
import org.bouncycastle.i18n.filter.UntrustedUrlInput;
import org.bouncycastle.jce.provider.AnnotatedException;
import org.bouncycastle.jce.provider.PKIXNameConstraintValidator;
import org.bouncycastle.jce.provider.PKIXNameConstraintValidatorException;
import org.bouncycastle.jce.provider.PKIXPolicyNode;
import org.bouncycastle.x509.extension.X509ExtensionUtil;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/x509/PKIXCertPathReviewer.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/x509/PKIXCertPathReviewer.class */
public class PKIXCertPathReviewer extends org.bouncycastle.jce.provider.CertPathValidatorUtilities {
    private static final String QC_STATEMENT = X509Extensions.QCStatements.getId();
    private static final String CRL_DIST_POINTS = X509Extensions.CRLDistributionPoints.getId();
    private static final String AUTH_INFO_ACCESS = X509Extensions.AuthorityInfoAccess.getId();
    private static final String RESOURCE_NAME = "org.bouncycastle.x509.CertPathReviewerMessages";
    protected CertPath certPath;
    protected PKIXParameters pkixParams;
    protected Date validDate;
    protected List certs;
    protected int n;
    protected List[] notifications;
    protected List[] errors;
    protected TrustAnchor trustAnchor;
    protected PublicKey subjectPublicKey;
    protected PolicyNode policyTree;
    private boolean initialized;

    public void init(CertPath certPath, PKIXParameters pKIXParameters) throws CertPathReviewerException {
        if (this.initialized) {
            throw new IllegalStateException("object is already initialized!");
        }
        this.initialized = true;
        if (certPath == null) {
            throw new NullPointerException("certPath was null");
        }
        this.certPath = certPath;
        this.certs = certPath.getCertificates();
        this.n = this.certs.size();
        if (this.certs.isEmpty()) {
            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.emptyCertPath"));
        }
        this.pkixParams = (PKIXParameters) pKIXParameters.clone();
        this.validDate = getValidDate(this.pkixParams);
        this.notifications = null;
        this.errors = null;
        this.trustAnchor = null;
        this.subjectPublicKey = null;
        this.policyTree = null;
    }

    public PKIXCertPathReviewer(CertPath certPath, PKIXParameters pKIXParameters) throws CertPathReviewerException {
        init(certPath, pKIXParameters);
    }

    public PKIXCertPathReviewer() {
    }

    public CertPath getCertPath() {
        return this.certPath;
    }

    public int getCertPathSize() {
        return this.n;
    }

    public List[] getErrors() throws Exception {
        doChecks();
        return this.errors;
    }

    public List getErrors(int i) throws Exception {
        doChecks();
        return this.errors[i + 1];
    }

    public List[] getNotifications() throws Exception {
        doChecks();
        return this.notifications;
    }

    public List getNotifications(int i) throws Exception {
        doChecks();
        return this.notifications[i + 1];
    }

    public PolicyNode getPolicyTree() throws Exception {
        doChecks();
        return this.policyTree;
    }

    public PublicKey getSubjectPublicKey() throws Exception {
        doChecks();
        return this.subjectPublicKey;
    }

    public TrustAnchor getTrustAnchor() throws Exception {
        doChecks();
        return this.trustAnchor;
    }

    public boolean isValidCertPath() throws Exception {
        doChecks();
        boolean z = true;
        int i = 0;
        while (true) {
            if (i >= this.errors.length) {
                break;
            }
            if (!this.errors[i].isEmpty()) {
                z = false;
                break;
            }
            i++;
        }
        return z;
    }

    protected void addNotification(ErrorBundle errorBundle) {
        this.notifications[0].add(errorBundle);
    }

    protected void addNotification(ErrorBundle errorBundle, int i) {
        if (i < -1 || i >= this.n) {
            throw new IndexOutOfBoundsException();
        }
        this.notifications[i + 1].add(errorBundle);
    }

    protected void addError(ErrorBundle errorBundle) {
        this.errors[0].add(errorBundle);
    }

    protected void addError(ErrorBundle errorBundle, int i) {
        if (i < -1 || i >= this.n) {
            throw new IndexOutOfBoundsException();
        }
        this.errors[i + 1].add(errorBundle);
    }

    protected void doChecks() throws Exception {
        if (!this.initialized) {
            throw new IllegalStateException("Object not initialized. Call init() first.");
        }
        if (this.notifications == null) {
            this.notifications = new List[this.n + 1];
            this.errors = new List[this.n + 1];
            for (int i = 0; i < this.notifications.length; i++) {
                this.notifications[i] = new ArrayList();
                this.errors[i] = new ArrayList();
            }
            checkSignatures();
            checkNameConstraints();
            checkPathLength();
            checkPolicy();
            checkCriticalExtensions();
        }
    }

    private void checkNameConstraints() throws CertPathReviewerException {
        PKIXNameConstraintValidator pKIXNameConstraintValidator = new PKIXNameConstraintValidator();
        try {
            for (int size = this.certs.size() - 1; size > 0; size--) {
                int i = this.n - size;
                X509Certificate x509Certificate = (X509Certificate) this.certs.get(size);
                if (!isSelfIssued(x509Certificate)) {
                    X500Principal subjectPrincipal = getSubjectPrincipal(x509Certificate);
                    try {
                        ASN1Sequence aSN1Sequence = (ASN1Sequence) new ASN1InputStream(new ByteArrayInputStream(subjectPrincipal.getEncoded())).readObject();
                        try {
                            pKIXNameConstraintValidator.checkPermittedDN(aSN1Sequence);
                            try {
                                pKIXNameConstraintValidator.checkExcludedDN(aSN1Sequence);
                                try {
                                    ASN1Sequence aSN1Sequence2 = (ASN1Sequence) getExtensionValue(x509Certificate, SUBJECT_ALTERNATIVE_NAME);
                                    if (aSN1Sequence2 != null) {
                                        for (int i2 = 0; i2 < aSN1Sequence2.size(); i2++) {
                                            GeneralName generalName = GeneralName.getInstance(aSN1Sequence2.getObjectAt(i2));
                                            try {
                                                pKIXNameConstraintValidator.checkPermitted(generalName);
                                                pKIXNameConstraintValidator.checkExcluded(generalName);
                                            } catch (PKIXNameConstraintValidatorException e) {
                                                throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.notPermittedEmail", new Object[]{new UntrustedInput(generalName)}), e, this.certPath, size);
                                            }
                                        }
                                    }
                                } catch (AnnotatedException e2) {
                                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.subjAltNameExtError"), e2, this.certPath, size);
                                }
                            } catch (PKIXNameConstraintValidatorException e3) {
                                throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.excludedDN", new Object[]{new UntrustedInput(subjectPrincipal.getName())}), e3, this.certPath, size);
                            }
                        } catch (PKIXNameConstraintValidatorException e4) {
                            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.notPermittedDN", new Object[]{new UntrustedInput(subjectPrincipal.getName())}), e4, this.certPath, size);
                        }
                    } catch (IOException e5) {
                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.ncSubjectNameError", new Object[]{new UntrustedInput(subjectPrincipal)}), e5, this.certPath, size);
                    }
                }
                try {
                    ASN1Sequence aSN1Sequence3 = (ASN1Sequence) getExtensionValue(x509Certificate, NAME_CONSTRAINTS);
                    if (aSN1Sequence3 != null) {
                        NameConstraints nameConstraints = new NameConstraints(aSN1Sequence3);
                        ASN1Sequence permittedSubtrees = nameConstraints.getPermittedSubtrees();
                        if (permittedSubtrees != null) {
                            pKIXNameConstraintValidator.intersectPermittedSubtree(permittedSubtrees);
                        }
                        ASN1Sequence excludedSubtrees = nameConstraints.getExcludedSubtrees();
                        if (excludedSubtrees != null) {
                            Enumeration objects = excludedSubtrees.getObjects();
                            while (objects.hasMoreElements()) {
                                pKIXNameConstraintValidator.addExcludedSubtree(GeneralSubtree.getInstance(objects.nextElement()));
                            }
                        }
                    }
                } catch (AnnotatedException e6) {
                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.ncExtError"), e6, this.certPath, size);
                }
            }
        } catch (CertPathReviewerException e7) {
            addError(e7.getErrorMessage(), e7.getIndex());
        }
    }

    private void checkPathLength() {
        BasicConstraints basicConstraints;
        BigInteger pathLenConstraint;
        int iIntValue;
        int i = this.n;
        int i2 = 0;
        for (int size = this.certs.size() - 1; size > 0; size--) {
            int i3 = this.n - size;
            X509Certificate x509Certificate = (X509Certificate) this.certs.get(size);
            if (!isSelfIssued(x509Certificate)) {
                if (i <= 0) {
                    addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.pathLenghtExtended"));
                }
                i--;
                i2++;
            }
            try {
                basicConstraints = BasicConstraints.getInstance(getExtensionValue(x509Certificate, BASIC_CONSTRAINTS));
            } catch (AnnotatedException e) {
                addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.processLengthConstError"), size);
                basicConstraints = null;
            }
            if (basicConstraints != null && (pathLenConstraint = basicConstraints.getPathLenConstraint()) != null && (iIntValue = pathLenConstraint.intValue()) < i) {
                i = iIntValue;
            }
        }
        addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.totalPathLength", new Object[]{new Integer(i2)}));
    }

    private void checkSignatures() throws Exception {
        boolean[] keyUsage;
        TrustAnchor trustAnchor = null;
        X500Principal subjectPrincipal = null;
        addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.certPathValidDate", new Object[]{new TrustedInput(this.validDate), new TrustedInput(new Date())}));
        try {
            X509Certificate x509Certificate = (X509Certificate) this.certs.get(this.certs.size() - 1);
            Collection trustAnchors = getTrustAnchors(x509Certificate, this.pkixParams.getTrustAnchors());
            if (trustAnchors.size() > 1) {
                addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.conflictingTrustAnchors", new Object[]{new Integer(trustAnchors.size()), new UntrustedInput(x509Certificate.getIssuerX500Principal())}));
            } else if (trustAnchors.isEmpty()) {
                addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noTrustAnchorFound", new Object[]{new UntrustedInput(x509Certificate.getIssuerX500Principal()), new Integer(this.pkixParams.getTrustAnchors().size())}));
            } else {
                trustAnchor = (TrustAnchor) trustAnchors.iterator().next();
                try {
                    org.bouncycastle.jce.provider.CertPathValidatorUtilities.verifyX509Certificate(x509Certificate, trustAnchor.getTrustedCert() != null ? trustAnchor.getTrustedCert().getPublicKey() : trustAnchor.getCAPublicKey(), this.pkixParams.getSigProvider());
                } catch (SignatureException e) {
                    addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.trustButInvalidCert"));
                } catch (Exception e2) {
                }
            }
        } catch (CertPathReviewerException e3) {
            addError(e3.getErrorMessage());
        } catch (Throwable th) {
            addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.unknown", new Object[]{new UntrustedInput(th.getMessage()), new UntrustedInput(th)}));
        }
        if (trustAnchor != null) {
            X509Certificate trustedCert = trustAnchor.getTrustedCert();
            try {
                subjectPrincipal = trustedCert != null ? getSubjectPrincipal(trustedCert) : new X500Principal(trustAnchor.getCAName());
            } catch (IllegalArgumentException e4) {
                addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.trustDNInvalid", new Object[]{new UntrustedInput(trustAnchor.getCAName())}));
            }
            if (trustedCert != null && (keyUsage = trustedCert.getKeyUsage()) != null && !keyUsage[5]) {
                addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.trustKeyUsage"));
            }
        }
        PublicKey nextWorkingKey = null;
        X500Principal subjectX500Principal = subjectPrincipal;
        X509Certificate trustedCert2 = null;
        if (trustAnchor != null) {
            trustedCert2 = trustAnchor.getTrustedCert();
            nextWorkingKey = trustedCert2 != null ? trustedCert2.getPublicKey() : trustAnchor.getCAPublicKey();
            try {
                AlgorithmIdentifier algorithmIdentifier = getAlgorithmIdentifier(nextWorkingKey);
                algorithmIdentifier.getObjectId();
                algorithmIdentifier.getParameters();
            } catch (CertPathValidatorException e5) {
                addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.trustPubKeyError"));
            }
        }
        for (int size = this.certs.size() - 1; size >= 0; size--) {
            int i = this.n - size;
            X509Certificate x509Certificate2 = (X509Certificate) this.certs.get(size);
            if (nextWorkingKey != null) {
                try {
                    org.bouncycastle.jce.provider.CertPathValidatorUtilities.verifyX509Certificate(x509Certificate2, nextWorkingKey, this.pkixParams.getSigProvider());
                } catch (GeneralSecurityException e6) {
                    addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.signatureNotVerified", new Object[]{e6.getMessage(), e6, e6.getClass().getName()}), size);
                }
            } else if (isSelfIssued(x509Certificate2)) {
                try {
                    org.bouncycastle.jce.provider.CertPathValidatorUtilities.verifyX509Certificate(x509Certificate2, x509Certificate2.getPublicKey(), this.pkixParams.getSigProvider());
                    addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.rootKeyIsValidButNotATrustAnchor"), size);
                } catch (GeneralSecurityException e7) {
                    addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.signatureNotVerified", new Object[]{e7.getMessage(), e7, e7.getClass().getName()}), size);
                }
            } else {
                ErrorBundle errorBundle = new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.NoIssuerPublicKey");
                byte[] extensionValue = x509Certificate2.getExtensionValue(X509Extensions.AuthorityKeyIdentifier.getId());
                if (extensionValue != null) {
                    try {
                        AuthorityKeyIdentifier authorityKeyIdentifier = AuthorityKeyIdentifier.getInstance(X509ExtensionUtil.fromExtensionValue(extensionValue));
                        GeneralNames authorityCertIssuer = authorityKeyIdentifier.getAuthorityCertIssuer();
                        if (authorityCertIssuer != null) {
                            GeneralName generalName = authorityCertIssuer.getNames()[0];
                            BigInteger authorityCertSerialNumber = authorityKeyIdentifier.getAuthorityCertSerialNumber();
                            if (authorityCertSerialNumber != null) {
                                errorBundle.setExtraArguments(new Object[]{new LocaleString(RESOURCE_NAME, "missingIssuer"), " \"", generalName, "\" ", new LocaleString(RESOURCE_NAME, "missingSerial"), SymbolConstants.SPACE_SYMBOL, authorityCertSerialNumber});
                            }
                        }
                    } catch (IOException e8) {
                    }
                }
                addError(errorBundle, size);
            }
            try {
                x509Certificate2.checkValidity(this.validDate);
            } catch (CertificateExpiredException e9) {
                addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.certificateExpired", new Object[]{new TrustedInput(x509Certificate2.getNotAfter())}), size);
            } catch (CertificateNotYetValidException e10) {
                addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.certificateNotYetValid", new Object[]{new TrustedInput(x509Certificate2.getNotBefore())}), size);
            }
            if (this.pkixParams.isRevocationEnabled()) {
                try {
                    DERObject extensionValue2 = getExtensionValue(x509Certificate2, CRL_DIST_POINTS);
                    cRLDistPoint = extensionValue2 != null ? CRLDistPoint.getInstance(extensionValue2) : null;
                } catch (AnnotatedException e11) {
                    addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlDistPtExtError"), size);
                }
                try {
                    DERObject extensionValue3 = getExtensionValue(x509Certificate2, AUTH_INFO_ACCESS);
                    authorityInformationAccess = extensionValue3 != null ? AuthorityInformationAccess.getInstance(extensionValue3) : null;
                } catch (AnnotatedException e12) {
                    addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlAuthInfoAccError"), size);
                }
                Vector cRLDistUrls = getCRLDistUrls(cRLDistPoint);
                Vector oCSPUrls = getOCSPUrls(authorityInformationAccess);
                Iterator it = cRLDistUrls.iterator();
                while (it.hasNext()) {
                    addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlDistPoint", new Object[]{new UntrustedUrlInput(it.next())}), size);
                }
                Iterator it2 = oCSPUrls.iterator();
                while (it2.hasNext()) {
                    addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.ocspLocation", new Object[]{new UntrustedUrlInput(it2.next())}), size);
                }
                try {
                    checkRevocation(this.pkixParams, x509Certificate2, this.validDate, trustedCert2, nextWorkingKey, cRLDistUrls, oCSPUrls, size);
                } catch (CertPathReviewerException e13) {
                    addError(e13.getErrorMessage(), size);
                }
            }
            if (subjectX500Principal != null && !x509Certificate2.getIssuerX500Principal().equals(subjectX500Principal)) {
                addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.certWrongIssuer", new Object[]{subjectX500Principal.getName(), x509Certificate2.getIssuerX500Principal().getName()}), size);
            }
            if (i != this.n) {
                if (x509Certificate2 != null && x509Certificate2.getVersion() == 1) {
                    addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noCACert"), size);
                }
                try {
                    BasicConstraints basicConstraints = BasicConstraints.getInstance(getExtensionValue(x509Certificate2, BASIC_CONSTRAINTS));
                    if (basicConstraints == null) {
                        addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noBasicConstraints"), size);
                    } else if (!basicConstraints.isCA()) {
                        addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noCACert"), size);
                    }
                } catch (AnnotatedException e14) {
                    addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.errorProcesingBC"), size);
                }
                boolean[] keyUsage2 = x509Certificate2.getKeyUsage();
                if (keyUsage2 != null && !keyUsage2[5]) {
                    addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noCertSign"), size);
                }
            }
            trustedCert2 = x509Certificate2;
            subjectX500Principal = x509Certificate2.getSubjectX500Principal();
            try {
                nextWorkingKey = getNextWorkingKey(this.certs, size);
                AlgorithmIdentifier algorithmIdentifier2 = getAlgorithmIdentifier(nextWorkingKey);
                algorithmIdentifier2.getObjectId();
                algorithmIdentifier2.getParameters();
            } catch (CertPathValidatorException e15) {
                addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.pubKeyError"), size);
            }
        }
        this.trustAnchor = trustAnchor;
        this.subjectPublicKey = nextWorkingKey;
    }

    private void checkPolicy() throws CertPathReviewerException {
        PKIXPolicyNode pKIXPolicyNode;
        int iIntValue;
        String id;
        Set<String> initialPolicies = this.pkixParams.getInitialPolicies();
        ArrayList[] arrayListArr = new ArrayList[this.n + 1];
        for (int i = 0; i < arrayListArr.length; i++) {
            arrayListArr[i] = new ArrayList();
        }
        HashSet hashSet = new HashSet();
        hashSet.add("2.5.29.32.0");
        PKIXPolicyNode pKIXPolicyNode2 = new PKIXPolicyNode(new ArrayList(), 0, hashSet, null, new HashSet(), "2.5.29.32.0", false);
        arrayListArr[0].add(pKIXPolicyNode2);
        int i2 = this.pkixParams.isExplicitPolicyRequired() ? 0 : this.n + 1;
        int i3 = this.pkixParams.isAnyPolicyInhibited() ? 0 : this.n + 1;
        int i4 = this.pkixParams.isPolicyMappingInhibited() ? 0 : this.n + 1;
        HashSet hashSet2 = null;
        X509Certificate x509Certificate = null;
        try {
            int size = this.certs.size() - 1;
            while (size >= 0) {
                int i5 = this.n - size;
                x509Certificate = (X509Certificate) this.certs.get(size);
                try {
                    ASN1Sequence aSN1Sequence = (ASN1Sequence) getExtensionValue(x509Certificate, CERTIFICATE_POLICIES);
                    if (aSN1Sequence != null && pKIXPolicyNode2 != null) {
                        Enumeration objects = aSN1Sequence.getObjects();
                        HashSet hashSet3 = new HashSet();
                        while (objects.hasMoreElements()) {
                            PolicyInformation policyInformation = PolicyInformation.getInstance(objects.nextElement());
                            DERObjectIdentifier policyIdentifier = policyInformation.getPolicyIdentifier();
                            hashSet3.add(policyIdentifier.getId());
                            if (!"2.5.29.32.0".equals(policyIdentifier.getId())) {
                                try {
                                    Set qualifierSet = getQualifierSet(policyInformation.getPolicyQualifiers());
                                    if (!processCertD1i(i5, arrayListArr, policyIdentifier, qualifierSet)) {
                                        processCertD1ii(i5, arrayListArr, policyIdentifier, qualifierSet);
                                    }
                                } catch (CertPathValidatorException e) {
                                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.policyQualifierError"), e, this.certPath, size);
                                }
                            }
                        }
                        if (hashSet2 == null || hashSet2.contains("2.5.29.32.0")) {
                            hashSet2 = hashSet3;
                        } else {
                            HashSet hashSet4 = new HashSet();
                            for (Object obj : hashSet2) {
                                if (hashSet3.contains(obj)) {
                                    hashSet4.add(obj);
                                }
                            }
                            hashSet2 = hashSet4;
                        }
                        if (i3 > 0 || (i5 < this.n && isSelfIssued(x509Certificate))) {
                            Enumeration objects2 = aSN1Sequence.getObjects();
                            while (true) {
                                if (objects2.hasMoreElements()) {
                                    PolicyInformation policyInformation2 = PolicyInformation.getInstance(objects2.nextElement());
                                    if ("2.5.29.32.0".equals(policyInformation2.getPolicyIdentifier().getId())) {
                                        try {
                                            Set qualifierSet2 = getQualifierSet(policyInformation2.getPolicyQualifiers());
                                            ArrayList arrayList = arrayListArr[i5 - 1];
                                            for (int i6 = 0; i6 < arrayList.size(); i6++) {
                                                PKIXPolicyNode pKIXPolicyNode3 = (PKIXPolicyNode) arrayList.get(i6);
                                                for (Object obj2 : pKIXPolicyNode3.getExpectedPolicies()) {
                                                    if (obj2 instanceof String) {
                                                        id = (String) obj2;
                                                    } else if (obj2 instanceof DERObjectIdentifier) {
                                                        id = ((DERObjectIdentifier) obj2).getId();
                                                    }
                                                    boolean z = false;
                                                    Iterator children = pKIXPolicyNode3.getChildren();
                                                    while (children.hasNext()) {
                                                        if (id.equals(((PKIXPolicyNode) children.next()).getValidPolicy())) {
                                                            z = true;
                                                        }
                                                    }
                                                    if (!z) {
                                                        HashSet hashSet5 = new HashSet();
                                                        hashSet5.add(id);
                                                        PKIXPolicyNode pKIXPolicyNode4 = new PKIXPolicyNode(new ArrayList(), i5, hashSet5, pKIXPolicyNode3, qualifierSet2, id, false);
                                                        pKIXPolicyNode3.addChild(pKIXPolicyNode4);
                                                        arrayListArr[i5].add(pKIXPolicyNode4);
                                                    }
                                                }
                                            }
                                        } catch (CertPathValidatorException e2) {
                                            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.policyQualifierError"), e2, this.certPath, size);
                                        }
                                    }
                                }
                            }
                        }
                        for (int i7 = i5 - 1; i7 >= 0; i7--) {
                            ArrayList arrayList2 = arrayListArr[i7];
                            for (int i8 = 0; i8 < arrayList2.size(); i8++) {
                                PKIXPolicyNode pKIXPolicyNode5 = (PKIXPolicyNode) arrayList2.get(i8);
                                if (!pKIXPolicyNode5.hasChildren()) {
                                    pKIXPolicyNode2 = removePolicyNode(pKIXPolicyNode2, arrayListArr, pKIXPolicyNode5);
                                    if (pKIXPolicyNode2 == null) {
                                        break;
                                    }
                                }
                            }
                        }
                        Set<String> criticalExtensionOIDs = x509Certificate.getCriticalExtensionOIDs();
                        if (criticalExtensionOIDs != null) {
                            boolean zContains = criticalExtensionOIDs.contains(CERTIFICATE_POLICIES);
                            ArrayList arrayList3 = arrayListArr[i5];
                            for (int i9 = 0; i9 < arrayList3.size(); i9++) {
                                ((PKIXPolicyNode) arrayList3.get(i9)).setCritical(zContains);
                            }
                        }
                    }
                    if (aSN1Sequence == null) {
                        pKIXPolicyNode2 = null;
                    }
                    if (i2 <= 0 && pKIXPolicyNode2 == null) {
                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noValidPolicyTree"));
                    }
                    if (i5 != this.n) {
                        try {
                            DERObject extensionValue = getExtensionValue(x509Certificate, POLICY_MAPPINGS);
                            if (extensionValue != null) {
                                ASN1Sequence aSN1Sequence2 = (ASN1Sequence) extensionValue;
                                for (int i10 = 0; i10 < aSN1Sequence2.size(); i10++) {
                                    ASN1Sequence aSN1Sequence3 = (ASN1Sequence) aSN1Sequence2.getObjectAt(i10);
                                    DERObjectIdentifier dERObjectIdentifier = (DERObjectIdentifier) aSN1Sequence3.getObjectAt(0);
                                    DERObjectIdentifier dERObjectIdentifier2 = (DERObjectIdentifier) aSN1Sequence3.getObjectAt(1);
                                    if ("2.5.29.32.0".equals(dERObjectIdentifier.getId())) {
                                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.invalidPolicyMapping"), this.certPath, size);
                                    }
                                    if ("2.5.29.32.0".equals(dERObjectIdentifier2.getId())) {
                                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.invalidPolicyMapping"), this.certPath, size);
                                    }
                                }
                            }
                            if (extensionValue != null) {
                                ASN1Sequence aSN1Sequence4 = (ASN1Sequence) extensionValue;
                                HashMap map = new HashMap();
                                HashSet<String> hashSet6 = new HashSet();
                                for (int i11 = 0; i11 < aSN1Sequence4.size(); i11++) {
                                    ASN1Sequence aSN1Sequence5 = (ASN1Sequence) aSN1Sequence4.getObjectAt(i11);
                                    String id2 = ((DERObjectIdentifier) aSN1Sequence5.getObjectAt(0)).getId();
                                    String id3 = ((DERObjectIdentifier) aSN1Sequence5.getObjectAt(1)).getId();
                                    if (map.containsKey(id2)) {
                                        ((Set) map.get(id2)).add(id3);
                                    } else {
                                        HashSet hashSet7 = new HashSet();
                                        hashSet7.add(id3);
                                        map.put(id2, hashSet7);
                                        hashSet6.add(id2);
                                    }
                                }
                                for (String str : hashSet6) {
                                    if (i4 > 0) {
                                        try {
                                            prepareNextCertB1(i5, arrayListArr, str, map, x509Certificate);
                                        } catch (CertPathValidatorException e3) {
                                            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.policyQualifierError"), e3, this.certPath, size);
                                        } catch (AnnotatedException e4) {
                                            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.policyExtError"), e4, this.certPath, size);
                                        }
                                    } else if (i4 <= 0) {
                                        pKIXPolicyNode2 = prepareNextCertB2(i5, arrayListArr, str, pKIXPolicyNode2);
                                    }
                                }
                            }
                            if (!isSelfIssued(x509Certificate)) {
                                if (i2 != 0) {
                                    i2--;
                                }
                                if (i4 != 0) {
                                    i4--;
                                }
                                if (i3 != 0) {
                                    i3--;
                                }
                            }
                            try {
                                ASN1Sequence aSN1Sequence6 = (ASN1Sequence) getExtensionValue(x509Certificate, POLICY_CONSTRAINTS);
                                if (aSN1Sequence6 != null) {
                                    Enumeration objects3 = aSN1Sequence6.getObjects();
                                    while (objects3.hasMoreElements()) {
                                        ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject) objects3.nextElement();
                                        switch (aSN1TaggedObject.getTagNo()) {
                                            case 0:
                                                int iIntValue2 = DERInteger.getInstance(aSN1TaggedObject, false).getValue().intValue();
                                                if (iIntValue2 < i2) {
                                                    i2 = iIntValue2;
                                                    break;
                                                } else {
                                                    break;
                                                }
                                            case 1:
                                                int iIntValue3 = DERInteger.getInstance(aSN1TaggedObject, false).getValue().intValue();
                                                if (iIntValue3 < i4) {
                                                    i4 = iIntValue3;
                                                    break;
                                                } else {
                                                    break;
                                                }
                                        }
                                    }
                                }
                                try {
                                    DERInteger dERInteger = (DERInteger) getExtensionValue(x509Certificate, INHIBIT_ANY_POLICY);
                                    if (dERInteger != null && (iIntValue = dERInteger.getValue().intValue()) < i3) {
                                        i3 = iIntValue;
                                    }
                                } catch (AnnotatedException e5) {
                                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.policyInhibitExtError"), this.certPath, size);
                                }
                            } catch (AnnotatedException e6) {
                                throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.policyConstExtError"), this.certPath, size);
                            }
                        } catch (AnnotatedException e7) {
                            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.policyMapExtError"), e7, this.certPath, size);
                        }
                    }
                    size--;
                } catch (AnnotatedException e8) {
                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.policyExtError"), e8, this.certPath, size);
                }
            }
            if (!isSelfIssued(x509Certificate) && i2 > 0) {
                i2--;
            }
            try {
                ASN1Sequence aSN1Sequence7 = (ASN1Sequence) getExtensionValue(x509Certificate, POLICY_CONSTRAINTS);
                if (aSN1Sequence7 != null) {
                    Enumeration objects4 = aSN1Sequence7.getObjects();
                    while (objects4.hasMoreElements()) {
                        ASN1TaggedObject aSN1TaggedObject2 = (ASN1TaggedObject) objects4.nextElement();
                        switch (aSN1TaggedObject2.getTagNo()) {
                            case 0:
                                if (DERInteger.getInstance(aSN1TaggedObject2, false).getValue().intValue() == 0) {
                                    i2 = 0;
                                    break;
                                } else {
                                    break;
                                }
                        }
                    }
                }
                if (pKIXPolicyNode2 == null) {
                    if (this.pkixParams.isExplicitPolicyRequired()) {
                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.explicitPolicy"), this.certPath, size);
                    }
                    pKIXPolicyNode = null;
                } else if (isAnyPolicy(initialPolicies)) {
                    if (this.pkixParams.isExplicitPolicyRequired()) {
                        if (hashSet2.isEmpty()) {
                            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.explicitPolicy"), this.certPath, size);
                        }
                        HashSet hashSet8 = new HashSet();
                        for (ArrayList arrayList4 : arrayListArr) {
                            for (int i12 = 0; i12 < arrayList4.size(); i12++) {
                                PKIXPolicyNode pKIXPolicyNode6 = (PKIXPolicyNode) arrayList4.get(i12);
                                if ("2.5.29.32.0".equals(pKIXPolicyNode6.getValidPolicy())) {
                                    Iterator children2 = pKIXPolicyNode6.getChildren();
                                    while (children2.hasNext()) {
                                        hashSet8.add(children2.next());
                                    }
                                }
                            }
                        }
                        Iterator it = hashSet8.iterator();
                        while (it.hasNext()) {
                            if (!hashSet2.contains(((PKIXPolicyNode) it.next()).getValidPolicy())) {
                            }
                        }
                        if (pKIXPolicyNode2 != null) {
                            for (int i13 = this.n - 1; i13 >= 0; i13--) {
                                ArrayList arrayList5 = arrayListArr[i13];
                                for (int i14 = 0; i14 < arrayList5.size(); i14++) {
                                    PKIXPolicyNode pKIXPolicyNode7 = (PKIXPolicyNode) arrayList5.get(i14);
                                    if (!pKIXPolicyNode7.hasChildren()) {
                                        pKIXPolicyNode2 = removePolicyNode(pKIXPolicyNode2, arrayListArr, pKIXPolicyNode7);
                                    }
                                }
                            }
                        }
                    }
                    pKIXPolicyNode = pKIXPolicyNode2;
                } else {
                    HashSet<PKIXPolicyNode> hashSet9 = new HashSet();
                    for (ArrayList arrayList6 : arrayListArr) {
                        for (int i15 = 0; i15 < arrayList6.size(); i15++) {
                            PKIXPolicyNode pKIXPolicyNode8 = (PKIXPolicyNode) arrayList6.get(i15);
                            if ("2.5.29.32.0".equals(pKIXPolicyNode8.getValidPolicy())) {
                                Iterator children3 = pKIXPolicyNode8.getChildren();
                                while (children3.hasNext()) {
                                    PKIXPolicyNode pKIXPolicyNode9 = (PKIXPolicyNode) children3.next();
                                    if (!"2.5.29.32.0".equals(pKIXPolicyNode9.getValidPolicy())) {
                                        hashSet9.add(pKIXPolicyNode9);
                                    }
                                }
                            }
                        }
                    }
                    for (PKIXPolicyNode pKIXPolicyNode10 : hashSet9) {
                        if (!initialPolicies.contains(pKIXPolicyNode10.getValidPolicy())) {
                            pKIXPolicyNode2 = removePolicyNode(pKIXPolicyNode2, arrayListArr, pKIXPolicyNode10);
                        }
                    }
                    if (pKIXPolicyNode2 != null) {
                        for (int i16 = this.n - 1; i16 >= 0; i16--) {
                            ArrayList arrayList7 = arrayListArr[i16];
                            for (int i17 = 0; i17 < arrayList7.size(); i17++) {
                                PKIXPolicyNode pKIXPolicyNode11 = (PKIXPolicyNode) arrayList7.get(i17);
                                if (!pKIXPolicyNode11.hasChildren()) {
                                    pKIXPolicyNode2 = removePolicyNode(pKIXPolicyNode2, arrayListArr, pKIXPolicyNode11);
                                }
                            }
                        }
                    }
                    pKIXPolicyNode = pKIXPolicyNode2;
                }
                if (i2 <= 0 && pKIXPolicyNode == null) {
                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.invalidPolicy"));
                }
            } catch (AnnotatedException e9) {
                throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.policyConstExtError"), this.certPath, size);
            }
        } catch (CertPathReviewerException e10) {
            addError(e10.getErrorMessage(), e10.getIndex());
        }
    }

    private void checkCriticalExtensions() throws CertPathReviewerException, CertPathValidatorException {
        List<PKIXCertPathChecker> certPathCheckers = this.pkixParams.getCertPathCheckers();
        Iterator<PKIXCertPathChecker> it = certPathCheckers.iterator();
        while (it.hasNext()) {
            try {
                try {
                    it.next().init(false);
                } catch (CertPathValidatorException e) {
                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.certPathCheckerError", new Object[]{e.getMessage(), e, e.getClass().getName()}), e);
                }
            } catch (CertPathReviewerException e2) {
                addError(e2.getErrorMessage(), e2.getIndex());
                return;
            }
        }
        for (int size = this.certs.size() - 1; size >= 0; size--) {
            X509Certificate x509Certificate = (X509Certificate) this.certs.get(size);
            Set<String> criticalExtensionOIDs = x509Certificate.getCriticalExtensionOIDs();
            if (criticalExtensionOIDs != null && !criticalExtensionOIDs.isEmpty()) {
                criticalExtensionOIDs.remove(KEY_USAGE);
                criticalExtensionOIDs.remove(CERTIFICATE_POLICIES);
                criticalExtensionOIDs.remove(POLICY_MAPPINGS);
                criticalExtensionOIDs.remove(INHIBIT_ANY_POLICY);
                criticalExtensionOIDs.remove(ISSUING_DISTRIBUTION_POINT);
                criticalExtensionOIDs.remove(DELTA_CRL_INDICATOR);
                criticalExtensionOIDs.remove(POLICY_CONSTRAINTS);
                criticalExtensionOIDs.remove(BASIC_CONSTRAINTS);
                criticalExtensionOIDs.remove(SUBJECT_ALTERNATIVE_NAME);
                criticalExtensionOIDs.remove(NAME_CONSTRAINTS);
                if (criticalExtensionOIDs.contains(QC_STATEMENT) && processQcStatements(x509Certificate, size)) {
                    criticalExtensionOIDs.remove(QC_STATEMENT);
                }
                Iterator<PKIXCertPathChecker> it2 = certPathCheckers.iterator();
                while (it2.hasNext()) {
                    try {
                        it2.next().check(x509Certificate, criticalExtensionOIDs);
                    } catch (CertPathValidatorException e3) {
                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.criticalExtensionError", new Object[]{e3.getMessage(), e3, e3.getClass().getName()}), e3.getCause(), this.certPath, size);
                    }
                }
                if (!criticalExtensionOIDs.isEmpty()) {
                    Iterator<String> it3 = criticalExtensionOIDs.iterator();
                    while (it3.hasNext()) {
                        addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.unknownCriticalExt", new Object[]{new DERObjectIdentifier(it3.next())}), size);
                    }
                }
            }
        }
    }

    private boolean processQcStatements(X509Certificate x509Certificate, int i) {
        try {
            boolean z = false;
            ASN1Sequence aSN1Sequence = (ASN1Sequence) getExtensionValue(x509Certificate, QC_STATEMENT);
            for (int i2 = 0; i2 < aSN1Sequence.size(); i2++) {
                QCStatement qCStatement = QCStatement.getInstance(aSN1Sequence.getObjectAt(i2));
                if (QCStatement.id_etsi_qcs_QcCompliance.equals(qCStatement.getStatementId())) {
                    addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.QcEuCompliance"), i);
                } else if (!QCStatement.id_qcs_pkixQCSyntax_v1.equals(qCStatement.getStatementId())) {
                    if (QCStatement.id_etsi_qcs_QcSSCD.equals(qCStatement.getStatementId())) {
                        addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.QcSSCD"), i);
                    } else if (QCStatement.id_etsi_qcs_LimiteValue.equals(qCStatement.getStatementId())) {
                        MonetaryValue monetaryValue = MonetaryValue.getInstance(qCStatement.getStatementInfo());
                        monetaryValue.getCurrency();
                        double dDoubleValue = monetaryValue.getAmount().doubleValue() * Math.pow(10.0d, monetaryValue.getExponent().doubleValue());
                        addNotification(monetaryValue.getCurrency().isAlphabetic() ? new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.QcLimitValueAlpha", new Object[]{monetaryValue.getCurrency().getAlphabetic(), new TrustedInput(new Double(dDoubleValue)), monetaryValue}) : new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.QcLimitValueNum", new Object[]{new Integer(monetaryValue.getCurrency().getNumeric()), new TrustedInput(new Double(dDoubleValue)), monetaryValue}), i);
                    } else {
                        addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.QcUnknownStatement", new Object[]{qCStatement.getStatementId(), new UntrustedInput(qCStatement)}), i);
                        z = true;
                    }
                }
            }
            return !z;
        } catch (AnnotatedException e) {
            addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.QcStatementExtError"), i);
            return false;
        }
    }

    private String IPtoString(byte[] bArr) {
        String string;
        try {
            string = InetAddress.getByAddress(bArr).getHostAddress();
        } catch (Exception e) {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i != bArr.length; i++) {
                stringBuffer.append(Integer.toHexString(bArr[i] & 255));
                stringBuffer.append(' ');
            }
            string = stringBuffer.toString();
        }
        return string;
    }

    protected void checkRevocation(PKIXParameters pKIXParameters, X509Certificate x509Certificate, Date date, X509Certificate x509Certificate2, PublicKey publicKey, Vector vector, Vector vector2, int i) throws Exception {
        checkCRLs(pKIXParameters, x509Certificate, date, x509Certificate2, publicKey, vector, i);
    }

    protected void checkCRLs(PKIXParameters pKIXParameters, X509Certificate x509Certificate, Date date, X509Certificate x509Certificate2, PublicKey publicKey, Vector vector, int i) throws Exception {
        Iterator it;
        boolean[] keyUsage;
        String str;
        X509CRL crl;
        X509CRLStoreSelector x509CRLStoreSelector = new X509CRLStoreSelector();
        try {
            x509CRLStoreSelector.addIssuerName(getEncodedIssuerPrincipal(x509Certificate).getEncoded());
            x509CRLStoreSelector.setCertificateChecking(x509Certificate);
            try {
                Set setFindCRLs = CRL_UTIL.findCRLs(x509CRLStoreSelector, pKIXParameters);
                it = setFindCRLs.iterator();
                if (setFindCRLs.isEmpty()) {
                    Iterator it2 = CRL_UTIL.findCRLs(new X509CRLStoreSelector(), pKIXParameters).iterator();
                    ArrayList arrayList = new ArrayList();
                    while (it2.hasNext()) {
                        arrayList.add(((X509CRL) it2.next()).getIssuerX500Principal());
                    }
                    addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noCrlInCertstore", new Object[]{new UntrustedInput(x509CRLStoreSelector.getIssuerNames()), new UntrustedInput(arrayList), new Integer(arrayList.size())}), i);
                }
            } catch (AnnotatedException e) {
                addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlExtractionError", new Object[]{e.getCause().getMessage(), e.getCause(), e.getCause().getClass().getName()}), i);
                it = new ArrayList().iterator();
            }
            boolean z = false;
            X509CRL x509crl = null;
            while (it.hasNext()) {
                x509crl = (X509CRL) it.next();
                if (x509crl.getNextUpdate() == null || new Date().before(x509crl.getNextUpdate())) {
                    z = true;
                    addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.localValidCRL", new Object[]{new TrustedInput(x509crl.getThisUpdate()), new TrustedInput(x509crl.getNextUpdate())}), i);
                    break;
                }
                addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.localInvalidCRL", new Object[]{new TrustedInput(x509crl.getThisUpdate()), new TrustedInput(x509crl.getNextUpdate())}), i);
            }
            if (!z) {
                Iterator it3 = vector.iterator();
                while (it3.hasNext()) {
                    try {
                        str = (String) it3.next();
                        crl = getCRL(str);
                    } catch (CertPathReviewerException e2) {
                        addNotification(e2.getErrorMessage(), i);
                    }
                    if (crl != null) {
                        if (!x509Certificate.getIssuerX500Principal().equals(crl.getIssuerX500Principal())) {
                            addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.onlineCRLWrongCA", new Object[]{new UntrustedInput(crl.getIssuerX500Principal().getName()), new UntrustedInput(x509Certificate.getIssuerX500Principal().getName()), new UntrustedUrlInput(str)}), i);
                        } else {
                            if (crl.getNextUpdate() == null || new Date().before(crl.getNextUpdate())) {
                                z = true;
                                addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.onlineValidCRL", new Object[]{new TrustedInput(crl.getThisUpdate()), new TrustedInput(crl.getNextUpdate()), new UntrustedUrlInput(str)}), i);
                                x509crl = crl;
                                break;
                            }
                            addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.onlineInvalidCRL", new Object[]{new TrustedInput(crl.getThisUpdate()), new TrustedInput(crl.getNextUpdate()), new UntrustedUrlInput(str)}), i);
                        }
                    }
                }
            }
            if (x509crl != null) {
                if (x509Certificate2 != null && (keyUsage = x509Certificate2.getKeyUsage()) != null && (keyUsage.length < 7 || !keyUsage[6])) {
                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noCrlSigningPermited"));
                }
                if (publicKey == null) {
                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlNoIssuerPublicKey"));
                }
                try {
                    x509crl.verify(publicKey, "BC");
                    X509CRLEntry revokedCertificate = x509crl.getRevokedCertificate(x509Certificate.getSerialNumber());
                    if (revokedCertificate != null) {
                        String str2 = null;
                        if (revokedCertificate.hasExtensions()) {
                            try {
                                DEREnumerated dEREnumerated = DEREnumerated.getInstance(getExtensionValue(revokedCertificate, X509Extensions.ReasonCode.getId()));
                                if (dEREnumerated != null) {
                                    str2 = crlReasons[dEREnumerated.getValue().intValue()];
                                }
                            } catch (AnnotatedException e3) {
                                throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlReasonExtError"), e3);
                            }
                        }
                        if (str2 == null) {
                            str2 = crlReasons[7];
                        }
                        LocaleString localeString = new LocaleString(RESOURCE_NAME, str2);
                        if (!date.before(revokedCertificate.getRevocationDate())) {
                            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.certRevoked", new Object[]{new TrustedInput(revokedCertificate.getRevocationDate()), localeString}));
                        }
                        addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.revokedAfterValidation", new Object[]{new TrustedInput(revokedCertificate.getRevocationDate()), localeString}), i);
                    } else {
                        addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.notRevoked"), i);
                    }
                    if (x509crl.getNextUpdate() != null && x509crl.getNextUpdate().before(new Date())) {
                        addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlUpdateAvailable", new Object[]{new TrustedInput(x509crl.getNextUpdate())}), i);
                    }
                    try {
                        DERObject extensionValue = getExtensionValue(x509crl, ISSUING_DISTRIBUTION_POINT);
                        try {
                            DERObject extensionValue2 = getExtensionValue(x509crl, DELTA_CRL_INDICATOR);
                            if (extensionValue2 != null) {
                                X509CRLStoreSelector x509CRLStoreSelector2 = new X509CRLStoreSelector();
                                try {
                                    x509CRLStoreSelector2.addIssuerName(getIssuerPrincipal(x509crl).getEncoded());
                                    x509CRLStoreSelector2.setMinCRLNumber(((DERInteger) extensionValue2).getPositiveValue());
                                    try {
                                        x509CRLStoreSelector2.setMaxCRLNumber(((DERInteger) getExtensionValue(x509crl, CRL_NUMBER)).getPositiveValue().subtract(BigInteger.valueOf(1L)));
                                        boolean z2 = false;
                                        try {
                                            Iterator it4 = CRL_UTIL.findCRLs(x509CRLStoreSelector2, pKIXParameters).iterator();
                                            while (true) {
                                                if (!it4.hasNext()) {
                                                    break;
                                                }
                                                try {
                                                    DERObject extensionValue3 = getExtensionValue((X509CRL) it4.next(), ISSUING_DISTRIBUTION_POINT);
                                                    if (extensionValue == null) {
                                                        if (extensionValue3 == null) {
                                                            z2 = true;
                                                            break;
                                                        }
                                                    } else if (extensionValue.equals(extensionValue3)) {
                                                        z2 = true;
                                                        break;
                                                    }
                                                } catch (AnnotatedException e4) {
                                                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.distrPtExtError"), e4);
                                                }
                                            }
                                            if (!z2) {
                                                throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noBaseCRL"));
                                            }
                                        } catch (AnnotatedException e5) {
                                            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlExtractionError"), e5);
                                        }
                                    } catch (AnnotatedException e6) {
                                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlNbrExtError"), e6);
                                    }
                                } catch (IOException e7) {
                                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlIssuerException"), e7);
                                }
                            }
                            if (extensionValue != null) {
                                IssuingDistributionPoint issuingDistributionPoint = IssuingDistributionPoint.getInstance(extensionValue);
                                try {
                                    BasicConstraints basicConstraints = BasicConstraints.getInstance(getExtensionValue(x509Certificate, BASIC_CONSTRAINTS));
                                    if (issuingDistributionPoint.onlyContainsUserCerts() && basicConstraints != null && basicConstraints.isCA()) {
                                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlOnlyUserCert"));
                                    }
                                    if (issuingDistributionPoint.onlyContainsCACerts() && (basicConstraints == null || !basicConstraints.isCA())) {
                                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlOnlyCaCert"));
                                    }
                                    if (issuingDistributionPoint.onlyContainsAttributeCerts()) {
                                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlOnlyAttrCert"));
                                    }
                                } catch (AnnotatedException e8) {
                                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlBCExtError"), e8);
                                }
                            }
                        } catch (AnnotatedException e9) {
                            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.deltaCrlExtError"));
                        }
                    } catch (AnnotatedException e10) {
                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.distrPtExtError"));
                    }
                } catch (Exception e11) {
                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlVerifyFailed"), e11);
                }
            }
            if (!z) {
                throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noValidCrlFound"));
            }
        } catch (IOException e12) {
            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlIssuerException"), e12);
        }
    }

    protected Vector getCRLDistUrls(CRLDistPoint cRLDistPoint) {
        Vector vector = new Vector();
        if (cRLDistPoint != null) {
            for (DistributionPoint distributionPoint : cRLDistPoint.getDistributionPoints()) {
                DistributionPointName distributionPoint2 = distributionPoint.getDistributionPoint();
                if (distributionPoint2.getType() == 0) {
                    GeneralName[] names = GeneralNames.getInstance(distributionPoint2.getName()).getNames();
                    for (int i = 0; i < names.length; i++) {
                        if (names[i].getTagNo() == 6) {
                            vector.add(((DERIA5String) names[i].getName()).getString());
                        }
                    }
                }
            }
        }
        return vector;
    }

    protected Vector getOCSPUrls(AuthorityInformationAccess authorityInformationAccess) {
        Vector vector = new Vector();
        if (authorityInformationAccess != null) {
            AccessDescription[] accessDescriptions = authorityInformationAccess.getAccessDescriptions();
            for (int i = 0; i < accessDescriptions.length; i++) {
                if (accessDescriptions[i].getAccessMethod().equals(AccessDescription.id_ad_ocsp)) {
                    GeneralName accessLocation = accessDescriptions[i].getAccessLocation();
                    if (accessLocation.getTagNo() == 6) {
                        vector.add(((DERIA5String) accessLocation.getName()).getString());
                    }
                }
            }
        }
        return vector;
    }

    private X509CRL getCRL(String str) throws Exception {
        X509CRL x509crl = null;
        try {
            URL url = new URL(str);
            if (url.getProtocol().equals("http") || url.getProtocol().equals("https")) {
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                if (httpURLConnection.getResponseCode() != 200) {
                    throw new Exception(httpURLConnection.getResponseMessage());
                }
                x509crl = (X509CRL) CertificateFactory.getInstance("X.509", "BC").generateCRL(httpURLConnection.getInputStream());
            }
            return x509crl;
        } catch (Exception e) {
            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.loadCrlDistPointError", new Object[]{new UntrustedInput(str), e.getMessage(), e, e.getClass().getName()}));
        }
    }

    protected Collection getTrustAnchors(X509Certificate x509Certificate, Set set) throws CertPathReviewerException, IOException {
        ArrayList arrayList = new ArrayList();
        Iterator it = set.iterator();
        X509CertSelector x509CertSelector = new X509CertSelector();
        try {
            x509CertSelector.setSubject(getEncodedIssuerPrincipal(x509Certificate).getEncoded());
            byte[] extensionValue = x509Certificate.getExtensionValue(X509Extensions.AuthorityKeyIdentifier.getId());
            if (extensionValue != null) {
                AuthorityKeyIdentifier authorityKeyIdentifier = AuthorityKeyIdentifier.getInstance(ASN1Object.fromByteArray(((ASN1OctetString) ASN1Object.fromByteArray(extensionValue)).getOctets()));
                x509CertSelector.setSerialNumber(authorityKeyIdentifier.getAuthorityCertSerialNumber());
                byte[] keyIdentifier = authorityKeyIdentifier.getKeyIdentifier();
                if (keyIdentifier != null) {
                    x509CertSelector.setSubjectKeyIdentifier(new DEROctetString(keyIdentifier).getEncoded());
                }
            }
            while (it.hasNext()) {
                TrustAnchor trustAnchor = (TrustAnchor) it.next();
                if (trustAnchor.getTrustedCert() != null) {
                    if (x509CertSelector.match(trustAnchor.getTrustedCert())) {
                        arrayList.add(trustAnchor);
                    }
                } else if (trustAnchor.getCAName() != null && trustAnchor.getCAPublicKey() != null && getEncodedIssuerPrincipal(x509Certificate).equals(new X500Principal(trustAnchor.getCAName()))) {
                    arrayList.add(trustAnchor);
                }
            }
            return arrayList;
        } catch (IOException e) {
            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.trustAnchorIssuerError"));
        }
    }
}
