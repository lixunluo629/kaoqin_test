package lombok.eclipse.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ConfigurationKeys;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.core.handlers.HandlerUtil;
import lombok.eclipse.Eclipse;
import lombok.eclipse.EclipseAnnotationHandler;
import lombok.eclipse.EclipseNode;
import lombok.eclipse.handlers.EclipseHandlerUtil;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.AllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.ArrayInitializer;
import org.eclipse.jdt.internal.compiler.ast.ArrayTypeReference;
import org.eclipse.jdt.internal.compiler.ast.Assignment;
import org.eclipse.jdt.internal.compiler.ast.CharLiteral;
import org.eclipse.jdt.internal.compiler.ast.ConstructorDeclaration;
import org.eclipse.jdt.internal.compiler.ast.DoubleLiteral;
import org.eclipse.jdt.internal.compiler.ast.ExplicitConstructorCall;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FalseLiteral;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.FloatLiteral;
import org.eclipse.jdt.internal.compiler.ast.IntLiteral;
import org.eclipse.jdt.internal.compiler.ast.LongLiteral;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.NullLiteral;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.SingleMemberAnnotation;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.StringLiteral;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.lookup.TypeConstants;
import org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader;

/* loaded from: lombok-1.16.22.jar:lombok/eclipse/handlers/HandleConstructor.SCL.lombok */
public class HandleConstructor {
    private static final char[][] JAVA_BEANS_CONSTRUCTORPROPERTIES = {"java".toCharArray(), DefaultBeanDefinitionDocumentReader.NESTED_BEANS_ELEMENT.toCharArray(), "ConstructorProperties".toCharArray()};

    /* loaded from: lombok-1.16.22.jar:lombok/eclipse/handlers/HandleConstructor$SkipIfConstructorExists.SCL.lombok */
    public enum SkipIfConstructorExists {
        YES,
        NO,
        I_AM_BUILDER;

