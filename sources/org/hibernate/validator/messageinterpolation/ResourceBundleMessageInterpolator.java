package org.hibernate.validator.messageinterpolation;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Locale;
import javax.el.ExpressionFactory;
import javax.validation.MessageInterpolator;
import org.hibernate.validator.internal.engine.messageinterpolation.InterpolationTerm;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.internal.util.privilegedactions.GetClassLoader;
import org.hibernate.validator.internal.util.privilegedactions.SetContextClassLoader;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/messageinterpolation/ResourceBundleMessageInterpolator.class */
public class ResourceBundleMessageInterpolator extends AbstractMessageInterpolator {
    private static final Log LOG = LoggerFactory.make();
    private final ExpressionFactory expressionFactory;

    public ResourceBundleMessageInterpolator() {
        this.expressionFactory = buildExpressionFactory();
    }

    public ResourceBundleMessageInterpolator(ResourceBundleLocator userResourceBundleLocator) {
        super(userResourceBundleLocator);
        this.expressionFactory = buildExpressionFactory();
    }

    public ResourceBundleMessageInterpolator(ResourceBundleLocator userResourceBundleLocator, ResourceBundleLocator contributorResourceBundleLocator) {
        super(userResourceBundleLocator, contributorResourceBundleLocator);
        this.expressionFactory = buildExpressionFactory();
    }

    public ResourceBundleMessageInterpolator(ResourceBundleLocator userResourceBundleLocator, ResourceBundleLocator contributorResourceBundleLocator, boolean cachingEnabled) {
        super(userResourceBundleLocator, contributorResourceBundleLocator, cachingEnabled);
        this.expressionFactory = buildExpressionFactory();
    }

    public ResourceBundleMessageInterpolator(ResourceBundleLocator userResourceBundleLocator, boolean cachingEnabled) {
        super(userResourceBundleLocator, null, cachingEnabled);
        this.expressionFactory = buildExpressionFactory();
    }

    public ResourceBundleMessageInterpolator(ResourceBundleLocator userResourceBundleLocator, boolean cachingEnabled, ExpressionFactory expressionFactory) {
        super(userResourceBundleLocator, null, cachingEnabled);
        this.expressionFactory = expressionFactory;
    }

    @Override // org.hibernate.validator.messageinterpolation.AbstractMessageInterpolator
    public String interpolate(MessageInterpolator.Context context, Locale locale, String term) {
        InterpolationTerm expression = new InterpolationTerm(term, locale, this.expressionFactory);
        return expression.interpolate(context);
    }

    private static ExpressionFactory buildExpressionFactory() {
        try {
            return ExpressionFactory.newInstance();
        } catch (Throwable th) {
            ClassLoader originalContextClassLoader = (ClassLoader) run(GetClassLoader.fromContext());
            try {
                try {
                    run(SetContextClassLoader.action(ResourceBundleMessageInterpolator.class.getClassLoader()));
                    ExpressionFactory expressionFactoryNewInstance = ExpressionFactory.newInstance();
                    run(SetContextClassLoader.action(originalContextClassLoader));
                    return expressionFactoryNewInstance;
                } catch (Throwable e) {
                    throw LOG.getUnableToInitializeELExpressionFactoryException(e);
                }
            } catch (Throwable th2) {
                run(SetContextClassLoader.action(originalContextClassLoader));
                throw th2;
            }
        }
    }

    private static <T> T run(PrivilegedAction<T> privilegedAction) {
        return System.getSecurityManager() != null ? (T) AccessController.doPrivileged(privilegedAction) : privilegedAction.run();
    }
}
