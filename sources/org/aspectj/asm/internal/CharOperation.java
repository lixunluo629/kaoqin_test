package org.aspectj.asm.internal;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/asm/internal/CharOperation.class */
public class CharOperation {
    public static final char[][] NO_CHAR_CHAR = new char[0];
    public static final char[] NO_CHAR = new char[0];

    public static final char[] subarray(char[] array, int start, int end) {
        if (end == -1) {
            end = array.length;
        }
        if (start > end || start < 0 || end > array.length) {
            return null;
        }
        char[] result = new char[end - start];
        System.arraycopy(array, start, result, 0, end - start);
        return result;
    }

    /* JADX WARN: Type inference failed for: r0v6, types: [char[], char[][], java.lang.Object] */
    public static final char[][] subarray(char[][] array, int start, int end) {
        if (end == -1) {
            end = array.length;
        }
        if (start > end) {
            return (char[][]) null;
        }
        if (start < 0) {
            return (char[][]) null;
        }
        if (end > array.length) {
            return (char[][]) null;
        }
        ?? r0 = new char[end - start];
        System.arraycopy(array, start, r0, 0, end - start);
        return r0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v9, types: [char[], char[][]] */
    public static final char[][] splitOn(char divider, char[] array) {
        int length = array == null ? 0 : array.length;
        if (length == 0) {
            return NO_CHAR_CHAR;
        }
        int wordCount = 1;
        for (int i = 0; i < length; i++) {
            if (array[i] == divider) {
                wordCount++;
            }
        }
        ?? r0 = new char[wordCount];
        int last = 0;
        int currentWord = 0;
        for (int i2 = 0; i2 < length; i2++) {
            if (array[i2] == divider) {
                r0[currentWord] = new char[i2 - last];
                int i3 = currentWord;
                currentWord++;
                System.arraycopy(array, last, r0[i3], 0, i2 - last);
                last = i2 + 1;
            }
        }
        r0[currentWord] = new char[length - last];
        System.arraycopy(array, last, r0[currentWord], 0, length - last);
        return r0;
    }

    public static final int lastIndexOf(char toBeFound, char[] array) {
        int i = array.length;
        do {
            i--;
            if (i < 0) {
                return -1;
            }
        } while (toBeFound != array[i]);
        return i;
    }

    public static final int indexOf(char toBeFound, char[] array) {
        for (int i = 0; i < array.length; i++) {
            if (toBeFound == array[i]) {
                return i;
            }
        }
        return -1;
    }

    public static final char[] concat(char[] first, char[] second) {
        if (first == null) {
            return second;
        }
        if (second == null) {
            return first;
        }
        int length1 = first.length;
        int length2 = second.length;
        char[] result = new char[length1 + length2];
        System.arraycopy(first, 0, result, 0, length1);
        System.arraycopy(second, 0, result, length1, length2);
        return result;
    }

    public static final boolean equals(char[] first, char[] second) {
        if (first == second) {
            return true;
        }
        if (first == null || second == null || first.length != second.length) {
            return false;
        }
        int i = first.length;
        do {
            i--;
            if (i < 0) {
                return true;
            }
        } while (first[i] == second[i]);
        return false;
    }

    public static final String toString(char[][] array) {
        char[] result = concatWith(array, '.');
        return new String(result);
    }

    public static final char[] concatWith(char[][] array, char separator) {
        int length = array == null ? 0 : array.length;
        if (length == 0) {
            return NO_CHAR;
        }
        int size = length - 1;
        int index = length;
        while (true) {
            index--;
            if (index < 0) {
                break;
            }
            if (array[index].length == 0) {
                size--;
            } else {
                size += array[index].length;
            }
        }
        if (size <= 0) {
            return NO_CHAR;
        }
        char[] result = new char[size];
        int index2 = length;
        while (true) {
            index2--;
            if (index2 >= 0) {
                int length2 = array[index2].length;
                if (length2 > 0) {
                    int size2 = size - length2;
                    System.arraycopy(array[index2], 0, result, size2, length2);
                    size = size2 - 1;
                    if (size >= 0) {
                        result[size] = separator;
                    }
                }
            } else {
                return result;
            }
        }
    }

    public static final int hashCode(char[] array) {
        int length = array.length;
        int hash = length == 0 ? (char) 31 : array[0];
        if (length < 8) {
            int i = length;
            while (true) {
                i--;
                if (i <= 0) {
                    break;
                }
                hash = (hash * 31) + array[i];
            }
        } else {
            int i2 = length - 1;
            int last = i2 > 16 ? i2 - 16 : 0;
            while (i2 > last) {
                hash = (hash * 31) + array[i2];
                i2 -= 2;
            }
        }
        return hash & Integer.MAX_VALUE;
    }

    public static final boolean equals(char[][] first, char[][] second) {
        if (first == second) {
            return true;
        }
        if (first == null || second == null || first.length != second.length) {
            return false;
        }
        int i = first.length;
        do {
            i--;
            if (i < 0) {
                return true;
            }
        } while (equals(first[i], second[i]));
        return false;
    }

    public static final void replace(char[] array, char toBeReplaced, char replacementChar) {
        if (toBeReplaced != replacementChar) {
            int max = array.length;
            for (int i = 0; i < max; i++) {
                if (array[i] == toBeReplaced) {
                    array[i] = replacementChar;
                }
            }
        }
    }
}
