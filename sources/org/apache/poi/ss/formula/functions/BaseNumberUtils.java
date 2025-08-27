package org.apache.poi.ss.formula.functions;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/functions/BaseNumberUtils.class */
public class BaseNumberUtils {
    public static double convertToDecimal(String value, int base, int maxNumberOfPlaces) throws IllegalArgumentException {
        long digit;
        if (value == null || value.length() == 0) {
            return 0.0d;
        }
        long stringLength = value.length();
        if (stringLength > maxNumberOfPlaces) {
            throw new IllegalArgumentException();
        }
        double decimalValue = 0.0d;
        long signedDigit = 0;
        boolean hasSignedDigit = true;
        char[] characters = value.toCharArray();
        for (char character : characters) {
            if ('0' <= character && character <= '9') {
                digit = character - '0';
            } else if ('A' <= character && character <= 'Z') {
                digit = 10 + (character - 'A');
            } else if ('a' <= character && character <= 'z') {
                digit = 10 + (character - 'a');
            } else {
                digit = base;
            }
            if (digit < base) {
                if (hasSignedDigit) {
                    hasSignedDigit = false;
                    signedDigit = digit;
                }
                decimalValue = (decimalValue * base) + digit;
            } else {
                throw new IllegalArgumentException("character not allowed");
            }
        }
        boolean isNegative = !hasSignedDigit && stringLength == ((long) maxNumberOfPlaces) && signedDigit >= ((long) (base / 2));
        if (isNegative) {
            decimalValue = getTwoComplement(base, maxNumberOfPlaces, decimalValue) * (-1.0d);
        }
        return decimalValue;
    }

    private static double getTwoComplement(double base, double maxNumberOfPlaces, double decimalValue) {
        return Math.pow(base, maxNumberOfPlaces) - decimalValue;
    }
}
