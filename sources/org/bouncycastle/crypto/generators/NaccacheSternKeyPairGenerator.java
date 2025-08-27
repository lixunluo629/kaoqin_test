package org.bouncycastle.crypto.generators;

import com.drew.metadata.exif.makernotes.SanyoMakernoteDirectory;
import com.drew.metadata.iptc.IptcDirectory;
import com.itextpdf.io.codec.TIFFConstants;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Vector;
import org.apache.ibatis.javassist.compiler.TokenId;
import org.apache.poi.ddf.EscherProperties;
import org.apache.poi.hssf.record.CFRuleRecord;
import org.apache.poi.hssf.record.MergeCellsRecord;
import org.apache.poi.hssf.record.ProtectionRev4Record;
import org.apache.poi.hssf.record.RefreshAllRecord;
import org.apache.poi.hssf.record.UnknownRecord;
import org.aspectj.apache.bcel.Constants;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.NaccacheSternKeyGenerationParameters;
import org.bouncycastle.crypto.params.NaccacheSternKeyParameters;
import org.bouncycastle.crypto.params.NaccacheSternPrivateKeyParameters;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/generators/NaccacheSternKeyPairGenerator.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/generators/NaccacheSternKeyPairGenerator.class */
public class NaccacheSternKeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private NaccacheSternKeyGenerationParameters param;
    private static int[] smallPrimes = {3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, Constants.MULTIANEWARRAY_QUICK, 227, MergeCellsRecord.sid, UnknownRecord.BITMAP_00E9, UnknownRecord.PHONETICPR_00EF, EscherProperties.GEOTEXT__HASTEXTEFFECT, 251, 257, 263, 269, 271, 277, 281, 283, TIFFConstants.TIFFTAG_GROUP4OPTIONS, 307, TokenId.DO, 313, 317, 331, 337, 347, 349, 353, TokenId.GE, TokenId.RSHIFT_E, 373, EscherProperties.GEOMETRY__3DOK, 383, EscherProperties.FILL__CRMOD, EscherProperties.FILL__TOLEFT, 401, 409, 419, 421, ProtectionRev4Record.sid, CFRuleRecord.sid, RefreshAllRecord.sid, 443, 449, EscherProperties.LINESTYLE__FILLHEIGHT, EscherProperties.LINESTYLE__LINESTYLE, EscherProperties.LINESTYLE__LINEDASHSTYLE, EscherProperties.LINESTYLE__LINESTARTARROWLENGTH, 479, 487, 491, 499, 503, EscherProperties.LINESTYLE__HITLINETEST, 521, 523, SanyoMakernoteDirectory.TAG_LIGHT_SOURCE_SPECIAL, 547, IptcDirectory.TAG_REFERENCE_SERVICE};
    private static final BigInteger ONE = BigInteger.valueOf(1);

    @Override // org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator
    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.param = (NaccacheSternKeyGenerationParameters) keyGenerationParameters;
    }

    @Override // org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator
    public AsymmetricCipherKeyPair generateKeyPair() {
        BigInteger bigIntegerGeneratePrime;
        BigInteger bigIntegerAdd;
        BigInteger bigIntegerGeneratePrime2;
        BigInteger bigIntegerAdd2;
        BigInteger bigIntegerMod;
        BigInteger bigInteger;
        int strength = this.param.getStrength();
        SecureRandom random = this.param.getRandom();
        int certainty = this.param.getCertainty();
        boolean zIsDebug = this.param.isDebug();
        if (zIsDebug) {
            System.out.println("Fetching first " + this.param.getCntSmallPrimes() + " primes.");
        }
        Vector vectorPermuteList = permuteList(findFirstPrimes(this.param.getCntSmallPrimes()), random);
        BigInteger bigIntegerMultiply = ONE;
        BigInteger bigIntegerMultiply2 = ONE;
        for (int i = 0; i < vectorPermuteList.size() / 2; i++) {
            bigIntegerMultiply = bigIntegerMultiply.multiply((BigInteger) vectorPermuteList.elementAt(i));
        }
        for (int size = vectorPermuteList.size() / 2; size < vectorPermuteList.size(); size++) {
            bigIntegerMultiply2 = bigIntegerMultiply2.multiply((BigInteger) vectorPermuteList.elementAt(size));
        }
        BigInteger bigIntegerMultiply3 = bigIntegerMultiply.multiply(bigIntegerMultiply2);
        int iBitLength = (strength - bigIntegerMultiply3.bitLength()) - 48;
        BigInteger bigIntegerGeneratePrime3 = generatePrime((iBitLength / 2) + 1, certainty, random);
        BigInteger bigIntegerGeneratePrime4 = generatePrime((iBitLength / 2) + 1, certainty, random);
        long j = 0;
        if (zIsDebug) {
            System.out.println("generating p and q");
        }
        BigInteger bigIntegerShiftLeft = bigIntegerGeneratePrime3.multiply(bigIntegerMultiply).shiftLeft(1);
        BigInteger bigIntegerShiftLeft2 = bigIntegerGeneratePrime4.multiply(bigIntegerMultiply2).shiftLeft(1);
        while (true) {
            j++;
            bigIntegerGeneratePrime = generatePrime(24, certainty, random);
            bigIntegerAdd = bigIntegerGeneratePrime.multiply(bigIntegerShiftLeft).add(ONE);
            if (bigIntegerAdd.isProbablePrime(certainty)) {
                while (true) {
                    bigIntegerGeneratePrime2 = generatePrime(24, certainty, random);
                    if (!bigIntegerGeneratePrime.equals(bigIntegerGeneratePrime2)) {
                        bigIntegerAdd2 = bigIntegerGeneratePrime2.multiply(bigIntegerShiftLeft2).add(ONE);
                        if (bigIntegerAdd2.isProbablePrime(certainty)) {
                            break;
                        }
                    }
                }
                if (!bigIntegerMultiply3.gcd(bigIntegerGeneratePrime.multiply(bigIntegerGeneratePrime2)).equals(ONE)) {
                    continue;
                } else {
                    if (bigIntegerAdd.multiply(bigIntegerAdd2).bitLength() >= strength) {
                        break;
                    }
                    if (zIsDebug) {
                        System.out.println("key size too small. Should be " + strength + " but is actually " + bigIntegerAdd.multiply(bigIntegerAdd2).bitLength());
                    }
                }
            }
        }
        if (zIsDebug) {
            System.out.println("needed " + j + " tries to generate p and q.");
        }
        BigInteger bigIntegerMultiply4 = bigIntegerAdd.multiply(bigIntegerAdd2);
        BigInteger bigIntegerMultiply5 = bigIntegerAdd.subtract(ONE).multiply(bigIntegerAdd2.subtract(ONE));
        long j2 = 0;
        if (zIsDebug) {
            System.out.println("generating g");
        }
        while (true) {
            Vector vector = new Vector();
            for (int i2 = 0; i2 != vectorPermuteList.size(); i2++) {
                BigInteger bigIntegerDivide = bigIntegerMultiply5.divide((BigInteger) vectorPermuteList.elementAt(i2));
                do {
                    j2++;
                    bigInteger = new BigInteger(strength, certainty, random);
                } while (bigInteger.modPow(bigIntegerDivide, bigIntegerMultiply4).equals(ONE));
                vector.addElement(bigInteger);
            }
            bigIntegerMod = ONE;
            for (int i3 = 0; i3 < vectorPermuteList.size(); i3++) {
                bigIntegerMod = bigIntegerMod.multiply(((BigInteger) vector.elementAt(i3)).modPow(bigIntegerMultiply3.divide((BigInteger) vectorPermuteList.elementAt(i3)), bigIntegerMultiply4)).mod(bigIntegerMultiply4);
            }
            boolean z = false;
            int i4 = 0;
            while (true) {
                if (i4 >= vectorPermuteList.size()) {
                    break;
                }
                if (bigIntegerMod.modPow(bigIntegerMultiply5.divide((BigInteger) vectorPermuteList.elementAt(i4)), bigIntegerMultiply4).equals(ONE)) {
                    if (zIsDebug) {
                        System.out.println("g has order phi(n)/" + vectorPermuteList.elementAt(i4) + "\n g: " + bigIntegerMod);
                    }
                    z = true;
                } else {
                    i4++;
                }
            }
            if (!z) {
                if (!bigIntegerMod.modPow(bigIntegerMultiply5.divide(BigInteger.valueOf(4L)), bigIntegerMultiply4).equals(ONE)) {
                    if (!bigIntegerMod.modPow(bigIntegerMultiply5.divide(bigIntegerGeneratePrime), bigIntegerMultiply4).equals(ONE)) {
                        if (!bigIntegerMod.modPow(bigIntegerMultiply5.divide(bigIntegerGeneratePrime2), bigIntegerMultiply4).equals(ONE)) {
                            if (!bigIntegerMod.modPow(bigIntegerMultiply5.divide(bigIntegerGeneratePrime3), bigIntegerMultiply4).equals(ONE)) {
                                if (!bigIntegerMod.modPow(bigIntegerMultiply5.divide(bigIntegerGeneratePrime4), bigIntegerMultiply4).equals(ONE)) {
                                    break;
                                }
                                if (zIsDebug) {
                                    System.out.println("g has order phi(n)/b\n g: " + bigIntegerMod);
                                }
                            } else if (zIsDebug) {
                                System.out.println("g has order phi(n)/a\n g: " + bigIntegerMod);
                            }
                        } else if (zIsDebug) {
                            System.out.println("g has order phi(n)/q'\n g: " + bigIntegerMod);
                        }
                    } else if (zIsDebug) {
                        System.out.println("g has order phi(n)/p'\n g: " + bigIntegerMod);
                    }
                } else if (zIsDebug) {
                    System.out.println("g has order phi(n)/4\n g:" + bigIntegerMod);
                }
            }
        }
        if (zIsDebug) {
            System.out.println("needed " + j2 + " tries to generate g");
            System.out.println();
            System.out.println("found new NaccacheStern cipher variables:");
            System.out.println("smallPrimes: " + vectorPermuteList);
            System.out.println("sigma:...... " + bigIntegerMultiply3 + " (" + bigIntegerMultiply3.bitLength() + " bits)");
            System.out.println("a:.......... " + bigIntegerGeneratePrime3);
            System.out.println("b:.......... " + bigIntegerGeneratePrime4);
            System.out.println("p':......... " + bigIntegerGeneratePrime);
            System.out.println("q':......... " + bigIntegerGeneratePrime2);
            System.out.println("p:.......... " + bigIntegerAdd);
            System.out.println("q:.......... " + bigIntegerAdd2);
            System.out.println("n:.......... " + bigIntegerMultiply4);
            System.out.println("phi(n):..... " + bigIntegerMultiply5);
            System.out.println("g:.......... " + bigIntegerMod);
            System.out.println();
        }
        return new AsymmetricCipherKeyPair(new NaccacheSternKeyParameters(false, bigIntegerMod, bigIntegerMultiply4, bigIntegerMultiply3.bitLength()), new NaccacheSternPrivateKeyParameters(bigIntegerMod, bigIntegerMultiply4, bigIntegerMultiply3.bitLength(), vectorPermuteList, bigIntegerMultiply5));
    }

    private static BigInteger generatePrime(int i, int i2, SecureRandom secureRandom) {
        BigInteger bigInteger = new BigInteger(i, i2, secureRandom);
        while (true) {
            BigInteger bigInteger2 = bigInteger;
            if (bigInteger2.bitLength() == i) {
                return bigInteger2;
            }
            bigInteger = new BigInteger(i, i2, secureRandom);
        }
    }

    private static Vector permuteList(Vector vector, SecureRandom secureRandom) {
        Vector vector2 = new Vector();
        Vector vector3 = new Vector();
        for (int i = 0; i < vector.size(); i++) {
            vector3.addElement(vector.elementAt(i));
        }
        vector2.addElement(vector3.elementAt(0));
        vector3.removeElementAt(0);
        while (vector3.size() != 0) {
            vector2.insertElementAt(vector3.elementAt(0), getInt(secureRandom, vector2.size() + 1));
            vector3.removeElementAt(0);
        }
        return vector2;
    }

    private static int getInt(SecureRandom secureRandom, int i) {
        int iNextInt;
        int i2;
        if ((i & (-i)) == i) {
            return (int) ((i * (secureRandom.nextInt() & Integer.MAX_VALUE)) >> 31);
        }
        do {
            iNextInt = secureRandom.nextInt() & Integer.MAX_VALUE;
            i2 = iNextInt % i;
        } while ((iNextInt - i2) + (i - 1) < 0);
        return i2;
    }

    private static Vector findFirstPrimes(int i) {
        Vector vector = new Vector(i);
        for (int i2 = 0; i2 != i; i2++) {
            vector.addElement(BigInteger.valueOf(smallPrimes[i2]));
        }
        return vector;
    }
}
