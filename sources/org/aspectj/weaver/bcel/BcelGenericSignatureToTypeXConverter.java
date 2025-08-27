package org.aspectj.weaver.bcel;

import java.util.HashMap;
import java.util.Map;
import org.aspectj.util.GenericSignature;
import org.aspectj.weaver.BoundedReferenceType;
import org.aspectj.weaver.ReferenceType;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.TypeFactory;
import org.aspectj.weaver.TypeVariable;
import org.aspectj.weaver.TypeVariableReferenceType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.World;
import org.aspectj.weaver.tools.Trace;
import org.aspectj.weaver.tools.TraceFactory;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/BcelGenericSignatureToTypeXConverter.class */
public class BcelGenericSignatureToTypeXConverter {
    private static Trace trace = TraceFactory.getTraceFactory().getTrace(BcelGenericSignatureToTypeXConverter.class);

    public static ResolvedType classTypeSignature2TypeX(GenericSignature.ClassTypeSignature aClassTypeSignature, GenericSignature.FormalTypeParameter[] typeParams, World world) throws GenericSignatureFormatException {
        Map<GenericSignature.FormalTypeParameter, ReferenceType> typeMap = new HashMap<>();
        ResolvedType ret = classTypeSignature2TypeX(aClassTypeSignature, typeParams, world, typeMap);
        fixUpCircularDependencies(ret, typeMap);
        return ret;
    }

