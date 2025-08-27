package org.hyperic.sigar.util;

import ch.qos.logback.core.rolling.helper.DateTokenConverter;
import com.alibaba.excel.constant.ExcelXmlConstants;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import com.mysql.jdbc.SQLError;
import java.text.DecimalFormatSymbols;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Vector;
import org.springframework.aop.framework.autoproxy.target.QuickTargetSourceCreator;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/util/PrintfFormat.class */
public class PrintfFormat {
    private Vector vFmt;
    private int cPos;
    private DecimalFormatSymbols dfs;

    public PrintfFormat(String fmtArg) throws IllegalArgumentException {
        this(Locale.getDefault(), fmtArg);
    }

    public PrintfFormat(Locale locale, String fmtArg) throws IllegalArgumentException {
        char c;
        this.vFmt = new Vector();
        this.cPos = 0;
        this.dfs = null;
        this.dfs = new DecimalFormatSymbols(locale);
        String unCS = nonControl(fmtArg, 0);
        if (unCS != null) {
            ConversionSpecification sFmt = new ConversionSpecification(this);
            sFmt.setLiteral(unCS);
            this.vFmt.addElement(sFmt);
        }
        while (this.cPos != -1 && this.cPos < fmtArg.length()) {
            int ePos = this.cPos + 1;
            while (ePos < fmtArg.length() && (c = fmtArg.charAt(ePos)) != 'i' && c != 'd' && c != 'f' && c != 'g' && c != 'G' && c != 'o' && c != 'x' && c != 'X' && c != 'e' && c != 'E' && c != 'c' && c != 's' && c != '%') {
                ePos++;
            }
            int ePos2 = Math.min(ePos + 1, fmtArg.length());
            this.vFmt.addElement(new ConversionSpecification(this, fmtArg.substring(this.cPos, ePos2)));
            String unCS2 = nonControl(fmtArg, ePos2);
            if (unCS2 != null) {
                ConversionSpecification sFmt2 = new ConversionSpecification(this);
                sFmt2.setLiteral(unCS2);
                this.vFmt.addElement(sFmt2);
            }
        }
    }

    private String nonControl(String s, int start) {
        this.cPos = s.indexOf(QuickTargetSourceCreator.PREFIX_THREAD_LOCAL, start);
        if (this.cPos == -1) {
            this.cPos = s.length();
        }
        return s.substring(start, this.cPos);
    }

    public String sprintf(Object[] o) {
        Enumeration e = this.vFmt.elements();
        int i = 0;
        StringBuffer sb = new StringBuffer();
        while (e.hasMoreElements()) {
            ConversionSpecification cs = (ConversionSpecification) e.nextElement();
            char c = cs.getConversionCharacter();
            if (c == 0) {
                sb.append(cs.getLiteral());
            } else if (c == '%') {
                sb.append(QuickTargetSourceCreator.PREFIX_THREAD_LOCAL);
            } else {
                if (cs.isPositionalSpecification()) {
                    i = cs.getArgumentPosition() - 1;
                    if (cs.isPositionalFieldWidth()) {
                        int ifw = cs.getArgumentPositionForFieldWidth() - 1;
                        cs.setFieldWidthWithArg(((Integer) o[ifw]).intValue());
                    }
                    if (cs.isPositionalPrecision()) {
                        int ipr = cs.getArgumentPositionForPrecision() - 1;
                        cs.setPrecisionWithArg(((Integer) o[ipr]).intValue());
                    }
                } else {
                    if (cs.isVariableFieldWidth()) {
                        cs.setFieldWidthWithArg(((Integer) o[i]).intValue());
                        i++;
                    }
                    if (cs.isVariablePrecision()) {
                        cs.setPrecisionWithArg(((Integer) o[i]).intValue());
                        i++;
                    }
                }
                if (o[i] instanceof Byte) {
                    sb.append(cs.internalsprintf((int) ((Byte) o[i]).byteValue()));
                } else if (o[i] instanceof Short) {
                    sb.append(cs.internalsprintf((int) ((Short) o[i]).shortValue()));
                } else if (o[i] instanceof Integer) {
                    sb.append(cs.internalsprintf(((Integer) o[i]).intValue()));
                } else if (o[i] instanceof Long) {
                    sb.append(cs.internalsprintf(((Long) o[i]).longValue()));
                } else if (o[i] instanceof Float) {
                    sb.append(cs.internalsprintf(((Float) o[i]).floatValue()));
                } else if (o[i] instanceof Double) {
                    sb.append(cs.internalsprintf(((Double) o[i]).doubleValue()));
                } else if (o[i] instanceof Character) {
                    sb.append(cs.internalsprintf((int) ((Character) o[i]).charValue()));
                } else if (o[i] instanceof String) {
                    sb.append(cs.internalsprintf((String) o[i]));
                } else {
                    sb.append(cs.internalsprintf(o[i]));
                }
                if (!cs.isPositionalSpecification()) {
                    i++;
                }
            }
        }
        return sb.toString();
    }

    public String sprintf() {
        Enumeration e = this.vFmt.elements();
        StringBuffer sb = new StringBuffer();
        while (e.hasMoreElements()) {
            ConversionSpecification cs = (ConversionSpecification) e.nextElement();
            char c = cs.getConversionCharacter();
            if (c == 0) {
                sb.append(cs.getLiteral());
            } else if (c == '%') {
                sb.append(QuickTargetSourceCreator.PREFIX_THREAD_LOCAL);
            }
        }
        return sb.toString();
    }

    public String sprintf(int x) throws IllegalArgumentException {
        Enumeration e = this.vFmt.elements();
        StringBuffer sb = new StringBuffer();
        while (e.hasMoreElements()) {
            ConversionSpecification cs = (ConversionSpecification) e.nextElement();
            char c = cs.getConversionCharacter();
            if (c == 0) {
                sb.append(cs.getLiteral());
            } else if (c == '%') {
                sb.append(QuickTargetSourceCreator.PREFIX_THREAD_LOCAL);
            } else {
                sb.append(cs.internalsprintf(x));
            }
        }
        return sb.toString();
    }

    public String sprintf(long x) throws IllegalArgumentException {
        Enumeration e = this.vFmt.elements();
        StringBuffer sb = new StringBuffer();
        while (e.hasMoreElements()) {
            ConversionSpecification cs = (ConversionSpecification) e.nextElement();
            char c = cs.getConversionCharacter();
            if (c == 0) {
                sb.append(cs.getLiteral());
            } else if (c == '%') {
                sb.append(QuickTargetSourceCreator.PREFIX_THREAD_LOCAL);
            } else {
                sb.append(cs.internalsprintf(x));
            }
        }
        return sb.toString();
    }

    public String sprintf(double x) throws IllegalArgumentException {
        Enumeration e = this.vFmt.elements();
        StringBuffer sb = new StringBuffer();
        while (e.hasMoreElements()) {
            ConversionSpecification cs = (ConversionSpecification) e.nextElement();
            char c = cs.getConversionCharacter();
            if (c == 0) {
                sb.append(cs.getLiteral());
            } else if (c == '%') {
                sb.append(QuickTargetSourceCreator.PREFIX_THREAD_LOCAL);
            } else {
                sb.append(cs.internalsprintf(x));
            }
        }
        return sb.toString();
    }

    public String sprintf(String x) throws IllegalArgumentException {
        Enumeration e = this.vFmt.elements();
        StringBuffer sb = new StringBuffer();
        while (e.hasMoreElements()) {
            ConversionSpecification cs = (ConversionSpecification) e.nextElement();
            char c = cs.getConversionCharacter();
            if (c == 0) {
                sb.append(cs.getLiteral());
            } else if (c == '%') {
                sb.append(QuickTargetSourceCreator.PREFIX_THREAD_LOCAL);
            } else {
                sb.append(cs.internalsprintf(x));
            }
        }
        return sb.toString();
    }

    public String sprintf(Object x) throws IllegalArgumentException {
        Enumeration e = this.vFmt.elements();
        StringBuffer sb = new StringBuffer();
        while (e.hasMoreElements()) {
            ConversionSpecification cs = (ConversionSpecification) e.nextElement();
            char c = cs.getConversionCharacter();
            if (c == 0) {
                sb.append(cs.getLiteral());
            } else if (c == '%') {
                sb.append(QuickTargetSourceCreator.PREFIX_THREAD_LOCAL);
            } else if (x instanceof Byte) {
                sb.append(cs.internalsprintf((int) ((Byte) x).byteValue()));
            } else if (x instanceof Short) {
                sb.append(cs.internalsprintf((int) ((Short) x).shortValue()));
            } else if (x instanceof Integer) {
                sb.append(cs.internalsprintf(((Integer) x).intValue()));
            } else if (x instanceof Long) {
                sb.append(cs.internalsprintf(((Long) x).longValue()));
            } else if (x instanceof Float) {
                sb.append(cs.internalsprintf(((Float) x).floatValue()));
            } else if (x instanceof Double) {
                sb.append(cs.internalsprintf(((Double) x).doubleValue()));
            } else if (x instanceof Character) {
                sb.append(cs.internalsprintf((int) ((Character) x).charValue()));
            } else if (x instanceof String) {
                sb.append(cs.internalsprintf((String) x));
            } else {
                sb.append(cs.internalsprintf(x));
            }
        }
        return sb.toString();
    }

    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/util/PrintfFormat$ConversionSpecification.class */
    private class ConversionSpecification {
        private boolean thousands;
        private boolean leftJustify;
        private boolean leadingSign;
        private boolean leadingSpace;
        private boolean alternateForm;
        private boolean leadingZeros;
        private boolean variableFieldWidth;
        private int fieldWidth;
        private boolean fieldWidthSet;
        private int precision;
        private static final int defaultDigits = 6;
        private boolean variablePrecision;
        private boolean precisionSet;
        private boolean positionalSpecification;
        private int argumentPosition;
        private boolean positionalFieldWidth;
        private int argumentPositionForFieldWidth;
        private boolean positionalPrecision;
        private int argumentPositionForPrecision;
        private boolean optionalh;
        private boolean optionall;
        private boolean optionalL;
        private char conversionCharacter;
        private int pos;
        private String fmt;
        private final PrintfFormat this$0;

