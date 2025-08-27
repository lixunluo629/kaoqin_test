package org.apache.tomcat.util.file;

import java.util.Set;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/file/Matcher.class */
public final class Matcher {
    public static boolean matchName(Set<String> patternSet, String fileName) {
        char[] fileNameArray = fileName.toCharArray();
        for (String pattern : patternSet) {
            if (match(pattern, fileNameArray, true)) {
                return true;
            }
        }
        return false;
    }

    public static boolean match(String pattern, String str, boolean caseSensitive) {
        return match(pattern, str.toCharArray(), caseSensitive);
    }

    /* JADX WARN: Code restructure failed: missing block: B:44:0x00b6, code lost:
    
        if (r12 <= r13) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00c1, code lost:
    
        return allStars(r0, r10, r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00c2, code lost:
    
        r0 = r0[r11];
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00cc, code lost:
    
        if (r0 == '*') goto L109;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00d3, code lost:
    
        if (r12 <= r13) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00dd, code lost:
    
        if (r0 == '?') goto L112;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00ea, code lost:
    
        if (different(r8, r0, r7[r13]) == false) goto L113;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00ed, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00ef, code lost:
    
        r11 = r11 - 1;
        r13 = r13 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00fc, code lost:
    
        if (r12 <= r13) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x0107, code lost:
    
        return allStars(r0, r10, r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x010c, code lost:
    
        if (r10 == r11) goto L114;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x0113, code lost:
    
        if (r12 > r13) goto L115;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0116, code lost:
    
        r16 = -1;
        r17 = r10 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x0123, code lost:
    
        if (r17 > r11) goto L122;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x012c, code lost:
    
        if (r0[r17] != '*') goto L73;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x012f, code lost:
    
        r16 = r17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0136, code lost:
    
        r17 = r17 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x0142, code lost:
    
        if (r16 != (r10 + 1)) goto L116;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0145, code lost:
    
        r10 = r10 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x014b, code lost:
    
        r0 = (r16 - r10) - 1;
        r0 = (r13 - r12) + 1;
        r19 = -1;
        r20 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x016a, code lost:
    
        if (r20 > (r0 - r0)) goto L125;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x016d, code lost:
    
        r21 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x0174, code lost:
    
        if (r21 >= r0) goto L124;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x0177, code lost:
    
        r0 = r0[(r10 + r21) + 1];
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x0186, code lost:
    
        if (r0 == '?') goto L127;
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x0199, code lost:
    
        if (different(r8, r0, r7[(r12 + r20) + r21]) == false) goto L128;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x019f, code lost:
    
        r21 = r21 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x01a5, code lost:
    
        r19 = r12 + r20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x01af, code lost:
    
        r20 = r20 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x01b8, code lost:
    
        if (r19 != (-1)) goto L95;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x01bb, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x01bd, code lost:
    
        r10 = r16;
        r12 = r19 + r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x01d3, code lost:
    
        return allStars(r0, r10, r11);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static boolean match(java.lang.String r6, char[] r7, boolean r8) {
        /*
            Method dump skipped, instructions count: 468
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.tomcat.util.file.Matcher.match(java.lang.String, char[], boolean):boolean");
    }

    private static boolean allStars(char[] chars, int start, int end) {
        for (int i = start; i <= end; i++) {
            if (chars[i] != '*') {
                return false;
            }
        }
        return true;
    }

    private static boolean different(boolean caseSensitive, char ch2, char other) {
        return caseSensitive ? ch2 != other : Character.toUpperCase(ch2) != Character.toUpperCase(other);
    }
}
