package org.bouncycastle.pqc.math.linearalgebra;

import com.drew.metadata.exif.makernotes.LeicaMakernoteDirectory;
import com.drew.metadata.exif.makernotes.SanyoMakernoteDirectory;
import com.drew.metadata.iptc.IptcDirectory;
import com.itextpdf.io.codec.TIFFConstants;
import com.mysql.jdbc.MysqlErrorNumbers;
import java.math.BigInteger;
import java.security.SecureRandom;
import org.apache.ibatis.javassist.compiler.TokenId;
import org.apache.poi.ddf.EscherProperties;
import org.apache.poi.hssf.record.CFRuleRecord;
import org.apache.poi.hssf.record.MergeCellsRecord;
import org.apache.poi.hssf.record.ProtectionRev4Record;
import org.apache.poi.hssf.record.RefreshAllRecord;
import org.apache.poi.hssf.record.StyleRecord;
import org.apache.poi.hssf.record.UnknownRecord;
import org.aspectj.apache.bcel.Constants;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.util.BigIntegers;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/pqc/math/linearalgebra/IntegerFunctions.class */
public final class IntegerFunctions {
    private static final long SMALL_PRIME_PRODUCT = 152125131763605L;
    private static final BigInteger ZERO = BigInteger.valueOf(0);
    private static final BigInteger ONE = BigInteger.valueOf(1);
    private static final BigInteger TWO = BigInteger.valueOf(2);
    private static final BigInteger FOUR = BigInteger.valueOf(4);
    private static final int[] SMALL_PRIMES = {3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41};
    private static SecureRandom sr = null;
    private static final int[] jacobiTable = {0, 1, 0, -1, 0, -1, 0, 1};

    private IntegerFunctions() {
    }

    public static int jacobi(BigInteger bigInteger, BigInteger bigInteger2) {
        long j = 1;
        if (bigInteger2.equals(ZERO)) {
            return bigInteger.abs().equals(ONE) ? 1 : 0;
        }
        if (!bigInteger.testBit(0) && !bigInteger2.testBit(0)) {
            return 0;
        }
        BigInteger bigIntegerSubtract = bigInteger;
        BigInteger bigIntegerDivide = bigInteger2;
        if (bigIntegerDivide.signum() == -1) {
            bigIntegerDivide = bigIntegerDivide.negate();
            if (bigIntegerSubtract.signum() == -1) {
                j = -1;
            }
        }
        BigInteger bigIntegerAdd = ZERO;
        while (!bigIntegerDivide.testBit(0)) {
            bigIntegerAdd = bigIntegerAdd.add(ONE);
            bigIntegerDivide = bigIntegerDivide.divide(TWO);
        }
        if (bigIntegerAdd.testBit(0)) {
            j *= jacobiTable[bigIntegerSubtract.intValue() & 7];
        }
        if (bigIntegerSubtract.signum() < 0) {
            if (bigIntegerDivide.testBit(1)) {
                j = -j;
            }
            bigIntegerSubtract = bigIntegerSubtract.negate();
        }
        while (bigIntegerSubtract.signum() != 0) {
            BigInteger bigIntegerAdd2 = ZERO;
            while (!bigIntegerSubtract.testBit(0)) {
                bigIntegerAdd2 = bigIntegerAdd2.add(ONE);
                bigIntegerSubtract = bigIntegerSubtract.divide(TWO);
            }
            if (bigIntegerAdd2.testBit(0)) {
                j *= jacobiTable[bigIntegerDivide.intValue() & 7];
            }
            if (bigIntegerSubtract.compareTo(bigIntegerDivide) < 0) {
                BigInteger bigInteger3 = bigIntegerSubtract;
                bigIntegerSubtract = bigIntegerDivide;
                bigIntegerDivide = bigInteger3;
                if (bigIntegerSubtract.testBit(1) && bigIntegerDivide.testBit(1)) {
                    j = -j;
                }
            }
            bigIntegerSubtract = bigIntegerSubtract.subtract(bigIntegerDivide);
        }
        if (bigIntegerDivide.equals(ONE)) {
            return (int) j;
        }
        return 0;
    }

