package org.aspectj.weaver.tools;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import org.aspectj.bridge.IMessageHandler;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.SourceLocation;
import org.aspectj.weaver.BindingScope;
import org.aspectj.weaver.IHasPosition;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.WeakClassLoaderReference;
import org.aspectj.weaver.World;
import org.aspectj.weaver.internal.tools.PointcutExpressionImpl;
import org.aspectj.weaver.internal.tools.TypePatternMatcherImpl;
import org.aspectj.weaver.patterns.AndPointcut;
import org.aspectj.weaver.patterns.CflowPointcut;
import org.aspectj.weaver.patterns.FormalBinding;
import org.aspectj.weaver.patterns.IScope;
import org.aspectj.weaver.patterns.KindedPointcut;
import org.aspectj.weaver.patterns.NotPointcut;
import org.aspectj.weaver.patterns.OrPointcut;
import org.aspectj.weaver.patterns.ParserException;
import org.aspectj.weaver.patterns.PatternParser;
import org.aspectj.weaver.patterns.Pointcut;
import org.aspectj.weaver.patterns.SimpleScope;
import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.aspectj.weaver.patterns.ThisOrTargetPointcut;
import org.aspectj.weaver.patterns.TypePattern;
import org.aspectj.weaver.reflect.PointcutParameterImpl;
import org.aspectj.weaver.reflect.ReflectionWorld;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/PointcutParser.class */
public class PointcutParser {
    private ReflectionWorld world;
    private WeakClassLoaderReference classLoaderReference;
    private final Set<PointcutPrimitive> supportedPrimitives;
    private final Set<PointcutDesignatorHandler> pointcutDesignators;

    public static Set<PointcutPrimitive> getAllSupportedPointcutPrimitives() {
        Set<PointcutPrimitive> primitives = new HashSet<>();
        primitives.add(PointcutPrimitive.ADVICE_EXECUTION);
        primitives.add(PointcutPrimitive.ARGS);
        primitives.add(PointcutPrimitive.CALL);
        primitives.add(PointcutPrimitive.EXECUTION);
        primitives.add(PointcutPrimitive.GET);
        primitives.add(PointcutPrimitive.HANDLER);
        primitives.add(PointcutPrimitive.INITIALIZATION);
        primitives.add(PointcutPrimitive.PRE_INITIALIZATION);
        primitives.add(PointcutPrimitive.SET);
        primitives.add(PointcutPrimitive.STATIC_INITIALIZATION);
        primitives.add(PointcutPrimitive.TARGET);
        primitives.add(PointcutPrimitive.THIS);
        primitives.add(PointcutPrimitive.WITHIN);
        primitives.add(PointcutPrimitive.WITHIN_CODE);
        primitives.add(PointcutPrimitive.AT_ANNOTATION);
        primitives.add(PointcutPrimitive.AT_THIS);
        primitives.add(PointcutPrimitive.AT_TARGET);
        primitives.add(PointcutPrimitive.AT_ARGS);
        primitives.add(PointcutPrimitive.AT_WITHIN);
        primitives.add(PointcutPrimitive.AT_WITHINCODE);
        primitives.add(PointcutPrimitive.REFERENCE);
        return primitives;
    }

    public static PointcutParser getPointcutParserSupportingAllPrimitivesAndUsingContextClassloaderForResolution() {
        PointcutParser p = new PointcutParser();
        p.setClassLoader(Thread.currentThread().getContextClassLoader());
        return p;
    }

    public static PointcutParser getPointcutParserSupportingSpecifiedPrimitivesAndUsingContextClassloaderForResolution(Set<PointcutPrimitive> supportedPointcutKinds) {
        PointcutParser p = new PointcutParser(supportedPointcutKinds);
        p.setClassLoader(Thread.currentThread().getContextClassLoader());
        return p;
    }

