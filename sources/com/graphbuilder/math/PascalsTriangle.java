package com.graphbuilder.math;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/math/PascalsTriangle.class */
public final class PascalsTriangle {
    private static double[][] pt = {new double[]{1.0d}};

    private PascalsTriangle() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v27, types: [double[][]] */
    /* JADX WARN: Type inference failed for: r0v28 */
    /* JADX WARN: Type inference failed for: r0v29 */
    /* JADX WARN: Type inference failed for: r0v30 */
    /* JADX WARN: Type inference failed for: r0v33 */
    /* JADX WARN: Type inference failed for: r0v34 */
    /* JADX WARN: Type inference failed for: r0v35, types: [double] */
    /* JADX WARN: Type inference failed for: r0v40 */
    /* JADX WARN: Type inference failed for: r0v41 */
    /* JADX WARN: Type inference failed for: r0v44 */
    /* JADX WARN: Type inference failed for: r1v19 */
    /* JADX WARN: Type inference failed for: r1v20 */
    /* JADX WARN: Type inference failed for: r1v26 */
    /* JADX WARN: Type inference failed for: r1v27 */
    /* JADX WARN: Type inference failed for: r1v32 */
    /* JADX WARN: Type inference failed for: r1v33 */
    /* JADX WARN: Type inference failed for: r1v34, types: [double] */
    public static synchronized double nCr(int n, int r) {
        double[] dArr;
        double d;
        if (n < 0 || r < 0 || r > n) {
            return 0.0d;
        }
        if (n >= pt.length) {
            int d2 = 2 * pt.length;
            if (n > d2) {
                dArr = new double[n + 1];
            } else {
                dArr = new double[d2 + 1];
            }
            for (int i = 0; i < pt.length; i++) {
                dArr[i] = pt[i];
            }
            for (int i2 = pt.length; i2 < dArr.length; i2++) {
                dArr[i2] = new double[(i2 / 2) + 1];
                dArr[i2][0] = 4607182418800017408;
                for (int j = 1; j < dArr[i2].length; j++) {
                    ?? r0 = dArr[i2 - 1][j - 1];
                    if (j < dArr[i2 - 1].length) {
                        d = r0 + dArr[i2 - 1][j];
                    } else {
                        d = 2.0d * r0;
                    }
                    double x = d;
                    dArr[i2][j] = x;
                }
            }
            pt = dArr;
        }
        if (2 * r > n) {
            r = n - r;
        }
        return pt[n][r];
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [double[], double[][]] */
    public static synchronized void reset() {
        pt = new double[]{new double[]{1.0d}};
    }
}
