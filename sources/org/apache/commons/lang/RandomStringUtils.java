package org.apache.commons.lang;

import com.fasterxml.jackson.core.base.GeneratorBase;
import java.util.Random;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/RandomStringUtils.class */
public class RandomStringUtils {
    private static final Random RANDOM = new Random();

    public static String random(int count) {
        return random(count, false, false);
    }

    public static String randomAscii(int count) {
        return random(count, 32, 127, false, false);
    }

    public static String randomAlphabetic(int count) {
        return random(count, true, false);
    }

    public static String randomAlphanumeric(int count) {
        return random(count, true, true);
    }

    public static String randomNumeric(int count) {
        return random(count, false, true);
    }

    public static String random(int count, boolean letters, boolean numbers) {
        return random(count, 0, 0, letters, numbers);
    }

    public static String random(int count, int start, int end, boolean letters, boolean numbers) {
        return random(count, start, end, letters, numbers, null, RANDOM);
    }

    public static String random(int count, int start, int end, boolean letters, boolean numbers, char[] chars) {
        return random(count, start, end, letters, numbers, chars, RANDOM);
    }

    public static String random(int count, int start, int end, boolean letters, boolean numbers, char[] chars, Random random) {
        char ch2;
        if (count == 0) {
            return "";
        }
        if (count < 0) {
            throw new IllegalArgumentException(new StringBuffer().append("Requested random string length ").append(count).append(" is less than 0.").toString());
        }
        if (start == 0 && end == 0) {
            end = 123;
            start = 32;
            if (!letters && !numbers) {
                start = 0;
                end = Integer.MAX_VALUE;
            }
        }
        char[] buffer = new char[count];
        int gap = end - start;
        while (true) {
            int i = count;
            count--;
            if (i != 0) {
                if (chars == null) {
                    ch2 = (char) (random.nextInt(gap) + start);
                } else {
                    ch2 = chars[random.nextInt(gap) + start];
                }
                if ((letters && Character.isLetter(ch2)) || ((numbers && Character.isDigit(ch2)) || (!letters && !numbers))) {
                    if (ch2 >= 56320 && ch2 <= 57343) {
                        if (count == 0) {
                            count++;
                        } else {
                            buffer[count] = ch2;
                            count--;
                            buffer[count] = (char) (GeneratorBase.SURR1_FIRST + random.nextInt(128));
                        }
                    } else if (ch2 >= 55296 && ch2 <= 56191) {
                        if (count == 0) {
                            count++;
                        } else {
                            buffer[count] = (char) (GeneratorBase.SURR2_FIRST + random.nextInt(128));
                            count--;
                            buffer[count] = ch2;
                        }
                    } else if (ch2 >= 56192 && ch2 <= 56319) {
                        count++;
                    } else {
                        buffer[count] = ch2;
                    }
                } else {
                    count++;
                }
            } else {
                return new String(buffer);
            }
        }
    }

    public static String random(int count, String chars) {
        if (chars == null) {
            return random(count, 0, 0, false, false, null, RANDOM);
        }
        return random(count, chars.toCharArray());
    }

    public static String random(int count, char[] chars) {
        if (chars == null) {
            return random(count, 0, 0, false, false, null, RANDOM);
        }
        return random(count, 0, chars.length, false, false, chars, RANDOM);
    }
}
