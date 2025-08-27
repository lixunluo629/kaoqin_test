package org.bouncycastle.jce.provider;

import java.security.AlgorithmParameterGeneratorSpi;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.DSAParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import javax.crypto.spec.DHGenParameterSpec;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;
import org.bouncycastle.crypto.generators.DHParametersGenerator;
import org.bouncycastle.crypto.generators.DSAParametersGenerator;
import org.bouncycastle.crypto.generators.ElGamalParametersGenerator;
import org.bouncycastle.crypto.generators.GOST3410ParametersGenerator;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.GOST3410Parameters;
import org.bouncycastle.jce.spec.GOST3410ParameterSpec;
import org.bouncycastle.jce.spec.GOST3410PublicKeyParameterSetSpec;

/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/JDKAlgorithmParameterGenerator.class */
public abstract class JDKAlgorithmParameterGenerator extends AlgorithmParameterGeneratorSpi {
    protected SecureRandom random;
    protected int strength = 1024;

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/JDKAlgorithmParameterGenerator$DES.class */
    public static class DES extends JDKAlgorithmParameterGenerator {
        @Override // java.security.AlgorithmParameterGeneratorSpi
        protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for DES parameter generation.");
        }

        @Override // java.security.AlgorithmParameterGeneratorSpi
        protected AlgorithmParameters engineGenerateParameters() throws NoSuchAlgorithmException, InvalidParameterSpecException, NoSuchProviderException {
            byte[] bArr = new byte[8];
            if (this.random == null) {
                this.random = new SecureRandom();
            }
            this.random.nextBytes(bArr);
            try {
                AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("DES", BouncyCastleProvider.PROVIDER_NAME);
                algorithmParameters.init(new IvParameterSpec(bArr));
                return algorithmParameters;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/JDKAlgorithmParameterGenerator$DH.class */
    public static class DH extends JDKAlgorithmParameterGenerator {
        private int l = 0;

        @Override // java.security.AlgorithmParameterGeneratorSpi
        protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            if (!(algorithmParameterSpec instanceof DHGenParameterSpec)) {
                throw new InvalidAlgorithmParameterException("DH parameter generator requires a DHGenParameterSpec for initialisation");
            }
            DHGenParameterSpec dHGenParameterSpec = (DHGenParameterSpec) algorithmParameterSpec;
            this.strength = dHGenParameterSpec.getPrimeSize();
            this.l = dHGenParameterSpec.getExponentSize();
            this.random = secureRandom;
        }

        @Override // java.security.AlgorithmParameterGeneratorSpi
        protected AlgorithmParameters engineGenerateParameters() throws NoSuchAlgorithmException, InvalidParameterSpecException, NoSuchProviderException {
            DHParametersGenerator dHParametersGenerator = new DHParametersGenerator();
            if (this.random != null) {
                dHParametersGenerator.init(this.strength, 20, this.random);
            } else {
                dHParametersGenerator.init(this.strength, 20, new SecureRandom());
            }
            DHParameters dHParametersGenerateParameters = dHParametersGenerator.generateParameters();
            try {
                AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("DH", BouncyCastleProvider.PROVIDER_NAME);
                algorithmParameters.init(new DHParameterSpec(dHParametersGenerateParameters.getP(), dHParametersGenerateParameters.getG(), this.l));
                return algorithmParameters;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/JDKAlgorithmParameterGenerator$DSA.class */
    public static class DSA extends JDKAlgorithmParameterGenerator {
        @Override // org.bouncycastle.jce.provider.JDKAlgorithmParameterGenerator, java.security.AlgorithmParameterGeneratorSpi
        protected void engineInit(int i, SecureRandom secureRandom) {
            if (i < 512 || i > 1024 || i % 64 != 0) {
                throw new InvalidParameterException("strength must be from 512 - 1024 and a multiple of 64");
            }
            this.strength = i;
            this.random = secureRandom;
        }

        @Override // java.security.AlgorithmParameterGeneratorSpi
        protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for DSA parameter generation.");
        }

        @Override // java.security.AlgorithmParameterGeneratorSpi
        protected AlgorithmParameters engineGenerateParameters() throws NoSuchAlgorithmException, InvalidParameterSpecException, NoSuchProviderException {
            DSAParametersGenerator dSAParametersGenerator = new DSAParametersGenerator();
            if (this.random != null) {
                dSAParametersGenerator.init(this.strength, 20, this.random);
            } else {
                dSAParametersGenerator.init(this.strength, 20, new SecureRandom());
            }
            DSAParameters dSAParametersGenerateParameters = dSAParametersGenerator.generateParameters();
            try {
                AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("DSA", BouncyCastleProvider.PROVIDER_NAME);
                algorithmParameters.init(new DSAParameterSpec(dSAParametersGenerateParameters.getP(), dSAParametersGenerateParameters.getQ(), dSAParametersGenerateParameters.getG()));
                return algorithmParameters;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/JDKAlgorithmParameterGenerator$ElGamal.class */
    public static class ElGamal extends JDKAlgorithmParameterGenerator {
        private int l = 0;

        @Override // java.security.AlgorithmParameterGeneratorSpi
        protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            if (!(algorithmParameterSpec instanceof DHGenParameterSpec)) {
                throw new InvalidAlgorithmParameterException("DH parameter generator requires a DHGenParameterSpec for initialisation");
            }
            DHGenParameterSpec dHGenParameterSpec = (DHGenParameterSpec) algorithmParameterSpec;
            this.strength = dHGenParameterSpec.getPrimeSize();
            this.l = dHGenParameterSpec.getExponentSize();
            this.random = secureRandom;
        }

        @Override // java.security.AlgorithmParameterGeneratorSpi
        protected AlgorithmParameters engineGenerateParameters() throws NoSuchAlgorithmException, InvalidParameterSpecException, NoSuchProviderException {
            ElGamalParametersGenerator elGamalParametersGenerator = new ElGamalParametersGenerator();
            if (this.random != null) {
                elGamalParametersGenerator.init(this.strength, 20, this.random);
            } else {
                elGamalParametersGenerator.init(this.strength, 20, new SecureRandom());
            }
            ElGamalParameters elGamalParametersGenerateParameters = elGamalParametersGenerator.generateParameters();
            try {
                AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("ElGamal", BouncyCastleProvider.PROVIDER_NAME);
                algorithmParameters.init(new DHParameterSpec(elGamalParametersGenerateParameters.getP(), elGamalParametersGenerateParameters.getG(), this.l));
                return algorithmParameters;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/JDKAlgorithmParameterGenerator$GOST3410.class */
    public static class GOST3410 extends JDKAlgorithmParameterGenerator {
        @Override // java.security.AlgorithmParameterGeneratorSpi
        protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for GOST3410 parameter generation.");
        }

        @Override // java.security.AlgorithmParameterGeneratorSpi
        protected AlgorithmParameters engineGenerateParameters() throws NoSuchAlgorithmException, InvalidParameterSpecException, NoSuchProviderException {
            GOST3410ParametersGenerator gOST3410ParametersGenerator = new GOST3410ParametersGenerator();
            if (this.random != null) {
                gOST3410ParametersGenerator.init(this.strength, 2, this.random);
            } else {
                gOST3410ParametersGenerator.init(this.strength, 2, new SecureRandom());
            }
            GOST3410Parameters gOST3410ParametersGenerateParameters = gOST3410ParametersGenerator.generateParameters();
            try {
                AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("GOST3410", BouncyCastleProvider.PROVIDER_NAME);
                algorithmParameters.init(new GOST3410ParameterSpec(new GOST3410PublicKeyParameterSetSpec(gOST3410ParametersGenerateParameters.getP(), gOST3410ParametersGenerateParameters.getQ(), gOST3410ParametersGenerateParameters.getA())));
                return algorithmParameters;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/JDKAlgorithmParameterGenerator$RC2.class */
    public static class RC2 extends JDKAlgorithmParameterGenerator {
        RC2ParameterSpec spec = null;

        @Override // java.security.AlgorithmParameterGeneratorSpi
        protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            if (!(algorithmParameterSpec instanceof RC2ParameterSpec)) {
                throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for RC2 parameter generation.");
            }
            this.spec = (RC2ParameterSpec) algorithmParameterSpec;
        }

        @Override // java.security.AlgorithmParameterGeneratorSpi
        protected AlgorithmParameters engineGenerateParameters() throws NoSuchAlgorithmException, InvalidParameterSpecException, NoSuchProviderException {
            AlgorithmParameters algorithmParameters;
            if (this.spec == null) {
                byte[] bArr = new byte[8];
                if (this.random == null) {
                    this.random = new SecureRandom();
                }
                this.random.nextBytes(bArr);
                try {
                    algorithmParameters = AlgorithmParameters.getInstance("RC2", BouncyCastleProvider.PROVIDER_NAME);
                    algorithmParameters.init(new IvParameterSpec(bArr));
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            } else {
                try {
                    algorithmParameters = AlgorithmParameters.getInstance("RC2", BouncyCastleProvider.PROVIDER_NAME);
                    algorithmParameters.init(this.spec);
                } catch (Exception e2) {
                    throw new RuntimeException(e2.getMessage());
                }
            }
            return algorithmParameters;
        }
    }

    @Override // java.security.AlgorithmParameterGeneratorSpi
    protected void engineInit(int i, SecureRandom secureRandom) {
        this.strength = i;
        this.random = secureRandom;
    }
}
