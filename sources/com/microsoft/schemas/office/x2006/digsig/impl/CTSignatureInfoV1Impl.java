package com.microsoft.schemas.office.x2006.digsig.impl;

import com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1;
import com.microsoft.schemas.office.x2006.digsig.STPositiveInteger;
import com.microsoft.schemas.office.x2006.digsig.STSignatureComments;
import com.microsoft.schemas.office.x2006.digsig.STSignatureProviderUrl;
import com.microsoft.schemas.office.x2006.digsig.STSignatureText;
import com.microsoft.schemas.office.x2006.digsig.STSignatureType;
import com.microsoft.schemas.office.x2006.digsig.STUniqueIdentifierWithBraces;
import com.microsoft.schemas.office.x2006.digsig.STVersion;
import javax.xml.namespace.QName;
import org.apache.poi.poifs.crypt.dsig.facets.SignatureFacet;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.XmlBase64Binary;
import org.apache.xmlbeans.XmlInt;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/x2006/digsig/impl/CTSignatureInfoV1Impl.class */
public class CTSignatureInfoV1Impl extends XmlComplexContentImpl implements CTSignatureInfoV1 {
    private static final QName SETUPID$0 = new QName(SignatureFacet.MS_DIGSIG_NS, "SetupID");
    private static final QName SIGNATURETEXT$2 = new QName(SignatureFacet.MS_DIGSIG_NS, "SignatureText");
    private static final QName SIGNATUREIMAGE$4 = new QName(SignatureFacet.MS_DIGSIG_NS, "SignatureImage");
    private static final QName SIGNATURECOMMENTS$6 = new QName(SignatureFacet.MS_DIGSIG_NS, "SignatureComments");
    private static final QName WINDOWSVERSION$8 = new QName(SignatureFacet.MS_DIGSIG_NS, "WindowsVersion");
    private static final QName OFFICEVERSION$10 = new QName(SignatureFacet.MS_DIGSIG_NS, "OfficeVersion");
    private static final QName APPLICATIONVERSION$12 = new QName(SignatureFacet.MS_DIGSIG_NS, "ApplicationVersion");
    private static final QName MONITORS$14 = new QName(SignatureFacet.MS_DIGSIG_NS, "Monitors");
    private static final QName HORIZONTALRESOLUTION$16 = new QName(SignatureFacet.MS_DIGSIG_NS, "HorizontalResolution");
    private static final QName VERTICALRESOLUTION$18 = new QName(SignatureFacet.MS_DIGSIG_NS, "VerticalResolution");
    private static final QName COLORDEPTH$20 = new QName(SignatureFacet.MS_DIGSIG_NS, "ColorDepth");
    private static final QName SIGNATUREPROVIDERID$22 = new QName(SignatureFacet.MS_DIGSIG_NS, "SignatureProviderId");
    private static final QName SIGNATUREPROVIDERURL$24 = new QName(SignatureFacet.MS_DIGSIG_NS, "SignatureProviderUrl");
    private static final QName SIGNATUREPROVIDERDETAILS$26 = new QName(SignatureFacet.MS_DIGSIG_NS, "SignatureProviderDetails");
    private static final QName SIGNATURETYPE$28 = new QName(SignatureFacet.MS_DIGSIG_NS, "SignatureType");
    private static final QName DELEGATESUGGESTEDSIGNER$30 = new QName(SignatureFacet.MS_DIGSIG_NS, "DelegateSuggestedSigner");
    private static final QName DELEGATESUGGESTEDSIGNER2$32 = new QName(SignatureFacet.MS_DIGSIG_NS, "DelegateSuggestedSigner2");
    private static final QName DELEGATESUGGESTEDSIGNEREMAIL$34 = new QName(SignatureFacet.MS_DIGSIG_NS, "DelegateSuggestedSignerEmail");
    private static final QName MANIFESTHASHALGORITHM$36 = new QName(SignatureFacet.MS_DIGSIG_NS, "ManifestHashAlgorithm");

