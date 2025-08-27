package org.hibernate.validator.internal.util;

import java.util.List;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/util/ModUtil.class */
public final class ModUtil {
    private ModUtil() {
    }

    public static int calculateLuhnMod10Check(List<Integer> digits) {
        int sum = 0;
        boolean even = true;
        for (int index = digits.size() - 1; index >= 0; index--) {
            int digit = digits.get(index).intValue();
            if (even) {
                digit <<= 1;
            }
            if (digit > 9) {
                digit -= 9;
            }
            sum += digit;
            even = !even;
        }
        return (10 - (sum % 10)) % 10;
    }

    public static int calculateMod10Check(List<Integer> digits, int multiplier, int weight) {
        int digit;
        int sum = 0;
        boolean even = true;
        for (int index = digits.size() - 1; index >= 0; index--) {
            int digit2 = digits.get(index).intValue();
            if (even) {
                digit = digit2 * multiplier;
            } else {
                digit = digit2 * weight;
            }
            sum += digit;
            even = !even;
        }
        return (10 - (sum % 10)) % 10;
    }

    public static int calculateMod11Check(List<Integer> digits, int threshold) {
        int sum = 0;
        int multiplier = 2;
        for (int index = digits.size() - 1; index >= 0; index--) {
            int i = multiplier;
            multiplier++;
            sum += digits.get(index).intValue() * i;
            if (multiplier > threshold) {
                multiplier = 2;
            }
        }
        return 11 - (sum % 11);
    }

    public static int calculateMod11Check(List<Integer> digits) {
        return calculateMod11Check(digits, Integer.MAX_VALUE);
    }
}
