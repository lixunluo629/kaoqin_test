package lombok.eclipse.handlers.singulars;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.core.handlers.HandlerUtil;
import lombok.eclipse.EclipseNode;
import lombok.eclipse.handlers.EclipseHandlerUtil;
import lombok.eclipse.handlers.EclipseSingularsRecipes;
import org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.EqualExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.IfStatement;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.NullLiteral;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.lookup.TypeConstants;

/* loaded from: lombok-1.16.22.jar:lombok/eclipse/handlers/singulars/EclipseJavaUtilListSetSingularizer.SCL.lombok */
abstract class EclipseJavaUtilListSetSingularizer extends EclipseJavaUtilSingularizer {
    EclipseJavaUtilListSetSingularizer() {
    }

    @Override // lombok.eclipse.handlers.EclipseSingularsRecipes.EclipseSingularizer
    public List<char[]> listFieldsToBeGenerated(EclipseSingularsRecipes.SingularData data, EclipseNode builderType) {
        if (useGuavaInstead(builderType)) {
            return this.guavaListSetSingularizer.listFieldsToBeGenerated(data, builderType);
        }
        return super.listFieldsToBeGenerated(data, builderType);
    }

    @Override // lombok.eclipse.handlers.EclipseSingularsRecipes.EclipseSingularizer
    public List<char[]> listMethodsToBeGenerated(EclipseSingularsRecipes.SingularData data, EclipseNode builderType) {
        if (useGuavaInstead(builderType)) {
            return this.guavaListSetSingularizer.listMethodsToBeGenerated(data, builderType);
        }
        return super.listMethodsToBeGenerated(data, builderType);
    }

    @Override // lombok.eclipse.handlers.EclipseSingularsRecipes.EclipseSingularizer
    public List<EclipseNode> generateFields(EclipseSingularsRecipes.SingularData data, EclipseNode builderType) {
        if (useGuavaInstead(builderType)) {
            return this.guavaListSetSingularizer.generateFields(data, builderType);
        }
        TypeReference type = addTypeArgs(1, false, builderType, new QualifiedTypeReference(JAVA_UTIL_ARRAYLIST, NULL_POSS), data.getTypeArgs());
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
        if (useGuavaInstead(builderType)) {
            this.guavaListSetSingularizer.generateMethods(data, deprecate, builderType, fluent, chain);
            return;
        }
        TypeReference returnType = chain ? EclipseHandlerUtil.cloneSelfType(builderType) : TypeReference.baseTypeReference(6, 0);
        generateSingularMethod(deprecate, returnType, chain ? new ReturnStatement(new ThisReference(0, 0), 0, 0) : null, data, builderType, fluent);
        TypeReference returnType2 = chain ? EclipseHandlerUtil.cloneSelfType(builderType) : TypeReference.baseTypeReference(6, 0);
        generatePluralMethod(deprecate, returnType2, chain ? new ReturnStatement(new ThisReference(0, 0), 0, 0) : null, data, builderType, fluent);
        TypeReference returnType3 = chain ? EclipseHandlerUtil.cloneSelfType(builderType) : TypeReference.baseTypeReference(6, 0);
        generateClearMethod(deprecate, returnType3, chain ? new ReturnStatement(new ThisReference(0, 0), 0, 0) : null, data, builderType);
    }

    private void generateClearMethod(boolean deprecate, TypeReference returnType, Statement returnStatement, EclipseSingularsRecipes.SingularData data, EclipseNode builderType) {
        MethodDeclaration md = new MethodDeclaration(builderType.top().get().compilationResult);
        md.bits |= 8388608;
        md.modifiers = 1;
        FieldReference thisDotField = new FieldReference(data.getPluralName(), 0L);
        thisDotField.receiver = new ThisReference(0, 0);
        FieldReference thisDotField2 = new FieldReference(data.getPluralName(), 0L);
        thisDotField2.receiver = new ThisReference(0, 0);
        md.selector = HandlerUtil.buildAccessorName("clear", new String(data.getPluralName())).toCharArray();
        MessageSend clearMsg = new MessageSend();
        clearMsg.receiver = thisDotField2;
        clearMsg.selector = "clear".toCharArray();
        Statement clearStatement = new IfStatement(new EqualExpression(thisDotField, new NullLiteral(0, 0), 29), clearMsg, 0, 0);
        md.statements = returnStatement != null ? new Statement[]{clearStatement, returnStatement} : new Statement[]{clearStatement};
        md.returnType = returnType;
        md.annotations = deprecate ? new Annotation[]{EclipseHandlerUtil.generateDeprecatedAnnotation(data.getSource())} : null;
        EclipseHandlerUtil.injectMethod(builderType, md);
    }

