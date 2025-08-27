package lombok.eclipse.handlers.singulars;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.core.LombokImmutableList;
import lombok.core.handlers.HandlerUtil;
import lombok.eclipse.EclipseNode;
import lombok.eclipse.handlers.EclipseHandlerUtil;
import lombok.eclipse.handlers.EclipseSingularsRecipes;
import org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.Block;
import org.eclipse.jdt.internal.compiler.ast.EqualExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.ForeachStatement;
import org.eclipse.jdt.internal.compiler.ast.IfStatement;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.NullLiteral;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;

/* loaded from: lombok-1.16.22.jar:lombok/eclipse/handlers/singulars/EclipseJavaUtilMapSingularizer.SCL.lombok */
public class EclipseJavaUtilMapSingularizer extends EclipseJavaUtilSingularizer {
    @Override // lombok.eclipse.handlers.EclipseSingularsRecipes.EclipseSingularizer
    public LombokImmutableList<String> getSupportedTypes() {
        return LombokImmutableList.of("java.util.Map", "java.util.SortedMap", "java.util.NavigableMap");
    }

    /* JADX WARN: Type inference failed for: r0v25, types: [char[], java.lang.Object[]] */
    @Override // lombok.eclipse.handlers.EclipseSingularsRecipes.EclipseSingularizer
    public List<char[]> listFieldsToBeGenerated(EclipseSingularsRecipes.SingularData data, EclipseNode builderType) {
        if (useGuavaInstead(builderType)) {
            return this.guavaMapSingularizer.listFieldsToBeGenerated(data, builderType);
        }
        char[] p = data.getPluralName();
        int len = p.length;
        char[] k = new char[len + 4];
        char[] v = new char[len + 6];
        System.arraycopy(p, 0, k, 0, len);
        System.arraycopy(p, 0, v, 0, len);
        k[len] = '$';
        k[len + 1] = 'k';
        k[len + 2] = 'e';
        k[len + 3] = 'y';
        v[len] = '$';
        v[len + 1] = 'v';
        v[len + 2] = 'a';
        v[len + 3] = 'l';
        v[len + 4] = 'u';
        v[len + 5] = 'e';
        return Arrays.asList(new char[]{k, v});
    }

    @Override // lombok.eclipse.handlers.EclipseSingularsRecipes.EclipseSingularizer
    public List<char[]> listMethodsToBeGenerated(EclipseSingularsRecipes.SingularData data, EclipseNode builderType) {
        if (useGuavaInstead(builderType)) {
            return this.guavaMapSingularizer.listFieldsToBeGenerated(data, builderType);
        }
        return super.listMethodsToBeGenerated(data, builderType);
    }

    @Override // lombok.eclipse.handlers.EclipseSingularsRecipes.EclipseSingularizer
    public List<EclipseNode> generateFields(EclipseSingularsRecipes.SingularData data, EclipseNode builderType) {
        if (useGuavaInstead(builderType)) {
            return this.guavaMapSingularizer.generateFields(data, builderType);
        }
        char[] keyName = (String.valueOf(new String(data.getPluralName())) + "$key").toCharArray();
        char[] valueName = (String.valueOf(new String(data.getPluralName())) + "$value").toCharArray();
        TypeReference type = addTypeArgs(1, false, builderType, new QualifiedTypeReference(JAVA_UTIL_ARRAYLIST, NULL_POSS), data.getTypeArgs());
        FieldDeclaration buildKeyField = new FieldDeclaration(keyName, 0, -1);
        buildKeyField.bits |= 8388608;
        buildKeyField.modifiers = 2;
        buildKeyField.declarationSourceEnd = -1;
        buildKeyField.type = type;
        QualifiedTypeReference qualifiedTypeReference = new QualifiedTypeReference(JAVA_UTIL_ARRAYLIST, NULL_POSS);
        List<TypeReference> tArgs = data.getTypeArgs();
        TypeReference type2 = addTypeArgs(1, false, builderType, qualifiedTypeReference, (tArgs == null || tArgs.size() <= 1) ? Collections.emptyList() : Collections.singletonList(tArgs.get(1)));
        FieldDeclaration buildValueField = new FieldDeclaration(valueName, 0, -1);
        buildValueField.bits |= 8388608;
        buildValueField.modifiers = 2;
        buildValueField.declarationSourceEnd = -1;
        buildValueField.type = type2;
        data.setGeneratedByRecursive(buildKeyField);
        data.setGeneratedByRecursive(buildValueField);
        EclipseNode keyFieldNode = EclipseHandlerUtil.injectFieldAndMarkGenerated(builderType, buildKeyField);
        EclipseNode valueFieldNode = EclipseHandlerUtil.injectFieldAndMarkGenerated(builderType, buildValueField);
        return Arrays.asList(keyFieldNode, valueFieldNode);
    }

