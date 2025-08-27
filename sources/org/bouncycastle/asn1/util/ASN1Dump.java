package org.bouncycastle.asn1.util;

import ch.qos.logback.core.joran.action.ActionConst;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.BERApplicationSpecific;
import org.bouncycastle.asn1.BERConstructedOctetString;
import org.bouncycastle.asn1.BERSequence;
import org.bouncycastle.asn1.BERSet;
import org.bouncycastle.asn1.BERTaggedObject;
import org.bouncycastle.asn1.DERApplicationSpecific;
import org.bouncycastle.asn1.DERBMPString;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERBoolean;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DEREnumerated;
import org.bouncycastle.asn1.DERExternal;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.DERT61String;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERUTCTime;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.DERUnknownTag;
import org.bouncycastle.asn1.DERVisibleString;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.PropertyAccessor;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/util/ASN1Dump.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/util/ASN1Dump.class */
public class ASN1Dump {
    private static final String TAB = "    ";
    private static final int SAMPLE_SIZE = 32;

    static void _dumpAsString(String str, boolean z, DERObject dERObject, StringBuffer stringBuffer) {
        String property = System.getProperty("line.separator");
        if (dERObject instanceof ASN1Sequence) {
            Enumeration objects = ((ASN1Sequence) dERObject).getObjects();
            String str2 = str + TAB;
            stringBuffer.append(str);
            if (dERObject instanceof BERSequence) {
                stringBuffer.append("BER Sequence");
            } else if (dERObject instanceof DERSequence) {
                stringBuffer.append("DER Sequence");
            } else {
                stringBuffer.append("Sequence");
            }
            stringBuffer.append(property);
            while (objects.hasMoreElements()) {
                Object objNextElement = objects.nextElement();
                if (objNextElement == null || objNextElement.equals(new DERNull())) {
                    stringBuffer.append(str2);
                    stringBuffer.append(ActionConst.NULL);
                    stringBuffer.append(property);
                } else if (objNextElement instanceof DERObject) {
                    _dumpAsString(str2, z, (DERObject) objNextElement, stringBuffer);
                } else {
                    _dumpAsString(str2, z, ((DEREncodable) objNextElement).getDERObject(), stringBuffer);
                }
            }
            return;
        }
        if (dERObject instanceof DERTaggedObject) {
            String str3 = str + TAB;
            stringBuffer.append(str);
            if (dERObject instanceof BERTaggedObject) {
                stringBuffer.append("BER Tagged [");
            } else {
                stringBuffer.append("Tagged [");
            }
            DERTaggedObject dERTaggedObject = (DERTaggedObject) dERObject;
            stringBuffer.append(Integer.toString(dERTaggedObject.getTagNo()));
            stringBuffer.append(']');
            if (!dERTaggedObject.isExplicit()) {
                stringBuffer.append(" IMPLICIT ");
            }
            stringBuffer.append(property);
            if (!dERTaggedObject.isEmpty()) {
                _dumpAsString(str3, z, dERTaggedObject.getObject(), stringBuffer);
                return;
            }
            stringBuffer.append(str3);
            stringBuffer.append("EMPTY");
            stringBuffer.append(property);
            return;
        }
        if (dERObject instanceof BERSet) {
            Enumeration objects2 = ((ASN1Set) dERObject).getObjects();
            String str4 = str + TAB;
            stringBuffer.append(str);
            stringBuffer.append("BER Set");
            stringBuffer.append(property);
            while (objects2.hasMoreElements()) {
                Object objNextElement2 = objects2.nextElement();
                if (objNextElement2 == null) {
                    stringBuffer.append(str4);
                    stringBuffer.append(ActionConst.NULL);
                    stringBuffer.append(property);
                } else if (objNextElement2 instanceof DERObject) {
                    _dumpAsString(str4, z, (DERObject) objNextElement2, stringBuffer);
                } else {
                    _dumpAsString(str4, z, ((DEREncodable) objNextElement2).getDERObject(), stringBuffer);
                }
            }
            return;
        }
        if (dERObject instanceof DERSet) {
            Enumeration objects3 = ((ASN1Set) dERObject).getObjects();
            String str5 = str + TAB;
            stringBuffer.append(str);
            stringBuffer.append("DER Set");
            stringBuffer.append(property);
            while (objects3.hasMoreElements()) {
                Object objNextElement3 = objects3.nextElement();
                if (objNextElement3 == null) {
                    stringBuffer.append(str5);
                    stringBuffer.append(ActionConst.NULL);
                    stringBuffer.append(property);
                } else if (objNextElement3 instanceof DERObject) {
                    _dumpAsString(str5, z, (DERObject) objNextElement3, stringBuffer);
                } else {
                    _dumpAsString(str5, z, ((DEREncodable) objNextElement3).getDERObject(), stringBuffer);
                }
            }
            return;
        }
        if (dERObject instanceof DERObjectIdentifier) {
            stringBuffer.append(str + "ObjectIdentifier(" + ((DERObjectIdentifier) dERObject).getId() + ")" + property);
            return;
        }
        if (dERObject instanceof DERBoolean) {
            stringBuffer.append(str + "Boolean(" + ((DERBoolean) dERObject).isTrue() + ")" + property);
            return;
        }
        if (dERObject instanceof DERInteger) {
            stringBuffer.append(str + "Integer(" + ((DERInteger) dERObject).getValue() + ")" + property);
            return;
        }
        if (dERObject instanceof BERConstructedOctetString) {
            ASN1OctetString aSN1OctetString = (ASN1OctetString) dERObject;
            stringBuffer.append(str + "BER Constructed Octet String" + PropertyAccessor.PROPERTY_KEY_PREFIX + aSN1OctetString.getOctets().length + "] ");
            if (z) {
                stringBuffer.append(dumpBinaryDataAsString(str, aSN1OctetString.getOctets()));
                return;
            } else {
                stringBuffer.append(property);
                return;
            }
        }
        if (dERObject instanceof DEROctetString) {
            ASN1OctetString aSN1OctetString2 = (ASN1OctetString) dERObject;
            stringBuffer.append(str + "DER Octet String" + PropertyAccessor.PROPERTY_KEY_PREFIX + aSN1OctetString2.getOctets().length + "] ");
            if (z) {
                stringBuffer.append(dumpBinaryDataAsString(str, aSN1OctetString2.getOctets()));
                return;
            } else {
                stringBuffer.append(property);
                return;
            }
        }
        if (dERObject instanceof DERBitString) {
            DERBitString dERBitString = (DERBitString) dERObject;
            stringBuffer.append(str + "DER Bit String" + PropertyAccessor.PROPERTY_KEY_PREFIX + dERBitString.getBytes().length + ", " + dERBitString.getPadBits() + "] ");
            if (z) {
                stringBuffer.append(dumpBinaryDataAsString(str, dERBitString.getBytes()));
                return;
            } else {
                stringBuffer.append(property);
                return;
            }
        }
        if (dERObject instanceof DERIA5String) {
            stringBuffer.append(str + "IA5String(" + ((DERIA5String) dERObject).getString() + ") " + property);
            return;
        }
        if (dERObject instanceof DERUTF8String) {
            stringBuffer.append(str + "UTF8String(" + ((DERUTF8String) dERObject).getString() + ") " + property);
            return;
        }
        if (dERObject instanceof DERPrintableString) {
            stringBuffer.append(str + "PrintableString(" + ((DERPrintableString) dERObject).getString() + ") " + property);
            return;
        }
        if (dERObject instanceof DERVisibleString) {
            stringBuffer.append(str + "VisibleString(" + ((DERVisibleString) dERObject).getString() + ") " + property);
            return;
        }
        if (dERObject instanceof DERBMPString) {
            stringBuffer.append(str + "BMPString(" + ((DERBMPString) dERObject).getString() + ") " + property);
            return;
        }
        if (dERObject instanceof DERT61String) {
            stringBuffer.append(str + "T61String(" + ((DERT61String) dERObject).getString() + ") " + property);
            return;
        }
        if (dERObject instanceof DERUTCTime) {
            stringBuffer.append(str + "UTCTime(" + ((DERUTCTime) dERObject).getTime() + ") " + property);
            return;
        }
        if (dERObject instanceof DERGeneralizedTime) {
            stringBuffer.append(str + "GeneralizedTime(" + ((DERGeneralizedTime) dERObject).getTime() + ") " + property);
            return;
        }
        if (dERObject instanceof DERUnknownTag) {
            stringBuffer.append(str + "Unknown " + Integer.toString(((DERUnknownTag) dERObject).getTag(), 16) + SymbolConstants.SPACE_SYMBOL + new String(Hex.encode(((DERUnknownTag) dERObject).getData())) + property);
            return;
        }
        if (dERObject instanceof BERApplicationSpecific) {
            stringBuffer.append(outputApplicationSpecific("BER", str, z, dERObject, property));
            return;
        }
        if (dERObject instanceof DERApplicationSpecific) {
            stringBuffer.append(outputApplicationSpecific("DER", str, z, dERObject, property));
            return;
        }
        if (dERObject instanceof DEREnumerated) {
            stringBuffer.append(str + "DER Enumerated(" + ((DEREnumerated) dERObject).getValue() + ")" + property);
            return;
        }
        if (!(dERObject instanceof DERExternal)) {
            stringBuffer.append(str + dERObject.toString() + property);
            return;
        }
        DERExternal dERExternal = (DERExternal) dERObject;
        stringBuffer.append(str + "External " + property);
        String str6 = str + TAB;
        if (dERExternal.getDirectReference() != null) {
            stringBuffer.append(str6 + "Direct Reference: " + dERExternal.getDirectReference().getId() + property);
        }
        if (dERExternal.getIndirectReference() != null) {
            stringBuffer.append(str6 + "Indirect Reference: " + dERExternal.getIndirectReference().toString() + property);
        }
        if (dERExternal.getDataValueDescriptor() != null) {
            _dumpAsString(str6, z, dERExternal.getDataValueDescriptor(), stringBuffer);
        }
        stringBuffer.append(str6 + "Encoding: " + dERExternal.getEncoding() + property);
        _dumpAsString(str6, z, dERExternal.getExternalContent(), stringBuffer);
    }

