package lombok.eclipse.handlers;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.ConfigurationKeys;
import lombok.Singular;
import lombok.ToString;
import lombok.Value;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.core.HandlerPriority;
import lombok.core.handlers.HandlerUtil;
import lombok.core.handlers.InclusionExclusionUtils;
import lombok.eclipse.EclipseAnnotationHandler;
import lombok.eclipse.EclipseNode;
import lombok.eclipse.handlers.EclipseHandlerUtil;
import lombok.eclipse.handlers.EclipseSingularsRecipes;
import lombok.eclipse.handlers.HandleConstructor;
import lombok.experimental.NonFinal;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.AllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.Assignment;
import org.eclipse.jdt.internal.compiler.ast.ConditionalExpression;
import org.eclipse.jdt.internal.compiler.ast.ConstructorDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FalseLiteral;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.IfStatement;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ParameterizedQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ParameterizedSingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedThisReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.ast.TrueLiteral;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeParameter;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.ast.UnaryExpression;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.compiler.lookup.MethodScope;
import org.eclipse.jdt.internal.compiler.lookup.TypeConstants;

@HandlerPriority(-1024)
/* loaded from: lombok-1.16.22.jar:lombok/eclipse/handlers/HandleBuilder.SCL.lombok */
public class HandleBuilder extends EclipseAnnotationHandler<Builder> {
    private HandleConstructor handleConstructor = new HandleConstructor();
    private static final char[] CLEAN_FIELD_NAME = "$lombokUnclean".toCharArray();
    private static final char[] CLEAN_METHOD_NAME = "$lombokClean".toCharArray();
    private static final char[] DEFAULT_PREFIX = {'$', 'd', 'e', 'f', 'a', 'u', 'l', 't', '$'};
    private static final char[] SET_PREFIX = {'$', 's', 'e', 't'};
    private static final AbstractMethodDeclaration[] EMPTY = new AbstractMethodDeclaration[0];
    private static /* synthetic */ int[] $SWITCH_TABLE$lombok$eclipse$handlers$EclipseHandlerUtil$MemberExistsResult;

    static /* synthetic */ int[] $SWITCH_TABLE$lombok$eclipse$handlers$EclipseHandlerUtil$MemberExistsResult() {
        int[] iArr = $SWITCH_TABLE$lombok$eclipse$handlers$EclipseHandlerUtil$MemberExistsResult;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[EclipseHandlerUtil.MemberExistsResult.valuesCustom().length];
        try {
            iArr2[EclipseHandlerUtil.MemberExistsResult.EXISTS_BY_LOMBOK.ordinal()] = 2;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[EclipseHandlerUtil.MemberExistsResult.EXISTS_BY_USER.ordinal()] = 3;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[EclipseHandlerUtil.MemberExistsResult.NOT_EXISTS.ordinal()] = 1;
        } catch (NoSuchFieldError unused3) {
        }
        $SWITCH_TABLE$lombok$eclipse$handlers$EclipseHandlerUtil$MemberExistsResult = iArr2;
        return iArr2;
    }

    private static final boolean toBoolean(Object expr, boolean defaultValue) {
        if (expr == null) {
            return defaultValue;
        }
        if (expr instanceof FalseLiteral) {
            return false;
        }
        if (expr instanceof TrueLiteral) {
            return true;
        }
        return ((Boolean) expr).booleanValue();
    }

    /* loaded from: lombok-1.16.22.jar:lombok/eclipse/handlers/HandleBuilder$BuilderFieldData.SCL.lombok */
    private static class BuilderFieldData {
        TypeReference type;
        char[] rawName;
        char[] name;
        char[] nameOfDefaultProvider;
        char[] nameOfSetFlag;
        EclipseSingularsRecipes.SingularData singularData;
        Builder.ObtainVia obtainVia;
        EclipseNode obtainViaNode;
        EclipseNode originalFieldNode;
        List<EclipseNode> createdFields;

        private BuilderFieldData() {
            this.createdFields = new ArrayList();
        }

        /* synthetic */ BuilderFieldData(BuilderFieldData builderFieldData) {
            this();
        }
    }

    private static boolean equals(String a, char[] b) {
        if (a.length() != b.length) {
            return false;
        }
        for (int i = 0; i < b.length; i++) {
            if (a.charAt(i) != b[i]) {
                return false;
            }
        }
        return true;
    }

    private static boolean equals(String a, char[][] b) {
        if (a == null || a.isEmpty()) {
            return b.length == 0;
        }
        String[] aParts = a.split("\\.");
        if (aParts.length != b.length) {
            return false;
        }
        for (int i = 0; i < b.length; i++) {
            if (!equals(aParts[i], b[i])) {
                return false;
            }
        }
        return true;
    }