    private static ResolvedType classTypeSignature2TypeX(GenericSignature.ClassTypeSignature aClassTypeSignature, GenericSignature.FormalTypeParameter[] typeParams, World world, Map<GenericSignature.FormalTypeParameter, ReferenceType> inProgressTypeVariableResolutions) throws GenericSignatureFormatException {
        StringBuffer sig = new StringBuffer();
        sig.append(aClassTypeSignature.outerType.identifier.replace(';', ' ').trim());
        for (int i = 0; i < aClassTypeSignature.nestedTypes.length; i++) {
            sig.append(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX);
            sig.append(aClassTypeSignature.nestedTypes[i].identifier.replace(';', ' ').trim());
        }
        sig.append(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
        GenericSignature.SimpleClassTypeSignature innerType = aClassTypeSignature.outerType;
        if (aClassTypeSignature.nestedTypes.length > 0) {
            innerType = aClassTypeSignature.nestedTypes[aClassTypeSignature.nestedTypes.length - 1];
        }
        if (innerType.typeArguments.length > 0) {
            ResolvedType theBaseType = UnresolvedType.forSignature(sig.toString()).resolve(world);
            if (!theBaseType.isGenericType() && !theBaseType.isRawType()) {
                if (trace.isTraceEnabled()) {
                    trace.event("classTypeSignature2TypeX: this type is not a generic type:", (Object) null, new Object[]{theBaseType});
                }
                return theBaseType;
            }
            ResolvedType[] typeArgumentTypes = new ResolvedType[innerType.typeArguments.length];
            for (int i2 = 0; i2 < typeArgumentTypes.length; i2++) {
                typeArgumentTypes[i2] = typeArgument2TypeX(innerType.typeArguments[i2], typeParams, world, inProgressTypeVariableResolutions);
            }
            return TypeFactory.createParameterizedType(theBaseType, typeArgumentTypes, world);
        }
        return world.resolve(UnresolvedType.forSignature(sig.toString()));
    }

    public static ResolvedType fieldTypeSignature2TypeX(GenericSignature.FieldTypeSignature aFieldTypeSignature, GenericSignature.FormalTypeParameter[] typeParams, World world) throws GenericSignatureFormatException {
        Map<GenericSignature.FormalTypeParameter, ReferenceType> typeMap = new HashMap<>();
        ResolvedType ret = fieldTypeSignature2TypeX(aFieldTypeSignature, typeParams, world, typeMap);
        fixUpCircularDependencies(ret, typeMap);
        return ret;
    }

    private static ResolvedType fieldTypeSignature2TypeX(GenericSignature.FieldTypeSignature aFieldTypeSignature, GenericSignature.FormalTypeParameter[] typeParams, World world, Map<GenericSignature.FormalTypeParameter, ReferenceType> inProgressTypeVariableResolutions) throws GenericSignatureFormatException {
        if (aFieldTypeSignature.isClassTypeSignature()) {
            return classTypeSignature2TypeX((GenericSignature.ClassTypeSignature) aFieldTypeSignature, typeParams, world, inProgressTypeVariableResolutions);
        }
        if (aFieldTypeSignature.isArrayTypeSignature()) {
            int dims = 0;
            GenericSignature.TypeSignature typeSignature = aFieldTypeSignature;
            while (true) {
                GenericSignature.TypeSignature ats = typeSignature;
                if (ats instanceof GenericSignature.ArrayTypeSignature) {
                    dims++;
                    typeSignature = ((GenericSignature.ArrayTypeSignature) ats).typeSig;
                } else {
                    return world.resolve(UnresolvedType.makeArray(typeSignature2TypeX(ats, typeParams, world, inProgressTypeVariableResolutions), dims));
                }
            }
        } else {
            if (aFieldTypeSignature.isTypeVariableSignature()) {
                ResolvedType rtx = typeVariableSignature2TypeX((GenericSignature.TypeVariableSignature) aFieldTypeSignature, typeParams, world, inProgressTypeVariableResolutions);
                return rtx;
            }
            throw new GenericSignatureFormatException("Cant understand field type signature: " + aFieldTypeSignature);
        }
    }

    public static TypeVariable formalTypeParameter2TypeVariable(GenericSignature.FormalTypeParameter aFormalTypeParameter, GenericSignature.FormalTypeParameter[] typeParams, World world) throws GenericSignatureFormatException {
        Map<GenericSignature.FormalTypeParameter, ReferenceType> typeMap = new HashMap<>();
        return formalTypeParameter2TypeVariable(aFormalTypeParameter, typeParams, world, typeMap);
    }

    private static TypeVariable formalTypeParameter2TypeVariable(GenericSignature.FormalTypeParameter aFormalTypeParameter, GenericSignature.FormalTypeParameter[] typeParams, World world, Map<GenericSignature.FormalTypeParameter, ReferenceType> inProgressTypeVariableResolutions) throws GenericSignatureFormatException {
        UnresolvedType upperBound = fieldTypeSignature2TypeX(aFormalTypeParameter.classBound, typeParams, world, inProgressTypeVariableResolutions);
        UnresolvedType[] ifBounds = new UnresolvedType[aFormalTypeParameter.interfaceBounds.length];
        for (int i = 0; i < ifBounds.length; i++) {
            ifBounds[i] = fieldTypeSignature2TypeX(aFormalTypeParameter.interfaceBounds[i], typeParams, world, inProgressTypeVariableResolutions);
        }
        return new TypeVariable(aFormalTypeParameter.identifier, upperBound, ifBounds);
    }

    private static ResolvedType typeArgument2TypeX(GenericSignature.TypeArgument aTypeArgument, GenericSignature.FormalTypeParameter[] typeParams, World world, Map<GenericSignature.FormalTypeParameter, ReferenceType> inProgressTypeVariableResolutions) throws GenericSignatureFormatException {
        if (aTypeArgument.isWildcard) {
            return UnresolvedType.SOMETHING.resolve(world);
        }
        if (aTypeArgument.isMinus) {
            UnresolvedType bound = fieldTypeSignature2TypeX(aTypeArgument.signature, typeParams, world, inProgressTypeVariableResolutions);
            ResolvedType resolvedBound = world.resolve(bound);
            if (resolvedBound.isMissing()) {
                world.getLint().cantFindType.signal("Unable to find type (for bound): " + resolvedBound.getName(), null);
                resolvedBound = world.resolve(UnresolvedType.OBJECT);
            }
            ReferenceType rBound = (ReferenceType) resolvedBound;
            return new BoundedReferenceType(rBound, false, world);
        }
        if (aTypeArgument.isPlus) {
            UnresolvedType bound2 = fieldTypeSignature2TypeX(aTypeArgument.signature, typeParams, world, inProgressTypeVariableResolutions);
            ResolvedType resolvedBound2 = world.resolve(bound2);
            if (resolvedBound2.isMissing()) {
                world.getLint().cantFindType.signal("Unable to find type (for bound): " + resolvedBound2.getName(), null);
                resolvedBound2 = world.resolve(UnresolvedType.OBJECT);
            }
            ReferenceType rBound2 = (ReferenceType) resolvedBound2;
            return new BoundedReferenceType(rBound2, true, world);
        }
        return fieldTypeSignature2TypeX(aTypeArgument.signature, typeParams, world, inProgressTypeVariableResolutions);
    }

    public static ResolvedType typeSignature2TypeX(GenericSignature.TypeSignature aTypeSig, GenericSignature.FormalTypeParameter[] typeParams, World world) throws GenericSignatureFormatException {
        Map<GenericSignature.FormalTypeParameter, ReferenceType> typeMap = new HashMap<>();
        ResolvedType ret = typeSignature2TypeX(aTypeSig, typeParams, world, typeMap);
        fixUpCircularDependencies(ret, typeMap);
        return ret;
    }

    private static ResolvedType typeSignature2TypeX(GenericSignature.TypeSignature aTypeSig, GenericSignature.FormalTypeParameter[] typeParams, World world, Map<GenericSignature.FormalTypeParameter, ReferenceType> inProgressTypeVariableResolutions) throws GenericSignatureFormatException {
        if (aTypeSig.isBaseType()) {
            return world.resolve(UnresolvedType.forSignature(((GenericSignature.BaseTypeSignature) aTypeSig).toString()));
        }
        return fieldTypeSignature2TypeX((GenericSignature.FieldTypeSignature) aTypeSig, typeParams, world, inProgressTypeVariableResolutions);
    }

    private static ResolvedType typeVariableSignature2TypeX(GenericSignature.TypeVariableSignature aTypeVarSig, GenericSignature.FormalTypeParameter[] typeParams, World world, Map<GenericSignature.FormalTypeParameter, ReferenceType> inProgressTypeVariableResolutions) throws GenericSignatureFormatException {
        GenericSignature.FormalTypeParameter typeVarBounds = null;
        int i = 0;
        while (true) {
            if (i >= typeParams.length) {
                break;
            }
            if (!typeParams[i].identifier.equals(aTypeVarSig.typeVariableName)) {
                i++;
            } else {
                typeVarBounds = typeParams[i];
                break;
            }
        }
        if (typeVarBounds == null) {
            return new TypeVariableReferenceType(new TypeVariable(aTypeVarSig.typeVariableName), world);
        }
        if (inProgressTypeVariableResolutions.containsKey(typeVarBounds)) {
            return inProgressTypeVariableResolutions.get(typeVarBounds);
        }
        inProgressTypeVariableResolutions.put(typeVarBounds, new FTPHolder(typeVarBounds, world));
        ReferenceType ret = new TypeVariableReferenceType(formalTypeParameter2TypeVariable(typeVarBounds, typeParams, world, inProgressTypeVariableResolutions), world);
        inProgressTypeVariableResolutions.put(typeVarBounds, ret);
        return ret;
    }

    private static void fixUpCircularDependencies(ResolvedType aTypeX, Map<GenericSignature.FormalTypeParameter, ReferenceType> typeVariableResolutions) {
        if (!(aTypeX instanceof ReferenceType)) {
            return;
        }
        ReferenceType rt = (ReferenceType) aTypeX;
        TypeVariable[] typeVars = rt.getTypeVariables();
        if (typeVars != null) {
            for (int i = 0; i < typeVars.length; i++) {
                if (typeVars[i].getUpperBound() instanceof FTPHolder) {
                    GenericSignature.FormalTypeParameter key = ((FTPHolder) typeVars[i].getUpperBound()).ftpToBeSubstituted;
                    typeVars[i].setUpperBound(typeVariableResolutions.get(key));
                }
            }
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/BcelGenericSignatureToTypeXConverter$FTPHolder.class */
    private static class FTPHolder extends ReferenceType {
        public GenericSignature.FormalTypeParameter ftpToBeSubstituted;

        public FTPHolder(GenericSignature.FormalTypeParameter ftp, World world) {
            super("Ljava/lang/Object;", world);
            this.ftpToBeSubstituted = ftp;
        }

        @Override // org.aspectj.weaver.UnresolvedType
        public String toString() {
            return "placeholder for TypeVariable of " + this.ftpToBeSubstituted.toString();
        }

        @Override // org.aspectj.weaver.UnresolvedType
        public ResolvedType resolve(World world) {
            return this;
        }

        @Override // org.aspectj.weaver.ResolvedType
        public boolean isCacheable() {
            return false;
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/BcelGenericSignatureToTypeXConverter$GenericSignatureFormatException.class */
    public static class GenericSignatureFormatException extends Exception {
        public GenericSignatureFormatException(String explanation) {
            super(explanation);
        }
    }
}