    @Override // lombok.eclipse.handlers.EclipseSingularsRecipes.EclipseSingularizer
    public void generateMethods(EclipseSingularsRecipes.SingularData data, boolean deprecate, EclipseNode builderType, boolean fluent, boolean chain) {
        if (useGuavaInstead(builderType)) {
            this.guavaMapSingularizer.generateMethods(data, deprecate, builderType, fluent, chain);
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
        String pN = new String(data.getPluralName());
        char[] keyFieldName = (String.valueOf(pN) + "$key").toCharArray();
        char[] valueFieldName = (String.valueOf(pN) + "$value").toCharArray();
        FieldReference thisDotField = new FieldReference(keyFieldName, 0L);
        thisDotField.receiver = new ThisReference(0, 0);
        FieldReference thisDotField2 = new FieldReference(keyFieldName, 0L);
        thisDotField2.receiver = new ThisReference(0, 0);
        FieldReference thisDotField3 = new FieldReference(valueFieldName, 0L);
        thisDotField3.receiver = new ThisReference(0, 0);
        md.selector = HandlerUtil.buildAccessorName("clear", new String(data.getPluralName())).toCharArray();
        Statement messageSend = new MessageSend();
        ((MessageSend) messageSend).receiver = thisDotField2;
        ((MessageSend) messageSend).selector = "clear".toCharArray();
        Statement messageSend2 = new MessageSend();
        ((MessageSend) messageSend2).receiver = thisDotField3;
        ((MessageSend) messageSend2).selector = "clear".toCharArray();
        Block clearMsgs = new Block(2);
        clearMsgs.statements = new Statement[]{messageSend, messageSend2};
        Statement clearStatement = new IfStatement(new EqualExpression(thisDotField, new NullLiteral(0, 0), 29), clearMsgs, 0, 0);
        md.statements = returnStatement != null ? new Statement[]{clearStatement, returnStatement} : new Statement[]{clearStatement};
        md.returnType = returnType;
        md.annotations = deprecate ? new Annotation[]{EclipseHandlerUtil.generateDeprecatedAnnotation(data.getSource())} : null;
        EclipseHandlerUtil.injectMethod(builderType, md);
    }

    private void generateSingularMethod(boolean deprecate, TypeReference returnType, Statement returnStatement, EclipseSingularsRecipes.SingularData data, EclipseNode builderType, boolean fluent) {
        MethodDeclaration md = new MethodDeclaration(builderType.top().get().compilationResult);
        md.bits |= 8388608;
        md.modifiers = 1;
        List<Statement> statements = new ArrayList<>();
        statements.add(createConstructBuilderVarIfNeeded(data, builderType, true));
        String sN = new String(data.getSingularName());
        String pN = new String(data.getPluralName());
        char[] keyParamName = (String.valueOf(sN) + "Key").toCharArray();
        char[] valueParamName = (String.valueOf(sN) + "Value").toCharArray();
        char[] keyFieldName = (String.valueOf(pN) + "$key").toCharArray();
        char[] valueFieldName = (String.valueOf(pN) + "$value").toCharArray();
        FieldReference thisDotKeyField = new FieldReference(keyFieldName, 0L);
        thisDotKeyField.receiver = new ThisReference(0, 0);
        MessageSend thisDotKeyFieldDotAdd = new MessageSend();
        thisDotKeyFieldDotAdd.arguments = new Expression[]{new SingleNameReference(keyParamName, 0L)};
        thisDotKeyFieldDotAdd.receiver = thisDotKeyField;
        thisDotKeyFieldDotAdd.selector = BeanUtil.PREFIX_ADDER.toCharArray();
        statements.add(thisDotKeyFieldDotAdd);
        FieldReference thisDotValueField = new FieldReference(valueFieldName, 0L);
        thisDotValueField.receiver = new ThisReference(0, 0);
        MessageSend thisDotValueFieldDotAdd = new MessageSend();
        thisDotValueFieldDotAdd.arguments = new Expression[]{new SingleNameReference(valueParamName, 0L)};
        thisDotValueFieldDotAdd.receiver = thisDotValueField;
        thisDotValueFieldDotAdd.selector = BeanUtil.PREFIX_ADDER.toCharArray();
        statements.add(thisDotValueFieldDotAdd);
        if (returnStatement != null) {
            statements.add(returnStatement);
        }
        md.statements = (Statement[]) statements.toArray(new Statement[statements.size()]);
        TypeReference keyParamType = cloneParamType(0, data.getTypeArgs(), builderType);
        Argument keyParam = new Argument(keyParamName, 0L, keyParamType, 0);
        TypeReference valueParamType = cloneParamType(1, data.getTypeArgs(), builderType);
        Argument valueParam = new Argument(valueParamName, 0L, valueParamType, 0);
        md.arguments = new Argument[]{keyParam, valueParam};
        md.returnType = returnType;
        md.selector = fluent ? data.getSingularName() : HandlerUtil.buildAccessorName("put", new String(data.getSingularName())).toCharArray();
        md.annotations = deprecate ? new Annotation[]{EclipseHandlerUtil.generateDeprecatedAnnotation(data.getSource())} : null;
        data.setGeneratedByRecursive(md);
        EclipseHandlerUtil.injectMethod(builderType, md);
    }

    private void generatePluralMethod(boolean deprecate, TypeReference returnType, Statement returnStatement, EclipseSingularsRecipes.SingularData data, EclipseNode builderType, boolean fluent) {
        MethodDeclaration md = new MethodDeclaration(builderType.top().get().compilationResult);
        md.bits |= 8388608;
        md.modifiers = 1;
        String pN = new String(data.getPluralName());
        char[] keyFieldName = (String.valueOf(pN) + "$key").toCharArray();
        char[] valueFieldName = (String.valueOf(pN) + "$value").toCharArray();
        List<Statement> statements = new ArrayList<>();
        statements.add(createConstructBuilderVarIfNeeded(data, builderType, true));
        char[] entryName = "$lombokEntry".toCharArray();
        TypeReference forEachType = addTypeArgs(2, true, builderType, new QualifiedTypeReference(JAVA_UTIL_MAP_ENTRY, NULL_POSS), data.getTypeArgs());
        Expression messageSend = new MessageSend();
        ((MessageSend) messageSend).receiver = new SingleNameReference(entryName, 0L);
        ((MessageSend) messageSend).selector = "getKey".toCharArray();
        Statement messageSend2 = new MessageSend();
        FieldReference thisDotKeyField = new FieldReference(keyFieldName, 0L);
        thisDotKeyField.receiver = new ThisReference(0, 0);
        ((MessageSend) messageSend2).receiver = thisDotKeyField;
        ((MessageSend) messageSend2).selector = new char[]{'a', 'd', 'd'};
        ((MessageSend) messageSend2).arguments = new Expression[]{messageSend};
        Expression messageSend3 = new MessageSend();
        ((MessageSend) messageSend3).receiver = new SingleNameReference(entryName, 0L);
        ((MessageSend) messageSend3).selector = "getValue".toCharArray();
        Statement messageSend4 = new MessageSend();
        FieldReference thisDotValueField = new FieldReference(valueFieldName, 0L);
        thisDotValueField.receiver = new ThisReference(0, 0);
        ((MessageSend) messageSend4).receiver = thisDotValueField;
        ((MessageSend) messageSend4).selector = new char[]{'a', 'd', 'd'};
        ((MessageSend) messageSend4).arguments = new Expression[]{messageSend3};
        LocalDeclaration elementVariable = new LocalDeclaration(entryName, 0, 0);
        elementVariable.type = forEachType;
        ForeachStatement forEach = new ForeachStatement(elementVariable, 0);
        MessageSend invokeEntrySet = new MessageSend();
        invokeEntrySet.selector = new char[]{'e', 'n', 't', 'r', 'y', 'S', 'e', 't'};
        invokeEntrySet.receiver = new SingleNameReference(data.getPluralName(), 0L);
        forEach.collection = invokeEntrySet;
        Block forEachContent = new Block(0);
        forEachContent.statements = new Statement[]{messageSend2, messageSend4};
        forEach.action = forEachContent;
        statements.add(forEach);
        if (returnStatement != null) {
            statements.add(returnStatement);
        }
        md.statements = (Statement[]) statements.toArray(new Statement[statements.size()]);
        TypeReference paramType = addTypeArgs(2, true, builderType, new QualifiedTypeReference(JAVA_UTIL_MAP, NULL_POSS), data.getTypeArgs());
        Argument param = new Argument(data.getPluralName(), 0L, paramType, 0);
        md.arguments = new Argument[]{param};
        md.returnType = returnType;
        md.selector = fluent ? data.getPluralName() : HandlerUtil.buildAccessorName("putAll", new String(data.getPluralName())).toCharArray();
        md.annotations = deprecate ? new Annotation[]{EclipseHandlerUtil.generateDeprecatedAnnotation(data.getSource())} : null;
        data.setGeneratedByRecursive(md);
        EclipseHandlerUtil.injectMethod(builderType, md);
    }

    @Override // lombok.eclipse.handlers.EclipseSingularsRecipes.EclipseSingularizer
    public void appendBuildCode(EclipseSingularsRecipes.SingularData data, EclipseNode builderType, List<Statement> statements, char[] targetVariableName) {
        if (useGuavaInstead(builderType)) {
            this.guavaMapSingularizer.appendBuildCode(data, builderType, statements, targetVariableName);
        } else if (data.getTargetFqn().equals("java.util.Map")) {
            statements.addAll(createJavaUtilSetMapInitialCapacitySwitchStatements(data, builderType, true, "emptyMap", "singletonMap", "LinkedHashMap"));
        } else {
            statements.addAll(createJavaUtilSimpleCreationAndFillStatements(data, builderType, true, true, false, true, "TreeMap"));
        }
    }
}
