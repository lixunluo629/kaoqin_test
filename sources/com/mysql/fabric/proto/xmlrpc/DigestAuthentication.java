package com.mysql.fabric.proto.xmlrpc;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.naming.ResourceRef;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/fabric/proto/xmlrpc/DigestAuthentication.class */
public class DigestAuthentication {
    private static Random random = new Random();

    public static String getChallengeHeader(String url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setDoOutput(true);
        conn.getOutputStream().close();
        try {
            conn.getInputStream().close();
            return null;
        } catch (IOException ex) {
            if (401 != conn.getResponseCode()) {
                if (400 == conn.getResponseCode()) {
                    throw new IOException("Fabric returns status 400. If authentication is disabled on the Fabric node, omit the `fabricUsername' and `fabricPassword' properties from your connection.");
                }
                throw ex;
            }
            String hdr = conn.getHeaderField("WWW-Authenticate");
            if (hdr != null && !"".equals(hdr)) {
                return hdr;
            }
            return null;
        }
    }

    public static String calculateMD5RequestDigest(String uri, String username, String password, String realm, String nonce, String nc, String cnonce, String qop) throws NoSuchAlgorithmException {
        String reqA1 = username + ":" + realm + ":" + password;
        String reqA2 = "POST:" + uri;
        String hashA1 = checksumMD5(reqA1);
        String hashA2 = checksumMD5(reqA2);
        String requestDigest = digestMD5(hashA1, nonce + ":" + nc + ":" + cnonce + ":" + qop + ":" + hashA2);
        return requestDigest;
    }

    private static String checksumMD5(String data) throws NoSuchAlgorithmException {
        try {
            MessageDigest md5 = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
            return hexEncode(md5.digest(data.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Unable to create MD5 instance", ex);
        }
    }

    private static String digestMD5(String secret, String data) {
        return checksumMD5(secret + ":" + data);
    }

    private static String hexEncode(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02x", Byte.valueOf(b)));
        }
        return sb.toString();
    }

    public static String serializeDigestResponse(Map<String, String> paramMap) {
        StringBuilder sb = new StringBuilder("Digest ");
        boolean prefixComma = false;
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            if (!prefixComma) {
                prefixComma = true;
            } else {
                sb.append(", ");
            }
            sb.append(entry.getKey());
            sb.append(SymbolConstants.EQUAL_SYMBOL);
            sb.append(entry.getValue());
        }
        return sb.toString();
    }

    public static Map<String, String> parseDigestChallenge(String headerValue) {
        if (!headerValue.startsWith("Digest ")) {
            throw new IllegalArgumentException("Header is not a digest challenge");
        }
        String params = headerValue.substring(7);
        Map<String, String> paramMap = new HashMap<>();
        String[] arr$ = params.split(",\\s*");
        for (String param : arr$) {
            String[] pieces = param.split(SymbolConstants.EQUAL_SYMBOL);
            paramMap.put(pieces[0], pieces[1].replaceAll("^\"(.*)\"$", "$1"));
        }
        return paramMap;
    }

    public static String generateCnonce(String nonce, String nc) throws NoSuchAlgorithmException {
        byte[] buf = new byte[8];
        random.nextBytes(buf);
        for (int i = 0; i < 8; i++) {
            buf[i] = (byte) (32 + (buf[i] % 95));
        }
        String combo = String.format("%s:%s:%s:%s", nonce, nc, new Date().toGMTString(), new String(buf));
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            return hexEncode(sha1.digest(combo.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Unable to create SHA-1 instance", ex);
        }
    }

    private static String quoteParam(String param) {
        if (param.contains(SymbolConstants.QUOTES_SYMBOL) || param.contains("'")) {
            throw new IllegalArgumentException("Invalid character in parameter");
        }
        return SymbolConstants.QUOTES_SYMBOL + param + SymbolConstants.QUOTES_SYMBOL;
    }

    public static String generateAuthorizationHeader(Map<String, String> digestChallenge, String username, String password) throws NoSuchAlgorithmException {
        String nonce = digestChallenge.get("nonce");
        String cnonce = generateCnonce(nonce, "00000001");
        String realm = digestChallenge.get("realm");
        String opaque = digestChallenge.get("opaque");
        String requestDigest = calculateMD5RequestDigest("/RPC2", username, password, realm, nonce, "00000001", cnonce, ResourceRef.AUTH);
        Map<String, String> digestResponseMap = new HashMap<>();
        digestResponseMap.put("algorithm", MessageDigestAlgorithms.MD5);
        digestResponseMap.put("username", quoteParam(username));
        digestResponseMap.put("realm", quoteParam(realm));
        digestResponseMap.put("nonce", quoteParam(nonce));
        digestResponseMap.put("uri", quoteParam("/RPC2"));
        digestResponseMap.put("qop", ResourceRef.AUTH);
        digestResponseMap.put("nc", "00000001");
        digestResponseMap.put("cnonce", quoteParam(cnonce));
        digestResponseMap.put("response", quoteParam(requestDigest));
        digestResponseMap.put("opaque", quoteParam(opaque));
        return serializeDigestResponse(digestResponseMap);
    }
}