    public static PointcutParser getPointcutParserSupportingAllPrimitivesAndUsingSpecifiedClassloaderForResolution(ClassLoader classLoader) {
        PointcutParser p = new PointcutParser();
        p.setClassLoader(classLoader);
        return p;
    }

    public static PointcutParser getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(Set<PointcutPrimitive> supportedPointcutKinds, ClassLoader classLoader) {
        PointcutParser p = new PointcutParser(supportedPointcutKinds);
        p.setClassLoader(classLoader);
        return p;
    }

    protected PointcutParser() {
        this.pointcutDesignators = new HashSet();
        this.supportedPrimitives = getAllSupportedPointcutPrimitives();
        setClassLoader(PointcutParser.class.getClassLoader());
    }

    private PointcutParser(Set<PointcutPrimitive> supportedPointcutKinds) {
        this.pointcutDesignators = new HashSet();
        this.supportedPrimitives = supportedPointcutKinds;
        for (PointcutPrimitive pointcutPrimitive : supportedPointcutKinds) {
            if (pointcutPrimitive == PointcutPrimitive.IF || pointcutPrimitive == PointcutPrimitive.CFLOW || pointcutPrimitive == PointcutPrimitive.CFLOW_BELOW) {
                throw new UnsupportedOperationException("Cannot handle if, cflow, and cflowbelow primitives");
            }
        }
        setClassLoader(PointcutParser.class.getClassLoader());
    }

    protected void setWorld(ReflectionWorld aWorld) {
        this.world = aWorld;
    }

    protected void setClassLoader(ClassLoader aLoader) {
        this.classLoaderReference = new WeakClassLoaderReference(aLoader);
        this.world = ReflectionWorld.getReflectionWorldFor(this.classLoaderReference);
    }

    protected void setClassLoader(ClassLoader aLoader, boolean shareWorlds) {
        this.classLoaderReference = new WeakClassLoaderReference(aLoader);
        if (shareWorlds) {
            this.world = ReflectionWorld.getReflectionWorldFor(this.classLoaderReference);
        } else {
            this.world = new ReflectionWorld(this.classLoaderReference);
        }
    }

    public void setLintProperties(String resourcePath) throws IOException {
        URL url = this.classLoaderReference.getClassLoader().getResource(resourcePath);
        InputStream is = url.openStream();
        Properties p = new Properties();
        p.load(is);
        setLintProperties(p);
    }

    public void setLintProperties(Properties properties) {
        getWorld().getLint().setFromProperties(properties);
    }

    public void registerPointcutDesignatorHandler(PointcutDesignatorHandler designatorHandler) {
        this.pointcutDesignators.add(designatorHandler);
        if (this.world != null) {
            this.world.registerPointcutHandler(designatorHandler);
        }
    }

    public PointcutParameter createPointcutParameter(String name, Class<?> type) {
        return new PointcutParameterImpl(name, type);
    }

    public PointcutExpression parsePointcutExpression(String expression) throws UnsupportedPointcutPrimitiveException, IllegalArgumentException {
        return parsePointcutExpression(expression, null, new PointcutParameter[0]);
    }

    public PointcutExpression parsePointcutExpression(String expression, Class<?> inScope, PointcutParameter[] formalParameters) throws UnsupportedPointcutPrimitiveException, IllegalArgumentException {
        try {
            Pointcut pc = concretizePointcutExpression(resolvePointcutExpression(expression, inScope, formalParameters), inScope, formalParameters);
            validateAgainstSupportedPrimitives(pc, expression);
            PointcutExpressionImpl pcExpr = new PointcutExpressionImpl(pc, expression, formalParameters, getWorld());
            return pcExpr;
        } catch (ParserException pEx) {
            throw new IllegalArgumentException(buildUserMessageFromParserException(expression, pEx));
        } catch (ReflectionWorld.ReflectionWorldException rwEx) {
            throw new IllegalArgumentException(rwEx.getMessage());
        }
    }

