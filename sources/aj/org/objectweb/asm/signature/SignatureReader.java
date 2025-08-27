package aj.org.objectweb.asm.signature;

/* loaded from: aspectjweaver-1.8.14.jar:aj/org/objectweb/asm/signature/SignatureReader.class */
public class SignatureReader {
    private final String a;

    public SignatureReader(String str) {
        this.a = str;
    }

    public void accept(SignatureVisitor signatureVisitor) {
        int i;
        char cCharAt;
        String str = this.a;
        int length = str.length();
        if (str.charAt(0) == '<') {
            i = 2;
            do {
                int iIndexOf = str.indexOf(58, i);
                signatureVisitor.visitFormalTypeParameter(str.substring(i - 1, iIndexOf));
                int iA = iIndexOf + 1;
                char cCharAt2 = str.charAt(iA);
                if (cCharAt2 == 'L' || cCharAt2 == '[' || cCharAt2 == 'T') {
                    iA = a(str, iA, signatureVisitor.visitClassBound());
                }
                while (true) {
                    int i2 = iA;
                    i = iA + 1;
                    cCharAt = str.charAt(i2);
                    if (cCharAt != ':') {
                        break;
                    } else {
                        iA = a(str, i, signatureVisitor.visitInterfaceBound());
                    }
                }
            } while (cCharAt != '>');
        } else {
            i = 0;
        }
        if (str.charAt(i) == '(') {
            int iA2 = i + 1;
            while (str.charAt(iA2) != ')') {
                iA2 = a(str, iA2, signatureVisitor.visitParameterType());
            }
            int iA3 = a(str, iA2 + 1, signatureVisitor.visitReturnType());
            while (true) {
                int i3 = iA3;
                if (i3 >= length) {
                    return;
                } else {
                    iA3 = a(str, i3 + 1, signatureVisitor.visitExceptionType());
                }
            }
        } else {
            int iA4 = a(str, i, signatureVisitor.visitSuperclass());
            while (true) {
                int i4 = iA4;
                if (i4 >= length) {
                    return;
                } else {
                    iA4 = a(str, i4, signatureVisitor.visitInterface());
                }
            }
        }
    }

    public void acceptType(SignatureVisitor signatureVisitor) {
        a(this.a, 0, signatureVisitor);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:30:0x0145. Please report as an issue. */
    private static int a(String str, int i, SignatureVisitor signatureVisitor) {
        int iA = i + 1;
        char cCharAt = str.charAt(i);
        switch (cCharAt) {
            case 'B':
            case 'C':
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 'V':
            case 'Z':
                signatureVisitor.visitBaseType(cCharAt);
                return iA;
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'U':
            case 'W':
            case 'X':
            case 'Y':
            default:
                int i2 = iA;
                boolean z = false;
                boolean z2 = false;
                while (true) {
                    int i3 = iA;
                    iA++;
                    char cCharAt2 = str.charAt(i3);
                    switch (cCharAt2) {
                        case '.':
                        case ';':
                            if (!z) {
                                String strSubstring = str.substring(i2, iA - 1);
                                if (z2) {
                                    signatureVisitor.visitInnerClassType(strSubstring);
                                } else {
                                    signatureVisitor.visitClassType(strSubstring);
                                }
                            }
                            if (cCharAt2 != ';') {
                                i2 = iA;
                                z = false;
                                z2 = true;
                                break;
                            } else {
                                signatureVisitor.visitEnd();
                                return iA;
                            }
                        case '<':
                            String strSubstring2 = str.substring(i2, iA - 1);
                            if (z2) {
                                signatureVisitor.visitInnerClassType(strSubstring2);
                            } else {
                                signatureVisitor.visitClassType(strSubstring2);
                            }
                            z = true;
                            while (true) {
                                char cCharAt3 = str.charAt(iA);
                                switch (cCharAt3) {
                                    case '*':
                                        iA++;
                                        signatureVisitor.visitTypeArgument();
                                    case '+':
                                    case '-':
                                        iA = a(str, iA + 1, signatureVisitor.visitTypeArgument(cCharAt3));
                                    case '>':
                                        break;
                                    default:
                                        iA = a(str, iA, signatureVisitor.visitTypeArgument('='));
                                }
                            }
                            break;
                    }
                }
                break;
            case 'T':
                int iIndexOf = str.indexOf(59, iA);
                signatureVisitor.visitTypeVariable(str.substring(iA, iIndexOf));
                return iIndexOf + 1;
            case '[':
                return a(str, iA, signatureVisitor.visitArrayType());
        }
    }
}
