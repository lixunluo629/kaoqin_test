package org.aspectj.weaver;

import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;
import org.apache.xmlbeans.XmlErrorCodes;
import org.aspectj.util.GenericSignature;
import org.aspectj.util.GenericSignatureParser;
import org.aspectj.weaver.tools.Traceable;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/UnresolvedType.class */
public class UnresolvedType implements Traceable, TypeVariableDeclaringElement {
    public static final UnresolvedType[] NONE;
    public static final UnresolvedType OBJECT;
    public static final UnresolvedType OBJECTARRAY;
    public static final UnresolvedType CLONEABLE;
    public static final UnresolvedType SERIALIZABLE;
    public static final UnresolvedType THROWABLE;
    public static final UnresolvedType RUNTIME_EXCEPTION;
    public static final UnresolvedType ERROR;
    public static final UnresolvedType AT_INHERITED;
    public static final UnresolvedType AT_RETENTION;
    public static final UnresolvedType ENUM;
    public static final UnresolvedType ANNOTATION;
    public static final UnresolvedType JL_CLASS;
    public static final UnresolvedType JAVA_LANG_CLASS_ARRAY;
    public static final UnresolvedType JL_STRING;
    public static final UnresolvedType JL_EXCEPTION;
    public static final UnresolvedType JAVA_LANG_REFLECT_METHOD;
    public static final UnresolvedType JAVA_LANG_REFLECT_FIELD;
    public static final UnresolvedType JAVA_LANG_REFLECT_CONSTRUCTOR;
    public static final UnresolvedType JAVA_LANG_ANNOTATION;
    public static final UnresolvedType SUPPRESS_AJ_WARNINGS;
    public static final UnresolvedType AT_TARGET;
    public static final UnresolvedType SOMETHING;
    public static final UnresolvedType[] ARRAY_WITH_JUST_OBJECT;
    public static final UnresolvedType JOINPOINT_STATICPART;
    public static final UnresolvedType JOINPOINT_ENCLOSINGSTATICPART;
    public static final UnresolvedType BOOLEAN;
    public static final UnresolvedType BYTE;
    public static final UnresolvedType CHAR;
    public static final UnresolvedType DOUBLE;
    public static final UnresolvedType FLOAT;
    public static final UnresolvedType INT;
    public static final UnresolvedType LONG;
    public static final UnresolvedType SHORT;
    public static final UnresolvedType VOID;
    public static final String MISSING_NAME = "@missing@";
    protected TypeKind typeKind;
    protected String signature;
    protected String signatureErasure;
    private String packageName;
    private String className;
    protected UnresolvedType[] typeParameters;
    protected TypeVariable[] typeVariables;
    private int size;
    private boolean needsModifiableDelegate;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !UnresolvedType.class.desiredAssertionStatus();
        NONE = new UnresolvedType[0];
        OBJECT = forSignature("Ljava/lang/Object;");
        OBJECTARRAY = forSignature("[Ljava/lang/Object;");
        CLONEABLE = forSignature("Ljava/lang/Cloneable;");
        SERIALIZABLE = forSignature("Ljava/io/Serializable;");
        THROWABLE = forSignature("Ljava/lang/Throwable;");
        RUNTIME_EXCEPTION = forSignature("Ljava/lang/RuntimeException;");
        ERROR = forSignature("Ljava/lang/Error;");
        AT_INHERITED = forSignature("Ljava/lang/annotation/Inherited;");
        AT_RETENTION = forSignature("Ljava/lang/annotation/Retention;");
        ENUM = forSignature("Ljava/lang/Enum;");
        ANNOTATION = forSignature("Ljava/lang/annotation/Annotation;");
        JL_CLASS = forSignature("Ljava/lang/Class;");
        JAVA_LANG_CLASS_ARRAY = forSignature("[Ljava/lang/Class;");
        JL_STRING = forSignature("Ljava/lang/String;");
        JL_EXCEPTION = forSignature("Ljava/lang/Exception;");
        JAVA_LANG_REFLECT_METHOD = forSignature("Ljava/lang/reflect/Method;");
        JAVA_LANG_REFLECT_FIELD = forSignature("Ljava/lang/reflect/Field;");
        JAVA_LANG_REFLECT_CONSTRUCTOR = forSignature("Ljava/lang/reflect/Constructor;");
        JAVA_LANG_ANNOTATION = forSignature("Ljava/lang/annotation/Annotation;");
        SUPPRESS_AJ_WARNINGS = forSignature("Lorg/aspectj/lang/annotation/SuppressAjWarnings;");
        AT_TARGET = forSignature("Ljava/lang/annotation/Target;");
        SOMETHING = new UnresolvedType("?");
        ARRAY_WITH_JUST_OBJECT = new UnresolvedType[]{OBJECT};
        JOINPOINT_STATICPART = forSignature("Lorg/aspectj/lang/JoinPoint$StaticPart;");
        JOINPOINT_ENCLOSINGSTATICPART = forSignature("Lorg/aspectj/lang/JoinPoint$EnclosingStaticPart;");
        BOOLEAN = forPrimitiveType("Z");
        BYTE = forPrimitiveType("B");
        CHAR = forPrimitiveType("C");
        DOUBLE = forPrimitiveType("D");
        FLOAT = forPrimitiveType("F");
        INT = forPrimitiveType("I");
        LONG = forPrimitiveType("J");
        SHORT = forPrimitiveType("S");
        VOID = forPrimitiveType("V");
    }

    public boolean isPrimitiveType() {
        return this.typeKind == TypeKind.PRIMITIVE;
    }

    public boolean isVoid() {
        return this.signature.equals("V");
    }

    public boolean isSimpleType() {
        return this.typeKind == TypeKind.SIMPLE;
    }

    public boolean isRawType() {
        return this.typeKind == TypeKind.RAW;
    }

    public boolean isGenericType() {
        return this.typeKind == TypeKind.GENERIC;
    }

    public boolean isParameterizedType() {
        return this.typeKind == TypeKind.PARAMETERIZED;
    }

    public boolean isParameterizedOrGenericType() {
        return this.typeKind == TypeKind.GENERIC || this.typeKind == TypeKind.PARAMETERIZED;
    }

    public boolean isParameterizedOrRawType() {
        return this.typeKind == TypeKind.PARAMETERIZED || this.typeKind == TypeKind.RAW;
    }

    public boolean isTypeVariableReference() {
        return this.typeKind == TypeKind.TYPE_VARIABLE;
    }

    public boolean isGenericWildcard() {
        return this.typeKind == TypeKind.WILDCARD;
    }

    public TypeKind getTypekind() {
        return this.typeKind;
    }

    public final boolean isArray() {
        return this.signature.length() > 0 && this.signature.charAt(0) == '[';
    }

    public boolean equals(Object other) {
        if (!(other instanceof UnresolvedType)) {
            return false;
        }
        return this.signature.equals(((UnresolvedType) other).signature);
    }

    public int hashCode() {
        return this.signature.hashCode();
    }

    protected UnresolvedType(String signature) {
        this.typeKind = TypeKind.SIMPLE;
        this.size = 1;
        this.needsModifiableDelegate = false;
        this.signature = signature;
        this.signatureErasure = signature;
    }

    protected UnresolvedType(String signature, String signatureErasure) {
        this.typeKind = TypeKind.SIMPLE;
        this.size = 1;
        this.needsModifiableDelegate = false;
        this.signature = signature;
        this.signatureErasure = signatureErasure;
    }

    public UnresolvedType(String signature, String signatureErasure, UnresolvedType[] typeParams) {
        this.typeKind = TypeKind.SIMPLE;
        this.size = 1;
        this.needsModifiableDelegate = false;
        this.signature = signature;
        this.signatureErasure = signatureErasure;
        this.typeParameters = typeParams;
        if (typeParams != null) {
            this.typeKind = TypeKind.PARAMETERIZED;
        }
    }

    public int getSize() {
        return this.size;
    }

    public static UnresolvedType forName(String name) {
        return forSignature(nameToSignature(name));
    }

    public static UnresolvedType[] forNames(String[] names) {
        UnresolvedType[] ret = new UnresolvedType[names.length];
        int len = names.length;
        for (int i = 0; i < len; i++) {
            ret[i] = forName(names[i]);
        }
        return ret;
    }

    public static UnresolvedType forGenericType(String name, TypeVariable[] tvbs, String genericSig) {
        String sig = nameToSignature(name);
        UnresolvedType ret = forSignature(sig);
        ret.typeKind = TypeKind.GENERIC;
        ret.typeVariables = tvbs;
        ret.signatureErasure = sig;
        return ret;
    }

    public static UnresolvedType forGenericTypeSignature(String sig, String declaredGenericSig) {
        UnresolvedType ret = forSignature(sig);
        ret.typeKind = TypeKind.GENERIC;
        GenericSignature.ClassSignature csig = new GenericSignatureParser().parseAsClassSignature(declaredGenericSig);
        GenericSignature.FormalTypeParameter[] ftps = csig.formalTypeParameters;
        ret.typeVariables = new TypeVariable[ftps.length];
        for (int i = 0; i < ftps.length; i++) {
            GenericSignature.FormalTypeParameter parameter = ftps[i];
            if (parameter.classBound instanceof GenericSignature.ClassTypeSignature) {
                GenericSignature.ClassTypeSignature cts = (GenericSignature.ClassTypeSignature) parameter.classBound;
                ret.typeVariables[i] = new TypeVariable(ftps[i].identifier, forSignature(cts.outerType.identifier + ScriptUtils.DEFAULT_STATEMENT_SEPARATOR));
            } else if (parameter.classBound instanceof GenericSignature.TypeVariableSignature) {
                GenericSignature.TypeVariableSignature tvs = (GenericSignature.TypeVariableSignature) parameter.classBound;
                UnresolvedTypeVariableReferenceType utvrt = new UnresolvedTypeVariableReferenceType(new TypeVariable(tvs.typeVariableName));
                ret.typeVariables[i] = new TypeVariable(ftps[i].identifier, utvrt);
            } else {
                throw new BCException("UnresolvedType.forGenericTypeSignature(): Do not know how to process type variable bound of type '" + parameter.classBound.getClass() + "'.  Full signature is '" + sig + "'");
            }
        }
        ret.signatureErasure = sig;
        ret.signature = ret.signatureErasure;
        return ret;
    }

    public static UnresolvedType forGenericTypeVariables(String sig, TypeVariable[] tVars) {
        UnresolvedType ret = forSignature(sig);
        ret.typeKind = TypeKind.GENERIC;
        ret.typeVariables = tVars;
        ret.signatureErasure = sig;
        ret.signature = ret.signatureErasure;
        return ret;
    }

    public static UnresolvedType forRawTypeName(String name) {
        UnresolvedType ret = forName(name);
        ret.typeKind = TypeKind.RAW;
        return ret;
    }

    public static UnresolvedType forPrimitiveType(String signature) {
        UnresolvedType ret = new UnresolvedType(signature);
        ret.typeKind = TypeKind.PRIMITIVE;
        if (signature.equals("J") || signature.equals("D")) {
            ret.size = 2;
        } else if (signature.equals("V")) {
            ret.size = 0;
        }
        return ret;
    }

    public static UnresolvedType[] add(UnresolvedType[] types, UnresolvedType end) {
        int len = types.length;
        UnresolvedType[] ret = new UnresolvedType[len + 1];
        System.arraycopy(types, 0, ret, 0, len);
        ret[len] = end;
        return ret;
    }

    public static UnresolvedType[] insert(UnresolvedType start, UnresolvedType[] types) {
        int len = types.length;
        UnresolvedType[] ret = new UnresolvedType[len + 1];
        ret[0] = start;
        System.arraycopy(types, 0, ret, 1, len);
        return ret;
    }

    public static UnresolvedType forSignature(String signature) {
        if (!$assertionsDisabled && signature.startsWith(StandardRoles.L) && signature.indexOf("<") != -1) {
            throw new AssertionError();
        }
        switch (signature.charAt(0)) {
            case '+':
                return TypeFactory.createTypeFromSignature(signature);
            case ',':
            case '.':
            case '/':
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
            case ':':
            case ';':
            case '<':
            case '=':
            case '>':
            case '@':
            case 'A':
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'M':
            case 'N':
            case 'O':
            case 'Q':
            case 'R':
            case 'U':
            case 'W':
            case 'X':
            case 'Y':
            default:
                throw new BCException("Bad type signature " + signature);
            case '-':
                return TypeFactory.createTypeFromSignature(signature);
            case '?':
                return TypeFactory.createTypeFromSignature(signature);
            case 'B':
                return BYTE;
            case 'C':
                return CHAR;
            case 'D':
                return DOUBLE;
            case 'F':
                return FLOAT;
            case 'I':
                return INT;
            case 'J':
                return LONG;
            case 'L':
                return TypeFactory.createTypeFromSignature(signature);
            case 'P':
                return TypeFactory.createTypeFromSignature(signature);
            case 'S':
                return SHORT;
            case 'T':
                return TypeFactory.createTypeFromSignature(signature);
            case 'V':
                return VOID;
            case 'Z':
                return BOOLEAN;
            case '[':
                return TypeFactory.createTypeFromSignature(signature);
        }
    }

    public static UnresolvedType[] forSignatures(String[] sigs) {
        UnresolvedType[] ret = new UnresolvedType[sigs.length];
        int len = sigs.length;
        for (int i = 0; i < len; i++) {
            ret[i] = forSignature(sigs[i]);
        }
        return ret;
    }

    public String getName() {
        return signatureToName(this.signature);
    }

    public String getSimpleName() {
        String name = getRawName();
        int lastDot = name.lastIndexOf(46);
        if (lastDot != -1) {
            name = name.substring(lastDot + 1);
        }
        if (isParameterizedType()) {
            StringBuffer sb = new StringBuffer(name);
            sb.append("<");
            for (int i = 0; i < this.typeParameters.length - 1; i++) {
                sb.append(this.typeParameters[i].getSimpleName());
                sb.append(",");
            }
            sb.append(this.typeParameters[this.typeParameters.length - 1].getSimpleName());
            sb.append(">");
            name = sb.toString();
        }
        return name;
    }

    public String getRawName() {
        return signatureToName(this.signatureErasure == null ? this.signature : this.signatureErasure);
    }

    public String getBaseName() {
        String name = getName();
        if (isParameterizedType() || isGenericType()) {
            if (this.typeParameters == null) {
                return name;
            }
            return name.substring(0, name.indexOf("<"));
        }
        return name;
    }

    public String getSimpleBaseName() {
        String name = getBaseName();
        int lastDot = name.lastIndexOf(46);
        if (lastDot != -1) {
            name = name.substring(lastDot + 1);
        }
        return name;
    }

    public static String[] getNames(UnresolvedType[] types) {
        String[] ret = new String[types.length];
        int len = types.length;
        for (int i = 0; i < len; i++) {
            ret[i] = types[i].getName();
        }
        return ret;
    }

    public String getSignature() {
        return this.signature;
    }

    public String getErasureSignature() {
        if (this.signatureErasure == null) {
            return this.signature;
        }
        return this.signatureErasure;
    }

    public boolean needsModifiableDelegate() {
        return this.needsModifiableDelegate;
    }

    public void setNeedsModifiableDelegate(boolean b) {
        this.needsModifiableDelegate = b;
    }

    public UnresolvedType getRawType() {
        return forSignature(getErasureSignature());
    }

    public UnresolvedType getOutermostType() {
        if (isArray() || isPrimitiveType()) {
            return this;
        }
        String sig = getErasureSignature();
        int dollar = sig.indexOf(36);
        if (dollar != -1) {
            return forSignature(sig.substring(0, dollar) + ';');
        }
        return this;
    }

    public UnresolvedType getComponentType() {
        if (isArray()) {
            return forSignature(this.signature.substring(1));
        }
        return null;
    }

    public String toString() {
        return getName();
    }

    public String toDebugString() {
        return getName();
    }

    public ResolvedType resolve(World world) {
        return world.resolve(this);
    }

    private static String signatureToName(String signature) {
        switch (signature.charAt(0)) {
            case '*':
                return "?";
            case '+':
                return "? extends " + signatureToName(signature.substring(1, signature.length()));
            case ',':
            case '.':
            case '/':
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
            case ':':
            case ';':
            case '<':
            case '=':
            case '>':
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
            case 'Q':
            case 'R':
            case 'U':
            case 'W':
            case 'X':
            case 'Y':
            default:
                throw new BCException("Bad type signature: " + signature);
            case '-':
                return "? super " + signatureToName(signature.substring(1, signature.length()));
            case 'B':
                return "byte";
            case 'C':
                return "char";
            case 'D':
                return XmlErrorCodes.DOUBLE;
            case 'F':
                return XmlErrorCodes.FLOAT;
            case 'I':
                return XmlErrorCodes.INT;
            case 'J':
                return XmlErrorCodes.LONG;
            case 'L':
                String name = signature.substring(1, signature.length() - 1).replace('/', '.');
                return name;
            case 'P':
                StringBuffer nameBuff = new StringBuffer();
                int paramNestLevel = 0;
                int i = 1;
                while (i < signature.length()) {
                    char c = signature.charAt(i);
                    switch (c) {
                        case '/':
                            nameBuff.append('.');
                            break;
                        case ';':
                            break;
                        case '<':
                            nameBuff.append("<");
                            paramNestLevel++;
                            StringBuffer innerBuff = new StringBuffer();
                            while (paramNestLevel > 0) {
                                i++;
                                char c2 = signature.charAt(i);
                                if (c2 == '<') {
                                    paramNestLevel++;
                                }
                                if (c2 == '>') {
                                    paramNestLevel--;
                                }
                                if (paramNestLevel > 0) {
                                    innerBuff.append(c2);
                                }
                                if (c2 == ';' && paramNestLevel == 1) {
                                    nameBuff.append(signatureToName(innerBuff.toString()));
                                    if (signature.charAt(i + 1) != '>') {
                                        nameBuff.append(',');
                                    }
                                    innerBuff = new StringBuffer();
                                }
                            }
                            nameBuff.append(">");
                            break;
                        default:
                            nameBuff.append(c);
                            break;
                    }
                    i++;
                }
                return nameBuff.toString();
            case 'S':
                return "short";
            case 'T':
                StringBuffer nameBuff2 = new StringBuffer();
                int colon = signature.indexOf(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
                String tvarName = signature.substring(1, colon);
                nameBuff2.append(tvarName);
                return nameBuff2.toString();
            case 'V':
                return "void";
            case 'Z':
                return "boolean";
            case '[':
                return signatureToName(signature.substring(1, signature.length())) + "[]";
        }
    }

    private static String nameToSignature(String name) {
        int len = name.length();
        if (len < 8) {
            if (name.equals(XmlErrorCodes.INT)) {
                return "I";
            }
            if (name.equals("void")) {
                return "V";
            }
            if (name.equals(XmlErrorCodes.LONG)) {
                return "J";
            }
            if (name.equals("boolean")) {
                return "Z";
            }
            if (name.equals(XmlErrorCodes.DOUBLE)) {
                return "D";
            }
            if (name.equals(XmlErrorCodes.FLOAT)) {
                return "F";
            }
            if (name.equals("byte")) {
                return "B";
            }
            if (name.equals("short")) {
                return "S";
            }
            if (name.equals("char")) {
                return "C";
            }
            if (name.equals("?")) {
                return name;
            }
        }
        if (len == 0) {
            throw new BCException("Bad type name: " + name);
        }
        if (name.endsWith("[]")) {
            return PropertyAccessor.PROPERTY_KEY_PREFIX + nameToSignature(name.substring(0, name.length() - 2));
        }
        if (name.charAt(0) == '[') {
            return name.replace('.', '/');
        }
        if (name.indexOf("<") == -1) {
            return StandardRoles.L + name.replace('.', '/') + ';';
        }
        StringBuffer nameBuff = new StringBuffer();
        int nestLevel = 0;
        nameBuff.append("P");
        int i = 0;
        while (i < len) {
            char c = name.charAt(i);
            switch (c) {
                case '.':
                    nameBuff.append('/');
                    break;
                case '<':
                    nameBuff.append("<");
                    nestLevel++;
                    StringBuffer innerBuff = new StringBuffer();
                    while (nestLevel > 0) {
                        i++;
                        char c2 = name.charAt(i);
                        if (c2 == '<') {
                            nestLevel++;
                        } else if (c2 == '>') {
                            nestLevel--;
                        }
                        if (c2 == ',' && nestLevel == 1) {
                            nameBuff.append(nameToSignature(innerBuff.toString()));
                            innerBuff = new StringBuffer();
                        } else if (nestLevel > 0) {
                            innerBuff.append(c2);
                        }
                    }
                    nameBuff.append(nameToSignature(innerBuff.toString()));
                    nameBuff.append('>');
                    break;
                default:
                    nameBuff.append(c);
                    break;
            }
            i++;
        }
        nameBuff.append(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
        return nameBuff.toString();
    }

    public final void write(CompressingDataOutputStream s) throws IOException {
        s.writeUTF(getSignature());
    }

    public static UnresolvedType read(DataInputStream s) throws IOException {
        String sig = s.readUTF();
        if (sig.equals(MISSING_NAME)) {
            return ResolvedType.MISSING;
        }
        return forSignature(sig);
    }

    public String getNameAsIdentifier() {
        return getName().replace('.', '_');
    }

    public String getPackageNameAsIdentifier() {
        String name = getName();
        int index = name.lastIndexOf(46);
        if (index == -1) {
            return "";
        }
        return name.substring(0, index).replace('.', '_');
    }

    public UnresolvedType[] getTypeParameters() {
        return this.typeParameters == null ? NONE : this.typeParameters;
    }

    public TypeVariable[] getTypeVariables() {
        return this.typeVariables;
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/UnresolvedType$TypeKind.class */
    public static class TypeKind {
        public static final TypeKind PRIMITIVE = new TypeKind("primitive");
        public static final TypeKind SIMPLE = new TypeKind(BeanDefinitionParserDelegate.DEPENDENCY_CHECK_SIMPLE_ATTRIBUTE_VALUE);
        public static final TypeKind RAW = new TypeKind("raw");
        public static final TypeKind GENERIC = new TypeKind("generic");
        public static final TypeKind PARAMETERIZED = new TypeKind("parameterized");
        public static final TypeKind TYPE_VARIABLE = new TypeKind("type_variable");
        public static final TypeKind WILDCARD = new TypeKind("wildcard");
        private final String type;

        public String toString() {
            return this.type;
        }

        private TypeKind(String type) {
            this.type = type;
        }
    }

    @Override // org.aspectj.weaver.TypeVariableDeclaringElement
    public TypeVariable getTypeVariableNamed(String name) {
        TypeVariable[] vars = getTypeVariables();
        if (vars == null || vars.length == 0) {
            return null;
        }
        for (TypeVariable aVar : vars) {
            if (aVar.getName().equals(name)) {
                return aVar;
            }
        }
        return null;
    }

    @Override // org.aspectj.weaver.tools.Traceable
    public String toTraceString() {
        return getClass().getName() + PropertyAccessor.PROPERTY_KEY_PREFIX + getName() + "]";
    }

    public UnresolvedType parameterize(Map<String, UnresolvedType> typeBindings) {
        throw new UnsupportedOperationException("unable to parameterize unresolved type: " + this.signature);
    }

    public String getClassName() {
        if (this.className == null) {
            String name = getName();
            if (name.indexOf("<") != -1) {
                name = name.substring(0, name.indexOf("<"));
            }
            int index = name.lastIndexOf(46);
            if (index == -1) {
                this.className = name;
            } else {
                this.className = name.substring(index + 1);
            }
        }
        return this.className;
    }

    public String getPackageName() {
        if (this.packageName == null) {
            String name = getName();
            int angly = name.indexOf(60);
            if (angly != -1) {
                name = name.substring(0, angly);
            }
            int index = name.lastIndexOf(46);
            if (index == -1) {
                this.packageName = "";
            } else {
                this.packageName = name.substring(0, index);
            }
        }
        return this.packageName;
    }

    public static void writeArray(UnresolvedType[] types, CompressingDataOutputStream stream) throws IOException {
        int len = types.length;
        stream.writeShort(len);
        for (UnresolvedType type : types) {
            type.write(stream);
        }
    }

    public static UnresolvedType[] readArray(DataInputStream s) throws IOException {
        int len = s.readShort();
        if (len == 0) {
            return NONE;
        }
        UnresolvedType[] types = new UnresolvedType[len];
        for (int i = 0; i < len; i++) {
            types[i] = read(s);
        }
        return types;
    }

    public static UnresolvedType makeArray(UnresolvedType base, int dims) {
        StringBuffer sig = new StringBuffer();
        for (int i = 0; i < dims; i++) {
            sig.append(PropertyAccessor.PROPERTY_KEY_PREFIX);
        }
        sig.append(base.getSignature());
        return forSignature(sig.toString());
    }
}