    private static final char[] prefixWith(char[] prefix, char[] name) {
        char[] out = new char[prefix.length + name.length];
        System.arraycopy(prefix, 0, out, 0, prefix.length);
        System.arraycopy(name, 0, out, prefix.length, name.length);
        return out;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v206, types: [org.eclipse.jdt.internal.compiler.ast.TypeReference[]] */
    /* JADX WARN: Type inference failed for: r1v64, types: [char[][]] */
    @Override // lombok.eclipse.EclipseAnnotationHandler
    public void handle(AnnotationValues<Builder> annotation, Annotation ast, EclipseNode annotationNode) throws SecurityException {
        EclipseNode tdParent;
        TypeReference returnType;
        TypeParameter[] typeParams;
        TypeReference[] thrownExceptions;
        char[] nameOfStaticBuilderMethod;
        char[] token;
        char[] token2;
        EclipseSingularsRecipes.EclipseSingularizer singularizer;
        MethodDeclaration md;
        MethodDeclaration cleanMethod;
        MethodDeclaration md2;
        ConstructorDeclaration cd;
        long p = (ast.sourceStart << 32) | ast.sourceEnd;
        Builder builderInstance = (Builder) annotation.getInstance();
        boolean fluent = toBoolean(annotation.getActualExpression("fluent"), true);
        boolean chain = toBoolean(annotation.getActualExpression("chain"), true);
        String builderMethodName = builderInstance.builderMethodName();
        String buildMethodName = builderInstance.buildMethodName();
        String builderClassName = builderInstance.builderClassName();
        boolean toBuilder = builderInstance.toBuilder();
        List<char[]> typeArgsForToBuilder = null;
        if (builderMethodName == null) {
            builderMethodName = "builder";
        }
        if (buildMethodName == null) {
            builderMethodName = JsonPOJOBuilder.DEFAULT_BUILD_METHOD;
        }
        if (builderClassName == null) {
            builderClassName = "";
        }
        if (HandlerUtil.checkName("builderMethodName", builderMethodName, annotationNode) && HandlerUtil.checkName("buildMethodName", buildMethodName, annotationNode)) {
            if (builderClassName.isEmpty() || HandlerUtil.checkName("builderClassName", builderClassName, annotationNode)) {
                EclipseNode parent = annotationNode.up();
                List<BuilderFieldData> builderFields = new ArrayList<>();
                EclipseNode fillParametersFrom = parent.get() instanceof AbstractMethodDeclaration ? parent : null;
                boolean addCleaning = false;
                boolean isStatic = true;
                if (parent.get() instanceof TypeDeclaration) {
                    tdParent = parent;
                    TypeDeclaration td = tdParent.get();
                    List<EclipseNode> allFields = new ArrayList<>();
                    boolean valuePresent = EclipseHandlerUtil.hasAnnotation((Class<? extends java.lang.annotation.Annotation>) Value.class, parent) || EclipseHandlerUtil.hasAnnotation("lombok.experimental.Value", parent);
                    for (EclipseNode fieldNode : HandleConstructor.findAllFields(tdParent, true)) {
                        FieldDeclaration fd = fieldNode.get();
                        EclipseNode isDefault = EclipseHandlerUtil.findAnnotation(Builder.Default.class, fieldNode);
                        boolean isFinal = (fd.modifiers & 16) != 0 || (valuePresent && !EclipseHandlerUtil.hasAnnotation((Class<? extends java.lang.annotation.Annotation>) NonFinal.class, fieldNode));
                        BuilderFieldData bfd = new BuilderFieldData(null);
                        bfd.rawName = fieldNode.getName().toCharArray();
                        bfd.name = EclipseHandlerUtil.removePrefixFromField(fieldNode);
                        bfd.type = fd.type;
                        bfd.singularData = getSingularData(fieldNode, ast);
                        bfd.originalFieldNode = fieldNode;
                        if (bfd.singularData != null && isDefault != null) {
                            isDefault.addError("@Builder.Default and @Singular cannot be mixed.");
                            isDefault = null;
                        }
                        if (fd.initialization == null && isDefault != null) {
                            isDefault.addWarning("@Builder.Default requires an initializing expression (' = something;').");
                            isDefault = null;
                        }
                        if (fd.initialization != null && isDefault == null) {
                            if (!isFinal) {
                                fieldNode.addWarning("@Builder will ignore the initializing expression entirely. If you want the initializing expression to serve as default, add @Builder.Default. If it is not supposed to be settable during building, make the field final.");
                            }
                        }
                        if (isDefault != null) {
                            bfd.nameOfDefaultProvider = prefixWith(DEFAULT_PREFIX, bfd.name);
                            bfd.nameOfSetFlag = prefixWith(bfd.name, SET_PREFIX);
                            MethodDeclaration md3 = generateDefaultProvider(bfd.nameOfDefaultProvider, td.typeParameters, fieldNode, ast);
                            if (md3 != null) {
                                EclipseHandlerUtil.injectMethod(tdParent, md3);
                            }
                        }
                        addObtainVia(bfd, fieldNode);
                        builderFields.add(bfd);
                        allFields.add(fieldNode);
                    }
                    this.handleConstructor.generateConstructor(tdParent, AccessLevel.PACKAGE, allFields, false, null, HandleConstructor.SkipIfConstructorExists.I_AM_BUILDER, Collections.emptyList(), annotationNode);
                    returnType = EclipseHandlerUtil.namePlusTypeParamsToTypeReference(td.name, td.typeParameters, p);
                    typeParams = td.typeParameters;
                    thrownExceptions = null;
                    nameOfStaticBuilderMethod = null;
                    if (builderClassName.isEmpty()) {
                        builderClassName = String.valueOf(new String(td.name)) + "Builder";
                    }
                } else if (parent.get() instanceof ConstructorDeclaration) {
                    ConstructorDeclaration cd2 = parent.get();
                    if (cd2.typeParameters != null && cd2.typeParameters.length > 0) {
                        annotationNode.addError("@Builder is not supported on constructors with constructor type parameters.");
                        return;
                    }
                    tdParent = parent.up();
                    TypeDeclaration td2 = tdParent.get();
                    returnType = EclipseHandlerUtil.namePlusTypeParamsToTypeReference(td2.name, td2.typeParameters, p);
                    typeParams = td2.typeParameters;
                    thrownExceptions = cd2.thrownExceptions;
                    nameOfStaticBuilderMethod = null;
                    if (builderClassName.isEmpty()) {
                        builderClassName = String.valueOf(new String(cd2.selector)) + "Builder";
                    }
                } else if (parent.get() instanceof MethodDeclaration) {
                    MethodDeclaration md4 = parent.get();
                    tdParent = parent.up();
                    isStatic = md4.isStatic();
                    if (toBuilder) {
                        char[] cArr = null;
                        if (md4.returnType.dimensions() > 0) {
                            annotationNode.addError("@Builder(toBuilder=true) is only supported if you return your own type.");
                            return;
                        }
                        if (md4.returnType instanceof SingleTypeReference) {
                            token2 = md4.returnType.token;
                        } else if (md4.returnType instanceof QualifiedTypeReference) {
                            char[][] pkg = md4.returnType.tokens;
                            token2 = pkg[pkg.length];
                            char[] cArr2 = new char[pkg.length - 1];
                            System.arraycopy(pkg, 0, cArr2, 0, cArr2.length);
                            cArr = cArr2;
                        } else {
                            annotationNode.addError("@Builder(toBuilder=true) is only supported if you return your own type.");
                            return;
                        }
                        if (cArr != null && !equals(parent.getPackageDeclaration(), (char[][]) cArr)) {
                            annotationNode.addError("@Builder(toBuilder=true) is only supported if you return your own type.");
                            return;
                        }
                        if (tdParent == null || !equals(tdParent.getName(), token2)) {
                            annotationNode.addError("@Builder(toBuilder=true) is only supported if you return your own type.");
                            return;
                        }
                        TypeParameter[] tpOnType = tdParent.get().typeParameters;
                        TypeParameter[] tpOnMethod = md4.typeParameters;
                        SingleTypeReference[][] singleTypeReferenceArr = null;
                        if (md4.returnType instanceof ParameterizedSingleTypeReference) {
                            singleTypeReferenceArr = new TypeReference[]{md4.returnType.typeArguments};
                        } else if (md4.returnType instanceof ParameterizedQualifiedTypeReference) {
                            singleTypeReferenceArr = md4.returnType.typeArguments;
                        }
                        if (singleTypeReferenceArr != null) {
                            for (int i = 0; i < singleTypeReferenceArr.length - 1; i++) {
                                if (singleTypeReferenceArr[i] != null && singleTypeReferenceArr[i].length > 0) {
                                    annotationNode.addError("@Builder(toBuilder=true) is not supported if returning a type with generics applied to an intermediate.");
                                    return;
                                }
                            }
                        }
                        SingleTypeReference[] singleTypeReferenceArr2 = singleTypeReferenceArr == null ? null : singleTypeReferenceArr[singleTypeReferenceArr.length - 1];
                        typeArgsForToBuilder = new ArrayList<>();
                        if (tpOnMethod != null) {
                            for (TypeParameter onMethod : tpOnMethod) {
                                int pos = -1;
                                if (singleTypeReferenceArr2 != null) {
                                    for (int i2 = 0; i2 < singleTypeReferenceArr2.length; i2++) {
                                        if (singleTypeReferenceArr2[i2].getClass() == SingleTypeReference.class && Arrays.equals(singleTypeReferenceArr2[i2].token, onMethod.name)) {
                                            pos = i2;
                                        }
                                    }
                                }
                                if (pos == -1 || tpOnType == null || tpOnType.length <= pos) {
                                    annotationNode.addError("@Builder(toBuilder=true) requires that each type parameter on the static method is part of the typeargs of the return value. Type parameter " + new String(onMethod.name) + " is not part of the return type.");
                                    return;
                                }
                                typeArgsForToBuilder.add(tpOnType[pos].name);
                            }
                        }
                    }
                    returnType = EclipseHandlerUtil.copyType(md4.returnType, ast);
                    typeParams = md4.typeParameters;
                    thrownExceptions = md4.thrownExceptions;
                    nameOfStaticBuilderMethod = md4.selector;
                    if (builderClassName.isEmpty()) {
                        if (md4.returnType instanceof QualifiedTypeReference) {
                            char[][] tokens = md4.returnType.tokens;
                            token = tokens[tokens.length - 1];
                        } else if (md4.returnType instanceof SingleTypeReference) {
                            token = md4.returnType.token;
                            if (!(md4.returnType instanceof ParameterizedSingleTypeReference) && typeParams != null) {
                                for (TypeParameter tp : typeParams) {
                                    if (Arrays.equals(tp.name, token)) {
                                        annotationNode.addError("@Builder requires specifying 'builderClassName' if used on methods with a type parameter as return type.");
                                        return;
                                    }
                                }
                            }
                        } else {
                            annotationNode.addError("Unexpected kind of return type on annotated method. Specify 'builderClassName' to solve this problem.");
                            return;
                        }
                        if (Character.isLowerCase(token[0])) {
                            char[] newToken = new char[token.length];
                            System.arraycopy(token, 1, newToken, 1, token.length - 1);
                            newToken[0] = Character.toTitleCase(token[0]);
                            token = newToken;
                        }
                        builderClassName = String.valueOf(new String(token)) + "Builder";
                    }
                } else {
                    annotationNode.addError("@Builder is only supported on types, constructors, and methods.");
                    return;
                }
                if (fillParametersFrom != null) {
                    Iterator<EclipseNode> it = fillParametersFrom.down().iterator();
                    while (it.hasNext()) {
                        EclipseNode param = it.next();
                        if (param.getKind() == AST.Kind.ARGUMENT) {
                            BuilderFieldData bfd2 = new BuilderFieldData(null);
                            Argument arg = param.get();
                            bfd2.rawName = arg.name;
                            bfd2.name = arg.name;
                            bfd2.type = arg.type;
                            bfd2.singularData = getSingularData(param, ast);
                            bfd2.originalFieldNode = param;
                            addObtainVia(bfd2, param);
                            builderFields.add(bfd2);
                        }
                    }
                }
                EclipseNode builderType = findInnerClass(tdParent, builderClassName);
                if (builderType == null) {
                    builderType = makeBuilderClass(isStatic, tdParent, builderClassName, typeParams, ast);
                } else {
                    TypeDeclaration builderTypeDeclaration = builderType.get();
                    if (isStatic && (builderTypeDeclaration.modifiers & 8) == 0) {
                        annotationNode.addError("Existing Builder must be a static inner class.");
                        return;
                    }
                    if (!isStatic && (builderTypeDeclaration.modifiers & 8) != 0) {
                        annotationNode.addError("Existing Builder must be a non-static inner class.");
                        return;
                    }
                    EclipseHandlerUtil.sanityCheckForMethodGeneratingAnnotationsOnBuilderClass(builderType, annotationNode);
                    for (BuilderFieldData bfd3 : builderFields) {
                        EclipseSingularsRecipes.SingularData sd = bfd3.singularData;
                        if (sd != null && (singularizer = sd.getSingularizer()) != null && singularizer.checkForAlreadyExistingNodesAndGenerateError(builderType, sd)) {
                            bfd3.singularData = null;
                        }
                    }
                }
                Iterator<BuilderFieldData> it2 = builderFields.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    BuilderFieldData bfd4 = it2.next();
                    if (bfd4.singularData != null && bfd4.singularData.getSingularizer() != null && bfd4.singularData.getSingularizer().requiresCleaning()) {
                        addCleaning = true;
                        break;
                    }
                    if (bfd4.obtainVia != null) {
                        if (bfd4.obtainVia.field().isEmpty() == bfd4.obtainVia.method().isEmpty()) {
                            bfd4.obtainViaNode.addError("The syntax is either @ObtainVia(field = \"fieldName\") or @ObtainVia(method = \"methodName\").");
                            return;
                        } else if (bfd4.obtainVia.method().isEmpty() && bfd4.obtainVia.isStatic()) {
                            bfd4.obtainViaNode.addError("@ObtainVia(isStatic = true) is not valid unless 'method' has been set.");
                            return;
                        }
                    }
                }
                generateBuilderFields(builderType, builderFields, ast);
                if (addCleaning) {
                    FieldDeclaration cleanDecl = new FieldDeclaration(CLEAN_FIELD_NAME, 0, -1);
                    cleanDecl.declarationSourceEnd = -1;
                    cleanDecl.modifiers = 2;
                    cleanDecl.type = TypeReference.baseTypeReference(5, 0);
                    EclipseHandlerUtil.injectFieldAndMarkGenerated(builderType, cleanDecl);
                }
                if (EclipseHandlerUtil.constructorExists(builderType) == EclipseHandlerUtil.MemberExistsResult.NOT_EXISTS && (cd = HandleConstructor.createConstructor(AccessLevel.PACKAGE, builderType, Collections.emptyList(), false, annotationNode, Collections.emptyList())) != null) {
                    EclipseHandlerUtil.injectMethod(builderType, cd);
                }
                Iterator<BuilderFieldData> it3 = builderFields.iterator();
                while (it3.hasNext()) {
                    makeSetterMethodsForBuilder(builderType, it3.next(), annotationNode, fluent, chain);
                }
                if (EclipseHandlerUtil.methodExists(buildMethodName, builderType, -1) == EclipseHandlerUtil.MemberExistsResult.NOT_EXISTS && (md2 = generateBuildMethod(tdParent, isStatic, buildMethodName, nameOfStaticBuilderMethod, returnType, builderFields, builderType, thrownExceptions, addCleaning, ast)) != null) {
                    EclipseHandlerUtil.injectMethod(builderType, md2);
                }
                if (EclipseHandlerUtil.methodExists("toString", builderType, 0) == EclipseHandlerUtil.MemberExistsResult.NOT_EXISTS) {
                    List<InclusionExclusionUtils.Included<EclipseNode, ToString.Include>> fieldNodes = new ArrayList<>();
                    Iterator<BuilderFieldData> it4 = builderFields.iterator();
                    while (it4.hasNext()) {
                        for (EclipseNode f : it4.next().createdFields) {
                            fieldNodes.add(new InclusionExclusionUtils.Included<>(f, null, true));
                        }
                    }
                    MethodDeclaration md5 = HandleToString.createToString(builderType, fieldNodes, true, false, ast, HandlerUtil.FieldAccess.ALWAYS_FIELD);
                    if (md5 != null) {
                        EclipseHandlerUtil.injectMethod(builderType, md5);
                    }
                }
                if (addCleaning && (cleanMethod = generateCleanMethod(builderFields, builderType, ast)) != null) {
                    EclipseHandlerUtil.injectMethod(builderType, cleanMethod);
                }
                if (EclipseHandlerUtil.methodExists(builderMethodName, tdParent, -1) == EclipseHandlerUtil.MemberExistsResult.NOT_EXISTS && (md = generateBuilderMethod(isStatic, builderMethodName, builderClassName, tdParent, typeParams, ast)) != null) {
                    EclipseHandlerUtil.injectMethod(tdParent, md);
                }
                if (toBuilder) {
                    switch ($SWITCH_TABLE$lombok$eclipse$handlers$EclipseHandlerUtil$MemberExistsResult()[EclipseHandlerUtil.methodExists("toBuilder", tdParent, 0).ordinal()]) {
                        case 1:
                            TypeParameter[] tps = typeParams;
                            if (typeArgsForToBuilder != null) {
                                tps = new TypeParameter[typeArgsForToBuilder.size()];
                                for (int i3 = 0; i3 < tps.length; i3++) {
                                    tps[i3] = new TypeParameter();
                                    tps[i3].name = typeArgsForToBuilder.get(i3);
                                }
                            }
                            MethodDeclaration md6 = generateToBuilderMethod("toBuilder", builderClassName, tdParent, tps, builderFields, fluent, ast);
                            if (md6 != null) {
                                EclipseHandlerUtil.injectMethod(tdParent, md6);
                                break;
                            }
                            break;
                        case 3:
                            annotationNode.addWarning("Not generating toBuilder() as it already exists.");
                            break;
                    }
                }
            }
        }
    }

