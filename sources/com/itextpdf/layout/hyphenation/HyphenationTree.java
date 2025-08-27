package com.itextpdf.layout.hyphenation;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xml.sax.SAXException;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/hyphenation/HyphenationTree.class */
public class HyphenationTree extends TernaryTree implements IPatternConsumer {
    private static final long serialVersionUID = -7842107987915665573L;
    private transient TernaryTree ivalues;
    protected Map<String, List> stoplist = new HashMap(23);
    protected TernaryTree classmap = new TernaryTree();
    protected ByteVector vspace = new ByteVector();

    public HyphenationTree() {
        this.vspace.alloc(1);
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
    }

    protected int packValues(String values) {
        int n = values.length();
        int m = (n & 1) == 1 ? (n >> 1) + 2 : (n >> 1) + 1;
        int offset = this.vspace.alloc(m);
        byte[] va = this.vspace.getArray();
        for (int i = 0; i < n; i++) {
            int j = i >> 1;
            byte v = (byte) (((values.charAt(i) - '0') + 1) & 15);
            if ((i & 1) == 1) {
                va[j + offset] = (byte) (va[j + offset] | v);
            } else {
                va[j + offset] = (byte) (v << 4);
            }
        }
        va[(m - 1) + offset] = 0;
        return offset;
    }

    protected String unpackValues(int k) {
        StringBuffer buf = new StringBuffer();
        int k2 = k + 1;
        byte b = this.vspace.get(k);
        while (true) {
            byte v = b;
            if (v == 0) {
                break;
            }
            buf.append((char) (((v >>> 4) - 1) + 48));
            char c = (char) (v & 15);
            if (c == 0) {
                break;
            }
            buf.append((char) ((c - 1) + 48));
            int i = k2;
            k2++;
            b = this.vspace.get(i);
        }
        return buf.toString();
    }

    public void loadPatterns(String filename) throws SAXException, HyphenationException, IOException {
        loadPatterns(new FileInputStream(filename), filename);
    }

    public void loadPatterns(InputStream stream, String name) throws SAXException, HyphenationException, IOException {
        PatternParser pp = new PatternParser(this);
        this.ivalues = new TernaryTree();
        pp.parse(stream, name);
        trimToSize();
        this.vspace.trimToSize();
        this.classmap.trimToSize();
        this.ivalues = null;
    }

    public String findPattern(String pat) {
        int k = super.find(pat);
        if (k >= 0) {
            return unpackValues(k);
        }
        return "";
    }

    protected int hstrcmp(char[] s, int si, char[] t, int ti) {
        while (s[si] == t[ti]) {
            if (s[si] != 0) {
                si++;
                ti++;
            } else {
                return 0;
            }
        }
        if (t[ti] == 0) {
            return 0;
        }
        return s[si] - t[ti];
    }

    protected byte[] getValues(int k) {
        StringBuffer buf = new StringBuffer();
        int k2 = k + 1;
        byte b = this.vspace.get(k);
        while (true) {
            byte v = b;
            if (v == 0) {
                break;
            }
            buf.append((char) ((v >>> 4) - 1));
            char c = (char) (v & 15);
            if (c == 0) {
                break;
            }
            buf.append((char) (c - 1));
            int i = k2;
            k2++;
            b = this.vspace.get(i);
        }
        byte[] res = new byte[buf.length()];
        for (int i2 = 0; i2 < res.length; i2++) {
            res[i2] = (byte) buf.charAt(i2);
        }
        return res;
    }

