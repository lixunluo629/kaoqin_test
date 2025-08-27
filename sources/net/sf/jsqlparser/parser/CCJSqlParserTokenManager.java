package net.sf.jsqlparser.parser;

import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
import com.google.common.primitives.Longs;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.util.IEEEDouble;
import org.aspectj.apache.bcel.Constants;
import org.aspectj.bridge.ISourceLocation;
import org.hyperic.sigar.NetFlags;
import org.springframework.aop.framework.autoproxy.target.QuickTargetSourceCreator;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/parser/CCJSqlParserTokenManager.class */
public class CCJSqlParserTokenManager implements CCJSqlParserConstants {
    public PrintStream debugStream;
    int curLexState;
    int defaultLexState;
    int jjnewStateCnt;
    int jjround;
    int jjmatchedPos;
    int jjmatchedKind;
    protected SimpleCharStream input_stream;
    private final int[] jjrounds;
    private final int[] jjstateSet;
    protected int curChar;
    static final long[] jjbitVec0 = {-2, -1, -1, -1};
    static final long[] jjbitVec2 = {0, 0, -1, -1};
    static final long[] jjbitVec3 = {0, 0, 0, 1170935974255919120L};
    static final int[] jjnextStates = {32, 0, 33, 34, 38, 39, 42, 10, 11, 13, 26, 27, 29, 30, 3, 4, 23, 24, 36, 37, 40, 41};
    public static final String[] jjstrLiteralImages = {"", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, ScriptUtils.DEFAULT_STATEMENT_SEPARATOR, ",", SymbolConstants.EQUAL_SYMBOL, "(", ")", "*", ".", "?", "(+)", ">", "<", ">=", "<=", "<>", "!=", "@@", "~", "~*", "!~", "!~*", "||", "|", "&", "+", "-", "/", QuickTargetSourceCreator.PREFIX_THREAD_LOCAL, "^", null, "}", null, null, NetFlags.ANY_ADDR_V6, ":", "->", null};
    public static final String[] lexStateNames = {"DEFAULT"};
    static final long[] jjtoToken = {-31, -175077435514028033L, 1073741823};
    static final long[] jjtoSkip = {30, 3377699720527872L, 0};
    static final long[] jjtoSpecial = {0, 3377699720527872L, 0};

    public void setDebugStream(PrintStream ds) {
        this.debugStream = ds;
    }

    private final int jjStopStringLiteralDfa_0(int pos, long active0, long active1, long active2) {
        switch (pos) {
            case 0:
                if ((active2 & 524288) != 0) {
                    return 8;
                }
                if ((active2 & 1) != 0) {
                    return 1;
                }
                if ((active2 & 268697600) != 0) {
                    return 5;
                }
                if ((active0 & (-32)) != 0 || (active1 & 140737488355327L) != 0) {
                    this.jjmatchedKind = 116;
                    return 43;
                }
                return -1;
            case 1:
                if ((active0 & (-4622382067551047680L)) == 0 && (active1 & 140737488223999L) == 0) {
                    if ((active0 & 4622382067551047648L) != 0 || (active1 & 131328) != 0) {
                        return 43;
                    }
                    return -1;
                }
                if (this.jjmatchedPos != 1) {
                    this.jjmatchedKind = 116;
                    this.jjmatchedPos = 1;
                    return 43;
                }
                return 43;
            case 2:
                if ((active0 & (-21990235701248L)) == 0 && (active1 & 139620259725311L) == 0) {
                    if ((active0 & 21990235697152L) != 0 || (active1 & 1117228630016L) != 0) {
                        return 43;
                    }
                    return -1;
                }
                if (this.jjmatchedPos != 2) {
                    this.jjmatchedKind = 116;
                    this.jjmatchedPos = 2;
                    return 43;
                }
                return 43;
            case 3:
                if ((active0 & 1648726376448L) != 0 || (active1 & 105581033558528L) != 0) {
                    return 43;
                }
                if ((active0 & (-23638962077696L)) != 0 || (active1 & 34056406298111L) != 0) {
                    if (this.jjmatchedPos != 3) {
                        this.jjmatchedKind = 116;
                        this.jjmatchedPos = 3;
                        return 43;
                    }
                    return 43;
                }
                return -1;
            case 4:
                if ((active0 & (-72057594036879360L)) == 0 && (active1 & 104386185389566L) == 0) {
                    if ((active0 & 72033955074801664L) != 0 || (active1 & 41112569857L) != 0) {
                        return 43;
                    }
                    return -1;
                }
                if (this.jjmatchedPos != 4) {
                    this.jjmatchedKind = 116;
                    this.jjmatchedPos = 4;
                    return 43;
                }
                return 43;
            case 5:
                if ((active0 & 1048576) != 0 || (active1 & 91190972113404L) != 0) {
                    this.jjmatchedKind = 116;
                    this.jjmatchedPos = 5;
                    return 43;
                }
                if ((active0 & (-72057594037927936L)) != 0 || (active1 & 13195213276163L) != 0) {
                    return 43;
                }
                return -1;
            case 6:
                if ((active0 & 1048576) != 0 || (active1 & 70918588383292L) != 0) {
                    return 43;
                }
                if ((active1 & 20272383730112L) != 0) {
                    this.jjmatchedKind = 116;
                    this.jjmatchedPos = 6;
                    return 43;
                }
                return -1;
            case 7:
                if ((active1 & 2680063336704L) != 0) {
                    this.jjmatchedKind = 116;
                    this.jjmatchedPos = 7;
                    return 43;
                }
                if ((active1 & 17592320393408L) != 0) {
                    return 43;
                }
                return -1;
            case 8:
                if ((active1 & 1638400) != 0) {
                    this.jjmatchedKind = 116;
                    this.jjmatchedPos = 8;
                    return 43;
                }
                if ((active1 & 2680061698304L) != 0) {
                    return 43;
                }
                return -1;
            case 9:
                if ((active1 & 1572864) != 0) {
                    return 43;
                }
                if ((active1 & Constants.EXCEPTION_THROWER) != 0) {
                    this.jjmatchedKind = 116;
                    this.jjmatchedPos = 9;
                    return 43;
                }
                return -1;
            case 10:
                if ((active1 & Constants.EXCEPTION_THROWER) != 0) {
                    this.jjmatchedKind = 116;
                    this.jjmatchedPos = 10;
                    return 43;
                }
                return -1;
            default:
                return -1;
        }
    }