    private MethodDeclaration generateToBuilderMethod(String methodName, String builderClassName, EclipseNode type, TypeParameter[] typeParams, List<BuilderFieldData> builderFields, boolean fluent, ASTNode source) {
        int pS = source.sourceStart;
        int pE = source.sourceEnd;
        long p = (pS << 32) | pE;
        MethodDeclaration out = new MethodDeclaration(type.top().get().compilationResult);
        out.selector = methodName.toCharArray();
        out.modifiers = 1;
        out.bits |= 8388608;
        out.returnType = EclipseHandlerUtil.namePlusTypeParamsToTypeReference(builderClassName.toCharArray(), typeParams, p);
        Expression allocationExpression = new AllocationExpression();
        ((AllocationExpression) allocationExpression).type = EclipseHandlerUtil.namePlusTypeParamsToTypeReference(builderClassName.toCharArray(), typeParams, p);
        Expression receiver = allocationExpression;
        for (BuilderFieldData bfd : builderFields) {
            char[] setterName = fluent ? bfd.name : HandlerUtil.buildAccessorName("set", new String(bfd.name)).toCharArray();
            Expression messageSend = new MessageSend();
            if (bfd.obtainVia == null || !bfd.obtainVia.field().isEmpty()) {
                char[] fieldName = bfd.obtainVia == null ? bfd.rawName : bfd.obtainVia.field().toCharArray();
                Expression fieldReference = new FieldReference(fieldName, 0L);
                ((FieldReference) fieldReference).receiver = new ThisReference(0, 0);
                ((MessageSend) messageSend).arguments = new Expression[]{fieldReference};
            } else {
                String obtainName = bfd.obtainVia.method();
                boolean obtainIsStatic = bfd.obtainVia.isStatic();
                Expression messageSend2 = new MessageSend();
                ((MessageSend) messageSend2).receiver = obtainIsStatic ? new SingleNameReference(type.getName().toCharArray(), 0L) : new ThisReference(0, 0);
                ((MessageSend) messageSend2).selector = obtainName.toCharArray();
                if (obtainIsStatic) {
                    ((MessageSend) messageSend2).arguments = new Expression[]{new ThisReference(0, 0)};
                }
                ((MessageSend) messageSend).arguments = new Expression[]{messageSend2};
            }
            ((MessageSend) messageSend).receiver = receiver;
            ((MessageSend) messageSend).selector = setterName;
            receiver = messageSend;
        }
        out.statements = new Statement[]{new ReturnStatement(receiver, pS, pE)};
        out.traverse(new SetGeneratedByVisitor(source), type.get().scope);
        return out;
    }

