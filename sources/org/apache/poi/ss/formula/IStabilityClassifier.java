package org.apache.poi.ss.formula;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/IStabilityClassifier.class */
public interface IStabilityClassifier {
    public static final IStabilityClassifier TOTALLY_IMMUTABLE = new IStabilityClassifier() { // from class: org.apache.poi.ss.formula.IStabilityClassifier.1
        @Override // org.apache.poi.ss.formula.IStabilityClassifier
        public boolean isCellFinal(int sheetIndex, int rowIndex, int columnIndex) {
            return true;
        }
    };

    boolean isCellFinal(int i, int i2, int i3);
}
