package org.apache.poi.ss.format;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import org.apache.poi.ss.format.CellFormatPart;
import org.apache.poi.ss.format.CellNumberFormatter;
import org.apache.poi.util.Internal;

@Internal
/* loaded from: poi-3.17.jar:org/apache/poi/ss/format/CellNumberPartHandler.class */
public class CellNumberPartHandler implements CellFormatPart.PartHandler {
    private char insertSignForExponent;
    private CellNumberFormatter.Special decimalPoint;
    private CellNumberFormatter.Special slash;
    private CellNumberFormatter.Special exponent;
    private CellNumberFormatter.Special numerator;
    private boolean improperFraction;
    private double scale = 1.0d;
    private final List<CellNumberFormatter.Special> specials = new LinkedList();

    @Override // org.apache.poi.ss.format.CellFormatPart.PartHandler
    public String handlePart(Matcher m, String part, CellFormatType type, StringBuffer descBuf) {
        int pos = descBuf.length();
        char firstCh = part.charAt(0);
        switch (firstCh) {
            case '#':
            case '0':
            case '?':
                if (this.insertSignForExponent != 0) {
                    this.specials.add(new CellNumberFormatter.Special(this.insertSignForExponent, pos));
                    descBuf.append(this.insertSignForExponent);
                    this.insertSignForExponent = (char) 0;
                    pos++;
                }
                for (int i = 0; i < part.length(); i++) {
                    char ch2 = part.charAt(i);
                    this.specials.add(new CellNumberFormatter.Special(ch2, pos + i));
                }
                break;
            case '%':
                this.scale *= 100.0d;
                break;
            case '.':
                if (this.decimalPoint == null && this.specials.size() > 0) {
                    this.decimalPoint = new CellNumberFormatter.Special('.', pos);
                    this.specials.add(this.decimalPoint);
                    break;
                }
                break;
            case '/':
                if (this.slash == null && this.specials.size() > 0) {
                    this.numerator = previousNumber();
                    this.improperFraction |= this.numerator == firstDigit(this.specials);
                    this.slash = new CellNumberFormatter.Special('.', pos);
                    this.specials.add(this.slash);
                    break;
                }
                break;
            case 'E':
            case 'e':
                if (this.exponent == null && this.specials.size() > 0) {
                    this.exponent = new CellNumberFormatter.Special('.', pos);
                    this.specials.add(this.exponent);
                    this.insertSignForExponent = part.charAt(1);
                    return part.substring(0, 1);
                }
                break;
            default:
                return null;
        }
        return part;
    }

    public double getScale() {
        return this.scale;
    }

    public CellNumberFormatter.Special getDecimalPoint() {
        return this.decimalPoint;
    }

    public CellNumberFormatter.Special getSlash() {
        return this.slash;
    }

    public CellNumberFormatter.Special getExponent() {
        return this.exponent;
    }

    public CellNumberFormatter.Special getNumerator() {
        return this.numerator;
    }

    public List<CellNumberFormatter.Special> getSpecials() {
        return this.specials;
    }

    public boolean isImproperFraction() {
        return this.improperFraction;
    }

    private CellNumberFormatter.Special previousNumber() {
        CellNumberFormatter.Special last;
        ListIterator<CellNumberFormatter.Special> it = this.specials.listIterator(this.specials.size());
        while (it.hasPrevious()) {
            CellNumberFormatter.Special s = it.previous();
            if (isDigitFmt(s)) {
                do {
                    last = s;
                    if (!it.hasPrevious()) {
                        break;
                    }
                    s = it.previous();
                    if (last.pos - s.pos > 1) {
                        break;
                    }
                } while (isDigitFmt(s));
                return last;
            }
        }
        return null;
    }

    private static boolean isDigitFmt(CellNumberFormatter.Special s) {
        return s.f9ch == '0' || s.f9ch == '?' || s.f9ch == '#';
    }

    private static CellNumberFormatter.Special firstDigit(List<CellNumberFormatter.Special> specials) {
        for (CellNumberFormatter.Special s : specials) {
            if (isDigitFmt(s)) {
                return s;
            }
        }
        return null;
    }
}
