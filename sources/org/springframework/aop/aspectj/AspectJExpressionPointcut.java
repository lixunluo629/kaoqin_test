package org.springframework.aop.aspectj;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.patterns.NamePattern;
import org.aspectj.weaver.reflect.ReflectionWorld;
import org.aspectj.weaver.reflect.ShadowMatchImpl;
import org.aspectj.weaver.tools.ContextBasedMatcher;
import org.aspectj.weaver.tools.JoinPointMatch;
import org.aspectj.weaver.tools.MatchingContext;
import org.aspectj.weaver.tools.PointcutDesignatorHandler;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParameter;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;
import org.aspectj.weaver.tools.ShadowMatch;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.IntroductionAwareMethodMatcher;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.aop.framework.autoproxy.ProxyCreationContext;
import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
import org.springframework.aop.support.AbstractExpressionPointcut;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/aspectj/AspectJExpressionPointcut.class */
public class AspectJExpressionPointcut extends AbstractExpressionPointcut implements ClassFilter, IntroductionAwareMethodMatcher, BeanFactoryAware {
    private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet();
    private static final Log logger;
    private Class<?> pointcutDeclarationScope;
    private String[] pointcutParameterNames;
    private Class<?>[] pointcutParameterTypes;
    private BeanFactory beanFactory;
    private transient ClassLoader pointcutClassLoader;
    private transient PointcutExpression pointcutExpression;
    private transient Map<Method, ShadowMatch> shadowMatchCache;