    protected Pointcut resolvePointcutExpression(String expression, Class<?> inScope, PointcutParameter[] formalParameters) {
        try {
            PatternParser parser = new PatternParser(expression);
            parser.setPointcutDesignatorHandlers(this.pointcutDesignators, this.world);
            Pointcut pc = parser.parsePointcut();
            validateAgainstSupportedPrimitives(pc, expression);
            IScope resolutionScope = buildResolutionScope(inScope == null ? Object.class : inScope, formalParameters);
            return pc.resolve(resolutionScope);
        } catch (ParserException pEx) {
            throw new IllegalArgumentException(buildUserMessageFromParserException(expression, pEx));
        }
    }

    protected Pointcut concretizePointcutExpression(Pointcut pc, Class<?> inScope, PointcutParameter[] formalParameters) {
        ResolvedType declaringTypeForResolution;
        if (inScope != null) {
            declaringTypeForResolution = getWorld().resolve(inScope.getName());
        } else {
            declaringTypeForResolution = ResolvedType.OBJECT.resolve(getWorld());
        }
        IntMap arity = new IntMap(formalParameters.length);
        for (int i = 0; i < formalParameters.length; i++) {
            arity.put(i, i);
        }
        return pc.concretize(declaringTypeForResolution, declaringTypeForResolution, arity);
    }

    public TypePatternMatcher parseTypePattern(String typePattern) throws IllegalArgumentException {
        try {
            TypePattern tp = new PatternParser(typePattern).parseTypePattern();
            tp.resolve(this.world);
            return new TypePatternMatcherImpl(tp, this.world);
        } catch (ParserException pEx) {
            throw new IllegalArgumentException(buildUserMessageFromParserException(typePattern, pEx));
        } catch (ReflectionWorld.ReflectionWorldException rwEx) {
            throw new IllegalArgumentException(rwEx.getMessage());
        }
    }

    private World getWorld() {
        return this.world;
    }

    Set<PointcutPrimitive> getSupportedPrimitives() {
        return this.supportedPrimitives;
    }

    IMessageHandler setCustomMessageHandler(IMessageHandler aHandler) {
        IMessageHandler current = getWorld().getMessageHandler();
        getWorld().setMessageHandler(aHandler);
        return current;
    }

    private IScope buildResolutionScope(Class<?> inScope, PointcutParameter[] formalParameters) {
        if (formalParameters == null) {
            formalParameters = new PointcutParameter[0];
        }
        FormalBinding[] formalBindings = new FormalBinding[formalParameters.length];
        for (int i = 0; i < formalBindings.length; i++) {
            formalBindings[i] = new FormalBinding(toUnresolvedType(formalParameters[i].getType()), formalParameters[i].getName(), i);
        }
        if (inScope == null) {
            return new SimpleScope(getWorld(), formalBindings);
        }
        ResolvedType inType = getWorld().resolve(inScope.getName());
        ISourceContext sourceContext = new ISourceContext() { // from class: org.aspectj.weaver.tools.PointcutParser.1
            @Override // org.aspectj.weaver.ISourceContext
            public ISourceLocation makeSourceLocation(IHasPosition position) {
                return new SourceLocation(new File(""), 0);
            }

            @Override // org.aspectj.weaver.ISourceContext
            public ISourceLocation makeSourceLocation(int line, int offset) {
                return new SourceLocation(new File(""), line);
            }

            @Override // org.aspectj.weaver.ISourceContext
            public int getOffset() {
                return 0;
            }

            @Override // org.aspectj.weaver.ISourceContext
            public void tidy() {
            }
        };
        return new BindingScope(inType, sourceContext, formalBindings);
    }

    private UnresolvedType toUnresolvedType(Class<?> clazz) {
        if (clazz.isArray()) {
            return UnresolvedType.forSignature(clazz.getName().replace('.', '/'));
        }
        return UnresolvedType.forName(clazz.getName());
    }

