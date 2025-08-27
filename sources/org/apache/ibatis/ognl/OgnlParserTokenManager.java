package org.apache.ibatis.ognl;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.util.IEEEDouble;
import org.aspectj.apache.bcel.Constants;
import org.aspectj.bridge.ISourceLocation;
import org.springframework.aop.framework.autoproxy.target.QuickTargetSourceCreator;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/OgnlParserTokenManager.class */
public class OgnlParserTokenManager implements OgnlParserConstants {
    Object literalValue;
    private char charValue;
    private char charLiteralStartQuote;
    private StringBuffer stringBuffer;
    public PrintStream debugStream;
    static final long[] jjbitVec0 = {2301339413881290750L, -16384, 4294967295L, 432345564227567616L};
    static final long[] jjbitVec2 = {0, 0, 0, -36028797027352577L};
    static final long[] jjbitVec3 = {0, -1, -1, -1};
    static final long[] jjbitVec4 = {-1, -1, 65535, 0};
    static final long[] jjbitVec5 = {-1, -1, 0, 0};
    static final long[] jjbitVec6 = {70368744177663L, 0, 0, 0};
    static final long[] jjbitVec7 = {-2, -1, -1, -1};
    static final long[] jjbitVec8 = {0, 0, -1, -1};
    static final int[] jjnextStates = {15, 16, 18, 19, 22, 13, 24, 25, 7, 9, 10, 13, 17, 10, 13, 11, 12, 20, 21, 1, 2, 3};
    public static final String[] jjstrLiteralImages = {"", ",", SymbolConstants.EQUAL_SYMBOL, "?", ":", "||", "or", "&&", "and", "|", "bor", "^", "xor", "&", "band", "==", "eq", "!=", "neq", "<", "lt", ">", "gt", "<=", "lte", ">=", "gte", "in", "not", "<<", "shl", ">>", "shr", ">>>", "ushr", "+", "-", "*", "/", QuickTargetSourceCreator.PREFIX_THREAD_LOCAL, "~", "!", "instanceof", ".", "(", ")", "true", "false", "null", "#this", "#root", "#", org.springframework.beans.PropertyAccessor.PROPERTY_KEY_PREFIX, "]", "{", "}", "@", "new", PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
    public static final String[] lexStateNames = {"DEFAULT", "WithinCharLiteral", "WithinBackCharLiteral", "WithinStringLiteral"};
    public static final int[] jjnewLexState = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 2, 1, 3, -1, -1, 0, -1, -1, 0, -1, -1, 0, -1, -1, -1, -1, -1, -1};
    static final long[] jjtoToken = {576460752303423487L, 233993};
    static final long[] jjtoSkip = {-576460752303423488L, 0};
    static final long[] jjtoMore = {0, 28144};
    protected JavaCharStream input_stream;
    private final int[] jjrounds;
    private final int[] jjstateSet;
    private final StringBuffer image;
    private int jjimageLen;
    private int lengthOfMatch;
    protected char curChar;
    int curLexState;
    int defaultLexState;
    int jjnewStateCnt;
    int jjround;
    int jjmatchedPos;
    int jjmatchedKind;

    private char escapeChar() {
        int ofs = this.image.length() - 1;
        switch (this.image.charAt(ofs)) {
            case '\"':
                return '\"';
            case '\'':
                return '\'';
            case '\\':
                return '\\';
            case 'b':
                return '\b';
            case 'f':
                return '\f';
            case 'n':
                return '\n';
            case 'r':
                return '\r';
            case 't':
                return '\t';
        }
        do {
            ofs--;
        } while (this.image.charAt(ofs) != '\\');
        int iCharAt = 0;
        while (true) {
            int value = iCharAt;
            ofs++;
            if (ofs < this.image.length()) {
                iCharAt = (value << 3) | (this.image.charAt(ofs) - '0');
            } else {
                return (char) value;
            }
        }
    }

    private Object makeInt() throws NumberFormatException {
        Object result;
        String s = this.image.toString();
        int base = 10;
        if (s.charAt(0) == '0') {
            base = (s.length() <= 1 || !(s.charAt(1) == 'x' || s.charAt(1) == 'X')) ? 8 : 16;
        }
        if (base == 16) {
            s = s.substring(2);
        }
        switch (s.charAt(s.length() - 1)) {
            case 'H':
            case 'h':
                result = new BigInteger(s.substring(0, s.length() - 1), base);
                break;
            case 'L':
            case 'l':
                result = Long.valueOf(s.substring(0, s.length() - 1), base);
                break;
            default:
                result = Integer.valueOf(s, base);
                break;
        }
        return result;
    }

    private Object makeFloat() {
        String s = this.image.toString();
        switch (s.charAt(s.length() - 1)) {
            case 'B':
            case 'b':
                return new BigDecimal(s.substring(0, s.length() - 1));
            case 'D':
            case 'd':
            default:
                return Double.valueOf(s);
            case 'F':
            case 'f':
                return Float.valueOf(s);
        }
    }

    public void setDebugStream(PrintStream ds) {
        this.debugStream = ds;
    }

    private final int jjStopStringLiteralDfa_0(int pos, long active0, long active1) {
        switch (pos) {
            case 0:
                if ((active0 & 144612190372320576L) != 0) {
                    this.jjmatchedKind = 64;
                    break;
                } else if ((active0 & 288230376151711744L) == 0) {
                    if ((active0 & IEEEDouble.FRAC_ASSUMED_HIGH_BIT) == 0) {
                        if ((active0 & 8796093022208L) != 0) {
                        }
                    }
                }
                break;
            case 1:
                if ((active0 & 144607792102397184L) != 0) {
                    if (this.jjmatchedPos != 1) {
                        this.jjmatchedKind = 64;
                        this.jjmatchedPos = 1;
                        break;
                    }
                } else if ((active0 & 4398269923392L) != 0) {
                }
                break;
            case 2:
                if ((active0 & 496996435640320L) != 0) {
                    this.jjmatchedKind = 64;
                    this.jjmatchedPos = 2;
                    break;
                } else if ((active0 & 144115193797154048L) != 0) {
                }
                break;
            case 3:
                if ((active0 & 351860900773888L) == 0) {
                    if ((active0 & 145135534866432L) != 0) {
                        this.jjmatchedKind = 64;
                        this.jjmatchedPos = 3;
                        break;
                    }
                }
                break;
            case 4:
                if ((active0 & 140737488355328L) == 0) {
                    if ((active0 & 4398046511104L) != 0) {
                        this.jjmatchedKind = 64;
                        this.jjmatchedPos = 4;
                        break;
                    }
                }
                break;
            case 5:
                if ((active0 & 4398046511104L) != 0) {
                    this.jjmatchedKind = 64;
                    this.jjmatchedPos = 5;
                    break;
                }
                break;
            case 6:
                if ((active0 & 4398046511104L) != 0) {
                    this.jjmatchedKind = 64;
                    this.jjmatchedPos = 6;
                    break;
                }
                break;
            case 7:
                if ((active0 & 4398046511104L) != 0) {
                    this.jjmatchedKind = 64;
                    this.jjmatchedPos = 7;
                    break;
                }
                break;
            case 8:
                if ((active0 & 4398046511104L) != 0) {
                    this.jjmatchedKind = 64;
                    this.jjmatchedPos = 8;
                    break;
                }
                break;
        }
        return 1;
    }