        /* renamed from: values, reason: to resolve conflict with enum method */
        public static SkipIfConstructorExists[] valuesCustom() {
            SkipIfConstructorExists[] skipIfConstructorExistsArrValuesCustom = values();
            int length = skipIfConstructorExistsArrValuesCustom.length;
            SkipIfConstructorExists[] skipIfConstructorExistsArr = new SkipIfConstructorExists[length];
            System.arraycopy(skipIfConstructorExistsArrValuesCustom, 0, skipIfConstructorExistsArr, 0, length);
            return skipIfConstructorExistsArr;
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/eclipse/handlers/HandleConstructor$HandleNoArgsConstructor.SCL.lombok */
    public static class HandleNoArgsConstructor extends EclipseAnnotationHandler<NoArgsConstructor> {
        private HandleConstructor handleConstructor = new HandleConstructor();

        @Override // lombok.eclipse.EclipseAnnotationHandler
        public void handle(AnnotationValues<NoArgsConstructor> annotation, Annotation ast, EclipseNode annotationNode) throws SecurityException {
            HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.NO_ARGS_CONSTRUCTOR_FLAG_USAGE, "@NoArgsConstructor", ConfigurationKeys.ANY_CONSTRUCTOR_FLAG_USAGE, "any @xArgsConstructor");
            EclipseNode typeNode = annotationNode.up();
            if (HandleConstructor.checkLegality(typeNode, annotationNode, NoArgsConstructor.class.getSimpleName())) {
                NoArgsConstructor ann = (NoArgsConstructor) annotation.getInstance();
                AccessLevel level = ann.access();
                String staticName = ann.staticName();
                if (level == AccessLevel.NONE) {
                    return;
                }
                boolean force = ann.force();
                List<EclipseNode> fields = force ? HandleConstructor.findFinalFields(typeNode) : Collections.emptyList();
                List<Annotation> onConstructor = EclipseHandlerUtil.unboxAndRemoveAnnotationParameter(ast, "onConstructor", "@NoArgsConstructor(onConstructor", annotationNode);
                this.handleConstructor.generateConstructor(typeNode, level, fields, force, staticName, SkipIfConstructorExists.NO, onConstructor, annotationNode);
            }
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/eclipse/handlers/HandleConstructor$HandleRequiredArgsConstructor.SCL.lombok */
    public static class HandleRequiredArgsConstructor extends EclipseAnnotationHandler<RequiredArgsConstructor> {
        private HandleConstructor handleConstructor = new HandleConstructor();

        @Override // lombok.eclipse.EclipseAnnotationHandler
        public void handle(AnnotationValues<RequiredArgsConstructor> annotation, Annotation ast, EclipseNode annotationNode) throws SecurityException {
            RequiredArgsConstructor ann;
            AccessLevel level;
            HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.REQUIRED_ARGS_CONSTRUCTOR_FLAG_USAGE, "@RequiredArgsConstructor", ConfigurationKeys.ANY_CONSTRUCTOR_FLAG_USAGE, "any @xArgsConstructor");
            EclipseNode typeNode = annotationNode.up();
            if (HandleConstructor.checkLegality(typeNode, annotationNode, RequiredArgsConstructor.class.getSimpleName()) && (level = (ann = (RequiredArgsConstructor) annotation.getInstance()).access()) != AccessLevel.NONE) {
                String staticName = ann.staticName();
                if (annotation.isExplicit("suppressConstructorProperties")) {
                    annotationNode.addError("This deprecated feature is no longer supported. Remove it; you can create a lombok.config file with 'lombok.anyConstructor.suppressConstructorProperties = true'.");
                }
                List<Annotation> onConstructor = EclipseHandlerUtil.unboxAndRemoveAnnotationParameter(ast, "onConstructor", "@RequiredArgsConstructor(onConstructor", annotationNode);
                this.handleConstructor.generateConstructor(typeNode, level, HandleConstructor.findRequiredFields(typeNode), false, staticName, SkipIfConstructorExists.NO, onConstructor, annotationNode);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static List<EclipseNode> findRequiredFields(EclipseNode typeNode) {
        return findFields(typeNode, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static List<EclipseNode> findFinalFields(EclipseNode typeNode) {
        return findFields(typeNode, false);
    }

    private static List<EclipseNode> findFields(EclipseNode typeNode, boolean nullMarked) {
        List<EclipseNode> fields = new ArrayList<>();
        Iterator<EclipseNode> it = typeNode.down().iterator();
        while (it.hasNext()) {
            EclipseNode child = it.next();
            if (child.getKind() == AST.Kind.FIELD) {
                FieldDeclaration fieldDecl = child.get();
                if (EclipseHandlerUtil.filterField(fieldDecl)) {
                    boolean isFinal = (fieldDecl.modifiers & 16) != 0;
                    boolean isNonNull = nullMarked && Eclipse.findAnnotations(fieldDecl, HandlerUtil.NON_NULL_PATTERN).length != 0;
                    if (isFinal || isNonNull) {
                        if (fieldDecl.initialization == null) {
                            fields.add(child);
                        }
                    }
                }
            }
        }
        return fields;
    }

    static List<EclipseNode> findAllFields(EclipseNode typeNode) {
        return findAllFields(typeNode, false);
    }

    static List<EclipseNode> findAllFields(EclipseNode typeNode, boolean evenFinalInitialized) {
        List<EclipseNode> fields = new ArrayList<>();
        Iterator<EclipseNode> it = typeNode.down().iterator();
        while (it.hasNext()) {
            EclipseNode child = it.next();
            if (child.getKind() == AST.Kind.FIELD) {
                FieldDeclaration fieldDecl = child.get();
                if (EclipseHandlerUtil.filterField(fieldDecl) && (evenFinalInitialized || (fieldDecl.modifiers & 16) == 0 || fieldDecl.initialization == null)) {
                    fields.add(child);
                }
            }
        }
        return fields;
    }

    /* loaded from: lombok-1.16.22.jar:lombok/eclipse/handlers/HandleConstructor$HandleAllArgsConstructor.SCL.lombok */
    public static class HandleAllArgsConstructor extends EclipseAnnotationHandler<AllArgsConstructor> {
        private HandleConstructor handleConstructor = new HandleConstructor();

        @Override // lombok.eclipse.EclipseAnnotationHandler
        public void handle(AnnotationValues<AllArgsConstructor> annotation, Annotation ast, EclipseNode annotationNode) throws SecurityException {
            AllArgsConstructor ann;
            AccessLevel level;
            HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.ALL_ARGS_CONSTRUCTOR_FLAG_USAGE, "@AllArgsConstructor", ConfigurationKeys.ANY_CONSTRUCTOR_FLAG_USAGE, "any @xArgsConstructor");
            EclipseNode typeNode = annotationNode.up();
            if (HandleConstructor.checkLegality(typeNode, annotationNode, AllArgsConstructor.class.getSimpleName()) && (level = (ann = (AllArgsConstructor) annotation.getInstance()).access()) != AccessLevel.NONE) {
                String staticName = ann.staticName();
                if (annotation.isExplicit("suppressConstructorProperties")) {
                    annotationNode.addError("This deprecated feature is no longer supported. Remove it; you can create a lombok.config file with 'lombok.anyConstructor.suppressConstructorProperties = true'.");
                }
                List<Annotation> onConstructor = EclipseHandlerUtil.unboxAndRemoveAnnotationParameter(ast, "onConstructor", "@AllArgsConstructor(onConstructor", annotationNode);
                this.handleConstructor.generateConstructor(typeNode, level, HandleConstructor.findAllFields(typeNode), false, staticName, SkipIfConstructorExists.NO, onConstructor, annotationNode);
            }
        }
    }

    static boolean checkLegality(EclipseNode typeNode, EclipseNode errorNode, String name) {
        TypeDeclaration typeDecl = null;
        if (typeNode.get() instanceof TypeDeclaration) {
            typeDecl = (TypeDeclaration) typeNode.get();
        }
        int modifiers = typeDecl == null ? 0 : typeDecl.modifiers;
        boolean notAClass = (modifiers & 8704) != 0;
        if (typeDecl == null || notAClass) {
            errorNode.addError(String.valueOf(name) + " is only supported on a class or an enum.");
            return false;
        }
        return true;
    }

    public void generateExtraNoArgsConstructor(EclipseNode typeNode, EclipseNode sourceNode) throws SecurityException {
        Boolean v = (Boolean) typeNode.getAst().readConfiguration(ConfigurationKeys.NO_ARGS_CONSTRUCTOR_EXTRA_PRIVATE);
        if (v == null || v.booleanValue()) {
            List<EclipseNode> fields = findFinalFields(typeNode);
            generate(typeNode, AccessLevel.PRIVATE, fields, true, null, SkipIfConstructorExists.NO, Collections.emptyList(), sourceNode, true);
        }
    }

    public void generateRequiredArgsConstructor(EclipseNode typeNode, AccessLevel level, String staticName, SkipIfConstructorExists skipIfConstructorExists, List<Annotation> onConstructor, EclipseNode sourceNode) throws SecurityException {
        generateConstructor(typeNode, level, findRequiredFields(typeNode), false, staticName, skipIfConstructorExists, onConstructor, sourceNode);
    }

    public void generateAllArgsConstructor(EclipseNode typeNode, AccessLevel level, String staticName, SkipIfConstructorExists skipIfConstructorExists, List<Annotation> onConstructor, EclipseNode sourceNode) throws SecurityException {
        generateConstructor(typeNode, level, findAllFields(typeNode), false, staticName, skipIfConstructorExists, onConstructor, sourceNode);
    }

    public void generateConstructor(EclipseNode typeNode, AccessLevel level, List<EclipseNode> fields, boolean allToDefault, String staticName, SkipIfConstructorExists skipIfConstructorExists, List<Annotation> onConstructor, EclipseNode sourceNode) throws SecurityException {
        generate(typeNode, level, fields, allToDefault, staticName, skipIfConstructorExists, onConstructor, sourceNode, false);
    }

    public void generate(EclipseNode typeNode, AccessLevel level, List<EclipseNode> fields, boolean allToDefault, String staticName, SkipIfConstructorExists skipIfConstructorExists, List<Annotation> onConstructor, EclipseNode sourceNode, boolean noArgs) throws SecurityException {
        ASTNode source = sourceNode.get();
        boolean staticConstrRequired = (staticName == null || staticName.equals("")) ? false : true;
        if (skipIfConstructorExists == SkipIfConstructorExists.NO || EclipseHandlerUtil.constructorExists(typeNode) == EclipseHandlerUtil.MemberExistsResult.NOT_EXISTS) {
            if (skipIfConstructorExists != SkipIfConstructorExists.NO) {
                Iterator<EclipseNode> it = typeNode.down().iterator();
                while (it.hasNext()) {
                    EclipseNode child = it.next();
                    if (child.getKind() == AST.Kind.ANNOTATION) {
                        boolean skipGeneration = EclipseHandlerUtil.annotationTypeMatches((Class<? extends java.lang.annotation.Annotation>) NoArgsConstructor.class, child) || EclipseHandlerUtil.annotationTypeMatches((Class<? extends java.lang.annotation.Annotation>) AllArgsConstructor.class, child) || EclipseHandlerUtil.annotationTypeMatches((Class<? extends java.lang.annotation.Annotation>) RequiredArgsConstructor.class, child);
                        if (!skipGeneration && skipIfConstructorExists == SkipIfConstructorExists.YES) {
                            skipGeneration = EclipseHandlerUtil.annotationTypeMatches((Class<? extends java.lang.annotation.Annotation>) Builder.class, child);
                        }
                        if (skipGeneration) {
                            if (staticConstrRequired) {
                                typeNode.addWarning("Ignoring static constructor name: explicit @XxxArgsConstructor annotation present; its `staticName` parameter will be used.", source.sourceStart, source.sourceEnd);
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
            ConstructorDeclaration constr = createConstructor(staticConstrRequired ? AccessLevel.PRIVATE : level, typeNode, fields, allToDefault, sourceNode, onConstructor);
            EclipseHandlerUtil.injectMethod(typeNode, constr);
            if (staticConstrRequired) {
                MethodDeclaration staticConstr = createStaticConstructor(level, staticName, typeNode, allToDefault ? Collections.emptyList() : fields, source);
                EclipseHandlerUtil.injectMethod(typeNode, staticConstr);
            }
        }
    }

    private static boolean noArgsConstructorExists(EclipseNode node) {
        Argument[] arguments;
        EclipseNode node2 = EclipseHandlerUtil.upToTypeNode(node);
        if (node2 != null && (node2.get() instanceof TypeDeclaration)) {
            TypeDeclaration typeDecl = node2.get();
            if (typeDecl.methods != null) {
                for (ConstructorDeclaration constructorDeclaration : typeDecl.methods) {
                    if ((constructorDeclaration instanceof ConstructorDeclaration) && ((arguments = constructorDeclaration.arguments) == null || arguments.length == 0)) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public static Annotation[] createConstructorProperties(ASTNode source, Collection<EclipseNode> fields) throws SecurityException {
        if (fields.isEmpty()) {
            return null;
        }
        int pS = source.sourceStart;
        int pE = source.sourceEnd;
        long p = (pS << 32) | pE;
        long[] poss = new long[3];
        Arrays.fill(poss, p);
        QualifiedTypeReference constructorPropertiesType = new QualifiedTypeReference(JAVA_BEANS_CONSTRUCTORPROPERTIES, poss);
        EclipseHandlerUtil.setGeneratedBy(constructorPropertiesType, source);
        Annotation singleMemberAnnotation = new SingleMemberAnnotation(constructorPropertiesType, pS);
        ((SingleMemberAnnotation) singleMemberAnnotation).declarationSourceEnd = pE;
        ArrayInitializer fieldNames = new ArrayInitializer();
        fieldNames.sourceStart = pS;
        fieldNames.sourceEnd = pE;
        fieldNames.expressions = new Expression[fields.size()];
        int ctr = 0;
        for (EclipseNode field : fields) {
            char[] fieldName = EclipseHandlerUtil.removePrefixFromField(field);
            fieldNames.expressions[ctr] = new StringLiteral(fieldName, pS, pE, 0);
            EclipseHandlerUtil.setGeneratedBy(fieldNames.expressions[ctr], source);
            ctr++;
        }
        ((SingleMemberAnnotation) singleMemberAnnotation).memberValue = fieldNames;
        EclipseHandlerUtil.setGeneratedBy(singleMemberAnnotation, source);
        EclipseHandlerUtil.setGeneratedBy(((SingleMemberAnnotation) singleMemberAnnotation).memberValue, source);
        return new Annotation[]{singleMemberAnnotation};
    }

    /* JADX WARN: Type inference failed for: r2v22, types: [org.eclipse.jdt.internal.compiler.ast.Annotation[], org.eclipse.jdt.internal.compiler.ast.Annotation[][]] */
    /* JADX WARN: Type inference failed for: r2v37, types: [org.eclipse.jdt.internal.compiler.ast.Annotation[], org.eclipse.jdt.internal.compiler.ast.Annotation[][]] */
    public static ConstructorDeclaration createConstructor(AccessLevel level, EclipseNode type, Collection<EclipseNode> fields, boolean allToDefault, EclipseNode sourceNode, List<Annotation> onConstructor) throws SecurityException {
        boolean addConstructorProperties;
        Statement nullCheck;
        ASTNode source = sourceNode.get();
        TypeDeclaration typeDeclaration = type.get();
        long p = (source.sourceStart << 32) | source.sourceEnd;
        boolean isEnum = (type.get().modifiers & 16384) != 0;
        if (isEnum) {
            level = AccessLevel.PRIVATE;
        }
        if (fields.isEmpty()) {
            addConstructorProperties = false;
        } else {
            Boolean v = (Boolean) type.getAst().readConfiguration(ConfigurationKeys.ANY_CONSTRUCTOR_ADD_CONSTRUCTOR_PROPERTIES);
            addConstructorProperties = v != null ? v.booleanValue() : Boolean.FALSE.equals(type.getAst().readConfiguration(ConfigurationKeys.ANY_CONSTRUCTOR_SUPPRESS_CONSTRUCTOR_PROPERTIES));
        }
        ConstructorDeclaration constructor = new ConstructorDeclaration(type.top().get().compilationResult);
        constructor.modifiers = EclipseHandlerUtil.toEclipseModifier(level);
        constructor.selector = typeDeclaration.name;
        constructor.constructorCall = new ExplicitConstructorCall(1);
        constructor.constructorCall.sourceStart = source.sourceStart;
        constructor.constructorCall.sourceEnd = source.sourceEnd;
        constructor.thrownExceptions = null;
        constructor.typeParameters = null;
        constructor.bits |= 8388608;
        int i = source.sourceStart;
        constructor.sourceStart = i;
        constructor.declarationSourceStart = i;
        constructor.bodyStart = i;
        int i2 = source.sourceEnd;
        constructor.sourceEnd = i2;
        constructor.declarationSourceEnd = i2;
        constructor.bodyEnd = i2;
        constructor.arguments = null;
        List<Argument> params = new ArrayList<>();
        ArrayList arrayList = new ArrayList();
        List<Statement> nullChecks = new ArrayList<>();
        for (EclipseNode fieldNode : fields) {
            FieldDeclaration field = fieldNode.get();
            char[] rawName = field.name;
            char[] fieldName = EclipseHandlerUtil.removePrefixFromField(fieldNode);
            FieldReference thisX = new FieldReference(rawName, p);
            int s = (int) (p >> 32);
            int e = (int) p;
            thisX.receiver = new ThisReference(s, e);
            Expression assignmentExpr = allToDefault ? getDefaultExpr(field.type, s, e) : new SingleNameReference(fieldName, p);
            Assignment assignment = new Assignment(thisX, assignmentExpr, (int) p);
            assignment.sourceStart = (int) (p >> 32);
            int i3 = (int) (p >> 32);
            assignment.statementEnd = i3;
            assignment.sourceEnd = i3;
            arrayList.add(assignment);
            if (!allToDefault) {
                long fieldPos = (field.sourceStart << 32) | field.sourceEnd;
                Argument parameter = new Argument(fieldName, fieldPos, EclipseHandlerUtil.copyType(field.type, source), 16);
                Annotation[] nonNulls = Eclipse.findAnnotations(field, HandlerUtil.NON_NULL_PATTERN);
                Annotation[] nullables = Eclipse.findAnnotations(field, HandlerUtil.NULLABLE_PATTERN);
                if (nonNulls.length != 0 && (nullCheck = EclipseHandlerUtil.generateNullCheck(parameter, sourceNode)) != null) {
                    nullChecks.add(nullCheck);
                }
                parameter.annotations = EclipseHandlerUtil.copyAnnotations(source, new Annotation[]{nonNulls, nullables});
                params.add(parameter);
            }
        }
        nullChecks.addAll(arrayList);
        constructor.statements = nullChecks.isEmpty() ? null : (Statement[]) nullChecks.toArray(new Statement[nullChecks.size()]);
        constructor.arguments = params.isEmpty() ? null : (Argument[]) params.toArray(new Argument[params.size()]);
        Annotation[] constructorProperties = null;
        if (!allToDefault && addConstructorProperties && !isLocalType(type)) {
            constructorProperties = createConstructorProperties(source, fields);
        }
        constructor.annotations = EclipseHandlerUtil.copyAnnotations(source, new Annotation[]{(Annotation[]) onConstructor.toArray(new Annotation[0]), constructorProperties});
        constructor.traverse(new SetGeneratedByVisitor(source), typeDeclaration.scope);
        return constructor;
    }

    private static Expression getDefaultExpr(TypeReference type, int s, int e) {
        boolean array = type instanceof ArrayTypeReference;
        if (array) {
            return new NullLiteral(s, e);
        }
        char[] lastToken = type.getLastToken();
        return Arrays.equals(TypeConstants.BOOLEAN, lastToken) ? new FalseLiteral(s, e) : Arrays.equals(TypeConstants.CHAR, lastToken) ? new CharLiteral(new char[]{'\'', '\\', '0', '\''}, s, e) : (Arrays.equals(TypeConstants.BYTE, lastToken) || Arrays.equals(TypeConstants.SHORT, lastToken) || Arrays.equals(TypeConstants.INT, lastToken)) ? IntLiteral.buildIntLiteral(new char[]{'0'}, s, e) : Arrays.equals(TypeConstants.LONG, lastToken) ? LongLiteral.buildLongLiteral(new char[]{'0', 'L'}, s, e) : Arrays.equals(TypeConstants.FLOAT, lastToken) ? new FloatLiteral(new char[]{'0', 'F'}, s, e) : Arrays.equals(TypeConstants.DOUBLE, lastToken) ? new DoubleLiteral(new char[]{'0', 'D'}, s, e) : new NullLiteral(s, e);
    }

    public static boolean isLocalType(EclipseNode type) {
        AST.Kind kind = type.up().getKind();
        if (kind == AST.Kind.COMPILATION_UNIT) {
            return false;
        }
        if (kind == AST.Kind.TYPE) {
            return isLocalType(type.up());
        }
        return true;
    }

    /* JADX WARN: Type inference failed for: r2v33, types: [org.eclipse.jdt.internal.compiler.ast.Annotation[], org.eclipse.jdt.internal.compiler.ast.Annotation[][]] */
    public MethodDeclaration createStaticConstructor(AccessLevel level, String name, EclipseNode type, Collection<EclipseNode> fields, ASTNode source) {
        int pS = source.sourceStart;
        int pE = source.sourceEnd;
        long p = (pS << 32) | pE;
        MethodDeclaration constructor = new MethodDeclaration(type.top().get().compilationResult);
        constructor.modifiers = EclipseHandlerUtil.toEclipseModifier(level) | 8;
        TypeDeclaration typeDecl = type.get();
        constructor.returnType = EclipseHandlerUtil.namePlusTypeParamsToTypeReference(typeDecl.name, typeDecl.typeParameters, p);
        constructor.annotations = null;
        constructor.selector = name.toCharArray();
        constructor.thrownExceptions = null;
        constructor.typeParameters = EclipseHandlerUtil.copyTypeParams(type.get().typeParameters, source);
        constructor.bits |= 8388608;
        int i = source.sourceStart;
        constructor.sourceStart = i;
        constructor.declarationSourceStart = i;
        constructor.bodyStart = i;
        int i2 = source.sourceEnd;
        constructor.sourceEnd = i2;
        constructor.declarationSourceEnd = i2;
        constructor.bodyEnd = i2;
        List<Argument> params = new ArrayList<>();
        List<Expression> assigns = new ArrayList<>();
        AllocationExpression statement = new AllocationExpression();
        statement.sourceStart = pS;
        statement.sourceEnd = pE;
        statement.type = EclipseHandlerUtil.copyType(constructor.returnType, source);
        for (EclipseNode fieldNode : fields) {
            FieldDeclaration field = fieldNode.get();
            long fieldPos = (field.sourceStart << 32) | field.sourceEnd;
            SingleNameReference nameRef = new SingleNameReference(field.name, fieldPos);
            assigns.add(nameRef);
            Argument parameter = new Argument(field.name, fieldPos, EclipseHandlerUtil.copyType(field.type, source), 16);
            parameter.annotations = EclipseHandlerUtil.copyAnnotations(source, new Annotation[]{Eclipse.findAnnotations(field, HandlerUtil.NON_NULL_PATTERN), Eclipse.findAnnotations(field, HandlerUtil.NULLABLE_PATTERN)});
            params.add(parameter);
        }
        statement.arguments = assigns.isEmpty() ? null : (Expression[]) assigns.toArray(new Expression[assigns.size()]);
        constructor.arguments = params.isEmpty() ? null : (Argument[]) params.toArray(new Argument[params.size()]);
        constructor.statements = new Statement[]{new ReturnStatement(statement, (int) (p >> 32), (int) p)};
        constructor.traverse(new SetGeneratedByVisitor(source), typeDecl.scope);
        return constructor;
    }
}