    private MethodDeclaration generateCleanMethod(List<BuilderFieldData> builderFields, EclipseNode builderType, ASTNode source) {
        List<Statement> statements = new ArrayList<>();
        for (BuilderFieldData bfd : builderFields) {
            if (bfd.singularData != null && bfd.singularData.getSingularizer() != null) {
                bfd.singularData.getSingularizer().appendCleaningCode(bfd.singularData, builderType, statements);
            }
        }
        FieldReference thisUnclean = new FieldReference(CLEAN_FIELD_NAME, 0L);
        thisUnclean.receiver = new ThisReference(0, 0);
        statements.add(new Assignment(thisUnclean, new FalseLiteral(0, 0), 0));
        MethodDeclaration decl = new MethodDeclaration(builderType.top().get().compilationResult);
        decl.selector = CLEAN_METHOD_NAME;
        decl.modifiers = 2;
        decl.bits |= 8388608;
        decl.returnType = TypeReference.baseTypeReference(6, 0);
        decl.statements = (Statement[]) statements.toArray(new Statement[0]);
        decl.traverse(new SetGeneratedByVisitor(source), (ClassScope) null);
        return decl;
    }

    public MethodDeclaration generateBuildMethod(EclipseNode tdParent, boolean isStatic, String name, char[] staticName, TypeReference returnType, List<BuilderFieldData> builderFields, EclipseNode type, TypeReference[] thrownExceptions, boolean addCleaning, ASTNode source) {
        MethodDeclaration out = new MethodDeclaration(type.top().get().compilationResult);
        out.bits |= 8388608;
        List<Statement> statements = new ArrayList<>();
        if (addCleaning) {
            FieldReference thisUnclean = new FieldReference(CLEAN_FIELD_NAME, 0L);
            thisUnclean.receiver = new ThisReference(0, 0);
            UnaryExpression unaryExpression = new UnaryExpression(thisUnclean, 11);
            MessageSend invokeClean = new MessageSend();
            invokeClean.selector = CLEAN_METHOD_NAME;
            statements.add(new IfStatement(unaryExpression, invokeClean, 0, 0));
        }
        for (BuilderFieldData bfd : builderFields) {
            if (bfd.singularData != null && bfd.singularData.getSingularizer() != null) {
                bfd.singularData.getSingularizer().appendBuildCode(bfd.singularData, type, statements, bfd.name);
            }
        }
        List<Expression> args = new ArrayList<>();
        for (BuilderFieldData bfd2 : builderFields) {
            if (bfd2.nameOfSetFlag != null) {
                MessageSend inv = new MessageSend();
                inv.sourceStart = source.sourceStart;
                inv.sourceEnd = source.sourceEnd;
                inv.receiver = new SingleNameReference(tdParent.get().name, 0L);
                inv.selector = bfd2.nameOfDefaultProvider;
                inv.typeArguments = typeParameterNames(type.get().typeParameters);
                args.add(new ConditionalExpression(new SingleNameReference(bfd2.nameOfSetFlag, 0L), new SingleNameReference(bfd2.name, 0L), inv));
            } else {
                args.add(new SingleNameReference(bfd2.name, 0L));
            }
        }
        if (addCleaning) {
            FieldReference thisUnclean2 = new FieldReference(CLEAN_FIELD_NAME, 0L);
            thisUnclean2.receiver = new ThisReference(0, 0);
            statements.add(new Assignment(thisUnclean2, new TrueLiteral(0, 0), 0));
        }
        out.modifiers = 1;
        out.selector = name.toCharArray();
        out.thrownExceptions = EclipseHandlerUtil.copyTypes(thrownExceptions);
        out.bits |= 8388608;
        out.returnType = returnType;
        if (staticName == null) {
            AllocationExpression allocationStatement = new AllocationExpression();
            allocationStatement.type = EclipseHandlerUtil.copyType(out.returnType);
            allocationStatement.arguments = args.isEmpty() ? null : (Expression[]) args.toArray(new Expression[args.size()]);
            statements.add(new ReturnStatement(allocationStatement, 0, 0));
        } else {
            MessageSend invoke = new MessageSend();
            invoke.selector = staticName;
            if (isStatic) {
                invoke.receiver = new SingleNameReference(type.up().getName().toCharArray(), 0L);
            } else {
                invoke.receiver = new QualifiedThisReference(new SingleTypeReference(type.up().getName().toCharArray(), 0L), 0, 0);
            }
            invoke.typeArguments = typeParameterNames(type.get().typeParameters);
            invoke.arguments = args.isEmpty() ? null : (Expression[]) args.toArray(new Expression[args.size()]);
            if ((returnType instanceof SingleTypeReference) && Arrays.equals(TypeConstants.VOID, ((SingleTypeReference) returnType).token)) {
                statements.add(invoke);
            } else {
                statements.add(new ReturnStatement(invoke, 0, 0));
            }
        }
        out.statements = statements.isEmpty() ? null : (Statement[]) statements.toArray(new Statement[statements.size()]);
        out.traverse(new SetGeneratedByVisitor(source), (ClassScope) null);
        return out;
    }