    protected void searchPatterns(char[] word, int index, byte[] il) {
        int i = index;
        char sp = word[i];
        char p = this.root;
        while (p > 0 && p < this.sc.length) {
            if (this.sc[p] == 65535) {
                if (hstrcmp(word, i, this.kv.getArray(), this.lo[p]) == 0) {
                    byte[] values = getValues(this.eq[p]);
                    int j = index;
                    for (int k = 0; k < values.length; k++) {
                        if (j < il.length && values[k] > il[j]) {
                            il[j] = values[k];
                        }
                        j++;
                    }
                    return;
                }
                return;
            }
            int d = sp - this.sc[p];
            if (d == 0) {
                if (sp != 0) {
                    i++;
                    sp = word[i];
                    p = this.eq[p];
                    char c = p;
                    while (true) {
                        char q = c;
                        if (q <= 0 || q >= this.sc.length || this.sc[q] == 65535) {
                            break;
                        }
                        if (this.sc[q] == 0) {
                            byte[] values2 = getValues(this.eq[q]);
                            int j2 = index;
                            for (int k2 = 0; k2 < values2.length; k2++) {
                                if (j2 < il.length && values2[k2] > il[j2]) {
                                    il[j2] = values2[k2];
                                }
                                j2++;
                            }
                        } else {
                            c = this.lo[q];
                        }
                    }
                } else {
                    return;
                }
            } else {
                p = d < 0 ? this.lo[p] : this.hi[p];
            }
        }
    }

    public Hyphenation hyphenate(String word, int remainCharCount, int pushCharCount) {
        char[] w = word.toCharArray();
        if (isMultiPartWord(w, w.length)) {
            List<char[]> words = splitOnNonCharacters(w);
            return new Hyphenation(new String(w), getHyphPointsForWords(words, remainCharCount, pushCharCount));
        }
        return hyphenate(w, 0, w.length, remainCharCount, pushCharCount);
    }

    private boolean isMultiPartWord(char[] w, int len) {
        int wordParts = 0;
        for (int i = 0; i < len; i++) {
            char[] c = {w[i], 0};
            int nc = this.classmap.find(c, 0);
            if (nc > 0) {
                if (wordParts > 1) {
                    return true;
                }
                wordParts = 1;
            } else if (wordParts == 1) {
                wordParts++;
            }
        }
        return false;
    }

    private List<char[]> splitOnNonCharacters(char[] word) {
        List<Integer> breakPoints = getNonLetterBreaks(word);
        if (breakPoints.size() == 0) {
            return Collections.emptyList();
        }
        List<char[]> words = new ArrayList<>();
        int ibreak = 0;
        while (ibreak < breakPoints.size()) {
            char[] newWord = getWordFromCharArray(word, ibreak == 0 ? 0 : breakPoints.get(ibreak - 1).intValue(), breakPoints.get(ibreak).intValue());
            words.add(newWord);
            ibreak++;
        }
        if ((word.length - breakPoints.get(breakPoints.size() - 1).intValue()) - 1 > 1) {
            char[] newWord2 = getWordFromCharArray(word, breakPoints.get(breakPoints.size() - 1).intValue(), word.length);
            words.add(newWord2);
        }
        return words;
    }

    private List<Integer> getNonLetterBreaks(char[] word) {
        char[] c = new char[2];
        List<Integer> breakPoints = new ArrayList<>();
        boolean foundLetter = false;
        for (int i = 0; i < word.length; i++) {
            c[0] = word[i];
            if (this.classmap.find(c, 0) < 0) {
                if (foundLetter) {
                    breakPoints.add(Integer.valueOf(i));
                }
            } else {
                foundLetter = true;
            }
        }
        return breakPoints;
    }

    private char[] getWordFromCharArray(char[] word, int startIndex, int endIndex) {
        char[] newWord = new char[endIndex - (startIndex == 0 ? startIndex : startIndex + 1)];
        int iChar = 0;
        for (int i = startIndex == 0 ? 0 : startIndex + 1; i < endIndex; i++) {
            int i2 = iChar;
            iChar++;
            newWord[i2] = word[i];
        }
        return newWord;
    }

    private int[] getHyphPointsForWords(List<char[]> nonLetterWords, int remainCharCount, int pushCharCount) {
        int[] breaks = new int[0];
        int iNonLetterWord = 0;
        while (iNonLetterWord < nonLetterWords.size()) {
            char[] nonLetterWord = nonLetterWords.get(iNonLetterWord);
            Hyphenation curHyph = hyphenate(nonLetterWord, 0, nonLetterWord.length, iNonLetterWord == 0 ? remainCharCount : 1, iNonLetterWord == nonLetterWords.size() - 1 ? pushCharCount : 1);
            if (curHyph != null) {
                int[] combined = new int[breaks.length + curHyph.getHyphenationPoints().length];
                int[] hyphPoints = curHyph.getHyphenationPoints();
                int foreWordsSize = calcForeWordsSize(nonLetterWords, iNonLetterWord);
                for (int i = 0; i < hyphPoints.length; i++) {
                    int i2 = i;
                    hyphPoints[i2] = hyphPoints[i2] + foreWordsSize;
                }
                System.arraycopy(breaks, 0, combined, 0, breaks.length);
                System.arraycopy(hyphPoints, 0, combined, breaks.length, hyphPoints.length);
                breaks = combined;
            }
            iNonLetterWord++;
        }
        return breaks;
    }

