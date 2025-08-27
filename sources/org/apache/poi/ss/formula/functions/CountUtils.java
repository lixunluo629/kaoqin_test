package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.ThreeDEval;
import org.apache.poi.ss.formula.TwoDEval;
import org.apache.poi.ss.formula.eval.RefEval;
import org.apache.poi.ss.formula.eval.ValueEval;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/CountUtils.class */
final class CountUtils {

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/CountUtils$I_MatchAreaPredicate.class */
    public interface I_MatchAreaPredicate extends I_MatchPredicate {
        boolean matches(TwoDEval twoDEval, int i, int i2);
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/CountUtils$I_MatchPredicate.class */
    public interface I_MatchPredicate {
        boolean matches(ValueEval valueEval);
    }

    private CountUtils() {
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x006c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int countMatchingCellsInArea(org.apache.poi.ss.formula.ThreeDEval r5, org.apache.poi.ss.formula.functions.CountUtils.I_MatchPredicate r6) {
        /*
            r0 = 0
            r7 = r0
            r0 = r5
            int r0 = r0.getFirstSheetIndex()
            r8 = r0
            r0 = r5
            int r0 = r0.getLastSheetIndex()
            r9 = r0
            r0 = r8
            r10 = r0
        L14:
            r0 = r10
            r1 = r9
            if (r0 > r1) goto L8c
            r0 = r5
            int r0 = r0.getHeight()
            r11 = r0
            r0 = r5
            int r0 = r0.getWidth()
            r12 = r0
            r0 = 0
            r13 = r0
        L2e:
            r0 = r13
            r1 = r11
            if (r0 >= r1) goto L86
            r0 = 0
            r14 = r0
        L38:
            r0 = r14
            r1 = r12
            if (r0 >= r1) goto L80
            r0 = r5
            r1 = r10
            r2 = r13
            r3 = r14
            org.apache.poi.ss.formula.eval.ValueEval r0 = r0.getValue(r1, r2, r3)
            r15 = r0
            r0 = r6
            boolean r0 = r0 instanceof org.apache.poi.ss.formula.functions.CountUtils.I_MatchAreaPredicate
            if (r0 == 0) goto L6c
            r0 = r6
            org.apache.poi.ss.formula.functions.CountUtils$I_MatchAreaPredicate r0 = (org.apache.poi.ss.formula.functions.CountUtils.I_MatchAreaPredicate) r0
            r16 = r0
            r0 = r16
            r1 = r5
            r2 = r13
            r3 = r14
            boolean r0 = r0.matches(r1, r2, r3)
            if (r0 != 0) goto L6c
            goto L7a
        L6c:
            r0 = r6
            r1 = r15
            boolean r0 = r0.matches(r1)
            if (r0 == 0) goto L7a
            int r7 = r7 + 1
        L7a:
            int r14 = r14 + 1
            goto L38
        L80:
            int r13 = r13 + 1
            goto L2e
        L86:
            int r10 = r10 + 1
            goto L14
        L8c:
            r0 = r7
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.ss.formula.functions.CountUtils.countMatchingCellsInArea(org.apache.poi.ss.formula.ThreeDEval, org.apache.poi.ss.formula.functions.CountUtils$I_MatchPredicate):int");
    }

    public static int countMatchingCellsInRef(RefEval refEval, I_MatchPredicate criteriaPredicate) {
        int result = 0;
        int firstSheetIndex = refEval.getFirstSheetIndex();
        int lastSheetIndex = refEval.getLastSheetIndex();
        for (int sIx = firstSheetIndex; sIx <= lastSheetIndex; sIx++) {
            ValueEval ve = refEval.getInnerValueEval(sIx);
            if (criteriaPredicate.matches(ve)) {
                result++;
            }
        }
        return result;
    }

    public static int countArg(ValueEval eval, I_MatchPredicate criteriaPredicate) {
        if (eval == null) {
            throw new IllegalArgumentException("eval must not be null");
        }
        if (eval instanceof ThreeDEval) {
            return countMatchingCellsInArea((ThreeDEval) eval, criteriaPredicate);
        }
        if (eval instanceof TwoDEval) {
            throw new IllegalArgumentException("Count requires 3D Evals, 2D ones aren't supported");
        }
        if (eval instanceof RefEval) {
            return countMatchingCellsInRef((RefEval) eval, criteriaPredicate);
        }
        return criteriaPredicate.matches(eval) ? 1 : 0;
    }
}
