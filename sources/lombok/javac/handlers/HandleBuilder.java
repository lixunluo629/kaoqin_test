package lombok.javac.handlers;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.lang.model.element.Modifier;
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
import lombok.experimental.NonFinal;
import lombok.javac.Javac;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import lombok.javac.handlers.HandleConstructor;
import lombok.javac.handlers.JavacHandlerUtil;
import lombok.javac.handlers.JavacSingularsRecipes;
import org.apache.ibatis.ognl.OgnlContext;

@HandlerPriority(-1024)
/* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleBuilder.SCL.lombok */
public class HandleBuilder extends JavacAnnotationHandler<Builder> {
    private HandleConstructor handleConstructor = new HandleConstructor();

    private static final boolean toBoolean(Object expr, boolean defaultValue) {
        if (expr == null) {
            return defaultValue;
        }
        if (expr instanceof JCTree.JCLiteral) {
            return ((Integer) ((JCTree.JCLiteral) expr).value).intValue() != 0;
        }
        return ((Boolean) expr).booleanValue();
    }

    /* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleBuilder$BuilderFieldData.SCL.lombok */
    private static class BuilderFieldData {
        JCTree.JCExpression type;
        Name rawName;
        Name name;
        Name nameOfDefaultProvider;
        Name nameOfSetFlag;
        JavacSingularsRecipes.SingularData singularData;
        Builder.ObtainVia obtainVia;
        JavacNode obtainViaNode;
        JavacNode originalFieldNode;
        List<JavacNode> createdFields;

        private BuilderFieldData() {
            this.createdFields = new ArrayList();
        }
    }

