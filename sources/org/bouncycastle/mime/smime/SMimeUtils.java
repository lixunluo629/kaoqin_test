package org.bouncycastle.mime.smime;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.cms.CMSAlgorithm;
import org.bouncycastle.util.Strings;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/mime/smime/SMimeUtils.class */
class SMimeUtils {
    private static final Map RFC5751_MICALGS;
    private static final Map RFC3851_MICALGS;
    private static final Map STANDARD_MICALGS;
    private static final Map forMic;
    private static final byte[] nl = new byte[2];

    SMimeUtils() {
    }

    static String lessQuotes(String str) {
        return (str == null || str.length() <= 1) ? str : (str.charAt(0) == '\"' && str.charAt(str.length() - 1) == '\"') ? str.substring(1, str.length() - 1) : str;
    }

    static String getParameter(String str, List<String> list) {
        for (String str2 : list) {
            if (str2.startsWith(str)) {
                return str2;
            }
        }
        return null;
    }

    static ASN1ObjectIdentifier getDigestOID(String str) {
        ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier) forMic.get(Strings.toLowerCase(str));
        if (aSN1ObjectIdentifier == null) {
            throw new IllegalArgumentException("unknown micalg passed: " + str);
        }
        return aSN1ObjectIdentifier;
    }

    static OutputStream createUnclosable(OutputStream outputStream) {
        return new FilterOutputStream(outputStream) { // from class: org.bouncycastle.mime.smime.SMimeUtils.1
            @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
            }
        };
    }

    static {
        nl[0] = 13;
        nl[1] = 10;
        HashMap map = new HashMap();
        map.put(CMSAlgorithm.MD5, "md5");
        map.put(CMSAlgorithm.SHA1, "sha-1");
        map.put(CMSAlgorithm.SHA224, "sha-224");
        map.put(CMSAlgorithm.SHA256, "sha-256");
        map.put(CMSAlgorithm.SHA384, "sha-384");
        map.put(CMSAlgorithm.SHA512, "sha-512");
        map.put(CMSAlgorithm.GOST3411, "gostr3411-94");
        map.put(CMSAlgorithm.GOST3411_2012_256, "gostr3411-2012-256");
        map.put(CMSAlgorithm.GOST3411_2012_512, "gostr3411-2012-512");
        RFC5751_MICALGS = Collections.unmodifiableMap(map);
        HashMap map2 = new HashMap();
        map2.put(CMSAlgorithm.MD5, "md5");
        map2.put(CMSAlgorithm.SHA1, "sha1");
        map2.put(CMSAlgorithm.SHA224, "sha224");
        map2.put(CMSAlgorithm.SHA256, "sha256");
        map2.put(CMSAlgorithm.SHA384, "sha384");
        map2.put(CMSAlgorithm.SHA512, "sha512");
        map2.put(CMSAlgorithm.GOST3411, "gostr3411-94");
        map2.put(CMSAlgorithm.GOST3411_2012_256, "gostr3411-2012-256");
        map2.put(CMSAlgorithm.GOST3411_2012_512, "gostr3411-2012-512");
        RFC3851_MICALGS = Collections.unmodifiableMap(map2);
        STANDARD_MICALGS = RFC5751_MICALGS;
        TreeMap treeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        for (Object obj : STANDARD_MICALGS.keySet()) {
            treeMap.put(STANDARD_MICALGS.get(obj).toString(), (ASN1ObjectIdentifier) obj);
        }
        for (Object obj2 : RFC3851_MICALGS.keySet()) {
            treeMap.put(RFC3851_MICALGS.get(obj2).toString(), (ASN1ObjectIdentifier) obj2);
        }
        forMic = Collections.unmodifiableMap(treeMap);
    }
}