    public static BigInteger ressol(BigInteger bigInteger, BigInteger bigInteger2) throws IllegalArgumentException {
        BigInteger bigInteger3;
        if (bigInteger.compareTo(ZERO) < 0) {
            bigInteger = bigInteger.add(bigInteger2);
        }
        if (bigInteger.equals(ZERO)) {
            return ZERO;
        }
        if (bigInteger2.equals(TWO)) {
            return bigInteger;
        }
        if (bigInteger2.testBit(0) && bigInteger2.testBit(1)) {
            if (jacobi(bigInteger, bigInteger2) != 1) {
                throw new IllegalArgumentException("No quadratic residue: " + bigInteger + ", " + bigInteger2);
            }
            return bigInteger.modPow(bigInteger2.add(ONE).shiftRight(2), bigInteger2);
        }
        BigInteger bigIntegerSubtract = bigInteger2.subtract(ONE);
        long j = 0;
        while (!bigIntegerSubtract.testBit(0)) {
            j++;
            bigIntegerSubtract = bigIntegerSubtract.shiftRight(1);
        }
        BigInteger bigIntegerShiftRight = bigIntegerSubtract.subtract(ONE).shiftRight(1);
        BigInteger bigIntegerModPow = bigInteger.modPow(bigIntegerShiftRight, bigInteger2);
        BigInteger bigIntegerRemainder = bigIntegerModPow.multiply(bigIntegerModPow).remainder(bigInteger2).multiply(bigInteger).remainder(bigInteger2);
        BigInteger bigIntegerRemainder2 = bigIntegerModPow.multiply(bigInteger).remainder(bigInteger2);
        if (bigIntegerRemainder.equals(ONE)) {
            return bigIntegerRemainder2;
        }
        BigInteger bigIntegerAdd = TWO;
        while (true) {
            bigInteger3 = bigIntegerAdd;
            if (jacobi(bigInteger3, bigInteger2) != 1) {
                break;
            }
            bigIntegerAdd = bigInteger3.add(ONE);
        }
        BigInteger bigIntegerModPow2 = bigInteger3.modPow(bigIntegerShiftRight.multiply(TWO).add(ONE), bigInteger2);
        while (bigIntegerRemainder.compareTo(ONE) == 1) {
            BigInteger bigIntegerMod = bigIntegerRemainder;
            long j2 = j;
            long j3 = 0;
            while (true) {
                j = j3;
                if (bigIntegerMod.equals(ONE)) {
                    break;
                }
                bigIntegerMod = bigIntegerMod.multiply(bigIntegerMod).mod(bigInteger2);
                j3 = j + 1;
            }
            long j4 = j2 - j;
            if (j4 == 0) {
                throw new IllegalArgumentException("No quadratic residue: " + bigInteger + ", " + bigInteger2);
            }
            BigInteger bigIntegerShiftLeft = ONE;
            long j5 = 0;
            while (true) {
                long j6 = j5;
                if (j6 < j4 - 1) {
                    bigIntegerShiftLeft = bigIntegerShiftLeft.shiftLeft(1);
                    j5 = j6 + 1;
                }
            }
            BigInteger bigIntegerModPow3 = bigIntegerModPow2.modPow(bigIntegerShiftLeft, bigInteger2);
            bigIntegerRemainder2 = bigIntegerRemainder2.multiply(bigIntegerModPow3).remainder(bigInteger2);
            bigIntegerModPow2 = bigIntegerModPow3.multiply(bigIntegerModPow3).remainder(bigInteger2);
            bigIntegerRemainder = bigIntegerRemainder.multiply(bigIntegerModPow2).mod(bigInteger2);
        }
        return bigIntegerRemainder2;
    }

    public static int gcd(int i, int i2) {
        return BigInteger.valueOf(i).gcd(BigInteger.valueOf(i2)).intValue();
    }

    public static int[] extGCD(int i, int i2) {
        BigInteger[] bigIntegerArrExtgcd = extgcd(BigInteger.valueOf(i), BigInteger.valueOf(i2));
        return new int[]{bigIntegerArrExtgcd[0].intValue(), bigIntegerArrExtgcd[1].intValue(), bigIntegerArrExtgcd[2].intValue()};
    }

    public static BigInteger divideAndRound(BigInteger bigInteger, BigInteger bigInteger2) {
        return bigInteger.signum() < 0 ? divideAndRound(bigInteger.negate(), bigInteger2).negate() : bigInteger2.signum() < 0 ? divideAndRound(bigInteger, bigInteger2.negate()).negate() : bigInteger.shiftLeft(1).add(bigInteger2).divide(bigInteger2.shiftLeft(1));
    }

    public static BigInteger[] divideAndRound(BigInteger[] bigIntegerArr, BigInteger bigInteger) {
        BigInteger[] bigIntegerArr2 = new BigInteger[bigIntegerArr.length];
        for (int i = 0; i < bigIntegerArr.length; i++) {
            bigIntegerArr2[i] = divideAndRound(bigIntegerArr[i], bigInteger);
        }
        return bigIntegerArr2;
    }