    private final int jjStartNfa_0(int pos, long active0, long active1, long active2) {
        return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0, active1, active2), pos + 1);
    }

    private int jjStopAtPos(int pos, int kind) {
        this.jjmatchedKind = kind;
        this.jjmatchedPos = pos;
        return pos + 1;
    }

    private int jjMoveStringLiteralDfa0_0() {
        switch (this.curChar) {
            case 33:
                return jjMoveStringLiteralDfa1_0(0L, 0L, 12544L);
            case 34:
            case 35:
            case 36:
            case 39:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 81:
            case 89:
            case 90:
            case 91:
            case 92:
            case 93:
            case 95:
            case 96:
            case 113:
            case 121:
            case 122:
            default:
                return jjMoveNfa_0(7, 0);
            case 37:
                return jjStopAtPos(0, 148);
            case 38:
                return jjStopAtPos(0, 144);
            case 40:
                this.jjmatchedKind = 125;
                return jjMoveStringLiteralDfa1_0(0L, 0L, 4L);
            case 41:
                return jjStopAtPos(0, 126);
            case 42:
                return jjStopAtPos(0, 127);
            case 43:
                return jjStopAtPos(0, 145);
            case 44:
                return jjStopAtPos(0, 123);
            case 45:
                this.jjmatchedKind = 146;
                return jjMoveStringLiteralDfa1_0(0L, 0L, 268435456L);
            case 46:
                return jjStartNfaWithStates_0(0, 128, 1);
            case 47:
                return jjStartNfaWithStates_0(0, 147, 8);
            case 58:
                this.jjmatchedKind = 155;
                return jjMoveStringLiteralDfa1_0(0L, 0L, 67108864L);
            case 59:
                return jjStopAtPos(0, 122);
            case 60:
                this.jjmatchedKind = 132;
                return jjMoveStringLiteralDfa1_0(0L, 0L, 192L);
            case 61:
                return jjStopAtPos(0, 124);
            case 62:
                this.jjmatchedKind = 131;
                return jjMoveStringLiteralDfa1_0(0L, 0L, 32L);
            case 63:
                return jjStopAtPos(0, 129);
            case 64:
                return jjMoveStringLiteralDfa1_0(0L, 0L, 512L);
            case 65:
            case 97:
                return jjMoveStringLiteralDfa1_0(290848L, 805306368L, 0L);
            case 66:
            case 98:
                return jjMoveStringLiteralDfa1_0(281474976710720L, 4398046511136L, 0L);
            case 67:
            case 99:
                return jjMoveStringLiteralDfa1_0(144115192907694080L, 550848954880L, 0L);
            case 68:
            case 100:
                return jjMoveStringLiteralDfa1_0(72057594109231232L, 128L, 0L);
            case 69:
            case 101:
                return jjMoveStringLiteralDfa1_0(1152921538968682496L, 105553116283906L, 0L);
            case 70:
            case 102:
                return jjMoveStringLiteralDfa1_0(4536559206400L, 279173136384L, 0L);
            case 71:
            case 103:
                return jjMoveStringLiteralDfa1_0(140737488355328L, 0L, 0L);
            case 72:
            case 104:
                return jjMoveStringLiteralDfa1_0(2305843009213693952L, 0L, 0L);
            case 73:
            case 105:
                return jjMoveStringLiteralDfa1_0(4613374868296041216L, 131328L, 0L);
            case 74:
            case 106:
                return jjMoveStringLiteralDfa1_0(134217728L, 0L, 0L);
            case 75:
            case 107:
                return jjMoveStringLiteralDfa1_0(Constants.RET_INST, 0L, 0L);
            case 76:
            case 108:
                return jjMoveStringLiteralDfa1_0(2251800115675136L, 8589967360L, 0L);
            case 77:
            case 109:
                return jjMoveStringLiteralDfa1_0(0L, 67584L, 0L);
            case 78:
            case 110:
                return jjMoveStringLiteralDfa1_0(16842752L, 2214592520L, 0L);
            case 79:
            case 111:
                return jjMoveStringLiteralDfa1_0(589971553333021696L, Constants.NEGATABLE, 0L);
            case 80:
            case 112:
                return jjMoveStringLiteralDfa1_0(8796094070784L, 137472516100L, 0L);
            case 82:
            case 114:
                return jjMoveStringLiteralDfa1_0(18014398509481984L, 12146168561680L, 0L);
            case 83:
            case 115:
                return jjMoveStringLiteralDfa1_0(288230444871319552L, 142606336L, 0L);
            case 84:
            case 116:
                return jjMoveStringLiteralDfa1_0(566936207360L, 64L, 0L);
            case 85:
            case 117:
                return jjMoveStringLiteralDfa1_0(-9223266483738509312L, 17660905521152L, 0L);
            case 86:
            case 118:
                return jjMoveStringLiteralDfa1_0(36029896530591744L, 4194305L, 0L);
            case 87:
            case 119:
                return jjMoveStringLiteralDfa1_0(2482491097088L, 0L, 0L);
            case 88:
            case 120:
                return jjMoveStringLiteralDfa1_0(17592186044416L, 0L, 0L);
            case 94:
                return jjStopAtPos(0, 149);
            case 123:
                return jjMoveStringLiteralDfa1_0(0L, 0L, 591396864L);
            case 124:
                this.jjmatchedKind = 143;
                return jjMoveStringLiteralDfa1_0(0L, 0L, 16384L);
            case 125:
                return jjStopAtPos(0, 151);
            case 126:
                this.jjmatchedKind = 138;
                return jjMoveStringLiteralDfa1_0(0L, 0L, 2048L);
        }
    }

    private int jjMoveStringLiteralDfa1_0(long active0, long active1, long active2) {
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case 42:
                    if ((active2 & 2048) != 0) {
                        return jjStopAtPos(1, 139);
                    }
                    break;
                case 43:
                    return jjMoveStringLiteralDfa2_0(active0, 0L, active1, 0L, active2, 4L);
                case 58:
                    if ((active2 & 67108864) != 0) {
                        return jjStopAtPos(1, 154);
                    }
                    break;
                case 61:
                    if ((active2 & 32) != 0) {
                        return jjStopAtPos(1, 133);
                    }
                    if ((active2 & 64) != 0) {
                        return jjStopAtPos(1, 134);
                    }
                    if ((active2 & 256) != 0) {
                        return jjStopAtPos(1, 136);
                    }
                    break;
                case 62:
                    if ((active2 & 128) != 0) {
                        return jjStopAtPos(1, 135);
                    }
                    if ((active2 & 268435456) != 0) {
                        return jjStopAtPos(1, 156);
                    }
                    break;
                case 64:
                    if ((active2 & 512) != 0) {
                        return jjStopAtPos(1, 137);
                    }
                    break;
                case 65:
                case 97:
                    return jjMoveStringLiteralDfa2_0(active0, 2341872360283439104L, active1, 42953974281L, active2, 0L);
                case 68:
                case 100:
                    if ((active2 & 4194304) != 0) {
                        return jjStopAtPos(1, 150);
                    }
                    return jjMoveStringLiteralDfa2_0(active0, 0L, active1, 536870912L, active2, 0L);
                case 69:
                case 101:
                    return jjMoveStringLiteralDfa2_0(active0, 360569445440192512L, active1, 10995117326384L, active2, 0L);
                case 70:
                case 102:
                    return jjMoveStringLiteralDfa2_0(active0, 576460752303423488L, active1, 0L, active2, 536870912L);
                case 72:
                case 104:
                    return jjMoveStringLiteralDfa2_0(active0, 2224793059328L, active1, 2097152L, active2, 0L);
                case 73:
                case 105:
                    return jjMoveStringLiteralDfa2_0(active0, 20276368839278592L, active1, 4402475698304L, active2, 0L);
                case 76:
                case 108:
                    return jjMoveStringLiteralDfa2_0(active0, 34359742464L, active1, 268435456L, active2, 0L);
                case 77:
                case 109:
                    return jjMoveStringLiteralDfa2_0(active0, 17592186044416L, active1, 0L, active2, 0L);
                case 78:
                case 110:
                    if ((active0 & 512) != 0) {
                        this.jjmatchedKind = 9;
                        this.jjmatchedPos = 1;
                    } else if ((active0 & 2048) != 0) {
                        return jjStartNfaWithStates_0(1, 11, 43);
                    }
                    return jjMoveStringLiteralDfa2_0(active0, 4613445237042339840L, active1, 17660905652480L, active2, 0L);
                case 79:
                case 111:
                    if ((active0 & 128) != 0) {
                        return jjStartNfaWithStates_0(1, 7, 43);
                    }
                    return jjMoveStringLiteralDfa2_0(active0, 4466900795392L, active1, 1392727818240L, active2, 0L);
                case 80:
                case 112:
                    return jjMoveStringLiteralDfa2_0(active0, -9223372034707292160L, active1, 0L, active2, 0L);
                case 82:
                case 114:
                    if ((active0 & 1024) != 0) {
                        this.jjmatchedKind = 10;
                        this.jjmatchedPos = 1;
                    }
                    return jjMoveStringLiteralDfa2_0(active0, 153263126496673792L, active1, 137472507972L, active2, 0L);
                case 83:
                case 115:
                    if ((active0 & 32) != 0) {
                        this.jjmatchedKind = 5;
                        this.jjmatchedPos = 1;
                    } else if ((active0 & 256) != 0) {
                        return jjStartNfaWithStates_0(1, 8, 43);
                    }
                    return jjMoveStringLiteralDfa2_0(active0, 35184372350976L, active1, 2L, active2, 0L);
                case 84:
                case 116:
                    if ((active2 & 16777216) != 0) {
                        this.jjmatchedKind = 152;
                        this.jjmatchedPos = 1;
                    }
                    return jjMoveStringLiteralDfa2_0(active0, 0L, active1, 8388608L, active2, 33554432L);
                case 85:
                case 117:
                    return jjMoveStringLiteralDfa2_0(active0, 4503737083101184L, active1, 551903297536L, active2, 0L);
                case 86:
                case 118:
                    return jjMoveStringLiteralDfa2_0(active0, 0L, active1, Constants.NEGATABLE, active2, 0L);
                case 88:
                case 120:
                    return jjMoveStringLiteralDfa2_0(active0, FileUtils.ONE_EB, active1, 105553116283904L, active2, 0L);
                case 89:
                case 121:
                    if ((active0 & 64) != 0) {
                        return jjStartNfaWithStates_0(1, 6, 43);
                    }
                    break;
                case 124:
                    if ((active2 & 16384) != 0) {
                        return jjStopAtPos(1, 142);
                    }
                    break;
                case 126:
                    if ((active2 & Constants.NEGATABLE) != 0) {
                        this.jjmatchedKind = 140;
                        this.jjmatchedPos = 1;
                    }
                    return jjMoveStringLiteralDfa2_0(active0, 0L, active1, 0L, active2, 8192L);
            }
            return jjStartNfa_0(0, active0, active1, active2);
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(0, active0, active1, active2);
            return 1;
        }
    }

    private int jjMoveStringLiteralDfa2_0(long old0, long active0, long old1, long active1, long old2, long active2) {
        long active02 = active0 & old0;
        long active22 = active02 | (active1 & old1);
        if ((active22 | (active2 & old2)) == 0) {
            return jjStartNfa_0(0, old0, old1, old2);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case 41:
                    if ((active22 & 4) != 0) {
                        return jjStopAtPos(2, 130);
                    }
                    break;
                case 42:
                    if ((active22 & 8192) != 0) {
                        return jjStopAtPos(2, 141);
                    }
                    break;
                case 65:
                case 97:
                    return jjMoveStringLiteralDfa3_0(active02, 0L, active02, SizeBasedTriggeringPolicy.DEFAULT_MAX_FILE_SIZE, active22, 0L);
                case 66:
                case 98:
                    return jjMoveStringLiteralDfa3_0(active02, 549755813888L, active02, 68853694464L, active22, 0L);
                case 67:
                case 99:
                    if ((active02 & 262144) != 0) {
                        return jjStartNfaWithStates_0(2, 18, 43);
                    }
                    return jjMoveStringLiteralDfa3_0(active02, 0L, active02, 67109890L, active22, 0L);
                case 68:
                case 100:
                    if ((active02 & 8192) != 0) {
                        return jjStartNfaWithStates_0(2, 13, 43);
                    }
                    if ((active02 & 2097152) != 0) {
                        return jjStartNfaWithStates_0(2, 21, 43);
                    }
                    if ((active02 & 536870912) != 0) {
                        return jjStartNfaWithStates_0(2, 93, 43);
                    }
                    return jjMoveStringLiteralDfa3_0(active02, -9213801887646613504L, active02, 0L, active22, 0L);
                case 69:
                case 101:
                    return jjMoveStringLiteralDfa3_0(active02, 144118514528026624L, active02, 105690555224064L, active22, 0L);
                case 70:
                case 102:
                    return jjMoveStringLiteralDfa3_0(active02, 576460752571858944L, active02, 1048576L, active22, 0L);
                case 71:
                case 103:
                    return jjMoveStringLiteralDfa3_0(active02, 18295873486192640L, active02, 8796093022208L, active22, 0L);
                case 73:
                case 105:
                    return jjMoveStringLiteralDfa3_0(active02, 1153027057857331200L, active02, 33554436L, active22, 0L);
                case 75:
                case 107:
                    return jjMoveStringLiteralDfa3_0(active02, 33554432L, active02, 0L, active22, 0L);
                case 76:
                case 108:
                    if ((active02 & Constants.NEGATABLE) != 0) {
                        return jjStartNfaWithStates_0(2, 12, 43);
                    }
                    if ((active02 & 17592186044416L) != 0) {
                        return jjStartNfaWithStates_0(2, 44, 43);
                    }
                    return jjMoveStringLiteralDfa3_0(active02, 396316904664334336L, active02, 17870285176833L, active22, 0L);
                case 77:
                case 109:
                    return jjMoveStringLiteralDfa3_0(active02, 2251868533161984L, active02, 0L, active22, 0L);
                case 78:
                case 110:
                    if ((active22 & 536870912) != 0) {
                        return jjStopAtPos(2, 157);
                    }
                    return jjMoveStringLiteralDfa3_0(active02, FileUtils.ONE_PB, active02, 4432423553024L, active22, 0L);
                case 79:
                case 111:
                    return jjMoveStringLiteralDfa3_0(active02, 140739166076928L, active02, 0L, active22, 0L);
                case 80:
                case 112:
                    if ((active02 & 524288) != 0) {
                        return jjStartNfaWithStates_0(2, 19, 43);
                    }
                    return jjMoveStringLiteralDfa3_0(active02, 0L, active02, 16L, active22, 0L);
                case 82:
                case 114:
                    if ((active02 & 4398046511104L) != 0) {
                        this.jjmatchedKind = 42;
                        this.jjmatchedPos = 2;
                    }
                    return jjMoveStringLiteralDfa3_0(active02, 1048576L, active02, 554055245824L, active22, 0L);
                case 83:
                case 115:
                    if ((active22 & 33554432) != 0) {
                        return jjStopAtPos(2, 153);
                    }
                    return jjMoveStringLiteralDfa3_0(active02, 4611686057086287872L, active02, 8589935232L, active22, 0L);
                case 84:
                case 116:
                    if ((active02 & Constants.EXCEPTION_THROWER) != 0) {
                        return jjStartNfaWithStates_0(2, 16, 43);
                    }
                    if ((active02 & 131072) != 0) {
                        return jjStartNfaWithStates_0(2, 17, 43);
                    }
                    return jjMoveStringLiteralDfa3_0(active02, 4503874513666048L, active02, 2199291937064L, active22, 0L);
                case 85:
                case 117:
                    return jjMoveStringLiteralDfa3_0(active02, 0L, active02, 64L, active22, 0L);
                case 86:
                case 118:
                    return jjMoveStringLiteralDfa3_0(active02, 2305851805306716160L, active02, 0L, active22, 0L);
                case 87:
                case 119:
                    if ((active02 & FileUtils.ONE_TB) != 0) {
                        this.jjmatchedKind = 104;
                        this.jjmatchedPos = 2;
                    }
                    return jjMoveStringLiteralDfa3_0(active02, 0L, active02, 17179869184L, active22, 0L);
                case 89:
                case 121:
                    if ((active02 & 16384) != 0) {
                        return jjStartNfaWithStates_0(2, 14, 43);
                    }
                    if ((active02 & Constants.RET_INST) != 0) {
                        return jjStartNfaWithStates_0(2, 15, 43);
                    }
                    break;
            }
            return jjStartNfa_0(1, active02, active02, active22);
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(1, active02, active02, active22);
            return 2;
        }
    }

    private int jjMoveStringLiteralDfa3_0(long old0, long active0, long old1, long active1, long old2, long active2) {
        long active02 = active0 & old0;
        if ((active02 | (active1 & old1) | (active2 & old2)) == 0) {
            return jjStartNfa_0(1, old0, old1, old2);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case 65:
                case 97:
                    return jjMoveStringLiteralDfa4_0(active02, -9079256848778919936L, active02, 4398046511106L);
                case 67:
                case 99:
                    if ((active02 & 4194304) != 0) {
                        return jjStartNfaWithStates_0(3, 22, 43);
                    }
                    if ((active02 & 35184372088832L) != 0) {
                        this.jjmatchedKind = 109;
                        this.jjmatchedPos = 3;
                    }
                    return jjMoveStringLiteralDfa4_0(active02, 1048576L, active02, 70506183131136L);
                case 69:
                case 101:
                    if ((active02 & 33554432) != 0) {
                        return jjStartNfaWithStates_0(3, 25, 43);
                    }
                    if ((active02 & 4294967296L) != 0) {
                        return jjStartNfaWithStates_0(3, 32, 43);
                    }
                    if ((active02 & 34359738368L) != 0) {
                        return jjStartNfaWithStates_0(3, 35, 43);
                    }
                    if ((active02 & 68719476736L) != 0) {
                        return jjStartNfaWithStates_0(3, 36, 43);
                    }
                    return jjMoveStringLiteralDfa4_0(active02, 4987173637359403008L, active02, 8796362999040L);
                case 71:
                case 103:
                    return jjMoveStringLiteralDfa4_0(active02, 0L, active02, 34359738368L);
                case 72:
                case 104:
                    if ((active02 & 274877906944L) != 0) {
                        return jjStartNfaWithStates_0(3, 38, 43);
                    }
                    return jjMoveStringLiteralDfa4_0(active02, 18014398509481984L, active02, 0L);
                case 73:
                case 105:
                    return jjMoveStringLiteralDfa4_0(active02, 2308376284004089856L, active02, 0L);
                case 76:
                case 108:
                    if ((active02 & 16777216) != 0) {
                        this.jjmatchedKind = 24;
                        this.jjmatchedPos = 3;
                    } else if ((active02 & 137438953472L) != 0) {
                        return jjStartNfaWithStates_0(3, 37, 43);
                    }
                    return jjMoveStringLiteralDfa4_0(active02, 549755813888L, active02, 277159608336L);
                case 77:
                case 109:
                    if ((active02 & 1073741824) != 0) {
                        return jjStartNfaWithStates_0(3, 30, 43);
                    }
                    return jjMoveStringLiteralDfa4_0(active02, 0L, active02, 4L);
                case 78:
                case 110:
                    if ((active02 & 134217728) != 0) {
                        return jjStartNfaWithStates_0(3, 27, 43);
                    }
                    if ((active02 & 2147483648L) != 0) {
                        return jjStartNfaWithStates_0(3, 31, 43);
                    }
                    if ((active02 & 8589934592L) != 0) {
                        return jjStartNfaWithStates_0(3, 33, 43);
                    }
                    if ((active02 & 17179869184L) != 0) {
                        return jjStartNfaWithStates_0(3, 34, 43);
                    }
                    return jjMoveStringLiteralDfa4_0(active02, 35184372088832L, active02, 16777280L);
                case 79:
                case 111:
                    if ((active02 & 8388608) != 0) {
                        return jjStartNfaWithStates_0(3, 23, 43);
                    }
                    return jjMoveStringLiteralDfa4_0(active02, 79164837199872L, active02, 17660939075584L);
                case 80:
                case 112:
                    if ((active02 & 67108864) != 0) {
                        return jjStartNfaWithStates_0(3, 26, 43);
                    }
                    break;
                case 82:
                case 114:
                    if ((active02 & Constants.NEGATABLE) != 0) {
                        return jjStartNfaWithStates_0(3, 76, 43);
                    }
                    return jjMoveStringLiteralDfa4_0(active02, 2199023255552L, active02, 549766316032L);
                case 83:
                case 115:
                    if ((active02 & 17179869184L) != 0) {
                        return jjStartNfaWithStates_0(3, 98, 43);
                    }
                    return jjMoveStringLiteralDfa4_0(active02, 1729382257447141376L, active02, 4295491584L);
                case 84:
                case 116:
                    if ((active02 & 268435456) != 0) {
                        return jjStartNfaWithStates_0(3, 28, 43);
                    }
                    if ((active02 & 512) != 0) {
                        return jjStartNfaWithStates_0(3, 73, 43);
                    }
                    if ((active02 & 8589934592L) != 0) {
                        return jjStartNfaWithStates_0(3, 97, 43);
                    }
                    return jjMoveStringLiteralDfa4_0(active02, 0L, active02, 8320L);
                case 85:
                case 117:
                    return jjMoveStringLiteralDfa4_0(active02, 36169534507319296L, active02, 2200096999433L);
                case 87:
                case 119:
                    if ((active02 & FileUtils.ONE_TB) != 0) {
                        return jjStartNfaWithStates_0(3, 40, 43);
                    }
                    return jjMoveStringLiteralDfa4_0(active02, 0L, active02, 32L);
                case 89:
                case 121:
                    return jjMoveStringLiteralDfa4_0(active02, 0L, active02, 71303168L);
            }
            return jjStartNfa_0(2, active02, active02, 0L);
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(2, active02, active02, 0L);
            return 3;
        }
    }

    private int jjMoveStringLiteralDfa4_0(long old0, long active0, long old1, long active1) {
        long active02 = active0 & old0;
        if ((active02 | (active1 & old1)) == 0) {
            return jjStartNfa_0(2, old0, old1, 0L);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case 65:
                case 97:
                    return jjMoveStringLiteralDfa5_0(active02, 0L, active02, 2113556L);
                case 67:
                case 99:
                    return jjMoveStringLiteralDfa5_0(active02, 288230376151711744L, active02, 67108928L);
                case 69:
                case 101:
                    if ((active02 & 549755813888L) != 0) {
                        return jjStartNfaWithStates_0(4, 39, 43);
                    }
                    if ((active02 & 2199023255552L) != 0) {
                        return jjStartNfaWithStates_0(4, 41, 43);
                    }
                    if ((active02 & 36028797018963968L) != 0) {
                        this.jjmatchedKind = 55;
                        this.jjmatchedPos = 4;
                    } else if ((active02 & 34359738368L) != 0) {
                        return jjStartNfaWithStates_0(4, 99, 43);
                    }
                    return jjMoveStringLiteralDfa5_0(active02, 576460752304472064L, active02, 687211544609L);
                case 71:
                case 103:
                    if ((active02 & 35184372088832L) != 0) {
                        return jjStartNfaWithStates_0(4, 45, 43);
                    }
                    return jjMoveStringLiteralDfa5_0(active02, 0L, active02, 17592186044416L);
                case 73:
                case 105:
                    return jjMoveStringLiteralDfa5_0(active02, 0L, active02, 138682496L);
                case 77:
                case 109:
                    return jjMoveStringLiteralDfa5_0(active02, 0L, active02, 1073741824L);
                case 78:
                case 110:
                    if ((active02 & 70368744177664L) != 0) {
                        return jjStartNfaWithStates_0(4, 46, 43);
                    }
                    if ((active02 & 281474976710656L) != 0) {
                        return jjStartNfaWithStates_0(4, 48, 43);
                    }
                    return jjMoveStringLiteralDfa5_0(active02, 2305843009213693952L, active02, 0L);
                case 79:
                case 111:
                    return jjMoveStringLiteralDfa5_0(active02, 0L, active02, 274877906944L);
                case 80:
                case 112:
                    if ((active02 & 140737488355328L) != 0) {
                        return jjStartNfaWithStates_0(4, 47, 43);
                    }
                    return jjMoveStringLiteralDfa5_0(active02, 0L, active02, 1026L);
                case 82:
                case 114:
                    if ((active02 & FileUtils.ONE_PB) != 0) {
                        return jjStartNfaWithStates_0(4, 50, 43);
                    }
                    if ((active02 & IEEEDouble.FRAC_ASSUMED_HIGH_BIT) != 0) {
                        return jjStartNfaWithStates_0(4, 52, 43);
                    }
                    if ((active02 & 9007199254740992L) != 0) {
                        return jjStartNfaWithStates_0(4, 53, 43);
                    }
                    if ((active02 & 33554432) != 0) {
                        return jjStartNfaWithStates_0(4, 89, 43);
                    }
                    if ((active02 & 268435456) != 0) {
                        return jjStartNfaWithStates_0(4, 92, 43);
                    }
                    return jjMoveStringLiteralDfa5_0(active02, Longs.MAX_POWER_OF_TWO, active02, 6597071044872L);
                case 83:
                case 115:
                    if ((active02 & 536870912) != 0) {
                        return jjStartNfaWithStates_0(4, 29, 43);
                    }
                    if ((active02 & 2048) != 0) {
                        return jjStartNfaWithStates_0(4, 75, 43);
                    }
                    if ((active02 & 2147483648L) != 0) {
                        return jjStartNfaWithStates_0(4, 95, 43);
                    }
                    break;
                case 84:
                case 116:
                    if ((active02 & 8796093022208L) != 0) {
                        return jjStartNfaWithStates_0(4, 43, 43);
                    }
                    if ((active02 & 2251799813685248L) != 0) {
                        return jjStartNfaWithStates_0(4, 51, 43);
                    }
                    if ((active02 & 18014398509481984L) != 0) {
                        return jjStartNfaWithStates_0(4, 54, 43);
                    }
                    if ((active02 & 8388608) != 0) {
                        return jjStartNfaWithStates_0(4, 87, 43);
                    }
                    if ((active02 & 4294967296L) != 0) {
                        return jjStartNfaWithStates_0(4, 96, 43);
                    }
                    return jjMoveStringLiteralDfa5_0(active02, -7854277750134145024L, active02, 524288L);
                case 85:
                case 117:
                    return jjMoveStringLiteralDfa5_0(active02, 0L, active02, 70437463654400L);
                case 88:
                case 120:
                    if ((active02 & 562949953421312L) != 0) {
                        return jjStartNfaWithStates_0(4, 49, 43);
                    }
                    return jjMoveStringLiteralDfa5_0(active02, 0L, active02, 8796093022208L);
            }
            return jjStartNfa_0(3, active02, active02, 0L);
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(3, active02, active02, 0L);
            return 4;
        }
    }

    private int jjMoveStringLiteralDfa5_0(long old0, long active0, long old1, long active1) {
        long active02 = active0 & old0;
        if ((active02 | (active1 & old1)) == 0) {
            return jjStartNfa_0(3, old0, old1, 0L);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case 65:
                case 97:
                    return jjMoveStringLiteralDfa6_0(active02, 0L, active02, 32840L);
                case 67:
                case 99:
                    return jjMoveStringLiteralDfa6_0(active02, 0L, active02, 18890768L);
                case 68:
                case 100:
                    return jjMoveStringLiteralDfa6_0(active02, 0L, active02, 137438953472L);
                case 69:
                case 101:
                    if ((active02 & 72057594037927936L) != 0) {
                        return jjStartNfaWithStates_0(5, 56, 43);
                    }
                    if ((active02 & 144115188075855872L) != 0) {
                        return jjStartNfaWithStates_0(5, 57, 43);
                    }
                    if ((active02 & Long.MIN_VALUE) != 0) {
                        return jjStartNfaWithStates_0(5, 63, 43);
                    }
                    if ((active02 & 2) != 0) {
                        return jjStartNfaWithStates_0(5, 65, 43);
                    }
                    return jjMoveStringLiteralDfa6_0(active02, 0L, active02, 1048608L);
                case 71:
                case 103:
                    if ((active02 & 2305843009213693952L) != 0) {
                        return jjStartNfaWithStates_0(5, 61, 43);
                    }
                    return jjMoveStringLiteralDfa6_0(active02, 0L, active02, 17592186306560L);
                case 73:
                case 105:
                    return jjMoveStringLiteralDfa6_0(active02, 0L, active02, Constants.EXCEPTION_THROWER);
                case 76:
                case 108:
                    return jjMoveStringLiteralDfa6_0(active02, 0L, active02, 67108864L);
                case 78:
                case 110:
                    if ((active02 & 1073741824) != 0) {
                        return jjStartNfaWithStates_0(5, 94, 43);
                    }
                    return jjMoveStringLiteralDfa6_0(active02, 1048576L, active02, 2817636958336L);
                case 80:
                case 112:
                    if ((active02 & 8796093022208L) != 0) {
                        return jjStartNfaWithStates_0(5, 107, 43);
                    }
                    break;
                case 82:
                case 114:
                    return jjMoveStringLiteralDfa6_0(active02, 0L, active02, 524292L);
                case 83:
                case 115:
                    if ((active02 & FileUtils.ONE_EB) != 0) {
                        return jjStartNfaWithStates_0(5, 60, 43);
                    }
                    if ((active02 & 1) != 0) {
                        return jjStartNfaWithStates_0(5, 64, 43);
                    }
                    return jjMoveStringLiteralDfa6_0(active02, 0L, active02, 256L);
                case 84:
                case 116:
                    if ((active02 & 288230376151711744L) != 0) {
                        return jjStartNfaWithStates_0(5, 58, 43);
                    }
                    if ((active02 & 576460752303423488L) != 0) {
                        return jjStartNfaWithStates_0(5, 59, 43);
                    }
                    if ((active02 & Longs.MAX_POWER_OF_TWO) != 0) {
                        return jjStartNfaWithStates_0(5, 62, 43);
                    }
                    if ((active02 & 1024) != 0) {
                        return jjStartNfaWithStates_0(5, 74, 43);
                    }
                    return jjMoveStringLiteralDfa6_0(active02, 0L, active02, 70368744185856L);
                case 86:
                case 118:
                    return jjMoveStringLiteralDfa6_0(active02, 0L, active02, 131072L);
                case 87:
                case 119:
                    return jjMoveStringLiteralDfa6_0(active02, 0L, active02, 274877906944L);
                case 89:
                case 121:
                    if ((active02 & 4398046511104L) != 0) {
                        return jjStartNfaWithStates_0(5, 106, 43);
                    }
                    break;
            }
            return jjStartNfa_0(4, active02, active02, 0L);
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(4, active02, active02, 0L);
            return 5;
        }
    }

    private int jjMoveStringLiteralDfa6_0(long old0, long active0, long old1, long active1) {
        long active02 = active0 & old0;
        if ((active02 | (active1 & old1)) == 0) {
            return jjStartNfa_0(4, old0, old1, 0L);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case 65:
                case 97:
                    return jjMoveStringLiteralDfa7_0(active02, 0L, active02, 720896L);
                case 67:
                case 99:
                    return jjMoveStringLiteralDfa7_0(active02, 0L, active02, 128L);
                case 68:
                case 100:
                    return jjMoveStringLiteralDfa7_0(active02, 0L, active02, 68719476736L);
                case 69:
                case 101:
                    if ((active02 & 16) != 0) {
                        return jjStartNfaWithStates_0(6, 68, 43);
                    }
                    if ((active02 & 67108864) != 0) {
                        return jjStartNfaWithStates_0(6, 90, 43);
                    }
                    if ((active02 & 70368744177664L) != 0) {
                        return jjStartNfaWithStates_0(6, 110, 43);
                    }
                    return jjMoveStringLiteralDfa7_0(active02, 0L, active02, 17592186044672L);
                case 71:
                case 103:
                    if ((active02 & 4194304) != 0) {
                        return jjStartNfaWithStates_0(6, 86, 43);
                    }
                    return jjMoveStringLiteralDfa7_0(active02, 0L, active02, 134217728L);
                case 73:
                case 105:
                    return jjMoveStringLiteralDfa7_0(active02, 0L, active02, 2611340124160L);
                case 76:
                case 108:
                    if ((active02 & 8) != 0) {
                        return jjStartNfaWithStates_0(6, 67, 43);
                    }
                    if ((active02 & Constants.RET_INST) != 0) {
                        return jjStartNfaWithStates_0(6, 79, 43);
                    }
                    break;
                case 78:
                case 110:
                    if ((active02 & 32) != 0) {
                        return jjStartNfaWithStates_0(6, 69, 43);
                    }
                    if ((active02 & 262144) != 0) {
                        return jjStartNfaWithStates_0(6, 82, 43);
                    }
                    return jjMoveStringLiteralDfa7_0(active02, 0L, active02, 1048576L);
                case 84:
                case 116:
                    if ((active02 & 1048576) != 0) {
                        return jjStartNfaWithStates_0(6, 20, 43);
                    }
                    if ((active02 & 16384) != 0) {
                        return jjStartNfaWithStates_0(6, 78, 43);
                    }
                    if ((active02 & 16777216) != 0) {
                        return jjStartNfaWithStates_0(6, 88, 43);
                    }
                    if ((active02 & 549755813888L) != 0) {
                        return jjStartNfaWithStates_0(6, 103, 43);
                    }
                    return jjMoveStringLiteralDfa7_0(active02, 0L, active02, 2097216L);
                case 89:
                case 121:
                    if ((active02 & 4) != 0) {
                        return jjStartNfaWithStates_0(6, 66, 43);
                    }
                    break;
            }
            return jjStartNfa_0(5, active02, active02, 0L);
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(5, active02, active02, 0L);
            return 6;
        }
    }

    private int jjMoveStringLiteralDfa7_0(long old0, long active0, long old1, long active1) {
        long active12 = active0 & old0;
        if ((active12 | (active1 & old1)) == 0) {
            return jjStartNfa_0(5, old0, old1, 0L);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case 67:
                case 99:
                    return jjMoveStringLiteralDfa8_0(active12, 1048832L);
                case 68:
                case 100:
                    if ((active12 & 17592186044416L) != 0) {
                        return jjStartNfaWithStates_0(7, 108, 43);
                    }
                    break;
                case 69:
                case 101:
                    if ((active12 & 64) != 0) {
                        return jjStartNfaWithStates_0(7, 70, 43);
                    }
                    return jjMoveStringLiteralDfa8_0(active12, 68721573888L);
                case 73:
                case 105:
                    return jjMoveStringLiteralDfa8_0(active12, 524288L);
                case 76:
                case 108:
                    if ((active12 & 131072) != 0) {
                        return jjStartNfaWithStates_0(7, 81, 43);
                    }
                    return jjMoveStringLiteralDfa8_0(active12, Constants.EXCEPTION_THROWER);
                case 78:
                case 110:
                    return jjMoveStringLiteralDfa8_0(active12, 2611340115968L);
                case 79:
                case 111:
                    return jjMoveStringLiteralDfa8_0(active12, 8192L);
                case 83:
                case 115:
                    if ((active12 & 134217728) != 0) {
                        return jjStartNfaWithStates_0(7, 91, 43);
                    }
                    break;
                case 84:
                case 116:
                    if ((active12 & 128) != 0) {
                        return jjStartNfaWithStates_0(7, 71, 43);
                    }
                    break;
            }
            return jjStartNfa_0(6, 0L, active12, 0L);
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(6, 0L, active12, 0L);
            return 7;
        }
    }

    private int jjMoveStringLiteralDfa8_0(long old1, long active1) {
        long active12 = active1 & old1;
        if (active12 == 0) {
            return jjStartNfa_0(6, 0L, old1, 0L);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case 68:
                case 100:
                    if ((active12 & 68719476736L) != 0) {
                        return jjStartNfaWithStates_0(8, 100, 43);
                    }
                    break;
                case 69:
                case 101:
                    return jjMoveStringLiteralDfa9_0(active12, 1048576L);
                case 71:
                case 103:
                    if ((active12 & 137438953472L) != 0) {
                        return jjStartNfaWithStates_0(8, 101, 43);
                    }
                    if ((active12 & 274877906944L) != 0) {
                        return jjStartNfaWithStates_0(8, 102, 43);
                    }
                    if ((active12 & 2199023255552L) != 0) {
                        return jjStartNfaWithStates_0(8, 105, 43);
                    }
                    break;
                case 73:
                case 105:
                    return jjMoveStringLiteralDfa9_0(active12, Constants.EXCEPTION_THROWER);
                case 78:
                case 110:
                    if ((active12 & 8192) != 0) {
                        return jjStartNfaWithStates_0(8, 77, 43);
                    }
                    return jjMoveStringLiteralDfa9_0(active12, 524288L);
                case 82:
                case 114:
                    if ((active12 & 2097152) != 0) {
                        return jjStartNfaWithStates_0(8, 85, 43);
                    }
                    break;
                case 84:
                case 116:
                    if ((active12 & 256) != 0) {
                        return jjStartNfaWithStates_0(8, 72, 43);
                    }
                    break;
            }
            return jjStartNfa_0(7, 0L, active12, 0L);
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(7, 0L, active12, 0L);
            return 8;
        }
    }

    private int jjMoveStringLiteralDfa9_0(long old1, long active1) {
        long active12 = active1 & old1;
        if (active12 == 0) {
            return jjStartNfa_0(7, 0L, old1, 0L);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case 83:
                case 115:
                    if ((active12 & 1048576) != 0) {
                        return jjStartNfaWithStates_0(9, 84, 43);
                    }
                    break;
                case 84:
                case 116:
                    if ((active12 & 524288) != 0) {
                        return jjStartNfaWithStates_0(9, 83, 43);
                    }
                    break;
                case 90:
                case 122:
                    return jjMoveStringLiteralDfa10_0(active12, Constants.EXCEPTION_THROWER);
            }
            return jjStartNfa_0(8, 0L, active12, 0L);
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(8, 0L, active12, 0L);
            return 9;
        }
    }

    private int jjMoveStringLiteralDfa10_0(long old1, long active1) {
        long active12 = active1 & old1;
        if (active12 == 0) {
            return jjStartNfa_0(8, 0L, old1, 0L);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case 69:
                case 101:
                    return jjMoveStringLiteralDfa11_0(active12, Constants.EXCEPTION_THROWER);
                default:
                    return jjStartNfa_0(9, 0L, active12, 0L);
            }
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(9, 0L, active12, 0L);
            return 10;
        }
    }

    private int jjMoveStringLiteralDfa11_0(long old1, long active1) {
        long active12 = active1 & old1;
        if (active12 == 0) {
            return jjStartNfa_0(9, 0L, old1, 0L);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case 68:
                case 100:
                    if ((active12 & Constants.EXCEPTION_THROWER) != 0) {
                        return jjStartNfaWithStates_0(11, 80, 43);
                    }
                    break;
            }
            return jjStartNfa_0(10, 0L, active12, 0L);
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(10, 0L, active12, 0L);
            return 11;
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
        this.jjnewStateCnt = 43;
        int i = 1;
        this.jjstateSet[0] = startState;
        int kind = Integer.MAX_VALUE;
        while (true) {
            int i2 = this.jjround + 1;
            this.jjround = i2;
            if (i2 == Integer.MAX_VALUE) {
                ReInitRounds();
            }
            if (this.curChar < 64) {
                long l = 1 << this.curChar;
                do {
                    i--;
                    switch (this.jjstateSet[i]) {
                        case 0:
                            if (this.curChar == 46) {
                                jjCheckNAdd(1);
                                break;
                            }
                            break;
                        case 1:
                            if ((287948901175001088L & l) != 0) {
                                if (kind > 111) {
                                    kind = 111;
                                }
                                jjCheckNAddTwoStates(1, 2);
                                break;
                            }
                            break;
                        case 3:
                            if ((43980465111040L & l) != 0) {
                                jjCheckNAdd(4);
                                break;
                            }
                            break;
                        case 4:
                            if ((287948901175001088L & l) != 0) {
                                if (kind > 111) {
                                    kind = 111;
                                }
                                jjCheckNAdd(4);
                                break;
                            }
                            break;
                        case 5:
                            if (this.curChar == 45) {
                                if (kind > 114) {
                                    kind = 114;
                                }
                                jjCheckNAdd(6);
                                break;
                            }
                            break;
                        case 6:
                            if (((-9217) & l) != 0) {
                                if (kind > 114) {
                                    kind = 114;
                                }
                                jjCheckNAdd(6);
                                break;
                            }
                            break;
                        case 7:
                            if ((287948901175001088L & l) != 0) {
                                if (kind > 112) {
                                    kind = 112;
                                }
                                jjCheckNAddStates(0, 6);
                                break;
                            } else if (this.curChar == 34) {
                                jjCheckNAddTwoStates(23, 24);
                                break;
                            } else if (this.curChar == 39) {
                                jjCheckNAddTwoStates(18, 19);
                                break;
                            } else if (this.curChar == 47) {
                                int[] iArr = this.jjstateSet;
                                int i3 = this.jjnewStateCnt;
                                this.jjnewStateCnt = i3 + 1;
                                iArr[i3] = 8;
                                break;
                            } else if (this.curChar == 45) {
                                int[] iArr2 = this.jjstateSet;
                                int i4 = this.jjnewStateCnt;
                                this.jjnewStateCnt = i4 + 1;
                                iArr2[i4] = 5;
                                break;
                            } else if (this.curChar == 46) {
                                jjCheckNAdd(1);
                                break;
                            }
                            break;
                        case 8:
                            if (this.curChar == 42) {
                                jjCheckNAddTwoStates(9, 10);
                                break;
                            }
                            break;
                        case 9:
                            if (((-4398046511105L) & l) != 0) {
                                jjCheckNAddTwoStates(9, 10);
                                break;
                            }
                            break;
                        case 10:
                            if (this.curChar == 42) {
                                jjCheckNAddStates(7, 9);
                                break;
                            }
                            break;
                        case 11:
                            if (((-145135534866433L) & l) != 0) {
                                jjCheckNAddTwoStates(12, 10);
                                break;
                            }
                            break;
                        case 12:
                            if (((-4398046511105L) & l) != 0) {
                                jjCheckNAddTwoStates(12, 10);
                                break;
                            }
                            break;
                        case 13:
                            if (this.curChar == 47 && kind > 115) {
                                kind = 115;
                                break;
                            }
                            break;
                        case 14:
                            if (this.curChar == 47) {
                                int[] iArr3 = this.jjstateSet;
                                int i5 = this.jjnewStateCnt;
                                this.jjnewStateCnt = i5 + 1;
                                iArr3[i5] = 8;
                                break;
                            }
                            break;
                        case 16:
                        case 43:
                            if ((287949004254216192L & l) != 0) {
                                if (kind > 116) {
                                    kind = 116;
                                }
                                jjCheckNAdd(16);
                                break;
                            }
                            break;
                        case 17:
                            if (this.curChar == 39) {
                                jjCheckNAddTwoStates(18, 19);
                                break;
                            }
                            break;
                        case 18:
                            if (((-549755813889L) & l) != 0) {
                                jjCheckNAddTwoStates(18, 19);
                                break;
                            }
                            break;
                        case 19:
                            if (this.curChar == 39) {
                                if (kind > 119) {
                                    kind = 119;
                                }
                                int[] iArr4 = this.jjstateSet;
                                int i6 = this.jjnewStateCnt;
                                this.jjnewStateCnt = i6 + 1;
                                iArr4[i6] = 20;
                                break;
                            }
                            break;
                        case 20:
                            if (this.curChar == 39) {
                                jjCheckNAddTwoStates(21, 19);
                                break;
                            }
                            break;
                        case 21:
                            if (((-549755813889L) & l) != 0) {
                                jjCheckNAddTwoStates(21, 19);
                                break;
                            }
                            break;
                        case 22:
                            if (this.curChar == 34) {
                                jjCheckNAddTwoStates(23, 24);
                                break;
                            }
                            break;
                        case 23:
                            if (((-17179878401L) & l) != 0) {
                                jjCheckNAddTwoStates(23, 24);
                                break;
                            }
                            break;
                        case 24:
                            if (this.curChar == 34 && kind > 120) {
                                kind = 120;
                                break;
                            }
                            break;
                        case 26:
                            if (((-9217) & l) != 0) {
                                jjAddStates(10, 11);
                                break;
                            }
                            break;
                        case 29:
                            if (((-9217) & l) != 0) {
                                jjAddStates(12, 13);
                                break;
                            }
                            break;
                        case 31:
                            if ((287948901175001088L & l) != 0) {
                                if (kind > 112) {
                                    kind = 112;
                                }
                                jjCheckNAddStates(0, 6);
                                break;
                            }
                            break;
                        case 32:
                            if ((287948901175001088L & l) != 0) {
                                jjCheckNAddTwoStates(32, 0);
                                break;
                            }
                            break;
                        case 33:
                            if ((287948901175001088L & l) != 0) {
                                jjCheckNAddTwoStates(33, 34);
                                break;
                            }
                            break;
                        case 34:
                            if (this.curChar == 46) {
                                if (kind > 111) {
                                    kind = 111;
                                }
                                int[] iArr5 = this.jjstateSet;
                                int i7 = this.jjnewStateCnt;
                                this.jjnewStateCnt = i7 + 1;
                                iArr5[i7] = 35;
                                break;
                            }
                            break;
                        case 36:
                            if ((43980465111040L & l) != 0) {
                                jjCheckNAdd(37);
                                break;
                            }
                            break;
                        case 37:
                            if ((287948901175001088L & l) != 0) {
                                if (kind > 111) {
                                    kind = 111;
                                }
                                jjCheckNAdd(37);
                                break;
                            }
                            break;
                        case 38:
                            if ((287948901175001088L & l) != 0) {
                                jjCheckNAddTwoStates(38, 39);
                                break;
                            }
                            break;
                        case 40:
                            if ((43980465111040L & l) != 0) {
                                jjCheckNAdd(41);
                                break;
                            }
                            break;
                        case 41:
                            if ((287948901175001088L & l) != 0) {
                                if (kind > 111) {
                                    kind = 111;
                                }
                                jjCheckNAdd(41);
                                break;
                            }
                            break;
                        case 42:
                            if ((287948901175001088L & l) != 0) {
                                if (kind > 112) {
                                    kind = 112;
                                }
                                jjCheckNAdd(42);
                                break;
                            }
                            break;
                    }
                } while (i != startsAt);
            } else if (this.curChar < 128) {
                long l2 = 1 << (this.curChar & 63);
                do {
                    i--;
                    switch (this.jjstateSet[i]) {
                        case 2:
                            if ((137438953504L & l2) != 0) {
                                jjAddStates(14, 15);
                                break;
                            }
                            break;
                        case 6:
                            if (kind > 114) {
                                kind = 114;
                            }
                            int[] iArr6 = this.jjstateSet;
                            int i8 = this.jjnewStateCnt;
                            this.jjnewStateCnt = i8 + 1;
                            iArr6[i8] = 6;
                            break;
                        case 7:
                            if ((576460745995190270L & l2) != 0) {
                                if (kind > 116) {
                                    kind = 116;
                                }
                                jjCheckNAddTwoStates(15, 16);
                                break;
                            } else if (this.curChar == 91) {
                                jjCheckNAddTwoStates(29, 30);
                                break;
                            } else if (this.curChar == 96) {
                                jjCheckNAddTwoStates(26, 27);
                                break;
                            }
                            break;
                        case 9:
                            jjCheckNAddTwoStates(9, 10);
                            break;
                        case 11:
                        case 12:
                            jjCheckNAddTwoStates(12, 10);
                            break;
                        case 15:
                            if ((576460745995190270L & l2) != 0) {
                                if (kind > 116) {
                                    kind = 116;
                                }
                                jjCheckNAddTwoStates(15, 16);
                                break;
                            }
                            break;
                        case 16:
                            if ((576460745995190271L & l2) != 0) {
                                if (kind > 116) {
                                    kind = 116;
                                }
                                jjCheckNAdd(16);
                                break;
                            }
                            break;
                        case 18:
                            jjCheckNAddTwoStates(18, 19);
                            break;
                        case 21:
                            jjCheckNAddTwoStates(21, 19);
                            break;
                        case 23:
                            jjAddStates(16, 17);
                            break;
                        case 25:
                            if (this.curChar == 96) {
                                jjCheckNAddTwoStates(26, 27);
                                break;
                            }
                            break;
                        case 26:
                            if (((-4294967297L) & l2) != 0) {
                                jjCheckNAddTwoStates(26, 27);
                                break;
                            }
                            break;
                        case 27:
                            if (this.curChar == 96 && kind > 120) {
                                kind = 120;
                                break;
                            }
                            break;
                        case 28:
                            if (this.curChar == 91) {
                                jjCheckNAddTwoStates(29, 30);
                                break;
                            }
                            break;
                        case 29:
                            if (((-536870913) & l2) != 0) {
                                jjCheckNAddTwoStates(29, 30);
                                break;
                            }
                            break;
                        case 30:
                            if (this.curChar == 93 && kind > 120) {
                                kind = 120;
                                break;
                            }
                            break;
                        case 35:
                            if ((137438953504L & l2) != 0) {
                                jjAddStates(18, 19);
                                break;
                            }
                            break;
                        case 39:
                            if ((137438953504L & l2) != 0) {
                                jjAddStates(20, 21);
                                break;
                            }
                            break;
                        case 43:
                            if ((576460745995190271L & l2) != 0) {
                                if (kind > 116) {
                                    kind = 116;
                                }
                                jjCheckNAdd(16);
                            }
                            if ((576460745995190270L & l2) != 0) {
                                if (kind > 116) {
                                    kind = 116;
                                }
                                jjCheckNAddTwoStates(15, 16);
                                break;
                            }
                            break;
                    }
                } while (i != startsAt);
            } else {
                int hiByte = this.curChar >> 8;
                int i1 = hiByte >> 6;
                long l1 = 1 << (hiByte & 63);
                int i22 = (this.curChar & 255) >> 6;
                long l22 = 1 << (this.curChar & 63);
                do {
                    i--;
                    switch (this.jjstateSet[i]) {
                        case 6:
                            if (jjCanMove_0(hiByte, i1, i22, l1, l22)) {
                                if (kind > 114) {
                                    kind = 114;
                                }
                                int[] iArr7 = this.jjstateSet;
                                int i9 = this.jjnewStateCnt;
                                this.jjnewStateCnt = i9 + 1;
                                iArr7[i9] = 6;
                                break;
                            }
                            break;
                        case 7:
                        case 15:
                            if (jjCanMove_1(hiByte, i1, i22, l1, l22)) {
                                if (kind > 116) {
                                    kind = 116;
                                }
                                jjCheckNAddTwoStates(15, 16);
                                break;
                            }
                            break;
                        case 8:
                        case 10:
                        case 13:
                        case 14:
                        case 17:
                        case 19:
                        case 20:
                        case 22:
                        case 24:
                        case 25:
                        case 27:
                        case 28:
                        case 30:
                        case 31:
                        case 32:
                        case 33:
                        case 34:
                        case 35:
                        case 36:
                        case 37:
                        case 38:
                        case 39:
                        case 40:
                        case 41:
                        case 42:
                        default:
                            if (i1 == 0 || l1 == 0 || i22 == 0 || l22 == 0) {
                            }
                            break;
                        case 9:
                            if (jjCanMove_0(hiByte, i1, i22, l1, l22)) {
                                jjCheckNAddTwoStates(9, 10);
                                break;
                            }
                            break;
                        case 11:
                        case 12:
                            if (jjCanMove_0(hiByte, i1, i22, l1, l22)) {
                                jjCheckNAddTwoStates(12, 10);
                                break;
                            }
                            break;
                        case 16:
                            if (jjCanMove_1(hiByte, i1, i22, l1, l22)) {
                                if (kind > 116) {
                                    kind = 116;
                                }
                                jjCheckNAdd(16);
                                break;
                            }
                            break;
                        case 18:
                            if (jjCanMove_0(hiByte, i1, i22, l1, l22)) {
                                jjCheckNAddTwoStates(18, 19);
                                break;
                            }
                            break;
                        case 21:
                            if (jjCanMove_0(hiByte, i1, i22, l1, l22)) {
                                jjCheckNAddTwoStates(21, 19);
                                break;
                            }
                            break;
                        case 23:
                            if (jjCanMove_0(hiByte, i1, i22, l1, l22)) {
                                jjAddStates(16, 17);
                                break;
                            }
                            break;
                        case 26:
                            if (jjCanMove_0(hiByte, i1, i22, l1, l22)) {
                                jjAddStates(10, 11);
                                break;
                            }
                            break;
                        case 29:
                            if (jjCanMove_0(hiByte, i1, i22, l1, l22)) {
                                jjAddStates(12, 13);
                                break;
                            }
                            break;
                        case 43:
                            if (jjCanMove_1(hiByte, i1, i22, l1, l22)) {
                                if (kind > 116) {
                                    kind = 116;
                                }
                                jjCheckNAddTwoStates(15, 16);
                            }
                            if (jjCanMove_1(hiByte, i1, i22, l1, l22)) {
                                if (kind > 116) {
                                    kind = 116;
                                }
                                jjCheckNAdd(16);
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
            int i10 = this.jjnewStateCnt;
            i = i10;
            int i11 = startsAt;
            this.jjnewStateCnt = i11;
            int i12 = 43 - i11;
            startsAt = i12;
            if (i10 == i12) {
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
                if ((jjbitVec3[i2] & l2) != 0) {
                }
                break;
        }
        return false;
    }

    protected Token jjFillToken() {
        String im = jjstrLiteralImages[this.jjmatchedKind];
        String curTokenImage = im == null ? this.input_stream.GetImage() : im;
        int beginLine = this.input_stream.getBeginLine();
        int beginColumn = this.input_stream.getBeginColumn();
        int endLine = this.input_stream.getEndLine();
        int endColumn = this.input_stream.getEndColumn();
        Token t = Token.newToken(this.jjmatchedKind, curTokenImage);
        t.beginLine = beginLine;
        t.endLine = endLine;
        t.beginColumn = beginColumn;
        t.endColumn = endColumn;
        return t;
    }

    public Token getNextToken() {
        int curPos;
        Token specialToken = null;
        while (true) {
            try {
                this.curChar = this.input_stream.BeginToken();
                try {
                    this.input_stream.backup(0);
                    while (this.curChar <= 32 && (4294977024L & (1 << this.curChar)) != 0) {
                        this.curChar = this.input_stream.BeginToken();
                    }
                    this.jjmatchedKind = Integer.MAX_VALUE;
                    this.jjmatchedPos = 0;
                    curPos = jjMoveStringLiteralDfa0_0();
                } catch (IOException e) {
                }
                if (this.jjmatchedKind != Integer.MAX_VALUE) {
                    if (this.jjmatchedPos + 1 < curPos) {
                        this.input_stream.backup((curPos - this.jjmatchedPos) - 1);
                    }
                    if ((jjtoToken[this.jjmatchedKind >> 6] & (1 << (this.jjmatchedKind & 63))) != 0) {
                        Token matchedToken = jjFillToken();
                        matchedToken.specialToken = specialToken;
                        return matchedToken;
                    }
                    if ((jjtoSpecial[this.jjmatchedKind >> 6] & (1 << (this.jjmatchedKind & 63))) != 0) {
                        Token matchedToken2 = jjFillToken();
                        if (specialToken == null) {
                            specialToken = matchedToken2;
                        } else {
                            matchedToken2.specialToken = specialToken;
                            specialToken.next = matchedToken2;
                            specialToken = matchedToken2;
                        }
                    }
                } else {
                    int error_line = this.input_stream.getEndLine();
                    int error_column = this.input_stream.getEndColumn();
                    String error_after = null;
                    boolean EOFSeen = false;
                    try {
                        this.input_stream.readChar();
                        this.input_stream.backup(1);
                    } catch (IOException e2) {
                        EOFSeen = true;
                        error_after = curPos <= 1 ? "" : this.input_stream.GetImage();
                        if (this.curChar == 10 || this.curChar == 13) {
                            error_line++;
                            error_column = 0;
                        } else {
                            error_column++;
                        }
                    }
                    if (!EOFSeen) {
                        this.input_stream.backup(1);
                        error_after = curPos <= 1 ? "" : this.input_stream.GetImage();
                    }
                    throw new TokenMgrError(EOFSeen, this.curLexState, error_line, error_column, error_after, this.curChar, 0);
                }
            } catch (Exception e3) {
                this.jjmatchedKind = 0;
                this.jjmatchedPos = -1;
                Token matchedToken3 = jjFillToken();
                matchedToken3.specialToken = specialToken;
                return matchedToken3;
            }
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

    public CCJSqlParserTokenManager(SimpleCharStream stream) {
        this.debugStream = System.out;
        this.curLexState = 0;
        this.defaultLexState = 0;
        this.jjrounds = new int[43];
        this.jjstateSet = new int[86];
        this.input_stream = stream;
    }

    public CCJSqlParserTokenManager(SimpleCharStream stream, int lexState) {
        this.debugStream = System.out;
        this.curLexState = 0;
        this.defaultLexState = 0;
        this.jjrounds = new int[43];
        this.jjstateSet = new int[86];
        ReInit(stream);
        SwitchTo(lexState);
    }

    public void ReInit(SimpleCharStream stream) {
        this.jjnewStateCnt = 0;
        this.jjmatchedPos = 0;
        this.curLexState = this.defaultLexState;
        this.input_stream = stream;
        ReInitRounds();
    }

    private void ReInitRounds() {
        this.jjround = ISourceLocation.NO_COLUMN;
        int i = 43;
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

    public void ReInit(SimpleCharStream stream, int lexState) {
        ReInit(stream);
        SwitchTo(lexState);
    }

    public void SwitchTo(int lexState) {
        if (lexState >= 1 || lexState < 0) {
            throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", 2);
        }
        this.curLexState = lexState;
    }
}
