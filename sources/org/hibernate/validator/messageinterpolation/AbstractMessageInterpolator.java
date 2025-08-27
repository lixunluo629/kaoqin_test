package org.hibernate.validator.messageinterpolation;

import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.MessageInterpolator;
import javax.validation.ValidationException;
import org.hibernate.validator.internal.engine.messageinterpolation.InterpolationTermType;
import org.hibernate.validator.internal.engine.messageinterpolation.LocalizedMessage;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.Token;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.TokenCollector;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.TokenIterator;
import org.hibernate.validator.internal.util.ConcurrentReferenceHashMap;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/messageinterpolation/AbstractMessageInterpolator.class */
public abstract class AbstractMessageInterpolator implements MessageInterpolator {
    private static final int DEFAULT_INITIAL_CAPACITY = 100;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
    private static final String DEFAULT_VALIDATION_MESSAGES = "org.hibernate.validator.ValidationMessages";
    public static final String USER_VALIDATION_MESSAGES = "ValidationMessages";
    public static final String CONTRIBUTOR_VALIDATION_MESSAGES = "ContributorValidationMessages";
    private final Locale defaultLocale;
    private final ResourceBundleLocator userResourceBundleLocator;
    private final ResourceBundleLocator defaultResourceBundleLocator;
    private final ResourceBundleLocator contributorResourceBundleLocator;
    private final ConcurrentReferenceHashMap<LocalizedMessage, String> resolvedMessages;
    private final ConcurrentReferenceHashMap<String, List<Token>> tokenizedParameterMessages;
    private final ConcurrentReferenceHashMap<String, List<Token>> tokenizedELMessages;
    private final boolean cachingEnabled;
    private static final Log log = LoggerFactory.make();
    private static final Pattern LEFT_BRACE = Pattern.compile("\\{", 16);
    private static final Pattern RIGHT_BRACE = Pattern.compile("\\}", 16);
    private static final Pattern SLASH = Pattern.compile("\\\\", 16);
    private static final Pattern DOLLAR = Pattern.compile("\\$", 16);

    public abstract String interpolate(MessageInterpolator.Context context, Locale locale, String str);

    public AbstractMessageInterpolator() {
        this(null);
    }

    public AbstractMessageInterpolator(ResourceBundleLocator userResourceBundleLocator) {
        this(userResourceBundleLocator, null);
    }

    public AbstractMessageInterpolator(ResourceBundleLocator userResourceBundleLocator, ResourceBundleLocator contributorResourceBundleLocator) {
        this(userResourceBundleLocator, contributorResourceBundleLocator, true);
    }

    public AbstractMessageInterpolator(ResourceBundleLocator userResourceBundleLocator, ResourceBundleLocator contributorResourceBundleLocator, boolean cacheMessages) {
        this.defaultLocale = Locale.getDefault();
        if (userResourceBundleLocator == null) {
            this.userResourceBundleLocator = new PlatformResourceBundleLocator(USER_VALIDATION_MESSAGES);
        } else {
            this.userResourceBundleLocator = userResourceBundleLocator;
        }
        if (contributorResourceBundleLocator == null) {
            this.contributorResourceBundleLocator = new PlatformResourceBundleLocator(CONTRIBUTOR_VALIDATION_MESSAGES, null, true);
        } else {
            this.contributorResourceBundleLocator = contributorResourceBundleLocator;
        }
        this.defaultResourceBundleLocator = new PlatformResourceBundleLocator(DEFAULT_VALIDATION_MESSAGES);
        this.cachingEnabled = cacheMessages;
        if (this.cachingEnabled) {
            this.resolvedMessages = new ConcurrentReferenceHashMap<>(100, DEFAULT_LOAD_FACTOR, 16, ConcurrentReferenceHashMap.ReferenceType.SOFT, ConcurrentReferenceHashMap.ReferenceType.SOFT, EnumSet.noneOf(ConcurrentReferenceHashMap.Option.class));
            this.tokenizedParameterMessages = new ConcurrentReferenceHashMap<>(100, DEFAULT_LOAD_FACTOR, 16, ConcurrentReferenceHashMap.ReferenceType.SOFT, ConcurrentReferenceHashMap.ReferenceType.SOFT, EnumSet.noneOf(ConcurrentReferenceHashMap.Option.class));
            this.tokenizedELMessages = new ConcurrentReferenceHashMap<>(100, DEFAULT_LOAD_FACTOR, 16, ConcurrentReferenceHashMap.ReferenceType.SOFT, ConcurrentReferenceHashMap.ReferenceType.SOFT, EnumSet.noneOf(ConcurrentReferenceHashMap.Option.class));
        } else {
            this.resolvedMessages = null;
            this.tokenizedParameterMessages = null;
            this.tokenizedELMessages = null;
        }
    }

    @Override // javax.validation.MessageInterpolator
    public String interpolate(String message, MessageInterpolator.Context context) {
        String interpolatedMessage = message;
        try {
            interpolatedMessage = interpolateMessage(message, context, this.defaultLocale);
        } catch (MessageDescriptorFormatException e) {
            log.warn(e.getMessage());
        }
        return interpolatedMessage;
    }

    @Override // javax.validation.MessageInterpolator
    public String interpolate(String message, MessageInterpolator.Context context, Locale locale) {
        String interpolatedMessage = message;
        try {
            interpolatedMessage = interpolateMessage(message, context, locale);
        } catch (ValidationException e) {
            log.warn(e.getMessage());
        }
        return interpolatedMessage;
    }