    private void validateAgainstSupportedPrimitives(Pointcut pc, String expression) {
        switch (pc.getPointcutKind()) {
            case 1:
                validateKindedPointcut((KindedPointcut) pc, expression);
                return;
            case 2:
                if (!this.supportedPrimitives.contains(PointcutPrimitive.WITHIN)) {
                    throw new UnsupportedPointcutPrimitiveException(expression, PointcutPrimitive.WITHIN);
                }
                return;
            case 3:
                boolean isThis = ((ThisOrTargetPointcut) pc).isThis();
                if (isThis && !this.supportedPrimitives.contains(PointcutPrimitive.THIS)) {
                    throw new UnsupportedPointcutPrimitiveException(expression, PointcutPrimitive.THIS);
                }
                if (!this.supportedPrimitives.contains(PointcutPrimitive.TARGET)) {
                    throw new UnsupportedPointcutPrimitiveException(expression, PointcutPrimitive.TARGET);
                }
                return;
            case 4:
                if (!this.supportedPrimitives.contains(PointcutPrimitive.ARGS)) {
                    throw new UnsupportedPointcutPrimitiveException(expression, PointcutPrimitive.ARGS);
                }
                return;
            case 5:
                validateAgainstSupportedPrimitives(((AndPointcut) pc).getLeft(), expression);
                validateAgainstSupportedPrimitives(((AndPointcut) pc).getRight(), expression);
                return;
            case 6:
                validateAgainstSupportedPrimitives(((OrPointcut) pc).getLeft(), expression);
                validateAgainstSupportedPrimitives(((OrPointcut) pc).getRight(), expression);
                return;
            case 7:
                validateAgainstSupportedPrimitives(((NotPointcut) pc).getNegatedPointcut(), expression);
                return;
            case 8:
                if (!this.supportedPrimitives.contains(PointcutPrimitive.REFERENCE)) {
                    throw new UnsupportedPointcutPrimitiveException(expression, PointcutPrimitive.REFERENCE);
                }
                return;
            case 9:
            case 14:
            case 15:
                throw new UnsupportedPointcutPrimitiveException(expression, PointcutPrimitive.IF);
            case 10:
                CflowPointcut cfp = (CflowPointcut) pc;
                if (cfp.isCflowBelow()) {
                    throw new UnsupportedPointcutPrimitiveException(expression, PointcutPrimitive.CFLOW_BELOW);
                }
                throw new UnsupportedPointcutPrimitiveException(expression, PointcutPrimitive.CFLOW);
            case 11:
            case 20:
            default:
                throw new IllegalArgumentException("Unknown pointcut kind: " + ((int) pc.getPointcutKind()));
            case 12:
                if (!this.supportedPrimitives.contains(PointcutPrimitive.WITHIN_CODE)) {
                    throw new UnsupportedPointcutPrimitiveException(expression, PointcutPrimitive.WITHIN_CODE);
                }
                return;
            case 13:
                if (!this.supportedPrimitives.contains(PointcutPrimitive.HANDLER)) {
                    throw new UnsupportedPointcutPrimitiveException(expression, PointcutPrimitive.HANDLER);
                }
                return;
            case 16:
                if (!this.supportedPrimitives.contains(PointcutPrimitive.AT_ANNOTATION)) {
                    throw new UnsupportedPointcutPrimitiveException(expression, PointcutPrimitive.AT_ANNOTATION);
                }
                return;
            case 17:
                if (!this.supportedPrimitives.contains(PointcutPrimitive.AT_WITHIN)) {
                    throw new UnsupportedPointcutPrimitiveException(expression, PointcutPrimitive.AT_WITHIN);
                }
                return;
            case 18:
                if (!this.supportedPrimitives.contains(PointcutPrimitive.AT_WITHINCODE)) {
                    throw new UnsupportedPointcutPrimitiveException(expression, PointcutPrimitive.AT_WITHINCODE);
                }
                return;
            case 19:
                boolean isThis2 = ((ThisOrTargetAnnotationPointcut) pc).isThis();
                if (isThis2 && !this.supportedPrimitives.contains(PointcutPrimitive.AT_THIS)) {
                    throw new UnsupportedPointcutPrimitiveException(expression, PointcutPrimitive.AT_THIS);
                }
                if (!this.supportedPrimitives.contains(PointcutPrimitive.AT_TARGET)) {
                    throw new UnsupportedPointcutPrimitiveException(expression, PointcutPrimitive.AT_TARGET);
                }
                return;
            case 21:
                if (!this.supportedPrimitives.contains(PointcutPrimitive.AT_ARGS)) {
                    throw new UnsupportedPointcutPrimitiveException(expression, PointcutPrimitive.AT_ARGS);
                }
                return;
            case 22:
                return;
        }
    }

