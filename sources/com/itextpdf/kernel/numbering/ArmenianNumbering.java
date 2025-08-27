package com.itextpdf.kernel.numbering;

import com.drew.metadata.photoshop.PhotoshopDirectory;
import com.itextpdf.io.font.constants.FontWeights;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/numbering/ArmenianNumbering.class */
public class ArmenianNumbering {
    private static final ArmenianDigit[] DIGITS = {new ArmenianDigit(1329, 1), new ArmenianDigit(1330, 2), new ArmenianDigit(1331, 3), new ArmenianDigit(1332, 4), new ArmenianDigit(1333, 5), new ArmenianDigit(1334, 6), new ArmenianDigit(1335, 7), new ArmenianDigit(1336, 8), new ArmenianDigit(1337, 9), new ArmenianDigit(1338, 10), new ArmenianDigit(1339, 20), new ArmenianDigit(1340, 30), new ArmenianDigit(1341, 40), new ArmenianDigit(1342, 50), new ArmenianDigit(1343, 60), new ArmenianDigit(1344, 70), new ArmenianDigit(1345, 80), new ArmenianDigit(1346, 90), new ArmenianDigit(1347, 100), new ArmenianDigit(1348, 200), new ArmenianDigit(1349, 300), new ArmenianDigit(1350, 400), new ArmenianDigit(1351, 500), new ArmenianDigit(1352, FontWeights.SEMI_BOLD), new ArmenianDigit(1353, 700), new ArmenianDigit(1354, 800), new ArmenianDigit(1355, 900), new ArmenianDigit(1356, 1000), new ArmenianDigit(1357, 2000), new ArmenianDigit(1358, 3000), new ArmenianDigit(1359, 4000), new ArmenianDigit(1360, 5000), new ArmenianDigit(1361, 6000), new ArmenianDigit(1362, PhotoshopDirectory.TAG_IMAGE_READY_VARIABLES_XML), new ArmenianDigit(1363, PhotoshopDirectory.TAG_LIGHTROOM_WORKFLOW), new ArmenianDigit(1364, 9000)};

    private ArmenianNumbering() {
    }

    public static String toArmenian(int number) {
        StringBuilder result = new StringBuilder();
        for (int i = DIGITS.length - 1; i >= 0; i--) {
            ArmenianDigit curDigit = DIGITS[i];
            while (number >= curDigit.value) {
                result.append(curDigit.digit);
                number -= curDigit.value;
            }
        }
        return result.toString();
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/numbering/ArmenianNumbering$ArmenianDigit.class */
    private static class ArmenianDigit {
        char digit;
        int value;

        ArmenianDigit(char digit, int value) {
            this.digit = digit;
            this.value = value;
        }
    }
}
