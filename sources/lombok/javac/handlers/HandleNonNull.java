package lombok.javac.handlers;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import java.util.Iterator;
import lombok.ConfigurationKeys;
import lombok.NonNull;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.core.HandlerPriority;
import lombok.core.handlers.HandlerUtil;
import lombok.javac.Javac;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;

@HandlerPriority(512)
/* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleNonNull.SCL.lombok */
public class HandleNonNull extends JavacAnnotationHandler<NonNull> {
    @Override // lombok.javac.JavacAnnotationHandler
    public void handle(AnnotationValues<NonNull> annotation, JCTree.JCAnnotation ast, JavacNode annotationNode) {
        HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.NON_NULL_FLAG_USAGE, "@NonNull");
        if (annotationNode.up().getKind() == AST.Kind.FIELD) {
            try {
                if (Javac.isPrimitive(annotationNode.up().get().vartype)) {
                    annotationNode.addWarning("@NonNull is meaningless on a primitive.");
                }
                return;
            } catch (Exception e) {
                return;
            }
        }
        if (annotationNode.up().getKind() != AST.Kind.ARGUMENT) {
            return;
        }
        try {
            JCTree.JCMethodDecl declaration = annotationNode.up().up().get();
            if (declaration.body == null) {
                return;
            }
            JCTree.JCStatement nullCheck = JavacHandlerUtil.recursiveSetGeneratedBy(JavacHandlerUtil.generateNullCheck(annotationNode.getTreeMaker(), annotationNode.up(), annotationNode), ast, annotationNode.getContext());
            if (nullCheck == null) {
                annotationNode.addWarning("@NonNull is meaningless on a primitive.");
                return;
            }
            List<JCTree.JCStatement> statements = declaration.body.stats;
            String expectedName = annotationNode.up().getName();
            List<JCTree.JCStatement> stats = statements;
            int idx = 0;
            while (stats.size() > idx) {
                int i = idx;
                idx++;
                JCTree.JCSynchronized jCSynchronized = (JCTree.JCStatement) stats.get(i);
                if (!JavacHandlerUtil.isConstructorCall(jCSynchronized)) {
                    if (jCSynchronized instanceof JCTree.JCTry) {
                        stats = ((JCTree.JCTry) jCSynchronized).body.stats;
                        idx = 0;
                    } else if (jCSynchronized instanceof JCTree.JCSynchronized) {
                        stats = jCSynchronized.body.stats;
                        idx = 0;
                    } else {
                        String varNameOfNullCheck = returnVarNameIfNullCheck(jCSynchronized);
                        if (varNameOfNullCheck == null) {
                            break;
                        } else if (varNameOfNullCheck.equals(expectedName)) {
                            return;
                        }
                    }
                }
            }
            List<JCTree.JCStatement> tail = statements;
            List<JCTree.JCStatement> head = List.nil();
            Iterator it = statements.iterator();
            while (it.hasNext()) {
                JCTree.JCStatement stat = (JCTree.JCStatement) it.next();
                if (!JavacHandlerUtil.isConstructorCall(stat) && (!JavacHandlerUtil.isGenerated(stat) || !isNullCheck(stat))) {
                    break;
                }
                tail = tail.tail;
                head = head.prepend(stat);
            }
            List<JCTree.JCStatement> newList = tail.prepend(nullCheck);
            Iterator it2 = head.iterator();
            while (it2.hasNext()) {
                newList = newList.prepend((JCTree.JCStatement) it2.next());
            }
            declaration.body.stats = newList;
            annotationNode.getAst().setChanged();
        } catch (Exception e2) {
        }
    }

    public boolean isNullCheck(JCTree.JCStatement stat) {
        return returnVarNameIfNullCheck(stat) != null;
    }

    public String returnVarNameIfNullCheck(JCTree.JCStatement stat) {
        JCTree.JCExpression cond;
        if (!(stat instanceof JCTree.JCIf)) {
            return null;
        }
        JCTree.JCStatement then = ((JCTree.JCIf) stat).thenpart;
        if (then instanceof JCTree.JCBlock) {
            List<JCTree.JCStatement> stats = ((JCTree.JCBlock) then).stats;
            if (stats.length() == 0) {
                return null;
            }
            then = (JCTree.JCStatement) stats.get(0);
        }
        if (!(then instanceof JCTree.JCThrow)) {
            return null;
        }
        JCTree.JCExpression jCExpression = ((JCTree.JCIf) stat).cond;
        while (true) {
            cond = jCExpression;
            if (!(cond instanceof JCTree.JCParens)) {
                break;
            }
            jCExpression = ((JCTree.JCParens) cond).expr;
        }
        if (!(cond instanceof JCTree.JCBinary)) {
            return null;
        }
        JCTree.JCBinary bin = (JCTree.JCBinary) cond;
        if (Javac.CTC_EQUAL.equals(JavacTreeMaker.TreeTag.treeTag((JCTree) bin)) && (bin.lhs instanceof JCTree.JCIdent) && (bin.rhs instanceof JCTree.JCLiteral) && Javac.CTC_BOT.equals(JavacTreeMaker.TypeTag.typeTag((JCTree) bin.rhs))) {
            return bin.lhs.name.toString();
        }
        return null;
    }
}