    private void validateKindedPointcut(KindedPointcut pc, String expression) {
        Shadow.Kind kind = pc.getKind();
        if (kind == Shadow.MethodCall || kind == Shadow.ConstructorCall) {
            if (!this.supportedPrimitives.contains(PointcutPrimitive.CALL)) {
                throw new UnsupportedPointcutPrimitiveException(expression, PointcutPrimitive.CALL);
            }
            return;
        }
        if (kind == Shadow.MethodExecution || kind == Shadow.ConstructorExecution) {
            if (!this.supportedPrimitives.contains(PointcutPrimitive.EXECUTION)) {
                throw new UnsupportedPointcutPrimitiveException(expression, PointcutPrimitive.EXECUTION);
            }
            return;
        }
        if (kind == Shadow.AdviceExecution) {
            if (!this.supportedPrimitives.contains(PointcutPrimitive.ADVICE_EXECUTION)) {
                throw new UnsupportedPointcutPrimitiveException(expression, PointcutPrimitive.ADVICE_EXECUTION);
            }
            return;
        }
        if (kind == Shadow.FieldGet) {
            if (!this.supportedPrimitives.contains(PointcutPrimitive.GET)) {
                throw new UnsupportedPointcutPrimitiveException(expression, PointcutPrimitive.GET);
            }
            return;
        }
        if (kind == Shadow.FieldSet) {
            if (!this.supportedPrimitives.contains(PointcutPrimitive.SET)) {
                throw new UnsupportedPointcutPrimitiveException(expression, PointcutPrimitive.SET);
            }
            return;
        }
        if (kind == Shadow.Initialization) {
            if (!this.supportedPrimitives.contains(PointcutPrimitive.INITIALIZATION)) {
                throw new UnsupportedPointcutPrimitiveException(expression, PointcutPrimitive.INITIALIZATION);
            }
        } else if (kind == Shadow.PreInitialization) {
            if (!this.supportedPrimitives.contains(PointcutPrimitive.PRE_INITIALIZATION)) {
                throw new UnsupportedPointcutPrimitiveException(expression, PointcutPrimitive.PRE_INITIALIZATION);
            }
        } else if (kind == Shadow.StaticInitialization && !this.supportedPrimitives.contains(PointcutPrimitive.STATIC_INITIALIZATION)) {
            throw new UnsupportedPointcutPrimitiveException(expression, PointcutPrimitive.STATIC_INITIALIZATION);
        }
    }

    private String buildUserMessageFromParserException(String pc, ParserException ex) {
        StringBuffer msg = new StringBuffer();
        msg.append("Pointcut is not well-formed: expecting '");
        msg.append(ex.getMessage());
        msg.append("'");
        IHasPosition location = ex.getLocation();
        msg.append(" at character position ");
        msg.append(location.getStart());
        msg.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        msg.append(pc);
        msg.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        for (int i = 0; i < location.getStart(); i++) {
            msg.append(SymbolConstants.SPACE_SYMBOL);
        }
        for (int j = location.getStart(); j <= location.getEnd(); j++) {
            msg.append("^");
        }
        msg.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        return msg.toString();
    }
}
