package org.apache.xmlbeans.impl.common;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/Levenshtein.class */
public class Levenshtein {
    private static int minimum(int a, int b, int c) {
        int mi = a;
        if (b < mi) {
            mi = b;
        }
        if (c < mi) {
            mi = c;
        }
        return mi;
    }

    public static int distance(String s, String t) {
        int i;
        int n = s.length();
        int m = t.length();
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        int[][] d = new int[n + 1][m + 1];
        for (int i2 = 0; i2 <= n; i2++) {
            d[i2][0] = i2;
        }
        for (int j = 0; j <= m; j++) {
            d[0][j] = j;
        }
        for (int i3 = 1; i3 <= n; i3++) {
            char s_i = s.charAt(i3 - 1);
            for (int j2 = 1; j2 <= m; j2++) {
                char t_j = t.charAt(j2 - 1);
                if (s_i == t_j) {
                    i = 0;
                } else {
                    i = 1;
                }
                int cost = i;
                d[i3][j2] = minimum(d[i3 - 1][j2] + 1, d[i3][j2 - 1] + 1, d[i3 - 1][j2 - 1] + cost);
            }
        }
        return d[n][m];
    }
}