    public CTSignatureInfoV1Impl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public String getSetupID() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(SETUPID$0, 0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public STUniqueIdentifierWithBraces xgetSetupID() {
        STUniqueIdentifierWithBraces sTUniqueIdentifierWithBracesFind_element_user;
        synchronized (monitor()) {
            check_orphaned();
            sTUniqueIdentifierWithBracesFind_element_user = get_store().find_element_user(SETUPID$0, 0);
        }
        return sTUniqueIdentifierWithBracesFind_element_user;
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void setSetupID(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(SETUPID$0, 0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_element_user(SETUPID$0);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void xsetSetupID(STUniqueIdentifierWithBraces sTUniqueIdentifierWithBraces) {
        synchronized (monitor()) {
            check_orphaned();
            STUniqueIdentifierWithBraces sTUniqueIdentifierWithBracesFind_element_user = get_store().find_element_user(SETUPID$0, 0);
            if (sTUniqueIdentifierWithBracesFind_element_user == null) {
                sTUniqueIdentifierWithBracesFind_element_user = (STUniqueIdentifierWithBraces) get_store().add_element_user(SETUPID$0);
            }
            sTUniqueIdentifierWithBracesFind_element_user.set(sTUniqueIdentifierWithBraces);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public String getSignatureText() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(SIGNATURETEXT$2, 0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public STSignatureText xgetSignatureText() {
        STSignatureText sTSignatureTextFind_element_user;
        synchronized (monitor()) {
            check_orphaned();
            sTSignatureTextFind_element_user = get_store().find_element_user(SIGNATURETEXT$2, 0);
        }
        return sTSignatureTextFind_element_user;
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void setSignatureText(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(SIGNATURETEXT$2, 0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_element_user(SIGNATURETEXT$2);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void xsetSignatureText(STSignatureText sTSignatureText) {
        synchronized (monitor()) {
            check_orphaned();
            STSignatureText sTSignatureTextFind_element_user = get_store().find_element_user(SIGNATURETEXT$2, 0);
            if (sTSignatureTextFind_element_user == null) {
                sTSignatureTextFind_element_user = (STSignatureText) get_store().add_element_user(SIGNATURETEXT$2);
            }
            sTSignatureTextFind_element_user.set(sTSignatureText);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public byte[] getSignatureImage() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(SIGNATUREIMAGE$4, 0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getByteArrayValue();
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public XmlBase64Binary xgetSignatureImage() {
        XmlBase64Binary xmlBase64Binary;
        synchronized (monitor()) {
            check_orphaned();
            xmlBase64Binary = (XmlBase64Binary) get_store().find_element_user(SIGNATUREIMAGE$4, 0);
        }
        return xmlBase64Binary;
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void setSignatureImage(byte[] bArr) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(SIGNATUREIMAGE$4, 0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_element_user(SIGNATUREIMAGE$4);
            }
            simpleValue.setByteArrayValue(bArr);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void xsetSignatureImage(XmlBase64Binary xmlBase64Binary) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBase64Binary xmlBase64Binary2 = (XmlBase64Binary) get_store().find_element_user(SIGNATUREIMAGE$4, 0);
            if (xmlBase64Binary2 == null) {
                xmlBase64Binary2 = (XmlBase64Binary) get_store().add_element_user(SIGNATUREIMAGE$4);
            }
            xmlBase64Binary2.set(xmlBase64Binary);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public String getSignatureComments() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(SIGNATURECOMMENTS$6, 0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public STSignatureComments xgetSignatureComments() {
        STSignatureComments sTSignatureCommentsFind_element_user;
        synchronized (monitor()) {
            check_orphaned();
            sTSignatureCommentsFind_element_user = get_store().find_element_user(SIGNATURECOMMENTS$6, 0);
        }
        return sTSignatureCommentsFind_element_user;
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void setSignatureComments(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(SIGNATURECOMMENTS$6, 0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_element_user(SIGNATURECOMMENTS$6);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void xsetSignatureComments(STSignatureComments sTSignatureComments) {
        synchronized (monitor()) {
            check_orphaned();
            STSignatureComments sTSignatureCommentsFind_element_user = get_store().find_element_user(SIGNATURECOMMENTS$6, 0);
            if (sTSignatureCommentsFind_element_user == null) {
                sTSignatureCommentsFind_element_user = (STSignatureComments) get_store().add_element_user(SIGNATURECOMMENTS$6);
            }
            sTSignatureCommentsFind_element_user.set(sTSignatureComments);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public String getWindowsVersion() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(WINDOWSVERSION$8, 0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public STVersion xgetWindowsVersion() {
        STVersion sTVersionFind_element_user;
        synchronized (monitor()) {
            check_orphaned();
            sTVersionFind_element_user = get_store().find_element_user(WINDOWSVERSION$8, 0);
        }
        return sTVersionFind_element_user;
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void setWindowsVersion(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(WINDOWSVERSION$8, 0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_element_user(WINDOWSVERSION$8);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void xsetWindowsVersion(STVersion sTVersion) {
        synchronized (monitor()) {
            check_orphaned();
            STVersion sTVersionFind_element_user = get_store().find_element_user(WINDOWSVERSION$8, 0);
            if (sTVersionFind_element_user == null) {
                sTVersionFind_element_user = (STVersion) get_store().add_element_user(WINDOWSVERSION$8);
            }
            sTVersionFind_element_user.set(sTVersion);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public String getOfficeVersion() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(OFFICEVERSION$10, 0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public STVersion xgetOfficeVersion() {
        STVersion sTVersionFind_element_user;
        synchronized (monitor()) {
            check_orphaned();
            sTVersionFind_element_user = get_store().find_element_user(OFFICEVERSION$10, 0);
        }
        return sTVersionFind_element_user;
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void setOfficeVersion(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(OFFICEVERSION$10, 0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_element_user(OFFICEVERSION$10);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void xsetOfficeVersion(STVersion sTVersion) {
        synchronized (monitor()) {
            check_orphaned();
            STVersion sTVersionFind_element_user = get_store().find_element_user(OFFICEVERSION$10, 0);
            if (sTVersionFind_element_user == null) {
                sTVersionFind_element_user = (STVersion) get_store().add_element_user(OFFICEVERSION$10);
            }
            sTVersionFind_element_user.set(sTVersion);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public String getApplicationVersion() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(APPLICATIONVERSION$12, 0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public STVersion xgetApplicationVersion() {
        STVersion sTVersionFind_element_user;
        synchronized (monitor()) {
            check_orphaned();
            sTVersionFind_element_user = get_store().find_element_user(APPLICATIONVERSION$12, 0);
        }
        return sTVersionFind_element_user;
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void setApplicationVersion(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(APPLICATIONVERSION$12, 0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_element_user(APPLICATIONVERSION$12);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void xsetApplicationVersion(STVersion sTVersion) {
        synchronized (monitor()) {
            check_orphaned();
            STVersion sTVersionFind_element_user = get_store().find_element_user(APPLICATIONVERSION$12, 0);
            if (sTVersionFind_element_user == null) {
                sTVersionFind_element_user = (STVersion) get_store().add_element_user(APPLICATIONVERSION$12);
            }
            sTVersionFind_element_user.set(sTVersion);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public int getMonitors() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(MONITORS$14, 0);
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public STPositiveInteger xgetMonitors() {
        STPositiveInteger sTPositiveIntegerFind_element_user;
        synchronized (monitor()) {
            check_orphaned();
            sTPositiveIntegerFind_element_user = get_store().find_element_user(MONITORS$14, 0);
        }
        return sTPositiveIntegerFind_element_user;
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void setMonitors(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(MONITORS$14, 0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_element_user(MONITORS$14);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void xsetMonitors(STPositiveInteger sTPositiveInteger) {
        synchronized (monitor()) {
            check_orphaned();
            STPositiveInteger sTPositiveIntegerFind_element_user = get_store().find_element_user(MONITORS$14, 0);
            if (sTPositiveIntegerFind_element_user == null) {
                sTPositiveIntegerFind_element_user = (STPositiveInteger) get_store().add_element_user(MONITORS$14);
            }
            sTPositiveIntegerFind_element_user.set(sTPositiveInteger);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public int getHorizontalResolution() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(HORIZONTALRESOLUTION$16, 0);
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public STPositiveInteger xgetHorizontalResolution() {
        STPositiveInteger sTPositiveIntegerFind_element_user;
        synchronized (monitor()) {
            check_orphaned();
            sTPositiveIntegerFind_element_user = get_store().find_element_user(HORIZONTALRESOLUTION$16, 0);
        }
        return sTPositiveIntegerFind_element_user;
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void setHorizontalResolution(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(HORIZONTALRESOLUTION$16, 0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_element_user(HORIZONTALRESOLUTION$16);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void xsetHorizontalResolution(STPositiveInteger sTPositiveInteger) {
        synchronized (monitor()) {
            check_orphaned();
            STPositiveInteger sTPositiveIntegerFind_element_user = get_store().find_element_user(HORIZONTALRESOLUTION$16, 0);
            if (sTPositiveIntegerFind_element_user == null) {
                sTPositiveIntegerFind_element_user = (STPositiveInteger) get_store().add_element_user(HORIZONTALRESOLUTION$16);
            }
            sTPositiveIntegerFind_element_user.set(sTPositiveInteger);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public int getVerticalResolution() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(VERTICALRESOLUTION$18, 0);
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public STPositiveInteger xgetVerticalResolution() {
        STPositiveInteger sTPositiveIntegerFind_element_user;
        synchronized (monitor()) {
            check_orphaned();
            sTPositiveIntegerFind_element_user = get_store().find_element_user(VERTICALRESOLUTION$18, 0);
        }
        return sTPositiveIntegerFind_element_user;
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void setVerticalResolution(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(VERTICALRESOLUTION$18, 0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_element_user(VERTICALRESOLUTION$18);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void xsetVerticalResolution(STPositiveInteger sTPositiveInteger) {
        synchronized (monitor()) {
            check_orphaned();
            STPositiveInteger sTPositiveIntegerFind_element_user = get_store().find_element_user(VERTICALRESOLUTION$18, 0);
            if (sTPositiveIntegerFind_element_user == null) {
                sTPositiveIntegerFind_element_user = (STPositiveInteger) get_store().add_element_user(VERTICALRESOLUTION$18);
            }
            sTPositiveIntegerFind_element_user.set(sTPositiveInteger);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public int getColorDepth() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(COLORDEPTH$20, 0);
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public STPositiveInteger xgetColorDepth() {
        STPositiveInteger sTPositiveIntegerFind_element_user;
        synchronized (monitor()) {
            check_orphaned();
            sTPositiveIntegerFind_element_user = get_store().find_element_user(COLORDEPTH$20, 0);
        }
        return sTPositiveIntegerFind_element_user;
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void setColorDepth(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(COLORDEPTH$20, 0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_element_user(COLORDEPTH$20);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void xsetColorDepth(STPositiveInteger sTPositiveInteger) {
        synchronized (monitor()) {
            check_orphaned();
            STPositiveInteger sTPositiveIntegerFind_element_user = get_store().find_element_user(COLORDEPTH$20, 0);
            if (sTPositiveIntegerFind_element_user == null) {
                sTPositiveIntegerFind_element_user = (STPositiveInteger) get_store().add_element_user(COLORDEPTH$20);
            }
            sTPositiveIntegerFind_element_user.set(sTPositiveInteger);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public String getSignatureProviderId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(SIGNATUREPROVIDERID$22, 0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public STUniqueIdentifierWithBraces xgetSignatureProviderId() {
        STUniqueIdentifierWithBraces sTUniqueIdentifierWithBracesFind_element_user;
        synchronized (monitor()) {
            check_orphaned();
            sTUniqueIdentifierWithBracesFind_element_user = get_store().find_element_user(SIGNATUREPROVIDERID$22, 0);
        }
        return sTUniqueIdentifierWithBracesFind_element_user;
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void setSignatureProviderId(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(SIGNATUREPROVIDERID$22, 0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_element_user(SIGNATUREPROVIDERID$22);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void xsetSignatureProviderId(STUniqueIdentifierWithBraces sTUniqueIdentifierWithBraces) {
        synchronized (monitor()) {
            check_orphaned();
            STUniqueIdentifierWithBraces sTUniqueIdentifierWithBracesFind_element_user = get_store().find_element_user(SIGNATUREPROVIDERID$22, 0);
            if (sTUniqueIdentifierWithBracesFind_element_user == null) {
                sTUniqueIdentifierWithBracesFind_element_user = (STUniqueIdentifierWithBraces) get_store().add_element_user(SIGNATUREPROVIDERID$22);
            }
            sTUniqueIdentifierWithBracesFind_element_user.set(sTUniqueIdentifierWithBraces);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public String getSignatureProviderUrl() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(SIGNATUREPROVIDERURL$24, 0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public STSignatureProviderUrl xgetSignatureProviderUrl() {
        STSignatureProviderUrl sTSignatureProviderUrlFind_element_user;
        synchronized (monitor()) {
            check_orphaned();
            sTSignatureProviderUrlFind_element_user = get_store().find_element_user(SIGNATUREPROVIDERURL$24, 0);
        }
        return sTSignatureProviderUrlFind_element_user;
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void setSignatureProviderUrl(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(SIGNATUREPROVIDERURL$24, 0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_element_user(SIGNATUREPROVIDERURL$24);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void xsetSignatureProviderUrl(STSignatureProviderUrl sTSignatureProviderUrl) {
        synchronized (monitor()) {
            check_orphaned();
            STSignatureProviderUrl sTSignatureProviderUrlFind_element_user = get_store().find_element_user(SIGNATUREPROVIDERURL$24, 0);
            if (sTSignatureProviderUrlFind_element_user == null) {
                sTSignatureProviderUrlFind_element_user = (STSignatureProviderUrl) get_store().add_element_user(SIGNATUREPROVIDERURL$24);
            }
            sTSignatureProviderUrlFind_element_user.set(sTSignatureProviderUrl);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public int getSignatureProviderDetails() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(SIGNATUREPROVIDERDETAILS$26, 0);
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public XmlInt xgetSignatureProviderDetails() {
        XmlInt xmlInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlInt = (XmlInt) get_store().find_element_user(SIGNATUREPROVIDERDETAILS$26, 0);
        }
        return xmlInt;
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void setSignatureProviderDetails(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(SIGNATUREPROVIDERDETAILS$26, 0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_element_user(SIGNATUREPROVIDERDETAILS$26);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void xsetSignatureProviderDetails(XmlInt xmlInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlInt xmlInt2 = (XmlInt) get_store().find_element_user(SIGNATUREPROVIDERDETAILS$26, 0);
            if (xmlInt2 == null) {
                xmlInt2 = (XmlInt) get_store().add_element_user(SIGNATUREPROVIDERDETAILS$26);
            }
            xmlInt2.set(xmlInt);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public int getSignatureType() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(SIGNATURETYPE$28, 0);
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public STSignatureType xgetSignatureType() {
        STSignatureType sTSignatureTypeFind_element_user;
        synchronized (monitor()) {
            check_orphaned();
            sTSignatureTypeFind_element_user = get_store().find_element_user(SIGNATURETYPE$28, 0);
        }
        return sTSignatureTypeFind_element_user;
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void setSignatureType(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(SIGNATURETYPE$28, 0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_element_user(SIGNATURETYPE$28);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void xsetSignatureType(STSignatureType sTSignatureType) {
        synchronized (monitor()) {
            check_orphaned();
            STSignatureType sTSignatureTypeFind_element_user = get_store().find_element_user(SIGNATURETYPE$28, 0);
            if (sTSignatureTypeFind_element_user == null) {
                sTSignatureTypeFind_element_user = (STSignatureType) get_store().add_element_user(SIGNATURETYPE$28);
            }
            sTSignatureTypeFind_element_user.set(sTSignatureType);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public String getDelegateSuggestedSigner() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(DELEGATESUGGESTEDSIGNER$30, 0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public XmlString xgetDelegateSuggestedSigner() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_element_user(DELEGATESUGGESTEDSIGNER$30, 0);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public boolean isSetDelegateSuggestedSigner() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(DELEGATESUGGESTEDSIGNER$30) != 0;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void setDelegateSuggestedSigner(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(DELEGATESUGGESTEDSIGNER$30, 0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_element_user(DELEGATESUGGESTEDSIGNER$30);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void xsetDelegateSuggestedSigner(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_element_user(DELEGATESUGGESTEDSIGNER$30, 0);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_element_user(DELEGATESUGGESTEDSIGNER$30);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void unsetDelegateSuggestedSigner() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(DELEGATESUGGESTEDSIGNER$30, 0);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public String getDelegateSuggestedSigner2() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(DELEGATESUGGESTEDSIGNER2$32, 0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public XmlString xgetDelegateSuggestedSigner2() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_element_user(DELEGATESUGGESTEDSIGNER2$32, 0);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public boolean isSetDelegateSuggestedSigner2() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(DELEGATESUGGESTEDSIGNER2$32) != 0;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void setDelegateSuggestedSigner2(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(DELEGATESUGGESTEDSIGNER2$32, 0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_element_user(DELEGATESUGGESTEDSIGNER2$32);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void xsetDelegateSuggestedSigner2(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_element_user(DELEGATESUGGESTEDSIGNER2$32, 0);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_element_user(DELEGATESUGGESTEDSIGNER2$32);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void unsetDelegateSuggestedSigner2() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(DELEGATESUGGESTEDSIGNER2$32, 0);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public String getDelegateSuggestedSignerEmail() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(DELEGATESUGGESTEDSIGNEREMAIL$34, 0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public XmlString xgetDelegateSuggestedSignerEmail() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_element_user(DELEGATESUGGESTEDSIGNEREMAIL$34, 0);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public boolean isSetDelegateSuggestedSignerEmail() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(DELEGATESUGGESTEDSIGNEREMAIL$34) != 0;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void setDelegateSuggestedSignerEmail(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(DELEGATESUGGESTEDSIGNEREMAIL$34, 0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_element_user(DELEGATESUGGESTEDSIGNEREMAIL$34);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void xsetDelegateSuggestedSignerEmail(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_element_user(DELEGATESUGGESTEDSIGNEREMAIL$34, 0);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_element_user(DELEGATESUGGESTEDSIGNEREMAIL$34);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void unsetDelegateSuggestedSignerEmail() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(DELEGATESUGGESTEDSIGNEREMAIL$34, 0);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public String getManifestHashAlgorithm() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(MANIFESTHASHALGORITHM$36, 0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public XmlAnyURI xgetManifestHashAlgorithm() {
        XmlAnyURI xmlAnyURI;
        synchronized (monitor()) {
            check_orphaned();
            xmlAnyURI = (XmlAnyURI) get_store().find_element_user(MANIFESTHASHALGORITHM$36, 0);
        }
        return xmlAnyURI;
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public boolean isSetManifestHashAlgorithm() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(MANIFESTHASHALGORITHM$36) != 0;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void setManifestHashAlgorithm(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(MANIFESTHASHALGORITHM$36, 0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_element_user(MANIFESTHASHALGORITHM$36);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void xsetManifestHashAlgorithm(XmlAnyURI xmlAnyURI) {
        synchronized (monitor()) {
            check_orphaned();
            XmlAnyURI xmlAnyURI2 = (XmlAnyURI) get_store().find_element_user(MANIFESTHASHALGORITHM$36, 0);
            if (xmlAnyURI2 == null) {
                xmlAnyURI2 = (XmlAnyURI) get_store().add_element_user(MANIFESTHASHALGORITHM$36);
            }
            xmlAnyURI2.set(xmlAnyURI);
        }
    }

    @Override // com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1
    public void unsetManifestHashAlgorithm() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(MANIFESTHASHALGORITHM$36, 0);
        }
    }
}
