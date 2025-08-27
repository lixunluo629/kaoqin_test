package lombok.javac.handlers;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.ConfigurationKeys;
import lombok.SneakyThrows;
import lombok.core.AnnotationValues;
import lombok.core.HandlerPriority;
import lombok.core.handlers.HandlerUtil;
import lombok.javac.Javac;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import org.springframework.util.ClassUtils;

@HandlerPriority(1024)
/* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleSneakyThrows.SCL.lombok */
public class HandleSneakyThrows extends JavacAnnotationHandler<SneakyThrows> {
    @Override // lombok.javac.JavacAnnotationHandler
    public void handle(AnnotationValues<SneakyThrows> annotation, JCTree.JCAnnotation ast, JavacNode annotationNode) {
        HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.SNEAKY_THROWS_FLAG_USAGE, "@SneakyThrows");
        JavacHandlerUtil.deleteAnnotationIfNeccessary(annotationNode, (Class<? extends Annotation>) SneakyThrows.class);
        Collection<String> exceptionNames = annotation.getRawExpressions("value");
        if (exceptionNames.isEmpty()) {
            exceptionNames = Collections.singleton("java.lang.Throwable");
        }
        List<String> exceptions = new ArrayList<>();
        for (String exception : exceptionNames) {
            if (exception.endsWith(ClassUtils.CLASS_FILE_SUFFIX)) {
                exception = exception.substring(0, exception.length() - 6);
            }
            exceptions.add(exception);
        }
        JavacNode owner = annotationNode.up();
        switch (owner.getKind()) {
            case METHOD:
                handleMethod(annotationNode, owner.get(), exceptions);
                break;
            default:
                annotationNode.addError("@SneakyThrows is legal only on methods and constructors.");
                break;
        }
    }

    public void handleMethod(JavacNode annotation, JCTree.JCMethodDecl method, Collection<String> exceptions) {
        JavacNode methodNode = annotation.up();
        if ((method.mods.flags & 1024) != 0) {
            annotation.addError("@SneakyThrows can only be used on concrete methods.");
            return;
        }
        if (method.body == null || method.body.stats.isEmpty()) {
            generateEmptyBlockWarning(methodNode, annotation, false);
            return;
        }
        JCTree.JCStatement constructorCall = (JCTree.JCStatement) method.body.stats.get(0);
        boolean isConstructorCall = JavacHandlerUtil.isConstructorCall(constructorCall);
        com.sun.tools.javac.util.List<JCTree.JCStatement> contents = isConstructorCall ? method.body.stats.tail : method.body.stats;
        if (contents == null || contents.isEmpty()) {
            generateEmptyBlockWarning(methodNode, annotation, true);
            return;
        }
        for (String exception : exceptions) {
            contents = com.sun.tools.javac.util.List.of(buildTryCatchBlock(methodNode, contents, exception, annotation.get()));
        }
        method.body.stats = isConstructorCall ? com.sun.tools.javac.util.List.of(constructorCall).appendList(contents) : contents;
        methodNode.rebuild();
    }

    public void generateEmptyBlockWarning(JavacNode methodNode, JavacNode annotation, boolean hasConstructorCall) {
        if (hasConstructorCall) {
            annotation.addWarning("Calls to sibling / super constructors are always excluded from @SneakyThrows; @SneakyThrows has been ignored because there is no other code in this constructor.");
        } else {
            annotation.addWarning("This method or constructor is empty; @SneakyThrows has been ignored.");
        }
    }

    public JCTree.JCStatement buildTryCatchBlock(JavacNode node, com.sun.tools.javac.util.List<JCTree.JCStatement> contents, String exception, JCTree source) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        JavacTreeMaker maker = node.getTreeMaker();
        Context context = node.getContext();
        JCTree.JCBlock tryBlock = (JCTree.JCBlock) JavacHandlerUtil.setGeneratedBy(maker.Block(0L, contents), source, context);
        JCTree.JCExpression varType = JavacHandlerUtil.chainDots(node, exception.split("\\."));
        JCTree.JCVariableDecl catchParam = maker.VarDef(maker.Modifiers(8589934608L), node.toName("$ex"), varType, null);
        JCTree.JCExpression lombokLombokSneakyThrowNameRef = JavacHandlerUtil.chainDots(node, "lombok", "Lombok", "sneakyThrow");
        JCTree.JCBlock catchBody = maker.Block(0L, com.sun.tools.javac.util.List.of(maker.Throw(maker.Apply(com.sun.tools.javac.util.List.nil(), lombokLombokSneakyThrowNameRef, com.sun.tools.javac.util.List.of(maker.Ident(node.toName("$ex")))))));
        JCTree.JCTry tryStatement = maker.Try(tryBlock, com.sun.tools.javac.util.List.of(JavacHandlerUtil.recursiveSetGeneratedBy(maker.Catch(catchParam, catchBody), source, context)), null);
        if (JavacHandlerUtil.inNetbeansEditor(node)) {
            JCTree.JCCompilationUnit top = node.top().get();
            int startPos = ((JCTree.JCStatement) contents.head).pos;
            int endPos = Javac.getEndPosition(((JCTree.JCStatement) contents.last()).pos(), top);
            tryBlock.pos = startPos;
            tryStatement.pos = startPos;
            Javac.storeEnd(tryBlock, endPos, top);
            Javac.storeEnd(tryStatement, endPos, top);
        }
        return JavacHandlerUtil.setGeneratedBy(tryStatement, source, context);
    }
}