        ConversionSpecification(PrintfFormat printfFormat) {
            this.this$0 = printfFormat;
            this.thousands = false;
            this.leftJustify = false;
            this.leadingSign = false;
            this.leadingSpace = false;
            this.alternateForm = false;
            this.leadingZeros = false;
            this.variableFieldWidth = false;
            this.fieldWidth = 0;
            this.fieldWidthSet = false;
            this.precision = 0;
            this.variablePrecision = false;
            this.precisionSet = false;
            this.positionalSpecification = false;
            this.argumentPosition = 0;
            this.positionalFieldWidth = false;
            this.argumentPositionForFieldWidth = 0;
            this.positionalPrecision = false;
            this.argumentPositionForPrecision = 0;
            this.optionalh = false;
            this.optionall = false;
            this.optionalL = false;
            this.conversionCharacter = (char) 0;
            this.pos = 0;
        }

        ConversionSpecification(PrintfFormat printfFormat, String fmtArg) throws IllegalArgumentException {
            this.this$0 = printfFormat;
            this.thousands = false;
            this.leftJustify = false;
            this.leadingSign = false;
            this.leadingSpace = false;
            this.alternateForm = false;
            this.leadingZeros = false;
            this.variableFieldWidth = false;
            this.fieldWidth = 0;
            this.fieldWidthSet = false;
            this.precision = 0;
            this.variablePrecision = false;
            this.precisionSet = false;
            this.positionalSpecification = false;
            this.argumentPosition = 0;
            this.positionalFieldWidth = false;
            this.argumentPositionForFieldWidth = 0;
            this.positionalPrecision = false;
            this.argumentPositionForPrecision = 0;
            this.optionalh = false;
            this.optionall = false;
            this.optionalL = false;
            this.conversionCharacter = (char) 0;
            this.pos = 0;
            if (fmtArg == null) {
                throw new NullPointerException();
            }
            if (fmtArg.length() == 0) {
                throw new IllegalArgumentException("Control strings must have positive lengths.");
            }
            if (fmtArg.charAt(0) == '%') {
                this.fmt = fmtArg;
                this.pos = 1;
                setArgPosition();
                setFlagCharacters();
                setFieldWidth();
                setPrecision();
                setOptionalHL();
                if (setConversionCharacter()) {
                    if (this.pos == fmtArg.length()) {
                        if (this.leadingZeros && this.leftJustify) {
                            this.leadingZeros = false;
                        }
                        if (this.precisionSet && this.leadingZeros) {
                            if (this.conversionCharacter == 'd' || this.conversionCharacter == 'i' || this.conversionCharacter == 'o' || this.conversionCharacter == 'x') {
                                this.leadingZeros = false;
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    throw new IllegalArgumentException(new StringBuffer().append("Malformed conversion specification=").append(fmtArg).toString());
                }
                throw new IllegalArgumentException(new StringBuffer().append("Malformed conversion specification=").append(fmtArg).toString());
            }
            throw new IllegalArgumentException("Control strings must begin with %.");
        }

        void setLiteral(String s) {
            this.fmt = s;
        }

        String getLiteral() {
            StringBuffer sb = new StringBuffer();
            int i = 0;
            while (i < this.fmt.length()) {
                if (this.fmt.charAt(i) == '\\') {
                    i++;
                    if (i < this.fmt.length()) {
                        char c = this.fmt.charAt(i);
                        switch (c) {
                            case '\\':
                                sb.append('\\');
                                break;
                            case 'a':
                                sb.append((char) 7);
                                break;
                            case 'b':
                                sb.append('\b');
                                break;
                            case 'f':
                                sb.append('\f');
                                break;
                            case 'n':
                                sb.append(System.getProperty("line.separator"));
                                break;
                            case 'r':
                                sb.append('\r');
                                break;
                            case 't':
                                sb.append('\t');
                                break;
                            case 'v':
                                sb.append((char) 11);
                                break;
                        }
                        i++;
                    } else {
                        sb.append('\\');
                    }
                } else {
                    i++;
                }
            }
            return this.fmt;
        }

        char getConversionCharacter() {
            return this.conversionCharacter;
        }

        boolean isVariableFieldWidth() {
            return this.variableFieldWidth;
        }

        void setFieldWidthWithArg(int fw) {
            if (fw < 0) {
                this.leftJustify = true;
            }
            this.fieldWidthSet = true;
            this.fieldWidth = Math.abs(fw);
        }

        boolean isVariablePrecision() {
            return this.variablePrecision;
        }

        void setPrecisionWithArg(int pr) {
            this.precisionSet = true;
            this.precision = Math.max(pr, 0);
        }

        String internalsprintf(int s) throws IllegalArgumentException {
            String s2;
            switch (this.conversionCharacter) {
                case 'C':
                case 'c':
                    s2 = printCFormat((char) s);
                    break;
                case 'X':
                case 'x':
                    if (this.optionalh) {
                        s2 = printXFormat((short) s);
                        break;
                    } else if (this.optionall) {
                        s2 = printXFormat(s);
                        break;
                    } else {
                        s2 = printXFormat(s);
                        break;
                    }
                case 'd':
                case 'i':
                    if (this.optionalh) {
                        s2 = printDFormat((short) s);
                        break;
                    } else if (this.optionall) {
                        s2 = printDFormat(s);
                        break;
                    } else {
                        s2 = printDFormat(s);
                        break;
                    }
                case 'o':
                    if (this.optionalh) {
                        s2 = printOFormat((short) s);
                        break;
                    } else if (this.optionall) {
                        s2 = printOFormat(s);
                        break;
                    } else {
                        s2 = printOFormat(s);
                        break;
                    }
                default:
                    throw new IllegalArgumentException(new StringBuffer().append("Cannot format a int with a format using a ").append(this.conversionCharacter).append(" conversion character.").toString());
            }
            return s2;
        }

        String internalsprintf(long s) throws IllegalArgumentException {
            String s2;
            switch (this.conversionCharacter) {
                case 'C':
                case 'c':
                    s2 = printCFormat((char) s);
                    break;
                case 'X':
                case 'x':
                    if (this.optionalh) {
                        s2 = printXFormat((short) s);
                        break;
                    } else if (this.optionall) {
                        s2 = printXFormat(s);
                        break;
                    } else {
                        s2 = printXFormat((int) s);
                        break;
                    }
                case 'd':
                case 'i':
                    if (this.optionalh) {
                        s2 = printDFormat((short) s);
                        break;
                    } else if (this.optionall) {
                        s2 = printDFormat(s);
                        break;
                    } else {
                        s2 = printDFormat((int) s);
                        break;
                    }
                case 'o':
                    if (this.optionalh) {
                        s2 = printOFormat((short) s);
                        break;
                    } else if (this.optionall) {
                        s2 = printOFormat(s);
                        break;
                    } else {
                        s2 = printOFormat((int) s);
                        break;
                    }
                default:
                    throw new IllegalArgumentException(new StringBuffer().append("Cannot format a long with a format using a ").append(this.conversionCharacter).append(" conversion character.").toString());
            }
            return s2;
        }

        String internalsprintf(double s) throws IllegalArgumentException {
            String s2;
            switch (this.conversionCharacter) {
                case 'E':
                case 'e':
                    s2 = printEFormat(s);
                    break;
                case 'G':
                case 'g':
                    s2 = printGFormat(s);
                    break;
                case 'f':
                    s2 = printFFormat(s);
                    break;
                default:
                    throw new IllegalArgumentException(new StringBuffer().append("Cannot format a double with a format using a ").append(this.conversionCharacter).append(" conversion character.").toString());
            }
            return s2;
        }

        String internalsprintf(String s) throws IllegalArgumentException {
            if (this.conversionCharacter == 's' || this.conversionCharacter == 'S') {
                String s2 = printSFormat(s);
                return s2;
            }
            throw new IllegalArgumentException(new StringBuffer().append("Cannot format a String with a format using a ").append(this.conversionCharacter).append(" conversion character.").toString());
        }

        String internalsprintf(Object s) {
            if (s == null) {
                return "";
            }
            if (this.conversionCharacter == 's' || this.conversionCharacter == 'S') {
                String s2 = printSFormat(s.toString());
                return s2;
            }
            throw new IllegalArgumentException(new StringBuffer().append("Cannot format a String with a format using a ").append(this.conversionCharacter).append(" conversion character.").toString());
        }

        private char[] fFormatDigits(double x) throws NumberFormatException {
            String sx;
            int n1In;
            int n2In;
            char[] ca3;
            char[] ca4;
            int j;
            char[] ca5;
            int expon = 0;
            boolean minusSign = false;
            if (x > 0.0d) {
                sx = Double.toString(x);
            } else if (x < 0.0d) {
                sx = Double.toString(-x);
                minusSign = true;
            } else {
                sx = Double.toString(x);
                if (sx.charAt(0) == '-') {
                    minusSign = true;
                    sx = sx.substring(1);
                }
            }
            int ePos = sx.indexOf(69);
            int rPos = sx.indexOf(46);
            if (rPos != -1) {
                n1In = rPos;
            } else {
                n1In = ePos != -1 ? ePos : sx.length();
            }
            if (rPos != -1) {
                n2In = ePos != -1 ? (ePos - rPos) - 1 : (sx.length() - rPos) - 1;
            } else {
                n2In = 0;
            }
            if (ePos != -1) {
                int ie = ePos + 1;
                expon = 0;
                if (sx.charAt(ie) == '-') {
                    do {
                        ie++;
                        if (ie >= sx.length()) {
                            break;
                        }
                    } while (sx.charAt(ie) == '0');
                    if (ie < sx.length()) {
                        expon = -Integer.parseInt(sx.substring(ie));
                    }
                } else {
                    if (sx.charAt(ie) == '+') {
                        ie++;
                    }
                    while (ie < sx.length() && sx.charAt(ie) == '0') {
                        ie++;
                    }
                    if (ie < sx.length()) {
                        expon = Integer.parseInt(sx.substring(ie));
                    }
                }
            }
            int p = this.precisionSet ? this.precision : 5;
            char[] ca1 = sx.toCharArray();
            char[] ca2 = new char[n1In + n2In];
            int j2 = 0;
            while (j2 < n1In) {
                ca2[j2] = ca1[j2];
                j2++;
            }
            int i = j2 + 1;
            for (int k = 0; k < n2In; k++) {
                ca2[j2] = ca1[i];
                j2++;
                i++;
            }
            if (n1In + expon <= 0) {
                ca3 = new char[(-expon) + n2In];
                int j3 = 0;
                int k2 = 0;
                while (k2 < (-n1In) - expon) {
                    ca3[j3] = '0';
                    k2++;
                    j3++;
                }
                int i2 = 0;
                while (i2 < n1In + n2In) {
                    ca3[j3] = ca2[i2];
                    i2++;
                    j3++;
                }
            } else {
                ca3 = ca2;
            }
            boolean carry = false;
            if (p < (-expon) + n2In) {
                int i3 = expon < 0 ? p : p + n1In;
                carry = checkForCarry(ca3, i3);
                if (carry) {
                    carry = startSymbolicCarry(ca3, i3 - 1, 0);
                }
            }
            if (n1In + expon <= 0) {
                ca4 = new char[2 + p];
                if (carry) {
                    ca4[0] = '1';
                } else {
                    ca4[0] = '0';
                }
                if (this.alternateForm || !this.precisionSet || this.precision != 0) {
                    ca4[1] = '.';
                    int i4 = 0;
                    int j4 = 2;
                    while (i4 < Math.min(p, ca3.length)) {
                        ca4[j4] = ca3[i4];
                        i4++;
                        j4++;
                    }
                    while (j4 < ca4.length) {
                        ca4[j4] = '0';
                        j4++;
                    }
                }
            } else {
                if (!carry) {
                    if (this.alternateForm || !this.precisionSet || this.precision != 0) {
                        ca4 = new char[n1In + expon + p + 1];
                    } else {
                        ca4 = new char[n1In + expon];
                    }
                    j = 0;
                } else {
                    if (this.alternateForm || !this.precisionSet || this.precision != 0) {
                        ca4 = new char[n1In + expon + p + 2];
                    } else {
                        ca4 = new char[n1In + expon + 1];
                    }
                    ca4[0] = '1';
                    j = 1;
                }
                int i5 = 0;
                while (i5 < Math.min(n1In + expon, ca3.length)) {
                    ca4[j] = ca3[i5];
                    i5++;
                    j++;
                }
                while (i5 < n1In + expon) {
                    ca4[j] = '0';
                    i5++;
                    j++;
                }
                if (this.alternateForm || !this.precisionSet || this.precision != 0) {
                    ca4[j] = '.';
                    int j5 = j + 1;
                    for (int k3 = 0; i5 < ca3.length && k3 < p; k3++) {
                        ca4[j5] = ca3[i5];
                        i5++;
                        j5++;
                    }
                    while (j5 < ca4.length) {
                        ca4[j5] = '0';
                        j5++;
                    }
                }
            }
            int nZeros = 0;
            if (!this.leftJustify && this.leadingZeros) {
                int xThousands = 0;
                if (this.thousands) {
                    int xlead = 0;
                    if (ca4[0] == '+' || ca4[0] == '-' || ca4[0] == ' ') {
                        xlead = 1;
                    }
                    int xdp = xlead;
                    while (xdp < ca4.length && ca4[xdp] != '.') {
                        xdp++;
                    }
                    xThousands = (xdp - xlead) / 3;
                }
                if (this.fieldWidthSet) {
                    nZeros = this.fieldWidth - ca4.length;
                }
                if ((!minusSign && (this.leadingSign || this.leadingSpace)) || minusSign) {
                    nZeros--;
                }
                nZeros -= xThousands;
                if (nZeros < 0) {
                    nZeros = 0;
                }
            }
            int j6 = 0;
            if ((!minusSign && (this.leadingSign || this.leadingSpace)) || minusSign) {
                ca5 = new char[ca4.length + nZeros + 1];
                j6 = 0 + 1;
            } else {
                ca5 = new char[ca4.length + nZeros];
            }
            if (!minusSign) {
                if (this.leadingSign) {
                    ca5[0] = '+';
                }
                if (this.leadingSpace) {
                    ca5[0] = ' ';
                }
            } else {
                ca5[0] = '-';
            }
            int i6 = 0;
            while (i6 < nZeros) {
                ca5[j6] = '0';
                i6++;
                j6++;
            }
            int i7 = 0;
            while (i7 < ca4.length) {
                ca5[j6] = ca4[i7];
                i7++;
                j6++;
            }
            int lead = 0;
            if (ca5[0] == '+' || ca5[0] == '-' || ca5[0] == ' ') {
                lead = 1;
            }
            int dp = lead;
            while (dp < ca5.length && ca5[dp] != '.') {
                dp++;
            }
            int nThousands = (dp - lead) / 3;
            if (dp < ca5.length) {
                ca5[dp] = this.this$0.dfs.getDecimalSeparator();
            }
            char[] ca6 = ca5;
            if (this.thousands && nThousands > 0) {
                ca6 = new char[ca5.length + nThousands + lead];
                ca6[0] = ca5[0];
                int i8 = lead;
                int k4 = lead;
                while (i8 < dp) {
                    if (i8 > 0 && (dp - i8) % 3 == 0) {
                        ca6[k4] = this.this$0.dfs.getGroupingSeparator();
                        ca6[k4 + 1] = ca5[i8];
                        k4 += 2;
                    } else {
                        ca6[k4] = ca5[i8];
                        k4++;
                    }
                    i8++;
                }
                while (i8 < ca5.length) {
                    ca6[k4] = ca5[i8];
                    i8++;
                    k4++;
                }
            }
            return ca6;
        }

        private String fFormatString(double x) throws NumberFormatException {
            char[] ca6;
            if (Double.isInfinite(x)) {
                if (x == Double.POSITIVE_INFINITY) {
                    if (this.leadingSign) {
                        ca6 = "+Inf".toCharArray();
                    } else if (this.leadingSpace) {
                        ca6 = " Inf".toCharArray();
                    } else {
                        ca6 = "Inf".toCharArray();
                    }
                } else {
                    ca6 = "-Inf".toCharArray();
                }
            } else if (Double.isNaN(x)) {
                if (this.leadingSign) {
                    ca6 = "+NaN".toCharArray();
                } else if (this.leadingSpace) {
                    ca6 = " NaN".toCharArray();
                } else {
                    ca6 = "NaN".toCharArray();
                }
            } else {
                ca6 = fFormatDigits(x);
            }
            char[] ca7 = applyFloatPadding(ca6, false);
            return new String(ca7);
        }

        private char[] eFormatDigits(double x, char eChar) throws NumberFormatException {
            String sx;
            char[] ca1;
            int i0;
            char[] ca2;
            int j;
            int i;
            int i2;
            char[] ca3;
            int expon = 0;
            boolean minusSign = false;
            if (x > 0.0d) {
                sx = Double.toString(x);
            } else if (x < 0.0d) {
                sx = Double.toString(-x);
                minusSign = true;
            } else {
                sx = Double.toString(x);
                if (sx.charAt(0) == '-') {
                    minusSign = true;
                    sx = sx.substring(1);
                }
            }
            int ePos = sx.indexOf(69);
            if (ePos == -1) {
                ePos = sx.indexOf(101);
            }
            int rPos = sx.indexOf(46);
            if (rPos == -1 && ePos == -1) {
                sx.length();
            }
            if (rPos != -1) {
                if (ePos != -1) {
                    int i3 = (ePos - rPos) - 1;
                } else {
                    int length = (sx.length() - rPos) - 1;
                }
            }
            if (ePos != -1) {
                int ie = ePos + 1;
                expon = 0;
                if (sx.charAt(ie) == '-') {
                    do {
                        ie++;
                        if (ie >= sx.length()) {
                            break;
                        }
                    } while (sx.charAt(ie) == '0');
                    if (ie < sx.length()) {
                        expon = -Integer.parseInt(sx.substring(ie));
                    }
                } else {
                    if (sx.charAt(ie) == '+') {
                        ie++;
                    }
                    while (ie < sx.length() && sx.charAt(ie) == '0') {
                        ie++;
                    }
                    if (ie < sx.length()) {
                        expon = Integer.parseInt(sx.substring(ie));
                    }
                }
            }
            if (rPos != -1) {
                expon += rPos - 1;
            }
            int p = this.precisionSet ? this.precision : 5;
            if (rPos != -1 && ePos != -1) {
                ca1 = new StringBuffer().append(sx.substring(0, rPos)).append(sx.substring(rPos + 1, ePos)).toString().toCharArray();
            } else if (rPos != -1) {
                ca1 = new StringBuffer().append(sx.substring(0, rPos)).append(sx.substring(rPos + 1)).toString().toCharArray();
            } else if (ePos != -1) {
                ca1 = sx.substring(0, ePos).toCharArray();
            } else {
                ca1 = sx.toCharArray();
            }
            if (ca1[0] != '0') {
                i0 = 0;
            } else {
                i0 = 0;
                while (i0 < ca1.length && ca1[i0] == '0') {
                    i0++;
                }
            }
            if (i0 + p < ca1.length - 1) {
                boolean carry = checkForCarry(ca1, i0 + p + 1);
                if (carry) {
                    carry = startSymbolicCarry(ca1, i0 + p, i0);
                }
                if (carry) {
                    char[] ca22 = new char[i0 + p + 1];
                    ca22[i0] = '1';
                    for (int j2 = 0; j2 < i0; j2++) {
                        ca22[j2] = '0';
                    }
                    int i4 = i0;
                    for (int j3 = i0 + 1; j3 < p + 1; j3++) {
                        ca22[j3] = ca1[i4];
                        i4++;
                    }
                    expon++;
                    ca1 = ca22;
                }
            }
            int eSize = (Math.abs(expon) >= 100 || this.optionalL) ? 5 : 4;
            if (this.alternateForm || !this.precisionSet || this.precision != 0) {
                ca2 = new char[2 + p + eSize];
            } else {
                ca2 = new char[1 + eSize];
            }
            if (ca1[0] != '0') {
                ca2[0] = ca1[0];
                j = 1;
            } else {
                int j4 = 1;
                while (true) {
                    if (j4 >= (ePos == -1 ? ca1.length : ePos) || ca1[j4] != '0') {
                        break;
                    }
                    j4++;
                }
                if ((ePos != -1 && j4 < ePos) || (ePos == -1 && j4 < ca1.length)) {
                    ca2[0] = ca1[j4];
                    expon -= j4;
                    j = j4 + 1;
                } else {
                    ca2[0] = '0';
                    j = 2;
                }
            }
            if (this.alternateForm || !this.precisionSet || this.precision != 0) {
                ca2[1] = '.';
                i = 2;
            } else {
                i = 1;
            }
            for (int k = 0; k < p && j < ca1.length; k++) {
                ca2[i] = ca1[j];
                j++;
                i++;
            }
            while (i < ca2.length - eSize) {
                ca2[i] = '0';
                i++;
            }
            int i5 = i;
            int i6 = i + 1;
            ca2[i5] = eChar;
            if (expon < 0) {
                i2 = i6 + 1;
                ca2[i6] = '-';
            } else {
                i2 = i6 + 1;
                ca2[i6] = '+';
            }
            int expon2 = Math.abs(expon);
            if (expon2 >= 100) {
                switch (expon2 / 100) {
                    case 1:
                        ca2[i2] = '1';
                        break;
                    case 2:
                        ca2[i2] = '2';
                        break;
                    case 3:
                        ca2[i2] = '3';
                        break;
                    case 4:
                        ca2[i2] = '4';
                        break;
                    case 5:
                        ca2[i2] = '5';
                        break;
                    case 6:
                        ca2[i2] = '6';
                        break;
                    case 7:
                        ca2[i2] = '7';
                        break;
                    case 8:
                        ca2[i2] = '8';
                        break;
                    case 9:
                        ca2[i2] = '9';
                        break;
                }
                i2++;
            }
            switch ((expon2 % 100) / 10) {
                case 0:
                    ca2[i2] = '0';
                    break;
                case 1:
                    ca2[i2] = '1';
                    break;
                case 2:
                    ca2[i2] = '2';
                    break;
                case 3:
                    ca2[i2] = '3';
                    break;
                case 4:
                    ca2[i2] = '4';
                    break;
                case 5:
                    ca2[i2] = '5';
                    break;
                case 6:
                    ca2[i2] = '6';
                    break;
                case 7:
                    ca2[i2] = '7';
                    break;
                case 8:
                    ca2[i2] = '8';
                    break;
                case 9:
                    ca2[i2] = '9';
                    break;
            }
            int i7 = i2 + 1;
            switch (expon2 % 10) {
                case 0:
                    ca2[i7] = '0';
                    break;
                case 1:
                    ca2[i7] = '1';
                    break;
                case 2:
                    ca2[i7] = '2';
                    break;
                case 3:
                    ca2[i7] = '3';
                    break;
                case 4:
                    ca2[i7] = '4';
                    break;
                case 5:
                    ca2[i7] = '5';
                    break;
                case 6:
                    ca2[i7] = '6';
                    break;
                case 7:
                    ca2[i7] = '7';
                    break;
                case 8:
                    ca2[i7] = '8';
                    break;
                case 9:
                    ca2[i7] = '9';
                    break;
            }
            int nZeros = 0;
            if (!this.leftJustify && this.leadingZeros) {
                int xThousands = 0;
                if (this.thousands) {
                    int xlead = 0;
                    if (ca2[0] == '+' || ca2[0] == '-' || ca2[0] == ' ') {
                        xlead = 1;
                    }
                    int xdp = xlead;
                    while (xdp < ca2.length && ca2[xdp] != '.') {
                        xdp++;
                    }
                    xThousands = (xdp - xlead) / 3;
                }
                if (this.fieldWidthSet) {
                    nZeros = this.fieldWidth - ca2.length;
                }
                if ((!minusSign && (this.leadingSign || this.leadingSpace)) || minusSign) {
                    nZeros--;
                }
                nZeros -= xThousands;
                if (nZeros < 0) {
                    nZeros = 0;
                }
            }
            int j5 = 0;
            if ((!minusSign && (this.leadingSign || this.leadingSpace)) || minusSign) {
                ca3 = new char[ca2.length + nZeros + 1];
                j5 = 0 + 1;
            } else {
                ca3 = new char[ca2.length + nZeros];
            }
            if (!minusSign) {
                if (this.leadingSign) {
                    ca3[0] = '+';
                }
                if (this.leadingSpace) {
                    ca3[0] = ' ';
                }
            } else {
                ca3[0] = '-';
            }
            for (int k2 = 0; k2 < nZeros; k2++) {
                ca3[j5] = '0';
                j5++;
            }
            int i8 = 0;
            while (i8 < ca2.length && j5 < ca3.length) {
                ca3[j5] = ca2[i8];
                i8++;
                j5++;
            }
            int lead = 0;
            if (ca3[0] == '+' || ca3[0] == '-' || ca3[0] == ' ') {
                lead = 1;
            }
            int dp = lead;
            while (dp < ca3.length && ca3[dp] != '.') {
                dp++;
            }
            int nThousands = dp / 3;
            if (dp < ca3.length) {
                ca3[dp] = this.this$0.dfs.getDecimalSeparator();
            }
            char[] ca4 = ca3;
            if (this.thousands && nThousands > 0) {
                ca4 = new char[ca3.length + nThousands + lead];
                ca4[0] = ca3[0];
                int i9 = lead;
                int k3 = lead;
                while (i9 < dp) {
                    if (i9 > 0 && (dp - i9) % 3 == 0) {
                        ca4[k3] = this.this$0.dfs.getGroupingSeparator();
                        ca4[k3 + 1] = ca3[i9];
                        k3 += 2;
                    } else {
                        ca4[k3] = ca3[i9];
                        k3++;
                    }
                    i9++;
                }
                while (i9 < ca3.length) {
                    ca4[k3] = ca3[i9];
                    i9++;
                    k3++;
                }
            }
            return ca4;
        }

        private boolean checkForCarry(char[] ca1, int icarry) {
            boolean carry = false;
            if (icarry < ca1.length) {
                if (ca1[icarry] == '6' || ca1[icarry] == '7' || ca1[icarry] == '8' || ca1[icarry] == '9') {
                    carry = true;
                } else if (ca1[icarry] == '5') {
                    int ii = icarry + 1;
                    while (ii < ca1.length && ca1[ii] == '0') {
                        ii++;
                    }
                    carry = ii < ca1.length;
                    if (!carry && icarry > 0) {
                        carry = ca1[icarry - 1] == '1' || ca1[icarry - 1] == '3' || ca1[icarry - 1] == '5' || ca1[icarry - 1] == '7' || ca1[icarry - 1] == '9';
                    }
                }
            }
            return carry;
        }

        private boolean startSymbolicCarry(char[] ca, int cLast, int cFirst) {
            boolean carry = true;
            for (int i = cLast; carry && i >= cFirst; i--) {
                carry = false;
                switch (ca[i]) {
                    case '0':
                        ca[i] = '1';
                        break;
                    case '1':
                        ca[i] = '2';
                        break;
                    case '2':
                        ca[i] = '3';
                        break;
                    case '3':
                        ca[i] = '4';
                        break;
                    case '4':
                        ca[i] = '5';
                        break;
                    case '5':
                        ca[i] = '6';
                        break;
                    case '6':
                        ca[i] = '7';
                        break;
                    case '7':
                        ca[i] = '8';
                        break;
                    case '8':
                        ca[i] = '9';
                        break;
                    case '9':
                        ca[i] = '0';
                        carry = true;
                        break;
                }
            }
            return carry;
        }

        private String eFormatString(double x, char eChar) throws NumberFormatException {
            char[] ca4;
            if (Double.isInfinite(x)) {
                if (x == Double.POSITIVE_INFINITY) {
                    if (this.leadingSign) {
                        ca4 = "+Inf".toCharArray();
                    } else if (this.leadingSpace) {
                        ca4 = " Inf".toCharArray();
                    } else {
                        ca4 = "Inf".toCharArray();
                    }
                } else {
                    ca4 = "-Inf".toCharArray();
                }
            } else if (Double.isNaN(x)) {
                if (this.leadingSign) {
                    ca4 = "+NaN".toCharArray();
                } else if (this.leadingSpace) {
                    ca4 = " NaN".toCharArray();
                } else {
                    ca4 = "NaN".toCharArray();
                }
            } else {
                ca4 = eFormatDigits(x, eChar);
            }
            char[] ca5 = applyFloatPadding(ca4, false);
            return new String(ca5);
        }

        private char[] applyFloatPadding(char[] ca4, boolean noDigits) {
            int nBlanks;
            char[] ca5 = ca4;
            if (this.fieldWidthSet) {
                if (this.leftJustify) {
                    int nBlanks2 = this.fieldWidth - ca4.length;
                    if (nBlanks2 > 0) {
                        ca5 = new char[ca4.length + nBlanks2];
                        int i = 0;
                        while (i < ca4.length) {
                            ca5[i] = ca4[i];
                            i++;
                        }
                        int j = 0;
                        while (j < nBlanks2) {
                            ca5[i] = ' ';
                            j++;
                            i++;
                        }
                    }
                } else if (!this.leadingZeros || noDigits) {
                    int nBlanks3 = this.fieldWidth - ca4.length;
                    if (nBlanks3 > 0) {
                        ca5 = new char[ca4.length + nBlanks3];
                        int i2 = 0;
                        while (i2 < nBlanks3) {
                            ca5[i2] = ' ';
                            i2++;
                        }
                        for (char c : ca4) {
                            ca5[i2] = c;
                            i2++;
                        }
                    }
                } else if (this.leadingZeros && (nBlanks = this.fieldWidth - ca4.length) > 0) {
                    ca5 = new char[ca4.length + nBlanks];
                    int i3 = 0;
                    int j2 = 0;
                    if (ca4[0] == '-') {
                        ca5[0] = '-';
                        i3 = 0 + 1;
                        j2 = 0 + 1;
                    }
                    for (int k = 0; k < nBlanks; k++) {
                        ca5[i3] = '0';
                        i3++;
                    }
                    while (j2 < ca4.length) {
                        ca5[i3] = ca4[j2];
                        i3++;
                        j2++;
                    }
                }
            }
            return ca5;
        }

        private String printFFormat(double x) {
            return fFormatString(x);
        }

        private String printEFormat(double x) {
            if (this.conversionCharacter == 'e') {
                return eFormatString(x, 'e');
            }
            return eFormatString(x, 'E');
        }

        private String printGFormat(double x) throws NumberFormatException {
            String sx;
            int ePos;
            String ret;
            char[] ca4;
            String sy;
            String sz;
            int savePrecision = this.precision;
            if (Double.isInfinite(x)) {
                if (x == Double.POSITIVE_INFINITY) {
                    if (this.leadingSign) {
                        ca4 = "+Inf".toCharArray();
                    } else if (this.leadingSpace) {
                        ca4 = " Inf".toCharArray();
                    } else {
                        ca4 = "Inf".toCharArray();
                    }
                } else {
                    ca4 = "-Inf".toCharArray();
                }
            } else if (Double.isNaN(x)) {
                if (this.leadingSign) {
                    ca4 = "+NaN".toCharArray();
                } else if (this.leadingSpace) {
                    ca4 = " NaN".toCharArray();
                } else {
                    ca4 = "NaN".toCharArray();
                }
            } else {
                if (!this.precisionSet) {
                    this.precision = 6;
                }
                if (this.precision == 0) {
                    this.precision = 1;
                }
                if (this.conversionCharacter == 'g') {
                    sx = eFormatString(x, 'e').trim();
                    ePos = sx.indexOf(101);
                } else {
                    sx = eFormatString(x, 'E').trim();
                    ePos = sx.indexOf(69);
                }
                int i = ePos + 1;
                int expon = 0;
                if (sx.charAt(i) == '-') {
                    do {
                        i++;
                        if (i >= sx.length()) {
                            break;
                        }
                    } while (sx.charAt(i) == '0');
                    if (i < sx.length()) {
                        expon = -Integer.parseInt(sx.substring(i));
                    }
                } else {
                    if (sx.charAt(i) == '+') {
                        i++;
                    }
                    while (i < sx.length() && sx.charAt(i) == '0') {
                        i++;
                    }
                    if (i < sx.length()) {
                        expon = Integer.parseInt(sx.substring(i));
                    }
                }
                if (!this.alternateForm) {
                    if (expon >= -4 && expon < this.precision) {
                        sy = fFormatString(x).trim();
                    } else {
                        sy = sx.substring(0, ePos);
                    }
                    int i2 = sy.length() - 1;
                    while (i2 >= 0 && sy.charAt(i2) == '0') {
                        i2--;
                    }
                    if (i2 >= 0 && sy.charAt(i2) == '.') {
                        i2--;
                    }
                    if (i2 == -1) {
                        sz = "0";
                    } else if (!Character.isDigit(sy.charAt(i2))) {
                        sz = new StringBuffer().append(sy.substring(0, i2 + 1)).append("0").toString();
                    } else {
                        sz = sy.substring(0, i2 + 1);
                    }
                    if (expon >= -4 && expon < this.precision) {
                        ret = sz;
                    } else {
                        ret = new StringBuffer().append(sz).append(sx.substring(ePos)).toString();
                    }
                } else if (expon >= -4 && expon < this.precision) {
                    ret = fFormatString(x).trim();
                } else {
                    ret = sx;
                }
                if (this.leadingSpace && x >= 0.0d) {
                    ret = new StringBuffer().append(SymbolConstants.SPACE_SYMBOL).append(ret).toString();
                }
                ca4 = ret.toCharArray();
            }
            char[] ca5 = applyFloatPadding(ca4, false);
            this.precision = savePrecision;
            return new String(ca5);
        }

        private String printDFormat(short x) {
            return printDFormat(Short.toString(x));
        }

        private String printDFormat(long x) {
            return printDFormat(Long.toString(x));
        }

        private String printDFormat(int x) {
            return printDFormat(Integer.toString(x));
        }

        private String printDFormat(String sx) {
            int nLeadingZeros = 0;
            int nBlanks = 0;
            int n = 0;
            int i = 0;
            boolean neg = sx.charAt(0) == '-';
            if (sx.equals("0") && this.precisionSet && this.precision == 0) {
                sx = "";
            }
            if (!neg) {
                if (this.precisionSet && sx.length() < this.precision) {
                    nLeadingZeros = this.precision - sx.length();
                }
            } else if (this.precisionSet && sx.length() - 1 < this.precision) {
                nLeadingZeros = (this.precision - sx.length()) + 1;
            }
            if (nLeadingZeros < 0) {
                nLeadingZeros = 0;
            }
            if (this.fieldWidthSet) {
                nBlanks = (this.fieldWidth - nLeadingZeros) - sx.length();
                if (!neg && (this.leadingSign || this.leadingSpace)) {
                    nBlanks--;
                }
            }
            if (nBlanks < 0) {
                nBlanks = 0;
            }
            if (this.leadingSign || this.leadingSpace) {
                n = 0 + 1;
            }
            char[] ca = new char[n + nBlanks + nLeadingZeros + sx.length()];
            if (this.leftJustify) {
                if (neg) {
                    i = 0 + 1;
                    ca[0] = '-';
                } else if (this.leadingSign) {
                    i = 0 + 1;
                    ca[0] = '+';
                } else if (this.leadingSpace) {
                    i = 0 + 1;
                    ca[0] = ' ';
                }
                char[] csx = sx.toCharArray();
                int jFirst = neg ? 1 : 0;
                for (int j = 0; j < nLeadingZeros; j++) {
                    ca[i] = '0';
                    i++;
                }
                int j2 = jFirst;
                while (j2 < csx.length) {
                    ca[i] = csx[j2];
                    j2++;
                    i++;
                }
                for (int j3 = 0; j3 < nBlanks; j3++) {
                    ca[i] = ' ';
                    i++;
                }
            } else {
                if (!this.leadingZeros) {
                    i = 0;
                    while (i < nBlanks) {
                        ca[i] = ' ';
                        i++;
                    }
                    if (neg) {
                        int i2 = i;
                        i++;
                        ca[i2] = '-';
                    } else if (this.leadingSign) {
                        int i3 = i;
                        i++;
                        ca[i3] = '+';
                    } else if (this.leadingSpace) {
                        int i4 = i;
                        i++;
                        ca[i4] = ' ';
                    }
                } else {
                    if (neg) {
                        i = 0 + 1;
                        ca[0] = '-';
                    } else if (this.leadingSign) {
                        i = 0 + 1;
                        ca[0] = '+';
                    } else if (this.leadingSpace) {
                        i = 0 + 1;
                        ca[0] = ' ';
                    }
                    int j4 = 0;
                    while (j4 < nBlanks) {
                        ca[i] = '0';
                        j4++;
                        i++;
                    }
                }
                int j5 = 0;
                while (j5 < nLeadingZeros) {
                    ca[i] = '0';
                    j5++;
                    i++;
                }
                char[] csx2 = sx.toCharArray();
                int jFirst2 = neg ? 1 : 0;
                int j6 = jFirst2;
                while (j6 < csx2.length) {
                    ca[i] = csx2[j6];
                    j6++;
                    i++;
                }
            }
            return new String(ca);
        }

        private String printXFormat(short x) {
            String t;
            String sx = null;
            if (x == Short.MIN_VALUE) {
                sx = "8000";
            } else if (x < 0) {
                if (x == Short.MIN_VALUE) {
                    t = "0";
                } else {
                    t = Integer.toString((((-x) - 1) ^ (-1)) ^ (-32768), 16);
                    if (t.charAt(0) == 'F' || t.charAt(0) == 'f') {
                        t = t.substring(16, 32);
                    }
                }
                switch (t.length()) {
                    case 1:
                        sx = new StringBuffer().append("800").append(t).toString();
                        break;
                    case 2:
                        sx = new StringBuffer().append("80").append(t).toString();
                        break;
                    case 3:
                        sx = new StringBuffer().append("8").append(t).toString();
                        break;
                    case 4:
                        switch (t.charAt(0)) {
                            case '1':
                                sx = new StringBuffer().append("9").append(t.substring(1, 4)).toString();
                                break;
                            case '2':
                                sx = new StringBuffer().append("a").append(t.substring(1, 4)).toString();
                                break;
                            case '3':
                                sx = new StringBuffer().append("b").append(t.substring(1, 4)).toString();
                                break;
                            case '4':
                                sx = new StringBuffer().append(ExcelXmlConstants.CELL_TAG).append(t.substring(1, 4)).toString();
                                break;
                            case '5':
                                sx = new StringBuffer().append(DateTokenConverter.CONVERTER_KEY).append(t.substring(1, 4)).toString();
                                break;
                            case '6':
                                sx = new StringBuffer().append("e").append(t.substring(1, 4)).toString();
                                break;
                            case '7':
                                sx = new StringBuffer().append(ExcelXmlConstants.CELL_FORMULA_TAG).append(t.substring(1, 4)).toString();
                                break;
                        }
                }
            } else {
                sx = Integer.toString(x, 16);
            }
            return printXFormat(sx);
        }

        private String printXFormat(long x) {
            String sx = null;
            if (x == Long.MIN_VALUE) {
                sx = "8000000000000000";
            } else if (x < 0) {
                String t = Long.toString((((-x) - 1) ^ (-1)) ^ Long.MIN_VALUE, 16);
                switch (t.length()) {
                    case 1:
                        sx = new StringBuffer().append("800000000000000").append(t).toString();
                        break;
                    case 2:
                        sx = new StringBuffer().append("80000000000000").append(t).toString();
                        break;
                    case 3:
                        sx = new StringBuffer().append("8000000000000").append(t).toString();
                        break;
                    case 4:
                        sx = new StringBuffer().append("800000000000").append(t).toString();
                        break;
                    case 5:
                        sx = new StringBuffer().append("80000000000").append(t).toString();
                        break;
                    case 6:
                        sx = new StringBuffer().append("8000000000").append(t).toString();
                        break;
                    case 7:
                        sx = new StringBuffer().append("800000000").append(t).toString();
                        break;
                    case 8:
                        sx = new StringBuffer().append("80000000").append(t).toString();
                        break;
                    case 9:
                        sx = new StringBuffer().append("8000000").append(t).toString();
                        break;
                    case 10:
                        sx = new StringBuffer().append("800000").append(t).toString();
                        break;
                    case 11:
                        sx = new StringBuffer().append("80000").append(t).toString();
                        break;
                    case 12:
                        sx = new StringBuffer().append("8000").append(t).toString();
                        break;
                    case 13:
                        sx = new StringBuffer().append("800").append(t).toString();
                        break;
                    case 14:
                        sx = new StringBuffer().append("80").append(t).toString();
                        break;
                    case 15:
                        sx = new StringBuffer().append("8").append(t).toString();
                        break;
                    case 16:
                        switch (t.charAt(0)) {
                            case '1':
                                sx = new StringBuffer().append("9").append(t.substring(1, 16)).toString();
                                break;
                            case '2':
                                sx = new StringBuffer().append("a").append(t.substring(1, 16)).toString();
                                break;
                            case '3':
                                sx = new StringBuffer().append("b").append(t.substring(1, 16)).toString();
                                break;
                            case '4':
                                sx = new StringBuffer().append(ExcelXmlConstants.CELL_TAG).append(t.substring(1, 16)).toString();
                                break;
                            case '5':
                                sx = new StringBuffer().append(DateTokenConverter.CONVERTER_KEY).append(t.substring(1, 16)).toString();
                                break;
                            case '6':
                                sx = new StringBuffer().append("e").append(t.substring(1, 16)).toString();
                                break;
                            case '7':
                                sx = new StringBuffer().append(ExcelXmlConstants.CELL_FORMULA_TAG).append(t.substring(1, 16)).toString();
                                break;
                        }
                }
            } else {
                sx = Long.toString(x, 16);
            }
            return printXFormat(sx);
        }

        private String printXFormat(int x) {
            String sx = null;
            if (x == Integer.MIN_VALUE) {
                sx = "80000000";
            } else if (x < 0) {
                String t = Integer.toString((((-x) - 1) ^ (-1)) ^ Integer.MIN_VALUE, 16);
                switch (t.length()) {
                    case 1:
                        sx = new StringBuffer().append("8000000").append(t).toString();
                        break;
                    case 2:
                        sx = new StringBuffer().append("800000").append(t).toString();
                        break;
                    case 3:
                        sx = new StringBuffer().append("80000").append(t).toString();
                        break;
                    case 4:
                        sx = new StringBuffer().append("8000").append(t).toString();
                        break;
                    case 5:
                        sx = new StringBuffer().append("800").append(t).toString();
                        break;
                    case 6:
                        sx = new StringBuffer().append("80").append(t).toString();
                        break;
                    case 7:
                        sx = new StringBuffer().append("8").append(t).toString();
                        break;
                    case 8:
                        switch (t.charAt(0)) {
                            case '1':
                                sx = new StringBuffer().append("9").append(t.substring(1, 8)).toString();
                                break;
                            case '2':
                                sx = new StringBuffer().append("a").append(t.substring(1, 8)).toString();
                                break;
                            case '3':
                                sx = new StringBuffer().append("b").append(t.substring(1, 8)).toString();
                                break;
                            case '4':
                                sx = new StringBuffer().append(ExcelXmlConstants.CELL_TAG).append(t.substring(1, 8)).toString();
                                break;
                            case '5':
                                sx = new StringBuffer().append(DateTokenConverter.CONVERTER_KEY).append(t.substring(1, 8)).toString();
                                break;
                            case '6':
                                sx = new StringBuffer().append("e").append(t.substring(1, 8)).toString();
                                break;
                            case '7':
                                sx = new StringBuffer().append(ExcelXmlConstants.CELL_FORMULA_TAG).append(t.substring(1, 8)).toString();
                                break;
                        }
                }
            } else {
                sx = Integer.toString(x, 16);
            }
            return printXFormat(sx);
        }

        private String printXFormat(String sx) {
            int nLeadingZeros = 0;
            int nBlanks = 0;
            if (sx.equals("0") && this.precisionSet && this.precision == 0) {
                sx = "";
            }
            if (this.precisionSet) {
                nLeadingZeros = this.precision - sx.length();
            }
            if (nLeadingZeros < 0) {
                nLeadingZeros = 0;
            }
            if (this.fieldWidthSet) {
                nBlanks = (this.fieldWidth - nLeadingZeros) - sx.length();
                if (this.alternateForm) {
                    nBlanks -= 2;
                }
            }
            if (nBlanks < 0) {
                nBlanks = 0;
            }
            int n = 0;
            if (this.alternateForm) {
                n = 0 + 2;
            }
            char[] ca = new char[n + nLeadingZeros + sx.length() + nBlanks];
            int i = 0;
            if (this.leftJustify) {
                if (this.alternateForm) {
                    int i2 = 0 + 1;
                    ca[0] = '0';
                    i = i2 + 1;
                    ca[i2] = 'x';
                }
                int j = 0;
                while (j < nLeadingZeros) {
                    ca[i] = '0';
                    j++;
                    i++;
                }
                char[] csx = sx.toCharArray();
                int j2 = 0;
                while (j2 < csx.length) {
                    ca[i] = csx[j2];
                    j2++;
                    i++;
                }
                int j3 = 0;
                while (j3 < nBlanks) {
                    ca[i] = ' ';
                    j3++;
                    i++;
                }
            } else {
                if (!this.leadingZeros) {
                    int j4 = 0;
                    while (j4 < nBlanks) {
                        ca[i] = ' ';
                        j4++;
                        i++;
                    }
                }
                if (this.alternateForm) {
                    int i3 = i;
                    int i4 = i + 1;
                    ca[i3] = '0';
                    i = i4 + 1;
                    ca[i4] = 'x';
                }
                if (this.leadingZeros) {
                    int j5 = 0;
                    while (j5 < nBlanks) {
                        ca[i] = '0';
                        j5++;
                        i++;
                    }
                }
                int j6 = 0;
                while (j6 < nLeadingZeros) {
                    ca[i] = '0';
                    j6++;
                    i++;
                }
                char[] csx2 = sx.toCharArray();
                int j7 = 0;
                while (j7 < csx2.length) {
                    ca[i] = csx2[j7];
                    j7++;
                    i++;
                }
            }
            String caReturn = new String(ca);
            if (this.conversionCharacter == 'X') {
                caReturn = caReturn.toUpperCase();
            }
            return caReturn;
        }

        private String printOFormat(short x) {
            String sx = null;
            if (x == Short.MIN_VALUE) {
                sx = "100000";
            } else if (x < 0) {
                String t = Integer.toString((((-x) - 1) ^ (-1)) ^ (-32768), 8);
                switch (t.length()) {
                    case 1:
                        sx = new StringBuffer().append("10000").append(t).toString();
                        break;
                    case 2:
                        sx = new StringBuffer().append("1000").append(t).toString();
                        break;
                    case 3:
                        sx = new StringBuffer().append("100").append(t).toString();
                        break;
                    case 4:
                        sx = new StringBuffer().append("10").append(t).toString();
                        break;
                    case 5:
                        sx = new StringBuffer().append("1").append(t).toString();
                        break;
                }
            } else {
                sx = Integer.toString(x, 8);
            }
            return printOFormat(sx);
        }

        private String printOFormat(long x) {
            String sx = null;
            if (x == Long.MIN_VALUE) {
                sx = "1000000000000000000000";
            } else if (x < 0) {
                String t = Long.toString((((-x) - 1) ^ (-1)) ^ Long.MIN_VALUE, 8);
                switch (t.length()) {
                    case 1:
                        sx = new StringBuffer().append("100000000000000000000").append(t).toString();
                        break;
                    case 2:
                        sx = new StringBuffer().append("10000000000000000000").append(t).toString();
                        break;
                    case 3:
                        sx = new StringBuffer().append("1000000000000000000").append(t).toString();
                        break;
                    case 4:
                        sx = new StringBuffer().append("100000000000000000").append(t).toString();
                        break;
                    case 5:
                        sx = new StringBuffer().append("10000000000000000").append(t).toString();
                        break;
                    case 6:
                        sx = new StringBuffer().append("1000000000000000").append(t).toString();
                        break;
                    case 7:
                        sx = new StringBuffer().append("100000000000000").append(t).toString();
                        break;
                    case 8:
                        sx = new StringBuffer().append("10000000000000").append(t).toString();
                        break;
                    case 9:
                        sx = new StringBuffer().append("1000000000000").append(t).toString();
                        break;
                    case 10:
                        sx = new StringBuffer().append("100000000000").append(t).toString();
                        break;
                    case 11:
                        sx = new StringBuffer().append("10000000000").append(t).toString();
                        break;
                    case 12:
                        sx = new StringBuffer().append("1000000000").append(t).toString();
                        break;
                    case 13:
                        sx = new StringBuffer().append("100000000").append(t).toString();
                        break;
                    case 14:
                        sx = new StringBuffer().append("10000000").append(t).toString();
                        break;
                    case 15:
                        sx = new StringBuffer().append("1000000").append(t).toString();
                        break;
                    case 16:
                        sx = new StringBuffer().append("100000").append(t).toString();
                        break;
                    case 17:
                        sx = new StringBuffer().append("10000").append(t).toString();
                        break;
                    case 18:
                        sx = new StringBuffer().append("1000").append(t).toString();
                        break;
                    case 19:
                        sx = new StringBuffer().append("100").append(t).toString();
                        break;
                    case 20:
                        sx = new StringBuffer().append("10").append(t).toString();
                        break;
                    case 21:
                        sx = new StringBuffer().append("1").append(t).toString();
                        break;
                }
            } else {
                sx = Long.toString(x, 8);
            }
            return printOFormat(sx);
        }

        private String printOFormat(int x) {
            String sx = null;
            if (x == Integer.MIN_VALUE) {
                sx = "20000000000";
            } else if (x < 0) {
                String t = Integer.toString((((-x) - 1) ^ (-1)) ^ Integer.MIN_VALUE, 8);
                switch (t.length()) {
                    case 1:
                        sx = new StringBuffer().append("2000000000").append(t).toString();
                        break;
                    case 2:
                        sx = new StringBuffer().append("200000000").append(t).toString();
                        break;
                    case 3:
                        sx = new StringBuffer().append("20000000").append(t).toString();
                        break;
                    case 4:
                        sx = new StringBuffer().append("2000000").append(t).toString();
                        break;
                    case 5:
                        sx = new StringBuffer().append("200000").append(t).toString();
                        break;
                    case 6:
                        sx = new StringBuffer().append(SQLError.SQL_STATE_CASE_NOT_FOUND_FOR_CASE_STATEMENT).append(t).toString();
                        break;
                    case 7:
                        sx = new StringBuffer().append("2000").append(t).toString();
                        break;
                    case 8:
                        sx = new StringBuffer().append("200").append(t).toString();
                        break;
                    case 9:
                        sx = new StringBuffer().append("20").append(t).toString();
                        break;
                    case 10:
                        sx = new StringBuffer().append("2").append(t).toString();
                        break;
                    case 11:
                        sx = new StringBuffer().append("3").append(t.substring(1)).toString();
                        break;
                }
            } else {
                sx = Integer.toString(x, 8);
            }
            return printOFormat(sx);
        }

        private String printOFormat(String sx) {
            int i;
            int nLeadingZeros = 0;
            int nBlanks = 0;
            if (sx.equals("0") && this.precisionSet && this.precision == 0) {
                sx = "";
            }
            if (this.precisionSet) {
                nLeadingZeros = this.precision - sx.length();
            }
            if (this.alternateForm) {
                nLeadingZeros++;
            }
            if (nLeadingZeros < 0) {
                nLeadingZeros = 0;
            }
            if (this.fieldWidthSet) {
                nBlanks = (this.fieldWidth - nLeadingZeros) - sx.length();
            }
            if (nBlanks < 0) {
                nBlanks = 0;
            }
            int n = nLeadingZeros + sx.length() + nBlanks;
            char[] ca = new char[n];
            if (this.leftJustify) {
                int i2 = 0;
                while (i2 < nLeadingZeros) {
                    ca[i2] = '0';
                    i2++;
                }
                char[] csx = sx.toCharArray();
                int j = 0;
                while (j < csx.length) {
                    ca[i2] = csx[j];
                    j++;
                    i2++;
                }
                int j2 = 0;
                while (j2 < nBlanks) {
                    ca[i2] = ' ';
                    j2++;
                    i2++;
                }
            } else {
                if (this.leadingZeros) {
                    i = 0;
                    while (i < nBlanks) {
                        ca[i] = '0';
                        i++;
                    }
                } else {
                    i = 0;
                    while (i < nBlanks) {
                        ca[i] = ' ';
                        i++;
                    }
                }
                int j3 = 0;
                while (j3 < nLeadingZeros) {
                    ca[i] = '0';
                    j3++;
                    i++;
                }
                char[] csx2 = sx.toCharArray();
                int j4 = 0;
                while (j4 < csx2.length) {
                    ca[i] = csx2[j4];
                    j4++;
                    i++;
                }
            }
            return new String(ca);
        }

        private String printCFormat(char x) {
            int width = this.fieldWidth;
            if (!this.fieldWidthSet) {
                width = 1;
            }
            char[] ca = new char[width];
            if (this.leftJustify) {
                ca[0] = x;
                for (int i = 1; i <= width - 1; i++) {
                    ca[i] = ' ';
                }
            } else {
                int i2 = 0;
                while (i2 < width - 1) {
                    ca[i2] = ' ';
                    i2++;
                }
                ca[i2] = x;
            }
            return new String(ca);
        }

        private String printSFormat(String x) {
            int i;
            int nPrint = x.length();
            int width = this.fieldWidth;
            if (this.precisionSet && nPrint > this.precision) {
                nPrint = this.precision;
            }
            if (!this.fieldWidthSet) {
                width = nPrint;
            }
            int n = width > nPrint ? 0 + (width - nPrint) : 0;
            char[] ca = new char[nPrint >= x.length() ? n + x.length() : n + nPrint];
            if (this.leftJustify) {
                if (nPrint >= x.length()) {
                    char[] csx = x.toCharArray();
                    i = 0;
                    while (i < x.length()) {
                        ca[i] = csx[i];
                        i++;
                    }
                } else {
                    char[] csx2 = x.substring(0, nPrint).toCharArray();
                    i = 0;
                    while (i < nPrint) {
                        ca[i] = csx2[i];
                        i++;
                    }
                }
                int j = 0;
                while (j < width - nPrint) {
                    ca[i] = ' ';
                    j++;
                    i++;
                }
            } else {
                int i2 = 0;
                while (i2 < width - nPrint) {
                    ca[i2] = ' ';
                    i2++;
                }
                if (nPrint >= x.length()) {
                    char[] csx3 = x.toCharArray();
                    for (int j2 = 0; j2 < x.length(); j2++) {
                        ca[i2] = csx3[j2];
                        i2++;
                    }
                } else {
                    char[] csx4 = x.substring(0, nPrint).toCharArray();
                    for (int j3 = 0; j3 < nPrint; j3++) {
                        ca[i2] = csx4[j3];
                        i2++;
                    }
                }
            }
            return new String(ca);
        }

        private boolean setConversionCharacter() {
            char c;
            boolean ret = false;
            this.conversionCharacter = (char) 0;
            if (this.pos < this.fmt.length() && ((c = this.fmt.charAt(this.pos)) == 'i' || c == 'd' || c == 'f' || c == 'g' || c == 'G' || c == 'o' || c == 'x' || c == 'X' || c == 'e' || c == 'E' || c == 'c' || c == 's' || c == '%')) {
                this.conversionCharacter = c;
                this.pos++;
                ret = true;
            }
            return ret;
        }

        private void setOptionalHL() {
            this.optionalh = false;
            this.optionall = false;
            this.optionalL = false;
            if (this.pos < this.fmt.length()) {
                char c = this.fmt.charAt(this.pos);
                if (c != 'h') {
                    if (c != 'l') {
                        if (c == 'L') {
                            this.optionalL = true;
                            this.pos++;
                            return;
                        }
                        return;
                    }
                    this.optionall = true;
                    this.pos++;
                    return;
                }
                this.optionalh = true;
                this.pos++;
            }
        }

        private void setPrecision() {
            int firstPos = this.pos;
            this.precisionSet = false;
            if (this.pos < this.fmt.length() && this.fmt.charAt(this.pos) == '.') {
                this.pos++;
                if (this.pos < this.fmt.length() && this.fmt.charAt(this.pos) == '*') {
                    this.pos++;
                    if (!setPrecisionArgPosition()) {
                        this.variablePrecision = true;
                        this.precisionSet = true;
                        return;
                    }
                    return;
                }
                while (this.pos < this.fmt.length()) {
                    char c = this.fmt.charAt(this.pos);
                    if (!Character.isDigit(c)) {
                        break;
                    } else {
                        this.pos++;
                    }
                }
                if (this.pos > firstPos + 1) {
                    String sz = this.fmt.substring(firstPos + 1, this.pos);
                    this.precision = Integer.parseInt(sz);
                    this.precisionSet = true;
                }
            }
        }

        private void setFieldWidth() {
            int firstPos = this.pos;
            this.fieldWidth = 0;
            this.fieldWidthSet = false;
            if (this.pos < this.fmt.length() && this.fmt.charAt(this.pos) == '*') {
                this.pos++;
                if (!setFieldWidthArgPosition()) {
                    this.variableFieldWidth = true;
                    this.fieldWidthSet = true;
                    return;
                }
                return;
            }
            while (this.pos < this.fmt.length()) {
                char c = this.fmt.charAt(this.pos);
                if (!Character.isDigit(c)) {
                    break;
                } else {
                    this.pos++;
                }
            }
            if (firstPos < this.pos && firstPos < this.fmt.length()) {
                String sz = this.fmt.substring(firstPos, this.pos);
                this.fieldWidth = Integer.parseInt(sz);
                this.fieldWidthSet = true;
            }
        }

        private void setArgPosition() {
            int xPos = this.pos;
            while (xPos < this.fmt.length() && Character.isDigit(this.fmt.charAt(xPos))) {
                xPos++;
            }
            if (xPos > this.pos && xPos < this.fmt.length() && this.fmt.charAt(xPos) == '$') {
                this.positionalSpecification = true;
                this.argumentPosition = Integer.parseInt(this.fmt.substring(this.pos, xPos));
                this.pos = xPos + 1;
            }
        }

        private boolean setFieldWidthArgPosition() {
            boolean ret = false;
            int xPos = this.pos;
            while (xPos < this.fmt.length() && Character.isDigit(this.fmt.charAt(xPos))) {
                xPos++;
            }
            if (xPos > this.pos && xPos < this.fmt.length() && this.fmt.charAt(xPos) == '$') {
                this.positionalFieldWidth = true;
                this.argumentPositionForFieldWidth = Integer.parseInt(this.fmt.substring(this.pos, xPos));
                this.pos = xPos + 1;
                ret = true;
            }
            return ret;
        }

        private boolean setPrecisionArgPosition() {
            boolean ret = false;
            int xPos = this.pos;
            while (xPos < this.fmt.length() && Character.isDigit(this.fmt.charAt(xPos))) {
                xPos++;
            }
            if (xPos > this.pos && xPos < this.fmt.length() && this.fmt.charAt(xPos) == '$') {
                this.positionalPrecision = true;
                this.argumentPositionForPrecision = Integer.parseInt(this.fmt.substring(this.pos, xPos));
                this.pos = xPos + 1;
                ret = true;
            }
            return ret;
        }

        boolean isPositionalSpecification() {
            return this.positionalSpecification;
        }

        int getArgumentPosition() {
            return this.argumentPosition;
        }

        boolean isPositionalFieldWidth() {
            return this.positionalFieldWidth;
        }

        int getArgumentPositionForFieldWidth() {
            return this.argumentPositionForFieldWidth;
        }

        boolean isPositionalPrecision() {
            return this.positionalPrecision;
        }

        int getArgumentPositionForPrecision() {
            return this.argumentPositionForPrecision;
        }

        private void setFlagCharacters() {
            this.thousands = false;
            this.leftJustify = false;
            this.leadingSign = false;
            this.leadingSpace = false;
            this.alternateForm = false;
            this.leadingZeros = false;
            while (this.pos < this.fmt.length()) {
                char c = this.fmt.charAt(this.pos);
                if (c == '\'') {
                    this.thousands = true;
                } else if (c == '-') {
                    this.leftJustify = true;
                    this.leadingZeros = false;
                } else if (c == '+') {
                    this.leadingSign = true;
                    this.leadingSpace = false;
                } else if (c == ' ') {
                    if (!this.leadingSign) {
                        this.leadingSpace = true;
                    }
                } else if (c == '#') {
                    this.alternateForm = true;
                } else if (c == '0') {
                    if (!this.leftJustify) {
                        this.leadingZeros = true;
                    }
                } else {
                    return;
                }
                this.pos++;
            }
        }
    }
}
