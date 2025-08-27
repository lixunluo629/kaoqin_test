package lombok.eclipse.handlers.singulars;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.core.GuavaTypeMap;
import lombok.core.LombokImmutableList;
import lombok.core.handlers.HandlerUtil;
import lombok.eclipse.Eclipse;
import lombok.eclipse.EclipseNode;
import lombok.eclipse.handlers.EclipseHandlerUtil;
import lombok.eclipse.handlers.EclipseSingularsRecipes;
import org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.Assignment;
import org.eclipse.jdt.internal.compiler.ast.ConditionalExpression;
import org.eclipse.jdt.internal.compiler.ast.EqualExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.IfStatement;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.NullLiteral;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;

/* loaded from: lombok-1.16.22.jar:lombok/eclipse/handlers/singulars/EclipseGuavaSingularizer.SCL.lombok */
abstract class EclipseGuavaSingularizer extends EclipseSingularsRecipes.EclipseSingularizer {
    protected abstract LombokImmutableList<String> getArgumentSuffixes();

    protected abstract String getAddMethodName();

    protected abstract String getAddAllTypeName();

    EclipseGuavaSingularizer() {
    }

    protected String getSimpleTargetTypeName(EclipseSingularsRecipes.SingularData data) {
        return GuavaTypeMap.getGuavaTypeName(data.getTargetFqn());
    }

