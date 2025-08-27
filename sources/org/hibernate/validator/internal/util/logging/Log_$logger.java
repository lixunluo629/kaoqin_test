package org.hibernate.validator.internal.util.logging;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.PatternSyntaxException;
import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintDefinitionException;
import javax.validation.ConstraintTarget;
import javax.validation.ElementKind;
import javax.validation.GroupDefinitionException;
import javax.validation.Path;
import javax.validation.UnexpectedTypeException;
import javax.validation.ValidationException;
import javax.xml.stream.XMLStreamException;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.jboss.logging.BasicLogger;
import org.jboss.logging.DelegatingBasicLogger;
import org.jboss.logging.Logger;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/util/logging/Log_$logger.class */
public class Log_$logger extends DelegatingBasicLogger implements Log, BasicLogger, Serializable {
    private static final long serialVersionUID = 1;
    private static final String FQCN = Log_$logger.class.getName();
    private static final String version = "HV000001: Hibernate Validator %s";
    private static final String ignoringXmlConfiguration = "HV000002: Ignoring XML configuration.";
    private static final String usingConstraintFactory = "HV000003: Using %s as constraint factory.";
    private static final String usingMessageInterpolator = "HV000004: Using %s as message interpolator.";
    private static final String usingTraversableResolver = "HV000005: Using %s as traversable resolver.";
    private static final String usingValidationProvider = "HV000006: Using %s as validation provider.";
    private static final String parsingXMLFile = "HV000007: %s found. Parsing XML based configuration.";
    private static final String unableToCloseInputStream = "HV000008: Unable to close input stream.";
    private static final String unableToCloseXMLFileInputStream = "HV000010: Unable to close input stream for %s.";
    private static final String unableToCreateSchema = "HV000011: Unable to create schema for %1$s: %2$s";
    private static final String getUnableToCreateAnnotationForConfiguredConstraintException = "HV000012: Unable to create annotation for configured constraint";
    private static final String getUnableToFindPropertyWithAccessException = "HV000013: The class %1$s does not have a property '%2$s' with access %3$s.";
    private static final String getUnableToFindMethodException = "HV000014: Type %1$s doesn't have a method %2$s.";
    private static final String getInvalidBigDecimalFormatException = "HV000016: %s does not represent a valid BigDecimal format.";
    private static final String getInvalidLengthForIntegerPartException = "HV000017: The length of the integer part cannot be negative.";
    private static final String getInvalidLengthForFractionPartException = "HV000018: The length of the fraction part cannot be negative.";
    private static final String getMinCannotBeNegativeException = "HV000019: The min parameter cannot be negative.";
    private static final String getMaxCannotBeNegativeException = "HV000020: The max parameter cannot be negative.";
    private static final String getLengthCannotBeNegativeException = "HV000021: The length cannot be negative.";
    private static final String getInvalidRegularExpressionException = "HV000022: Invalid regular expression.";
    private static final String getErrorDuringScriptExecutionException = "HV000023: Error during execution of script \"%s\" occurred.";
    private static final String getScriptMustReturnTrueOrFalseException1 = "HV000024: Script \"%s\" returned null, but must return either true or false.";
    private static final String getScriptMustReturnTrueOrFalseException3 = "HV000025: Script \"%1$s\" returned %2$s (of type %3$s), but must return either true or false.";
    private static final String getInconsistentConfigurationException = "HV000026: Assertion error: inconsistent ConfigurationImpl construction.";
    private static final String getUnableToFindProviderException = "HV000027: Unable to find provider: %s.";
    private static final String getExceptionDuringIsValidCallException = "HV000028: Unexpected exception during isValid call.";
    private static final String getConstraintFactoryMustNotReturnNullException = "HV000029: Constraint factory returned null when trying to create instance of %s.";
    private static final String getNoValidatorFoundForTypeException = "HV000030: No validator could be found for constraint '%s' validating type '%s'. Check configuration for '%s'";
    private static final String getMoreThanOneValidatorFoundForTypeException = "HV000031: There are multiple validator classes which could validate the type %1$s. The validator classes are: %2$s.";
    private static final String getUnableToInitializeConstraintValidatorException = "HV000032: Unable to initialize %s.";
    private static final String getAtLeastOneCustomMessageMustBeCreatedException = "HV000033: At least one custom message must be created if the default error message gets disabled.";
    private static final String getInvalidJavaIdentifierException = "HV000034: %s is not a valid Java Identifier.";
    private static final String getUnableToParsePropertyPathException = "HV000035: Unable to parse property path %s.";
    private static final String getTypeNotSupportedForUnwrappingException = "HV000036: Type %s not supported for unwrapping.";
    private static final String getInconsistentFailFastConfigurationException = "HV000037: Inconsistent fail fast configuration. Fail fast enabled via programmatic API, but explicitly disabled via properties.";
    private static final String getInvalidPropertyPathException0 = "HV000038: Invalid property path.";
    private static final String getInvalidPropertyPathException2 = "HV000039: Invalid property path. Either there is no property %2$s in entity %1$s or it is not possible to cascade to the property.";
    private static final String getPropertyPathMustProvideIndexOrMapKeyException = "HV000040: Property path must provide index or map key.";
    private static final String getErrorDuringCallOfTraversableResolverIsReachableException = "HV000041: Call to TraversableResolver.isReachable() threw an exception.";
    private static final String getErrorDuringCallOfTraversableResolverIsCascadableException = "HV000042: Call to TraversableResolver.isCascadable() threw an exception.";
    private static final String getUnableToExpandDefaultGroupListException = "HV000043: Unable to expand default group list %1$s into sequence %2$s.";
    private static final String getAtLeastOneGroupHasToBeSpecifiedException = "HV000044: At least one group has to be specified.";
    private static final String getGroupHasToBeAnInterfaceException = "HV000045: A group has to be an interface. %s is not.";
    private static final String getSequenceDefinitionsNotAllowedException = "HV000046: Sequence definitions are not allowed as composing parts of a sequence.";
    private static final String getCyclicDependencyInGroupsDefinitionException = "HV000047: Cyclic dependency in groups definition";
    private static final String getUnableToExpandGroupSequenceException = "HV000048: Unable to expand group sequence.";
    private static final String getInvalidDefaultGroupSequenceDefinitionException = "HV000052: Default group sequence and default group sequence provider cannot be defined at the same time.";
    private static final String getNoDefaultGroupInGroupSequenceException = "HV000053: 'Default.class' cannot appear in default group sequence list.";
    private static final String getBeanClassMustBePartOfRedefinedDefaultGroupSequenceException = "HV000054: %s must be part of the redefined default group sequence.";
    private static final String getWrongDefaultGroupSequenceProviderTypeException = "HV000055: The default group sequence provider defined for %s has the wrong type";
    private static final String getInvalidExecutableParameterIndexException = "HV000056: Method or constructor %1$s doesn't have a parameter with index %2$d.";
    private static final String getUnableToRetrieveAnnotationParameterValueException = "HV000059: Unable to retrieve annotation parameter value.";
    private static final String getInvalidLengthOfParameterMetaDataListException = "HV000062: Method or constructor %1$s has %2$s parameters, but the passed list of parameter meta data has a size of %3$s.";
    private static final String getUnableToInstantiateException1 = "HV000063: Unable to instantiate %s.";
    private static final String getUnableToInstantiateException2 = "HV000064: Unable to instantiate %1$s: %2$s.";
    private static final String getUnableToLoadClassException = "HV000065: Unable to load class: %s from %s.";
    private static final String getStartIndexCannotBeNegativeException = "HV000068: Start index cannot be negative: %d.";
    private static final String getEndIndexCannotBeNegativeException = "HV000069: End index cannot be negative: %d.";
    private static final String getInvalidRangeException = "HV000070: Invalid Range: %1$d > %2$d.";
    private static final String getInvalidCheckDigitException = "HV000071: A explicitly specified check digit must lie outside the interval: [%1$d, %2$d].";
    private static final String getCharacterIsNotADigitException = "HV000072: '%c' is not a digit.";
    private static final String getConstraintParametersCannotStartWithValidException = "HV000073: Parameters starting with 'valid' are not allowed in a constraint.";
    private static final String getConstraintWithoutMandatoryParameterException = "HV000074: %2$s contains Constraint annotation, but does not contain a %1$s parameter.";
    private static final String getWrongDefaultValueForPayloadParameterException = "HV000075: %s contains Constraint annotation, but the payload parameter default value is not the empty array.";
    private static final String getWrongTypeForPayloadParameterException = "HV000076: %s contains Constraint annotation, but the payload parameter is of wrong type.";
    private static final String getWrongDefaultValueForGroupsParameterException = "HV000077: %s contains Constraint annotation, but the groups parameter default value is not the empty array.";
    private static final String getWrongTypeForGroupsParameterException = "HV000078: %s contains Constraint annotation, but the groups parameter is of wrong type.";
    private static final String getWrongTypeForMessageParameterException = "HV000079: %s contains Constraint annotation, but the message parameter is not of type java.lang.String.";
    private static final String getOverriddenConstraintAttributeNotFoundException = "HV000080: Overridden constraint does not define an attribute with name %s.";
    private static final String getWrongAttributeTypeForOverriddenConstraintException = "HV000081: The overriding type of a composite constraint must be identical to the overridden one. Expected %1$s found %2$s.";
    private static final String getWrongParameterTypeException = "HV000082: Wrong parameter type. Expected: %1$s Actual: %2$s.";
    private static final String getUnableToFindAnnotationParameterException = "HV000083: The specified annotation defines no parameter '%s'.";
    private static final String getUnableToGetAnnotationParameterException = "HV000084: Unable to get '%1$s' from %2$s.";
    private static final String getNoValueProvidedForAnnotationParameterException = "HV000085: No value provided for parameter '%1$s' of annotation @%2$s.";
    private static final String getTryingToInstantiateAnnotationWithUnknownParametersException = "HV000086: Trying to instantiate %1$s with unknown parameter(s): %2$s.";
    private static final String getPropertyNameCannotBeNullOrEmptyException = "HV000087: Property name cannot be null or empty.";
    private static final String getElementTypeHasToBeFieldOrMethodException = "HV000088: Element type has to be FIELD or METHOD.";
    private static final String getMemberIsNeitherAFieldNorAMethodException = "HV000089: Member %s is neither a field nor a method.";
    private static final String getUnableToAccessMemberException = "HV000090: Unable to access %s.";
    private static final String getHasToBeAPrimitiveTypeException = "HV000091: %s has to be a primitive type.";
    private static final String getNullIsAnInvalidTypeForAConstraintValidatorException = "HV000093: null is an invalid type for a constraint validator.";
    private static final String getMissingActualTypeArgumentForTypeParameterException = "HV000094: Missing actual type argument for type parameter: %s.";
    private static final String getUnableToInstantiateConstraintFactoryClassException = "HV000095: Unable to instantiate constraint factory class %s.";
    private static final String getUnableToOpenInputStreamForMappingFileException = "HV000096: Unable to open input stream for mapping file %s.";
    private static final String getUnableToInstantiateMessageInterpolatorClassException = "HV000097: Unable to instantiate message interpolator class %s.";
    private static final String getUnableToInstantiateTraversableResolverClassException = "HV000098: Unable to instantiate traversable resolver class %s.";
    private static final String getUnableToInstantiateValidationProviderClassException = "HV000099: Unable to instantiate validation provider class %s.";
    private static final String getUnableToParseValidationXmlFileException = "HV000100: Unable to parse %s.";
    private static final String getIsNotAnAnnotationException = "HV000101: %s is not an annotation.";
    private static final String getIsNotAConstraintValidatorClassException = "HV000102: %s is not a constraint validator class.";
    private static final String getBeanClassHasAlreadyBeConfiguredInXmlException = "HV000103: %s is configured at least twice in xml.";
    private static final String getIsDefinedTwiceInMappingXmlForBeanException = "HV000104: %1$s is defined twice in mapping xml for bean %2$s.";
    private static final String getBeanDoesNotContainTheFieldException = "HV000105: %1$s does not contain the fieldType %2$s.";
    private static final String getBeanDoesNotContainThePropertyException = "HV000106: %1$s does not contain the property %2$s.";
    private static final String getAnnotationDoesNotContainAParameterException = "HV000107: Annotation of type %1$s does not contain a parameter %2$s.";
    private static final String getAttemptToSpecifyAnArrayWhereSingleValueIsExpectedException = "HV000108: Attempt to specify an array where single value is expected.";
    private static final String getUnexpectedParameterValueException = "HV000109: Unexpected parameter value.";
    private static final String getInvalidNumberFormatException = "HV000110: Invalid %s format.";
    private static final String getInvalidCharValueException = "HV000111: Invalid char value: %s.";
    private static final String getInvalidReturnTypeException = "HV000112: Invalid return type: %s. Should be a enumeration type.";
    private static final String getReservedParameterNamesException = "HV000113: %s, %s, %s are reserved parameter names.";
    private static final String getWrongPayloadClassException = "HV000114: Specified payload class %s does not implement javax.validation.Payload";
    private static final String getErrorParsingMappingFileException = "HV000115: Error parsing mapping file.";
    private static final String getIllegalArgumentException = "HV000116: %s";
    private static final String getUnableToNarrowNodeTypeException = "HV000118: Unable to cast %s (with element kind %s) to %s";
    private static final String usingParameterNameProvider = "HV000119: Using %s as parameter name provider.";
    private static final String getUnableToInstantiateParameterNameProviderClassException = "HV000120: Unable to instantiate parameter name provider class %s.";
    private static final String getUnableToDetermineSchemaVersionException = "HV000121: Unable to parse %s.";
    private static final String getUnsupportedSchemaVersionException = "HV000122: Unsupported schema version for %s: %s.";
    private static final String getMultipleGroupConversionsForSameSourceException = "HV000124: Found multiple group conversions for source group %s: %s.";
    private static final String getGroupConversionOnNonCascadingElementException = "HV000125: Found group conversions for non-cascading element: %s.";
    private static final String getGroupConversionForSequenceException = "HV000127: Found group conversion using a group sequence as source: %s.";
    private static final String unknownPropertyInExpressionLanguage = "HV000129: EL expression '%s' references an unknown property";
    private static final String errorInExpressionLanguage = "HV000130: Error in EL expression '%s'";
    private static final String getMethodReturnValueMustNotBeMarkedMoreThanOnceForCascadedValidationException = "HV000131: A method return value must not be marked for cascaded validation more than once in a class hierarchy, but the following two methods are marked as such: %s, %s.";
    private static final String getVoidMethodsMustNotBeConstrainedException = "HV000132: Void methods must not be constrained or marked for cascaded validation, but method %s is.";
    private static final String getBeanDoesNotContainConstructorException = "HV000133: %1$s does not contain a constructor with the parameter types %2$s.";
    private static final String getInvalidParameterTypeException = "HV000134: Unable to load parameter of type '%1$s' in %2$s.";
    private static final String getBeanDoesNotContainMethodException = "HV000135: %1$s does not contain a method with the name '%2$s' and parameter types %3$s.";
    private static final String getUnableToLoadConstraintAnnotationClassException = "HV000136: The specified constraint annotation class %1$s cannot be loaded.";
    private static final String getMethodIsDefinedTwiceInMappingXmlForBeanException = "HV000137: The method '%1$s' is defined twice in the mapping xml for bean %2$s.";
    private static final String getConstructorIsDefinedTwiceInMappingXmlForBeanException = "HV000138: The constructor '%1$s' is defined twice in the mapping xml for bean %2$s.";
    private static final String getMultipleCrossParameterValidatorClassesException = "HV000139: The constraint '%1$s' defines multiple cross parameter validators. Only one is allowed.";
    private static final String getImplicitConstraintTargetInAmbiguousConfigurationException = "HV000141: The constraint %1$s used ConstraintTarget#IMPLICIT where the target cannot be inferred.";
    private static final String getCrossParameterConstraintOnMethodWithoutParametersException = "HV000142: Cross parameter constraint %1$s is illegally placed on a parameterless method or constructor '%2$s'.";
    private static final String getCrossParameterConstraintOnClassException = "HV000143: Cross parameter constraint %1$s is illegally placed on class level.";
    private static final String getCrossParameterConstraintOnFieldException = "HV000144: Cross parameter constraint %1$s is illegally placed on field '%2$s'.";
    private static final String getParameterNodeAddedForNonCrossParameterConstraintException = "HV000146: No parameter nodes may be added since path %s doesn't refer to a cross-parameter constraint.";
    private static final String getConstrainedElementConfiguredMultipleTimesException = "HV000147: %1$s is configured multiple times (note, <getter> and <method> nodes for the same method are not allowed)";
    private static final String evaluatingExpressionLanguageExpressionCausedException = "HV000148: An exception occurred during evaluation of EL expression '%s'";
    private static final String getExceptionOccurredDuringMessageInterpolationException = "HV000149: An exception occurred during message interpolation";
    private static final String getMultipleValidatorsForSameTypeException = "HV000150: The constraint '%s' defines multiple validators for the type '%s'. Only one is allowed.";
    private static final String getParameterConfigurationAlteredInSubTypeException = "HV000151: A method overriding another method must not alter the parameter constraint configuration, but method %2$s changes the configuration of %1$s.";
    private static final String getParameterConstraintsDefinedInMethodsFromParallelTypesException = "HV000152: Two methods defined in parallel types must not declare parameter constraints, if they are overridden by the same method, but methods %s and %s both define parameter constraints.";
    private static final String getParametersOrReturnValueConstraintTargetGivenAtNonExecutableException = "HV000153: The constraint %1$s used ConstraintTarget#%2$s but is not specified on a method or constructor.";
    private static final String getCrossParameterConstraintHasNoValidatorException = "HV000154: Cross parameter constraint %1$s has no cross-parameter validator.";
    private static final String getComposedAndComposingConstraintsHaveDifferentTypesException = "HV000155: Composed and composing constraints must have the same constraint type, but composed constraint %1$s has type %3$s, while composing constraint %2$s has type %4$s.";
    private static final String getGenericAndCrossParameterConstraintDoesNotDefineValidationAppliesToParameterException = "HV000156: Constraints with generic as well as cross-parameter validators must define an attribute validationAppliesTo(), but constraint %s doesn't.";
    private static final String getValidationAppliesToParameterMustHaveReturnTypeConstraintTargetException = "HV000157: Return type of the attribute validationAppliesTo() of the constraint %s must be javax.validation.ConstraintTarget.";
    private static final String getValidationAppliesToParameterMustHaveDefaultValueImplicitException = "HV000158: Default value of the attribute validationAppliesTo() of the constraint %s must be ConstraintTarget#IMPLICIT.";
    private static final String getValidationAppliesToParameterMustNotBeDefinedForNonGenericAndCrossParameterConstraintException = "HV000159: Only constraints with generic as well as cross-parameter validators must define an attribute validationAppliesTo(), but constraint %s does.";
    private static final String getValidatorForCrossParameterConstraintMustEitherValidateObjectOrObjectArrayException = "HV000160: Validator for cross-parameter constraint %s does not validate Object nor Object[].";
    private static final String getMethodsFromParallelTypesMustNotDefineGroupConversionsForCascadedReturnValueException = "HV000161: Two methods defined in parallel types must not define group conversions for a cascaded method return value, if they are overridden by the same method, but methods %s and %s both define parameter constraints.";
    private static final String getMethodOrConstructorNotDefinedByValidatedTypeException = "HV000162: The validated type %1$s does not specify the constructor/method: %2$s";
    private static final String getParameterTypesDoNotMatchException = "HV000163: The actual parameter type '%1$s' is not assignable to the expected one '%2$s' for parameter %3$d of '%4$s'";
    private static final String getHasToBeABoxedTypeException = "HV000164: %s has to be a auto-boxed type.";
    private static final String getMixingImplicitWithOtherExecutableTypesException = "HV000165: Mixing IMPLICIT and other executable types is not allowed.";
    private static final String getValidateOnExecutionOnOverriddenOrInterfaceMethodException = "HV000166: @ValidateOnExecution is not allowed on methods overriding a superclass method or implementing an interface. Check configuration for %1$s";
    private static final String getOverridingConstraintDefinitionsInMultipleMappingFilesException = "HV000167: A given constraint definition can only be overridden in one mapping file. %1$s is overridden in multiple files";
    private static final String getNonTerminatedParameterException = "HV000168: The message descriptor '%1$s' contains an unbalanced meta character '%2$c' parameter.";
    private static final String getNestedParameterException = "HV000169: The message descriptor '%1$s' has nested parameters.";
    private static final String getCreationOfScriptExecutorFailedException = "HV000170: No JSR-223 scripting engine could be bootstrapped for language \"%s\".";
    private static final String getBeanClassHasAlreadyBeConfiguredViaProgrammaticApiException = "HV000171: %s is configured more than once via the programmatic constraint declaration API.";
    private static final String getPropertyHasAlreadyBeConfiguredViaProgrammaticApiException = "HV000172: Property \"%2$s\" of type %1$s is configured more than once via the programmatic constraint declaration API.";
    private static final String getMethodHasAlreadyBeConfiguredViaProgrammaticApiException = "HV000173: Method %2$s of type %1$s is configured more than once via the programmatic constraint declaration API.";
    private static final String getParameterHasAlreadyBeConfiguredViaProgrammaticApiException = "HV000174: Parameter %3$s of method or constructor %2$s of type %1$s is configured more than once via the programmatic constraint declaration API.";
    private static final String getReturnValueHasAlreadyBeConfiguredViaProgrammaticApiException = "HV000175: The return value of method or constructor %2$s of type %1$s is configured more than once via the programmatic constraint declaration API.";
    private static final String getConstructorHasAlreadyBeConfiguredViaProgrammaticApiException = "HV000176: Constructor %2$s of type %1$s is configured more than once via the programmatic constraint declaration API.";
    private static final String getCrossParameterElementHasAlreadyBeConfiguredViaProgrammaticApiException = "HV000177: Cross-parameter constraints for the method or constructor %2$s of type %1$s are declared more than once via the programmatic constraint declaration API.";
    private static final String getMultiplierCannotBeNegativeException = "HV000178: Multiplier cannot be negative: %d.";
    private static final String getWeightCannotBeNegativeException = "HV000179: Weight cannot be negative: %d.";
    private static final String getTreatCheckAsIsNotADigitNorALetterException = "HV000180: '%c' is not a digit nor a letter.";
    private static final String getInvalidParameterCountForExecutableException = "HV000181: Wrong number of parameters. Method or constructor %1$s expects %2$d parameters, but got %3$d.";
    private static final String getNoUnwrapperFoundForTypeException = "HV000182: No validation value unwrapper is registered for type '%1$s'.";
    private static final String getUnableToInitializeELExpressionFactoryException = "HV000183: Unable to initialize 'javax.el.ExpressionFactory'. Check that you have the EL dependencies on the classpath, or use ParameterMessageInterpolator instead";
    private static final String creationOfParameterMessageInterpolation = "HV000184: ParameterMessageInterpolator has been chosen, EL interpolation will not be supported";
    private static final String getElUnsupported = "HV000185: Message contains EL expression: %1s, which is unsupported with chosen Interpolator";
    private static final String getConstraintValidatorExistsForWrapperAndWrappedValueException = "HV000186: The constraint of type '%2$s' defined on '%1$s' has multiple matching constraint validators which is due to an additional value handler of type '%3$s'. It is unclear which value needs validating. Clarify configuration via @UnwrapValidatedValue.";
    private static final String getTypeAnnotationConstraintOnIterableRequiresUseOfValidAnnotationException = "HV000187: When using type annotation constraints on parameterized iterables or map @Valid must be used. Check %s#%s";
    private static final String parameterizedTypeWithMoreThanOneTypeArgumentIsNotSupported = "HV000188: Parameterized type with more than one argument is not supported: %s";
    private static final String getInconsistentValueUnwrappingConfigurationBetweenFieldAndItsGetterException = "HV000189: The configuration of value unwrapping for property '%s' of bean '%s' is inconsistent between the field and its getter.";
    private static final String getUnableToCreateXMLEventReader = "HV000190: Unable to parse %s.";
    private static final String validatedValueUnwrapperCannotBeCreated = "HV000191: Error creating unwrapper: %s";
    private static final String unknownJvmVersion = "HV000192: Couldn't determine Java version from value %1s; Not enabling features requiring Java 8";
    private static final String getConstraintHasAlreadyBeenConfiguredViaProgrammaticApiException = "HV000193: %s is configured more than once via the programmatic constraint definition API.";
    private static final String getEmptyElementOnlySupportedWhenCharSequenceIsExpectedExpection = "HV000194: An empty element is only supported when a CharSequence is expected.";
    private static final String getUnableToReachPropertyToValidateException = "HV000195: Unable to reach the property to validate for the bean %s and the property path %s. A property is null along the way.";
    private static final String getUnableToConvertTypeToClassException = "HV000196: Unable to convert the Type %s to a Class.";

