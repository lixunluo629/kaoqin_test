package lombok.javac.handlers;

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import lombok.javac.JavacNode;
import lombok.javac.JavacResolution;

/* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/JavacResolver.SCL.lombok */
public enum JavacResolver {
    CLASS { // from class: lombok.javac.handlers.JavacResolver.1
        @Override // lombok.javac.handlers.JavacResolver
        public Type resolveMember(JavacNode node, JCTree.JCExpression expr) {
            Type type = expr.type;
            if (type == null) {
                try {
                    new JavacResolution(node.getContext()).resolveClassMember(node);
                    type = expr.type;
                } catch (Exception e) {
                }
            }
            return type;
        }
    },
    METHOD { // from class: lombok.javac.handlers.JavacResolver.2
        @Override // lombok.javac.handlers.JavacResolver
        public Type resolveMember(JavacNode node, JCTree.JCExpression expr) {
            Type type = expr.type;
            if (type == null) {
                try {
                    JCTree.JCExpression resolvedExpression = new JavacResolution(node.getContext()).resolveMethodMember(node).get(expr);
                    if (resolvedExpression != null) {
                        type = resolvedExpression.type;
                    }
                } catch (Exception e) {
                }
            }
            return type;
        }
    },
    CLASS_AND_METHOD { // from class: lombok.javac.handlers.JavacResolver.3
        @Override // lombok.javac.handlers.JavacResolver
        public Type resolveMember(JavacNode node, JCTree.JCExpression expr) {
            JavacNode classNode;
            Type type = METHOD.resolveMember(node, expr);
            if (type == null) {
                JavacNode javacNodeUp = node;
                while (true) {
                    classNode = javacNodeUp;
                    if (classNode == null || !noneOf(classNode.get(), JCTree.JCBlock.class, JCTree.JCMethodDecl.class, JCTree.JCVariableDecl.class)) {
                        break;
                    }
                    javacNodeUp = classNode.up();
                }
                if (classNode != null) {
                    type = CLASS.resolveMember(classNode, expr);
                }
            }
            return type;
        }

        private boolean noneOf(Object o, Class<?>... clazzes) {
            for (Class<?> clazz : clazzes) {
                if (clazz.isInstance(o)) {
                    return false;
                }
            }
            return true;
        }
    };

    public abstract Type resolveMember(JavacNode javacNode, JCTree.JCExpression jCExpression);
}