    private static String outputApplicationSpecific(String str, String str2, boolean z, DERObject dERObject, String str3) {
        DERApplicationSpecific dERApplicationSpecific = (DERApplicationSpecific) dERObject;
        StringBuffer stringBuffer = new StringBuffer();
        if (!dERApplicationSpecific.isConstructed()) {
            return str2 + str + " ApplicationSpecific[" + dERApplicationSpecific.getApplicationTag() + "] (" + new String(Hex.encode(dERApplicationSpecific.getContents())) + ")" + str3;
        }
        try {
            ASN1Sequence aSN1Sequence = ASN1Sequence.getInstance(dERApplicationSpecific.getObject(16));
            stringBuffer.append(str2 + str + " ApplicationSpecific[" + dERApplicationSpecific.getApplicationTag() + "]" + str3);
            Enumeration objects = aSN1Sequence.getObjects();
            while (objects.hasMoreElements()) {
                _dumpAsString(str2 + TAB, z, (DERObject) objects.nextElement(), stringBuffer);
            }
        } catch (IOException e) {
            stringBuffer.append(e);
        }
        return stringBuffer.toString();
    }

    public static String dumpAsString(Object obj) {
        return dumpAsString(obj, false);
    }

    public static String dumpAsString(Object obj, boolean z) {
        StringBuffer stringBuffer = new StringBuffer();
        if (obj instanceof DERObject) {
            _dumpAsString("", z, (DERObject) obj, stringBuffer);
        } else {
            if (!(obj instanceof DEREncodable)) {
                return "unknown object type " + obj.toString();
            }
            _dumpAsString("", z, ((DEREncodable) obj).getDERObject(), stringBuffer);
        }
        return stringBuffer.toString();
    }

