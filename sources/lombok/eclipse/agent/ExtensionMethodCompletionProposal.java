package lombok.eclipse.agent;

import java.util.Arrays;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.codeassist.CompletionEngine;
import org.eclipse.jdt.internal.codeassist.InternalCompletionProposal;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnMemberAccess;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnQualifiedNameReference;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnSingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;

/* loaded from: lombok-1.16.22.jar:lombok/eclipse/agent/ExtensionMethodCompletionProposal.SCL.lombok */
public class ExtensionMethodCompletionProposal extends InternalCompletionProposal {
    public ExtensionMethodCompletionProposal(int replacementOffset) {
        super(6, replacementOffset - 1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v16, types: [char[], char[][]] */
    /* JADX WARN: Type inference failed for: r0v18, types: [char[], char[][]] */
    public void setMethodBinding(MethodBinding method, ASTNode node) {
        MethodBinding original = method.original();
        TypeBinding[] parameters = (TypeBinding[]) Arrays.copyOf(method.parameters, method.parameters.length);
        method.parameters = (TypeBinding[]) Arrays.copyOfRange(method.parameters, 1, method.parameters.length);
        TypeBinding[] originalParameters = null;
        if (original != method) {
            originalParameters = (TypeBinding[]) Arrays.copyOf(method.original().parameters, method.original().parameters.length);
            method.original().parameters = (TypeBinding[]) Arrays.copyOfRange(method.original().parameters, 1, method.original().parameters.length);
        }
        int length = method.parameters == null ? 0 : method.parameters.length;
        ?? r0 = new char[length];
        ?? r02 = new char[length];
        for (int i = 0; i < length; i++) {
            TypeBinding type = method.original().parameters[i];
            r0[i] = type.qualifiedPackageName();
            r02[i] = type.qualifiedSourceName();
        }
        char[] completion = CharOperation.concat(method.selector, new char[]{'(', ')'});
        setDeclarationSignature(CompletionEngine.getSignature(method.declaringClass));
        setSignature(CompletionEngine.getSignature(method));
        if (original != method) {
            setOriginalSignature(CompletionEngine.getSignature(original));
        }
        setDeclarationPackageName(method.declaringClass.qualifiedPackageName());
        setDeclarationTypeName(method.declaringClass.qualifiedSourceName());
        setParameterPackageNames(r0);
        setParameterTypeNames(r02);
        setPackageName(method.returnType.qualifiedPackageName());
        setTypeName(method.returnType.qualifiedSourceName());
        setName(method.selector);
        setCompletion(completion);
        setFlags(method.modifiers & (-9));
        int index = node.sourceEnd + 1;
        if (node instanceof CompletionOnQualifiedNameReference) {
            index -= ((CompletionOnQualifiedNameReference) node).completionIdentifier.length;
        }
        if (node instanceof CompletionOnMemberAccess) {
            index -= ((CompletionOnMemberAccess) node).token.length;
        }
        if (node instanceof CompletionOnSingleNameReference) {
            index -= ((CompletionOnSingleNameReference) node).token.length;
        }
        setReplaceRange(index, index);
        setTokenRange(index, index);
        setRelevance(100);
        method.parameters = parameters;
        if (original != method) {
            method.original().parameters = originalParameters;
        }
    }
}