    private int calcForeWordsSize(List<char[]> nonLetterWords, int iNonLetterWord) {
        int result = 0;
        for (int i = 0; i < iNonLetterWord; i++) {
            result += nonLetterWords.get(i).length + 1;
        }
        return result;
    }

    public Hyphenation hyphenate(char[] w, int offset, int len, int remainCharCount, int pushCharCount) {
        char[] word = new char[len + 3];
        char[] c = new char[2];
        int iIgnoreAtBeginning = 0;
        int iLength = len;
        boolean bEndOfLetters = false;
        for (int i = 1; i <= len; i++) {
            c[0] = w[(offset + i) - 1];
            int nc = this.classmap.find(c, 0);
            if (nc < 0) {
                if (i == 1 + iIgnoreAtBeginning) {
                    iIgnoreAtBeginning++;
                } else {
                    bEndOfLetters = true;
                }
                iLength--;
            } else if (!bEndOfLetters) {
                word[i - iIgnoreAtBeginning] = (char) nc;
            } else {
                return null;
            }
        }
        int len2 = iLength;
        if (len2 < remainCharCount + pushCharCount) {
            return null;
        }
        int[] result = new int[len2 + 1];
        int k = 0;
        String sw = new String(word, 1, len2);
        if (this.stoplist.containsKey(sw)) {
            ArrayList hw = (ArrayList) this.stoplist.get(sw);
            int j = 0;
            for (int i2 = 0; i2 < hw.size(); i2++) {
                Object o = hw.get(i2);
                if (o instanceof String) {
                    j += ((String) o).length();
                    if (j >= remainCharCount && j < len2 - pushCharCount) {
                        int i3 = k;
                        k++;
                        result[i3] = j + iIgnoreAtBeginning;
                    }
                }
            }
        } else {
            word[0] = '.';
            word[len2 + 1] = '.';
            word[len2 + 2] = 0;
            byte[] il = new byte[len2 + 3];
            for (int i4 = 0; i4 < len2 + 1; i4++) {
                searchPatterns(word, i4, il);
            }
            for (int i5 = 0; i5 < len2; i5++) {
                if ((il[i5 + 1] & 1) == 1 && i5 >= remainCharCount && i5 <= len2 - pushCharCount) {
                    int i6 = k;
                    k++;
                    result[i6] = i5;
                }
            }
        }
        if (k > 0) {
            int[] res = new int[k];
            System.arraycopy(result, 0, res, 0, k);
            return new Hyphenation(new String(w, iIgnoreAtBeginning, len2), res);
        }
        return null;
    }

    @Override // com.itextpdf.layout.hyphenation.IPatternConsumer
    public void addClass(String chargroup) {
        if (chargroup.length() > 0) {
            char equivChar = chargroup.charAt(0);
            char[] key = new char[2];
            key[1] = 0;
            for (int i = 0; i < chargroup.length(); i++) {
                key[0] = chargroup.charAt(i);
                this.classmap.insert(key, 0, equivChar);
            }
        }
    }

    @Override // com.itextpdf.layout.hyphenation.IPatternConsumer
    public void addException(String word, List hyphenatedword) {
        this.stoplist.put(word, hyphenatedword);
    }

    @Override // com.itextpdf.layout.hyphenation.IPatternConsumer
    public void addPattern(String pattern, String ivalue) {
        int k = this.ivalues.find(ivalue);
        if (k <= 0) {
            k = packValues(ivalue);
            this.ivalues.insert(ivalue, (char) k);
        }
        insert(pattern, (char) k);
    }
}