    public static int ceilLog(BigInteger bigInteger) {
        int i = 0;
        BigInteger bigIntegerShiftLeft = ONE;
        while (true) {
            BigInteger bigInteger2 = bigIntegerShiftLeft;
            if (bigInteger2.compareTo(bigInteger) >= 0) {
                return i;
            }
            i++;
            bigIntegerShiftLeft = bigInteger2.shiftLeft(1);
        }
    }

    public static int ceilLog(int i) {
        int i2 = 0;
        int i3 = 1;
        while (i3 < i) {
            i3 <<= 1;
            i2++;
        }
        return i2;
    }

    public static int ceilLog256(int i) {
        if (i == 0) {
            return 1;
        }
        int i2 = 0;
        for (int i3 = i < 0 ? -i : i; i3 > 0; i3 >>>= 8) {
            i2++;
        }
        return i2;
    }

    public static int ceilLog256(long j) {
        if (j == 0) {
            return 1;
        }
        int i = 0;
        for (long j2 = j < 0 ? -j : j; j2 > 0; j2 >>>= 8) {
            i++;
        }
        return i;
    }

    public static int floorLog(BigInteger bigInteger) {
        int i = -1;
        BigInteger bigIntegerShiftLeft = ONE;
        while (true) {
            BigInteger bigInteger2 = bigIntegerShiftLeft;
            if (bigInteger2.compareTo(bigInteger) > 0) {
                return i;
            }
            i++;
            bigIntegerShiftLeft = bigInteger2.shiftLeft(1);
        }
    }

    public static int floorLog(int i) {
        int i2 = 0;
        if (i <= 0) {
            return -1;
        }
        int i3 = i;
        while (true) {
            int i4 = i3 >>> 1;
            if (i4 <= 0) {
                return i2;
            }
            i2++;
            i3 = i4;
        }
    }

    public static int maxPower(int i) {
        int i2 = 0;
        if (i != 0) {
            int i3 = 1;
            while (true) {
                int i4 = i3;
                if ((i & i4) != 0) {
                    break;
                }
                i2++;
                i3 = i4 << 1;
            }
        }
        return i2;
    }

    public static int bitCount(int i) {
        int i2 = 0;
        while (i != 0) {
            i2 += i & 1;
            i >>>= 1;
        }
        return i2;
    }

    public static int order(int i, int i2) {
        int i3 = i % i2;
        int i4 = 1;
        if (i3 == 0) {
            throw new IllegalArgumentException(i + " is not an element of Z/(" + i2 + "Z)^*; it is not meaningful to compute its order.");
        }
        while (i3 != 1) {
            i3 = (i3 * i) % i2;
            if (i3 < 0) {
                i3 += i2;
            }
            i4++;
        }
        return i4;
    }

    public static BigInteger reduceInto(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        return bigInteger.subtract(bigInteger2).mod(bigInteger3.subtract(bigInteger2)).add(bigInteger2);
    }

    public static int pow(int i, int i2) {
        int i3 = 1;
        while (i2 > 0) {
            if ((i2 & 1) == 1) {
                i3 *= i;
            }
            i *= i;
            i2 >>>= 1;
        }
        return i3;
    }

    public static long pow(long j, int i) {
        long j2 = 1;
        while (i > 0) {
            if ((i & 1) == 1) {
                j2 *= j;
            }
            j *= j;
            i >>>= 1;
        }
        return j2;
    }

    public static int modPow(int i, int i2, int i3) {
        if (i3 <= 0 || i3 * i3 > Integer.MAX_VALUE || i2 < 0) {
            return 0;
        }
        int i4 = 1;
        int i5 = ((i % i3) + i3) % i3;
        while (i2 > 0) {
            if ((i2 & 1) == 1) {
                i4 = (i4 * i5) % i3;
            }
            i5 = (i5 * i5) % i3;
            i2 >>>= 1;
        }
        return i4;
    }