    public Log_$logger(Logger log) {
        super(log);
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final void version(String version2) {
        this.log.logf(FQCN, Logger.Level.INFO, (Throwable) null, version$str(), version2);
    }

    protected String version$str() {
        return version;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final void ignoringXmlConfiguration() {
        this.log.logf(FQCN, Logger.Level.INFO, (Throwable) null, ignoringXmlConfiguration$str(), new Object[0]);
    }

    protected String ignoringXmlConfiguration$str() {
        return ignoringXmlConfiguration;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final void usingConstraintFactory(String constraintFactoryClassName) {
        this.log.logf(FQCN, Logger.Level.INFO, (Throwable) null, usingConstraintFactory$str(), constraintFactoryClassName);
    }

    protected String usingConstraintFactory$str() {
        return usingConstraintFactory;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final void usingMessageInterpolator(String messageInterpolatorClassName) {
        this.log.logf(FQCN, Logger.Level.INFO, (Throwable) null, usingMessageInterpolator$str(), messageInterpolatorClassName);
    }

    protected String usingMessageInterpolator$str() {
        return usingMessageInterpolator;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final void usingTraversableResolver(String traversableResolverClassName) {
        this.log.logf(FQCN, Logger.Level.INFO, (Throwable) null, usingTraversableResolver$str(), traversableResolverClassName);
    }

    protected String usingTraversableResolver$str() {
        return usingTraversableResolver;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final void usingValidationProvider(String validationProviderClassName) {
        this.log.logf(FQCN, Logger.Level.INFO, (Throwable) null, usingValidationProvider$str(), validationProviderClassName);
    }

    protected String usingValidationProvider$str() {
        return usingValidationProvider;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final void parsingXMLFile(String fileName) {
        this.log.logf(FQCN, Logger.Level.INFO, (Throwable) null, parsingXMLFile$str(), fileName);
    }

    protected String parsingXMLFile$str() {
        return parsingXMLFile;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final void unableToCloseInputStream() {
        this.log.logf(FQCN, Logger.Level.WARN, (Throwable) null, unableToCloseInputStream$str(), new Object[0]);
    }

    protected String unableToCloseInputStream$str() {
        return unableToCloseInputStream;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final void unableToCloseXMLFileInputStream(String fileName) {
        this.log.logf(FQCN, Logger.Level.WARN, (Throwable) null, unableToCloseXMLFileInputStream$str(), fileName);
    }

    protected String unableToCloseXMLFileInputStream$str() {
        return unableToCloseXMLFileInputStream;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final void unableToCreateSchema(String fileName, String message) {
        this.log.logf(FQCN, Logger.Level.WARN, (Throwable) null, unableToCreateSchema$str(), fileName, message);
    }

    protected String unableToCreateSchema$str() {
        return unableToCreateSchema;
    }

    protected String getUnableToCreateAnnotationForConfiguredConstraintException$str() {
        return getUnableToCreateAnnotationForConfiguredConstraintException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getUnableToCreateAnnotationForConfiguredConstraintException(RuntimeException e) {
        ValidationException result = new ValidationException(String.format(getUnableToCreateAnnotationForConfiguredConstraintException$str(), new Object[0]), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getUnableToFindPropertyWithAccessException$str() {
        return getUnableToFindPropertyWithAccessException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getUnableToFindPropertyWithAccessException(Class<? extends Object> beanClass, String property, ElementType elementType) {
        ValidationException result = new ValidationException(String.format(getUnableToFindPropertyWithAccessException$str(), beanClass, property, elementType));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getUnableToFindMethodException$str() {
        return getUnableToFindMethodException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getUnableToFindMethodException(Class<? extends Object> beanClass, String method) {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getUnableToFindMethodException$str(), beanClass, method));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getInvalidBigDecimalFormatException$str() {
        return getInvalidBigDecimalFormatException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getInvalidBigDecimalFormatException(String value, NumberFormatException e) {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getInvalidBigDecimalFormatException$str(), value), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getInvalidLengthForIntegerPartException$str() {
        return getInvalidLengthForIntegerPartException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getInvalidLengthForIntegerPartException() {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getInvalidLengthForIntegerPartException$str(), new Object[0]));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getInvalidLengthForFractionPartException$str() {
        return getInvalidLengthForFractionPartException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getInvalidLengthForFractionPartException() {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getInvalidLengthForFractionPartException$str(), new Object[0]));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getMinCannotBeNegativeException$str() {
        return getMinCannotBeNegativeException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getMinCannotBeNegativeException() {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getMinCannotBeNegativeException$str(), new Object[0]));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getMaxCannotBeNegativeException$str() {
        return getMaxCannotBeNegativeException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getMaxCannotBeNegativeException() {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getMaxCannotBeNegativeException$str(), new Object[0]));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getLengthCannotBeNegativeException$str() {
        return getLengthCannotBeNegativeException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getLengthCannotBeNegativeException() {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getLengthCannotBeNegativeException$str(), new Object[0]));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getInvalidRegularExpressionException$str() {
        return getInvalidRegularExpressionException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getInvalidRegularExpressionException(PatternSyntaxException e) {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getInvalidRegularExpressionException$str(), new Object[0]), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getErrorDuringScriptExecutionException$str() {
        return getErrorDuringScriptExecutionException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDeclarationException getErrorDuringScriptExecutionException(String script, Exception e) {
        ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getErrorDuringScriptExecutionException$str(), script), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getScriptMustReturnTrueOrFalseException1$str() {
        return getScriptMustReturnTrueOrFalseException1;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDeclarationException getScriptMustReturnTrueOrFalseException(String script) {
        ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getScriptMustReturnTrueOrFalseException1$str(), script));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getScriptMustReturnTrueOrFalseException3$str() {
        return getScriptMustReturnTrueOrFalseException3;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDeclarationException getScriptMustReturnTrueOrFalseException(String script, Object executionResult, String type) {
        ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getScriptMustReturnTrueOrFalseException3$str(), script, executionResult, type));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getInconsistentConfigurationException$str() {
        return getInconsistentConfigurationException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getInconsistentConfigurationException() {
        ValidationException result = new ValidationException(String.format(getInconsistentConfigurationException$str(), new Object[0]));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getUnableToFindProviderException$str() {
        return getUnableToFindProviderException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getUnableToFindProviderException(Class<? extends Object> providerClass) {
        ValidationException result = new ValidationException(String.format(getUnableToFindProviderException$str(), providerClass));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getExceptionDuringIsValidCallException$str() {
        return getExceptionDuringIsValidCallException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getExceptionDuringIsValidCallException(RuntimeException e) {
        ValidationException result = new ValidationException(String.format(getExceptionDuringIsValidCallException$str(), new Object[0]), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getConstraintFactoryMustNotReturnNullException$str() {
        return getConstraintFactoryMustNotReturnNullException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getConstraintFactoryMustNotReturnNullException(String validatorClassName) {
        ValidationException result = new ValidationException(String.format(getConstraintFactoryMustNotReturnNullException$str(), validatorClassName));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getNoValidatorFoundForTypeException$str() {
        return getNoValidatorFoundForTypeException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final UnexpectedTypeException getNoValidatorFoundForTypeException(String constraintType, String validatedValueType, String path) {
        UnexpectedTypeException result = new UnexpectedTypeException(String.format(getNoValidatorFoundForTypeException$str(), constraintType, validatedValueType, path));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getMoreThanOneValidatorFoundForTypeException$str() {
        return getMoreThanOneValidatorFoundForTypeException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final UnexpectedTypeException getMoreThanOneValidatorFoundForTypeException(Type type, String validatorClasses) {
        UnexpectedTypeException result = new UnexpectedTypeException(String.format(getMoreThanOneValidatorFoundForTypeException$str(), type, validatorClasses));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getUnableToInitializeConstraintValidatorException$str() {
        return getUnableToInitializeConstraintValidatorException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getUnableToInitializeConstraintValidatorException(String validatorClassName, RuntimeException e) {
        ValidationException result = new ValidationException(String.format(getUnableToInitializeConstraintValidatorException$str(), validatorClassName), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getAtLeastOneCustomMessageMustBeCreatedException$str() {
        return getAtLeastOneCustomMessageMustBeCreatedException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getAtLeastOneCustomMessageMustBeCreatedException() {
        ValidationException result = new ValidationException(String.format(getAtLeastOneCustomMessageMustBeCreatedException$str(), new Object[0]));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getInvalidJavaIdentifierException$str() {
        return getInvalidJavaIdentifierException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getInvalidJavaIdentifierException(String identifier) {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getInvalidJavaIdentifierException$str(), identifier));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getUnableToParsePropertyPathException$str() {
        return getUnableToParsePropertyPathException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getUnableToParsePropertyPathException(String propertyPath) {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getUnableToParsePropertyPathException$str(), propertyPath));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getTypeNotSupportedForUnwrappingException$str() {
        return getTypeNotSupportedForUnwrappingException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getTypeNotSupportedForUnwrappingException(Class<? extends Object> type) {
        ValidationException result = new ValidationException(String.format(getTypeNotSupportedForUnwrappingException$str(), type));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getInconsistentFailFastConfigurationException$str() {
        return getInconsistentFailFastConfigurationException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getInconsistentFailFastConfigurationException() {
        ValidationException result = new ValidationException(String.format(getInconsistentFailFastConfigurationException$str(), new Object[0]));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getInvalidPropertyPathException0$str() {
        return getInvalidPropertyPathException0;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getInvalidPropertyPathException() {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getInvalidPropertyPathException0$str(), new Object[0]));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getInvalidPropertyPathException2$str() {
        return getInvalidPropertyPathException2;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getInvalidPropertyPathException(String beanClassName, String propertyName) {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getInvalidPropertyPathException2$str(), beanClassName, propertyName));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getPropertyPathMustProvideIndexOrMapKeyException$str() {
        return getPropertyPathMustProvideIndexOrMapKeyException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getPropertyPathMustProvideIndexOrMapKeyException() {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getPropertyPathMustProvideIndexOrMapKeyException$str(), new Object[0]));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getErrorDuringCallOfTraversableResolverIsReachableException$str() {
        return getErrorDuringCallOfTraversableResolverIsReachableException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getErrorDuringCallOfTraversableResolverIsReachableException(RuntimeException e) {
        ValidationException result = new ValidationException(String.format(getErrorDuringCallOfTraversableResolverIsReachableException$str(), new Object[0]), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getErrorDuringCallOfTraversableResolverIsCascadableException$str() {
        return getErrorDuringCallOfTraversableResolverIsCascadableException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getErrorDuringCallOfTraversableResolverIsCascadableException(RuntimeException e) {
        ValidationException result = new ValidationException(String.format(getErrorDuringCallOfTraversableResolverIsCascadableException$str(), new Object[0]), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getUnableToExpandDefaultGroupListException$str() {
        return getUnableToExpandDefaultGroupListException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final GroupDefinitionException getUnableToExpandDefaultGroupListException(List<? extends Object> defaultGroupList, List<? extends Object> groupList) {
        GroupDefinitionException result = new GroupDefinitionException(String.format(getUnableToExpandDefaultGroupListException$str(), defaultGroupList, groupList));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getAtLeastOneGroupHasToBeSpecifiedException$str() {
        return getAtLeastOneGroupHasToBeSpecifiedException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getAtLeastOneGroupHasToBeSpecifiedException() {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getAtLeastOneGroupHasToBeSpecifiedException$str(), new Object[0]));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getGroupHasToBeAnInterfaceException$str() {
        return getGroupHasToBeAnInterfaceException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getGroupHasToBeAnInterfaceException(String className) {
        ValidationException result = new ValidationException(String.format(getGroupHasToBeAnInterfaceException$str(), className));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getSequenceDefinitionsNotAllowedException$str() {
        return getSequenceDefinitionsNotAllowedException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final GroupDefinitionException getSequenceDefinitionsNotAllowedException() {
        GroupDefinitionException result = new GroupDefinitionException(String.format(getSequenceDefinitionsNotAllowedException$str(), new Object[0]));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getCyclicDependencyInGroupsDefinitionException$str() {
        return getCyclicDependencyInGroupsDefinitionException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final GroupDefinitionException getCyclicDependencyInGroupsDefinitionException() {
        GroupDefinitionException result = new GroupDefinitionException(String.format(getCyclicDependencyInGroupsDefinitionException$str(), new Object[0]));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getUnableToExpandGroupSequenceException$str() {
        return getUnableToExpandGroupSequenceException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final GroupDefinitionException getUnableToExpandGroupSequenceException() {
        GroupDefinitionException result = new GroupDefinitionException(String.format(getUnableToExpandGroupSequenceException$str(), new Object[0]));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getInvalidDefaultGroupSequenceDefinitionException$str() {
        return getInvalidDefaultGroupSequenceDefinitionException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final GroupDefinitionException getInvalidDefaultGroupSequenceDefinitionException() {
        GroupDefinitionException result = new GroupDefinitionException(String.format(getInvalidDefaultGroupSequenceDefinitionException$str(), new Object[0]));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getNoDefaultGroupInGroupSequenceException$str() {
        return getNoDefaultGroupInGroupSequenceException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final GroupDefinitionException getNoDefaultGroupInGroupSequenceException() {
        GroupDefinitionException result = new GroupDefinitionException(String.format(getNoDefaultGroupInGroupSequenceException$str(), new Object[0]));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getBeanClassMustBePartOfRedefinedDefaultGroupSequenceException$str() {
        return getBeanClassMustBePartOfRedefinedDefaultGroupSequenceException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final GroupDefinitionException getBeanClassMustBePartOfRedefinedDefaultGroupSequenceException(String beanClassName) {
        GroupDefinitionException result = new GroupDefinitionException(String.format(getBeanClassMustBePartOfRedefinedDefaultGroupSequenceException$str(), beanClassName));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getWrongDefaultGroupSequenceProviderTypeException$str() {
        return getWrongDefaultGroupSequenceProviderTypeException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final GroupDefinitionException getWrongDefaultGroupSequenceProviderTypeException(String beanClassName) {
        GroupDefinitionException result = new GroupDefinitionException(String.format(getWrongDefaultGroupSequenceProviderTypeException$str(), beanClassName));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getInvalidExecutableParameterIndexException$str() {
        return getInvalidExecutableParameterIndexException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getInvalidExecutableParameterIndexException(String executable, int index) {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getInvalidExecutableParameterIndexException$str(), executable, Integer.valueOf(index)));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getUnableToRetrieveAnnotationParameterValueException$str() {
        return getUnableToRetrieveAnnotationParameterValueException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getUnableToRetrieveAnnotationParameterValueException(Exception e) {
        ValidationException result = new ValidationException(String.format(getUnableToRetrieveAnnotationParameterValueException$str(), new Object[0]), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getInvalidLengthOfParameterMetaDataListException$str() {
        return getInvalidLengthOfParameterMetaDataListException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getInvalidLengthOfParameterMetaDataListException(String executableName, int nbParameters, int listSize) {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getInvalidLengthOfParameterMetaDataListException$str(), executableName, Integer.valueOf(nbParameters), Integer.valueOf(listSize)));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getUnableToInstantiateException1$str() {
        return getUnableToInstantiateException1;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getUnableToInstantiateException(String className, Exception e) {
        ValidationException result = new ValidationException(String.format(getUnableToInstantiateException1$str(), className), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getUnableToInstantiateException(Class<? extends Object> clazz, Exception e) {
        ValidationException result = new ValidationException(String.format(getUnableToInstantiateException1$str(), clazz), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getUnableToInstantiateException2$str() {
        return getUnableToInstantiateException2;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getUnableToInstantiateException(String message, Class<? extends Object> clazz, Exception e) {
        ValidationException result = new ValidationException(String.format(getUnableToInstantiateException2$str(), message, clazz), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getUnableToLoadClassException$str() {
        return getUnableToLoadClassException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getUnableToLoadClassException(String className, ClassLoader loader, Exception e) {
        ValidationException result = new ValidationException(String.format(getUnableToLoadClassException$str(), className, loader), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getStartIndexCannotBeNegativeException$str() {
        return getStartIndexCannotBeNegativeException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getStartIndexCannotBeNegativeException(int startIndex) {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getStartIndexCannotBeNegativeException$str(), Integer.valueOf(startIndex)));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getEndIndexCannotBeNegativeException$str() {
        return getEndIndexCannotBeNegativeException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getEndIndexCannotBeNegativeException(int endIndex) {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getEndIndexCannotBeNegativeException$str(), Integer.valueOf(endIndex)));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getInvalidRangeException$str() {
        return getInvalidRangeException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getInvalidRangeException(int startIndex, int endIndex) {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getInvalidRangeException$str(), Integer.valueOf(startIndex), Integer.valueOf(endIndex)));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getInvalidCheckDigitException$str() {
        return getInvalidCheckDigitException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getInvalidCheckDigitException(int startIndex, int endIndex) {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getInvalidCheckDigitException$str(), Integer.valueOf(startIndex), Integer.valueOf(endIndex)));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getCharacterIsNotADigitException$str() {
        return getCharacterIsNotADigitException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final NumberFormatException getCharacterIsNotADigitException(char c) {
        NumberFormatException result = new NumberFormatException(String.format(getCharacterIsNotADigitException$str(), Character.valueOf(c)));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getConstraintParametersCannotStartWithValidException$str() {
        return getConstraintParametersCannotStartWithValidException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDefinitionException getConstraintParametersCannotStartWithValidException() {
        ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getConstraintParametersCannotStartWithValidException$str(), new Object[0]));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getConstraintWithoutMandatoryParameterException$str() {
        return getConstraintWithoutMandatoryParameterException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDefinitionException getConstraintWithoutMandatoryParameterException(String parameterName, String constraintName) {
        ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getConstraintWithoutMandatoryParameterException$str(), parameterName, constraintName));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getWrongDefaultValueForPayloadParameterException$str() {
        return getWrongDefaultValueForPayloadParameterException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDefinitionException getWrongDefaultValueForPayloadParameterException(String constraintName) {
        ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getWrongDefaultValueForPayloadParameterException$str(), constraintName));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getWrongTypeForPayloadParameterException$str() {
        return getWrongTypeForPayloadParameterException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDefinitionException getWrongTypeForPayloadParameterException(String constraintName, ClassCastException e) {
        ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getWrongTypeForPayloadParameterException$str(), constraintName), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getWrongDefaultValueForGroupsParameterException$str() {
        return getWrongDefaultValueForGroupsParameterException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDefinitionException getWrongDefaultValueForGroupsParameterException(String constraintName) {
        ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getWrongDefaultValueForGroupsParameterException$str(), constraintName));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getWrongTypeForGroupsParameterException$str() {
        return getWrongTypeForGroupsParameterException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDefinitionException getWrongTypeForGroupsParameterException(String constraintName, ClassCastException e) {
        ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getWrongTypeForGroupsParameterException$str(), constraintName), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getWrongTypeForMessageParameterException$str() {
        return getWrongTypeForMessageParameterException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDefinitionException getWrongTypeForMessageParameterException(String constraintName) {
        ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getWrongTypeForMessageParameterException$str(), constraintName));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getOverriddenConstraintAttributeNotFoundException$str() {
        return getOverriddenConstraintAttributeNotFoundException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDefinitionException getOverriddenConstraintAttributeNotFoundException(String attributeName) {
        ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getOverriddenConstraintAttributeNotFoundException$str(), attributeName));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getWrongAttributeTypeForOverriddenConstraintException$str() {
        return getWrongAttributeTypeForOverriddenConstraintException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDefinitionException getWrongAttributeTypeForOverriddenConstraintException(String expectedReturnType, Class<? extends Object> currentReturnType) {
        ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getWrongAttributeTypeForOverriddenConstraintException$str(), expectedReturnType, currentReturnType));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getWrongParameterTypeException$str() {
        return getWrongParameterTypeException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getWrongParameterTypeException(String expectedType, String currentType) {
        ValidationException result = new ValidationException(String.format(getWrongParameterTypeException$str(), expectedType, currentType));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getUnableToFindAnnotationParameterException$str() {
        return getUnableToFindAnnotationParameterException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getUnableToFindAnnotationParameterException(String parameterName, NoSuchMethodException e) {
        ValidationException result = new ValidationException(String.format(getUnableToFindAnnotationParameterException$str(), parameterName), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getUnableToGetAnnotationParameterException$str() {
        return getUnableToGetAnnotationParameterException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getUnableToGetAnnotationParameterException(String parameterName, String annotationName, Exception e) {
        ValidationException result = new ValidationException(String.format(getUnableToGetAnnotationParameterException$str(), parameterName, annotationName), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getNoValueProvidedForAnnotationParameterException$str() {
        return getNoValueProvidedForAnnotationParameterException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getNoValueProvidedForAnnotationParameterException(String parameterName, String annotation) {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getNoValueProvidedForAnnotationParameterException$str(), parameterName, annotation));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getTryingToInstantiateAnnotationWithUnknownParametersException$str() {
        return getTryingToInstantiateAnnotationWithUnknownParametersException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final RuntimeException getTryingToInstantiateAnnotationWithUnknownParametersException(Class<? extends Object> annotationType, Set<String> unknownParameters) {
        RuntimeException result = new RuntimeException(String.format(getTryingToInstantiateAnnotationWithUnknownParametersException$str(), annotationType, unknownParameters));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getPropertyNameCannotBeNullOrEmptyException$str() {
        return getPropertyNameCannotBeNullOrEmptyException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getPropertyNameCannotBeNullOrEmptyException() {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getPropertyNameCannotBeNullOrEmptyException$str(), new Object[0]));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getElementTypeHasToBeFieldOrMethodException$str() {
        return getElementTypeHasToBeFieldOrMethodException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getElementTypeHasToBeFieldOrMethodException() {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getElementTypeHasToBeFieldOrMethodException$str(), new Object[0]));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getMemberIsNeitherAFieldNorAMethodException$str() {
        return getMemberIsNeitherAFieldNorAMethodException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getMemberIsNeitherAFieldNorAMethodException(Member member) {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getMemberIsNeitherAFieldNorAMethodException$str(), member));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getUnableToAccessMemberException$str() {
        return getUnableToAccessMemberException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getUnableToAccessMemberException(String memberName, Exception e) {
        ValidationException result = new ValidationException(String.format(getUnableToAccessMemberException$str(), memberName), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getHasToBeAPrimitiveTypeException$str() {
        return getHasToBeAPrimitiveTypeException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getHasToBeAPrimitiveTypeException(Class<? extends Object> clazz) {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getHasToBeAPrimitiveTypeException$str(), clazz));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getNullIsAnInvalidTypeForAConstraintValidatorException$str() {
        return getNullIsAnInvalidTypeForAConstraintValidatorException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getNullIsAnInvalidTypeForAConstraintValidatorException() {
        ValidationException result = new ValidationException(String.format(getNullIsAnInvalidTypeForAConstraintValidatorException$str(), new Object[0]));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getMissingActualTypeArgumentForTypeParameterException$str() {
        return getMissingActualTypeArgumentForTypeParameterException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getMissingActualTypeArgumentForTypeParameterException(TypeVariable<? extends Object> typeParameter) {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getMissingActualTypeArgumentForTypeParameterException$str(), typeParameter));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getUnableToInstantiateConstraintFactoryClassException$str() {
        return getUnableToInstantiateConstraintFactoryClassException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getUnableToInstantiateConstraintFactoryClassException(String constraintFactoryClassName, ValidationException e) {
        ValidationException result = new ValidationException(String.format(getUnableToInstantiateConstraintFactoryClassException$str(), constraintFactoryClassName), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getUnableToOpenInputStreamForMappingFileException$str() {
        return getUnableToOpenInputStreamForMappingFileException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getUnableToOpenInputStreamForMappingFileException(String mappingFileName) {
        ValidationException result = new ValidationException(String.format(getUnableToOpenInputStreamForMappingFileException$str(), mappingFileName));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getUnableToInstantiateMessageInterpolatorClassException$str() {
        return getUnableToInstantiateMessageInterpolatorClassException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getUnableToInstantiateMessageInterpolatorClassException(String messageInterpolatorClassName, Exception e) {
        ValidationException result = new ValidationException(String.format(getUnableToInstantiateMessageInterpolatorClassException$str(), messageInterpolatorClassName), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getUnableToInstantiateTraversableResolverClassException$str() {
        return getUnableToInstantiateTraversableResolverClassException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getUnableToInstantiateTraversableResolverClassException(String traversableResolverClassName, Exception e) {
        ValidationException result = new ValidationException(String.format(getUnableToInstantiateTraversableResolverClassException$str(), traversableResolverClassName), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getUnableToInstantiateValidationProviderClassException$str() {
        return getUnableToInstantiateValidationProviderClassException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getUnableToInstantiateValidationProviderClassException(String providerClassName, Exception e) {
        ValidationException result = new ValidationException(String.format(getUnableToInstantiateValidationProviderClassException$str(), providerClassName), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getUnableToParseValidationXmlFileException$str() {
        return getUnableToParseValidationXmlFileException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getUnableToParseValidationXmlFileException(String file, Exception e) {
        ValidationException result = new ValidationException(String.format(getUnableToParseValidationXmlFileException$str(), file), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getIsNotAnAnnotationException$str() {
        return getIsNotAnAnnotationException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getIsNotAnAnnotationException(String annotationClassName) {
        ValidationException result = new ValidationException(String.format(getIsNotAnAnnotationException$str(), annotationClassName));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getIsNotAConstraintValidatorClassException$str() {
        return getIsNotAConstraintValidatorClassException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getIsNotAConstraintValidatorClassException(Class<? extends Object> validatorClass) {
        ValidationException result = new ValidationException(String.format(getIsNotAConstraintValidatorClassException$str(), validatorClass));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getBeanClassHasAlreadyBeConfiguredInXmlException$str() {
        return getBeanClassHasAlreadyBeConfiguredInXmlException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getBeanClassHasAlreadyBeConfiguredInXmlException(String beanClassName) {
        ValidationException result = new ValidationException(String.format(getBeanClassHasAlreadyBeConfiguredInXmlException$str(), beanClassName));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getIsDefinedTwiceInMappingXmlForBeanException$str() {
        return getIsDefinedTwiceInMappingXmlForBeanException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getIsDefinedTwiceInMappingXmlForBeanException(String name, String beanClassName) {
        ValidationException result = new ValidationException(String.format(getIsDefinedTwiceInMappingXmlForBeanException$str(), name, beanClassName));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getBeanDoesNotContainTheFieldException$str() {
        return getBeanDoesNotContainTheFieldException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getBeanDoesNotContainTheFieldException(String beanClassName, String fieldName) {
        ValidationException result = new ValidationException(String.format(getBeanDoesNotContainTheFieldException$str(), beanClassName, fieldName));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getBeanDoesNotContainThePropertyException$str() {
        return getBeanDoesNotContainThePropertyException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getBeanDoesNotContainThePropertyException(String beanClassName, String getterName) {
        ValidationException result = new ValidationException(String.format(getBeanDoesNotContainThePropertyException$str(), beanClassName, getterName));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getAnnotationDoesNotContainAParameterException$str() {
        return getAnnotationDoesNotContainAParameterException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getAnnotationDoesNotContainAParameterException(String annotationClassName, String parameterName) {
        ValidationException result = new ValidationException(String.format(getAnnotationDoesNotContainAParameterException$str(), annotationClassName, parameterName));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getAttemptToSpecifyAnArrayWhereSingleValueIsExpectedException$str() {
        return getAttemptToSpecifyAnArrayWhereSingleValueIsExpectedException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getAttemptToSpecifyAnArrayWhereSingleValueIsExpectedException() {
        ValidationException result = new ValidationException(String.format(getAttemptToSpecifyAnArrayWhereSingleValueIsExpectedException$str(), new Object[0]));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getUnexpectedParameterValueException$str() {
        return getUnexpectedParameterValueException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getUnexpectedParameterValueException() {
        ValidationException result = new ValidationException(String.format(getUnexpectedParameterValueException$str(), new Object[0]));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getUnexpectedParameterValueException(ClassCastException e) {
        ValidationException result = new ValidationException(String.format(getUnexpectedParameterValueException$str(), new Object[0]), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getInvalidNumberFormatException$str() {
        return getInvalidNumberFormatException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getInvalidNumberFormatException(String formatName, NumberFormatException e) {
        ValidationException result = new ValidationException(String.format(getInvalidNumberFormatException$str(), formatName), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getInvalidCharValueException$str() {
        return getInvalidCharValueException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getInvalidCharValueException(String value) {
        ValidationException result = new ValidationException(String.format(getInvalidCharValueException$str(), value));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getInvalidReturnTypeException$str() {
        return getInvalidReturnTypeException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getInvalidReturnTypeException(Class<? extends Object> returnType, ClassCastException e) {
        ValidationException result = new ValidationException(String.format(getInvalidReturnTypeException$str(), returnType), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getReservedParameterNamesException$str() {
        return getReservedParameterNamesException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getReservedParameterNamesException(String messageParameterName, String groupsParameterName, String payloadParameterName) {
        ValidationException result = new ValidationException(String.format(getReservedParameterNamesException$str(), messageParameterName, groupsParameterName, payloadParameterName));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getWrongPayloadClassException$str() {
        return getWrongPayloadClassException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getWrongPayloadClassException(String payloadClassName) {
        ValidationException result = new ValidationException(String.format(getWrongPayloadClassException$str(), payloadClassName));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getErrorParsingMappingFileException$str() {
        return getErrorParsingMappingFileException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getErrorParsingMappingFileException(Exception e) {
        ValidationException result = new ValidationException(String.format(getErrorParsingMappingFileException$str(), new Object[0]), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getIllegalArgumentException$str() {
        return getIllegalArgumentException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getIllegalArgumentException(String message) {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getIllegalArgumentException$str(), message));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getUnableToNarrowNodeTypeException$str() {
        return getUnableToNarrowNodeTypeException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ClassCastException getUnableToNarrowNodeTypeException(String actualDescriptorType, ElementKind kind, String expectedDescriptorType) {
        ClassCastException result = new ClassCastException(String.format(getUnableToNarrowNodeTypeException$str(), actualDescriptorType, kind, expectedDescriptorType));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final void usingParameterNameProvider(String parameterNameProviderClassName) {
        this.log.logf(FQCN, Logger.Level.INFO, (Throwable) null, usingParameterNameProvider$str(), parameterNameProviderClassName);
    }

    protected String usingParameterNameProvider$str() {
        return usingParameterNameProvider;
    }

    protected String getUnableToInstantiateParameterNameProviderClassException$str() {
        return getUnableToInstantiateParameterNameProviderClassException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getUnableToInstantiateParameterNameProviderClassException(String parameterNameProviderClassName, ValidationException e) {
        ValidationException result = new ValidationException(String.format(getUnableToInstantiateParameterNameProviderClassException$str(), parameterNameProviderClassName), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getUnableToDetermineSchemaVersionException$str() {
        return getUnableToDetermineSchemaVersionException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getUnableToDetermineSchemaVersionException(String file, XMLStreamException e) {
        ValidationException result = new ValidationException(String.format(getUnableToDetermineSchemaVersionException$str(), file), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getUnsupportedSchemaVersionException$str() {
        return getUnsupportedSchemaVersionException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getUnsupportedSchemaVersionException(String file, String version2) {
        ValidationException result = new ValidationException(String.format(getUnsupportedSchemaVersionException$str(), file, version2));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getMultipleGroupConversionsForSameSourceException$str() {
        return getMultipleGroupConversionsForSameSourceException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDeclarationException getMultipleGroupConversionsForSameSourceException(Class<? extends Object> from, Set<Class<? extends Object>> tos) {
        ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getMultipleGroupConversionsForSameSourceException$str(), from, tos));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getGroupConversionOnNonCascadingElementException$str() {
        return getGroupConversionOnNonCascadingElementException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDeclarationException getGroupConversionOnNonCascadingElementException(String location) {
        ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getGroupConversionOnNonCascadingElementException$str(), location));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getGroupConversionForSequenceException$str() {
        return getGroupConversionForSequenceException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDeclarationException getGroupConversionForSequenceException(Class<? extends Object> from) {
        ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getGroupConversionForSequenceException$str(), from));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final void unknownPropertyInExpressionLanguage(String expression, Exception e) {
        this.log.logf(FQCN, Logger.Level.WARN, e, unknownPropertyInExpressionLanguage$str(), expression);
    }

    protected String unknownPropertyInExpressionLanguage$str() {
        return unknownPropertyInExpressionLanguage;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final void errorInExpressionLanguage(String expression, Exception e) {
        this.log.logf(FQCN, Logger.Level.WARN, e, errorInExpressionLanguage$str(), expression);
    }

    protected String errorInExpressionLanguage$str() {
        return errorInExpressionLanguage;
    }

    protected String getMethodReturnValueMustNotBeMarkedMoreThanOnceForCascadedValidationException$str() {
        return getMethodReturnValueMustNotBeMarkedMoreThanOnceForCascadedValidationException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDeclarationException getMethodReturnValueMustNotBeMarkedMoreThanOnceForCascadedValidationException(Member member1, Member member2) {
        ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getMethodReturnValueMustNotBeMarkedMoreThanOnceForCascadedValidationException$str(), member1, member2));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getVoidMethodsMustNotBeConstrainedException$str() {
        return getVoidMethodsMustNotBeConstrainedException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDeclarationException getVoidMethodsMustNotBeConstrainedException(Member member) {
        ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getVoidMethodsMustNotBeConstrainedException$str(), member));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getBeanDoesNotContainConstructorException$str() {
        return getBeanDoesNotContainConstructorException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getBeanDoesNotContainConstructorException(String beanClassName, String parameterTypes) {
        ValidationException result = new ValidationException(String.format(getBeanDoesNotContainConstructorException$str(), beanClassName, parameterTypes));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getInvalidParameterTypeException$str() {
        return getInvalidParameterTypeException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getInvalidParameterTypeException(String type, String beanClassName) {
        ValidationException result = new ValidationException(String.format(getInvalidParameterTypeException$str(), type, beanClassName));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getBeanDoesNotContainMethodException$str() {
        return getBeanDoesNotContainMethodException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getBeanDoesNotContainMethodException(String beanClassName, String methodName, List<Class<? extends Object>> parameterTypes) {
        ValidationException result = new ValidationException(String.format(getBeanDoesNotContainMethodException$str(), beanClassName, methodName, parameterTypes));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getUnableToLoadConstraintAnnotationClassException$str() {
        return getUnableToLoadConstraintAnnotationClassException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getUnableToLoadConstraintAnnotationClassException(String constraintAnnotationClass, Exception e) {
        ValidationException result = new ValidationException(String.format(getUnableToLoadConstraintAnnotationClassException$str(), constraintAnnotationClass), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getMethodIsDefinedTwiceInMappingXmlForBeanException$str() {
        return getMethodIsDefinedTwiceInMappingXmlForBeanException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getMethodIsDefinedTwiceInMappingXmlForBeanException(String name, String beanClassName) {
        ValidationException result = new ValidationException(String.format(getMethodIsDefinedTwiceInMappingXmlForBeanException$str(), name, beanClassName));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getConstructorIsDefinedTwiceInMappingXmlForBeanException$str() {
        return getConstructorIsDefinedTwiceInMappingXmlForBeanException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getConstructorIsDefinedTwiceInMappingXmlForBeanException(String name, String beanClassName) {
        ValidationException result = new ValidationException(String.format(getConstructorIsDefinedTwiceInMappingXmlForBeanException$str(), name, beanClassName));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getMultipleCrossParameterValidatorClassesException$str() {
        return getMultipleCrossParameterValidatorClassesException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDefinitionException getMultipleCrossParameterValidatorClassesException(String constraint) {
        ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getMultipleCrossParameterValidatorClassesException$str(), constraint));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getImplicitConstraintTargetInAmbiguousConfigurationException$str() {
        return getImplicitConstraintTargetInAmbiguousConfigurationException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDeclarationException getImplicitConstraintTargetInAmbiguousConfigurationException(String constraint) {
        ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getImplicitConstraintTargetInAmbiguousConfigurationException$str(), constraint));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getCrossParameterConstraintOnMethodWithoutParametersException$str() {
        return getCrossParameterConstraintOnMethodWithoutParametersException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDeclarationException getCrossParameterConstraintOnMethodWithoutParametersException(String constraint, String member) {
        ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getCrossParameterConstraintOnMethodWithoutParametersException$str(), constraint, member));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getCrossParameterConstraintOnClassException$str() {
        return getCrossParameterConstraintOnClassException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDeclarationException getCrossParameterConstraintOnClassException(String constraint) {
        ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getCrossParameterConstraintOnClassException$str(), constraint));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getCrossParameterConstraintOnFieldException$str() {
        return getCrossParameterConstraintOnFieldException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDeclarationException getCrossParameterConstraintOnFieldException(String constraint, String field) {
        ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getCrossParameterConstraintOnFieldException$str(), constraint, field));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getParameterNodeAddedForNonCrossParameterConstraintException$str() {
        return getParameterNodeAddedForNonCrossParameterConstraintException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalStateException getParameterNodeAddedForNonCrossParameterConstraintException(Path path) {
        IllegalStateException result = new IllegalStateException(String.format(getParameterNodeAddedForNonCrossParameterConstraintException$str(), path));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getConstrainedElementConfiguredMultipleTimesException$str() {
        return getConstrainedElementConfiguredMultipleTimesException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getConstrainedElementConfiguredMultipleTimesException(String location) {
        ValidationException result = new ValidationException(String.format(getConstrainedElementConfiguredMultipleTimesException$str(), location));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final void evaluatingExpressionLanguageExpressionCausedException(String expression, Exception e) {
        this.log.logf(FQCN, Logger.Level.WARN, e, evaluatingExpressionLanguageExpressionCausedException$str(), expression);
    }

    protected String evaluatingExpressionLanguageExpressionCausedException$str() {
        return evaluatingExpressionLanguageExpressionCausedException;
    }

    protected String getExceptionOccurredDuringMessageInterpolationException$str() {
        return getExceptionOccurredDuringMessageInterpolationException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getExceptionOccurredDuringMessageInterpolationException(Exception e) {
        ValidationException result = new ValidationException(String.format(getExceptionOccurredDuringMessageInterpolationException$str(), new Object[0]), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getMultipleValidatorsForSameTypeException$str() {
        return getMultipleValidatorsForSameTypeException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final UnexpectedTypeException getMultipleValidatorsForSameTypeException(String constraint, String type) {
        UnexpectedTypeException result = new UnexpectedTypeException(String.format(getMultipleValidatorsForSameTypeException$str(), constraint, type));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getParameterConfigurationAlteredInSubTypeException$str() {
        return getParameterConfigurationAlteredInSubTypeException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDeclarationException getParameterConfigurationAlteredInSubTypeException(Member superMethod, Member subMethod) {
        ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getParameterConfigurationAlteredInSubTypeException$str(), superMethod, subMethod));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getParameterConstraintsDefinedInMethodsFromParallelTypesException$str() {
        return getParameterConstraintsDefinedInMethodsFromParallelTypesException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDeclarationException getParameterConstraintsDefinedInMethodsFromParallelTypesException(Member method1, Member method2) {
        ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getParameterConstraintsDefinedInMethodsFromParallelTypesException$str(), method1, method2));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getParametersOrReturnValueConstraintTargetGivenAtNonExecutableException$str() {
        return getParametersOrReturnValueConstraintTargetGivenAtNonExecutableException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDeclarationException getParametersOrReturnValueConstraintTargetGivenAtNonExecutableException(String constraint, ConstraintTarget target) {
        ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getParametersOrReturnValueConstraintTargetGivenAtNonExecutableException$str(), constraint, target));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getCrossParameterConstraintHasNoValidatorException$str() {
        return getCrossParameterConstraintHasNoValidatorException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDefinitionException getCrossParameterConstraintHasNoValidatorException(String constraint) {
        ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getCrossParameterConstraintHasNoValidatorException$str(), constraint));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getComposedAndComposingConstraintsHaveDifferentTypesException$str() {
        return getComposedAndComposingConstraintsHaveDifferentTypesException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDefinitionException getComposedAndComposingConstraintsHaveDifferentTypesException(String composedConstraintTypeName, String composingConstraintTypeName, ConstraintDescriptorImpl.ConstraintType composedConstraintType, ConstraintDescriptorImpl.ConstraintType composingConstraintType) {
        ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getComposedAndComposingConstraintsHaveDifferentTypesException$str(), composedConstraintTypeName, composingConstraintTypeName, composedConstraintType, composingConstraintType));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getGenericAndCrossParameterConstraintDoesNotDefineValidationAppliesToParameterException$str() {
        return getGenericAndCrossParameterConstraintDoesNotDefineValidationAppliesToParameterException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDefinitionException getGenericAndCrossParameterConstraintDoesNotDefineValidationAppliesToParameterException(String constraint) {
        ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getGenericAndCrossParameterConstraintDoesNotDefineValidationAppliesToParameterException$str(), constraint));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getValidationAppliesToParameterMustHaveReturnTypeConstraintTargetException$str() {
        return getValidationAppliesToParameterMustHaveReturnTypeConstraintTargetException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDefinitionException getValidationAppliesToParameterMustHaveReturnTypeConstraintTargetException(String constraint) {
        ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getValidationAppliesToParameterMustHaveReturnTypeConstraintTargetException$str(), constraint));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getValidationAppliesToParameterMustHaveDefaultValueImplicitException$str() {
        return getValidationAppliesToParameterMustHaveDefaultValueImplicitException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDefinitionException getValidationAppliesToParameterMustHaveDefaultValueImplicitException(String constraint) {
        ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getValidationAppliesToParameterMustHaveDefaultValueImplicitException$str(), constraint));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getValidationAppliesToParameterMustNotBeDefinedForNonGenericAndCrossParameterConstraintException$str() {
        return getValidationAppliesToParameterMustNotBeDefinedForNonGenericAndCrossParameterConstraintException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDefinitionException getValidationAppliesToParameterMustNotBeDefinedForNonGenericAndCrossParameterConstraintException(String constraint) {
        ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getValidationAppliesToParameterMustNotBeDefinedForNonGenericAndCrossParameterConstraintException$str(), constraint));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getValidatorForCrossParameterConstraintMustEitherValidateObjectOrObjectArrayException$str() {
        return getValidatorForCrossParameterConstraintMustEitherValidateObjectOrObjectArrayException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDefinitionException getValidatorForCrossParameterConstraintMustEitherValidateObjectOrObjectArrayException(String constraint) {
        ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getValidatorForCrossParameterConstraintMustEitherValidateObjectOrObjectArrayException$str(), constraint));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getMethodsFromParallelTypesMustNotDefineGroupConversionsForCascadedReturnValueException$str() {
        return getMethodsFromParallelTypesMustNotDefineGroupConversionsForCascadedReturnValueException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDeclarationException getMethodsFromParallelTypesMustNotDefineGroupConversionsForCascadedReturnValueException(Member method1, Member method2) {
        ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getMethodsFromParallelTypesMustNotDefineGroupConversionsForCascadedReturnValueException$str(), method1, method2));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getMethodOrConstructorNotDefinedByValidatedTypeException$str() {
        return getMethodOrConstructorNotDefinedByValidatedTypeException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getMethodOrConstructorNotDefinedByValidatedTypeException(String validatedTypeName, Member member) {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getMethodOrConstructorNotDefinedByValidatedTypeException$str(), validatedTypeName, member));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getParameterTypesDoNotMatchException$str() {
        return getParameterTypesDoNotMatchException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getParameterTypesDoNotMatchException(String actualType, String expectedType, int index, Member member) {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getParameterTypesDoNotMatchException$str(), actualType, expectedType, Integer.valueOf(index), member));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getHasToBeABoxedTypeException$str() {
        return getHasToBeABoxedTypeException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getHasToBeABoxedTypeException(Class<? extends Object> clazz) {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getHasToBeABoxedTypeException$str(), clazz));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getMixingImplicitWithOtherExecutableTypesException$str() {
        return getMixingImplicitWithOtherExecutableTypesException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getMixingImplicitWithOtherExecutableTypesException() {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getMixingImplicitWithOtherExecutableTypesException$str(), new Object[0]));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getValidateOnExecutionOnOverriddenOrInterfaceMethodException$str() {
        return getValidateOnExecutionOnOverriddenOrInterfaceMethodException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getValidateOnExecutionOnOverriddenOrInterfaceMethodException(Method m) {
        ValidationException result = new ValidationException(String.format(getValidateOnExecutionOnOverriddenOrInterfaceMethodException$str(), m));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getOverridingConstraintDefinitionsInMultipleMappingFilesException$str() {
        return getOverridingConstraintDefinitionsInMultipleMappingFilesException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getOverridingConstraintDefinitionsInMultipleMappingFilesException(String constraintClass) {
        ValidationException result = new ValidationException(String.format(getOverridingConstraintDefinitionsInMultipleMappingFilesException$str(), constraintClass));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getNonTerminatedParameterException$str() {
        return getNonTerminatedParameterException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final MessageDescriptorFormatException getNonTerminatedParameterException(String messageDescriptor, char character) {
        MessageDescriptorFormatException result = new MessageDescriptorFormatException(String.format(getNonTerminatedParameterException$str(), messageDescriptor, Character.valueOf(character)));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getNestedParameterException$str() {
        return getNestedParameterException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final MessageDescriptorFormatException getNestedParameterException(String messageDescriptor) {
        MessageDescriptorFormatException result = new MessageDescriptorFormatException(String.format(getNestedParameterException$str(), messageDescriptor));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getCreationOfScriptExecutorFailedException$str() {
        return getCreationOfScriptExecutorFailedException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDeclarationException getCreationOfScriptExecutorFailedException(String languageName, Exception e) {
        ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getCreationOfScriptExecutorFailedException$str(), languageName), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getBeanClassHasAlreadyBeConfiguredViaProgrammaticApiException$str() {
        return getBeanClassHasAlreadyBeConfiguredViaProgrammaticApiException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getBeanClassHasAlreadyBeConfiguredViaProgrammaticApiException(String beanClassName) {
        ValidationException result = new ValidationException(String.format(getBeanClassHasAlreadyBeConfiguredViaProgrammaticApiException$str(), beanClassName));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getPropertyHasAlreadyBeConfiguredViaProgrammaticApiException$str() {
        return getPropertyHasAlreadyBeConfiguredViaProgrammaticApiException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getPropertyHasAlreadyBeConfiguredViaProgrammaticApiException(String beanClassName, String propertyName) {
        ValidationException result = new ValidationException(String.format(getPropertyHasAlreadyBeConfiguredViaProgrammaticApiException$str(), beanClassName, propertyName));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getMethodHasAlreadyBeConfiguredViaProgrammaticApiException$str() {
        return getMethodHasAlreadyBeConfiguredViaProgrammaticApiException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getMethodHasAlreadyBeConfiguredViaProgrammaticApiException(String beanClassName, String method) {
        ValidationException result = new ValidationException(String.format(getMethodHasAlreadyBeConfiguredViaProgrammaticApiException$str(), beanClassName, method));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getParameterHasAlreadyBeConfiguredViaProgrammaticApiException$str() {
        return getParameterHasAlreadyBeConfiguredViaProgrammaticApiException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getParameterHasAlreadyBeConfiguredViaProgrammaticApiException(String beanClassName, String executable, int parameterIndex) {
        ValidationException result = new ValidationException(String.format(getParameterHasAlreadyBeConfiguredViaProgrammaticApiException$str(), beanClassName, executable, Integer.valueOf(parameterIndex)));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getReturnValueHasAlreadyBeConfiguredViaProgrammaticApiException$str() {
        return getReturnValueHasAlreadyBeConfiguredViaProgrammaticApiException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getReturnValueHasAlreadyBeConfiguredViaProgrammaticApiException(String beanClassName, String executable) {
        ValidationException result = new ValidationException(String.format(getReturnValueHasAlreadyBeConfiguredViaProgrammaticApiException$str(), beanClassName, executable));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getConstructorHasAlreadyBeConfiguredViaProgrammaticApiException$str() {
        return getConstructorHasAlreadyBeConfiguredViaProgrammaticApiException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getConstructorHasAlreadyBeConfiguredViaProgrammaticApiException(String beanClassName, String constructor) {
        ValidationException result = new ValidationException(String.format(getConstructorHasAlreadyBeConfiguredViaProgrammaticApiException$str(), beanClassName, constructor));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getCrossParameterElementHasAlreadyBeConfiguredViaProgrammaticApiException$str() {
        return getCrossParameterElementHasAlreadyBeConfiguredViaProgrammaticApiException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getCrossParameterElementHasAlreadyBeConfiguredViaProgrammaticApiException(String beanClassName, String executable) {
        ValidationException result = new ValidationException(String.format(getCrossParameterElementHasAlreadyBeConfiguredViaProgrammaticApiException$str(), beanClassName, executable));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getMultiplierCannotBeNegativeException$str() {
        return getMultiplierCannotBeNegativeException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getMultiplierCannotBeNegativeException(int multiplier) {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getMultiplierCannotBeNegativeException$str(), Integer.valueOf(multiplier)));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getWeightCannotBeNegativeException$str() {
        return getWeightCannotBeNegativeException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getWeightCannotBeNegativeException(int weight) {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getWeightCannotBeNegativeException$str(), Integer.valueOf(weight)));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getTreatCheckAsIsNotADigitNorALetterException$str() {
        return getTreatCheckAsIsNotADigitNorALetterException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getTreatCheckAsIsNotADigitNorALetterException(int weight) {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getTreatCheckAsIsNotADigitNorALetterException$str(), Integer.valueOf(weight)));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getInvalidParameterCountForExecutableException$str() {
        return getInvalidParameterCountForExecutableException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final IllegalArgumentException getInvalidParameterCountForExecutableException(String executable, int expectedParameterCount, int actualParameterCount) {
        IllegalArgumentException result = new IllegalArgumentException(String.format(getInvalidParameterCountForExecutableException$str(), executable, Integer.valueOf(expectedParameterCount), Integer.valueOf(actualParameterCount)));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getNoUnwrapperFoundForTypeException$str() {
        return getNoUnwrapperFoundForTypeException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getNoUnwrapperFoundForTypeException(String typeName) {
        ValidationException result = new ValidationException(String.format(getNoUnwrapperFoundForTypeException$str(), typeName));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getUnableToInitializeELExpressionFactoryException$str() {
        return getUnableToInitializeELExpressionFactoryException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getUnableToInitializeELExpressionFactoryException(Throwable e) {
        ValidationException result = new ValidationException(String.format(getUnableToInitializeELExpressionFactoryException$str(), new Object[0]), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final void creationOfParameterMessageInterpolation() {
        this.log.logf(FQCN, Logger.Level.WARN, (Throwable) null, creationOfParameterMessageInterpolation$str(), new Object[0]);
    }

    protected String creationOfParameterMessageInterpolation$str() {
        return creationOfParameterMessageInterpolation;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final void getElUnsupported(String expression) {
        this.log.logf(FQCN, Logger.Level.WARN, (Throwable) null, getElUnsupported$str(), expression);
    }

    protected String getElUnsupported$str() {
        return getElUnsupported;
    }

    protected String getConstraintValidatorExistsForWrapperAndWrappedValueException$str() {
        return getConstraintValidatorExistsForWrapperAndWrappedValueException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final UnexpectedTypeException getConstraintValidatorExistsForWrapperAndWrappedValueException(String property, String constraint, String valueHandler) {
        UnexpectedTypeException result = new UnexpectedTypeException(String.format(getConstraintValidatorExistsForWrapperAndWrappedValueException$str(), property, constraint, valueHandler));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getTypeAnnotationConstraintOnIterableRequiresUseOfValidAnnotationException$str() {
        return getTypeAnnotationConstraintOnIterableRequiresUseOfValidAnnotationException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getTypeAnnotationConstraintOnIterableRequiresUseOfValidAnnotationException(String declaringClass, String name) {
        ValidationException result = new ValidationException(String.format(getTypeAnnotationConstraintOnIterableRequiresUseOfValidAnnotationException$str(), declaringClass, name));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final void parameterizedTypeWithMoreThanOneTypeArgumentIsNotSupported(String type) {
        this.log.logf(FQCN, Logger.Level.DEBUG, (Throwable) null, parameterizedTypeWithMoreThanOneTypeArgumentIsNotSupported$str(), type);
    }

    protected String parameterizedTypeWithMoreThanOneTypeArgumentIsNotSupported$str() {
        return parameterizedTypeWithMoreThanOneTypeArgumentIsNotSupported;
    }

    protected String getInconsistentValueUnwrappingConfigurationBetweenFieldAndItsGetterException$str() {
        return getInconsistentValueUnwrappingConfigurationBetweenFieldAndItsGetterException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ConstraintDeclarationException getInconsistentValueUnwrappingConfigurationBetweenFieldAndItsGetterException(String property, String clazz) {
        ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getInconsistentValueUnwrappingConfigurationBetweenFieldAndItsGetterException$str(), property, clazz));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getUnableToCreateXMLEventReader$str() {
        return getUnableToCreateXMLEventReader;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getUnableToCreateXMLEventReader(String file, Exception e) {
        ValidationException result = new ValidationException(String.format(getUnableToCreateXMLEventReader$str(), file), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String validatedValueUnwrapperCannotBeCreated$str() {
        return validatedValueUnwrapperCannotBeCreated;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException validatedValueUnwrapperCannotBeCreated(String className, Exception e) {
        ValidationException result = new ValidationException(String.format(validatedValueUnwrapperCannotBeCreated$str(), className), e);
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final void unknownJvmVersion(String vmVersionStr) {
        this.log.logf(FQCN, Logger.Level.WARN, (Throwable) null, unknownJvmVersion$str(), vmVersionStr);
    }

    protected String unknownJvmVersion$str() {
        return unknownJvmVersion;
    }

    protected String getConstraintHasAlreadyBeenConfiguredViaProgrammaticApiException$str() {
        return getConstraintHasAlreadyBeenConfiguredViaProgrammaticApiException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getConstraintHasAlreadyBeenConfiguredViaProgrammaticApiException(String annotationClassName) {
        ValidationException result = new ValidationException(String.format(getConstraintHasAlreadyBeenConfiguredViaProgrammaticApiException$str(), annotationClassName));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getEmptyElementOnlySupportedWhenCharSequenceIsExpectedExpection$str() {
        return getEmptyElementOnlySupportedWhenCharSequenceIsExpectedExpection;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getEmptyElementOnlySupportedWhenCharSequenceIsExpectedExpection() {
        ValidationException result = new ValidationException(String.format(getEmptyElementOnlySupportedWhenCharSequenceIsExpectedExpection$str(), new Object[0]));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getUnableToReachPropertyToValidateException$str() {
        return getUnableToReachPropertyToValidateException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getUnableToReachPropertyToValidateException(Object bean, Path path) {
        ValidationException result = new ValidationException(String.format(getUnableToReachPropertyToValidateException$str(), bean, path));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }

    protected String getUnableToConvertTypeToClassException$str() {
        return getUnableToConvertTypeToClassException;
    }

    @Override // org.hibernate.validator.internal.util.logging.Log
    public final ValidationException getUnableToConvertTypeToClassException(Type type) {
        ValidationException result = new ValidationException(String.format(getUnableToConvertTypeToClassException$str(), type));
        StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
}