    protected char[] getBuilderMethodName(EclipseSingularsRecipes.SingularData data) {
        String simpleTypeName = getSimpleTargetTypeName(data);
        return ("ImmutableSortedSet".equals(simpleTypeName) || "ImmutableSortedMap".equals(simpleTypeName)) ? "naturalOrder".toCharArray() : "builder".toCharArray();
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [char[], char[][]] */
    protected char[][] makeGuavaTypeName(String simpleName, boolean addBuilder) {
        ?? r0 = new char[addBuilder ? 6 : 5];
        r0[0] = new char[]{'c', 'o', 'm'};
        r0[1] = new char[]{'g', 'o', 'o', 'g', 'l', 'e'};
        r0[2] = new char[]{'c', 'o', 'm', 'm', 'o', 'n'};
        r0[3] = new char[]{'c', 'o', 'l', 'l', 'e', 'c', 't'};
        r0[4] = simpleName.toCharArray();
        if (addBuilder) {
            r0[5] = new char[]{'B', 'u', 'i', 'l', 'd', 'e', 'r'};
        }
        return r0;
    }

    @Override // lombok.eclipse.handlers.EclipseSingularsRecipes.EclipseSingularizer
    public List<EclipseNode> generateFields(EclipseSingularsRecipes.SingularData data, EclipseNode builderType) {
        String simpleTypeName = getSimpleTargetTypeName(data);
        char[][] tokenizedName = makeGuavaTypeName(simpleTypeName, true);
        TypeReference type = addTypeArgs(getTypeArgumentsCount(), false, builderType, new QualifiedTypeReference(tokenizedName, NULL_POSS), data.getTypeArgs());
        FieldDeclaration buildField = new FieldDeclaration(data.getPluralName(), 0, -1);
        buildField.bits |= 8388608;
        buildField.modifiers = 2;
        buildField.declarationSourceEnd = -1;
        buildField.type = type;
        data.setGeneratedByRecursive(buildField);
        return Collections.singletonList(EclipseHandlerUtil.injectFieldAndMarkGenerated(builderType, buildField));
    }

    @Override // lombok.eclipse.handlers.EclipseSingularsRecipes.EclipseSingularizer
    public void generateMethods(EclipseSingularsRecipes.SingularData data, boolean deprecate, EclipseNode builderType, boolean fluent, boolean chain) {
        TypeReference returnType = chain ? EclipseHandlerUtil.cloneSelfType(builderType) : TypeReference.baseTypeReference(6, 0);
        generateSingularMethod(deprecate, returnType, chain ? new ReturnStatement(new ThisReference(0, 0), 0, 0) : null, data, builderType, fluent);
        TypeReference returnType2 = chain ? EclipseHandlerUtil.cloneSelfType(builderType) : TypeReference.baseTypeReference(6, 0);
        generatePluralMethod(deprecate, returnType2, chain ? new ReturnStatement(new ThisReference(0, 0), 0, 0) : null, data, builderType, fluent);
        TypeReference returnType3 = chain ? EclipseHandlerUtil.cloneSelfType(builderType) : TypeReference.baseTypeReference(6, 0);
        generateClearMethod(deprecate, returnType3, chain ? new ReturnStatement(new ThisReference(0, 0), 0, 0) : null, data, builderType);
    }

    void generateClearMethod(boolean deprecate, TypeReference returnType, Statement returnStatement, EclipseSingularsRecipes.SingularData data, EclipseNode builderType) {
        MethodDeclaration md = new MethodDeclaration(builderType.top().get().compilationResult);
        md.bits |= 8388608;
        md.modifiers = 1;
        FieldReference thisDotField = new FieldReference(data.getPluralName(), 0L);
        thisDotField.receiver = new ThisReference(0, 0);
        Statement assignment = new Assignment(thisDotField, new NullLiteral(0, 0), 0);
        md.selector = HandlerUtil.buildAccessorName("clear", new String(data.getPluralName())).toCharArray();
        md.statements = returnStatement != null ? new Statement[]{assignment, returnStatement} : new Statement[]{assignment};
        md.returnType = returnType;
        md.annotations = deprecate ? new Annotation[]{EclipseHandlerUtil.generateDeprecatedAnnotation(data.getSource())} : null;
        EclipseHandlerUtil.injectMethod(builderType, md);
    }

    /* JADX WARN: Multi-variable type inference failed */
    void generateSingularMethod(boolean deprecate, TypeReference returnType, Statement returnStatement, EclipseSingularsRecipes.SingularData data, EclipseNode builderType, boolean fluent) {
        LombokImmutableList<String> suffixes = getArgumentSuffixes();
        char[] cArr = new char[suffixes.size()];
        for (int i = 0; i < suffixes.size(); i++) {
            String s = suffixes.get(i);
            char[] n = data.getSingularName();
            cArr[i] = s.isEmpty() ? n : s.toCharArray();
        }
        MethodDeclaration md = new MethodDeclaration(builderType.top().get().compilationResult);
        md.bits |= 8388608;
        md.modifiers = 1;
        List<Statement> statements = new ArrayList<>();
        statements.add(createConstructBuilderVarIfNeeded(data, builderType));
        FieldReference thisDotField = new FieldReference(data.getPluralName(), 0L);
        thisDotField.receiver = new ThisReference(0, 0);
        MessageSend thisDotFieldDotAdd = new MessageSend();
        thisDotFieldDotAdd.arguments = new Expression[suffixes.size()];
        for (int i2 = 0; i2 < suffixes.size(); i2++) {
            thisDotFieldDotAdd.arguments[i2] = new SingleNameReference(cArr[i2], 0L);
        }
        thisDotFieldDotAdd.receiver = thisDotField;
        thisDotFieldDotAdd.selector = getAddMethodName().toCharArray();
        statements.add(thisDotFieldDotAdd);
        if (returnStatement != null) {
            statements.add(returnStatement);
        }
        md.statements = (Statement[]) statements.toArray(new Statement[statements.size()]);
        md.arguments = new Argument[suffixes.size()];
        for (int i3 = 0; i3 < suffixes.size(); i3++) {
            TypeReference tr = cloneParamType(i3, data.getTypeArgs(), builderType);
            md.arguments[i3] = new Argument(cArr[i3], 0L, tr, 0);
        }
        md.returnType = returnType;
        md.selector = fluent ? data.getSingularName() : HandlerUtil.buildAccessorName(getAddMethodName(), new String(data.getSingularName())).toCharArray();
        md.annotations = deprecate ? new Annotation[]{EclipseHandlerUtil.generateDeprecatedAnnotation(data.getSource())} : null;
        data.setGeneratedByRecursive(md);
        EclipseHandlerUtil.injectMethod(builderType, md);
    }

    void generatePluralMethod(boolean deprecate, TypeReference returnType, Statement returnStatement, EclipseSingularsRecipes.SingularData data, EclipseNode builderType, boolean fluent) {
        MethodDeclaration md = new MethodDeclaration(builderType.top().get().compilationResult);
        md.bits |= 8388608;
        md.modifiers = 1;
        List<Statement> statements = new ArrayList<>();
        statements.add(createConstructBuilderVarIfNeeded(data, builderType));
        FieldReference thisDotField = new FieldReference(data.getPluralName(), 0L);
        thisDotField.receiver = new ThisReference(0, 0);
        MessageSend thisDotFieldDotAddAll = new MessageSend();
        thisDotFieldDotAddAll.arguments = new Expression[]{new SingleNameReference(data.getPluralName(), 0L)};
        thisDotFieldDotAddAll.receiver = thisDotField;
        thisDotFieldDotAddAll.selector = (String.valueOf(getAddMethodName()) + "All").toCharArray();
        statements.add(thisDotFieldDotAddAll);
        if (returnStatement != null) {
            statements.add(returnStatement);
        }
        md.statements = (Statement[]) statements.toArray(new Statement[statements.size()]);
        TypeReference paramType = addTypeArgs(getTypeArgumentsCount(), true, builderType, new QualifiedTypeReference(Eclipse.fromQualifiedName(getAddAllTypeName()), NULL_POSS), data.getTypeArgs());
        Argument param = new Argument(data.getPluralName(), 0L, paramType, 0);
        md.arguments = new Argument[]{param};
        md.returnType = returnType;
        md.selector = fluent ? data.getPluralName() : HandlerUtil.buildAccessorName(String.valueOf(getAddMethodName()) + "All", new String(data.getPluralName())).toCharArray();
        md.annotations = deprecate ? new Annotation[]{EclipseHandlerUtil.generateDeprecatedAnnotation(data.getSource())} : null;
        data.setGeneratedByRecursive(md);
        EclipseHandlerUtil.injectMethod(builderType, md);
    }

    @Override // lombok.eclipse.handlers.EclipseSingularsRecipes.EclipseSingularizer
    public void appendBuildCode(EclipseSingularsRecipes.SingularData data, EclipseNode builderType, List<Statement> statements, char[] targetVariableName) {
        QualifiedTypeReference qualifiedTypeReference = new QualifiedTypeReference(Eclipse.fromQualifiedName(data.getTargetFqn()), NULL_POSS);
        String simpleTypeName = getSimpleTargetTypeName(data);
        int agrumentsCount = getTypeArgumentsCount();
        TypeReference varType = addTypeArgs(agrumentsCount, false, builderType, qualifiedTypeReference, data.getTypeArgs());
        MessageSend emptyInvoke = new MessageSend();
        emptyInvoke.selector = new char[]{'o', 'f'};
        emptyInvoke.receiver = new QualifiedNameReference(makeGuavaTypeName(simpleTypeName, false), NULL_POSS, 0, 0);
        emptyInvoke.typeArguments = createTypeArgs(agrumentsCount, false, builderType, data.getTypeArgs());
        MessageSend invokeBuild = new MessageSend();
        invokeBuild.selector = new char[]{'b', 'u', 'i', 'l', 'd'};
        FieldReference thisDotField = new FieldReference(data.getPluralName(), 0L);
        thisDotField.receiver = new ThisReference(0, 0);
        invokeBuild.receiver = thisDotField;
        FieldReference thisDotField2 = new FieldReference(data.getPluralName(), 0L);
        thisDotField2.receiver = new ThisReference(0, 0);
        ConditionalExpression conditionalExpression = new ConditionalExpression(new EqualExpression(thisDotField2, new NullLiteral(0, 0), 18), emptyInvoke, invokeBuild);
        LocalDeclaration varDefStat = new LocalDeclaration(data.getPluralName(), 0, 0);
        varDefStat.type = varType;
        varDefStat.initialization = conditionalExpression;
        statements.add(varDefStat);
    }

    protected Statement createConstructBuilderVarIfNeeded(EclipseSingularsRecipes.SingularData data, EclipseNode builderType) {
        FieldReference thisDotField = new FieldReference(data.getPluralName(), 0L);
        thisDotField.receiver = new ThisReference(0, 0);
        FieldReference thisDotField2 = new FieldReference(data.getPluralName(), 0L);
        thisDotField2.receiver = new ThisReference(0, 0);
        EqualExpression equalExpression = new EqualExpression(thisDotField, new NullLiteral(0, 0), 18);
        MessageSend createBuilderInvoke = new MessageSend();
        char[][] tokenizedName = makeGuavaTypeName(getSimpleTargetTypeName(data), false);
        createBuilderInvoke.receiver = new QualifiedNameReference(tokenizedName, NULL_POSS, 0, 0);
        createBuilderInvoke.selector = getBuilderMethodName(data);
        return new IfStatement(equalExpression, new Assignment(thisDotField2, createBuilderInvoke, 0), 0, 0);
    }

    protected int getTypeArgumentsCount() {
        return getArgumentSuffixes().size();
    }
}