    public static BigInteger[] extgcd(BigInteger bigInteger, BigInteger bigInteger2) {
        BigInteger bigInteger3 = ONE;
        BigInteger bigIntegerDivide = ZERO;
        BigInteger bigInteger4 = bigInteger;
        if (bigInteger2.signum() != 0) {
            BigInteger bigInteger5 = ZERO;
            BigInteger bigInteger6 = bigInteger2;
            while (true) {
                BigInteger bigInteger7 = bigInteger6;
                if (bigInteger7.signum() == 0) {
                    break;
                }
                BigInteger[] bigIntegerArrDivideAndRemainder = bigInteger4.divideAndRemainder(bigInteger7);
                BigInteger bigInteger8 = bigIntegerArrDivideAndRemainder[0];
                BigInteger bigInteger9 = bigIntegerArrDivideAndRemainder[1];
                BigInteger bigIntegerSubtract = bigInteger3.subtract(bigInteger8.multiply(bigInteger5));
                bigInteger3 = bigInteger5;
                bigInteger4 = bigInteger7;
                bigInteger5 = bigIntegerSubtract;
                bigInteger6 = bigInteger9;
            }
            bigIntegerDivide = bigInteger4.subtract(bigInteger.multiply(bigInteger3)).divide(bigInteger2);
        }
        return new BigInteger[]{bigInteger4, bigInteger3, bigIntegerDivide};
    }

    public static BigInteger leastCommonMultiple(BigInteger[] bigIntegerArr) {
        int length = bigIntegerArr.length;
        BigInteger bigIntegerDivide = bigIntegerArr[0];
        for (int i = 1; i < length; i++) {
            bigIntegerDivide = bigIntegerDivide.multiply(bigIntegerArr[i]).divide(bigIntegerDivide.gcd(bigIntegerArr[i]));
        }
        return bigIntegerDivide;
    }

    public static long mod(long j, long j2) {
        long j3 = j % j2;
        if (j3 < 0) {
            j3 += j2;
        }
        return j3;
    }

    public static int modInverse(int i, int i2) {
        return BigInteger.valueOf(i).modInverse(BigInteger.valueOf(i2)).intValue();
    }

    public static long modInverse(long j, long j2) {
        return BigInteger.valueOf(j).modInverse(BigInteger.valueOf(j2)).longValue();
    }

    public static int isPower(int i, int i2) {
        if (i <= 0) {
            return -1;
        }
        int i3 = 0;
        int i4 = i;
        while (i4 > 1) {
            if (i4 % i2 != 0) {
                return -1;
            }
            i4 /= i2;
            i3++;
        }
        return i3;
    }

    public static int leastDiv(int i) {
        if (i < 0) {
            i = -i;
        }
        if (i == 0) {
            return 1;
        }
        if ((i & 1) == 0) {
            return 2;
        }
        for (int i2 = 3; i2 <= i / i2; i2 += 2) {
            if (i % i2 == 0) {
                return i2;
            }
        }
        return i;
    }

    public static boolean isPrime(int i) {
        if (i < 2) {
            return false;
        }
        if (i == 2) {
            return true;
        }
        if ((i & 1) == 0) {
            return false;
        }
        if (i < 42) {
            for (int i2 = 0; i2 < SMALL_PRIMES.length; i2++) {
                if (i == SMALL_PRIMES[i2]) {
                    return true;
                }
            }
        }
        if (i % 3 == 0 || i % 5 == 0 || i % 7 == 0 || i % 11 == 0 || i % 13 == 0 || i % 17 == 0 || i % 19 == 0 || i % 23 == 0 || i % 29 == 0 || i % 31 == 0 || i % 37 == 0 || i % 41 == 0) {
            return false;
        }
        return BigInteger.valueOf(i).isProbablePrime(20);
    }