    void generateSingularMethod(boolean deprecate, TypeReference returnType, Statement returnStatement, EclipseSingularsRecipes.SingularData data, EclipseNode builderType, boolean fluent) {
        MethodDeclaration md = new MethodDeclaration(builderType.top().get().compilationResult);
        md.bits |= 8388608;
        md.modifiers = 1;
        List<Statement> statements = new ArrayList<>();
        statements.add(createConstructBuilderVarIfNeeded(data, builderType, false));
        FieldReference thisDotField = new FieldReference(data.getPluralName(), 0L);
        thisDotField.receiver = new ThisReference(0, 0);
        MessageSend thisDotFieldDotAdd = new MessageSend();
        thisDotFieldDotAdd.arguments = new Expression[]{new SingleNameReference(data.getSingularName(), 0L)};
        thisDotFieldDotAdd.receiver = thisDotField;
        thisDotFieldDotAdd.selector = BeanUtil.PREFIX_ADDER.toCharArray();
        statements.add(thisDotFieldDotAdd);
        if (returnStatement != null) {
            statements.add(returnStatement);
        }
        md.statements = (Statement[]) statements.toArray(new Statement[statements.size()]);
        TypeReference paramType = cloneParamType(0, data.getTypeArgs(), builderType);
        Argument param = new Argument(data.getSingularName(), 0L, paramType, 0);
        md.arguments = new Argument[]{param};
        md.returnType = returnType;
        md.selector = fluent ? data.getSingularName() : HandlerUtil.buildAccessorName(BeanUtil.PREFIX_ADDER, new String(data.getSingularName())).toCharArray();
        md.annotations = deprecate ? new Annotation[]{EclipseHandlerUtil.generateDeprecatedAnnotation(data.getSource())} : null;
        data.setGeneratedByRecursive(md);
        EclipseHandlerUtil.injectMethod(builderType, md);
    }

    void generatePluralMethod(boolean deprecate, TypeReference returnType, Statement returnStatement, EclipseSingularsRecipes.SingularData data, EclipseNode builderType, boolean fluent) {
        MethodDeclaration md = new MethodDeclaration(builderType.top().get().compilationResult);
        md.bits |= 8388608;
        md.modifiers = 1;
        List<Statement> statements = new ArrayList<>();
        statements.add(createConstructBuilderVarIfNeeded(data, builderType, false));
        FieldReference thisDotField = new FieldReference(data.getPluralName(), 0L);
        thisDotField.receiver = new ThisReference(0, 0);
        MessageSend thisDotFieldDotAddAll = new MessageSend();
        thisDotFieldDotAddAll.arguments = new Expression[]{new SingleNameReference(data.getPluralName(), 0L)};
        thisDotFieldDotAddAll.receiver = thisDotField;
        thisDotFieldDotAddAll.selector = "addAll".toCharArray();
        statements.add(thisDotFieldDotAddAll);
        if (returnStatement != null) {
            statements.add(returnStatement);
        }
        md.statements = (Statement[]) statements.toArray(new Statement[statements.size()]);
        TypeReference paramType = addTypeArgs(1, true, builderType, new QualifiedTypeReference(TypeConstants.JAVA_UTIL_COLLECTION, NULL_POSS), data.getTypeArgs());
        Argument param = new Argument(data.getPluralName(), 0L, paramType, 0);
        md.arguments = new Argument[]{param};
        md.returnType = returnType;
        md.selector = fluent ? data.getPluralName() : HandlerUtil.buildAccessorName("addAll", new String(data.getPluralName())).toCharArray();
        md.annotations = deprecate ? new Annotation[]{EclipseHandlerUtil.generateDeprecatedAnnotation(data.getSource())} : null;
        data.setGeneratedByRecursive(md);
        EclipseHandlerUtil.injectMethod(builderType, md);
    }
}
