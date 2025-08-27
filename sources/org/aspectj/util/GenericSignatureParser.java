package org.aspectj.util;

import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import java.util.ArrayList;
import java.util.List;
import org.aspectj.util.GenericSignature;
import org.springframework.beans.PropertyAccessor;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/util/GenericSignatureParser.class */
public class GenericSignatureParser {
    private String inputString;
    private String[] tokenStream;
    private int tokenIndex = 0;

    public GenericSignature.ClassSignature parseAsClassSignature(String sig) {
        this.inputString = sig;
        this.tokenStream = tokenize(sig);
        this.tokenIndex = 0;
        GenericSignature.ClassSignature classSig = new GenericSignature.ClassSignature();
        if (maybeEat("<")) {
            List<GenericSignature.FormalTypeParameter> formalTypeParametersList = new ArrayList<>();
            do {
                formalTypeParametersList.add(parseFormalTypeParameter());
            } while (!maybeEat(">"));
            classSig.formalTypeParameters = new GenericSignature.FormalTypeParameter[formalTypeParametersList.size()];
            formalTypeParametersList.toArray(classSig.formalTypeParameters);
        }
        classSig.superclassSignature = parseClassTypeSignature();
        List<GenericSignature.ClassTypeSignature> superIntSigs = new ArrayList<>();
        while (this.tokenIndex < this.tokenStream.length) {
            superIntSigs.add(parseClassTypeSignature());
        }
        classSig.superInterfaceSignatures = new GenericSignature.ClassTypeSignature[superIntSigs.size()];
        superIntSigs.toArray(classSig.superInterfaceSignatures);
        return classSig;
    }

    public GenericSignature.MethodTypeSignature parseAsMethodSignature(String sig) {
        this.inputString = sig;
        this.tokenStream = tokenize(sig);
        this.tokenIndex = 0;
        GenericSignature.FormalTypeParameter[] formals = new GenericSignature.FormalTypeParameter[0];
        if (maybeEat("<")) {
            List<GenericSignature.FormalTypeParameter> formalTypeParametersList = new ArrayList<>();
            do {
                formalTypeParametersList.add(parseFormalTypeParameter());
            } while (!maybeEat(">"));
            formals = new GenericSignature.FormalTypeParameter[formalTypeParametersList.size()];
            formalTypeParametersList.toArray(formals);
        }
        eat("(");
        List<GenericSignature.TypeSignature> paramList = new ArrayList<>();
        while (!maybeEat(")")) {
            GenericSignature.FieldTypeSignature fsig = parseFieldTypeSignature(true);
            if (fsig != null) {
                paramList.add(fsig);
            } else {
                paramList.add(new GenericSignature.BaseTypeSignature(eatIdentifier()));
            }
        }
        GenericSignature.TypeSignature[] params = new GenericSignature.TypeSignature[paramList.size()];
        paramList.toArray(params);
        GenericSignature.TypeSignature returnType = parseFieldTypeSignature(true);
        if (returnType == null) {
            returnType = new GenericSignature.BaseTypeSignature(eatIdentifier());
        }
        List<GenericSignature.FieldTypeSignature> throwsList = new ArrayList<>();
        while (maybeEat("^")) {
            throwsList.add(parseFieldTypeSignature(false));
        }
        GenericSignature.FieldTypeSignature[] throwsSigs = new GenericSignature.FieldTypeSignature[throwsList.size()];
        throwsList.toArray(throwsSigs);
        return new GenericSignature.MethodTypeSignature(formals, params, returnType, throwsSigs);
    }

    public GenericSignature.FieldTypeSignature parseAsFieldSignature(String sig) {
        this.inputString = sig;
        this.tokenStream = tokenize(sig);
        this.tokenIndex = 0;
        return parseFieldTypeSignature(false);
    }

    private GenericSignature.FormalTypeParameter parseFormalTypeParameter() {
        GenericSignature.FormalTypeParameter ftp = new GenericSignature.FormalTypeParameter();
        ftp.identifier = eatIdentifier();
        eat(":");
        ftp.classBound = parseFieldTypeSignature(true);
        if (ftp.classBound == null) {
            ftp.classBound = new GenericSignature.ClassTypeSignature("Ljava/lang/Object;", "Ljava/lang/Object");
        }
        List<GenericSignature.FieldTypeSignature> optionalBounds = new ArrayList<>();
        while (maybeEat(":")) {
            optionalBounds.add(parseFieldTypeSignature(false));
        }
        ftp.interfaceBounds = new GenericSignature.FieldTypeSignature[optionalBounds.size()];
        optionalBounds.toArray(ftp.interfaceBounds);
        return ftp;
    }