    private static String dumpBinaryDataAsString(String str, byte[] bArr) {
        String property = System.getProperty("line.separator");
        StringBuffer stringBuffer = new StringBuffer();
        String str2 = str + TAB;
        stringBuffer.append(property);
        for (int i = 0; i < bArr.length; i += 32) {
            if (bArr.length - i > 32) {
                stringBuffer.append(str2);
                stringBuffer.append(new String(Hex.encode(bArr, i, 32)));
                stringBuffer.append(TAB);
                stringBuffer.append(calculateAscString(bArr, i, 32));
                stringBuffer.append(property);
            } else {
                stringBuffer.append(str2);
                stringBuffer.append(new String(Hex.encode(bArr, i, bArr.length - i)));
                for (int length = bArr.length - i; length != 32; length++) {
                    stringBuffer.append("  ");
                }
                stringBuffer.append(TAB);
                stringBuffer.append(calculateAscString(bArr, i, bArr.length - i));
                stringBuffer.append(property);
            }
        }
        return stringBuffer.toString();
    }

    private static String calculateAscString(byte[] bArr, int i, int i2) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i3 = i; i3 != i + i2; i3++) {
            if (bArr[i3] >= 32 && bArr[i3] <= 126) {
                stringBuffer.append((char) bArr[i3]);
            }
        }
        return stringBuffer.toString();
    }
}
