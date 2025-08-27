package org.apache.commons.collections4.sequence;

import java.util.List;
import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.functors.DefaultEquator;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/sequence/SequencesComparator.class */
public class SequencesComparator<T> {
    private final List<T> sequence1;
    private final List<T> sequence2;
    private final Equator<? super T> equator;
    private final int[] vDown;
    private final int[] vUp;

    public SequencesComparator(List<T> sequence1, List<T> sequence2) {
        this(sequence1, sequence2, DefaultEquator.defaultEquator());
    }

    public SequencesComparator(List<T> sequence1, List<T> sequence2, Equator<? super T> equator) {
        this.sequence1 = sequence1;
        this.sequence2 = sequence2;
        this.equator = equator;
        int size = sequence1.size() + sequence2.size() + 2;
        this.vDown = new int[size];
        this.vUp = new int[size];
    }

    public EditScript<T> getScript() {
        EditScript<T> script = new EditScript<>();
        buildScript(0, this.sequence1.size(), 0, this.sequence2.size(), script);
        return script;
    }

    private Snake buildSnake(int i, int i2, int i3, int i4) {
        int i5 = i;
        while (i5 - i2 < i4 && i5 < i3 && this.equator.equate(this.sequence1.get(i5), this.sequence2.get(i5 - i2))) {
            i5++;
        }
        return new Snake(i, i5, i2);
    }

    private Snake getMiddleSnake(int i, int i2, int i3, int i4) {
        int i5 = i2 - i;
        int i6 = i4 - i3;
        if (i5 == 0 || i6 == 0) {
            return null;
        }
        int i7 = i5 - i6;
        int i8 = i6 + i5;
        int i9 = (i8 % 2 == 0 ? i8 : i8 + 1) / 2;
        this.vDown[1 + i9] = i;
        this.vUp[1 + i9] = i2 + 1;
        for (int i10 = 0; i10 <= i9; i10++) {
            for (int i11 = -i10; i11 <= i10; i11 += 2) {
                int i12 = i11 + i9;
                if (i11 == (-i10) || (i11 != i10 && this.vDown[i12 - 1] < this.vDown[i12 + 1])) {
                    this.vDown[i12] = this.vDown[i12 + 1];
                } else {
                    this.vDown[i12] = this.vDown[i12 - 1] + 1;
                }
                int i13 = this.vDown[i12];
                for (int i14 = ((i13 - i) + i3) - i11; i13 < i2 && i14 < i4 && this.equator.equate(this.sequence1.get(i13), this.sequence2.get(i14)); i14++) {
                    i13++;
                    this.vDown[i12] = i13;
                }
                if (i7 % 2 != 0 && i7 - i10 <= i11 && i11 <= i7 + i10 && this.vUp[i12 - i7] <= this.vDown[i12]) {
                    return buildSnake(this.vUp[i12 - i7], (i11 + i) - i3, i2, i4);
                }
            }
            for (int i15 = i7 - i10; i15 <= i7 + i10; i15 += 2) {
                int i16 = (i15 + i9) - i7;
                if (i15 == i7 - i10 || (i15 != i7 + i10 && this.vUp[i16 + 1] <= this.vUp[i16 - 1])) {
                    this.vUp[i16] = this.vUp[i16 + 1] - 1;
                } else {
                    this.vUp[i16] = this.vUp[i16 - 1];
                }
                int i17 = this.vUp[i16] - 1;
                for (int i18 = ((i17 - i) + i3) - i15; i17 >= i && i18 >= i3 && this.equator.equate(this.sequence1.get(i17), this.sequence2.get(i18)); i18--) {
                    int i19 = i17;
                    i17--;
                    this.vUp[i16] = i19;
                }
                if (i7 % 2 == 0 && (-i10) <= i15 && i15 <= i10 && this.vUp[i16] <= this.vDown[i16 + i7]) {
                    return buildSnake(this.vUp[i16], (i15 + i) - i3, i2, i4);
                }
            }
        }
        throw new RuntimeException("Internal Error");
    }

    private void buildScript(int i, int i2, int i3, int i4, EditScript<T> editScript) {
        Snake middleSnake = getMiddleSnake(i, i2, i3, i4);
        if (middleSnake == null || ((middleSnake.getStart() == i2 && middleSnake.getDiag() == i2 - i4) || (middleSnake.getEnd() == i && middleSnake.getDiag() == i - i3))) {
            int i5 = i;
            int i6 = i3;
            while (true) {
                if (i5 < i2 || i6 < i4) {
                    if (i5 < i2 && i6 < i4 && this.equator.equate(this.sequence1.get(i5), this.sequence2.get(i6))) {
                        editScript.append(new KeepCommand<>(this.sequence1.get(i5)));
                        i5++;
                        i6++;
                    } else if (i2 - i > i4 - i3) {
                        editScript.append(new DeleteCommand<>(this.sequence1.get(i5)));
                        i5++;
                    } else {
                        editScript.append(new InsertCommand<>(this.sequence2.get(i6)));
                        i6++;
                    }
                } else {
                    return;
                }
            }
        } else {
            buildScript(i, middleSnake.getStart(), i3, middleSnake.getStart() - middleSnake.getDiag(), editScript);
            for (int start = middleSnake.getStart(); start < middleSnake.getEnd(); start++) {
                editScript.append(new KeepCommand<>(this.sequence1.get(start)));
            }
            buildScript(middleSnake.getEnd(), i2, middleSnake.getEnd() - middleSnake.getDiag(), i4, editScript);
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/sequence/SequencesComparator$Snake.class */
    private static class Snake {
        private final int start;
        private final int end;
        private final int diag;

        public Snake(int start, int end, int diag) {
            this.start = start;
            this.end = end;
            this.diag = diag;
        }

        public int getStart() {
            return this.start;
        }

        public int getEnd() {
            return this.end;
        }

        public int getDiag() {
            return this.diag;
        }
    }
}