    private GenericSignature.FieldTypeSignature parseFieldTypeSignature(boolean isOptional) {
        if (isOptional && !this.tokenStream[this.tokenIndex].startsWith(StandardRoles.L) && !this.tokenStream[this.tokenIndex].startsWith("T") && !this.tokenStream[this.tokenIndex].startsWith(PropertyAccessor.PROPERTY_KEY_PREFIX)) {
            return null;
        }
        if (maybeEat(PropertyAccessor.PROPERTY_KEY_PREFIX)) {
            return parseArrayTypeSignature();
        }
        if (this.tokenStream[this.tokenIndex].startsWith(StandardRoles.L)) {
            return parseClassTypeSignature();
        }
        if (this.tokenStream[this.tokenIndex].startsWith("T")) {
            return parseTypeVariableSignature();
        }
        throw new IllegalStateException("Expecting [,L, or T, but found " + this.tokenStream[this.tokenIndex] + " while unpacking " + this.inputString);
    }

    private GenericSignature.ArrayTypeSignature parseArrayTypeSignature() {
        GenericSignature.FieldTypeSignature fieldType = parseFieldTypeSignature(true);
        if (fieldType != null) {
            return new GenericSignature.ArrayTypeSignature(fieldType);
        }
        return new GenericSignature.ArrayTypeSignature(new GenericSignature.BaseTypeSignature(eatIdentifier()));
    }

