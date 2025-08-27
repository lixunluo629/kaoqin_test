package org.springframework.expression.spel.standard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.asm.ClassWriter;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Opcodes;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.CodeFlow;
import org.springframework.expression.spel.CompiledExpression;
import org.springframework.expression.spel.ast.SpelNodeImpl;
import org.springframework.util.ClassUtils;
import org.springframework.util.ConcurrentReferenceHashMap;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/standard/SpelCompiler.class */
public class SpelCompiler implements Opcodes {
    private static final Log logger = LogFactory.getLog(SpelCompiler.class);
    private static final Map<ClassLoader, SpelCompiler> compilers = new ConcurrentReferenceHashMap();
    private final ChildClassLoader ccl;
    private final AtomicInteger suffixId = new AtomicInteger(1);

    private SpelCompiler(ClassLoader classloader) {
        this.ccl = new ChildClassLoader(classloader);
    }

    public CompiledExpression compile(SpelNodeImpl expression) {
        if (expression.isCompilable()) {
            if (logger.isDebugEnabled()) {
                logger.debug("SpEL: compiling " + expression.toStringAST());
            }
            Class<? extends CompiledExpression> clazz = createExpressionClass(expression);
            if (clazz != null) {
                try {
                    return clazz.newInstance();
                } catch (Throwable ex) {
                    throw new IllegalStateException("Failed to instantiate CompiledExpression", ex);
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("SpEL: unable to compile " + expression.toStringAST());
            return null;
        }
        return null;
    }

    private int getNextSuffix() {
        return this.suffixId.incrementAndGet();
    }

    private Class<? extends CompiledExpression> createExpressionClass(SpelNodeImpl expressionToCompile) {
        String clazzName = "spel/Ex" + getNextSuffix();
        ClassWriter cw = new ExpressionClassWriter();
        cw.visit(49, 1, clazzName, null, "org/springframework/expression/spel/CompiledExpression", null);
        MethodVisitor mv = cw.visitMethod(1, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(25, 0);
        mv.visitMethodInsn(183, "org/springframework/expression/spel/CompiledExpression", "<init>", "()V", false);
        mv.visitInsn(177);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
        MethodVisitor mv2 = cw.visitMethod(1, "getValue", "(Ljava/lang/Object;Lorg/springframework/expression/EvaluationContext;)Ljava/lang/Object;", null, new String[]{"org/springframework/expression/EvaluationException"});
        mv2.visitCode();
        CodeFlow cf = new CodeFlow(clazzName, cw);
        try {
            expressionToCompile.generateCode(mv2, cf);
            CodeFlow.insertBoxIfNecessary(mv2, cf.lastDescriptor());
            if ("V".equals(cf.lastDescriptor())) {
                mv2.visitInsn(1);
            }
            mv2.visitInsn(176);
            mv2.visitMaxs(0, 0);
            mv2.visitEnd();
            cw.visitEnd();
            cf.finish();
            byte[] data = cw.toByteArray();
            return this.ccl.defineClass(clazzName.replaceAll("/", "."), data);
        } catch (IllegalStateException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug(expressionToCompile.getClass().getSimpleName() + ".generateCode opted out of compilation: " + ex.getMessage());
                return null;
            }
            return null;
        }
    }

    public static SpelCompiler getCompiler(ClassLoader classLoader) {
        SpelCompiler spelCompiler;
        ClassLoader clToUse = classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader();
        synchronized (compilers) {
            SpelCompiler compiler = compilers.get(clToUse);
            if (compiler == null) {
                compiler = new SpelCompiler(clToUse);
                compilers.put(clToUse, compiler);
            }
            spelCompiler = compiler;
        }
        return spelCompiler;
    }

    public static boolean compile(Expression expression) {
        return (expression instanceof SpelExpression) && ((SpelExpression) expression).compileExpression();
    }

    public static void revertToInterpreted(Expression expression) {
        if (expression instanceof SpelExpression) {
            ((SpelExpression) expression).revertToInterpreted();
        }
    }

    private static void dump(String expressionText, String name, byte[] bytecode) throws IOException {
        String nameToUse = name.replace('.', '/');
        String dir = nameToUse.indexOf(47) != -1 ? nameToUse.substring(0, nameToUse.lastIndexOf(47)) : "";
        String dumpLocation = null;
        try {
            File tempFile = File.createTempFile("tmp", null);
            dumpLocation = tempFile + File.separator + nameToUse + ClassUtils.CLASS_FILE_SUFFIX;
            tempFile.delete();
            File f = new File(tempFile, dir);
            f.mkdirs();
            if (logger.isDebugEnabled()) {
                logger.debug("Expression '" + expressionText + "' compiled code dumped to " + dumpLocation);
            }
            File f2 = new File(dumpLocation);
            FileOutputStream fos = new FileOutputStream(f2);
            fos.write(bytecode);
            fos.flush();
            fos.close();
        } catch (IOException ex) {
            throw new IllegalStateException("Unexpected problem dumping class '" + nameToUse + "' into " + dumpLocation, ex);
        }
    }

    /* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/standard/SpelCompiler$ChildClassLoader.class */
    private static class ChildClassLoader extends URLClassLoader {
        private static final URL[] NO_URLS = new URL[0];

        public ChildClassLoader(ClassLoader classLoader) {
            super(NO_URLS, classLoader);
        }

        public Class<?> defineClass(String name, byte[] bytes) {
            return super.defineClass(name, bytes, 0, bytes.length);
        }
    }

    /* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/standard/SpelCompiler$ExpressionClassWriter.class */
    private class ExpressionClassWriter extends ClassWriter {
        public ExpressionClassWriter() {
            super(3);
        }

        @Override // org.springframework.asm.ClassWriter
        protected ClassLoader getClassLoader() {
            return SpelCompiler.this.ccl;
        }
    }
}