    private String interpolateMessage(String message, MessageInterpolator.Context context, Locale locale) throws MessageDescriptorFormatException {
        String cachedResolvedMessage;
        LocalizedMessage localisedMessage = new LocalizedMessage(message, locale);
        String resolvedMessage = null;
        if (this.cachingEnabled) {
            resolvedMessage = this.resolvedMessages.get(localisedMessage);
        }
        if (resolvedMessage == null) {
            ResourceBundle userResourceBundle = this.userResourceBundleLocator.getResourceBundle(locale);
            ResourceBundle constraintContributorResourceBundle = this.contributorResourceBundleLocator.getResourceBundle(locale);
            ResourceBundle defaultResourceBundle = this.defaultResourceBundleLocator.getResourceBundle(locale);
            resolvedMessage = message;
            boolean z = false;
            while (true) {
                boolean evaluatedDefaultBundleOnce = z;
                String userBundleResolvedMessage = interpolateBundleMessage(resolvedMessage, userResourceBundle, locale, true);
                if (!hasReplacementTakenPlace(userBundleResolvedMessage, resolvedMessage)) {
                    userBundleResolvedMessage = interpolateBundleMessage(resolvedMessage, constraintContributorResourceBundle, locale, true);
                }
                if (evaluatedDefaultBundleOnce && !hasReplacementTakenPlace(userBundleResolvedMessage, resolvedMessage)) {
                    break;
                }
                resolvedMessage = interpolateBundleMessage(userBundleResolvedMessage, defaultResourceBundle, locale, false);
                z = true;
            }
        }
        if (this.cachingEnabled && (cachedResolvedMessage = this.resolvedMessages.putIfAbsent(localisedMessage, resolvedMessage)) != null) {
            resolvedMessage = cachedResolvedMessage;
        }
        List<Token> tokens = null;
        if (this.cachingEnabled) {
            tokens = this.tokenizedParameterMessages.get(resolvedMessage);
        }
        if (tokens == null) {
            TokenCollector tokenCollector = new TokenCollector(resolvedMessage, InterpolationTermType.PARAMETER);
            tokens = tokenCollector.getTokenList();
            if (this.cachingEnabled) {
                this.tokenizedParameterMessages.putIfAbsent(resolvedMessage, tokens);
            }
        }
        String resolvedMessage2 = interpolateExpression(new TokenIterator(tokens), context, locale);
        List<Token> tokens2 = null;
        if (this.cachingEnabled) {
            tokens2 = this.tokenizedELMessages.get(resolvedMessage2);
        }
        if (tokens2 == null) {
            TokenCollector tokenCollector2 = new TokenCollector(resolvedMessage2, InterpolationTermType.EL);
            tokens2 = tokenCollector2.getTokenList();
            if (this.cachingEnabled) {
                this.tokenizedELMessages.putIfAbsent(resolvedMessage2, tokens2);
            }
        }
        String resolvedMessage3 = interpolateExpression(new TokenIterator(tokens2), context, locale);
        return replaceEscapedLiterals(resolvedMessage3);
    }

    private String replaceEscapedLiterals(String resolvedMessage) {
        return DOLLAR.matcher(SLASH.matcher(RIGHT_BRACE.matcher(LEFT_BRACE.matcher(resolvedMessage).replaceAll("{")).replaceAll("}")).replaceAll(Matcher.quoteReplacement("\\"))).replaceAll(Matcher.quoteReplacement(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX));
    }

    private boolean hasReplacementTakenPlace(String origMessage, String newMessage) {
        return !origMessage.equals(newMessage);
    }

    private String interpolateBundleMessage(String message, ResourceBundle bundle, Locale locale, boolean recursive) throws MessageDescriptorFormatException {
        TokenCollector tokenCollector = new TokenCollector(message, InterpolationTermType.PARAMETER);
        TokenIterator tokenIterator = new TokenIterator(tokenCollector.getTokenList());
        while (tokenIterator.hasMoreInterpolationTerms()) {
            String term = tokenIterator.nextInterpolationTerm();
            String resolvedParameterValue = resolveParameter(term, bundle, locale, recursive);
            tokenIterator.replaceCurrentInterpolationTerm(resolvedParameterValue);
        }
        return tokenIterator.getInterpolatedMessage();
    }

    private String interpolateExpression(TokenIterator tokenIterator, MessageInterpolator.Context context, Locale locale) throws MessageDescriptorFormatException {
        while (tokenIterator.hasMoreInterpolationTerms()) {
            String term = tokenIterator.nextInterpolationTerm();
            String resolvedExpression = interpolate(context, locale, term);
            tokenIterator.replaceCurrentInterpolationTerm(resolvedExpression);
        }
        return tokenIterator.getInterpolatedMessage();
    }

    private String resolveParameter(String parameterName, ResourceBundle bundle, Locale locale, boolean recursive) throws MessageDescriptorFormatException {
        String parameterValue;
        if (bundle != null) {
            try {
                parameterValue = bundle.getString(removeCurlyBraces(parameterName));
                if (recursive) {
                    parameterValue = interpolateBundleMessage(parameterValue, bundle, locale, recursive);
                }
            } catch (MissingResourceException e) {
                parameterValue = parameterName;
            }
        } else {
            parameterValue = parameterName;
        }
        return parameterValue;
    }

    private String removeCurlyBraces(String parameter) {
        return parameter.substring(1, parameter.length() - 1);
    }
}