    private TypeReference[] typeParameterNames(TypeParameter[] typeParameters) {
        if (typeParameters == null) {
            return null;
        }
        TypeReference[] trs = new TypeReference[typeParameters.length];
        for (int i = 0; i < trs.length; i++) {
            trs[i] = new SingleTypeReference(typeParameters[i].name, 0L);
        }
        return trs;
    }

    public MethodDeclaration generateDefaultProvider(char[] methodName, TypeParameter[] typeParameters, EclipseNode fieldNode, ASTNode source) {
        int pS = source.sourceStart;
        int pE = source.sourceEnd;
        MethodDeclaration out = new MethodDeclaration(fieldNode.top().get().compilationResult);
        out.typeParameters = EclipseHandlerUtil.copyTypeParams(typeParameters, source);
        out.selector = methodName;
        out.modifiers = 10;
        out.bits |= 8388608;
        FieldDeclaration fd = fieldNode.get();
        out.returnType = EclipseHandlerUtil.copyType(fd.type, source);
        out.statements = new Statement[]{new ReturnStatement(fd.initialization, pS, pE)};
        fd.initialization = null;
        out.traverse(new SetGeneratedByVisitor(source), fieldNode.up().get().scope);
        return out;
    }

    public MethodDeclaration generateBuilderMethod(boolean isStatic, String builderMethodName, String builderClassName, EclipseNode type, TypeParameter[] typeParams, ASTNode source) {
        int pS = source.sourceStart;
        int pE = source.sourceEnd;
        long p = (pS << 32) | pE;
        MethodDeclaration out = new MethodDeclaration(type.top().get().compilationResult);
        out.selector = builderMethodName.toCharArray();
        out.modifiers = 1;
        if (isStatic) {
            out.modifiers |= 8;
        }
        out.bits |= 8388608;
        out.returnType = EclipseHandlerUtil.namePlusTypeParamsToTypeReference(builderClassName.toCharArray(), typeParams, p);
        out.typeParameters = EclipseHandlerUtil.copyTypeParams(typeParams, source);
        AllocationExpression invoke = new AllocationExpression();
        invoke.type = EclipseHandlerUtil.namePlusTypeParamsToTypeReference(builderClassName.toCharArray(), typeParams, p);
        out.statements = new Statement[]{new ReturnStatement(invoke, pS, pE)};
        out.traverse(new SetGeneratedByVisitor(source), type.get().scope);
        return out;
    }

