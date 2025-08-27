package org.aspectj.weaver;

import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/TypeFactory.class */
public class TypeFactory {
    public static ReferenceType createParameterizedType(ResolvedType aBaseType, UnresolvedType[] someTypeParameters, World inAWorld) {
        ReferenceType pType;
        ResolvedType baseType = aBaseType;
        if (!aBaseType.isGenericType() && someTypeParameters != null && someTypeParameters.length > 0) {
            if (!aBaseType.isRawType()) {
                throw new IllegalStateException("Expecting raw type, but " + aBaseType + " is of type " + aBaseType.getTypekind());
            }
            baseType = baseType.getGenericType();
            if (baseType == null) {
                throw new IllegalStateException("Raw type does not have generic type set");
            }
        }
        ResolvedType[] resolvedParameters = inAWorld.resolve(someTypeParameters);
        ReferenceType existingType = ((ReferenceType) baseType).findDerivativeType(resolvedParameters);
        if (existingType != null) {
            pType = existingType;
        } else {
            pType = new ReferenceType(baseType, resolvedParameters, inAWorld);
        }
        return (ReferenceType) pType.resolve(inAWorld);
    }

    public static UnresolvedType createUnresolvedParameterizedType(String sig, String erasuresig, UnresolvedType[] arguments) {
        return new UnresolvedType(sig, erasuresig, arguments);
    }

    static UnresolvedType convertSigToType(String aSignature) {
        UnresolvedType bound;
        int startOfParams = aSignature.indexOf(60);
        if (startOfParams == -1) {
            bound = UnresolvedType.forSignature(aSignature);
        } else {
            int endOfParams = aSignature.lastIndexOf(62);
            String signatureErasure = StandardRoles.L + aSignature.substring(1, startOfParams) + ScriptUtils.DEFAULT_STATEMENT_SEPARATOR;
            UnresolvedType[] typeParams = createTypeParams(aSignature.substring(startOfParams + 1, endOfParams));
            bound = new UnresolvedType("P" + aSignature.substring(1), signatureErasure, typeParams);
        }
        return bound;
    }