    @Override // lombok.javac.JavacAnnotationHandler
    public void handle(AnnotationValues<Builder> annotation, JCTree.JCAnnotation ast, JavacNode annotationNode) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        JavacNode tdParent;
        JCTree.JCExpression returnType;
        com.sun.tools.javac.util.List<JCTree.JCTypeParameter> typeParams;
        com.sun.tools.javac.util.List<JCTree.JCExpression> thrownExceptions;
        Name nameOfBuilderMethod;
        Name simpleName;
        String pkg;
        JavacSingularsRecipes.JavacSingularizer singularizer;
        JCTree.JCMethodDecl md;
        JCTree.JCMethodDecl cd;
        Builder builderInstance = (Builder) annotation.getInstance();
        boolean fluent = toBoolean(annotation.getActualExpression("fluent"), true);
        boolean chain = toBoolean(annotation.getActualExpression("chain"), true);
        String builderMethodName = builderInstance.builderMethodName();
        String buildMethodName = builderInstance.buildMethodName();
        String builderClassName = builderInstance.builderClassName();
        boolean toBuilder = builderInstance.toBuilder();
        List<Name> typeArgsForToBuilder = null;
        if (builderMethodName == null) {
            builderMethodName = "builder";
        }
        if (buildMethodName == null) {
            buildMethodName = JsonPOJOBuilder.DEFAULT_BUILD_METHOD;
        }
        if (builderClassName == null) {
            builderClassName = "";
        }
        if (HandlerUtil.checkName("builderMethodName", builderMethodName, annotationNode) && HandlerUtil.checkName("buildMethodName", buildMethodName, annotationNode)) {
            if (builderClassName.isEmpty() || HandlerUtil.checkName("builderClassName", builderClassName, annotationNode)) {
                JavacHandlerUtil.deleteAnnotationIfNeccessary(annotationNode, (Class<? extends Annotation>) Builder.class, "lombok.experimental.Builder");
                JavacNode parent = annotationNode.up();
                List<BuilderFieldData> builderFields = new ArrayList<>();
                com.sun.tools.javac.util.List.nil();
                com.sun.tools.javac.util.List.nil();
                JavacNode fillParametersFrom = parent.get() instanceof JCTree.JCMethodDecl ? parent : null;
                boolean addCleaning = false;
                boolean isStatic = true;
                if (parent.get() instanceof JCTree.JCClassDecl) {
                    tdParent = parent;
                    JCTree.JCClassDecl td = tdParent.get();
                    ListBuffer<JavacNode> allFields = new ListBuffer<>();
                    boolean valuePresent = JavacHandlerUtil.hasAnnotation((Class<? extends Annotation>) Value.class, parent) || JavacHandlerUtil.hasAnnotation("lombok.experimental.Value", parent);
                    Iterator it = HandleConstructor.findAllFields(tdParent, true).iterator();
                    while (it.hasNext()) {
                        JavacNode fieldNode = (JavacNode) it.next();
                        JCTree.JCVariableDecl fd = fieldNode.get();
                        JavacNode isDefault = JavacHandlerUtil.findAnnotation(Builder.Default.class, fieldNode, true);
                        boolean isFinal = (fd.mods.flags & 16) != 0 || (valuePresent && !JavacHandlerUtil.hasAnnotation((Class<? extends Annotation>) NonFinal.class, fieldNode));
                        BuilderFieldData bfd = new BuilderFieldData();
                        bfd.rawName = fd.name;
                        bfd.name = JavacHandlerUtil.removePrefixFromField(fieldNode);
                        bfd.type = fd.vartype;
                        bfd.singularData = getSingularData(fieldNode);
                        bfd.originalFieldNode = fieldNode;
                        if (bfd.singularData != null && isDefault != null) {
                            isDefault.addError("@Builder.Default and @Singular cannot be mixed.");
                            isDefault = null;
                        }
                        if (fd.init == null && isDefault != null) {
                            isDefault.addWarning("@Builder.Default requires an initializing expression (' = something;').");
                            isDefault = null;
                        }
                        if (fd.init != null && isDefault == null) {
                            if (!isFinal) {
                                fieldNode.addWarning("@Builder will ignore the initializing expression entirely. If you want the initializing expression to serve as default, add @Builder.Default. If it is not supposed to be settable during building, make the field final.");
                            }
                        }
                        if (isDefault != null) {
                            bfd.nameOfDefaultProvider = parent.toName("$default$" + bfd.name);
                            bfd.nameOfSetFlag = parent.toName(bfd.name + "$set");
                            JCTree.JCMethodDecl md2 = generateDefaultProvider(bfd.nameOfDefaultProvider, fieldNode, td.typarams);
                            JavacHandlerUtil.recursiveSetGeneratedBy(md2, ast, annotationNode.getContext());
                            if (md2 != null) {
                                JavacHandlerUtil.injectMethod(tdParent, md2);
                            }
                        }
                        addObtainVia(bfd, fieldNode);
                        builderFields.add(bfd);
                        allFields.append(fieldNode);
                    }
                    this.handleConstructor.generateConstructor(tdParent, AccessLevel.PACKAGE, com.sun.tools.javac.util.List.nil(), allFields.toList(), false, null, HandleConstructor.SkipIfConstructorExists.I_AM_BUILDER, annotationNode);
                    returnType = JavacHandlerUtil.namePlusTypeParamsToTypeReference(tdParent.getTreeMaker(), td.name, td.typarams);
                    typeParams = td.typarams;
                    thrownExceptions = com.sun.tools.javac.util.List.nil();
                    nameOfBuilderMethod = null;
                    if (builderClassName.isEmpty()) {
                        builderClassName = td.name.toString() + "Builder";
                    }
                } else if (fillParametersFrom != null && fillParametersFrom.getName().toString().equals("<init>")) {
                    JCTree.JCMethodDecl jmd = fillParametersFrom.get();
                    if (!jmd.typarams.isEmpty()) {
                        annotationNode.addError("@Builder is not supported on constructors with constructor type parameters.");
                        return;
                    }
                    tdParent = parent.up();
                    JCTree.JCClassDecl td2 = tdParent.get();
                    returnType = JavacHandlerUtil.namePlusTypeParamsToTypeReference(tdParent.getTreeMaker(), td2.name, td2.typarams);
                    typeParams = td2.typarams;
                    thrownExceptions = jmd.thrown;
                    nameOfBuilderMethod = null;
                    if (builderClassName.isEmpty()) {
                        builderClassName = td2.name.toString() + "Builder";
                    }
                } else if (fillParametersFrom != null) {
                    tdParent = parent.up();
                    JCTree.JCClassDecl td3 = tdParent.get();
                    JCTree.JCMethodDecl jmd2 = fillParametersFrom.get();
                    isStatic = (jmd2.mods.flags & 8) != 0;
                    JCTree.JCExpression fullReturnType = jmd2.restype;
                    returnType = fullReturnType;
                    typeParams = jmd2.typarams;
                    thrownExceptions = jmd2.thrown;
                    nameOfBuilderMethod = jmd2.name;
                    if (returnType instanceof JCTree.JCTypeApply) {
                        returnType = JavacHandlerUtil.cloneType(tdParent.getTreeMaker(), returnType, ast, annotationNode.getContext());
                    }
                    if (builderClassName.isEmpty()) {
                        if (returnType instanceof JCTree.JCFieldAccess) {
                            builderClassName = ((JCTree.JCFieldAccess) returnType).name.toString() + "Builder";
                        } else if (returnType instanceof JCTree.JCIdent) {
                            Name n = ((JCTree.JCIdent) returnType).name;
                            Iterator it2 = typeParams.iterator();
                            while (it2.hasNext()) {
                                if (((JCTree.JCTypeParameter) it2.next()).name.equals(n)) {
                                    annotationNode.addError("@Builder requires specifying 'builderClassName' if used on methods with a type parameter as return type.");
                                    return;
                                }
                            }
                            builderClassName = n.toString() + "Builder";
                        } else if (returnType instanceof JCTree.JCPrimitiveTypeTree) {
                            builderClassName = returnType.toString() + "Builder";
                            if (Character.isLowerCase(builderClassName.charAt(0))) {
                                builderClassName = Character.toTitleCase(builderClassName.charAt(0)) + builderClassName.substring(1);
                            }
                        } else if (returnType instanceof JCTree.JCTypeApply) {
                            JCTree.JCFieldAccess jCFieldAccess = ((JCTree.JCTypeApply) returnType).clazz;
                            if (jCFieldAccess instanceof JCTree.JCFieldAccess) {
                                builderClassName = jCFieldAccess.name + "Builder";
                            } else if (jCFieldAccess instanceof JCTree.JCIdent) {
                                builderClassName = ((JCTree.JCIdent) jCFieldAccess).name + "Builder";
                            }
                        }
                        if (builderClassName.isEmpty()) {
                            System.err.println("Lombok bug ID#20140614-1651: javac HandleBuilder: return type to name conversion failed: " + returnType.getClass());
                            builderClassName = td3.name.toString() + "Builder";
                        }
                    }
                    if (toBuilder) {
                        if (returnType instanceof JCTree.JCArrayTypeTree) {
                            annotationNode.addError("@Builder(toBuilder=true) is only supported if you return your own type.");
                            return;
                        }
                        com.sun.tools.javac.util.List<JCTree.JCExpression> tpOnRet = com.sun.tools.javac.util.List.nil();
                        if (fullReturnType instanceof JCTree.JCTypeApply) {
                            tpOnRet = ((JCTree.JCTypeApply) fullReturnType).arguments;
                        }
                        JCTree.JCExpression namingType = returnType;
                        if (returnType instanceof JCTree.JCTypeApply) {
                            namingType = ((JCTree.JCTypeApply) returnType).clazz;
                        }
                        if (namingType instanceof JCTree.JCIdent) {
                            simpleName = ((JCTree.JCIdent) namingType).name;
                            pkg = null;
                        } else if (namingType instanceof JCTree.JCFieldAccess) {
                            JCTree.JCFieldAccess jcfa = (JCTree.JCFieldAccess) namingType;
                            simpleName = jcfa.name;
                            pkg = unpack(jcfa.selected);
                            if (pkg.startsWith("ERR:")) {
                                String err = pkg.substring(4, pkg.indexOf("__ERR__"));
                                annotationNode.addError(err);
                                return;
                            }
                        } else {
                            annotationNode.addError("Expected a (parameterized) type here instead of a " + namingType.getClass().getName());
                            return;
                        }
                        if (pkg != null && !parent.getPackageDeclaration().equals(pkg)) {
                            annotationNode.addError("@Builder(toBuilder=true) is only supported if you return your own type.");
                            return;
                        }
                        if (!tdParent.getName().contentEquals((CharSequence) simpleName)) {
                            annotationNode.addError("@Builder(toBuilder=true) is only supported if you return your own type.");
                            return;
                        }
                        com.sun.tools.javac.util.List<JCTree.JCTypeParameter> tpOnMethod = jmd2.typarams;
                        com.sun.tools.javac.util.List<JCTree.JCTypeParameter> tpOnType = tdParent.get().typarams;
                        typeArgsForToBuilder = new ArrayList<>();
                        Iterator it3 = tpOnMethod.iterator();
                        while (it3.hasNext()) {
                            JCTree.JCTypeParameter tp = (JCTree.JCTypeParameter) it3.next();
                            int pos = -1;
                            int idx = -1;
                            Iterator it4 = tpOnRet.iterator();
                            while (it4.hasNext()) {
                                JCTree.JCIdent jCIdent = (JCTree.JCExpression) it4.next();
                                idx++;
                                if ((jCIdent instanceof JCTree.JCIdent) && jCIdent.name == tp.name) {
                                    pos = idx;
                                }
                            }
                            if (pos == -1 || tpOnType.size() <= pos) {
                                annotationNode.addError("@Builder(toBuilder=true) requires that each type parameter on the static method is part of the typeargs of the return value. Type parameter " + tp.name + " is not part of the return type.");
                                return;
                            }
                            typeArgsForToBuilder.add(((JCTree.JCTypeParameter) tpOnType.get(pos)).name);
                        }
                    }
                } else {
                    annotationNode.addError("@Builder is only supported on types, constructors, and methods.");
                    return;
                }
                if (fillParametersFrom != null) {
                    Iterator<JavacNode> it5 = fillParametersFrom.down().iterator();
                    while (it5.hasNext()) {
                        JavacNode param = it5.next();
                        if (param.getKind() == AST.Kind.ARGUMENT) {
                            BuilderFieldData bfd2 = new BuilderFieldData();
                            JCTree.JCVariableDecl raw = param.get();
                            bfd2.name = raw.name;
                            bfd2.rawName = raw.name;
                            bfd2.type = raw.vartype;
                            bfd2.singularData = getSingularData(param);
                            bfd2.originalFieldNode = param;
                            addObtainVia(bfd2, param);
                            builderFields.add(bfd2);
                        }
                    }
                }
                JavacNode builderType = findInnerClass(tdParent, builderClassName);
                if (builderType == null) {
                    builderType = makeBuilderClass(isStatic, annotationNode, tdParent, builderClassName, typeParams, ast);
                } else {
                    JCTree.JCClassDecl builderTypeDeclaration = builderType.get();
                    if (isStatic && !builderTypeDeclaration.getModifiers().getFlags().contains(Modifier.STATIC)) {
                        annotationNode.addError("Existing Builder must be a static inner class.");
                        return;
                    }
                    if (!isStatic && builderTypeDeclaration.getModifiers().getFlags().contains(Modifier.STATIC)) {
                        annotationNode.addError("Existing Builder must be a non-static inner class.");
                        return;
                    }
                    JavacHandlerUtil.sanityCheckForMethodGeneratingAnnotationsOnBuilderClass(builderType, annotationNode);
                    for (BuilderFieldData bfd3 : builderFields) {
                        JavacSingularsRecipes.SingularData sd = bfd3.singularData;
                        if (sd != null && (singularizer = sd.getSingularizer()) != null && singularizer.checkForAlreadyExistingNodesAndGenerateError(builderType, sd)) {
                            bfd3.singularData = null;
                        }
                    }
                }
                Iterator<BuilderFieldData> it6 = builderFields.iterator();
                while (true) {
                    if (!it6.hasNext()) {
                        break;
                    }
                    BuilderFieldData bfd4 = it6.next();
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
                    JavacTreeMaker maker = builderType.getTreeMaker();
                    JCTree.JCVariableDecl uncleanField = maker.VarDef(maker.Modifiers(2L), builderType.toName("$lombokUnclean"), maker.TypeIdent(Javac.CTC_BOOLEAN), null);
                    JavacHandlerUtil.injectFieldAndMarkGenerated(builderType, uncleanField);
                }
                if (JavacHandlerUtil.constructorExists(builderType) == JavacHandlerUtil.MemberExistsResult.NOT_EXISTS && (cd = HandleConstructor.createConstructor(AccessLevel.PACKAGE, com.sun.tools.javac.util.List.nil(), builderType, com.sun.tools.javac.util.List.nil(), false, annotationNode)) != null) {
                    JavacHandlerUtil.injectMethod(builderType, cd);
                }
                Iterator<BuilderFieldData> it7 = builderFields.iterator();
                while (it7.hasNext()) {
                    makeSetterMethodsForBuilder(builderType, it7.next(), annotationNode, fluent, chain);
                }
                if (JavacHandlerUtil.methodExists(buildMethodName, builderType, -1) == JavacHandlerUtil.MemberExistsResult.NOT_EXISTS && (md = generateBuildMethod(tdParent, isStatic, buildMethodName, nameOfBuilderMethod, returnType, builderFields, builderType, thrownExceptions, ast, addCleaning)) != null) {
                    JavacHandlerUtil.injectMethod(builderType, md);
                }
                if (JavacHandlerUtil.methodExists("toString", builderType, 0) == JavacHandlerUtil.MemberExistsResult.NOT_EXISTS) {
                    List<InclusionExclusionUtils.Included<JavacNode, ToString.Include>> fieldNodes = new ArrayList<>();
                    Iterator<BuilderFieldData> it8 = builderFields.iterator();
                    while (it8.hasNext()) {
                        for (JavacNode f : it8.next().createdFields) {
                            fieldNodes.add(new InclusionExclusionUtils.Included<>(f, null, true));
                        }
                    }
                    JCTree.JCMethodDecl md3 = HandleToString.createToString(builderType, fieldNodes, true, false, HandlerUtil.FieldAccess.ALWAYS_FIELD, ast);
                    if (md3 != null) {
                        JavacHandlerUtil.injectMethod(builderType, md3);
                    }
                }
                if (addCleaning) {
                    JavacHandlerUtil.injectMethod(builderType, generateCleanMethod(builderFields, builderType, ast));
                }
                if (JavacHandlerUtil.methodExists(builderMethodName, tdParent, -1) == JavacHandlerUtil.MemberExistsResult.NOT_EXISTS) {
                    JCTree.JCMethodDecl md4 = generateBuilderMethod(isStatic, builderMethodName, builderClassName, annotationNode, tdParent, typeParams);
                    JavacHandlerUtil.recursiveSetGeneratedBy(md4, ast, annotationNode.getContext());
                    if (md4 != null) {
                        JavacHandlerUtil.injectMethod(tdParent, md4);
                    }
                }
                if (toBuilder) {
                    switch (JavacHandlerUtil.methodExists("toBuilder", tdParent, 0)) {
                        case EXISTS_BY_USER:
                            annotationNode.addWarning("Not generating toBuilder() as it already exists.");
                            return;
                        case NOT_EXISTS:
                            com.sun.tools.javac.util.List<JCTree.JCTypeParameter> tps = typeParams;
                            if (typeArgsForToBuilder != null) {
                                ListBuffer<JCTree.JCTypeParameter> lb = new ListBuffer<>();
                                JavacTreeMaker maker2 = tdParent.getTreeMaker();
                                Iterator<Name> it9 = typeArgsForToBuilder.iterator();
                                while (it9.hasNext()) {
                                    lb.append(maker2.TypeParameter(it9.next(), com.sun.tools.javac.util.List.nil()));
                                }
                                tps = lb.toList();
                            }
                            JCTree.JCMethodDecl md5 = generateToBuilderMethod("toBuilder", builderClassName, tdParent, tps, builderFields, fluent, ast);
                            if (md5 != null) {
                                JavacHandlerUtil.injectMethod(tdParent, md5);
                                break;
                            }
                            break;
                    }
                }
                JavacHandlerUtil.recursiveSetGeneratedBy(builderType.get(), ast, annotationNode.getContext());
            }
        }
    }

    private static String unpack(JCTree.JCExpression expr) {
        StringBuilder sb = new StringBuilder();
        unpack(sb, expr);
        return sb.toString();
    }

    private static void unpack(StringBuilder sb, JCTree.JCExpression expr) {
        if (expr instanceof JCTree.JCIdent) {
            sb.append(((JCTree.JCIdent) expr).name.toString());
            return;
        }
        if (expr instanceof JCTree.JCFieldAccess) {
            JCTree.JCFieldAccess jcfa = (JCTree.JCFieldAccess) expr;
            unpack(sb, jcfa.selected);
            sb.append(".").append(jcfa.name.toString());
        } else {
            if (expr instanceof JCTree.JCTypeApply) {
                sb.setLength(0);
                sb.append("ERR:");
                sb.append("@Builder(toBuilder=true) is not supported if returning a type with generics applied to an intermediate.");
                sb.append("__ERR__");
                return;
            }
            sb.setLength(0);
            sb.append("ERR:");
            sb.append("Expected a type of some sort, not a " + expr.getClass().getName());
            sb.append("__ERR__");
        }
    }

    private JCTree.JCMethodDecl generateToBuilderMethod(String toBuilderMethodName, String builderClassName, JavacNode type, com.sun.tools.javac.util.List<JCTree.JCTypeParameter> typeParams, List<BuilderFieldData> builderFields, boolean fluent, JCTree.JCAnnotation ast) {
        JCTree.JCMethodInvocation jCMethodInvocationSelect;
        JavacTreeMaker maker = type.getTreeMaker();
        ListBuffer<JCTree.JCExpression> typeArgs = new ListBuffer<>();
        Iterator it = typeParams.iterator();
        while (it.hasNext()) {
            JCTree.JCTypeParameter typeParam = (JCTree.JCTypeParameter) it.next();
            typeArgs.append(maker.Ident(typeParam.name));
        }
        JCTree.JCMethodInvocation jCMethodInvocationNewClass = maker.NewClass(null, com.sun.tools.javac.util.List.nil(), JavacHandlerUtil.namePlusTypeParamsToTypeReference(maker, type.toName(builderClassName), typeParams), com.sun.tools.javac.util.List.nil(), null);
        for (BuilderFieldData bfd : builderFields) {
            Name setterName = fluent ? bfd.name : type.toName(HandlerUtil.buildAccessorName("set", bfd.name.toString()));
            if (bfd.obtainVia == null || !bfd.obtainVia.field().isEmpty()) {
                jCMethodInvocationSelect = maker.Select(maker.Ident(type.toName(OgnlContext.THIS_CONTEXT_KEY)), bfd.obtainVia == null ? bfd.rawName : type.toName(bfd.obtainVia.field()));
            } else if (bfd.obtainVia.isStatic()) {
                jCMethodInvocationSelect = maker.Apply(com.sun.tools.javac.util.List.nil(), maker.Select(maker.Ident(type.toName(type.getName())), type.toName(bfd.obtainVia.method())), com.sun.tools.javac.util.List.of(maker.Ident(type.toName(OgnlContext.THIS_CONTEXT_KEY))));
            } else {
                jCMethodInvocationSelect = maker.Apply(com.sun.tools.javac.util.List.nil(), maker.Select(maker.Ident(type.toName(OgnlContext.THIS_CONTEXT_KEY)), type.toName(bfd.obtainVia.method())), com.sun.tools.javac.util.List.nil());
            }
            jCMethodInvocationNewClass = maker.Apply(com.sun.tools.javac.util.List.nil(), maker.Select(jCMethodInvocationNewClass, setterName), com.sun.tools.javac.util.List.of(jCMethodInvocationSelect));
        }
        JCTree.JCBlock body = maker.Block(0L, com.sun.tools.javac.util.List.of(maker.Return(jCMethodInvocationNewClass)));
        return maker.MethodDef(maker.Modifiers(1L), type.toName(toBuilderMethodName), JavacHandlerUtil.namePlusTypeParamsToTypeReference(maker, type.toName(builderClassName), typeParams), com.sun.tools.javac.util.List.nil(), com.sun.tools.javac.util.List.nil(), com.sun.tools.javac.util.List.nil(), body, null);
    }

    private JCTree.JCMethodDecl generateCleanMethod(List<BuilderFieldData> builderFields, JavacNode type, JCTree source) {
        JavacTreeMaker maker = type.getTreeMaker();
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        for (BuilderFieldData bfd : builderFields) {
            if (bfd.singularData != null && bfd.singularData.getSingularizer() != null) {
                bfd.singularData.getSingularizer().appendCleaningCode(bfd.singularData, type, source, statements);
            }
        }
        statements.append(maker.Exec(maker.Assign(maker.Select(maker.Ident(type.toName(OgnlContext.THIS_CONTEXT_KEY)), type.toName("$lombokUnclean")), maker.Literal(Javac.CTC_BOOLEAN, 0))));
        JCTree.JCBlock body = maker.Block(0L, statements.toList());
        return maker.MethodDef(maker.Modifiers(1L), type.toName("$lombokClean"), maker.Type(Javac.createVoidType(type.getSymbolTable(), Javac.CTC_VOID)), com.sun.tools.javac.util.List.nil(), com.sun.tools.javac.util.List.nil(), com.sun.tools.javac.util.List.nil(), body, null);
    }

    private JCTree.JCMethodDecl generateBuildMethod(JavacNode tdParent, boolean isStatic, String buildName, Name builderName, JCTree.JCExpression returnType, List<BuilderFieldData> builderFields, JavacNode type, com.sun.tools.javac.util.List<JCTree.JCExpression> thrownExceptions, JCTree source, boolean addCleaning) {
        JavacTreeMaker maker = type.getTreeMaker();
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        if (addCleaning) {
            JCTree.JCIf ifUnclean = maker.If(maker.Unary(Javac.CTC_NOT, maker.Select(maker.Ident(type.toName(OgnlContext.THIS_CONTEXT_KEY)), type.toName("$lombokUnclean"))), maker.Exec(maker.Apply(com.sun.tools.javac.util.List.nil(), maker.Ident(type.toName("$lombokClean")), com.sun.tools.javac.util.List.nil())), null);
            statements.append(ifUnclean);
        }
        for (BuilderFieldData bfd : builderFields) {
            if (bfd.singularData != null && bfd.singularData.getSingularizer() != null) {
                bfd.singularData.getSingularizer().appendBuildCode(bfd.singularData, type, source, statements, bfd.name);
            }
        }
        ListBuffer<JCTree.JCExpression> args = new ListBuffer<>();
        for (BuilderFieldData bfd2 : builderFields) {
            if (bfd2.nameOfSetFlag != null) {
                statements.append(maker.VarDef(maker.Modifiers(0L), bfd2.name, JavacHandlerUtil.cloneType(maker, bfd2.type, source, tdParent.getContext()), maker.Select(maker.Ident(type.toName(OgnlContext.THIS_CONTEXT_KEY)), bfd2.name)));
                statements.append(maker.If(maker.Unary(Javac.CTC_NOT, maker.Ident(bfd2.nameOfSetFlag)), maker.Exec(maker.Assign(maker.Ident(bfd2.name), maker.Apply(JavacHandlerUtil.typeParameterNames(maker, tdParent.get().typarams), maker.Select(maker.Ident(tdParent.get().name), bfd2.nameOfDefaultProvider), com.sun.tools.javac.util.List.nil()))), null));
            }
            args.append(maker.Ident(bfd2.name));
        }
        if (addCleaning) {
            statements.append(maker.Exec(maker.Assign(maker.Select(maker.Ident(type.toName(OgnlContext.THIS_CONTEXT_KEY)), type.toName("$lombokUnclean")), maker.Literal(Javac.CTC_BOOLEAN, 1))));
        }
        if (builderName == null) {
            statements.append(maker.Return(maker.NewClass(null, com.sun.tools.javac.util.List.nil(), returnType, args.toList(), null)));
        } else {
            ListBuffer<JCTree.JCExpression> typeParams = new ListBuffer<>();
            Iterator it = type.get().typarams.iterator();
            while (it.hasNext()) {
                JCTree.JCTypeParameter tp = (JCTree.JCTypeParameter) it.next();
                typeParams.append(maker.Ident(tp.name));
            }
            JCTree.JCExpression callee = maker.Ident(type.up().get().name);
            if (!isStatic) {
                callee = maker.Select(callee, type.up().toName(OgnlContext.THIS_CONTEXT_KEY));
            }
            JCTree.JCMethodInvocation jCMethodInvocationApply = maker.Apply(typeParams.toList(), maker.Select(callee, builderName), args.toList());
            if ((returnType instanceof JCTree.JCPrimitiveTypeTree) && Javac.CTC_VOID.equals(JavacTreeMaker.TypeTag.typeTag((JCTree) returnType))) {
                statements.append(maker.Exec(jCMethodInvocationApply));
            } else {
                statements.append(maker.Return(jCMethodInvocationApply));
            }
        }
        JCTree.JCBlock body = maker.Block(0L, statements.toList());
        return maker.MethodDef(maker.Modifiers(1L), type.toName(buildName), returnType, com.sun.tools.javac.util.List.nil(), com.sun.tools.javac.util.List.nil(), thrownExceptions, body, null);
    }

    public JCTree.JCMethodDecl generateDefaultProvider(Name methodName, JavacNode fieldNode, com.sun.tools.javac.util.List<JCTree.JCTypeParameter> params) {
        JavacTreeMaker maker = fieldNode.getTreeMaker();
        JCTree.JCVariableDecl field = fieldNode.get();
        JCTree.JCReturn jCReturnReturn = maker.Return(field.init);
        field.init = null;
        JCTree.JCBlock body = maker.Block(0L, com.sun.tools.javac.util.List.of(jCReturnReturn));
        return maker.MethodDef(maker.Modifiers(10), methodName, JavacHandlerUtil.cloneType(maker, field.vartype, field, fieldNode.getContext()), JavacHandlerUtil.copyTypeParams(fieldNode, params), com.sun.tools.javac.util.List.nil(), com.sun.tools.javac.util.List.nil(), body, null);
    }

    public JCTree.JCMethodDecl generateBuilderMethod(boolean isStatic, String builderMethodName, String builderClassName, JavacNode source, JavacNode type, com.sun.tools.javac.util.List<JCTree.JCTypeParameter> typeParams) {
        JavacTreeMaker maker = type.getTreeMaker();
        ListBuffer<JCTree.JCExpression> typeArgs = new ListBuffer<>();
        Iterator it = typeParams.iterator();
        while (it.hasNext()) {
            JCTree.JCTypeParameter typeParam = (JCTree.JCTypeParameter) it.next();
            typeArgs.append(maker.Ident(typeParam.name));
        }
        JCTree.JCBlock body = maker.Block(0L, com.sun.tools.javac.util.List.of(maker.Return(maker.NewClass(null, com.sun.tools.javac.util.List.nil(), JavacHandlerUtil.namePlusTypeParamsToTypeReference(maker, type.toName(builderClassName), typeParams), com.sun.tools.javac.util.List.nil(), null))));
        int modifiers = 1;
        if (isStatic) {
            modifiers = 1 | 8;
        }
        return maker.MethodDef(maker.Modifiers(modifiers), type.toName(builderMethodName), JavacHandlerUtil.namePlusTypeParamsToTypeReference(maker, type.toName(builderClassName), typeParams), JavacHandlerUtil.copyTypeParams(source, typeParams), com.sun.tools.javac.util.List.nil(), com.sun.tools.javac.util.List.nil(), body, null);
    }

    public void generateBuilderFields(JavacNode builderType, List<BuilderFieldData> builderFields, JCTree source) {
        int len = builderFields.size();
        List<JavacNode> existing = new ArrayList<>();
        Iterator<JavacNode> it = builderType.down().iterator();
        while (it.hasNext()) {
            JavacNode child = it.next();
            if (child.getKind() == AST.Kind.FIELD) {
                existing.add(child);
            }
        }
        for (int i = len - 1; i >= 0; i--) {
            BuilderFieldData bfd = builderFields.get(i);
            if (bfd.singularData != null && bfd.singularData.getSingularizer() != null) {
                bfd.createdFields.addAll(bfd.singularData.getSingularizer().generateFields(bfd.singularData, builderType, source));
            } else {
                JavacNode field = null;
                JavacNode setFlag = null;
                for (JavacNode exists : existing) {
                    Name n = exists.get().name;
                    if (n.equals(bfd.name)) {
                        field = exists;
                    }
                    if (n.equals(bfd.nameOfSetFlag)) {
                        setFlag = exists;
                    }
                }
                JavacTreeMaker maker = builderType.getTreeMaker();
                if (field == null) {
                    JCTree.JCModifiers mods = maker.Modifiers(2L);
                    JCTree.JCVariableDecl newField = maker.VarDef(mods, bfd.name, JavacHandlerUtil.cloneType(maker, bfd.type, source, builderType.getContext()), null);
                    field = JavacHandlerUtil.injectFieldAndMarkGenerated(builderType, newField);
                }
                if (setFlag == null && bfd.nameOfSetFlag != null) {
                    JCTree.JCModifiers mods2 = maker.Modifiers(2L);
                    JCTree.JCVariableDecl newField2 = maker.VarDef(mods2, bfd.nameOfSetFlag, maker.TypeIdent(Javac.CTC_BOOLEAN), null);
                    JavacHandlerUtil.injectFieldAndMarkGenerated(builderType, newField2);
                }
                bfd.createdFields.add(field);
            }
        }
    }

    public void makeSetterMethodsForBuilder(JavacNode builderType, BuilderFieldData fieldNode, JavacNode source, boolean fluent, boolean chain) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        boolean deprecate = JavacHandlerUtil.isFieldDeprecated(fieldNode.originalFieldNode);
        if (fieldNode.singularData == null || fieldNode.singularData.getSingularizer() == null) {
            makeSimpleSetterMethodForBuilder(builderType, deprecate, fieldNode.createdFields.get(0), fieldNode.nameOfSetFlag, source, fluent, chain);
        } else {
            fieldNode.singularData.getSingularizer().generateMethods(fieldNode.singularData, deprecate, builderType, source.get(), fluent, chain);
        }
    }

    private void makeSimpleSetterMethodForBuilder(JavacNode builderType, boolean deprecate, JavacNode fieldNode, Name nameOfSetFlag, JavacNode source, boolean fluent, boolean chain) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Name fieldName = fieldNode.get().name;
        Iterator<JavacNode> it = builderType.down().iterator();
        while (it.hasNext()) {
            JavacNode child = it.next();
            if (child.getKind() == AST.Kind.METHOD) {
                JCTree.JCMethodDecl methodDecl = child.get();
                Name existingName = methodDecl.name;
                if (existingName.equals(fieldName) && !JavacHandlerUtil.isTolerate(fieldNode, methodDecl)) {
                    return;
                }
            }
        }
        String setterName = fluent ? fieldNode.getName() : HandlerUtil.buildAccessorName("set", fieldNode.getName());
        JavacTreeMaker maker = fieldNode.getTreeMaker();
        JCTree.JCMethodDecl newMethod = HandleSetter.createSetter(1L, deprecate, fieldNode, maker, setterName, nameOfSetFlag, chain, source, com.sun.tools.javac.util.List.nil(), com.sun.tools.javac.util.List.nil());
        JavacHandlerUtil.injectMethod(builderType, newMethod);
    }

    public JavacNode findInnerClass(JavacNode parent, String name) {
        Iterator<JavacNode> it = parent.down().iterator();
        while (it.hasNext()) {
            JavacNode child = it.next();
            if (child.getKind() == AST.Kind.TYPE) {
                JCTree.JCClassDecl td = child.get();
                if (td.name.contentEquals(name)) {
                    return child;
                }
            }
        }
        return null;
    }

    public JavacNode makeBuilderClass(boolean isStatic, JavacNode source, JavacNode tdParent, String builderClassName, com.sun.tools.javac.util.List<JCTree.JCTypeParameter> typeParams, JCTree.JCAnnotation ast) {
        JavacTreeMaker maker = tdParent.getTreeMaker();
        int modifiers = 1;
        if (isStatic) {
            modifiers = 1 | 8;
        }
        JCTree.JCModifiers mods = maker.Modifiers(modifiers);
        JCTree.JCClassDecl builder = maker.ClassDef(mods, tdParent.toName(builderClassName), JavacHandlerUtil.copyTypeParams(source, typeParams), null, com.sun.tools.javac.util.List.nil(), com.sun.tools.javac.util.List.nil());
        return JavacHandlerUtil.injectType(tdParent, builder);
    }

    private void addObtainVia(BuilderFieldData bfd, JavacNode node) {
        Iterator<JavacNode> it = node.down().iterator();
        while (it.hasNext()) {
            JavacNode child = it.next();
            if (JavacHandlerUtil.annotationTypeMatches((Class<? extends Annotation>) Builder.ObtainVia.class, child)) {
                AnnotationValues<Builder.ObtainVia> ann = JavacHandlerUtil.createAnnotation(Builder.ObtainVia.class, child);
                bfd.obtainVia = (Builder.ObtainVia) ann.getInstance();
                bfd.obtainViaNode = child;
                JavacHandlerUtil.deleteAnnotationIfNeccessary(child, (Class<? extends Annotation>) Builder.ObtainVia.class);
                return;
            }
        }
    }

    private JavacSingularsRecipes.SingularData getSingularData(JavacNode node) {
        Iterator<JavacNode> it = node.down().iterator();
        while (it.hasNext()) {
            JavacNode child = it.next();
            if (JavacHandlerUtil.annotationTypeMatches((Class<? extends Annotation>) Singular.class, child)) {
                Name pluralName = node.getKind() == AST.Kind.FIELD ? JavacHandlerUtil.removePrefixFromField(node) : node.get().name;
                AnnotationValues<Singular> ann = JavacHandlerUtil.createAnnotation(Singular.class, child);
                JavacHandlerUtil.deleteAnnotationIfNeccessary(child, (Class<? extends Annotation>) Singular.class);
                String explicitSingular = ((Singular) ann.getInstance()).value();
                if (explicitSingular.isEmpty()) {
                    if (Boolean.FALSE.equals(node.getAst().readConfiguration(ConfigurationKeys.SINGULAR_AUTO))) {
                        node.addError("The singular must be specified explicitly (e.g. @Singular(\"task\")) because auto singularization is disabled.");
                        explicitSingular = pluralName.toString();
                    } else {
                        explicitSingular = HandlerUtil.autoSingularize(pluralName.toString());
                        if (explicitSingular == null) {
                            node.addError("Can't singularize this name; please specify the singular explicitly (i.e. @Singular(\"sheep\"))");
                            explicitSingular = pluralName.toString();
                        }
                    }
                }
                Name singularName = node.toName(explicitSingular);
                JCTree.JCExpression type = null;
                if (node.get() instanceof JCTree.JCVariableDecl) {
                    type = node.get().vartype;
                }
                com.sun.tools.javac.util.List<JCTree.JCExpression> typeArgs = com.sun.tools.javac.util.List.nil();
                if (type instanceof JCTree.JCTypeApply) {
                    typeArgs = ((JCTree.JCTypeApply) type).arguments;
                    type = ((JCTree.JCTypeApply) type).clazz;
                }
                String name = type.toString();
                String targetFqn = JavacSingularsRecipes.get().toQualified(name);
                JavacSingularsRecipes.JavacSingularizer singularizer = JavacSingularsRecipes.get().getSingularizer(targetFqn);
                if (singularizer == null) {
                    node.addError("Lombok does not know how to create the singular-form builder methods for type '" + name + "'; they won't be generated.");
                    return null;
                }
                return new JavacSingularsRecipes.SingularData(child, singularName, pluralName, typeArgs, targetFqn, singularizer);
            }
        }
        return null;
    }
}