    public void generateBuilderFields(EclipseNode builderType, List<BuilderFieldData> builderFields, ASTNode source) {
        List<EclipseNode> existing = new ArrayList<>();
        Iterator<EclipseNode> it = builderType.down().iterator();
        while (it.hasNext()) {
            EclipseNode child = it.next();
            if (child.getKind() == AST.Kind.FIELD) {
                existing.add(child);
            }
        }
        for (BuilderFieldData bfd : builderFields) {
            if (bfd.singularData != null && bfd.singularData.getSingularizer() != null) {
                bfd.createdFields.addAll(bfd.singularData.getSingularizer().generateFields(bfd.singularData, builderType));
            } else {
                EclipseNode field = null;
                EclipseNode setFlag = null;
                for (EclipseNode exists : existing) {
                    char[] n = exists.get().name;
                    if (Arrays.equals(n, bfd.name)) {
                        field = exists;
                    }
                    if (bfd.nameOfSetFlag != null && Arrays.equals(n, bfd.nameOfSetFlag)) {
                        setFlag = exists;
                    }
                }
                if (field == null) {
                    FieldDeclaration fd = new FieldDeclaration(bfd.name, 0, 0);
                    fd.bits |= 8388608;
                    fd.modifiers = 2;
                    fd.type = EclipseHandlerUtil.copyType(bfd.type);
                    fd.traverse(new SetGeneratedByVisitor(source), (MethodScope) null);
                    field = EclipseHandlerUtil.injectFieldAndMarkGenerated(builderType, fd);
                }
                if (setFlag == null && bfd.nameOfSetFlag != null) {
                    FieldDeclaration fd2 = new FieldDeclaration(bfd.nameOfSetFlag, 0, 0);
                    fd2.bits |= 8388608;
                    fd2.modifiers = 2;
                    fd2.type = TypeReference.baseTypeReference(5, 0);
                    fd2.traverse(new SetGeneratedByVisitor(source), (MethodScope) null);
                    EclipseHandlerUtil.injectFieldAndMarkGenerated(builderType, fd2);
                }
                bfd.createdFields.add(field);
            }
        }
    }

