package lombok.javac.handlers;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import javax.lang.model.type.TypeKind;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ConfigurationKeys;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.core.handlers.HandlerUtil;
import lombok.delombok.LombokOptionsFactory;
import lombok.javac.Javac;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import lombok.javac.handlers.JavacHandlerUtil;
import org.apache.ibatis.ognl.OgnlContext;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader;

/* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleConstructor.SCL.lombok */
public class HandleConstructor {

    /* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleConstructor$SkipIfConstructorExists.SCL.lombok */
    public enum SkipIfConstructorExists {
        YES,
        NO,
        I_AM_BUILDER
    }

    /* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleConstructor$HandleNoArgsConstructor.SCL.lombok */
    public static class HandleNoArgsConstructor extends JavacAnnotationHandler<NoArgsConstructor> {
        private HandleConstructor handleConstructor = new HandleConstructor();

        @Override // lombok.javac.JavacAnnotationHandler
        public void handle(AnnotationValues<NoArgsConstructor> annotation, JCTree.JCAnnotation ast, JavacNode annotationNode) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.NO_ARGS_CONSTRUCTOR_FLAG_USAGE, "@NoArgsConstructor", ConfigurationKeys.ANY_CONSTRUCTOR_FLAG_USAGE, "any @xArgsConstructor");
            JavacHandlerUtil.deleteAnnotationIfNeccessary(annotationNode, (Class<? extends Annotation>) NoArgsConstructor.class);
            JavacHandlerUtil.deleteImportFromCompilationUnit(annotationNode, "lombok.AccessLevel");
            JavacNode typeNode = annotationNode.up();
            if (HandleConstructor.checkLegality(typeNode, annotationNode, NoArgsConstructor.class.getSimpleName())) {
                List<JCTree.JCAnnotation> onConstructor = JavacHandlerUtil.unboxAndRemoveAnnotationParameter(ast, "onConstructor", "@NoArgsConstructor(onConstructor", annotationNode);
                NoArgsConstructor ann = (NoArgsConstructor) annotation.getInstance();
                AccessLevel level = ann.access();
                if (level == AccessLevel.NONE) {
                    return;
                }
                String staticName = ann.staticName();
                boolean force = ann.force();
                List<JavacNode> fields = force ? HandleConstructor.findFinalFields(typeNode) : List.nil();
                this.handleConstructor.generateConstructor(typeNode, level, onConstructor, fields, force, staticName, SkipIfConstructorExists.NO, annotationNode);
            }
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleConstructor$HandleRequiredArgsConstructor.SCL.lombok */
    public static class HandleRequiredArgsConstructor extends JavacAnnotationHandler<RequiredArgsConstructor> {
        private HandleConstructor handleConstructor = new HandleConstructor();

