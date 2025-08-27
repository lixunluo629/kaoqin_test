package org.bouncycastle.asn1.x509;

import java.util.Enumeration;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERUTF8String;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/x509/IetfAttrSyntax.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/x509/IetfAttrSyntax.class */
public class IetfAttrSyntax extends ASN1Encodable {
    public static final int VALUE_OCTETS = 1;
    public static final int VALUE_OID = 2;
    public static final int VALUE_UTF8 = 3;
    GeneralNames policyAuthority;
    Vector values = new Vector();
    int valueChoice;

    public IetfAttrSyntax(ASN1Sequence aSN1Sequence) {
        int i;
        this.policyAuthority = null;
        this.valueChoice = -1;
        int i2 = 0;
        if (aSN1Sequence.getObjectAt(0) instanceof ASN1TaggedObject) {
            this.policyAuthority = GeneralNames.getInstance((ASN1TaggedObject) aSN1Sequence.getObjectAt(0), false);
            i2 = 0 + 1;
        } else if (aSN1Sequence.size() == 2) {
            this.policyAuthority = GeneralNames.getInstance(aSN1Sequence.getObjectAt(0));
            i2 = 0 + 1;
        }
        if (!(aSN1Sequence.getObjectAt(i2) instanceof ASN1Sequence)) {
            throw new IllegalArgumentException("Non-IetfAttrSyntax encoding");
        }
        Enumeration objects = ((ASN1Sequence) aSN1Sequence.getObjectAt(i2)).getObjects();
        while (objects.hasMoreElements()) {
            DERObject dERObject = (DERObject) objects.nextElement();
            if (dERObject instanceof DERObjectIdentifier) {
                i = 2;
            } else if (dERObject instanceof DERUTF8String) {
                i = 3;
            } else {
                if (!(dERObject instanceof DEROctetString)) {
                    throw new IllegalArgumentException("Bad value type encoding IetfAttrSyntax");
                }
                i = 1;
            }
            if (this.valueChoice < 0) {
                this.valueChoice = i;
            }
            if (i != this.valueChoice) {
                throw new IllegalArgumentException("Mix of value types in IetfAttrSyntax");
            }
            this.values.addElement(dERObject);
        }
    }

    public GeneralNames getPolicyAuthority() {
        return this.policyAuthority;
    }

    public int getValueType() {
        return this.valueChoice;
    }

    public Object[] getValues() {
        if (getValueType() == 1) {
            ASN1OctetString[] aSN1OctetStringArr = new ASN1OctetString[this.values.size()];
            for (int i = 0; i != aSN1OctetStringArr.length; i++) {
                aSN1OctetStringArr[i] = (ASN1OctetString) this.values.elementAt(i);
            }
            return aSN1OctetStringArr;
        }
        if (getValueType() == 2) {
            DERObjectIdentifier[] dERObjectIdentifierArr = new DERObjectIdentifier[this.values.size()];
            for (int i2 = 0; i2 != dERObjectIdentifierArr.length; i2++) {
                dERObjectIdentifierArr[i2] = (DERObjectIdentifier) this.values.elementAt(i2);
            }
            return dERObjectIdentifierArr;
        }
        DERUTF8String[] dERUTF8StringArr = new DERUTF8String[this.values.size()];
        for (int i3 = 0; i3 != dERUTF8StringArr.length; i3++) {
            dERUTF8StringArr[i3] = (DERUTF8String) this.values.elementAt(i3);
        }
        return dERUTF8StringArr;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.policyAuthority != null) {
            aSN1EncodableVector.add(new DERTaggedObject(0, this.policyAuthority));
        }
        ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
        Enumeration enumerationElements = this.values.elements();
        while (enumerationElements.hasMoreElements()) {
            aSN1EncodableVector2.add((ASN1Encodable) enumerationElements.nextElement());
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector2));
        return new DERSequence(aSN1EncodableVector);
    }
}