    public static UnresolvedType createTypeFromSignature(String signature) {
        String lastType;
        String lastType2;
        char firstChar = signature.charAt(0);
        if (firstChar == 'P') {
            int startOfParams = signature.indexOf(60);
            if (startOfParams == -1) {
                String signatureErasure = StandardRoles.L + signature.substring(1);
                return new UnresolvedType(signature, signatureErasure, UnresolvedType.NONE);
            }
            int endOfParams = locateMatchingEndAngleBracket(signature, startOfParams);
            StringBuffer erasureSig = new StringBuffer(signature);
            erasureSig.setCharAt(0, 'L');
            while (startOfParams != -1) {
                erasureSig.delete(startOfParams, endOfParams + 1);
                startOfParams = locateFirstBracket(erasureSig);
                if (startOfParams != -1) {
                    endOfParams = locateMatchingEndAngleBracket(erasureSig, startOfParams);
                }
            }
            String signatureErasure2 = erasureSig.toString();
            int nestedTypePosition = signature.indexOf(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX, endOfParams);
            if (nestedTypePosition != -1) {
                lastType2 = signature.substring(nestedTypePosition + 1);
            } else {
                lastType2 = new String(signature);
            }
            int startOfParams2 = lastType2.indexOf("<");
            UnresolvedType[] typeParams = UnresolvedType.NONE;
            if (startOfParams2 != -1) {
                int endOfParams2 = locateMatchingEndAngleBracket(lastType2, startOfParams2);
                typeParams = createTypeParams(lastType2.substring(startOfParams2 + 1, endOfParams2));
            }
            StringBuilder s = new StringBuilder();
            int firstAngleBracket = signature.indexOf(60);
            s.append("P").append(signature.substring(1, firstAngleBracket));
            s.append('<');
            for (UnresolvedType typeParameter : typeParams) {
                s.append(typeParameter.getSignature());
            }
            s.append(">;");
            return new UnresolvedType(s.toString(), signatureErasure2, typeParams);
        }
        if ((firstChar == '?' || firstChar == '*') && signature.length() == 1) {
            return WildcardedUnresolvedType.QUESTIONMARK;
        }
        if (firstChar == '+') {
            UnresolvedType upperBound = convertSigToType(signature.substring(1));
            WildcardedUnresolvedType wildcardedUT = new WildcardedUnresolvedType(signature, upperBound, null);
            return wildcardedUT;
        }
        if (firstChar == '-') {
            UnresolvedType lowerBound = convertSigToType(signature.substring(1));
            WildcardedUnresolvedType wildcardedUT2 = new WildcardedUnresolvedType(signature, null, lowerBound);
            return wildcardedUT2;
        }
        if (firstChar == 'T') {
            String typeVariableName = signature.substring(1);
            if (typeVariableName.endsWith(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR)) {
                typeVariableName = typeVariableName.substring(0, typeVariableName.length() - 1);
            }
            return new UnresolvedTypeVariableReferenceType(new TypeVariable(typeVariableName));
        }
        if (firstChar == '[') {
            int dims = 0;
            while (signature.charAt(dims) == '[') {
                dims++;
            }
            UnresolvedType componentType = createTypeFromSignature(signature.substring(dims));
            return new UnresolvedType(signature, signature.substring(0, dims) + componentType.getErasureSignature());
        }
        if (signature.length() == 1) {
            switch (firstChar) {
                case 'B':
                    return UnresolvedType.BYTE;
                case 'C':
                    return UnresolvedType.CHAR;
                case 'D':
                    return UnresolvedType.DOUBLE;
                case 'F':
                    return UnresolvedType.FLOAT;
                case 'I':
                    return UnresolvedType.INT;
                case 'J':
                    return UnresolvedType.LONG;
                case 'S':
                    return UnresolvedType.SHORT;
                case 'V':
                    return UnresolvedType.VOID;
                case 'Z':
                    return UnresolvedType.BOOLEAN;
            }
        }
        if (firstChar == '@') {
            return ResolvedType.MISSING;
        }
        if (firstChar == 'L') {
            int leftAngleBracket = signature.indexOf(60);
            if (leftAngleBracket == -1) {
                return new UnresolvedType(signature);
            }
            int endOfParams3 = locateMatchingEndAngleBracket(signature, leftAngleBracket);
            StringBuffer erasureSig2 = new StringBuffer(signature);
            erasureSig2.setCharAt(0, 'L');
            while (leftAngleBracket != -1) {
                erasureSig2.delete(leftAngleBracket, endOfParams3 + 1);
                leftAngleBracket = locateFirstBracket(erasureSig2);
                if (leftAngleBracket != -1) {
                    endOfParams3 = locateMatchingEndAngleBracket(erasureSig2, leftAngleBracket);
                }
            }
            String signatureErasure3 = erasureSig2.toString();
            int nestedTypePosition2 = signature.indexOf(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX, endOfParams3);
            if (nestedTypePosition2 != -1) {
                lastType = signature.substring(nestedTypePosition2 + 1);
            } else {
                lastType = new String(signature);
            }
            int leftAngleBracket2 = lastType.indexOf("<");
            UnresolvedType[] typeParams2 = UnresolvedType.NONE;
            if (leftAngleBracket2 != -1) {
                int endOfParams4 = locateMatchingEndAngleBracket(lastType, leftAngleBracket2);
                typeParams2 = createTypeParams(lastType.substring(leftAngleBracket2 + 1, endOfParams4));
            }
            StringBuilder s2 = new StringBuilder();
            int firstAngleBracket2 = signature.indexOf(60);
            s2.append("P").append(signature.substring(1, firstAngleBracket2));
            s2.append('<');
            for (UnresolvedType typeParameter2 : typeParams2) {
                s2.append(typeParameter2.getSignature());
            }
            s2.append(">;");
            return new UnresolvedType(s2.toString(), signatureErasure3, typeParams2);
        }
        return new UnresolvedType(signature);
    }