        @Override // lombok.javac.JavacAnnotationHandler
        public void handle(AnnotationValues<RequiredArgsConstructor> annotation, JCTree.JCAnnotation ast, JavacNode annotationNode) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.REQUIRED_ARGS_CONSTRUCTOR_FLAG_USAGE, "@RequiredArgsConstructor", ConfigurationKeys.ANY_CONSTRUCTOR_FLAG_USAGE, "any @xArgsConstructor");
            JavacHandlerUtil.deleteAnnotationIfNeccessary(annotationNode, (Class<? extends Annotation>) RequiredArgsConstructor.class);
            JavacHandlerUtil.deleteImportFromCompilationUnit(annotationNode, "lombok.AccessLevel");
            JavacNode typeNode = annotationNode.up();
            if (HandleConstructor.checkLegality(typeNode, annotationNode, RequiredArgsConstructor.class.getSimpleName())) {
                List<JCTree.JCAnnotation> onConstructor = JavacHandlerUtil.unboxAndRemoveAnnotationParameter(ast, "onConstructor", "@RequiredArgsConstructor(onConstructor", annotationNode);
                RequiredArgsConstructor ann = (RequiredArgsConstructor) annotation.getInstance();
                AccessLevel level = ann.access();
                if (level == AccessLevel.NONE) {
                    return;
                }
                String staticName = ann.staticName();
                if (annotation.isExplicit("suppressConstructorProperties")) {
                    annotationNode.addError("This deprecated feature is no longer supported. Remove it; you can create a lombok.config file with 'lombok.anyConstructor.suppressConstructorProperties = true'.");
                }
                this.handleConstructor.generateConstructor(typeNode, level, onConstructor, HandleConstructor.findRequiredFields(typeNode), false, staticName, SkipIfConstructorExists.NO, annotationNode);
            }
        }
    }

    public static List<JavacNode> findRequiredFields(JavacNode typeNode) {
        return findFields(typeNode, true);
    }

    public static List<JavacNode> findFinalFields(JavacNode typeNode) {
        return findFields(typeNode, false);
    }

    public static List<JavacNode> findFields(JavacNode typeNode, boolean nullMarked) {
        ListBuffer<JavacNode> fields = new ListBuffer<>();
        Iterator<JavacNode> it = typeNode.down().iterator();
        while (it.hasNext()) {
            JavacNode child = it.next();
            if (child.getKind() == AST.Kind.FIELD) {
                JCTree.JCVariableDecl fieldDecl = child.get();
                if (!fieldDecl.name.toString().startsWith(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX)) {
                    long fieldFlags = fieldDecl.mods.flags;
                    if ((fieldFlags & 8) == 0) {
                        boolean isFinal = (fieldFlags & 16) != 0;
                        boolean isNonNull = nullMarked && !JavacHandlerUtil.findAnnotations(child, HandlerUtil.NON_NULL_PATTERN).isEmpty();
                        if (isFinal || isNonNull) {
                            if (fieldDecl.init == null) {
                                fields.append(child);
                            }
                        }
                    }
                }
            }
        }
        return fields.toList();
    }

    /* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleConstructor$HandleAllArgsConstructor.SCL.lombok */
    public static class HandleAllArgsConstructor extends JavacAnnotationHandler<AllArgsConstructor> {
        private HandleConstructor handleConstructor = new HandleConstructor();

        @Override // lombok.javac.JavacAnnotationHandler
        public void handle(AnnotationValues<AllArgsConstructor> annotation, JCTree.JCAnnotation ast, JavacNode annotationNode) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.ALL_ARGS_CONSTRUCTOR_FLAG_USAGE, "@AllArgsConstructor", ConfigurationKeys.ANY_CONSTRUCTOR_FLAG_USAGE, "any @xArgsConstructor");
            JavacHandlerUtil.deleteAnnotationIfNeccessary(annotationNode, (Class<? extends Annotation>) AllArgsConstructor.class);
            JavacHandlerUtil.deleteImportFromCompilationUnit(annotationNode, "lombok.AccessLevel");
            JavacNode typeNode = annotationNode.up();
            if (HandleConstructor.checkLegality(typeNode, annotationNode, AllArgsConstructor.class.getSimpleName())) {
                List<JCTree.JCAnnotation> onConstructor = JavacHandlerUtil.unboxAndRemoveAnnotationParameter(ast, "onConstructor", "@AllArgsConstructor(onConstructor", annotationNode);
                AllArgsConstructor ann = (AllArgsConstructor) annotation.getInstance();
                AccessLevel level = ann.access();
                if (level == AccessLevel.NONE) {
                    return;
                }
                String staticName = ann.staticName();
                if (annotation.isExplicit("suppressConstructorProperties")) {
                    annotationNode.addError("This deprecated feature is no longer supported. Remove it; you can create a lombok.config file with 'lombok.anyConstructor.suppressConstructorProperties = true'.");
                }
                this.handleConstructor.generateConstructor(typeNode, level, onConstructor, HandleConstructor.findAllFields(typeNode), false, staticName, SkipIfConstructorExists.NO, annotationNode);
            }
        }
    }

    public static List<JavacNode> findAllFields(JavacNode typeNode) {
        return findAllFields(typeNode, false);
    }

    public static List<JavacNode> findAllFields(JavacNode typeNode, boolean evenFinalInitialized) {
        ListBuffer<JavacNode> fields = new ListBuffer<>();
        Iterator<JavacNode> it = typeNode.down().iterator();
        while (it.hasNext()) {
            JavacNode child = it.next();
            if (child.getKind() == AST.Kind.FIELD) {
                JCTree.JCVariableDecl fieldDecl = child.get();
                if (!fieldDecl.name.toString().startsWith(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX)) {
                    long fieldFlags = fieldDecl.mods.flags;
                    if ((fieldFlags & 8) == 0) {
                        boolean isFinal = (fieldFlags & 16) != 0;
                        if (evenFinalInitialized || !isFinal || fieldDecl.init == null) {
                            fields.append(child);
                        }
                    }
                }
            }
        }
        return fields.toList();
    }

    public static boolean checkLegality(JavacNode typeNode, JavacNode errorNode, String name) {
        JCTree.JCClassDecl typeDecl = null;
        if (typeNode.get() instanceof JCTree.JCClassDecl) {
            typeDecl = (JCTree.JCClassDecl) typeNode.get();
        }
        long modifiers = typeDecl == null ? 0L : typeDecl.mods.flags;
        boolean notAClass = (modifiers & 8704) != 0;
        if (typeDecl == null || notAClass) {
            errorNode.addError(name + " is only supported on a class or an enum.");
            return false;
        }
        return true;
    }

    public void generateExtraNoArgsConstructor(JavacNode typeNode, JavacNode source) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Boolean v = (Boolean) typeNode.getAst().readConfiguration(ConfigurationKeys.NO_ARGS_CONSTRUCTOR_EXTRA_PRIVATE);
        if (v == null || v.booleanValue()) {
            List<JavacNode> fields = findFinalFields(typeNode);
            generate(typeNode, AccessLevel.PRIVATE, List.nil(), fields, true, null, SkipIfConstructorExists.NO, source, true);
        }
    }

    public void generateRequiredArgsConstructor(JavacNode typeNode, AccessLevel level, String staticName, SkipIfConstructorExists skipIfConstructorExists, JavacNode source) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        generateConstructor(typeNode, level, List.nil(), findRequiredFields(typeNode), false, staticName, skipIfConstructorExists, source);
    }

    public void generateAllArgsConstructor(JavacNode typeNode, AccessLevel level, String staticName, SkipIfConstructorExists skipIfConstructorExists, JavacNode source) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        generateConstructor(typeNode, level, List.nil(), findAllFields(typeNode), false, staticName, skipIfConstructorExists, source);
    }

    public void generateConstructor(JavacNode typeNode, AccessLevel level, List<JCTree.JCAnnotation> onConstructor, List<JavacNode> fields, boolean allToDefault, String staticName, SkipIfConstructorExists skipIfConstructorExists, JavacNode source) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        generate(typeNode, level, onConstructor, fields, allToDefault, staticName, skipIfConstructorExists, source, false);
    }

    private void generate(JavacNode typeNode, AccessLevel level, List<JCTree.JCAnnotation> onConstructor, List<JavacNode> fields, boolean allToDefault, String staticName, SkipIfConstructorExists skipIfConstructorExists, JavacNode source, boolean noArgs) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        boolean staticConstrRequired = (staticName == null || staticName.equals("")) ? false : true;
        if (skipIfConstructorExists == SkipIfConstructorExists.NO || JavacHandlerUtil.constructorExists(typeNode) == JavacHandlerUtil.MemberExistsResult.NOT_EXISTS) {
            if (skipIfConstructorExists != SkipIfConstructorExists.NO) {
                Iterator<JavacNode> it = typeNode.down().iterator();
                while (it.hasNext()) {
                    JavacNode child = it.next();
                    if (child.getKind() == AST.Kind.ANNOTATION) {
                        boolean skipGeneration = JavacHandlerUtil.annotationTypeMatches((Class<? extends Annotation>) NoArgsConstructor.class, child) || JavacHandlerUtil.annotationTypeMatches((Class<? extends Annotation>) AllArgsConstructor.class, child) || JavacHandlerUtil.annotationTypeMatches((Class<? extends Annotation>) RequiredArgsConstructor.class, child);
                        if (!skipGeneration && skipIfConstructorExists == SkipIfConstructorExists.YES) {
                            skipGeneration = JavacHandlerUtil.annotationTypeMatches((Class<? extends Annotation>) Builder.class, child);
                        }
                        if (skipGeneration) {
                            if (staticConstrRequired) {
                                source.addWarning("Ignoring static constructor name: explicit @XxxArgsConstructor annotation present; its `staticName` parameter will be used.");
                                return;
                            }
                            return;
                        }
                    }
                }
            }
            if (noArgs && noArgsConstructorExists(typeNode)) {
                return;
            }
            JCTree.JCMethodDecl constr = createConstructor(staticConstrRequired ? AccessLevel.PRIVATE : level, onConstructor, typeNode, fields, allToDefault, source);
            ListBuffer<Type> argTypes = new ListBuffer<>();
            Iterator it2 = fields.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                JavacNode fieldNode = (JavacNode) it2.next();
                Type mirror = JavacHandlerUtil.getMirrorForFieldType(fieldNode);
                if (mirror == null) {
                    argTypes = null;
                    break;
                }
                argTypes.append(mirror);
            }
            List<Type> argTypes_ = argTypes == null ? null : argTypes.toList();
            JavacHandlerUtil.injectMethod(typeNode, constr, argTypes_, Javac.createVoidType(typeNode.getSymbolTable(), Javac.CTC_VOID));
            if (staticConstrRequired) {
                Symbol.ClassSymbol sym = typeNode.get().sym;
                Type returnType = sym == null ? null : sym.type;
                JCTree.JCMethodDecl staticConstr = createStaticConstructor(staticName, level, typeNode, allToDefault ? List.nil() : fields, source.get());
                JavacHandlerUtil.injectMethod(typeNode, staticConstr, argTypes_, returnType);
            }
        }
    }

    private static boolean noArgsConstructorExists(JavacNode node) {
        JavacNode node2 = JavacHandlerUtil.upToTypeNode(node);
        if (node2 != null && (node2.get() instanceof JCTree.JCClassDecl)) {
            Iterator it = node2.get().defs.iterator();
            while (it.hasNext()) {
                JCTree.JCMethodDecl jCMethodDecl = (JCTree) it.next();
                if (jCMethodDecl instanceof JCTree.JCMethodDecl) {
                    JCTree.JCMethodDecl md = jCMethodDecl;
                    if (md.name.contentEquals("<init>") && md.params.size() == 0) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    public static void addConstructorProperties(JCTree.JCModifiers mods, JavacNode node, List<JavacNode> fields) {
        if (fields.isEmpty()) {
            return;
        }
        JavacTreeMaker maker = node.getTreeMaker();
        JCTree.JCExpression constructorPropertiesType = JavacHandlerUtil.chainDots(node, "java", DefaultBeanDefinitionDocumentReader.NESTED_BEANS_ELEMENT, "ConstructorProperties");
        ListBuffer<JCTree.JCExpression> fieldNames = new ListBuffer<>();
        Iterator it = fields.iterator();
        while (it.hasNext()) {
            JavacNode field = (JavacNode) it.next();
            Name fieldName = JavacHandlerUtil.removePrefixFromField(field);
            fieldNames.append(maker.Literal(fieldName.toString()));
        }
        JCTree.JCAnnotation annotation = maker.Annotation(constructorPropertiesType, List.of(maker.NewArray(null, List.nil(), fieldNames.toList())));
        mods.annotations = mods.annotations.append(annotation);
    }

    public static JCTree.JCMethodDecl createConstructor(AccessLevel level, List<JCTree.JCAnnotation> onConstructor, JavacNode typeNode, List<JavacNode> fields, boolean allToDefault, JavacNode source) {
        boolean addConstructorProperties;
        JCTree.JCStatement nullCheck;
        JavacTreeMaker maker = typeNode.getTreeMaker();
        boolean isEnum = (typeNode.get().mods.flags & 16384) != 0;
        if (isEnum) {
            level = AccessLevel.PRIVATE;
        }
        if (fields.isEmpty()) {
            addConstructorProperties = false;
        } else {
            Boolean v = (Boolean) typeNode.getAst().readConfiguration(ConfigurationKeys.ANY_CONSTRUCTOR_ADD_CONSTRUCTOR_PROPERTIES);
            addConstructorProperties = v != null ? v.booleanValue() : Boolean.FALSE.equals(typeNode.getAst().readConfiguration(ConfigurationKeys.ANY_CONSTRUCTOR_SUPPRESS_CONSTRUCTOR_PROPERTIES));
        }
        ListBuffer<JCTree.JCStatement> nullChecks = new ListBuffer<>();
        ListBuffer<JCTree.JCStatement> assigns = new ListBuffer<>();
        ListBuffer<JCTree.JCVariableDecl> params = new ListBuffer<>();
        Iterator it = fields.iterator();
        while (it.hasNext()) {
            JavacNode fieldNode = (JavacNode) it.next();
            JCTree.JCVariableDecl field = fieldNode.get();
            Name fieldName = JavacHandlerUtil.removePrefixFromField(fieldNode);
            Name rawName = field.name;
            List<JCTree.JCAnnotation> nonNulls = JavacHandlerUtil.findAnnotations(fieldNode, HandlerUtil.NON_NULL_PATTERN);
            if (!allToDefault) {
                List<JCTree.JCAnnotation> nullables = JavacHandlerUtil.findAnnotations(fieldNode, HandlerUtil.NULLABLE_PATTERN);
                long flags = JavacHandlerUtil.addFinalIfNeeded(8589934592L, typeNode.getContext());
                JCTree.JCVariableDecl param = maker.VarDef(maker.Modifiers(flags, nonNulls.appendList(nullables)), fieldName, field.vartype, null);
                params.append(param);
                if (!nonNulls.isEmpty() && (nullCheck = JavacHandlerUtil.generateNullCheck(maker, fieldNode, param, source)) != null) {
                    nullChecks.append(nullCheck);
                }
            }
            JCTree.JCFieldAccess thisX = maker.Select(maker.Ident(fieldNode.toName(OgnlContext.THIS_CONTEXT_KEY)), rawName);
            assigns.append(maker.Exec(maker.Assign(thisX, allToDefault ? getDefaultExpr(maker, field.vartype) : maker.Ident(fieldName))));
        }
        JCTree.JCModifiers mods = maker.Modifiers(JavacHandlerUtil.toJavacModifier(level), List.nil());
        if (!allToDefault && addConstructorProperties && !isLocalType(typeNode) && LombokOptionsFactory.getDelombokOptions(typeNode.getContext()).getFormatPreferences().generateConstructorProperties()) {
            addConstructorProperties(mods, typeNode, fields);
        }
        if (onConstructor != null) {
            mods.annotations = mods.annotations.appendList(JavacHandlerUtil.copyAnnotations(onConstructor));
        }
        return JavacHandlerUtil.recursiveSetGeneratedBy(maker.MethodDef(mods, typeNode.toName("<init>"), null, List.nil(), params.toList(), List.nil(), maker.Block(0L, nullChecks.appendList(assigns).toList()), null), source.get(), typeNode.getContext());
    }

    /* renamed from: lombok.javac.handlers.HandleConstructor$1, reason: invalid class name */
    /* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleConstructor$1.SCL.lombok */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$javax$lang$model$type$TypeKind = new int[TypeKind.values().length];

        static {
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.BOOLEAN.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.CHAR.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.BYTE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.SHORT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.INT.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.LONG.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.FLOAT.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.DOUBLE.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
        }
    }

    private static JCTree.JCExpression getDefaultExpr(JavacTreeMaker maker, JCTree.JCExpression type) {
        if (type instanceof JCTree.JCPrimitiveTypeTree) {
            switch (AnonymousClass1.$SwitchMap$javax$lang$model$type$TypeKind[((JCTree.JCPrimitiveTypeTree) type).getPrimitiveTypeKind().ordinal()]) {
                case 1:
                    return maker.Literal(Javac.CTC_BOOLEAN, 0);
                case 2:
                    return maker.Literal(Javac.CTC_CHAR, 0);
                case 3:
                case 4:
                case 5:
                default:
                    return maker.Literal(Javac.CTC_INT, 0);
                case 6:
                    return maker.Literal(Javac.CTC_LONG, 0L);
                case 7:
                    return maker.Literal(Javac.CTC_FLOAT, Float.valueOf(0.0f));
                case 8:
                    return maker.Literal(Javac.CTC_DOUBLE, Double.valueOf(0.0d));
            }
        }
        return maker.Literal(Javac.CTC_BOT, null);
    }

    public static boolean isLocalType(JavacNode type) {
        AST.Kind kind = type.up().getKind();
        if (kind == AST.Kind.COMPILATION_UNIT) {
            return false;
        }
        if (kind == AST.Kind.TYPE) {
            return isLocalType(type.up());
        }
        return true;
    }

    public JCTree.JCMethodDecl createStaticConstructor(String name, AccessLevel level, JavacNode typeNode, List<JavacNode> fields, JCTree source) {
        JCTree.JCTypeApply jCTypeApplyIdent;
        JCTree.JCTypeApply jCTypeApplyIdent2;
        JavacTreeMaker maker = typeNode.getTreeMaker();
        JCTree.JCClassDecl type = typeNode.get();
        JCTree.JCModifiers mods = maker.Modifiers(8 | JavacHandlerUtil.toJavacModifier(level));
        ListBuffer<JCTree.JCTypeParameter> typeParams = new ListBuffer<>();
        ListBuffer<JCTree.JCVariableDecl> params = new ListBuffer<>();
        ListBuffer<JCTree.JCExpression> typeArgs1 = new ListBuffer<>();
        ListBuffer<JCTree.JCExpression> typeArgs2 = new ListBuffer<>();
        ListBuffer<JCTree.JCExpression> args = new ListBuffer<>();
        if (!type.typarams.isEmpty()) {
            Iterator it = type.typarams.iterator();
            while (it.hasNext()) {
                JCTree.JCTypeParameter param = (JCTree.JCTypeParameter) it.next();
                typeArgs1.append(maker.Ident(param.name));
                typeArgs2.append(maker.Ident(param.name));
                typeParams.append(maker.TypeParameter(param.name, param.bounds));
            }
            jCTypeApplyIdent = maker.TypeApply(maker.Ident(type.name), typeArgs1.toList());
            jCTypeApplyIdent2 = maker.TypeApply(maker.Ident(type.name), typeArgs2.toList());
        } else {
            jCTypeApplyIdent = maker.Ident(type.name);
            jCTypeApplyIdent2 = maker.Ident(type.name);
        }
        Iterator it2 = fields.iterator();
        while (it2.hasNext()) {
            JavacNode fieldNode = (JavacNode) it2.next();
            JCTree.JCVariableDecl field = fieldNode.get();
            Name fieldName = JavacHandlerUtil.removePrefixFromField(fieldNode);
            JCTree.JCExpression pType = JavacHandlerUtil.cloneType(maker, field.vartype, source, typeNode.getContext());
            List<JCTree.JCAnnotation> nonNulls = JavacHandlerUtil.findAnnotations(fieldNode, HandlerUtil.NON_NULL_PATTERN);
            List<JCTree.JCAnnotation> nullables = JavacHandlerUtil.findAnnotations(fieldNode, HandlerUtil.NULLABLE_PATTERN);
            long flags = JavacHandlerUtil.addFinalIfNeeded(8589934592L, typeNode.getContext());
            params.append(maker.VarDef(maker.Modifiers(flags, nonNulls.appendList(nullables)), fieldName, pType, null));
            args.append(maker.Ident(fieldName));
        }
        JCTree.JCReturn returnStatement = maker.Return(maker.NewClass(null, List.nil(), jCTypeApplyIdent2, args.toList(), null));
        JCTree.JCBlock body = maker.Block(0L, List.of(returnStatement));
        return JavacHandlerUtil.recursiveSetGeneratedBy(maker.MethodDef(mods, typeNode.toName(name), jCTypeApplyIdent, typeParams.toList(), params.toList(), List.nil(), body, null), source, typeNode.getContext());
    }
}
