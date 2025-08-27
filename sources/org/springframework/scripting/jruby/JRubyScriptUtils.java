package org.springframework.scripting.jruby;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;
import org.jruby.Ruby;
import org.jruby.RubyArray;
import org.jruby.RubyNil;
import org.jruby.ast.ClassNode;
import org.jruby.ast.Colon2Node;
import org.jruby.ast.NewlineNode;
import org.jruby.ast.Node;
import org.jruby.exceptions.JumpException;
import org.jruby.exceptions.RaiseException;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.DynamicScope;
import org.jruby.runtime.builtin.IRubyObject;
import org.springframework.core.NestedRuntimeException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

@Deprecated
/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scripting/jruby/JRubyScriptUtils.class */
public abstract class JRubyScriptUtils {
    public static Object createJRubyObject(String scriptSource, Class<?>... interfaces) throws JumpException {
        return createJRubyObject(scriptSource, interfaces, ClassUtils.getDefaultClassLoader());
    }

    public static Object createJRubyObject(String scriptSource, Class<?>[] interfaces, ClassLoader classLoader) {
        Ruby ruby = initializeRuntime();
        Node scriptRootNode = ruby.parseEval(scriptSource, "", (DynamicScope) null, 0);
        IRubyObject rubyObject = ruby.runNormally(scriptRootNode);
        if (rubyObject instanceof RubyNil) {
            String className = findClassName(scriptRootNode);
            rubyObject = ruby.evalScriptlet(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR + className + ".new");
        }
        if (rubyObject instanceof RubyNil) {
            throw new IllegalStateException("Compilation of JRuby script returned RubyNil: " + rubyObject);
        }
        return Proxy.newProxyInstance(classLoader, interfaces, new RubyObjectInvocationHandler(rubyObject, ruby));
    }

    private static Ruby initializeRuntime() {
        return JavaEmbedUtils.initialize(Collections.EMPTY_LIST);
    }

    private static String findClassName(Node rootNode) {
        ClassNode classNode = findClassNode(rootNode);
        if (classNode == null) {
            throw new IllegalArgumentException("Unable to determine class name for root node '" + rootNode + "'");
        }
        Colon2Node node = classNode.getCPath();
        return node.getName();
    }

    private static ClassNode findClassNode(Node node) {
        if (node == null) {
            return null;
        }
        if (node instanceof ClassNode) {
            return (ClassNode) node;
        }
        List<Node> children = node.childNodes();
        for (Node node2 : children) {
            if (node2 instanceof ClassNode) {
                return (ClassNode) node2;
            }
            if (node2 instanceof NewlineNode) {
                NewlineNode nn = (NewlineNode) node2;
                ClassNode found = findClassNode(nn.getNextNode());
                if (found != null) {
                    return found;
                }
            }
        }
        for (Node child : children) {
            ClassNode found2 = findClassNode(child);
            if (found2 != null) {
                return found2;
            }
        }
        return null;
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scripting/jruby/JRubyScriptUtils$RubyObjectInvocationHandler.class */
    private static class RubyObjectInvocationHandler implements InvocationHandler {
        private final IRubyObject rubyObject;
        private final Ruby ruby;

        public RubyObjectInvocationHandler(IRubyObject rubyObject, Ruby ruby) {
            this.rubyObject = rubyObject;
            this.ruby = ruby;
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (ReflectionUtils.isEqualsMethod(method)) {
                return Boolean.valueOf(isProxyForSameRubyObject(args[0]));
            }
            if (ReflectionUtils.isHashCodeMethod(method)) {
                return Integer.valueOf(this.rubyObject.hashCode());
            }
            if (ReflectionUtils.isToStringMethod(method)) {
                String toStringResult = this.rubyObject.toString();
                if (!StringUtils.hasText(toStringResult)) {
                    toStringResult = ObjectUtils.identityToString(this.rubyObject);
                }
                return "JRuby object [" + toStringResult + "]";
            }
            try {
                IRubyObject[] rubyArgs = convertToRuby(args);
                IRubyObject rubyResult = this.rubyObject.callMethod(this.ruby.getCurrentContext(), method.getName(), rubyArgs);
                return convertFromRuby(rubyResult, method.getReturnType());
            } catch (RaiseException ex) {
                throw new JRubyExecutionException(ex);
            }
        }

        private boolean isProxyForSameRubyObject(Object other) throws IllegalArgumentException {
            if (!Proxy.isProxyClass(other.getClass())) {
                return false;
            }
            InvocationHandler ih = Proxy.getInvocationHandler(other);
            return (ih instanceof RubyObjectInvocationHandler) && this.rubyObject.equals(((RubyObjectInvocationHandler) ih).rubyObject);
        }

        private IRubyObject[] convertToRuby(Object[] javaArgs) {
            if (javaArgs == null || javaArgs.length == 0) {
                return new IRubyObject[0];
            }
            IRubyObject[] rubyArgs = new IRubyObject[javaArgs.length];
            for (int i = 0; i < javaArgs.length; i++) {
                rubyArgs[i] = JavaEmbedUtils.javaToRuby(this.ruby, javaArgs[i]);
            }
            return rubyArgs;
        }

        private Object convertFromRuby(IRubyObject rubyResult, Class<?> returnType) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
            Object result = JavaEmbedUtils.rubyToJava(this.ruby, rubyResult, returnType);
            if ((result instanceof RubyArray) && returnType.isArray()) {
                result = convertFromRubyArray(((RubyArray) result).toJavaArray(), returnType);
            }
            return result;
        }

        private Object convertFromRubyArray(IRubyObject[] rubyArray, Class<?> returnType) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
            Class<?> targetType = returnType.getComponentType();
            Object javaArray = Array.newInstance(targetType, rubyArray.length);
            for (int i = 0; i < rubyArray.length; i++) {
                IRubyObject rubyObject = rubyArray[i];
                Array.set(javaArray, i, convertFromRuby(rubyObject, targetType));
            }
            return javaArray;
        }
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scripting/jruby/JRubyScriptUtils$JRubyExecutionException.class */
    public static class JRubyExecutionException extends NestedRuntimeException {
        public JRubyExecutionException(RaiseException ex) {
            super(ex.getMessage(), ex);
        }
    }
}