    private static int locateMatchingEndAngleBracket(CharSequence signature, int startOfParams) {
        if (startOfParams == -1) {
            return -1;
        }
        int count = 1;
        int idx = startOfParams;
        int max = signature.length();
        while (idx < max) {
            idx++;
            char ch2 = signature.charAt(idx);
            if (ch2 == '<') {
                count++;
            } else if (ch2 != '>') {
                continue;
            } else {
                if (count == 1) {
                    break;
                }
                count--;
            }
        }
        return idx;
    }

    private static int locateFirstBracket(StringBuffer signature) {
        int max = signature.length();
        for (int idx = 0; idx < max; idx++) {
            if (signature.charAt(idx) == '<') {
                return idx;
            }
        }
        return -1;
    }

    private static UnresolvedType[] createTypeParams(String typeParameterSpecification) {
        int endOfSig;
        List<UnresolvedType> types = new ArrayList<>();
        for (String remainingToProcess = typeParameterSpecification; remainingToProcess.length() != 0; remainingToProcess = remainingToProcess.substring(endOfSig)) {
            int anglies = 0;
            boolean hadAnglies = false;
            boolean sigFound = false;
            endOfSig = 0;
            while (endOfSig < remainingToProcess.length() && !sigFound) {
                char thisChar = remainingToProcess.charAt(endOfSig);
                switch (thisChar) {
                    case '*':
                        if (anglies != 0) {
                            break;
                        } else {
                            int nextCharPos = endOfSig + 1;
                            if (nextCharPos >= remainingToProcess.length()) {
                                sigFound = true;
                                break;
                            } else {
                                char nextChar = remainingToProcess.charAt(nextCharPos);
                                if (nextChar != '+' && nextChar != '-') {
                                    sigFound = true;
                                    break;
                                } else {
                                    break;
                                }
                            }
                        }
                        break;
                    case ';':
                        if (anglies == 0) {
                            sigFound = true;
                            break;
                        } else {
                            break;
                        }
                    case '<':
                        anglies++;
                        hadAnglies = true;
                        break;
                    case '>':
                        anglies--;
                        break;
                    case '[':
                        if (anglies == 0) {
                            int nextChar2 = endOfSig + 1;
                            while (remainingToProcess.charAt(nextChar2) == '[') {
                                nextChar2++;
                            }
                            if ("BCDFIJSZ".indexOf(remainingToProcess.charAt(nextChar2)) != -1) {
                                sigFound = true;
                                endOfSig = nextChar2;
                                break;
                            } else {
                                break;
                            }
                        } else {
                            break;
                        }
                }
                endOfSig++;
            }
            String forProcessing = remainingToProcess.substring(0, endOfSig);
            if (hadAnglies && forProcessing.charAt(0) == 'L') {
                forProcessing = "P" + forProcessing.substring(1);
            }
            types.add(createTypeFromSignature(forProcessing));
        }
        UnresolvedType[] typeParams = new UnresolvedType[types.size()];
        types.toArray(typeParams);
        return typeParams;
    }

    public static UnresolvedType createUnresolvedParameterizedType(String baseTypeSignature, UnresolvedType[] arguments) {
        StringBuffer parameterizedSig = new StringBuffer();
        parameterizedSig.append("P");
        parameterizedSig.append(baseTypeSignature.substring(1, baseTypeSignature.length() - 1));
        if (arguments.length > 0) {
            parameterizedSig.append("<");
            for (UnresolvedType unresolvedType : arguments) {
                parameterizedSig.append(unresolvedType.getSignature());
            }
            parameterizedSig.append(">");
        }
        parameterizedSig.append(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
        return createUnresolvedParameterizedType(parameterizedSig.toString(), baseTypeSignature, arguments);
    }
}