    public void makeSetterMethodsForBuilder(EclipseNode builderType, BuilderFieldData bfd, EclipseNode sourceNode, boolean fluent, boolean chain) throws SecurityException {
        boolean deprecate = EclipseHandlerUtil.isFieldDeprecated(bfd.originalFieldNode);
        if (bfd.singularData == null || bfd.singularData.getSingularizer() == null) {
            makeSimpleSetterMethodForBuilder(builderType, deprecate, bfd.createdFields.get(0), bfd.nameOfSetFlag, sourceNode, fluent, chain);
        } else {
            bfd.singularData.getSingularizer().generateMethods(bfd.singularData, deprecate, builderType, fluent, chain);
        }
    }

    private void makeSimpleSetterMethodForBuilder(EclipseNode builderType, boolean deprecate, EclipseNode fieldNode, char[] nameOfSetFlag, EclipseNode sourceNode, boolean fluent, boolean chain) throws SecurityException {
        TypeDeclaration td = builderType.get();
        AbstractMethodDeclaration[] existing = td.methods;
        if (existing == null) {
            existing = EMPTY;
        }
        int len = existing.length;
        FieldDeclaration fd = fieldNode.get();
        char[] name = fd.name;
        for (int i = 0; i < len; i++) {
            if (existing[i] instanceof MethodDeclaration) {
                char[] existingName = existing[i].selector;
                if (Arrays.equals(name, existingName) && !EclipseHandlerUtil.isTolerate(fieldNode, existing[i])) {
                    return;
                }
            }
        }
        String setterName = fluent ? fieldNode.getName() : HandlerUtil.buildAccessorName("set", fieldNode.getName());
        MethodDeclaration setter = HandleSetter.createSetter(td, deprecate, fieldNode, setterName, nameOfSetFlag, chain, 1, sourceNode, Collections.emptyList(), Collections.emptyList());
        EclipseHandlerUtil.injectMethod(builderType, setter);
    }

