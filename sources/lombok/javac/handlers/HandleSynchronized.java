package lombok.javac.handlers;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import java.lang.annotation.Annotation;
import lombok.ConfigurationKeys;
import lombok.Synchronized;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.core.HandlerPriority;
import lombok.core.handlers.HandlerUtil;
import lombok.javac.Javac;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import lombok.javac.handlers.JavacHandlerUtil;
import org.apache.ibatis.ognl.OgnlContext;

@HandlerPriority(1024)
/* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleSynchronized.SCL.lombok */
public class HandleSynchronized extends JavacAnnotationHandler<Synchronized> {
    private static final String INSTANCE_LOCK_NAME = "$lock";
    private static final String STATIC_LOCK_NAME = "$LOCK";

    @Override // lombok.javac.JavacAnnotationHandler
    public void handle(AnnotationValues<Synchronized> annotation, JCTree.JCAnnotation ast, JavacNode annotationNode) {
        JCTree.JCExpression lockNode;
        HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.SYNCHRONIZED_FLAG_USAGE, "@Synchronized");
        if (JavacHandlerUtil.inNetbeansEditor(annotationNode)) {
            return;
        }
        JavacHandlerUtil.deleteAnnotationIfNeccessary(annotationNode, (Class<? extends Annotation>) Synchronized.class);
        JavacNode methodNode = annotationNode.up();
        if (methodNode == null || methodNode.getKind() != AST.Kind.METHOD || !(methodNode.get() instanceof JCTree.JCMethodDecl)) {
            annotationNode.addError("@Synchronized is legal only on methods.");
            return;
        }
        JCTree.JCMethodDecl method = methodNode.get();
        if ((method.mods.flags & 1024) != 0) {
            annotationNode.addError("@Synchronized is legal only on concrete methods.");
            return;
        }
        boolean isStatic = (method.mods.flags & 8) != 0;
        String lockName = ((Synchronized) annotation.getInstance()).value();
        boolean autoMake = false;
        if (lockName.length() == 0) {
            autoMake = true;
            lockName = isStatic ? STATIC_LOCK_NAME : INSTANCE_LOCK_NAME;
        }
        JavacTreeMaker maker = methodNode.getTreeMaker().at(ast.pos);
        Context context = methodNode.getContext();
        if (JavacHandlerUtil.fieldExists(lockName, methodNode) == JavacHandlerUtil.MemberExistsResult.NOT_EXISTS) {
            if (!autoMake) {
                annotationNode.addError("The field " + lockName + " does not exist.");
                return;
            }
            JCTree.JCExpression objectType = JavacHandlerUtil.genJavaLangTypeRef(methodNode, ast.pos, "Object");
            JCTree.JCNewArray newObjectArray = maker.NewArray(JavacHandlerUtil.genJavaLangTypeRef(methodNode, ast.pos, "Object"), List.of(maker.Literal(Javac.CTC_INT, 0)), null);
            JCTree.JCVariableDecl fieldDecl = JavacHandlerUtil.recursiveSetGeneratedBy(maker.VarDef(maker.Modifiers(18 | (isStatic ? 8 : 0)), methodNode.toName(lockName), objectType, newObjectArray), ast, context);
            JavacHandlerUtil.injectFieldAndMarkGenerated(methodNode.up(), fieldDecl);
        }
        if (method.body == null) {
            return;
        }
        if (isStatic) {
            lockNode = JavacHandlerUtil.chainDots(methodNode, ast.pos, methodNode.up().getName(), lockName, new String[0]);
        } else {
            lockNode = maker.Select(maker.Ident(methodNode.toName(OgnlContext.THIS_CONTEXT_KEY)), methodNode.toName(lockName));
        }
        JavacHandlerUtil.recursiveSetGeneratedBy(lockNode, ast, context);
        method.body = JavacHandlerUtil.setGeneratedBy(maker.Block(0L, List.of(JavacHandlerUtil.setGeneratedBy(maker.Synchronized(lockNode, method.body), ast, context))), ast, context);
        methodNode.rebuild();
    }
}
