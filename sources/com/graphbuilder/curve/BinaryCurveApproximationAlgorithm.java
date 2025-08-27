package com.graphbuilder.curve;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/curve/BinaryCurveApproximationAlgorithm.class */
public final class BinaryCurveApproximationAlgorithm {
    private BinaryCurveApproximationAlgorithm() {
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x00e6, code lost:
    
        throw new java.lang.RuntimeException("NaN or infinity resulted from calling the eval method of the " + r6.getClass().getName() + " class.");
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v22, types: [double[]] */
    /* JADX WARN: Type inference failed for: r0v7, types: [double[]] */
    /* JADX WARN: Type inference failed for: r17v7 */
    /* JADX WARN: Type inference failed for: r2v12 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void genPts(com.graphbuilder.curve.ParametricCurve r6, double r7, double r9, com.graphbuilder.curve.MultiPath r11) {
        /*
            Method dump skipped, instructions count: 504
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.graphbuilder.curve.BinaryCurveApproximationAlgorithm.genPts(com.graphbuilder.curve.ParametricCurve, double, double, com.graphbuilder.curve.MultiPath):void");
    }

    /* JADX WARN: Type inference failed for: r0v4, types: [double[], double[][]] */
    private static double[][] checkSpace(double[][] stack, int size) {
        if (size == stack.length) {
            ?? r0 = new double[2 * size];
            for (int i = 0; i < size; i++) {
                r0[i] = stack[i];
            }
            return r0;
        }
        return stack;
    }
}
