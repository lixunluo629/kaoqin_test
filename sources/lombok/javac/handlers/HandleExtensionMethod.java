package lombok.javac.handlers;

import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.tree.JCTree;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.ElementKind;
import lombok.ConfigurationKeys;
import lombok.core.AnnotationValues;
import lombok.core.HandlerPriority;
import lombok.core.handlers.HandlerUtil;
import lombok.experimental.ExtensionMethod;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import org.apache.ibatis.ognl.OgnlContext;

@HandlerPriority(66560)
/* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleExtensionMethod.SCL.lombok */
public class HandleExtensionMethod extends JavacAnnotationHandler<ExtensionMethod> {
    @Override // lombok.javac.JavacAnnotationHandler
    public void handle(AnnotationValues<ExtensionMethod> annotation, JCTree.JCAnnotation source, JavacNode annotationNode) {
        HandlerUtil.handleExperimentalFlagUsage(annotationNode, ConfigurationKeys.EXTENSION_METHOD_FLAG_USAGE, "@ExtensionMethod");
        JavacHandlerUtil.deleteAnnotationIfNeccessary(annotationNode, (Class<? extends Annotation>) ExtensionMethod.class);
        JavacNode typeNode = annotationNode.up();
        boolean isClassOrEnum = JavacHandlerUtil.isClassOrEnum(typeNode);
        if (!isClassOrEnum) {
            annotationNode.addError("@ExtensionMethod can only be used on a class or an enum");
            return;
        }
        boolean suppressBaseMethods = ((ExtensionMethod) annotation.getInstance()).suppressBaseMethods();
        List<Object> extensionProviders = annotation.getActualExpressions("value");
        if (extensionProviders.isEmpty()) {
            annotationNode.addError(String.format("@%s has no effect since no extension types were specified.", ExtensionMethod.class.getName()));
            return;
        }
        List<Extension> extensions = getExtensions(annotationNode, extensionProviders);
        if (extensions.isEmpty()) {
            return;
        }
        new ExtensionMethodReplaceVisitor(annotationNode, extensions, suppressBaseMethods).replace();
        annotationNode.rebuild();
    }

    public List<Extension> getExtensions(JavacNode typeNode, List<Object> extensionProviders) {
        Type providerType;
        List<Extension> extensions = new ArrayList<>();
        for (Object extensionProvider : extensionProviders) {
            if (extensionProvider instanceof JCTree.JCFieldAccess) {
                JCTree.JCFieldAccess provider = (JCTree.JCFieldAccess) extensionProvider;
                if ("class".equals(provider.name.toString()) && (providerType = JavacResolver.CLASS.resolveMember(typeNode, provider.selected)) != null && (providerType.tsym.flags() & 8704) == 0) {
                    extensions.add(getExtension(typeNode, (Type.ClassType) providerType));
                }
            }
        }
        return extensions;
    }

    public Extension getExtension(JavacNode typeNode, Type.ClassType extensionMethodProviderType) {
        List<Symbol.MethodSymbol> extensionMethods = new ArrayList<>();
        Symbol.TypeSymbol tsym = extensionMethodProviderType.asElement();
        if (tsym != null) {
            for (Symbol.MethodSymbol methodSymbol : tsym.getEnclosedElements()) {
                if (methodSymbol.getKind() == ElementKind.METHOD) {
                    Symbol.MethodSymbol method = methodSymbol;
                    if ((method.flags() & 9) != 0 && !method.params().isEmpty()) {
                        extensionMethods.add(method);
                    }
                }
            }
        }
        return new Extension(extensionMethods, tsym);
    }

    /* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleExtensionMethod$Extension.SCL.lombok */
    private static class Extension {
        final List<Symbol.MethodSymbol> extensionMethods;
        final Symbol.TypeSymbol extensionProvider;

        public Extension(List<Symbol.MethodSymbol> extensionMethods, Symbol.TypeSymbol extensionProvider) {
            this.extensionMethods = extensionMethods;
            this.extensionProvider = extensionProvider;
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleExtensionMethod$ExtensionMethodReplaceVisitor.SCL.lombok */
    private static class ExtensionMethodReplaceVisitor extends TreeScanner<Void, Void> {
        final JavacNode annotationNode;
        final List<Extension> extensions;
        final boolean suppressBaseMethods;

        public ExtensionMethodReplaceVisitor(JavacNode annotationNode, List<Extension> extensions, boolean suppressBaseMethods) {
            this.annotationNode = annotationNode;
            this.extensions = extensions;
            this.suppressBaseMethods = suppressBaseMethods;
        }

        public void replace() {
            this.annotationNode.up().get().accept(this, (Object) null);
        }

        public Void visitMethodInvocation(MethodInvocationTree tree, Void p) {
            handleMethodCall((JCTree.JCMethodInvocation) tree);
            return (Void) super.visitMethodInvocation(tree, p);
        }

        private void handleMethodCall(JCTree.JCMethodInvocation methodCall) {
            Type resolvedMethodCall;
            Type receiverType;
            JavacNode methodCallNode = this.annotationNode.getAst().get(methodCall);
            if (methodCallNode == null) {
                return;
            }
            JavacNode surroundingType = JavacHandlerUtil.upToTypeNode(methodCallNode);
            Symbol.TypeSymbol surroundingTypeSymbol = surroundingType.get().sym;
            JCTree.JCExpression receiver = receiverOf(methodCall);
            String methodName = methodNameOf(methodCall);
            if (OgnlContext.THIS_CONTEXT_KEY.equals(methodName) || "super".equals(methodName) || (resolvedMethodCall = JavacResolver.CLASS_AND_METHOD.resolveMember(methodCallNode, methodCall)) == null) {
                return;
            }
            if ((!this.suppressBaseMethods && !(resolvedMethodCall instanceof Type.ErrorType)) || (receiverType = JavacResolver.CLASS_AND_METHOD.resolveMember(methodCallNode, receiver)) == null || receiverType.tsym.toString().endsWith(receiver.toString())) {
                return;
            }
            Types types = Types.instance(this.annotationNode.getContext());
            for (Extension extension : this.extensions) {
                Symbol.TypeSymbol extensionProvider = extension.extensionProvider;
                if (surroundingTypeSymbol != extensionProvider) {
                    for (Symbol.MethodSymbol extensionMethod : extension.extensionMethods) {
                        if (methodName.equals(extensionMethod.name.toString())) {
                            Type extensionMethodType = extensionMethod.type;
                            if (Type.MethodType.class.isInstance(extensionMethodType) || Type.ForAll.class.isInstance(extensionMethodType)) {
                                Type firstArgType = types.erasure((Type) extensionMethodType.asMethodType().argtypes.get(0));
                                if (types.isAssignable(receiverType, firstArgType)) {
                                    methodCall.args = methodCall.args.prepend(receiver);
                                    methodCall.meth = JavacHandlerUtil.chainDotsString(this.annotationNode, extensionProvider.toString() + "." + methodName);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }

        private String methodNameOf(JCTree.JCMethodInvocation methodCall) {
            if (methodCall.meth instanceof JCTree.JCIdent) {
                return methodCall.meth.name.toString();
            }
            return methodCall.meth.name.toString();
        }

        private JCTree.JCExpression receiverOf(JCTree.JCMethodInvocation methodCall) {
            if (methodCall.meth instanceof JCTree.JCIdent) {
                return this.annotationNode.getTreeMaker().Ident(this.annotationNode.toName(OgnlContext.THIS_CONTEXT_KEY));
            }
            return methodCall.meth.selected;
        }
    }
}