    private GenericSignature.ClassTypeSignature parseClassTypeSignature() {
        GenericSignature.SimpleClassTypeSignature outerType = null;
        GenericSignature.SimpleClassTypeSignature[] nestedTypes = new GenericSignature.SimpleClassTypeSignature[0];
        StringBuffer ret = new StringBuffer();
        ret.append(eatIdentifier());
        while (maybeEat("/")) {
            ret.append("/");
            ret.append(eatIdentifier());
        }
        String identifier = ret.toString();
        while (!maybeEat(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR)) {
            if (this.tokenStream[this.tokenIndex].equals(".")) {
                outerType = new GenericSignature.SimpleClassTypeSignature(identifier);
                nestedTypes = parseNestedTypesHelper(ret);
            } else if (this.tokenStream[this.tokenIndex].equals("<")) {
                ret.append("<");
                GenericSignature.TypeArgument[] tArgs = maybeParseTypeArguments();
                for (GenericSignature.TypeArgument typeArgument : tArgs) {
                    ret.append(typeArgument.toString());
                }
                ret.append(">");
                outerType = new GenericSignature.SimpleClassTypeSignature(identifier, tArgs);
                nestedTypes = parseNestedTypesHelper(ret);
            } else {
                throw new IllegalStateException("Expecting .,<, or ;, but found " + this.tokenStream[this.tokenIndex] + " while unpacking " + this.inputString);
            }
        }
        ret.append(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
        if (outerType == null) {
            outerType = new GenericSignature.SimpleClassTypeSignature(ret.toString());
        }
        return new GenericSignature.ClassTypeSignature(ret.toString(), outerType, nestedTypes);
    }

    private GenericSignature.SimpleClassTypeSignature[] parseNestedTypesHelper(StringBuffer ret) {
        boolean brokenSignature = false;
        List<GenericSignature.SimpleClassTypeSignature> nestedTypeList = new ArrayList<>();
        while (maybeEat(".")) {
            ret.append(".");
            GenericSignature.SimpleClassTypeSignature sig = parseSimpleClassTypeSignature();
            if (this.tokenStream[this.tokenIndex].equals("/")) {
                if (!brokenSignature) {
                    System.err.println("[See bug 406167] Bad class file signature encountered, nested types appear package qualified, ignoring those incorrect pieces. Signature: " + this.inputString);
                }
                brokenSignature = true;
                this.tokenIndex++;
                while (this.tokenStream[this.tokenIndex + 1].equals("/")) {
                    this.tokenIndex += 2;
                }
                sig = parseSimpleClassTypeSignature();
            }
            ret.append(sig.toString());
            nestedTypeList.add(sig);
        }
        GenericSignature.SimpleClassTypeSignature[] nestedTypes = new GenericSignature.SimpleClassTypeSignature[nestedTypeList.size()];
        nestedTypeList.toArray(nestedTypes);
        return nestedTypes;
    }

    private GenericSignature.SimpleClassTypeSignature parseSimpleClassTypeSignature() {
        String identifier = eatIdentifier();
        GenericSignature.TypeArgument[] tArgs = maybeParseTypeArguments();
        if (tArgs != null) {
            return new GenericSignature.SimpleClassTypeSignature(identifier, tArgs);
        }
        return new GenericSignature.SimpleClassTypeSignature(identifier);
    }

    private GenericSignature.TypeArgument parseTypeArgument() {
        boolean isPlus = false;
        boolean isMinus = false;
        if (maybeEat("*")) {
            return new GenericSignature.TypeArgument();
        }
        if (maybeEat("+")) {
            isPlus = true;
        } else if (maybeEat("-")) {
            isMinus = true;
        }
        GenericSignature.FieldTypeSignature sig = parseFieldTypeSignature(false);
        return new GenericSignature.TypeArgument(isPlus, isMinus, sig);
    }

    private GenericSignature.TypeArgument[] maybeParseTypeArguments() {
        if (maybeEat("<")) {
            List<GenericSignature.TypeArgument> typeArgs = new ArrayList<>();
            do {
                GenericSignature.TypeArgument arg = parseTypeArgument();
                typeArgs.add(arg);
            } while (!maybeEat(">"));
            GenericSignature.TypeArgument[] tArgs = new GenericSignature.TypeArgument[typeArgs.size()];
            typeArgs.toArray(tArgs);
            return tArgs;
        }
        return null;
    }

    private GenericSignature.TypeVariableSignature parseTypeVariableSignature() {
        GenericSignature.TypeVariableSignature tv = new GenericSignature.TypeVariableSignature(eatIdentifier());
        eat(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
        return tv;
    }

    private boolean maybeEat(String token) {
        if (this.tokenStream.length > this.tokenIndex && this.tokenStream[this.tokenIndex].equals(token)) {
            this.tokenIndex++;
            return true;
        }
        return false;
    }

    private void eat(String token) {
        if (!this.tokenStream[this.tokenIndex].equals(token)) {
            throw new IllegalStateException("Expecting " + token + " but found " + this.tokenStream[this.tokenIndex] + " while unpacking " + this.inputString);
        }
        this.tokenIndex++;
    }

    private String eatIdentifier() {
        String[] strArr = this.tokenStream;
        int i = this.tokenIndex;
        this.tokenIndex = i + 1;
        return strArr[i];
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public String[] tokenize(String signatureString) {
        char[] chars = signatureString.toCharArray();
        int index = 0;
        List<String> tokens = new ArrayList<>();
        StringBuffer identifier = new StringBuffer();
        boolean inParens = false;
        boolean inArray = false;
        boolean couldSeePrimitive = false;
        do {
            switch (chars[index]) {
                case '(':
                    tokens.add("(");
                    inParens = true;
                    couldSeePrimitive = true;
                    break;
                case ')':
                    tokens.add(")");
                    inParens = false;
                    break;
                case '*':
                    tokens.add("*");
                    break;
                case '+':
                    tokens.add("+");
                    break;
                case ',':
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
                case '=':
                case '?':
                case '@':
                case 'A':
                case 'E':
                case 'G':
                case 'H':
                case 'K':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'T':
                case 'U':
                case 'W':
                case 'X':
                case 'Y':
                case '\\':
                case ']':
                default:
                    identifier.append(chars[index]);
                    break;
                case '-':
                    tokens.add("-");
                    break;
                case '.':
                    if (identifier.length() > 0) {
                        tokens.add(identifier.toString());
                    }
                    identifier = new StringBuffer();
                    couldSeePrimitive = false;
                    tokens.add(".");
                    break;
                case '/':
                    if (identifier.length() > 0) {
                        tokens.add(identifier.toString());
                    }
                    identifier = new StringBuffer();
                    tokens.add("/");
                    couldSeePrimitive = false;
                    break;
                case ':':
                    if (identifier.length() > 0) {
                        tokens.add(identifier.toString());
                    }
                    identifier = new StringBuffer();
                    tokens.add(":");
                    break;
                case ';':
                    if (identifier.length() > 0) {
                        tokens.add(identifier.toString());
                    }
                    identifier = new StringBuffer();
                    tokens.add(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
                    couldSeePrimitive = true;
                    inArray = false;
                    break;
                case '<':
                    if (identifier.length() > 0) {
                        tokens.add(identifier.toString());
                    }
                    identifier = new StringBuffer();
                    tokens.add("<");
                    break;
                case '>':
                    if (identifier.length() > 0) {
                        tokens.add(identifier.toString());
                    }
                    identifier = new StringBuffer();
                    tokens.add(">");
                    break;
                case 'B':
                case 'C':
                case 'D':
                case 'F':
                case 'I':
                case 'J':
                case 'S':
                case 'V':
                case 'Z':
                    if ((inParens || inArray) && couldSeePrimitive && identifier.length() == 0) {
                        tokens.add(new String("" + chars[index]));
                    } else {
                        identifier.append(chars[index]);
                    }
                    inArray = false;
                    break;
                case 'L':
                    couldSeePrimitive = false;
                    identifier.append(chars[index]);
                    break;
                case '[':
                    tokens.add(PropertyAccessor.PROPERTY_KEY_PREFIX);
                    couldSeePrimitive = true;
                    inArray = true;
                    break;
                case '^':
                    if (identifier.length() > 0) {
                        tokens.add(identifier.toString());
                    }
                    identifier = new StringBuffer();
                    tokens.add("^");
                    break;
            }
            index++;
        } while (index < chars.length);
        if (identifier.length() > 0) {
            tokens.add(identifier.toString());
        }
        String[] tokenArray = new String[tokens.size()];
        tokens.toArray(tokenArray);
        return tokenArray;
    }
}
