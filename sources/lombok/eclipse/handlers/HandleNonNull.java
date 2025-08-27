package lombok.eclipse.handlers;

import java.util.Arrays;
import lombok.ConfigurationKeys;
import lombok.NonNull;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.core.HandlerPriority;
import lombok.core.handlers.HandlerUtil;
import lombok.eclipse.DeferUntilPostDiet;
import lombok.eclipse.Eclipse;
import lombok.eclipse.EclipseAnnotationHandler;
import lombok.eclipse.EclipseNode;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.Block;
import org.eclipse.jdt.internal.compiler.ast.EqualExpression;
import org.eclipse.jdt.internal.compiler.ast.IfStatement;
import org.eclipse.jdt.internal.compiler.ast.NullLiteral;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.SynchronizedStatement;
import org.eclipse.jdt.internal.compiler.ast.ThrowStatement;
import org.eclipse.jdt.internal.compiler.ast.TryStatement;

@DeferUntilPostDiet
@HandlerPriority(512)
/* loaded from: lombok-1.16.22.jar:lombok/eclipse/handlers/HandleNonNull.SCL.lombok */
public class HandleNonNull extends EclipseAnnotationHandler<NonNull> {
    @Override // lombok.eclipse.EclipseAnnotationHandler
    public void handle(AnnotationValues<NonNull> annotation, Annotation ast, EclipseNode annotationNode) {
        HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.NON_NULL_FLAG_USAGE, "@NonNull");
        if (annotationNode.up().getKind() == AST.Kind.FIELD) {
            try {
                if (Eclipse.isPrimitive(annotationNode.up().get().type)) {
                    annotationNode.addWarning("@NonNull is meaningless on a primitive.");
                    return;
                }
                return;
            } catch (Exception unused) {
                return;
            }
        }
        if (annotationNode.up().getKind() != AST.Kind.ARGUMENT) {
            return;
        }
        try {
            Argument arg = annotationNode.up().get();
            AbstractMethodDeclaration declaration = annotationNode.up().up().get();
            if (EclipseHandlerUtil.isGenerated(declaration) || declaration.isAbstract()) {
                return;
            }
            Statement nullCheck = EclipseHandlerUtil.generateNullCheck(arg, annotationNode);
            if (nullCheck == null) {
                annotationNode.addWarning("@NonNull is meaningless on a primitive.");
                return;
            }
            if (declaration.statements == null) {
                declaration.statements = new Statement[]{nullCheck};
            } else {
                char[] expectedName = arg.name;
                Statement[] stats = declaration.statements;
                int idx = 0;
                while (stats != null && stats.length > idx) {
                    int i = idx;
                    idx++;
                    Statement stat = stats[i];
                    if (stat instanceof TryStatement) {
                        stats = ((TryStatement) stat).tryBlock.statements;
                        idx = 0;
                    } else if (stat instanceof SynchronizedStatement) {
                        stats = ((SynchronizedStatement) stat).block.statements;
                        idx = 0;
                    } else {
                        char[] varNameOfNullCheck = returnVarNameIfNullCheck(stat);
                        if (varNameOfNullCheck == null) {
                            break;
                        } else if (Arrays.equals(varNameOfNullCheck, expectedName)) {
                            return;
                        }
                    }
                }
                Statement[] newStatements = new Statement[declaration.statements.length + 1];
                int skipOver = 0;
                for (ASTNode aSTNode : declaration.statements) {
                    if (!EclipseHandlerUtil.isGenerated(aSTNode) || !isNullCheck(aSTNode)) {
                        break;
                    }
                    skipOver++;
                }
                System.arraycopy(declaration.statements, 0, newStatements, 0, skipOver);
                System.arraycopy(declaration.statements, skipOver, newStatements, skipOver + 1, declaration.statements.length - skipOver);
                newStatements[skipOver] = nullCheck;
                declaration.statements = newStatements;
            }
            annotationNode.up().up().rebuild();
        } catch (Exception unused2) {
        }
    }

    public boolean isNullCheck(Statement stat) {
        return returnVarNameIfNullCheck(stat) != null;
    }

    public char[] returnVarNameIfNullCheck(Statement stat) {
        if (!(stat instanceof IfStatement)) {
            return null;
        }
        Statement then = ((IfStatement) stat).thenStatement;
        if (then instanceof Block) {
            Statement[] blockStatements = ((Block) then).statements;
            if (blockStatements == null || blockStatements.length == 0) {
                return null;
            }
            then = blockStatements[0];
        }
        if (!(then instanceof ThrowStatement)) {
            return null;
        }
        EqualExpression equalExpression = ((IfStatement) stat).condition;
        if (!(equalExpression instanceof EqualExpression)) {
            return null;
        }
        EqualExpression bin = equalExpression;
        int operatorId = (bin.bits & 4032) >> 6;
        if (operatorId == 18 && (bin.left instanceof SingleNameReference) && (bin.right instanceof NullLiteral)) {
            return bin.left.token;
        }
        return null;
    }
}