    public EclipseNode findInnerClass(EclipseNode parent, String name) {
        char[] c = name.toCharArray();
        Iterator<EclipseNode> it = parent.down().iterator();
        while (it.hasNext()) {
            EclipseNode child = it.next();
            if (child.getKind() == AST.Kind.TYPE) {
                TypeDeclaration td = child.get();
                if (Arrays.equals(td.name, c)) {
                    return child;
                }
            }
        }
        return null;
    }

    public EclipseNode makeBuilderClass(boolean isStatic, EclipseNode tdParent, String builderClassName, TypeParameter[] typeParams, ASTNode source) {
        TypeDeclaration parent = tdParent.get();
        TypeDeclaration builder = new TypeDeclaration(parent.compilationResult);
        builder.bits |= 8388608;
        builder.modifiers |= 1;
        if (isStatic) {
            builder.modifiers |= 8;
        }
        builder.typeParameters = EclipseHandlerUtil.copyTypeParams(typeParams, source);
        builder.name = builderClassName.toCharArray();
        builder.traverse(new SetGeneratedByVisitor(source), (ClassScope) null);
        return EclipseHandlerUtil.injectType(tdParent, builder);
    }

    private void addObtainVia(BuilderFieldData bfd, EclipseNode node) throws SecurityException {
        Iterator<EclipseNode> it = node.down().iterator();
        while (it.hasNext()) {
            EclipseNode child = it.next();
            if (EclipseHandlerUtil.annotationTypeMatches((Class<? extends java.lang.annotation.Annotation>) Builder.ObtainVia.class, child)) {
                AnnotationValues<Builder.ObtainVia> ann = EclipseHandlerUtil.createAnnotation(Builder.ObtainVia.class, child);
                bfd.obtainVia = (Builder.ObtainVia) ann.getInstance();
                bfd.obtainViaNode = child;
                return;
            }
        }
    }

    private EclipseSingularsRecipes.SingularData getSingularData(EclipseNode node, ASTNode source) throws SecurityException {
        String typeName;
        Iterator<EclipseNode> it = node.down().iterator();
        while (it.hasNext()) {
            EclipseNode child = it.next();
            if (EclipseHandlerUtil.annotationTypeMatches((Class<? extends java.lang.annotation.Annotation>) Singular.class, child)) {
                char[] pluralName = node.getKind() == AST.Kind.FIELD ? EclipseHandlerUtil.removePrefixFromField(node) : node.get().name;
                AnnotationValues<Singular> ann = EclipseHandlerUtil.createAnnotation(Singular.class, child);
                String explicitSingular = ((Singular) ann.getInstance()).value();
                if (explicitSingular.isEmpty()) {
                    if (Boolean.FALSE.equals(node.getAst().readConfiguration(ConfigurationKeys.SINGULAR_AUTO))) {
                        node.addError("The singular must be specified explicitly (e.g. @Singular(\"task\")) because auto singularization is disabled.");
                        explicitSingular = new String(pluralName);
                    } else {
                        explicitSingular = HandlerUtil.autoSingularize(new String(pluralName));
                        if (explicitSingular == null) {
                            node.addError("Can't singularize this name; please specify the singular explicitly (i.e. @Singular(\"sheep\"))");
                            explicitSingular = new String(pluralName);
                        }
                    }
                }
                char[] singularName = explicitSingular.toCharArray();
                ParameterizedSingleTypeReference parameterizedSingleTypeReference = node.get().type;
                if (parameterizedSingleTypeReference instanceof ParameterizedSingleTypeReference) {
                    typeArgs = parameterizedSingleTypeReference.typeArguments;
                    typeName = new String(parameterizedSingleTypeReference.token);
                } else if (parameterizedSingleTypeReference instanceof ParameterizedQualifiedTypeReference) {
                    TypeReference[][] tr = ((ParameterizedQualifiedTypeReference) parameterizedSingleTypeReference).typeArguments;
                    typeArgs = tr != null ? tr[tr.length - 1] : null;
                    char[][] tokens = ((ParameterizedQualifiedTypeReference) parameterizedSingleTypeReference).tokens;
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < tokens.length; i++) {
                        if (i > 0) {
                            sb.append(".");
                        }
                        sb.append(tokens[i]);
                    }
                    typeName = sb.toString();
                } else {
                    typeName = parameterizedSingleTypeReference.toString();
                }
                String targetFqn = EclipseSingularsRecipes.get().toQualified(typeName);
                EclipseSingularsRecipes.EclipseSingularizer singularizer = EclipseSingularsRecipes.get().getSingularizer(targetFqn);
                if (singularizer == null) {
                    node.addError("Lombok does not know how to create the singular-form builder methods for type '" + typeName + "'; they won't be generated.");
                    return null;
                }
                return new EclipseSingularsRecipes.SingularData(child, singularName, pluralName, typeArgs == null ? Collections.emptyList() : Arrays.asList(typeArgs), targetFqn, singularizer, source);
            }
        }
        return null;
    }
}