    public static boolean passesSmallPrimeTest(BigInteger bigInteger) {
        for (int i : new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, Constants.MULTIANEWARRAY_QUICK, 227, MergeCellsRecord.sid, UnknownRecord.BITMAP_00E9, UnknownRecord.PHONETICPR_00EF, EscherProperties.GEOTEXT__HASTEXTEFFECT, 251, 257, 263, 269, 271, 277, 281, 283, TIFFConstants.TIFFTAG_GROUP4OPTIONS, 307, TokenId.DO, 313, 317, 331, 337, 347, 349, 353, TokenId.GE, TokenId.RSHIFT_E, 373, EscherProperties.GEOMETRY__3DOK, 383, EscherProperties.FILL__CRMOD, EscherProperties.FILL__TOLEFT, 401, 409, 419, 421, ProtectionRev4Record.sid, CFRuleRecord.sid, RefreshAllRecord.sid, 443, 449, EscherProperties.LINESTYLE__FILLHEIGHT, EscherProperties.LINESTYLE__LINESTYLE, EscherProperties.LINESTYLE__LINEDASHSTYLE, EscherProperties.LINESTYLE__LINESTARTARROWLENGTH, 479, 487, 491, 499, 503, EscherProperties.LINESTYLE__HITLINETEST, 521, 523, SanyoMakernoteDirectory.TAG_LIGHT_SOURCE_SPECIAL, 547, IptcDirectory.TAG_REFERENCE_SERVICE, 563, 569, 571, 577, 587, 593, 599, 601, IptcDirectory.TAG_PROVINCE_OR_STATE, IptcDirectory.TAG_COUNTRY_OR_PRIMARY_LOCATION_NAME, IptcDirectory.TAG_HEADLINE, 619, 631, 641, IptcDirectory.TAG_IMAGE_ORIENTATION, IptcDirectory.TAG_LANGUAGE_IDENTIFIER, 653, StyleRecord.sid, EscherProperties.THREED__DIFFUSEAMOUNT, 673, 677, 683, 691, EscherProperties.THREED__METALLIC, EscherProperties.THREEDSTYLE__ROTATIONANGLE, EscherProperties.THREEDSTYLE__ORIGINY, EscherProperties.THREEDSTYLE__FILLX, 733, 739, 743, 751, 757, 761, 769, 773, LeicaMakernoteDirectory.TAG_APPROXIMATE_F_NUMBER, 797, 809, 811, 821, 823, EscherProperties.SHAPE__PREFERRELATIVERESIZE, 829, 839, 853, 857, 859, 863, 877, 881, 883, 887, 907, EscherProperties.GROUPSHAPE__POSH, EscherProperties.GROUPSHAPE__SCRIPTEXT, 929, EscherProperties.GROUPSHAPE__METROBLOB, 941, 947, EscherProperties.GROUPSHAPE__EDITEDWRAP, 967, 971, 977, 983, 991, 997, 1009, 1013, 1019, 1021, 1031, 1033, 1039, 1049, 1051, 1061, MysqlErrorNumbers.ER_WRONG_FIELD_SPEC, 1069, 1087, MysqlErrorNumbers.ER_CANT_DROP_FIELD_OR_KEY, MysqlErrorNumbers.ER_UPDATE_TABLE_USED, MysqlErrorNumbers.ER_TOO_BIG_SET, MysqlErrorNumbers.ER_WRONG_TABLE_NAME, MysqlErrorNumbers.ER_UNKNOWN_TABLE, MysqlErrorNumbers.ER_TOO_MANY_FIELDS, MysqlErrorNumbers.ER_CANT_INITIALIZE_UDF, MysqlErrorNumbers.ER_HOST_IS_BLOCKED, MysqlErrorNumbers.ER_TOO_MANY_DELAYED_THREADS, MysqlErrorNumbers.ER_NET_PACKET_TOO_LARGE, MysqlErrorNumbers.ER_TABLE_CANT_HANDLE_BLOB, MysqlErrorNumbers.ER_PRIMARY_CANT_HAVE_NULL, MysqlErrorNumbers.ER_ERROR_DURING_ROLLBACK, MysqlErrorNumbers.ER_INDEX_REBUILD, MysqlErrorNumbers.ER_UNKNOWN_SYSTEM_VARIABLE, 1201, MysqlErrorNumbers.ER_LOCK_DEADLOCK, MysqlErrorNumbers.ER_ROW_IS_REFERENCED, MysqlErrorNumbers.ER_CANT_UPDATE_WITH_READLOCK, MysqlErrorNumbers.ER_GLOBAL_VARIABLE, MysqlErrorNumbers.ER_WRONG_VALUE_FOR_VAR, MysqlErrorNumbers.ER_SLAVE_IGNORED_TABLE, MysqlErrorNumbers.ER_SELECT_REDUCED, MysqlErrorNumbers.ER_ZLIB_Z_DATA_ERROR, MysqlErrorNumbers.ER_BAD_SLAVE_UNTIL_COND, MysqlErrorNumbers.ER_UNTIL_COND_IGNORED, 1283, 1289, 1291, MysqlErrorNumbers.ER_GET_TEMPORARY_ERRMSG, MysqlErrorNumbers.ER_WARN_ALLOWED_PACKET_OVERFLOWED, MysqlErrorNumbers.ER_SP_NO_RECURSIVE_CREATE, MysqlErrorNumbers.ER_SP_STORE_FAILED, 1319, 1321, 1327, 1361, MysqlErrorNumbers.ER_ILLEGAL_VALUE_FOR_TYPE, MysqlErrorNumbers.ER_UNKNOWN_TARGET_BINLOG, MysqlErrorNumbers.ER_NO_BINARY_LOGGING, MysqlErrorNumbers.ER_XAER_RMFAIL, MysqlErrorNumbers.ER_LOAD_FROM_FIXED_SIZE_ROWS_TO_VAR, MysqlErrorNumbers.ER_NO_DEFAULT_FOR_VIEW_FIELD, MysqlErrorNumbers.ER_M_BIGGER_THAN_D, MysqlErrorNumbers.ER_CONNECT_TO_FOREIGN_DATA_SOURCE, MysqlErrorNumbers.ER_FOREIGN_DATA_STRING_INVALID, MysqlErrorNumbers.ER_TOO_BIG_DISPLAYWIDTH, MysqlErrorNumbers.ER_VIEW_FRM_NO_USER, MysqlErrorNumbers.ER_ROW_IS_REFERENCED_2, MysqlErrorNumbers.ER_SP_BAD_VAR_SHADOW, MysqlErrorNumbers.ER_TABLE_NEEDS_UPGRADE, 1471, MysqlErrorNumbers.ER_PARTITION_MAXVALUE_ERROR, MysqlErrorNumbers.ER_PARTITION_SUBPART_MIX_ERROR, MysqlErrorNumbers.ER_NO_CONST_EXPR_IN_RANGE_OR_LIST_ERROR, MysqlErrorNumbers.ER_LIST_OF_FIELDS_ONLY_IN_HASH_ERROR, MysqlErrorNumbers.ER_RANGE_NOT_INCREASING_ERROR, MysqlErrorNumbers.ER_TOO_MANY_PARTITIONS_ERROR}) {
            if (bigInteger.mod(BigInteger.valueOf(i)).equals(ZERO)) {
                return false;
            }
        }
        return true;
    }

    public static int nextSmallerPrime(int i) {
        if (i <= 2) {
            return 1;
        }
        if (i == 3) {
            return 2;
        }
        int i2 = (i & 1) == 0 ? i - 1 : i - 2;
        while (i2 > 3 && !isPrime(i2)) {
            i2 -= 2;
        }
        return i2;
    }

    public static BigInteger nextProbablePrime(BigInteger bigInteger, int i) {
        if (bigInteger.signum() < 0 || bigInteger.signum() == 0 || bigInteger.equals(ONE)) {
            return TWO;
        }
        BigInteger bigIntegerAdd = bigInteger.add(ONE);
        if (!bigIntegerAdd.testBit(0)) {
            bigIntegerAdd = bigIntegerAdd.add(ONE);
        }
        while (true) {
            if (bigIntegerAdd.bitLength() > 6) {
                long jLongValue = bigIntegerAdd.remainder(BigInteger.valueOf(SMALL_PRIME_PRODUCT)).longValue();
                if (jLongValue % 3 == 0 || jLongValue % 5 == 0 || jLongValue % 7 == 0 || jLongValue % 11 == 0 || jLongValue % 13 == 0 || jLongValue % 17 == 0 || jLongValue % 19 == 0 || jLongValue % 23 == 0 || jLongValue % 29 == 0 || jLongValue % 31 == 0 || jLongValue % 37 == 0 || jLongValue % 41 == 0) {
                    bigIntegerAdd = bigIntegerAdd.add(TWO);
                }
            }
            if (bigIntegerAdd.bitLength() >= 4 && !bigIntegerAdd.isProbablePrime(i)) {
                bigIntegerAdd = bigIntegerAdd.add(TWO);
            }
            return bigIntegerAdd;
        }
    }

    public static BigInteger nextProbablePrime(BigInteger bigInteger) {
        return nextProbablePrime(bigInteger, 20);
    }

    public static BigInteger nextPrime(long j) {
        boolean z;
        boolean z2 = false;
        long j2 = 0;
        if (j <= 1) {
            return BigInteger.valueOf(2L);
        }
        if (j == 2) {
            return BigInteger.valueOf(3L);
        }
        long j3 = j + 1;
        long j4 = j & 1;
        while (true) {
            long j5 = j3 + j4;
            if (j5 > (j << 1) || z2) {
                break;
            }
            long j6 = 3;
            while (true) {
                long j7 = j6;
                if (j7 > (j5 >> 1) || z2) {
                    break;
                }
                if (j5 % j7 == 0) {
                    z2 = true;
                }
                j6 = j7 + 2;
            }
            if (z2) {
                z = false;
            } else {
                j2 = j5;
                z = true;
            }
            z2 = z;
            j3 = j5;
            j4 = 2;
        }
        return BigInteger.valueOf(j2);
    }

    public static BigInteger binomial(int i, int i2) {
        BigInteger bigIntegerDivide = ONE;
        if (i == 0) {
            return i2 == 0 ? bigIntegerDivide : ZERO;
        }
        if (i2 > (i >>> 1)) {
            i2 = i - i2;
        }
        for (int i3 = 1; i3 <= i2; i3++) {
            bigIntegerDivide = bigIntegerDivide.multiply(BigInteger.valueOf(i - (i3 - 1))).divide(BigInteger.valueOf(i3));
        }
        return bigIntegerDivide;
    }

    public static BigInteger randomize(BigInteger bigInteger) {
        if (sr == null) {
            sr = CryptoServicesRegistrar.getSecureRandom();
        }
        return randomize(bigInteger, sr);
    }

    public static BigInteger randomize(BigInteger bigInteger, SecureRandom secureRandom) {
        int iBitLength = bigInteger.bitLength();
        BigInteger bigIntegerValueOf = BigInteger.valueOf(0L);
        if (secureRandom == null) {
            secureRandom = sr != null ? sr : CryptoServicesRegistrar.getSecureRandom();
        }
        for (int i = 0; i < 20; i++) {
            bigIntegerValueOf = BigIntegers.createRandomBigInteger(iBitLength, secureRandom);
            if (bigIntegerValueOf.compareTo(bigInteger) < 0) {
                return bigIntegerValueOf;
            }
        }
        return bigIntegerValueOf.mod(bigInteger);
    }

    public static BigInteger squareRoot(BigInteger bigInteger) {
        if (bigInteger.compareTo(ZERO) < 0) {
            throw new ArithmeticException("cannot extract root of negative number" + bigInteger + ".");
        }
        int iBitLength = bigInteger.bitLength();
        BigInteger bigIntegerMultiply = ZERO;
        BigInteger bigIntegerAdd = ZERO;
        if ((iBitLength & 1) != 0) {
            bigIntegerMultiply = bigIntegerMultiply.add(ONE);
            iBitLength--;
        }
        while (iBitLength > 0) {
            BigInteger bigIntegerMultiply2 = bigIntegerAdd.multiply(FOUR);
            int i = iBitLength - 1;
            int i2 = bigInteger.testBit(i) ? 2 : 0;
            iBitLength = i - 1;
            bigIntegerAdd = bigIntegerMultiply2.add(BigInteger.valueOf(i2 + (bigInteger.testBit(iBitLength) ? 1 : 0)));
            BigInteger bigIntegerAdd2 = bigIntegerMultiply.multiply(FOUR).add(ONE);
            bigIntegerMultiply = bigIntegerMultiply.multiply(TWO);
            if (bigIntegerAdd.compareTo(bigIntegerAdd2) != -1) {
                bigIntegerMultiply = bigIntegerMultiply.add(ONE);
                bigIntegerAdd = bigIntegerAdd.subtract(bigIntegerAdd2);
            }
        }
        return bigIntegerMultiply;
    }

    public static float intRoot(int i, int i2) {
        float f;
        float fFloatPow = i / i2;
        float f2 = 0.0f;
        int i3 = 0;
        while (Math.abs(f2 - fFloatPow) > 1.0E-4d) {
            float fFloatPow2 = floatPow(fFloatPow, i2);
            while (true) {
                f = fFloatPow2;
                if (Float.isInfinite(f)) {
                    fFloatPow = (fFloatPow + f2) / 2.0f;
                    fFloatPow2 = floatPow(fFloatPow, i2);
                }
            }
            i3++;
            f2 = fFloatPow;
            fFloatPow = f2 - ((f - i) / (i2 * floatPow(f2, i2 - 1)));
        }
        return fFloatPow;
    }

    public static float floatPow(float f, int i) {
        float f2 = 1.0f;
        while (i > 0) {
            f2 *= f;
            i--;
        }
        return f2;
    }

    public static double log(double d) {
        if (d > 0.0d && d < 1.0d) {
            return -log(1.0d / d);
        }
        int i = 0;
        double d2 = 1.0d;
        double d3 = d;
        while (d3 > 2.0d) {
            d3 /= 2.0d;
            i++;
            d2 *= 2.0d;
        }
        return i + logBKM(d / d2);
    }

    public static double log(long j) {
        return floorLog(BigInteger.valueOf(j)) + logBKM(j / (1 << r0));
    }

    private static double logBKM(double d) {
        double[] dArr = {1.0d, 0.5849625007211562d, 0.32192809488736235d, 0.16992500144231237d, 0.0874628412503394d, 0.044394119358453436d, 0.02236781302845451d, 0.01122725542325412d, 0.005624549193878107d, 0.0028150156070540383d, 0.0014081943928083889d, 7.042690112466433E-4d, 3.5217748030102726E-4d, 1.7609948644250602E-4d, 8.80524301221769E-5d, 4.4026886827316716E-5d, 2.2013611360340496E-5d, 1.1006847667481442E-5d, 5.503434330648604E-6d, 2.751719789561283E-6d, 1.375860550841138E-6d, 6.879304394358497E-7d, 3.4396526072176454E-7d, 1.7198264061184464E-7d, 8.599132286866321E-8d, 4.299566207501687E-8d, 2.1497831197679756E-8d, 1.0748915638882709E-8d, 5.374457829452062E-9d, 2.687228917228708E-9d, 1.3436144592400231E-9d, 6.718072297764289E-10d, 3.3590361492731876E-10d, 1.6795180747343547E-10d, 8.397590373916176E-11d, 4.1987951870191886E-11d, 2.0993975935248694E-11d, 1.0496987967662534E-11d, 5.2484939838408146E-12d, 2.624246991922794E-12d, 1.3121234959619935E-12d, 6.56061747981146E-13d, 3.2803087399061026E-13d, 1.6401543699531447E-13d, 8.200771849765956E-14d, 4.1003859248830365E-14d, 2.0501929624415328E-14d, 1.02509648122077E-14d, 5.1254824061038595E-15d, 2.5627412030519317E-15d, 1.2813706015259665E-15d, 6.406853007629834E-16d, 3.203426503814917E-16d, 1.6017132519074588E-16d, 8.008566259537294E-17d, 4.004283129768647E-17d, 2.0021415648843235E-17d, 1.0010707824421618E-17d, 5.005353912210809E-18d, 2.5026769561054044E-18d, 1.2513384780527022E-18d, 6.256692390263511E-19d, 3.1283461951317555E-19d, 1.5641730975658778E-19d, 7.820865487829389E-20d, 3.9104327439146944E-20d, 1.9552163719573472E-20d, 9.776081859786736E-21d, 4.888040929893368E-21d, 2.444020464946684E-21d, 1.222010232473342E-21d, 6.11005116236671E-22d, 3.055025581183355E-22d, 1.5275127905916775E-22d, 7.637563952958387E-23d, 3.818781976479194E-23d, 1.909390988239597E-23d, 9.546954941197984E-24d, 4.773477470598992E-24d, 2.386738735299496E-24d, 1.193369367649748E-24d, 5.96684683824874E-25d, 2.98342341912437E-25d, 1.491711709562185E-25d, 7.458558547810925E-26d, 3.7292792739054626E-26d, 1.8646396369527313E-26d, 9.323198184763657E-27d, 4.661599092381828E-27d, 2.330799546190914E-27d, 1.165399773095457E-27d, 5.826998865477285E-28d, 2.9134994327386427E-28d, 1.4567497163693213E-28d, 7.283748581846607E-29d, 3.6418742909233034E-29d, 1.8209371454616517E-29d, 9.104685727308258E-30d, 4.552342863654129E-30d, 2.2761714318270646E-30d};
        double d2 = 1.0d;
        double d3 = 0.0d;
        double d4 = 1.0d;
        for (int i = 0; i < 53; i++) {
            double d5 = d2 + (d2 * d4);
            if (d5 <= d) {
                d2 = d5;
                d3 += dArr[i];
            }
            d4 *= 0.5d;
        }
        return d3;
    }

    public static boolean isIncreasing(int[] iArr) {
        for (int i = 1; i < iArr.length; i++) {
            if (iArr[i - 1] >= iArr[i]) {
                System.out.println("a[" + (i - 1) + "] = " + iArr[i - 1] + " >= " + iArr[i] + " = a[" + i + "]");
                return false;
            }
        }
        return true;
    }

    public static byte[] integerToOctets(BigInteger bigInteger) {
        byte[] byteArray = bigInteger.abs().toByteArray();
        if ((bigInteger.bitLength() & 7) != 0) {
            return byteArray;
        }
        byte[] bArr = new byte[bigInteger.bitLength() >> 3];
        System.arraycopy(byteArray, 1, bArr, 0, bArr.length);
        return bArr;
    }

    public static BigInteger octetsToInteger(byte[] bArr, int i, int i2) {
        byte[] bArr2 = new byte[i2 + 1];
        bArr2[0] = 0;
        System.arraycopy(bArr, i, bArr2, 1, i2);
        return new BigInteger(bArr2);
    }

    public static BigInteger octetsToInteger(byte[] bArr) {
        return octetsToInteger(bArr, 0, bArr.length);
    }
}