    private final int jjStartNfa_0(int pos, long active0, long active1) {
        return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0, active1), pos + 1);
    }

    private int jjStopAtPos(int pos, int kind) {
        this.jjmatchedKind = kind;
        this.jjmatchedPos = pos;
        return pos + 1;
    }

    private int jjMoveStringLiteralDfa0_0() {
        switch (this.curChar) {
            case '!':
                this.jjmatchedKind = 41;
                return jjMoveStringLiteralDfa1_0(131072L);
            case '\"':
                return jjStopAtPos(0, 70);
            case '#':
                this.jjmatchedKind = 51;
                return jjMoveStringLiteralDfa1_0(1688849860263936L);
            case '$':
                return jjStartNfaWithStates_0(0, 58, 1);
            case '%':
                return jjStopAtPos(0, 39);
            case '&':
                this.jjmatchedKind = 13;
                return jjMoveStringLiteralDfa1_0(128L);
            case '\'':
                return jjStopAtPos(0, 69);
            case '(':
                return jjStopAtPos(0, 44);
            case ')':
                return jjStopAtPos(0, 45);
            case '*':
                return jjStopAtPos(0, 37);
            case '+':
                return jjStopAtPos(0, 35);
            case ',':
                return jjStopAtPos(0, 1);
            case '-':
                return jjStopAtPos(0, 36);
            case '.':
                return jjStartNfaWithStates_0(0, 43, 9);
            case '/':
                return jjStopAtPos(0, 38);
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case ';':
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
            case '\\':
            case '_':
            case 'c':
            case 'd':
            case 'h':
            case 'j':
            case 'k':
            case 'm':
            case 'p':
            case 'q':
            case 'r':
            case 'v':
            case 'w':
            case 'y':
            case 'z':
            default:
                return jjMoveNfa_0(0, 0);
            case ':':
                return jjStopAtPos(0, 4);
            case '<':
                this.jjmatchedKind = 19;
                return jjMoveStringLiteralDfa1_0(545259520L);
            case '=':
                this.jjmatchedKind = 2;
                return jjMoveStringLiteralDfa1_0(Constants.RET_INST);
            case '>':
                this.jjmatchedKind = 21;
                return jjMoveStringLiteralDfa1_0(10770972672L);
            case '?':
                return jjStopAtPos(0, 3);
            case '@':
                return jjStopAtPos(0, 56);
            case '[':
                return jjStartNfaWithStates_0(0, 52, 3);
            case ']':
                return jjStopAtPos(0, 53);
            case '^':
                return jjStopAtPos(0, 11);
            case '`':
                return jjStopAtPos(0, 68);
            case 'a':
                return jjMoveStringLiteralDfa1_0(256L);
            case 'b':
                return jjMoveStringLiteralDfa1_0(17408L);
            case 'e':
                return jjMoveStringLiteralDfa1_0(Constants.EXCEPTION_THROWER);
            case 'f':
                return jjMoveStringLiteralDfa1_0(140737488355328L);
            case 'g':
                return jjMoveStringLiteralDfa1_0(71303168L);
            case 'i':
                return jjMoveStringLiteralDfa1_0(4398180728832L);
            case 'l':
                return jjMoveStringLiteralDfa1_0(17825792L);
            case 'n':
                return jjMoveStringLiteralDfa1_0(144396663321264128L);
            case 'o':
                return jjMoveStringLiteralDfa1_0(64L);
            case 's':
                return jjMoveStringLiteralDfa1_0(5368709120L);
            case 't':
                return jjMoveStringLiteralDfa1_0(70368744177664L);
            case 'u':
                return jjMoveStringLiteralDfa1_0(17179869184L);
            case 'x':
                return jjMoveStringLiteralDfa1_0(Constants.NEGATABLE);
            case '{':
                return jjStopAtPos(0, 54);
            case '|':
                this.jjmatchedKind = 9;
                return jjMoveStringLiteralDfa1_0(32L);
            case '}':
                return jjStopAtPos(0, 55);
            case '~':
                return jjStopAtPos(0, 40);
        }
    }

    private int jjMoveStringLiteralDfa1_0(long active0) {
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case '&':
                    if ((active0 & 128) != 0) {
                        return jjStopAtPos(1, 7);
                    }
                    break;
                case '<':
                    if ((active0 & 536870912) != 0) {
                        return jjStopAtPos(1, 29);
                    }
                    break;
                case '=':
                    if ((active0 & Constants.RET_INST) != 0) {
                        return jjStopAtPos(1, 15);
                    }
                    if ((active0 & 131072) != 0) {
                        return jjStopAtPos(1, 17);
                    }
                    if ((active0 & 8388608) != 0) {
                        return jjStopAtPos(1, 23);
                    }
                    if ((active0 & 33554432) != 0) {
                        return jjStopAtPos(1, 25);
                    }
                    break;
                case '>':
                    if ((active0 & 2147483648L) != 0) {
                        this.jjmatchedKind = 31;
                        this.jjmatchedPos = 1;
                    }
                    return jjMoveStringLiteralDfa2_0(active0, 8589934592L);
                case 'a':
                    return jjMoveStringLiteralDfa2_0(active0, 140737488371712L);
                case 'e':
                    return jjMoveStringLiteralDfa2_0(active0, 144115188076118016L);
                case 'h':
                    return jjMoveStringLiteralDfa2_0(active0, 5368709120L);
                case 'n':
                    if ((active0 & 134217728) != 0) {
                        this.jjmatchedKind = 27;
                        this.jjmatchedPos = 1;
                    }
                    return jjMoveStringLiteralDfa2_0(active0, 4398046511360L);
                case 'o':
                    return jjMoveStringLiteralDfa2_0(active0, 268440576L);
                case 'q':
                    if ((active0 & Constants.EXCEPTION_THROWER) != 0) {
                        return jjStartNfaWithStates_0(1, 16, 1);
                    }
                    break;
                case 'r':
                    if ((active0 & 64) != 0) {
                        return jjStartNfaWithStates_0(1, 6, 1);
                    }
                    return jjMoveStringLiteralDfa2_0(active0, 1196268651020288L);
                case 's':
                    return jjMoveStringLiteralDfa2_0(active0, 17179869184L);
                case 't':
                    if ((active0 & 1048576) != 0) {
                        this.jjmatchedKind = 20;
                        this.jjmatchedPos = 1;
                    } else if ((active0 & 4194304) != 0) {
                        this.jjmatchedKind = 22;
                        this.jjmatchedPos = 1;
                    }
                    return jjMoveStringLiteralDfa2_0(active0, 562950037307392L);
                case 'u':
                    return jjMoveStringLiteralDfa2_0(active0, 281474976710656L);
                case '|':
                    if ((active0 & 32) != 0) {
                        return jjStopAtPos(1, 5);
                    }
                    break;
            }
            return jjStartNfa_0(0, active0, 0L);
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(0, active0, 0L);
            return 1;
        }
    }

    private int jjMoveStringLiteralDfa2_0(long old0, long active0) {
        long active02 = active0 & old0;
        if (active02 == 0) {
            return jjStartNfa_0(0, old0, 0L);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case '>':
                    if ((active02 & 8589934592L) != 0) {
                        return jjStopAtPos(2, 33);
                    }
                    break;
                case 'd':
                    if ((active02 & 256) != 0) {
                        return jjStartNfaWithStates_0(2, 8, 1);
                    }
                    break;
                case 'e':
                    if ((active02 & 16777216) != 0) {
                        return jjStartNfaWithStates_0(2, 24, 1);
                    }
                    if ((active02 & 67108864) != 0) {
                        return jjStartNfaWithStates_0(2, 26, 1);
                    }
                    break;
                case 'h':
                    return jjMoveStringLiteralDfa3_0(active02, 562967133290496L);
                case 'l':
                    if ((active02 & 1073741824) != 0) {
                        return jjStartNfaWithStates_0(2, 30, 1);
                    }
                    return jjMoveStringLiteralDfa3_0(active02, 422212465065984L);
                case 'n':
                    return jjMoveStringLiteralDfa3_0(active02, 16384L);
                case 'o':
                    return jjMoveStringLiteralDfa3_0(active02, FileUtils.ONE_PB);
                case 'q':
                    if ((active02 & 262144) != 0) {
                        return jjStartNfaWithStates_0(2, 18, 1);
                    }
                    break;
                case 'r':
                    if ((active02 & 1024) != 0) {
                        return jjStartNfaWithStates_0(2, 10, 1);
                    }
                    if ((active02 & Constants.NEGATABLE) != 0) {
                        return jjStartNfaWithStates_0(2, 12, 1);
                    }
                    if ((active02 & 4294967296L) != 0) {
                        return jjStartNfaWithStates_0(2, 32, 1);
                    }
                    break;
                case 's':
                    return jjMoveStringLiteralDfa3_0(active02, 4398046511104L);
                case 't':
                    if ((active02 & 268435456) != 0) {
                        return jjStartNfaWithStates_0(2, 28, 1);
                    }
                    break;
                case 'u':
                    return jjMoveStringLiteralDfa3_0(active02, 70368744177664L);
                case 'w':
                    if ((active02 & 144115188075855872L) != 0) {
                        return jjStartNfaWithStates_0(2, 57, 1);
                    }
                    break;
            }
            return jjStartNfa_0(1, active02, 0L);
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(1, active02, 0L);
            return 2;
        }
    }

    private int jjMoveStringLiteralDfa3_0(long old0, long active0) {
        long active02 = active0 & old0;
        if (active02 == 0) {
            return jjStartNfa_0(1, old0, 0L);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case 'd':
                    if ((active02 & 16384) != 0) {
                        return jjStartNfaWithStates_0(3, 14, 1);
                    }
                    break;
                case 'e':
                    if ((active02 & 70368744177664L) != 0) {
                        return jjStartNfaWithStates_0(3, 46, 1);
                    }
                    break;
                case 'i':
                    return jjMoveStringLiteralDfa4_0(active02, 562949953421312L);
                case 'l':
                    if ((active02 & 281474976710656L) != 0) {
                        return jjStartNfaWithStates_0(3, 48, 1);
                    }
                    break;
                case 'o':
                    return jjMoveStringLiteralDfa4_0(active02, FileUtils.ONE_PB);
                case 'r':
                    if ((active02 & 17179869184L) != 0) {
                        return jjStartNfaWithStates_0(3, 34, 1);
                    }
                    break;
                case 's':
                    return jjMoveStringLiteralDfa4_0(active02, 140737488355328L);
                case 't':
                    return jjMoveStringLiteralDfa4_0(active02, 4398046511104L);
            }
            return jjStartNfa_0(2, active02, 0L);
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(2, active02, 0L);
            return 3;
        }
    }

    private int jjMoveStringLiteralDfa4_0(long old0, long active0) {
        long active02 = active0 & old0;
        if (active02 == 0) {
            return jjStartNfa_0(2, old0, 0L);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case 'a':
                    return jjMoveStringLiteralDfa5_0(active02, 4398046511104L);
                case 'e':
                    if ((active02 & 140737488355328L) != 0) {
                        return jjStartNfaWithStates_0(4, 47, 1);
                    }
                    break;
                case 's':
                    if ((active02 & 562949953421312L) != 0) {
                        return jjStopAtPos(4, 49);
                    }
                    break;
                case 't':
                    if ((active02 & FileUtils.ONE_PB) != 0) {
                        return jjStopAtPos(4, 50);
                    }
                    break;
            }
            return jjStartNfa_0(3, active02, 0L);
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(3, active02, 0L);
            return 4;
        }
    }

    private int jjMoveStringLiteralDfa5_0(long old0, long active0) {
        long active02 = active0 & old0;
        if (active02 == 0) {
            return jjStartNfa_0(3, old0, 0L);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case 'n':
                    return jjMoveStringLiteralDfa6_0(active02, 4398046511104L);
                default:
                    return jjStartNfa_0(4, active02, 0L);
            }
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(4, active02, 0L);
            return 5;
        }
    }

    private int jjMoveStringLiteralDfa6_0(long old0, long active0) {
        long active02 = active0 & old0;
        if (active02 == 0) {
            return jjStartNfa_0(4, old0, 0L);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case 'c':
                    return jjMoveStringLiteralDfa7_0(active02, 4398046511104L);
                default:
                    return jjStartNfa_0(5, active02, 0L);
            }
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(5, active02, 0L);
            return 6;
        }
    }

    private int jjMoveStringLiteralDfa7_0(long old0, long active0) {
        long active02 = active0 & old0;
        if (active02 == 0) {
            return jjStartNfa_0(5, old0, 0L);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case 'e':
                    return jjMoveStringLiteralDfa8_0(active02, 4398046511104L);
                default:
                    return jjStartNfa_0(6, active02, 0L);
            }
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(6, active02, 0L);
            return 7;
        }
    }

    private int jjMoveStringLiteralDfa8_0(long old0, long active0) {
        long active02 = active0 & old0;
        if (active02 == 0) {
            return jjStartNfa_0(6, old0, 0L);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case 'o':
                    return jjMoveStringLiteralDfa9_0(active02, 4398046511104L);
                default:
                    return jjStartNfa_0(7, active02, 0L);
            }
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(7, active02, 0L);
            return 8;
        }
    }

    private int jjMoveStringLiteralDfa9_0(long old0, long active0) {
        long active02 = active0 & old0;
        if (active02 == 0) {
            return jjStartNfa_0(7, old0, 0L);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case 'f':
                    if ((active02 & 4398046511104L) != 0) {
                        return jjStartNfaWithStates_0(9, 42, 1);
                    }
                    break;
            }
            return jjStartNfa_0(8, active02, 0L);
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(8, active02, 0L);
            return 9;
        }
    }

    private int jjStartNfaWithStates_0(int pos, int kind, int state) {
        this.jjmatchedKind = kind;
        this.jjmatchedPos = pos;
        try {
            this.curChar = this.input_stream.readChar();
            return jjMoveNfa_0(state, pos + 1);
        } catch (IOException e) {
            return pos + 1;
        }
    }

    private int jjMoveNfa_0(int startState, int curPos) {
        int startsAt = 0;
        this.jjnewStateCnt = 27;
        int i = 1;
        this.jjstateSet[0] = startState;
        int kind = Integer.MAX_VALUE;
        while (true) {
            int i2 = this.jjround + 1;
            this.jjround = i2;
            if (i2 == Integer.MAX_VALUE) {
                ReInitRounds();
            }
            if (this.curChar < '@') {
                long l = 1 << this.curChar;
                do {
                    i--;
                    switch (this.jjstateSet[i]) {
                        case 0:
                            if ((287948901175001088L & l) != 0) {
                                jjCheckNAddStates(0, 5);
                            } else if (this.curChar == '.') {
                                jjCheckNAdd(9);
                            } else if (this.curChar == '$') {
                                if (kind > 64) {
                                    kind = 64;
                                }
                                jjCheckNAdd(1);
                            }
                            if ((287667426198290432L & l) != 0) {
                                if (kind > 80) {
                                    kind = 80;
                                }
                                jjCheckNAddTwoStates(6, 7);
                                break;
                            } else if (this.curChar == '0') {
                                if (kind > 80) {
                                    kind = 80;
                                }
                                jjCheckNAddStates(6, 8);
                                break;
                            }
                            break;
                        case 1:
                            if ((287948969894477824L & l) != 0) {
                                if (kind > 64) {
                                    kind = 64;
                                }
                                jjCheckNAdd(1);
                                break;
                            }
                            break;
                        case 3:
                            if ((4466765987840L & l) != 0) {
                                int[] iArr = this.jjstateSet;
                                int i3 = this.jjnewStateCnt;
                                this.jjnewStateCnt = i3 + 1;
                                iArr[i3] = 4;
                                break;
                            }
                            break;
                        case 5:
                            if ((287667426198290432L & l) != 0) {
                                if (kind > 80) {
                                    kind = 80;
                                }
                                jjCheckNAddTwoStates(6, 7);
                                break;
                            }
                            break;
                        case 6:
                            if ((287948901175001088L & l) != 0) {
                                if (kind > 80) {
                                    kind = 80;
                                }
                                jjCheckNAddTwoStates(6, 7);
                                break;
                            }
                            break;
                        case 8:
                            if (this.curChar == '.') {
                                jjCheckNAdd(9);
                                break;
                            }
                            break;
                        case 9:
                            if ((287948901175001088L & l) != 0) {
                                if (kind > 81) {
                                    kind = 81;
                                }
                                jjCheckNAddStates(9, 11);
                                break;
                            }
                            break;
                        case 11:
                            if ((43980465111040L & l) != 0) {
                                jjCheckNAdd(12);
                                break;
                            }
                            break;
                        case 12:
                            if ((287948901175001088L & l) != 0) {
                                if (kind > 81) {
                                    kind = 81;
                                }
                                jjCheckNAddTwoStates(12, 13);
                                break;
                            }
                            break;
                        case 14:
                            if ((287948901175001088L & l) != 0) {
                                jjCheckNAddStates(0, 5);
                                break;
                            }
                            break;
                        case 15:
                            if ((287948901175001088L & l) != 0) {
                                jjCheckNAddTwoStates(15, 16);
                                break;
                            }
                            break;
                        case 16:
                            if (this.curChar == '.') {
                                if (kind > 81) {
                                    kind = 81;
                                }
                                jjCheckNAddStates(12, 14);
                                break;
                            }
                            break;
                        case 17:
                            if ((287948901175001088L & l) != 0) {
                                if (kind > 81) {
                                    kind = 81;
                                }
                                jjCheckNAddStates(12, 14);
                                break;
                            }
                            break;
                        case 18:
                            if ((287948901175001088L & l) != 0) {
                                jjCheckNAddTwoStates(18, 19);
                                break;
                            }
                            break;
                        case 20:
                            if ((43980465111040L & l) != 0) {
                                jjCheckNAdd(21);
                                break;
                            }
                            break;
                        case 21:
                            if ((287948901175001088L & l) != 0) {
                                if (kind > 81) {
                                    kind = 81;
                                }
                                jjCheckNAddTwoStates(21, 13);
                                break;
                            }
                            break;
                        case 22:
                            if ((287948901175001088L & l) != 0) {
                                jjCheckNAddTwoStates(22, 13);
                                break;
                            }
                            break;
                        case 23:
                            if (this.curChar == '0') {
                                if (kind > 80) {
                                    kind = 80;
                                }
                                jjCheckNAddStates(6, 8);
                                break;
                            }
                            break;
                        case 24:
                            if ((71776119061217280L & l) != 0) {
                                if (kind > 80) {
                                    kind = 80;
                                }
                                jjCheckNAddTwoStates(24, 7);
                                break;
                            }
                            break;
                        case 26:
                            if ((287948901175001088L & l) != 0) {
                                if (kind > 80) {
                                    kind = 80;
                                }
                                jjCheckNAddTwoStates(26, 7);
                                break;
                            }
                            break;
                    }
                } while (i != startsAt);
            } else if (this.curChar < 128) {
                long l2 = 1 << (this.curChar & '?');
                do {
                    i--;
                    switch (this.jjstateSet[i]) {
                        case 0:
                            if ((576460745995190270L & l2) != 0) {
                                if (kind > 64) {
                                    kind = 64;
                                }
                                jjCheckNAdd(1);
                                break;
                            } else if (this.curChar == '[') {
                                int[] iArr2 = this.jjstateSet;
                                int i4 = this.jjnewStateCnt;
                                this.jjnewStateCnt = i4 + 1;
                                iArr2[i4] = 3;
                                break;
                            }
                            break;
                        case 1:
                            if ((576460745995190270L & l2) != 0) {
                                if (kind > 64) {
                                    kind = 64;
                                }
                                jjCheckNAdd(1);
                                break;
                            }
                            break;
                        case 2:
                            if (this.curChar == '[') {
                                int[] iArr3 = this.jjstateSet;
                                int i5 = this.jjnewStateCnt;
                                this.jjnewStateCnt = i5 + 1;
                                iArr3[i5] = 3;
                                break;
                            }
                            break;
                        case 3:
                            if ((1152921505680588800L & l2) != 0) {
                                int[] iArr4 = this.jjstateSet;
                                int i6 = this.jjnewStateCnt;
                                this.jjnewStateCnt = i6 + 1;
                                iArr4[i6] = 4;
                                break;
                            }
                            break;
                        case 4:
                            if (this.curChar == ']') {
                                kind = 67;
                                break;
                            }
                            break;
                        case 7:
                            if ((18691697676544L & l2) != 0 && kind > 80) {
                                kind = 80;
                                break;
                            }
                            break;
                        case 10:
                            if ((137438953504L & l2) != 0) {
                                jjAddStates(15, 16);
                                break;
                            }
                            break;
                        case 13:
                            if ((360777252948L & l2) != 0 && kind > 81) {
                                kind = 81;
                                break;
                            }
                            break;
                        case 19:
                            if ((137438953504L & l2) != 0) {
                                jjAddStates(17, 18);
                                break;
                            }
                            break;
                        case 25:
                            if ((72057594054705152L & l2) != 0) {
                                jjCheckNAdd(26);
                                break;
                            }
                            break;
                        case 26:
                            if ((541165879422L & l2) != 0) {
                                if (kind > 80) {
                                    kind = 80;
                                }
                                jjCheckNAddTwoStates(26, 7);
                                break;
                            }
                            break;
                    }
                } while (i != startsAt);
            } else {
                int hiByte = this.curChar >> '\b';
                int i1 = hiByte >> 6;
                long l1 = 1 << (hiByte & 63);
                int i22 = (this.curChar & 255) >> 6;
                long l22 = 1 << (this.curChar & '?');
                do {
                    i--;
                    switch (this.jjstateSet[i]) {
                        case 0:
                        case 1:
                            if (jjCanMove_0(hiByte, i1, i22, l1, l22)) {
                                if (kind > 64) {
                                    kind = 64;
                                }
                                jjCheckNAdd(1);
                                break;
                            }
                            break;
                    }
                } while (i != startsAt);
            }
            if (kind != Integer.MAX_VALUE) {
                this.jjmatchedKind = kind;
                this.jjmatchedPos = curPos;
                kind = Integer.MAX_VALUE;
            }
            curPos++;
            int i7 = this.jjnewStateCnt;
            i = i7;
            int i8 = startsAt;
            this.jjnewStateCnt = i8;
            int i9 = 27 - i8;
            startsAt = i9;
            if (i7 == i9) {
                return curPos;
            }
            try {
                this.curChar = this.input_stream.readChar();
            } catch (IOException e) {
                return curPos;
            }
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:3:0x0001. Please report as an issue. */
    private final int jjStopStringLiteralDfa_2(int pos, long active0, long active1) {
        switch (pos) {
        }
        return -1;
    }

    private final int jjStartNfa_2(int pos, long active0, long active1) {
        return jjMoveNfa_2(jjStopStringLiteralDfa_2(pos, active0, active1), pos + 1);
    }

    private int jjMoveStringLiteralDfa0_2() {
        switch (this.curChar) {
            case '`':
                return jjStopAtPos(0, 76);
            default:
                return jjMoveNfa_2(0, 0);
        }
    }

    private int jjMoveNfa_2(int startState, int curPos) {
        int startsAt = 0;
        this.jjnewStateCnt = 6;
        int i = 1;
        this.jjstateSet[0] = startState;
        int kind = Integer.MAX_VALUE;
        while (true) {
            int i2 = this.jjround + 1;
            this.jjround = i2;
            if (i2 == Integer.MAX_VALUE) {
                ReInitRounds();
            }
            if (this.curChar < '@') {
                long l = 1 << this.curChar;
                do {
                    i--;
                    switch (this.jjstateSet[i]) {
                        case 0:
                            if (kind > 75) {
                                kind = 75;
                                break;
                            }
                            break;
                        case 1:
                            if ((566935683072L & l) != 0 && kind > 74) {
                                kind = 74;
                                break;
                            }
                            break;
                        case 2:
                            if ((4222124650659840L & l) != 0) {
                                int[] iArr = this.jjstateSet;
                                int i3 = this.jjnewStateCnt;
                                this.jjnewStateCnt = i3 + 1;
                                iArr[i3] = 3;
                                break;
                            }
                            break;
                        case 3:
                            if ((71776119061217280L & l) != 0) {
                                if (kind > 74) {
                                    kind = 74;
                                }
                                int[] iArr2 = this.jjstateSet;
                                int i4 = this.jjnewStateCnt;
                                this.jjnewStateCnt = i4 + 1;
                                iArr2[i4] = 4;
                                break;
                            }
                            break;
                        case 4:
                            if ((71776119061217280L & l) != 0 && kind > 74) {
                                kind = 74;
                                break;
                            }
                            break;
                    }
                } while (i != startsAt);
            } else if (this.curChar < 128) {
                long l2 = 1 << (this.curChar & '?');
                do {
                    i--;
                    switch (this.jjstateSet[i]) {
                        case 0:
                            if (((-4563402753L) & l2) != 0) {
                                if (kind > 75) {
                                    kind = 75;
                                    break;
                                }
                            } else if (this.curChar == '\\') {
                                jjAddStates(19, 21);
                                break;
                            }
                            break;
                        case 1:
                            if ((5700164899569664L & l2) != 0 && kind > 74) {
                                kind = 74;
                                break;
                            }
                            break;
                        case 5:
                            if (((-4563402753L) & l2) != 0 && kind > 75) {
                                kind = 75;
                                break;
                            }
                            break;
                    }
                } while (i != startsAt);
            } else {
                int hiByte = this.curChar >> '\b';
                int i1 = hiByte >> 6;
                long l1 = 1 << (hiByte & 63);
                int i22 = (this.curChar & 255) >> 6;
                long l22 = 1 << (this.curChar & '?');
                do {
                    i--;
                    switch (this.jjstateSet[i]) {
                        case 0:
                            if (jjCanMove_1(hiByte, i1, i22, l1, l22) && kind > 75) {
                                kind = 75;
                                break;
                            }
                            break;
                    }
                } while (i != startsAt);
            }
            if (kind != Integer.MAX_VALUE) {
                this.jjmatchedKind = kind;
                this.jjmatchedPos = curPos;
                kind = Integer.MAX_VALUE;
            }
            curPos++;
            int i5 = this.jjnewStateCnt;
            i = i5;
            int i6 = startsAt;
            this.jjnewStateCnt = i6;
            int i7 = 6 - i6;
            startsAt = i7;
            if (i5 == i7) {
                return curPos;
            }
            try {
                this.curChar = this.input_stream.readChar();
            } catch (IOException e) {
                return curPos;
            }
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:3:0x0001. Please report as an issue. */
    private final int jjStopStringLiteralDfa_1(int pos, long active0, long active1) {
        switch (pos) {
        }
        return -1;
    }

    private final int jjStartNfa_1(int pos, long active0, long active1) {
        return jjMoveNfa_1(jjStopStringLiteralDfa_1(pos, active0, active1), pos + 1);
    }

    private int jjMoveStringLiteralDfa0_1() {
        switch (this.curChar) {
            case '\'':
                return jjStopAtPos(0, 73);
            default:
                return jjMoveNfa_1(0, 0);
        }
    }

    private int jjMoveNfa_1(int startState, int curPos) {
        int startsAt = 0;
        this.jjnewStateCnt = 6;
        int i = 1;
        this.jjstateSet[0] = startState;
        int kind = Integer.MAX_VALUE;
        while (true) {
            int i2 = this.jjround + 1;
            this.jjround = i2;
            if (i2 == Integer.MAX_VALUE) {
                ReInitRounds();
            }
            if (this.curChar < '@') {
                long l = 1 << this.curChar;
                do {
                    i--;
                    switch (this.jjstateSet[i]) {
                        case 0:
                            if (((-549755813889L) & l) != 0 && kind > 72) {
                                kind = 72;
                                break;
                            }
                            break;
                        case 1:
                            if ((566935683072L & l) != 0 && kind > 71) {
                                kind = 71;
                                break;
                            }
                            break;
                        case 2:
                            if ((4222124650659840L & l) != 0) {
                                int[] iArr = this.jjstateSet;
                                int i3 = this.jjnewStateCnt;
                                this.jjnewStateCnt = i3 + 1;
                                iArr[i3] = 3;
                                break;
                            }
                            break;
                        case 3:
                            if ((71776119061217280L & l) != 0) {
                                if (kind > 71) {
                                    kind = 71;
                                }
                                int[] iArr2 = this.jjstateSet;
                                int i4 = this.jjnewStateCnt;
                                this.jjnewStateCnt = i4 + 1;
                                iArr2[i4] = 4;
                                break;
                            }
                            break;
                        case 4:
                            if ((71776119061217280L & l) != 0 && kind > 71) {
                                kind = 71;
                                break;
                            }
                            break;
                    }
                } while (i != startsAt);
            } else if (this.curChar < 128) {
                long l2 = 1 << (this.curChar & '?');
                do {
                    i--;
                    switch (this.jjstateSet[i]) {
                        case 0:
                            if (((-268435457) & l2) != 0) {
                                if (kind > 72) {
                                    kind = 72;
                                    break;
                                }
                            } else if (this.curChar == '\\') {
                                jjAddStates(19, 21);
                                break;
                            }
                            break;
                        case 1:
                            if ((5700164899569664L & l2) != 0 && kind > 71) {
                                kind = 71;
                                break;
                            }
                            break;
                        case 5:
                            if (((-268435457) & l2) != 0 && kind > 72) {
                                kind = 72;
                                break;
                            }
                            break;
                    }
                } while (i != startsAt);
            } else {
                int hiByte = this.curChar >> '\b';
                int i1 = hiByte >> 6;
                long l1 = 1 << (hiByte & 63);
                int i22 = (this.curChar & 255) >> 6;
                long l22 = 1 << (this.curChar & '?');
                do {
                    i--;
                    switch (this.jjstateSet[i]) {
                        case 0:
                            if (jjCanMove_1(hiByte, i1, i22, l1, l22) && kind > 72) {
                                kind = 72;
                                break;
                            }
                            break;
                    }
                } while (i != startsAt);
            }
            if (kind != Integer.MAX_VALUE) {
                this.jjmatchedKind = kind;
                this.jjmatchedPos = curPos;
                kind = Integer.MAX_VALUE;
            }
            curPos++;
            int i5 = this.jjnewStateCnt;
            i = i5;
            int i6 = startsAt;
            this.jjnewStateCnt = i6;
            int i7 = 6 - i6;
            startsAt = i7;
            if (i5 == i7) {
                return curPos;
            }
            try {
                this.curChar = this.input_stream.readChar();
            } catch (IOException e) {
                return curPos;
            }
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:3:0x0001. Please report as an issue. */
    private final int jjStopStringLiteralDfa_3(int pos, long active0, long active1) {
        switch (pos) {
        }
        return -1;
    }

    private final int jjStartNfa_3(int pos, long active0, long active1) {
        return jjMoveNfa_3(jjStopStringLiteralDfa_3(pos, active0, active1), pos + 1);
    }

    private int jjMoveStringLiteralDfa0_3() {
        switch (this.curChar) {
            case '\"':
                return jjStopAtPos(0, 79);
            default:
                return jjMoveNfa_3(0, 0);
        }
    }

    private int jjMoveNfa_3(int startState, int curPos) {
        int startsAt = 0;
        this.jjnewStateCnt = 6;
        int i = 1;
        this.jjstateSet[0] = startState;
        int kind = Integer.MAX_VALUE;
        while (true) {
            int i2 = this.jjround + 1;
            this.jjround = i2;
            if (i2 == Integer.MAX_VALUE) {
                ReInitRounds();
            }
            if (this.curChar < '@') {
                long l = 1 << this.curChar;
                do {
                    i--;
                    switch (this.jjstateSet[i]) {
                        case 0:
                            if (((-17179869185L) & l) != 0 && kind > 78) {
                                kind = 78;
                                break;
                            }
                            break;
                        case 1:
                            if ((566935683072L & l) != 0 && kind > 77) {
                                kind = 77;
                                break;
                            }
                            break;
                        case 2:
                            if ((4222124650659840L & l) != 0) {
                                int[] iArr = this.jjstateSet;
                                int i3 = this.jjnewStateCnt;
                                this.jjnewStateCnt = i3 + 1;
                                iArr[i3] = 3;
                                break;
                            }
                            break;
                        case 3:
                            if ((71776119061217280L & l) != 0) {
                                if (kind > 77) {
                                    kind = 77;
                                }
                                int[] iArr2 = this.jjstateSet;
                                int i4 = this.jjnewStateCnt;
                                this.jjnewStateCnt = i4 + 1;
                                iArr2[i4] = 4;
                                break;
                            }
                            break;
                        case 4:
                            if ((71776119061217280L & l) != 0 && kind > 77) {
                                kind = 77;
                                break;
                            }
                            break;
                    }
                } while (i != startsAt);
            } else if (this.curChar < 128) {
                long l2 = 1 << (this.curChar & '?');
                do {
                    i--;
                    switch (this.jjstateSet[i]) {
                        case 0:
                            if (((-268435457) & l2) != 0) {
                                if (kind > 78) {
                                    kind = 78;
                                    break;
                                }
                            } else if (this.curChar == '\\') {
                                jjAddStates(19, 21);
                                break;
                            }
                            break;
                        case 1:
                            if ((5700164899569664L & l2) != 0 && kind > 77) {
                                kind = 77;
                                break;
                            }
                            break;
                        case 5:
                            if (((-268435457) & l2) != 0 && kind > 78) {
                                kind = 78;
                                break;
                            }
                            break;
                    }
                } while (i != startsAt);
            } else {
                int hiByte = this.curChar >> '\b';
                int i1 = hiByte >> 6;
                long l1 = 1 << (hiByte & 63);
                int i22 = (this.curChar & 255) >> 6;
                long l22 = 1 << (this.curChar & '?');
                do {
                    i--;
                    switch (this.jjstateSet[i]) {
                        case 0:
                            if (jjCanMove_1(hiByte, i1, i22, l1, l22) && kind > 78) {
                                kind = 78;
                                break;
                            }
                            break;
                    }
                } while (i != startsAt);
            }
            if (kind != Integer.MAX_VALUE) {
                this.jjmatchedKind = kind;
                this.jjmatchedPos = curPos;
                kind = Integer.MAX_VALUE;
            }
            curPos++;
            int i5 = this.jjnewStateCnt;
            i = i5;
            int i6 = startsAt;
            this.jjnewStateCnt = i6;
            int i7 = 6 - i6;
            startsAt = i7;
            if (i5 == i7) {
                return curPos;
            }
            try {
                this.curChar = this.input_stream.readChar();
            } catch (IOException e) {
                return curPos;
            }
        }
    }

    private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2) {
        switch (hiByte) {
            case 0:
                return (jjbitVec2[i2] & l2) != 0;
            case 48:
                return (jjbitVec3[i2] & l2) != 0;
            case 49:
                return (jjbitVec4[i2] & l2) != 0;
            case 51:
                return (jjbitVec5[i2] & l2) != 0;
            case 61:
                return (jjbitVec6[i2] & l2) != 0;
            default:
                if ((jjbitVec0[i1] & l1) != 0) {
                    return true;
                }
                return false;
        }
    }

    private static final boolean jjCanMove_1(int hiByte, int i1, int i2, long l1, long l2) {
        switch (hiByte) {
            case 0:
                return (jjbitVec8[i2] & l2) != 0;
            default:
                if ((jjbitVec7[i1] & l1) != 0) {
                    return true;
                }
                return false;
        }
    }

    public OgnlParserTokenManager(JavaCharStream stream) {
        this.debugStream = System.out;
        this.jjrounds = new int[27];
        this.jjstateSet = new int[54];
        this.image = new StringBuffer();
        this.curLexState = 0;
        this.defaultLexState = 0;
        this.input_stream = stream;
    }

    public OgnlParserTokenManager(JavaCharStream stream, int lexState) {
        this(stream);
        SwitchTo(lexState);
    }

    public void ReInit(JavaCharStream stream) {
        this.jjnewStateCnt = 0;
        this.jjmatchedPos = 0;
        this.curLexState = this.defaultLexState;
        this.input_stream = stream;
        ReInitRounds();
    }

    private void ReInitRounds() {
        this.jjround = ISourceLocation.NO_COLUMN;
        int i = 27;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                this.jjrounds[i] = Integer.MIN_VALUE;
            } else {
                return;
            }
        }
    }

    public void ReInit(JavaCharStream stream, int lexState) {
        ReInit(stream);
        SwitchTo(lexState);
    }

    public void SwitchTo(int lexState) {
        if (lexState >= 4 || lexState < 0) {
            throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", 2);
        }
        this.curLexState = lexState;
    }

    protected Token jjFillToken() {
        String im = jjstrLiteralImages[this.jjmatchedKind];
        String tokenImage = im == null ? this.input_stream.GetImage() : im;
        int beginLine = this.input_stream.getBeginLine();
        int beginColumn = this.input_stream.getBeginColumn();
        int endLine = this.input_stream.getEndLine();
        int endColumn = this.input_stream.getEndColumn();
        Token t = Token.newToken(this.jjmatchedKind, tokenImage);
        t.beginLine = beginLine;
        t.endLine = endLine;
        t.beginColumn = beginColumn;
        t.endColumn = endColumn;
        return t;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00d8  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0198 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public org.apache.ibatis.ognl.Token getNextToken() {
        /*
            Method dump skipped, instructions count: 562
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.ibatis.ognl.OgnlParserTokenManager.getNextToken():org.apache.ibatis.ognl.Token");
    }

    void MoreLexicalActions() {
        int i = this.jjimageLen;
        int i2 = this.jjmatchedPos + 1;
        this.lengthOfMatch = i2;
        this.jjimageLen = i + i2;
        switch (this.jjmatchedKind) {
            case 69:
                this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                this.jjimageLen = 0;
                this.stringBuffer = new StringBuffer();
                break;
            case 70:
                this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                this.jjimageLen = 0;
                this.stringBuffer = new StringBuffer();
                break;
            case 71:
                this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                this.jjimageLen = 0;
                this.charValue = escapeChar();
                this.stringBuffer.append(this.charValue);
                break;
            case 72:
                this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                this.jjimageLen = 0;
                this.charValue = this.image.charAt(this.image.length() - 1);
                this.stringBuffer.append(this.charValue);
                break;
            case 74:
                this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                this.jjimageLen = 0;
                this.charValue = escapeChar();
                break;
            case 75:
                this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                this.jjimageLen = 0;
                this.charValue = this.image.charAt(this.image.length() - 1);
                break;
            case 77:
                this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                this.jjimageLen = 0;
                this.stringBuffer.append(escapeChar());
                break;
            case 78:
                this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                this.jjimageLen = 0;
                this.stringBuffer.append(this.image.charAt(this.image.length() - 1));
                break;
        }
    }

    void TokenLexicalActions(Token matchedToken) {
        switch (this.jjmatchedKind) {
            case 67:
                StringBuffer stringBuffer = this.image;
                JavaCharStream javaCharStream = this.input_stream;
                int i = this.jjimageLen;
                int i2 = this.jjmatchedPos + 1;
                this.lengthOfMatch = i2;
                stringBuffer.append(javaCharStream.GetSuffix(i + i2));
                switch (this.image.charAt(1)) {
                    case '$':
                        this.literalValue = DynamicSubscript.last;
                        break;
                    case '*':
                        this.literalValue = DynamicSubscript.all;
                        break;
                    case '^':
                        this.literalValue = DynamicSubscript.first;
                        break;
                    case '|':
                        this.literalValue = DynamicSubscript.mid;
                        break;
                }
            case 73:
                StringBuffer stringBuffer2 = this.image;
                JavaCharStream javaCharStream2 = this.input_stream;
                int i3 = this.jjimageLen;
                int i4 = this.jjmatchedPos + 1;
                this.lengthOfMatch = i4;
                stringBuffer2.append(javaCharStream2.GetSuffix(i3 + i4));
                if (this.stringBuffer.length() == 1) {
                    this.literalValue = new Character(this.charValue);
                    break;
                } else {
                    this.literalValue = new String(this.stringBuffer);
                    break;
                }
            case 76:
                StringBuffer stringBuffer3 = this.image;
                JavaCharStream javaCharStream3 = this.input_stream;
                int i5 = this.jjimageLen;
                int i6 = this.jjmatchedPos + 1;
                this.lengthOfMatch = i6;
                stringBuffer3.append(javaCharStream3.GetSuffix(i5 + i6));
                this.literalValue = new Character(this.charValue);
                break;
            case 79:
                StringBuffer stringBuffer4 = this.image;
                JavaCharStream javaCharStream4 = this.input_stream;
                int i7 = this.jjimageLen;
                int i8 = this.jjmatchedPos + 1;
                this.lengthOfMatch = i8;
                stringBuffer4.append(javaCharStream4.GetSuffix(i7 + i8));
                this.literalValue = new String(this.stringBuffer);
                break;
            case 80:
                StringBuffer stringBuffer5 = this.image;
                JavaCharStream javaCharStream5 = this.input_stream;
                int i9 = this.jjimageLen;
                int i10 = this.jjmatchedPos + 1;
                this.lengthOfMatch = i10;
                stringBuffer5.append(javaCharStream5.GetSuffix(i9 + i10));
                this.literalValue = makeInt();
                break;
            case 81:
                StringBuffer stringBuffer6 = this.image;
                JavaCharStream javaCharStream6 = this.input_stream;
                int i11 = this.jjimageLen;
                int i12 = this.jjmatchedPos + 1;
                this.lengthOfMatch = i12;
                stringBuffer6.append(javaCharStream6.GetSuffix(i11 + i12));
                this.literalValue = makeFloat();
                break;
        }
    }

    private void jjCheckNAdd(int state) {
        if (this.jjrounds[state] != this.jjround) {
            int[] iArr = this.jjstateSet;
            int i = this.jjnewStateCnt;
            this.jjnewStateCnt = i + 1;
            iArr[i] = state;
            this.jjrounds[state] = this.jjround;
        }
    }

    private void jjAddStates(int start, int end) {
        int i;
        do {
            int[] iArr = this.jjstateSet;
            int i2 = this.jjnewStateCnt;
            this.jjnewStateCnt = i2 + 1;
            iArr[i2] = jjnextStates[start];
            i = start;
            start++;
        } while (i != end);
    }

    private void jjCheckNAddTwoStates(int state1, int state2) {
        jjCheckNAdd(state1);
        jjCheckNAdd(state2);
    }

    private void jjCheckNAddStates(int start, int end) {
        int i;
        do {
            jjCheckNAdd(jjnextStates[start]);
            i = start;
            start++;
        } while (i != end);
    }
}