    static {
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.ARGS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.REFERENCE);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.THIS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.TARGET);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.WITHIN);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ANNOTATION);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_WITHIN);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ARGS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_TARGET);
        logger = LogFactory.getLog(AspectJExpressionPointcut.class);
    }

    public AspectJExpressionPointcut() {
        this.pointcutParameterNames = new String[0];
        this.pointcutParameterTypes = new Class[0];
        this.shadowMatchCache = new ConcurrentHashMap(32);
    }

    public AspectJExpressionPointcut(Class<?> declarationScope, String[] paramNames, Class<?>[] paramTypes) {
        this.pointcutParameterNames = new String[0];
        this.pointcutParameterTypes = new Class[0];
        this.shadowMatchCache = new ConcurrentHashMap(32);
        this.pointcutDeclarationScope = declarationScope;
        if (paramNames.length != paramTypes.length) {
            throw new IllegalStateException("Number of pointcut parameter names must match number of pointcut parameter types");
        }
        this.pointcutParameterNames = paramNames;
        this.pointcutParameterTypes = paramTypes;
    }

    public void setPointcutDeclarationScope(Class<?> pointcutDeclarationScope) {
        this.pointcutDeclarationScope = pointcutDeclarationScope;
    }

    public void setParameterNames(String... names) {
        this.pointcutParameterNames = names;
    }

    public void setParameterTypes(Class<?>... types) {
        this.pointcutParameterTypes = types;
    }

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override // org.springframework.aop.Pointcut
    public ClassFilter getClassFilter() {
        checkReadyToMatch();
        return this;
    }

    @Override // org.springframework.aop.Pointcut
    public MethodMatcher getMethodMatcher() {
        checkReadyToMatch();
        return this;
    }

    private void checkReadyToMatch() {
        if (getExpression() == null) {
            throw new IllegalStateException("Must set property 'expression' before attempting to match");
        }
        if (this.pointcutExpression == null) {
            this.pointcutClassLoader = determinePointcutClassLoader();
            this.pointcutExpression = buildPointcutExpression(this.pointcutClassLoader);
        }
    }

    private ClassLoader determinePointcutClassLoader() {
        if (this.beanFactory instanceof ConfigurableBeanFactory) {
            return ((ConfigurableBeanFactory) this.beanFactory).getBeanClassLoader();
        }
        if (this.pointcutDeclarationScope != null) {
            return this.pointcutDeclarationScope.getClassLoader();
        }
        return ClassUtils.getDefaultClassLoader();
    }

    private PointcutExpression buildPointcutExpression(ClassLoader classLoader) {
        PointcutParser parser = initializePointcutParser(classLoader);
        PointcutParameter[] pointcutParameters = new PointcutParameter[this.pointcutParameterNames.length];
        for (int i = 0; i < pointcutParameters.length; i++) {
            pointcutParameters[i] = parser.createPointcutParameter(this.pointcutParameterNames[i], this.pointcutParameterTypes[i]);
        }
        return parser.parsePointcutExpression(replaceBooleanOperators(getExpression()), this.pointcutDeclarationScope, pointcutParameters);
    }

    private PointcutParser initializePointcutParser(ClassLoader cl) {
        PointcutParser parser = PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(SUPPORTED_PRIMITIVES, cl);
        parser.registerPointcutDesignatorHandler(new BeanNamePointcutDesignatorHandler());
        return parser;
    }

    private String replaceBooleanOperators(String pcExpr) {
        String result = StringUtils.replace(pcExpr, " and ", " && ");
        return StringUtils.replace(StringUtils.replace(result, " or ", " || "), " not ", " ! ");
    }

    public PointcutExpression getPointcutExpression() {
        checkReadyToMatch();
        return this.pointcutExpression;
    }

    @Override // org.springframework.aop.ClassFilter
    public boolean matches(Class<?> targetClass) {
        checkReadyToMatch();
        try {
            try {
                return this.pointcutExpression.couldMatchJoinPointsInType(targetClass);
            } catch (ReflectionWorld.ReflectionWorldException ex) {
                logger.debug("PointcutExpression matching rejected target class - trying fallback expression", ex);
                PointcutExpression fallbackExpression = getFallbackPointcutExpression(targetClass);
                if (fallbackExpression != null) {
                    return fallbackExpression.couldMatchJoinPointsInType(targetClass);
                }
                return false;
            }
        } catch (Throwable ex2) {
            logger.debug("PointcutExpression matching rejected target class", ex2);
            return false;
        }
    }

    @Override // org.springframework.aop.IntroductionAwareMethodMatcher
    public boolean matches(Method method, Class<?> targetClass, boolean beanHasIntroductions) {
        checkReadyToMatch();
        Method targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
        ShadowMatch shadowMatch = getShadowMatch(targetMethod, method);
        if (shadowMatch.alwaysMatches()) {
            return true;
        }
        if (shadowMatch.neverMatches()) {
            return false;
        }
        if (beanHasIntroductions) {
            return true;
        }
        RuntimeTestWalker walker = getRuntimeTestWalker(shadowMatch);
        return !walker.testsSubtypeSensitiveVars() || walker.testTargetInstanceOfResidue(targetClass);
    }

    @Override // org.springframework.aop.MethodMatcher
    public boolean matches(Method method, Class<?> targetClass) {
        return matches(method, targetClass, false);
    }

    @Override // org.springframework.aop.MethodMatcher
    public boolean isRuntime() {
        checkReadyToMatch();
        return this.pointcutExpression.mayNeedDynamicTest();
    }

    @Override // org.springframework.aop.MethodMatcher
    public boolean matches(Method method, Class<?> targetClass, Object... args) {
        MethodInvocation mi;
        checkReadyToMatch();
        ShadowMatch shadowMatch = getShadowMatch(AopUtils.getMostSpecificMethod(method, targetClass), method);
        ShadowMatch originalShadowMatch = getShadowMatch(method, method);
        ProxyMethodInvocation pmi = null;
        Object targetObject = null;
        Object thisObject = null;
        try {
            mi = ExposeInvocationInterceptor.currentInvocation();
            targetObject = mi.getThis();
        } catch (IllegalStateException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("Could not access current invocation - matching with limited context: " + ex);
            }
        }
        if (!(mi instanceof ProxyMethodInvocation)) {
            throw new IllegalStateException("MethodInvocation is not a Spring ProxyMethodInvocation: " + mi);
        }
        pmi = (ProxyMethodInvocation) mi;
        thisObject = pmi.getProxy();
        try {
            JoinPointMatch joinPointMatch = shadowMatch.matchesJoinPoint(thisObject, targetObject, args);
            if (pmi != null) {
                RuntimeTestWalker originalMethodResidueTest = getRuntimeTestWalker(originalShadowMatch);
                if (!originalMethodResidueTest.testThisInstanceOfResidue(thisObject.getClass())) {
                    return false;
                }
                if (joinPointMatch.matches()) {
                    bindParameters(pmi, joinPointMatch);
                }
            }
            return joinPointMatch.matches();
        } catch (Throwable ex2) {
            if (logger.isDebugEnabled()) {
                logger.debug("Failed to evaluate join point for arguments " + Arrays.asList(args) + " - falling back to non-match", ex2);
                return false;
            }
            return false;
        }
    }

    protected String getCurrentProxiedBeanName() {
        return ProxyCreationContext.getCurrentProxiedBeanName();
    }

    private PointcutExpression getFallbackPointcutExpression(Class<?> targetClass) {
        try {
            ClassLoader classLoader = targetClass.getClassLoader();
            if (classLoader != null && classLoader != this.pointcutClassLoader) {
                return buildPointcutExpression(classLoader);
            }
            return null;
        } catch (Throwable ex) {
            logger.debug("Failed to create fallback PointcutExpression", ex);
            return null;
        }
    }

    private RuntimeTestWalker getRuntimeTestWalker(ShadowMatch shadowMatch) {
        if (shadowMatch instanceof DefensiveShadowMatch) {
            return new RuntimeTestWalker(((DefensiveShadowMatch) shadowMatch).primary);
        }
        return new RuntimeTestWalker(shadowMatch);
    }

    private void bindParameters(ProxyMethodInvocation invocation, JoinPointMatch jpm) {
        invocation.setUserAttribute(getExpression(), jpm);
    }

    private ShadowMatch getShadowMatch(Method targetMethod, Method originalMethod) {
        ShadowMatch shadowMatch = this.shadowMatchCache.get(targetMethod);
        if (shadowMatch == null) {
            synchronized (this.shadowMatchCache) {
                PointcutExpression fallbackExpression = null;
                Method methodToMatch = targetMethod;
                shadowMatch = this.shadowMatchCache.get(targetMethod);
                if (shadowMatch == null) {
                    try {
                        try {
                            shadowMatch = this.pointcutExpression.matchesMethodExecution(methodToMatch);
                        } catch (Throwable ex) {
                            logger.debug("PointcutExpression matching rejected target method", ex);
                            fallbackExpression = null;
                        }
                    } catch (ReflectionWorld.ReflectionWorldException e) {
                        try {
                            fallbackExpression = getFallbackPointcutExpression(methodToMatch.getDeclaringClass());
                            if (fallbackExpression != null) {
                                shadowMatch = fallbackExpression.matchesMethodExecution(methodToMatch);
                            }
                        } catch (ReflectionWorld.ReflectionWorldException e2) {
                            fallbackExpression = null;
                        }
                    }
                    if (shadowMatch == null && targetMethod != originalMethod) {
                        methodToMatch = originalMethod;
                        try {
                            shadowMatch = this.pointcutExpression.matchesMethodExecution(methodToMatch);
                        } catch (ReflectionWorld.ReflectionWorldException e3) {
                            try {
                                fallbackExpression = getFallbackPointcutExpression(methodToMatch.getDeclaringClass());
                                if (fallbackExpression != null) {
                                    shadowMatch = fallbackExpression.matchesMethodExecution(methodToMatch);
                                }
                            } catch (ReflectionWorld.ReflectionWorldException e4) {
                                fallbackExpression = null;
                            }
                        }
                    }
                    if (shadowMatch == null) {
                        shadowMatch = new ShadowMatchImpl(FuzzyBoolean.NO, null, null, null);
                    } else if (shadowMatch.maybeMatches() && fallbackExpression != null) {
                        shadowMatch = new DefensiveShadowMatch(shadowMatch, fallbackExpression.matchesMethodExecution(methodToMatch));
                    }
                    this.shadowMatchCache.put(targetMethod, shadowMatch);
                }
            }
        }
        return shadowMatch;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AspectJExpressionPointcut)) {
            return false;
        }
        AspectJExpressionPointcut otherPc = (AspectJExpressionPointcut) other;
        return ObjectUtils.nullSafeEquals(getExpression(), otherPc.getExpression()) && ObjectUtils.nullSafeEquals(this.pointcutDeclarationScope, otherPc.pointcutDeclarationScope) && ObjectUtils.nullSafeEquals(this.pointcutParameterNames, otherPc.pointcutParameterNames) && ObjectUtils.nullSafeEquals(this.pointcutParameterTypes, otherPc.pointcutParameterTypes);
    }

    public int hashCode() {
        int hashCode = ObjectUtils.nullSafeHashCode(getExpression());
        return (31 * ((31 * ((31 * hashCode) + ObjectUtils.nullSafeHashCode(this.pointcutDeclarationScope))) + ObjectUtils.nullSafeHashCode((Object[]) this.pointcutParameterNames))) + ObjectUtils.nullSafeHashCode((Object[]) this.pointcutParameterTypes);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AspectJExpressionPointcut: ");
        if (this.pointcutParameterNames != null && this.pointcutParameterTypes != null) {
            sb.append("(");
            for (int i = 0; i < this.pointcutParameterTypes.length; i++) {
                sb.append(this.pointcutParameterTypes[i].getName());
                sb.append(SymbolConstants.SPACE_SYMBOL);
                sb.append(this.pointcutParameterNames[i]);
                if (i + 1 < this.pointcutParameterTypes.length) {
                    sb.append(", ");
                }
            }
            sb.append(")");
        }
        sb.append(SymbolConstants.SPACE_SYMBOL);
        if (getExpression() != null) {
            sb.append(getExpression());
        } else {
            sb.append("<pointcut expression not set>");
        }
        return sb.toString();
    }

    /* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/aspectj/AspectJExpressionPointcut$BeanNamePointcutDesignatorHandler.class */
    private class BeanNamePointcutDesignatorHandler implements PointcutDesignatorHandler {
        private static final String BEAN_DESIGNATOR_NAME = "bean";

        private BeanNamePointcutDesignatorHandler() {
        }

        @Override // org.aspectj.weaver.tools.PointcutDesignatorHandler
        public String getDesignatorName() {
            return "bean";
        }

        @Override // org.aspectj.weaver.tools.PointcutDesignatorHandler
        public ContextBasedMatcher parse(String expression) {
            return AspectJExpressionPointcut.this.new BeanNameContextMatcher(expression);
        }
    }

    /* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/aspectj/AspectJExpressionPointcut$BeanNameContextMatcher.class */
    private class BeanNameContextMatcher implements ContextBasedMatcher {
        private final NamePattern expressionPattern;

        public BeanNameContextMatcher(String expression) {
            this.expressionPattern = new NamePattern(expression);
        }

        @Override // org.aspectj.weaver.tools.ContextBasedMatcher
        @Deprecated
        public boolean couldMatchJoinPointsInType(Class someClass) {
            return contextMatch(someClass) == org.aspectj.weaver.tools.FuzzyBoolean.YES;
        }

        @Override // org.aspectj.weaver.tools.ContextBasedMatcher
        @Deprecated
        public boolean couldMatchJoinPointsInType(Class someClass, MatchingContext context) {
            return contextMatch(someClass) == org.aspectj.weaver.tools.FuzzyBoolean.YES;
        }

        @Override // org.aspectj.weaver.tools.ContextBasedMatcher
        public boolean matchesDynamically(MatchingContext context) {
            return true;
        }

        @Override // org.aspectj.weaver.tools.ContextBasedMatcher
        public org.aspectj.weaver.tools.FuzzyBoolean matchesStatically(MatchingContext context) {
            return contextMatch(null);
        }

        @Override // org.aspectj.weaver.tools.ContextBasedMatcher
        public boolean mayNeedDynamicTest() {
            return false;
        }

        private org.aspectj.weaver.tools.FuzzyBoolean contextMatch(Class<?> targetType) {
            String advisedBeanName = AspectJExpressionPointcut.this.getCurrentProxiedBeanName();
            if (advisedBeanName == null) {
                return org.aspectj.weaver.tools.FuzzyBoolean.MAYBE;
            }
            if (BeanFactoryUtils.isGeneratedBeanName(advisedBeanName)) {
                return org.aspectj.weaver.tools.FuzzyBoolean.NO;
            }
            if (targetType != null) {
                boolean isFactory = FactoryBean.class.isAssignableFrom(targetType);
                return org.aspectj.weaver.tools.FuzzyBoolean.fromBoolean(matchesBeanName(isFactory ? "&" + advisedBeanName : advisedBeanName));
            }
            return org.aspectj.weaver.tools.FuzzyBoolean.fromBoolean(matchesBeanName(advisedBeanName) || matchesBeanName(new StringBuilder().append("&").append(advisedBeanName).toString()));
        }

        private boolean matchesBeanName(String advisedBeanName) {
            if (!this.expressionPattern.matches(advisedBeanName)) {
                if (AspectJExpressionPointcut.this.beanFactory != null) {
                    String[] aliases = AspectJExpressionPointcut.this.beanFactory.getAliases(advisedBeanName);
                    for (String alias : aliases) {
                        if (this.expressionPattern.matches(alias)) {
                            return true;
                        }
                    }
                    return false;
                }
                return false;
            }
            return true;
        }
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        this.shadowMatchCache = new ConcurrentHashMap(32);
    }

    /* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/aspectj/AspectJExpressionPointcut$DefensiveShadowMatch.class */
    private static class DefensiveShadowMatch implements ShadowMatch {
        private final ShadowMatch primary;
        private final ShadowMatch other;

        public DefensiveShadowMatch(ShadowMatch primary, ShadowMatch other) {
            this.primary = primary;
            this.other = other;
        }

        @Override // org.aspectj.weaver.tools.ShadowMatch
        public boolean alwaysMatches() {
            return this.primary.alwaysMatches();
        }

        @Override // org.aspectj.weaver.tools.ShadowMatch
        public boolean maybeMatches() {
            return this.primary.maybeMatches();
        }

        @Override // org.aspectj.weaver.tools.ShadowMatch
        public boolean neverMatches() {
            return this.primary.neverMatches();
        }

        @Override // org.aspectj.weaver.tools.ShadowMatch
        public JoinPointMatch matchesJoinPoint(Object thisObject, Object targetObject, Object[] args) {
            try {
                return this.primary.matchesJoinPoint(thisObject, targetObject, args);
            } catch (ReflectionWorld.ReflectionWorldException e) {
                return this.other.matchesJoinPoint(thisObject, targetObject, args);
            }
        }

        @Override // org.aspectj.weaver.tools.ShadowMatch
        public void setMatchingContext(MatchingContext aMatchContext) {
            this.primary.setMatchingContext(aMatchContext);
            this.other.setMatchingContext(aMatchContext);
        }
    }
}
