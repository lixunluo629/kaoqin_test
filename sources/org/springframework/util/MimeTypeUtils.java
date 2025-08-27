package org.springframework.util;

import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.springframework.util.MimeType;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/MimeTypeUtils.class */
public abstract class MimeTypeUtils {
    public static final String ALL_VALUE = "*/*";

    @Deprecated
    public static final String APPLICATION_ATOM_XML_VALUE = "application/atom+xml";

    @Deprecated
    public static final String APPLICATION_FORM_URLENCODED_VALUE = "application/x-www-form-urlencoded";
    public static final String APPLICATION_JSON_VALUE = "application/json";
    public static final String APPLICATION_OCTET_STREAM_VALUE = "application/octet-stream";

    @Deprecated
    public static final String APPLICATION_XHTML_XML_VALUE = "application/xhtml+xml";
    public static final String APPLICATION_XML_VALUE = "application/xml";
    public static final String IMAGE_GIF_VALUE = "image/gif";
    public static final String IMAGE_JPEG_VALUE = "image/jpeg";
    public static final String IMAGE_PNG_VALUE = "image/png";

    @Deprecated
    public static final String MULTIPART_FORM_DATA_VALUE = "multipart/form-data";
    public static final String TEXT_HTML_VALUE = "text/html";
    public static final String TEXT_PLAIN_VALUE = "text/plain";
    public static final String TEXT_XML_VALUE = "text/xml";
    private static final byte[] BOUNDARY_CHARS = {45, 95, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90};
    private static final Random RND = new SecureRandom();
    private static Charset US_ASCII = Charset.forName("US-ASCII");
    public static final Comparator<MimeType> SPECIFICITY_COMPARATOR = new MimeType.SpecificityComparator();
    public static final MimeType ALL = MimeType.valueOf("*/*");

    @Deprecated
    public static final MimeType APPLICATION_ATOM_XML = MimeType.valueOf("application/atom+xml");

    @Deprecated
    public static final MimeType APPLICATION_FORM_URLENCODED = MimeType.valueOf("application/x-www-form-urlencoded");
    public static final MimeType APPLICATION_JSON = MimeType.valueOf("application/json");
    public static final MimeType APPLICATION_OCTET_STREAM = MimeType.valueOf("application/octet-stream");

    @Deprecated
    public static final MimeType APPLICATION_XHTML_XML = MimeType.valueOf("application/xhtml+xml");
    public static final MimeType APPLICATION_XML = MimeType.valueOf("application/xml");
    public static final MimeType IMAGE_GIF = MimeType.valueOf("image/gif");
    public static final MimeType IMAGE_JPEG = MimeType.valueOf("image/jpeg");
    public static final MimeType IMAGE_PNG = MimeType.valueOf("image/png");

    @Deprecated
    public static final MimeType MULTIPART_FORM_DATA = MimeType.valueOf("multipart/form-data");
    public static final MimeType TEXT_HTML = MimeType.valueOf("text/html");
    public static final MimeType TEXT_PLAIN = MimeType.valueOf("text/plain");
    public static final MimeType TEXT_XML = MimeType.valueOf("text/xml");

    /* JADX WARN: Code restructure failed: missing block: B:60:0x0165, code lost:
    
        r13 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x018b, code lost:
    
        throw new org.springframework.util.InvalidMimeTypeException(r6, "unsupported charset '" + r13.getCharsetName() + "'");
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x018c, code lost:
    
        r13 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x019b, code lost:
    
        throw new org.springframework.util.InvalidMimeTypeException(r6, r13.getMessage());
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static org.springframework.util.MimeType parseMimeType(java.lang.String r6) {
        /*
            Method dump skipped, instructions count: 412
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.springframework.util.MimeTypeUtils.parseMimeType(java.lang.String):org.springframework.util.MimeType");
    }

    public static List<MimeType> parseMimeTypes(String mimeTypes) {
        if (!StringUtils.hasLength(mimeTypes)) {
            return Collections.emptyList();
        }
        String[] tokens = StringUtils.tokenizeToStringArray(mimeTypes, ",");
        List<MimeType> result = new ArrayList<>(tokens.length);
        for (String token : tokens) {
            result.add(parseMimeType(token));
        }
        return result;
    }

    public static String toString(Collection<? extends MimeType> mimeTypes) {
        StringBuilder builder = new StringBuilder();
        Iterator<? extends MimeType> iterator = mimeTypes.iterator();
        while (iterator.hasNext()) {
            MimeType mimeType = iterator.next();
            mimeType.appendTo(builder);
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    public static void sortBySpecificity(List<MimeType> mimeTypes) {
        Assert.notNull(mimeTypes, "'mimeTypes' must not be null");
        if (mimeTypes.size() > 1) {
            Collections.sort(mimeTypes, SPECIFICITY_COMPARATOR);
        }
    }

    public static byte[] generateMultipartBoundary() {
        byte[] boundary = new byte[RND.nextInt(11) + 30];
        for (int i = 0; i < boundary.length; i++) {
            boundary[i] = BOUNDARY_CHARS[RND.nextInt(BOUNDARY_CHARS.length)];
        }
        return boundary;
    }

    public static String generateMultipartBoundaryString() {
        return new String(generateMultipartBoundary(), US_ASCII);
    }
}
